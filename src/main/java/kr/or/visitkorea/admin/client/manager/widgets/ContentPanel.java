package kr.or.visitkorea.admin.client.manager.widgets;

import kr.or.visitkorea.admin.client.widgets.window.MaterialExtentsWindow;

public class ContentPanel extends AbstractContentPanel {

	public ContentPanel() {
		super();
	}

	public ContentPanel(AbstractContentPanel panel) {
		super(panel);
	}

	public ContentPanel(MaterialExtentsWindow meWindow) {
		super(meWindow);
	}

	public ContentPanel(String... initialClass) {
		super(initialClass);
	}

	@Override
	public void init() {

	}

}
