import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public record Log(String borrowerName, Book book, LocalDateTime borrowDate) implements Serializable {
    public Log{
        borrowerName = borrowerName.trim();
    }

    @Override
    public String toString(){
        return "Borrower Name: %s\nBook Name: %s\nAuthor Name: %s\nDate Borrowed: %s\n".formatted(this.borrowerName,this.book.bookTitle(),this.book.author(), this.borrowDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd  HH:mm")));
    }
}
