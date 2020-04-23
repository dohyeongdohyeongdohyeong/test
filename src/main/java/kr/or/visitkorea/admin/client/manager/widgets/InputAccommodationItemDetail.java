package kr.or.visitkorea.admin.client.manager.widgets;

import java.util.HashMap;
import java.util.Map;

import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.json.client.JSONValue;

import gwt.material.design.client.MaterialDesignBase;
import gwt.material.design.client.constants.Display;
import gwt.material.design.client.constants.IconType;
import gwt.material.design.client.constants.TextAlign;
import gwt.material.design.client.ui.MaterialColumn;
import gwt.material.design.client.ui.MaterialLink;
import gwt.material.design.client.ui.MaterialPanel;
import gwt.material.design.client.ui.MaterialRow;
import kr.or.visitkorea.admin.client.manager.contents.database.DatabaseApplication;
import kr.or.visitkorea.admin.client.manager.contents.database.DatabaseContentType;

public class InputAccommodationItemDetail extends MaterialPanel {

    static {
        MaterialDesignBase.injectCss(ManagerWidgetBundle.INSTANCE.getFrameCss());
    }

	private JSONObject jsonObject;
	private InputAccommodationItem inputAccommodationItem;
	private int index;
	private int listSize;

	public InputAccommodationItemDetail(JSONObject jsonValue, InputAccommodationItem inputAccommodationItem) {
		super();
		this.jsonObject = jsonValue;
		this.inputAccommodationItem = inputAccommodationItem;
		this.index = this.inputAccommodationItem.getIndex(jsonValue);
		this.listSize = this.inputAccommodationItem.getListSize();
		init();
	}
	
	private void init() {
		
		this.setLayoutPosition(Position.RELATIVE);
		this.setPaddingLeft(20);
		this.setPaddingTop(10);
		this.setPaddingBottom(10);
		this.setPaddingRight(20);
		this.setMinHeight("340px");
		this.setBorderBottom("1px solid #efefef");
		
		
		MaterialRow headerRow = new MaterialRow();
		headerRow.setBorderBottom("1px solid #efefef");
		add(headerRow);
		
		MaterialColumn headerColumn = new MaterialColumn();
		headerColumn.setGrid("s6");
		headerColumn.setTextAlign(TextAlign.LEFT);
		headerRow.add(headerColumn);
		
		MaterialColumn controlColumn = new MaterialColumn();
		controlColumn.setGrid("s6");
		controlColumn.setTextAlign(TextAlign.RIGHT);
		headerRow.add(controlColumn);
		
		MaterialRow row = new MaterialRow();
		add(row);
		
		MaterialColumn imageColumn = new MaterialColumn();
		imageColumn.setGrid("s4");
		
		MaterialColumn conentColumn = new MaterialColumn();
		conentColumn.setGrid("s8");
		
		row.add(imageColumn);
		row.add(conentColumn);
		
		// 룸 이미지 컴포넌트
		ContentDetailAccommodationItem cdci = new ContentDetailAccommodationItem("룸 이미지", DatabaseContentType.INPUT_IMAGES, this, new String[] {"ROOM_IMG1","ROOM_IMG2","ROOM_IMG3","ROOM_IMG4","ROOM_IMG5"});
		imageColumn.add(cdci);
		
		// 일반 정보
		ContentDetailAccommodationItem ROOM_TITLE = new ContentDetailAccommodationItem("룸명", DatabaseContentType.INPUT_HTML, this, "ROOM_TITLE");
		ROOM_TITLE.setPaddingBottom(20);
		conentColumn.add(ROOM_TITLE);
		
		// 안내
		ContentDetailAccommodationItem ROOM_INTRO = new ContentDetailAccommodationItem("안내", DatabaseContentType.INPUT_HTML, this, "ROOM_INTRO");
		ROOM_INTRO.setPaddingBottom(20);
		conentColumn.add(ROOM_INTRO);
		
		ContentDetailAccommodationItem ROOM_SIZE1 = new ContentDetailAccommodationItem("크기(평)", DatabaseContentType.INPUT_HTML, this, "ROOM_SIZE1");
		ROOM_SIZE1.setPaddingBottom(20);
		conentColumn.add(ROOM_SIZE1);
		
		ContentDetailAccommodationItem ROOM_BASE_COUNT = new ContentDetailAccommodationItem("기준 인원", DatabaseContentType.INPUT_HTML, this, "ROOM_BASE_COUNT");
		ROOM_BASE_COUNT.setPaddingBottom(20);
		conentColumn.add(ROOM_BASE_COUNT);
		
		ContentDetailAccommodationItem ROOM_MAX_COUNT = new ContentDetailAccommodationItem("기준 최대 인원", DatabaseContentType.INPUT_HTML, this, "ROOM_MAX_COUNT");
		ROOM_MAX_COUNT.setPaddingBottom(20);
		conentColumn.add(ROOM_MAX_COUNT);
		
		
		
		// 비수기, 성수기 요금
		ContentDetailAccommodationItem ROOM_OFF_SEASON_MIN_FEE1 = new ContentDetailAccommodationItem("비수기 주중 최소 요금", DatabaseContentType.INPUT_HTML, this, "ROOM_OFF_SEASON_MIN_FEE1");
		ROOM_OFF_SEASON_MIN_FEE1.setPaddingBottom(20);
		conentColumn.add(ROOM_OFF_SEASON_MIN_FEE1);
		
		ContentDetailAccommodationItem ROOM_OFF_SEASON_MIN_FEE2 = new ContentDetailAccommodationItem("비수기 주말 최소 요금", DatabaseContentType.INPUT_HTML, this, "ROOM_OFF_SEASON_MIN_FEE2");
		ROOM_OFF_SEASON_MIN_FEE2.setPaddingBottom(20);
		conentColumn.add(ROOM_OFF_SEASON_MIN_FEE2);
		
		ContentDetailAccommodationItem ROOM_PEAK_SEASON_MIN_FEE1 = new ContentDetailAccommodationItem("성수기 주중 최소 요금", DatabaseContentType.INPUT_HTML, this, "ROOM_PEAK_SEASON_MIN_FEE1");
		ROOM_PEAK_SEASON_MIN_FEE1.setPaddingBottom(20);
		conentColumn.add(ROOM_PEAK_SEASON_MIN_FEE1);
		
		ContentDetailAccommodationItem ROOM_PEAK_SEASON_MIN_FEE2 = new ContentDetailAccommodationItem("성수기 주말 최소 요금", DatabaseContentType.INPUT_HTML, this, "ROOM_PEAK_SEASON_MIN_FEE2");
		ROOM_PEAK_SEASON_MIN_FEE2.setPaddingBottom(20);
		conentColumn.add(ROOM_PEAK_SEASON_MIN_FEE2);
		
		
		// 시설 섹션
		ContentDetailAccommodationItem ROOM_BATH_FACILITY = new ContentDetailAccommodationItem("목욕 시설 여부", DatabaseContentType.INPUT_BOOLEAN, this, "ROOM_BATH_FACILITY");
		ROOM_BATH_FACILITY.setPaddingBottom(20);
		conentColumn.add(ROOM_BATH_FACILITY);
		
		ContentDetailAccommodationItem ROOM_BATH = new ContentDetailAccommodationItem("욕조 설치 여부", DatabaseContentType.INPUT_BOOLEAN, this, "ROOM_BATH");
		ROOM_BATH.setPaddingBottom(20);
		conentColumn.add(ROOM_BATH);
		
		ContentDetailAccommodationItem ROOM_HOME_THEATER = new ContentDetailAccommodationItem("홈시어터 설치 여부 ", DatabaseContentType.INPUT_BOOLEAN, this, "ROOM_HOME_THEATER");
		ROOM_HOME_THEATER.setPaddingBottom(20);
		conentColumn.add(ROOM_HOME_THEATER);
		
		ContentDetailAccommodationItem ROOM_AIR_CONDITIONER = new ContentDetailAccommodationItem("에어컨 설치 여부", DatabaseContentType.INPUT_BOOLEAN, this, "ROOM_AIR_CONDITIONER");
		ROOM_AIR_CONDITIONER.setPaddingBottom(20);
		conentColumn.add(ROOM_AIR_CONDITIONER);
		
		ContentDetailAccommodationItem ROOM_TV = new ContentDetailAccommodationItem("TV 설치 여부", DatabaseContentType.INPUT_BOOLEAN, this, "ROOM_TV");
		ROOM_TV.setPaddingBottom(20);
		conentColumn.add(ROOM_TV);
		
		ContentDetailAccommodationItem ROOM_PC = new ContentDetailAccommodationItem("PC 설치 여부", DatabaseContentType.INPUT_BOOLEAN, this, "ROOM_PC");
		ROOM_PC.setPaddingBottom(20);
		conentColumn.add(ROOM_PC);
		
		ContentDetailAccommodationItem ROOM_CABLE = new ContentDetailAccommodationItem("케이블 방송 설치 여부", DatabaseContentType.INPUT_BOOLEAN, this, "ROOM_CABLE");
		ROOM_CABLE.setPaddingBottom(20);
		conentColumn.add(ROOM_CABLE);
		
		ContentDetailAccommodationItem ROOM_INTERNET = new ContentDetailAccommodationItem("인터넷 설치 여부", DatabaseContentType.INPUT_BOOLEAN, this, "ROOM_INTERNET");
		ROOM_INTERNET.setPaddingBottom(20);
		conentColumn.add(ROOM_INTERNET);
		
		ContentDetailAccommodationItem ROOM_REFRIGERATOR = new ContentDetailAccommodationItem("냉장고 설치 여부", DatabaseContentType.INPUT_BOOLEAN, this, "ROOM_REFRIGERATOR");
		ROOM_REFRIGERATOR.setPaddingBottom(20);
		conentColumn.add(ROOM_REFRIGERATOR);
		
		ContentDetailAccommodationItem ROOM_TOILETRIES = new ContentDetailAccommodationItem("세면 도구 비치 여부", DatabaseContentType.INPUT_BOOLEAN, this, "ROOM_TOILETRIES");
		ROOM_TOILETRIES.setPaddingBottom(20);
		conentColumn.add(ROOM_TOILETRIES);
		
		ContentDetailAccommodationItem ROOM_SOFA = new ContentDetailAccommodationItem("소파 설치 여부", DatabaseContentType.INPUT_BOOLEAN, this, "ROOM_SOFA");
		ROOM_SOFA.setPaddingBottom(20);
		conentColumn.add(ROOM_SOFA);
		
		ContentDetailAccommodationItem ROOM_COOK = new ContentDetailAccommodationItem("취사용품 비치 여부", DatabaseContentType.INPUT_BOOLEAN, this, "ROOM_COOK");
		ROOM_COOK.setPaddingBottom(20);
		conentColumn.add(ROOM_COOK);
		
		ContentDetailAccommodationItem ROOM_TABLE = new ContentDetailAccommodationItem("테이블 설치 여부", DatabaseContentType.INPUT_BOOLEAN, this, "ROOM_TABLE");
		ROOM_TABLE.setPaddingBottom(20);
		conentColumn.add(ROOM_TABLE);
		
		ContentDetailAccommodationItem ROOM_HAIRDRYER = new ContentDetailAccommodationItem("드라이기 설치 여부", DatabaseContentType.INPUT_BOOLEAN, this, "ROOM_HAIRDRYER");
		ROOM_HAIRDRYER.setPaddingBottom(20);
		conentColumn.add(ROOM_HAIRDRYER);
		
		
	}
	
	public JSONValue getValue(String key) {
		return this.jsonObject.get(key);
	}
	
	public void setValue(String key, String value) {
		this.jsonObject.put(key, new JSONString(value));
	}
	
	public String getCotId() {
		return this.jsonObject.get("COT_ID").isString().stringValue();
	}

	public String getSubContentId() {
		return this.jsonObject.get("SUB_CONTENT_ID").isString().stringValue();
	}

}
