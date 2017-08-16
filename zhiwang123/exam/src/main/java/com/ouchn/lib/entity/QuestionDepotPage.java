package com.ouchn.lib.entity;

public class QuestionDepotPage {
	
	private String depotId;
	private int score;
	private int totleNum;
	private int answeredNum;
	private int wrongNum;
	private int depotState;
	private String costTime;
	
	public String getDepotId() {
		return depotId;
	}
	public void setDepotId(String depotId) {
		this.depotId = depotId;
	}
	public int getScore() {
		return score;
	}
	public void setScore(int score) {
		this.score = score;
	}
	public int getTotleNum() {
		return totleNum;
	}
	public void setTotleNum(int totleNum) {
		this.totleNum = totleNum;
	}
	public int getAnsweredNum() {
		return answeredNum;
	}
	public void setAnsweredNum(int answeredNum) {
		this.answeredNum = answeredNum;
	}
	public int getWrongNum() {
		return wrongNum;
	}
	public void setWrongNum(int wrongNum) {
		this.wrongNum = wrongNum;
	}
	public int getDepotState() {
		return depotState;
	}
	public void setDepotState(int depotState) {
		this.depotState = depotState;
	}
	public String getCostTime() {
		return costTime;
	}
	public void setCostTime(String costTime) {
		this.costTime = costTime;
	}
	
}
