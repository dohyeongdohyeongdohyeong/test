package kr.or.visitkorea.admin.client.manager.contents.codecategory.dialog;

import java.util.HashMap;
import java.util.Map;

import com.google.gwt.dom.client.Style.FontWeight;
import com.google.gwt.dom.client.Style.Overflow;
import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONNumber;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.user.client.Window;

import gwt.material.design.addins.client.combobox.MaterialComboBox;
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
import kr.or.visitkorea.admin.client.application.Registry;
import kr.or.visitkorea.admin.client.manager.VisitKoreaBusiness;
import kr.or.visitkorea.admin.client.manager.contents.recommand.RecommApplication;
import kr.or.visitkorea.admin.client.manager.widgets.ContentTable;
import kr.or.visitkorea.admin.client.manager.widgets.ContentTableRow;
import kr.or.visitkorea.admin.client.widgets.dialog.DialogContent;
import kr.or.visitkorea.admin.client.widgets.window.MaterialExtentsWindow;

public class CodeRecommDialog extends DialogContent {

	
	private String Code;
	private int isRank = 0;
	private int isOpen = 0;
	private int offset = 0;
	private int totcnt=0;
	private ContentTable table;
	private MaterialTextBox sdate, edate; 
	private MaterialComboBox<Object> areatype; //, sigungutype;
	private MaterialComboBox<Object> dateopt, mode;
	private MaterialTextBox edidname;
	private MaterialLabel countLabel;
	private MaterialLabel dialogTitle;
	private Object value;
	
	public CodeRecommDialog(MaterialExtentsWindow tgrWindow) {
		super(tgrWindow);
	}
	
	@Override
	protected void onLoad() {
		mode.setSelectedIndex(0);
		edidname.setText("");
		dateopt.setSelectedIndex(0);
		edate.setText("");
		sdate.setText("");
		areatype.setSelectedIndex(0);
		
		isRank = 0;
		isOpen = 0;
		dialogTitle.setText("분류관리 - 추천 컨텐츠 목록");
		Code = (String) this.getParameters().get("Code");
		isRank = (int) this.getParameters().get("isRank");
		isOpen = (int) this.getParameters().get("isOpen");
		value = (String) this.getParameters().get("value");
			dialogTitle.setText(dialogTitle.getText()+"("+value);
		if(isRank == 0)
			dialogTitle.setText(dialogTitle.getText()+" - 1순위 , ");
		else
			dialogTitle.setText(dialogTitle.getText()+" - 2순위 , ");
		if(isOpen == 0)
			dialogTitle.setText(dialogTitle.getText()+"전체)");
		else
			dialogTitle.setText(dialogTitle.getText()+"표출)");
		
		qryList(true);
		
	}

	@Override
	public void init() {
		
		dialogTitle = new MaterialLabel("분류관리 - 추천 컨텐츠 목록");
		dialogTitle.setFontSize("1.4em");
		dialogTitle.setTextColor(Color.BLUE);
		dialogTitle.setFontWeight(FontWeight.BOLD);
		dialogTitle.setPaddingLeft(15);
		dialogTitle.setPaddingTop(15);
		dialogTitle.setTextAlign(TextAlign.LEFT);
		this.add(dialogTitle);
		
		this.setLayoutPosition(Position.RELATIVE);
		this.setTextAlign(TextAlign.CENTER);
		this.setFloat(com.google.gwt.dom.client.Style.Float.LEFT);
		this.setHeight("100%");
		this.setWidth("100%");
		this.setOverflow(Overflow.HIDDEN);
		
		MaterialIcon CloseIcon = new MaterialIcon(IconType.CLOSE);
		
		CloseIcon.setLayoutPosition(Position.ABSOLUTE);
		CloseIcon.setRight(5);
		CloseIcon.setTop(10);
		
		CloseIcon.addClickHandler(event->{
		
			getMaterialExtentsWindow().closeDialog();
			
		});
		
		this.add(CloseIcon);
		
		MaterialPanel mrtop = new MaterialPanel();
		mrtop.setWidth("100%");	mrtop.setHeight("80px");
		mrtop.setPadding(10);
		this.add(mrtop);
		
		mode = new MaterialComboBox<>();
		mode.setLabel("검색조건");
		mode.setLayoutPosition(Position.ABSOLUTE);
		mode.setTop(50); mode.setLeft(10); mode.setWidth("120px");
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
		edidname.setTop(50);	edidname.setLeft(140); edidname.setWidth("300px");
		mrtop.add(edidname);
		edidname.addKeyPressHandler(event -> {
			if (event.getNativeEvent().getKeyCode() == KeyCodes.KEY_ENTER) {
				qryList(true);
			}
		});
		
		dateopt = new MaterialComboBox<>();
		dateopt.setLabel("날짜옵션");
		dateopt.setLayoutPosition(Position.ABSOLUTE);
		dateopt.setTop(50); dateopt.setLeft(480); dateopt.setWidth("120px");
//		dateopt.addItem("선택안함", 0);
		dateopt.addItem("생성일", 0);
		dateopt.addItem("최종 수정일", 1);
		dateopt.setSelectedIndex(0);
		mrtop.add(dateopt);
		
		sdate = new MaterialTextBox();
		sdate.setType(InputType.DATE);
		sdate.setLayoutPosition(Position.ABSOLUTE);
		sdate.setLabel("기간"); sdate.setText("-");
		sdate.setTop(50); sdate.setLeft(620); sdate.setWidth("150px");
		mrtop.add(sdate);
		edate = new MaterialTextBox();
		edate.setType(InputType.DATE);
		edate.setLayoutPosition(Position.ABSOLUTE);
		edate.setTop(50); edate.setLeft(780); edate.setWidth("150px");
		mrtop.add(edate);
		
		areatype = new MaterialComboBox<>();
		areatype.setLabel("지자체선택");
		areatype.setLayoutPosition(Position.ABSOLUTE);
		areatype.setTop(50); areatype.setLeft(970); areatype.setWidth("150px");
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
			qryList(true);
		});
		
		
		
		buildLayout();
	}

	private void buildLayout() {

		table = new ContentTable(ContentTable.TABLE_SELECT_TYPE.SELECT_SINGLE);
		table.setLayoutPosition(Position.ABSOLUTE);
		table.setTop(85);table.setWidth("98.5%"); table.setHeight(520); table.setMargin(10);
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
		
		MaterialButton EXCELBUTTON = new MaterialButton("엑셀 다운로드");
		
		EXCELBUTTON.addClickHandler(event->{
			table.loading(true);
			String keyWord = edidname.getText().replaceAll("\\\\", "");
			String downurl = "./call?cmd=FILE_DOWNLOAD_XLS";

			downurl += "&select_type=codeRecomm";
			downurl += "&CODE="+Code;
			downurl += "&IS_RANK="+isRank;
			downurl += "&IS_OPEN="+isOpen;
			downurl += "&keyword="+keyWord;
			downurl += "&dateType="+dateopt.getSelectedIndex();
			if(sdate.getText() != "")
				downurl += "&startInput="+sdate.getText();
			if(edate.getText() != "")
				downurl += "&endInput="+edate.getText();
			if(areatype.getSelectedIndex()>0) 
				downurl += "&areaCode="+areatype.getValues().get(areatype.getSelectedIndex()).toString().split("\\^")[0];
			
			downurl += "&mode="+mode.getSelectedIndex();
				
			Window.open(downurl,"_self", "enabled");
			table.loading(false);
			
		});
		table.getButtomMenu().addButton(EXCELBUTTON, com.google.gwt.dom.client.Style.Float.LEFT);
		
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
		
	}
	
	public void qryList(boolean bstart) {
		if(bstart) {
			offset = 0; 
			table.clearRows();
		} else offset += 20;
		
		
		String keyWord = edidname.getText().replaceAll("\\\\", "");
		
		JSONObject parameterJSON = new JSONObject();
		parameterJSON.put("cmd", new JSONString("ARTICLE_LIST"));
		parameterJSON.put("mode", new JSONNumber(mode.getSelectedIndex()));
		if(Code != null)
			parameterJSON.put("CODE", new JSONString(Code));
		parameterJSON.put("IS_RANK", new JSONNumber(isRank));
		parameterJSON.put("IS_OPEN", new JSONNumber(isOpen));
		parameterJSON.put("keyword", new JSONString(keyWord));
		
		if (RecommApplication.getValue("OTD_ID") != null && RecommApplication.getValue("OTD_ID").toString() != "") {
			parameterJSON.put("OTD_ID", new JSONString(RecommApplication.getValue("OTD_ID").toString()));
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
						ContentTableRow tableRow = table.addRow(Color.WHITE, new int[] {}, 
								getString(recObj, "CONTENT_ID"),
								getString(recObj, "CONTENT_CATEGORYK"),
								getString(recObj, "AREA_NAME"),
								getString(recObj, "TITLE"),
								getTagString(recObj, "MASTER_TAG"),
								getString(recObj, "CONTENT_STATUS"),
								getString(recObj, "FINAL_MODIFIED_DATE"),
								getString(recObj, "CREATE_DATE"),
								getString(recObj, "USER_NAME"));
						
						tableRow.put("RETOBJ", recObj);
						
						tableRow.addDoubleClickHandler(event->{
							String title = getString(recObj, "TITLE");
							this.copy(title);
							MaterialToast.fireToast("제목이 복사되었습니다.");
						});
					}
					
				}else if (processResult.equals("fail")) {
					getMaterialExtentsWindow().alert("검색이 실패했습니다. 관리자에게 문의하세요.", 500);
					countLabel.setText("실패?");
				}
				table.loading(false);
			}

			public native void copy(String cotId) /*-{
			var dummy = document.createElement("textarea");
			document.body.appendChild(dummy);
			dummy.value = cotId;
			dummy.select();
			document.execCommand("copy");
			document.body.removeChild(dummy);
			}-*/;
			
		});
	}
	
	@Override
	public int getHeight() {
		return 660;
	}
	
	private String getTagString(JSONObject recObj, String key) {
		if (recObj.get(key) == null || recObj.get(key).isString().stringValue().equals("")) return "·";
		else return "#"+recObj.get(key).isString().stringValue();
	}
	

	protected String getString(JSONObject recObj, String key) {
		if (recObj.get(key) == null || recObj.get(key).isString().stringValue().equals("")) return "";
		else return recObj.get(key).isString().stringValue();
	}
	
	
	
}
