package com.company;

import java.io.File;
import java.util.Scanner;

public class Main {

    //create Borrow.bookReturn and then upload to GitHub
    public static void main(String[] args) {
        Login.createLoginFile();//sets up the log in file
        Book.bookInitializer();//sets up the array of Book objects
        Borrower.borrowInfoInitializer();//sets up the array of Borrower objects
        defaultMenu();
    }

    //method which takes input to avoid having to declare a new 'Scanner' object in each method
    public static String input(String prompt) {
        System.out.println(prompt);
        Scanner input = new Scanner(System.in);
        return input.nextLine();
    }

    private static void defaultMenu() {
        adminMenu();// allows any admins to go into the admin-only menu
        boolean closeMenu = false;
        while (!closeMenu) {
            switch (input("You have entered the library-user system." + "\nThese are the range of options you have:" +
                    "\n -'view'; look at the entire library" +
                    "\n -'search'; search for a specific book" +
                    "\n -'borrow'; borrow up to three books" +
                    "\n -'return'; return a book you have previously borrowed").toLowerCase()) {
                case "view":
                    Book.libraryFilePrint();
                    break;
                case "search":
                    Book.searchForBook();
                    break;
                case "borrow":
                    Borrower.borrowFilePrint();
                    Borrower.bookBorrow();
                    break;
                case "return":
                    Borrower.borrowFilePrint();
                    Borrower.bookReturn();
                    break;
                default:
                    System.out.println("You have not selected a valid option");
            }
            if (input("Would you like to close the menu?").toLowerCase().contains("y")) {
                closeMenu = true;
            }
        }
    }

    //method which creates a menu of choices for the user if they log into an admin account
    private static void adminMenu() {
        if (Login.signUpMenu()) {
            switch (input("Which menu would you like to go to?:" +
                    "\n - 'default'" + "\n - 'admin'")) {
                case "default":
                    return;
                case "admin":
                    System.out.println("Entering the admin menu");
                    break;
                default:
                    System.out.println("You haven't typed a valid input.");
                    System.out.println("Entering the admin system.");
                    System.out.println("________________________________________");
            }
            boolean closeInterface = false;
            while (!closeInterface) {
                switch (input("You have entered the admin system." + "\nThese are the range of options you have:" +
                        "\n -'reset'; reset the library of books or reset the login information or reset the borrowing list" +
                        "\n -'add'; append the library and add books into the system" +
                        "\n -'delete'; delete a specific book" +
                        "\n -'edit'; edit the content of a specific book" +
                        "\n -'close'; close the admin menu")) {
                    case "reset":
                        String fileToReset = input("What file would you like to reset?" + "\n 'library' or 'login' or 'borrow'").toLowerCase();
                        if (fileToReset.equals("library")) {
                            deleteFile(Book.bookLibrary);
                        } else if (fileToReset.equals("login")) {
                            deleteFile(Login.loginDetails);
                        } else {
                            deleteFile(Borrower.borrowInfo);
                        }
                        break;
                    case "add":
                        Book.add();
                        break;
                    case "delete":
                        Book.remove(input("What is the book title?"));
                        break;
                    case "edit":
                        Book.edit();
                        break;
                    case "close":
                        closeInterface = true;
                        break;
                    default:
                        System.out.println("You have not selected a valid option");
                }
            }
            Book.createLibraryFile();
        }
    }

    public static void deleteFile(File fileName) {
        if (fileName.delete()) {
            System.out.println("The file has been reset.");
        }
    }
}