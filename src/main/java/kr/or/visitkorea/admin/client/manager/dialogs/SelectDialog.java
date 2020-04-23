package kr.or.visitkorea.admin.client.manager.dialogs;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.dom.client.Style.Position;

import gwt.material.design.client.ui.MaterialButton;
import gwt.material.design.client.ui.MaterialCheckBox;
import gwt.material.design.client.ui.MaterialColumn;
import gwt.material.design.client.ui.MaterialRow;
import kr.or.visitkorea.admin.client.manager.widgets.ContentTreeItem;
import kr.or.visitkorea.admin.client.widgets.dialog.DialogContent;
import kr.or.visitkorea.admin.client.widgets.window.MaterialExtentsWindow;

public class SelectDialog extends DialogContent {

	private ContentTreeItem target;
	private List<MaterialCheckBox> boxList = new ArrayList<MaterialCheckBox>();
	
	public SelectDialog(MaterialExtentsWindow window) {
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
		
	       target = (ContentTreeItem)this.getParameters().get("TARGET");
	       List<String> resultValue = (List<String>)this.getParameters().get("RESULT");
	       String[] dispValue = (String[])this.getParameters().get("VALUES");
	       String divValue = (String)this.getParameters().get("DIV");
	       
			MaterialRow row = new MaterialRow();
			row.setLayoutPosition(Position.RELATIVE);
			row.setTop(40);
			
			for (String value : dispValue) {
				
				MaterialColumn col = new MaterialColumn();
				col.setGrid(divValue);
				
				MaterialCheckBox box = new MaterialCheckBox();
				box.setText(value);
				
				box.setValue(resultValue.contains(value));
				
				col.add(box);
				boxList.add(box);
				
				row.add(col);
			}

			this.add(row);
			
			MaterialButton selectButton = new MaterialButton("선택");
			selectButton.addClickHandler(event->{
				
				List<String> resultList = new ArrayList<String>();
				
				for (MaterialCheckBox box : boxList) {
					if (box.getValue()) {
						resultList.add(box.getText());
					}
				}
				
				target.setDialogResult(resultList);
				getMaterialExtentsWindow().closeDialog();
			});
			this.addButton(selectButton);


   }

	@Override
	public int getHeight() {
		return 250;
	}
	
}
