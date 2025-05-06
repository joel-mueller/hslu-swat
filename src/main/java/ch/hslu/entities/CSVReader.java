package ch.hslu.entities;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class CSVReader {
    public final static String DELIMITER = ";";
    public final static String FILENAME = "books.csv";
    public final static int MIN_ID = 100000;
    public final static int NUMBER_OF_ARTICLES = 271379;
    public final static int MAX_ID = MIN_ID + NUMBER_OF_ARTICLES - 1;
    private static final Logger LOG = LoggerFactory.getLogger(CSVReader.class);

    public static List<Book> getBooks() {
        List<Book> records = new ArrayList<>();
        int currentId = MIN_ID;
        int count = 0;
        try (BufferedReader br = new BufferedReader(new FileReader(FILENAME))) {
            String line;
            br.readLine();
            while ((line = br.readLine()) != null) {
                String[] values = line.split(DELIMITER);
                records.add(generateBooksFromList(currentId, Arrays.asList(values)));
                currentId++;
                count++;
            }
        } catch (IOException e) {
            LOG.error("Error occurred while reading the file: {}", e.getMessage());
        }
        if (count != NUMBER_OF_ARTICLES)
            throw new RuntimeException("Not all books could be scanned only {} books" + currentId);
        LOG.info("All {} articles have been successfully initialized", NUMBER_OF_ARTICLES);
        return records;
    }

    public static Book generateBooksFromList(int id, List<String> listRaw) {
        List<String> list = new ArrayList<>();
        for (String item : listRaw) {
            list.add(removeQuotes(item));
        }
        return new Book(id, list.get(0), list.get(1), list.get(2), list.get(3), list.get(4), list.get(5), list.get(6),
                list.get(7));
    }

    public static String removeQuotes(String input) {
        if (input == null) {
            return null;
        }
        return input.replace("\"", "").replace("'", "");
    }

    public static void main(String[] args) {
        List<Book> books = CSVReader.getBooks();
        for (Book book : books) {
            System.out.println(book);
        }
    }
}
