//Program : Write a Java program to calculate the factorial of a number using a loop. @ Devaj TN

import java.util.Scanner;
class Factorial {
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        System.out.print("Enter a number: ");
        int num = input.nextInt();
        int fact = 1;
        for(int i = 1; i <= num; i++) {
            fact *= i;
        }
        System.out.println("Factorial is: " + fact);
        input.close();
    }
}
