package kr.or.visitkorea.admin.client.manager.monitoring.panel;

import java.util.ArrayList;

import com.google.gwt.dom.client.Style.FontWeight;
import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONString;

import gwt.material.design.addins.client.combobox.MaterialComboBox;
import gwt.material.design.client.constants.Color;
import gwt.material.design.client.constants.TextAlign;
import gwt.material.design.client.ui.MaterialButton;
import gwt.material.design.client.ui.MaterialLabel;
import gwt.material.design.client.ui.MaterialTextBox;
import gwt.material.design.client.ui.MaterialToast;
import gwt.material.design.jquery.client.api.Functions.Func3;
import gwt.material.design.jscore.client.api.JSON;
import gwt.material.design.jscore.client.api.JsObject;
import kr.or.visitkorea.admin.client.manager.VisitKoreaBusiness;
import kr.or.visitkorea.admin.client.manager.monitoring.MonitoringMain;
import kr.or.visitkorea.admin.client.widgets.dialog.DialogContent;
import kr.or.visitkorea.admin.client.widgets.window.MaterialExtentsWindow;

public class GroupAddDialog extends DialogContent {

	public GroupAddDialog(MaterialExtentsWindow mw,MonitoringMain host) {
		super(mw);
		this.host = host;
	}
	private MonitoringMain host;
	private MaterialTextBox 
	Title,Level,ReadTimeOut,Url,CheckCycle,ConnectionTimeOut,CheckCount,LastDuration,Template;
	private MaterialComboBox<String> Report;
	
	@Override
	public void init() {
		Bodybuild();
		
	}

	@Override
	public int getHeight() {
		return 570;
	}

	public void Bodybuild() {
		MaterialLabel dialogTitle = new MaterialLabel("-단계 추가");
		dialogTitle.setFontSize("1.4em");
		dialogTitle.setFontWeight(FontWeight.BOLD);
		dialogTitle.setTextColor(Color.BLUE);
		dialogTitle.setPaddingTop(20);
		dialogTitle.setPaddingLeft(30);
		this.add(dialogTitle);

		MaterialLabel TitleLabel = new MaterialLabel("단계명");
		TitleLabel.setTextAlign(TextAlign.CENTER);
		TitleLabel.setWidth("100px");
		TitleLabel.setLayoutPosition(Position.ABSOLUTE);
		TitleLabel.setTop(60);
		TitleLabel.setLeft(20);
		TitleLabel.setHeight("46px");
		TitleLabel.setLineHeight(46);
		TitleLabel.setFontWeight(FontWeight.BOLD);
		TitleLabel.setBackgroundColor(Color.GREY_LIGHTEN_2);
		TitleLabel.setFontSize(13, Unit.PX);
		this.add(TitleLabel);
		
		MaterialLabel GradeLabel = new MaterialLabel("단계등급");
		GradeLabel.setTextAlign(TextAlign.CENTER);
		GradeLabel.setFontWeight(FontWeight.BOLD);
		GradeLabel.setWidth("100px");
		GradeLabel.setHeight("46px");
		GradeLabel.setLineHeight(46);
		GradeLabel.setTop(60);	
		GradeLabel.setLeft(355);
		GradeLabel.setLayoutPosition(Position.ABSOLUTE);
		GradeLabel.setBackgroundColor(Color.GREY_LIGHTEN_2);
		GradeLabel.setFontSize(13, Unit.PX);
		this.add(GradeLabel);
		
		
		Title = new MaterialTextBox();
		Title.setWidth("190px");
		Title.setHeight("46px");
		Title.setLayoutPosition(Position.ABSOLUTE);
		Title.setTop(44);
		Title.setLeft(140);
		this.add(Title);
		
		Level = new MaterialTextBox();
		Level.setWidth("190px");
		Level.setHeight("46px");
		Level.setTop(44);
		Level.setLeft(475);
		Level.setLayoutPosition(Position.ABSOLUTE);
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
		UrlLabel.setBackgroundColor(Color.GREY_LIGHTEN_2);
		this.add(UrlLabel);
		
		Url = new MaterialTextBox();
		Url.setWidth("525px");
		Url.setHeight("46px");
		Url.setLayoutPosition(Position.ABSOLUTE);
		Url.setTop(114);
		Url.setLeft(140);
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
		CheckCountLabel.setBackgroundColor(Color.GREY_LIGHTEN_2);
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
		CheckCycleLabel.setBackgroundColor(Color.GREY_LIGHTEN_2);
		CheckCycleLabel.setFontSize(13, Unit.PX);
		this.add(CheckCycleLabel);
		
		
		CheckCount = new MaterialTextBox();
		CheckCount.setWidth("190px");
		CheckCount.setHeight("46px");
		CheckCount.setLayoutPosition(Position.ABSOLUTE);
		CheckCount.setTop(184);
		CheckCount.setLeft(140);
//		CheckCount.setBackgroundColor(Color.WHITE);
		this.add(CheckCount);
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
		CheckCount.addFocusHandler(event ->{
			
		});
		CheckCycle = new MaterialTextBox();
		CheckCycle.setWidth("190px");
		CheckCycle.setHeight("46px");
		CheckCycle.setTop(184);
		CheckCycle.setLeft(475);
		CheckCycle.setPlaceholder("초 단위로 입력하세요.");
		CheckCycle.setLayoutPosition(Position.ABSOLUTE);
//		CheckCycle.setBackgroundColor(Color.WHITE);
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
		ConnectionLabel.setBackgroundColor(Color.GREY_LIGHTEN_2);
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
		ReadLabel.setBackgroundColor(Color.GREY_LIGHTEN_2);
		ReadLabel.setFontSize(13, Unit.PX);
		this.add(ReadLabel);
		
		
		ConnectionTimeOut = new MaterialTextBox();
		ConnectionTimeOut.setWidth("190px");
		ConnectionTimeOut.setHeight("46px");
		ConnectionTimeOut.setLayoutPosition(Position.ABSOLUTE);
		ConnectionTimeOut.setTop(254);
		ConnectionTimeOut.setLeft(140);
		ConnectionTimeOut.setPlaceholder("초 단위로 입력하세요.");
		
		this.add(ConnectionTimeOut);
		
		ReadTimeOut = new MaterialTextBox();
		ReadTimeOut.setWidth("190px");
		ReadTimeOut.setHeight("46px");
		ReadTimeOut.setTop(254);
		ReadTimeOut.setLeft(475);
		ReadTimeOut.setLayoutPosition(Position.ABSOLUTE);
		ReadTimeOut.setPlaceholder("초 단위로 입력하세요.");
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
		ReportLabel.setBackgroundColor(Color.GREY_LIGHTEN_2);
		ReportLabel.setFontSize(13, Unit.PX);
		this.add(ReportLabel);
		
		MaterialLabel LastDurationLabel = new MaterialLabel("마지막 단계 \n 이후 체크주기");
		LastDurationLabel.setTextAlign(TextAlign.CENTER);
		LastDurationLabel.setFontWeight(FontWeight.BOLD);
		LastDurationLabel.setWidth("100px");
		LastDurationLabel.setHeight("46px");
		LastDurationLabel.setLineHeight(23);
		LastDurationLabel.setTop(340);
		LastDurationLabel.setLeft(355);
		LastDurationLabel.setLayoutPosition(Position.ABSOLUTE);
		LastDurationLabel.setBackgroundColor(Color.GREY_LIGHTEN_2);
		LastDurationLabel.setFontSize(13, Unit.PX);
		this.add(LastDurationLabel);
		
		
		Report = new MaterialComboBox<String>();
		Report.setWidth("190px");
		Report.setHeight("46px");
		Report.setLayoutPosition(Position.ABSOLUTE);
		Report.setTop(324);
		Report.setLeft(140);
		Report.addItem("-1");
		
		this.add(Report);
		
		LastDuration = new MaterialTextBox();
		LastDuration.setWidth("190px");
		LastDuration.setHeight("46px");
		LastDuration.setTop(324);
		LastDuration.setLeft(475);
		LastDuration.setLayoutPosition(Position.ABSOLUTE);
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
		Templatelabel.setBackgroundColor(Color.GREY_LIGHTEN_2);
		this.add(Templatelabel);
		
		Template = new MaterialTextBox();
		Template.setWidth("525px");
		Template.setHeight("46px");
		Template.setLayoutPosition(Position.ABSOLUTE);
		Template.setTop(394);
		Template.setLeft(140);
		this.add(Template);
		
		
		MaterialButton savebutton = new MaterialButton("저장");
		savebutton.setLayoutPosition(Position.ABSOLUTE);
		savebutton.setRight(160);
		savebutton.setBottom(30);
		this.add(savebutton);
		savebutton.addClickHandler(event->{
			if(checktext()) {
				
			JSONObject parameterJSON = new JSONObject();
			parameterJSON.put("cmd", new JSONString("MONITORING_GROUP_INSERT"));
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
						getMaterialExtentsWindow().closeDialog();
						MaterialToast.fireToast("그룹 추가에 성공하였습니다.");
						host.clearGrouptree();
						host.readGroupName(true);
						clearData();
					} else {
						MaterialToast.fireToast("그룹 추가에 실패하였습니다");
					}
					
				}
			});
			} 
		});
		
		
		MaterialButton closebutton = new MaterialButton("취소");
		closebutton.setLayoutPosition(Position.ABSOLUTE);
		closebutton.setBottom(30);
		closebutton.setRight(40);
		this.add(closebutton);
		closebutton.addClickHandler(event->{
			getMaterialExtentsWindow().closeDialog();
		});
		
		
	}
	
	public boolean checktext() {
		if(Title.getText().matches("^[0-9]*$")) {
			MaterialToast.fireToast("단계명을 숫자만으로 입력할 수 없습니다.");
			Title.setText("");
			Title.setFocus(true);
			return false;
		} 
		
		ArrayList<String> GroupNames = (ArrayList<String>) getParameters().get("GroupNames");
		for (int i = 0; i <GroupNames.size(); i++) {
			
		if(Title.getText().equals(GroupNames.get(i))) {
			MaterialToast.fireToast("이미 존재하는 단계명입니다. ");
			Title.setText("");
			Title.setFocus(true);	
			return false;
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
	
	public void clearData() {
		Title.setText("");
		Level.setText("");
		CheckCount.setText("");
		CheckCycle.setText("");
		ConnectionTimeOut.setText("");
		ReadTimeOut.setText("");
		Url.setText("");
		Report.setSelectedIndex(0);
		LastDuration.setText("");
		Template.setText("");
	}
}
