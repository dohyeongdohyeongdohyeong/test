package kr.or.visitkorea.admin.client.manager.widgets.editor.panel;

import com.google.gwt.json.client.JSONObject;
import com.google.gwt.user.client.ui.AbsolutePanel;

import gwt.material.design.client.ui.MaterialToast;
import kr.or.visitkorea.admin.client.manager.widgets.editor.ContentEditor;
import kr.or.visitkorea.admin.client.manager.widgets.editor.items.EditorContentDialog.TYPE;
import kr.or.visitkorea.admin.client.manager.widgets.editor.items.ItemBox;

public class ContentDetailPanel extends AbsolutePanel {

	private ItemBox selectedItembox;
	private ContentEditor editor;
	
	public void setSelectedItemBox(ItemBox itembox) {
		this.selectedItembox = itembox;
	}

	public ItemBox getSelectedItemBox() {
		return this.selectedItembox;
	}
	
	public ContentDetailPanel(ContentEditor editor) {
		super();
		this.editor = editor;
	}

	public ItemBox getSelectedItembox() {
		return selectedItembox;
	}

	public void setSelectedItembox(ItemBox selectedItembox) {
		this.selectedItembox = selectedItembox;
	}

	public ContentEditor getEditor() {
		return editor;
	}

}
