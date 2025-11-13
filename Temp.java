import java.util.Scanner; 

class Temp{
    public static void main(String[] args){
        Scanner s = new Scanner(System.in);
        System.out.println("Enter the temperature in Celsius: ");
        int c = s.nextInt();
        int f = (c*(9/5)) + 32; 
        System.out.println("In Fahrenheit: " +f);
    }
}