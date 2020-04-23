package kr.or.visitkorea.admin.client.manager.contents.recommand.composite;

import gwt.material.design.addins.client.richeditor.MaterialRichEditor;
import gwt.material.design.addins.client.richeditor.base.constants.ToolbarButton;
import kr.or.visitkorea.admin.client.application.Console;
import kr.or.visitkorea.admin.client.manager.widgets.ContentTreeItem;
import kr.or.visitkorea.admin.client.widgets.window.MaterialExtentsWindow;

public class RecommContentsEditor extends AbtractRecommContents {

	private MaterialRichEditor editor;
	private ContentTreeItem treeItem;
	private String title = "정보 편집기";

	public RecommContentsEditor(MaterialExtentsWindow materialExtentsWindow) {
		super(materialExtentsWindow);
	}

	@Override
	public void init() {
		super.init();
		this.setTitle(title);
       
		editor = new MaterialRichEditor();
		editor.setHeight("405px");
		editor.setAirMode(false);
		editor.setStyleOptions(ToolbarButton.STYLE, ToolbarButton.BOLD, ToolbarButton.ITALIC);
		editor.setParaOptions(ToolbarButton.LEFT, ToolbarButton.CENTER, ToolbarButton.RIGHT, ToolbarButton.JUSTIFY);
		editor.setUndoOptions();
		editor.setHeightOptions();
		editor.setMiscOptions(ToolbarButton.CODE_VIEW);
		editor.addValueChangeHandler(event->{
			if (this.treeItem != null) {
				this.treeItem.setEditorValue(editor.getHTML());
			}
		});
		
		this.add(editor);		
	}
	
	public void reset() {
		this.editor.setText("");
	}

	public void setTarget(ContentTreeItem treeItem) {
		this.treeItem = treeItem;
		this.setTitle(treeItem.getText());
		Console.log(treeItem.getText());
		editor.setHTML(this.treeItem.getEditorValue());
//		editor.setHTML("<html><body><div>fdslkjflsdjflkdsfjdskljf</div></body></html>");
	}

	@Override
	public void setReadOnly(boolean readFlag) {
		
	}

}
