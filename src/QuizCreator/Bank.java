package QuizCreator;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class Bank implements Serializable {
	//Finals
	public static final int MAX_NUMBER_OF_QUESTIONS = 100;
		
	// Members
	private String bankName;
	private int currentNumberOfQuestions;
	private Question[] allQuestions;

	
	//Methods
	//Constructors
	public Bank() {
		//Create new bank
		setBankName("");
		Question.resetCounter();
		currentNumberOfQuestions = 0;
		allQuestions = new Question[MAX_NUMBER_OF_QUESTIONS];
	}

	public Bank(String bankName) {
		//Load bank
		setBankName(bankName);
		Question.resetCounter();
		currentNumberOfQuestions = 0;
		allQuestions = new Question[MAX_NUMBER_OF_QUESTIONS];
	}
	
	//Methods
	
	//GETTERS
	public String getBankName() { // set/get name
		return bankName;
	}
	
	public int getCurrentNumberOfQuestions() {
		return currentNumberOfQuestions;
	}
	
	public Question[] getQuestions() {
		return allQuestions;
	}
	
	// Setters
	public void setBankName(String name) { 
		bankName = name;
	}


	//Functions
	
	public void addOpenQuestion(String text, Question.Difficulity difficulity){
		allQuestions[currentNumberOfQuestions++] = new OpenQuestion(text, difficulity);
	}
	
	public void addClosedQuestion(String text, Question.Difficulity difficulity){
		allQuestions[currentNumberOfQuestions++] = new ClosedQuestion(text, difficulity);
	}
	
	public Question getQuestionByID(int id) {
		/*
		 * This function compares by ID of a question and returns the question.
		 */
		int temp = -1;
		for (int i = 0; i < currentNumberOfQuestions; i++) {
			if(allQuestions[i].getQuestionID() == id) {
				temp = i;
				break;
			}
		} 
		return allQuestions[temp];
	}

	public void removeQuestionByID(int id) {
		if (currentNumberOfQuestions == id) { // Last question in allQuestion[]
			allQuestions[id - 1] = null;
		}else { //Replaces last in allQuestion[] with the one we want to remove
			allQuestions[id - 1] = allQuestions[currentNumberOfQuestions - 1];
			allQuestions[currentNumberOfQuestions - 1] = null;
			allQuestions[id - 1].setQuestionID(id);
		}
		//Reduce counter and current number of questions
		currentNumberOfQuestions--;
		Question.reduceCount();
	}
	
	public boolean canAddQuestion() {
		return (currentNumberOfQuestions < MAX_NUMBER_OF_QUESTIONS) ? true : false;
	}
	
	public boolean isExist(int id) {
		for (int i = 0; i < currentNumberOfQuestions; i++) {
			if (allQuestions[i].getQuestionID() == id) {
				return true;
			}
		}
		return false;
	}
	
	public void saveBank() throws FileNotFoundException, IOException {
		ObjectOutputStream outBank = new ObjectOutputStream(new FileOutputStream(getBankName() + ".dat"));
		outBank.writeObject(this);
		outBank.close();
	}

	@Override
	public String toString() {
		String message = "Bank: " + bankName + ".\nCurrent number of Questions: " + currentNumberOfQuestions + ".\n";
		for (int i = 0; i < currentNumberOfQuestions; i++) {
			message += allQuestions[i].toString();
			message += "--------------\n";
		}
		return message;
	}
}
