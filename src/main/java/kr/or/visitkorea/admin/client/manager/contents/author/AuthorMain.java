package kr.or.visitkorea.admin.client.manager.contents.author;

import kr.or.visitkorea.admin.client.manager.contents.author.panel.AuthorPanel;
import kr.or.visitkorea.admin.client.manager.widgets.AbstractContentPanel;
import kr.or.visitkorea.admin.client.widgets.window.MaterialExtentsWindow;

public class AuthorMain extends AbstractContentPanel {

	private AuthorPanel authorPanel;
	
	public AuthorMain(MaterialExtentsWindow meWindow) {
		super(meWindow);
	}
	
	@Override
	public void init() {
		authorPanel = new AuthorPanel(this);
		
		this.add(authorPanel);
	}

}
