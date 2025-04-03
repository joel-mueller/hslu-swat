package ch.hslu.business;

import ch.hslu.entities.Book;
import ch.hslu.entities.BorrowRecord;
import ch.hslu.entities.Customer;
import ch.hslu.persistence.Database;
import ch.hslu.persistence.RecordFilter;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public class FakeDatabase implements Database {
    private List<Customer> customerList;
    private List<Book> bookList;
    private List<BorrowRecord> borrowRecordList;

    public FakeDatabase() {
        UUID customerMaxBooksNothingOverdueUUID = UUID.randomUUID();
        UUID customerNotMaxBooksOverdueUUID = UUID.randomUUID();
        UUID customerNotMaxBooksNothingOverdueUUID = UUID.randomUUID();
        UUID customerMaxBooksBooksOverdueUUID = UUID.randomUUID();

        this.customerList = List.of(
                new Customer(customerMaxBooksNothingOverdueUUID, "Hans", "Müller", "Grubenacker 7", "4417"),
                new Customer(customerNotMaxBooksOverdueUUID, "Anna", "Schmidt", "Bachstraße 12", "5000"),
                new Customer(customerNotMaxBooksNothingOverdueUUID, "Peter", "Weber", "Wiesenweg 5", "3002"),
                new Customer(customerMaxBooksBooksOverdueUUID, "Lisa", "Fischer", "Seestraße 9", "6023"));

        this.bookList = List.of(
                new Book(1, "2342455344", "Harry Potter", "J.K. Rowling", "2010", "Ex Libris",
                        "https://imagesmall.com/1", "https://imagemedium.com/1", "https://imagebig.com/1"),
                new Book(2, "9876543210", "Herr der Ringe", "J.R.R. Tolkien", "1954", "Hobbit Presse",
                        "https://imagesmall.com/2", "https://imagemedium.com/2", "https://imagebig.com/2"),
                new Book(3, "1928374655", "Die Tribute von Panem", "Suzanne Collins", "2008", "Oetinger",
                        "https://imagesmall.com/3", "https://imagemedium.com/3", "https://imagebig.com/3"),
                new Book(4, "5647382910", "1984", "George Orwell", "1949", "Ullstein", "https://imagesmall.com/4",
                        "https://imagemedium.com/4", "https://imagebig.com/4"),
                new Book(5, "1029384756", "Moby Dick", "Herman Melville", "1851", "Fischer", "https://imagesmall.com/5",
                        "https://imagemedium.com/5", "https://imagebig.com/5"),
                new Book(6, "6789054321", "Der kleine Prinz", "Antoine de Saint-Exupéry", "1943", "Karl Rauch Verlag",
                        "https://imagesmall.com/6", "https://imagemedium.com/6", "https://imagebig.com/6"),
                new Book(7, "4561237890", "Faust", "Johann Wolfgang von Goethe", "1808", "Reclam",
                        "https://imagesmall.com/7", "https://imagemedium.com/7", "https://imagebig.com/7"),
                new Book(8, "3216549870", "Sofies Welt", "Jostein Gaarder", "1991", "Hanser",
                        "https://imagesmall.com/8", "https://imagemedium.com/8", "https://imagebig.com/8"),
                new Book(9, "7418529630", "Das Parfum", "Patrick Süskind", "1985", "Diogenes",
                        "https://imagesmall.com/9", "https://imagemedium.com/9", "https://imagebig.com/9"),
                new Book(10, "3692581470", "Alice im Wunderland", "Lewis Carroll", "1865", "Anaconda",
                        "https://imagesmall.com/10", "https://imagemedium.com/10", "https://imagebig.com/10"),
                new Book(11, "8529637410", "Der Prozess", "Franz Kafka", "1925", "Fischer", "https://imagesmall.com/11",
                        "https://imagemedium.com/11", "https://imagebig.com/11"),
                new Book(12, "1593574860", "Der Alchemist", "Paulo Coelho", "1988", "Diogenes",
                        "https://imagesmall.com/12", "https://imagemedium.com/12", "https://imagebig.com/12"),
                new Book(13, "7539514560", "Der Graf von Monte Christo", "Alexandre Dumas", "1844", "Piper",
                        "https://imagesmall.com/13", "https://imagemedium.com/13", "https://imagebig.com/13"),
                new Book(14, "1597534862", "Stolz und Vorurteil", "Jane Austen", "1813", "Anaconda",
                        "https://imagesmall.com/14", "https://imagemedium.com/14", "https://imagebig.com/14"),
                new Book(15, "8527419630", "Die unendliche Geschichte", "Michael Ende", "1979", "Thienemann",
                        "https://imagesmall.com/15", "https://imagemedium.com/15", "https://imagebig.com/15"),
                new Book(16, "4569871230", "Sherlock Holmes", "Arthur Conan Doyle", "1892", "Reclam",
                        "https://imagesmall.com/16", "https://imagemedium.com/16", "https://imagebig.com/16"),
                new Book(17, "9513578524", "Brave New World", "Aldous Huxley", "1932", "Fischer",
                        "https://imagesmall.com/17", "https://imagemedium.com/17", "https://imagebig.com/17"),
                new Book(18, "7531598524", "Ulysses", "James Joyce", "1922", "Suhrkamp", "https://imagesmall.com/18",
                        "https://imagemedium.com/18", "https://imagebig.com/18"),
                new Book(19, "8524561239", "Dune", "Frank Herbert", "1965", "Heyne", "https://imagesmall.com/19",
                        "https://imagemedium.com/19", "https://imagebig.com/19"),
                new Book(20, "1594872635", "Der Name der Rose", "Umberto Eco", "1980", "Hanser",
                        "https://imagesmall.com/20", "https://imagemedium.com/20", "https://imagebig.com/20"));

        this.borrowRecordList = List.of(
                new BorrowRecord.Builder().bookId(1).customerId(customerMaxBooksNothingOverdueUUID)
                        .dateBorrowed(LocalDate.now().minusDays(60)).build(),
                new BorrowRecord.Builder().bookId(2).customerId(customerMaxBooksNothingOverdueUUID)
                        .dateBorrowed(LocalDate.now().minusDays(20)).build(),
                new BorrowRecord.Builder().bookId(3).customerId(customerMaxBooksNothingOverdueUUID)
                        .dateBorrowed(LocalDate.now().minusDays(90)).build(),
                new BorrowRecord.Builder().bookId(4).customerId(customerMaxBooksNothingOverdueUUID)
                        .dateBorrowed(LocalDate.now().minusDays(60)).build(),
                new BorrowRecord.Builder().bookId(5).customerId(customerMaxBooksNothingOverdueUUID).build(),

                new BorrowRecord.Builder().bookId(6).customerId(customerNotMaxBooksOverdueUUID)
                        .dateBorrowed(LocalDate.now().minusDays(91)).build(),
                new BorrowRecord.Builder().bookId(7).customerId(customerNotMaxBooksOverdueUUID)
                        .dateBorrowed(LocalDate.now().minusDays(40)).build(),

                new BorrowRecord.Builder().bookId(11).customerId(customerNotMaxBooksNothingOverdueUUID).build(),
                new BorrowRecord.Builder().bookId(12).customerId(customerNotMaxBooksNothingOverdueUUID).build(),
                new BorrowRecord.Builder().bookId(14).customerId(customerNotMaxBooksNothingOverdueUUID).build(),

                new BorrowRecord.Builder().bookId(15).customerId(customerMaxBooksBooksOverdueUUID)
                        .dateBorrowed(LocalDate.now().minusDays(60)).build(),
                new BorrowRecord.Builder().bookId(18).customerId(customerMaxBooksBooksOverdueUUID)
                        .dateBorrowed(LocalDate.now().minusDays(91)).build(),
                new BorrowRecord.Builder().bookId(19).customerId(customerMaxBooksBooksOverdueUUID)
                        .dateBorrowed(LocalDate.now().minusDays(40)).build(),
                new BorrowRecord.Builder().bookId(8).customerId(customerMaxBooksBooksOverdueUUID)
                        .dateBorrowed(LocalDate.now().minusDays(100)).build(),
                new BorrowRecord.Builder().bookId(10).customerId(customerMaxBooksBooksOverdueUUID)
                        .dateBorrowed(LocalDate.now().minusDays(60)).build());
    }

    @Override
    public String testDB() {
        return "";
    }

    @Override
    public Customer getCustomer(UUID id) {
        return this.customerList.stream().filter(customer -> customer.id().equals(id)).findFirst().orElse(null);
    }

    @Override
    public Book getBook(int id) {
        return this.bookList.stream().filter(book -> book.id() == id).findFirst().orElse(null);
    }

    @Override
    public List<BorrowRecord> getRecords(RecordFilter filter) {
        List<BorrowRecord> filtered = this.borrowRecordList.stream().filter(record -> record.filter(filter)).toList();
        return List.copyOf(filtered);
    }

    @Override
    public boolean addBorrowRecord(BorrowRecord record) {
        this.borrowRecordList.add(record);
        return true;
    }

    @Override
    public boolean updateBorrowRecord(BorrowRecord record) {
        List<BorrowRecord> records = getRecords(new RecordFilter.Builder().id(record.getId()).build());
        // TODO funktionalität fertig machen
        return true;
    }
}
