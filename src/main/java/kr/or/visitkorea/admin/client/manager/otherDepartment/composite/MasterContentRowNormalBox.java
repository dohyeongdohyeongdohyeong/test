package kr.or.visitkorea.admin.client.manager.otherDepartment.composite;

import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.user.client.ui.Widget;

import gwt.material.design.client.ui.MaterialCheckBox;
import gwt.material.design.client.ui.MaterialPanel;

public class MasterContentRowNormalBox extends MaterialPanel implements ContentRowAdapter{

	private CustomRowTable table;
	private MaterialCheckBox selectCheckBox;
	private ContentRow contentRow;
	
	public MasterContentRowNormalBox(CustomRowTable customRowTable) {
		super();
		this.table = customRowTable;
		init();
	}

	public MasterContentRowNormalBox(String... initialClass) {
		super(initialClass);
		init();
	}
	
	private MasterContentRowNormalBox getPanel() {
		return this;
	}

	private void init() {
		
		this.setLayoutPosition(Position.RELATIVE);
		this.setWidth("100%");
		this.setHeight("210px");
		this.setBorderBottom("2px dashed #1E90FF");
		this.setMarginBottom(10);
		this.setMarginTop(10);
		this.setPadding(5);
	}
	
	public void addPanel(ContentRow child) {
		
		this.setHeight(child.getParentHeight());
		child.setLayoutPosition(Position.ABSOLUTE);
		child.setTop(0);
		child.setLeft(50);
		contentRow = (ContentRow) child;
		this.add((Widget) child);
	}
	
	public void setCheckBoxValue(boolean cbv) {
		selectCheckBox.setValue(cbv);
	}
	
	public boolean isChecked() {
		return this.selectCheckBox.getValue();
	}

	public ContentRow getContentRow() {
		return this.contentRow;
	}

}
