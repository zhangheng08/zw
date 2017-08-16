package com.ouchn.lib.entity;

public class QuestionAnswer {

	private String depotId;
	private String questionId;
	private String questionContent;
	private String questionAnswer;
	private int questionType;
	private boolean isAnswerRight;
	
	public String getDepotId() {
		return depotId;
	}
	public void setDepotId(String depotId) {
		this.depotId = depotId;
	}
	public String getQuestionId() {
		return questionId;
	}
	public void setQuestionId(String questionId) {
		this.questionId = questionId;
	}
	public String getQuestionContent() {
		return questionContent;
	}
	public void setQuestionContent(String questionContent) {
		this.questionContent = questionContent;
	}
	public String getQuestionAnswer() {
		return questionAnswer;
	}
	public void setQuestionAnswer(String questionAnswer) {
		this.questionAnswer = questionAnswer;
	}
	public int getQuestionType() {
		return questionType;
	}
	public void setQuestionType(int questionType) {
		this.questionType = questionType;
	}
	public boolean isAnswerRight() {
		return isAnswerRight;
	}
	public void setAnswerRight(boolean isAnswerRight) {
		this.isAnswerRight = isAnswerRight;
	}
	
	
	
}
