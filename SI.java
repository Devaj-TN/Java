import java.util.Scanner;

class SI{
    public static void main(String[] args){
        Scanner s = new Scanner(System.in); 
        System.out.println("Enter Principal: ");
        int p = s.nextInt();
        System.out.println("Enter Rate: ");
        int r = s.nextInt();
        System.out.println("Enter Time: ");
        int t = s.nextInt(); 

        int si = (p * r * t)/100; 
        System.out.println("Simple Interest: " +si);
    } 
}