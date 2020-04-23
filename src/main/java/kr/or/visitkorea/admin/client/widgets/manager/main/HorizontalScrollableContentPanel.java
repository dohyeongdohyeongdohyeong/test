package kr.or.visitkorea.admin.client.widgets.manager.main;

import kr.or.visitkorea.admin.client.manager.widgets.AbstractContentPanel;
import kr.or.visitkorea.admin.client.widgets.window.MaterialExtentsWindow;

abstract public class HorizontalScrollableContentPanel extends AbstractContentPanel {

	public HorizontalScrollableContentPanel() {
		super();
	}

	public HorizontalScrollableContentPanel(AbstractContentPanel panel) {
		super(panel);
	}

	public HorizontalScrollableContentPanel(MaterialExtentsWindow meWindow) {
		super(meWindow);
	}

	@Override
	public void init() {

	}

	abstract public void next(int offset);

	abstract public void prev(int offset);

}
