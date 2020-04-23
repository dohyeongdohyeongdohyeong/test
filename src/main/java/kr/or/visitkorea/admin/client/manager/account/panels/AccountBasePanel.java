package kr.or.visitkorea.admin.client.manager.account.panels;

import gwt.material.design.client.constants.Display;
import gwt.material.design.client.ui.MaterialPanel;
import kr.or.visitkorea.admin.client.manager.account.AccountMain;
import kr.or.visitkorea.admin.client.widgets.window.MaterialExtentsWindow;

public abstract class AccountBasePanel extends MaterialPanel {
	
	protected AccountMain main;
	
	public AccountBasePanel(AccountMain main) {
		super();
		this.main = main;
		this.initFrame();
		this.init();
	}

	private void initFrame() {
		this.setDisplay(Display.INLINE_BLOCK);
		this.setWidth(this.main.getMaterialExtentsWindow().getWidth() + "px");
		this.setPadding(10);
	}
	
	protected MaterialExtentsWindow getMaterialExtentsWindow() {
		return this.main.getMaterialExtentsWindow();
	}

	public abstract void fetch(boolean isStart);
	protected abstract void init();
}
