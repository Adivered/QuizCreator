package AdiVered_GuyBenMoshe;

import java.io.FileNotFoundException;
import java.io.IOException;

public class Bank {
	//Finals
	public static final int MAX_NUMBER_OF_QUESTIONS = 100;
	
	// Static Members
	private static int counter = 0;
	
	// Members
	private int bankID = 0;
	private String bankName;
	private int currentNumberOfQuestions;
	private Question[] allQuestions;

	
	//Methods
	//Constructors
	public Bank() {
		//Create new bank
		bankName = "";
		counter++;
		setBankID(counter);
		Question.resetCounter();
		currentNumberOfQuestions = 0;
		allQuestions = new Question[MAX_NUMBER_OF_QUESTIONS];
	}

	public Bank(String bankName) {
		//Load bank
		counter++;
		setBankID(counter);
		setBankName(bankName);
		Question.resetCounter();
		currentNumberOfQuestions = 0;
		allQuestions = new Question[MAX_NUMBER_OF_QUESTIONS];
	}

	//Methods
	public String getBankName() { // set/get name
		return bankName;
	}

	public void setBankName(String name) { 
		bankName = name;
	}

	public int getBankID() {
		return bankID;
	}
	
	public void setBankID(int counter) { // set/get ID
		bankID = counter;
	}

	public int getCurrentNumberOfQuestions() {
		return currentNumberOfQuestions;
	}
	
	public Question[] getQuestions() {
		return allQuestions;
	}
	
	//Functions
	
	public void addQuestion(String text){
		allQuestions[currentNumberOfQuestions++] = new Question(text);
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
		/*
		 * In this function we create new text file with fileHandler class with the
		 * bank name and then we add:
		 * Index 0 - bankID, Index 1 - bankName, Index 2 = Current number of questions in bank
		 * Index 3+ --> Questions and answers
		 */
		fileHandler bankToText = new fileHandler(bankName + ".txt");
		bankToText.addToFile(bankID + ",");
		bankToText.addToFile(bankName + ",");
		bankToText.addToFile(currentNumberOfQuestions + ",\n");
		for (Question question : allQuestions) {
			if (question == null) { 
				break; // No more questions can break
			}
			bankToText.addToFile(question.getQuestionID() + ",");
			bankToText.addToFile(question.getQuestionText() + ",");
			bankToText.addToFile(question.getCurrentNumberOfAnswers() + ",");
			for (Answer answer : question.getAnswers()) {
				if (answer == null) {
					break; // No more answers can break
				}
				bankToText.addToFile(answer.getAnswer() + ",");
				bankToText.addToFile(answer.getIsCorrect() + ",");
			}
			bankToText.addToFile("\n");
		}
		bankToText.saveFile();
	}

	@Override
	public String toString() {
		String message = "Bank: " +bankName + ".\nCurrent number of Questions: " + currentNumberOfQuestions + ".\n";
		for (int i = 0; i < currentNumberOfQuestions; i++) {
			message += allQuestions[i].toString();
		}
		return message;
	}
}
