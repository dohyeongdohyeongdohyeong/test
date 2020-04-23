package kr.or.visitkorea.admin.client.manager.repair;

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
import kr.or.visitkorea.admin.client.manager.widgets.AbstractContentPanel;
import kr.or.visitkorea.admin.client.manager.widgets.ContentTable;
import kr.or.visitkorea.admin.client.manager.widgets.ContentTableRow;
import kr.or.visitkorea.admin.client.widgets.window.MaterialExtentsWindow;

public class RepairMain extends AbstractContentPanel {

	private boolean bmanager = false;
	private ContentTable table;
	private MaterialLabel countLabel;
	private int offset;

	private MaterialComboBox<Object> rtype, status, repairman, datetype;
	private MaterialTextBox sdate, edate; //, sdate2, edate2;
//	private MaterialButton btnAdd;
	private String stfid = null;
	
	private MaterialComboBox<Object> cbidname;
	private MaterialTextBox edidname;
	
	private int totcnt;
	
	private int index =0;
	
	RepairApplication appview;
	public RepairMain(MaterialExtentsWindow materialExtentsWindow,  RepairApplication pa) {
		super(materialExtentsWindow);
		appview = pa;
	}
	private Map<String, Object> parametersMap = new HashMap<String, Object>();
	
	@Override
	public void init() {
		this.setLayoutPosition(Position.RELATIVE);
		
		MaterialPanel mrtop = new MaterialPanel(); this.add(mrtop);
		mrtop.setWidth("100%");	mrtop.setHeight("80px"); mrtop.setPadding(10);
		
		rtype = new MaterialComboBox<>(); mrtop.add(rtype);
		rtype.setLabel("요청분류");
		rtype.setLayoutPosition(Position.ABSOLUTE);
		rtype.setTop(5); rtype.setLeft(10); rtype.setWidth("120px");
		rtype.addItem("전체", -1);
		rtype.addItem("신규요청", 0);
		rtype.addItem("기능오류", 1);
		rtype.addItem("수정요청", 2);
		rtype.setSelectedIndex(0);
		rtype.addValueChangeHandler(e->{
			qryList(true);
		});
		
		status = new MaterialComboBox<>(); mrtop.add(status);
		status.setLabel("진행상태");
		status.setLayoutPosition(Position.ABSOLUTE);
		status.setTop(5); status.setLeft(150); status.setWidth("120px");
		status.addItem("전체", -1);
		status.addItem("접수", 0);
		status.addItem("처리중", 1);
		status.addItem("처리완료", 2);
		status.addItem("처리불가", 3);
		status.setSelectedIndex(0);
		status.addValueChangeHandler(e->{
			qryList(true);
		});
		
		repairman = new MaterialComboBox<>(); mrtop.add(repairman);
		repairman.setLabel("처리담당자");
		repairman.setLayoutPosition(Position.ABSOLUTE);
		repairman.setTop(5); repairman.setLeft(290); repairman.setWidth("100px");
		repairman.addItem("전체", 0);
		repairman.setSelectedIndex(0);
		JSONObject parameterJSON = new JSONObject();
		parameterJSON.put("cmd", new JSONString("SELECT_REPAIRMAN_LIST"));
		VisitKoreaBusiness.post("call", parameterJSON.toString(), new Func3<Object, String, Object>() {
			@Override
			public void call(Object param1, String param2, Object param3) {
				JSONObject resultObj = (JSONObject) JSONParser.parseStrict(JSON.stringify((JsObject)param1));
				JSONObject headerObj = (JSONObject) resultObj.get("header");
				String processResult = headerObj.get("process").isString().stringValue();
				if (processResult.equals("success")) {
					JSONObject bodyObj = (JSONObject) resultObj.get("body");
					JSONArray bodyResultObj = (JSONArray) bodyObj.get("result");
					for(int i= 0;i< bodyResultObj.size();i++) {
						JSONObject obj = (JSONObject)bodyResultObj.get(i);
						repairman.addItem(obj.get("name").isString().stringValue(), obj.get("remid").isString().stringValue());
					}
				}
			}
		});
		repairman.addValueChangeHandler(e->{
			qryList(true);
		});
		
		cbidname = new MaterialComboBox<>(); mrtop.add(cbidname);
		cbidname.setLayoutPosition(Position.ABSOLUTE);
		cbidname.setLabel("검색조건");
		cbidname.setTop(5);	cbidname.setLeft(410); cbidname.setWidth("100px");
		cbidname.addItem("제목+내용", 0);
		cbidname.addItem("제목", 1);
		cbidname.addItem("내용", 2);
		cbidname.addItem("ID", 3);
		cbidname.setSelectedIndex(0);
		edidname = new MaterialTextBox(); mrtop.add(edidname);
		edidname.setLabel("검색어입력");
		edidname.setLayoutPosition(Position.ABSOLUTE);
		edidname.setTop(5);	edidname.setLeft(520); edidname.setWidth("400px");
		edidname.addKeyPressHandler(event -> {
			if (event.getNativeEvent().getKeyCode() == KeyCodes.KEY_ENTER) {
				qryList(true);
			}
		});
		
		datetype = new MaterialComboBox<>(); mrtop.add(datetype);
		datetype.setLayoutPosition(Position.ABSOLUTE);
		datetype.setLabel("요청/처리일");
		datetype.setTop(5);	datetype.setRight(400); datetype.setWidth("100px");
		datetype.addItem("요청일", 0);
		datetype.addItem("처리일", 1);
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
		
		
		table = new ContentTable(ContentTable.TABLE_SELECT_TYPE.SELECT_SINGLE);
		table.setLayoutPosition(Position.ABSOLUTE);
		table.setTop(40);table.setWidth("98.5%"); table.setHeight(595); table.setMargin(10);
		table.appendTitle("번호", 80, TextAlign.CENTER);
		table.appendTitle("제목", 450, TextAlign.CENTER);
		table.appendTitle("요청분류", 150, TextAlign.CENTER);
		table.appendTitle("등록자", 170, TextAlign.CENTER);
		table.appendTitle("진행상태", 120, TextAlign.CENTER);
		table.appendTitle("담당자", 150, TextAlign.CENTER);
		table.appendTitle("요청일", 160, TextAlign.CENTER);
		table.appendTitle("처리일", 160, TextAlign.CENTER);
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
		
		btnXlsDn = new MaterialButton("엑셀다운로드");
		table.getButtomMenu().addButton(btnXlsDn, com.google.gwt.dom.client.Style.Float.LEFT);
		btnXlsDn.addClickHandler(e->{
			if(countLabel.getText().startsWith("0")) {
				getMaterialExtentsWindow().alert("결과 목록이 없습니다.", 500);
				return;
			}
			String downurl = "./call?cmd=FILE_DOWNLOAD_XLS";
			downurl += "&select_type=repair";
			
			if(rtype.getSelectedIndex() > 0) {
				downurl += "&rtype="+((int)rtype.getValues().get(rtype.getSelectedIndex()));
			}
			if(status.getSelectedIndex() > 0) {
				downurl += "&status="+((int)status.getValues().get(status.getSelectedIndex()));
			}
			if(repairman.getSelectedIndex() > 0) {
				downurl += "&remid="+(String)repairman.getValues().get(repairman.getSelectedIndex());
			}
			
			downurl += "&datetype="+((int)datetype.getValues().get(datetype.getSelectedIndex()));
			String strsdate = sdate.getText().toString();
			if(!"".equals(strsdate)) {
				downurl += "&sdate="+strsdate;
			}
			String stredate = edate.getText().toString();
			if(!"".equals(stredate)) {
				downurl += "&edate="+stredate+" 23:59:59";
			}
			
			String idname = edidname.getText();
			
			if(!"".equals(idname)) {
				downurl += "&searchtype="+cbidname.getSelectedIndex();
				downurl += "&word="+idname;
			}
			Window.open(downurl,"_self", "enabled");
		});
		
		
		MaterialButton btnrequest = new MaterialButton("요청하기");
		table.getButtomMenu().addButton(btnrequest, com.google.gwt.dom.client.Style.Float.LEFT);
		btnrequest.addClickHandler(event->{
			parametersMap.clear();
			getMaterialExtentsWindow().openDialog(RepairApplication.REPAIR_REQUEST_ADD, parametersMap,800,e2->{
				qryList(true);
			});
		});
		
		btnrepmanadd = new MaterialButton("담당자추가");
		table.getButtomMenu().addButton(btnrepmanadd, com.google.gwt.dom.client.Style.Float.LEFT);
		btnrepmanadd.addClickHandler(event->{
			parametersMap.clear();
			getMaterialExtentsWindow().openDialog(RepairApplication.REPAIR_MAN_ADD, parametersMap,500,e2->{
				repairman.clear();
				repairman.addItem("전체", 0);
				repairman.setSelectedIndex(0);
				JSONObject pjson = new JSONObject();
				pjson.put("cmd", new JSONString("SELECT_REPAIRMAN_LIST"));
				VisitKoreaBusiness.post("call", pjson.toString(), new Func3<Object, String, Object>() {
					@Override
					public void call(Object param1, String param2, Object param3) {
						JSONObject resultObj = (JSONObject) JSONParser.parseStrict(JSON.stringify((JsObject)param1));
						JSONObject headerObj = (JSONObject) resultObj.get("header");
						String processResult = headerObj.get("process").isString().stringValue();
						if (processResult.equals("success")) {
							JSONObject bodyObj = (JSONObject) resultObj.get("body");
							JSONArray bodyResultObj = (JSONArray) bodyObj.get("result");
							for(int i= 0;i< bodyResultObj.size();i++) {
								JSONObject obj = (JSONObject)bodyResultObj.get(i);
								repairman.addItem(obj.get("name").isString().stringValue(), obj.get("remid").isString().stringValue());
							}
						}
					}
				});
			});
		});
		
		btnrepman = new MaterialButton("담당자 삭제");
		table.getButtomMenu().addButton(btnrepman, com.google.gwt.dom.client.Style.Float.LEFT);
		btnrepman.addClickHandler(event->{
			parametersMap.clear();
			getMaterialExtentsWindow().openDialog(RepairApplication.REPAIR_MAN_DEL, parametersMap,600,e2->{
				repairman.clear();
				repairman.addItem("전체", 0);
				repairman.setSelectedIndex(0);
				JSONObject pjson = new JSONObject();
				pjson.put("cmd", new JSONString("SELECT_REPAIRMAN_LIST"));
				VisitKoreaBusiness.post("call", pjson.toString(), new Func3<Object, String, Object>() {
					@Override
					public void call(Object param1, String param2, Object param3) {
						JSONObject resultObj = (JSONObject) JSONParser.parseStrict(JSON.stringify((JsObject)param1));
						JSONObject headerObj = (JSONObject) resultObj.get("header");
						String processResult = headerObj.get("process").isString().stringValue();
						if (processResult.equals("success")) {
							JSONObject bodyObj = (JSONObject) resultObj.get("body");
							JSONArray bodyResultObj = (JSONArray) bodyObj.get("result");
							for(int i= 0;i< bodyResultObj.size();i++) {
								JSONObject obj = (JSONObject)bodyResultObj.get(i);
								repairman.addItem(obj.get("name").isString().stringValue(), obj.get("remid").isString().stringValue());
							}
						}
					}
				});
			});
		});
		
	}
	MaterialButton btnXlsDn;
	MaterialButton btnrepman;
	MaterialButton btnrepmanadd;
	@Override
	public void onLoad() {
	
		if (Registry.getPermission("8d5a99ee-89fa-4843-b7ce-75590965d379")) {
			bmanager = true;
			stfid = null;
		} else {
			bmanager = false;
			stfid = Registry.getStfId();
		}
		
		btnXlsDn.setVisible(bmanager);
		repairman.setVisible(bmanager);
		btnrepman.setVisible(bmanager);
		btnrepmanadd.setVisible(bmanager);
		qryList(true);
	}
	public void qryList(boolean bstart) {
		if(bstart) {
			offset = 0; 
			index = 1;
			table.clearRows();
		} else offset += 20;
		
		JSONObject parameterJSON = new JSONObject();
		parameterJSON.put("cmd", new JSONString("SELECT_REPAIR_LIST"));

		if(stfid != null)
			parameterJSON.put("stfid", new JSONString(stfid));
		if(rtype.getSelectedIndex() > 0) {
			parameterJSON.put("rtype", new JSONString((int)rtype.getValues().get(rtype.getSelectedIndex())+""));
		}
		if(status.getSelectedIndex() > 0) {
			parameterJSON.put("status", new JSONString((int)status.getValues().get(status.getSelectedIndex())+""));
		}
		if(repairman.getSelectedIndex() > 0) {
			parameterJSON.put("remid", new JSONString((String)repairman.getValues().get(repairman.getSelectedIndex())));
		}
		
		parameterJSON.put("datetype", new JSONString((int)datetype.getValues().get(datetype.getSelectedIndex())+""));
		String strsdate = sdate.getText().toString();
		if(!"".equals(strsdate)) {
			parameterJSON.put("sdate", new JSONString(strsdate));
		}
		String stredate = edate.getText().toString();
		if(!"".equals(stredate)) {
			parameterJSON.put("edate", new JSONString(stredate+" 23:59:59"));
		}
		
		if(status.getSelectedIndex() > 0) {
			parameterJSON.put("status", new JSONString(status.getValues().get(status.getSelectedIndex())+""));
		}
		
		String idname = edidname.getText(); 
		
		if(!"".equals(idname)) {
			parameterJSON.put("searchtype", new JSONString(cbidname.getSelectedIndex()+""));
			parameterJSON.put("word", new JSONString(idname));
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
						tableRow = table.addRow(Color.WHITE, new int[]{1}
								,index++
								,obj.get("title")!=null?obj.get("title").isString().stringValue():""
								,getRequestType((obj.get("rtype")!=null?(int)obj.get("rtype").isNumber().doubleValue():-1))
								,obj.get("stfid")!=null?obj.get("stfid").isString().stringValue():""
								,status==0?"접수":status==1?"처리중":status==2?"처리완료":"처리불가"
								,obj.get("resname")!=null?obj.get("resname").isString().stringValue():""
								,obj.get("reqdate")!=null?obj.get("reqdate").isString().stringValue():""
								,obj.get("resdate")!=null?obj.get("resdate").isString().stringValue():""
								);
						
						tableRow.put("usrid", obj.get("usrid")!=null?obj.get("usrid").isString().stringValue():"");
						
						tableRow.addClickHandler(event->{
							ContentTableRow ctr = (ContentTableRow)event.getSource();
							parametersMap.clear();
							parametersMap.put("repid", obj.get("repid").isString().stringValue());
							parametersMap.put("title", obj.get("title").isString().stringValue());
							parametersMap.put("reqman", obj.get("stfid").isString().stringValue());
							parametersMap.put("reqdate", obj.get("reqdate").isString().stringValue());
							parametersMap.put("reqbody", obj.get("reqbody").isString().stringValue());
							if(obj.get("reqfile") != null) {
								parametersMap.put("reqfile", obj.get("reqfile").isString().stringValue());
								parametersMap.put("reqfilepath", obj.get("reqfilepath").isString().stringValue());
							}
							parametersMap.put("rtype", obj.get("rtype")!=null?(int)obj.get("rtype").isNumber().doubleValue():0);
							parametersMap.put("status", obj.get("status")!=null?(int)obj.get("status").isNumber().doubleValue():0);
							parametersMap.put("remid", obj.get("remid")!=null?obj.get("remid").isString().stringValue():"0");
							if(obj.get("resfile") != null) {
								parametersMap.put("resfile", obj.get("resfile").isString().stringValue());
								parametersMap.put("resfilepath", obj.get("resfilepath").isString().stringValue());
							}
							parametersMap.put("resbody", obj.get("resbody")!=null?obj.get("resbody").isString().stringValue():"");
							if(ctr.getSelectedColumn() == 1) {
								String WriteusrId = ctr.get("usrid").toString();
								String MyusrId = Registry.getUserId();
								if(status == 0 && MyusrId == WriteusrId) {
									getMaterialExtentsWindow().openDialog(RepairApplication.REPAIR_REQUEST_ADD, parametersMap,800,e2->{
										qryList(true);
									});
								} else {
									parametersMap.put("manager", bmanager);
									getMaterialExtentsWindow().openDialog(RepairApplication.REPAIR_RESULT, parametersMap,800,e2->{
										qryList(true);
									});
								}
							}
						});
					}
					
				}else if (processResult.equals("fail")) {
					MaterialToast.fireToast("검색이 실패했습니다.", 3000);
					countLabel.setText("실패?");
				}
				table.loading(false);
			}
		});
	}
	
	public static String getRequestType(int val) {
		String type = "-";
		switch (val) {
		case 0 : type= "신규요청"; break;
		case 1 : type= "기능오류"; break;
		case 2 : type= "수정요청"; break;
		}
		return type;
	}
}
