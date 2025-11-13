class Book {
    // private attributes
    private int id;
    private String title;
    private String author;
    private boolean available;

    // constructor
    public Book(int id, String title, String author) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.available = true; // book is available at first
    }

    // borrow by ID
    public void borrowBook(int id) {
        if (this.id == id && available) {
            available = false;
            System.out.println(title + " borrowed (by ID).");
        } else {
            System.out.println("Not available (by ID).");
        }
    }

    // borrow by Title
    public void borrowBook(String title) {
        if (this.title.equals(title) && available) {
            available = false;
            System.out.println(title + " borrowed (by Title).");
        } else {
            System.out.println("Not available (by Title).");
        }
    }

    // return by ID
    public void returnBook(int id) {
        if (this.id == id && !available) {
            available = true;
            System.out.println(title + " returned (by ID).");
        } else {
            System.out.println("Cannot return (by ID).");
        }
    }

    // return by Title
    public void returnBook(String title) {
        if (this.title.equals(title) && !available) {
            available = true;
            System.out.println(title + " returned (by Title).");
        } else {
            System.out.println("Cannot return (by Title).");
        }
    }

    // show details
    public void display() {
        System.out.println(id + " | " + title + " | " + author + " | Available: " + available);
    }
}

// main class
public class LibraryDemo {
    public static void main(String[] args) {
        Book b1 = new Book(1, "The Alchemist", "Paulo Coelho");
        Book b2 = new Book(2, "1984", "George Orwell");

        b1.display();
        b2.display();

        // borrow
        b1.borrowBook(1);
        b2.borrowBook("1984");

        b1.display();
        b2.display();

        // return
        b1.returnBook("The Alchemist");
        b2.returnBook(2);

        b1.display();
        b2.display();
    }
}