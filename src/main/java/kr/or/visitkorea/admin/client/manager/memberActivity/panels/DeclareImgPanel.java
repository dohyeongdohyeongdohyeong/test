package kr.or.visitkorea.admin.client.manager.memberActivity.panels;

import java.util.Date;
import java.util.HashMap;

import com.google.gwt.dom.client.Style.FontWeight;
import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONNumber;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.user.client.ui.Widget;

import gwt.material.design.addins.client.combobox.MaterialComboBox;
import gwt.material.design.client.base.MaterialWidget;
import gwt.material.design.client.constants.Color;
import gwt.material.design.client.constants.Display;
import gwt.material.design.client.constants.FlexAlignItems;
import gwt.material.design.client.constants.FlexJustifyContent;
import gwt.material.design.client.constants.IconType;
import gwt.material.design.client.constants.TextAlign;
import gwt.material.design.client.ui.MaterialIcon;
import gwt.material.design.client.ui.MaterialImage;
import gwt.material.design.client.ui.MaterialLabel;
import gwt.material.design.client.ui.MaterialPanel;
import gwt.material.design.client.ui.MaterialTextBox;
import gwt.material.design.client.ui.MaterialToast;
import gwt.material.design.jscore.client.api.JSON;
import gwt.material.design.jscore.client.api.JsObject;
import kr.or.visitkorea.admin.client.application.Registry;
import kr.or.visitkorea.admin.client.manager.VisitKoreaBusiness;
import kr.or.visitkorea.admin.client.manager.memberActivity.MemberActivityMain;
import kr.or.visitkorea.admin.client.manager.widgets.ContentTable;
import kr.or.visitkorea.admin.client.manager.widgets.ContentTableRow;
import kr.or.visitkorea.admin.client.manager.widgets.SelectionPanel;

public class DeclareImgPanel extends BasePanel {
	private ContentTable table;
	private MaterialLabel countLabel;
	private MaterialComboBox<Object> cbidname;
	private MaterialTextBox edidname;
	private int offset;
	private int totcnt;
	private int index;
	
	public DeclareImgPanel(MemberActivityMain host) {
		super(host);
	}

	@Override
	protected Widget createBody() {
		table = new ContentTable(ContentTable.TABLE_SELECT_TYPE.SELECT_SINGLE);
		table.setLayoutPosition(Position.ABSOLUTE);
		table.setTop(80); table.setWidth("97.5%");table.setHeight(555);table.setMargin(10);
		table.appendTitle("번호", 100, TextAlign.CENTER);
		table.appendTitle("컨텐츠명", 340, TextAlign.CENTER);
		table.appendTitle("신고자", 230, TextAlign.CENTER);
		table.appendTitle("신고일시", 200, TextAlign.CENTER);
		table.appendTitle("신고사진", 400, TextAlign.CENTER);
		table.appendTitle("노출관리", 200, TextAlign.CENTER);
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
		edidname.addKeyPressHandler(event -> {
			if (event.getNativeEvent().getKeyCode() == KeyCodes.KEY_ENTER) {
				qryList(true);
			}
		});
		
		row.add(edidname);
		return row;
	}
	
	public void qryList(boolean bstart) {
		if (bstart) {
			offset = 0;
			index = 1;
			table.clearRows();
		} else offset += 20;
		
		JSONObject parameterJSON = new JSONObject();
		parameterJSON.put("cmd", new JSONString("SELECT_DECLARE_IMAGE_LIST"));
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
				countLabel.setText(bodyResultcnt.get("CNT") + " 건");
				totcnt = (int) bodyResultcnt.get("CNT").isNumber().doubleValue();

				int size = bodyResultObj.size();
				if (size == 0) {
					MaterialToast.fireToast("검색된 결과가 없습니다.", 3000);
				}
				
				for (int i = 0; i < size; i++) {
					JSONObject obj = bodyResultObj.get(i).isObject();
					
					String declareDate = "-";
					if (obj.containsKey("DECLARE_DATE")) {
						Date dDate = DateTimeFormat.getFormat("yyyy-MM-dd HH:mm:ss.s").parse(obj.get("DECLARE_DATE").isString().stringValue());
						declareDate = DateTimeFormat.getFormat("yyyy-MM-dd").format(dDate);
					}
					ContentTableRow tableRow = table.addRow(Color.WHITE, new int[] {1}
						,index++
						,obj.containsKey("TITLE") ? obj.get("TITLE").isString().stringValue() : "-"
						,obj.containsKey("SNS_IDENTIFY") ? obj.get("SNS_IDENTIFY").isString().stringValue() : "-"
						,declareDate
						,""
						,""
					);
					tableRow.setDisplay(Display.FLEX);
					tableRow.setFlexAlignItems(FlexAlignItems.CENTER);
					tableRow.setHeight("150px");
					tableRow.addClickHandler(event -> {
						ContentTableRow ctr = (ContentTableRow) event.getSource();
						if (ctr.getSelectedColumn() == 1) {
							MaterialIcon courseIcon5 = new MaterialIcon(IconType.WEB);
							StringBuffer tgrUrl = new StringBuffer("https://korean.visitkorea.or.kr/detail/detail_view.html?cotid=")
									 .append(obj.containsKey("COT_ID") ? obj.get("COT_ID").isString().stringValue() : "");
//							StringBuffer tgrUrl = new StringBuffer(Registry.get("service.server") + "/detail/detail_view.html?cotid=")
//									 .append(obj.containsKey("COT_ID") ? obj.get("COT_ID").isString().stringValue() : "");
							Registry.openPreview(courseIcon5, tgrUrl.toString());
						}
					});
					
					String imgId = obj.containsKey("IMG_ID") ? obj.get("IMG_ID").isString().stringValue() : "";
					String imgPath = obj.containsKey("IMAGE_PATH") ? obj.get("IMAGE_PATH").isString().stringValue() : "";
					MaterialLabel imageLabel = (MaterialLabel) tableRow.getColumnObject(4);
//					MaterialImage image = new MaterialImage(
//							Registry.get("image.server") + "/img/call?cmd=VIEW&id=" + imgId);
					
					MaterialImage image = new MaterialImage(
							Registry.get("image.server") + "/img/call?cmd=TEMP_VIEW&name="+ imgPath.substring(imgPath.lastIndexOf("/") + 1));
					
					image.setPadding(5);
					image.setHeight("150px");
					
					this.addWidgetToColumn(imageLabel, image);
					
					int visiable = obj.containsKey("IS_VISIABLE") ? (int) obj.get("IS_VISIABLE").isNumber().doubleValue() : -1;
					MaterialLabel isViewLabel = (MaterialLabel) tableRow.getColumnObject(5);
					SelectionPanel isView = addViewPanel(imgId);
					isView.setSelectionOnSingleMode(visiable == 1 ? "노출" : "비노출");

					this.addWidgetToColumn(isViewLabel, isView);
				}
			} else if (process.equals("fail")) {
				host.getMaterialExtentsWindow().alert("검색이 실패했습니다. 관리자에게 문의하세요.", 500);
				countLabel.setText("실패?");
			}
			table.loading(false);
		});
	}
	
	public SelectionPanel addViewPanel(String imgId) {
		HashMap<String, Object> viewValueMap = new HashMap<>();
		viewValueMap.put("노출", 1);
		viewValueMap.put("비노출", 0);
		
		SelectionPanel isView = new SelectionPanel();
		isView.setValues(viewValueMap);
		isView.addStatusChangeEvent(event -> {
			JSONObject paramJson = new JSONObject();
			paramJson.put("cmd", new JSONString("UPDATE_IMAGE_VISIABLE"));
			paramJson.put("imgId", new JSONString(imgId));
			paramJson.put("visiable", new JSONNumber((int) isView.getSelectedValue()));
			VisitKoreaBusiness.post("call", paramJson.toString(), (o1, o2, o3) -> {});
		});
		return isView;
	}

	@Override
	protected void onLoad() {
		// TODO Auto-generated method stub
		
	}
}
