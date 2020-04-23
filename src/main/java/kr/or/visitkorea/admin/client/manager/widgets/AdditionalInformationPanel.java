package kr.or.visitkorea.admin.client.manager.widgets;

import gwt.material.design.addins.client.tree.MaterialTree;
import gwt.material.design.addins.client.tree.MaterialTreeItem;
import gwt.material.design.client.constants.Color;
import gwt.material.design.client.constants.IconType;
import gwt.material.design.client.constants.TextAlign;

public class AdditionalInformationPanel extends AbstractContentPanel {

	@Override
	public void init() {
		this.setGrid("s12");
		this.setBackgroundColor(Color.BLUE_GREY_LIGHTEN_3);
		buildLayout();
	}

	private void buildLayout() {
		MaterialTree tree = new MaterialTree();
		tree.setGrid("s3");
		tree.setTextAlign(TextAlign.LEFT);
		
		MaterialTreeItem treeItem1 = new MaterialTreeItem("첫 번째", IconType.APPS);
		
		treeItem1.add(new MaterialTreeItem("차일드1"));
		treeItem1.add(new MaterialTreeItem("차일드2"));
		treeItem1.add(new MaterialTreeItem("차일드3"));
		treeItem1.add(new MaterialTreeItem("차일드4"));
		
		
		tree.add(treeItem1);
		
		MaterialTreeItem treeItem2 = new MaterialTreeItem("두 번째", IconType.APPS);
		
		treeItem2.add(new MaterialTreeItem("차일드1"));
		treeItem2.add(new MaterialTreeItem("차일드2"));
		treeItem2.add(new MaterialTreeItem("차일드3"));
		treeItem2.add(new MaterialTreeItem("차일드4"));
		
		
		tree.add(treeItem2);
		
		this.add(tree);
	}

}
