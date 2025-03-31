package ch.hslu.entities;

import java.util.Objects;

public record Book(int id, String isbn, String title, String author, String year, String publisher, String imageUrlS,
        String imageUrlM, String imageUrlL) {

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof Book book))
            return false;
        return Objects.equals(isbn, book.isbn);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(isbn);
    }

    @Override
    public String toString() {
        return "Book{" + "id=" + id + ", isbn='" + isbn + '\'' + ", title='" + title + '\'' + '}';
    }
}
