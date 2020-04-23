package kr.or.visitkorea.admin.client.manager.partners.content;

import java.util.HashMap;
import java.util.Map;

import com.google.gwt.dom.client.Style.FontWeight;
import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONString;

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
import kr.or.visitkorea.admin.client.application.Console;
import kr.or.visitkorea.admin.client.application.Registry;
import kr.or.visitkorea.admin.client.manager.VisitKoreaBusiness;
import kr.or.visitkorea.admin.client.manager.contents.recommand.RecommApplication;
import kr.or.visitkorea.admin.client.manager.widgets.AbstractContentPanel;
import kr.or.visitkorea.admin.client.manager.widgets.ContentTable;
import kr.or.visitkorea.admin.client.manager.widgets.ContentTableRow;
import kr.or.visitkorea.admin.client.widgets.window.MaterialExtentsWindow;

public class PartnersContentMain extends AbstractContentPanel {


	private ContentTable table;
	private MaterialLabel countLabel;
	private int offset;

	private MaterialComboBox<Object> cbType, status;
	private MaterialTextBox sdate, edate; // , sdate2, edate2;
//	private MaterialButton btnAdd;
	
	private MaterialComboBox<Object> cbidname;
	private MaterialTextBox edidname;
	
	private int totcnt;
	
	private int index =0;
	
	PartnersContentApplication appview;
	public PartnersContentMain(MaterialExtentsWindow materialExtentsWindow,  PartnersContentApplication pa) {
		super(materialExtentsWindow);
		appview = pa;
	}
	private Map<String, Object> parametersMap = new HashMap<String, Object>();
	@Override
	public void init() {
		
		this.setLayoutPosition(Position.RELATIVE);
		
		
		MaterialPanel mrtop = new MaterialPanel();
		mrtop.setWidth("100%");	mrtop.setHeight("80px");
		mrtop.setPadding(10);
		cbType = new MaterialComboBox<>();
		cbType.setLabel("컨텐츠분류");
		cbType.setLayoutPosition(Position.ABSOLUTE);
		cbType.setTop(5); cbType.setLeft(10); cbType.setWidth("150px");
		cbType.addItem("전체", -1);
		cbType.addItem("추천", 0);
		cbType.addItem("DB", 1);
		cbType.setSelectedIndex(0);
		mrtop.add(cbType);
		cbType.addValueChangeHandler(e->{
			qryList(true);
		});
		
		status = new MaterialComboBox<>();
		status.setLabel("처리상태");
		status.setLayoutPosition(Position.ABSOLUTE);
		status.setTop(5); status.setLeft(190); status.setWidth("120px");
		status.addItem("전체", -1); //접수":status==1?"처리중":status==2?"승인완료":status==3?"반려":"사용내역등록"
		status.addItem("접수", 0);
		status.addItem("처리중", 1);
		status.addItem("승인완료", 2);
		status.addItem("반려", 3);
		status.addItem("사용내역등록", 4);
		status.setSelectedIndex(0);
		mrtop.add(status);
		status.addValueChangeHandler(e->{
			qryList(true);
		});
		
		sdate = new MaterialTextBox();
		sdate.setType(InputType.DATE);
		sdate.setLabel("기간"); sdate.setText("-");
		sdate.setLayoutPosition(Position.ABSOLUTE);
		sdate.setTop(5); sdate.setRight(240); sdate.setWidth("150px");
		mrtop.add(sdate);
		edate = new MaterialTextBox();
		edate.setType(InputType.DATE);
		edate.setLayoutPosition(Position.ABSOLUTE);
		edate.setTop(5); edate.setRight(80); edate.setWidth("150px");
		mrtop.add(edate);
		
		cbidname = new MaterialComboBox<>();
		cbidname.setLayoutPosition(Position.ABSOLUTE);
		cbidname.setLabel("검색조건");
		cbidname.setTop(5);	cbidname.setLeft(340); cbidname.setWidth("150px");
		cbidname.addItem("컨텐츠명", 0);
		cbidname.addItem("아이디", 1);
		cbidname.addItem("기관명", 2);
		cbidname.setSelectedIndex(0);
		mrtop.add(cbidname);
		
		edidname = new MaterialTextBox();
		edidname.setLabel("검색어입력");
		edidname.setLayoutPosition(Position.ABSOLUTE);
		edidname.setTop(5);	edidname.setLeft(500); edidname.setWidth("500px");
		mrtop.add(edidname);
		edidname.addKeyPressHandler(event -> {
			if (event.getNativeEvent().getKeyCode() == KeyCodes.KEY_ENTER) {
				qryList(true);
			}
		});
		this.add(mrtop);
		
		table = new ContentTable(ContentTable.TABLE_SELECT_TYPE.SELECT_SINGLE);
		table.setLayoutPosition(Position.ABSOLUTE);
		table.setTop(40);table.setWidth("98.5%"); table.setHeight(595); table.setMargin(10);
		table.appendTitle("번호", 80, TextAlign.CENTER);
		table.appendTitle("아이디", 120, TextAlign.CENTER);
		table.appendTitle("기관명", 200, TextAlign.CENTER);
		table.appendTitle("카테고리", 150, TextAlign.CENTER);
		table.appendTitle("컨텐츠명", 450, TextAlign.CENTER);
		table.appendTitle("등록일시", 150, TextAlign.CENTER);
		table.appendTitle("처리상태", 150, TextAlign.CENTER);
		table.appendTitle("처리일시", 150, TextAlign.CENTER);
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

		MaterialButton EXCELBUTTON = new MaterialButton("엑셀 다운로드");
		
		EXCELBUTTON.addClickHandler(event->{
			getMaterialExtentsWindow().openDialog(PartnersContentApplication.PARTNERS_CONTENT_EXCEL_DIALOG, parametersMap,900);
		});
		table.getButtomMenu().addButton(EXCELBUTTON, com.google.gwt.dom.client.Style.Float.LEFT);
	}

	public void qryList(boolean bstart) {
		if(bstart) {
			offset = 0; 
			index = 1;
			table.clearRows();
		} else offset += 20;
		
		JSONObject parameterJSON = new JSONObject();
		parameterJSON.put("cmd", new JSONString("SELECT_PARTNERS_CONTENT_LIST"));
		String strsdate = sdate.getText().toString();
		if(!"".equals(strsdate)) {
			parameterJSON.put("sdate", new JSONString(strsdate));
		}
		String stredate = edate.getText().toString();
		if(!"".equals(stredate)) {
			parameterJSON.put("edate", new JSONString(stredate+" 23:59:59"));
		}
		if((int)cbType.getValues().get(cbType.getSelectedIndex()) > -1) {
			parameterJSON.put("ctype", new JSONString(cbType.getValues().get(cbType.getSelectedIndex())+""));
		}
		
		if(status.getSelectedIndex() > 0) {
			parameterJSON.put("status", new JSONString(status.getValues().get(status.getSelectedIndex())+""));
		}
		
		String idname = edidname.getText();
		
		if(!"".equals(idname)) {
			if(cbidname.getSelectedIndex()== 0) {
				parameterJSON.put("title", new JSONString(idname));
			} else if(cbidname.getSelectedIndex()== 1) {
				parameterJSON.put("id", new JSONString(idname));
			} else 
				parameterJSON.put("name", new JSONString(idname));
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
						JSONObject obj = (JSONObject)bodyResultObj.get(i);
						int status = (int)obj.get("status").isNumber().doubleValue();
						ContentTableRow tableRow = null;
						tableRow = table.addRow(Color.WHITE, new int[]{4,6}
								,index++
								,obj.get("id")!=null?obj.get("id").isString().stringValue():""
								,obj.get("name")!=null?obj.get("name").isString().stringValue():""
								,Registry.getContentType((obj.get("ctype")!=null?(int)obj.get("ctype").isNumber().doubleValue():0))
								,obj.get("title")!=null?obj.get("title").isString().stringValue():""
								,obj.get("cdate")!=null?obj.get("cdate").isString().stringValue():""
								,status==0?"접수":status==1?"처리중":status==2?"승인완료":status==3?"반려":"사용내역등록"
								,obj.get("udate")!=null?obj.get("udate").isString().stringValue():""
								);
						
//						MaterialLabel materialLabel_00 = (MaterialLabel) tableRow.getColumnObject(9);
//						MaterialLabel materialLabel_01 = (MaterialLabel) tableRow.getColumnObject(10);
//						if (materialLabel_00.getText().equals("000")) {
//							materialLabel_01.setTextColor(Color.RED_LIGHTEN_2);
//						}else {
//							materialLabel_01.setTextColor(Color.WHITE);
//							
//						}
						
						tableRow.addClickHandler(event->{
							ContentTableRow ctr = (ContentTableRow)event.getSource();
							parametersMap.put("pacid", obj.get("pacid").isString().stringValue());
							parametersMap.put("id", obj.get("id").isString().stringValue());
							parametersMap.put("ctype", obj.get("ctype")!=null?(int)obj.get("ctype").isNumber().doubleValue():0);
							parametersMap.put("status", obj.get("status")!=null?(int)obj.get("status").isNumber().doubleValue():0);
							parametersMap.put("dnoffer", obj.get("dnoffer")!=null?(int)obj.get("dnoffer").isNumber().doubleValue():0);
							parametersMap.put("title", obj.get("title").isString().stringValue());
							parametersMap.put("name", obj.get("name").isString().stringValue());
							parametersMap.put("cdate", obj.get("cdate")!=null?obj.get("cdate").isString().stringValue():"");
							parametersMap.put("udate", obj.get("udate")!=null?obj.get("udate").isString().stringValue():"");
							parametersMap.put("usage", obj.get("usage")!=null?obj.get("usage").isString().stringValue():"");
							parametersMap.put("applytype", obj.get("applytype")!=null?obj.get("applytype").isString().stringValue():"");
							parametersMap.put("etcqna", obj.get("etcqna")!=null?obj.get("etcqna").isString().stringValue():"");
							parametersMap.put("cmt", obj.get("cmt")!=null?obj.get("cmt").isString().stringValue():"");
							parametersMap.put("url", obj.get("url")!=null?obj.get("url").isString().stringValue():"");
							parametersMap.put("cotid", obj.get("cotid")!=null?obj.get("cotid").isString().stringValue():"");
							if(ctr.getSelectedColumn() == 4) {//파트너정보 상세 보기
								getMaterialExtentsWindow().openDialog(PartnersContentApplication.PARTNERS_CONTENT_DETAIL, parametersMap,800,e2->{
									qryList(true);
								});
							} else if(ctr.getSelectedColumn() == 6 && status == 4) {
								parametersMap.put("fname", obj.get("fname")!=null?obj.get("fname").isString().stringValue():"");
								parametersMap.put("furl", obj.get("furl")!=null?obj.get("furl").isString().stringValue():"");
								parametersMap.put("resurl", obj.get("resurl")!=null?obj.get("resurl").isString().stringValue():"");
								parametersMap.put("dbody", obj.get("dbody")!=null?obj.get("dbody").isString().stringValue():"");
								getMaterialExtentsWindow().openDialog(PartnersContentApplication.PARTNERS_CONTENT_RESULT, parametersMap,800);
							}
						});
					}
					
				}else if (processResult.equals("fail")) {
					getMaterialExtentsWindow().alert("검색이 실패했습니다. 관리자에게 문의하세요.", 500);
					countLabel.setText("실패?");
				}
				table.loading(false);
			}
		});
	}
}
