package com.company.libraryFunctions;

import java.util.Scanner;

public class GetInput {

    //method which takes input to avoid having to declare a new 'Scanner' object in each method
    public static String input(String prompt) {
        System.out.println(prompt);
        Scanner input = new Scanner(System.in);
        return input.nextLine();
    }
}
