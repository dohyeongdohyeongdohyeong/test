package kr.or.visitkorea.admin.client.manager.partners.content;

import java.util.Map;

import com.google.gwt.dom.client.Style.Float;
import com.google.gwt.dom.client.Style.FontWeight;
import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.user.client.Window;

import gwt.material.design.addins.client.inputmask.MaterialInputMask;
import gwt.material.design.client.constants.Color;
import gwt.material.design.client.ui.MaterialButton;
import gwt.material.design.client.ui.MaterialLabel;
import gwt.material.design.client.ui.MaterialPanel;
import gwt.material.design.client.ui.MaterialRow;
import kr.or.visitkorea.admin.client.application.Console;
import kr.or.visitkorea.admin.client.application.Registry;
import kr.or.visitkorea.admin.client.widgets.dialog.DialogContent;
import kr.or.visitkorea.admin.client.widgets.window.MaterialExtentsWindow;

public class PartnersContentResultDialog extends DialogContent {

	private MaterialLabel dialogTitle, mlresultmsg;
	private MaterialInputMask<Object> resurl, dbody;
	private MaterialButton fname;
	
	public PartnersContentResultDialog(MaterialExtentsWindow window) {
		super(window);
	}

	@Override
	public void init() {
		buildContent();
	}

	@Override
	protected void onLoad() {
		super.onLoad();
		resurl.setText(this.getParameters().get("resurl")!=null?this.getParameters().get("resurl").toString():"");
		dbody.setText(this.getParameters().get("dbody")!=null?this.getParameters().get("dbody").toString():"");
		fname.setText(this.getParameters().get("fname")!=null?this.getParameters().get("fname").toString():"");
		String tt = this.getParameters().get("fname")!=null?this.getParameters().get("fname").toString():"";
		final String ext = tt.substring(tt.lastIndexOf("."));
		fname.getHandlerRegistry().clearHandlers();
		fname.getHandlerRegistry().registerHandler(
		fname.addClickHandler(ee->{
			String fileUrl2 = this.getParameters().get("furl")!=null?this.getParameters().get("furl").toString():"";
			
			if(fileUrl2.lastIndexOf(".") > -1) {
				Window.open((String) Registry.get("image.server") + "/img/call?cmd=TEMP_VIEW&name=" +fileUrl2,"","");
			} else { 
				Console.log((String) Registry.get("image.server") + "/img/call?cmd=TEMP_VIEW&name=" +fileUrl2+ext);
				Window.open((String) Registry.get("image.server") + "/img/call?cmd=TEMP_VIEW&name=" +fileUrl2+ext,"","");
			}
		}));
	}

	public void buildContent() {
		
		addDefaultButtons();

		// dialog title define
		dialogTitle = new MaterialLabel("Partners 컨텐츠 사용결과 정보");
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
		MaterialRow mr1 = new MaterialRow();
		mr1.setLayoutPosition(Position.RELATIVE);
		mr1.setMarginTop(20);
		mr1.setPaddingLeft(30);	mr1.setPaddingRight(30);
		resurl = new MaterialInputMask<Object>();
		resurl.setLabel("등록URL");
		resurl.setFloat(Float.LEFT);resurl.setMarginLeft(30);
		resurl.setWidth("570px"); resurl.setHeight("35px");
		mr1.add(resurl);
		this.add(mr1);
		
		MaterialRow mr2 = new MaterialRow();
		mr2.setLayoutPosition(Position.RELATIVE);
		mr2.setPaddingLeft(30);	mr2.setPaddingRight(30);
		dbody = new MaterialInputMask<Object>();
		dbody.setLabel("상세내용or사유");
		dbody.setFloat(Float.LEFT);
		dbody.setWidth("570px");
		dbody.setHeight("35px");
		dbody.setMarginLeft(30);
		mr2.add(dbody);
		dbody.addKeyDownHandler(ee->{
		});
		this.add(mr2);
		
		MaterialRow mr3 = new MaterialRow();
		mr3.setLayoutPosition(Position.RELATIVE);
		mr3.setPaddingLeft(30);	mr3.setPaddingRight(30);
		MaterialLabel lb03 = new MaterialLabel("* 첨부파일");
		lb03.setLayoutPosition(Position.RELATIVE);
		lb03.setFloat(Float.LEFT);
		lb03.setTop(5);lb03.setWidth("120px");
		mr3.add(lb03);
		fname = new MaterialButton();
		fname.setText("다운로드");
		fname.setFloat(Float.LEFT);
		fname.setTop(-15); fname.setWidth("300px");fname.setHeight("35px");
		fname.setMarginLeft(30);
		mr3.add(fname);
		this.add(mr3);
	}
	@Override
	public void setParameters(Map<String, Object> parameters) {
		super.setParameters(parameters);
	}
	
	private MaterialPanel buttonAreaPanel;
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
		
		this.add(buttonAreaPanel); 
	}

	@Override
	public int getHeight() {
		return 520;
	}
}
