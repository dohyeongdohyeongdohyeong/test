
package kr.or.visitkorea.admin.client.manager.tags.dialogs;

import com.google.gwt.dom.client.Style.Position;

import gwt.material.design.client.constants.Color;
import gwt.material.design.client.ui.MaterialButton;
import gwt.material.design.client.ui.MaterialCheckBox;
import gwt.material.design.client.ui.MaterialLabel;
import gwt.material.design.client.ui.MaterialPanel;
import gwt.material.design.client.ui.MaterialTextBox;
import kr.or.visitkorea.admin.client.widgets.dialog.DialogContent;
import kr.or.visitkorea.admin.client.widgets.window.MaterialExtentsWindow;

public class CreateGroupTagDialog extends DialogContent {


	private MaterialCheckBox checkBox;
	private MaterialLabel commentLabel;
	private MaterialTextBox inputBox;

	public CreateGroupTagDialog(MaterialExtentsWindow window) {
		super(window);
	}

	@Override
	public void init() {
	
	}

	@Override
	protected void onLoad() {
	       super.onLoad();
	       clear();
	       addDefaultButtons();
			
	        // dialog title define
			inputBox = new MaterialTextBox();
			inputBox.setLabel("생성 그룹 태그명");
			
			inputBox.addKeyUpHandler(event->{
				if (event.getNativeKeyCode() == 13) {
					checkDuplicateTagName(inputBox);
				}
			});
			
			inputBox.setLayoutPosition(Position.ABSOLUTE);
			inputBox.setRight(220);
			inputBox.setLeft(100);
			inputBox.setTop(30);
			
			checkBox = new MaterialCheckBox();
			checkBox.setText("중복 여부");
			checkBox.setEnabled(false);

			MaterialPanel checkBoxPanel = new MaterialPanel();
			checkBoxPanel.setLayoutPosition(Position.ABSOLUTE);
			checkBoxPanel.setRight(100);
			checkBoxPanel.setTop(70);
			checkBoxPanel.add(checkBox);
			
			commentLabel = new MaterialLabel();
			commentLabel.setLayoutPosition(Position.ABSOLUTE);
			commentLabel.setLeft(100);
			commentLabel.setRight(100);
			commentLabel.setTop(130);
		
			this.add(inputBox);
			this.add(checkBoxPanel);
			this.add(commentLabel);

			
			MaterialButton selectButton = new MaterialButton("생성");
			selectButton.setBackgroundColor(Color.RED_LIGHTEN_2);
			selectButton.addClickHandler(event->{
				getMaterialExtentsWindow().closeDialog();
			});
			this.addButton(selectButton);
			
			inputBox.setFocus(true);
   }
	
	private void checkDuplicateTagName(MaterialTextBox materialTextBox) {
		String targetSearchText = materialTextBox.getText();
		if (checkTagname(targetSearchText)) {
			checkBox.setValue(true);
			commentLabel.setText("[" + inputBox.getText() + "] 사용 가능합니다. 생성 버튼을 선택해서 생성을 완료해 주세요.");
		}else{
			checkBox.setValue(false);
			commentLabel.setText("입력한 정보로는 태그를 생성할 수 없습니다.");
		}
	}

	private boolean checkTagname(String targetSearchText) {
		return false;
	}

	@Override
	public int getHeight() {
		return 250;
	}
	
}
