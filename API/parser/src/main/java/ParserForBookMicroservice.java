import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class ParserForBookMicroservice {
    static public void parseTags(boolean isClearFile) {
        try {
            Document document = Jsoup.connect("https://knigopoisk.org/filter#tags").get();
            Elements elements = document
                    .select("div.page-wrapper")
                    .select("#page")
                    .select("div.container")
                    .select("#content")
                    .select("div.content-wrapper")
                    .select("div.filter-page")
                    .select("div.tags").select("ul").select("li");
            List<String> names = new ArrayList<>();
            for (org.jsoup.nodes.Element element : elements) {
                names.add(element.text());
            }
            createSqlFileForInsertingTags(names, isClearFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static void createSqlFileForInsertingTags(List<String> names, boolean isClearFile) {
        File file = new File(".");
        file = new File(file.getPath().substring(0, file.getPath().length() - 1) + "book-service\\src\\main\\resources\\import.sql");
        try (FileWriter writer = new FileWriter(file, !isClearFile)) {
            if (isClearFile) {
                for (int i = 0; i < names.size(); i++) {
                    writer.write("INSERT INTO tags (id, name) VALUES (" + (i + 1) + ", '" + names.get(i) + "');\n");
                }
            } else {
                for (int i = 0; i < names.size(); i++) {
                    writer.append("INSERT INTO tags (id, name) VALUES (").append(String.valueOf(i + 1)).append(", '").append(names.get(i)).append("');\n");
                }
            }
            writer.flush();
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    static public void parseGenres(boolean isClearFile) {
        try {
            Document document = Jsoup.connect("https://knigopoisk.org/filter#genres").get();
            Elements elements = document
                    .select("div.page-wrapper")
                    .select("#page")
                    .select("div.container")
                    .select("#content")
                    .select("div.content-wrapper")
                    .select("div.filter-page")
                    .select("div.genres").select("ul").select("li");
            List<String> names = new ArrayList<>();
            for (org.jsoup.nodes.Element element : elements) {
                names.add(element.child(0).text());
            }
            createSqlFileForInsertingGenres(names, isClearFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static void createSqlFileForInsertingGenres(List<String> names, boolean isClearFile) {
        File file = new File(".");
        file = new File(file.getPath().substring(0, file.getPath().length() - 1) + "book-service\\src\\main\\resources\\import.sql");
        try (FileWriter writer = new FileWriter(file, !isClearFile)) {
            if (isClearFile) {
                for (int i = 0; i < names.size(); i++) {
                    writer.write("INSERT INTO genres (id, name) VALUES (" + (i + 1) + ", '" + names.get(i) + "');\n");
                }
            } else {
                for (int i = 0; i < names.size(); i++) {
                    writer.append("INSERT INTO genres (id, name) VALUES (").append(String.valueOf(i + 1)).append(", '").append(names.get(i)).append("');\n");
                }
            }
            writer.flush();
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }
}
