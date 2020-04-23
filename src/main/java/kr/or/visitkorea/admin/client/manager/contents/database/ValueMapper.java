package kr.or.visitkorea.admin.client.manager.contents.database;

import java.util.Map;

import com.google.gwt.json.client.JSONObject;

import kr.or.visitkorea.admin.client.manager.widgets.ContentTreeItem;

public interface ValueMapper {
	
	public void setupValue(Map<String, ContentTreeItem> items, JSONObject resultObject);

}
