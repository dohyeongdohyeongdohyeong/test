package kr.or.visitkorea.admin.client.manager.widgets.editor.models;

import java.util.List;
import java.util.Map;

import gwt.material.design.client.ui.MaterialIcon;
import kr.or.visitkorea.admin.client.widgets.dialog.DialogContent;

public abstract class AbstractEditComponent implements EditComponent {

	private MaterialIcon icon;
	
	private List<PropertyUnit> properties;
	
	private Map<String, DialogContent> dialogs;
	
	public MaterialIcon getIcon() {
		return icon;
	}
	
	public void setIcon(MaterialIcon icon) {
		this.icon = icon;
	}
	
	public List<PropertyUnit> getProperties() {
		return properties;
	}
	
	public void setProperties(List<PropertyUnit> properties) {
		this.properties = properties;
	}
	
	public Map<String, DialogContent> getDialogs() {
		return dialogs;
	}
	
	public void setDialogs(Map<String, DialogContent> dialogs) {
		this.dialogs = dialogs;
	}
	
	public void appendDialogs(String key, DialogContent value) {
		this.dialogs.put(key, value);
	}
	
	
}
