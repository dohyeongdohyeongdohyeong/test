package kr.or.visitkorea.admin.client.manager.contents.database.dialogs;

import java.util.HashMap;

import com.google.gwt.dom.client.Style.FontWeight;

import gwt.material.design.client.constants.Color;
import gwt.material.design.client.constants.TextAlign;
import gwt.material.design.client.ui.MaterialLabel;
import gwt.material.design.client.ui.MaterialPanel;
import kr.or.visitkorea.admin.client.manager.contents.database.composite.ContentsETC;
import kr.or.visitkorea.admin.client.manager.contents.database.dialogs.panel.AbstractOfferDialogContentPanel;
import kr.or.visitkorea.admin.client.manager.contents.database.dialogs.panel.OfferDialogMemberPanel;
import kr.or.visitkorea.admin.client.manager.contents.database.dialogs.panel.OfferDialogZikimiPanel;
import kr.or.visitkorea.admin.client.manager.widgets.SelectionPanel;
import kr.or.visitkorea.admin.client.widgets.dialog.DialogContent;
import kr.or.visitkorea.admin.client.widgets.window.MaterialExtentsWindow;

public class SelectInformOfferDialog extends DialogContent {
	private String cotId;
	private String snsId;
	private SelectionPanel tapPanel;
	private AbstractOfferDialogContentPanel selectedPanel;
	private MaterialPanel contentArea;
	private MaterialLabel dialogTitle;
	
	public SelectInformOfferDialog(MaterialExtentsWindow tgrWindow) {
		super(tgrWindow);
	}

	@Override
	public int getHeight() {
		return 660;
	}
	
	@Override
	public void init() {
		buildContent();
	}

	public void buildContent() {
		addDefaultButtons();

		dialogTitle = new MaterialLabel("정보제공자 검색 :: 관광정보지킴이");
		dialogTitle.setFontSize("1.4em");
		dialogTitle.setFontWeight(FontWeight.BOLD);
		dialogTitle.setTextColor(Color.BLUE);
		dialogTitle.setPaddingTop(10);
		dialogTitle.setPaddingLeft(30);
		this.add(dialogTitle);
		
		HashMap<String, Object> tapValueMap = new HashMap<>();
		tapValueMap.put("사용자", 0);
		tapValueMap.put("관광정보지킴이", 1);
		
		tapPanel = addSelectionPanel(tapValueMap);
		tapPanel.setHeight("50px");
		tapPanel.addStatusChangeEvent(event -> {
			changePanel((int) tapPanel.getSelectedValue());
		});
		this.add(tapPanel);

		contentArea = new MaterialPanel();
		contentArea.setPaddingLeft(15);
		contentArea.setPaddingRight(15);
		
		selectedPanel = new OfferDialogZikimiPanel(this);
		contentArea.add(selectedPanel);
		this.add(contentArea);
	}
	
	public void changePanel(int index) {
		contentArea.clear();
		if (index == 0) {
			dialogTitle.setText("정보제공자 검색 :: 사용자");
			selectedPanel = new OfferDialogMemberPanel(this);
		} else {
			dialogTitle.setText("정보제공자 검색 :: 관광정보지킴이");
			selectedPanel = new OfferDialogZikimiPanel(this);
		}
		contentArea.add(selectedPanel);
	}
	
	@Override
	protected void onLoad() {
		super.onLoad();
		if (this.getParameters().get("cotId") != null)
			this.cotId = this.getParameters().get("cotId").toString();
		if (this.getParameters().get("snsId") != null)
			this.snsId = this.getParameters().get("snsId").toString();
		tapPanel.setSelectionOnSingleMode("관광정보지킴이");
		changePanel(1);
	}
	
	public SelectionPanel addSelectionPanel(HashMap<String, Object> valueMap) {
		SelectionPanel panel = new SelectionPanel();
		panel.setValues(valueMap);
		panel.setPadding(15);
		panel.setTextAlign(TextAlign.LEFT);
		return panel;
	}

	public String getCotId() {
		return cotId;
	}

	public String getSnsId() {
		return snsId;
	}

}
