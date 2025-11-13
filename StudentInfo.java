public class Studentinfo {


String name; int age;
char grade;
String feeStatus;


public static void main(String[] args) { Studentinfo s = new Studentinfo(); s.name = "Devaj";
s.age = 18; s.grade = 'A';
s.feeStatus = "Paid";


System.out.println("Student Information:"); System.out.println("	");
System.out.println("Name: " + s.name); System.out.println("Age: " + s.age); System.out.println("Grade: " + s.grade);
System.out.println("Fee Status: " + s.feeStatus);
}
}
