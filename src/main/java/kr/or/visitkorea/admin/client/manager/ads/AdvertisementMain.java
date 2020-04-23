package kr.or.visitkorea.admin.client.manager.ads;

import com.google.gwt.dom.client.Style.Position;

import gwt.material.design.client.MaterialDesignBase;
import kr.or.visitkorea.admin.client.application.Registry;
import kr.or.visitkorea.admin.client.manager.contents.recommand.RecommContentBundle;
import kr.or.visitkorea.admin.client.manager.preview.widgets.Navigator;
import kr.or.visitkorea.admin.client.manager.widgets.AbstractContentPanel;
import kr.or.visitkorea.admin.client.widgets.window.MaterialExtentsWindow;

public class AdvertisementMain extends AbstractContentPanel {

	static {
		MaterialDesignBase.injectCss(RecommContentBundle.INSTANCE.contentCss());
	}
	
	public AdvertisementMain(MaterialExtentsWindow meWindow, AdvertisementApplication aa) {
		super(meWindow);
		
	}

	private String path = null;

	private Navigator navi;
	
	@Override
	public void init() {
		navi = new Navigator();
		navi.setWidth("99.4%");
		navi.setHeight("100%");
		navi.setLeft(0);
		navi.setLayoutPosition(Position.ABSOLUTE);
		navi.setUrl(Registry.get("ads.server").toString());
		
		this.add(navi);
	}

	@Override
	public void onLoad() {
		super.onLoad();

		navi.navigate();
	}
}
