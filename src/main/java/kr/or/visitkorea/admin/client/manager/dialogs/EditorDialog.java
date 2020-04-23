package kr.or.visitkorea.admin.client.manager.dialogs;

import java.util.Map;

import gwt.material.design.addins.client.richeditor.MaterialRichEditor;
import gwt.material.design.client.MaterialDesignBase;
import gwt.material.design.client.constants.Color;
import gwt.material.design.client.ui.MaterialButton;
import kr.or.visitkorea.admin.client.widgets.dialog.DialogContent;
import kr.or.visitkorea.admin.client.widgets.window.MaterialExtentsWindow;

public class EditorDialog extends DialogContent {

	static {
		MaterialDesignBase.injectCss(DialogsBundle.INSTANCE.contentCss());
	}

	private MaterialRichEditor editor;

	public EditorDialog(MaterialExtentsWindow window) {
		super(window);
	}

	public void init() {
		addDefaultButtons();

		MaterialButton selectButton = new MaterialButton("검색");
		selectButton.setBackgroundColor(Color.RED_LIGHTEN_2);
		selectButton.addClickHandler(event->{
		});
		this.addButton(selectButton);

		editor = new MaterialRichEditor();
		editor.setHeight("210px");
		editor.setTop(10);
		this.add(editor);		
	}

	@Override
	public void setParameters(Map<String, Object> parameters) {
		super.setParameters(parameters);
	}
	

	@Override
	public int getHeight() {
		return 480;
	}
	
}
