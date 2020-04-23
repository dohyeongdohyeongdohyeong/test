package kr.or.visitkorea.admin.client.manager.contents.database.dialogs;

import java.util.Map;

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
import kr.or.visitkorea.admin.client.application.Registry;
import kr.or.visitkorea.admin.client.manager.VisitKoreaBusiness;
import kr.or.visitkorea.admin.client.manager.contents.database.DatabaseContentsTree;
import kr.or.visitkorea.admin.client.manager.widgets.ContentTableRow;
import kr.or.visitkorea.admin.client.widgets.dialog.DialogContent;
import kr.or.visitkorea.admin.client.widgets.window.MaterialExtentsWindow;

public class DBExcelDownloadDialog extends DialogContent {

	private MaterialLabel dialogTitle;
	private MaterialComboBox<Object> areatype, sigungutype;
	private MaterialComboBox<Object> dateopt, mode;
	private MaterialTextBox edidname;
	private MaterialTextBox sdate;
	private MaterialTextBox edate;
	private MaterialComboBox<Object> Firstcategory, StatusSearch;
	
	private MaterialCheckBox cId;
	private MaterialCheckBox cotId;
	private MaterialCheckBox title;
	private MaterialCheckBox typename;
	private MaterialCheckBox contenttype;
	private MaterialCheckBox cat1;
	private MaterialCheckBox cat2;
	private MaterialCheckBox cat3;
	private MaterialCheckBox Sigungu;
	private MaterialCheckBox Area;
	private MaterialCheckBox status;
	private MaterialCheckBox create_date;
	private MaterialCheckBox update_date;
	private MaterialCheckBox stfId;
	private MaterialCheckBox otdId;
	private MaterialCheckBox catchphrase;
	private MaterialCheckBox setstatus;
	private MaterialCheckBox authcode;
	private MaterialCheckBox fesSdate;
	private MaterialCheckBox fesEdate;

	
	private boolean check;
	private String OTD_ID;

	public DBExcelDownloadDialog(MaterialExtentsWindow tgrWindow) {
		super(tgrWindow);
	}

	@Override
	protected void onLoad() {
		if(getParameters().containsKey("OTD_ID")) {
			OTD_ID = getParameters().get("OTD_ID").toString();
		}
	}

	@Override
	public void init() {
		addDefaultButtons();
		BuildLayout();
	}

	private void BuildLayout() {
		this.setOverflow(Overflow.AUTO);
		dialogTitle = new MaterialLabel("엑셀 다운로드 - 데이터베이스 컨텐츠");
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
		SearchPanel.setWidth("600px");
		SearchPanel.setHeight("450px");
		SearchPanel.setBorder("1px solid Black");
		SearchPanel.add(CreateTitleLabel("검색 조건", TextAlign.LEFT));
		SearchPanel.getElement().getStyle().setProperty("margin", "0px 40px 0px 40px");
		SearchPanel.setFloat(Float.LEFT);
		CreateSearchPanel(SearchPanel);

		MaterialPanel basePanel = new MaterialPanel();
		basePanel.setBackgroundColor(Color.WHITE);
		this.add(basePanel);
		MaterialRow CheckRow = new MaterialRow();
		CheckRow.setHeight("365px");
		basePanel.setWidth("200px");
		basePanel.setHeight("450px");
		basePanel.setBorder("1px solid Black");
		basePanel.add(CreateTitleLabel("노출 항목", TextAlign.CENTER));
		CheckRow.setOverflow(Overflow.AUTO);
		basePanel.setFloat(Float.LEFT);
		basePanel.getElement().getStyle().setProperty("margin", "auto");
		basePanel.add(CheckRow);
		MaterialRow AllCheckRow = new MaterialRow();
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
		
		
		cId = CreateCheckBox(CheckRow, "CID");
		cotId = CreateCheckBox(CheckRow, "COTID");
		title = CreateCheckBox(CheckRow, "컨텐츠 제목");
		typename = CreateCheckBox(CheckRow, "서비스 분류");
		contenttype = CreateCheckBox(CheckRow, "분류 코드");
		cat1 = CreateCheckBox(CheckRow, "카테고리 1분류");
		cat2 = CreateCheckBox(CheckRow, "카테고리 2분류");
		cat3 = CreateCheckBox(CheckRow, "카테고리 3분류");
		Sigungu = CreateCheckBox(CheckRow, "시군구");
		Area = CreateCheckBox(CheckRow, "지역");
		status = CreateCheckBox(CheckRow, "처리 상태");
		create_date = CreateCheckBox(CheckRow, "생성일");
		update_date = CreateCheckBox(CheckRow, "수정일");
		stfId = CreateCheckBox(CheckRow, "생성자");
		otdId = CreateCheckBox(CheckRow, "타부서 적용여부");
		catchphrase = CreateCheckBox(CheckRow, "캐치 프레이즈");
		authcode = CreateCheckBox(CheckRow, "인증번호");
		setstatus = CreateCheckBox(CheckRow, "여행지 - 지정현황");
		fesSdate = CreateCheckBox(CheckRow, "축제 - 시작일");
		fesEdate = CreateCheckBox(CheckRow, "축제 - 종료일");
	}

	private Widget CreateSearchPanel(MaterialPanel panel) {
		
		mode = new MaterialComboBox<>();
		mode.addItem("제목", 0);
		mode.addItem("CID", 1);
		mode.addItem("태그", 2);
		mode.addItem("작성자", 3);
		mode.setFloat(Float.LEFT);
		mode.setLabel("키워드 검색");
		mode.setGrid("s4");
		mode.addValueChangeHandler(e -> {
			if (mode.getSelectedIndex() == 0) {
				edidname.setLabel("제목입력");
			} else if (mode.getSelectedIndex() == 1) {
				edidname.setLabel("CID입력");
			} else if (mode.getSelectedIndex() == 2) {
				edidname.setLabel("태그입력");
			} else {
				edidname.setLabel("작성자입력");
			}
		});

		edidname = new MaterialTextBox();
		edidname.setLabel("키워드 입력");
		edidname.setGrid("s8");
		edidname.setFloat(Float.LEFT);

		MaterialRow row1 = new MaterialRow();
		row1.add(mode);
		row1.add(edidname);
		panel.add(row1);

		dateopt = new MaterialComboBox<>();
		dateopt.setLabel("날짜옵션");
		dateopt.setGrid("s4");
//		dateopt.addItem("선택안함", 0);
		dateopt.addItem("생성일", 0);
		dateopt.addItem("수정일", 1);
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
		panel.add(edate);

		MaterialRow row2 = new MaterialRow();
		row2.add(dateopt);
		row2.add(sdate);
		row2.add(edate);
		panel.add(row2);

		areatype = new MaterialComboBox<>();
		areatype.setLabel("지자체선택");
		areatype.setFloat(Float.LEFT);
		areatype.setGrid("s6");
		areatype.addItem("전체", 0);
		JSONObject parameterJSON = new JSONObject();
		parameterJSON.put("cmd", new JSONString("SELECT_AREA"));
		VisitKoreaBusiness.post("call", parameterJSON.toString(), new Func3<Object, String, Object>() {
			@Override
			public void call(Object param1, String param2, Object param3) {
				JSONObject resultObj = (JSONObject) JSONParser.parseStrict(JSON.stringify((JsObject) param1));
				JSONObject headerObj = (JSONObject) resultObj.get("header");
				String processResult = headerObj.get("process").isString().stringValue();
				if (processResult.equals("success")) {
					JSONObject bodyObj = (JSONObject) resultObj.get("body");
					JSONArray bodyResultObj = (JSONArray) bodyObj.get("result");

					int usrCnt = bodyResultObj.size();
					if (usrCnt == 0) {
						MaterialToast.fireToast("검색된 결과가 없습니다.", 3000);
					}

					for (int i = 0; i < usrCnt; i++) {
						JSONObject obj = (JSONObject) bodyResultObj.get(i);
						areatype.addItem(obj.get("name").isString().stringValue(),
								obj.get("code").isString().stringValue() + "^"
										+ obj.get("name").isString().stringValue());
					}
					areatype.setSelectedIndex(0);
				}
			}
		});
		areatype.addValueChangeHandler(ee -> {
			sigungutype.clear();
			JSONObject paramJSON = new JSONObject();
			paramJSON.put("cmd", new JSONString("SELECT_AREA_SIGUNGU"));
			String areacode = areatype.getValues().get(areatype.getSelectedIndex()).toString().split("\\^")[0];
			paramJSON.put("areacode", new JSONString(areacode));
			VisitKoreaBusiness.post("call", paramJSON.toString(), new Func3<Object, String, Object>() {
				@Override
				public void call(Object param1, String param2, Object param3) {
					JSONObject resultObj = (JSONObject) JSONParser.parseStrict(JSON.stringify((JsObject) param1));
					JSONObject headerObj = (JSONObject) resultObj.get("header");
					String processResult = headerObj.get("process").isString().stringValue();
					if (processResult.equals("success")) {
						JSONObject bodyObj = (JSONObject) resultObj.get("body");
						JSONArray bodyResultObj = (JSONArray) bodyObj.get("result");
						int usrCnt = bodyResultObj.size();
						for (int i = 0; i < usrCnt; i++) {
							JSONObject obj = (JSONObject) bodyResultObj.get(i);
							if (obj.get("code").isString().stringValue().equals("0")) {
								sigungutype.addItem("전체", 0);
							} else
								sigungutype.addItem(obj.get("sigungu").isString().stringValue(),
										obj.get("code").isString().stringValue() + "^"
												+ obj.get("sigungu").isString().stringValue());
						}
						sigungutype.setSelectedIndex(0);
					}
				}
			});
		});

		sigungutype = new MaterialComboBox<>();
		sigungutype.setLabel("지자체선택");
		sigungutype.setFloat(Float.LEFT);
		sigungutype.setGrid("s6");
		MaterialRow row3 = new MaterialRow();
		row3.add(areatype);
		row3.add(sigungutype);
		panel.add(row3);

		Firstcategory = new MaterialComboBox<>();
		Firstcategory.setLabel("분류");
		Firstcategory.setGrid("s6");
		Firstcategory.addItem("전체");
		Firstcategory.addItem("관광지");
		Firstcategory.addItem("문화시설");
		Firstcategory.addItem("레포츠");
		Firstcategory.addItem("숙박");
		Firstcategory.addItem("쇼핑");
		Firstcategory.addItem("음식점");
		Firstcategory.addItem("여행코스");
		Firstcategory.addItem("축제공연행사");
		Firstcategory.addItem("생태관광");
		Firstcategory.addItem("시티투어");


		StatusSearch = new MaterialComboBox<>();
		StatusSearch.setLabel("처리상태");
		StatusSearch.setGrid("s6");
		StatusSearch.addItem("전체",-1);
		StatusSearch.addItem("작업 미완료",0);
		StatusSearch.addItem("상태 미등록",1);
		StatusSearch.addItem("작업 완료",2);
		StatusSearch.addItem("비 노출",7);
		StatusSearch.addItem("삭제",8);
		StatusSearch.addItem("작업 보류",9);
		
		MaterialRow row4 = new MaterialRow();
		panel.add(row4);
		row4.add(Firstcategory);
		row4.add(StatusSearch);

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
		downurl += "&select_type=DatabaseList";

		// 노출 항목 관련 파라메터
		downurl += "&cId=" + (this.cId.getValue() ? 1 : 0);
		downurl += "&cotId=" + (this.cotId.getValue() ? 1 : 0);
		downurl += "&title=" + (this.title.getValue() ? 1 : 0);
		downurl += "&typename=" + (this.typename.getValue() ? 1 : 0);
		downurl += "&contenttype=" + (this.contenttype.getValue() ? 1 : 0);
		downurl += "&cat1=" + (this.cat1.getValue() ? 1 : 0);
		downurl += "&cat2=" + (this.cat2.getValue() ? 1 : 0);
		downurl += "&cat3=" + (this.cat3.getValue() ? 1 : 0);
		downurl += "&Sigungu=" + (this.Sigungu.getValue() ? 1 : 0);
		downurl += "&Area=" + (this.Area.getValue() ? 1 : 0);
		downurl += "&status=" + (this.status.getValue() ? 1 : 0);
		downurl += "&create_date=" + (this.create_date.getValue() ? 1 : 0);
		downurl += "&update_date=" + (this.update_date.getValue() ? 1 : 0);
		downurl += "&stfId=" + (this.stfId.getValue() ? 1 : 0);
		downurl += "&otdId=" + (this.otdId.getValue() ? 1 : 0);
		downurl += "&catchphrase=" + (this.catchphrase.getValue() ? 1 : 0);
		downurl += "&authcode=" + (this.authcode.getValue() ? 1 : 0);
		downurl += "&setstatus=" + (this.setstatus.getValue() ? 1 : 0);
		downurl += "&fesSdate=" + (this.fesSdate.getValue() ? 1 : 0);
		downurl += "&fesEdate=" + (this.fesEdate.getValue() ? 1 : 0);

		// 검색조건 관련 파라메터
		String strsdate = sdate.getText().toString();
		if (!"".equals(strsdate))
			downurl += "&startInput=" + strsdate;
		String stredate = edate.getText().toString();
		if (!"".equals(stredate)) 
			downurl += "&endInput=" + stredate + " 23:59:59";
		if(OTD_ID != null)
			downurl += "&OTD_ID=" + OTD_ID;
		downurl += "&mode=" +mode.getSelectedIndex();
		downurl += "&keyword=" +keyWord;
		downurl += "&dateType=" +dateopt.getSelectedIndex();
		downurl += "&contentPart=" +Firstcategory.getSelectedIndex();
		downurl += "&StatusSearch=" +StatusSearch.getSelectedValue().get(0);
		if (areatype.getSelectedIndex() > 0) {
			String areacode = areatype.getValues().get(areatype.getSelectedIndex()).toString().split("\\^")[0];
			downurl += "&areaCode=" +areacode;
			if (!sigungutype.getValues().get(sigungutype.getSelectedIndex()).toString().equals("0")) {
				String sigunguname = sigungutype.getValues().get(sigungutype.getSelectedIndex()).toString()
						.split("\\^")[0];
				downurl += "&sigunguname=" +sigunguname;
			}
		}

			
		
		Window.open(downurl,"_self", "enabled");
		String[] alertmessages = {"엑셀 다운로드 알림","데이터 양이 많을 경우","최대 2분정도의 시간이 소요 될 수 있습니다."};
		alert("알림", 500, 300, alertmessages);
	}
	
	public void AllCheck(boolean check) {
		cId.setValue(check);
		cotId.setValue(check);
		title.setValue(check);
		typename.setValue(check);
		contenttype.setValue(check);
		cat1.setValue(check);
		cat2.setValue(check);
		cat3.setValue(check);
		Sigungu.setValue(check);
		Area.setValue(check);
		status.setValue(check);
		Area.setValue(check);
		create_date.setValue(check);
		update_date.setValue(check);
		stfId.setValue(check);
		otdId.setValue(check);
		catchphrase.setValue(check);
		authcode.setValue(check);
		setstatus.setValue(check);
		fesSdate.setValue(check);
		fesEdate.setValue(check);
		
	}
	
	private void CheckBoxCheck() {
		
		int Check = this.cId.getValue() ? 1 : 0;
		Check += this.cId.getValue() ? 1 : 0;
		Check += this.cotId.getValue() ? 1 : 0;
		Check += this.title.getValue() ? 1 : 0;
		Check += this.typename.getValue() ? 1 : 0;
		Check += this.contenttype.getValue() ? 1 : 0;
		Check += this.cat1.getValue() ? 1 : 0;
		Check += this.cat2.getValue() ? 1 : 0;
		Check += this.cat3.getValue() ? 1 : 0;
		Check += this.Sigungu.getValue() ? 1 : 0;
		Check += this.Area.getValue() ? 1 : 0;
		Check += this.status.getValue() ? 1 : 0;
		Check += this.create_date.getValue() ? 1 : 0;
		Check += this.update_date.getValue() ? 1 : 0;
		Check += this.stfId.getValue() ? 1 : 0;
		Check += this.otdId.getValue() ? 1 : 0;
		Check += this.catchphrase.getValue() ? 1 : 0;
		Check += this.authcode.getValue() ? 1 : 0;
		Check += this.setstatus.getValue() ? 1 : 0;
		Check += this.fesSdate.getValue() ? 1 : 0;
		Check += this.fesEdate.getValue() ? 1 : 0;
		
		if(Check == 0 ) {
			String[] alertmessages = {"엑셀 다운로드 알림","노출항목을 선택해주세요."};
			alert("알림", 500, 300, alertmessages);
			return;
		}
		
		String keyWord = edidname.getText().replaceAll("\\\\", "");
		
		JSONObject parameterJSON = new JSONObject();
		parameterJSON.put("cmd", new JSONString("SELECT_XLS_COUNT"));
		parameterJSON.put("select_type", new JSONString("DatabaseList"));
		parameterJSON.put("mode", new JSONNumber(mode.getSelectedIndex()));
		parameterJSON.put("keyword", new JSONString(keyWord));
		parameterJSON.put("contentPart", new JSONNumber(Firstcategory.getSelectedIndex()));
		parameterJSON.put("StatusSearch", new JSONNumber((int)StatusSearch.getSelectedValue().get(0)));
		
		String strsdate = sdate.getText().toString();
		if (!"".equals(strsdate))
			parameterJSON.put("startInput", new JSONString(strsdate));
		String stredate = edate.getText().toString();
		if (!"".equals(stredate)) 
			parameterJSON.put("endInput", new JSONString(stredate+" 23:59:59"));
		if(OTD_ID != null)
			parameterJSON.put("OTD_ID", new JSONString(OTD_ID));
		parameterJSON.put("dateType", new JSONNumber(dateopt.getSelectedIndex()));
		if (areatype.getSelectedIndex() > 0) {
			String areacode = areatype.getValues().get(areatype.getSelectedIndex()).toString().split("\\^")[0];
			parameterJSON.put("areacode", new JSONString(areacode));
			if (!sigungutype.getValues().get(sigungutype.getSelectedIndex()).toString().equals("0")) {
				String sigunguname = sigungutype.getValues().get(sigungutype.getSelectedIndex()).toString()
						.split("\\^")[0];
				parameterJSON.put("sigunguname", new JSONString(sigunguname));
			}
		}
		
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
}
