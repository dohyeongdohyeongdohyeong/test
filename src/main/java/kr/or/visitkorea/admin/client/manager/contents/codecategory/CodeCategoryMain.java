package kr.or.visitkorea.admin.client.manager.contents.codecategory;

import java.util.HashMap;

import com.google.gwt.dom.client.Style;
import com.google.gwt.dom.client.Style.FontWeight;
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
import kr.or.visitkorea.admin.client.manager.contents.recommand.RecommContentsList;
import kr.or.visitkorea.admin.client.manager.contents.recommand.RecommContentsTree;
import kr.or.visitkorea.admin.client.manager.widgets.AbstractContentPanel;
import kr.or.visitkorea.admin.client.manager.widgets.ContentTable;
import kr.or.visitkorea.admin.client.manager.widgets.ContentTable.TABLE_SELECT_TYPE;
import kr.or.visitkorea.admin.client.manager.widgets.ContentTableRow;
import kr.or.visitkorea.admin.client.widgets.window.MaterialExtentsWindow;

public class CodeCategoryMain extends AbstractContentPanel {

	
	private ContentTable CodeTable;
	private MaterialComboBox<Object> SearchBox;
	private MaterialTextBox codename;
	private MaterialExtentsWindow host;
	private CodeCategoryMain main = this;
	private MaterialLabel AllcountLabel;
	public CodeCategoryMain(MaterialExtentsWindow meWindow) {
		super(meWindow);
		this.host = meWindow;
	}
	
	@Override
	public void init() {
		CreateSearchMenu();
	}

	
	public void CreateSearchMenu() {
		
		MaterialPanel mrtop = new MaterialPanel();
		mrtop.setWidth("100%");	mrtop.setHeight("80px");
		mrtop.setPadding(10);
		this.add(mrtop);
		
		SearchBox = new MaterialComboBox<>();
		SearchBox.setLabel("검색조건");
		SearchBox.setLayoutPosition(Position.ABSOLUTE);
		SearchBox.setTop(5); SearchBox.setLeft(30); SearchBox.setWidth("200px");
		SearchBox.addItem("분류명", 1);
		SearchBox.addItem("가이드명", 2);
		SearchBox.setSelectedIndex(0);
		SearchBox.addValueChangeHandler(e->{
			if(SearchBox.getSelectedIndex() == 0) {
				codename.setLabel("분류명 입력");
			} else if(SearchBox.getSelectedIndex() == 1) {
				codename.setLabel("가이드명 입력");
			} 
		});
		mrtop.add(SearchBox);
		
		codename = new MaterialTextBox();
		codename.setLabel("제목입력");
		codename.setLayoutPosition(Position.ABSOLUTE);
		codename.setTop(5);	codename.setLeft(270); codename.setWidth("400px");
		codename.addKeyPressHandler(event -> {
			if (event.getNativeEvent().getKeyCode() == KeyCodes.KEY_ENTER) {
				qryList(true);
			}
		});
		mrtop.add(codename);
		CreateTable();
	}
	
	
	public void CreateTable(){
		
		CodeTable = new ContentTable(TABLE_SELECT_TYPE.SELECT_SINGLE);
		CodeTable.setLayoutPosition(Position.ABSOLUTE);
		CodeTable.setTop(40);
		CodeTable.setWidth("98.5%"); 
		CodeTable.setHeight(595); 
		CodeTable.setMargin(10);
		CodeTable.appendTitle("번호", 60, TextAlign.CENTER);
		CodeTable.appendTitle("분류 명", 300, TextAlign.CENTER);
		CodeTable.appendTitle("가이드 명", 400, TextAlign.CENTER);
		CodeTable.appendTitle("사용여부", 100, TextAlign.CENTER);
		CodeTable.appendTitle("분류별 컨텐츠 수 (전체/표출) ", 100, TextAlign.CENTER);
		CodeTable.appendTitle("", 100, TextAlign.CENTER);
		CodeTable.appendTitle("1순위 (전체/표출)", 100, TextAlign.CENTER);
		CodeTable.appendTitle("", 100, TextAlign.CENTER);
		CodeTable.appendTitle("2순위 (전체/표출)", 100, TextAlign.CENTER);
		CodeTable.appendTitle("", 100, TextAlign.CENTER);
		this.add(CodeTable);
		
		
		CodeTable.getHeader().getChildrenList().get(4).setWidth("200px");
		CodeTable.getHeader().getChildrenList().get(6).setWidth("200px");
		CodeTable.getHeader().getChildrenList().get(8).setWidth("200px");
		
		MaterialIcon searchIcon = new MaterialIcon(IconType.SEARCH);
		searchIcon.setTextAlign(TextAlign.CENTER);
		searchIcon.addClickHandler(event->{
			qryList(true);
		});
		
		MaterialIcon addIcon = new MaterialIcon(IconType.ADD);
		
		addIcon.addClickHandler(event->{
			
			HashMap<String, Object> params = new HashMap<>();
			params.put("mode", "ADD");
			params.put("host", main);
			this.host.openDialog(CodeCategoryApplication.CODE_EDIT_DIALOG, params, 500);
			
		});
		CodeTable.getTopMenu().addIcon(searchIcon, "검색", com.google.gwt.dom.client.Style.Float.RIGHT, "1.8em", "26px", 24, false);
		CodeTable.getTopMenu().addIcon(addIcon, "추가", Style.Float.RIGHT, "1.8em", "34px", 25, false );
		
		AllcountLabel = new MaterialLabel("");
		AllcountLabel.setFontWeight(FontWeight.BOLDER);
		AllcountLabel.setTextColor(Color.BLACK);
		CodeTable.getButtomMenu().addLabel(AllcountLabel, com.google.gwt.dom.client.Style.Float.RIGHT);
		AllcountLabel.setMarginRight(20);
		
		MaterialButton EXCELBUTTON = new MaterialButton("엑셀 다운로드");
		
		EXCELBUTTON.addClickHandler(event->{
			
			String keyWord = codename.getText().replaceAll("\\\\", "");
			
			String downurl = "./call?cmd=FILE_DOWNLOAD_XLS";

			downurl += "&select_type=codeCategoryXls";
			if (SearchBox.getSelectedIndex() >= 0) {
				downurl += "&SearchType="+SearchBox.getSelectedIndex()+1;
			}
				downurl += "&keyWord="+keyWord;
				
			Window.open(downurl,"_self", "enabled");
			
		});
		CodeTable.getButtomMenu().addButton(EXCELBUTTON, com.google.gwt.dom.client.Style.Float.LEFT);
		
		
		qryList(true);
	}
	
	public void qryList (boolean bstart) {
		CodeTable.clearRows();
		
		String keyWord = codename.getText().replaceAll("\\\\", "");
		
		JSONObject parameterJSON = new JSONObject();
		parameterJSON.put("cmd", new JSONString("SELECT_CODE_LIST"));
		parameterJSON.put("SearchType", new JSONNumber(SearchBox.getSelectedIndex()+1));
		parameterJSON.put("keyword", new JSONString(keyWord));
		
		CodeTable.loading(true);
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
					JSONArray bodyResultCntAry = (JSONArray) bodyObj.get("resultAllCnt");
					JSONObject ResultCnt = bodyResultCntAry.get(0).isObject();
					
					int ALLCNT = (int)ResultCnt.get("ALLCNT").isNumber().doubleValue();
					int OPENCNT = (int)ResultCnt.get("OPENCNT").isNumber().doubleValue();
					int NOTINALL = (int)ResultCnt.get("NOTINALL").isNumber().doubleValue();
					int NULLALL = (int)ResultCnt.get("NULLALL").isNumber().doubleValue();
					
					String Count = "기사 전체 : "+ ALLCNT + "건 / 표출 : " +OPENCNT + "건 / 미분류 : " + (NOTINALL+NULLALL)+"건";
					AllcountLabel.setText(Count);
					
					int usrCnt = bodyResultObj.size();
					if (usrCnt == 0) {
						MaterialToast.fireToast("검색된 결과가 없습니다.", 3000);
					}
					
					for(int i= 0;i< usrCnt;i++) {
						JSONObject recObj = bodyResultObj.get(i).isObject();
						
						String Status = "";
						if(recObj.get("STATUS").isNumber().doubleValue() == 1) Status ="Y";
						else Status ="N";
						
						int CATEONE_ALL = 0;
						int CATEONE_OPEN = 0;
						int CATETWO_ALL = 0;
						int CATETWO_OPEN = 0;
						
						
						if(recObj.containsKey("CATE_ALL"))
							CATEONE_ALL = (int)recObj.get("CATE_ALL").isNumber().doubleValue();
						if(recObj.containsKey("CATE_OPEN"))
							CATEONE_OPEN = (int)recObj.get("CATE_OPEN").isNumber().doubleValue();
						if(recObj.containsKey("CATETWO_ALL"))
							CATETWO_ALL = (int)recObj.get("CATETWO_ALL").isNumber().doubleValue();
						if(recObj.containsKey("CATETWO_OPEN"))
							CATETWO_OPEN = (int)recObj.get("CATETWO_OPEN").isNumber().doubleValue();
						
						int CATE_ALL = CATEONE_ALL+CATETWO_ALL;
						int CATE_OPEN = CATEONE_OPEN+CATETWO_OPEN;
						
						
						ContentTableRow tableRow = CodeTable.addRow(
								Color.WHITE, new int[]{1,2,6,7,8,9},
								i,
								getString(recObj, "VALUE"),
								getString(recObj, "FILE_DESCRIPTION"),
								Status,
								CATE_ALL,
								CATE_OPEN,
								CATEONE_ALL,
								CATEONE_OPEN,
								CATETWO_ALL,
								CATETWO_OPEN);
						
						
						tableRow.addClickHandler(event->{
							ContentTableRow ctr = (ContentTableRow)event.getSource();
							int ClickColumn = ctr.getSelectedColumn();
							if(ClickColumn == 1 || ClickColumn == 2 ) {
								
								String Status2 = "";
								if(recObj.get("STATUS").isNumber().doubleValue() == 1) Status2 ="사용";
								else Status2 ="미사용(비활성화)";
								
								HashMap<String, Object> params = new HashMap<>();
								params.put("mode", "UPDATE");
								params.put("codId", getString(recObj, "COD_ID"));
								params.put("value", getString(recObj, "VALUE"));
								params.put("desc", getString(recObj, "FILE_DESCRIPTION"));
								params.put("status", Status2);
								params.put("host", main);
								host.openDialog(CodeCategoryApplication.CODE_EDIT_DIALOG, params, 500);
								
							} else if(ClickColumn == 6 
									|| ClickColumn == 7
									|| ClickColumn == 8
									|| ClickColumn == 9) {
								HashMap<String, Object> params = new HashMap<>();
								params.put("Code", getString(recObj, "BIG_CATEGORY"));
								params.put("value", getString(recObj, "VALUE"));
								params.put("host", main);
								if(ClickColumn == 6) {
									params.put("isRank", 0);
									params.put("isOpen", 0);
								} else if(ClickColumn == 7) {
									params.put("isRank", 0);
									params.put("isOpen", 1);
								} else if(ClickColumn == 8) {
									params.put("isRank", 1);
									params.put("isOpen", 0);
								} else if(ClickColumn == 9) {
									params.put("isRank", 1);
									params.put("isOpen", 1);
								}
							host.openDialog(CodeCategoryApplication.CODE_RECOMM_DIALOG, params, 1500);
							} 
						});
					}
					
				}else if (processResult.equals("fail")) {
					getMaterialExtentsWindow().alert("검색이 실패했습니다. 관리자에게 문의하세요.", 500);
				}
				CodeTable.loading(false);
			}
		});
		
	}
}
