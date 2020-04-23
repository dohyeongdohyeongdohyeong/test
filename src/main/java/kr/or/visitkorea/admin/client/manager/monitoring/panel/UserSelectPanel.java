package kr.or.visitkorea.admin.client.manager.monitoring.panel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


import com.google.gwt.dom.client.Style.FontWeight;
import com.google.gwt.dom.client.Style.Overflow;
import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.dom.client.Style.VerticalAlign;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONString;

import gwt.material.design.addins.client.tree.MaterialTree;
import gwt.material.design.addins.client.tree.MaterialTreeItem;
import gwt.material.design.client.constants.Color;
import gwt.material.design.client.constants.IconType;
import gwt.material.design.client.constants.TextAlign;
import gwt.material.design.client.ui.MaterialIcon;
import gwt.material.design.client.ui.MaterialLabel;
import gwt.material.design.client.ui.MaterialPanel;
import gwt.material.design.client.ui.MaterialToast;
import gwt.material.design.jquery.client.api.Functions.Func3;
import gwt.material.design.jscore.client.api.JSON;
import gwt.material.design.jscore.client.api.JsObject;
import kr.or.visitkorea.admin.client.manager.monitoring.MonitoringMain;
import kr.or.visitkorea.admin.client.manager.monitoring.model.GroupUser;
import kr.or.visitkorea.admin.client.manager.monitoring.model.User;
import kr.or.visitkorea.admin.client.manager.VisitKoreaBusiness;

public class UserSelectPanel extends BasePanel {

	public UserSelectPanel(MonitoringMain host) {
		super(host);
		Build();
	}

	private MaterialTree Userlisttree;
	private MaterialTree UserSelecttree;
	private MaterialTreeItem[] UserSelectList;
	private MaterialTreeItem[] UserList;
	private MaterialPanel UserListPanel;
	private MaterialPanel UserSelectListPanel;
	private Map<String, Object> parameter =new HashMap<String, Object>();
	private ArrayList<User> Users = new ArrayList<User>();
	private ArrayList<GroupUser> SelectUsers = new ArrayList<GroupUser>();
	private User ClickUser;
	public void Build() {
		
		this.setWidth("100%");
		this.setHeight("100%");
		
		MaterialLabel MainLabel = new MaterialLabel("담당자 지정");
		MainLabel.setBackgroundColor(Color.BLUE);
		MainLabel.setPaddingLeft(10);
		MainLabel.setPaddingTop(3);
		MainLabel.setFontWeight(FontWeight.BOLD);
		MainLabel.setTextColor(Color.WHITE);
		MainLabel.setFontSize("1.1em");
		this.add(MainLabel);
		
		MaterialIcon SaveIcon = new MaterialIcon(IconType.SAVE);
		SaveIcon.setLineHeight(27);
		SaveIcon.setVerticalAlign(VerticalAlign.MIDDLE);
		SaveIcon.setFontSize("1.4em");
		SaveIcon.setHeight("27px");
		SaveIcon.setMargin(0);
		SaveIcon.setWidth("27px");
		SaveIcon.setLayoutPosition(Position.ABSOLUTE);
		SaveIcon.setTop(0);
		SaveIcon.setRight(5);

		SaveIcon.addClickHandler(event -> {
			String sendUsers = "";
			for (int i = 0; i < SelectUsers.size(); i++) {
				sendUsers = sendUsers + SelectUsers.get(i).id.stringValue() + ",";
			}
			
			JSONObject parameterJSON = new JSONObject();
			parameterJSON.put("cmd", new JSONString("MONITORING_GROUP_USER_UPDATE"));
			parameterJSON.put("SelectUsers",new JSONString(sendUsers));
			parameterJSON.put("GroupTitle",new JSONString(parameter.get("Grouptitle").toString()));
			
			VisitKoreaBusiness.post("call", parameterJSON.toString(), new Func3<Object, String, Object>() {
				
				@Override
				public void call(Object param1, String param2, Object param3) {
					

					JSONObject resultObj = (JSONObject) JSONParser.parseStrict(JSON.stringify((JsObject) param1));
					JSONObject bodyObj = resultObj.get("body").isObject();
					String result = bodyObj.get("Result").isString().stringValue();
					if(result.equals("sucess")) {
						MaterialToast.fireToast("설정 변경이 성공하였습니다");
					}else {
						MaterialToast.fireToast("설정 변경이 실패하였습니다");
					}
					
					
					
				}
			});
			
			
		});

		add(SaveIcon);
		
		UserListPanel = new MaterialPanel();
		UserListPanel.setLayoutPosition(Position.ABSOLUTE);
		UserListPanel.setLeft(40);
		UserListPanel.setTop(40);
		UserListPanel.setOverflow(Overflow.AUTO);
		UserListPanel.setWidth("250px");
		UserListPanel.setHeight("440px");
		UserListPanel.setBorder("1px solid rgb(224, 224, 224)");
		UserListPanel.setBackgroundColor(Color.WHITE);
		this.add(UserListPanel);
		
		MaterialIcon Insert = new MaterialIcon(IconType.ARROW_FORWARD);
		Insert.setLineHeight(50);
		Insert.setVerticalAlign(VerticalAlign.MIDDLE);
		Insert.setFontSize("2.3em");
		Insert.setHeight("50px");
		Insert.setLayoutPosition(Position.ABSOLUTE);
		Insert.setTop(170);
		Insert.setLeft(325);
		Insert.addClickHandler(event-> {
			if(ClickUser != null) {
			GroupUser user = new GroupUser();
			user.id = ClickUser.id;
			SelectUsers.add(user);
			setData();
			ClickUser = null;
			}else {
				MaterialToast.fireToast("이동할 사용자를 클릭해 주세요.");
			}
		});
		this.add(Insert);
		
		MaterialIcon Delete = new MaterialIcon(IconType.ARROW_BACK);
		Delete.setLineHeight(50);
		Delete.setVerticalAlign(VerticalAlign.MIDDLE);
		Delete.setFontSize("2.3em");
		Delete.setHeight("50px");
		Delete.setLayoutPosition(Position.ABSOLUTE);
		Delete.setTop(280);
		Delete.setLeft(325);
		Delete.addClickHandler(event -> {
				if(ClickUser != null) {
					
			for (int i = 0; i < SelectUsers.size(); i++) {
				if(SelectUsers.get(i).id.equals(ClickUser.id)) {
				SelectUsers.remove(i);
				}
			}
			setData();
			ClickUser = null;
				} else {
					MaterialToast.fireToast("이동할 사용자를 클릭해 주세요.");
				}
		});
		this.add(Delete);
		
		UserSelectListPanel = new MaterialPanel();
		UserSelectListPanel.setLayoutPosition(Position.ABSOLUTE);
		UserSelectListPanel.setRight(40);
		UserSelectListPanel.setTop(40);
		UserSelectListPanel.setOverflow(Overflow.AUTO);
		UserSelectListPanel.setWidth("250px");
		UserSelectListPanel.setHeight("440px");
		UserSelectListPanel.setBorder("1px solid rgb(224, 224, 224)");
		UserSelectListPanel.setBackgroundColor(Color.WHITE);
		this.add(UserSelectListPanel);
			
	}
		
	public void getparameter( Map<String, Object> parametersMap) {
		parameter = parametersMap;
		setData();
	}
	
	public void setData() {
		if(Userlisttree != null) {
			Userlisttree.clear();
		}
		
		if(UserSelecttree !=null) {
			UserSelecttree.clear();
		}
		Userlisttree = new MaterialTree();
		Userlisttree.setTextAlign(TextAlign.LEFT);
		Userlisttree.setFontSize("1.0em");
		
		UserSelecttree = new MaterialTree();
		UserSelecttree.setTextAlign(TextAlign.LEFT);
		UserSelecttree.setFontSize("1.0em");
		
		Users = (ArrayList<User>) parameter.get("Users");
		int check = 0;
		UserList = new MaterialTreeItem[Users.size()];
		SelectUsers = (ArrayList<GroupUser>) parameter.get("GroupUsers");
		UserSelectList = new MaterialTreeItem[SelectUsers.size()];
		Boolean UserSelectCheck = true;
		
		for (int i = 0; i < Users.size(); i++) {
			for (int j = 0; j < SelectUsers.size(); j++) {
			if(Users.get(i).id.equals(SelectUsers.get(j).id)) {
				check++;
			}
			}
			if(check == 0) {
			UserList[i] = new MaterialTreeItem();
			UserList[i].setTextAlign(TextAlign.LEFT);
			UserList[i].setFontSize("1.1em");
			UserList[i].setTextColor(Color.BLUE);
			UserList[i].setText(Users.get(i).name.stringValue());
			UserList[i].setIconType(IconType.PERSON);
			UserList[i].setTitle(Users.get(i).name.stringValue()+","+Users.get(i).sms.content.stringValue());

			UserList[i].addClickHandler(event->{
				String title = event.getRelativeElement().getTitle();
				String[] titles = title.split(",");
				String name = titles[0];
				String sms = titles[1];
				
				for (int j = 0; j < Users.size(); j++) {
					
					if(Users.get(j).name.stringValue().equals(name) 
					&& Users.get(j).sms.content.stringValue().equals(sms)) {
						
						ClickUser = Users.get(j);
						
					}
				}
			});
			Userlisttree.add(UserList[i]);
			} else {
			UserSelectList[i] = new MaterialTreeItem();
			UserSelectList[i].setTextAlign(TextAlign.LEFT);
			UserSelectList[i].setFontSize("1.1em");
			UserSelectList[i].setTextColor(Color.BLUE);
			UserSelectList[i].setText(Users.get(i).name.stringValue());
			UserSelectList[i].setIconType(IconType.PERSON);
			UserSelectList[i].setTitle(Users.get(i).name.stringValue()+","+Users.get(i).sms.content.stringValue());
			UserSelecttree.add(UserSelectList[i]);
			
			UserSelectList[i].addClickHandler(event->{
				String title = event.getRelativeElement().getTitle();
				String[] titles = title.split(",");
				String name = titles[0];
				String sms = titles[1];
				
				for (int j = 0; j < Users.size(); j++) {
					
					if(Users.get(j).name.stringValue().equals(name) 
					&& Users.get(j).sms.content.stringValue().equals(sms)) {
						
						ClickUser = Users.get(j);
						
					}
				}
				
			});
			UserSelectCheck = false;
			}
			
			check = 0;
		}
		UserListPanel.add(Userlisttree);
		if(UserSelectCheck == false) {
		UserSelectListPanel.add(UserSelecttree);
		}
	}
	
	
	public void InsertGroupUser() {
		
	}
	
	public void DeleteGroupUser() {
		
	}
	
}
