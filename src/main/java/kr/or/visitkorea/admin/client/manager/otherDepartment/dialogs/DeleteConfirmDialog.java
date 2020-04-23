package kr.or.visitkorea.admin.client.manager.otherDepartment.dialogs;

import java.util.Map;

import com.google.gwt.dom.client.Style.FontWeight;
import com.google.gwt.dom.client.Style.Position;

import gwt.material.design.client.constants.Color;
import gwt.material.design.client.ui.MaterialButton;
import gwt.material.design.client.ui.MaterialLabel;
import gwt.material.design.client.ui.MaterialListBox;
import gwt.material.design.client.ui.MaterialPanel;
import gwt.material.design.client.ui.MaterialTextBox;
import kr.or.visitkorea.admin.client.application.Registry;
import kr.or.visitkorea.admin.client.manager.otherDepartment.composite.ContentDetail;
import kr.or.visitkorea.admin.client.manager.otherDepartment.composite.CustomRowTable;
import kr.or.visitkorea.admin.client.manager.otherDepartment.composite.marketing.MarketingContentDetail;
import kr.or.visitkorea.admin.client.manager.otherDepartment.composite.marketing.MarketingDetailPanel;
import kr.or.visitkorea.admin.client.manager.widgets.ContentTable;
import kr.or.visitkorea.admin.client.widgets.dialog.DialogContent;
import kr.or.visitkorea.admin.client.widgets.window.MaterialExtentsWindow;

public class DeleteConfirmDialog extends DialogContent {

	private ContentTable table;
	private MaterialListBox listBox;
	private MaterialTextBox searchBox;
	private String contentType;
	private MaterialLabel dialogTitle;
	private boolean multipleContents;
	private ContentTable targetTable;
	private String tbl;
	private String cotId;
	private Object tgrPanel;
	private String otdId;
	private String manId;
	private MaterialLabel contentsInfo;
	private MaterialButton selectButton;

	public DeleteConfirmDialog(MaterialExtentsWindow window) {
		super(window);
	}

	@Override
	public void init() {
		buildContent();
	}

	@Override
	protected void onLoad() {
		super.onLoad();
		
		manId = (String) getParameters().get("MAN_ID");
		contentsInfo.setText((String) getParameters().get("CONTENT_INFO"));
		
		selectButton.setEnabled(true);
		okButton.setEnabled(true);
		if (manId.equals("05427f59-a30a-11e8-8165-020027310001")) { // PC 버전 기준
			// 쇼케이스
			selectButton.setEnabled(Registry.getPermission("0b91626f-406b-4241-b050-bb72e20342b2"));
			okButton.setEnabled(Registry.getPermission("fce8ad68-8aab-4f6e-87dd-7fa41bd61ffc"));
			
			// 큐레이션
			selectButton.setEnabled(Registry.getPermission("7fe99d2f-fbe1-4d68-b46f-15209c455957"));
			okButton.setEnabled(Registry.getPermission("b6fa204f-d529-4881-9795-2f2766d08b12"));
			
			// 마케팅
			selectButton.setEnabled(Registry.getPermission("01474679-b8a5-4b99-aa33-bac7a92e8c66"));
			okButton.setEnabled(Registry.getPermission("4e4170f6-7179-49fe-9a80-adf130301f48"));
		} else if (manId.equals("9ab9e7af-ba3a-472b-a8bb-3203aa21d0a2")) { // 모바일 버전 기준
			// 쇼케이스
			selectButton.setEnabled(Registry.getPermission("dd7ddc97-2503-4c6c-b29f-63a909f366b3"));
			okButton.setEnabled(Registry.getPermission("18cb99e7-b89f-442f-b56e-8369cc53d275"));
			
			// 큐레이션
			selectButton.setEnabled(Registry.getPermission("0d0da6e3-7ad9-44e6-9145-902ff5521569"));
			okButton.setEnabled(Registry.getPermission("8ab6fc12-8556-4307-ad89-f73d71206a72"));
			
			// 마케팅
			selectButton.setEnabled(Registry.getPermission("8a29fd21-40d8-4c60-b599-916479669c11"));
			okButton.setEnabled(Registry.getPermission("40dc5537-9d8c-44d2-95e3-7f9e382d370d"));
		}
		
	}

	public void buildContent() {
		
		addDefaultButtons();

		// dialog title define
		dialogTitle = new MaterialLabel("컨텐츠 삭제");
		dialogTitle.setFontSize("1.4em");
		dialogTitle.setFontWeight(FontWeight.BOLD);
		dialogTitle.setTextColor(Color.BLUE);
		dialogTitle.setPaddingTop(10);
		dialogTitle.setPaddingLeft(30);

		this.add(dialogTitle);
		
		contentsInfo = new MaterialLabel();
		contentsInfo.setLayoutPosition(Position.ABSOLUTE);
		contentsInfo.setLeft(30);
		contentsInfo.setTop(70);
		
		this.add(contentsInfo);
		
		selectButton = new MaterialButton("삭제");
		selectButton.setBackgroundColor(Color.RED_LIGHTEN_2);
		selectButton.addClickHandler(event->{
			
			Object parentObj = getParameters().get("PARENT");
			
			if (parentObj == null) {
				ContentDetail detail = (ContentDetail) getParameters().get("DETAIL");
				CustomRowTable table = (CustomRowTable) getParameters().get("TABLE");
				if (detail instanceof MarketingContentDetail)  detail.setRecIndex(table.deleteRow());
				else table.deleteRow();
			}else if (parentObj instanceof MarketingDetailPanel) {

				MarketingDetailPanel mdp = (MarketingDetailPanel)parentObj;
				
				mdp.getImagePanel1().setImageId(Registry.getDefaultImageId());
				mdp.getDisplayitle().setValue("");
				mdp.getUrl().setValue("");
				
			}
			getMaterialExtentsWindow().closeDialog();
		});
		
		this.addButton(selectButton);

	}
	
	private MaterialPanel buildPreview() {

		MaterialPanel previewPanel = new MaterialPanel();
		previewPanel.setLayoutPosition(Position.ABSOLUTE);
		previewPanel.setTop(60);
		previewPanel.setLeft(530);
		previewPanel.setRight(30);
		previewPanel.setBottom(123);
		previewPanel.setBorder("1px solid #dddddd");

		return previewPanel;
	}

	@Override
	public void setParameters(Map<String, Object> parameters) {
		super.setParameters(parameters);
	}


	@Override
	public int getHeight() {
		return 220;
	}

}
