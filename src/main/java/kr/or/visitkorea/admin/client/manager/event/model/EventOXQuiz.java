package kr.or.visitkorea.admin.client.manager.event.model;

import com.google.gwt.json.client.JSONNumber;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONString;

import kr.or.visitkorea.admin.client.application.Console;

public class EventOXQuiz {

	private String eqcId;
	private String subEvtId;
	private String evtId;
	private String question;
	private String questionimgId;
	private String questionimgAlt;
	private String questionimgPath;
	private String hintimgAlt;
	private String hintbody;
	private String hintimgId;
	private String hintimgPath;
	private boolean questiontype;
	private boolean answer;
	private boolean hintYn;
	private int hintType;
	private int sort;
	
	public EventOXQuiz() {
		
	}


	public String getEqcId() {
		return eqcId;
	}


	public void setEqcId(String eqcId) {
		this.eqcId = eqcId;
	}


	public String getSubEvtId() {
		return subEvtId;
	}


	public void setSubEvtId(String subEvtId) {
		this.subEvtId = subEvtId;
	}


	public String getEvtId() {
		return evtId;
	}


	public void setEvtId(String evtId) {
		this.evtId = evtId;
	}


	public String getQuestion() {
		return question;
	}


	public void setQuestion(String question) {
		this.question = question;
	}


	public String getQuestionimgId() {
		return questionimgId;
	}


	public void setQuestionimgId(String questionimgId) {
		this.questionimgId = questionimgId;
	}


	public String getQuestionimgAlt() {
		return questionimgAlt;
	}


	public void setQuestionimgAlt(String questionimgAlt) {
		this.questionimgAlt = questionimgAlt;
	}


	public String getQuestionimgPath() {
		return questionimgPath;
	}


	public void setQuestionimgPath(String questionimgPath) {
		this.questionimgPath = questionimgPath;
	}


	public String getHintimgAlt() {
		return hintimgAlt;
	}


	public void setHintimgAlt(String hintimgAlt) {
		this.hintimgAlt = hintimgAlt;
	}


	public String getHintbody() {
		return hintbody;
	}


	public void setHintbody(String hintbody) {
		this.hintbody = hintbody;
	}



	public String getHintimgId() {
		return hintimgId;
	}


	public void setHintimgId(String hintimgId) {
		this.hintimgId = hintimgId;
	}


	public String getHintimgPath() {
		return hintimgPath;
	}


	public void setHintimgPath(String hintimgPath) {
		this.hintimgPath = hintimgPath;
	}


	public boolean isQuestiontype() {
		return questiontype;
	}


	public void setQuestiontype(boolean questiontype) {
		this.questiontype = questiontype;
	}


	public boolean isAnswer() {
		return answer;
	}


	public void setAnswer(boolean answer) {
		this.answer = answer;
	}


	public boolean isHintYn() {
		return hintYn;
	}


	public void setHintYn(boolean hintYn) {
		this.hintYn = hintYn;
	}


	public int getHintType() {
		return hintType;
	}


	public void setHintType(int hintType) {
		this.hintType = hintType;
	}


	public int isSort() {
		return sort;
	}


	public void setSort(int sort) {
		this.sort = sort;
	}

	public static EventOXQuiz fromJson(JSONObject obj) {
		EventOXQuiz eventquiz = new EventOXQuiz();
		if (obj.containsKey("EQC_ID"))
			eventquiz.setEqcId(obj.get("EQC_ID").isString().stringValue());
		if (obj.containsKey("SUB_EVT_ID"))
			eventquiz.setSubEvtId(obj.get("SUB_EVT_ID").isString().stringValue());
		if (obj.containsKey("QUESTION"))
			eventquiz.setQuestion(obj.get("QUESTION").isString().stringValue());
		if (obj.containsKey("QUESTION_TYPE"))
			eventquiz.setQuestiontype(obj.get("QUESTION_TYPE").isString().stringValue() == "T" ? true : false);
		if (obj.containsKey("QUESTION_IMG_ID"))
			eventquiz.setQuestionimgId(obj.get("QUESTION_IMG_ID").isString().stringValue());
		if (obj.containsKey("QUESTION_IMG_ALT"))
			eventquiz.setQuestionimgAlt(obj.get("QUESTION_IMG_ALT").isString().stringValue());
		if (obj.containsKey("QUESTION_IMG_PATH"))
			eventquiz.setQuestionimgPath(obj.get("QUESTION_IMG_PATH").isString().stringValue());
		if (obj.containsKey("ANSWER"))
			eventquiz.setAnswer(obj.get("ANSWER").isString().stringValue() == "O" ? true : false);
		if (obj.containsKey("HINT_USE_YN"))
			eventquiz.setHintYn(obj.get("HINT_USE_YN").isString().stringValue() == "Y" ? true : false);
		if (obj.containsKey("HINT_TYPE"))
			eventquiz.setHintType(obj.get("HINT_TYPE").isString().stringValue() == "T" ? 0 
					: obj.get("HINT_TYPE").isString().stringValue() == "U" ? 1 : 2);
		if (obj.containsKey("HINT_BODY"))
			eventquiz.setHintbody(obj.get("HINT_BODY").isString().stringValue());
		if (obj.containsKey("HINT_IMG_ID"))
			eventquiz.setHintimgId(obj.get("HINT_IMG_ID").isString().stringValue());
		if (obj.containsKey("HINT_IMG_ALT"))
			eventquiz.setHintimgAlt(obj.get("HINT_IMG_ALT").isString().stringValue());
		if (obj.containsKey("HINT_IMG_PATH"))
			eventquiz.setHintimgPath(obj.get("HINT_IMG_PATH").isString().stringValue());
		if (obj.containsKey("SORT"))
			eventquiz.setSort((int) obj.get("SORT").isNumber().doubleValue());
		
		return eventquiz;
	}
	
	public JSONObject toJson() {
		JSONObject obj = new JSONObject();
		if (this.eqcId != null)
			obj.put("EQC_ID", new JSONString(this.eqcId));
		if (this.subEvtId != null)
			obj.put("SUB_EVT_ID", new JSONString(this.subEvtId));
		if (this.question != null)
			obj.put("QUESTION", new JSONString(this.question));
		if (this.questionimgId != null)
			obj.put("QUESTION_IMG_ID", new JSONString(this.questionimgId));
		if (this.questionimgAlt != null)
			obj.put("QUESTION_IMG_ALT", new JSONString(this.questionimgAlt));
		if (this.questionimgPath != null)
			obj.put("QUESTION_IMG_PATH", new JSONString(this.questionimgPath));
		if (this.hintbody != null)
			obj.put("HINT_BODY", new JSONString(this.hintbody));
		if (this.hintimgId != null)
			obj.put("HINT_IMG_ID", new JSONString(this.hintimgId));
		if (this.hintimgAlt != null)
			obj.put("HINT_IMG_ALT", new JSONString(this.hintimgAlt));
		if (this.hintimgPath != null)
			obj.put("HINT_IMG_PATH", new JSONString(this.hintimgPath));
		
		obj.put("QUESTION_TYPE", new JSONString(this.questiontype ? "T" : "I"));
		obj.put("ANSWER", new JSONString(this.answer ? "O" : "X"));
		obj.put("HINT_USE_YN", new JSONString(this.hintYn ? "Y" : "N"));
		obj.put("HINT_TYPE", new JSONString(this.hintType == 0 ? "T" : this.hintType == 1 ? "U" : "I"));
		obj.put("SORT", new JSONNumber(this.sort));
		
		return obj;
		
	}
	
	

	public String toString() {
		return "EventOXQuiz ["
				+ "\n\t eqcId=" + eqcId
				+ ",\n\t subEvtId=" + subEvtId
				+ ",\n\t evtId=" + evtId
				+ ",\n\t question=" + question
				+ ",\n\t questionimgId=" + questionimgId
				+ ",\n\t questionimgAlt=" + questionimgAlt
				+ ",\n\t questionimgPath=" + questionimgPath
				+ ",\n\t hintimgAlt=" + hintimgAlt
				+ ",\n\t hintbody=" + hintbody
				+ ",\n\t hintimgId=" + hintimgId
				+ ",\n\t hintimgPath=" + hintimgPath
				+ ",\n\t questiontype=" + questiontype
				+ ",\n\t answer=" + answer
				+ ",\n\t hintYn=" + hintYn
				+ ",\n\t hintType=" + hintType
				+ ",\n\t sort=" + sort
				+ ",\n]";
	}
	
	
}

