package kr.or.visitkorea.admin.client.manager.contents.department.dialogs;

import java.util.Map;

import com.google.gwt.dom.client.Style.Float;
import com.google.gwt.dom.client.Style.FontWeight;
import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.user.client.Timer;

import gwt.material.design.client.constants.Color;
import gwt.material.design.client.ui.MaterialButton;
import gwt.material.design.client.ui.MaterialInput;
import gwt.material.design.client.ui.MaterialLabel;
import gwt.material.design.client.ui.MaterialPanel;
import gwt.material.design.client.ui.MaterialRow;
import gwt.material.design.jquery.client.api.Functions.Func3;
import kr.or.visitkorea.admin.client.manager.VisitKoreaBusiness;
import kr.or.visitkorea.admin.client.manager.widgets.IDUtil;
import kr.or.visitkorea.admin.client.widgets.dialog.DialogContent;
import kr.or.visitkorea.admin.client.widgets.window.MaterialExtentsWindow;

public class DepartmentInfoDialog extends DialogContent {

	private MaterialLabel dialogTitle,mlresultmsg;
	private MaterialInput name, tel;

	public DepartmentInfoDialog(MaterialExtentsWindow window) {
		super(window);
		name.setText("");
		tel.setText("");
	}

	@Override
	public void init() {
		addDefaultButtons();

		// dialog title define
		dialogTitle = new MaterialLabel("신규 부서 등록");
		dialogTitle.setFontSize("1.4em");
		dialogTitle.setFontWeight(FontWeight.BOLD);
		dialogTitle.setTextColor(Color.BLUE);
		dialogTitle.setPaddingTop(10);
		dialogTitle.setPaddingLeft(30);
		this.add(dialogTitle);
		mlresultmsg = new MaterialLabel("");
		mlresultmsg.setLayoutPosition(Position.ABSOLUTE);
		mlresultmsg.setFontSize("1.2em");
		mlresultmsg.setFontWeight(FontWeight.BOLD);
		mlresultmsg.setTextColor(Color.GREY_DARKEN_3);
		mlresultmsg.setTop(10);
		mlresultmsg.setLeft(200);
		this.add(mlresultmsg);
		
		MaterialRow mr1 = new MaterialRow();
		mr1.setPadding(50);
		mr1.setWidth("100%");
		MaterialLabel mlname = new MaterialLabel("부서명");
		mlname.setFloat(Float.LEFT);
		mlname.setHeight("44px");
		mlname.setMarginTop(10);
		mr1.add(mlname);
		name = new MaterialInput();
		name.setFloat(Float.RIGHT);
		name.setWidth("300px");
		mr1.add(name);
		this.add(mr1);
		
		MaterialRow mr2 = new MaterialRow();
		mr2.setPadding(50);
		mr2.setWidth("100%");
		MaterialLabel mltel = new MaterialLabel("연락처");
		mltel.setFloat(Float.LEFT);
		mltel.setHeight("44px");
		mltel.setMarginTop(10);
		mr2.add(mltel);
		tel = new MaterialInput();
		tel.setFloat(Float.RIGHT);
		tel.setWidth("300px");
		mr2.add(tel);
		this.add(mr2);
		
	}

	@Override
	protected void onLoad() {
		super.onLoad();
	}
	
	private MaterialPanel buttonAreaPanel;
	protected MaterialButton saveButton;
	protected MaterialButton closeButton;
	protected void addDefaultButtons() {
		buttonAreaPanel = new MaterialPanel();
		buttonAreaPanel.setLayoutPosition(Position.ABSOLUTE);
		buttonAreaPanel.setWidth("100%");
		buttonAreaPanel.setPaddingLeft(20);
		buttonAreaPanel.setPaddingRight(20);
		buttonAreaPanel.setLeft(0); 
		buttonAreaPanel.setBottom(20); 
		
		closeButton = new MaterialButton("닫기");
		closeButton.setLayoutPosition(Position.RELATIVE);
		closeButton.setFloat(com.google.gwt.dom.client.Style.Float.RIGHT);
		closeButton.setMarginLeft(20);
		closeButton.setId("close");
		closeButton.addClickHandler(event->{
			getMaterialExtentsWindow().closeDialog();
		});
		buttonAreaPanel.add(closeButton);
		
		saveButton = new MaterialButton("저장");
		saveButton.setLayoutPosition(Position.RELATIVE);
		saveButton.setFloat(com.google.gwt.dom.client.Style.Float.RIGHT);
		saveButton.setId("save");
		saveButton.addClickHandler(event->{
			if(name.getText().trim().equals("") || tel.getText().trim().equals("")) {
				mlresultmsg.setText("부서 수정 기능이 없습니다. 부서명과 연락처를 정확히 기입하세요!");
				Timer t = new Timer() {
					@Override
					public void run() {
						mlresultmsg.setText("");
					}
				};t.schedule(3000);
				return;
			}
			JSONObject parameterJSON = new JSONObject();
			parameterJSON.put("cmd", new JSONString("INSERT_DEPARTMENT_INFO"));
			parameterJSON.put("depid", new JSONString(IDUtil.uuid()));
			parameterJSON.put("name", new JSONString(name.getText().trim()));
			parameterJSON.put("tel", new JSONString(tel.getText().trim()));
			VisitKoreaBusiness.post("call", parameterJSON.toString(), new Func3<Object, String, Object>() {
				@Override
				public void call(Object param1, String param2, Object param3) {}
			});
			
			if(handler != null) {
				handler.onClick(event);
			}
			getMaterialExtentsWindow().closeDialog();
		});
		buttonAreaPanel.add(saveButton);
		this.add(buttonAreaPanel); 
	}

	@Override
	public void setParameters(Map<String, Object> parameters) {
		super.setParameters(parameters);
	}

	@Override
	public int getHeight() {
		return 520;
	}

}
