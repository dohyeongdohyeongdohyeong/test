package kr.or.visitkorea.admin.client.manager.widgets;

import java.util.HashMap;
import java.util.List;
import java.util.Set;

import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.user.client.ui.Widget;

import gwt.material.design.client.base.MaterialWidget;
import gwt.material.design.client.constants.Color;
import gwt.material.design.client.constants.Display;
import gwt.material.design.client.constants.TextAlign;
import gwt.material.design.client.ui.MaterialLink;
import gwt.material.design.client.ui.MaterialPanel;

public class ItemSelectionPanel extends MaterialPanel {

	private HashMap<String, Object> keyMap;
	private MaterialLink selectedLink;

	public ItemSelectionPanel() {
		super();
		init(50, 150, 50);
	}

	public ItemSelectionPanel(String... initialClass) {
		super(initialClass);
		init(50, 150, 50);
	}

	public ItemSelectionPanel(int left, int top, int right) {
		super();
		init(left, top, right);
	}

	public ItemSelectionPanel(int left, int top, int right, HashMap<String, Object> keyMap) {
		super();
		this.keyMap = keyMap;
		init(left, top, right);
	}

	public HashMap<String, Object> getKeyMap() {
		return keyMap;
	}

	public void setKeyMap(HashMap<String, Object> keyMap) {
		this.keyMap = keyMap;
		this.clear();
	}

	public void init(int left, int top, int right) {
		
		this.setLayoutPosition(Position.ABSOLUTE);
		this.setLeft(left);
		this.setTop(top);
		this.setRight(right);
		this.setTextAlign(TextAlign.CENTER);
	}

	private MaterialWidget getPanel() {
		return this;
	}

	public void buildItem() {
		
		Set<String> keySet = keyMap.keySet();
		int i=0;
		for (String key : keySet) {
			MaterialLink mlink = new MaterialLink(key);
			if (i == 0) {
				this.selectedLink = mlink;
				this.selectedLink.setBackgroundColor(Color.RED_LIGHTEN_1);
				this.selectedLink.setTextColor(Color.WHITE);
				i++;
			}else {
				mlink.setTextColor(Color.BLUE);
				mlink.setBackgroundColor(Color.GREY_LIGHTEN_4);
			}
			mlink.setLineHeight(22);
			mlink.setMargin(5);
			mlink.setPadding(5);
			mlink.setDisplay(Display.INLINE_BLOCK);
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
				
				List<Widget> widgetList = getPanel().getChildrenList();
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
			add(mlink);
		}
	}
	
	public Object getSelectedValue() {
		return this.keyMap.get(selectedLink.getText());
	}

	public MaterialLink getSelectedItem() {
		return this.selectedLink;
	}
	
}