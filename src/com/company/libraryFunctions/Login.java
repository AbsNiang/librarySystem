package com.company.libraryFunctions;

import java.io.IOException;
import java.util.Scanner;

import static com.company.libraryFunctions.GetInput.input;
import static com.company.libraryFunctions.FileHandling.loginDetails;

public class Login {
    public static String userEmail;

    //method which allows the user to either register a new account or log into a previously existing account
    public static boolean signUpMenu() {
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

    //checks if the user has an account and signs them in
    public static boolean loginAccount() {
        boolean accountExists = false;
        boolean isAdmin = false;
        while (!accountExists) {
            userEmail = input("What is your email").toLowerCase();
            String accountDetails = (userEmail + ", " + input("What is your password"));
            //checks if the user is an admin (there is only one administrator account)
            if (accountDetails.equals("libraryadmin123@reigate.ac.uk, Admin1")) {
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
        accountDetails = (username.toLowerCase() + ", " + password + "\n");// email addresses aren't case sensitive
        FileHandling.appendLoginFile(accountDetails);
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
