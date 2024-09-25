package org.bharath.utils;

import java.util.Scanner;

public class CustomUserInput {
    public Scanner scanner = new Scanner(System.in);
    private CustomUserInput(){}
    private static boolean isObjectCreated_ = false;
    private static CustomUserInput customUserInput8_ = null;

    public static CustomUserInput getInstance(){
        if(isObjectCreated_){
            return customUserInput8_;
        }
        customUserInput8_ = new CustomUserInput();
        isObjectCreated_ = true;
        return customUserInput8_;
    }

    public int validateAndGetIntegerInput(int startRange, int endRange){
        int userChoice;
        boolean isFirstAttempt = true;
        while(true){
            System.out.println("Enter user input: ");
            while(!scanner.hasNextInt()){
                System.out.println("Enter user input: ");
                System.out.println("Entered input is invalid, Please enter the valid integer");
                scanner.next();
            }
            userChoice = scanner.nextInt();
            if(!(userChoice >= startRange && userChoice <= endRange)){
                System.out.println(String.format("Entered input is not within %d - %d range", startRange, endRange));
            }else{
                scanner.nextLine();
                break;
            }
        }
        return userChoice;
    }

    public long validateAndGetLongInput(long startRange, long endRange){
        long userChoice;
        boolean isFirstAttempt = true;
        while(true){
            System.out.println("Enter user input: ");
            while(!scanner.hasNextLong()){
                System.out.println("Enter user input: ");
                System.out.println("Entered input is invalid, Please enter the valid integer");
                scanner.next();
            }
            userChoice = scanner.nextLong();
            if(!(userChoice >= startRange && userChoice <= endRange)){
                System.out.println(String.format("Entered input is not within %d - %d range", startRange, endRange));
            }else{
                scanner.nextLine();
                break;
            }
        }
        return userChoice;
    }

    public double validateAndGetDoubleInput(double startRange, double endRange){
        double userChoice;
        boolean isFirstAttempt = true;
        while(true){
            System.out.println("Enter user input: ");
            while(!scanner.hasNextDouble()){
                System.out.println("Enter user input: ");
                System.out.println("Entered input is invalid, Please enter the valid integer");
                scanner.next();
            }
            userChoice = scanner.nextDouble();
            if(!(userChoice >= startRange && userChoice <= endRange)){
                System.out.println(String.format("Entered input is not within %f - %f range", startRange, endRange));
            }else{
                scanner.nextLine();
                break;
            }
        }
        return userChoice;
    }

    public void printOptionsMenu(String options){
        System.out.println(options);
    }

    public void printSeparatorLine() {
        for(int i = 0; i < 30; i++ )
            System.out.print("-");
        System.out.println();
    }
}
