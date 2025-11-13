//Program : Write a Java program to find the sum of digits of a number. @ Devaj TN

import java.util.Scanner;
class SumOfDigits {
public static void main(String[] args) {
Scanner input = new Scanner(System.in);
System.out.print("Enter a number: ");
int num = input.nextInt();
int sum = 0;
while(num != 0) {
    sum = sum + num % 10;
    num = num / 10;
}
System.out.println("Sum of digits is: " + sum);
input.close();
}
}
