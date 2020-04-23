package kr.or.visitkorea.admin.client.widgets.manager.main;

import java.util.HashMap;

import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.dom.client.Style.VerticalAlign;

import gwt.material.design.client.constants.IconType;
import gwt.material.design.client.constants.TextAlign;
import gwt.material.design.client.ui.MaterialCollectionItem;
import gwt.material.design.client.ui.MaterialColumn;
import gwt.material.design.client.ui.MaterialIcon;
import gwt.material.design.client.ui.MaterialPanel;
import gwt.material.design.client.ui.MaterialRow;
import kr.or.visitkorea.admin.client.manager.widgets.ContentCollection;
import kr.or.visitkorea.admin.client.manager.widgets.ContentDetailPanel;
import kr.or.visitkorea.admin.client.manager.widgets.ContentImageListItem;
import kr.or.visitkorea.admin.client.widgets.window.MaterialExtentsWindow;

public class VisitKoreaMainManager extends MaterialPanel {

    private MaterialExtentsWindow window;

	public VisitKoreaMainManager(MaterialExtentsWindow meWindow) {
		super();
		init();
		this.window = meWindow;
	}
    
    private void init() {
		this.setTextAlign(TextAlign.CENTER);
		this.setHeight("100%");
		this.setWidth("100%");
		this.setBorder("1 solid grey");
	}
    
    public void setWidow(MaterialExtentsWindow meWindow) {
		this.window = meWindow;
   }

	protected void onLoad() {
        super.onLoad();

		MaterialRow row = new MaterialRow();
		row.setHeight("100%");
		
		MaterialColumn col1 = new MaterialColumn();
		col1.setPaddingLeft(30);
		col1.setPaddingBottom(76);
		col1.setPaddingTop(22);
		col1.setGrid("s4");
		col1.setHeight("100%");
		
		MaterialColumn col2 = new MaterialColumn();
		col2.setPaddingRight(30);
		col2.setPaddingBottom(42);
		col2.setPaddingTop(30);
		col2.setGrid("s8");
		col2.setHeight("100%");
		
		row.add(col1);
		row.add(col2);

		MaterialPanel panelTop = new MaterialPanel();
		panelTop.setLayoutPosition(Position.RELATIVE);
		panelTop.setHeight("100%");

		MaterialPanel panelBottom = new MaterialPanel();
		panelBottom.setBorder("1px solid #e0e0e0");
		panelBottom.setHeight("26px");
		panelBottom.setPadding(0);
		panelBottom.setTop(-40);
		panelBottom.setLayoutPosition(Position.RELATIVE);
		
		MaterialIcon icon1 = new MaterialIcon(IconType.SEARCH);
		icon1.setLineHeight(26);
		icon1.setFontSize("1.0em");
		icon1.setBorderRight("1px solid #e0e0e0");
		icon1.setHeight("26px");
		icon1.setMargin(0);
		icon1.setWidth("26px");
		icon1.setFloat(com.google.gwt.dom.client.Style.Float.LEFT);
		icon1.setTooltip("검색으로 추가");
		panelBottom.add(icon1);
		
		MaterialIcon icon2 = new MaterialIcon(IconType.CLOUD_QUEUE);
		icon2.setLineHeight(26);
		icon2.setVerticalAlign(VerticalAlign.MIDDLE);
		icon2.setFontSize("1.0em");
		icon2.setBorderRight("1px solid #e0e0e0");
		icon2.setHeight("26px");
		icon2.setMargin(0);
		icon2.setWidth("26px");
		icon2.setFloat(com.google.gwt.dom.client.Style.Float.LEFT);
		icon2.setTooltip("외부 링크 추가");
		panelBottom.add(icon2);
		
		ContentCollection contentList = new ContentCollection();
		contentList.setStyleName("contentoverflow");
		
		panelTop.add(contentList);
		
		ContentDetailPanel contentDetailPanel = new ContentDetailPanel();
		
		contentDetailPanel.setPadding(30);
		
		col1.add(panelTop);
		col1.add(panelBottom);
		col2.add(contentDetailPanel);
	
		String[] listImgArr = new String[] {
				"https://img-wishbeen.akamaized.net/plan/1463029230608__%EC%8A%AC%EB%A1%9C%EB%B2%A0%EB%8B%88%EC%95%8403.jpg",
				"https://img-wishbeen.akamaized.net/plan/1460453012183_GreeceOia.jpg",
				"https://img-wishbeen.akamaized.net/plan/1463709040242_993553_10153867049365891_1315228110210941110_n-1.jpg",
				"https://img-wishbeen.akamaized.net/plan/1474600488022_%EB%9F%B0%EB%8D%98--.jpg",
				"https://img-wishbeen.akamaized.net/plan/1442301594219_Untitled-21.jpg",
				"https://www.sktinsight.com/wp-content/uploads/2017/11/%EC%A4%91%EA%B5%AD%EC%97%AC%ED%96%89%EC%A7%80%EC%B6%94%EC%B2%9C_2.jpg",
		};

		
		for (int i=0; i<6; i++) {
			
			MaterialCollectionItem item1 = new MaterialCollectionItem();
			item1.setHoverable(true); 
			
			ContentImageListItem link = new ContentImageListItem(contentList, contentList.getChildrenList().size()+1);
			link.setData(new HashMap<String, Object>());
			link.setContentDetailPanel(contentDetailPanel);
			link.setUrl(listImgArr[i]);
			item1.add(link);
			contentList.add(item1);
		}
		
		contentList.complete();
		this.add(row);

    }
  
	
}
