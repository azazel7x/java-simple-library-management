import java.io.*;
import java.util.Scanner;

public class LibraryManagementMenu {
    public static void main(String[] args) {
        clearScreen();
        String menuText = """
                      Library Management System
                *************************************
                *  1. Display Books                 *
                *  2. Add New Books                 *
                *  3. Remove Books                  *
                *  4. Check-out Books               *
                *  5. Return Books                  *
                *  6. View Borrowed Books           *
                *  7. Exit                          *
                *************************************
                """;
        Library library = new Library();
        library.checkFileExists();
        try(Scanner sc = new Scanner(System.in);){
            int menuChoice;
            do{
                clearScreen();
                System.out.println(menuText);
                System.out.print("=> ");
                menuChoice = sc.nextInt();
                sc.nextLine();
                switch (menuChoice){
                    case 1:
                        System.out.println(library.displayBooks());
                        System.out.println("Press Enter to continue...");
                        sc.nextLine();
                        break;

                    case 2:
                        library.addBook(sc);
                        System.out.println("Press Enter to continue...");
                        sc.nextLine();
                        break;

                    case 3:
                        library.removeBook(sc);
                        System.out.println("Press Enter to continue...");
                        sc.nextLine();
                        break;

                    case 4:
                        System.out.println(library.displayBooks());;
                        library.checkoutBook(sc);
                        System.out.println("Press Enter to continue...");
                        sc.nextLine();
                        break;

                    case 5:
                        library.returnBook(sc);
                        System.out.println("Press Enter to continue...");
                        sc.nextLine();
                        break;

                    case 6:
                        System.out.println(library.displayLog());
                        System.out.println("Press Enter to continue...");
                        sc.nextLine();
                        break;

                    case 7:
                        System.out.println("Exiting...");
                        break;
                }
            }while(menuChoice != 7);
        }catch(Exception e){
            System.out.println("Error: " + e.getMessage());
        }

    }

    public static void clearScreen(){
        try{
            if (System.getProperty("os.name").contains("Windows")) {
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            } else {
                System.out.print("\033[H\033[2J");
                System.out.flush();
            }
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }
}
