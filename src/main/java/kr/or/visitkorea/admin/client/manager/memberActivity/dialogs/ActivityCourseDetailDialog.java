package kr.or.visitkorea.admin.client.manager.memberActivity.dialogs;


import java.util.Date;
import java.util.Map;

import com.google.gwt.dom.client.Style.FontWeight;
import com.google.gwt.dom.client.Style.Overflow;
import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.json.client.JSONNumber;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.user.client.ui.ScrollPanel;

import gwt.material.design.client.constants.Color;
import gwt.material.design.client.ui.MaterialButton;
import gwt.material.design.client.ui.MaterialLabel;
import gwt.material.design.client.ui.MaterialPanel;
import gwt.material.design.client.ui.MaterialRadioButton;
import gwt.material.design.client.ui.MaterialTextArea;
import gwt.material.design.client.ui.MaterialTextBox;
import gwt.material.design.client.ui.MaterialToast;
import gwt.material.design.jquery.client.api.Functions.Func3;
import gwt.material.design.jscore.client.api.JSON;
import gwt.material.design.jscore.client.api.JsObject;
import kr.or.visitkorea.admin.client.application.Console;
import kr.or.visitkorea.admin.client.manager.VisitKoreaBusiness;
import kr.or.visitkorea.admin.client.manager.memberActivity.MemberActivityMain;
import kr.or.visitkorea.admin.client.manager.widgets.ContentTableRow;
import kr.or.visitkorea.admin.client.manager.widgets.ScrollContentPanel;
import kr.or.visitkorea.admin.client.widgets.dialog.DialogContent;
import kr.or.visitkorea.admin.client.widgets.window.MaterialExtentsWindow;

public class ActivityCourseDetailDialog extends DialogContent {	
	protected MemberActivityMain host;
	MaterialExtentsWindow window;
	
	private ScrollPanel scPanel;
	private ScrollContentPanel scpPanel;
	private MaterialTextBox csTitle;
	private MaterialTextBox csCount;
	private MaterialTextBox csWriter;
	private MaterialTextBox csCreateDate;
	private MaterialTextBox csUpdateDate;
	private MaterialTextArea csDesc;
	private MaterialRadioButton csOpen, csNotOpen;
	private MaterialPanel cp;
	private MaterialTextBox csShareCount;
	private MaterialButton csSave;
	private String crsId;
	private int isOpen;
	
	public ActivityCourseDetailDialog(MaterialExtentsWindow window, MemberActivityMain host) {
		super(window);
		this.host = host;
	}

	@Override
	public void init() {
	}

	@Override
	protected void onLoad() {
		super.onLoad();
		this.clear();
		
		buildContent();
		bindDataset();
	}
	
	private void bindDataset() {
		if (this.getParameters().get("crsid") != null && this.getParameters().get("crsid") != "")
			crsId = this.getParameters().get("crsid").toString();
		if (this.getParameters().get("title") != null && this.getParameters().get("title") != "")
			csTitle.setValue(this.getParameters().get("title").toString());
		if (this.getParameters().get("cnt") != null && this.getParameters().get("cnt") != "")
			csCount.setValue(this.getParameters().get("cnt").toString() + " 코스");
		if (this.getParameters().get("id") != null && this.getParameters().get("id") != "")
			csWriter.setValue(this.getParameters().get("id").toString());
		if (this.getParameters().get("date") != null && this.getParameters().get("date") != "")
			csCreateDate.setValue(this.getParameters().get("date").toString());
		if (this.getParameters().get("updateDate") != null && this.getParameters().get("updateDate") != "")
			csUpdateDate.setValue(this.getParameters().get("updateDate").toString());
		else
			csUpdateDate.setValue(this.getParameters().get("date").toString());
		if (this.getParameters().get("shareCnt") != null && this.getParameters().get("shareCnt") != "")
			csShareCount.setValue(this.getParameters().get("shareCnt").toString() + " 건");
		if (this.getParameters().get("desc") != null && this.getParameters().get("desc") != "")
			csDesc.setValue(this.getParameters().get("desc").toString());
		if (this.getParameters().get("isOpen") != null && this.getParameters().get("isOpen") != "")
			if (this.getParameters().get("isOpen").toString().equals("0"))
				csNotOpen.setValue(true);
			else
				csOpen.setValue(true);
	}
	
	public void buildContent() {
		MaterialLabel dialogTitle = new MaterialLabel("회원관리 : 활동관리");
		dialogTitle.setFontSize("1.4em");
		dialogTitle.setFontWeight(FontWeight.BOLD);
		dialogTitle.setTextColor(Color.BLUE);
		dialogTitle.setPaddingTop(10);
		dialogTitle.setPaddingLeft(30);
		this.add(dialogTitle);
		
		buildDetailTab();
	}
	
	private void buildDetailTab() {
		
		scPanel = new ScrollPanel();
		scPanel.setHeight("100%");
		this.add(scPanel);
		
		scpPanel = new ScrollContentPanel(this);
		scpPanel.setWidth("100%");
		scpPanel.setBackgroundColor(Color.WHITE);
		scpPanel.setOverflow(Overflow.HIDDEN);
		scPanel.add(scpPanel);
		
		csTitle = new MaterialTextBox();
		csTitle.setLayoutPosition(Position.ABSOLUTE);
		csTitle.setLabel("코스제목");
		csTitle.setTop(40); 
		csTitle.setLeft(15);
		csTitle.setWidth("200px");
		csTitle.setHeight("35px");
		csTitle.setReadOnly(true);
		csTitle.setFontSize("18px");
		scpPanel.add(csTitle);
		
		csCount = new MaterialTextBox();
		csCount.setLayoutPosition(Position.ABSOLUTE);
		csCount.setLabel("코스수");
		csCount.setTop(110); 
		csCount.setLeft(15);
		csCount.setWidth("200px");
		csCount.setHeight("35px");
		csCount.setReadOnly(true);
		csCount.setFontSize("18px");
		scpPanel.add(csCount);
		
		csWriter = new MaterialTextBox();
		csWriter.setLayoutPosition(Position.ABSOLUTE);
		csWriter.setLabel("작성자");
		csWriter.setTop(110); 
		csWriter.setLeft(600);
		csWriter.setWidth("200px");
		csWriter.setHeight("35px");
		csWriter.setReadOnly(true);
		csWriter.setFontSize("18px");
		scpPanel.add(csWriter);
		
		csCreateDate = new MaterialTextBox();
		csCreateDate.setLayoutPosition(Position.ABSOLUTE);
		csCreateDate.setLabel("등록일시");
		csCreateDate.setTop(180); 
		csCreateDate.setLeft(15);
		csCreateDate.setWidth("200px");
		csCreateDate.setHeight("35px");
		csCreateDate.setReadOnly(true);
		csCreateDate.setFontSize("18px");
		scpPanel.add(csCreateDate);
		
		csUpdateDate = new MaterialTextBox();
		csUpdateDate.setLayoutPosition(Position.ABSOLUTE);
		csUpdateDate.setLabel("수정일시");
		csUpdateDate.setTop(180); 
		csUpdateDate.setLeft(600);
		csUpdateDate.setWidth("200px");
		csUpdateDate.setHeight("35px");
		csUpdateDate.setReadOnly(true);
		csUpdateDate.setFontSize("18px");
		scpPanel.add(csUpdateDate);
		
		csDesc = new MaterialTextArea();
		csDesc.setLayoutPosition(Position.ABSOLUTE);
		csDesc.setLabel("코스설명");
		csDesc.setTop(250);
		csDesc.setLeft(15);
		csDesc.setWidth("1000px");
		csDesc.getWidget(0).getElement().getStyle().setProperty("maxHeight", "70px");
		csDesc.getWidget(0).getElement().getStyle().setProperty("overflow", "auto");
		scpPanel.add(csDesc);
		
		csOpen = new MaterialRadioButton("open");
		csNotOpen = new MaterialRadioButton("open");
		csOpen.setText("공개");
		csNotOpen.setText("비공개");
		csNotOpen.setValue(true, false);
		
		cp = new MaterialPanel();
		cp.setLayoutPosition(Position.ABSOLUTE);
		cp.setTop(400);
		cp.setLeft(15);
		cp.setWidth("200px");
		cp.setHeight("70px");
		cp.add(csOpen);
		cp.add(csNotOpen);
		scpPanel.add(cp);
		
		csShareCount = new MaterialTextBox();
		csShareCount.setLayoutPosition(Position.ABSOLUTE);
		csShareCount.setLabel("공유건수");
		csShareCount.setTop(400); 
		csShareCount.setLeft(600);
		csShareCount.setWidth("200px");
		csShareCount.setHeight("35px");
		csShareCount.setReadOnly(true);
		csShareCount.setFontSize("18px");
		scpPanel.add(csShareCount);
		
		addDefaultButtons();
		
		csSave = new MaterialButton("저장");
		csSave.setBackgroundColor(Color.RED_LIGHTEN_2);
		this.addButton(csSave);
		csSave.addClickHandler(event -> {
			
			isOpen = 0;
			if (csOpen.getValue())
				isOpen = 1;
			
			JSONObject parameterJSON = new JSONObject();
			parameterJSON.put("cmd", new JSONString("UPDATE_SNS_COURSE_DETAIL"));
			parameterJSON.put("crsId", new JSONString(crsId));
			parameterJSON.put("isOpen", new JSONNumber(isOpen));
			parameterJSON.put("desc", new JSONString(csDesc.getValue()));
			
			VisitKoreaBusiness.post("call", parameterJSON.toString(), new Func3<Object, String, Object>() {

				@Override
				public void call(Object param1, String param2, Object param3) {

					JSONObject resultObj = (JSONObject) JSONParser.parseStrict(JSON.stringify((JsObject)param1));
					JSONObject headerObj = (JSONObject) resultObj.get("header");
					String processResult = headerObj.get("process").isString().toString();
					processResult = processResult.replaceAll("\"", "");
					
					if (processResult.equals("success")) {
						
						JSONObject obj = (JSONObject) getParameters().get("obj");
						obj.put("desc", new JSONString(csDesc.getValue()));
						
						ContentTableRow row = (ContentTableRow) getParameters().get("row");
						MaterialLabel rowUpdateUpdateDate = (MaterialLabel) row.getColumnObject(5);
						Date date = new Date();
						String currentDate = DateTimeFormat.getFormat("yyyy-MM-dd HH:mm:ss").format(date);
						rowUpdateUpdateDate.setText(currentDate + ".0");
						
						MaterialLabel rowUpdateIsOpen = (MaterialLabel) row.getColumnObject(7);
						rowUpdateIsOpen.setText(
								isOpen == 0 ? "비공개" : "공개"
								);
						
						getMaterialExtentsWindow().closeDialog();
						
						MaterialToast.fireToast("변경 사항이 저장 되었습니다.", 5000);
					} else {
						getMaterialExtentsWindow().alert("상태변경에 실패했습니다. 관리자에게 문의하세요.", 500);
					}
				}
			});
		});
		
	}
	@Override
	public void setParameters(Map<String, Object> parameters) {
		super.setParameters(parameters);
	}
	
	@Override
	public int getHeight() {
		return 600;
	}
}
