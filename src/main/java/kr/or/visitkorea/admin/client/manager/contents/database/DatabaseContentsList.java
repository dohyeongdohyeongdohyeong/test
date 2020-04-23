package kr.or.visitkorea.admin.client.manager.contents.database;

import java.util.HashMap;
import java.util.Map;

import com.google.gwt.dom.client.Style.Float;
import com.google.gwt.dom.client.Style.FontWeight;
import com.google.gwt.dom.client.Style.Overflow;
import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONNumber;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONString;

import gwt.material.design.addins.client.combobox.MaterialComboBox;
import gwt.material.design.client.MaterialDesignBase;
import gwt.material.design.client.constants.Color;
import gwt.material.design.client.constants.IconType;
import gwt.material.design.client.constants.InputType;
import gwt.material.design.client.constants.TextAlign;
import gwt.material.design.client.ui.MaterialButton;
import gwt.material.design.client.ui.MaterialIcon;
import gwt.material.design.client.ui.MaterialLabel;
import gwt.material.design.client.ui.MaterialListBox;
import gwt.material.design.client.ui.MaterialPanel;
import gwt.material.design.client.ui.MaterialTextBox;
import gwt.material.design.client.ui.MaterialToast;
import gwt.material.design.jquery.client.api.Functions.Func3;
import gwt.material.design.jscore.client.api.JSON;
import gwt.material.design.jscore.client.api.JsObject;
import kr.or.visitkorea.admin.client.application.Console;
import kr.or.visitkorea.admin.client.application.Registry;
import kr.or.visitkorea.admin.client.manager.VisitKoreaBusiness;
import kr.or.visitkorea.admin.client.manager.contents.recommand.RecommApplication;
import kr.or.visitkorea.admin.client.manager.widgets.AbstractContentPanel;
import kr.or.visitkorea.admin.client.manager.widgets.ContentTable;
import kr.or.visitkorea.admin.client.manager.widgets.ContentTableRow;
import kr.or.visitkorea.admin.client.widgets.window.MaterialExtentsWindow;

public class DatabaseContentsList extends AbstractContentPanel {

	static {
		MaterialDesignBase.injectCss(DatabaseContentBundle.INSTANCE.contentCss());
	}

	private MaterialListBox listBox;
	private ContentTable table;

	
	private int offset = 0;
	private int totcnt=0;
	
	private MaterialTextBox sdate, edate; 
	private MaterialComboBox<Object> areatype, sigungutype;
	private MaterialComboBox<Object> dateopt, mode;
	private MaterialTextBox edidname;
	private String OTD_ID;
	private MaterialLabel countLabel;
	private MaterialIcon insertIcon;
	
	public DatabaseContentsList(MaterialExtentsWindow meWindow) {
		super(meWindow);
	}
	
	public DatabaseContentsList getPanel() {
		return this;
	}

	@Override
	public void init() {
		this.setLayoutPosition(Position.RELATIVE);
		this.setTextAlign(TextAlign.CENTER);
		this.setFloat(com.google.gwt.dom.client.Style.Float.LEFT);
		this.setHeight("100%");
		this.setOverflow(Overflow.HIDDEN);
		
		MaterialPanel mrtop = new MaterialPanel();
		mrtop.setWidth("100%");	mrtop.setHeight("80px");
		mrtop.setPadding(10);
		this.add(mrtop);
		
		mode = new MaterialComboBox<>();
		mode.setLabel("검색조건");
		mode.setLayoutPosition(Position.ABSOLUTE);
		mode.setTop(5); mode.setLeft(10); mode.setWidth("120px");
		mode.addItem("제목", 0);
		mode.addItem("CID", 1);
		mode.addItem("태그", 2);
		mode.setSelectedIndex(0);
		mrtop.add(mode);
		mode.addValueChangeHandler(e->{
			if(mode.getSelectedIndex() == 0) {
				edidname.setLabel("제목입력");
			} else if(mode.getSelectedIndex() == 1) {
				edidname.setLabel("CID입력");
			} else {
				edidname.setLabel("태그입력");
			}
		});
		edidname = new MaterialTextBox();
		edidname.setLabel("제목입력");
		edidname.setLayoutPosition(Position.ABSOLUTE);
		edidname.setTop(5);	edidname.setLeft(140); edidname.setWidth("300px");
		mrtop.add(edidname);
		edidname.addKeyPressHandler(event -> {
			if (event.getNativeEvent().getKeyCode() == KeyCodes.KEY_ENTER) {
				qryList(true);
			}
		});
		
		dateopt = new MaterialComboBox<>();
		dateopt.setLabel("날짜옵션");
		dateopt.setLayoutPosition(Position.ABSOLUTE);
		dateopt.setTop(5); dateopt.setLeft(480); dateopt.setWidth("120px");
//		dateopt.addItem("선택안함", 0);
		dateopt.addItem("생성일", 0);
		dateopt.addItem("수정일", 1);
		dateopt.setSelectedIndex(0);
		mrtop.add(dateopt);
		
		sdate = new MaterialTextBox();
		sdate.setType(InputType.DATE);
		sdate.setLayoutPosition(Position.ABSOLUTE);
		sdate.setLabel("기간"); sdate.setText("-");
		sdate.setTop(5); sdate.setLeft(620); sdate.setWidth("150px");
		mrtop.add(sdate);
		edate = new MaterialTextBox();
		edate.setType(InputType.DATE);
		edate.setLayoutPosition(Position.ABSOLUTE);
		edate.setTop(5); edate.setLeft(780); edate.setWidth("150px");
		mrtop.add(edate);
		
		areatype = new MaterialComboBox<>();
		areatype.setLabel("지자체선택");
		areatype.setLayoutPosition(Position.ABSOLUTE);
		areatype.setTop(5); areatype.setLeft(950); areatype.setWidth("140px");
		mrtop.add(areatype);
		areatype.addItem("전체", 0);
		JSONObject parameterJSON = new JSONObject();
		parameterJSON.put("cmd", new JSONString("SELECT_AREA"));
		//VK.post("call", parameterJSON.toString(), new Func3<Object, String, Object>() {
		VisitKoreaBusiness.post("call", parameterJSON.toString(), new Func3<Object, String, Object>() {
			@Override
			public void call(Object param1, String param2, Object param3) {
				JSONObject resultObj = (JSONObject) JSONParser.parseStrict(JSON.stringify((JsObject)param1));
				JSONObject headerObj = (JSONObject) resultObj.get("header");
				String processResult = headerObj.get("process").isString().stringValue();
				if (processResult.equals("success")) {
					JSONObject bodyObj = (JSONObject) resultObj.get("body");
					JSONArray bodyResultObj = (JSONArray) bodyObj.get("result");
					
					int usrCnt = bodyResultObj.size();
					if (usrCnt == 0) {
						MaterialToast.fireToast("시도 검색된 결과가 없습니다.", 3000);
					}
					
					for(int i= 0;i< usrCnt;i++) {
						JSONObject obj = (JSONObject)bodyResultObj.get(i);
						areatype.addItem(obj.get("name").isString().stringValue(), obj.get("code").isString().stringValue()+"^"+obj.get("name").isString().stringValue());
					}
					areatype.setSelectedIndex(0);
				}
			}
		});
		areatype.addValueChangeHandler(ee->{
			sigungutype.clear();
			JSONObject paramJSON = new JSONObject();
			paramJSON.put("cmd", new JSONString("SELECT_AREA_SIGUNGU"));
			String areacode = areatype.getValues().get(areatype.getSelectedIndex()).toString().split("\\^")[0];
			paramJSON.put("areacode", new JSONString(areacode));
			//VK.post("call", paramJSON.toString(), new Func3<Object, String, Object>() {
			VisitKoreaBusiness.post("call", paramJSON.toString(), new Func3<Object, String, Object>() {
				@Override
				public void call(Object param1, String param2, Object param3) {
					JSONObject resultObj = (JSONObject) JSONParser.parseStrict(JSON.stringify((JsObject)param1));
					JSONObject headerObj = (JSONObject) resultObj.get("header");
					String processResult = headerObj.get("process").isString().stringValue();
					if (processResult.equals("success")) {
						JSONObject bodyObj = (JSONObject) resultObj.get("body");
						JSONArray bodyResultObj = (JSONArray) bodyObj.get("result");
						int usrCnt = bodyResultObj.size();
						for(int i= 0;i< usrCnt;i++) {
							JSONObject obj = (JSONObject)bodyResultObj.get(i);
							if(obj.get("code").isString().stringValue().equals("0")) {
								sigungutype.addItem("전체", 0);
							} else
								sigungutype.addItem(obj.get("sigungu").isString().stringValue(), obj.get("code").isString().stringValue()+"^"+obj.get("sigungu").isString().stringValue());
						}
						sigungutype.setSelectedIndex(0);
						qryList(true);
					}
				}
			});
		});
		
		sigungutype = new MaterialComboBox<>();
		sigungutype.setLayoutPosition(Position.ABSOLUTE);
		sigungutype.setTop(5); sigungutype.setLeft(1100); sigungutype.setWidth("140px");
		this.add(sigungutype);
		sigungutype.addValueChangeHandler(ee->{
			qryList(true);
		});
		
		listBox = new MaterialListBox();
		listBox.setPlaceholder("분류");
		listBox.setLayoutPosition(Position.ABSOLUTE);
		listBox.setTop(5); listBox.setLeft(1270); listBox.setWidth("150px");
		listBox.addItem("전체");
		listBox.addItem("관광지");
		listBox.addItem("문화시설");
		listBox.addItem("레포츠");
		listBox.addItem("숙박");
		listBox.addItem("쇼핑");
		listBox.addItem("음식점");
		listBox.addItem("여행코스");
		listBox.addItem("축제공연행사");
		listBox.addItem("생태관광");
		listBox.addItem("시티투어");
		
		listBox.addValueChangeHandler(event->{
			if (listBox.getSelectedIndex() == 8) {
				table.clearRows();
				countLabel.setText("0 건");
				table.removeTitleAll();
				table.appendTitle("CID", 80, TextAlign.RIGHT);
				table.appendTitle("서비스 분류", 110, TextAlign.CENTER);
				table.appendTitle("지역", 80, TextAlign.CENTER);
				table.appendTitle("시군구", 80, TextAlign.CENTER);
				table.appendTitle("제목", 220, TextAlign.LEFT);
				table.appendTitle("대표태그", 100, TextAlign.CENTER);
				table.appendTitle("처리상태", 90, TextAlign.CENTER);
				table.appendTitle("기간", 230, TextAlign.CENTER);
				table.appendTitle("수정일", 165, TextAlign.CENTER);
				table.appendTitle("생성일", 165, TextAlign.CENTER);
				table.appendTitle("작성자", 130, TextAlign.CENTER);
			}else {
				table.clearRows();
				countLabel.setText("0 건");
				table.removeTitleAll();
				table.appendTitle("CID", 80, TextAlign.RIGHT);
				table.appendTitle("서비스 분류", 110, TextAlign.CENTER); 
				table.appendTitle("지역", 80, TextAlign.CENTER);
				table.appendTitle("시군구", 80, TextAlign.CENTER);
				table.appendTitle("제목", 400, TextAlign.LEFT);
				table.appendTitle("대표태그", 150, TextAlign.CENTER);
				table.appendTitle("처리상태", 90, TextAlign.CENTER);
				table.appendTitle("수정일", 165, TextAlign.CENTER);
				table.appendTitle("생성일", 165, TextAlign.CENTER);
				table.appendTitle("작성자", 130, TextAlign.CENTER);
			}
			
			qryList(true);
		});
		
		mrtop.add(listBox);

		table = new ContentTable(ContentTable.TABLE_SELECT_TYPE.SELECT_SINGLE);
		table.setLayoutPosition(Position.ABSOLUTE);
		table.setTop(40);table.setWidth("98.5%"); table.setHeight(595); table.setMargin(10);
		this.add(table);
		
		table.appendTitle("CID", 80, TextAlign.RIGHT);
		table.appendTitle("서비스 분류", 110, TextAlign.CENTER); 
		table.appendTitle("지역", 80, TextAlign.CENTER);
		table.appendTitle("시군구", 80, TextAlign.CENTER);
		table.appendTitle("제목", 400, TextAlign.LEFT);
		table.appendTitle("대표태그", 150, TextAlign.CENTER);
		table.appendTitle("처리상태", 90, TextAlign.CENTER);
		table.appendTitle("수정일", 165, TextAlign.CENTER);
		table.appendTitle("생성일", 165, TextAlign.CENTER);
		table.appendTitle("작성자", 130, TextAlign.CENTER);
		
		
		MaterialButton EXCELBUTTON = new MaterialButton("엑셀 다운로드");
		EXCELBUTTON.addClickHandler(event->{
			
			Map<String, Object> paramters = new HashMap<String, Object>();
			
			if (OTD_ID != null && OTD_ID != "") {
				paramters.put("OTD_ID", OTD_ID);
			}
			
			getMaterialExtentsWindow().openDialog(DatabaseApplication.DB_EXCEL_DOWNLOAD, paramters, 900);
			
		});
		table.getButtomMenu().addButton(EXCELBUTTON, com.google.gwt.dom.client.Style.Float.LEFT);
		
		
		insertIcon = new MaterialIcon(IconType.ADD);
		insertIcon.setTextAlign(TextAlign.CENTER);
		insertIcon.addClickHandler(event -> {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("OTD_ID", OTD_ID);
			map.put("IS_ADMIN", this.getWindowParameters().get("IS_ADMIN"));
			
			getMaterialExtentsWindow().openDialog(DatabaseApplication.INSERT_DATABASE_CONTENT, map, 800);
		});
		table.getTopMenu().addIcon(insertIcon, "추가", Float.RIGHT, "1.8em", "26px", 24, false);
		
		MaterialIcon searchIcon = new MaterialIcon(IconType.SEARCH);
		searchIcon.setTextAlign(TextAlign.CENTER);
		searchIcon.addClickHandler(event->{
			qryList(true);
		});
		table.getTopMenu().addIcon(searchIcon, "검색", Float.RIGHT, "1.8em", "26px", 24, false);
		
		MaterialIcon nextRowsIcon = new MaterialIcon(IconType.ARROW_DOWNWARD);
		nextRowsIcon.setTextAlign(TextAlign.CENTER);
		nextRowsIcon.addClickHandler(event->{
			if((offset+20) >= totcnt) {
				MaterialToast.fireToast("더이상의 결과 값이 없습니다.", 3000);
			} else {
				qryList(false);
			}
		});
		table.getButtomMenu().addIcon(nextRowsIcon, "다음 20개", com.google.gwt.dom.client.Style.Float.RIGHT);
		
		countLabel = new MaterialLabel("0 건");
		countLabel.setFontWeight(FontWeight.BOLDER);
		countLabel.setTextColor(Color.BLACK);
		table.getButtomMenu().addLabel(countLabel, com.google.gwt.dom.client.Style.Float.RIGHT);
	}
	
	@Override
	public void onLoad() {
		super.onLoad();
		
		if (!this.getWindowParameters().containsKey("IS_ADMIN"))
			this.OTD_ID = this.getWindowParameters().get("OTD_ID").toString();
		
		
		insertIcon.setVisible(Registry.getPermission("aab04d13-9410-11e9-ba55-e0cb4e4f79cd"));
		
		qryList(true);
	}

	public void qryList(boolean bstart) {
		if(bstart) {
			offset = 0; 
			table.clearRows();
		} else offset += 20;
		
		String keyWord = edidname.getText().replaceAll("\\\\", "");
		int moden = mode.getSelectedIndex();
		if(moden < 0) moden = 0;
		JSONObject parameterJSON = new JSONObject();
		parameterJSON.put("cmd", new JSONString("DATABASE_LIST"));
		parameterJSON.put("mode", new JSONNumber(moden));
		parameterJSON.put("contentPart", new JSONNumber(listBox.getSelectedIndex()));
		parameterJSON.put("keyword", new JSONString(keyWord));
		if (OTD_ID != null) {
			parameterJSON.put("OTD_ID", new JSONString(OTD_ID));
		}
		
		// 생성일, 수정일 구분
		parameterJSON.put("dateType", new JSONNumber(dateopt.getSelectedIndex()));
		
		// 기간 텍스트
		String strsdate = sdate.getText().toString();
		if(!"".equals(strsdate)) {
			parameterJSON.put("startInput", new JSONString(strsdate));
		}
		String stredate = edate.getText().toString();
		if(!"".equals(stredate)) {
			parameterJSON.put("endInput", new JSONString(stredate+" 23:59:59"));
		}
		// 지역 구분
		if(areatype.getSelectedIndex()>0) {
			String areacode = areatype.getValues().get(areatype.getSelectedIndex()).toString().split("\\^")[0];
			parameterJSON.put("areaCode", new JSONString(areacode));
			if(!sigungutype.getValues().get(sigungutype.getSelectedIndex()).toString().equals("0")) {
				String sigungucode = sigungutype.getValues().get(sigungutype.getSelectedIndex()).toString().split("\\^")[0];
				parameterJSON.put("areaSubCode", new JSONString(sigungucode));
			}
		}
		
		parameterJSON.put("offset", new JSONString(offset+""));
		
		table.loading(true);
		
		VisitKoreaBusiness.post("call", parameterJSON.toString(), new Func3<Object, String, Object>() {
			@Override
			public void call(Object param1, String param2, Object param3) {
				JSONObject resultObj = (JSONObject) JSONParser.parseStrict(JSON.stringify((JsObject)param1));
				JSONObject headerObj = (JSONObject) resultObj.get("header");
				String processResult = headerObj.get("process").isString().stringValue();
				if (processResult.equals("success")) {
					JSONObject bodyObj = (JSONObject) resultObj.get("body");
					JSONArray bodyResultObj = (JSONArray) bodyObj.get("result");
					JSONObject bodyResultcnt = (JSONObject) bodyObj.get("resultCnt");
					countLabel.setText(bodyResultcnt.get("CNT")+" 건");
					totcnt = Integer.parseInt(bodyResultcnt.get("CNT").toString());
					
					int usrCnt = bodyResultObj.size();
					if (usrCnt == 0) {
						MaterialToast.fireToast("검색된 결과가 없습니다.", 3000);
					}
					
					for(int i= 0;i< usrCnt;i++) {
						JSONObject recObj = bodyResultObj.get(i).isObject();
						String ctype = getContentType(recObj, "CONTENT_TYPE");
						if (!ctype.equals("미분류")) {
							if (listBox.getSelectedIndex() != 8) {
								ContentTableRow tableRow = table.addRow(Color.WHITE, new int[]{4},
										getString(recObj, "CONTENT_ID"),
										ctype,
										getString(recObj, "AREA_NAME"),
										getString(recObj, "AREA_SUB_NAME"),
										getString(recObj, "TITLE"),
										getString(recObj, "MASTER_TAG"),
										getString(recObj, "CONTENT_STATUS"),
										getString(recObj, "MODIFIED_DATE"),
										getString(recObj, "CREATE_DATE"),
										getString(recObj, "USER_NAME")
									);
								
								
								tableRow.put("RETOBJ", recObj);
								tableRow.addClickHandler(event->{
									ContentTableRow ctr = (ContentTableRow)event.getSource();
									if(ctr.getSelectedColumn() == 4) {
										DatabaseContentsTree cPanel = (DatabaseContentsTree)Registry.put("DatabaseContentsTree");
										cPanel.setTitle(recObj.get("TITLE").isString().stringValue());
										if(OTD_ID != null)
											cPanel.setOtdId(OTD_ID);
										cPanel.setCotId(getString(recObj, "COT_ID"));
										cPanel.setRow(tableRow);
										cPanel.goTree(1);
										cPanel.go(0);
										cPanel.loading(); 
										
										getMaterialExtentsWindow().goContentSlider(getMaterialExtentsWindow().getWidth() * 1 * -1);
									}
								});
							} else {
								ContentTableRow tableRow = table.addRow(Color.WHITE, new int[]{4},
										getString(recObj, "CONTENT_ID"),
										getContentType(recObj, "CONTENT_TYPE"),
										getString(recObj, "AREA_NAME"),
										getString(recObj, "AREA_SUB_NAME"),
										getString(recObj, "TITLE"),
										getString(recObj, "MASTER_TAG"),
										getString(recObj, "CONTENT_STATUS"),
										getString(recObj, "EVENT_DATE"),
										getString(recObj, "MODIFIED_DATE"),
										getString(recObj, "CREATE_DATE"),
										getString(recObj, "USER_NAME"));
								tableRow.put("RETOBJ", recObj);
								//2020-02-21(doubleclick -> oneclick)
								tableRow.addClickHandler(event->{
									ContentTableRow ctr = (ContentTableRow)event.getSource();
									if(ctr.getSelectedColumn() == 4) {
										DatabaseContentsTree cPanel = (DatabaseContentsTree)Registry.put("DatabaseContentsTree");
										cPanel.setTitle(recObj.get("TITLE").isString().stringValue());
										if(OTD_ID != null)
											cPanel.setOtdId(OTD_ID);
										cPanel.setCotId(getString(recObj, "COT_ID"));
										cPanel.setRow(tableRow);
										cPanel.goTree(1);
										cPanel.go(0);
										cPanel.loading();
										
										getMaterialExtentsWindow().goContentSlider(getMaterialExtentsWindow().getWidth() * 1 * -1);
									}
								});
							}
						}
					}
				}else if (processResult.equals("fail")) {
					getMaterialExtentsWindow().alert("검색이 실패했습니다. 관리자에게 문의하세요.", 500);
					countLabel.setText("실패?");
				}
				table.loading(false);
			}
		});
	}
	
	private String getContentType(JSONObject recObj, String string) {
		
		if (recObj.get(string) != null) {
			
			double contentType = recObj.get(string).isNumber().doubleValue();
		
			if (contentType == 12) {
				return "관광지";
			}else if (contentType == 14) {
				return "문화시설";
			}else if (contentType == 15) {
				return "축제공연행사";
			}else if (contentType == 25) {
				return "여행코스";
			}else if (contentType == 28) {
				return "레포츠";
			}else if (contentType == 32) {
				return "숙박";
			}else if (contentType == 38) {
				return "쇼핑";
			}else if (contentType == 39) {
				return "음식점";
			}else if (contentType == 2000) {
				return "생태관광";
			}else if (contentType == 25000) {
				return "시티투어";
			}
			
		}
		return "미분류";
	}
}
