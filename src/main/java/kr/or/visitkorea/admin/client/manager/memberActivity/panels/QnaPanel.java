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
import com.google.gwt.user.client.ui.Widget;

import gwt.material.design.addins.client.combobox.MaterialComboBox;
import gwt.material.design.client.constants.Color;
import gwt.material.design.client.constants.IconPosition;
import gwt.material.design.client.constants.IconType;
import gwt.material.design.client.constants.TextAlign;
import gwt.material.design.client.constants.WavesType;
import gwt.material.design.client.ui.MaterialIcon;
import gwt.material.design.client.ui.MaterialLabel;
import gwt.material.design.client.ui.MaterialLink;
import gwt.material.design.client.ui.MaterialPanel;
import gwt.material.design.client.ui.MaterialTextBox;
import gwt.material.design.client.ui.MaterialToast;
import gwt.material.design.jquery.client.api.Functions.Func3;
import gwt.material.design.jscore.client.api.JSON;
import gwt.material.design.jscore.client.api.JsObject;
import kr.or.visitkorea.admin.client.application.ApplicationView;
import kr.or.visitkorea.admin.client.application.Registry;
import kr.or.visitkorea.admin.client.application.WindowParamter;
import kr.or.visitkorea.admin.client.manager.VisitKoreaBusiness;
import kr.or.visitkorea.admin.client.manager.memberActivity.ActivityApplication;
import kr.or.visitkorea.admin.client.manager.memberActivity.MemberActivityMain;
import kr.or.visitkorea.admin.client.manager.widgets.ContentTable;
import kr.or.visitkorea.admin.client.manager.widgets.ContentTableRow;

public class QnaPanel extends BasePanel {

	private ContentTable table;
	private MaterialLabel countLabel;
	private MaterialComboBox<Object> cbidname;
	private MaterialTextBox edidname;
	private int totcnt;
	private int index;
	private int offset;
	
	public QnaPanel(MemberActivityMain host) {
		super(host);
	}
	
	@Override
	protected Widget createBody() {
		table = new ContentTable(ContentTable.TABLE_SELECT_TYPE.SELECT_SINGLE);
		table.setLayoutPosition(Position.ABSOLUTE);
		table.setTop(80); table.setWidth("97.5%");table.setHeight(555);table.setMargin(10);
		table.appendTitle("번호", 100, TextAlign.CENTER);
		table.appendTitle("제목", 700, TextAlign.CENTER);
		table.appendTitle("작성자", 150, TextAlign.CENTER);
		table.appendTitle("등록일자", 200, TextAlign.CENTER);
//		table.appendTitle("조회수", 150, TextAlign.CENTER);
		table.appendTitle("처리상태", 100, TextAlign.CENTER);
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
		cbidname.addItem("제목", "0");
		cbidname.addItem("아이디", "1");
		cbidname.addItem("이름(별명)", "2");
		row.add(cbidname);
		edidname = new MaterialTextBox();
		edidname.setLayoutPosition(Position.ABSOLUTE);
		edidname.setLabel("검색어입력");
		edidname.setTop(45); edidname.setLeft(225); edidname.setWidth("500px");
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

	
	private Map<String, Object> parametersMap = new HashMap<String, Object>();
	public void qryList(boolean bstart) {
		if(bstart) {
			offset = 0; 
			index = 1;
			table.clearRows();
		} else offset += 20;
		JSONObject parameterJSON = new JSONObject();
		parameterJSON.put("cmd", new JSONString("SELECT_SNSQNA_LIST"));
//		String strsdate = sdate.getText().toString();
//		if(!"".equals(strsdate)) {
//			parameterJSON.put("sdate", new JSONString(strsdate));
//		}
//		String stredate = edate.getText().toString();
//		if(!"".equals(stredate)) {
//			parameterJSON.put("edate", new JSONString(stredate+" 23:59:59"));
//		}

		String idname = edidname.getText();
		if(!"".equals(idname)) {
			if(cbidname.getSelectedIndex()== 0)
				parameterJSON.put("title", new JSONString(idname));
			else if(cbidname.getSelectedIndex() == 1)
				parameterJSON.put("id", new JSONString(idname));
			else parameterJSON.put("name", new JSONString(idname));
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
//					Console.log(" - rjsdsfdsf");
					countLabel.setText(bodyResultcnt.get("CNT")+" 건");
//					Console.log(" - 4532423");
					totcnt = Integer.parseInt(bodyResultcnt.get("CNT").toString());
					
					int usrCnt = bodyResultObj.size();
					if (usrCnt == 0) {
						MaterialToast.fireToast("검색된 결과가 없습니다.", 3000);
					}
					
					for(int i= 0;i< usrCnt;i++) {
						JSONObject obj = (JSONObject)bodyResultObj.get(i);
						ContentTableRow tableRow = table.addRow(Color.WHITE, new int[] {1,2}
								,index++
								,obj.get("title").isString().stringValue()
								,obj.get("id").isString().stringValue()
								,obj.get("date").isString().stringValue()
								,obj.get("res").isString().stringValue()
								);
						
						tableRow.put("qnaid", obj.get("qnaid").isString().stringValue());
						tableRow.put("title", obj.get("title").isString().stringValue());
						tableRow.put("body", obj.get("body").isString().stringValue());
						tableRow.put("date", obj.get("date").isString().stringValue());
						tableRow.put("id", obj.get("id").isString().stringValue());
						tableRow.put("no", (index-1));
						tableRow.addClickHandler(e->{
							ContentTableRow ctr = (ContentTableRow)e.getSource();
							if(ctr.getSelectedColumn() == 1) {//QnA 상세 보기
								parametersMap.put("qnaid", ctr.get("qnaid").toString());
								parametersMap.put("body", ctr.get("body").toString());
								parametersMap.put("date", ctr.get("date").toString());
								parametersMap.put("id", ctr.get("id").toString());
								parametersMap.put("title", ctr.get("title").toString());
								parametersMap.put("no", ctr.get("no").toString());
								host.getMaterialExtentsWindow().openDialog(ActivityApplication.QNA_DETAIL, parametersMap,1300,e2->{
									qryList(true);
								});
							} else if(ctr.getSelectedColumn() == 2) {// 회원 화면 이동
					        	MaterialLink tgrLink = new MaterialLink("회원관리");
					        	tgrLink.setIconType(IconType.CARD_MEMBERSHIP);
					        	tgrLink.setWaves(WavesType.DEFAULT);
					        	tgrLink.setIconPosition(IconPosition.LEFT);
					        	tgrLink.setTextColor(Color.BLUE);
								WindowParamter winParam = new WindowParamter(tgrLink, ApplicationView.WINDOW_MEMBER, "회원관리", 1500, 700);
								Map<String, Object> paramMap = new HashMap<String, Object>();
//								Console.log(ctr.get("id").toString().replaceAll("\"", ""));
				    			paramMap.put("SNS_ID", ctr.get("id").toString());
				    			winParam.setParams(paramMap);
								Registry.put("TARGET_LINK", winParam);
			        			host.appview.getAppView().openTargetWindow(paramMap);
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