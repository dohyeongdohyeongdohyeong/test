package kr.or.visitkorea.admin.client.manager.main;

import com.google.gwt.dom.client.Style.FontWeight;
import com.google.gwt.dom.client.Style.Overflow;

import gwt.material.design.client.MaterialDesignBase;
import gwt.material.design.client.constants.Color;
import gwt.material.design.client.constants.TextAlign;
import gwt.material.design.client.ui.MaterialLabel;
import kr.or.visitkorea.admin.client.manager.widgets.AbstractContentPanel;
import kr.or.visitkorea.admin.client.manager.widgets.MarketingPanel;
import kr.or.visitkorea.admin.client.widgets.window.MaterialExtentsWindow;

public class MainManagerMarketing extends AbstractContentPanel {

	static {
		MaterialDesignBase.injectCss(MainManagerContentBundle.INSTANCE.contentCss());
	}

	public MainManagerMarketing(MaterialExtentsWindow meWindow) {
		super(meWindow);
	}

	public void init() {
		
		this.setStyleName("mobileManageDetailContent");
		this.setPaddingTop(15);
		this.setPaddingLeft(30);
		this.setPaddingRight(30);
		this.setTextAlign(TextAlign.CENTER);
		this.setFloat(com.google.gwt.dom.client.Style.Float.LEFT);
		this.setHeight("100%");
		this.setOverflow(Overflow.HIDDEN);
		
		buildTitle();
		buildLayout();
	}

	private void buildTitle() {
		MaterialLabel titleLabel = new MaterialLabel("- 홍보마케팅");
		titleLabel.setTextAlign(TextAlign.LEFT);
		titleLabel.setFontWeight(FontWeight.BOLD);
		titleLabel.setTextColor(Color.BLUE);
		titleLabel.setFontSize("1.7em");
		this.add(titleLabel);

	}

	private void buildLayout() {
		
		MarketingPanel mPanel1 = new MarketingPanel(getMaterialExtentsWindow(), MainManagerApplication.SELECT_CONTENT);
		mPanel1.setMarginTop(10); 
		mPanel1.setWidth("100%");
		mPanel1.setText("영역1");
		this.add(mPanel1);
		
		MarketingPanel mPanel2 = new MarketingPanel(getMaterialExtentsWindow(), MainManagerApplication.SELECT_CONTENT);
		mPanel2.setMarginTop(10); 
		mPanel2.setWidth("100%");
		mPanel2.setText("영역2");
		this.add(mPanel2);

	}

}
