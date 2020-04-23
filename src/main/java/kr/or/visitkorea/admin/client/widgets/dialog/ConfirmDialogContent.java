package kr.or.visitkorea.admin.client.widgets.dialog;

import java.util.Map;

import com.google.gwt.dom.client.Style.Position;

import gwt.material.design.client.constants.Color;
import gwt.material.design.client.ui.MaterialButton;
import kr.or.visitkorea.admin.client.manager.widgets.editor.items.EditorDialogContent;
import kr.or.visitkorea.admin.client.manager.widgets.editor.items.ItemBox;

public class ConfirmDialogContent extends EditorDialogContent {

	public ConfirmDialogContent() {
		super();
	}
	
	@Override
	public void setParameters(Map<String, Object> parameters) {
		
		super.setParameters(parameters);
		

		MaterialButton removeButton = new MaterialButton("삭제");
		removeButton.setBackgroundColor(Color.RED);
		removeButton.setMarginRight(10);
		removeButton.setLayoutPosition(Position.RELATIVE);
		removeButton.setFloat(com.google.gwt.dom.client.Style.Float.RIGHT);
		removeButton.addClickHandler(event->{
			
			if (parameters.containsKey("item")) {
				ItemBox tgrItemBox = (ItemBox) parameters.get("item");
				tgrItemBox.removeFromParent();
			}
			
			closeDialog();
			
		});
		
		buttonAreaPanel.add(removeButton);
		
	}

	
}
