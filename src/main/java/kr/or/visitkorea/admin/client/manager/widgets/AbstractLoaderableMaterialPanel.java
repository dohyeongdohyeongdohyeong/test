package kr.or.visitkorea.admin.client.manager.widgets;

import com.google.gwt.user.client.Timer;

import gwt.material.design.client.constants.LoaderType;
import gwt.material.design.client.ui.MaterialPanel;

abstract public class AbstractLoaderableMaterialPanel extends MaterialPanel {

	private ContentLoader loader;

	public AbstractLoaderableMaterialPanel() {
		super();
		panelInit();
	}

	public AbstractLoaderableMaterialPanel(String... initialClass) {
		super(initialClass);
		panelInit();
	}
	
	private void panelInit() {
		this.loader = new ContentLoader();
		this.loader.setContainer(this);
		this.loader.setType(LoaderType.CIRCULAR);
	}

	public void loading(boolean loadFlag) {

		if (loadFlag) {
			loader.show();
		}else {
			Timer timer = new Timer() {
				@Override
				public void run() {
					loader.hide();
				}
			};
			timer.schedule(10);
		}
	}
	
}
