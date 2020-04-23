package kr.or.visitkorea.admin.client.manager.event.composite;

import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.json.client.JSONObject;

import gwt.material.design.addins.client.richeditor.MaterialRichEditor;
import gwt.material.design.client.constants.Color;
import kr.or.visitkorea.admin.client.manager.event.widgets.FullTitleContentRow;

public class ContentsBaseCaution extends FullTitleContentRow {
	private MaterialRichEditor cautionEditor;
	private String value = "";
	
	public ContentsBaseCaution() {
		super();
		init();
	}

	public ContentsBaseCaution(AbstractEventContents host, String title, Color color) {
		super(host, title, color);
		init();
	}
	
	private void init() {
		this.cautionEditor = new MaterialRichEditor();
		this.cautionEditor.setHeight("200px");
		this.cautionEditor.addValueChangeHandler(e -> {
			this.value = e.getValue();
		});
	}

	@Override
	protected void setValue(JSONObject obj) {
		this.value = obj.containsKey("CAUTION") ? obj.get("CAUTION").isString().stringValue() : "";
		this.getContentRow().getElement().setInnerHTML(this.value);
	}
	
	@Override
	protected ClickHandler addSaveIconClickEvent() {
		return event -> {
			this.cautionEditor.removeFromParent();
			this.getContentRow().getElement().setInnerHTML(value);
		};
	}

	@Override
	protected ClickHandler addEditIconClickEvent() {
		return event -> {
			this.cautionEditor.setHTML(this.value);
			this.getContentRow().getElement().setInnerHTML("");
			this.getContentRow().add(this.cautionEditor);
		};
	}

	@Override
	protected JSONObject buildModel() {
		return null;
	}
	
	public String getCaution() {
		return this.value;
	}
}
