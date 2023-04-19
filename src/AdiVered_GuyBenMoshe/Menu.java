package AdiVered_GuyBenMoshe;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

public class Menu {
	// Finals
	/*
	 * We have 3 different menus, so we use constants to differ the menu stages
	 */
	private final int MAIN = -1;
	private final int BANK = 1, ADD_NEW_QUESTION =1, EDIT_QUESTION_TEXT = 1;
	private final int LOAD_BANK = 2, REMOVE_QUESTION =2, ADD_ANSWER = 2;
	private final int CREATE_NEW_QUIZ = 3, VIEW_QUIZ = 3, EDIT_QUESTION_IN_BANK = 3, EDIT_ANSWER = 3;
	private final int EDIT_QUESTION = 4, SAVE_QUIZ = 4, VIEW_BANK = 4, REMOVE_ANSWER = 4;
	private final int SAVE_BANK = 5;
	private final int EXIT = 0;
	
	//Static Members
	private static Scanner scan = new Scanner(System.in);
	private Quiz quiz;
	private Bank bank;
	
	// Members
	private int selection;
	private int currentMenuStage = MAIN;
	
	// Methods
	public void runMainMenu() throws IOException {
		firstPrint();
		do {
			promptByStage();	
			currentMenuStage = readInt("Enter your selection:\n");
			switch (currentMenuStage) {
			case BANK:
				createBank();
				handleBankSelection();
				break;
				
			case LOAD_BANK:
				loadBank();
				pressEnterToContinue();
				if (bank != null) { // Bank loaded
					handleBankSelection();
				}else { // Bank not loaded
					currentMenuStage = MAIN;
				}
				break;
				
			case CREATE_NEW_QUIZ:
				if(bank != null) { // Bank exists
					createQuiz();
					handleQuizSelection();
				}else { // Bank doesn't exist
					System.out.println("Please create/load bank first");
					currentMenuStage = MAIN;
					pressEnterToContinue();
				}
				break;
			
			default:
				System.out.println("No such option in menu");
				currentMenuStage = MAIN;
				break;
				
			case EXIT:
				System.out.println("GOODBYE!");
				scan.close();
				break;
			}
			
		}while (currentMenuStage !=0);
		
	}
	
	// BANK Functions
		
	public void createBank() {
		/*
		 * Creates a new bank
		 */
		System.out.println("Let's create a new bank!\nPlease insert bank name: ");
		String bankName = scan.nextLine();
		bank = new Bank(bankName);	
	}
	
	public Bank loadBank() throws FileNotFoundException, IOException {
		/*
		 * This function loads bank from text file using fileHandler class
		 * We still have bankID and questionID for next steps of project
		 * Index 0 - bankID, Index 1 - bankName, Index 2 = Current number of questions in bank
		 * Index 3+ --> Questions and answers
		 */
		System.out.println("Please Enter Bank name: ");
		String bankName = scan.nextLine() + ".txt";
		String[] bankInfo = null;
		try {
			fileHandler file = new fileHandler(bankName);
			bankInfo = file.readFile().toString().split(",");
		} catch (FileNotFoundException e) {
			System.out.println("Please make sure such bank exists");
			return null;
		}
		int bankID = Integer.parseInt(bankInfo[0]);
		bankName = bankInfo[1];
		int startIndex = 3;
		int totalQuestion = Integer.parseInt(bankInfo[2]);
		bank = null;
		bank = new Bank(bankName);
		for (int i = 0; i < totalQuestion; i++) { // Iterate on questions
			int questionID = Integer.parseInt(bankInfo[startIndex++]);
			String question = bankInfo[startIndex++];
			int numberOfAnswers = Integer.parseInt(bankInfo[startIndex++]);
			bank.addQuestion(question);
			for (int j = 0; j < numberOfAnswers; j++) { // Iterate on answers
				String answer = bankInfo[startIndex++];
				boolean isCorrect = (bankInfo[startIndex++].equals("true")) ? true : false;
				bank.getQuestionByID(i + 1).addAnswer(answer, isCorrect);
			}
		}
		System.out.println(bankName + " has been loaded successfully!");
		return bank;
	}

	
	public void createQuestion() {
		/*
		 * Add new question to bank
		 */
		if (bank.canAddQuestion()) { //Bank isn't full
			System.out.println("Please insert a question: ");
			String questionText = scan.nextLine();
			int numberOfAnswers = readInt("How many answers? (MAX 10)\n");
			bank.addQuestion(questionText);
			if (numberOfAnswers > 0) {
				addAnswer(bank.getQuestions()[bank.getCurrentNumberOfQuestions()-1], numberOfAnswers);
			}else {
				selection = BANK;
			}
		}else {
			System.out.println("Question Bank is Full");
		}
	}
	
	public void editQuestion() {
		/*
		 * Edit question in bank by question ID
		 */
		int id = readInt("Which question do you want to edit? (By ID)\n");
		if (bank.isExist(id)) {
			currentMenuStage = EDIT_QUESTION;
			Question question = bank.getQuestionByID(id);
			handleEditQuestion(question);
		}else {
			System.out.println("Question ID doesn't exist");
			selection = BANK;
			pressEnterToContinue();
		}
	}
	
	public void addAnswer(Question question, int numberOfAnswersToAdd) {
		/*
		 * Adds a new answer
		 */
		int currentNumberOfAnswers = question.getCurrentNumberOfAnswers();
		int temp = currentNumberOfAnswers;
		if (question.isFull()) { // Can't add answers
			System.out.println("Answer has reached it maximum number of questions");
			return;
		}
		while (currentNumberOfAnswers < temp + numberOfAnswersToAdd) { // Add answers
			if(currentNumberOfAnswers + numberOfAnswersToAdd <= question.getMaxNumberOfAnswers()) {
				System.out.println("Please enter answer #" +(currentNumberOfAnswers + 1)+":");
				String answer = scan.nextLine();
				boolean isCorrect = readBoolean("Please enter if correct or not (true/false):\n");
				question.addAnswer(answer, isCorrect);
				currentNumberOfAnswers++;
			}else { // Stop adding if reached limit 
				System.out.println("Too many answers (MAX 10)");
				return;
			}
		}
		System.out.println("Answers updated");
	}
	
	public void editAnswer (Question question) {
		/*
		 * Edit answer in Question question
		 */
		int toEdit = readInt("Which answer do you want to edit?\n");
		if (question.isAnswerExist(toEdit)) {
			System.out.println("New Answer: ");
			String newAnswer = scan.nextLine();
			boolean isCorrect = readBoolean("True/False:\n");
			question.getAnswerByID(toEdit).setAnswer(newAnswer);
			question.getAnswerByID(toEdit).setIsCorrect(isCorrect);
			System.out.println("Answer has been changed");
		}else {
			selection = EDIT_QUESTION;
			System.out.println("Answer ID doesn't exist");
		}
	}
	
	public void removeAnswer(Question question) {
		/*
		 * Removes answer in Question question
		 */
		int questionToRemove = readInt("Which answer do you want to remove?\n");
		if (question.isAnswerExist(questionToRemove)) {
			question.removeAnswerByID(questionToRemove);
			System.out.println("Answer has been removed");
		}else {
			selection = EDIT_QUESTION;
			System.out.println("Answer ID doesn't exist");
		}
	}
	
	// Quiz Functions
	
	public void createQuiz() {
		/*
		 * Creates new quiz, must have bank loaded
		 */
		System.out.println("Please insert quiz name: ");
		String quizName = scan.nextLine();
		int numberOfQuestions = readInt("How many questions? (MAX 10)\n");
		quiz = new Quiz(quizName, numberOfQuestions);
	}
	
	public void addQuestionFromBank() {
		/*
		 * Adding question to a quiz from bank
		 */
		if(quiz.isFull()) { // Quiz full
			System.out.println("Quiz is full.");
			return;
		} 
		else if(bank.getCurrentNumberOfQuestions() == 0) { // Bank empty
			System.out.println("Question Bank is Empty, please add questions to bank first.");
			return;
		}
		//------------------
		if (quiz.canAddQuestions()) { // Can add
			System.out.println(bank.toString());
			int id = readInt("Enter Question ID:\n");
			if (bank.isExist(id)) { // Making sure question exists in bank
				quiz.addQuestion(bank.getQuestionByID(id));
				System.out.println("Question has been added to quiz!");
			}else {
				System.out.println("Question ID doesn't exist.");
			}
		}else { // Quiz has reached it maximum number of questions, maybe user wants more?
			int decision = readInt("You have limited the quiz to " + quiz.getTotalNumberOfQuestions() +".\nDo you want to add more questions?\n1.Yes\n2.No\n");
			if(decision == 1) {
				increaseQuizNumberOfQuestions();
			}
		}
	}
	
	public void increaseQuizNumberOfQuestions() {
		/*
		 * Increases number of questions per quiz
		 */
		int available = quiz.getMaxNumberOfQuestions() - quiz.getTotalNumberOfQuestions();
		int num = readInt("You can add up to " + available + " more questions.\nHow many do you want to add?\n") + quiz.getTotalNumberOfQuestions();
		if (quiz.setQuizNumberOfQuestions(num)) {
			System.out.println("Quiz total number of questions had been updated to " + num);
		}else {
			System.out.println("Total number of questions exceeded, please enter a proper number.");
		}
	}
	
	public void removeQuestion(int option) {
		// Option 0 = remove from quiz
		// Option 1 = remove from bank
		int id = readInt("Which question do you want to remove? (By ID)\n");
		boolean isExist = (option == 0) ? quiz.isExist(id) : bank.isExist(id);
		if (option == 0 && isExist) {
			quiz.removeQuestionByID(id);
			System.out.println("Question has been removed");
		}else if (option == 1 && isExist){
			bank.removeQuestionByID(id);
			System.out.println("Question has been removed");
		}else {
			selection = (option == 0) ? CREATE_NEW_QUIZ : BANK;
			System.out.println("Question ID doesn't exist");
		}
	}
	
	// READ FUNCTIONS
	
	public boolean readBoolean(String prompt) {
		boolean isCorrect;
		while (true) {
			System.out.print(prompt);
			if(scan.hasNextBoolean()){
				isCorrect = scan.nextBoolean();
				scan.nextLine();
				return isCorrect;
			}
			scan.nextLine();
		}
	}
	
	public int readInt(String prompt) {
		while(true) {
			System.out.print(prompt);
			try { // VALIDATE INPUT
				selection = Integer.parseInt(scan.next()); // CASTING
				scan.nextLine();
				return selection;
			}catch (NumberFormatException ex){ // INPUT IS NOT A NUMBER
				System.out.println("Please enter a proper number");
			}
		}
	}
	
	private void pressEnterToContinue(){ 
        System.out.println("Press Enter key to continue...");
        try
        {
            scan.nextLine();
        }  
        catch(Exception e)
        {}  
	}
	
	//PRINTS FUNCTIONS
	public void firstPrint() {
		System.out.println("****************");
		System.out.println("**QUIZ BUILDER**");
		System.out.println("****************");		
	}
	
	public void promptByStage() {
		/*
		 * In this function we print the info for each menu, using switch on currentMenuStage
		 */
		if(bank != null) {
			System.out.println("Current Bank Loaded: " + bank.getBankName());
			System.out.println("****************");	
		}else {
			System.out.println("Currently no bank loaded");
			System.out.println("****************");	
		}
		switch(currentMenuStage) {
		case MAIN:
			System.out.println("Please select one of the following:");
			System.out.println("1. Create New Question Bank");
			System.out.println("2. Load Question Bank");
			System.out.println("3. Create New Quiz");
			System.out.println("0. Exit");
			break;
			
		case CREATE_NEW_QUIZ: // First Screen
			System.out.println("Please select one of the following:");
			System.out.println("1. Add New Question From Bank");
			System.out.println("2. Remove Question");
			System.out.println("3. View all questions");
			System.out.println("4. Save quiz to file");
			System.out.println("0. Exit");
			break;
			
		case BANK, LOAD_BANK:
			System.out.println("Please select one of the following:");
			System.out.println("1. Add New Question");
			System.out.println("2. Remove Question");
			System.out.println("3. Edit Question");
			System.out.println("4. View all questions");
			System.out.println("5. Save Question Bank to file");
			System.out.println("0. Exit");
			break;
		
		case EDIT_QUESTION:
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
	public void handleQuizSelection() {
		do {
			promptByStage();
			selection = readInt("");
			switch (selection) {
			case ADD_NEW_QUESTION:
				addQuestionFromBank();
				pressEnterToContinue();
				break;
				
			case REMOVE_QUESTION:
				removeQuestion(0);
				pressEnterToContinue();
				break;
				
			case VIEW_QUIZ:
				System.out.println(quiz.toString());
				pressEnterToContinue();
				break;
				
			case SAVE_QUIZ:
				quiz.addDefultAnswersToQuestions();
				try {
					quiz.saveQuiz();
					System.out.println("Quiz have been saved to PC");
					pressEnterToContinue();
				}catch (IOException e){
					System.out.println("Couldn't save bank");
				}
				break;
				
			case EXIT:
				currentMenuStage = MAIN;
				break;
				
			default:
				System.out.println("No such option in menu");
				selection = CREATE_NEW_QUIZ;
				break;
			}
			
		}while (selection != EXIT);
	}
	
	public void handleBankSelection() {
		do {
			promptByStage();
			selection = readInt("");
			switch (selection) {
			case ADD_NEW_QUESTION:
				createQuestion();
				pressEnterToContinue();
				break;
				
			case REMOVE_QUESTION:
				removeQuestion(1);
				pressEnterToContinue();
				break;
				
			case EDIT_QUESTION_IN_BANK:
				editQuestion();
				break;
				
			case VIEW_BANK:
				System.out.println(bank.toString());
				pressEnterToContinue();
				break;
				
			case SAVE_BANK:
				try {
					bank.saveBank();
					System.out.println("Bank have been saved to PC");
					pressEnterToContinue();
				}
				catch (IOException e){
					System.out.println("Couldn't save bank");
				}
				break;

			case EXIT:
				currentMenuStage = MAIN;
				break;	
				
			default:
				System.out.println("No such option in menu");
				selection = BANK;
				break;
			}
			
		}while (selection != EXIT);
	}
	
	public void handleEditQuestion(Question question) {
		int select;
		do {
			System.out.println("****EDIT QUESTION SCREEN****");
			System.out.println(question.toString());
			promptByStage();
			select = readInt("");
			switch(select) {
			case EDIT_QUESTION_TEXT:
				System.out.println("Current Question Text: " + question.getQuestionText());
				System.out.println("Enter Text to Replace with: ");
				String replaceWith = scan.nextLine();
				question.setQuestionText(replaceWith);
				System.out.println("Question Text has been changed");
				pressEnterToContinue();
				break;					
			
			case ADD_ANSWER:
				System.out.println(question.toString());
				int num = readInt("How many answers to add?\n");
				addAnswer(question, num);
				pressEnterToContinue();
				break;
			
			case EDIT_ANSWER:
				System.out.println(question.toString());
				editAnswer(question);
				pressEnterToContinue();
				break;
				
			case REMOVE_ANSWER:
				System.out.println(question.toString());
				removeAnswer(question);
				pressEnterToContinue();
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
			
		}while (select != EXIT);
	}
	//------------------------------\
}
