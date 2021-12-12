package com.company;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import static com.company.Main.*;

public class Book {
    public static File bookLibrary = new File("BookInformation.txt");
    public static ArrayList<Book> books = new ArrayList<>();
    private String title;
    private int ISBN;
    private String author;
    private String genre;

    public Book(String title, int ISBN, String author, String genre) {
        this.title = title;
        this.ISBN = ISBN;
        this.author = author;
        this.genre = genre;

    }

    //method which adds a stated amount of books to the book library file
    public static void add() {
        int numbOfBooks;
        while (true) {
            try {
                numbOfBooks = Integer.parseInt(input("How many books will there be?"));
                break;
            } catch (NumberFormatException e) {
                System.out.println("You have not typed an integer");
            }
        }
        for (int i = 0; i < numbOfBooks; i++) {
            books.add(bookSetup());
        }

        appendFile();
    }

    //creates a Book object and uses the toString method to turn it into the format used in the BookInfo. file
    private static Book bookSetup() {
        while (true) {
            try {
                Book currentBook = new Book(input("What is the book title?"),
                        Integer.parseInt(input("What is the ISBN of the book?")),
                        input("Who is the book author?"), input("What is the book genre?"));
                if (ISBNCheck(currentBook.getISBN())) {
                    return currentBook;
                }
            } catch (Exception e) {
                System.out.println("You did not type an integer for the ISBN");
            }
            System.out.println("The ISBN you gave is already saved in the library.");
        }
    }

    public static void remove(String title) {
        boolean bookFound = false;
        StringBuilder newLibrary = new StringBuilder();
        int count = 0;
        while (!bookFound) {
            System.out.println("___________________________________________________________________________");
            boolean finalLine = false;
            if (count > 0) {
                title = input("What is the book title?" + "\nType 'end' to stop searching.").toLowerCase();
            }
            if (title.equalsIgnoreCase("end")) {
                break;
            }
            try {
                Scanner fileInput = new Scanner(bookLibrary); //searches file line by line for the book title
                while (fileInput.hasNextLine()) {
                    String data = fileInput.nextLine().toLowerCase();
                    String[] bookInformation = data.split(", "); //There are four different types of information we have stored about the book
                    if (bookInformation[0].equals(title)) {
                        if (fileInput.hasNextLine()) {
                            data = fileInput.nextLine().toLowerCase();
                        } else {
                            finalLine = true;
                        }
                        bookFound = true;
                    }
                    if (!finalLine) { //avoids an error as you can't go to the next line when you are at the end of the library file
                        newLibrary.append(data).append("\n");
                    }
                }
            } catch (Exception e) {
                System.out.println("An error has occurred");
            }
            if (!bookFound) {
                System.out.println("This book cannot be located. Check the spelling of the title.");
            }
            count++;
        }
        if (bookFound) {
            Main.deleteFile(bookLibrary);
            try {
                FileWriter fileEdit = new FileWriter(bookLibrary.getName(), false); //appends file (change to 'false' to overwrite file)
                fileEdit.write(String.valueOf(newLibrary));
                fileEdit.close();
            } catch (IOException e) {
                System.out.println("An error has occurred");
            }
        }
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public int getISBN() {
        return ISBN;
    }

    public void setISBN(int ISBN) {
        this.ISBN = ISBN;
    }

    public static boolean ISBNCheck(int ISBN) { //each book needs to have a unique ISBN and be 9 digits
        try {
            Scanner fileInput = new Scanner(bookLibrary); //searches file line by line for the book title
            while (fileInput.hasNextLine()) {
                String data = fileInput.nextLine().toLowerCase();
                String[] bookInformation = data.split(", ");
                if (Integer.parseInt(bookInformation[1]) == (ISBN)) { // ISBN location in array is '1'
                    System.out.println("A book with this ISBN has already been registered.");
                    System.out.println("    Book Title: " + bookInformation[0]);
                    System.out.println("    ISBN: " + bookInformation[1]);
                    System.out.println("    Author: " + bookInformation[2]);
                    System.out.println("    Genre: " + bookInformation[3]);
                    return false;
                }
            }
        } catch (Exception e) {
            System.out.println("An error has occurred");
        }
        if (String.valueOf(ISBN).length() == 9) {
            return true;
        } else {
            System.out.println("The ISBN is not an adequate length.");
            return false;
        }
    }

    public static void edit() {
        boolean bookFound = false;
        while (!bookFound) {
            String bookTitle = input("What is the book title?" + "\nType 'end' to stop searching.").toLowerCase();
            if (bookTitle.equalsIgnoreCase("end")) {
                break;
            }
            try {
                for (Book book : books) {
                    if (book.getTitle().equals(bookTitle)) {
                        bookFound = true;
                        boolean endEdit = false;
                        do {
                            switch (input("What part of the book would you like to edit?" + "\nYou can edit the: " +
                                    "\n -'Title'" + "\n -'ISBN'" + "\n -'Author'" + "\n -'Genre'").toLowerCase()) {
                                case "title":
                                    book.setTitle(input("What is the new title?"));
                                    break;
                                case "isbn":
                                    book.setISBN(Integer.parseInt(input("What is the new ISBN?")));
                                    break;
                                case "author":
                                    book.setAuthor(input("What is the new author?"));
                                    break;
                                case "genre":
                                    book.setGenre(input("What is the new genre?"));
                                    break;
                                default:
                                    System.out.println("You did not type a valid input.");
                            }
                            if (!(input("Would you like to stop editing?").toLowerCase().contains("y"))) {
                                endEdit = true;
                            }
                        } while (!endEdit);
                    }
                }
            } catch (Exception e) {
                System.out.println("An error has occurred");
            }
            if (!bookFound) {
                System.out.println("This book cannot be located. Check the spelling of the title.");
            }
        }
    }

    public static void searchForBook() {
        switch (input("What would you like to search for the book by?" + "\nYou can search through the: " +
                "\n -'Title'" + "\n -'ISBN'" + "\n -'Author'" + "\n -'Genre'").toLowerCase()) {
            //each number represents what item they are using to search for the book
            case "title":
                searchByParameter(0);
                break;
            case "isbn":
                searchByParameter(1);
                break;
            case "author":
                searchByParameter(2);
                break;
            case "genre":
                searchByParameter(3);
                break;
        }

    }

    private static void searchByParameter(int itemToSearch) {
        //itemToSearch represents how they are searching for the book e.g. book name or author
        boolean characteristicFound = false;
        while (!characteristicFound) {
            String characteristic = bookCharacteristic(itemToSearch);
            if (characteristic.equals("end")) {
                break;
            }
            try {
                Scanner fileInput = new Scanner(bookLibrary); //searches file line by line for the book title
                int count = 1;
                while (fileInput.hasNextLine()) {
                    String data = fileInput.nextLine().toLowerCase();
                    String[] bookInformation = data.split(", "); //There are four different types of information we have stored about the book
                    if (bookInformation[itemToSearch].contains(characteristic)) {
                        System.out.println(" Book " + count + ":");
                        characteristicFound = true;
                        System.out.println("    Book Title: " + bookInformation[0]);
                        System.out.println("    ISBN: " + bookInformation[1]);
                        System.out.println("    Author: " + bookInformation[2]);
                        System.out.println("    Genre: " + bookInformation[3]);
                        count++;
                    }
                }
            } catch (Exception e) {
                System.out.println("An error has occurred");
            }
            if (!characteristicFound) {
                System.out.println("Unfortunately the library does not currently have any books with this data.");
            }
        }
    }

    //collects the characteristic that the user is using to sort the books
    private static String bookCharacteristic(int itemToSearch) {
        String bookCharacteristic;
        if (itemToSearch == 0) {
            bookCharacteristic = input("What is the book title?" + "\nType 'end' to stop searching.").toLowerCase();
        } else if (itemToSearch == 1) {
            bookCharacteristic = input("What is the ISBN number?" + "\nType 'end' to stop searching.").toLowerCase();
        } else if (itemToSearch == 2) {
            bookCharacteristic = input("Who is the book author?" + "\nType 'end' to stop searching.").toLowerCase();
        } else {
            bookCharacteristic = input("What is the book genre?" + "\nType 'end' to stop searching.").toLowerCase();
        }
        return bookCharacteristic;
    }

    public static void libraryFilePrint() {
        for (Book book : books) {
            System.out.println(book.fancyToString());
        }
    }

    public static void appendFile() {
        try {
            FileWriter fileEdit = new FileWriter(bookLibrary.getName(), false); //appends file (change to 'false' to overwrite file)
            StringBuilder entireLibrary = new StringBuilder();
            for (Book book : books) {
                entireLibrary.append(book.toString()).append("\n");
            }
            fileEdit.write(entireLibrary.toString());
            fileEdit.close();
            System.out.println("Successfully saved books.");
        } catch (IOException e) {
            System.out.println("An error has occurred");
        }
    }

    public static void bookInitializer() {
        try {
            Scanner fileInput = new Scanner(bookLibrary);
            while (fileInput.hasNextLine()) {
                String data = fileInput.nextLine().toLowerCase();
                String[] bookInformation = data.split(", "); //There are four different types of information we have stored about the book
                Book lineBook = new Book(bookInformation[0], Integer.parseInt(bookInformation[1]), bookInformation[2], bookInformation[3]);
                books.add(lineBook);
            }
        } catch (IOException e) {
            System.out.println("An error has occurred");
        }
    }

    public static void createLibraryFile() {
        try {
            if (bookLibrary.createNewFile()) {
                System.out.println("The file '" + bookLibrary.getName() + "' has been created.");
            }
        } catch (IOException e) {
            System.out.println("An error has occurred");
        }
    }

    public void setAuthor(String author) {
        this.author = author;
    }


    public void setGenre(String genre) {
        this.genre = genre;
    }

    @Override
    public String toString() {
        return title + ", " + ISBN + ", " + author + ", " + genre;
    }

    public String fancyToString() {
        return "title= " + title + '\n' + "  ISBN= " + ISBN + "\n" + "  author= " + author + '\n' + "  genre= " + genre + '\n';
    }
}
