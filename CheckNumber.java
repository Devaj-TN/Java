//Program : Write a Java program to check whether a number is positive, negative, or zero. @ Devaj TN

import java.util.Scanner;
class CheckNumber {
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        System.out.print("Enter a number: ");
        int num = input.nextInt();
        if(num > 0) {
            System.out.println("The number is positive.");
        } else if(num < 0) {
            System.out.println("The number is negative.");
        } else {
            System.out.println("The number is zero.");
        }
        input.close();
    }
}
