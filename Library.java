import java.io.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Library {
    private HashSet<Book> bookList = new HashSet<>();
    private HashSet<Log> bookLog = new HashSet<>();

    public void checkFileExists(){
        try {
            if(!(new File("book.ser").exists())) {
                try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("book.ser"))){
                    oos.writeObject(new HashSet<Book>());
                }
            }
            if(!(new File("log.ser").exists())) {
                try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("log.ser"))){
                    oos.writeObject(new HashSet<Log>());
                }
            }
        } catch (IOException e) {
            System.out.println("Error: Can't create book and/or log files" + e.getMessage());
        }
    }

    public String displayBooks(){
        try {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("book.ser"))) {
                bookList = (HashSet<Book>) ois.readObject();
            }
            if(!(bookList.isEmpty()))
                return bookList.stream().map(b -> "**********\n%s\n**********".formatted(b.toString())).collect(Collectors.joining("\n"));
            else return "No Books in the Database!";
        } catch (Exception e) {
            System.out.println("Error" + e.getMessage());
        }
        return "Error reading book database!";
    }

    public void addBook(Scanner sc){
        try{
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("book.ser"))) {
                bookList = (HashSet<Book>) ois.readObject();
            }
            ObjectOutputStream oosBook = new ObjectOutputStream(new FileOutputStream("book.ser"));
            System.out.print("Enter name of book: ");
            String bookName = sc.nextLine();
            System.out.print("Enter name of author: ");
            String authorName = sc.nextLine();
            if(bookList.add(new Book(bookName, authorName))){
                oosBook.writeObject(bookList);
                oosBook.flush();
                System.out.println("Book Added Successfully to Library Database!");
            }else{
                System.out.println("There was a problem adding the book.");
            }
            oosBook.close();
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public void removeBook(Scanner sc){
        try{
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("book.ser"))) {
                bookList = (HashSet<Book>) ois.readObject();
            }
            ObjectOutputStream oosBook = new ObjectOutputStream(new FileOutputStream("book.ser"));
            System.out.print("Enter full title of book to be removed: ");
            String bookTitle = sc.nextLine();
            if(bookList.removeIf(b -> b.bookTitle().equalsIgnoreCase(bookTitle))){
                oosBook.writeObject(bookList);
                oosBook.flush();
                System.out.println("Book has been removed successfully!");
            }else{
                System.out.println("Error removing the book!");
            }
            oosBook.close();
        }catch(Exception e){
            System.out.println("Error: " + e.getMessage());
        }
    }

    public void checkoutBook(Scanner sc){
        try{
            this.displayBooks();
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("book.ser"))) {
                bookList = (HashSet<Book>) ois.readObject();
            }
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("log.ser"))) {
                bookLog = (HashSet<Log>) ois.readObject();
                if (bookLog == null) bookLog = new HashSet<>();
            }
            ObjectOutputStream oosLog = new ObjectOutputStream(new FileOutputStream("log.ser"));
            System.out.print("Enter name of book to check out: ");
            String bookTitle = sc.nextLine();
            System.out.print("Enter name of the author: ");
            String authorName = sc.nextLine();
            List<Book> checkoutBook =  bookList.stream().filter(b -> b.bookTitle().equals(bookTitle) && b.author().equals(authorName)).toList();
            if(!checkoutBook.isEmpty()){
                System.out.print("Enter name of borrower: ");
                String borrowName = sc.nextLine();
                bookLog.add(new Log(borrowName, checkoutBook.getFirst(), LocalDateTime.now()));
                oosLog.writeObject(bookLog);
                System.out.println("Book has been checked out!");
            }else{
                System.out.println("The book title or author name was not found!");
            }
            oosLog.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());;
        }
    }

    public String displayLog(){
       try{
           try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("log.ser"))) {
               bookLog = (HashSet<Log>) ois.readObject();
               if (bookLog == null) bookLog = new HashSet<>();
           }
           if(!bookLog.isEmpty()){
               return bookLog.stream()
                       .map(l -> "**********\n%s\n**********".formatted(l.toString()))
                       .collect(Collectors.joining("\n"));
           }else{
               return "Book Log is Empty!";
           }
       }catch (Exception e){
           System.out.println("Error: " + e.getMessage());
       }
       return "Error reading the log database!";
    }

    private String displayCustomerLog(String name){
        try{
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("log.ser"))) {
                bookLog = (HashSet<Log>) ois.readObject();
                if (bookLog == null){
                    return "No customer log data found!";
                };
            };
            if(!bookLog.isEmpty()){
                String customerList = bookLog.stream()
                        .filter(l -> l.borrowerName().equalsIgnoreCase(name))
                        .map(l -> "**********\n%s\n**********".formatted(l.toString()))
                        .collect(Collectors.joining("\n"));
                return customerList.isEmpty() ? "" : customerList;
            }else{
                return "Book Log is Empty!";
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
        return "Error reading the log database!";
    }

    public void returnBook(Scanner sc){
        try {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("log.ser"))) {
                bookLog = (HashSet<Log>) ois.readObject();
                if (bookLog == null) bookLog = new HashSet<>();
            } catch (Exception e) {
                System.out.println("File not found!");
                return;
            }
//            ObjectOutputStream oosLog = new ObjectOutputStream(new FileOutputStream("log.ser"));
            if(!bookLog.isEmpty()){
                System.out.print("Enter name of borrower: ");
                String borrowName = sc.nextLine().trim();
                String borrowList = displayCustomerLog(borrowName);
                if (!(borrowList == null || borrowList.isEmpty())){
                    System.out.println(borrowList);
                    System.out.print("Enter name of book to be returned: ");
                    String bookName = sc.nextLine().trim();
                    System.out.print("Enter name of the author: ");
                    String authorName = sc.nextLine().trim();
                    try (ObjectOutputStream oosLog = new ObjectOutputStream(new FileOutputStream("log.ser"));){
                        if(bookLog.removeIf(b -> b.book().bookTitle().equalsIgnoreCase(bookName) &&
                                b.book().author().equalsIgnoreCase(authorName) &&
                                b.borrowerName().equalsIgnoreCase(borrowName))){
                            System.out.println("Book has been returned!\nBook Log Updated!");
                            oosLog.writeObject(bookLog);
                        }else{
                            System.out.println("Book with given title and/or author was not found!");
                        }
                    }
                }else{
                    System.out.println("Customer not found!");
                }
            }else{
                System.out.println("Book Log is empty!");
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

}
