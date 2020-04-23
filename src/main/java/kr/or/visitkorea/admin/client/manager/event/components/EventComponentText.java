package kr.or.visitkorea.admin.client.manager.event.components;

import com.google.gwt.dom.client.Style.Float;
import com.google.gwt.user.client.Timer;

import gwt.material.design.addins.client.richeditor.MaterialRichEditor;
import gwt.material.design.client.base.MaterialWidget;
import gwt.material.design.client.constants.Color;
import gwt.material.design.client.constants.IconType;
import gwt.material.design.client.constants.TextAlign;
import gwt.material.design.client.ui.MaterialIcon;
import gwt.material.design.client.ui.MaterialLabel;
import gwt.material.design.client.ui.MaterialPanel;
import kr.or.visitkorea.admin.client.application.Console;
import kr.or.visitkorea.admin.client.manager.widgets.AbstractContentPanel;

public class EventComponentText extends AbstractEventComponent {
	private MaterialPanel bodyPanel;
	private MaterialRichEditor editor;
	private String contents;
	private MaterialIcon sizeIcon;
	private int sizeType;
	private String mode;
	
	public EventComponentText(EventComponentType componentType, AbstractContentPanel host,String mode) {
		super(componentType, host);
		this.mode = mode;
	}

	@Override
	protected void init() {
		bodyPanel = addPanel();
		
		this.sizeIcon = this.addIcon(this.titleRow, IconType.CROP_7_5, Float.RIGHT);
		this.sizeIcon.setVisible(false);
	
		this.sizeIcon.setTooltip("와이드 형");
		this.sizeIcon.addClickHandler(e -> {
			if (this.sizeType == 1) {
				this.sizeIcon.setTextColor(Color.BLACK);
				this.titleLabel.setText("텍스트");
			} else {
				this.sizeIcon.setTextColor(Color.RED);
				this.titleLabel.setText("텍스트 (와이드형)");
			}
			this.sizeType = this.sizeType == 1 ? 0 : 1;
			this.getComponentObj().setSizeType(sizeType);
		});
		this.add(bodyPanel);
	}

	@Override
	protected void modifyClickAction() {
		this.bodyPanel.removeFromParent();
		
		this.editor = addEditor();
		this.editor.setValue(this.contents);
		this.add(this.editor);
		editor.getEditor().find("div[class='note-toolbar btn-toolbar']").attr("style", "z-index: 0 !important");
		this.sizeIcon.setVisible(true);
		if(mode == "result")
			this.sizeIcon.setVisible(false);
				
	}

	@Override
	protected void saveClickAction() {
		this.editor.removeFromParent();
		
		this.bodyPanel = addPanel();
		this.bodyPanel.getElement().setInnerHTML(this.contents);
		this.add(this.bodyPanel);

		this.sizeIcon.setVisible(false);
	}
	
	@Override
	public void setupContents() {
		this.contents = this.getComponentObj().getCompBody();
		this.sizeType = this.getComponentObj().getSizeType();
		if (this.sizeType == 1) {
			this.sizeIcon.setTextColor(Color.RED);
			this.titleLabel.setText("텍스트 (와이드형)");
		} else {
			this.sizeIcon.setTextColor(Color.BLACK);
			this.titleLabel.setText("텍스트");
		}
		bodyPanel.getElement().setInnerHTML(this.contents);
	}
	
	@Override
	public void refresh() {
		
	}

	private MaterialPanel addPanel() {
		MaterialPanel panel = new MaterialPanel();
		panel.setBackgroundColor(Color.GREY_LIGHTEN_5);
		panel.setWidth("100%");
		panel.setPadding(10);
		return panel;
	}
	
	private MaterialRichEditor addEditor() {
		MaterialRichEditor editor = new MaterialRichEditor();
		editor.setWidth("100%");
		editor.setHeight("200px");
		editor.setTextAlign(TextAlign.CENTER);
		editor.addValueChangeHandler(e -> {
			this.contents = e.getValue();
			this.getComponentObj().setCompBody(this.contents);
		});
		return editor;
	}
	
	public MaterialIcon getSizeIcon() {
		return this.sizeIcon;
	}
	
	public MaterialLabel getTitleLabel() {
		return this.titleLabel;
	}
}
