package kr.or.visitkorea.admin.client.manager.monitoring;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.google.gwt.dom.client.Style.Float;
import com.google.gwt.dom.client.Style.FontWeight;
import com.google.gwt.dom.client.Style.Overflow;
import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.dom.client.Style.VerticalAlign;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.user.client.Timer;

import gwt.material.design.addins.client.tree.MaterialTree;
import gwt.material.design.addins.client.tree.MaterialTreeItem;
import gwt.material.design.client.base.MaterialWidget;
import gwt.material.design.client.constants.Color;
import gwt.material.design.client.constants.IconType;
import gwt.material.design.client.constants.TextAlign;
import gwt.material.design.client.ui.MaterialButton;
import gwt.material.design.client.ui.MaterialColumn;
import gwt.material.design.client.ui.MaterialIcon;
import gwt.material.design.client.ui.MaterialLabel;
import gwt.material.design.client.ui.MaterialPanel;
import gwt.material.design.client.ui.MaterialToast;
import gwt.material.design.jquery.client.api.Functions.Func3;
import gwt.material.design.jscore.client.api.JSON;
import gwt.material.design.jscore.client.api.JsObject;
import kr.or.visitkorea.admin.client.manager.VisitKoreaBusiness;
import kr.or.visitkorea.admin.client.manager.monitoring.model.Group;
import kr.or.visitkorea.admin.client.manager.monitoring.model.GroupUser;
import kr.or.visitkorea.admin.client.manager.monitoring.model.Monitor;
import kr.or.visitkorea.admin.client.manager.monitoring.model.SMS;
import kr.or.visitkorea.admin.client.manager.monitoring.model.User;
import kr.or.visitkorea.admin.client.manager.monitoring.panel.GroupDetailPanel;
import kr.or.visitkorea.admin.client.manager.monitoring.panel.UserSelectPanel;
import kr.or.visitkorea.admin.client.manager.widgets.AbstractContentPanel;
import kr.or.visitkorea.admin.client.widgets.window.MaterialExtentsWindow;

public class MonitoringMain extends AbstractContentPanel {

	private MaterialTree Usertree;
	private MaterialTree Grouptree;
	private MaterialTreeItem[] Lists;
	private MaterialTreeItem[] Lists2;
	private MaterialPanel GradePanel;
	private GroupDetailPanel gradeDetailPanel;
	private UserSelectPanel userSelectPanel;
	private ArrayList<User> Users;
	private ArrayList<Group> Groups = new ArrayList<Group>();
	private ArrayList<String> GroupNames;
	private boolean first = true;
	private boolean firstchk = true;
	private boolean onoff = false;
	private boolean oncheck = false;
	private boolean Statusonoff = true;
//	private int index;
	private Map<String, Object> parametersMap =new HashMap<String, Object>();
	private User ClickUser;
	private int TimerTimeCheck = 150;
	private String ClickGroup;
	private MaterialLabel StatusLabel;
	private MaterialLabel TitleLabel;
	private Timer t;
	private MaterialButton SystemButton;
	public MonitoringMain(MaterialExtentsWindow window, MonitoringApplication monitoringApplication) {
		super(window);
	}

	@Override
	public void init() {
		readGroupName(false);
		readUser();
		Labels();
		UsersPanel();
		GroupPanel();
		add();
	}
	
	public void StatusPanel() {

		MaterialPanel StatusPanel = new MaterialPanel();

		StatusPanel.setLayoutPosition(Position.ABSOLUTE);

		StatusPanel.setLeft(30);
		StatusPanel.setTop(60);

		StatusPanel.setWidth("330px");
		StatusPanel.setHeight("70	px");
		MaterialPanel LevelPanel = new MaterialPanel();
		LevelPanel.setFloat(Float.LEFT);
		LevelPanel.setWidth("40%");
		LevelPanel.setHeight("70px");
		LevelPanel.setBorder("1px solid rgb(224, 224, 224)");
		LevelPanel.setBackgroundColor(Color.GREY_LIGHTEN_4);
		StatusLabel = new MaterialLabel("");
		StatusLabel.setWidth("100%");
		StatusLabel.setTextAlign(TextAlign.CENTER);
		LevelPanel.setVerticalAlign(VerticalAlign.MIDDLE);
		LevelPanel.setLineHeight(70);
		StatusLabel.setFontSize("20pt");
		StatusLabel.setTextColor(Color.GREEN_DARKEN_2);
		LevelPanel.add(StatusLabel);
		StatusPanel.add(LevelPanel);

		MaterialPanel TitlePanel = new MaterialPanel();
		TitlePanel.setWidth("60%");
		TitlePanel.setHeight("70px");
		TitlePanel.setVerticalAlign(VerticalAlign.MIDDLE);
		TitlePanel.setFloat(Float.LEFT);
		TitlePanel.setLineHeight(70);
		TitlePanel.setBorderRight("1px solid rgb(224, 224, 224)");
		TitlePanel.setBorderTop("1px solid rgb(224, 224, 224)");
		TitlePanel.setBorderBottom("1px solid rgb(224, 224, 224)");
		TitlePanel.setBackgroundColor(Color.WHITE);
		TitleLabel = new MaterialLabel(GroupNames.size()>0?GroupNames.get(0):"");
		TitleLabel.setTextColor(Color.GREEN_DARKEN_2);
		TitleLabel.setWidth("100%");
		TitleLabel.setTextAlign(TextAlign.CENTER);
		TitleLabel.setFontSize("30pt");
		TitlePanel.setVerticalAlign(VerticalAlign.MIDDLE);
		TitlePanel.add(TitleLabel);
		StatusPanel.add(TitlePanel);
		this.add(StatusPanel);
		
		timer();
	}

	public void Labels() {
		MaterialColumn Statuscolumn = new MaterialColumn();
		
		MaterialLabel StatusLabel2 = new MaterialLabel("- 도메인 현재 상태");
		Statuscolumn.setLayoutPosition(Position.ABSOLUTE);
		Statuscolumn.setLeft(30);
		Statuscolumn.setTop(35);
		Statuscolumn.setWidth("330px");
		StatusLabel2.setFontWeight(FontWeight.BOLD);
		StatusLabel2.setFontSize("12pt");
		StatusLabel2.setTextAlign(TextAlign.LEFT);
		
		
		MaterialColumn Systemcolumn = new MaterialColumn();
		
		MaterialLabel Systemlabel = new MaterialLabel("시스템 상태 : ");
		Systemlabel.setFontSize("18pt");
		Systemlabel.setWidth("200px");
		Systemlabel.setFloat(Float.LEFT);
		SystemButton = new MaterialButton("Off");
		SystemButton.setFloat(Float.LEFT);
		SystemButton.setMarginLeft(20);
		SystemButton.addClickHandler(e ->{
			
			if(onoff) {
				
				this.getMaterialExtentsWindow().confirm("시스템을 종료하시겠습니까?",event->{
					MaterialButton btn = (MaterialButton) event.getSource();
					if (btn.getId().equals("yes")) {
						
					
						JSONObject parameterJSON = new JSONObject();
						parameterJSON.put("cmd", new JSONString("MONITORING_STOP"));
					
						VisitKoreaBusiness.post("call", parameterJSON.toString(), new Func3<Object, String, Object>() {
	
							@Override
							public void call(Object param1, String param2, Object param3) {
								JSONObject resultObj = (JSONObject) JSONParser.parseStrict(JSON.stringify((JsObject) param1));
								JSONObject bodyObj = resultObj.get("body").isObject();
								JSONObject headerObj = resultObj.get("header").isObject();
								String process = headerObj.get("process").isString().stringValue();
								
								if(process.equals("success")) {
									
									clear();
									Labels();
									UsersPanel();
									GroupPanel();
									add();
									MaterialToast.fireToast("시스템이 종료되었습니다.");
									SystemButton.setText("OFF");
									Statusonoff = false;
									onoff = false;
								} else {
									MaterialToast.fireToast("시스템 종료에 실패하였습니다.");
								}
							}
						
						});
					}
				});
				
				
			} else {
				this.getMaterialExtentsWindow().confirm("시스템을 실행하시겠습니까?",event->{
					
					MaterialButton btn = (MaterialButton) event.getSource();
					if (btn.getId().equals("yes")) {
						JSONObject parameterJSON = new JSONObject();
						parameterJSON.put("cmd", new JSONString("MONITORING_PLAY"));
						VisitKoreaBusiness.post("call", parameterJSON.toString(), new Func3<Object, String, Object>() {
							
							@Override
							public void call(Object param1, String param2, Object param3) {
								clear();
								Labels();
								UsersPanel();
								GroupPanel();
								add();
								
								MaterialToast.fireToast("모니터링 시스템이 실행되었습니다.");
								SystemButton.setText("ON");
								Statusonoff = true;
								onoff = true;
								oncheck = true;
								
							}
						});
					}
				});
			}
			
		});
		
		Systemcolumn.setLayoutPosition(Position.ABSOLUTE);
		Systemcolumn.setRight(30);
		Systemcolumn.setTop(15);
		Systemcolumn.setWidth("330px");
		Systemcolumn.add(Systemlabel);
		Systemcolumn.add(SystemButton);
		this.add(Systemcolumn);
		
		MaterialLabel UsersLabel = new MaterialLabel("- 사용자 목록");
		UsersLabel.setLayoutPosition(Position.ABSOLUTE);
		UsersLabel.setLeft(30);
		UsersLabel.setTop(165);
		UsersLabel.setFontWeight(FontWeight.BOLD);
		UsersLabel.setWidth("380px");
		UsersLabel.setFontSize("12pt");
		UsersLabel.setTextAlign(TextAlign.LEFT);

		MaterialLabel GroupLabel = new MaterialLabel("- 단계");
		GroupLabel.setLayoutPosition(Position.ABSOLUTE);
		GroupLabel.setLeft(420);
		GroupLabel.setTop(35);
		GroupLabel.setFontWeight(FontWeight.BOLD);
		GroupLabel.setWidth("1050px");
		GroupLabel.setFontSize("12pt");
		GroupLabel.setTextAlign(TextAlign.LEFT);

		Statuscolumn.add(StatusLabel2);
		this.add(Statuscolumn);
		this.add(GroupLabel);
		this.add(UsersLabel);
	}
	public void UsersPanel() {
		MaterialPanel UsersPanel = new MaterialPanel();
		UsersPanel.setLayoutPosition(Position.ABSOLUTE);
		UsersPanel.setLeft(30);
		UsersPanel.setTop(190);
		UsersPanel.setWidth("330px");
		UsersPanel.setHeight("440px");
		UsersPanel.setBorder("1px solid rgb(224, 224, 224)");
		UsersPanel.setBackgroundColor(Color.WHITE);
		this.add(UsersPanel);
		
		MaterialPanel Usertreepanel = new MaterialPanel();
		Usertreepanel.setHeight("415px");
		Usertreepanel.setWidth("328px");
		Usertreepanel.setTop(0);
		Usertreepanel.setOverflow(Overflow.AUTO);
		Usertreepanel.setLayoutPosition(Position.ABSOLUTE);
		UsersPanel.add(Usertreepanel);
		
		Usertree = new MaterialTree();
		Usertree.setTextAlign(TextAlign.LEFT);
		Usertree.setFontSize("1.0em");
		Usertreepanel.add(Usertree);
		
		MaterialPanel panelBottom = new MaterialPanel();
		panelBottom.setBorderTop("1px solid #e0e0e0");
		panelBottom.setHeight("25px");
		panelBottom.setWidth("328px");
		panelBottom.setPadding(0);
		panelBottom.setBottom(0);
		panelBottom.setLayoutPosition(Position.ABSOLUTE);
		
		MaterialIcon UserUpdate = new MaterialIcon(IconType.EDIT);
		UserUpdate.setLineHeight(25);
		UserUpdate.setVerticalAlign(VerticalAlign.MIDDLE);
		UserUpdate.setFontSize("1.0em");
		UserUpdate.setBorderRight("1px solid #e0e0e0");
		UserUpdate.setHeight("25px");
		UserUpdate.setMargin(0);
		UserUpdate.setWidth("25px");
		UserUpdate.setLayoutPosition(Position.ABSOLUTE);
		UserUpdate.setBottom(0);
		UserUpdate.setLeft(50);
		panelBottom.add(UserUpdate);
		
		HashMap<String, Object> UpdateDialog = new HashMap<String, Object>();
		UserUpdate.addClickHandler(event -> {
			if(ClickUser != null) {
			UpdateDialog.put("User", ClickUser);
			this.getMaterialExtentsWindow().openDialog(MonitoringApplication.USER_UPDATE_DIALOG, UpdateDialog,400);
			ClickUser = null;
			} else {
				
					MaterialToast.fireToast("삭제할 사용자를 클릭해 주세요.");
			}
		});
		
		MaterialIcon UserInsert = new MaterialIcon(IconType.ADD);
		UserInsert.setLineHeight(25);
		UserInsert.setVerticalAlign(VerticalAlign.MIDDLE);
		UserInsert.setFontSize("1.0em");
		UserInsert.setBorderRight("1px solid #e0e0e0");
		UserInsert.setHeight("25px");
		UserInsert.setMargin(0);
		UserInsert.setWidth("25px");
		UserInsert.setLayoutPosition(Position.ABSOLUTE);
		UserInsert.setBottom(0);
		UserInsert.setLeft(0);
		panelBottom.add(UserInsert);
		UserInsert.addClickHandler(event -> {
			this.getMaterialExtentsWindow().openDialog(MonitoringApplication.USER_ADD_DIALOG, null,400);
			GradePanel.clear();
		});
		
		MaterialIcon UserDelete = new MaterialIcon(IconType.REMOVE);
		UserDelete.setLineHeight(25);
		UserDelete.setVerticalAlign(VerticalAlign.MIDDLE);
		UserDelete.setFontSize("1.0em");
		UserDelete.setBorderRight("1px solid #e0e0e0");
		UserDelete.setHeight("25px");
		UserDelete.setMargin(0);
		UserDelete.setWidth("25px");
		UserDelete.setLayoutPosition(Position.ABSOLUTE);
		UserDelete.setBottom(0);
		UserDelete.setLeft(25);
		UserDelete.addClickHandler(event -> {
			if(ClickUser != null) {
				
			this.getMaterialExtentsWindow().confirm("정말 삭제하시겠습니까?", event2->{
				if(((MaterialButton)event2.getSource()).getId().equals("yes")) {
				JSONObject parameterJSON = new JSONObject();
				parameterJSON.put("cmd", new JSONString("MONITORING_USER_DELETE"));
				parameterJSON.put("uuid", ClickUser.id);
				
				VisitKoreaBusiness.post("call", parameterJSON.toString(), new Func3<Object, String, Object>() {

					@Override
					public void call(Object param1, String param2, Object param3) {
						JSONObject resultObj = (JSONObject) JSONParser.parseStrict(JSON.stringify((JsObject) param1));
						JSONObject bodyObj = resultObj.get("body").isObject();
						String result = bodyObj.get("Result").isString().stringValue();
						if(result.equals("sucess")) {
							MaterialToast.fireToast("사용자 삭제가 성공하였습니다");
							clearUsertree();
							readUser();
							ClickUser = null;
							GradePanel.clear();
						}else {
							MaterialToast.fireToast("사용자 삭제가 실패하였습니다");
						}
						
					
					}
					});
				}
			});
			} else {
				MaterialToast.fireToast("삭제할 사용자를 클릭해 주세요.");
			}
		});
		
		panelBottom.add(UserDelete);
		
		
		UsersPanel.add(panelBottom);
	}

	public void GroupPanel() {
		MaterialPanel groupPanel = new MaterialPanel();
		groupPanel.setLayoutPosition(Position.ABSOLUTE);
		groupPanel.setLeft(420);
		groupPanel.setTop(60);
		groupPanel.setOverflow(Overflow.AUTO);
		groupPanel.setWidth("1050px");
		groupPanel.setHeight("570px");
		groupPanel.setBorder("1px solid rgb(224, 224, 224)");

		MaterialPanel grouplistPanel = new MaterialPanel();
		grouplistPanel.setLayoutPosition(Position.ABSOLUTE);
		grouplistPanel.setLeft(40);
		grouplistPanel.setTop(35);
		grouplistPanel.setWidth("250px");
		grouplistPanel.setHeight("500px");
		grouplistPanel.setBorder("1px solid rgb(224, 224, 224)");
		groupPanel.add(grouplistPanel);
		
		MaterialPanel grouplisttreepanel = new MaterialPanel();
		grouplisttreepanel.setOverflow(Overflow.AUTO);
		grouplisttreepanel.setWidth("250px");
		grouplisttreepanel.setHeight("475px");
		grouplistPanel.add(grouplisttreepanel);
		
		GradePanel = new MaterialPanel();
		GradePanel.setLayoutPosition(Position.ABSOLUTE);
		GradePanel.setLeft(320);
		GradePanel.setTop(35);
		GradePanel.setOverflow(Overflow.AUTO);
		GradePanel.setWidth("690px");
		GradePanel.setHeight("500px");
		GradePanel.setBorder("1px solid rgb(224, 224, 224)");

		Grouptree = new MaterialTree();
		Grouptree.setTextAlign(TextAlign.LEFT);
		Grouptree.setFontSize("1.0em");
		grouplisttreepanel.add(Grouptree);
		groupPanel.add(GradePanel);
		
		MaterialPanel panelBottom = new MaterialPanel();
		panelBottom.setBorderTop("1px solid #e0e0e0");
		panelBottom.setHeight("25px");
		panelBottom.setWidth("248px");
		panelBottom.setPadding(0);
		panelBottom.setBottom(0);
		panelBottom.setLayoutPosition(Position.ABSOLUTE);
		
		MaterialIcon GroupDelete = new MaterialIcon(IconType.REMOVE);
		GroupDelete.setLineHeight(25);
		GroupDelete.setVerticalAlign(VerticalAlign.MIDDLE);
		GroupDelete.setFontSize("1.0em");
		GroupDelete.setBorderRight("1px solid #e0e0e0");
		GroupDelete.setHeight("25px");
		GroupDelete.setMargin(0);
		GroupDelete.setWidth("25px");
		GroupDelete.setLayoutPosition(Position.ABSOLUTE);
		GroupDelete.setBottom(0);
		GroupDelete.setLeft(25);
		
		GroupDelete.addClickHandler(event -> {
			if(ClickGroup != null) {
				if(GroupNames.size()>1) {
					
				getMaterialExtentsWindow().confirm("선택한 단계명 : "+ClickGroup, "해당단계를 정말 삭제하시겠습니까?", 500,  event2 ->{
				if(((MaterialButton)event2.getSource()).getId().equals("yes")) {
				JSONObject parameterJSON = new JSONObject();
				parameterJSON.put("cmd", new JSONString("MONITORING_GROUP_DELETE"));
				parameterJSON.put("GroupName", new JSONString(ClickGroup));
				
				VisitKoreaBusiness.post("call", parameterJSON.toString(), new Func3<Object, String, Object>() {

					@Override
					public void call(Object param1, String param2, Object param3) {
						
						JSONObject resultObj = (JSONObject) JSONParser.parseStrict(JSON.stringify((JsObject) param1));
						JSONObject bodyObj = resultObj.get("body").isObject();
						String result = bodyObj.get("Result").isString().stringValue();
						if(result.equals("sucess")) {
							MaterialToast.fireToast("그룹 삭제가 성공하였습니다");
							clearGrouptree();
							readGroupName(true);
							ClickGroup = null;
							GradePanel.clear();
							Groups.clear();
							
						}else {
							MaterialToast.fireToast("그룹 삭제가 실패하였습니다");
						}
						
					}
					});
				}
			});
				} else {
					MaterialToast.fireToast("그룹은 반드시 1개이상 있어야 합니다..");
				}
			}else {
				MaterialToast.fireToast("삭제할 그룹을 클릭해 주세요.");
			}
		});
		
		panelBottom.add(GroupDelete);
		
		
		MaterialIcon GroupInsert = new MaterialIcon(IconType.ADD);
		GroupInsert.setLineHeight(25);
		GroupInsert.setVerticalAlign(VerticalAlign.MIDDLE);
		GroupInsert.setFontSize("1.0em");
		GroupInsert.setBorderRight("1px solid #e0e0e0");
		GroupInsert.setHeight("25px");
		GroupInsert.setMargin(0);
		GroupInsert.setWidth("25px");
		GroupInsert.setLayoutPosition(Position.ABSOLUTE);
		GroupInsert.setBottom(0);
		GroupInsert.setLeft(0);
		HashMap<String, Object> InsertDialog = new HashMap<String, Object>();
		GroupInsert.addClickHandler(event -> {
			InsertDialog.put("GroupNames",GroupNames);
			this.getMaterialExtentsWindow().openDialog(MonitoringApplication.GROUP_ADD_DIALOG, InsertDialog,700);
		});
		
		panelBottom.add(GroupInsert);
		
		MaterialIcon GroupIndexUp = new MaterialIcon(IconType.KEYBOARD_ARROW_UP);
		GroupIndexUp.setLineHeight(25);
		GroupIndexUp.setVerticalAlign(VerticalAlign.MIDDLE);
		GroupIndexUp.setFontSize("1.0em");
		GroupIndexUp.setBorderRight("1px solid #e0e0e0");
		GroupIndexUp.setHeight("25px");
		GroupIndexUp.setMargin(0);
		GroupIndexUp.setWidth("25px");
		GroupIndexUp.setLayoutPosition(Position.ABSOLUTE);
		GroupIndexUp.setBottom(0);
		GroupIndexUp.setLeft(50);
		
		GroupIndexUp.addClickHandler(event -> {
			if(ClickGroup != null) {
				if(!ClickGroup.equals(GroupNames.get(0))){
				JSONObject parameterJSON = new JSONObject();
				parameterJSON.put("cmd", new JSONString("MONITORING_GROUP_INDEX_UP"));
				parameterJSON.put("GroupName", new JSONString(ClickGroup));
				
				VisitKoreaBusiness.post("call", parameterJSON.toString(), new Func3<Object, String, Object>() {

					@Override
					public void call(Object param1, String param2, Object param3) {
						
						JSONObject resultObj = (JSONObject) JSONParser.parseStrict(JSON.stringify((JsObject) param1));
						JSONObject bodyObj = resultObj.get("body").isObject();
						String result = bodyObj.get("Result").isString().stringValue();
						if(result.equals("sucess")) {
							MaterialToast.fireToast("순서변경이 성공하였습니다");
							clearGrouptree();
							readGroupName(true);
							ClickGroup = null;
							GradePanel.clear();
						}else {
							MaterialToast.fireToast("순서변경이 실패하였습니다");
						}
						
					}
			});
				} else {
					MaterialToast.fireToast("더이상 이동할 수 없습니다.");
				}
			}else {
				MaterialToast.fireToast("이동할 그룹을 클릭해 주세요.");
			}
		});
		
		MaterialIcon GroupIndexDown = new MaterialIcon(IconType.KEYBOARD_ARROW_DOWN);
		GroupIndexDown.setLineHeight(25);
		GroupIndexDown.setVerticalAlign(VerticalAlign.MIDDLE);
		GroupIndexDown.setFontSize("1.0em");
		GroupIndexDown.setBorderRight("1px solid #e0e0e0");
		GroupIndexDown.setHeight("25px");
		GroupIndexDown.setMargin(0);
		GroupIndexDown.setWidth("25px");
		GroupIndexDown.setLayoutPosition(Position.ABSOLUTE);
		GroupIndexDown.setBottom(0);
		GroupIndexDown.setLeft(75);
		
		GroupIndexDown.addClickHandler(event -> {
			if(ClickGroup != null) {
				if(!ClickGroup.equals(GroupNames.get(GroupNames.size()-1))){
				JSONObject parameterJSON = new JSONObject();
				parameterJSON.put("cmd", new JSONString("MONITORING_GROUP_INDEX_DOWN"));
				parameterJSON.put("GroupName", new JSONString(ClickGroup));
				
				VisitKoreaBusiness.post("call", parameterJSON.toString(), new Func3<Object, String, Object>() {

					@Override
					public void call(Object param1, String param2, Object param3) {
						
						JSONObject resultObj = (JSONObject) JSONParser.parseStrict(JSON.stringify((JsObject) param1));
						JSONObject bodyObj = resultObj.get("body").isObject();
						String result = bodyObj.get("Result").isString().stringValue();
						if(result.equals("sucess")) {
							MaterialToast.fireToast("순서변경이 성공하였습니다");
							clearGrouptree();
							readGroupName(true);
							ClickGroup = null;
							GradePanel.clear();
						}else {
							MaterialToast.fireToast("순서변경이 실패하였습니다");
						}
						
					}
			});
				} else {
					MaterialToast.fireToast("더이상 이동할 수 없습니다.");
				}
			}else {
				MaterialToast.fireToast("이동할 그룹을 클릭해 주세요.");
			}
		});
		panelBottom.add(GroupIndexDown);
		panelBottom.add(GroupIndexUp);
		grouplistPanel.add(panelBottom);
		
		this.add(groupPanel);

	}

	public void add() {
		this.gradeDetailPanel = new GroupDetailPanel(this);
		this.userSelectPanel = new UserSelectPanel(this);
	}

	public void readGroupName(boolean reload) {
		JSONObject parameterJSON = new JSONObject();
		parameterJSON.put("cmd", new JSONString("MONITORING_GROUP_NAME"));
		
		VisitKoreaBusiness.post("call", parameterJSON.toString(), new Func3<Object, String, Object>() {

			@Override
			public void call(Object param1, String param2, Object param3) {
				GroupNames = new ArrayList<String>();
				JSONObject resultObj = (JSONObject) JSONParser.parseStrict(JSON.stringify((JsObject) param1));
				JSONObject bodyObj = resultObj.get("body").isObject();
				
				if(bodyObj.containsKey("GroupName")) {
					
				if(bodyObj.get("GroupName") instanceof JSONArray) {
					JSONArray Groups = bodyObj.get("GroupName").isArray();
					for (int i = 0; i < Groups.size(); i++) {
						GroupNames.add((Groups.get(i).isObject().get("name").isString().stringValue()));
					}
				} else if(bodyObj.get("GroupName") instanceof JSONObject){
					JSONObject User = bodyObj.get("GroupName").isObject();
					GroupNames.add(User.get("name").isString().stringValue());
				} 
				} else if(bodyObj.containsKey("result")){
					String result = bodyObj.get("result").isString().stringValue();
					if(result.equals("GroupZero")) {
						MaterialToast.fireToast("그룹이 없습니다. 그룹을 추가해주세요");
					} else {
						MaterialToast.fireToast("그룹 읽기에 실패하였습니다. 관리자에게 문의하세요");
					}
				} else {
					oncheck = true;
				}
				
				if(reload == false)
					StatusPanel();
					
					
					buildGrouptree();
					
			}
		});
	
		
	}
	public void readUser() {
		JSONObject parameterJSON = new JSONObject();
		parameterJSON.put("cmd", new JSONString("MONITORING_USERS"));

		VisitKoreaBusiness.post("call", parameterJSON.toString(), new Func3<Object, String, Object>() {

			@Override
			public void call(Object param1, String param2, Object param3) {
				
				
				Users = new ArrayList<User>();
				
				JSONObject resultObj = (JSONObject) JSONParser.parseStrict(JSON.stringify((JsObject) param1));
				JSONObject bodyObj = resultObj.get("body").isObject();
				
				User user = null;
				SMS sms = null;
				if(bodyObj.containsKey("Users")) {
					
				if(bodyObj.get("Users") instanceof JSONArray) {
					JSONArray Userlist = bodyObj.get("Users").isArray();
					for (int i = 0; i < Userlist.size(); i++) {
							user = new User();
							user.name = Userlist.get(i).isObject().get("name").isString();
							user.id = Userlist.get(i).isObject().get("id").isString();
							user.enable = Userlist.get(i).isObject().get("enable").isString();

							sms = new SMS();
							sms.enable = Userlist.get(i).isObject().get("sms").isObject().get("enable").isBoolean();
							sms.content = Userlist.get(i).isObject().get("sms").isObject().get("content").isString();
							user.sms = sms;
							Users.add(user);
							
					}
				} else if(bodyObj.get("Users") instanceof JSONObject){
					JSONObject User = bodyObj.get("Users").isObject();
					user = new User();
					user.name = User.isObject().get("name").isString();
					user.id = User.isObject().get("id").isString();
					user.enable = User.isObject().get("enable").isString();
					
					sms = new SMS();
					sms.enable = User.isObject().get("sms").isObject().get("enable").isBoolean();
					sms.content = User.isObject().get("sms").isObject().get("content").isString();
					user.sms = sms;
					Users.add(user);
				} 
				} else if(bodyObj.containsKey("result")){
					String result = bodyObj.get("result").isString().stringValue();
					if(result.equals("UserZero")) {
						MaterialToast.fireToast("담당자가 없습니다. 담당자를 추가해주세요");
					} else {
						MaterialToast.fireToast("담당자 읽기에 실패하였습니다. 관리자에게 문의하세요");
					}
				}
				
						
				
				buildUsertree();
			}
			});
	}

	
	//단계 읽어오는 메서드\
	public void getGroupDetail(String grade) {
		
		JSONObject parameterJSON = new JSONObject();
		parameterJSON.put("cmd", new JSONString("MONITORING_GROUP_DETAIL"));
		parameterJSON.put("Grade", new JSONString(grade));
		VisitKoreaBusiness.post("call", parameterJSON.toString(), new Func3<Object, String, Object>() {

			@Override
			public void call(Object param1, String param2, Object param3) {
				
				JSONObject resultObj = (JSONObject) JSONParser.parseStrict(JSON.stringify((JsObject) param1));
				JSONObject bodyObj = resultObj.get("body").isObject();
				
				Group Group = new Group();
				Monitor Monitor = new Monitor();
				Group.Title = bodyObj.get("Groups").isObject().get("title").isString();
				Group.Level = bodyObj.get("Groups").isObject().get("level").isNumber();
				if(bodyObj.get("Groups").isObject().get("lastDuration") != null) 
				Group.lastDuration = bodyObj.get("Groups").isObject().get("lastDuration").isNumber();
				
				Group.Repeat = bodyObj.get("Groups").isObject().get("repeat").isNumber();
				Group.Report = bodyObj.get("Groups").isObject().get("report").isNumber();
				
				Monitor.Connectiontimeout = bodyObj.get("Groups").isObject().get("monitor").isObject().get("connectionTimeout").isNumber();
				Monitor.Readtimeout = bodyObj.get("Groups").isObject().get("monitor").isObject().get("readTimeout").isNumber();
				Monitor.Duration = bodyObj.get("Groups").isObject().get("monitor").isObject().get("duration").isNumber();
				Monitor.Template = bodyObj.get("Groups").isObject().get("monitor").isObject().get("template").isString();
				Monitor.Url = bodyObj.get("Groups").isObject().get("monitor").isObject().get("url").isString();
				Group.Monitor = Monitor;
				if(bodyObj.get("Groups").isObject().get("user") instanceof JSONArray) {
					
					for (int i = 0; i < ((JSONArray) bodyObj.get("Groups").isObject().get("user").isArray()).size(); i++) {
						GroupUser gu = new GroupUser();
					gu.id = bodyObj.get("Groups").isObject().get("user").isArray().get(i).isObject().get("id").isString();
					Group.Users.add(gu);
					
					}
				} else if(bodyObj.get("Groups").isObject().get("user") instanceof JSONObject) {
					GroupUser gu = new GroupUser();
					gu.id = bodyObj.get("Groups").isObject().get("user").isObject().get("id").isString();
					Group.Users.add(gu);
				}
				
				int check = 0;
				if(first == true) {
					Groups.add(Group);
				} else {
					for (int i = 0; i < Groups.size(); i++) {
						if(Groups.get(i).Title.equals(Group.Title)) {
							check++;
					}
				}
			} 
				
			if(check == 0) {
				if(first == false) {
					
				Groups.add(Group);
				} else {
					first = false;
				}
			}
				
			}
			});
	}

	private void buildUsertree() {



		ArrayList<String> a = new ArrayList<String>();
		for (int i = 0; i < Users.size(); i++) {

			a.add((Users.get(i).name + " (" + Users.get(i).sms.content + ")").replaceAll("\"", ""));
		}

		Lists = new MaterialTreeItem[a.size()];

		for (int i = 0; i < a.size(); i++) {
			Lists[i] = new MaterialTreeItem();
			Lists[i].setTextAlign(TextAlign.LEFT);
			Lists[i].setFontSize("1.1em");
			Lists[i].setTextColor(Color.BLUE);
			Lists[i].setText(a.get(i));
			Lists[i].setTitle(Users.get(i).name.toString());
			Lists[i].setIconType(IconType.PERSON);
			
			Lists[i].addClickHandler(event ->{
				
				for (int j = 0; j < Lists.length; j++) {
					
					if((Users.get(j).name+"").equals(event.getRelativeElement().getTitle())) {
						ClickUser =	Users.get(j);
					}
					
				}
				
			});
			
			Usertree.add(Lists[i]);
		}
	}

	private void buildGrouptree() {

		

		Lists2 = new MaterialTreeItem[GroupNames.size()];

		for (int i = 0; i < GroupNames.size(); i++) {
			Lists2[i] = new MaterialTreeItem();
			Lists2[i].setTextAlign(TextAlign.LEFT);
			Lists2[i].setFontSize("1.0em");
			Lists2[i].setTextColor(Color.BLUE);
			Lists2[i].setText(GroupNames.get(i));
			Lists2[i].setIconType(IconType.MENU);
			Lists2[i].setHide(false);
			Lists2[i].setTitle(GroupNames.get(i));
			
			Lists2[i].addClickHandler(event ->{
				ClickGroup = event.getRelativeElement().getTitle();
				getGroupDetail(event.getRelativeElement().getTitle());
				GradePanel.clear();
			});
			
			
			
			MaterialTreeItem option = new MaterialTreeItem();
			option.setTextAlign(TextAlign.LEFT);
			option.setFontSize("1.0em");
			option.setTextColor(Color.BLUE);
			option.setText("설정");
			option.setIconType(IconType.SETTINGS);
			Lists2[i].add(option);
			option.setTitle(GroupNames.get(i));
			option.setVisible(false);
			option.addClickHandler(event -> {
				event.stopPropagation();
				
				if (userSelectPanel != null) {
					GradePanel.remove(userSelectPanel);
				}
				
				parametersMap.remove("Group");
				parametersMap.clear();
				String title = event.getRelativeElement().getTitle();
				for (int j = 0; j < Groups.size(); j++) {
					
					if(((Groups.get(j).Title+"").replace("\"", "")).equals(title)){
						parametersMap.put("Group", Groups.get(j));
						parametersMap.put("GroupName",GroupNames);
					}
				}
				gradeDetailPanel.getparameter(parametersMap);
				GradePanel.add(gradeDetailPanel);
			});
			
			MaterialTreeItem groupusers = new MaterialTreeItem();
			groupusers.setTextAlign(TextAlign.LEFT);
			groupusers.setFontSize("1.0em");
			groupusers.setTextColor(Color.BLUE);
			groupusers.setText("담당자");
			groupusers.setVisible(false);
			groupusers.setTitle(GroupNames.get(i));
			groupusers.setIconType(IconType.GROUP);
			Lists2[i].add(groupusers);
		
			groupusers.addClickHandler(event -> {
				event.stopPropagation();
				if (gradeDetailPanel != null) {
					GradePanel.remove(gradeDetailPanel);
				}
				String title = event.getRelativeElement().getTitle();
				parametersMap.put("Users", Users);
				for (int j = 0; j < Groups.size(); j++) {
					
					if(((Groups.get(j).Title+"").replace("\"", "")).equals(title)){
						parametersMap.put("GroupUsers", Groups.get(j).Users);
						parametersMap.put("Grouptitle", title);
					}
				}

				GradePanel.add(userSelectPanel);
				userSelectPanel.getparameter(parametersMap);
			});
			
			
			
			Grouptree.add(Lists2[i]);
		}
	}

	public void readStatus() {
		JSONObject parameterJSON = new JSONObject();
		parameterJSON.put("cmd", new JSONString("MONITORING_STATUS"));
		
				VisitKoreaBusiness.post("call", parameterJSON.toString(), new Func3<Object, String, Object>() {

					@Override
					public void call(Object param1, String param2, Object param3) {
						JSONObject resultObj = (JSONObject) JSONParser.parseStrict(JSON.stringify((JsObject) param1));
						JSONObject bodyObj = resultObj.get("body").isObject();
						JSONObject headerObj = resultObj.get("header").isObject();
						String process = headerObj.get("process").isString().stringValue();
						
						if (process.equals("success")) {
							if(firstchk == true) {
								onoff = true;
								SystemButton.setText("ON");
								firstchk = false;
							}
							
							if(oncheck == true) {
								oncheck = false;
								readGroupName(false);
								readUser();
							}
						
						
						String level = bodyObj.get("Status").isObject().get("level").isString().stringValue();
						String title = bodyObj.get("Status").isObject().get("title").isString().stringValue();
						String CheckCount = bodyObj.get("Status").isObject().get("CheckCount").isString().stringValue();
						String hour = bodyObj.get("Hour").isString().stringValue();
							
						if(!TitleLabel.getText().equals(title)) {
							String before = TitleLabel.getText();
							MaterialToast.fireToast(" ["+before+"] -> "+ "["+title+"]"+" 단계로 변경되었습니다.   " + hour,60000);
							TitleLabel.setText(title);
						}
						
						StatusLabel.setText(level+"단계");
						
						levelColor(Integer.parseInt(level));
						
							} else {
								if(firstchk == true) {
									Statusonoff = false;
								}
						}
						
					}
					});
		
		
		
		
	}
	
	public void levelColor(int level) {
		if(level == 0) {
		TitleLabel.setTextColor(Color.GREEN);
		StatusLabel.setTextColor(Color.GREEN);
		} else if (level == 1 ) {
			TitleLabel.setTextColor(Color.BLUE);
			StatusLabel.setTextColor(Color.BLUE);
		} else if (level == 2) {
			TitleLabel.setTextColor(Color.ORANGE);
			StatusLabel.setTextColor(Color.ORANGE);
		} else if (level == 3) {
			TitleLabel.setTextColor(Color.RED);
			StatusLabel.setTextColor(Color.RED);
		}
	}
	
	public void timer() {
		t = new Timer() {
			
			

			@Override
			public void run() {
				
				
				if(TimerTimeCheck >= 150) {
					if(Statusonoff) {
						readStatus();
					}
					TimerTimeCheck = 0;
				} else {
					TimerTimeCheck++;
				}
			}
		};
		
		t.schedule(0);
		t.scheduleRepeating(100);
		t.run();
	}
	public void clearUsertree() {
		Usertree.clear();
	}
	public void clearGrouptree() {
		Grouptree.clear();
	}
	
	public void stoptimer() {
		t.cancel();
	}
	
	public void GradePanelClear() {
		GradePanel.clear();
	}
	public void GroupClear() {
		Groups.clear();
	}
	
	protected MaterialIcon addIcon(MaterialWidget parent, IconType iconType, Float floatAlign) {
		MaterialIcon icon = new MaterialIcon(iconType);
		icon.setFloat(floatAlign);
		icon.setMargin(0);
		icon.setFontSize("26px");
		parent.add(icon);
		return icon;
	}

	
}
