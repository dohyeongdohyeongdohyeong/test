package kr.or.visitkorea.admin.client.manager.memberActivity.panels;

import java.util.HashMap;
import java.util.Map;

import com.google.gwt.dom.client.Style.FontWeight;
import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Widget;

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
import kr.or.visitkorea.admin.client.manager.VisitKoreaBusiness;
import kr.or.visitkorea.admin.client.manager.memberActivity.ActivityApplication;
import kr.or.visitkorea.admin.client.manager.memberActivity.MemberActivityMain;
import kr.or.visitkorea.admin.client.manager.widgets.ContentTable;
import kr.or.visitkorea.admin.client.manager.widgets.ContentTableRow;

public class CoursePanel extends BasePanel {
	private ContentTable table;
	private MaterialLabel countLabel;
	private MaterialTextBox sdate, edate;
	private MaterialComboBox<Object> cbidname, openBox;
	private MaterialTextBox edidname;
	private int totcnt;
	private int index;
	private int offset;
	private MaterialButton ExcelButton;
	
	public CoursePanel(MemberActivityMain host) {
		super(host);
	}
	
	@Override
	protected Widget createBody() {
		table = new ContentTable(ContentTable.TABLE_SELECT_TYPE.SELECT_SINGLE);
		table.setLayoutPosition(Position.ABSOLUTE);
		table.setTop(80); table.setWidth("97.5%");table.setHeight(555);table.setMargin(10);
		table.appendTitle("번호", 100, TextAlign.CENTER);
		table.appendTitle("코스제목", 300, TextAlign.CENTER);
		table.appendTitle("코스수", 200, TextAlign.CENTER);
		table.appendTitle("작성자", 150, TextAlign.CENTER);
		table.appendTitle("등록일시", 200, TextAlign.CENTER);
		table.appendTitle("수정일시", 200, TextAlign.CENTER);
		table.appendTitle("공유건수", 200, TextAlign.CENTER);
		table.appendTitle("공개여부", 80, TextAlign.CENTER);
		this.add(table);
		
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
		
		ExcelButton = new MaterialButton("엑셀 다운로드");
		ExcelButton.setVisible(true);
		ExcelButton.addClickHandler(event->{
			String downurl = "./call?cmd=FILE_DOWNLOAD_XLS";
			downurl += "&select_type=SnsCourse";
			Window.open(downurl,"_self", "enabled");
		});
		
		table.getButtomMenu().addButton(ExcelButton, com.google.gwt.dom.client.Style.Float.LEFT);
		
		return table;
	}
	@Override
	protected Widget createSearchForm() {
		MaterialPanel row = new MaterialPanel();
		row.setWidth("100%");
		row.setHeight("80px");
		row.setPadding(10);
		
		cbidname = new MaterialComboBox<>();
		cbidname.setLayoutPosition(Position.ABSOLUTE);
		cbidname.setLabel("검색조건");
		cbidname.setTop(45); cbidname.setLeft(15); cbidname.setWidth("200px");
		cbidname.addItem("코스제목", "0");
		cbidname.addItem("아이디", "1");
		cbidname.addItem("이름(별명)", "2");
		row.add(cbidname);
		
		openBox = new MaterialComboBox<>();
		openBox.setLayoutPosition(Position.ABSOLUTE);
		openBox.setLabel("공개여부");
		openBox.setTop(45); openBox.setLeft(225); openBox.setWidth("200px");
		openBox.addItem("공개여부", "-1");
		openBox.addItem("비공개", "0");
		openBox.addItem("공개", "1");
		row.add(openBox);
		
		edidname = new MaterialTextBox();
		edidname.setLayoutPosition(Position.ABSOLUTE);
		edidname.setLabel("검색어입력");
		edidname.setTop(45); edidname.setLeft(435); edidname.setWidth("500px");
		row.add(edidname);
		edidname.addKeyPressHandler(event -> {
			if (event.getNativeEvent().getKeyCode() == KeyCodes.KEY_ENTER) {
				qryList(true);
			}
		});
		
		sdate = new MaterialTextBox();
		sdate.setType(InputType.DATE);
		sdate.setLayoutPosition(Position.ABSOLUTE);
		sdate.setLabel("기간"); sdate.setText("-");
		sdate.setTop(45); sdate.setRight(240); sdate.setWidth("150px");
		row.add(sdate);
		edate = new MaterialTextBox();
		edate.setType(InputType.DATE);
		edate.setLayoutPosition(Position.ABSOLUTE);
		edate.setTop(45); edate.setRight(80); edate.setWidth("150px");
		row.add(edate);
		return row;
	}
	public void qryList(boolean bstart) {
		if(bstart) {
			offset = 0; 
			index = 1;
			table.clearRows();
		} else offset += 20;
		
		JSONObject parameterJSON = new JSONObject();
		parameterJSON.put("cmd", new JSONString("SELECT_SNSCOURSE_LIST"));
		String strsdate = sdate.getText().toString();
		if(!"".equals(strsdate)) {
			parameterJSON.put("sdate", new JSONString(strsdate));
		}
		String stredate = edate.getText().toString();
		if(!"".equals(stredate)) {
			parameterJSON.put("edate", new JSONString(stredate+" 23:59:59"));
		}

		String idname = edidname.getText();
		if(!"".equals(idname)) {
			if(cbidname.getSelectedIndex()== 0)
				parameterJSON.put("title", new JSONString(idname));
			else if(cbidname.getSelectedIndex()== 1)
				parameterJSON.put("id", new JSONString(idname));
			else parameterJSON.put("name", new JSONString(idname));
		}
		
		if (!"-1".equals(openBox.getSelectedValue().get(0)))
			parameterJSON.put("isOpen", new JSONString(openBox.getSelectedValue().get(0).toString()));
		
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
					totcnt = (int)bodyResultcnt.get("CNT").isNumber().doubleValue();//Integer.parseInt(bodyResultcnt.get("CNT").isString().stringValue());
					
					int usrCnt = bodyResultObj.size();
					if (usrCnt == 0) {
						MaterialToast.fireToast("검색된 결과가 없습니다.", 3000);
					}
					
					for(int i= 0;i< usrCnt;i++) {
						JSONObject obj = (JSONObject)bodyResultObj.get(i);
						ContentTableRow tableRow = table.addRow(Color.WHITE
								, new int[] {1}
								,index++
								,obj.get("title").isString().stringValue()
								,obj.get("cnt").toString().replaceAll("\"", "") + " 코스"
								,obj.get("id").isString().stringValue()
								,obj.get("date").isString().stringValue()
								,obj.containsKey("updateDate") ? obj.get("updateDate").isString().stringValue() : obj.get("date").isString().stringValue() 
								,obj.get("shareCnt").isNumber().toString()
								,obj.get("isOpen").isNumber().toString() == "0" ? "비공개" : 
									obj.get("isOpen").isNumber().toString() == "1" ? "공개" : "-"
								);
						tableRow.put("crsid", obj.get("crsid").isString().stringValue());
						tableRow.put("id", obj.get("id").isString().stringValue());
						
						tableRow.addClickHandler(event -> {
							ContentTableRow ctr = (ContentTableRow) event.getSource();
							
							if (ctr.getSelectedColumn() == 1) {
								Map<String, Object> parameterMap = new HashMap<String, Object>();
								parameterMap.put("crsid", obj.get("crsid") != null ? obj.get("crsid").isString().stringValue() : "");
								parameterMap.put("title", obj.get("title") != null ? obj.get("title").isString().stringValue() : "");
								parameterMap.put("cnt", obj.get("cnt") != null ? obj.get("cnt").isNumber().toString() : "");
								parameterMap.put("id", obj.get("id") != null ? obj.get("id").isString().stringValue(): "");
								parameterMap.put("date", obj.get("date") != null ? obj.get("date").isString().stringValue() : "");
								parameterMap.put("updateDate", obj.get("updateDate") != null ? obj.get("updateDate").isString().stringValue(): "");
								parameterMap.put("shareCnt", obj.get("shareCnt")!= null ? obj.get("shareCnt").isNumber().toString() : "");
								parameterMap.put("isOpen", obj.get("isOpen") != null ? obj.get("isOpen").isNumber().toString() : "");
								parameterMap.put("desc", obj.get("desc")!= null ? obj.get("desc").isString().stringValue() : "");
								parameterMap.put("row", ctr);
								parameterMap.put("obj", obj);
								host.getMaterialExtentsWindow().openDialog(ActivityApplication.COURSE_DETAIL, parameterMap, 1300);
							}
						});
					}
					
				}else if (processResult.equals("fail")) {
					host.getMaterialExtentsWindow().alert("검색이 실패했습니다. 관리자에게 문의하세요.", 500);
					countLabel.setText("실패?");
				}
				table.loading(false);
			}
		});
	}

	@Override
	protected void onLoad() {
		// TODO Auto-generated method stub
		
	}
}