package kr.or.visitkorea.admin.client.manager.contents.recommand.dialogs;

import java.util.ArrayList;
import java.util.Map;
import java.util.Set;

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
import kr.or.visitkorea.admin.client.application.Registry;
import kr.or.visitkorea.admin.client.manager.VisitKoreaBusiness;
import kr.or.visitkorea.admin.client.widgets.dialog.DialogContent;
import kr.or.visitkorea.admin.client.widgets.window.MaterialExtentsWindow;

public class ExcelDownloadDialog extends DialogContent {

	private MaterialLabel dialogTitle;
	private MaterialComboBox<Object> areatype, sigungutype;
	private MaterialComboBox<Object> dateopt, mode;
	private MaterialTextBox edidname;
	private MaterialTextBox sdate;
	private MaterialTextBox edate;
	private MaterialComboBox<Object> Firstcategory, Secondcategory;
	private MaterialCheckBox cId;
	private MaterialCheckBox cotId;
	private MaterialCheckBox title;
	private MaterialCheckBox create_date;
	private MaterialCheckBox status;
	private MaterialCheckBox finalupdate_date;
	private MaterialCheckBox stfId;
	private MaterialCheckBox otdId;
	private MaterialCheckBox Kogl;
	private MaterialCheckBox update_date;
	private MaterialCheckBox Sigungu;
	private MaterialCheckBox Area;
	private MaterialCheckBox secondcategory;
	private MaterialCheckBox firstcategory;
	private MaterialCheckBox categorytype;
	private MaterialCheckBox contentdiv;
	private boolean check;
	private String OTD_ID;
	private MaterialComboBox<Object> StatusSearch,Division;

	public ExcelDownloadDialog(MaterialExtentsWindow tgrWindow) {
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
		dialogTitle = new MaterialLabel("엑셀 다운로드 - 추천 컨텐츠");
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
		basePanel.add(CreateTitleLabel("노출 항목", TextAlign.CENTER));
		MaterialRow CheckRow = addRow(basePanel,null,null,null);
		CheckRow.setHeight("365px");
		basePanel.setWidth("200px");
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
		
		cId = CreateCheckBox(CheckRow, "CID");
		cotId = CreateCheckBox(CheckRow, "COTID");
		title = CreateCheckBox(CheckRow, "컨텐츠 제목");
		categorytype = CreateCheckBox(CheckRow, "타입 코드");
		firstcategory = CreateCheckBox(CheckRow, "분류");
		secondcategory = CreateCheckBox(CheckRow, "분류 2순위");
		contentdiv = CreateCheckBox(CheckRow, "구분");
		Area = CreateCheckBox(CheckRow, "지역");
		Sigungu = CreateCheckBox(CheckRow, "시군구");
		status = CreateCheckBox(CheckRow, "처리 상태");
		create_date = CreateCheckBox(CheckRow, "생성일");
		update_date = CreateCheckBox(CheckRow, "수정일");
		finalupdate_date = CreateCheckBox(CheckRow, "최종 수정일");
		stfId = CreateCheckBox(CheckRow, "생성자");
		otdId = CreateCheckBox(CheckRow, "타부서 적용여부");
		Kogl = CreateCheckBox(CheckRow, "공공누리 적용여부");
	}

	private Widget CreateSearchPanel(MaterialPanel panel) {

		mode = new MaterialComboBox<>();
		mode.addItem("제목", 0);
		mode.addItem("CID", 1);
		mode.addItem("태그", 2);
		mode.addItem("작성자", 3);
		mode.setFloat(Float.LEFT);
		mode.setLabel("키워드 검색");
		mode.setMinHeight("68px");
		mode.setGrid("s3");
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
		edidname.setMinHeight("68px");
		edidname.setGrid("s9");
		edidname.setFloat(Float.LEFT);

		
		
		MaterialRow row1 = addRow(panel,mode,edidname,null);

		dateopt = new MaterialComboBox<>();
		dateopt.setLabel("날짜옵션");
		dateopt.setMinHeight("68px");
		dateopt.setGrid("s4");
//		dateopt.addItem("선택안함", 0);
		dateopt.addItem("생성일", 0);
		dateopt.addItem("수정일", 1);
		dateopt.addItem("최종 수정일", 2);
		dateopt.setSelectedIndex(0);
		dateopt.setFloat(Float.LEFT);
		panel.add(dateopt);

		sdate = new MaterialTextBox();
		sdate.setType(InputType.DATE);
		sdate.setLabel("기간");
		sdate.setMinHeight("68px");
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

		areatype = new MaterialComboBox<>();
		areatype.setLabel("지자체선택");
		areatype.setFloat(Float.LEFT);
		areatype.setGrid("s6");
		areatype.addItem("전체", 0);
		areatype.setMinHeight("68px");
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
		sigungutype.setMinHeight("68px");
		MaterialRow row3 = addRow(panel,areatype,sigungutype,null);

		Firstcategory = new MaterialComboBox<>();
		Firstcategory.setLabel("1순위 분류");
		Firstcategory.setMinHeight("68px");
		Firstcategory.setGrid("s6");

		Secondcategory = new MaterialComboBox<>();
		Secondcategory.setLabel("2순위 분류");
		Secondcategory.setGrid("s6");

		Map<String, Map<String,String>> map =(Map<String, Map<String,String>>) Registry.get("ARTICLE_CATEGORIES");
		
		Set<String> CategoryStringSet = map.keySet();
		ArrayList<String> CategoryStringList = new ArrayList<String>(CategoryStringSet);
		this.Firstcategory.addItem("전체");
		this.Secondcategory.addItem("전체");
		for (String key : CategoryStringList) {
				this.Firstcategory.addItem(map.get(key).get("VALUE"),key);
				this.Secondcategory.addItem(map.get(key).get("VALUE"),key);
		}
		
		

		
		MaterialRow row4 = addRow(panel,Firstcategory,Secondcategory,null);
		
		Division =  new MaterialComboBox<>();
		Division.setLabel("구분");
		Division.setGrid("s6");
		Division.setMinHeight("68px");
		Division.addItem("전체",-1);
		Division.addItem("일반 기사",0);
		Division.addItem("테마 기사",1);
		
		StatusSearch = new MaterialComboBox<>();
		StatusSearch.setLabel("처리상태");
		StatusSearch.setGrid("s6");
		StatusSearch.setMinHeight("68px");
		StatusSearch.addItem("전체",-1);
		StatusSearch.addItem("작업 미완료",0);
		StatusSearch.addItem("상태 미등록",1);
		StatusSearch.addItem("작업 완료",2);
		StatusSearch.addItem("비 노출",7);
		StatusSearch.addItem("삭제",8);
		StatusSearch.addItem("작업 보류",9);
		
		MaterialRow row5 = addRow(panel,Division,StatusSearch,null);
		
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
		downurl += "&select_type=RecommandList";

		// 노출 항목 관련 파라메터
		downurl += "&cId=" + (this.cId.getValue() ? 1 : 0);
		downurl += "&cotId=" + (this.cotId.getValue() ? 1 : 0);
		downurl += "&title=" + (this.title.getValue() ? 1 : 0);
		downurl += "&create_date=" + (this.create_date.getValue() ? 1 : 0);
		downurl += "&status=" + (this.status.getValue() ? 1 : 0);
		downurl += "&finalupdate_date=" + (this.finalupdate_date.getValue() ? 1 : 0);
		downurl += "&stfId=" + (this.stfId.getValue() ? 1 : 0);
		downurl += "&otdId=" + (this.otdId.getValue() ? 1 : 0);
		downurl += "&Kogl=" + (this.Kogl.getValue() ? 1 : 0);
		downurl += "&update_date=" + (this.update_date.getValue() ? 1 : 0);
		downurl += "&Sigungu=" + (this.Sigungu.getValue() ? 1 : 0);
		downurl += "&Area=" + (this.Area.getValue() ? 1 : 0);
		downurl += "&secondcategory=" + (this.secondcategory.getValue() ? 1 : 0);
		downurl += "&firstcategory=" + (this.firstcategory.getValue() ? 1 : 0);
		downurl += "&categorytype=" + (this.categorytype.getValue() ? 1 : 0);
		downurl += "&contentdiv=" + (this.contentdiv.getValue() ? 1 : 0);

		
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
		if (areatype.getSelectedIndex() > 0) {
			String areacode = areatype.getValues().get(areatype.getSelectedIndex()).toString().split("\\^")[0];
			downurl += "&areaCode=" +areacode;
			if (!sigungutype.getValues().get(sigungutype.getSelectedIndex()).toString().equals("0")) {
				String sigunguname = sigungutype.getValues().get(sigungutype.getSelectedIndex()).toString()
						.split("\\^")[1];
				downurl += "&sigunguname=" +sigunguname;
			}
		}

		if (Firstcategory.getSelectedIndex() > 0) {
			String categorycode = Firstcategory.getSelectedValue().get(0).toString();
			downurl += "&CategoryCode=" +categorycode;
		}
		
		if (Secondcategory.getSelectedIndex() > 0) {
			String categorycode = Secondcategory.getSelectedValue().get(0).toString();
			downurl += "&CategoryCode_two=" +categorycode;
		}
		
		
			String division = Division.getValues().get(Division.getSelectedIndex()).toString().split("\\^")[0];
			downurl += "&Division=" +division;
		
			downurl += "&StatusSearch=" +StatusSearch.getSelectedValue().get(0);
		
		Window.open(downurl,"_self", "enabled");
		String[] alertmessages = {"엑셀 다운로드 알림","데이터 양이 많을 경우","최대 2분정도의 시간이 소요 될 수 있습니다."};
		alert("알림", 500, 300, alertmessages);
	}
	
	public void AllCheck(boolean check) {
		cId.setValue(check);
		cotId.setValue(check);
		title.setValue(check);
		create_date.setValue(check);
		status.setValue(check);
		finalupdate_date.setValue(check);
		stfId.setValue(check);
		otdId.setValue(check);
		Kogl.setValue(check);
		update_date.setValue(check);
		Sigungu.setValue(check);
		Area.setValue(check);
		firstcategory.setValue(check);
		secondcategory.setValue(check);
		categorytype.setValue(check);
		contentdiv.setValue(check);
	}
	
	private void CheckBoxCheck() {
		int Check = this.cId.getValue() ? 1 : 0;
		Check += this.cId.getValue() ? 1 : 0;
		Check += this.cotId.getValue() ? 1 : 0;
		Check += this.title.getValue() ? 1 : 0;
		Check += this.create_date.getValue() ? 1 : 0;
		Check += this.status.getValue() ? 1 : 0;
		Check += this.finalupdate_date.getValue() ? 1 : 0;
		Check += this.stfId.getValue() ? 1 : 0;
		Check += this.otdId.getValue() ? 1 : 0;
		Check += this.Kogl.getValue() ? 1 : 0;
		Check += this.update_date.getValue() ? 1 : 0;
		Check += this.Sigungu.getValue() ? 1 : 0;
		Check += this.Area.getValue() ? 1 : 0;
		Check += this.firstcategory.getValue() ? 1 : 0;
		Check += this.categorytype.getValue() ? 1 : 0;
		Check += this.contentdiv.getValue() ? 1 : 0;
		
		if(Check == 0 ) {
			String[] alertmessages = {"엑셀 다운로드 알림","노출항목을 선택해주세요."};
			alert("알림", 500, 300, alertmessages);
			return;
		}
		
		String keyWord = edidname.getText().replaceAll("\\\\", "");
				
		JSONObject parameterJSON = new JSONObject();
		parameterJSON.put("cmd", new JSONString("SELECT_XLS_COUNT"));
		parameterJSON.put("select_type", new JSONString("RecommandList"));
		parameterJSON.put("mode", new JSONNumber(mode.getSelectedIndex()));
		parameterJSON.put("keyword", new JSONString(keyWord));
		if(Firstcategory.getSelectedIndex() != 0)
			parameterJSON.put("CategoryCode", new JSONString(Firstcategory.getSelectedValue().get(0).toString()));
		parameterJSON.put("StatusSearch", new JSONNumber((int)StatusSearch.getSelectedValue().get(0)));
		parameterJSON.put("division", new JSONNumber((int)(Division.getSelectedValue().get(0))));
		
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
	
	
	private MaterialRow addRow(MaterialWidget Widget ,MaterialWidget Child1, MaterialWidget Child2, MaterialWidget Child3) {
		MaterialRow row = new MaterialRow();
		row.setMarginBottom(0);
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
