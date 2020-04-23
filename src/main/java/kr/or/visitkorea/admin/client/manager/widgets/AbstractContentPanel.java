package kr.or.visitkorea.admin.client.manager.widgets;

import java.util.Map;

import com.google.gwt.json.client.JSONObject;

import kr.or.visitkorea.admin.client.widgets.window.MaterialExtentsWindow;

abstract public class AbstractContentPanel extends AbstractLoaderableMaterialPanel {

	private MaterialExtentsWindow meWindow;
	private AbstractContentPanel parentPanel;
	private Map<String, Object> valueMap;
	
	public AbstractContentPanel() {
		super();
		this.setHeight("100%");
		init();
	}

	public AbstractContentPanel(AbstractContentPanel panel) {
		this.meWindow = panel.getMaterialExtentsWindow();
		this.parentPanel = panel;
		this.setHeight("100%");
		init();
	}

	public AbstractContentPanel(String... initialClass) {
		super(initialClass);
		this.setHeight("100%");
		init();
	}

	public AbstractContentPanel(MaterialExtentsWindow meWindow) {
		this.meWindow = meWindow;
		this.setWidth(this.meWindow.getWidth() + "px");
		this.setHeight("100%");
		init();
	}

	public void setMaterialExtentsWindow(MaterialExtentsWindow meWindow) {
		this.meWindow = meWindow;
	}
	
	public MaterialExtentsWindow getMaterialExtentsWindow() {
		return this.meWindow;
	}
	
	protected AbstractContentPanel getParentPanel() {
		return this.parentPanel;
	}
	
	protected String getString(JSONObject recObj, String key) {
		if (recObj.get(key) == null || recObj.get(key).isString().stringValue().equals("")) return "-";
		else return recObj.get(key).isString().stringValue();
	}
	
	abstract public void init();

	public void setWindowParameters(Map<String, Object> valueMap) {
		this.valueMap = valueMap;
	}

	public Map<String, Object> getWindowParameters() {
		return this.valueMap;
	}


}
