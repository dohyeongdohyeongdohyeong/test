package kr.or.visitkorea.admin.client.manager.repair;

import java.util.Map;

import com.google.gwt.dom.client.Style.Float;
import com.google.gwt.dom.client.Style.FontWeight;
import com.google.gwt.dom.client.Style.Overflow;
import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONString;

import gwt.material.design.client.constants.Color;
import gwt.material.design.client.ui.MaterialButton;
import gwt.material.design.client.ui.MaterialLabel;
import gwt.material.design.client.ui.MaterialPanel;
import gwt.material.design.client.ui.MaterialRow;
import gwt.material.design.client.ui.MaterialTextArea;
import gwt.material.design.client.ui.MaterialTextArea.ResizeRule;
import gwt.material.design.client.ui.MaterialTextBox;
import gwt.material.design.client.ui.MaterialToast;
import gwt.material.design.jquery.client.api.Functions.Func3;
import gwt.material.design.jscore.client.api.JSON;
import gwt.material.design.jscore.client.api.JsObject;
import kr.or.visitkorea.admin.client.application.Registry;
import kr.or.visitkorea.admin.client.manager.VisitKoreaBusiness;
import kr.or.visitkorea.admin.client.manager.widgets.FileUploadSimplePanel;
import kr.or.visitkorea.admin.client.manager.widgets.IDUtil;
import kr.or.visitkorea.admin.client.widgets.dialog.DialogContent;
import kr.or.visitkorea.admin.client.widgets.window.MaterialExtentsWindow;

public class RepairRequestAddDialog extends DialogContent {

	private MaterialLabel dialogTitle;
	private MaterialTextBox title;
	private MaterialTextBox reqfile;
	private String reqfilepath;
	private String repid = null;
	private MaterialTextArea reqbody;
	private FileUploadSimplePanel fileupload;
	
	public RepairRequestAddDialog(MaterialExtentsWindow window) {
		super(window);
	}

	@Override
	public void init() {
		buildContent();
	}

	@Override
	protected void onLoad() {
		super.onLoad();
		repid = this.getParameters().get("repid")!=null?this.getParameters().get("repid").toString():null;
		if(repid != null && !"".equals(repid)) {
			title.setText(this.getParameters().get("title")!=null?this.getParameters().get("title").toString():"");
			reqbody.setText(this.getParameters().get("reqbody")!=null?this.getParameters().get("reqbody").toString():"");
			reqfile.setText(this.getParameters().get("reqfile")!=null?this.getParameters().get("reqfile").toString():"");
		} else {
			title.setText("");
			reqbody.setText("");
			reqfile.setText("");
		}
	}

	public void buildContent() {
		addDefaultButtons();

		// dialog title define
		dialogTitle = new MaterialLabel("유지보수 요청 내용 작성"); this.add(dialogTitle);
		dialogTitle.setFontSize("1.4em");
		dialogTitle.setFontWeight(FontWeight.BOLD);
		dialogTitle.setTextColor(Color.BLUE);
		dialogTitle.setPaddingTop(10);
		dialogTitle.setPaddingLeft(30);
		
		buildBody();
	}
	

	private void buildBody() {
		MaterialPanel mpbody = new MaterialPanel(); this.add(mpbody); mpbody.setHeight("400px");
		
		MaterialRow mr1 = new MaterialRow(); mpbody.add(mr1); mr1.setGrid("s1");
		mr1.setLayoutPosition(Position.RELATIVE); mr1.setPaddingLeft(30); mr1.setPaddingRight(30); mr1.setMarginTop(20);
		
		title = new MaterialTextBox(); mr1.add(title); title.setGrid("s1");
		title.setLabel("제목");
		title.setFloat(Float.LEFT);
		title.setWidth("100%"); title.setHeight("30px"); 
		
		MaterialRow mr2 = new MaterialRow(); mpbody.add(mr2); mr2.setGrid("s1");
		mr2.setLayoutPosition(Position.RELATIVE); mr2.setPaddingLeft(30); mr2.setPaddingRight(30);
		
		reqbody = new MaterialTextArea(); mr2.add(reqbody); reqbody.setGrid("s1");
		reqbody.setLabel("내용");
		reqbody.setFloat(Float.LEFT);
		reqbody.setWidth("100%"); reqbody.setResizeRule(ResizeRule.AUTO);
		
		MaterialRow mr3 = new MaterialRow(); mpbody.add(mr3); mr3.setGrid("s1");
		mr3.setLayoutPosition(Position.RELATIVE);mr3.setPaddingLeft(30);mr3.setPaddingRight(30);
		
		fileupload = new FileUploadSimplePanel(170, 40, Registry.get("image.server").toString() + "/img/call?cmd=VIEW&id=a9158773-bd4d-49f2-a901-d3a7e204a773&chk=e1b88981-5050-4e79-989d-7006b8b41714");
		mr3.add(fileupload); fileupload.setGrid("s1"); //mfile.getUploader().setAcceptedFiles("image/*"); 
		fileupload.setMarginTop(20); 
		fileupload.setFloat(Float.LEFT);
		fileupload.getUploader().setMaxFileSize(20);
		fileupload.getUploader().addSuccessHandler(event->{
			JSONObject resultObj = (JSONObject) JSONParser.parseStrict(event.getResponse().getBody());
			String uploadValue = resultObj.get("body").isObject().get("result").isArray().get(0).isObject().get("saveName").isString().stringValue();
			reqfilepath = (String) Registry.get("image.server") + "/img/call?cmd=TEMP_VIEW&name=" + uploadValue;
			reqfile.setText(uploadValue);
		});
		fileupload.getUploader().addErrorHandler(event->{
			new MaterialToast().toast("파일 용량이 초과 또는 기타 오류가 발생했습니다. 20M 이하의 파일만 가능합니다.", 3000);
			fileupload.setEnabled(true);
		});
		reqfile = new MaterialTextBox("파일은 최대 20MB까지 첨부 가능합니다."); mr3.add(reqfile); reqfile.setGrid("s1");
		reqfile.setWidth("510px"); reqfile.setHeight("100%"); reqfile.setMarginLeft(200);
		reqfile.setFloat(Float.LEFT);
		
		mpbody.setOverflow(Overflow.AUTO);
	}
	
	@Override
	public void setParameters(Map<String, Object> parameters) {
		super.setParameters(parameters);
	}
	
	private MaterialPanel buttonAreaPanel;
	protected MaterialButton saveButton;
	protected MaterialButton closeButton;
	protected void addDefaultButtons() {
		buttonAreaPanel = new MaterialPanel(); this.add(buttonAreaPanel); 
		buttonAreaPanel.setLayoutPosition(Position.ABSOLUTE);
		buttonAreaPanel.setWidth("100%");
		buttonAreaPanel.setPaddingLeft(20);
		buttonAreaPanel.setPaddingRight(20);
		buttonAreaPanel.setLeft(0); 
		buttonAreaPanel.setBottom(20); 
		
		closeButton = new MaterialButton("닫기"); buttonAreaPanel.add(closeButton);
		closeButton.setLayoutPosition(Position.RELATIVE);
		closeButton.setFloat(com.google.gwt.dom.client.Style.Float.RIGHT);
		closeButton.setMarginLeft(20);
		closeButton.setId("close");
		closeButton.addClickHandler(event->{
			getMaterialExtentsWindow().closeDialog();
		});
		
		saveButton = new MaterialButton("저장"); buttonAreaPanel.add(saveButton);
		saveButton.setLayoutPosition(Position.RELATIVE);
		saveButton.setFloat(com.google.gwt.dom.client.Style.Float.RIGHT);
		saveButton.setId("save");
		saveButton.addClickHandler(event->{
			
			if(title.getText().trim().equals("") || reqbody.getText().trim().equals("")) {
				MaterialToast.fireToast("제목과 내용은 필수 입력항목입니다.", 3000);
				return;
			}
			JSONObject parameterJSON = new JSONObject();
			
			if(repid != null && !"".equals(repid)) {
				parameterJSON.put("cmd", new JSONString("UPDATE_REPAIR"));
			} else {
				repid = IDUtil.uuid();
				parameterJSON.put("cmd", new JSONString("INSERT_REPAIR"));
			}
//			MaterialToast.fireToast(title.getText(), 3000);
			parameterJSON.put("repid", new JSONString(repid));
			parameterJSON.put("title", new JSONString(title.getText()));
			parameterJSON.put("stfid", new JSONString(Registry.getStfId()));
			parameterJSON.put("reqbody", new JSONString(reqbody.getText()));
			if(reqfilepath != null && !reqfile.getText().equals("")) {
				parameterJSON.put("reqfile", new JSONString(reqfile.getText()));
				parameterJSON.put("reqfilepath", new JSONString(reqfilepath));
			}
			VisitKoreaBusiness.post("call", parameterJSON.toString(), new Func3<Object, String, Object>() {
				@Override
				public void call(Object param1, String param2, Object param3) {
					JSONObject resultObj = (JSONObject) JSONParser.parseStrict(JSON.stringify((JsObject)param1));
					if (((JSONObject)resultObj.get("header")).get("process").isString().stringValue().equals("success")) {
						getMaterialExtentsWindow().closeDialog();
					} else { 
						MaterialToast.fireToast("저장 실패 !", 3000);
					}
				}
			});
			if(handler != null) {
				handler.onClick(event);
			}
		});
	}

	@Override
	public int getHeight() {
		return 500;
	}
}
