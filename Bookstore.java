import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;
/**
 *  Homework 8 - Challenge
 *
 *
 * To manage book store.
 *
 * @author advitbhullar, L-24
 *
 * @version October 16 ,2021
 */
class Bookstore {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String menu = "Choose one of the following options:\n" + "(1) View book catalog\n" + "(2) Purchase Book\n" +
                "(3) Search for a book by name\n" + "(4) Search for a book by author\n" + "(0) Exit the store";
        StoreCatalog catalog;
        System.out.println("Welcome to the CS180 Bookstore!");
        while (true) {
            System.out.println("What is the name of the bookstore file?");
            String fileName = sc.nextLine();
            try {
                catalog = new StoreCatalog(fileName);
                break;
            } catch (FileNotFoundException e) {
                System.out.println("The given file was not found! Try again.");
            } catch (BookParseException e) {
                System.out.println(e.getMessage());
                return;
            }
        }
        System.out.println(menu);
        int choice = sc.nextInt();
        sc.nextLine();
        while (choice != 0) {
            if (choice == 1) {
                do {
                    ArrayList<Book> books = catalog.getBookList();
                    System.out.println("Select one of the books from the given options to view more details");
                    for (int i = 0; i < books.size(); i++) {
                        System.out.printf("(%d) %s\n", (i + 1), books.get(i).getDisplayString());
                    }
                    System.out.println("(0) Go to previous menu");
                    choice = sc.nextInt();
                    sc.nextLine();
                    if (choice < 0 || choice > books.size()) {
                        System.out.println("Select a valid choice!");
                    }
                    if (choice != 0) {
                        System.out.println(books.get(choice - 1).getInformationString());
                        System.out.println("Would you like to purchase this book?(y/N)");
                        if (sc.nextLine().toLowerCase().charAt(0) == 'y') {
                            try {
                                catalog.purchaseBook(books.get(choice - 1).getBookName());
                                System.out.println("Book purchased!");
                                choice = 0;
                            } catch (BookNotFoundException e) {
                                System.out.println(e.getMessage());
                            }
                            System.out.println();
                        }
                    }
                } while (choice != 0);
            } else if (choice == 2) {
                System.out.println("What is the name of the book you wish to purchase?");
                String bookName = sc.nextLine();
                try {
                    catalog.purchaseBook(bookName);
                    System.out.println("Book purchased!");
                } catch (BookNotFoundException e) {
                    System.out.println(e.getMessage());
                }
            } else if (choice == 3) {
                System.out.println("What is the name of the book you wish to search for?");
                String bookName = sc.nextLine();
                try {
                    ArrayList<Book> foundBooks = catalog.searchByName(bookName);
                    System.out.println("The following books were found:");
                    for (Book book : foundBooks) {
                        System.out.println(book.getDisplayString());
                    }
                } catch (BookNotFoundException e) {
                    System.out.println(e.getMessage());
                }
            } else if (choice == 4) {
                System.out.println("What is the name of the author you wish to search for?");
                String authorName = sc.nextLine();
                try {
                    ArrayList<Book> foundBooks = catalog.searchByAuthor(authorName);
                    System.out.println("The following books were found:");
                    for (Book book : foundBooks) {
                        System.out.println(book.getDisplayString());
                    }
                } catch (BookNotFoundException e) {
                    System.out.println(e.getMessage());
                }
            }
            System.out.println();
            System.out.println(menu);
            choice = sc.nextInt();
            sc.nextLine();
        }

        try {
            catalog.writeChangesToFile();
        } catch (FileNotFoundException e) {
            System.out.println("The given file was not found! Failed to save changes.");
            return;
        }
        System.out.println("Thank you for using the bookstore!");
    }
}
