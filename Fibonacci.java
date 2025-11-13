//Program : Write a Java program to print Fibonacci series up to N terms.  @ Devaj TN

import java.util.Scanner;
class Fibonacci {
public static void main(String[] args) {
Scanner input = new Scanner(System.in);
System.out.print("Enter N: ");
int n = input.nextInt();
int a = 0, b = 1;
System.out.print("Fibonacci series: ");
for(int i = 1; i <= n; i++) {
    System.out.print(a + " ");
    int next = a + b;
    a = b;
    b = next;
}
input.close();
}
}
