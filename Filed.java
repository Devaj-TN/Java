import java.io.*; 
import java.util.*; 

class Filed{
	public static void main(String args[]){
		Scanner io = new Scanner(System.in);
			try{
				System.out.println("Enter the file name: "); 	
				String filename = io.nextLine(); 
			
				while (true) {
					System.out.println("\n--- File Editor Menu ---"); 
					System.out.println("1. Append text"); 
					System.out.println("2. Overwrite file"); 
					System.out.println("3. Delete specific text"); 
					System.out.println("4. View file content"); 
					System.out.println("5. Exit"); 
					System.out.println("Choose an option: "); 
	
					int choice = io.nextInt(); 
					io.nextLine(); 
		
					if (choice == 1){
						System.out.println("Enter text to append:"); 
						String data = io.nextLine(); 
						FileWriter fw = new FileWriter(filename, true); 
						fw.write(data + "\n"); 
						fw.close(); 
						System.out.println(" Text appended."); 
					}

					else if (choice == 2){
						System.out.println("Enter new content (old data will be erased):"); 
						String data = io.nextLine(); 
						FileWriter fw = new FileWriter(filename, false);
						fw.write(data + "\n"); 
						fw.close(); 
						System.out.println("File overwritten."); 
					} 
			
					else if (choice == 3){
						System.out.println("Enter text to delete:"); 
						String toDelete = io.nextLine(); 
						
						FileReader fr = new FileReader(filename);
						StringBuilder sb = new StringBuilder(); 
						int ch; 
						while ((ch = fr.read()) != -1) {
							sb.append((char) ch); 
						}
						fr.close(); 
						
						String newContent = sb.toString().replace(toDelete, ""); 
		
						FileWriter fw = new FileWriter(filename, false); 
						fw.write(newContent); 
						fw.close(); 
						System.out.println("Text deleted (if found)."); 
					} 
		
					else if (choice == 4){
						File f = new File(filename); 
						if(!f.exists()) {
							System.out.println("File not found."); 
					} else {
						FileReader fr = new FileReader(filename); 
						int ch; 
						String content= ""; 
						while((ch = fr.read()) != -1)
							content += (char) ch; 
						fr.close(); 
						System.out.println("\n--- File Content ---"); 
						System.out.println(content); 
						System.out.println("-------------"); 
					    } 

					} else if (choice == 5){
						System.out.println("Exiting Program..."); 
						break; 
					} else {
					    	System.out.println("Invalid choice, try again."); 
					}
				      }
				} catch (IOException e) {
					System.out.println("Error: " + e); 
				}
			} 
}