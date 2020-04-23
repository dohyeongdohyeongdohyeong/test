package kr.or.visitkorea.admin.client.manager.contents.components;

import com.google.gwt.dom.client.Style.Float;
import com.google.gwt.json.client.JSONNumber;
import com.google.gwt.json.client.JSONString;

import gwt.material.design.addins.client.richeditor.MaterialRichEditor;
import gwt.material.design.client.constants.Color;
import gwt.material.design.client.constants.IconType;
import gwt.material.design.client.ui.MaterialIcon;
import gwt.material.design.client.ui.MaterialPanel;

public class ContentComponentText extends AbstractContentComponent implements ContentComponent {
	private MaterialPanel bodyPanel;
	private MaterialRichEditor editor;
	private String contents;
	private MaterialIcon sizeIcon;
	private int sizeType;
	
	public ContentComponentText(ContentComponentType componentType) {
		super(componentType);
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
			this.getInternalObject().put("SIZE_TYPE", new JSONNumber(sizeType));
		});
		this.add(bodyPanel);
	}

	@Override
	protected void modifyClickAction() {
		if (!this.isEditMode) {
			this.bodyPanel.removeFromParent();
			
			this.editor = addEditor();
			this.editor.setValue(this.contents);
			this.add(this.editor);
			
			this.sizeIcon.setVisible(true);
			this.isEditMode = true;
		}
	}

	@Override
	protected void saveClickAction() {
		if (isEditMode) {
			this.editor.removeFromParent();
			
			this.bodyPanel = addPanel();
			this.bodyPanel.getElement().setInnerHTML(this.contents);
			this.add(this.bodyPanel);

			this.sizeIcon.setVisible(false);
			this.isEditMode = false;
		}
	}
	
	@Override
	public void setupContents() {
		this.contents = this.getInternalObject().containsKey("COMP_BODY") ? this.getInternalObject().get("COMP_BODY").isString().stringValue() : "";
		this.sizeType = (int) this.getInternalObject().get("SIZE_TYPE").isNumber().doubleValue();
		if (this.sizeType == 1) {
			this.sizeIcon.setTextColor(Color.RED);
			this.titleLabel.setText("텍스트 (와이드형)");
		} else {
			this.sizeIcon.setTextColor(Color.BLACK);
			this.titleLabel.setText("텍스트");
		}
		bodyPanel.getElement().setInnerHTML(this.contents);
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
		editor.addValueChangeHandler(e -> {
			this.contents = e.getValue();
			this.getInternalObject().put("COMP_BODY", new JSONString(this.contents));
		});
		return editor;
	}
}
