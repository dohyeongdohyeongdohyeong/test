package kr.or.visitkorea.admin.client.manager.memberActivity.panels;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.google.gwt.dom.client.Style.Float;
import com.google.gwt.dom.client.Style.FontWeight;
import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.user.client.ui.Widget;

import gwt.material.design.addins.client.combobox.MaterialComboBox;
import gwt.material.design.client.constants.Color;
import gwt.material.design.client.constants.IconType;
import gwt.material.design.client.constants.TextAlign;
import gwt.material.design.client.ui.MaterialButton;
import gwt.material.design.client.ui.MaterialIcon;
import gwt.material.design.client.ui.MaterialLabel;
import gwt.material.design.client.ui.MaterialPanel;
import gwt.material.design.client.ui.MaterialTextBox;
import gwt.material.design.client.ui.MaterialToast;
import gwt.material.design.jscore.client.api.JSON;
import gwt.material.design.jscore.client.api.JsObject;
import kr.or.visitkorea.admin.client.application.Console;
import kr.or.visitkorea.admin.client.manager.VisitKoreaBusiness;
import kr.or.visitkorea.admin.client.manager.contents.recommand.RecommApplication;
import kr.or.visitkorea.admin.client.manager.memberActivity.ActivityApplication;
import kr.or.visitkorea.admin.client.manager.memberActivity.MemberActivityMain;
import kr.or.visitkorea.admin.client.manager.widgets.ContentTable;
import kr.or.visitkorea.admin.client.manager.widgets.ContentTableRow;

public class UsrImgPanel extends BasePanel {

	private ContentTable table;
	private MaterialLabel countLabel;
	private MaterialComboBox<Object> cbidname;
	private MaterialTextBox edidname;
	private int offset;
	private int totcnt;
	private int index;
	private MaterialLabel NotVisibleCntLabel;
	private MaterialLabel VisibleCntLabel;
	
	public UsrImgPanel(MemberActivityMain host) {
		super(host);
	}

	@Override
	protected Widget createBody() {
		table = new ContentTable(ContentTable.TABLE_SELECT_TYPE.SELECT_SINGLE);
		table.setLayoutPosition(Position.ABSOLUTE);
		table.setTop(80); table.setWidth("97.5%"); table.setHeight(555); table.setMargin(10);
		table.appendTitle("번호", 200, TextAlign.CENTER);
		table.appendTitle("컨텐츠명", 500, TextAlign.CENTER);
		table.appendTitle("사진등록일시", 400, TextAlign.CENTER);
		table.appendTitle("사진수", 250, TextAlign.CENTER);
		this.add(table);
		
		
		
		MaterialIcon nextRowsIcon = new MaterialIcon(IconType.ARROW_DOWNWARD);
		nextRowsIcon.setTextAlign(TextAlign.CENTER);
		nextRowsIcon.addClickHandler(event->{
			if ((offset + 20) >= totcnt) {
				MaterialToast.fireToast("더이상의 결과 값이 없습니다.", 3000);
			} else {
				qryList(false);
			}
		});
		table.getButtomMenu().addIcon(nextRowsIcon, "다음 20개", Float.RIGHT);
		VisibleCntLabel = new MaterialLabel("이미지 노출건수 0 건 / ");
		VisibleCntLabel.setMarginLeft(10);
		VisibleCntLabel.setFontWeight(FontWeight.BOLDER);
		VisibleCntLabel.setTextColor(Color.BLACK);
		table.getButtomMenu().addLabel(VisibleCntLabel, Float.LEFT);
		
		NotVisibleCntLabel = new MaterialLabel("비노출건수 0 건");
		NotVisibleCntLabel.setFontWeight(FontWeight.BOLDER);
		NotVisibleCntLabel.setTextColor(Color.BLACK);
		table.getButtomMenu().addLabel(NotVisibleCntLabel, Float.LEFT); 
		
		countLabel = new MaterialLabel("0 건 ");
		countLabel.setFontWeight(FontWeight.BOLDER);
		countLabel.setTextColor(Color.BLACK);
		table.getButtomMenu().addLabel(countLabel, Float.RIGHT);
		
		
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
		cbidname.addItem("컨텐츠명", "0");
		row.add(cbidname);
		
		edidname = new MaterialTextBox();
		edidname.setLayoutPosition(Position.ABSOLUTE);
		edidname.setLabel("검색어입력");
		edidname.setTop(45); edidname.setLeft(235); edidname.setWidth("500px");
		row.add(edidname);
		edidname.addKeyPressHandler(event -> {
			if (event.getNativeEvent().getKeyCode() == KeyCodes.KEY_ENTER) {
				qryList(true);
			}
		});
		
		MaterialIcon searchIcon = new MaterialIcon(IconType.SEARCH);
		searchIcon.setTextAlign(TextAlign.CENTER);
		searchIcon.setMarginTop(7);
		searchIcon.addClickHandler(event->{
			qryList(true);
		});
		row.add(searchIcon);
		
		MaterialButton EXCELBUTTON = new MaterialButton("엑셀 다운로드");
		EXCELBUTTON.setLayoutPosition(Position.ABSOLUTE);
		EXCELBUTTON.setTop(65); 
		EXCELBUTTON.setRight(30); 
		
		EXCELBUTTON.addClickHandler(event->{
			
			Map<String, Object> paramters = new HashMap<String, Object>();
			host.getMaterialExtentsWindow().openDialog(ActivityApplication.USER_IMAGE_EXCEL, paramters, 400);
			
		});
		
		row.add(EXCELBUTTON);
		
		return row;
	}
	
	public void qryList(boolean bstart) {
		if (bstart) {
			offset = 0;
			index = 1;
			table.clearRows();
		} else offset += 20;
		
		JSONObject parameterJSON = new JSONObject();
		parameterJSON.put("cmd", new JSONString("SELECT_USER_IMAGE_LIST"));
		parameterJSON.put("title", new JSONString(edidname.getValue()));
		parameterJSON.put("offset", new JSONString(offset + ""));
		table.loading(true);
		VisitKoreaBusiness.post("call", parameterJSON.toString(), (param1, param2, param3) -> {
			JSONObject resultObj = (JSONObject) JSONParser.parseStrict(JSON.stringify((JsObject) param1));
			JSONObject headerObj = resultObj.get("header").isObject();
			String process = headerObj.get("process").isString().stringValue();
			
			if (process.equals("success")) {
				JSONObject bodyObj = resultObj.get("body").isObject();
				JSONArray bodyResultObj = bodyObj.get("result").isArray();
				JSONObject bodyResultcnt = bodyObj.get("resultCnt").isObject();
				JSONObject bodyResultimgcnt = bodyObj.get("resultImgCnt").isObject();
				countLabel.setText(bodyResultcnt.get("CNT") + " 건");
				NotVisibleCntLabel.setText("비노출건수 " + bodyResultimgcnt.get("NOT_VISIABLE_IMGCNT") + " 건 ");
				VisibleCntLabel.setText("이미지 노출건수 " + bodyResultimgcnt.get("VISIABLE_IMGCNT") + " 건 / ");
				totcnt = (int) bodyResultcnt.get("CNT").isNumber().doubleValue();

				int usrCnt = bodyResultObj.size();
				if (usrCnt == 0) {
					MaterialToast.fireToast("검색된 결과가 없습니다.", 3000);
				}
				
				for (int i = 0; i < usrCnt; i++) {
					JSONObject obj = (JSONObject)bodyResultObj.get(i);
					
					String date = "";
					if (obj.containsKey("date")) {
						Date dDate = DateTimeFormat.getFormat("yyyy-MM-dd HH:mm:ss.s").parse(obj.get("date").isString().stringValue());
						date = DateTimeFormat.getFormat("yyyy-MM-dd").format(dDate);
					}
					
					ContentTableRow tableRow = table.addRow(Color.WHITE, new int[] {1}
							,index++
							,obj.containsKey("title") ? obj.get("title").isString().stringValue() : "-"
							,date
							,obj.get("imgCnt").isNumber().toString()
							);
					
					tableRow.addClickHandler(event -> {
						ContentTableRow ctr = (ContentTableRow) event.getSource();
						
						if (ctr.getSelectedColumn() == 1) {
							HashMap<String, Object> params = new HashMap<String, Object>();
							params.put("title", obj.containsKey("title") ? obj.get("title").isString().stringValue() : "");
							params.put("cotId", obj.containsKey("cotId") ? obj.get("cotId").isString().stringValue() : "");
							host.getMaterialExtentsWindow().openDialog(ActivityApplication.USER_IMAGE_DETAIL, params, 700);
						}
					});
				}
			} else if (process.equals("fail")) {
				host.getMaterialExtentsWindow().alert("검색이 실패했습니다. 관리자에게 문의하세요.", 500);
				countLabel.setText("실패?");
			}
			table.loading(false);
		});
	}

	@Override
	protected void onLoad() {
		// TODO Auto-generated method stub
		
	}
}
