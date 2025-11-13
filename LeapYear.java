//Program : Write a Java program to check whether a year is a leap year or not.  @ Devaj TN

import java.util.Scanner;
class LeapYear {
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        System.out.print("Enter a year: ");
        int year = input.nextInt();
        if((year % 4 == 0 && year % 100 != 0) || (year % 400 == 0)) {
            System.out.println("It is a leap year.");
        } else {
            System.out.println("It is not a leap year.");
        }
        input.close();
    }
}
