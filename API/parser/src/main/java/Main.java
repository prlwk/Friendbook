import java.io.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class Main {
    public static void main(String[] args) throws IOException {
        ParserForBookMicroservice.parseTags(true);
        ParserForBookMicroservice.parseGenres(false);
        ParserForAuthorMicroservice.parseAuthors();
        parseAllBooks();
    }

    static void parseAllBooks() {
        final int THREAD_COUNT = 10;
        final int ALL_PAGES_NUMBER = 2138;
        final int PAGES_PER_THREAD = ALL_PAGES_NUMBER / THREAD_COUNT;
        int startPage = 0;
        int endPage = 0;
        List<Future<?>> futureList = new ArrayList<>(THREAD_COUNT + 1);
        List<String> fileNamesList = new ArrayList<>(THREAD_COUNT + 1);
        try (ExecutorService executorService = Executors.newFixedThreadPool(THREAD_COUNT)) {
            Map<String, Long> authorMap = getAuthorMap();
            for (int i = 0; i < THREAD_COUNT; i++) {
                startPage = i * PAGES_PER_THREAD + 1;
                endPage = (i + 1) * PAGES_PER_THREAD;

                ParserForAllBooks parserForAllBooks = new ParserForAllBooks(startPage, endPage, i + 1, authorMap);
                fileNamesList.add(parserForAllBooks.getOutputFileName());

                futureList.add(executorService.submit(parserForAllBooks));
            }

            if (endPage < ALL_PAGES_NUMBER) {
                startPage = endPage + 1;
                endPage = ALL_PAGES_NUMBER;
                ParserForAllBooks parserForAllBooks = new ParserForAllBooks(startPage, endPage, THREAD_COUNT + 1, authorMap);
                fileNamesList.add(parserForAllBooks.getOutputFileName());
                futureList.add(executorService.submit(parserForAllBooks));
            }
            while (!checkTasksCompleted(futureList)) {

            }
            //After that we can merge all generated script files into one.

            for (String fileName : fileNamesList) {
                try (BufferedInputStream bufferedInputStream = new BufferedInputStream(new FileInputStream(fileName), 1024);
                     BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(new FileOutputStream("insert_result_script.sql", true))) {

                    bufferedOutputStream.write(bufferedInputStream.readAllBytes());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static boolean checkTasksCompleted(List<Future<?>> futureList) {
        for (Future<?> future : futureList) {
            if (!future.isDone()) {
                return false;
            }
        }
        return true;
    }

    static Map<String, Long> getAuthorMap() {
        String sqlSelectAuthor = "SELECT id, name FROM authors";
        String connectionUrl = "jdbc:mysql://localhost:3306/author";
        Map<String, Long> authorMap = new HashMap<>();
        try (Connection conn = DriverManager.getConnection(connectionUrl, "root", "strongpassword");
             PreparedStatement ps = conn.prepareStatement(sqlSelectAuthor);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                authorMap.put(rs.getString("name"), rs.getLong("id"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return new HashMap<>();
        }
        return authorMap;
    }
}
