package AdiVered_GuyBenMoshe;

import java.io.IOException;

public class ManualQuiz implements Quizzable {
	Bank bank;
	Quiz quiz;
	int selection;

	@Override
	public void createQuiz(Bank bank) {
		try {
			newQuiz();
			this.bank = bank;
			handleManualQuizSelection();
		} catch (QuizNumberOfQuestionsOutOfRange e) {
			System.out.println(e.getMessage());
			InputReader.pressEnterToContinue();
		}
		 
	}
	
	public void newQuiz() throws QuizNumberOfQuestionsOutOfRange{
		/*
		 * Creates new quiz, must have bank loaded
		 */
		String quizName = InputReader.readString("Enter Quiz Name:");
		int numberOfQuestions = InputReader.readInt("How many questions?\n");
		quiz = new Quiz(quizName, numberOfQuestions);
	}
	
	public void addQuestionFromBank() throws QuestionWithLessThanThreeAnswers{
		/*
		 * Adding question to a quiz from bank
		 */
		if (quiz.isFull()) { // Quiz full
			System.out.println("Quiz is full.");
			return;
		} else if (bank.getCurrentNumberOfQuestions() == 0) { // Bank empty
			System.out.println("Question Bank is Empty, please add questions to bank first.");
			return;
		}
		// ------------------
		if (quiz.canAddQuestions()) { // Can add
			System.out.println(bank.toString());
			int id = InputReader.readInt("Enter Question ID:\n");
			if (bank.isExist(id)) { // Making sure question exists in bank
				quiz.addQuestion(bank.getQuestionByID(id));
				System.out.println("Question has been added to quiz!");
			} else {
				System.out.println("Question ID doesn't exist.");
			}
		} else { // Quiz has reached it maximum number of questions, maybe user wants more?
			int decision = InputReader.readInt("You have limited the quiz to " + quiz.getTotalNumberOfQuestions()
					+ ".\nDo you want to add more questions?\n1. Yes\n2. No\n");
			if (decision == 1) {
				increaseQuizNumberOfQuestions();
			}
		}
	}

	public void increaseQuizNumberOfQuestions() {
		/*
		 * Increases number of questions per quiz
		 */
		int available = quiz.getMaxNumberOfQuestions() - quiz.getTotalNumberOfQuestions();
		int num = InputReader
				.readInt("You can add up to " + available + " more questions.\nHow many do you want to add?\n")
				+ quiz.getTotalNumberOfQuestions();
		if (quiz.setQuizNumberOfQuestions(num)) {
			System.out.println("Quiz total number of questions had been updated to " + num);
		} else {
			System.out.println("Total number of questions exceeded, please enter a proper number.");
		}
	}
	
	public void removeQuestionFromQuiz(int id){
		if (!quiz.isExist(id)) {
			System.out.println("Question ID doesn't exist");
			return;
		}
		quiz.removeQuestionByID(id);
		System.out.println("Question has been removed from Quiz.");
	}
	
	public void handleManualQuizSelection() {
		final int ADD_NEW_QUESTION = 1;
		final int REMOVE_QUESTION = 2;
		final int VIEW_QUIZ = 3;
		final int SAVE_QUIZ = 4;
		final int EXIT = 0;
		do {
			System.out.println("---CREATE MANUAL QUIZ MENU---");
			System.out.println("Please select one of the following:");
			System.out.println("1. Add New Question From Bank");
			System.out.println("2. Remove Question");
			System.out.println("3. View all questions");
			System.out.println("4. Save quiz to file");
			System.out.println("0. Exit");
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
					System.out.println(quiz.toString());
					InputReader.pressEnterToContinue();
					break;

				case SAVE_QUIZ:
					quiz.addDefultAnswersToQuestions();
					try {
						quiz.saveQuiz();
						System.out.println("Quiz have been saved to PC");
						InputReader.pressEnterToContinue();
					} catch (IOException e) {
						System.out.println("Couldn't save quiz");
					}
					break;

				case EXIT:
					if (InputReader.readBoolean("Are you sure you want to exit? (Any unsaved questions will be removed)\nWrite True or False\n")) {
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
	

}
