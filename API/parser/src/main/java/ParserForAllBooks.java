import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ParserForAllBooks implements Runnable {

    private static class BookData {
        public String name;
        public List<String> genreList;
        public List<String> tagList;
        public String releaseYear;
        public List<String> authorList;
        public String description;
        public String imageLink;
        public String imageName;

        public BookData(String name, List<String> genreList, List<String> tagList, String releaseYear,
                        List<String> authorList, String description, String imageLink, String imageName) {
            this.name = name;
            this.genreList = genreList;
            this.tagList = tagList;
            this.releaseYear = releaseYear;
            this.authorList = authorList;
            this.description = description;
            this.imageLink = imageLink;
            if (imageLink == null || imageLink.isEmpty()) {
                this.imageName = null;
            } else {
                this.imageName = imageName;
            }
        }
    }

    private static List<String> allGenreList;
    private static List<String> allTagList;

    private final int startPageNumber;
    private final int endPageNumber;
    private final String outputFileName;
    private final Map<String, Long> authorMap;

    public ParserForAllBooks(int startPageNumber, int endPageNumber, int outputFileNumber, Map<String, Long> authorMap) {
        this.startPageNumber = startPageNumber;
        this.endPageNumber = endPageNumber;
        outputFileName = "insert_book_data_script_" + outputFileNumber + ".sql";
        this.authorMap = authorMap;
    }

    @Override
    public void run() {
        parseAllBooks();
    }

    public String getOutputFileName() {
        return outputFileName;
    }

    private void parseAllBooks() {
        allGenreList = parseAllGenres();
        allTagList = parseAllTags();
        int pageCounter = startPageNumber - 1;
        while (pageCounter != endPageNumber) {
            try {
                parseAllBooksOnPage(Jsoup.connect("https://knigopoisk.org/books/allbooks?page=" + pageCounter).get(),
                        pageCounter * 20 + 1);
                pageCounter++;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void parseAllBooksOnPage(Document allBooksDocumentPage, int bookNumberCounter) {
        try {
            Elements bookLinkElements = allBooksDocumentPage.getElementsByClass("list-book__link");
            Elements authorNamesElements = allBooksDocumentPage.getElementsByClass("list-book__link-author");
            for (int i = 0; i < bookLinkElements.size(); i++) {
                String bookPageLink = bookLinkElements.get(i).attr("href");
                String bookName = bookLinkElements.get(i).attr("title");
                List<String> authorList = authorNamesElements.get(i).getElementsByClass("list-author")
                        .eachAttr("title");
                Document bookPageDocument = Jsoup.connect(bookPageLink).get();

                BookData bookData = new BookData(
                        bookName,
                        getBookGenreList(bookPageDocument),
                        getBookTagList(bookPageDocument),
                        getBookReleaseYear(bookPageDocument),
                        authorList,
                        getBookDescription(bookPageDocument),
                        getBookImageLink(bookPageDocument),
                        bookNumberCounter + ".jpg"
                );
                appendBookDataToScriptFile(bookData, bookNumberCounter);
                appendBookAuthorDataToScriptFile(bookData, bookNumberCounter);
                appendBookGenreDataToScriptFile(bookData, bookNumberCounter, allGenreList);
                appendBookTagDataToScriptFile(bookData, bookNumberCounter, allTagList);
                saveBookImage(bookData);
                bookNumberCounter++;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void saveBookImage(BookData bookData) {
        if (bookData.imageName == null || bookData.imageLink == null || bookData.imageLink.isEmpty() || bookData.imageName.isEmpty()) {
            return;
        }
        try {
            URL url = new URL(bookData.imageLink);
            InputStream in = new BufferedInputStream(url.openStream());
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            byte[] buf = new byte[1024];
            int n = 0;
            while (-1 != (n = in.read(buf))) {
                out.write(buf, 0, n);
            }
            out.close();
            in.close();
            byte[] response = out.toByteArray();

            FileOutputStream fos = new FileOutputStream("./images/" + bookData.imageName);
            fos.write(response);
            fos.close();
        } catch (Exception ignored) {

        }
    }

    private void appendBookDataToScriptFile(BookData bookData, int bookNumber) throws FileNotFoundException {
        try (FileOutputStream fileOutputStream = new FileOutputStream(outputFileName, true)) {
            String resultStringBuilder = "INSERT INTO book.books(id, count_marks, count_requests, description, link_cover, name, sum_marks,year) values (" +
                    bookNumber + ", 0, 0, " +
                    getQueryArgument(bookData.description) +
                    ", " + getQueryArgument(bookData.imageName) +
                    "," + getQueryArgument(bookData.name) +
                    ", 0," +
                    getQueryArgument(bookData.releaseYear) +
                    ");\n";
            fileOutputStream.write(resultStringBuilder.getBytes());
        } catch (Exception ignored) {

        }
    }

    private void appendBookAuthorDataToScriptFile(BookData bookData, int bookNumber) {
        try (FileOutputStream fileOutputStream = new FileOutputStream(outputFileName, true)) {
            StringBuilder resultStringBuilder = new StringBuilder();
            for (String author : bookData.authorList) {
                resultStringBuilder.append("INSERT INTO book.book_author(book_id, author_id) values (")
                        .append(bookNumber)
                        .append(", ")
                        .append(authorMap.get(author))
                        .append(");\n");
            }

            fileOutputStream.write(resultStringBuilder.toString().getBytes());
        } catch (Exception ignored) {

        }
    }

    private void appendBookGenreDataToScriptFile(BookData bookData, int bookNumber, List<String> genreList) {
        try (FileOutputStream fileOutputStream = new FileOutputStream(outputFileName, true)) {
            StringBuilder resultStringBuilder = new StringBuilder();
            for (String bookGenre : bookData.genreList) {
                resultStringBuilder.append("INSERT INTO book.book_genre(book_id, genre_id) " + "values (")
                        .append(bookNumber)
                        .append(", ")
                        .append(getElementIdFromList(genreList, bookGenre))
                        .append(");\n");
            }
            fileOutputStream.write(resultStringBuilder.toString().getBytes());
        } catch (Exception ignored) {

        }
    }

    private void appendBookTagDataToScriptFile(BookData bookData, int bookNumber, List<String> tagList) {
        try (FileOutputStream fileOutputStream = new FileOutputStream(outputFileName, true)) {
            StringBuilder resultStringBuilder = new StringBuilder();
            for (String bookTag : bookData.tagList) {
                resultStringBuilder.append("INSERT INTO book.book_tag(book_id, tag_id) " + "values (")
                        .append(bookNumber).append(", ")
                        .append(getElementIdFromList(tagList, bookTag))
                        .append(");\n");
            }
            fileOutputStream.write(resultStringBuilder.toString().getBytes());
        } catch (Exception ignored) {

        }
    }

    private int getElementIdFromList(List<String> list, String element) {
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).equals(element)) {
                return i + 1;
            }
        }
        return -1;
    }

    private String getQueryArgument(String arg) {
        return arg == null || arg.isEmpty() ? "null" : "'" + arg.replaceAll("'", "%27") + "'";
    }

    private static List<String> parseAllTags() {
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
            return names;
        } catch (IOException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    private static List<String> parseAllGenres() {
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
            return names;
        } catch (IOException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    private List<String> getBookGenreList(Document bookPageDocument) {
        try {
            Element genreMainElement = bookPageDocument
                    .getElementsByClass("short-info")
                    .select("tr").get(0);
            if (!genreMainElement.select("th").text().equals("Жанры:")) {
                return null;
            }

            Elements genreElements = genreMainElement.select("span");
            List<String> genreList = new ArrayList<>();
            for (Element element : genreElements) {
                genreList.add(element.select("a").text());
            }
            return genreList;
        } catch (Exception e) {
            return null;
        }
    }

    private List<String> getBookTagList(Document bookPageDocument) {
        try {
            Element tagMainElement = bookPageDocument
                    .getElementsByClass("short-info")
                    .select("tr").get(1);
            if (!tagMainElement.select("th").text().equals("Теги:")) {
                return null;
            }

            Elements tagElements = tagMainElement.select("span");
            List<String> tagList = new ArrayList<>();
            for (Element element : tagElements) {
                tagList.add(element.select("a").text());
            }
            return tagList;
        } catch (Exception e) {
            return null;
        }
    }

    private String getBookReleaseYear(Document bookPageDocument) {
        try {
            String firstElementOfSubtitle = bookPageDocument.getElementsByClass("page__subtitle").toString();
            firstElementOfSubtitle = firstElementOfSubtitle.replaceFirst("<div class=\"page__subtitle\">", "");
            firstElementOfSubtitle = firstElementOfSubtitle.replaceAll("[\n\0 ().,a-zA-ZА-Яа-я]", "");
            if (firstElementOfSubtitle.startsWith("<a")) {
                return null;
            }
            return firstElementOfSubtitle.split("<")[0];
        } catch (Exception e) {
            return null;
        }
    }

    private String getBookDescription(Document bookPageDocument) {
        try {
            return bookPageDocument.getElementById("description-block").text();
        } catch (Exception e) {
            return null;
        }
    }

    private String getBookImageLink(Document bookPageDocument) {
        final String emptyPosterLink = "/media/books/po/200x298/poster_200x298.png";

        try {
            String bookPosterLink = bookPageDocument.getElementsByClass("poster__img").attr("src");
            if (bookPosterLink.equals(emptyPosterLink)) {
                return null;
            }
            return "https://knigopoisk.org" + bookPosterLink;
        } catch (Exception e) {
            return null;
        }
    }
}
