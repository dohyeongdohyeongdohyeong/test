package kr.or.visitkorea.admin.client.manager.guidebook;

import kr.or.visitkorea.admin.client.application.Console;
import kr.or.visitkorea.admin.client.manager.guidebook.model.GuideBook;
import kr.or.visitkorea.admin.client.manager.guidebook.panel.AddPanel;
import kr.or.visitkorea.admin.client.manager.guidebook.panel.BasePanel;
import kr.or.visitkorea.admin.client.manager.guidebook.panel.SearchPanel;
import kr.or.visitkorea.admin.client.manager.widgets.AbstractContentPanel;
import kr.or.visitkorea.admin.client.widgets.window.MaterialExtentsWindow;

public class GuideBookMain extends AbstractContentPanel {

	private SearchPanel searchPanel;
	private AddPanel addPanel;
	private AddPanel editPanel;

	private GuideBook selectedGuideBook;

	GuideBookMain(MaterialExtentsWindow materialExtentsWindow) {
		super(materialExtentsWindow);
	}

	@Override
	public void init() {
		searchPanel = new SearchPanel(this);
		switchToSearchPanel();
	}

	private BasePanel getAddPanel() {
		if (addPanel == null) {
			addPanel = new AddPanel(this);
		}
		return addPanel;
	}

	private BasePanel getEditPanel() {
		if (editPanel == null) {
			editPanel = new AddPanel(this, true);
		}
		return editPanel;
	}

	public void switchToSearchPanel() {
		if (addPanel != null) {
			remove(addPanel);
			addPanel = null;
		}
		if (editPanel != null) {
			remove(editPanel);
			editPanel = null;
		}
		add(searchPanel);
	}

	public void switchToAddPanel() {
		remove(searchPanel);
		add(getAddPanel());
	}

	public void switchToEditPanel() {
		if (selectedGuideBook == null) {
			throw new IllegalStateException("Not set a guidebook yet!!");
		}
		remove(searchPanel);
		add(getEditPanel());
	}

	public void setSelectedGuideBook(GuideBook item) {
		selectedGuideBook = item;
		Console.log(item.toString());
	}

	public GuideBook getSelectedGuideBook() {
		return selectedGuideBook;
	}

	public void refreshSearchPanel() {
		searchPanel.refresh();
	}
}
