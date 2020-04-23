package kr.or.visitkorea.admin.client.manager.preview.composite;

import com.google.gwt.dom.client.Style.Position;

import gwt.material.design.client.MaterialDesignBase;
import gwt.material.design.client.constants.IconType;
import gwt.material.design.client.ui.MaterialLink;
import gwt.material.design.client.ui.MaterialPanel;
import kr.or.visitkorea.admin.client.application.Registry;
import kr.or.visitkorea.admin.client.manager.contents.recommand.RecommContentBundle;
import kr.or.visitkorea.admin.client.manager.preview.widgets.Navigator;
import kr.or.visitkorea.admin.client.manager.widgets.AbstractContentPanel;
import kr.or.visitkorea.admin.client.widgets.window.MaterialExtentsWindow;

public class PreviewMainComposite extends AbstractContentPanel {

	static {
		MaterialDesignBase.injectCss(RecommContentBundle.INSTANCE.contentCss());
	}

	private Navigator navi;
	private MaterialPanel topMenu;
	private MaterialPanel naviPanel;

	public PreviewMainComposite(MaterialExtentsWindow materialExtentsWindow) {
		super(materialExtentsWindow);
	}

	@Override
	public void init() {

		topMenu = new MaterialPanel();
		this.add(topMenu);
		
		naviPanel = new MaterialPanel();

		this.add(naviPanel);
		
		initTopMenu();
		initNavigation();
		
	}
	
	private void initNavigation() {
		
		naviPanel.setWidth("100%");
		naviPanel.setHeight("100%");
		
		navi = new Navigator();
		navi.setWidth("600px");
		navi.setHeight("720px");
		navi.setLeft(0);
		navi.setLayoutPosition(Position.ABSOLUTE);
		naviPanel.add(navi);
		
		String prevUrl = (String)Registry.get("PREVIEW_URL");
		if (prevUrl == null || prevUrl.trim().length() == 0) prevUrl = "https://kor.uniess.co.kr";
		navi.setUrl(prevUrl);
		
		Registry.put("NAVIGATOR", navi);
		
	}
	
	public void setUrl(String prevUrl) {
		navi.setUrl(prevUrl);
	}

	private void initTopMenu() {
		
		topMenu.setHeight("40px");
		topMenu.setPaddingTop(10);
		topMenu.setPaddingLeft(10);
		topMenu.setBorderBottom("1px solid #efefef");

		MaterialLink desktopMac = new MaterialLink();
		desktopMac.setIconType(IconType.DESKTOP_MAC);
		desktopMac.setLineHeight(40);
		desktopMac.setFloat(com.google.gwt.dom.client.Style.Float.RIGHT);
		desktopMac.addClickHandler(event->{
			com.google.gwt.dom.client.Style naviStyle = navi.getElement().getStyle();
			naviStyle.setProperty("transform", "scale(0.5)");
			naviStyle.setProperty("transformOrigin", "0px top 60px");
			double frameWidth = 600 * (1/0.5);
			double frameHeight = 720 * (1/0.5);
			navi.setWidth(frameWidth + "px");
			navi.setHeight(frameHeight + "px");
		});
		topMenu.add(desktopMac);
		
		MaterialLink mobile = new MaterialLink();
		mobile.setIconType(IconType.PHONE_IPHONE);
		mobile.setLineHeight(40);
		mobile.setFloat(com.google.gwt.dom.client.Style.Float.RIGHT);
		mobile.addClickHandler(event->{
			com.google.gwt.dom.client.Style naviStyle = navi.getElement().getStyle();
			naviStyle.setProperty("transform", "scale(1.0)");
			naviStyle.setProperty("transformOrigin", "0px top 60px");
			double frameWidth = 600;
			double frameHeight = 720;
			navi.setWidth(frameWidth + "px");
			navi.setHeight(frameHeight + "px");
		});
		topMenu.add(mobile);
		
		MaterialLink home = new MaterialLink();
		home.setIconType(IconType.HOME);
		home.setLineHeight(40);
		home.setFloat(com.google.gwt.dom.client.Style.Float.LEFT);
		home.addClickHandler(event->{
			
			String prevUrl = (String)Registry.get("PREVIEW_URL");
			if (prevUrl == null || prevUrl.trim().length() == 0) prevUrl = "https://kor.uniess.co.kr";
			navi.setUrl(prevUrl);
			navi.navigate();

		});
		topMenu.add(home);
		
	}

	@Override
	public void onLoad() {
		super.onLoad();

		navi.navigate();
		
		this.setWidth("600px");
		this.setHeight("720px");

	}

}
