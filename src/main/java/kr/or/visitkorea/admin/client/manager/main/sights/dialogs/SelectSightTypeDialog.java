
package kr.or.visitkorea.admin.client.manager.main.sights.dialogs;

import java.util.List;
import java.util.Map;

import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.user.client.ui.Widget;

import gwt.material.design.client.constants.Color;
import gwt.material.design.client.constants.Display;
import gwt.material.design.client.constants.TextAlign;
import gwt.material.design.client.ui.MaterialButton;
import gwt.material.design.client.ui.MaterialLabel;
import gwt.material.design.client.ui.MaterialLink;
import gwt.material.design.client.ui.MaterialPanel;
import kr.or.visitkorea.admin.client.widgets.dialog.DialogContent;
import kr.or.visitkorea.admin.client.widgets.window.MaterialExtentsWindow;

public class SelectSightTypeDialog extends DialogContent {


	private MaterialLink selectedLink;
			
	public SelectSightTypeDialog(MaterialExtentsWindow window) {
		super(window); 
	}

	@Override
	public void init() {

		addDefaultButtons();
		
		String[] autoTypeTextArray = {"이달의 명소","큐레이션 영역","요즘 많이 찾는 명소","소셜 버즈"};
		
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
			
			MaterialLabel cop = (MaterialLabel)this.getParameters().get("CONTENT_TITLE_LABEL");
			cop.setText("- 명소 메인 :: "+selectedLink.getText());
			getMaterialExtentsWindow().closeDialog();
			
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
		return 200;
	}
	
}
