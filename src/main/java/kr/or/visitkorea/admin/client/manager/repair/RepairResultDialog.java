package kr.or.visitkorea.admin.client.manager.repair;

import java.util.Map;

import com.google.gwt.dom.client.Style.Float;
import com.google.gwt.dom.client.Style.FontWeight;
import com.google.gwt.dom.client.Style.Overflow;
import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.user.client.Window;

import gwt.material.design.addins.client.combobox.MaterialComboBox;
import gwt.material.design.client.constants.Color;
import gwt.material.design.client.ui.MaterialButton;
import gwt.material.design.client.ui.MaterialLabel;
import gwt.material.design.client.ui.MaterialLink;
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
import kr.or.visitkorea.admin.client.widgets.dialog.DialogContent;
import kr.or.visitkorea.admin.client.widgets.window.MaterialExtentsWindow;

public class RepairResultDialog extends DialogContent {

	private MaterialLabel dialogTitle, dialogTitle1, dialogTitle2;
	
	private boolean bmanager;
	private MaterialRow mr2, mr3,mr4;
	private MaterialTextBox title;
	private MaterialTextBox reqman, reqdate;
	private MaterialLink reqfile, resfilelink;
	private String reqfilepath;
	private MaterialTextBox resfile;
	private String resfilepath;
	private String repid = null, remid = null;
	private MaterialTextArea reqbody;
	
	private MaterialComboBox<Object> rtype, status, repairman;
	private MaterialTextArea resbody;
	public RepairResultDialog(MaterialExtentsWindow window) {
		super(window);
	}

	@Override
	public void init() {
		buildContent();
	}

	@Override
	protected void onLoad() {
		super.onLoad();
		JSONObject parameterJSON = new JSONObject();
		parameterJSON.put("cmd", new JSONString("SELECT_REPAIRMAN_LIST"));
		repairman.clear();
		repairman.addItem("배정안됨", 0);
		remid = this.getParameters().get("remid")!=null?this.getParameters().get("remid").toString():null;
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
					if(remid != null && !"0".equals(remid))
						repairman.setSelectedIndex(repairman.getValueIndex(remid));
					else repairman.setSelectedIndex(0);
				}
			}
		});
		
		bmanager = (boolean)this.getParameters().get("manager");
//		mr2.setVisible(bmanager);
//		mr3.setVisible(bmanager);
//		mr4.setVisible(bmanager);
		saveButton.setVisible(bmanager);
		
		repid = this.getParameters().get("repid")!=null?this.getParameters().get("repid").toString():null;
		title.setText(this.getParameters().get("title")!=null?this.getParameters().get("title").toString():""); //title.setReadOnly(true);
		reqman.setText(this.getParameters().get("reqman")!=null?this.getParameters().get("reqman").toString():""); //reqman.setReadOnly(true);
		reqdate.setText(this.getParameters().get("reqdate")!=null?this.getParameters().get("reqdate").toString():""); //reqdate.setReadOnly(true);
		reqbody.setText(this.getParameters().get("reqbody")!=null?this.getParameters().get("reqbody").toString():""); //reqbody.setReadOnly(true);
		reqfile.setText(this.getParameters().get("reqfile")!=null?"첨부파일 : "+this.getParameters().get("reqfile").toString():"첨부파일 없음");
		reqfilepath = this.getParameters().get("reqfilepath")!=null?this.getParameters().get("reqfilepath").toString():"";
		reqfile.setTop(-60);
		
		if(this.getParameters().get("rtype") != null)
			rtype.setSelectedIndex((int)this.getParameters().get("rtype"));
		else rtype.setSelectedIndex(3);
		status.setSelectedIndex((int)this.getParameters().get("status"));
		String remid = this.getParameters().get("remid")!=null?this.getParameters().get("remid").toString():"";
		if(!remid.equals("")) {
			for(int i = 1;i< repairman.getValues().size();i++) {
				if(remid.equals((String)repairman.getValues().get(i))) {
					repairman.setSelectedIndex(i);
					break;
				}
					
			}
		} else repairman.setSelectedIndex(0);
		resbody.setText(this.getParameters().get("resbody")!=null?this.getParameters().get("resbody").toString():""); //reqbody.setReadOnly(true);
		resfilelink.setText(this.getParameters().get("resfile")!=null?"첨부파일 : "+this.getParameters().get("resfile").toString():"첨부파일 없음");
		resfilepath = this.getParameters().get("resfilepath")!=null?this.getParameters().get("resfilepath").toString():"";
		if(bmanager) {
			dialogTitle2.setText("처리결과 입력");
			if("".equals(resfilepath))
				mr4.setVisible(false);
			else mr4.setVisible(true);
			mr3.setVisible(true);
			rtype.setReadOnly(false);
			status.setReadOnly(false);
			repairman.setReadOnly(false);
			resbody.setReadOnly(false);
		} else {
			dialogTitle2.setText("처리결과");
			mr3.setVisible(false);
//			mr4.setVisible(true);
			rtype.setReadOnly(true);
			status.setReadOnly(true);
			repairman.setReadOnly(true);
			resbody.setReadOnly(true);
		}
	}

	public void buildContent() {
		addDefaultButtons();

		// dialog title define
		dialogTitle = new MaterialLabel("유지보수 요청"); this.add(dialogTitle);
		dialogTitle.setFontSize("1.4em");
		dialogTitle.setFontWeight(FontWeight.BOLD);
		dialogTitle.setTextColor(Color.BLUE);
		dialogTitle.setPaddingTop(10);
		dialogTitle.setPaddingLeft(30);
		
		buildBody();
	}

	private void buildBody() {
		MaterialPanel mpbody = new MaterialPanel(); this.add(mpbody); mpbody.setHeight("500px");
		
		MaterialRow mr1 = new MaterialRow(); mpbody.add(mr1); mr1.setGrid("s1");
		mr1.setLayoutPosition(Position.RELATIVE); mr1.setPaddingLeft(30); mr1.setPaddingRight(30); mr1.setMarginTop(20);
		dialogTitle1 = new MaterialLabel("요청 내용"); mr1.add(dialogTitle1);
		dialogTitle1.setWidth("100%"); dialogTitle1.setMarginBottom(10);
		dialogTitle1.setFontSize("1.2em");
		dialogTitle1.setFontWeight(FontWeight.BOLD);
		dialogTitle1.setTextColor(Color.GREY_DARKEN_1);
		dialogTitle1.setPaddingTop(10);
		
		title = new MaterialTextBox(); mr1.add(title); title.setGrid("s1");
		title.setLabel("제목"); title.setReadOnly(true); 
		title.setFloat(Float.LEFT);
		title.setWidth("100%"); title.setHeight("30px"); 
		
		reqman = new MaterialTextBox(); mr1.add(reqman); reqman.setGrid("s1");
		reqman.setLabel("요청자"); reqman.setFloat(Float.LEFT); reqman.setReadOnly(true); 
		reqman.setWidth("50%"); reqman.setTop(-20);
		reqdate = new MaterialTextBox(); mr1.add(reqdate); reqdate.setGrid("s1");
		reqdate.setLabel("요청일"); reqdate.setFloat(Float.LEFT); reqdate.setReadOnly(true); 
		reqdate.setWidth("50%"); reqdate.setTop(-20);
		
		reqbody = new MaterialTextArea(); mr1.add(reqbody); reqbody.setGrid("s1");
		reqbody.setLabel("내용"); reqbody.setFloat(Float.LEFT); reqbody.setTop(-40); reqbody.setReadOnly(true); 
		reqbody.setWidth("100%"); reqbody.setResizeRule(ResizeRule.AUTO);
		
		reqfile = new MaterialLink(); mr1.add(reqfile); reqfile.setGrid("s1");
		reqfile.setFloat(Float.LEFT); reqfile.setMarginBottom(20);
		reqfile.setWidth("100%");
		reqfile.addClickHandler(event->{
			if(reqfilepath != null && !"".equals(reqfilepath))
				Window.open(reqfilepath, "", "");
		});
		
		
		mr2 = new MaterialRow(); mpbody.add(mr2); mr2.setGrid("s1");
		mr2.setLayoutPosition(Position.RELATIVE);mr2.setPaddingLeft(30);mr2.setPaddingRight(30); //mr2.setTop(-30);
		
		dialogTitle2 = new MaterialLabel("처리결과 입력"); mr2.add(dialogTitle2);
		dialogTitle2.setWidth("100%"); dialogTitle2.setMarginBottom(10);
		dialogTitle2.setFontSize("1.2em");
		dialogTitle2.setFontWeight(FontWeight.BOLD);
		dialogTitle2.setTextColor(Color.GREY_DARKEN_1);
		dialogTitle2.setPaddingTop(10);
		
		rtype = new MaterialComboBox<>(); mr2.add(rtype);
		rtype.setLabel("요청분류"); rtype.setWidth("30%");rtype.setFloat(Float.LEFT);rtype.setMarginLeft(5);rtype.setGrid("s1");
		rtype.addItem("신규요청", 0);
		rtype.addItem("기능오류", 1);
		rtype.addItem("수정요청", 2);
		rtype.addItem("지정안됨", 3);
		rtype.setSelectedIndex(3); 
		
		status = new MaterialComboBox<>(); mr2.add(status);
		status.setLabel("진행상태"); status.setWidth("30%");status.setFloat(Float.LEFT); status.setMarginLeft(20);status.setGrid("s1");
		status.addItem("접수", 0);
		status.addItem("처리중", 1);
		status.addItem("처리완료", 2);
		status.addItem("처리불가", 3);
		status.setSelectedIndex(0);
		
		repairman = new MaterialComboBox<>(); mr2.add(repairman);
		repairman.setLabel("처리담당자"); repairman.setWidth("30%"); repairman.setFloat(Float.LEFT); repairman.setMarginLeft(20);repairman.setGrid("s1");
		repairman.addItem("배정안됨", 0);
		
		resbody = new MaterialTextArea(); mr2.add(resbody); resbody.setGrid("s1");
		resbody.setLabel("내용"); resbody.setFloat(Float.LEFT);
		resbody.setWidth("100%"); resbody.setResizeRule(ResizeRule.AUTO);
		
		mr3 = new MaterialRow(); mpbody.add(mr3); mr3.setGrid("s1");
		mr3.setLayoutPosition(Position.RELATIVE);mr3.setPaddingLeft(30);mr3.setPaddingRight(30); //mr2.setTop(-30);
		
		FileUploadSimplePanel fileupload = new FileUploadSimplePanel(170, 40, Registry.get("image.server").toString() + "/img/call?cmd=VIEW&id=a9158773-bd4d-49f2-a901-d3a7e204a773&chk=e1b88981-5050-4e79-989d-7006b8b41714");
		mr3.add(fileupload); fileupload.setGrid("s1"); //mfile.getUploader().setAcceptedFiles("image/*"); 
		fileupload.setMarginTop(20); fileupload.setFloat(Float.LEFT);
		fileupload.getUploader().setMaxFileSize(20);
		fileupload.getUploader().addSuccessHandler(event->{
			JSONObject resultObj = (JSONObject) JSONParser.parseStrict(event.getResponse().getBody());
			String uploadValue = resultObj.get("body").isObject().get("result").isArray().get(0).isObject().get("saveName").isString().stringValue();
			resfilepath = (String) Registry.get("image.server") + "/img/call?cmd=TEMP_VIEW&name=" + uploadValue;
			resfile.setText(uploadValue);
		});
		fileupload.getUploader().addErrorHandler(event->{
			new MaterialToast().toast("파일 용량이 초과 또는 기타 오류가 발생했습니다. 20M 이하의 파일만 가능합니다.", 3000);
			fileupload.setEnabled(true);
		});
		
		resfile = new MaterialTextBox("파일은 최대 20MB까지 첨부 가능합니다."); mr3.add(resfile); resfile.setGrid("s1");
		resfile.setWidth("510px"); resfile.setHeight("100%"); resfile.setMarginLeft(200);
		resfile.setFloat(Float.LEFT);
		mr3.setVisible(false);
		
		mr4 = new MaterialRow(); mpbody.add(mr4); mr4.setGrid("s1");
		mr4.setLayoutPosition(Position.RELATIVE);mr4.setPaddingLeft(30);mr4.setPaddingRight(30); //mr2.setTop(-30);
		resfilelink = new MaterialLink(); mr4.add(resfilelink); resfilelink.setGrid("s1");
		resfilelink.setFloat(Float.LEFT); resfilelink.setMarginBottom(20);
		resfilelink.setWidth("100%");
		resfilelink.addClickHandler(event->{
			if(resfilepath != null && !"".equals(resfilepath))
				Window.open(resfilepath, "", "");
		});
//		mr4.setVisible(false);
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
			
			parameterJSON.put("cmd", new JSONString("UPDATE_REPAIR"));
			parameterJSON.put("repid", new JSONString(repid));
			if(rtype.getSelectedIndex() < 3)
				parameterJSON.put("rtype", new JSONString(rtype.getSelectedIndex()+""));
			if(status.getSelectedIndex() >= 0)
				parameterJSON.put("status", new JSONString(status.getSelectedIndex()+""));
			if(repairman.getSelectedIndex() > 0)
				parameterJSON.put("remid", new JSONString((String)repairman.getValues().get(repairman.getSelectedIndex())));
			if(!resbody.getText().equals(""))
				parameterJSON.put("resbody", new JSONString(resbody.getText()));
			if(resfilepath != null && !resfilepath.equals("") && !resfile.getText().equals("")) {
				parameterJSON.put("resfile", new JSONString(resfile.getText()));
				parameterJSON.put("resfilepath", new JSONString(resfilepath));
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
		return 600;
	}
}
