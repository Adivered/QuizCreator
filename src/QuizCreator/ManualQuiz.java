package QuizCreator;

import java.io.IOException;

public class ManualQuiz extends Quiz implements Quizzable {
	// Private Members
	private Bank bank;
	private int selection;
	
	public ManualQuiz(String quizName, int numberOfQuestions) throws QuizNumberOfQuestionsOutOfRange {
		super(quizName, numberOfQuestions);
	} 

	@Override
	public void createQuiz(Bank bank) {
		/*
		 * IN: bank Type: BANK
		 * OUT: New Quiz in saved in TXT file
		 * This function creates a new quiz, using a do-while menu, that allows user to add questions and answers
		 */
		this.bank = bank; // Reassign the bank global variable so it can be accessed through the global scope
		generateQuiz(); 
		 
	}
	
	public void addQuestionFromBank() throws QuestionWithLessThanThreeAnswers{
		/*
		 * Adding question to a quiz from bank
		 */
		if (this.isFull()) { // Quiz full
			System.out.println("Quiz is full.");
			return;
		} else if (bank.getCurrentNumberOfQuestions() == 0) { // Bank empty
			System.out.println("Question Bank is Empty, please add questions to bank first.");
			return;
		}
		// ------------------
		if (this.canAddQuestions()) { // Can add
			System.out.println(bank.toString()); // Print all available questions
			int id = InputReader.readInt("Enter Question ID:\n");
			if (bank.isExist(id)) { // Making sure question exists in bank
				this.addQuestion(bank.getQuestionByID(id));
				System.out.println("Question has been added to quiz!");
			} else {
				System.out.println("Question ID doesn't exist.");
			}
		} else { // Quiz has reached it maximum number of questions, maybe user wants more questions?
			int decision = InputReader.readInt("You have limited the quiz to " + this.getTotalNumberOfQuestions()
					+ ".\nDo you want to add more questions?\n1. Yes\n2. No\n");
			if (decision == 1) {
				increaseQuizNumberOfQuestions();
			}
		}
	}

	public void increaseQuizNumberOfQuestions() {
		/*
		 * Increases number of questions in quiz
		 */
		int available = this.getMaxNumberOfQuestions() - this.getTotalNumberOfQuestions();
		int num = InputReader
				.readInt("You can add up to " + available + " more questions.\nHow many do you want to add?\n")
				+ this.getTotalNumberOfQuestions();
		if (this.setQuizNumberOfQuestions(num)) {
			System.out.println("Quiz total number of questions had been updated to " + num);
		} else {
			System.out.println("Total number of questions exceeded, please enter a proper number.");
		}
	}
	
	public void removeQuestionFromQuiz(int id){
		/*
		 * Removes a question from Quiz by ID
		 */
		if (!this.isExist(id)) {
			System.out.println("Question ID doesn't exist");
			return;
		}
		this.removeQuestionByID(id);
		System.out.println("Question has been removed from Quiz.");
	}
	
	public void generateQuiz() {
		final int ADD_NEW_QUESTION = 1;
		final int REMOVE_QUESTION = 2;
		final int VIEW_QUIZ = 3;
		final int SAVE_QUIZ = 4;
		final int EXIT = 0;
		do {
			printManualQuiz();
			selection = InputReader.readInt("");
			switch (selection) {
				case ADD_NEW_QUESTION:
					try{ 
						addQuestionFromBank();
					} catch (QuestionWithLessThanThreeAnswers e) {
						System.out.println(e.getMessage());
					}
					InputReader.pressEnterToContinue();
					break;

				case REMOVE_QUESTION:
					int id = InputReader.readInt("Which question do you want to remove? (By ID)\n");
					removeQuestionFromQuiz(id);
					InputReader.pressEnterToContinue();
					break;

				case VIEW_QUIZ:
					System.out.println(this.toString());
					InputReader.pressEnterToContinue();
					break;

				case SAVE_QUIZ:
					this.addDefultAnswersToQuestions();
					try {
						this.saveQuiz();
						System.out.println("Quiz have been saved to PC");
					} catch (IOException e) {
						System.out.println("Couldn't save quiz");
					}
					InputReader.pressEnterToContinue();
					break;

				case EXIT:
					if (!InputReader.readBoolean("Are you sure you want to exit? (Any unsaved questions will be removed)\nWrite True or False\n")) {
						selection = -1;
						break;
					}
					break;

				default:
					System.out.println("No such option in menu");
					break;
			}

		} while (selection != EXIT);
	}
	
	
	public void printManualQuiz() {
		System.out.println("---CREATE MANUAL QUIZ MENU---");
		System.out.println("Please select one of the following:");
		System.out.println("1. Add New Question From Bank");
		System.out.println("2. Remove Question");
		System.out.println("3. View all questions");
		System.out.println("4. Save quiz to file");
		System.out.println("0. Exit");
	}

}
