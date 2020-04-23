package kr.or.visitkorea.admin.client.manager.member;


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
import kr.or.visitkorea.admin.client.application.UI;
import kr.or.visitkorea.admin.client.manager.VisitKoreaBusiness;
import kr.or.visitkorea.admin.client.manager.widgets.ContentTable;
import kr.or.visitkorea.admin.client.manager.widgets.ContentTableRow;

public class AnalysisPanel extends BasePanel {

	private ContentTable table;
	private MaterialLabel countLabel;
	private int offset;

//	private MaterialTextBox sdate, edate;
	
	private MaterialComboBox<Object> cbidname, cbType;
	private MaterialTextBox edidname;
	
	private int totcnt;
	
	private int index;
	
	private String sort = "f";
	private MaterialButton PartnersExcelButton;
	public AnalysisPanel(MemberMain host) {
		super(host);
		add(createSearchForm());
		add(createBody());
	}
	private Widget createBody() {
		table = new ContentTable(ContentTable.TABLE_SELECT_TYPE.SELECT_SINGLE);
		table.setLayoutPosition(Position.ABSOLUTE);
		table.setTop(80); table.setWidth("97.5%");table.setHeight(555);table.setMargin(10);
		table.appendTitle("번호", 60, TextAlign.CENTER);
		table.appendTitle("아이디", 250, TextAlign.CENTER);
		table.appendTitle("로그인 유형", 130, TextAlign.CENTER);
		table.appendTitle("즐겨찾기", 100, TextAlign.CENTER);
		table.appendTitle("댓글", 100, TextAlign.CENTER);
		table.appendTitle("공유하기", 100, TextAlign.CENTER);
		table.appendTitle("좋아요", 100, TextAlign.CENTER);
		table.appendTitle("Q&A", 100, TextAlign.CENTER);
		table.appendTitle("관광정보지킴이", 100, TextAlign.CENTER);
		table.appendTitle("코스생성", 100, TextAlign.CENTER);
		table.appendTitle("코스 공유", 100, TextAlign.CENTER);
		table.appendTitle("발도장", 100, TextAlign.CENTER);
		table.appendTitle("쿠폰", 100, TextAlign.CENTER);
		
		table.getColumn(3).addClickHandler(e->{
			initTable2Header();
			((MaterialLabel)e.getSource()).setTextColor(Color.WHITE);
			sort = "f"; qryList(true);	});
		table.getColumn(4).addClickHandler(e->{ 
			initTable2Header();
			((MaterialLabel)e.getSource()).setTextColor(Color.WHITE);
			sort = "cc"; qryList(true); });
		table.getColumn(5).addClickHandler(e->{ 
			initTable2Header();
			((MaterialLabel)e.getSource()).setTextColor(Color.WHITE);
			sort = "sc"; qryList(true); });
		table.getColumn(6).addClickHandler(e->{ 
			initTable2Header();
			((MaterialLabel)e.getSource()).setTextColor(Color.WHITE);
			sort = "l"; qryList(true); });
		table.getColumn(7).addClickHandler(e->{ 
			initTable2Header();
			((MaterialLabel)e.getSource()).setTextColor(Color.WHITE);
			sort = "q"; qryList(true); });
		table.getColumn(8).addClickHandler(e->{ 
			initTable2Header();
			((MaterialLabel)e.getSource()).setTextColor(Color.WHITE);
			sort = "zi"; qryList(true); });
		table.getColumn(12).addClickHandler(e->{ 
			initTable2Header();
			((MaterialLabel)e.getSource()).setTextColor(Color.WHITE);
			sort = "cp"; qryList(true); });
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
		
		PartnersExcelButton = new MaterialButton("엑셀 다운로드");
		PartnersExcelButton.setVisible(false);
		PartnersExcelButton.addClickHandler(event->{
			String downurl = "./call?cmd=FILE_DOWNLOAD_XLS";
			downurl += "&select_type=PartnersMemberActivity";
			Window.open(downurl,"_self", "enabled");
		});
		table.getButtomMenu().addButton(PartnersExcelButton, com.google.gwt.dom.client.Style.Float.LEFT);
		
		return table;
	}
	private Widget createSearchForm() {
		MaterialPanel row = new MaterialPanel();
		row.setWidth("100%");
		row.setHeight("80px");
		row.setPadding(10);
		
		cbType = new MaterialComboBox<>();
		cbType.setLabel("사용자 유형");
		cbType.setLayoutPosition(Position.ABSOLUTE);
		cbType.setTop(45); cbType.setLeft(15); cbType.setWidth("150px");
		//SNS 타입 : 000 :: FACEBOOK,		001 :: TWITTER,		002 :: INSTAGRAM,		003 :: DAUM,		004 :: NAVER,		005 :: KAKAOTALK,		006 :: GOOGLE
		cbType.addItem("전체", -1);
		cbType.addItem("Partners", 10);
		cbType.addItem("FACEBOOK", 0);
		cbType.addItem("TWITTER", 1);
		cbType.addItem("INSTAGRAM", 2);
		cbType.addItem("DAUM", 3);
		cbType.addItem("NAVER", 4);
		cbType.addItem("KAKAOTALK", 5);
		cbType.addItem("GOOGLE", 6);
		cbType.setSelectedIndex(0);
		row.add(cbType);
		cbType.addValueChangeHandler(e->{
			table.removeTitleAll();
			if(cbType.getSelectedIndex() == 1) {
				PartnersExcelButton.setVisible(true);
				table.appendTitle("번호", 100, TextAlign.CENTER);
				table.appendTitle("아이디", 200, TextAlign.CENTER);
				table.appendTitle("로그인 유형", 200, TextAlign.CENTER);
				table.appendTitle("즐겨찾기", 110, TextAlign.CENTER);
				table.appendTitle("댓글", 110, TextAlign.CENTER);
				table.appendTitle("공유하기", 110, TextAlign.CENTER);
				table.appendTitle("좋아요", 110, TextAlign.CENTER);
				table.appendTitle("Q&A", 110, TextAlign.CENTER);
				table.appendTitle("관광정보지킴이", 110, TextAlign.CENTER);
				table.appendTitle("콘텐츠제휴", 110, TextAlign.CENTER);
				table.appendTitle("체널제휴", 110, TextAlign.CENTER);
			} else {
				PartnersExcelButton.setVisible(false);
				table.appendTitle("번호", 60, TextAlign.CENTER);
				table.appendTitle("아이디", 250, TextAlign.CENTER);
				table.appendTitle("로그인 유형", 130, TextAlign.CENTER);
				table.appendTitle("즐겨찾기", 100, TextAlign.CENTER);
				table.appendTitle("댓글", 100, TextAlign.CENTER);
				table.appendTitle("공유하기", 100, TextAlign.CENTER);
				table.appendTitle("좋아요", 100, TextAlign.CENTER);
				table.appendTitle("Q&A", 100, TextAlign.CENTER);
				table.appendTitle("관광정보지킴이", 100, TextAlign.CENTER);
				table.appendTitle("코스생성", 100, TextAlign.CENTER);
				table.appendTitle("코스 공유", 100, TextAlign.CENTER);
				table.appendTitle("발도장", 100, TextAlign.CENTER);
				table.appendTitle("쿠폰", 100, TextAlign.CENTER);
			}
			qryList(true);
		});
		
		cbidname = new MaterialComboBox<>();
		cbidname.setLayoutPosition(Position.ABSOLUTE);
		cbidname.setLabel("검색조건");
		cbidname.setTop(45); cbidname.setLeft(190); cbidname.setWidth("150px");
		cbidname.addItem("아이디", "0");
		cbidname.addItem("이름", "1");
		row.add(cbidname);
		edidname = new MaterialTextBox();
		edidname.setLayoutPosition(Position.ABSOLUTE);
		edidname.setLabel("검색어입력");
		edidname.setTop(45); edidname.setLeft(350); edidname.setWidth("500px");
		row.add(edidname);
		edidname.addKeyPressHandler(event -> {
			if (event.getNativeEvent().getKeyCode() == KeyCodes.KEY_ENTER) {
				qryList(true);
			}
		});
		
//		sdate = new MaterialTextBox();
//		sdate.setType(InputType.DATE);
//		sdate.setLayoutPosition(Position.ABSOLUTE);
//		sdate.setLabel("기간"); sdate.setText("-");
//		sdate.setTop(45); sdate.setRight(240); sdate.setWidth("150px");
//		row.add(sdate);
//		edate = new MaterialTextBox();
//		edate.setType(InputType.DATE);
//		edate.setLayoutPosition(Position.ABSOLUTE);
//		edate.setTop(45); edate.setRight(80); edate.setWidth("150px");
//		row.add(edate);
		return row;
	}
	public void qryList(boolean bstart) {
		if(bstart) {
			offset = 0; 
			index = 1;
			table.clearRows();
		} else offset += 20;
		
		JSONObject parameterJSON = new JSONObject();
		parameterJSON.put("cmd", new JSONString("SELECT_SNS_ANALYSIS"));
		int snstype = (int)cbType.getValues().get(cbType.getSelectedIndex());
		if(snstype > -1)
			parameterJSON.put("snstype", new JSONString(snstype+""));
		
//		String strsdate = sdate2.getText().toString();
//		if(!"".equals(strsdate)) {
//			parameterJSON.put("sdate", new JSONString(strsdate));
//		}
//		String stredate = edate2.getText().toString();
//		if(!"".equals(stredate)) {
//			parameterJSON.put("edate", new JSONString(stredate+" 23:59:59"));
//		}

		String idname = edidname.getText();
		if(!"".equals(idname)) {
			if(cbidname.getValues().get(cbidname.getSelectedIndex()).toString().equals("0"))
				parameterJSON.put("id", new JSONString(idname));
			else parameterJSON.put("name", new JSONString(idname));
		}
		if(!"".equals(sort)) {
			parameterJSON.put("sort", new JSONString(sort));
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
						String id = obj.get("SNS_IDENTIFY").isString().stringValue();
						if(obj.get("SNS_TYPE").isNumber().doubleValue() != 10) {
							id = obj.get("SNS_IDENTIFY").isString().stringValue()+"("+obj.get("SNS_USR_NAME").isString().stringValue()+")";
						}
						String snstype = UI.getType((int)obj.get("SNS_TYPE").isNumber().doubleValue());
						
						if(cbType.getSelectedIndex() == 1) {
							ContentTableRow tableRow = table.addRow(Color.WHITE 
								,index++
								,id
								,snstype==null?obj.get("SNS_USR_NAME").isString().stringValue():snstype
								,obj.get("f")!=null?obj.get("f").isNumber().doubleValue()+"":"0"
								,obj.get("cc")!=null?obj.get("cc").isNumber().doubleValue()+"":"0"
								,obj.get("sc")!=null?obj.get("sc").isNumber().doubleValue()+"":"0"
								,obj.get("l")!=null?obj.get("l").isNumber().doubleValue()+"":"0"
								,obj.get("q")!=null?obj.get("q").isNumber().doubleValue()+"":"0"
								,obj.get("zi")!=null?obj.get("zi").isNumber().doubleValue()+"":"0"
								,obj.get("pc")!=null?obj.get("pc").isNumber().doubleValue()+"":"0"
								,obj.get("ph")!=null?obj.get("ph").isNumber().doubleValue()+"":"0"
							);
							tableRow.addClickHandler(event->{});
						} else {
							
							Double f = 0.0;
							
							if(obj.get("f")!=null) {
								f = obj.get("f").isNumber().doubleValue();
							}
							if(obj.get("ms")!=null) {
								f += obj.get("ms").isNumber().doubleValue();
							}
							
							ContentTableRow tableRow = table.addRow(
								Color.WHITE 
								,index++
								,id
								,snstype==null?obj.get("SNS_USR_NAME").isString().stringValue():snstype
								,f
								,obj.get("cc")!=null?obj.get("cc").isNumber().doubleValue()+"":"0"
								,obj.get("sc")!=null?obj.get("sc").isNumber().doubleValue()+"":"0"
								,obj.get("l")!=null?obj.get("l").isNumber().doubleValue()+"":"0"
								,obj.get("q")!=null?obj.get("q").isNumber().doubleValue()+"":"0"
								,obj.get("zi")!=null?obj.get("zi").isNumber().doubleValue()+"":"0"
								,obj.get("ssc") != null ? obj.get("ssc").isNumber().doubleValue()+"":"0"
								,obj.get("cs") != null ? obj.get("cs").isNumber().doubleValue()+"":"0"
								,obj.get("sh") != null ? obj.get("sh").isNumber().doubleValue()+"":"0"
								,obj.get("cp") != null ? obj.get("cp").isNumber().doubleValue()+"":"0"
							);
							tableRow.addClickHandler(event->{});
						}
					}
				} else if (processResult.equals("fail")) {
					host.getMaterialExtentsWindow().alert("검색이 실패했습니다. 관리자에게 문의하세요.", 500);
					countLabel.setText("실패?");
				}
				table.loading(false);
			}
		});
	}
	
	private void initTable2Header() {
		table.getColumn(3).setTextColor(Color.BLACK);
		table.getColumn(4).setTextColor(Color.BLACK);
		table.getColumn(5).setTextColor(Color.BLACK);
		table.getColumn(6).setTextColor(Color.BLACK);
		table.getColumn(7).setTextColor(Color.BLACK);
		table.getColumn(8).setTextColor(Color.BLACK);
	//	table.getColumn(9).setTextColor(Color.BLACK);
		table.getColumn(12).setTextColor(Color.BLACK);
	}
}