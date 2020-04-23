package kr.or.visitkorea.admin.client.manager.contents.recommand.dialogs;

import com.google.gwt.dom.client.Style.Float;
import com.google.gwt.dom.client.Style.FontWeight;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONNumber;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONString;

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
import kr.or.visitkorea.admin.client.manager.otherDepartment.widgets.TagListLabelCell;
import kr.or.visitkorea.admin.client.manager.otherDepartment.widgets.TagListRow;
import kr.or.visitkorea.admin.client.manager.widgets.ContentDetailInputTravelInfo;
import kr.or.visitkorea.admin.client.manager.widgets.ContentTable;
import kr.or.visitkorea.admin.client.manager.widgets.ContentTable.TABLE_SELECT_TYPE;
import kr.or.visitkorea.admin.client.manager.widgets.ContentTableRow;
import kr.or.visitkorea.admin.client.manager.widgets.IDUtil;
import kr.or.visitkorea.admin.client.widgets.dialog.DialogContent;
import kr.or.visitkorea.admin.client.widgets.window.MaterialExtentsWindow;

public class TravelInfoContentsDialog extends DialogContent {
	private ContentDetailInputTravelInfo host;
	private MaterialPanel contentPanel;
	private MaterialLabel modeLabel;
	private MaterialTextBox keyword;
	private MaterialTextBox address;
	private MaterialTextBox tel;
	private MaterialTextBox homepage;
	private MaterialButton searchBtn;
	private ContentTable table;
	private JSONObject selectedObj;
	private MaterialLabel countLabel;
	
	private String dialogMode;
	private String triId = null;
	private String aciId = null;
	private String cotId = null;
	private int offset;
	private int index;
	private int totcnt;
	private MaterialTextBox Etc;
	
	public TravelInfoContentsDialog(MaterialExtentsWindow tgrWindow) {
		super(tgrWindow);
	}

	public void buildHeader() {
		MaterialLabel title = new MaterialLabel();
		title.setText("* 여행정보 컨텐츠 등록");
		title.setFontSize("1.4em");
		title.setTextColor(Color.BLUE);
		title.setPadding(18);
		title.setFontWeight(FontWeight.BOLD);
		this.add(title);
	}
	
	@Override
	public void init() {
		buildHeader();
		addDefaultButtons();
		contentPanel = new MaterialPanel();
		contentPanel.setWidth("100%");
		contentPanel.setHeight("500px");
		this.add(contentPanel);
		
		MaterialRow row1 = addRow(); row1.setMarginBottom(25);
		addLabel(row1, "s3", "구분", Color.GREY_LIGHTEN_2);
		modeLabel = addLabel(row1, "s9", "", Color.GREY_LIGHTEN_3);
		modeLabel.setFontWeight(FontWeight.BOLD);
		modeLabel.setTextAlign(TextAlign.LEFT);
		modeLabel.setFontSize("20px");
		
		MaterialRow row2 = addRow();
		addLabel(row2, "s3", "* 컨텐츠 이름", Color.GREY_LIGHTEN_2);
		keyword = addInputBox(row2, "s6", "컨텐츠명을 입력해주세요.");
		keyword.setMaxLength(100);
		keyword.addKeyUpHandler(event -> {
			if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
				qryList(true);
			}
		});
		searchBtn = addBtn(row2, "s2", "검색");
		searchBtn.addClickHandler(event -> {
			qryList(true);
		});
		
		MaterialRow row4 = addRow(); row4.setPaddingLeft(10); row4.setPaddingRight(10);
		table = new ContentTable(TABLE_SELECT_TYPE.SELECT_SINGLE);
		table.appendTitle("분류", 200, TextAlign.CENTER);
		table.appendTitle("컨텐츠명", 450, TextAlign.CENTER);
		table.setHeight(140);
		table.getRowContainer().setHeight("120px");
		table.getRowContainer().setTop(0);
		table.getRowContainer().setBorderTop("1px solid #aaaaaa");
		table.setTop(-10);
		table.setTopMenuDisplay(Display.NONE);
		table.setHeaderVisiable(false);
		row4.add(table);

		MaterialIcon nextRowsIcon = new MaterialIcon(IconType.ARROW_DOWNWARD);
		nextRowsIcon.setTextAlign(TextAlign.CENTER);
		nextRowsIcon.addClickHandler(event->{
			if ((offset+20) >= totcnt) {
				MaterialToast.fireToast("더이상의 결과 값이 없습니다.", 3000);
			} else {
				qryList(false);
			}
		});
		
		countLabel = new MaterialLabel("0 건");
		countLabel.setFontWeight(FontWeight.BOLDER);
		countLabel.setTextColor(Color.BLACK);
		
		table.getButtomMenu().addIcon(nextRowsIcon, "다음 20개", Float.RIGHT);
		table.getButtomMenu().addLabel(countLabel, Float.RIGHT);
		
		MaterialRow row5 = addRow();
		addLabel(row5, "s3", "* 주소", Color.GREY_LIGHTEN_2);
		address = addInputBox(row5, "s9", "주소를 입력해주세요.");
		address.setMaxLength(100);
		MaterialRow row6 = addRow();
		addLabel(row6, "s3", "전화번호", Color.GREY_LIGHTEN_2);
		tel = addInputBox(row6, "s9", "전화번호를 입력해주세요.");
		tel.setMaxLength(100);
		
		MaterialRow row7 = addRow();
		addLabel(row7, "s3", "홈페이지", Color.GREY_LIGHTEN_2);
		homepage = addInputBox(row7, "s9", "홈페이지를 입력해주세요.");
		homepage.setMaxLength(100);
		
		MaterialRow row8 = addRow();
		addLabel(row8, "s3", "기타", Color.GREY_LIGHTEN_2);
		Etc = addInputBox(row8, "s9", "기타 내용을 입력해주세요.");
		Etc.setMaxLength(200);
		
		MaterialButton submitBtn = new MaterialButton("저장");
		submitBtn.addClickHandler(event -> {
			submitProcess();
		});
		
		this.addButton(submitBtn);
	}
	
	private String isValidate() {
		if (this.keyword.getValue().equals("")) {
			return "제목을 입력해주세요.";
		} else if (this.address.getValue().equals("")) {
			return "주소를 입력해주세요.";
		}
		return null;
	}
	
	public void submitProcess() {
		String msg = isValidate();
		if (msg != null) {
			alert("알림", 400, 300, new String[] { msg });
			return;
		}
		JSONObject obj = new JSONObject();
		obj.put("TRI_ID", new JSONString(this.triId));
		obj.put("ACI_ID", new JSONString(this.aciId));
		obj.put("TITLE", new JSONString(this.keyword.getValue()));
		obj.put("ADDRESS", new JSONString(this.address.getValue()));
		obj.put("TEL", new JSONString(this.tel.getValue()));
		obj.put("HOMEPAGE", new JSONString(this.homepage.getValue()));
		obj.put("ETC", new JSONString(this.Etc.getValue()));
		
		if (this.dialogMode.contains("SELF")) {
			obj.put("STATUS", new JSONNumber(1));
		} else if (this.dialogMode.contains("CONTENTS")) {
			obj.put("COT_ID", new JSONString(this.cotId));
			obj.put("STATUS", new JSONNumber(1));
		}
		
		if (this.dialogMode.contains("MODIFY")) {
			if (this.triId != null)
				obj.put("TRI_ID", new JSONString(this.triId));
			if (this.cotId != null)
				obj.put("COT_ID", new JSONString(this.cotId));
			
			TagListRow tagRow = (TagListRow) this.host.getSearchBody().getSelectedRow();
			((TagListLabelCell) tagRow.getCell(1)).setText(this.keyword.getValue());
			((TagListLabelCell) tagRow.getCell(2)).setText(this.address.getValue());
			((TagListLabelCell) tagRow.getCell(3)).setText(this.tel.getValue());
			((TagListLabelCell) tagRow.getCell(4)).setText(this.homepage.getValue());
			tagRow.put("TRAVEL_INFO", obj);
			
		} else {
			this.host.appendTableRow(obj, host.getSearchBody().getRows().size());
		}
		this.getMaterialExtentsWindow().closeDialog();
	}
	
	@Override
	protected void onLoad() {
		this.table.clearRows();
		this.dialogMode = this.getParameters().get("mode").toString();
		this.aciId = this.getParameters().get("ACI_ID").toString();
		this.triId = IDUtil.uuid();
		this.host = (ContentDetailInputTravelInfo) this.getParameters().get("host");
		
		this.keyword.setValue("");
		this.address.setValue("");
		this.tel.setValue("");
		this.homepage.setValue("");
		this.Etc.setValue("");
		
		if (this.dialogMode.contains("SELF")) {
			this.modeLabel.setText("직접등록");
			this.searchBtn.setDisplay(Display.NONE);
			this.table.setDisplay(Display.NONE);
		} else if (this.dialogMode.contains("CONTENTS")) {
			this.modeLabel.setText("컨텐츠");
			this.searchBtn.setDisplay(Display.BLOCK);
			this.table.setDisplay(Display.BLOCK);
		}
		
		if (this.dialogMode.contains("MODIFY")) {
			this.selectedObj = (JSONObject) this.getParameters().get("OBJ");
			if (this.selectedObj.containsKey("TRI_ID"))
				this.triId = this.selectedObj.get("TRI_ID").isString().stringValue();
			if (this.selectedObj.containsKey("COT_ID"))
				this.cotId = this.selectedObj.get("COT_ID").isString().stringValue();
			if (this.selectedObj.containsKey("TITLE"))
				this.keyword.setValue(this.selectedObj.get("TITLE").isString().stringValue());
			if (this.selectedObj.containsKey("ADDRESS"))
				this.address.setValue(this.selectedObj.get("ADDRESS").isString().stringValue());
			if (this.selectedObj.containsKey("TEL"))
				this.tel.setValue(this.selectedObj.get("TEL").isString().stringValue());
			if (this.selectedObj.containsKey("HOMEPAGE"))
				this.homepage.setValue(this.selectedObj.get("HOMEPAGE").isString().stringValue());
			if (this.selectedObj.containsKey("ETC"))
				this.Etc.setValue(this.selectedObj.get("ETC").isString().stringValue());
		}
	}
	
	public void qryList(boolean bstart) {
		if (bstart) {
			offset = 0; 
			index = 1;
			table.clearRows();
		} else offset += 20;
		
		String keyWord = this.keyword.getText().replaceAll("\\\\", "");
		
		JSONObject parameterJSON = new JSONObject();
		parameterJSON.put("cmd", new JSONString("DATABASE_LIST"));
		parameterJSON.put("keyword", new JSONString(keyWord));
		parameterJSON.put("mode", new JSONNumber(0));
		parameterJSON.put("offset", new JSONString(offset+""));
		VisitKoreaBusiness.post("call", parameterJSON.toString(), (param1, param2, param3) -> {
			JSONObject resultObj = JSONParser.parseStrict(JSON.stringify((JsObject) param1)).isObject();
			String process = resultObj.get("header").isObject().get("process").isString().stringValue();
			
			if (process.equals("success")) {
				JSONObject bodyObj = resultObj.get("body").isObject();
				JSONArray bodyResult = bodyObj.get("result").isArray();
				JSONObject bodyResultCnt = bodyObj.get("resultCnt").isObject();

				countLabel.setText(bodyResultCnt.get("CNT")+" 건");
				totcnt = Integer.parseInt(bodyResultCnt.get("CNT").toString());
				
				int usrCnt = bodyResult.size();
				if (usrCnt == 0) {
					MaterialToast.fireToast("검색된 결과가 없습니다.", 3000);
				}
				
				for (int i = 0; i < bodyResult.size(); i++) {
					JSONObject obj = bodyResult.get(i).isObject();
					String ctype = Registry.getContentType((int) obj.get("CONTENT_TYPE").isNumber().doubleValue());
					String cotId = obj.containsKey("COT_ID") ? obj.get("COT_ID").isString().stringValue() : null;
					String title = obj.containsKey("TITLE") ? obj.get("TITLE").isString().stringValue() : null;
					String address = obj.containsKey("ADDR1") ? obj.get("ADDR1").isString().stringValue() : null;
					String tel = obj.containsKey("ADMIN_TEL") ? obj.get("ADMIN_TEL").isString().stringValue() : null;
					String homepage = obj.containsKey("HOMEPAGE") ? obj.get("HOMEPAGE").isString().stringValue() : null;
					
					
					if((int) obj.get("CONTENT_TYPE").isNumber().doubleValue() == 12) {
						tel = obj.containsKey("touristNo") ? obj.get("touristNo").isString().stringValue() : null;
					} else if((int) obj.get("CONTENT_TYPE").isNumber().doubleValue() == 14) {
						tel = obj.containsKey("culturalNo") ? obj.get("culturalNo").isString().stringValue() : null;
					} else if((int) obj.get("CONTENT_TYPE").isNumber().doubleValue() == 28) {
						tel = obj.containsKey("leportsNo") ? obj.get("leportsNo").isString().stringValue() : null;
					} else if((int) obj.get("CONTENT_TYPE").isNumber().doubleValue() == 38) {
						tel = obj.containsKey("shoppingNo") ? obj.get("shoppingNo").isString().stringValue() : null;
					} else if((int) obj.get("CONTENT_TYPE").isNumber().doubleValue() == 32) {
						tel = obj.containsKey("accommodationNo") ? obj.get("accommodationNo").isString().stringValue() : null;
					} else if((int) obj.get("CONTENT_TYPE").isNumber().doubleValue() == 39) {
						tel = obj.containsKey("eateryNo") ? obj.get("eateryNo").isString().stringValue() : null;
					} else {
						tel = obj.containsKey("ADMIN_TEL") ? obj.get("ADMIN_TEL").isString().stringValue() : null;
					}
					
					ContentTableRow tableRow = this.table.addRow(Color.WHITE
							, ctype
							, obj.containsKey("TITLE") ? obj.get("TITLE").isString().stringValue() : null
							);
					tableRow.put("TEL", tel);
					tableRow.put("COT_ID", cotId);
					tableRow.put("TITLE", title);
					tableRow.addClickHandler(event -> {
						String TEL = "";
						if((String) tableRow.get("TEL") != null)
						 TEL = (String) tableRow.get("TEL");
						if(TEL == "null") TEL = "";
						
						this.cotId = (String) tableRow.get("COT_ID");
						this.keyword.setValue(title);
						this.address.setValue((address != null && address =="null") ? null : address);
						this.tel.setValue(TEL);
						this.homepage.setValue((homepage != null && homepage.equals("null")) ? null : 
							homepage.split("href=\"")[1].split("\"")[0]);
					});
				}
			} else {
				
			}
		});
	}
	
	public MaterialButton addBtn(MaterialRow row, String grid, String text) {
		MaterialButton btn = new MaterialButton(text);
		btn.setTextAlign(TextAlign.CENTER);
		btn.setMarginTop(5);
		
		MaterialColumn col = addColumn(grid);
		col.add(btn);
		row.add(col);
		return btn;
	}
	
	public MaterialLabel addLabel(MaterialRow row, String grid, String text, Color color) {
		MaterialLabel lbl = new MaterialLabel(text);
		lbl.setTextAlign(TextAlign.CENTER);
		lbl.setBackgroundColor(color);
		lbl.setLineHeight(45);
		lbl.setHeight("45px");
		
		MaterialColumn col = addColumn(grid);
		col.add(lbl);
		row.add(col);
		return lbl;
	}
	
	public MaterialRow addRow() {
		MaterialRow row = new MaterialRow();
		row.setWidth("100%");
		row.setMarginBottom(0);
		
		contentPanel.add(row);
		return row;
	}
	
	public MaterialTextBox addInputBox(MaterialRow row, String grid, String placeholder) {
		MaterialTextBox tBox = new MaterialTextBox(placeholder);
		tBox.setMargin(0);
		tBox.setPadding(0);
		
		MaterialColumn col = addColumn(grid);
		col.add(tBox);
		row.add(col);
		return tBox;
	}
	
	public MaterialColumn addColumn(String grid) {
		MaterialColumn col = new MaterialColumn();
		col.setGrid(grid);
		
		return col;
	}
	
	@Override
	public int getHeight() {
		return 660;
	}

}
