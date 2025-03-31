package ch.hslu.entities;

import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class CSVReaderTest {
    @Test
    void testCSVReader() {
        List<Book> bookList = CSVReader.getBooks();
        assertEquals(CSVReader.NUMBER_OF_ARTICLES, bookList.size());
        assertEquals(new Book(200343, "1563524872", "The Terrible Truth About Liberals", "", "", "", "", "", ""),
                bookList.get(100343));
    }

    @Test
    void testGenerateBooksFromList() {
        List<String> bookData = List.of("978-3-16-148410-0", "The Catcher in the Rye", "J.D. Salinger", "1951",
                "Little, Brown and Company", "small.jpg", "medium.jpg", "large.jpg");

        Book book = CSVReader.generateBooksFromList(2, bookData);

        assertEquals(2, book.id());
        assertEquals("978-3-16-148410-0", book.isbn());
        assertEquals("The Catcher in the Rye", book.title());
        assertEquals("J.D. Salinger", book.author());
        assertEquals("1951", book.year());
        assertEquals("Little, Brown and Company", book.publisher());
        assertEquals("small.jpg", book.imageUrlS());
        assertEquals("medium.jpg", book.imageUrlM());
        assertEquals("large.jpg", book.imageUrlL());
    }

    @Test
    void testRemoveQuotesWithBothQuotes() {
        String input = "He said, \"It's a sunny day!\"";
        String expected = "He said, Its a sunny day!";
        assertEquals(expected, CSVReader.removeQuotes(input));
    }

    @Test
    void testRemoveQuotesWithOnlyDoubleQuotes() {
        String input = "This \"string\" has double quotes";
        String expected = "This string has double quotes";
        assertEquals(expected, CSVReader.removeQuotes(input));
    }

    @Test
    void testRemoveQuotesWithOnlySingleQuotes() {
        String input = "It's a test string";
        String expected = "Its a test string";
        assertEquals(expected, CSVReader.removeQuotes(input));
    }

    @Test
    void testRemoveQuotesWithNoQuotes() {
        String input = "No quotes here!";
        String expected = "No quotes here!";
        assertEquals(expected, CSVReader.removeQuotes(input));
    }

    @Test
    void testRemoveQuotesEmptyString() {
        String input = "";
        String expected = "";
        assertEquals(expected, CSVReader.removeQuotes(input));
    }

    @Test
    void testRemoveQuotesNullInput() {
        String input = null;
        assertNull(CSVReader.removeQuotes(input));
    }

    @Test
    void testRemoveQuotesOnlyQuotes() {
        String input = "\"'\"";
        String expected = "";
        assertEquals(expected, CSVReader.removeQuotes(input));
    }

    @Test
    void testRemoveQuotesMixedQuotes() {
        String input = "\"Hello\" 'world'";
        String expected = "Hello world";
        assertEquals(expected, CSVReader.removeQuotes(input));
    }

}
