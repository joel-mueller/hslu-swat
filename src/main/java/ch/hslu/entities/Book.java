package ch.hslu.entities;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public record Book(int id, String isbn, String title, String author, String year, String publisher, String imageUrlS,
        String imageUrlM, String imageUrlL) {

    public static Book generateFromList(int id, List<String> listRaw) {
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
