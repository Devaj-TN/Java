import java.util.Scanner; 

class OddEven{
    public static void main(String[] args){
        Scanner s = new Scanner(System.in);
        System.out.println("Enter a number: ");
        int n = s.nextInt();
        
        if(n % 2 == 0){
        System.out.println("Even");
        }
        else{
        System.out.println("Odd");
        }
     } 
}
