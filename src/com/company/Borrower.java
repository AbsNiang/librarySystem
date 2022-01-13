package com.company;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import static com.company.Login.userEmail;
import static com.company.Main.*;

import static com.company.Book.books;

public class Borrower {
    public static File borrowInfo = new File("borrowingInformation.txt");
    private static ArrayList<Borrower> borrowerList = new ArrayList<>();
    private static ArrayList<Book> borrowedBooks = new ArrayList<>();
    private String dateBorrowed; //  dd/mm/yy
    private String borrowerEmail;// will be taken and verified from the LoginDetails file in main

    public Borrower(String dateBorrowed, String borrowerEmail) {
        this.dateBorrowed = dateBorrowed;
        this.borrowerEmail = borrowerEmail;
    }

    public static void bookBorrow() {
        int noOfBorrows = borrowLimitChecker(userEmail);
        if (noOfBorrows == 3) {
            System.out.println("You have already reached the maximum number of books borrowed.");
            return;
        }
        Borrower currentBorrower = new Borrower(dateFormatter(), userEmail);
        try {
            System.out.println("__________________________________________________________________");
            int noOfBooks;
            int totalBooksLoaned;
            do {
                noOfBooks = Integer.parseInt(input("How many books will you borrow? (0-3) " +
                        "\nYou must not have over 3 books loaned at once"));
                totalBooksLoaned = noOfBooks + noOfBorrows;
                System.out.println("You will have " + totalBooksLoaned + " books loaned.");
            } while (totalBooksLoaned > 3);
            for (int i = 0; i < noOfBooks; i++) {
                String bookTitle = input("What is the name of the book?").toLowerCase();
                for (Book book : books) {
                    if (book.getTitle().equals(bookTitle)) {
                        borrowedBooks.add(book);
                        Book.remove(bookTitle);
                    }
                }
            }
            borrowerList.add(currentBorrower);
            appendBorrowInfo();
        } catch (Exception e) {
            System.out.println(e);
        }


    }

    private static String dateFormatter() {
        while (true) {
            String date = input("What is the date? (dd/mm/yyyy)");
            String[] dateSplit = date.split("/");
            int days = Integer.parseInt(dateSplit[0]);
            int months = Integer.parseInt(dateSplit[1]);
            int yrs = Integer.parseInt(dateSplit[2]);
            if (((days <= 31) && (days > 0)) && ((months <= 12) && (months > 0)) && ((yrs > 2021))) {
                return date;
            }
        }
    }

    //searches the text file to see if the email holder has any books currently. If so, it makes sure it is < 3
    private static int borrowLimitChecker(String email) {
        int count = 0;
        for (Borrower borrower : borrowerList) {
            if ((borrower.getBorrowerEmail()).equalsIgnoreCase(email)) {
                count++;
            }
        }

        return count;
    }

    // 'returns' a book to the bookInformation file and removes that book from the current borrow list
    public static void bookReturn() {
        try {
            boolean validMember = false;
            boolean validBook = false;
            while (!validBook) {
                String borrowedBookName = input("What is the book title?").toLowerCase();
                for (Borrower borrower : borrowerList) {
                    if (borrower.getBorrowerEmail().equals(userEmail)) {
                        validMember = true;
                        Book currentBook = borrowedBooks.get(borrowerList.indexOf(borrower));
                        if (currentBook.getTitle().contains(borrowedBookName)) {
                            validBook = true;
                            books.add(currentBook);
                            borrowedBooks.remove(currentBook);
                            borrowerList.remove(borrower);
                            System.out.println("Successfully returned book.");
                        }
                    }
                }
                if (!validMember) {
                    System.out.println("You do not currently have any books borrowed.");
                }
                if (!validBook) {
                    System.out.println("You have not typed a valid book.");
                    if (!(input("Would you like to continue searching for the book").toLowerCase().contains("y"))) {
                        validBook = true;
                    }
                }
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        Book.appendFile();
        appendBorrowInfo();
    }

    public static void borrowInfoInitializer() {
        try {
            createBorrowInfoFile();
            Scanner fileInput = new Scanner(borrowInfo);
            while (fileInput.hasNextLine()) {
                String data = fileInput.nextLine().toLowerCase();
                String[] borrowInformation = data.split("%"); //There are four different types of information we have stored about the book
                Borrower currentBorrower = new Borrower(borrowInformation[0], borrowInformation[1]);
                String[] bookInfo = borrowInformation[2].split(", ");
                Book currentBook = new Book(bookInfo[0], Integer.parseInt(bookInfo[1]), bookInfo[2], bookInfo[3]);
                borrowedBooks.add(currentBook);
                borrowerList.add(currentBorrower);
            }
        } catch (Exception e) {
            System.out.println("An error has occurred");
        }
    }

    public static void createBorrowInfoFile() {
        try {
            if (borrowInfo.createNewFile()) {
                System.out.println("Setting up borrowing system.");
            }
        } catch (IOException e) {
            System.out.println("An error has occurred.");
        }
    }

    //appends the borrowing information file
    private static void appendBorrowInfo() {
        try {
            FileWriter fileEdit = new FileWriter(borrowInfo.getName(), false); //appends file (change to 'false' to overwrite file)
            StringBuilder borrowerDetails = new StringBuilder();
            for (int i = 0; i < borrowedBooks.size(); i++) {
                borrowerDetails.append(borrowerList.get(i)).append(borrowedBooks.get(i)).append("\n");
            }
            fileEdit.write(borrowerDetails.toString());
            fileEdit.close();
            System.out.println("Successfully altered borrower list.");
        } catch (IOException e) {
            System.out.println("An error has occurred");
        }
    }

    public static void borrowFilePrint() {
        if (input("Would you like to see the list of current book loans?").toLowerCase().contains("y")) {
            for (int i = 0; i < borrowerList.size(); i++) {
                System.out.println(borrowerList.get(i).fancyToString() + "\nBook Borrowed: \n" + borrowedBooks.get(i).fancyToString());
            }
        }
    }

    private String getBorrowerEmail() {
        return borrowerEmail;
    }


    @Override
    public String toString() {
        return dateBorrowed + "%" + borrowerEmail + "%";
    }

    private String fancyToString() {
        return "Date Borrowed = " + dateBorrowed
                + "\nBorrower Email = " + borrowerEmail;
    }
}
