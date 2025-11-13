class Book
{
	String Title;
	String Author;
	String Genre;
	double Price;
}
public class Bookinfo
{
	public static void main(String a[]){
		Book e = new Book();
		e.Title = "Ruskin Bond";
		e.Author =  "Ghost trouble";
		e.Genre = "Myth";
		e.Price = 1000;
		System.out.println("Book Title: " +e.Title);
		System.out.println("Author: " +e.Author);
		System.out.println("Genre: " +e.Genre);
		System.out.println("price: " +e.Price);
}
}