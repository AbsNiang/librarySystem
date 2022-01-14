package com.company.libraryFunctions;

import com.company.objects.Book;

import java.io.FileWriter;
import java.io.IOException;
import java.io.File;

import static com.company.objects.Book.books;

public class FileHandling {

    public static File borrowInfo = new File("borrowingInformation.txt");
    public static File bookLibrary = new File("BookInformation.txt");
    public static File loginDetails = new File("LoginInformation.txt");

    public static void createFile(File currentFile) {
        try {
            if (currentFile.createNewFile()) {
                System.out.println("The file '" + currentFile.getName() + "' has been created.");
            }
        } catch (IOException e) {
            System.out.println("An error has occurred");
        }
    }
    public static void deleteFile(File fileName) {
        if (fileName.delete()) {
            System.out.println("The file has been reset.");
        }
    }
    public static void bookAppendFile() {
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
    //appends the login file
    public static void appendLoginFile(String accountDetails) {
        try {
            FileWriter fileEdit = new FileWriter(loginDetails.getName(), true); //appends file (change to 'false' to overwrite file)
            fileEdit.write(accountDetails);
            fileEdit.close();
            System.out.println("Successfully registered into the library.");
        } catch (IOException e) {
            System.out.println("An error has occurred");
        }
    }
    public static void libraryFilePrint() {
        for (Book book : books) {
            System.out.println(book.fancyToString());
        }
    }
}
