package kr.or.visitkorea.admin.client.manager.monitoring.model;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.json.client.JSONNumber;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.json.client.JSONValue;

public class Group{

	public JSONNumber Level;
	public JSONNumber Repeat;
	public JSONNumber Report;
	public JSONString Title;
	public Monitor Monitor;
	public List<GroupUser> Users = new ArrayList<GroupUser>();
	public JSONNumber lastDuration;
	
	public Group() {
	}	
	
}
