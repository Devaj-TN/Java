//Program : Write a Java program to find the largest of three numbers  
//@ Devaj TN

import java.util.Scanner;
class LargestOfThree {
public static void main(String[] args) {
Scanner input = new Scanner(System.in);
System.out.print("Enter the first number: ");
int a = input.nextInt();
System.out.print("Enter the second number: ");
int b = input.nextInt();
System.out.print("Enter the third number: ");
int c = input.nextInt();
int largest;
if(a >= b && a >= c) {
    largest = a;
} else if(b >= a && b >= c) {
    largest = b;
} else {
    largest = c;
}
System.out.println("The largest number is: " + largest);
input.close();
}
}
