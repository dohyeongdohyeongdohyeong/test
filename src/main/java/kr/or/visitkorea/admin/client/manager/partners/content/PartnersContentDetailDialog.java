package kr.or.visitkorea.admin.client.manager.partners.content;

import java.util.Map;

import com.google.gwt.dom.client.Style.Float;
import com.google.gwt.dom.client.Style.FontWeight;
import com.google.gwt.dom.client.Style.Overflow;
import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.ScrollPanel;

import gwt.material.design.addins.client.combobox.MaterialComboBox;
import gwt.material.design.client.constants.Color;
import gwt.material.design.client.ui.MaterialButton;
import gwt.material.design.client.ui.MaterialLabel;
import gwt.material.design.client.ui.MaterialPanel;
import gwt.material.design.client.ui.MaterialRow;
import gwt.material.design.client.ui.MaterialTextArea;
import gwt.material.design.client.ui.MaterialTextBox;
import gwt.material.design.client.ui.MaterialToast;
import gwt.material.design.jquery.client.api.Functions.Func3;
import gwt.material.design.jscore.client.api.JSON;
import gwt.material.design.jscore.client.api.JsObject;
import kr.or.visitkorea.admin.client.application.Console;
import kr.or.visitkorea.admin.client.application.Registry;
import kr.or.visitkorea.admin.client.manager.VisitKoreaBusiness;
import kr.or.visitkorea.admin.client.manager.widgets.ScrollContentPanel;
import kr.or.visitkorea.admin.client.widgets.dialog.DialogContent;
import kr.or.visitkorea.admin.client.widgets.window.MaterialExtentsWindow;

public class PartnersContentDetailDialog extends DialogContent {

	private MaterialLabel dialogTitle, mlresultmsg;
	private MaterialTextBox title, name, ctype, downurl, inputid, udate, cdate, usage, applytype;
	private MaterialTextArea companion;
	private MaterialComboBox<Object> status, dnoffer;
	private String pacid, cotid;
	private MaterialTextArea etcqna;
	public PartnersContentDetailDialog(MaterialExtentsWindow window) {
		super(window);
	}

	@Override
	public void init() {
		buildContent();
	}

	@Override
	protected void onLoad() {
		super.onLoad();
		title.setText(this.getParameters().get("title")!=null?this.getParameters().get("title").toString():"");
		ctype.setText(Registry.getContentType((int)this.getParameters().get("ctype")));
		name.setText(this.getParameters().get("name")!=null?this.getParameters().get("name").toString():"");
		udate.setText(this.getParameters().get("udate")!=null?Registry.getConvertDateTime(this.getParameters().get("udate").toString()):"");
		inputid.setText(this.getParameters().get("id")!=null?this.getParameters().get("id").toString():"");
		cdate.setText(this.getParameters().get("cdate")!=null?Registry.getConvertDateTime(this.getParameters().get("cdate").toString()):"");
		status.setSelectedIndex((int)this.getParameters().get("status"));
		dnoffer.setSelectedIndex((int)this.getParameters().get("dnoffer"));
		if(dnoffer.getSelectedIndex()==0) {
			downurl.setLabel("다운로드URL (예시 : https://korean.visitkorea.or.kr)");
		} else {
			downurl.setLabel("다운로드 미제공 사유입력");
		}
		companion.setText(this.getParameters().get("cmt")!=null?this.getParameters().get("cmt").toString():"");
		downurl.setText(this.getParameters().get("url")!=null?this.getParameters().get("url").toString():"");
		
		//신청종류(1:이미지, 2:본문내용(only 텍스트), 3:본문내용+이미지)
		String strapplytype = this.getParameters().get("applytype")!=null?this.getParameters().get("applytype").toString():"";
		applytype.setText(
				strapplytype.equals("1")?"이미지"
						:strapplytype.equals("2")?"본문내용(only 텍스트)"
								:strapplytype.equals("3")? "본문내용+이미지"
										:strapplytype.equals("4")? "온라인"
												:strapplytype.equals("5")? "인쇄물"
														:strapplytype.equals("6")? "기타" : ""
					);
		
		usage.setText(this.getParameters().get("usage")!=null?this.getParameters().get("usage").toString():"");
		etcqna.setText(this.getParameters().get("etcqna")!=null?this.getParameters().get("etcqna").toString():"");
		pacid = this.getParameters().get("pacid")!=null?this.getParameters().get("pacid").toString():null;
		cotid = this.getParameters().get("cotid")!=null?this.getParameters().get("cotid").toString():null;
		
//		selisuse.setSelectionOnSingleMode("사용");
	}

	public void buildContent() {
		
		addDefaultButtons();

		// dialog title define
		dialogTitle = new MaterialLabel("Partners 컨텐츠 내용보기");
		dialogTitle.setFontSize("1.4em");
		dialogTitle.setFontWeight(FontWeight.BOLD);
		dialogTitle.setTextColor(Color.BLUE);
		dialogTitle.setPaddingTop(10);
		dialogTitle.setPaddingLeft(30);
		this.add(dialogTitle);
		mlresultmsg = new MaterialLabel("");
		mlresultmsg.setLayoutPosition(Position.ABSOLUTE);
		mlresultmsg.setFontSize("1.1em");
		mlresultmsg.setFontWeight(FontWeight.BOLD);
		mlresultmsg.setTextColor(Color.GREY_DARKEN_3);
		mlresultmsg.setTop(10);
		mlresultmsg.setLeft(250);
		this.add(mlresultmsg);
		
		buildBody();
	}
	
	private void buildBody() {
		ScrollPanel sp = new ScrollPanel();
		ScrollContentPanel scp = new ScrollContentPanel(this);
		scp.setWidth("780px");
		scp.setOverflow(Overflow.HIDDEN);
		sp.setHeight("450px");
		sp.add(scp);
		this.add(sp);
		
		MaterialRow mr1 = new MaterialRow();
		mr1.setLayoutPosition(Position.RELATIVE); mr1.setMarginTop(20); mr1.setPaddingLeft(30); mr1.setPaddingRight(30);
		this.add(mr1);
		title = new MaterialTextBox();
		title.setLabel("콘텐츠명");
		title.setFloat(Float.LEFT);
		title.setWidth("490px"); title.setHeight("30px"); 
		title.setReadOnly(true);
		title.setFontWeight(FontWeight.BOLDER);
		mr1.add(title);
		
		ctype = new MaterialTextBox();
		ctype.setLabel("카테고리");
		ctype.setFloat(Float.LEFT); ctype.setMarginLeft(30);
		ctype.setWidth("200px"); ctype.setHeight("30px");
		ctype.setReadOnly(true);
		mr1.add(ctype);
		scp.add(mr1);
		
		MaterialRow mr2 = new MaterialRow();
		mr2.setLayoutPosition(Position.RELATIVE); mr2.setPaddingLeft(30);	mr2.setPaddingRight(30);
		this.add(mr2);
		inputid = new MaterialTextBox();
		inputid.setLabel("아이디");
		inputid.setFloat(Float.LEFT);
		inputid.setWidth("250px"); inputid.setHeight("30px");
		inputid.setReadOnly(true);
		mr2.add(inputid);
		
		name = new MaterialTextBox();
		name.setLabel("기관명");
//		name.setMarginLeft(30);
		name.setFloat(Float.LEFT);
		name.setWidth("240px");	name.setHeight("30px");
		name.setReadOnly(true);
		mr2.add(name);
		cdate = new MaterialTextBox();
		cdate.setLabel("등록일시");
		cdate.setMarginLeft(30);
		cdate.setFloat(Float.LEFT);
		cdate.setWidth("200px"); cdate.setHeight("30px");
		cdate.setReadOnly(true);
		mr2.add(cdate);
		scp.add(mr2);
		
		MaterialRow mr3 = new MaterialRow();
		mr3.setLayoutPosition(Position.RELATIVE); mr3.setPaddingLeft(30); mr3.setPaddingRight(30);
		this.add(mr3);
		usage = new MaterialTextBox();
		usage.setLabel("사용처");
		usage.setFloat(Float.LEFT);
		usage.setWidth("490px"); usage.setHeight("30px");
		usage.setReadOnly(true);
		mr3.add(usage);
		udate = new MaterialTextBox();
		udate.setLabel("처리일시");
		udate.setMarginLeft(30);
		udate.setFloat(Float.LEFT);
		udate.setWidth("200px"); udate.setHeight("30px");
		udate.setReadOnly(true);
		mr3.add(udate);
		scp.add(mr3);
		
		MaterialRow mr4 = new MaterialRow();
		mr4.setLayoutPosition(Position.RELATIVE);mr4.setPaddingLeft(30);mr4.setPaddingRight(30);
		this.add(mr4);
		applytype = new MaterialTextBox();
		applytype.setLabel("사용용도");
		applytype.setFloat(Float.LEFT);
		applytype.setWidth("250px"); applytype.setHeight("60px");
		applytype.setReadOnly(true);
		mr4.add(applytype);
		
//		MaterialRow mr5 = new MaterialRow();
//		mr5.setLayoutPosition(Position.RELATIVE);mr5.setPaddingLeft(30);mr5.setPaddingRight(30);
//		this.add(mr5);
		etcqna = new MaterialTextArea();
		etcqna.setLabel("기타 문의사항");
		etcqna.setFloat(Float.LEFT);
		etcqna.setWidth("470px"); etcqna.setHeight("60px");
		etcqna.setReadOnly(true);
		etcqna.getChildrenList().get(0).getElement().getStyle().setPadding(5, Unit.PX);
		etcqna.getChildrenList().get(0).getElement().getStyle().setTop(0, Unit.PX);
		etcqna.getChildrenList().get(0).getElement().getStyle().setHeight(70, Unit.PX);
		etcqna.getChildrenList().get(0).getElement().getStyle().setOverflow(Overflow.AUTO);
		mr4.add(etcqna);
		scp.add(mr4);
				
		MaterialRow mr6 = new MaterialRow();
		mr6.setLayoutPosition(Position.RELATIVE);mr6.setPaddingLeft(30);mr6.setPaddingRight(30);
		this.add(mr6);
		dnoffer = new MaterialComboBox<Object> ();
		dnoffer.setLabel("다운로드 제공여부");
		dnoffer.setFloat(Float.LEFT);
		dnoffer.setWidth("200px"); dnoffer.setHeight("30px");
		dnoffer.addItem("제공", 0);
		dnoffer.addItem("미제공", 1);
		mr6.add(dnoffer);
		downurl = new MaterialTextBox();
		downurl.setLabel("다운로드URL (예시 : https://korean.visitkorea.or.kr)");
		downurl.setFloat(Float.LEFT); downurl.setMarginLeft(20);
		downurl.setWidth("500px"); downurl.setHeight("30px");
		mr6.add(downurl);
		dnoffer.addValueChangeHandler(ee->{
			if(dnoffer.getSelectedIndex()==0) {
				downurl.setLabel("다운로드URL (예시 : https://korean.visitkorea.or.kr)");
			} else {
				downurl.setLabel("다운로드 미제공 사유입력");
			}
		});
		scp.add(mr6);
		
		MaterialRow mr7 = new MaterialRow();
		mr7.setLayoutPosition(Position.RELATIVE); mr7.setMarginTop(20); mr7.setPaddingLeft(30);	mr7.setPaddingRight(30);
		this.add(mr7);
		status = new MaterialComboBox<Object> ();
		status.setLabel("처리상태");
		status.setFloat(Float.LEFT);
		status.setWidth("200px"); status.setHeight("30px");
		status.addItem("접수", 0);
		status.addItem("처리중", 1);
		status.addItem("승인완료", 2);
		status.addItem("반려", 3);
		status.addItem("사용내역등록", 4);
		mr7.add(status);
		scp.add(mr7);
		
		MaterialRow mr8 = new MaterialRow();
		mr8.setLayoutPosition(Position.RELATIVE); mr8.setMarginTop(20); mr8.setPaddingLeft(30);	mr8.setPaddingRight(30);
		this.add(mr8);
		
		companion = new MaterialTextArea();
		companion.setLayoutPosition(Position.RELATIVE);
		companion.setLabel("반려사유");
		companion.setFloat(Float.LEFT);
//		companion.setOverflow(Overflow.AUTO);
		companion.setWidth("100%");
		mr8.add(companion);
		scp.add(mr8);
	}
	
	@Override
	public void setParameters(Map<String, Object> parameters) {
		super.setParameters(parameters);
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
			
			if(status.getSelectedIndex() == 3) {

				if(companion.getText().length() == 0) {
					MaterialToast.fireToast("정확한 반려사유를 입력해주세요.");
					return;
				}
				
			}
			JSONObject parameterJSON = new JSONObject();
			parameterJSON.put("cmd", new JSONString("UPDATE_PARTNERS_CONTENT"));
			parameterJSON.put("pacid", new JSONString(pacid));
			
			parameterJSON.put("status", new JSONString(""+status.getSelectedIndex()));
			parameterJSON.put("dnoffer", new JSONString(""+dnoffer.getSelectedIndex()));
			if(!companion.getText().equals("")) {
				parameterJSON.put("cmt", new JSONString(companion.getText()));
			}
			if(!downurl.getText().equals("")) {
				parameterJSON.put("url", new JSONString(downurl.getText()));
			}
			VisitKoreaBusiness.post("call", parameterJSON.toString(), new Func3<Object, String, Object>() {
				@Override
				public void call(Object param1, String param2, Object param3) {
					JSONObject resultObj = (JSONObject) JSONParser.parseStrict(JSON.stringify((JsObject)param1));
					if (((JSONObject)resultObj.get("header")).get("process").isString().stringValue().equals("success")) {
						getMaterialExtentsWindow().closeDialog();
					} else {
						Registry.showMsg(mlresultmsg, "저장 실패 !  관리자에게 문의하세요.", 3000);
					}
				}
			});
			if(handler != null) {
				handler.onClick(event);
			}
			
		});
		buttonAreaPanel.add(saveButton);
		
		MaterialButton preview = new MaterialButton();
		preview.setText("미리보기");
		preview.setFloat(com.google.gwt.dom.client.Style.Float.LEFT);
		buttonAreaPanel.add(preview);
		preview.addClickHandler(ee->{
			Window.open((String) Registry.get("service.server") + "/detail/detail_view.html?cotid=" +cotid,"","");
		});
		
		this.add(buttonAreaPanel); 
	}

	@Override
	public int getHeight() {
		return 600;
	}
}
