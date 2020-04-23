package kr.or.visitkorea.admin.client.manager.widgets;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.gwt.user.client.ui.Widget;

import gwt.material.design.client.constants.Color;
import gwt.material.design.client.constants.Display;
import gwt.material.design.client.constants.TextAlign;
import gwt.material.design.client.ui.MaterialLink;
import gwt.material.design.client.ui.MaterialPanel;

public class SelectionPanel extends AbstractContentPanel {

	private Map<String, Object> map;
	private MaterialLink selecteBaseLink;
	private Set<String> divStringSet;
	private int elementMargin = 5;
	private int elementPadding = 5;
	private int elementRadius = 8;
	private boolean isSingleSelection = true;
	private Map<String, Boolean> multipleSelection = new HashMap<String, Boolean>();
	private Map<String, MaterialLink> referenceLinks = new HashMap<String, MaterialLink>();
	private StatusChangeEvent statusChangeEvent;
	private boolean isEnabled = true;

	@Override
	public void init() {
		
		this.setLeft(50);
		this.setTop(50);
		this.setRight(50);
		this.setTextAlign(TextAlign.CENTER);

	}
	
	public void setValues(Map<String, Object> parameters) {
		
		this.map = parameters;
		divStringSet = this.map.keySet();
		buildContent();
	}
	
	public String getSelectedText() {
		return this.selecteBaseLink.getText();
	}
	
	public List<String> getSelectedTexts() {
		
		List<String> retList = new ArrayList<String>();
		
		for (String key : divStringSet) {
			if (this.multipleSelection.get(key)) {
				retList.add(key);
			}
		}
		
		return retList;
	}
	
	public Object getSelectedValue() {
		if(this.selecteBaseLink == null)
			return null;
		return map.get(this.selecteBaseLink.getText());
	}
	
	public List<Object> getSelectedValues() {
		
		List<Object> retList = new ArrayList<Object>();
		
		for (String key : divStringSet) {
			if (this.multipleSelection.get(key)) {
				retList.add(this.map.get(key));
			}
		}
		
		return retList;
	}
	
	private AbstractContentPanel getPanel() {
		return this;
	}
	
	public void buildContent() {
		
		if (isSingleSelection == false) {
			for (String key : divStringSet) {
				this.multipleSelection.put(key, false);
			}
		}
		
		
		for (String strMember : divStringSet) {
			
			MaterialLink mlink = new MaterialLink(strMember);
	
			mlink.setTextColor(Color.BLUE);
			mlink.setBackgroundColor(Color.GREY_LIGHTEN_4);
/*
			if (isSingleSelection) {
				if (this.selecteBaseLink == null) {
					this.selecteBaseLink = mlink;
					this.selecteBaseLink.setTextColor(Color.WHITE);
					this.selecteBaseLink.setBackgroundColor(Color.RED_LIGHTEN_1);
					
				}else {
					
					mlink.setTextColor(Color.BLUE);
					mlink.setBackgroundColor(Color.GREY_LIGHTEN_4);
					
				}
			}
*/			
			mlink.setMargin(elementMargin);
			mlink.setPadding(elementPadding);
			mlink.setDisplay(Display.INLINE);
			mlink.setBorderRadius(this.elementRadius + "px");
			
			mlink.addMouseOutHandler(event->{
				if (!isEnabled)
					return;
				if (isSingleSelection) {
					if (selecteBaseLink == null || !mlink.equals(selecteBaseLink)) {
						mlink.setBackgroundColor(Color.GREY_LIGHTEN_4);
						mlink.setTextColor(Color.BLUE);
					}
				}else {
					if (!multipleSelection.get(mlink.getText())) {
						mlink.setBackgroundColor(Color.GREY_LIGHTEN_4);
						mlink.setTextColor(Color.BLUE);
					}
				}
			});
			
			mlink.addMouseOverHandler(event->{
				if (!isEnabled)
					return;
				if (isSingleSelection) {
					if (selecteBaseLink == null || !mlink.equals(selecteBaseLink)) {
						mlink.setBackgroundColor(Color.BLUE);
						mlink.setTextColor(Color.WHITE);
					}
				}else {
					if (!multipleSelection.get(mlink.getText())) {
						mlink.setBackgroundColor(Color.BLUE);
						mlink.setTextColor(Color.WHITE);
					}
				}
			});
			
			mlink.addClickHandler(event->{
				if (!isEnabled)
					return;
				
				if (isSingleSelection) {
					
					List<Widget> widgetList = getPanel().getChildrenList();
					for (Widget widget : widgetList) {
						if (widget instanceof MaterialLink) {
							MaterialLink tgrLink = (MaterialLink)widget;
							tgrLink.setBackgroundColor(Color.GREY_LIGHTEN_4);
							tgrLink.setTextColor(Color.BLUE);
						}
	
					}

					selecteBaseLink = mlink; 
					mlink.setBackgroundColor(Color.RED_LIGHTEN_1);
					mlink.setTextColor(Color.WHITE);
					if (this.statusChangeEvent != null) this.statusChangeEvent.fire(selecteBaseLink);

				}else {
					
					boolean mlinkStatus = multipleSelection.get(mlink.getText());
					if (mlinkStatus) {
						mlink.setBackgroundColor(Color.GREY_LIGHTEN_4);
						mlink.setTextColor(Color.BLUE);
					}else {
						mlink.setBackgroundColor(Color.RED_LIGHTEN_1);
						mlink.setTextColor(Color.WHITE);
					}
					
					multipleSelection.put(mlink.getText(), !mlinkStatus);
					if (this.statusChangeEvent != null) this.statusChangeEvent.fire(multipleSelection);
				}
				
			});
			
			referenceLinks.put(strMember, mlink);
			this.add(referenceLinks.get(strMember));
		}

	}

	public void setElementMargin(int margin) {
		this.elementMargin = margin;
	}

	public void setElementPadding(int padding) {
		this.elementPadding = padding;
	}

	public void setElementRadius(int radius) {
		this.elementRadius  = radius;
	}

	public void setSingleSelection(boolean isSingleSelection) {
		this.isSingleSelection = isSingleSelection;
	}
	
	public void selectBaseLinkReset() {
		selecteBaseLink = null;
	}
	
	public MaterialLink getselectBaseLink() {
		return selecteBaseLink;
	}

	public void setSelectionOnSingleMode(String key) {
		MaterialLink link = referenceLinks.get(key);
		
		this.reset();
		
		if (link != null) {
			link.setBackgroundColor(Color.RED_LIGHTEN_1);
			link.setTextColor(Color.WHITE);
			selecteBaseLink = link;
		}
	}
	
	public void setSelectionOnMultipleMode(String key, Boolean value) {
		
//		this.reset();
		
		if (key != null && key.length() > 0 && !key.equals("|")) {
			MaterialLink link = referenceLinks.get(key);
			link.setBackgroundColor(Color.RED_LIGHTEN_1);
			link.setTextColor(Color.WHITE);
			multipleSelection.put(key, value);
		}
	}
	
	public void reset() {
		Set<String> keys = referenceLinks.keySet();
		for (String key : keys) {
			MaterialLink link = referenceLinks.get(key);
			link.setBackgroundColor(Color.GREY_LIGHTEN_4);
			link.setTextColor(Color.BLUE);
			multipleSelection.put(key, false);
		}
		
	}

	public void setEnabled(boolean isEnable) {
		this.isEnabled = isEnable;
	}
	
	public boolean getEnabled() {
		return this.isEnabled;
	}
	
	public void addStatusChangeEvent(StatusChangeEvent statusChangeEvent) {
		this.statusChangeEvent = statusChangeEvent;
	}

}
