
package kr.or.visitkorea.admin.client.manager.main.festival.dialogs;

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
import gwt.material.design.client.ui.MaterialTextBox;
import kr.or.visitkorea.admin.client.widgets.dialog.DialogContent;
import kr.or.visitkorea.admin.client.widgets.window.MaterialExtentsWindow;

public class SearchContentsDialog extends DialogContent {


	private MaterialLink selectedLink;
private MaterialTextBox searchBox;
			
	public SearchContentsDialog(MaterialExtentsWindow window) {
		super(window); 
	}

	@Override
	public void init() {

		addDefaultButtons();
/*		
		MaterialListBox listBox = new MaterialListBox();
		listBox.setPlaceholder("검색 구분을 선택해 주세요.");
		listBox.add(new Option("CID"));
		listBox.add(new Option("축제명"));
		listBox.setLayoutPosition(Position.ABSOLUTE);
		listBox.setLeft(50);
		listBox.setTop(50);
		listBox.setWidth("150px");
		listBox.setTextAlign(TextAlign.CENTER);
		
		this.add(listBox);
*/		
		String[] autoTypeTextArray = {"CID","축제명"};
		
		MaterialPanel tagSelectionPanel = new MaterialPanel();
		tagSelectionPanel.setLayoutPosition(Position.ABSOLUTE);
		tagSelectionPanel.setLeft(50);
		tagSelectionPanel.setTop(30);
		tagSelectionPanel.setRight(50);
		tagSelectionPanel.setTextAlign(TextAlign.CENTER);
		
		for (int i=0; i<autoTypeTextArray.length; i++) {
			
			
			MaterialLink mlink = new MaterialLink(autoTypeTextArray[i]);

			if (i==0) {
				selectedLink = mlink;
				mlink.setBackgroundColor(Color.RED_LIGHTEN_1);
				mlink.setTextColor(Color.WHITE);
			}else {
				mlink.setBackgroundColor(Color.GREY_LIGHTEN_4);
				mlink.setTextColor(Color.BLUE);
			
			}
			
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
		
		searchBox = new MaterialTextBox();
		searchBox.setPlaceholder("검색할 단어를 입력해 주세요.");
		searchBox.setLabel("검색어");
		searchBox.setLayoutPosition(Position.ABSOLUTE);
		searchBox.setLeft(50);
		searchBox.setTop(60);
		searchBox.setRight(50);
		searchBox.setTextAlign(TextAlign.CENTER);
		searchBox.addKeyUpHandler(event->{
			if (event.getNativeKeyCode() == 13) {
				
				MaterialLabel contentTitle = (MaterialLabel)this.getParameters().get("CONTENT_TITLE_LABEL");
				contentTitle.setText("- " + selectedLink.getText() + " 기준 검색 :: " +  searchBox.getValue() );
				getMaterialExtentsWindow().closeDialog();
				
			}
		});
		
		this.add(searchBox);

		MaterialButton selectButton = new MaterialButton("검색");
		selectButton.setBackgroundColor(Color.RED_LIGHTEN_2);
		selectButton.addClickHandler(event->{
			
			MaterialLabel contentTitle = (MaterialLabel)this.getParameters().get("CONTENT_TITLE_LABEL");
			contentTitle.setText("- " + selectedLink.getText() + " 기준 검색 :: " +  searchBox.getValue() );
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
		return 250;
	}
	
}
