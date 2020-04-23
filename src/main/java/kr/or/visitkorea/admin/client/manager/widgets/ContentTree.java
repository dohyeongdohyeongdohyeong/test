package kr.or.visitkorea.admin.client.manager.widgets;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.user.client.ui.Widget;

import gwt.material.design.addins.client.tree.MaterialTree;
import kr.or.visitkorea.admin.client.manager.contents.database.ValueMapper;

public class ContentTree extends MaterialTree {
	
	private Map<String, ContentTreeItem> items = new HashMap<String, ContentTreeItem>();
	private JSONObject resultObject;
	private ValueMapper valueMapper;
	private String atmId;
	
    public ContentTree() {
		super();
		this.setLayoutPosition(Position.RELATIVE);
	}
    
    public ContentTreeItem getItem(String key) {
    	return this.items.get(key);
    }

    @Override
	public void onLoad() {
        super.onLoad();
        
        for (Widget child : getChildren()) {
        	appendItems(child);
        }

    }
	
    private void appendItems(Widget child) {
    	if (child instanceof ContentTreeItem) {
	    	ContentTreeItem item = (ContentTreeItem) child;
	    	String itemid = "";
	    	if (item.getParent() instanceof ContentTreeItem) {
	    		ContentTreeItem tpItem = (ContentTreeItem) item.getParent();
	    		itemid = tpItem.getText() + "." + item.getText();
	    	}else {
	    		itemid = item.getText();
	    	}
	    	
	    	this.items.put(itemid, item);
	    	
	    	for (Widget tgrChild : item.getChildren()) {
	        	appendItems(tgrChild);
	        }
    	}
    }
    
    public Set<String> getKeys() {
    	return this.items.keySet();
    }
    
	public void setupResult(JSONObject resultObject) {
		this.resultObject = resultObject;
	}

	public void setupValueMapper(ValueMapper valueMapper) {
		this.valueMapper = valueMapper;
		this.valueMapper.setupValue(items, this.resultObject);
	}

	public String getAtmId() {
		return atmId;
	}

	public void setAtmId(String atmId) {
		this.atmId = atmId;
	}

	
}
