package kr.or.visitkorea.admin.client.manager.contents.database.widget;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.user.client.ui.Widget;

import gwt.material.design.client.base.MaterialWidget;
import gwt.material.design.client.ui.html.UnorderedList;

public class LinkDetailList extends UnorderedList {

	LinkDetail selectedItem = null;
	List<Widget> itemList = new ArrayList<>();
	
    public LinkDetailList() {
        super();
        this.getElement().getStyle().setProperty("listStyle", "none");
        this.getElement().getStyle().setProperty("width", "100%");
        this.getElement().getStyle().setProperty("position", "relative");
    }

    @Override
    public void add(Widget child) {
    	super.add(child);
    	this.itemList.add(child);
    }

    public List<Widget> getItemList() {
    	return this.itemList;
    }
    
	public LinkDetail getSelectedItem() {
		return selectedItem;
	}

	public int getSelectedItemIndex() {
		if (this.selectedItem != null) {
			return this.itemList.indexOf(this.selectedItem);
		}
		return -1;
	}
	
	public MaterialWidget setSelectedItem(LinkDetail linkDetail) {
		selectedItem = linkDetail;
		return selectedItem;
	}

}
