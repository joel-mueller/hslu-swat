package ch.hslu.entities;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CSVReaderTest {
    @Test
    void testCSVReader() {
        List<Book> bookList = CSVReader.getBooks();
        assertEquals(bookList.size(), CSVReader.NUMBER_OF_ARTICLES);
        assertEquals(bookList.get(100343), new Book(200343, "1563524872", "The Terrible Truth About Liberals", "", "", "", "", "", ""));
    }
}