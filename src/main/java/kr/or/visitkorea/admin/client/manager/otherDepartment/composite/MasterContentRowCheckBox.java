package kr.or.visitkorea.admin.client.manager.otherDepartment.composite;

import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.user.client.ui.Widget;

import gwt.material.design.client.ui.MaterialCheckBox;
import gwt.material.design.client.ui.MaterialPanel;

public class MasterContentRowCheckBox extends MaterialPanel implements ContentRowAdapter{

	private CustomRowTable table;
	private MaterialCheckBox selectCheckBox;
	private ContentRow contentRow;
	private MaterialPanel checkBoxPanel;
	
	public MasterContentRowCheckBox(CustomRowTable customRowTable) {
		super();
		this.table = customRowTable;
		init();
	}

	public MasterContentRowCheckBox(String... initialClass) {
		super(initialClass);
		init();
	}
	
	private MasterContentRowCheckBox getPanel() {
		return this;
	}

	private void init() {
		
		this.setLayoutPosition(Position.RELATIVE);
		this.setWidth("100%");
		this.setBorderBottom("2px dashed #1E90FF");
		this.setPadding(5);
		
		checkBoxPanel = new MaterialPanel();
		checkBoxPanel.setLayoutPosition(Position.ABSOLUTE);
		checkBoxPanel.setLeft(20);
		checkBoxPanel.setTop(0);
		checkBoxPanel.setLineHeight(180);
		
		selectCheckBox = new MaterialCheckBox();
		selectCheckBox.setEnabled(true);
		selectCheckBox.setValue(false);
		selectCheckBox.addValueChangeHandler(event->{
			
			table.setSelectedPanel(getPanel());
			
		});

		checkBoxPanel.add(selectCheckBox);
		this.add(checkBoxPanel);
			
	}
	
	public void addPanel(ContentRow child) {
		String parentHeight = child.getParentHeight();
		String parentNewString = parentHeight.substring(0, parentHeight.lastIndexOf("p"));
		int pHeight = Integer.parseInt(parentNewString);
		this.setHeight(child.getParentHeight());
		
		checkBoxPanel.setLineHeight(child.getParentCheckBoxLineHeight());
		child.setLayoutPosition(Position.ABSOLUTE);
		child.setTop(10);
		child.setLeft(50);
		contentRow = (ContentRow) child;
		this.add((Widget) child);
	}
	
	public MaterialPanel getCheckBoxPanel() {
		return this.checkBoxPanel;
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
