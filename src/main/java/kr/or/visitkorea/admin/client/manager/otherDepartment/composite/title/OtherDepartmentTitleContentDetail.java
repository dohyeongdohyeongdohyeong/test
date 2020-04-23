package kr.or.visitkorea.admin.client.manager.otherDepartment.composite.title;

import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONString;

import gwt.material.design.client.constants.Color;
import gwt.material.design.client.constants.IconType;
import gwt.material.design.client.constants.TextAlign;
import gwt.material.design.client.ui.MaterialIcon;
import gwt.material.design.client.ui.MaterialLabel;
import gwt.material.design.client.ui.MaterialLink;
import gwt.material.design.client.ui.MaterialPanel;
import gwt.material.design.client.ui.MaterialRow;
import gwt.material.design.client.ui.MaterialTextArea;
import gwt.material.design.client.ui.MaterialTextBox;
import kr.or.visitkorea.admin.client.application.Console;
import kr.or.visitkorea.admin.client.manager.otherDepartment.composite.AbtractOtherDepartmentMainContents;
import kr.or.visitkorea.admin.client.manager.otherDepartment.composite.OtherDepartmentMainEditor;
import kr.or.visitkorea.admin.client.manager.otherDepartment.widgets.AreaComponent;
import kr.or.visitkorea.admin.client.manager.widgets.ContentTable;
import kr.or.visitkorea.admin.client.manager.widgets.SelectionPanel;
import kr.or.visitkorea.admin.client.widgets.window.MaterialExtentsWindow;

public class OtherDepartmentTitleContentDetail extends AbtractOtherDepartmentMainContents {

	private MaterialPanel panel;
	private MaterialTextBox sectionTitle;
	private SelectionPanel selectStatus;
	private SelectionPanel selectStatus1;
	private MaterialLabel tag1;
	private ContentTable table;
	private AreaComponent areaCompo;
	private OtherDepartmentMainEditor otherDepartmentMainEditor;
	private String COMP_ID;
	private MaterialIcon courseIcon5;
	private MaterialTextArea contentTextArea;
	private MaterialLabel countLabel;
	private MaterialIcon saveIcon;

	public OtherDepartmentTitleContentDetail(MaterialExtentsWindow materialExtentsWindow, OtherDepartmentMainEditor otherDepartmentMainEditor) {
		
		super(materialExtentsWindow);
		this.otherDepartmentMainEditor = otherDepartmentMainEditor;
	}

	@Override
	public void init() {
		
		super.init();
		
		this.setLayoutPosition(Position.RELATIVE);
		this.setHeight("100%");
		this.setTitle("문구 영역 상세");
		
		buildContent();
	}

	private void buildContent() {

		panel =  new MaterialPanel();
		panel.setPadding(20);
		this.add(panel);
		
		MaterialLink headLink = this.addLink(new MaterialLink());
		headLink.setLayoutPosition(Position.ABSOLUTE);
		headLink.setTooltip("서버 반영");
		headLink.setTop(5);
		headLink.setRight(0);
		headLink.setIconType(IconType.SAVE);
		headLink.setIconColor(Color.WHITE);
		headLink.addClickHandler(event -> {
			JSONObject jObj = new JSONObject();
			jObj.put("cmd", new JSONString("UPDATE_COMP_CONNECT_MORE"));
			jObj.put("CONNECT_MORE", new JSONString(this.contentTextArea.getText()));
			jObj.put("COMP_ID", new JSONString(this.areaCompo.getCOMP_ID()));
			jObj.put("ODM_ID", new JSONString(this.areaCompo.getODM_ID()));
			
			this.areaCompo.getInfo().get(0).put("CONNECT_MORE", jObj.get("CONNECT_MORE"));
			executeBusiness(jObj);
		});
		
		//첫 줄
		MaterialRow row1 = addRow(panel);
		this.countLabel = addLabel(row1, "내용 입력 [0]", TextAlign.CENTER, Color.GREY_LIGHTEN_3,"s3");
		this.contentTextArea = addTextArea(row1, "", TextAlign.CENTER, "s9");
		this.contentTextArea.addKeyUpHandler(event->{
			countLabel.setText("내용 입력 [ " + this.contentTextArea.getText().length() + " ]");
		});
	}

	@Override
	public void loading(boolean loadFlag) {
		super.loading(loadFlag);
	}

	@Override
	public void setReadOnly(boolean readFlag) {}

	public void setAreaComponent(AreaComponent aac) {
		
		if (!aac.equals(this.areaCompo)) this.areaCompo = aac;
		
		Console.log("this.areaCompo.getInfo().get(0).get(\"CONNECT_MORE\") :: " + this.areaCompo.getInfo().get(0).get("CONNECT_MORE"));
		
		if (this.areaCompo.getInfo().get(0).get("CONNECT_MORE") != null) {
			this.contentTextArea.setText("");
			countLabel.setText("내용 입력 [0]");
			String connMoreValue = this.areaCompo.getInfo().get(0).get("CONNECT_MORE").isString().stringValue();
			if (connMoreValue.length() > 0) {
				this.contentTextArea.setText(connMoreValue);
				countLabel.setText("내용 입력 [ " + this.contentTextArea.getText().length() + " ]");
			}
		}else {
			this.contentTextArea.setText("");
			countLabel.setText("내용 입력 [0]");
		}

	}

	@Override
	public void loadData() {
		
	}

}