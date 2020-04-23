package kr.or.visitkorea.admin.client.manager.widgets;

import com.google.gwt.dom.client.Style.Cursor;
import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.dom.client.Style.Visibility;

import gwt.material.design.client.constants.Color;
import gwt.material.design.client.constants.IconType;
import gwt.material.design.client.constants.InputType;
import gwt.material.design.client.constants.TextAlign;
import gwt.material.design.client.ui.MaterialIcon;
import gwt.material.design.client.ui.MaterialInput;
import gwt.material.design.client.ui.MaterialLabel;
import gwt.material.design.client.ui.MaterialPanel;

public class ContentInput extends MaterialPanel{

	private MaterialIcon iconDelete;
	private MaterialIcon iconSave;

	private MaterialInput input;
	private MaterialLabel inputLabel;
	
	public ContentInput(InputType inputtype, String grid, String placeholder, String inputWidth, String labelWidth) {
		init();
		this.setGrid(grid);
		input.setWidth(inputWidth);
		inputLabel.setWidth(labelWidth);
		inputLabel.setHeight("46.25px");
		input.getElement().setAttribute("placeholder", placeholder);
		
	}

	private void init() {
		
		this.setTextAlign(TextAlign.LEFT);
		this.setLayoutPosition(Position.RELATIVE);
		
		inputLabel = new MaterialLabel();
		inputLabel.setTop(0);
		inputLabel.setLeft(20);
		inputLabel.setBackgroundColor(Color.GREY_LIGHTEN_4);
		inputLabel.addMouseOverHandler(event->{
			inputLabel.setBackgroundColor(Color.GREY_LIGHTEN_3);
			inputLabel.getElement().getStyle().setCursor(Cursor.POINTER);
		});
		inputLabel.addMouseOutHandler(event->{
			inputLabel.setBackgroundColor(Color.GREY_LIGHTEN_4);
			inputLabel.getElement().getStyle().setCursor(Cursor.DEFAULT);
		});
		inputLabel.addClickHandler(event->{
			inputLabel.setVisibility(Visibility.HIDDEN);
			inputLabel.setVisible(false);
			input.setVisibility(Visibility.VISIBLE);
			input.setVisible(true);
			input.setFocus(true);
			iconSave.setVisibility(Visibility.VISIBLE);
			iconSave.setVisible(true);
			iconDelete.setVisibility(Visibility.VISIBLE);
			iconDelete.setVisible(true);
		});
		
		input = new MaterialInput(InputType.TEXT);
		input.setVisible(false);
		input.setLayoutPosition(Position.ABSOLUTE);
		input.setTop(0);
		input.setLeft(20);
		input.addKeyUpHandler(event->{
			inputLabel.setText(input.getText());
			if (event.getNativeKeyCode() == 13) {
				save();
			}
		});
		
		iconSave = new MaterialIcon(IconType.SAVE);
		iconSave.setLayoutPosition(Position.ABSOLUTE);
		iconSave.setLineHeight(46);
		iconSave.setRight(10);
		iconSave.setTextAlign(TextAlign.CENTER);
		iconSave.setTextColor(Color.BLUE);
		iconSave.addClickHandler(event->{
			save();
		});
		
		iconDelete = new MaterialIcon(IconType.CLEAR);
		iconDelete.setLayoutPosition(Position.ABSOLUTE);
		iconDelete.setLineHeight(46);
		iconDelete.setRight(34);
		iconDelete.setTextAlign(TextAlign.CENTER);
		iconDelete.setTextColor(Color.BLUE);
		iconDelete.addClickHandler(event->{
			input.setText("");
		});

		this.add(inputLabel);
		this.add(input);
		this.add(iconDelete);
		this.add(iconSave);
		
	}

	private void save() {
		input.setVisibility(Visibility.HIDDEN);
		input.setVisible(false);
		inputLabel.setVisibility(Visibility.VISIBLE);
		inputLabel.setVisible(true);
		iconSave.setVisibility(Visibility.HIDDEN);
		iconSave.setVisible(false);
		iconDelete.setVisibility(Visibility.HIDDEN);
		iconDelete.setVisible(false);
	}

	public void setFocus(boolean bb) {
		input.setFocus(bb);
	}
	public void setValue(String value) {
		input.setValue(value);
	}
	public String getValue() {
		return input.getValue();
	}
}