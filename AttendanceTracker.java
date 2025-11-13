class Employee {
    // private fields (encapsulation)
    private int uniqueId;
    private String name;
    private String department;
    private int workingHours;

    // constructor
    public Employee(int uniqueId, String name, String department, int workingHours) {
        this.uniqueId = uniqueId;
        this.name = name;
        this.department = department;

        // validate working hours (not more than 24)
        if (workingHours >= 0 && workingHours <= 24) {
            this.workingHours = workingHours;
        } else {
            System.out.println("Invalid hours for " + name + ". Setting to 0.");
            this.workingHours = 0;
        }
    }

    // method to display employee details
    public void display() {
        System.out.println(uniqueId + " | " + name + " | " + department + " | Hours Worked: " + workingHours);
    }
}

// main class
public class AttendanceTracker {
    public static void main(String[] args) {
        Employee e1 = new Employee(101, "Alice", "HR", 8);
        Employee e2 = new Employee(102, "Bob", "IT", 25); // invalid hours
        Employee e3 = new Employee(103, "Charlie", "Finance", 6);

        e1.display();
        e2.display();
        e3.display();
    }
}