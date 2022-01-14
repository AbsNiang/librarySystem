package com.company.objects;

public class Borrower {

    private String dateBorrowed; //  dd/mm/yy
    private String borrowerEmail;// will be taken and verified from the LoginDetails file in main

    public Borrower(String dateBorrowed, String borrowerEmail) {
        this.dateBorrowed = dateBorrowed;
        this.borrowerEmail = borrowerEmail;
    }

    public String getBorrowerEmail() {
        return borrowerEmail;
    }

    @Override
    public String toString() {
        return dateBorrowed + "%" + borrowerEmail + "%";
    }

    public String fancyToString() {
        return "Date Borrowed = " + dateBorrowed
                + "\nBorrower Email = " + borrowerEmail;
    }
}
