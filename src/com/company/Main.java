package com.company;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class Main {
    public static File loginDetails = new File("LoginInformation.txt");
    public static String userEmail;

    public static void main(String[] args) {
        createLoginFile();//sets up the log in file
        Book.bookInitializer();//sets up the array of Book objects
        Member.borrowInfoInitializer();//sets up the array of Member objects
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
                    Member.borrowFilePrint();
                    Member.bookBorrow();
                    break;
                case "return":
                    Member.borrowFilePrint();
                    Member.bookReturn();
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
        if (signUpMenu()) {
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
                            deleteFile(loginDetails);
                        } else {
                            deleteFile(Member.borrowInfo);
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


    //method which allows the user to either register a new account or log into a previously existing account
    private static boolean signUpMenu() {
        boolean adminCheck = false;
        boolean validOption = false;
        while (!validOption) {
            switch (input("Would you like to login or register.")) {
                case "login":
                    adminCheck = loginAccount(); //opens up more options if the user is an admin
                    validOption = true;
                    break;
                case "register":
                    registerAccount();
                    validOption = true;
                    break;
                default:
                    System.out.println("You have not selected a valid option." + "\nTry typing again in lower case.");
            }
        }
        return adminCheck;
    }

    private static void createLoginFile() {
        try {
            if (loginDetails.createNewFile()) {
                System.out.println("Setting up login system.");
            } else {
                System.out.println("Login System is set up.");
            }
        } catch (IOException e) {
            System.out.println("An error has occurred.");
        }
    }

    //appends the login file
    private static void appendLoginFile(String accountDetails) {
        try {
            FileWriter fileEdit = new FileWriter(loginDetails.getName(), true); //appends file (change to 'false' to overwrite file)
            fileEdit.write(accountDetails);
            fileEdit.close();
            System.out.println("Successfully registered into the library.");
        } catch (IOException e) {
            System.out.println("An error has occurred");
        }

    }


    public static void deleteFile(File fileName) {
        if (fileName.delete()) {
            System.out.println("The file has been reset.");
        }
    }

    //checks if the user has an account and signs them in
    private static boolean loginAccount() {
        boolean accountExists = false;
        boolean isAdmin = false;
        while (!accountExists) {
            userEmail = input("What is your email").toLowerCase();
            String accountDetails = (userEmail+ "%" + input("What is your password"));
            //checks if the user is an admin (there is only one administrator account)
            if (accountDetails.equals("libraryadmin123@reigate.ac.uk%Admin1")) {
                isAdmin = true;
                break;
            }
            try {
                Scanner fileInput = new Scanner(loginDetails); //searches file line by line for email and password match
                while (fileInput.hasNextLine()) {
                    String data = fileInput.nextLine();
                    if (data.equals(accountDetails)) {
                        System.out.println("You have successfully been logged in.");
                        accountExists = true;
                    }
                }
            } catch (IOException e) {
                System.out.println("An error has occurred");
            }
            if (!accountExists) {
                System.out.println("Either the email or password is incorrect.");
            }
        }

        return isAdmin;
    }

    private static void registerAccount() {
        String accountDetails;
        String username;
        //ensures the username isn't a previously existing one so there isn't any clashing
        while (true) {
            username = input("Type in your email.");
            if (username.contains("@") && username.contains(".com")) {
                break;
            } else if (username.contains("@") && username.contains(".uk")) {
                break;
            } else {
                System.out.println("This is not a valid email address.");
            }
        }
        try {
            Scanner fileInput = new Scanner(loginDetails);
            while (fileInput.hasNextLine()) {
                String data = fileInput.nextLine();
                String fileUsername = data.substring(0, username.length() - 1);
                if (fileUsername.equals(username)) {
                    System.out.println("This email is already in use." + "\nTry another email or check the spelling.");
                }
            }
        } catch (IOException e) {
            System.out.println("An error has occurred");
        }
        String password = passwordSecurity();
        accountDetails = (username.toLowerCase() + "%" + password + "\n");// email addresses aren't case sensitive
        appendLoginFile(accountDetails);
    }

    private static String passwordSecurity() {
        String password;
        while (true) {
            boolean containsCapital = false;
            boolean containsNumbers = false;
            password = input("Type a password.");
            for (int i = 0; i < password.length(); i++) {
                char charAti = password.charAt(i);
                if (Character.isUpperCase(charAti)) {
                    containsCapital = true;
                }
                if (Character.isDigit(charAti)) {
                    containsNumbers = true;
                }
            }
            if (!(password.length() < 8) && containsCapital && containsNumbers) {
                break;
            } else {
                System.out.println("The password needs to contain at least one number and at least one capital letter.");
            }
        }
        return password;
    }
}