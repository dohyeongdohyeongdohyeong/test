package kr.or.visitkorea.admin.client.widgets.dialog;

import com.google.gwt.dom.client.Style.Float;
import com.google.gwt.event.dom.client.ClickHandler;

import gwt.material.design.client.constants.Color;

public class ButtonInfo {
	
	private String name;
	
	private ClickHandler clickHandler;
	
	private Color color;
	
	private Float cssFloat;

	public ButtonInfo(String name, Color color, Float cssFloat, ClickHandler clickHandler) {
		super();
		this.name = name;
		this.clickHandler = clickHandler;
		this.color = color;
		this.cssFloat = cssFloat;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public ClickHandler getClickHandler() {
		return clickHandler;
	}

	public void setClickHandler(ClickHandler clickHandler) {
		this.clickHandler = clickHandler;
	}

	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}

	public Float getFloat() {
		return cssFloat;
	}

	public void setFloat(Float cssFloat) {
		this.cssFloat = cssFloat;
	}
	
}
