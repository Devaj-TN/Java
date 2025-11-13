import java.util.Scanner;
	class area
	{
		public static void main(String[] args) {
		System.out.println("Enter the radius");
		Scanner s = new Scanner(System.in);
		int r = s.nextInt();
		double c = 3.14 * (r*r); 
	
		System.out.println("area:" + c);
		}
	}

