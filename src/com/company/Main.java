package com.company;

import com.company.libraryFunctions.BookFunctions;
import com.company.libraryFunctions.Borrowing;
import com.company.libraryFunctions.FileHandling;
import com.company.libraryFunctions.Login;
import com.company.objects.Book;
import static com.company.libraryFunctions.GetInput.input;
import static com.company.libraryFunctions.FileHandling.deleteFile;

public class Main {

    //create Borrow.bookReturn and then upload to GitHub
    public static void main(String[] args) {
        FileHandling.createFile(FileHandling.bookLibrary);//sets up the log in file
        Book.bookInitializer();//sets up the array of Book objects
        Borrowing.borrowInfoInitializer();//sets up the array of Borrower objects
        defaultMenu();
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
                    FileHandling.libraryFilePrint();
                    break;
                case "search":
                    BookFunctions.searchForBook();
                    break;
                case "borrow":
                    Borrowing.borrowFilePrint();
                    Borrowing.bookBorrow();
                    break;
                case "return":
                    Borrowing.borrowFilePrint();
                    Borrowing.bookReturn();
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
                            deleteFile(FileHandling.bookLibrary);
                        } else if (fileToReset.equals("login")) {
                            deleteFile(FileHandling.loginDetails);
                        } else {
                            deleteFile(FileHandling.borrowInfo);
                        }
                        break;
                    case "add":
                        BookFunctions.add();
                        break;
                    case "delete":
                        BookFunctions.remove(input("What is the book title?"));
                        break;
                    case "edit":
                        BookFunctions.edit();
                        break;
                    case "close":
                        closeInterface = true;
                        break;
                    default:
                        System.out.println("You have not selected a valid option");
                }
            }
        }
    }
}