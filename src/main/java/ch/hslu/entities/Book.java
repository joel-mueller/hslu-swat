package ch.hslu.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
public final class Book {
    @Id
    private final int id;
    private final String isbn;
    private final String title;
    private final String author;
    private final String year;
    private final String publisher;
    private final String imageUrlS;
    private final String imageUrlM;
    private final String imageUrlL;

    public Book() {
        this.id = 0;
        this.isbn = "";
        this.title = "";
        this.author = "";
        this.year = "";
        this.publisher = "";
        this.imageUrlS = "";
        this.imageUrlM = "";
        this.imageUrlL = "";
    }

    public Book(final int id, String isbn, String title, String author, String year, String publisher, String imageUrlS, String imageUrlM, String imageUrlL) {
        this.id = id;
        this.isbn = isbn;
        this.title = title;
        this.author = author;
        this.year = year;
        this.publisher = publisher;
        this.imageUrlS = imageUrlS;
        this.imageUrlM = imageUrlM;
        this.imageUrlL = imageUrlL;
    }

    public static Book generateFromList(int id, List<String> listRaw) {
        List<String> list = new ArrayList<>();
        for (String item : listRaw) {
            list.add(removeQuotes(item));
        }
        return new Book(id, list.get(0), list.get(1), list.get(2), list.get(3), list.get(4), list.get(5), list.get(6), list.get(7));
    }

    public static String removeQuotes(String input) {
        if (input == null) {
            return null;
        }
        return input.replace("\"", "").replace("'", "");
    }

    public int getId() {
        return id;
    }

    public String getIsbn() {
        return isbn;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public String getYear() {
        return year;
    }

    public String getPublisher() {
        return publisher;
    }

    public String getImageUrlS() {
        return imageUrlS;
    }

    public String getImageUrlM() {
        return imageUrlM;
    }

    public String getImageUrlL() {
        return imageUrlL;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Book book)) return false;
        return Objects.equals(isbn, book.isbn);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(isbn);
    }

    @Override
    public String toString() {
        return "Book{" +
                "id=" + id +
                ", isbn='" + isbn + '\'' +
                ", title='" + title + '\'' +
                '}';
    }
}