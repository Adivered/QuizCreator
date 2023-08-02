package QuizCreator;

import java.io.FileNotFoundException;
import java.time.LocalDateTime;
import java.io.IOException;
import java.time.format.DateTimeFormatter;

public abstract class Quiz {
	// Finals
	private final int MAX_NUMBER_OF_QUESTIONS = 10;
	
	// Members
	private String quizName;
	private int currentNumberOfQuestion;
	private int totalNumberOfQuestions;
	private Question[] allQuestions;
	private String timeNow;

	// Methods:
	// Constructors:
	/*
	 * Quiz Class needs:
	 * 1. Quiz name
	 * 2. Number of questions in a quiz
	 * we can keep track of how many questions added using currentNumberOfQuestion
	 * we limit the quiz to totalNumberOfQuestions
	 * questions are taken from bank
	 */
	
	public Quiz(String quizName, int numberOfQuestions) throws QuizNumberOfQuestionsOutOfRange {
		setQuizName(quizName);
		currentNumberOfQuestion = 0;
		if (!setQuizNumberOfQuestions(numberOfQuestions)){
			throw new QuizNumberOfQuestionsOutOfRange("Quiz number of questions out of range");
		} 
		allQuestions = new Question[MAX_NUMBER_OF_QUESTIONS];
	}
	
	// Getters
	
	public String getQuizName() {
		return quizName;
	}
	
	public int getTotalNumberOfQuestions() {
		return totalNumberOfQuestions;
	}
	
	public int getMaxNumberOfQuestions() {
		return MAX_NUMBER_OF_QUESTIONS;
	}
	
	public boolean setQuizNumberOfQuestions(int newNumber) {
		if (newNumber <= MAX_NUMBER_OF_QUESTIONS) {
			totalNumberOfQuestions = newNumber;
			return true;
		}
		return false;
	}
	
	public Question[] getQuestions() {
		return allQuestions;
	}
	
	 // Setters
	
	public void setQuizName(String newName) {
		quizName = newName;
	}
	
	public int getCurrentNumberOfQuestions() {
		return currentNumberOfQuestion;
	}
	
	//Functions:
	
	public void addQuestion(Question question) throws QuestionWithLessThanThreeAnswers {
		if (question instanceof ClosedQuestion) {
			if(((ClosedQuestion)question).getCurrentNumberOfAnswers() < 4)
				throw new QuestionWithLessThanThreeAnswers("Cant add a Closed Question with less than three answers");
		}
		allQuestions[currentNumberOfQuestion++] = question;
	}
	
	public boolean canAddQuestions() {
		return (currentNumberOfQuestion < totalNumberOfQuestions) ? true : false;
	}
	
	public boolean isFull() {
		return (currentNumberOfQuestion == MAX_NUMBER_OF_QUESTIONS) ? true : false;
	}
	
	public boolean isExist(int id) {
		/*
		 * This function checks if a question exists in quiz
		 */
		for (int i = 0; i < currentNumberOfQuestion; i++) {
			if (allQuestions[i].getQuestionID() == id) {
				return true;
			}
		}
		return false;
	}
	
	public void removeQuestionByID(int id) {
		/*
		 * This function removes a question from quiz but not from bank
		 */
		for (int i = 0; i < currentNumberOfQuestion; i++) {
			if(allQuestions[i].getQuestionID() == id) {
				if (currentNumberOfQuestion == i + 1) {
					allQuestions[i] = null;
				}else {
					allQuestions[i] = allQuestions[currentNumberOfQuestion - 1];
					allQuestions[currentNumberOfQuestion - 1] = null;
				}
				currentNumberOfQuestion--;
			}
		}
	}
	
	public void saveQuiz() throws FileNotFoundException, IOException {
		/*
		 * This function saves quiz into a new text file, using fileHandler class
		 * File name is by date using localDateTime and then calls saveQuizAnswers()
		 */
		timeNow = getLocalDate();
		FileHandler quizToText = new FileHandler(quizName + "_exam_" + timeNow + ".txt");
		quizToText.addToFile("Quiz Name: " + quizName + "\n\n");
		for (int i = 0; i < currentNumberOfQuestion; i++) {
			quizToText.addToFile( "Question #"+ (i+1) + ": " + allQuestions[i].getQuestionText() + "\n");
			if (allQuestions[i] instanceof OpenQuestion) {
				quizToText.addToFile( "\tAnswer Here: ____________________________\n");
			} else if (allQuestions[i] instanceof ClosedQuestion) {
				ClosedQuestion closedQuestion = (ClosedQuestion)allQuestions[i];
				for (int j = 0; j < closedQuestion.getCurrentNumberOfAnswers(); j++) {
					quizToText.addToFile( "\t"+ (char)('a' + j) + ") " + (closedQuestion.getAnswers())[j].getAnswer() + "\n");
				}
			}

			quizToText.addToFile("\n");
		}
		quizToText.saveFile();
		saveQuizAnswers();
	}
	
	public void saveQuizAnswers() throws FileNotFoundException, IOException {
		/*
		 * This function saves quiz answers into a new text file, using fileHandler class
		 */
		FileHandler answersToText = new FileHandler(quizName + "_solutions_" + timeNow + ".txt");
		for (int i = 0; i < currentNumberOfQuestion; i++) {
			answersToText.addToFile((i+1) + ") " + allQuestions[i].getQuestionText() + "\n");
			if (allQuestions[i] instanceof OpenQuestion) {
				OpenQuestion openQuestion = (OpenQuestion)allQuestions[i];
				answersToText.addToFile("Answer: " + openQuestion.getAnswer().getAnswer() + "\n");
			} else if (allQuestions[i] instanceof ClosedQuestion) {
				ClosedQuestion closedQuestion = (ClosedQuestion)allQuestions[i];
				answersToText.addToFile("Answers: ");
				for (int j = 0; j < closedQuestion.getCurrentNumberOfAnswers(); j++) {
					if(closedQuestion.getAnswers()[j].getIsCorrect())
						answersToText.addToFile((char)('a' + j) + ", ");
				}
			}

			answersToText.addToFile("\n");
		}
		answersToText.saveFile();
	}
	
	public void addDefultAnswersToQuestions() {
		for (int i = 0; i < currentNumberOfQuestion; i++) {
			if (allQuestions[i] instanceof ClosedQuestion)
			((ClosedQuestion)allQuestions[i]).addDefultAnswers();
		}
	}
	
	private static String getLocalDate() {
		/*
		 * This function returns current time in yyyy_MM_dd_HH_mm format
		 */
		LocalDateTime t1 = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy_MM_dd_HH_mm");
		String ldt = t1.format(formatter);
        return ldt;
    }
	
	public abstract void generateQuiz() throws  CloneNotSupportedException, QuestionWithLessThanThreeAnswers;
	
	@Override
	public String toString() {
		String message = "Quiz: " + quizName + ".\nCurrent number of Questions: " + currentNumberOfQuestion + "/" + totalNumberOfQuestions+ ".\n";
		for(int i=0; i < currentNumberOfQuestion; i++) {
			message +=  "Quiz Question #" + (i + 1) + ":\n------------\n" + allQuestions[i].toString();
		}
		return message;	
	}
	
}
