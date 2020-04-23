package kr.or.visitkorea.admin.client.application.component;

import com.google.gwt.dom.client.Style.Float;

import gwt.material.design.client.constants.Color;
import gwt.material.design.client.constants.IconType;
import gwt.material.design.client.ui.MaterialDialog;
import gwt.material.design.client.ui.MaterialDialogHeader;
import gwt.material.design.client.ui.MaterialIcon;
import gwt.material.design.client.ui.MaterialLabel;
import gwt.material.design.client.ui.MaterialRow;
import kr.or.visitkorea.admin.client.manager.preview.widgets.Navigator;

public class EventExcelDownloadDialog extends MaterialDialog {
	
	private Navigator navi;
	private MaterialDialogHeader dialogHeader = new MaterialDialogHeader();
	public EventExcelDownloadDialog() {
		super();
		init();
	}
	
	public void init() {
		
		this.setWidth("1000px");
		this.setHeight("300px");
		
		
		MaterialRow row1 = new MaterialRow();
		row1.setBackgroundColor(Color.LIGHT_BLUE_DARKEN_3);
		row1.setHeight("40px");
		MaterialLabel title = new MaterialLabel("이벤트 엑셀 다운로드");
		title.setPaddingLeft(10);
		title.setLineHeight(40);
		title.setTextColor(Color.WHITE);
		title.setFontSize("20px");
		title.setFloat(Float.LEFT);
		row1.add(title);
		row1.setMarginBottom(0);
		dialogHeader.add(row1);
		
		MaterialIcon closeBtn = new MaterialIcon(IconType.CLOSE);
		closeBtn.setFloat(Float.RIGHT);
		closeBtn.setTextColor(Color.WHITE);
		closeBtn.setLineHeight(40);
		closeBtn.setMarginRight(10);
    	closeBtn.addClickHandler(e -> {
    		this.close();
    	});
    	closeBtn.setTooltip("닫기");
    	
    	row1.add(closeBtn);
		
		this.add(dialogHeader);
		this.navi = new Navigator();
		this.navi.setWidth("100%");
		this.navi.setHeight("250px");
		this.navi.setUrl("https://kor.uniess.co.kr/EventDataExporter/");
		this.navi.navigate();
		this.add(navi);
		
	}
	
	
}
