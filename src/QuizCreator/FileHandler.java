package QuizCreator;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

public class FileHandler {
	// Members
	private File file;
	private Scanner scan;
	private String fileName;
	private StringBuilder content;
	
	// Constructor
	public FileHandler(String fileName) throws IOException {
		this.fileName = fileName;
		this.file = openFile(fileName);
		this.content = new StringBuilder();
	}
	
	// Methods
	// Getters
	
	public String getFileName() {
		return fileName;
	}
	
	// Setters
	
	public void setFileName(String name) {
		this.fileName = name;
	}
	
	// Functions
	
	public File openFile(String fileName) throws FileNotFoundException {
		return new File(fileName);
	}
	
	public void addToFile(String str) {
		content.append(str);
	}
	
	public void saveFile() throws IOException {
		/*
		 * This function writes StringBuilder content into file
		 */
		PrintWriter pw = new PrintWriter(file);
		pw.print(content);
		pw.close();
	}
	
	public StringBuilder readFile() throws FileNotFoundException {
		/*
		 * This function reads into content and returns it
		 */
		scan = new Scanner(file);
		while (scan.hasNextLine()) {
			content.append(scan.nextLine());
		}
		scan.close();
		return content;
	}

}
