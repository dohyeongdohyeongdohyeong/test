package kr.or.visitkorea.admin.client.widgets.manager.main;

import com.google.gwt.dom.client.Style.VerticalAlign;

import gwt.material.design.client.constants.Color;
import gwt.material.design.client.constants.Display;
import gwt.material.design.client.constants.TextAlign;
import gwt.material.design.client.ui.MaterialCollection;
import gwt.material.design.client.ui.MaterialCollectionItem;
import gwt.material.design.client.ui.MaterialColumn;
import gwt.material.design.client.ui.MaterialPanel;
import gwt.material.design.client.ui.MaterialRow;

public class VisitKoreaListOrderPanel extends MaterialPanel {
    
	private MaterialRow row;
	private MaterialColumn col1;
	private MaterialColumn col2;
	private MaterialCollection contentList;
	private MaterialPanel contentDetailPanel;
	

    public VisitKoreaListOrderPanel() {
		super();
		init();
	}


	public VisitKoreaListOrderPanel(String... initialClass) {
		super(initialClass);
		init();
	}


	private void init() {
		
		this.setTextAlign(TextAlign.CENTER);
		this.setHeight("100%");
		this.setWidth("100%");
		this.setBorder("1 solid grey");
		
		row = new MaterialRow();
		col1 = new MaterialColumn();
		col2 = new MaterialColumn();
		contentList = new MaterialCollection();
		contentDetailPanel = new MaterialPanel();
	}


	protected void onLoad() {
		
        super.onLoad();

		row.setHeight("100%");
		
		col1.setPaddingLeft(30);
		col1.setPaddingBottom(30);
		col1.setPaddingTop(22);
		col1.setGrid("s4");
		col1.setHeight("100%");
		
		col2.setPaddingRight(30);
		col2.setPaddingBottom(30);
		col2.setPaddingTop(30);
		col2.setGrid("s8");
		col2.setHeight("100%");
		
		row.add(col1);
		row.add(col2);

		//contentList.setHeight("500px");
		contentList.setStyleName("contentoverflow");
		
		contentDetailPanel.setTextAlign(TextAlign.CENTER);
		//contentDetailPanel.setHeight("500px");
		contentDetailPanel.setWidth("100%"); 
		contentDetailPanel.setBackgroundColor(Color.GREY_LIGHTEN_1);
		contentDetailPanel.setDisplay(Display.INLINE_BLOCK);
		contentDetailPanel.setVerticalAlign(VerticalAlign.MIDDLE);
		contentDetailPanel.setStyleName("contentoverflow");
		
		col1.add(contentList);
		
		col2.add(contentDetailPanel);
		
		this.add(row);

    }
	
	public void appendItem(MaterialCollectionItem item) {
		this.contentList.add(item);
	}
  
	
}
