//Program : Write a Java program to check if a character is a vowel or consonant.@ Devaj TN

import java.util.Scanner;
class VowelCheck {
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        System.out.print("Enter a character: ");
        char ch = input.next().charAt(0);
        if(ch == 'a' || ch == 'e' || ch == 'i' || ch == 'o' || ch == 'u' ||
           ch == 'A' || ch == 'E' || ch == 'I' || ch == 'O' || ch == 'U') {
            System.out.println("It is a vowel.");
        } else {
            System.out.println("It is a consonant.");
        }
        input.close();
    }
}
