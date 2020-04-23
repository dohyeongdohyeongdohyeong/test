package kr.or.visitkorea.admin.client.manager.partners.content;

import com.google.gwt.dom.client.Style.Float;
import com.google.gwt.dom.client.Style.FontWeight;
import com.google.gwt.dom.client.Style.Overflow;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONNumber;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Widget;

import gwt.material.design.addins.client.combobox.MaterialComboBox;
import gwt.material.design.client.base.MaterialWidget;
import gwt.material.design.client.constants.Color;
import gwt.material.design.client.constants.InputType;
import gwt.material.design.client.constants.TextAlign;
import gwt.material.design.client.ui.MaterialButton;
import gwt.material.design.client.ui.MaterialCheckBox;
import gwt.material.design.client.ui.MaterialColumn;
import gwt.material.design.client.ui.MaterialLabel;
import gwt.material.design.client.ui.MaterialPanel;
import gwt.material.design.client.ui.MaterialRow;
import gwt.material.design.client.ui.MaterialTextBox;
import gwt.material.design.client.ui.MaterialToast;
import gwt.material.design.jquery.client.api.Functions.Func3;
import gwt.material.design.jscore.client.api.JSON;
import gwt.material.design.jscore.client.api.JsObject;
import kr.or.visitkorea.admin.client.application.Console;
import kr.or.visitkorea.admin.client.manager.VisitKoreaBusiness;
import kr.or.visitkorea.admin.client.widgets.dialog.DialogContent;
import kr.or.visitkorea.admin.client.widgets.window.MaterialExtentsWindow;

public class PartnersContentExcelDialog extends DialogContent {

	private MaterialLabel dialogTitle;
	private MaterialComboBox<Object> areatype, sigungutype;
	private MaterialComboBox<Object> dateopt, mode;
	private MaterialTextBox edidname;
	private MaterialTextBox sdate;
	private MaterialTextBox edate;
	private MaterialComboBox<Object> category, Secondcategory;
	private boolean check;
	private String OTD_ID;
	private MaterialComboBox<Object> StatusSearch,Division;
	private MaterialCheckBox[] CheckList;
	private MaterialComboBox<Object> IS_USE,APPLY_TYPE;

	public PartnersContentExcelDialog(MaterialExtentsWindow tgrWindow) {
		super(tgrWindow);
	}

	@Override
	protected void onLoad() {
	}

	@Override
	public void init() {
		addDefaultButtons();
		CheckList = new MaterialCheckBox[21];
		BuildLayout();
	}

	private void BuildLayout() {
		this.setOverflow(Overflow.AUTO);
		dialogTitle = new MaterialLabel("엑셀 다운로드 - Partners 컨텐츠 사용신청");
		dialogTitle.setFontSize("1.4em");
		dialogTitle.setTextColor(Color.BLUE);
		dialogTitle.setFontWeight(FontWeight.BOLD);
		dialogTitle.setPadding(15);
		this.add(dialogTitle);

		MaterialButton saveBtn = new MaterialButton("다운로드");
		saveBtn.setMarginRight(10);
		saveBtn.setFloat(Float.RIGHT);
		saveBtn.addClickHandler(event -> {
			CheckBoxCheck();
		});
		this.getButtonArea().add(saveBtn);

		MaterialPanel SearchPanel = new MaterialPanel();
		SearchPanel.setBackgroundColor(Color.WHITE);
		this.add(SearchPanel);
		SearchPanel.setWidth("460px");
		SearchPanel.setHeight("450px");
		SearchPanel.setBorder("1px solid Black");
		SearchPanel.add(CreateTitleLabel("검색 조건", TextAlign.LEFT));
		SearchPanel.getElement().getStyle().setProperty("margin", "0px 40px 0px 40px");
		SearchPanel.setFloat(Float.LEFT);
		CreateSearchPanel(SearchPanel);

		MaterialPanel basePanel = new MaterialPanel();
		basePanel.setBackgroundColor(Color.WHITE);
		this.add(basePanel);
		basePanel.add(CreateTitleLabel("노출 항목", TextAlign.CENTER));
		MaterialRow CheckRow = addRow(basePanel,null,null,null);
		CheckRow.setHeight("365px");
		basePanel.setWidth("340px");
		basePanel.setHeight("450px");
		basePanel.setBorder("1px solid Black");
		CheckRow.setOverflow(Overflow.AUTO);
		basePanel.setFloat(Float.LEFT);
		basePanel.getElement().getStyle().setProperty("margin", "auto");
		MaterialRow AllCheckRow = addRow(basePanel,null,null,null);
		AllCheckRow.setMarginBottom(5);
		AllCheckRow.setBorderTop("solid 1px black");
		AllCheckRow.setHeight("43px");
		AllCheckRow.setPaddingTop(5);
		basePanel.add(AllCheckRow);
		CheckRow.setMarginBottom(0);
		MaterialCheckBox allcheck = CreateCheckBox(AllCheckRow, "전체 선택");
		check = true;
		allcheck.addClickHandler(event->{
			if(check == true) {
				check = false;
				AllCheck(true);
			} else {
				check = true;
				AllCheck(false);
			}
		});
		
		String[] CheckListName = {"SNS 고유 키","파트너스 계정","기관 명", "담당자 명", "COTID", "신청 컨텐츠",
				"컨텐츠 카테고리", "사용처", "반려 사유", "사용 용도", "기타 문의사항", "미제공 사유 또는 다운로드 URL", "다운로드 제공여부",
				"사용내역 등록 URL", "사용내역 등록 상세내용 또는 미사용 사유", "사용내역 등록 파일명", "사용내역 등록 파일ID", "사용정보 사용여부", "처리 상태", "신청일", "처리완료일"};
		
		for (int i = 0; i < CheckListName.length; i++) {
			CheckList[i] = CreateCheckBox(CheckRow, CheckListName[i]);
		}
	}

	private Widget CreateSearchPanel(MaterialPanel panel) {

		mode = new MaterialComboBox<>();
		mode.addItem("컨텐츠 명", 1);
		mode.addItem("파트너스 ID", 2);
		mode.addItem("기관명", 3);
		mode.setFloat(Float.LEFT);
		mode.setLabel("검색 조건");
		mode.setMinHeight("68px");
		mode.setGrid("s5");

		edidname = new MaterialTextBox();
		edidname.setLabel("검색어 입력");
		edidname.setGrid("s7");
		edidname.setFloat(Float.LEFT);

		MaterialRow row1 = addRow(panel,mode,edidname,null);

		dateopt = new MaterialComboBox<>();
		dateopt.setLabel("날짜옵션");
		dateopt.setGrid("s4");
		dateopt.addItem("선택안함", 0);
		dateopt.addItem("생성일", 1);
		dateopt.addItem("수정일", 2);
		dateopt.setSelectedIndex(0);
		dateopt.setFloat(Float.LEFT);
		panel.add(dateopt);

		sdate = new MaterialTextBox();
		sdate.setType(InputType.DATE);
		sdate.setLabel("기간");
		sdate.setText("-");
		sdate.setFloat(Float.LEFT);
		sdate.setGrid("s4");
		panel.add(sdate);
		edate = new MaterialTextBox();
		edate.setType(InputType.DATE);
		edate.setFloat(Float.LEFT);
		edate.setGrid("s4");
		edate.setMinHeight("68px");
		panel.add(edate);

		MaterialRow row2 = addRow(panel,dateopt,sdate,edate);

		category = new MaterialComboBox<>();
		category.setLabel("컨텐츠분류");
		category.setGrid("s6");
		category.addItem("전체", 0);
		category.addItem("추천", 1);
		category.addItem("DB", 2);

		StatusSearch = new MaterialComboBox<>();
		StatusSearch.setLabel("처리상태");
		StatusSearch.setGrid("s6");
		StatusSearch.addItem("전체",-1);
		StatusSearch.addItem("접수",0);
		StatusSearch.addItem("처리중",1);
		StatusSearch.addItem("승인완료",2);
		StatusSearch.addItem("반려",3);
		StatusSearch.addItem("사용내역등록",4);

		MaterialRow row4 = addRow(panel,category,StatusSearch,null);
		
		IS_USE = new MaterialComboBox<>();
		IS_USE.setLabel("사용여부");
		IS_USE.setGrid("s6");
		IS_USE.addItem("전체",-1);
		IS_USE.addItem("미사용",0);
		IS_USE.addItem("사용",1);
		
		APPLY_TYPE = new MaterialComboBox<>();
		APPLY_TYPE.setLabel("사용용도");
		APPLY_TYPE.setGrid("s6");
		APPLY_TYPE.addItem("전체",0);
		APPLY_TYPE.addItem("이미지",1);
		APPLY_TYPE.addItem("본문내용(only 텍스트)",2);
		APPLY_TYPE.addItem("본문내용+이미지",3);
		APPLY_TYPE.addItem("온라인",4);
		APPLY_TYPE.addItem("인쇄물",5);
		APPLY_TYPE.addItem("기타",6);
		
		MaterialRow row5 = addRow(panel,IS_USE,APPLY_TYPE,null);
		
		return null;
	}

	@Override
	public int getHeight() {
		return 600;
	}

	private MaterialCheckBox CreateCheckBox(MaterialWidget parent, String Title) {

		MaterialCheckBox CheckBox = new MaterialCheckBox(Title);

		MaterialColumn col = new MaterialColumn();
		col.setWidth("100%");
		col.setPadding(5);
		col.add(CheckBox);
		parent.add(col);

		return CheckBox;
	}

	private MaterialLabel CreateTitleLabel(String title, TextAlign Align) {
		MaterialLabel TitleLabel = new MaterialLabel(title);
		TitleLabel.setWidth("100%");
		TitleLabel.setBackgroundColor(Color.BLUE_DARKEN_1);
		TitleLabel.setHeight("30px");
		TitleLabel.setFontSize("17px");
		TitleLabel.setFontWeight(FontWeight.BOLD);
		TitleLabel.setLineHeight(30);
		TitleLabel.setTextAlign(Align);
		TitleLabel.setPaddingLeft(10);
		TitleLabel.setTextColor(Color.WHITE);
		TitleLabel.setMarginBottom(10);
		return TitleLabel;
	}

	private void Download() {

		String keyWord = edidname.getText().replaceAll("\\\\", "");

		String downurl = "./call?cmd=FILE_DOWNLOAD_XLS";
		downurl += "&select_type=PartnersContent";

		// 노출 항목 관련 파라메터

		downurl += "&CheckList="+CheckList.length;
		for (int i = 0; i < CheckList.length; i++) {
			downurl += "&Chk"+i+"="+(CheckList[i].getValue() ? 1 : 0);
		}
		// 검색조건 관련 파라메터
		String strsdate = sdate.getText().toString();
		if (!"".equals(strsdate))
			downurl += "&startInput=" + strsdate;
		String stredate = edate.getText().toString();
		if (!"".equals(stredate)) 
			downurl += "&endInput=" + stredate + " 23:59:59";
		downurl += "&mode=" +mode.getSelectedIndex();
		downurl += "&keyword=" +keyWord;
		if (category.getSelectedIndex() > 0) {
			String categorycode = category.getSelectedValue().get(0).toString();
			downurl += "&CategoryCode=" +categorycode;
		}
		downurl += "&StatusSearch=" +StatusSearch.getSelectedValue().get(0);
		downurl += "&Is_Use=" +IS_USE.getSelectedValue().get(0);
		downurl += "&Apply_type=" +APPLY_TYPE.getSelectedValue().get(0);
		downurl += "&dateType=" +dateopt.getSelectedIndex();
		Window.open(downurl,"_self", "enabled");
		String[] alertmessages = {"엑셀 다운로드 알림","데이터 양이 많을 경우","최대 2분정도의 시간이 소요 될 수 있습니다."};
		alert("알림", 500, 300, alertmessages);
	}
	
	public void AllCheck(boolean check) {
		
		for (int i = 0; i < CheckList.length; i++) {
			CheckList[i].setValue(check);
		}
	}
	
	private void CheckBoxCheck() {
		int Check = 0;
		for (int i = 0; i < CheckList.length; i++) {
			Check += CheckList[i].getValue() ? 1 : 0;
		}
		
		if(Check == 0 ) {
			String[] alertmessages = {"엑셀 다운로드 알림","노출항목을 선택해주세요."};
			alert("알림", 500, 300, alertmessages);
			return;
		}
		
		String keyWord = edidname.getText().replaceAll("\\\\", "");
				
		JSONObject parameterJSON = new JSONObject();
		parameterJSON.put("cmd", new JSONString("SELECT_XLS_COUNT"));
		parameterJSON.put("select_type", new JSONString("PartnersContent"));
		parameterJSON.put("mode", new JSONNumber(mode.getSelectedIndex()));
		parameterJSON.put("keyword", new JSONString(keyWord));
		if(category.getSelectedIndex() != 0)
			parameterJSON.put("CategoryCode", new JSONNumber((int)category.getSelectedValue().get(0)));
		parameterJSON.put("StatusSearch", new JSONNumber((int)StatusSearch.getSelectedValue().get(0)));
		parameterJSON.put("Is_Use", new JSONNumber((int)(IS_USE.getSelectedValue().get(0))));
		parameterJSON.put("Apply_type", new JSONNumber((int)(APPLY_TYPE.getSelectedValue().get(0))));
		
		String strsdate = sdate.getText().toString();
		if (!"".equals(strsdate))
			parameterJSON.put("startInput", new JSONString(strsdate));
		String stredate = edate.getText().toString();
		if (!"".equals(stredate)) 
			parameterJSON.put("endInput", new JSONString(stredate+" 23:59:59"));
		parameterJSON.put("dateType", new JSONNumber(dateopt.getSelectedIndex()));
		
		VisitKoreaBusiness.post("call", parameterJSON.toString(), new Func3<Object, String, Object>() {
			@Override
			public void call(Object param1, String param2, Object param3) {
				JSONObject resultObj = (JSONObject) JSONParser.parseStrict(JSON.stringify((JsObject)param1));
				JSONObject headerObj = (JSONObject) resultObj.get("header");
				int processResult = (int) headerObj.get("result").isNumber().doubleValue();
				
				if(processResult == 0) {
					String[] alertmessages = {"엑셀 다운로드 알림","해당 조건에 만족하는 데이터가 없습니다."};
					alert("알림", 500, 300, alertmessages);
				} else 
					Download();
			}
		});
			}
	
	private MaterialRow addRow(MaterialWidget Widget ,MaterialWidget Child1, MaterialWidget Child2, MaterialWidget Child3) {
		MaterialRow row = new MaterialRow();
		if(Child1 != null)
			row.add(Child1);
		if(Child2 != null)
			row.add(Child2);
		if(Child3 != null)
			row.add(Child3);
		Widget.add(row);
		return row;
	};
}
