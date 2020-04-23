package kr.or.visitkorea.admin.client.manager.event.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONBoolean;
import com.google.gwt.json.client.JSONNumber;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.json.client.JSONValue;

import kr.or.visitkorea.admin.client.application.Console;

public class EventProcess {
	private String subEvtId;
	private String evtId;
	private String title;
	private String startDate;
	private String endDate;
	private String ExcelFileNm;
	private int evtTypeCd;
	private int electType;
	private int immediType;
	private int randomType;
	private int userPredictCnt;
	private int announcePer;
	private int winconduse;
	private int wincondvalue;
	private boolean isNotWin;
	private boolean isDelete;
	private List<EventGift> giftList = new ArrayList<>();
	private List<EventOXQuiz> OXQuizList = new ArrayList<>();
	private List<EventOXQuiz> OXQuizDeleteList = new ArrayList<>();
	private List<EventGift> giftDeleteList = new ArrayList<>();
	
	public static EventProcess fromJson(JSONObject obj) {
		EventProcess process = new EventProcess();
		process.setDelete(false);
		if (obj.containsKey("SUB_EVT_ID"))
			process.setSubEvtId(obj.get("SUB_EVT_ID").isString().stringValue());
		if (obj.containsKey("EVT_ID"))
			process.setEvtId(obj.get("EVT_ID").isString().stringValue());
		if (obj.containsKey("TITLE"))
			process.setTitle(obj.get("TITLE").isString().stringValue());
		if (obj.containsKey("START_DATE"))
			process.setStartDate(obj.get("START_DATE").isString().stringValue());
		if (obj.containsKey("END_DATE"))
			process.setEndDate(obj.get("END_DATE").isString().stringValue());
		
		if (obj.containsKey("EVT_TYPE_CD"))
			process.setEvtTypeCd((int) obj.get("EVT_TYPE_CD").isNumber().doubleValue());
		if (obj.containsKey("ELECT_TYPE"))
			process.setElectType((int) obj.get("ELECT_TYPE").isNumber().doubleValue());
		if (obj.containsKey("IMMEDI_TYPE"))
			process.setImmediType((int) obj.get("IMMEDI_TYPE").isNumber().doubleValue());
		if (obj.containsKey("RANDOM_TYPE"))
			process.setRandomType((int) obj.get("RANDOM_TYPE").isNumber().doubleValue());
		if (obj.containsKey("USER_PREDICT_CNT"))
			process.setUserPredictCnt((int) obj.get("USER_PREDICT_CNT").isNumber().doubleValue());
		if (obj.containsKey("ANNOUNCE_PER"))
			process.setAnnouncePer((int) obj.get("ANNOUNCE_PER").isNumber().doubleValue());
		if (obj.containsKey("WIN_COND_USE_YN"))
			process.setWinCondUse(obj.get("WIN_COND_USE_YN").isBoolean().booleanValue() ? 1 : 0);
		if (obj.containsKey("WIN_COND_VALUE"))
			process.setWinCondValue((int) obj.get("WIN_COND_VALUE").isNumber().doubleValue());
		if (obj.containsKey("EXCEL_FILE_NM"))
			process.setExcelFileNm( obj.get("EXCEL_FILE_NM").isString().stringValue());
		if (obj.containsKey("IS_NOT_WIN"))
			process.setNotWin(obj.get("IS_NOT_WIN").isBoolean().booleanValue());
		if (obj.containsKey("GIFTS")) {
			JSONValue gifts = obj.get("GIFTS");
			
			if (gifts instanceof JSONObject) {
				process.setGiftList(Arrays.asList(EventGift.fromJson(gifts.isObject())));
			} else if (gifts instanceof JSONArray) {
				List<EventGift> giftList = new ArrayList<>();
				
				JSONArray giftArr = gifts.isArray();
				
				for (int i = 0; i < giftArr.size(); i++) {
					JSONObject giftObj = giftArr.get(i).isObject();
					giftList.add(EventGift.fromJson(giftObj));
				}
				
				process.setGiftList(giftList);
			}
		}
		if (obj.containsKey("OXQUIZS")) {
			JSONValue OXQuizs = obj.get("OXQUIZS");
			
			if (OXQuizs instanceof JSONObject) {
				process.setOXQuizList(Arrays.asList(EventOXQuiz.fromJson(OXQuizs.isObject())));
			} else if (OXQuizs instanceof JSONArray) {
				List<EventOXQuiz> OXQuizList = new ArrayList<>();
				
				JSONArray OXQuizArr = OXQuizs.isArray();
				
				for (int i = 0; i < OXQuizArr.size(); i++) {
					JSONObject OXQuizObj = OXQuizArr.get(i).isObject();
					OXQuizList.add(EventOXQuiz.fromJson(OXQuizObj));
				}
				
				process.setOXQuizList(OXQuizList);
			}
		}
		return process;
	}
	
	public JSONObject toJson() {
		JSONObject obj = new JSONObject();
		if (this.subEvtId != null)
			obj.put("SUB_EVT_ID", new JSONString(this.subEvtId));
		if (this.evtId != null)
			obj.put("EVT_ID", new JSONString(this.evtId));
		if (this.title != null)
			obj.put("TITLE", new JSONString(this.title));
		if (this.startDate != null)
			obj.put("START_DATE", new JSONString(this.startDate));
		if (this.endDate != null)
			obj.put("END_DATE", new JSONString(this.endDate));
		if (this.ExcelFileNm != null)
			obj.put("EXCEL_FILE_NM", new JSONString(this.ExcelFileNm));
			
		obj.put("EVT_TYPE_CD", new JSONNumber(this.evtTypeCd));
		obj.put("ELECT_TYPE", new JSONNumber(this.electType));
		obj.put("IMMEDI_TYPE", new JSONNumber(this.immediType));
		obj.put("RANDOM_TYPE", new JSONNumber(this.randomType));
		obj.put("USER_PREDICT_CNT", new JSONNumber(this.userPredictCnt));
		obj.put("ANNOUNCE_PER", new JSONNumber(this.announcePer));
		obj.put("WIN_COND_USE_YN", new JSONNumber(this.winconduse));
		obj.put("WIN_COND_VALUE", new JSONNumber(this.wincondvalue));
		obj.put("IS_NOT_WIN", JSONBoolean.getInstance(this.isNotWin));
		obj.put("IS_DELETE", JSONBoolean.getInstance(this.isDelete));
		JSONArray giftDeleteArr = new JSONArray();
		obj.put("GIFT_DELETE", giftDeleteArr);
		JSONArray giftArr = new JSONArray();
		obj.put("GIFTS", giftArr);
		JSONArray OXQuizDeleteArr = new JSONArray();
		obj.put("OXQUIZ_DELETE", giftDeleteArr);
		JSONArray OXQuizArr = new JSONArray();
		obj.put("OXQUIZS", OXQuizArr);
		this.giftList = StreamSupport.stream(this.giftList.spliterator(), false)
							.sorted((o1, o2) -> o1.getOrder() > o2.getOrder() ? 1 : -1)
							.collect(Collectors.toList());

		int order = 0;
		for (EventGift gift : this.giftList) {
				gift.setOrder(order++);
			giftArr.set(giftArr.size(), gift.toJson());
		}
		
		for (EventGift gift : this.giftDeleteList) {
				giftDeleteArr.set(giftDeleteArr.size(), gift.toJson());
		}
		
		for (EventOXQuiz OXQuiz : this.OXQuizDeleteList) {
			giftDeleteArr.set(giftDeleteArr.size(), OXQuiz.toJson());
		}
		
		int sort = 0;
		for (EventOXQuiz eventOXQuiz : this.OXQuizList) {
			eventOXQuiz.setSort(sort++);
			OXQuizArr.set(OXQuizArr.size(), eventOXQuiz.toJson());
		}
		
		
		
		return obj;
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
	
	public String getTitle() {
		return title;
	}
	
	public void setTitle(String title) {
		this.title = title;
	}
	
	public String getStartDate() {
		return startDate;
	}
	
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}
	
	public String getEndDate() {
		return endDate;
	}
	
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	
	public int getWinCondValue() {
		return wincondvalue;
	}
	
	public void setWinCondValue(int wincondvalue) {
		this.wincondvalue = wincondvalue;
	}
	
	public int getEvtTypeCd() {
		return evtTypeCd;
	}
	
	public void setEvtTypeCd(int evtTypeCd) {
		this.evtTypeCd = evtTypeCd;
	}

	public int getElectType() {
		return electType;
	}

	public void setElectType(int electType) {
		this.electType = electType;
	}
	
	public String getExcelFileNm() {
		return ExcelFileNm;
	}
	
	public void setExcelFileNm(String ExcelFileNm) {
		this.ExcelFileNm = ExcelFileNm;
	}

	public int getImmediType() {
		return immediType;
	}

	public void setImmediType(int immediType) {
		this.immediType = immediType;
	}

	public int getRandomType() {
		return randomType;
	}

	public void setRandomType(int randomType) {
		this.randomType = randomType;
	}

	public int getUserPredictCnt() {
		return userPredictCnt;
	}

	public void setUserPredictCnt(int userPredictCnt) {
		this.userPredictCnt = userPredictCnt;
	}

	public int getAnnouncePer() {
		return announcePer;
	}
	

	public void setAnnouncePer(int announcePer) {
		this.announcePer = announcePer;
	}
	
	public int getWinCondUse() {
		return winconduse;
	}

	public void setWinCondUse(int winconduse) {
		this.winconduse = winconduse;
	}
	
	public boolean isNotWin() {
		return isNotWin;
	}

	public void setNotWin(boolean isNotWin) {
		this.isNotWin = isNotWin;
	}

	public List<EventGift> getGiftList() {
		return giftList;
	}
	
	public List<EventOXQuiz> getOXQuizList() {
		return OXQuizList;
	}
	
	public List<EventOXQuiz> getOXQuizDeleteList() {
		return OXQuizDeleteList;
	}
	
	public List<EventGift> getGiftDeleteList() {
		return giftDeleteList;
	}
	

	public void setGiftList(List<EventGift> giftList) {
		this.giftList = giftList;
	}
	
	public void setOXQuizList(List<EventOXQuiz> OXQuizList) {
		this.OXQuizList = OXQuizList;
	}
	public void setOXQuizDeleteList(List<EventOXQuiz> OXQuizDeleteList) {
		this.OXQuizDeleteList = OXQuizDeleteList;
	}
	
	public void setGiftDeleteList(List<EventGift> giftDeleteList) {
		this.giftDeleteList = giftDeleteList;
	}
	

	public boolean isDelete() {
		return isDelete;
	}

	public void setDelete(boolean isDelete) {
		this.isDelete = isDelete;
	}

	@Override
	public String toString() {
		return "EventProcess ["
				+ "\n\t subEvtId=" + subEvtId
				+ ",\n\t evtId=" + evtId
				+ ",\n\t title=" + title
				+ ",\n\t startDate=" + startDate
				+ ",\n\t endDate=" + endDate
				+ ",\n\t wincondvalue=" + wincondvalue
				+ ",\n\t evtTypeCd=" + evtTypeCd
				+ ",\n\t electType=" + electType
				+ ",\n\t immediType=" + immediType
				+ ",\n\t randomType=" + randomType
				+ ",\n\t userPredictCnt=" + userPredictCnt
				+ ",\n\t announcePer=" + announcePer
				+ ",\n\t winconduse=" + winconduse
				+ ",\n\t xlsfileName=" + ExcelFileNm
				+ ",\n\t isNotWin=" + isNotWin
				+ ",\n\t isDelete=" + isDelete
				+ ",\n\t giftList=" + giftList
				+ ",\n\t OXQuizList=" + OXQuizList
				+ ",\n]";
	}
	
	
}
