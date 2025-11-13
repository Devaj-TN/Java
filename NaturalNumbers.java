//Program : Write a Java program to print all natural numbers from 1 to N using a loop.  @ Devaj TN

import java.util.Scanner;
class NaturalNumbers {
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        System.out.print("Enter N: ");
        int n = input.nextInt();
        for(int i = 1; i <= n; i++) {
            System.out.println(i);
        }
        input.close();
    }
}
