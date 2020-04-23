package kr.or.visitkorea.admin.client.manager.memberActivity.panels;

import java.util.HashMap;
import java.util.Map;

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
import kr.or.visitkorea.admin.client.application.Console;
import kr.or.visitkorea.admin.client.application.Registry;
import kr.or.visitkorea.admin.client.application.UI;
import kr.or.visitkorea.admin.client.application.WindowParamter;
import kr.or.visitkorea.admin.client.manager.VisitKoreaBusiness;
import kr.or.visitkorea.admin.client.manager.memberActivity.ActivityApplication;
import kr.or.visitkorea.admin.client.manager.memberActivity.MemberActivityMain;
import kr.or.visitkorea.admin.client.manager.widgets.ContentTable;
import kr.or.visitkorea.admin.client.manager.widgets.ContentTableRow;

public class ZikimiPanel extends BasePanel {

	private ContentTable table;
	private MaterialLabel countLabel;
	private MaterialComboBox<Object> cbidname;
	private MaterialComboBox<Object> category, status, reqType;
	private MaterialTextBox edidname;
	private MaterialPanel checkboxContainer;
	protected String zikid;
	private MaterialIcon nextRowsIcon;
	private MaterialIcon searchIcon;
	private int totcnt;
	private int index;
	private int offset;
	private int viewType;
	private boolean permissionEnabled;
	private Map<String, Object> parametersMap = new HashMap<String, Object>();
	private int Order;
	
	public ZikimiPanel(MemberActivityMain host) {
		super(host);
		if (viewType == 9)
			permissionEnabled = Registry.getPermission("7e17d1fd-9b33-44d7-9d30-d23d4eb7052b");
		if (viewType == 10)
			permissionEnabled = Registry.getPermission("c1be1855-0e5f-4c2e-9c01-04769d216978");
		cbidname.setEnabled(permissionEnabled);
		edidname.setEnabled(permissionEnabled);
		reqType.setEnabled(permissionEnabled);
		category.setEnabled(permissionEnabled);
		status.setEnabled(permissionEnabled);
		searchIcon.setEnabled(permissionEnabled);
	}

	@Override
	protected Widget createBody() {
		table = new ContentTable(ContentTable.TABLE_SELECT_TYPE.SELECT_SINGLE);
		table.setLayoutPosition(Position.ABSOLUTE);
		table.setTop(80); table.setWidth("97.5%");table.setHeight(555);table.setMargin(10);
		table.appendTitle("번호", 70, TextAlign.CENTER);
		table.appendTitle("요청구분", 150, TextAlign.CENTER);
		table.appendTitle("카테고리", 150, TextAlign.CENTER);
		table.appendTitle("콘텐츠명", 300, TextAlign.CENTER);
		table.appendTitle("작성자", 150, TextAlign.CENTER);
		table.appendTitle("등록일시", 170, TextAlign.CENTER);
		table.appendTitle("처리일시", 170, TextAlign.CENTER);
		table.appendTitle("처리상태", 150, TextAlign.CENTER);
		table.appendTitle("댓글확인", 150, TextAlign.CENTER);
		this.add(table);
		
		Order = 0;
		MaterialLabel CreateLabel = (MaterialLabel)table.getHeader().getChildrenList().get(5);
		MaterialLabel UpdateLabel = (MaterialLabel)table.getHeader().getChildrenList().get(6);
		MaterialLabel CommentLabel = (MaterialLabel)table.getHeader().getChildrenList().get(8);
		CreateLabel.addClickHandler(event->{
			if(Order != 2) {
				Order = 2;
				CreateLabel.setText("등록일시↓");
				UpdateLabel.setText("처리일시");
				CommentLabel.setText("댓글확인");
			}else if(Order != 1){
				CreateLabel.setText("등록일시↑");
				UpdateLabel.setText("처리일시");
				CommentLabel.setText("댓글확인");
				Order = 1;
			}
			qryList(true);
		});
		UpdateLabel.addClickHandler(event->{
			if(Order != 4) {
				Order = 4;
				UpdateLabel.setText("처리일시↓");
				CreateLabel.setText("등록일시");
				CommentLabel.setText("댓글확인");
			}else if(Order != 3){
				UpdateLabel.setText("처리일시↑");
				CreateLabel.setText("등록일시");
				CommentLabel.setText("댓글확인");
				Order = 3;
			}
			qryList(true);
		});
		CommentLabel.addClickHandler(event->{
				Order = 5;
				CommentLabel.setText("댓글확인↓");
				UpdateLabel.setText("처리일시");
				CreateLabel.setText("등록일시");
			qryList(true);
		});
		
		searchIcon = new MaterialIcon(IconType.SEARCH);
		searchIcon.setTextAlign(TextAlign.CENTER);
		searchIcon.addClickHandler(event->{
			qryList(true);
		});
		table.getTopMenu().addIcon(searchIcon, "검색", com.google.gwt.dom.client.Style.Float.RIGHT, "1.8em", "26px", 24, false);
		
		nextRowsIcon = new MaterialIcon(IconType.ARROW_DOWNWARD);
		nextRowsIcon.setTextAlign(TextAlign.CENTER);
		nextRowsIcon.addClickHandler(event->{
			if((offset+20) >= totcnt) {
				MaterialToast.fireToast("더이상의 결과 값이 없습니다.", 3000);
			} else {
				qryList(false);
			}
		});
		
		if (viewType == 9)
			nextRowsIcon.setEnabled(Registry.getPermission("fed394a6-3b59-4315-8ed5-1cefdc61b716"));
		else if (viewType == 10) 
			nextRowsIcon.setEnabled(Registry.getPermission("c750b9e7-5c36-4353-b948-1b09fcb27e07"));
		else
			nextRowsIcon.setEnabled(false);
		
		table.getButtomMenu().addIcon(nextRowsIcon, "다음 20개", com.google.gwt.dom.client.Style.Float.RIGHT);
		
		countLabel = new MaterialLabel("0 건");
		countLabel.setFontWeight(FontWeight.BOLDER);
		countLabel.setTextColor(Color.BLACK);
		table.getButtomMenu().addLabel(countLabel, com.google.gwt.dom.client.Style.Float.RIGHT);
		
		MaterialLabel xls = new MaterialLabel("XLS"); 
		xls.setFontWeight(FontWeight.BOLD);
		xls.setHoverable(true); xls.setPadding(3);
		xls.getElement().getStyle().setCursor(Style.Cursor.POINTER);
		table.getButtomMenu().addLabel(xls, com.google.gwt.dom.client.Style.Float.LEFT);
		xls.addClickHandler(e->{
			if(countLabel.getText().startsWith("0")) {
				host.getMaterialExtentsWindow().alert("결과 목록이 없습니다.", 500);
				return;
			}

			String idname = edidname.getText();
			String downurl = "./call?cmd=SELECT_ZIKIMI_LIST_XLS";
			if(!"".equals(idname)) {
				if(cbidname.getSelectedIndex()== 0)
					downurl += "&title="+idname;
				else if(cbidname.getSelectedIndex() == 1)
					downurl += "&id="+idname;
				else if(cbidname.getSelectedIndex() == 2)
					downurl += "&name="+idname;
			}
			downurl += "&viewType=" + viewType;

			if (reqType.getSelectedIndex() >= 0) {
				downurl += "&reqType="+reqType.getSelectedIndex();
			}
			if (viewType == 9)
				downurl += "&utype=1";
			else
				downurl += "&utype=10";
			if(category.getSelectedIndex() > 0) {
				downurl += "&contenttype="+(int) category.getValues().get(category.getSelectedIndex());
			}
			if(status.getSelectedIndex() > 0) {
				downurl += "&status="+status.getValues().get(status.getSelectedIndex());
			}
			Window.open(downurl,"_self", "enabled");
		});
		
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
		cbidname.setTop(45); cbidname.setLeft(15); cbidname.setWidth("150px");
		cbidname.addItem("콘텐츠명", 0);
		cbidname.addItem("아이디", 1);
		cbidname.addItem("이름(기관명)", 2);
		row.add(cbidname);
		
		edidname = new MaterialTextBox();
		edidname.setLayoutPosition(Position.ABSOLUTE);
		edidname.setLabel("검색어입력");
		edidname.setTop(45); edidname.setLeft(175); edidname.setWidth("400px");
		row.add(edidname);
		
		edidname.addKeyPressHandler(event -> {
			if (event.getNativeEvent().getKeyCode() == KeyCodes.KEY_ENTER) {
				qryList(true);
			}
		});
		
		reqType = new MaterialComboBox<>();
		reqType.setLayoutPosition(Position.ABSOLUTE);
		reqType.setTop(45); reqType.setLeft(615); reqType.setWidth("150px");
		row.add(reqType);
		reqType.addItem("수정 요청", 0);
		reqType.addItem("신규 요청", 1);
		
		Console.log("viewType :" +viewType);
		
		if (viewType == 9)
			reqType.addItem("구석피디아1", 2);
			reqType.addItem("구석피디아2", 3);
			reqType.addItem("구석피디아3", 4);
		reqType.addValueChangeHandler(ee->{
			qryList(true);
		});
		reqType.setSelectedIndex(0);
		
		category = new MaterialComboBox<>();
		category.setLayoutPosition(Position.ABSOLUTE);
		category.setTop(45); category.setLeft(780); category.setWidth("150px");
		row.add(category);
		category.addItem("전체", 0);
		category.addItem("관광지",12);
		category.addItem("문화시설",14);
		category.addItem("축제행사공연",15);
		category.addItem("여행코스",25);
		category.addItem("레포츠",28);
		category.addItem("숙박",32);
		category.addItem("쇼핑",38);
		category.addItem("음식점",39);
		category.addItem("생태관광",2000);
		category.addValueChangeHandler(ee->{
			qryList(true);
		});
		category.setSelectedIndex(0);
		
		status = new MaterialComboBox<>();
		status.setLayoutPosition(Position.ABSOLUTE);
		status.setTop(45); status.setLeft(950); status.setWidth("150px");
		status.addItem("전체", -1);
		status.addItem("접수", 0);
		status.addItem("처리중", 1);
		status.addItem("승인완료", 2);
		status.addItem("승인거절", 3);
		status.setSelectedIndex(0);
		row.add(status);
		status.addValueChangeHandler(e->{
			qryList(true);
		});
		
		return row;
	}
	
	public void qryList(boolean bstart) {
		if(bstart) {
			offset = 0; 
			index = 1;
			table.clearRows();
		} else offset += 20;
		JSONObject parameterJSON = new JSONObject();
		parameterJSON.put("cmd", new JSONString("SELECT_ZIKIMI_LIST"));

		String idname = edidname.getText();
		if(!"".equals(idname)) {
			if(cbidname.getSelectedIndex()== 0)
				parameterJSON.put("title", new JSONString(idname));
			else if(cbidname.getSelectedIndex() == 1)
				parameterJSON.put("id", new JSONString(idname));
			else if(cbidname.getSelectedIndex() == 2)
				parameterJSON.put("name", new JSONString(idname));
		}
		// 일반 사용자 구분 적용
		parameterJSON.put("viewType", new JSONNumber(viewType));
		// 요청 구분 적용
		if (reqType.getSelectedIndex() >= 0) {
			parameterJSON.put("reqType", new JSONString(reqType.getSelectedIndex()+""));
		} 
		if(category.getSelectedIndex() > 0) {
			parameterJSON.put("contenttype", new JSONString(category.getValues().get(category.getSelectedIndex())+""));
		}
		if(status.getSelectedIndex() > 0) {
			parameterJSON.put("status", new JSONString(status.getValues().get(status.getSelectedIndex())+""));
		}
		
		if(Order > 0) {
			parameterJSON.put("order", new JSONString(Order+""));
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
						String ctype = UI.getContentType((int)(obj.get("contenttype")!=null?obj.get("contenttype").isNumber().doubleValue():-1));
							
						final String fctype = ctype;
						String sstatus = "접수";
						switch((int)(obj.get("status")!=null?obj.get("status").isNumber().doubleValue():0)) {
						case 0: sstatus = "접수"; break;
						case 1: sstatus = "처리중"; break;
						case 2: sstatus = "승인완료"; break;
						case 3: sstatus = "승인거절"; break;
						}
						
						ContentTableRow tableRow = table.addRow(Color.WHITE, new int[] {3,4}
								,""+(index++)
								,obj.get("reqType").isNumber().doubleValue() == 0 ? "수정요청" 
										: obj.get("reqType").isNumber().doubleValue() == 1 ? "신규등록" 
												: obj.get("reqType").isNumber().doubleValue() == 2 ? "구석피디아1" 
													: obj.get("reqType").isNumber().doubleValue() == 3 ? "구석피디아2" 
														: obj.get("reqType").isNumber().doubleValue() == 4 ? "구석피디아3" : "-"
								,ctype
								,reqType.getSelectedValue().get(0).toString().equals("1") ? 
										obj.get("reqtitle")!=null?obj.get("reqtitle").isString().stringValue():""
										: obj.get("title")!=null?obj.get("title").isString().stringValue():""
								,obj.get("snsid")!=null?obj.get("snsid").isString().stringValue():""
								,obj.get("cdate")!=null?obj.get("cdate").isString().stringValue():""
								,obj.get("udate")!=null?obj.get("udate").isString().stringValue():""
								,sstatus
								,obj.get("res")!=null?obj.get("res").isString().stringValue():"-"
								);
						
						if (obj.get("reqType") != null && obj.get("reqType").isNumber().doubleValue() == 1) {
							MaterialLabel checkPointRow = (MaterialLabel) tableRow.getColumnObject(2);
							checkPointRow.setTextColor(Color.RED_LIGHTEN_2);
							checkPointRow.setFontWeight(FontWeight.BOLD);
						}
						
						tableRow.addClickHandler(e->{
							ContentTableRow ctr = (ContentTableRow)e.getSource();
//							Object row = e.getSource();
							if(ctr.getSelectedColumn() == 3) {//지킴이 정보 상세 보기
								if ((viewType == 9 && Registry.getPermission("8c1ea2b2-d642-4285-8c0a-32c23ec13c7b")) ||
								(viewType == 10 && Registry.getPermission("60308cdf-c6e9-447e-97e3-18bd30f63ee4"))) {
									parametersMap.put("requrl", obj.get("requrl") != null ? obj.get("requrl").isString().stringValue():"");
									parametersMap.put("reqtitle", obj.get("reqtitle") != null ? obj.get("reqtitle").isString().stringValue(): "");
									parametersMap.put("utype", obj.get("utype").isNumber().doubleValue()==10?"Partners":"일반사용자");
									parametersMap.put("reqType", obj.get("reqType").isNumber().doubleValue() == 0 ? "수정요청"
											: obj.get("reqType").isNumber().doubleValue() == 1 ? "신규등록"
													: obj.get("reqType").isNumber().doubleValue() == 2 ? "구석피디아1"
															: obj.get("reqType").isNumber().doubleValue() == 3 ? "구석피디아2"
																		: obj.get("reqType").isNumber().doubleValue() == 4 ? "구석피디아3" : "-");
									parametersMap.put("title", obj.get("title")!=null?obj.get("title").isString().stringValue():"");
									parametersMap.put("id", obj.get("snsid")!=null?obj.get("snsid").isString().stringValue():"");
									parametersMap.put("cdate", obj.get("cdate")!=null?obj.get("cdate").isString().stringValue():"");
									parametersMap.put("udate", obj.get("udate")!=null?obj.get("udate").isString().stringValue():"");
									parametersMap.put("body", obj.get("body")!=null?obj.get("body").isString().stringValue():"");
									parametersMap.put("fctype", fctype);
									parametersMap.put("status", (int)obj.get("status").isNumber().doubleValue());
									parametersMap.put("zikid", obj.get("zikid")!=null?obj.get("zikid").isString().stringValue():"");
									parametersMap.put("viewType", viewType);
									parametersMap.put("row", ctr);
									parametersMap.put("obj", obj);
									
									host.getMaterialExtentsWindow().openDialog(ActivityApplication.ZIKIMI_DETAIL, parametersMap,1300,e2->{
										qryList(true);
									});
								}
							} else if(ctr.getSelectedColumn() == 4) {// 회원 화면 이동
								if ((viewType == 9 && Registry.getPermission("09d010c9-7fca-443c-a393-f45cd8929786")) ||
										(viewType == 10 && Registry.getPermission("ee2a311b-9160-49e6-a857-59b7051d86ff"))) {
				        			MaterialLink tgrLink = new MaterialLink("회원관리");
						        	tgrLink.setIconType(IconType.CARD_MEMBERSHIP);
						        	tgrLink.setWaves(WavesType.DEFAULT);
						        	tgrLink.setIconPosition(IconPosition.LEFT);
						        	tgrLink.setTextColor(Color.BLUE);
									WindowParamter winParam = new WindowParamter(tgrLink, ApplicationView.WINDOW_MEMBER, "회원관리", 1500, 700);
									Map<String, Object> paramMap = new HashMap<String, Object>();
					    			paramMap.put("SNS_ID", obj.get("snsid")!=null?obj.get("snsid").isString().stringValue():"");
					    			winParam.setParams(paramMap);
									Registry.put("TARGET_LINK", winParam);
				        			host.appview.getAppView().openTargetWindow(paramMap);
								}
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
		//구분값 10: 파트너, 이외에 값은 사용자
		viewType = ((ActivityApplication) Registry.get("WINDOW_MEMBER_ACTIVITY")).getReqType() == "USERS" ? 9 : 10;
	}
}