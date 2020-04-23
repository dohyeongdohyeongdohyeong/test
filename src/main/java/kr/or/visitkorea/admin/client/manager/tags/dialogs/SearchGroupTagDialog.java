
package kr.or.visitkorea.admin.client.manager.tags.dialogs;

import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Style.Position;

import gwt.material.design.client.constants.Color;
import gwt.material.design.client.constants.IconPosition;
import gwt.material.design.client.constants.IconType;
import gwt.material.design.client.ui.MaterialButton;
import gwt.material.design.client.ui.MaterialDropDown;
import gwt.material.design.client.ui.MaterialLink;
import gwt.material.design.client.ui.MaterialTextBox;
import kr.or.visitkorea.admin.client.widgets.dialog.DialogContent;
import kr.or.visitkorea.admin.client.widgets.window.MaterialExtentsWindow;

public class SearchGroupTagDialog extends DialogContent {


	private MaterialButton divBtn;
	private MaterialTextBox inputBox;

	public SearchGroupTagDialog(MaterialExtentsWindow window) {
		super(window);
	}

	@Override
	public void init() {

       addDefaultButtons();
		
        // dialog title define
		inputBox = new MaterialTextBox();
		inputBox.setLabel("그룹 태그명");
	
		inputBox.addKeyUpHandler(event->{
			if (event.getNativeKeyCode() == 13) {
				getMaterialExtentsWindow().closeDialog();
			}
		});
		
		inputBox.setLayoutPosition(Position.ABSOLUTE);
		inputBox.setRight(100);
		inputBox.setLeft(300);
		inputBox.setTop(30);
		
		String uniqueId = Document.get().createUniqueId();
		
		divBtn = new MaterialButton();
		divBtn.setText("그룹 태그명");
		divBtn.setIconType(IconType.ARROW_DROP_DOWN);
		divBtn.setIconPosition(IconPosition.RIGHT);
		divBtn.setTextColor(Color.WHITE);
		divBtn.setActivates(uniqueId);
		divBtn.setLayoutPosition(Position.ABSOLUTE);
		divBtn.setLeft(100);
		divBtn.setTop(50);
		divBtn.setWidth("176px");
		
		MaterialDropDown dropDown = new MaterialDropDown();
		dropDown.setActivator(uniqueId);
		dropDown.setConstrainWidth(false);
		dropDown.addSelectionHandler(event->{
			MaterialLink link = (MaterialLink)event.getSelectedItem();
			divBtn.setText(link.getText());
			inputBox.setLabel(link.getText());
		});
		
		MaterialLink link1 = new MaterialLink("태그 그룹명");
		MaterialLink link2 = new MaterialLink("태그명");
		
		dropDown.add(link1);
		dropDown.add(link2);

		this.add(inputBox);
		this.add(divBtn);
		this.add(dropDown);

		
		MaterialButton selectButton = new MaterialButton("검색");
		selectButton.setBackgroundColor(Color.RED_LIGHTEN_2);
		selectButton.addClickHandler(event->{
			getMaterialExtentsWindow().closeDialog();
		});
		this.addButton(selectButton);
		
		inputBox.setFocus(true);
	}

	@Override
	protected void onLoad() {
	       super.onLoad();

   }
	
	@Override
	public int getHeight() {
		return 200;
	}
}
