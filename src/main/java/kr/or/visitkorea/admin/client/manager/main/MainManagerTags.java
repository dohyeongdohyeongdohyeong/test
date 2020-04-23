package kr.or.visitkorea.admin.client.manager.main;

import com.google.gwt.dom.client.Style.FontWeight;
import com.google.gwt.dom.client.Style.Overflow;
import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.dom.client.Style.VerticalAlign;

import gwt.material.design.client.MaterialDesignBase;
import gwt.material.design.client.constants.Color;
import gwt.material.design.client.constants.IconType;
import gwt.material.design.client.constants.TextAlign;
import gwt.material.design.client.ui.MaterialIcon;
import gwt.material.design.client.ui.MaterialLabel;
import gwt.material.design.client.ui.MaterialPanel;
import kr.or.visitkorea.admin.client.manager.widgets.AbstractContentPanel;
import kr.or.visitkorea.admin.client.manager.widgets.ContentTable;
import kr.or.visitkorea.admin.client.widgets.window.MaterialExtentsWindow;

public class MainManagerTags extends AbstractContentPanel {

	static {
		MaterialDesignBase.injectCss(MainManagerContentBundle.INSTANCE.contentCss());
	}

	public MainManagerTags(MaterialExtentsWindow meWindow) {
		super(meWindow);
	}

	private void buildTitle() {
		MaterialLabel titleLabel = new MaterialLabel("- 태그");
		titleLabel.setTextAlign(TextAlign.LEFT);
		titleLabel.setFontWeight(FontWeight.BOLD);
		titleLabel.setTextColor(Color.BLUE);
		titleLabel.setFontSize("1.7em");
		titleLabel.setMarginBottom(10);
	//	titleLabel.setIconType(IconType.CODE);
		this.add(titleLabel);
	}

	public void init() {
		
		buildTitle();
		
		this.setPaddingTop(15);
		this.setPaddingBottom(30);
		this.setPaddingRight(30);
		this.setPaddingLeft(30);
		this.setStyleName("mobileManageDetailContent");
		this.setTextAlign(TextAlign.CENTER);
		this.setFloat(com.google.gwt.dom.client.Style.Float.LEFT);
		this.setHeight("100%");
		this.setOverflow(Overflow.HIDDEN);
		
		ContentTable table = new ContentTable(ContentTable.TABLE_SELECT_TYPE.SELECT_SINGLE);
		table.setHeight("440px");

		table.appendTitle("태그 ID", 150, TextAlign.CENTER);
		table.appendTitle("태그 TYPE", 150, TextAlign.CENTER);
		table.appendTitle("태그명", 437, TextAlign.CENTER);
		
		for (int i=0; i<20; i++) {
			if (i%2 == 0) {
				table.addRow(Color.WHITE, "122445","일반","논산시");
			}else {
				table.addRow(Color.GREY_LIGHTEN_4, "2334443","그룹","속초시");
			}
		}

		MaterialPanel panelBottom = new MaterialPanel();
		panelBottom.setBorder("1px solid #e0e0e0");
		panelBottom.setHeight("26px");
		panelBottom.setTop(0);
		panelBottom.setLayoutPosition(Position.RELATIVE);
		
		MaterialIcon icon1 = new MaterialIcon(IconType.ADD);
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
		
		MaterialIcon icon2 = new MaterialIcon(IconType.DELETE);
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
		
		panelBottom.add(icon1);
		panelBottom.add(icon2);
	
		this.add(table);
		this.add(panelBottom);

	}

}
