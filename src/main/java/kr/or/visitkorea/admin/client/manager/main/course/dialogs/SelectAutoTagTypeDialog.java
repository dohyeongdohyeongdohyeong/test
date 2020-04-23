
package kr.or.visitkorea.admin.client.manager.main.course.dialogs;

import java.util.List;
import java.util.Map;

import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.user.client.ui.Widget;

import gwt.material.design.client.constants.Color;
import gwt.material.design.client.constants.Display;
import gwt.material.design.client.constants.TextAlign;
import gwt.material.design.client.ui.MaterialButton;
import gwt.material.design.client.ui.MaterialLink;
import gwt.material.design.client.ui.MaterialPanel;
import kr.or.visitkorea.admin.client.widgets.dialog.DialogContent;
import kr.or.visitkorea.admin.client.widgets.window.MaterialExtentsWindow;

public class SelectAutoTagTypeDialog extends DialogContent {


	private MaterialLink selectedLink;
			
	public SelectAutoTagTypeDialog(MaterialExtentsWindow window) {
		super(window); 
	}

	@Override
	public void init() {

		addDefaultButtons();
		
		String[] autoTypeTextArray = {"1일 전 빈도 적용","7일 전 빈도 적용","한달 전 빈도 적용","누적 빈도 적용"};
		
		MaterialPanel tagSelectionPanel = new MaterialPanel();
		tagSelectionPanel.setLayoutPosition(Position.ABSOLUTE);
		tagSelectionPanel.setLeft(50);
		tagSelectionPanel.setTop(50);
		tagSelectionPanel.setRight(50);
		tagSelectionPanel.setTextAlign(TextAlign.CENTER);
		
		for (int i=0; i<4; i++) {
			
			
			MaterialLink mlink = new MaterialLink(autoTypeTextArray[i]);

			mlink.setBackgroundColor(Color.GREY_LIGHTEN_4);
			mlink.setTextColor(Color.BLUE);
			mlink.setMargin(5);
			mlink.setPadding(5);
			mlink.setPaddingLeft(10);
			mlink.setPaddingRight(10);
			mlink.setDisplay(Display.INLINE);
			mlink.setBorderRadius("8px");

			mlink.addMouseOutHandler(event->{
				if (selectedLink == null || !mlink.equals(selectedLink)) {
					mlink.setBackgroundColor(Color.GREY_LIGHTEN_4);
					mlink.setTextColor(Color.BLUE);
				}
			});
			mlink.addMouseOverHandler(event->{
				if (selectedLink == null || !mlink.equals(selectedLink)) {
					mlink.setBackgroundColor(Color.BLUE);
					mlink.setTextColor(Color.WHITE);
				}
			});
			mlink.addClickHandler(event->{
				
				List<Widget> widgetList = tagSelectionPanel.getChildrenList();
				for (Widget widget : widgetList) {
					if (widget instanceof MaterialLink) {
						MaterialLink tgrLink = (MaterialLink)widget;
						tgrLink.setBackgroundColor(Color.GREY_LIGHTEN_4);
						tgrLink.setTextColor(Color.BLUE);
					}
				}
				
				selectedLink = mlink; 
				mlink.setBackgroundColor(Color.RED_LIGHTEN_1);
				mlink.setTextColor(Color.WHITE);
				
			});
			tagSelectionPanel.add(mlink);
		}

		this.add(tagSelectionPanel);

		MaterialButton selectButton = new MaterialButton("선택");
		selectButton.setBackgroundColor(Color.RED_LIGHTEN_2);
		selectButton.addClickHandler(event->{
			
			MaterialLink cop = (MaterialLink)this.getParameters().get("AUTO_SELECTION_LABEL");
			cop.setText(selectedLink.getText());
			getMaterialExtentsWindow().closeDialog();
			
		});
		this.addButton(selectButton);
		
	}
	
	@Override
	public void setParameters(Map<String, Object> parameters) {
		super.setParameters(parameters);
		
		MaterialLink cop = (MaterialLink)this.getParameters().get("AUTO_SELECTION_LABEL");
		
		List<Widget> children = this.getChildrenList();
		
		for (Widget child : children) {
			
			if (child instanceof MaterialLink) {
				MaterialLink clink = (MaterialLink) child;
				if (cop.getText().equals(clink.getText())){
					clink.setBackgroundColor(Color.RED_LIGHTEN_1);
					clink.setTextColor(Color.WHITE);
				}
			}
		}
		
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
