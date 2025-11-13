class Car{
	String Name ;
	String Company;
	String Model;
	String Colour;
	int Price;
}
public class CarInfo{
	public static void main(String a[]){
		Car c=new Car();
		c.Name="Xelec";
		c.Company="Scarborgini";
		c.Model="XZV9029";
		c.Colour="Black";
		c.Price=12000000;
		System.out.println("Name:"+c.Name);
		System.out.println("Salary:"+c.Company);
		System.out.println("Experience:"+c.Model);
		System.out.println("Salary:"+c.Colour);
		System.out.println("Experience:"+c.Price);
	}
}