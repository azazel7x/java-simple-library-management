import java.io.Serializable;

public record Book(String bookTitle, String author) implements Serializable {
    public Book{
        bookTitle = bookTitle.trim();
        author = author.trim();
    }

    @Override
    public String toString(){
        return "Book: %s\nAuthor:%s".formatted(this.bookTitle,this.author);
    }
}
