package QuizCreator;

import java.io.IOException;
import java.util.Arrays;
import java.util.Random;


public class AutomaticQuiz extends Quiz implements Quizzable {

	// Private Members
	private Bank bank;
	
	public AutomaticQuiz(String quizName, int numberOfQuestions) throws QuizNumberOfQuestionsOutOfRange{
		super(quizName, numberOfQuestions);
	} 

	@Override
	public void createQuiz(Bank bank) {
		/*
		 * IN: bank Type: BANK
		 * OUT: New Quiz in saved in TXT file
		 * This function creates a new quiz, based on random elimination using the "Random" Module
		 * and Clone interface.
		 */
		try {
			this.bank = bank; // Reassign the bank global variable so it can be accessed through the global scope
			generateQuiz();
		} catch (CloneNotSupportedException e) { // Class doesn't implement clone interface
			System.out.println(e.getMessage());

		} catch (QuestionWithLessThanThreeAnswers e) { // When question has less than 3 answers --> Catch here
			System.out.println(e.getMessage());
		}
		InputReader.pressEnterToContinue(); // Regardless of outcome, pause for message completion
	}

	// Functions
	public void generateQuiz() throws  CloneNotSupportedException, QuestionWithLessThanThreeAnswers {
		/*
		 * Generates new quiz, "automatically", using Random module and by cloning question and it answers,
		 * using elimination method to avoid repeating grabbing the same questions and answers
		 */
		Question[] questions = Arrays.copyOf(bank.getQuestions(), bank.getCurrentNumberOfQuestions());
		Question question;
		ClosedQuestion copyQuestion;
		int randomQuestion;
		while (this.canAddQuestions() && !isEmpty(questions)) { // Quiz isn't full && Questions array isn't empty
			randomQuestion = generateRandomNumber(1, getNumberOfQuestions(questions));
			question = questions[randomQuestion - 1];
			if(question instanceof OpenQuestion) { // If a question is an OpenQuestion, can just add question to quiz
				this.addQuestion(question);
			} else { // Question is ClosedQuestoin
				copyQuestion = ((ClosedQuestion)question).clone();
				if (isEligibleQuestion(copyQuestion)) {
					setFourRandomAnswers(copyQuestion);
					this.addQuestion(copyQuestion);
				}
			}
			questions = removeQuestionFromArray(questions, question, getNumberOfQuestions(questions));
		}
		try {
			this.saveQuiz();
			System.out.println("Quiz has been saved (:");
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
	}

	public boolean isEligibleQuestion(ClosedQuestion q) {
		/*
		 * This function checks if @ClosedQuestion q can be used in the automatic exam.
		 * @q has to have at least: Four wrong answers OR Three wrong answers and one true
		 */
		final int MIN_WRONG_ANSWERS = 4;
		final int MIN_WRONG_ANSWERS_WITH_TRUE_ANSWER = 3;
		int wrongCounter = 0;
		int trueCounter = 0;
		for (int i = 0; i < q.getCurrentNumberOfAnswers(); i++) {
			if (!q.getAnswerByID(i+1).getIsCorrect()) {
				wrongCounter++;
			}else {
				trueCounter++;
			}
			if (wrongCounter == MIN_WRONG_ANSWERS || (wrongCounter >= MIN_WRONG_ANSWERS_WITH_TRUE_ANSWER && trueCounter >= 1)) {
				return true;
			}
		}
		return false;
	}

	public void setFourRandomAnswers(ClosedQuestion copyQuestion) {
		/*
		 * Sets four random answers in copyQuestion
		 */
		boolean hasOneTrue = false;
		int rand;
		int currentNumberOfAnswers = 0;
		Answer[] newAnswers = new Answer[5]; // 4 random + 1 that must be added
		while(currentNumberOfAnswers < 4) { // Can still add questions..
			rand = generateRandomNumber(1, copyQuestion.getCurrentNumberOfAnswers());
			if(copyQuestion.getAnswerByID(rand).getIsCorrect() && !hasOneTrue) { // Question has no answer with true value yet
				newAnswers[currentNumberOfAnswers++] = copyQuestion.getAnswerByID(rand);
				hasOneTrue = true; // Once set to true, question has one true answer placed
				copyQuestion.removeAnswerByID(rand); // Remove from basket
			} else if (!copyQuestion.getAnswerByID(rand).getIsCorrect()) { // Answer is wrong and can be selected
				newAnswers[currentNumberOfAnswers++] = copyQuestion.getAnswerByID(rand);
				copyQuestion.removeAnswerByID(rand);
			}
		}
		copyQuestion.setAnswers(newAnswers, 4); // Updates answers in copyQuestion
		copyQuestion.addAutoDefultAnswers(); // Adds the default answers
	}

	public int generateRandomNumber(int min, int max) {
		/*
		 * This function generates random Integer, between min and max.
		 */
		Random rand = new Random();
		int randomNum = rand.nextInt((max - min) + 1) + min;

		return randomNum;
	}

	public boolean isEmpty(Question[] questions) {
		/*
		 * This function returns true if array is empty, false otherwise
		 */
		for (int i = 0; i < questions.length; i++) {
			if (questions[i] != null) {
				return false;
			}
		}
		return true;
	}

	public int getNumberOfQuestions(Question[] questions) {
		/*
		 * This function iterates over array of questions, and returns how many questions are placed in array
		 */
		int counter = 0;
		for (int i = 0; i < questions.length; i++) {
			if (questions[i] != null) {
				counter++;
			}
		}
		return counter;
	}

	public Question[] removeQuestionFromArray(Question[] questions, Question q, int currentNumberOfQuestions) {
		/*
		 * This function removes @Question q from @Question[] questions.
		 * @currentNumberOfQuestion is used to track how many questions are currently stored in array questions
		 */
		for (int i = 0; i < currentNumberOfQuestions; i++) {
			if (questions[i] == q && i == currentNumberOfQuestions - 1) {
				questions[i] = null;

			}else if (questions[i] == q) { // Found the question to remove
				questions[i] = questions[currentNumberOfQuestions-1];
				questions[currentNumberOfQuestions-1] = null;
				return questions;
			}
		}
		return questions;
	}
}
