package kr.or.visitkorea.admin.client.manager.otherDepartment.composite;

import java.util.Map;

import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;

public interface ContentRow {

	public JSONObject getJSONObject();

	public void buildComponent(JSONObject recordObj);

	public void buildComponent(JSONArray recordObj);

	public void setCotId(String cotId);

	public void buildComponent();

	public String getParentHeight();

	public void setLayoutPosition(Position absolute);

	public void setTop(double i);

	public void setLeft(double i);

	int getParentCheckBoxLineHeight();

	public void setSelected(boolean selected);

	public Object getIdenifyedvalue();
	
	public Map<String, Object> getItems();

	public boolean isValidate();

	public void setRowTitle(String string);

}
