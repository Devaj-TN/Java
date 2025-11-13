//Program : Write a Java program to reverse a number. @ Devaj TN

import java.util.Scanner;
class ReverseNumber {
public static void main(String[] args) {
Scanner input = new Scanner(System.in);
System.out.print("Enter a number: ");
int num = input.nextInt();
int reversed = 0;
while(num != 0) {
    int digit = num % 10;
    reversed = reversed * 10 + digit;
    num = num / 10;
}
System.out.println("Reversed number is: " + reversed);
input.close();
}
}
