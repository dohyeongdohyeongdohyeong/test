package kr.or.visitkorea.admin.client.manager.monitoring.panel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.google.gwt.dom.client.Style.FontWeight;
import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.dom.client.Style.VerticalAlign;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONString;

import gwt.material.design.addins.client.combobox.MaterialComboBox;
import gwt.material.design.client.constants.Color;
import gwt.material.design.client.constants.IconType;
import gwt.material.design.client.constants.TextAlign;
import gwt.material.design.client.ui.MaterialIcon;
import gwt.material.design.client.ui.MaterialLabel;
import gwt.material.design.client.ui.MaterialTextBox;
import gwt.material.design.client.ui.MaterialToast;
import gwt.material.design.jquery.client.api.Functions.Func3;
import gwt.material.design.jscore.client.api.JSON;
import gwt.material.design.jscore.client.api.JsObject;
import kr.or.visitkorea.admin.client.application.Console;
import kr.or.visitkorea.admin.client.manager.VisitKoreaBusiness;
import kr.or.visitkorea.admin.client.manager.monitoring.MonitoringMain;
import kr.or.visitkorea.admin.client.manager.monitoring.model.Group;
import kr.or.visitkorea.admin.client.manager.monitoring.model.Monitor;

public class GroupDetailPanel extends BasePanel {


	private MaterialTextBox Title,Level,ReadTimeOut,Url,CheckCycle,CheckCount,ConnectionTimeOut;
	private MaterialTextBox LastDuration;
	private MaterialComboBox<String> Report;
	private MaterialTextBox Template;

	private Map<String, Object> parameter =new HashMap<String, Object>();
	
	private Group Group;
	private Monitor Monitor;
	
	public GroupDetailPanel(MonitoringMain host) {
		super(host);
		Bodybuild();
	}

	
	
	public void Bodybuild() {
		this.setWidth("100%");
		this.setHeight("100%");
		MaterialLabel MainLabel = new MaterialLabel("단계 설정");
		MainLabel.setBackgroundColor(Color.BLUE);
		MainLabel.setPaddingLeft(10);
		MainLabel.setPaddingTop(3);
		MainLabel.setFontWeight(FontWeight.BOLD);
		MainLabel.setTextColor(Color.WHITE);
		MainLabel.setFontSize("1.1em");
		this.add(MainLabel);

		MaterialIcon GroupEdit = new MaterialIcon(IconType.EDIT);
		GroupEdit.setLineHeight(27);
		GroupEdit.setVerticalAlign(VerticalAlign.MIDDLE);
		GroupEdit.setFontSize("1.4em");
		GroupEdit.setHeight("27px");
		GroupEdit.setMargin(0);
		GroupEdit.setWidth("27px");
		GroupEdit.setLayoutPosition(Position.ABSOLUTE);
		GroupEdit.setTop(0);
		GroupEdit.setRight(0);
		GroupEdit.addClickHandler(event ->{
			Edit(false);
			
		});
		
		
		add(GroupEdit);
		
		
		MaterialIcon GroupSave = new MaterialIcon(IconType.SAVE);
		GroupSave.setLineHeight(27);
		GroupSave.setVerticalAlign(VerticalAlign.MIDDLE);
		GroupSave.setFontSize("1.4em");
		GroupSave.setHeight("27px");
		GroupSave.setMargin(0);
		GroupSave.setWidth("27px");
		GroupSave.setLayoutPosition(Position.ABSOLUTE);
		GroupSave.setTop(0);
		GroupSave.setRight(27);
		GroupSave.addClickHandler(event ->{
			if(checktext()) {
				
			Edit(true);
			JSONObject parameterJSON = new JSONObject();
			parameterJSON.put("cmd", new JSONString("MONITORING_GROUP_UPDATE"));
			parameterJSON.put("beforetitle", new JSONString(Group.Title.stringValue()));
			parameterJSON.put("title", new JSONString(Title.getText()));
			parameterJSON.put("level", new JSONString(Level.getText()));
			parameterJSON.put("url", new JSONString(Url.getText()));
			parameterJSON.put("repeat", new JSONString(CheckCount.getText()));
			parameterJSON.put("duration", new JSONString((Double.parseDouble(CheckCycle.getText()) * 1000)+""));
			parameterJSON.put("connectionTimeout", new JSONString((Double.parseDouble(ConnectionTimeOut.getText()) * 1000)+""));
			parameterJSON.put("readTimeout", new JSONString((Double.parseDouble(ReadTimeOut.getText()) * 1000)+""));
			if(Report.getSelectedIndex() == 0) {
				parameterJSON.put("report", new JSONString(-1+""));
			}else {
				parameterJSON.put("report", new JSONString(Report.getSelectedIndex()+""));
			}
			
			if(!LastDuration.getText().equals("")) {
				parameterJSON.put("LastDuration", new JSONString((Double.parseDouble(LastDuration.getText())* 1000)+""));
			} else {
				parameterJSON.put("LastDuration", new JSONString("0"));
			}
			
			parameterJSON.put("template", new JSONString(Template.getText()));
			
			
			VisitKoreaBusiness.post("call", parameterJSON.toString(), new Func3<Object, String, Object>() {
				@Override
				public void call(Object param1, String param2, Object param3) {
					
					JSONObject resultObj = (JSONObject) JSONParser.parseStrict(JSON.stringify((JsObject) param1));
					JSONObject bodyObj = resultObj.get("body").isObject();
					String result = bodyObj.get("Result").isString().stringValue();
					if(result.equals("sucess")) {
						MaterialToast.fireToast("설정 변경이 성공하였습니다");
						host.getGroupDetail(Title.getText());
						host.clearGrouptree();
						host.readGroupName(true);
						host.GradePanelClear();
						host.GroupClear();
					}else {
						MaterialToast.fireToast("설정 변경이 실패하였습니다");
					}
					
				}
			});
			
			} 
		});
		add(GroupSave);
		MaterialLabel TitleLabel = new MaterialLabel("단계명");
		TitleLabel.setTextAlign(TextAlign.CENTER);
		TitleLabel.setWidth("100px");
		TitleLabel.setLayoutPosition(Position.ABSOLUTE);
		TitleLabel.setTop(60);
		TitleLabel.setLeft(20);
		TitleLabel.setHeight("46px");
		TitleLabel.setLineHeight(46);
		TitleLabel.setFontWeight(FontWeight.BOLD);
		TitleLabel.setBackgroundColor(Color.GREY_LIGHTEN_3);
		TitleLabel.setFontSize(13, Unit.PX);
		this.add(TitleLabel);
		
		MaterialLabel GroupLabel = new MaterialLabel("단계등급");
		GroupLabel.setTextAlign(TextAlign.CENTER);
		GroupLabel.setFontWeight(FontWeight.BOLD);
		GroupLabel.setWidth("100px");
		GroupLabel.setHeight("46px");
		GroupLabel.setLineHeight(46);
		GroupLabel.setTop(60);
		GroupLabel.setLeft(355);
		GroupLabel.setLayoutPosition(Position.ABSOLUTE);
		GroupLabel.setBackgroundColor(Color.GREY_LIGHTEN_3);
		GroupLabel.setFontSize(13, Unit.PX);
		this.add(GroupLabel);
		
		
		Title = new MaterialTextBox();
		Title.setWidth("190px");
		Title.setHeight("46px");
		Title.setLayoutPosition(Position.ABSOLUTE);
		Title.setTop(44);
		Title.setLeft(140);
		Title.setReadOnly(true);
		this.add(Title);
		
		Level = new MaterialTextBox();
		Level.setWidth("190px");
		Level.setHeight("46px");
		Level.setTop(44);
		Level.setLeft(475);
		Level.setLayoutPosition(Position.ABSOLUTE);
		Level.setReadOnly(true);
//		Grade.setBackgroundColor(Color.WHITE);
		this.add(Level);
		
		
		MaterialLabel UrlLabel = new MaterialLabel("체크URL");
		UrlLabel.setFontSize(13, Unit.PX);
		UrlLabel.setTextAlign(TextAlign.CENTER);
		UrlLabel.setFontWeight(FontWeight.BOLD);
		UrlLabel.setWidth("100px");
		UrlLabel.setHeight("46px");
		UrlLabel.setLineHeight(46);
		UrlLabel.setTop(130);
		UrlLabel.setLeft(20);
		UrlLabel.setLayoutPosition(Position.ABSOLUTE);
		UrlLabel.setBackgroundColor(Color.GREY_LIGHTEN_3);
		this.add(UrlLabel);
		
		Url = new MaterialTextBox();
		Url.setWidth("525px");
		Url.setHeight("46px");
		Url.setLayoutPosition(Position.ABSOLUTE);
		Url.setTop(114);
		Url.setLeft(140);
		Url.setReadOnly(true);
//		Url.setBackgroundColor(Color.WHITE);
		this.add(Url);
		
		MaterialLabel CheckCountLabel = new MaterialLabel("체크횟수");
		CheckCountLabel.setTextAlign(TextAlign.CENTER);
		CheckCountLabel.setWidth("100px");
		CheckCountLabel.setLayoutPosition(Position.ABSOLUTE);
		CheckCountLabel.setTop(200);
		CheckCountLabel.setLeft(20);
		CheckCountLabel.setHeight("46px");
		CheckCountLabel.setLineHeight(46);
		CheckCountLabel.setFontWeight(FontWeight.BOLD);
		CheckCountLabel.setBackgroundColor(Color.GREY_LIGHTEN_3);
		CheckCountLabel.setFontSize(13, Unit.PX);
		this.add(CheckCountLabel);
		
		MaterialLabel CheckCycleLabel = new MaterialLabel("체크주기");
		CheckCycleLabel.setTextAlign(TextAlign.CENTER);
		CheckCycleLabel.setFontWeight(FontWeight.BOLD);
		CheckCycleLabel.setWidth("100px");
		CheckCycleLabel.setHeight("46px");
		CheckCycleLabel.setLineHeight(46);
		CheckCycleLabel.setTop(200);
		CheckCycleLabel.setLeft(355);
		CheckCycleLabel.setLayoutPosition(Position.ABSOLUTE);
		CheckCycleLabel.setBackgroundColor(Color.GREY_LIGHTEN_3);
		CheckCycleLabel.setFontSize(13, Unit.PX);
		this.add(CheckCycleLabel);
		
		
		CheckCount = new MaterialTextBox();
		CheckCount.setWidth("190px");
		CheckCount.setHeight("46px");
		CheckCount.setLayoutPosition(Position.ABSOLUTE);
		CheckCount.setTop(184);
		CheckCount.setLeft(140);
		CheckCount.setReadOnly(true);
		CheckCount.addKeyUpHandler(event ->{
			Report.clear();
			Report.addItem(-1+"");
			if(!CheckCount.getText().equals("")) {
				if(!CheckCount.getText().matches("^[0-9]*$")) {
					MaterialToast.fireToast("체크횟수는 숫자만 입력할 수 있습니다.");
					CheckCount.setText("");
					CheckCount.setFocus(true);
				} else {
					int CheckCount2 = Integer.parseInt(CheckCount.getText());
					if(CheckCount2>100) {
						MaterialToast.fireToast("체크횟수를 100회 이상 입력할 수 없습니다.");
						CheckCount.setText("");
						CheckCount.setFocus(true);
					} else {
						
					for (int i = 1; i < CheckCount2+1; i++) {
						Report.addItem(i+"");
						Report.setSelectedIndex(0);
					}
					}
				}
			} 
		});
		this.add(CheckCount);
		CheckCycle = new MaterialTextBox();
		CheckCycle.setWidth("190px");
		CheckCycle.setHeight("46px");
		CheckCycle.setTop(184);
		CheckCycle.setLeft(475);
		CheckCycle.setLayoutPosition(Position.ABSOLUTE);
		CheckCycle.setReadOnly(true);
		this.add(CheckCycle);
		
		
		MaterialLabel ConnectionLabel = new MaterialLabel("연결 제한시간");
		ConnectionLabel.setTextAlign(TextAlign.CENTER);
		ConnectionLabel.setWidth("100px");
		ConnectionLabel.setLayoutPosition(Position.ABSOLUTE);
		ConnectionLabel.setTop(270);
		ConnectionLabel.setLeft(20);
		ConnectionLabel.setHeight("46px");
		ConnectionLabel.setLineHeight(46);
		ConnectionLabel.setFontWeight(FontWeight.BOLD);
		ConnectionLabel.setBackgroundColor(Color.GREY_LIGHTEN_3);
		ConnectionLabel.setFontSize(13, Unit.PX);
		this.add(ConnectionLabel);
		
		MaterialLabel ReadLabel = new MaterialLabel("읽기 제한시간");
		ReadLabel.setTextAlign(TextAlign.CENTER);
		ReadLabel.setFontWeight(FontWeight.BOLD);
		ReadLabel.setWidth("100px");
		ReadLabel.setHeight("46px");
		ReadLabel.setLineHeight(46);
		ReadLabel.setTop(270);
		ReadLabel.setLeft(355);
		ReadLabel.setLayoutPosition(Position.ABSOLUTE);
		ReadLabel.setBackgroundColor(Color.GREY_LIGHTEN_3);
		ReadLabel.setFontSize(13, Unit.PX);
		this.add(ReadLabel);
		
		
		ConnectionTimeOut = new MaterialTextBox();
		ConnectionTimeOut.setWidth("190px");
		ConnectionTimeOut.setHeight("46px");
		ConnectionTimeOut.setLayoutPosition(Position.ABSOLUTE);
		ConnectionTimeOut.setTop(254);
		ConnectionTimeOut.setLeft(140);
		ConnectionTimeOut.setReadOnly(true);
		
		this.add(ConnectionTimeOut);
		
		ReadTimeOut = new MaterialTextBox();
		ReadTimeOut.setWidth("190px");
		ReadTimeOut.setHeight("46px");
		ReadTimeOut.setTop(254);
		ReadTimeOut.setLeft(475);
		ReadTimeOut.setLayoutPosition(Position.ABSOLUTE);
		ReadTimeOut.setReadOnly(true);
		this.add(ReadTimeOut);
		MaterialLabel ReportLabel = new MaterialLabel("알림 시점");
		ReportLabel.setTextAlign(TextAlign.CENTER);
		ReportLabel.setWidth("100px");
		ReportLabel.setLayoutPosition(Position.ABSOLUTE);
		ReportLabel.setTop(340);
		ReportLabel.setLeft(20);
		ReportLabel.setHeight("46px");
		ReportLabel.setLineHeight(46);
		ReportLabel.setFontWeight(FontWeight.BOLD);
		ReportLabel.setBackgroundColor(Color.GREY_LIGHTEN_3);
		ReportLabel.setFontSize(13, Unit.PX);
		this.add(ReportLabel);
		
		MaterialLabel LastDurationLabel = new MaterialLabel("마지막 단계 이후 체크주기");
		LastDurationLabel.setTextAlign(TextAlign.CENTER);
		LastDurationLabel.setFontWeight(FontWeight.BOLD);
		LastDurationLabel.setWidth("100px");
		LastDurationLabel.setHeight("46px");
		LastDurationLabel.setLineHeight(23);
		LastDurationLabel.setTop(340);
		LastDurationLabel.setLeft(355);
		LastDurationLabel.setLayoutPosition(Position.ABSOLUTE);
		LastDurationLabel.setBackgroundColor(Color.GREY_LIGHTEN_3);
		LastDurationLabel.setFontSize(13, Unit.PX);
		this.add(LastDurationLabel);
		
		
		Report = new MaterialComboBox<String>();
		Report.setWidth("190px");
		Report.setHeight("46px");
		Report.setLayoutPosition(Position.ABSOLUTE);
		Report.setTop(324);
		Report.setLeft(140);
		Report.setReadOnly(true);
		Report.setTextAlign(TextAlign.LEFT);
		Report.addItem("-1");
		this.add(Report);
		
		LastDuration = new MaterialTextBox();
		LastDuration.setWidth("190px");
		LastDuration.setHeight("46px");
		LastDuration.setTop(324);
		LastDuration.setLeft(475);
		LastDuration.setLayoutPosition(Position.ABSOLUTE);
		LastDuration.setReadOnly(true);
		this.add(LastDuration);
		
		MaterialLabel Templatelabel = new MaterialLabel("비고");
		Templatelabel.setFontSize(13, Unit.PX);
		Templatelabel.setTextAlign(TextAlign.CENTER);
		Templatelabel.setFontWeight(FontWeight.BOLD);
		Templatelabel.setWidth("100px");
		Templatelabel.setHeight("46px");
		Templatelabel.setLineHeight(46);
		Templatelabel.setTop(410);
		Templatelabel.setLeft(20);
		Templatelabel.setLayoutPosition(Position.ABSOLUTE);
		Templatelabel.setBackgroundColor(Color.GREY_LIGHTEN_3);
		this.add(Templatelabel);
		
		Template = new MaterialTextBox();
		Template.setWidth("525px");
		Template.setHeight("46px");
		Template.setLayoutPosition(Position.ABSOLUTE);
		Template.setTop(394);
		Template.setLeft(140);
		Template.setReadOnly(true);
		this.add(Template);
		
	}
	
	public void Edit(boolean readonly) {
		Title.setReadOnly(readonly);
		Level.setReadOnly(readonly);
		ReadTimeOut.setReadOnly(readonly);
		Url.setReadOnly(readonly);
		CheckCycle.setReadOnly(readonly);
		CheckCount.setReadOnly(readonly);
		ConnectionTimeOut.setReadOnly(readonly);
		LastDuration.setReadOnly(readonly);
		Report.setReadOnly(readonly);
		Template.setReadOnly(readonly);
	}



	public void getparameter( Map<String, Object> parametersMap) {
		parameter = parametersMap;
		setData();
	}
	
	public void setData() {
		
		clearData();
		
		Group = (Group) parameter.get("Group");
		Monitor = Group.Monitor;
		Title.setText(Group.Title.stringValue());
		Level.setText(Group.Level.toString());
		CheckCount.setText(Group.Repeat.toString());
		CheckCycle.setText((Double.parseDouble(Monitor.Duration.toString())/1000) +"");
		ConnectionTimeOut.setText((Double.parseDouble(Monitor.Connectiontimeout.toString())/1000) +"");
		ReadTimeOut.setText((Double.parseDouble(Monitor.Readtimeout.toString())/1000) +"");
		Url.setText(Monitor.Url.stringValue());
		Report.addItem(-1+"");
		if(!CheckCount.getText().equals("")) {
		int CheckCount2 = Integer.parseInt(CheckCount.getText());
		for (int i = 1; i < CheckCount2+1; i++) {
			Report.addItem(i+"");
		}
		} 
		if(Integer.parseInt(Group.Report+"") == -1)
		Report.setSelectedIndex(0);
		else 
		Report.setSelectedIndex(Integer.parseInt(Group.Report+""));
		
		if(Group.lastDuration != null) {
			LastDuration.setText((Double.parseDouble(Group.lastDuration.toString())/1000) +"");
		}
		Template.setText(Monitor.Template.stringValue());
		Edit(true);
	}
	
	public void clearData() {
		Title.setText("");
		Level.setText("");
		CheckCount.setText("");
		CheckCycle.setText("");
		ConnectionTimeOut.setText("");
		ReadTimeOut.setText("");
		Url.setText("");
		Report.clear();
		LastDuration.setText("");
		Template.setText("");
	}
	
	public boolean checktext() {
		if(Title.getText().matches("^[0-9]*$")) {
			MaterialToast.fireToast("단계명을 숫자만으로 입력할 수 없습니다.");
			Title.setText("");
			Title.setFocus(true);
			return false;
		} 
		
		ArrayList<String> GroupNames = (ArrayList<String>) parameter.get("GroupName");
		for (int i = 0; i <GroupNames.size(); i++) {
			if(!GroupNames.get(i).equals(Group.Title.stringValue())){
				if(Title.getText().equals(GroupNames.get(i))) {
					MaterialToast.fireToast("이미 존재하는 단계명입니다. ");
					Title.setText("");
					Title.setFocus(true);	
					return false;
				}
			} 
		}
		
		if(Title.getText().equals("")) {
			MaterialToast.fireToast("단계명을 입력해주세요. ");
			Title.setFocus(true);	
			return false;
		}
		
		if(!Level.getText().matches("^[0-9]*$")) {
			MaterialToast.fireToast("단계등급은 숫자만 입력할 수 있습니다.");
			Level.setText("");
			Level.setFocus(true);
			return false;
		}
		
		if(Level.getText().equals("")) {
			MaterialToast.fireToast("단계등급을 입력해주세요. ");
			Level.setFocus(true);	
			return false;
		}
		
		if(Url.getText().equals("")) {
			MaterialToast.fireToast("체크URL을 입력해주세요. ");
			Url.setFocus(true);	
			return false;
		}
		
		if(!CheckCount.getText().matches("^[0-9]*$")) {
			MaterialToast.fireToast("체크횟수는 숫자만 입력할 수 있습니다.");
			CheckCount.setText("");
			CheckCount.setFocus(true);
			return false;
		}
		
		if(CheckCount.getText().equals("")) {
			MaterialToast.fireToast("체크횟수를 입력해주세요. ");
			CheckCount.setFocus(true);	
			return false;
		}
		if(!CheckCycle.getText().matches("^[0-9|.]*$")) {
			MaterialToast.fireToast("체크주기는 숫자만 입력할 수 있습니다.");
			CheckCycle.setText("");
			CheckCycle.setFocus(true);
			return false;
		}
		
		if(CheckCycle.getText().equals("")) {
			MaterialToast.fireToast("체크주기를 입력해주세요. ");
			CheckCycle.setFocus(true);	
			return false;
		}
		if(!ConnectionTimeOut.getText().matches("^[0-9|.]*$")) {
			MaterialToast.fireToast("연결 제한시간은 숫자만 입력할 수 있습니다.");
			ConnectionTimeOut.setText("");
			ConnectionTimeOut.setFocus(true);
			return false;
		}
		
		if(ConnectionTimeOut.getText().equals("")) {
			MaterialToast.fireToast("연결 제한시간을 입력해주세요.");
			ConnectionTimeOut.setFocus(true);	
			return false;
		}
		if(!ReadTimeOut.getText().matches("^[0-9|.]*$")) {
			MaterialToast.fireToast("읽기 제한시간은 숫자만 입력할 수 있습니다.");
			ReadTimeOut.setText("");
			ReadTimeOut.setFocus(true);
			return false;
		}
		
		if(ReadTimeOut.getText().equals("")) {
			MaterialToast.fireToast("읽기 제한시간을 입력해주세요. ");
			ReadTimeOut.setFocus(true);	
			return false;
		}
		if(!LastDuration.getText().matches("^[0-9|.]*$")) {
			MaterialToast.fireToast("마지막 단계 이후 체크주기는 숫자만 입력할 수 있습니다.");
			LastDuration.setText("");
			LastDuration.setFocus(true);
			return false;
		}
		
		return true;
	}
}
