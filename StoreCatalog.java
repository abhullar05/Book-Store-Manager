import java.io.*;
import java.util.ArrayList;
import java.util.Locale;
/**
 * Homework 8 - Challenge
 * <p>
 *
 * To manage book store.
 *
 * @author advitbhullar, L-24
 *
 * @version October 16 ,2021
 */

public class StoreCatalog {
    private ArrayList<Book> books;
    private String fileName;
    public StoreCatalog(String fileName) throws FileNotFoundException, BookParseException {
        this.fileName = fileName;
        books = new ArrayList<>();
        File f = new File(fileName);
        FileReader fr = new FileReader(f);
        BufferedReader bfr = new BufferedReader(fr);
        String s;
        try {
            s = bfr.readLine();
            while (s != null) {
                books.add(parseBook(s));
                s = bfr.readLine();
            }
            bfr.close();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    public ArrayList<Book> getBookList() {
        return books;
    }
    private static Book parseBook(String line) throws BookParseException {
        String[] s = line.split(" ");
        if (s.length != 5) {
            throw new BookParseException("Error parsing book! Data provided in a single line is invalid!");
        }
        float price;
        int quantity;
        int year;
        String bookName = s[0].replace('_', ' ');
        String authorName = s[1].replace('_', ' ');
        try {
            price = Float.parseFloat(s[2]);
            quantity = Integer.parseInt(s[3]);
            year = Integer.parseInt(s[4]);
        } catch (Exception e) {
            throw new BookParseException("Error parsing book! Failed to parse numbers from file");
        }
        Book b = new Book(bookName, authorName, price , quantity , year) ;
        return b;
    }
    public void purchaseBook(String bookName) throws BookNotFoundException {
        for (int i = 0; i < books.size() ; i++) {
            if (books.get(i).getBookName().equals(bookName)) {
                Book c = books.get(i);
                c.setQuantity(c.getQuantity() - 1);
                if (c.getQuantity() == 0)
                    books.remove(c);
                return;
            }
        }
        throw new BookNotFoundException("The given book was not found");

    }
    public ArrayList<Book> searchByAuthor(String authorName) throws BookNotFoundException {
        try {
            ArrayList<Book> booksFound = new ArrayList<Book>();
            for (int i = 0; i < books.size(); i++) {
                String authorName1 = books.get(i).getAuthorName();
                if (authorName1.toLowerCase().contains(authorName.toLowerCase())) {
                    booksFound.add(books.get(i));
                }
            }
            if (booksFound.size() == 0)
                throw new BookNotFoundException("No books found with the given author name!");
            else
                return booksFound;
        } catch (Exception e) {
            throw new BookNotFoundException("No books found with the given author name!");
        }
    }
    public ArrayList<Book> searchByName(String bookName) throws BookNotFoundException {
        ArrayList<Book> booksFound = new ArrayList<Book>();
        for (int i = 0; i < books.size(); i++) {
            String bookName1 =  books.get(i).getBookName();
            if (bookName1.toLowerCase().contains(bookName.toLowerCase())) {
                booksFound.add(books.get(i));
            }
        }
        if (booksFound.size() == 0)
            throw new BookNotFoundException("No books found with the given book name!");
        else
            return booksFound;
    }
    public void writeChangesToFile() throws FileNotFoundException {
        File f = new File(fileName);
        FileOutputStream fos = new FileOutputStream(f);
        PrintWriter pw = new PrintWriter(fos);
        for (int i = 0; i < books.size(); i++) {
            String line = books.get(i).getBookName().replace(' ', '_') +
                    " " + books.get(i).getAuthorName().replace(' ', '_')
                    + " " + String.format("%.2f", books.get(i).getPrice())
                    + " " + books.get(i).getQuantity()
                    + " " + books.get(i).getYear();
            pw.println(line);
        }
        pw.close();
    }



}
