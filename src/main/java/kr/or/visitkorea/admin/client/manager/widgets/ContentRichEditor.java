package kr.or.visitkorea.admin.client.manager.widgets;

import com.google.gwt.dom.client.Style.Cursor;
import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.dom.client.Style.Visibility;

import gwt.material.design.addins.client.richeditor.MaterialRichEditor;
import gwt.material.design.addins.client.richeditor.base.constants.ToolbarButton;
import gwt.material.design.client.constants.Color;
import gwt.material.design.client.constants.IconType;
import gwt.material.design.client.constants.TextAlign;
import gwt.material.design.client.ui.MaterialIcon;
import gwt.material.design.client.ui.MaterialPanel;

public class ContentRichEditor extends MaterialPanel{

	private MaterialIcon iconDelete;
	private MaterialIcon iconSave;

	private MaterialRichEditor input;
	private MaterialPanel inputPanel;
	
	public ContentRichEditor() {
		init();
	}

	public String getHTML() {
		return input.getHTML();
	}
	public void setHTML(String html) {
		input.setHTML(html);
	}
	private void init() {
		
		this.setTextAlign(TextAlign.LEFT);
		this.setLayoutPosition(Position.RELATIVE);
		
		inputPanel = new MaterialPanel();
		inputPanel.setTop(0);
		inputPanel.setLeft(20);
		inputPanel.setBackgroundColor(Color.GREY_LIGHTEN_4);
		inputPanel.addMouseOverHandler(event->{
			inputPanel.setBackgroundColor(Color.GREY_LIGHTEN_3);
			inputPanel.getElement().getStyle().setCursor(Cursor.POINTER);
		});
		inputPanel.addMouseOutHandler(event->{
			inputPanel.setBackgroundColor(Color.GREY_LIGHTEN_4);
			inputPanel.getElement().getStyle().setCursor(Cursor.DEFAULT);
		});
		inputPanel.addClickHandler(event->{
			inputPanel.setVisibility(Visibility.HIDDEN);
			inputPanel.setVisible(false);
			input.setVisibility(Visibility.VISIBLE);
			input.setVisible(true);
			input.setFocus(true);
			iconSave.setVisibility(Visibility.VISIBLE);
			iconSave.setVisible(true);
			iconDelete.setVisibility(Visibility.VISIBLE);
			iconDelete.setVisible(true);
		});
		
		input = new MaterialRichEditor();
		input.setMiscOptions(ToolbarButton.CODE_VIEW);
		input.setUndoOptions();
		input.setHeight("300px");
		input.setVisible(false);
		input.setLayoutPosition(Position.ABSOLUTE);
		input.setTop(0);
		input.setLeft(20);
		input.addKeyUpHandler(event->{
			inputPanel.getElement().setInnerHTML(input.getText());
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

		this.add(inputPanel);
		this.add(input);
		
//		this.add(iconDelete);
//		this.add(iconSave);
		
	}

	private void save() {
		input.setVisibility(Visibility.HIDDEN);
		input.setVisible(false);
		inputPanel.setVisibility(Visibility.VISIBLE);
		inputPanel.setVisible(true);
		iconSave.setVisibility(Visibility.HIDDEN);
		iconSave.setVisible(false);
		iconDelete.setVisibility(Visibility.HIDDEN);
		iconDelete.setVisible(false);
	}
	
	
}