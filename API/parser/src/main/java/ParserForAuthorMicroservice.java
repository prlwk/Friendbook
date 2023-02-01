import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class ParserForAuthorMicroservice {
    public static String BASE_URL = "https://knigopoisk.org";

    static public void parseAuthors() {
        long counterId = 1;
        File file = new File(".");
        file = new File(file.getPath().substring(0, file.getPath().length() - 1) + "author-service\\src\\main\\resources\\import.sql");
        try (FileWriter writer = new FileWriter(file, true)) {
            for (int i = 1; i <= 93; i++) {
                Document document = Jsoup.connect("https://knigopoisk.org/authors/allauthors?url=allauthors&page=" + i).get();
                Elements elements = document
                        .select("div.page-wrapper")
                        .select("#page")
                        .select("div.container")
                        .select("#content")
                        .select("div.content-wrapper")
                        .select("div.block-table")
                        .select("div.pdl-30")
                        .select("a.list-book__link");
                for (Element element : elements) {
                    String url = element.attr("href");
                    String name = element.text();
                    name = name.replaceAll("'", "%27");
                    Document document2 = Jsoup.connect(url).get();
                    Elements authorContent = document2
                            .select("div.page-wrapper")
                            .select("#page")
                            .select("div.container")
                            .select("#content");
                    Elements biographyElements = authorContent
                            .select("div.book-content__right")
                            .select("div.biography > *");
                    StringBuilder stringBuilder = new StringBuilder();
                    for (int k = 1; k < biographyElements.size(); k++) {
                        stringBuilder.append(biographyElements.get(k).text());
                        stringBuilder.append("\\n");
                    }
                    String biography = stringBuilder.toString();
                    biography = biography.replaceAll("'", "%27");
                    String yearsLife = authorContent.select("div.page__subtitle").text();
                    String[] words = yearsLife.split(" ");
                    if (words.length == 7) {
                        yearsLife = words[2] + " - " + words[6];
                    } else if (words.length == 3) {
                        yearsLife = words[2];
                    }
                    String imageLocation = authorContent.select("div.book-content__left").select("img").attr("src");
                    if (!imageLocation.contains("poster")) {
                        Connection.Response resultImageResponse = Jsoup.connect(BASE_URL + imageLocation).ignoreContentType(true).execute();
                        File image = new File(".");
                        image = new File(image.getPath().substring(0, image.getPath().length() - 1)
                                + "author-service\\src\\main\\resources\\images\\" + counterId + ".jpg");
                        FileOutputStream out = (new FileOutputStream(image));
                        out.write(resultImageResponse.bodyAsBytes());
                        out.close();
                        writer.write("INSERT INTO authors(id, biography, name,"
                                + " photo_src, years_life, count_requests, rating) VALUES (" + counterId + ", '" + biography
                                + "', '" + name + "', '" + counterId + ".jpg', '" + yearsLife + "', " + 0 + ", " + 0 +");\n ");
                    } else {
                        writer.write("INSERT INTO authors(id, biography, name,"
                                + " years_life, count_requests, rating) VALUES (" + counterId + ", '" + biography
                                + "', '" + name + "', '" + yearsLife + "', " + 0 + ", " + 0 +");\n ");
                    }
                    counterId++;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
