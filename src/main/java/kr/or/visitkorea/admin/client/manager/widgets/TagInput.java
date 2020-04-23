package kr.or.visitkorea.admin.client.manager.widgets;

import com.google.gwt.dom.client.Style.Position;

import gwt.material.design.addins.client.iconmorph.MaterialIconMorph;
import gwt.material.design.client.constants.Color;
import gwt.material.design.client.constants.IconSize;
import gwt.material.design.client.constants.IconType;
import gwt.material.design.client.constants.InputType;
import gwt.material.design.client.constants.TextAlign;
import gwt.material.design.client.ui.MaterialIcon;
import gwt.material.design.client.ui.MaterialInput;
import gwt.material.design.client.ui.MaterialPanel;

public class TagInput extends MaterialPanel{

	private MaterialIcon iconDelete;
	private int width;
	private MaterialIcon icon;
	private MaterialInput input;
	private int height;

	public TagInput(int width, int height) {
		super();
		this.width = width;
		this.height = height;
		init();
	}

	private void init() {
		
		this.setPaddingLeft(40);
		this.setTextAlign(TextAlign.LEFT);
		this.setWidth(width+"px");
		this.setLayoutPosition(Position.RELATIVE);
		this.setLeft(30);
		this.setHeight(this.height + "px");
		
		input = new MaterialInput(InputType.TEXT);
		input.setStyle("border-bottom: 0px solid #e0e0e0;");
		input.setLayoutPosition(Position.ABSOLUTE);
		input.setTop(0);
		input.setLeft(0);
		input.setBorderRadius("4px");
		input.setMargin(2);
		input.setPadding(4);
		input.setPaddingLeft(40);
		input.setPaddingRight(30);
		input.setHeight((this.height - 15 )+"px");
		input.setWidth((width-73)+"px");
		input.getElement().setAttribute("placeholder", "입력해 주세요.");
		
		MaterialIconMorph morph = new MaterialIconMorph();
		morph.setIconSize(IconSize.SMALL);
		
		icon = new MaterialIcon(IconType.CHECK);
		icon.setWidth("40px");
		icon.setHeight("40px");
		icon.setMargin(2);
		icon.setLineHeight(40);
		icon.setLeft(0);
		icon.setLayoutPosition(Position.ABSOLUTE);
		icon.setTextAlign(TextAlign.CENTER);
		icon.setTextColor(Color.BLUE);
		icon.setBorderRadius("4px 0px 0px 4px");
		
		morph.add(new MaterialIcon(IconType.CHECK_BOX));
		morph.add(new MaterialIcon(IconType.CHECK_BOX_OUTLINE_BLANK));
		morph.setLayoutPosition(Position.ABSOLUTE);
		morph.setTop(-3);

		iconDelete = new MaterialIcon(IconType.CLEAR);
		iconDelete.setLeft(100);
		iconDelete.setWidth("40px");
		iconDelete.setHeight("40px");
		iconDelete.setMargin(2);
		iconDelete.setLeft(this.width - 36);
		iconDelete.setLineHeight(40);
		iconDelete.setLayoutPosition(Position.ABSOLUTE);
		iconDelete.setTextAlign(TextAlign.CENTER);
		iconDelete.setTextColor(Color.BLUE);
		iconDelete.setTop(-3);
		iconDelete.setBorderRadius("4px 0px 0px 4px");

		iconDelete.addClickHandler(event->{
			input.setText("");
		});
		
		this.add(input);
		this.add(morph);
		this.add(iconDelete);
		
	}
	
	
}