  package kr.or.visitkorea.admin.client.manager.contents.recommand;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.google.gwt.dom.client.Style;
import com.google.gwt.dom.client.Style.Float;
import com.google.gwt.dom.client.Style.FontWeight;
import com.google.gwt.dom.client.Style.Overflow;
import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.i18n.client.DateTimeFormat;
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
import gwt.material.design.client.ui.MaterialPanel;
import gwt.material.design.client.ui.MaterialTextBox;
import gwt.material.design.client.ui.MaterialToast;
import gwt.material.design.jquery.client.api.Functions.Func3;
import gwt.material.design.jscore.client.api.JSON;
import gwt.material.design.jscore.client.api.JsObject;
import kr.or.visitkorea.admin.client.application.Console;
import kr.or.visitkorea.admin.client.application.Registry;
import kr.or.visitkorea.admin.client.manager.VisitKoreaBusiness;
import kr.or.visitkorea.admin.client.manager.widgets.AbstractContentPanel;
import kr.or.visitkorea.admin.client.manager.widgets.ContentTable;
import kr.or.visitkorea.admin.client.manager.widgets.ContentTableRow;
import kr.or.visitkorea.admin.client.manager.widgets.IDUtil;
import kr.or.visitkorea.admin.client.widgets.window.MaterialExtentsWindow;

public class RecommContentsList extends AbstractContentPanel {

	static {
		MaterialDesignBase.injectCss(RecommContentBundle.INSTANCE.contentCss());
	}

//	private ContentMenu pager;
	private ContentTable table;
//	private MaterialLabel titleLabel;
	private int offset = 0;
//	private int rowCount = 20;
	private int totcnt=0;
//	private int index =0;
	
	private MaterialTextBox sdate, edate; 
	private MaterialComboBox<Object> areatype; //, sigungutype;
	private MaterialComboBox<Object> dateopt, mode;
	private MaterialTextBox edidname;
	private String OTD_ID;
	private MaterialLabel countLabel;
	private MaterialComboBox<Object> category;
	private MaterialComboBox<Object> division;
	
	public RecommContentsList() {
		super();
	}
	
	public RecommContentsList(AbstractContentPanel panel) {
		super(panel);
	}
	
	public RecommContentsList(String... initialClass) {
		super(initialClass);
	}
	
	public RecommContentsList(MaterialExtentsWindow meWindow) {
		super(meWindow);
	}

	
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
		mode.setLabel("검색");
		mode.setLayoutPosition(Position.ABSOLUTE);
		mode.setTop(5); mode.setLeft(10); mode.setWidth("120px");
		mode.addItem("제목", 0);
		mode.addItem("CID", 1);
		mode.addItem("태그", 2);
		mode.addItem("작성자", 3);
		mode.setSelectedIndex(0);
		mrtop.add(mode);
		mode.addValueChangeHandler(e->{
			if(mode.getSelectedIndex() == 0) {
				edidname.setLabel("제목입력");
			} else if(mode.getSelectedIndex() == 1) {
				edidname.setLabel("CID입력");
			} else if(mode.getSelectedIndex() == 2) {
				edidname.setLabel("태그입력");
			} else {
				edidname.setLabel("작성자입력");
			}
		});
		edidname = new MaterialTextBox();
		edidname.setLabel("제목입력");
		edidname.setLayoutPosition(Position.ABSOLUTE);
		edidname.setTop(5);	edidname.setLeft(150); edidname.setWidth("300px");
		mrtop.add(edidname);
		edidname.addKeyPressHandler(event -> {
			if (event.getNativeEvent().getKeyCode() == KeyCodes.KEY_ENTER) {
				qryList(true);
			}
		});
		
		dateopt = new MaterialComboBox<>();
		dateopt.setLabel("날짜옵션");
		dateopt.setLayoutPosition(Position.ABSOLUTE);
		dateopt.setTop(5); dateopt.setLeft(470); dateopt.setWidth("120px");
//		dateopt.addItem("선택안함", 0);
		dateopt.addItem("생성일", 0);
		dateopt.addItem("최종 수정일", 1);
		dateopt.setSelectedIndex(0);
		mrtop.add(dateopt);
		
		sdate = new MaterialTextBox();
		sdate.setType(InputType.DATE);
		sdate.setLayoutPosition(Position.ABSOLUTE);
		sdate.setLabel("기간"); sdate.setText("-");
		sdate.setTop(5); sdate.setLeft(610); sdate.setWidth("130px");
		mrtop.add(sdate);
		edate = new MaterialTextBox();
		edate.setType(InputType.DATE);
		edate.setLayoutPosition(Position.ABSOLUTE);
		edate.setTop(5); edate.setLeft(760); edate.setWidth("130px");
		mrtop.add(edate);
		
		areatype = new MaterialComboBox<>();
		areatype.setLabel("지자체선택");
		areatype.setLayoutPosition(Position.ABSOLUTE);
		areatype.setTop(5); areatype.setLeft(910); areatype.setWidth("150px");
		mrtop.add(areatype);
		areatype.addItem("전체", 0);
		JSONObject parameterJSON = new JSONObject();
		parameterJSON.put("cmd", new JSONString("SELECT_AREA"));
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
						MaterialToast.fireToast("검색된 결과가 없습니다.", 3000);
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
//			sigungutype.clear();
//			JSONObject paramJSON = new JSONObject();
//			paramJSON.put("cmd", new JSONString("SELECT_AREA_SIGUNGU"));
//			String areacode = areatype.getValues().get(areatype.getSelectedIndex()).toString().split("\\^")[0];
//			paramJSON.put("areacode", new JSONString(areacode));
//			VK.post("call", paramJSON.toString(), new Func3<Object, String, Object>() {
//				@Override
//				public void call(Object param1, String param2, Object param3) {
//					JSONObject resultObj = (JSONObject) JSONParser.parseStrict(JSON.stringify((JsObject)param1));
//					JSONObject headerObj = (JSONObject) resultObj.get("header");
//					String processResult = headerObj.get("process").isString().stringValue();
//					if (processResult.equals("success")) {
//						JSONObject bodyObj = (JSONObject) resultObj.get("body");
//						JSONArray bodyResultObj = (JSONArray) bodyObj.get("result");
//						int usrCnt = bodyResultObj.size();
//						for(int i= 0;i< usrCnt;i++) {
//							JSONObject obj = (JSONObject)bodyResultObj.get(i);
//							if(obj.get("code").isString().stringValue().equals("0")) {
//								sigungutype.addItem("전체", 0);
//							} else
//								sigungutype.addItem(obj.get("sigungu").isString().stringValue(), obj.get("code").isString().stringValue()+"^"+obj.get("sigungu").isString().stringValue());
//						}
//						sigungutype.setSelectedIndex(0);
//						qryList(true);
//					}
//				}
//			});
			qryList(true);
		});
		
//		sigungutype = new MaterialComboBox<>();
//		sigungutype.setWidth("200px");
//		sigungutype.setLayoutPosition(Position.ABSOLUTE);
//		sigungutype.setTop(0);
//		sigungutype.setLeft(470);
//		this.add(sigungutype);
//		sigungutype.addValueChangeHandler(ee->{
//			qryList(true);
//		});
		
		
		category = new MaterialComboBox<>();
		category.setLabel("분류");
		category.setLayoutPosition(Position.ABSOLUTE);
		category.setTop(5); category.setLeft(1080); category.setWidth("150px");
		category.addItem("전체", 0);
		mrtop.add(category);
		
		Map<String, Map<String,String>> map =(Map<String, Map<String,String>>) Registry.get("ARTICLE_CATEGORIES");
		
		Set<String> CategoryStringSet = map.keySet();
		ArrayList<String> CategoryStringList = new ArrayList<String>(CategoryStringSet);
		for (String key : CategoryStringList) {
				this.category.addItem(map.get(key).get("VALUE"),key);
		}
		category.addValueChangeHandler(ee->{
			qryList(true);
		});
		
		division = new MaterialComboBox<>();
		division.setLabel("구분");
		division.setLayoutPosition(Position.ABSOLUTE);
		division.setTop(5); division.setLeft(1250); division.setWidth("150px");
		division.addItem("전체", -1);
		division.addItem("일반기사", 0);
		division.addItem("테마기사", 1);
		mrtop.add(division);
		
		division.addValueChangeHandler(ee->{
			qryList(true);
		});
		
		
		
		//2020-03-27 dohyeong 추가.
		MaterialIcon historyIcon = new MaterialIcon(IconType.HISTORY);
		historyIcon.setTooltip("히스토리 관리");
		historyIcon.setFloat(Float.RIGHT);
		historyIcon.setMarginTop(5);
		historyIcon.setMarginRight(8);
		historyIcon.addClickHandler(e -> {
			super.getMaterialExtentsWindow().openDialog(RecommApplication.HISTORY_SEARCH, super.getWindowParameters(), 1490, 660);
		});
		// 품질인증 부서만 사용 가능하게..
//		if(super.getMaterialExtentsWindow().getValueMap().get("OTD_ID").equals("456a84d1-84c4-11e8-8165-020027310001")) {
		mrtop.add(historyIcon);
//		}
		
		
		
		//테스트
		MaterialIcon test = new MaterialIcon(IconType.INSERT_EMOTICON);
		test.setTooltip("테스트");
		test.setFloat(Float.RIGHT);
		test.setMarginTop(5);
		test.setMarginRight(5);
		test.addClickHandler(e -> {super.getMaterialExtentsWindow().openDialog(RecommApplication.HISTORY_MANAGEMENT, super.getWindowParameters(), 1010, 660);
			
		});
		mrtop.add(test);
		
		
		buildLayout();
		qryList(true);
	}

	private void buildLayout() {

		table = new ContentTable(ContentTable.TABLE_SELECT_TYPE.SELECT_SINGLE);
		table.setLayoutPosition(Position.ABSOLUTE);
		table.setTop(40);table.setWidth("98.5%"); table.setHeight(595); table.setMargin(10);
		table.appendTitle("CID/COT", 80, TextAlign.CENTER);
		table.appendTitle("분류", 120, TextAlign.CENTER);
		table.appendTitle("지역", 110, TextAlign.CENTER);
		table.appendTitle("제목", 440, TextAlign.LEFT);
		table.appendTitle("대표태그", 140, TextAlign.CENTER);
		table.appendTitle("처리상태", 120, TextAlign.CENTER);
		table.appendTitle("최종 수정일", 160, TextAlign.CENTER);
		table.appendTitle("생성일", 160, TextAlign.CENTER);
		table.appendTitle("작성자", 120, TextAlign.CENTER);
		
		this.add(table);

		MaterialIcon icon3 = new MaterialIcon(IconType.VIEW_STREAM);
		icon3.setTextAlign(TextAlign.CENTER);
		icon3.addClickHandler(event->{
			if(table.getSelectedRows().size() <=0) return;
			JSONObject retObj = (JSONObject)table.getSelectedRows().get(0).get("RETOBJ");
			if(retObj== null || retObj.get("COT_ID") == null) return;
			String cotId = retObj.get("COT_ID").isString().stringValue();
			if(cotId== null) return;
			Registry.openPreview(icon3, (String) Registry.get("service.server") + "/detail/rem_detail.html?cotid=" + cotId);
		});

		MaterialIcon icon5 = new MaterialIcon(IconType.DELETE);
		icon5.setTextAlign(TextAlign.CENTER);
		icon5.addClickHandler(event->{
			if (table.getSelectedRows().size() > 0) {
				Map<String, Object> paramters = new HashMap<String, Object>();
				JSONObject retObj = (JSONObject) table.getSelectedRows().get(0).get("RETOBJ");
				if(retObj== null || retObj.get("COT_ID") == null) return;
				paramters.put("cotId", getString(retObj, "COT_ID") );
				paramters.put("tgrItem", table.getSelectedRows().get(0));
				getMaterialExtentsWindow().openDialog(RecommApplication.STATUS_DELETE, paramters, 900);
			}
		});

		table.getButtomMenu().addIcon(icon3, "미리 보기", com.google.gwt.dom.client.Style.Float.LEFT);
		table.getButtomMenu().addIcon(icon5, "삭제", com.google.gwt.dom.client.Style.Float.LEFT);
		
		MaterialIcon searchIcon = new MaterialIcon(IconType.SEARCH);
		searchIcon.setTextAlign(TextAlign.CENTER);
		searchIcon.addClickHandler(event->{
			qryList(true);
		});
		table.getTopMenu().addIcon(searchIcon, "검색", com.google.gwt.dom.client.Style.Float.RIGHT, "1.8em", "26px", 24, false);
		
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
		
		
		MaterialButton EXCELBUTTON = new MaterialButton("엑셀 다운로드");
		
		EXCELBUTTON.addClickHandler(event->{
			
			Map<String, Object> paramters = new HashMap<String, Object>();
			
			
			Console.log("OTD_ID :: " + RecommApplication.getValue("OTD_ID"));
			if (RecommApplication.getValue("OTD_ID") != null && RecommApplication.getValue("OTD_ID").toString() != "") {
				String OTD_ID = RecommApplication.getValue("OTD_ID").toString();
				if (!OTD_ID.equals("0a01eb7b-96de-11e8-8165-020027310001")) {
					paramters.put("OTD_ID", OTD_ID);
				}
			}
			getMaterialExtentsWindow().openDialog(RecommApplication.EXCEL_DOWNLOAD, paramters, 900);
			
		});
		table.getButtomMenu().addButton(EXCELBUTTON, com.google.gwt.dom.client.Style.Float.LEFT);
		
	
		MaterialIcon addIcon = new MaterialIcon(IconType.ADD);
		addIcon.addClickHandler(event->{

			String cotId = IDUtil.uuid();
			String atmId = IDUtil.uuid();
			String usrId = Registry.getUserId();
			String otdId = null;
			Boolean isMain = false;
			
			if (RecommApplication.getValue("OTD_ID") != null && RecommApplication.getValue("OTD_ID").toString() != "") {
				otdId = RecommApplication.getValue("OTD_ID").toString();
			}
			
			if (this.getWindowParameters() != null && this.getWindowParameters().get("IS_MAIN") != null) {
				isMain = (boolean) this.getWindowParameters().get("IS_MAIN");
			}
			
			JSONObject parameterJSON = new JSONObject();
			parameterJSON.put("cmd", new JSONString("INSERT_ARTICLE"));
			parameterJSON.put("cotId", new JSONString(cotId));
			parameterJSON.put("atmId", new JSONString(atmId));
			parameterJSON.put("usrId", new JSONString(usrId));
			parameterJSON.put("isMain", new JSONString(isMain+""));
			if (otdId != null)  parameterJSON.put("otdId", new JSONString(otdId));
			
			VisitKoreaBusiness.post("call", parameterJSON.toString(), new Func3<Object, String, Object>() {

				@Override
				public void call(Object param1, String param2, Object param3) {

					JSONObject resultObj = (JSONObject) JSONParser.parseStrict(JSON.stringify((JsObject)param1));
					JSONObject headerObj = (JSONObject) resultObj.get("header");
					String processResult = headerObj.get("process").isString().toString();
					processResult = processResult.replaceAll("\"", "");
					
					if (processResult.equals("success")) {

						JSONObject recObj = new JSONObject();
						
						Date date = new Date();
						String dateString = DateTimeFormat.getFormat("yyyy-MM-dd").format(date);
						
						ContentTableRow tableRow = table.addRow(
								Color.WHITE, 
								cotId,
								"미지정",
								"미지정",
								"신규 기사 컨텐츠",
								"미지정",
								"작업 미완료",
								dateString,
								dateString,
								"");
						
						tableRow.put("RETOBJ", recObj);
						tableRow.put("COT_ID", cotId);
						tableRow.put("ATM_ID", atmId);
						tableRow.addDoubleClickHandler(event->{
							RecommContentsTree cPanel = (RecommContentsTree)getMaterialExtentsWindow().getContentPanel(1);
							cPanel.setTitle("기사 컨텐츠 신규 작성");
							cPanel.setCotId(cotId);
							cPanel.setRow(tableRow);
							cPanel.goTree(1);
							cPanel.go(0);
							cPanel.loading();
							getMaterialExtentsWindow().goContentSlider(getMaterialExtentsWindow().getWidth() * 1 * -1);
						});

						RecommContentsTree cPanel = (RecommContentsTree)getMaterialExtentsWindow().getContentPanel(1);
						cPanel.setTitle("기사 컨텐츠 신규 작성");
						cPanel.setCotId(cotId);
						cPanel.setRow(tableRow);
						cPanel.goTree(1);
						cPanel.go(0);
						cPanel.loading();
						getMaterialExtentsWindow().goContentSlider(getMaterialExtentsWindow().getWidth() * 1 * -1);

					}
					
					table.loading(false);
				}
			});
			
			
			
		});
		table.getTopMenu().addIcon(addIcon, "추가", Style.Float.RIGHT, "1.8em", "34px", 25, false );
	}

	public void qryList(boolean bstart) {
		if(bstart) {
			offset = 0; 
//			index = 1;
			table.clearRows();
		} else offset += 20;
		
		
		String keyWord = edidname.getText().replaceAll("\\\\", "");
		
		JSONObject parameterJSON = new JSONObject();
		parameterJSON.put("cmd", new JSONString("ARTICLE_LIST"));
		parameterJSON.put("mode", new JSONNumber(mode.getSelectedIndex()));
		parameterJSON.put("keyword", new JSONString(keyWord));
		
		if (RecommApplication.getValue("OTD_ID") != null && RecommApplication.getValue("OTD_ID").toString() != "") {
			parameterJSON.put("OTD_ID", new JSONString(RecommApplication.getValue("OTD_ID").toString()));
		}
		
		// 생성일, 수정일 구분
//		if(dateopt.getSelectedIndex() > 0) {
		parameterJSON.put("dateType", new JSONNumber(dateopt.getSelectedIndex()));
//		}		
		
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
//			if(!sigungutype.getValues().get(sigungutype.getSelectedIndex()).toString().equals("0")) {
//				String sigunguname = sigungutype.getValues().get(sigungutype.getSelectedIndex()).toString().split("\\^")[1];
//				parameterJSON.put("sigunguname", new JSONString(sigunguname));
//			}
		}
		
		if(category.getSelectedIndex()>0) {
			String categorycode = category.getValues().get(category.getSelectedIndex()).toString().split("\\^")[0];
			parameterJSON.put("CategoryCode", new JSONString(categorycode));
		}
		
		if(division.getSelectedIndex()>0) {
			String Division = division.getValues().get(division.getSelectedIndex()).toString().split("\\^")[0];
			parameterJSON.put("Division", new JSONString(Division));
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
							String Division = "[일반] ";
						 if(recObj.get("CONTENT_DIV").isNumber().doubleValue() != 0) {
							 Division = "[테마] ";
						 }
						ContentTableRow tableRow = table.addRow(Color.WHITE, new int[]{3}, 
								getString(recObj, "CONTENT_ID"),
								getString(recObj, "CONTENT_CATEGORYK"),
								getString(recObj, "AREA_NAME"),
								Division+getString(recObj, "TITLE"),
								getTagString(recObj, "MASTER_TAG"),
								getString(recObj, "CONTENT_STATUS"),
								getString(recObj, "FINAL_MODIFIED_DATE"),
								getString(recObj, "CREATE_DATE"),
								getString(recObj, "USER_NAME"));
						
						tableRow.put("RETOBJ", recObj);
						tableRow.addClickHandler(event->{
							ContentTableRow ctr = (ContentTableRow)event.getSource();
							if(ctr.getSelectedColumn() == 3) {
								RecommContentsTree cPanel = (RecommContentsTree)getMaterialExtentsWindow().getContentPanel(1);
								cPanel.setTitle(recObj.get("TITLE").isString().stringValue());
								cPanel.setCotId(getString(recObj, "COT_ID"));
								cPanel.setRow(tableRow);
								cPanel.goTree(1);
								cPanel.go(0);
								cPanel.loading();
								getMaterialExtentsWindow().goContentSlider(getMaterialExtentsWindow().getWidth() * 1 * -1);
							}
						});
					}
					
				}else if (processResult.equals("fail")) {
					getMaterialExtentsWindow().alert("검색이 실패했습니다. 관리자에게 문의하세요.", 500);
					countLabel.setText("실패?");
				}
				table.loading(false);
			}
		});
	}
//	private String getString(JSONObject recObj, String key) {
//		return getString(recObj, key, "·");
//	}
//	
//	private String getString(JSONObject recObj, String key, String nullvalue) {
//		if (recObj.get(key) == null || recObj.get(key).isString().stringValue().equals("")) return nullvalue;
//		else return recObj.get(key).isString().stringValue();
//	}
	private String getTagString(JSONObject recObj, String key) {
		if (recObj.get(key) == null || recObj.get(key).isString().stringValue().equals("")) return "·";
		else return "#"+recObj.get(key).isString().stringValue();
	}
}
