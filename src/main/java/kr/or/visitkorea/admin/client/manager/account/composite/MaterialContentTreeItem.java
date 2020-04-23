package kr.or.visitkorea.admin.client.manager.account.composite;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.gwt.event.logical.shared.SelectionEvent;

import gwt.material.design.addins.client.tree.MaterialTreeItem;
import gwt.material.design.client.constants.Color;
import gwt.material.design.client.constants.IconType;

public class MaterialContentTreeItem extends MaterialTreeItem {
	
	private HashMap<String, Object> internalMap = new HashMap<String, Object>();

	@Override
    protected void onLoad() {
        super.onLoad();
	}
	
	@Override
    public void select() {
        SelectionEvent.fire(getTree(), this);
        List<MaterialTreeItem> treeItems = getTreeItems();
        for (MaterialTreeItem item : treeItems) {
        	item.setTextColor(Color.BLACK);
        }
    }

	public MaterialContentTreeItem(String stringValue, IconType permIdentity) {
		super(stringValue, permIdentity);
	}

	public int size() {
		return internalMap.size();
	}
	
	public Object get(String key) {
		return internalMap.get(key);
	}

	public Object put(String key, Object value) {
		return internalMap.put(key, value);
	}

	public void putAll(Map<? extends String, ? extends Object> m) {
		internalMap.putAll(m);
	}

	public Object remove(Object key) {
		return internalMap.remove(key);
	}

	public void clear() {
		internalMap.clear();
	}

	public Set<String> keySet() {
		return internalMap.keySet();
	}

	public Collection<Object> values() {
		return internalMap.values();
	}

	public boolean replace(String key, Object oldValue, Object newValue) {
		return internalMap.replace(key, oldValue, newValue);
	}

	public Object replace(String key, Object value) {
		return internalMap.replace(key, value);
	}
	

}
