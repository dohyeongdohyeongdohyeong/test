package kr.or.visitkorea.admin.client.application;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import gwt.material.design.client.constants.IconType;

public class SubMenu {
	
	private String permId;
	
	private String parent;
	
	private String title;
	
	private IconType iconType;

	private String viewId;
	
	private int width = 1500;
	
	private int height = 700;
	
	private SubMenu parentMenu;
	
	private Map<String, Object> winParam;
	
	private List<SubMenu> children = new ArrayList<SubMenu>();

	public SubMenu(String viewId, String permId, String title, IconType iconType) {
		this.viewId = viewId;
		this.permId = permId;
		this.title = title;
		this.iconType = iconType;
		this.winParam = new HashMap<String, Object>();
	}
	
	public SubMenu(String viewId, String permId, String title, IconType iconType, SubMenu parentMenu) {
		this.viewId = viewId;
		this.permId = permId;
		this.title = title;
		this.iconType = iconType;
		this.parentMenu = parentMenu;
		this.winParam = new HashMap<String, Object>();
		
	}

	public SubMenu getParentMenu() {
		return parentMenu;
	}

	public void setParentMenu(SubMenu parentMenu) {
		this.parentMenu = parentMenu;
		this.parentMenu.addChild(this);
	}

	private void addChild(SubMenu subMenu) {
		this.children.add(subMenu);
	}
	
	public List<SubMenu> getChildren(){
		return this.children;
	}

	public Map<String, Object> getWinParam() {
		return winParam;
	}

	public void setWinParam(Map<String, Object> winParam) {
		this.winParam = winParam;
	}

	public String getPermId() {
		return permId;
	}

	public void setPermId(String permId) {
		this.permId = permId;
	}

	public String getViewId() {
		return viewId;
	}

	public void setViewId(String viewId) {
		this.viewId = viewId;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public String getParent() {
		return parent;
	}

	public void setParent(String parent) {
		this.parent = parent;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public IconType getIconType() {
		return iconType;
	}

	public void setIconType(IconType iconType) {
		this.iconType = iconType;
	}
	
	
}
