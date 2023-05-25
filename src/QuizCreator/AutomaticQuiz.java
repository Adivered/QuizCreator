package AdiVered_GuyBenMoshe;

import java.io.IOException;
import java.util.Random;


public class AutomaticQuiz implements Quizzable {

	Bank bank;
	Quiz quiz;
	int selection;

	@Override
	public void createQuiz(Bank bank) {
		try {
			newQuiz();
			this.bank = bank;
			generateQuiz();
		} catch (QuizNumberOfQuestionsOutOfRange e) {
			System.out.println(e.getMessage());
			InputReader.pressEnterToContinue();
		} catch (CloneNotSupportedException e) {

		} catch (QuestionWithLessThanThreeAnswers e) {

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

	public void generateQuiz() throws  CloneNotSupportedException, QuestionWithLessThanThreeAnswers {
		int randomQuestion;
		Question[] questions = bank.getQuestions();
		Question q;
		ClosedQuestion copyQ;
		while (quiz.canAddQuestions() && !isEmpty(questions)) {
			randomQuestion = randInt(1, getNumberOfQuestions(questions));
			q = questions[randomQuestion - 1];
			if(q instanceof OpenQuestion) {
				quiz.addQuestion(q);
			} else {
				copyQ = ((ClosedQuestion)q).clone();
				try {
					if (isEligibleQuestion(copyQ)) {
						Answer[] randomAnswers = setFourRandomAmnswers(copyQ);
						copyQ.setAnswers(randomAnswers, 4);
						copyQ.addAutoDefultAnswers();
						quiz.addQuestion(copyQ);
					}
					
				}catch (QuestionWithLessThanThreeAnswers e) {
					//do nothing...
				}
			}
			questions = removeQuestionFromArray(questions, q, getNumberOfQuestions(questions));
		}
		try {
			quiz.saveQuiz();
			System.out.println("quiz has saved (:");
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
	}

	public boolean isEligibleQuestion(ClosedQuestion q) {
		int wrongCounter = 0;
		int trueCounter = 0;
		for (int i = 0; i < q.getCurrentNumberOfAnswers(); i++) {
			if (!q.getAnswerByID(i+1).getIsCorrect()) {
				wrongCounter++;
			}else {
				trueCounter++;
			}
			if (wrongCounter == 4 || (wrongCounter >= 3 && trueCounter >= 1)) {
				return true;
			}
		}
		return false;
	}

	public Answer[] setFourRandomAmnswers(ClosedQuestion copyQ) {
		boolean hasOneTrue = false;
		boolean isExist;
		int rand;
		Answer[] newAnswers = new Answer[5];
		int currentNumberOfAnswers = 0;
		while(currentNumberOfAnswers < 4) {
			isExist = false;
			rand = randInt(1, copyQ.getCurrentNumberOfAnswers());
			if(copyQ.getAnswerByID(rand).getIsCorrect() && !hasOneTrue) {
				newAnswers[currentNumberOfAnswers++] = copyQ.getAnswerByID(rand);
				hasOneTrue = true;
				copyQ.removeAnswerByID(rand);
			} else if (!copyQ.getAnswerByID(rand).getIsCorrect()) {
				newAnswers[currentNumberOfAnswers++] = copyQ.getAnswerByID(rand);
				copyQ.removeAnswerByID(rand);
			}

		}
		return newAnswers;
	}

	public int randInt(int min, int max) {
		Random rand = new Random();
		int randomNum = rand.nextInt((max - min) + 1) + min;

		return randomNum;
	}
	
	public boolean isEmpty(Question[] questions) {
		for (int i = 0; i < questions.length; i++) {
			if (questions[i] != null) {
				return false;
			}
		}
		return true;
	}
	
	public int getNumberOfQuestions(Question[] questions) {
		int counter = 0;
		for (int i = 0; i < questions.length; i++) {
			if (questions[i] != null) {
				counter++;
			}
		}
		return counter;
	}
	
	public Question[] removeQuestionFromArray(Question[] questions, Question q, int currentNumberOfQuestions) {
		for (int i = 0; i < currentNumberOfQuestions; i++) {
			if (questions[i] == q && i == currentNumberOfQuestions - 1) {
				questions[i] = null;
			}else if (questions[i] == q) {
				questions[i] = questions[currentNumberOfQuestions-1];
				questions[currentNumberOfQuestions-1] = null;
				return questions;
			}
		}
		return questions;
	}
}
