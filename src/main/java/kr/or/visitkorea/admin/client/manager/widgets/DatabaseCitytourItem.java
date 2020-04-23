package kr.or.visitkorea.admin.client.manager.widgets;

import com.google.gwt.dom.client.Style;
import com.google.gwt.dom.client.Style.Position;

import gwt.material.design.client.constants.Color;
import gwt.material.design.client.constants.IconType;
import gwt.material.design.client.constants.TextAlign;
import gwt.material.design.client.ui.MaterialIcon;
import gwt.material.design.client.ui.MaterialLabel;
import gwt.material.design.client.ui.MaterialPanel;

public class DatabaseCitytourItem extends MaterialPanel {

	private MaterialLabel titleLabel;
	private MaterialPanel functionContainer;
	private MaterialIcon editFunction;
	private MaterialIcon saveFunction;
	private MaterialIcon deleteFunction;

	public DatabaseCitytourItem() {
		super();
		init();
	}

	public DatabaseCitytourItem(String... initialClass) {
		super(initialClass);
		init();
	}

	private void init() {
		
		this.setLayoutPosition(Position.RELATIVE);
		this.setHeight("100px");
		this.setWidth((941.74-20.0)+"px");
		this.setMarginTop(10);
		this.setMarginLeft(10);
		this.setMarginBottom(10);
		this.setBackgroundColor(Color.GREY_LIGHTEN_3);

		MaterialPanel titlePanel =new MaterialPanel();
		titlePanel.setLayoutPosition(Position.ABSOLUTE);
		titlePanel.setTop(0);
		titlePanel.setLeft(0);
		titlePanel.setWidth("100%");
		titlePanel.setHeight("24px");
		titlePanel.setBackgroundColor(Color.GREY_LIGHTEN_1);
		
		titleLabel = new MaterialLabel("제목 입니다. ");
		titleLabel.setLayoutPosition(Position.ABSOLUTE);
		titleLabel.setTextAlign(TextAlign.LEFT);
		titleLabel.setTop(0);
		titleLabel.setLeft(5);
		titleLabel.setHeight("24px");
		
		functionContainer = new MaterialPanel();
		functionContainer.setLayoutPosition(Position.ABSOLUTE);
		functionContainer.setTop(0);
		functionContainer.setRight(0);
		functionContainer.setWidth("100%");
		functionContainer.setHeight("24px");
		
		editFunction = new MaterialIcon(IconType.EDIT);
		editFunction.setFloat(Style.Float.RIGHT);
		editFunction.setMarginLeft(0);
		
		saveFunction = new MaterialIcon(IconType.SAVE);
		saveFunction.setFloat(Style.Float.RIGHT);
		saveFunction.setMarginLeft(0);
		
		deleteFunction = new MaterialIcon(IconType.REMOVE);
		deleteFunction.setFloat(Style.Float.RIGHT);
		deleteFunction.setMarginLeft(0);
		
		functionContainer.add(deleteFunction);
		functionContainer.add(saveFunction);
		functionContainer.add(editFunction);
		
		titlePanel.add(functionContainer);
		titlePanel.add(titleLabel);
		
		this.add(titlePanel);
		
	}
	
	
	public void setTitle(String title) {
		super.setTitle(title);
		this.titleLabel.setText(title);
	}

}
