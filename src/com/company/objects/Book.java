package com.company.objects;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import static com.company.libraryFunctions.FileHandling.bookLibrary;

public class Book {

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