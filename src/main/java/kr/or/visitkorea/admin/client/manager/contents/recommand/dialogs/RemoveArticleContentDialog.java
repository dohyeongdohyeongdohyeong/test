
package kr.or.visitkorea.admin.client.manager.contents.recommand.dialogs;

import java.util.Map;

import com.google.gwt.dom.client.Style.FontWeight;
import com.google.gwt.dom.client.Style.Position;

import gwt.material.design.client.constants.Color;
import gwt.material.design.client.constants.TextAlign;
import gwt.material.design.client.ui.MaterialButton;
import gwt.material.design.client.ui.MaterialLabel;
import kr.or.visitkorea.admin.client.manager.widgets.ContentDetailItem;
import kr.or.visitkorea.admin.client.widgets.dialog.DialogContent;
import kr.or.visitkorea.admin.client.widgets.window.MaterialExtentsWindow;

public class RemoveArticleContentDialog extends DialogContent {


	public RemoveArticleContentDialog(MaterialExtentsWindow window) {
		super(window); 
	}

	@Override
	public void init() {

		addDefaultButtons();

		MaterialLabel ment00 = new MaterialLabel("정말 삭제 하시겠습니까?");
		ment00.setLayoutPosition(Position.ABSOLUTE);
		ment00.setFontWeight(FontWeight.BOLD);
		ment00.setFontSize("1.5em");
		ment00.setLeft(50);
		ment00.setTop(30);
		ment00.setRight(50);
		ment00.setTextAlign(TextAlign.CENTER);
		
		this.add(ment00);

		MaterialLabel ment01 = new MaterialLabel("현재 선택된 항목은 추가적인 정보를 가지고 있으며 삭제 시 포함된 모든 정보를 잃게 됩니다.");
		ment01.setLayoutPosition(Position.ABSOLUTE);
		ment00.setFontSize("1.2em");
		ment01.setLeft(50);
		ment01.setTop(80);
		ment01.setRight(50);
		ment01.setTextAlign(TextAlign.CENTER);
		
		this.add(ment01);

		MaterialButton selectButton = new MaterialButton("삭제");
		selectButton.setBackgroundColor(Color.RED_LIGHTEN_2);
		selectButton.addClickHandler(event->{
			
			Object itemObj = getParameters().get("ITEM");
			Object contentTree = getParameters().get("CONTENT_TREE");
			
			if (itemObj != null && contentTree != null) {
				ContentDetailItem cdi = (ContentDetailItem) itemObj;
				cdi.deleteData();
				getMaterialExtentsWindow().closeDialog();
			}
			
		});
		this.addButton(selectButton);
		
	}
	
	@Override
	public void setParameters(Map<String, Object> parameters) {
		super.setParameters(parameters);
	}

	@Override
	protected void onLoad() {
       super.onLoad();
   }
	
	@Override
	public int getHeight() {
		return 250;
	}
	
}
