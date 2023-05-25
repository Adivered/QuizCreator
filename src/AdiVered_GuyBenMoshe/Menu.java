package AdiVered_GuyBenMoshe;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;

public class Menu {
	// Finals
	/*
	 * We have 3 different menus, so we use constants to differ the menu stages
	 */
	private final int MAIN = -1;
	private final int BANK = 1, ADD_NEW_QUESTION = 1, EDIT_QUESTION_TEXT = 1;
	private final int LOAD_BANK = 2, REMOVE_QUESTION = 2, ADD_ANSWER = 2;
	private final int CREATE_NEW_QUIZ = 3, EDIT_QUESTION_IN_BANK = 3, EDIT_ANSWER = 3;
	private final int EDIT_QUESTION = 4, VIEW_BANK = 4, REMOVE_ANSWER = 4;
	private final int SAVE_BANK = 5;
	private final int EXIT = 0;

	// Static Members
	private Bank bank;

	// Members
	private int selection;
	private int currentMenuStage;

	// Methods
	public void runMainMenu() throws IOException {
		firstPrint();
		do {
			currentMenuStage = MAIN;
			promptByStage();
			currentMenuStage = InputReader.readInt("Enter your selection:\n");
			switch (currentMenuStage) {
				case BANK:
					createBank();
					handleBankSelection();
					break;

				case LOAD_BANK:
					try{
						loadBank();
						InputReader.pressEnterToContinue();
						handleBankSelection();
					} catch(FileNotFoundException e) { // No Such File
						System.out.println("Please make sure such bank exists");
						InputReader.pressEnterToContinue();
					} catch (ClassNotFoundException e) {
						System.out.println(e.getMessage());
						InputReader.pressEnterToContinue();
					}
					break;

				case CREATE_NEW_QUIZ:
					if (bank != null) { // Bank exists
							createNewQuiz();
					} else { // Bank doesn't exist
						System.out.println("Please Create or Load Question Bank first");
						InputReader.pressEnterToContinue();
					}
					break;

				default:
					System.out.println("No such option");
					InputReader.pressEnterToContinue();
					break;

				case EXIT:
					System.out.println("GOODBYE!");
					InputReader.closeScanner();
					break;
			}

		} while (currentMenuStage != 0);

	}

	// Bank Functions
	public void createBank() {
		/*
		 * Creates a new bank
		 */
		String bankName = InputReader.readString("Enter Bank name:");
		bank = new Bank(bankName);
	}
	
	public void loadBank() throws FileNotFoundException, IOException, ClassNotFoundException {
		/*
		 * This function load Bank from .dat file
		 */
		String bankName = InputReader.readString("Enter Bank Name: ") + ".dat";
		ObjectInputStream inBank = new ObjectInputStream(new FileInputStream(bankName));
		bank  = (Bank)inBank.readObject();
		inBank.close();
		System.out.println(bankName + " has been loaded successfully!");
	}
	
	public void createQuestion() {
		/*
		 * Add new question to bank
		 */
		if (!bank.canAddQuestion()) { // Bank is full
			System.out.println("Question Bank is Full");
			return;
		}
		// Read necessary inputs for both type of questions
		String type = InputReader.readType("Enter Type of Question: ('OPEN' or 'CLOSED')");
		String questionText = InputReader.readString("Insert a question:");
		Question.Difficulity diff = InputReader.readDifficulty("Enter Question Difficulity ('EASY', 'MEDIUM' or 'HARD')");
		if (type.equals("OPEN")) {
			addOpenQuestion(questionText, diff);
		} else {
			addClosedQuestion(questionText, diff);
		}
		System.out.println("Question has been added.");
	}

	public void addOpenQuestion(String questionText, Question.Difficulity difficulty) {
		bank.addOpenQuestion(questionText, difficulty);
		addAnswer(bank.getQuestions()[bank.getCurrentNumberOfQuestions() - 1], 0);
	}

	public void addClosedQuestion(String questionText, Question.Difficulity difficulty) {
		bank.addClosedQuestion(questionText, difficulty);
		int numberOfAnswers;
		while (true) { // Validate number of answers between 1 : MAX
			numberOfAnswers = InputReader.readInt("How many answers? (Max: " + ClosedQuestion.getMaxNumberOfAnswers() + ")\n");
			if (numberOfAnswers > 0 && numberOfAnswers <= ClosedQuestion.getMaxNumberOfAnswers()) {
				break;
			}
			System.out.println("Can't add answers (Max: " + ClosedQuestion.getMaxNumberOfAnswers() + ")");
		}
		addAnswer(bank.getQuestions()[bank.getCurrentNumberOfQuestions() - 1], numberOfAnswers);
	}

	public void addAnswer(Question question, int numberOfAnswersToAdd) {
		/*
		 * Adds a new answer depends on Question instance
		 */
		if (question instanceof ClosedQuestion) { // Question is a Closed Question
			addAnswersToClosedQuestion((ClosedQuestion) question, numberOfAnswersToAdd);
		} else if (question instanceof OpenQuestion) { // Question is an Open Question
			addAnswerToOpenQuestion((OpenQuestion) question);
		}
	}

	public static void addAnswerToOpenQuestion(OpenQuestion openQuestion) {
		String answer = InputReader.readString("Please enter answer:");
		openQuestion.addAnswer(answer);
	}

	public static void addAnswersToClosedQuestion(ClosedQuestion closedQuestion, int numberOfAnswersToAdd) {
		if (closedQuestion.isFull()) { // Can't add answers
			System.out.println("Answer has reached it maximum number of questions");
			return;
		}
		int currentNumberOfAnswers = closedQuestion.getCurrentNumberOfAnswers();
		int maxNumberOfAnswers = ClosedQuestion.getMaxNumberOfAnswers(); // Static method
		int totalNumberOfAnswers = currentNumberOfAnswers + numberOfAnswersToAdd;
		while (currentNumberOfAnswers < totalNumberOfAnswers) { // Add answers
			if (totalNumberOfAnswers > maxNumberOfAnswers) {
				System.out.println("Too many answers (MAX 8)");
				return;
			}
			String answer = InputReader.readString("Please enter answer #" + (currentNumberOfAnswers + 1) + ":");
			boolean isCorrect = InputReader.readBoolean("Please enter if correct or not (true/false):\n");
			closedQuestion.addAnswer(answer, isCorrect);
			currentNumberOfAnswers++;
		}
	}

	public void editQuestion() {
		/*
		 * Edit question in bank by question ID
		 */
		int id = InputReader.readInt("Which question do you want to edit? (By ID)\n");
		if (bank.isExist(id)) {
			currentMenuStage = EDIT_QUESTION;
			Question question = bank.getQuestionByID(id);
			handleEditQuestion(question);
		} else {
			System.out.println("Question ID doesn't exist");
			selection = BANK;
			InputReader.pressEnterToContinue();
		}
	}

	public void editAnswer(Question question) {
		/*
		 * Edit answer in Question question
		 */
		if (question instanceof ClosedQuestion) { // Question is a Closed Question
			editClosedQuestionAnswer((ClosedQuestion) question);
		} else if (question instanceof OpenQuestion) { // Question is an Open Question
			editOpenQuestionAnswer((OpenQuestion) question);
		}
	}

	public void editOpenQuestionAnswer(OpenQuestion openQuestion) {
		String newAnswer = InputReader.readString("New Answer:");
		openQuestion.getAnswer().setAnswer(newAnswer);
	}

	public void editClosedQuestionAnswer(ClosedQuestion closedQuestion) {
		int toEdit = InputReader.readInt("Which answer do you want to edit?\n");
		if (closedQuestion.isAnswerExist(toEdit)) {
			String newAnswer = InputReader.readString("New Answer:");
			boolean isCorrect = InputReader.readBoolean("True/False:\n");
			closedQuestion.getAnswerByID(toEdit).setAnswer(newAnswer);
			closedQuestion.getAnswerByID(toEdit).setIsCorrect(isCorrect);
			System.out.println("Answer has been changed");
		} else {
			selection = EDIT_QUESTION;
			System.out.println("Answer ID doesn't exist");
		}
	}

	public void removeAnswer(Question question) {
		/*
		 * Removes answer in Question question
		 */
		if (question instanceof ClosedQuestion) { // Handle Closed Question event
			int questionToRemove = InputReader.readInt("Which answer do you want to remove?\n");
			ClosedQuestion closedQuestion = (ClosedQuestion) question;
			if (closedQuestion.isAnswerExist(questionToRemove)) {
				closedQuestion.removeAnswerByID(questionToRemove);
				System.out.println("Answer has been removed");
			} else {
				selection = EDIT_QUESTION;
				System.out.println("Answer ID doesn't exist");
			}
		} else { // Remove answer for Open Question..
			System.out.println("Answer has been removed (Not Yet need to edit)");
		}

	}
	
	public void removeQuestionFromBank(int id) {
		if (!bank.isExist(id)) {
			System.out.println("Question ID doesn't exist");
			return;
		}
		bank.removeQuestionByID(id);
		System.out.println("Question has been removed from Bank.");
	}
	
	// QUIZ FUNCTIONS
	
	public void createNewQuiz() {
		if (InputReader.readQuizType().equals("MANUAL")){
			ManualQuiz manual = new ManualQuiz();
			manual.createQuiz(bank);
		} else {
			AutomaticQuiz auto = new AutomaticQuiz();
			auto.createQuiz(bank);
		}
	}

	// PRINTS FUNCTIONS
	public void firstPrint() {
		System.out.println("****************");
		System.out.println("**QUIZ BUILDER**");
		System.out.println("****************");
	}

	public void promptByStage() {
		/*
		 * In this function we print the info for each menu, using switch on
		 * currentMenuStage
		 */
		if (bank != null) {
			System.out.println("Current Bank Loaded: " + bank.getBankName());
			System.out.println("****************");
		} else {
			System.out.println("No Bank Loaded");
			System.out.println("****************");
		}
		switch (currentMenuStage) {
			case MAIN:
				System.out.println("Please select one of the following:");
				System.out.println("1. Create New Question Bank");
				System.out.println("2. Load Question Bank");
				System.out.println("3. Create New Quiz");
				System.out.println("0. Exit");
				break;
				
			case BANK, LOAD_BANK:
				System.out.println("---BANK MENU---");
				System.out.println("Please select one of the following:");
				System.out.println("1. Add New Question");
				System.out.println("2. Remove Question");
				System.out.println("3. Edit Question");
				System.out.println("4. View all questions");
				System.out.println("5. Save Question Bank to file");
				System.out.println("0. Exit");
				break;

			case EDIT_QUESTION:
				System.out.println("---EDIT QUESTION MENU---");
				System.out.println("Please select one of the following:");
				System.out.println("1. Edit Question Text");
				System.out.println("2. Add Answers");
				System.out.println("3. Edit Answers");
				System.out.println("4. Remove Answers");
				System.out.println("0. Exit");
				break;
		}
	}

	// SUB-MENUS HANDLERS

	public void handleBankSelection() {
		int id;
		do {
			promptByStage();
			selection = InputReader.readInt("");
			switch (selection) {
				case ADD_NEW_QUESTION:
					createQuestion();
					InputReader.pressEnterToContinue();
					break;

				case REMOVE_QUESTION:
					id = InputReader.readInt("Which question do you want to remove? (By ID)\n");
					removeQuestionFromBank(id);
					InputReader.pressEnterToContinue();
					break;

				case EDIT_QUESTION_IN_BANK:
					id = InputReader.readInt("Which question do you want to edit? (By ID)\n");
					if (bank.isExist(id)) {
						currentMenuStage = EDIT_QUESTION;
						Question question = bank.getQuestionByID(id);
						handleEditQuestion(question);
					} else {
						System.out.println("Question ID doesn't exist");
						selection = BANK;
						InputReader.pressEnterToContinue();
					}
					break;

				case VIEW_BANK:
					System.out.println(bank.toString());
					InputReader.pressEnterToContinue();
					break;

				case SAVE_BANK:
					try {
						bank.saveBank();
						System.out.println("Bank have been saved to PC");
					} catch (IOException e) {
						System.out.println("Couldn't save bank");
					}
					InputReader.pressEnterToContinue();
					break;

				case EXIT:
					if (InputReader.readBoolean("Are you sure you want to exit? (Any unsaved questions will be removed)\nWrite True or False\n")) {
						currentMenuStage = MAIN;
						break;
					}
					currentMenuStage = BANK;
					selection = BANK;
					break;
					
				default:
					System.out.println("No such option in menu");
					selection = BANK;
					break;
			}

		} while (selection != EXIT);
	}

	public void handleEditQuestion(Question question) {
		int select;
		do {
			System.out.println("****EDIT QUESTION SCREEN****");
			System.out.println(question.toString());
			promptByStage();
			select = InputReader.readInt("");
			switch (select) {
				case EDIT_QUESTION_TEXT:
					System.out.println("Current Question Text: " + question.getQuestionText());
					String replaceWith = InputReader.readString("Enter Text to Replace with:");
					question.setQuestionText(replaceWith);
					System.out.println("Question Text has been changed");
					InputReader.pressEnterToContinue();
					break;

				case ADD_ANSWER:
					if(question instanceof OpenQuestion) {
						System.out.println("Option is not available for Open Question");
						InputReader.pressEnterToContinue();
						break;
					}
					System.out.println(question.toString());
					int num = InputReader.readInt("How many answers to add?\n");
					addAnswer(question, num);
					InputReader.pressEnterToContinue();
					break;

				case EDIT_ANSWER:
					System.out.println(question.toString());
					editAnswer(question);
					InputReader.pressEnterToContinue();
					break;

				case REMOVE_ANSWER:
					if(question instanceof OpenQuestion) {
						System.out.println("Option is not available for Open Question");
						InputReader.pressEnterToContinue();
						break;
					}
					System.out.println(question.toString());
					removeAnswer(question);
					InputReader.pressEnterToContinue();
					break;

				case EXIT:
					currentMenuStage = BANK;
					selection = BANK;
					break;

				default:
					System.out.println("No such option in menu");
					currentMenuStage = EDIT_QUESTION;
					break;
			}

		} while (select != EXIT);
	}
	// ------------------------------\
}
