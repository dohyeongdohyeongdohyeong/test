package kr.or.visitkorea.admin.client.manager.main;

import java.util.HashMap;

import com.google.gwt.dom.client.Style.FontWeight;
import com.google.gwt.dom.client.Style.Overflow;
import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.dom.client.Style.VerticalAlign;

import gwt.material.design.client.MaterialDesignBase;
import gwt.material.design.client.constants.Color;
import gwt.material.design.client.constants.IconType;
import gwt.material.design.client.constants.TextAlign;
import gwt.material.design.client.ui.MaterialCollectionItem;
import gwt.material.design.client.ui.MaterialColumn;
import gwt.material.design.client.ui.MaterialIcon;
import gwt.material.design.client.ui.MaterialLabel;
import gwt.material.design.client.ui.MaterialPanel;
import gwt.material.design.client.ui.MaterialRow;
import kr.or.visitkorea.admin.client.manager.VisitKorea;
import kr.or.visitkorea.admin.client.manager.main.composite.InternalConntentComposite;
import kr.or.visitkorea.admin.client.manager.widgets.AbstractContentPanel;
import kr.or.visitkorea.admin.client.manager.widgets.ContentCollection;
import kr.or.visitkorea.admin.client.manager.widgets.ContentDetailPanel;
import kr.or.visitkorea.admin.client.manager.widgets.ContentImageListItem;
import kr.or.visitkorea.admin.client.widgets.window.MaterialExtentsWindow;

public class MainManagerShowCase extends AbstractContentPanel {

	static {
		MaterialDesignBase.injectCss(MainManagerContentBundle.INSTANCE.contentCss());
	}

	public MainManagerShowCase(MaterialExtentsWindow meWindow) {
		super(meWindow);
	}

	public void init() {
		
		this.setStyleName("mobileManageDetailContent");
		this.setTextAlign(TextAlign.CENTER);
		this.setFloat(com.google.gwt.dom.client.Style.Float.LEFT);
		this.setHeight("100%");
		this.setOverflow(Overflow.HIDDEN);
		
		buildTitle();
		buildLayout(); 
	}

	private void buildTitle() {
		MaterialLabel titleLabel = new MaterialLabel("- 쇼케이스");
		titleLabel.setMarginTop(15);
		titleLabel.setTextAlign(TextAlign.LEFT);
		titleLabel.setPaddingLeft(30);
		titleLabel.setFontWeight(FontWeight.BOLD);
		titleLabel.setTextColor(Color.BLUE);
		titleLabel.setFontSize("1.7em");
		//titleLabel.setIconType(IconType.COLLECTIONS);
		this.add(titleLabel);
	}
	
	private void buildLayout() {
		
		MaterialRow row = new MaterialRow();
		row.setHeight("100%");
		
		MaterialColumn col1 = new MaterialColumn();
		col1.setPaddingLeft(30);
		col1.setPaddingBottom(76);
		col1.setGrid("s4");
		col1.setHeight("100%");
		
		MaterialColumn col2 = new MaterialColumn();
		col2.setPaddingRight(30);
		col2.setPaddingBottom(42);
		col2.setPaddingTop(8);
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
		
		icon1.addClickHandler(event->{
			getMaterialExtentsWindow().openDialog(MainManagerApplication.SELECT_CONTENT, 720);
		});
		
		MaterialIcon icon2 = new MaterialIcon(IconType.CLOUD_QUEUE);
		icon2.setLineHeight(26);
		icon2.setVerticalAlign(VerticalAlign.MIDDLE);
		icon2.setFontSize("1.0em");
		icon2.setBorderRight("1px solid #e0e0e0");
		icon2.setHeight("26px");
		icon2.setMargin(0);
		icon2.setWidth("26px");
		icon2.setFloat(com.google.gwt.dom.client.Style.Float.LEFT);
		
		icon2.addClickHandler(event->{
			getMaterialExtentsWindow().openDialog(MainManagerApplication.CREATE_URL_LINK, 720);
		});
		
		MaterialIcon icon3 = new MaterialIcon(IconType.DELETE);
		icon3.setLineHeight(26);
		icon2.setVerticalAlign(VerticalAlign.MIDDLE);
		icon3.setFontSize("1.0em");
		icon3.setBorderRight("1px solid #e0e0e0");
		icon3.setHeight("26px");
		icon3.setMargin(0);
		icon3.setWidth("26px");
		icon3.setFloat(com.google.gwt.dom.client.Style.Float.LEFT);
		
		icon3.addClickHandler(event->{
		});
		
		panelBottom.add(icon1);
		panelBottom.add(icon2);
		panelBottom.add(icon3);

		ContentCollection contentList = new ContentCollection();
		contentList.setStyleName("contentoverflow");

		panelTop.add(contentList);
		
		ContentDetailPanel contentDetailPanel = new ContentDetailPanel();
		contentDetailPanel.setPadding(30);
		contentDetailPanel.setBackgroundColor(Color.WHITE);
		
		InternalConntentComposite icc = new InternalConntentComposite();
		
		contentDetailPanel.setComposite(icc);
		
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
			
			// examples for content type display.
			if (i%2 == 0) {
				link.setContentType(VisitKorea.CONTENT_TYPE_DEFAULT);
			}else {
				link.setContentType(VisitKorea.CONTENT_TYPE_OUT_LINK);
			}

			item1.add(link);
			contentList.add(item1);
		}
		
		col1.add(panelTop);
		col1.add(panelBottom);
		col2.add(contentDetailPanel);
		
		this.add(row);
	}

}
