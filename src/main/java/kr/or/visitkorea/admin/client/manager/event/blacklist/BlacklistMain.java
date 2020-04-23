package kr.or.visitkorea.admin.client.manager.event.blacklist;

import java.util.HashMap;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.dom.client.Style.FontWeight;
import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONNumber;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.user.client.Window;

import gwt.material.design.client.MaterialTable;
import gwt.material.design.client.constants.ButtonType;
import gwt.material.design.client.constants.Color;
import gwt.material.design.client.constants.Display;
import gwt.material.design.client.constants.IconType;
import gwt.material.design.client.constants.TextAlign;
import gwt.material.design.client.ui.MaterialButton;
import gwt.material.design.client.ui.MaterialColumn;
import gwt.material.design.client.ui.MaterialIcon;
import gwt.material.design.client.ui.MaterialLabel;
import gwt.material.design.client.ui.MaterialPanel;
import gwt.material.design.client.ui.MaterialRow;
import gwt.material.design.client.ui.MaterialTextBox;
import gwt.material.design.client.ui.MaterialToast;
import gwt.material.design.jscore.client.api.JSON;
import gwt.material.design.jscore.client.api.JsObject;
import kr.or.visitkorea.admin.client.application.Console;
import kr.or.visitkorea.admin.client.application.Registry;
import kr.or.visitkorea.admin.client.manager.VisitKoreaBusiness;
import kr.or.visitkorea.admin.client.manager.memberActivity.ActivityApplication;
import kr.or.visitkorea.admin.client.manager.widgets.AbstractContentPanel;
import kr.or.visitkorea.admin.client.manager.widgets.ContentTable;
import kr.or.visitkorea.admin.client.manager.widgets.ContentTableRow;
import kr.or.visitkorea.admin.client.manager.widgets.UploadPanelWithNoImage;
import kr.or.visitkorea.admin.client.widgets.window.MaterialExtentsWindow;
import net.sourceforge.htmlunit.corejs.javascript.StackStyle;

public class BlacklistMain extends AbstractContentPanel {

	private MaterialLabel UpdateLabel;
	private MaterialLabel FileNameLabel;
	private MaterialLabel NewFileNameLabel;
	private JSONArray BlacklistArray;
	private ContentTable table;
	private MaterialLabel countLabel;
	private int index;
	private String fileName;
	private String saveName;
	private String MainCheck;
	private String bfiId;
	
	public BlacklistMain(MaterialExtentsWindow window) {
		super(window);
	}
	
	@Override
	public void init() {
		
		this.setLayoutPosition(Position.RELATIVE);
		TopBody();
		BottomBody();
		qryList();
		
	}
	
	private void TopBody() {
		MaterialRow rowtop = new MaterialRow();
		add(rowtop);
		MaterialIcon SaveIcon = new MaterialIcon(IconType.SAVE);
		SaveIcon.setFloat(Style.Float.RIGHT);
		SaveIcon.setMargin(10);
		SaveIcon.setFontSize("30px");
		rowtop.add(SaveIcon);
		SaveIcon.addClickHandler(e ->{
			
			SaveData();
			
		});
		
		
		MaterialPanel main = new MaterialPanel();
		main.setMargin(100);
		main.setMarginTop(00);
		add(main);
		MaterialRow row = new MaterialRow();
		row.setMarginTop(-50);
		row.setHeight("340px");
		row.setPadding(20);
		main.add(row);
		
		MaterialLabel topinfo = new MaterialLabel("- 목록");
		topinfo.setTextColor(Color.BLUE);
		topinfo.setFontSize("23px");
		topinfo.setTextAlign(TextAlign.LEFT);
		topinfo.setFontWeight(FontWeight.BOLD);
		topinfo.setMarginLeft(10);
		row.add(topinfo);
		
		table = new ContentTable(ContentTable.TABLE_SELECT_TYPE.SELECT_SINGLE);
		table.setLayoutPosition(Position.ABSOLUTE);
		table.setTop(50);
		table.setWidth("85.1%");
		table.setHeight(400);
		table.setTopMenuVisible(false);
		
		table.appendTitle("번호", 150, TextAlign.CENTER);
		table.appendTitle("등록일", 210, TextAlign.CENTER);
		table.appendTitle("등록자", 300, TextAlign.CENTER);
		table.appendTitle("파일명", 300, TextAlign.CENTER);
		table.appendTitle("메인설정", 150, TextAlign.CENTER);
		table.appendTitle("다운로드", 160, TextAlign.CENTER);
		row.add(table);
		
		countLabel = new MaterialLabel("0 건");
		countLabel.setFontWeight(FontWeight.BOLDER);
		countLabel.setTextColor(Color.BLACK);
		table.getButtomMenu().addLabel(countLabel, com.google.gwt.dom.client.Style.Float.RIGHT);
		
	}

	private void BottomBody() {
		
		MaterialPanel main = new MaterialPanel();
		main.setMargin(100);
		main.setMarginTop(0);
		add(main);
		MaterialRow row = new MaterialRow();
		main.add(row);
		
		MaterialLabel bottominfo = new MaterialLabel("- 신규 등록");
		bottominfo.setTextColor(Color.BLUE);
		bottominfo.setFontSize("23px");
		bottominfo.setTextAlign(TextAlign.LEFT);
		bottominfo.setFontWeight(FontWeight.BOLD);
		bottominfo.setMarginBottom(15);
		bottominfo.setMarginLeft(10);
		row.add(bottominfo);
		
		addLabel(row, "파일명 ", TextAlign.CENTER, Color.GREY_LIGHTEN_3, "s2");
		 
		NewFileNameLabel = addLabel(row, "", TextAlign.CENTER, Color.WHITE, "s6");
		UploadPanelWithNoImage xlsFileUploader = new UploadPanelWithNoImage(0, 0
				, GWT.getHostPageBaseURL() + "call?cmd=BLACK_LIST_XLS&requestType=event");
		xlsFileUploader.setWidth("100%");
		
		MaterialButton xlsUploadBtn = xlsFileUploader.getBtn();
		xlsUploadBtn.setHeight("46.25px");
		xlsUploadBtn.setWidth("300px");
		xlsUploadBtn.setType(ButtonType.FLAT);
		xlsUploadBtn.setTop(-8);
		xlsUploadBtn.setRight(-100);
		xlsUploadBtn.setPadding(0);
		xlsUploadBtn.setText("엑셀 파일 업로드");
		xlsUploadBtn.setBackgroundColor(Color.BLUE_DARKEN_1);
		xlsUploadBtn.remove(0);
		xlsUploadBtn.setLineHeight(0);
		xlsUploadBtn.setTextColor(Color.WHITE);
		xlsUploadBtn.setTextAlign(TextAlign.CENTER);
		xlsFileUploader.getUploader().setAcceptedFiles(".xls, .xlsx");
		xlsFileUploader.getUploader().addSuccessHandler(event -> {
			JSONObject resultObj = JSONParser.parseStrict(event.getResponse().getBody()).isObject();
			NewFileNameLabel.setText(event.getTarget().getName().toString());
			JSONObject headerObj = resultObj.get("header").isObject();
			String process = headerObj.get("process").isString().stringValue();
			if (process.equals("success")) {
				JSONObject bodyResult = resultObj.get("body").isObject();
				JSONArray resultArr = bodyResult.get("resultArr").isArray();
				JSONObject Result = bodyResult.get("result").isArray().get(0).isObject();
				
				 fileName = Result.get("fileName").isString().stringValue();
				 saveName = Result.get("saveName").isString().stringValue();

				Console.log("filename ::" + fileName);
				Console.log("saveName ::" + saveName);
				this.BlacklistArray = new JSONArray();
				
				for (int i = 1; i < resultArr.size(); i++) {
					JSONArray resultRow = resultArr.get(i).isArray();

					JSONObject cellObj = new JSONObject();
					cellObj.put("HP", new JSONString(resultRow.get(0).isString().stringValue()));
					Console.log("HP :: " +resultRow.get(0).isString().stringValue());
					this.BlacklistArray.set(BlacklistArray.size(), cellObj);
				}
			} else {
				MaterialToast.fireToast(headerObj.get("ment").isString().stringValue());
			}
			
		});
		
		row.add(xlsFileUploader);
		
		
		
		row = new MaterialRow();
		
		MaterialLabel info1 = addLabel(row, " * 엑셀파일 업로드를 통하여 블랙리스트를 업데이트 할 수 있습니다.", 
				TextAlign.LEFT, Color.GREY_LIGHTEN_3, "s12");
		info1.setPaddingLeft(300);
		MaterialLabel info2 = addLabel(row, " * 업로드 된 블랙리스트는 이벤트의 블랙리스트 설정 시 기본 리스트로 제공됩니다.", 
				TextAlign.LEFT, Color.GREY_LIGHTEN_3, "s12");
		info2.setPaddingLeft(300);
		main.add(row);
		
	}
	
	
	protected MaterialLabel addLabel(MaterialRow row, String defaultValue, TextAlign tAlign, Color bgColor, String grid) {
		MaterialLabel tmpLabel = new MaterialLabel(defaultValue);
		tmpLabel.setTextAlign(tAlign);
		tmpLabel.setLineHeight(46.25);
		tmpLabel.setHeight("46.25px");
		tmpLabel.setBackgroundColor(bgColor);
		
		MaterialColumn col1 = new MaterialColumn();
		col1.setGrid(grid);
		col1.add(tmpLabel);
		row.add(col1);
		return tmpLabel;
	}	
	
	public void qryList() {
		table.clearRows();
		index = 0;
		table.loading(true);
		NewFileNameLabel.setText("");
		
		JSONObject paramJson = new JSONObject();
		paramJson.put("cmd", new JSONString("GET_BLACK_LIST_FILE_LIST"));
		VisitKoreaBusiness.post("call", paramJson.toString(), (param1, param2, param3) -> {
			JSONObject resultObj = JSONParser.parseStrict(JSON.stringify((JsObject) param1)).isObject();
			JSONObject headerObj = resultObj.get("header").isObject();
			String processResult = headerObj.get("process").isString().stringValue();
			if (processResult.equals("fail")) {
				MaterialToast.fireToast("업로드된 파일이 없습니다.", 3000);
			} else {
				JSONObject bodyObj = resultObj.get("body").isObject();
				JSONArray bodyResultObj = bodyObj.get("result").isArray();
				
				int totcnt = (int) bodyObj.get("resultCnt").isNumber().doubleValue();
				countLabel.setText(totcnt + " 건");
				
				int usrCnt = bodyResultObj.size();
				if (usrCnt == 0) {
					MaterialToast.fireToast("업로드된 파일이 없습니다.", 3000);
				}

				for (int i = 0; i < usrCnt; i++) {
					JSONObject obj = bodyResultObj.get(i).isObject();
					
					this.addTableRow(obj);
				}
				
			
			}
			table.loading(false);
		});
	}

	private ContentTableRow addTableRow(JSONObject obj) {
		String bfiId = obj.containsKey("BFI_ID") ? obj.get("BFI_ID").isString().stringValue() : "";
		String regId = obj.containsKey("REG_ID") ? obj.get("REG_ID").isString().stringValue() : "";
		String regDate = obj.containsKey("REG_DATE") ? obj.get("REG_DATE").isString().stringValue() : "";
		String filenm = obj.containsKey("FILE_NM") ? obj.get("FILE_NM").isString().stringValue() : "";
		String filesavenm = obj.containsKey("FILE_SAVE_NM") ? obj.get("FILE_SAVE_NM").isString().stringValue() : "";
		String useyn = obj.containsKey("USE_YN") ? obj.get("USE_YN").isString().stringValue() : "";
		ContentTableRow tableRow = table.addRow(Color.WHITE, new int[]{4,5}
			, index++
			, regDate
			, regId
			, filenm
			, useyn=="Y" ? "[설정]" : "[미설정]"
			, "[다운로드]"
		);
		tableRow.put("bfiId", bfiId);
		tableRow.put("regId", regId);
		tableRow.put("regDate", regDate);
		tableRow.put("filenm", filenm);
		tableRow.put("filesavenm", filesavenm);
		tableRow.put("useyn", useyn);
		tableRow.addClickHandler(this.setTableClickHandler());
		
		return tableRow;
	}
	
	private ClickHandler setTableClickHandler() {
		return e -> {
			ContentTableRow ctr = (ContentTableRow) e.getSource();
			if (ctr.getSelectedColumn() == 4) {
				MainCheck = ctr.get("bfiId").toString();
				
				for (int i = 0; i < table.getRowsList().size(); i++) {
					ContentTableRow tableRow = table.getRowsList().get(i);
					tableRow.getChildren().get(4).getElement().getChild(0).setNodeValue("[미설정]");
				}
							
				ctr.getChildren().get(4).getElement().getChild(0).setNodeValue("[설정]");
			}

			if (ctr.getSelectedColumn() == 5) {
				
				String downurl = "./call?cmd=BLACK_LIST_XLS_DOWNLOAD";
				
					
					StringBuffer  sb = new StringBuffer();
					sb.append(downurl);
					sb.append("&saveName=");
					sb.append(ctr.get("filesavenm").toString());
					sb.append("&fileName=");
					sb.append(ctr.get("filenm").toString());
					sb.append("&select_type=");
					Window.open(sb.toString(),"_self", "enabled");
					
				
			}
		};
	}
	
	private void SaveData() {
			
		JSONObject paramJson = new JSONObject();
		paramJson.put("cmd", new JSONString("SAVE_BLACK_LIST"));
		
		paramJson.put("fileName", new JSONString((fileName != null ? fileName : "")));
		paramJson.put("saveName", new JSONString((saveName != null ? saveName : "")));
		paramJson.put("mainCheck", new JSONString((MainCheck != null ? MainCheck : "")));
		
		VisitKoreaBusiness.post("call", paramJson.toString(), (param1, param2, param3) -> {
			JSONObject resultObj = JSONParser.parseStrict(JSON.stringify((JsObject) param1)).isObject();
			JSONObject headerObj = resultObj.get("header").isObject();
			String processResult = headerObj.get("process").isString().stringValue();
			if (processResult.equals("fail")) {
				MaterialToast.fireToast("오류가 발생했습니다. 관리자에게 문의해주세요", 3000);
			} else {
				MaterialToast.fireToast("저장에 성공하였습니다.", 3000);
				qryList();
				fileName = null;
				saveName = null;
				MainCheck = null;
			}
			table.loading(false);
		});
		
	}
}
