package ch.hslu.entities;

import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class BookTest {
    @Test
    void testConstructorAndGetters() {
        Book book = new Book(1, "978-3-16-148410-0", "The Great Gatsby", "F. Scott Fitzgerald", "1925", "Scribner",
                "small.jpg", "medium.jpg", "large.jpg");

        assertEquals(1, book.id());
        assertEquals("978-3-16-148410-0", book.isbn());
        assertEquals("The Great Gatsby", book.title());
        assertEquals("F. Scott Fitzgerald", book.author());
        assertEquals("1925", book.year());
        assertEquals("Scribner", book.publisher());
        assertEquals("small.jpg", book.imageUrlS());
        assertEquals("medium.jpg", book.imageUrlM());
        assertEquals("large.jpg", book.imageUrlL());
    }

    @Test
    void testEqualsAndHashCodeSameISBN() {
        Book book1 = new Book(1, "978-3-16-148410-0", "Book One", "Author One", "2000", "Publisher One", "small1.jpg",
                "medium1.jpg", "large1.jpg");
        Book book2 = new Book(2, "978-3-16-148410-0", "Book Two", "Author Two", "2010", "Publisher Two", "small2.jpg",
                "medium2.jpg", "large2.jpg");

        assertEquals(book1, book2);
        assertEquals(book1.hashCode(), book2.hashCode());
    }

    @Test
    void testEqualsAndHashCodeDifferentISBN() {
        Book book1 = new Book(1, "978-3-16-148410-0", "Book One", "Author One", "2000", "Publisher One", "small1.jpg",
                "medium1.jpg", "large1.jpg");
        Book book2 = new Book(2, "978-0-00-000000-0", "Book Two", "Author Two", "2010", "Publisher Two", "small2.jpg",
                "medium2.jpg", "large2.jpg");

        assertNotEquals(book1, book2);
        assertNotEquals(book1.hashCode(), book2.hashCode());
    }

    @Test
    void testEqualsWithDifferentObject() {
        Book book = new Book(1, "978-3-16-148410-0", "Book One", "Author One", "2000", "Publisher One", "small.jpg",
                "medium.jpg", "large.jpg");

        assertNotEquals(book, "Some String");
    }

    @Test
    void testEqualsWithNull() {
        Book book = new Book(1, "978-3-16-148410-0", "Book One", "Author One", "2000", "Publisher One", "small.jpg",
                "medium.jpg", "large.jpg");

        assertNotEquals(book, null);
    }

    @Test
    void testToString() {
        Book book = new Book(1, "978-3-16-148410-0", "Book Title", "Book Author", "2024", "Book Publisher", "small.jpg",
                "medium.jpg", "large.jpg");

        String expected = "Book{id=1, isbn='978-3-16-148410-0', title='Book Title'}";
        assertEquals(expected, book.toString());
    }

    @Test
    void testWithEqualsVerifier() {
        EqualsVerifier.simple().forClass(Book.class)
                .withIgnoredFields("id", "title", "author", "year", "publisher", "imageUrlS", "imageUrlM", "imageUrlL")
                .verify();
    }

}
