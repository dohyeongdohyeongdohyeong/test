package kr.or.visitkorea.admin.client.manager.otherDepartment.composite.festival;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Cursor;
import com.google.gwt.dom.client.Style.Float;
import com.google.gwt.dom.client.Style.FontWeight;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONString;

import gwt.material.design.client.constants.Color;
import gwt.material.design.client.constants.TextAlign;
import gwt.material.design.client.ui.MaterialCheckBox;
import gwt.material.design.client.ui.MaterialColumn;
import gwt.material.design.client.ui.MaterialImage;
import gwt.material.design.client.ui.MaterialLabel;
import gwt.material.design.client.ui.MaterialPanel;
import gwt.material.design.client.ui.MaterialRow;
import kr.or.visitkorea.admin.client.application.Registry;
import kr.or.visitkorea.admin.client.manager.otherDepartment.dialogs.SeasonContentDialog;
import kr.or.visitkorea.admin.client.manager.otherDepartment.widgets.TagListImageCell;
import kr.or.visitkorea.admin.client.manager.otherDepartment.widgets.TagListLabelCell;
import kr.or.visitkorea.admin.client.manager.otherDepartment.widgets.TagListRow;
import kr.or.visitkorea.admin.client.manager.otherDepartment.widgets.ViewPanel;
import kr.or.visitkorea.admin.client.widgets.dialog.ButtonInfo;
import kr.or.visitkorea.admin.client.widgets.window.MaterialExtentsWindow;

public class FestivalContentLeftArea extends FestivalContentLeft {

	private FestivalContentRight rightArea;
	private FestivalContentSearchPanel searchPanel;
	private MaterialExtentsWindow window;
	private SeasonContentDialog dialog;
	private Map<String, Object> param;
	private MaterialLabel nameValueLabel;
	private MaterialLabel contentCountValueLabel;
	private MaterialLabel selectedContentCountValueLabel;
	private MaterialCheckBox isAutomation;
	private TagListLabelCell isAutomationLabelCell;
	private TagListImageCell recordImageCell;
	private MaterialImage image;
	private TagListRow targetListRow;
	private ViewPanel viewPanel;
	private JSONObject result;
	private int index;

	public FestivalContentLeftArea(MaterialExtentsWindow materialExtentsWindow, SeasonContentDialog seasonContentDialog) {
		super();
		this.window = materialExtentsWindow;
		this.dialog = seasonContentDialog;
		layout();
	}

	private void layout() {

		this.setBorder("1px solid #c8c8c8");
		this.setPaddingTop(10);

		// dialog title define
		MaterialLabel panelTitle = new MaterialLabel("- 기본 정보");
		panelTitle.setFontSize("1.2em");
		panelTitle.setFontWeight(FontWeight.BOLD);
		panelTitle.setTextColor(Color.BLUE);
		panelTitle.setPaddingLeft(10);
		panelTitle.setPaddingBottom(10);
		this.add(panelTitle);

		addImage("- 아이콘 : ");
		this.nameValueLabel = addLabel("- 태그 명 : ", "여름 축제");
		this.contentCountValueLabel = addLabel("- 컨텐츠 보유 수 : ", "1,000 개");
		this.selectedContentCountValueLabel = addLabel("- 선택된 컨텐츠 수 : ", "10 개");
		MaterialRow row = addButtons("- 노출 설 정 : ");
		row.setMarginBottom(0);
		
		searchPanel = new FestivalContentSearchPanel(getWindow(), this.dialog);
		searchPanel.setPaddingLeft(30);
		searchPanel.setPaddingRight(10);
		searchPanel.setEnabled(false);
		this.add(searchPanel);
		
	}

	private MaterialExtentsWindow getWindow() {
		return this.window;
	}

	private MaterialRow addButtons(String label) {
		
		// label
		MaterialLabel nameKeyLabel = new MaterialLabel(label);
		nameKeyLabel.setTextAlign(TextAlign.LEFT);
		nameKeyLabel.setFontWeight(FontWeight.BOLD);
		
		// value
		MaterialPanel panel = new MaterialPanel();
		panel.setTextAlign(TextAlign.RIGHT);
		
		isAutomation = new MaterialCheckBox("자동");
		isAutomation.setGrid("s12");
		isAutomation.getElement().getElementsByTagName("label").getItem(0).getStyle().setPaddingLeft(25d, Unit.PX);
		isAutomation.setValue(true);
		isAutomation.addValueChangeHandler(event->{
			if (this.searchPanel != null) {
				this.searchPanel.setEnabled(!event.getValue());
				this.rightArea.setDisableContent(event.getValue());
				if (event.getValue()) {
					this.isAutomationLabelCell.setText("자동");
				}else {
					this.isAutomationLabelCell.setText("수동");
				}
			}
		});
		
		panel.add(isAutomation);
		panel.getElement().getElementsByTagName("span").getItem(0).getStyle().setPaddingRight(0, Unit.PX);

		MaterialColumn col_1 = new MaterialColumn();
		col_1.setGrid("s6");
		col_1.add(nameKeyLabel);

		MaterialColumn col_2 = new MaterialColumn();
		col_2.setGrid("s6");
		col_2.add(panel);

		MaterialRow row_1 = new MaterialRow();
		row_1.add(col_1);
		row_1.add(col_2);
		row_1.setPaddingLeft(10);
		
		this.add(row_1);
		
		return row_1;
	}

	private MaterialLabel addLabel(String label, String value) {
		
		// 명칭
		MaterialLabel nameKeyLabel = new MaterialLabel(label);
		nameKeyLabel.setTextAlign(TextAlign.LEFT);
		nameKeyLabel.setFontWeight(FontWeight.BOLD);
		
		// 값
		MaterialLabel nameValueLabel = new MaterialLabel(value);
		nameValueLabel.setTextAlign(TextAlign.RIGHT);
		
		MaterialColumn col_1 = new MaterialColumn();
		col_1.setGrid("s6");
		col_1.add(nameKeyLabel);

		MaterialColumn col_2 = new MaterialColumn();
		col_2.setGrid("s6");
		col_2.add(nameValueLabel);

		MaterialRow row_1 = new MaterialRow();
		row_1.add(col_1);
		row_1.add(col_2);
		row_1.setPaddingLeft(10);
		this.add(row_1);
		
		return nameValueLabel;
	}
	
	private String getID(String targetUrl) {
		String fileNameStirng = targetUrl.replaceAll(Registry.get("image.server") + "/img/call?cmd=TEMP_VIEW&name=", "");
		
		return fileNameStirng.substring(fileNameStirng.lastIndexOf("="), fileNameStirng.lastIndexOf("."));
		
	}

	private void addImage(String label) {
		
		// 명칭
		MaterialLabel nameKeyLabel = new MaterialLabel(label);
		nameKeyLabel.setTextAlign(TextAlign.LEFT);
		nameKeyLabel.setFontWeight(FontWeight.BOLD);
		
		image = new MaterialImage();
		image.setMaxWidth("16px");
		image.setMaxHeight("16px");
		image.setUrl(GWT.getHostPageBaseURL() + "images/small_log.png");
		image.addValueChangeHandler(event->{

			String imageUrl = image.getUrl();
			recordImageCell.setUrl(imageUrl);
			
			JSONObject targetObject = result.get("seasonTagsArr").isArray().get(this.index).isObject();
			targetObject.put("TMP_IMG_URL", new JSONString(imageUrl));
			targetObject.put("IMG_ID", new JSONString(getID(imageUrl)));
			
		});
		image.addMouseUpHandler(event->{
			List<ButtonInfo> infos = new ArrayList<ButtonInfo>();
			infos.add(new ButtonInfo("선택", Color.AMBER, Float.RIGHT, new ClickHandler() {
				
				@Override
				public void onClick(ClickEvent event) {
					
				}
			}));

			Map<String, Object> params = new HashMap<String, Object>();
			params.put("RESULT", result);
			params.put("INDEX", index);
			
			this.dialog.openImage("아이콘 선택", 400, 430, image, infos, params);
		});
		image.addMouseOverHandler(event->{
			image.getElement().getStyle().setCursor(Cursor.POINTER);
		});
		image.addMouseOutHandler(event->{
			image.getElement().getStyle().setCursor(Cursor.DEFAULT);
		});
		
		MaterialColumn col_1 = new MaterialColumn();
		col_1.setGrid("s6");
		col_1.add(nameKeyLabel);

		MaterialColumn col_2 = new MaterialColumn();
		col_2.setGrid("s6");
		col_2.setTextAlign(TextAlign.RIGHT);
		col_2.add(image);

		MaterialRow row_1 = new MaterialRow();
		row_1.add(col_1);
		row_1.add(col_2);
		row_1.setPaddingLeft(10);
		this.add(row_1);
	}

	public void setRightPanel(FestivalContentRight rightArea) {
		this.rightArea = rightArea;
		this.rightArea.setDisableContent(true);
		this.searchPanel.setRightArea(this.rightArea);
	}

	public void setParamters(Map<String, Object> parameters) {
		this.param = parameters;

		targetListRow = (TagListRow) this.param.get("SELECTED_CONDITION");
		viewPanel = (ViewPanel)parameters.get("VIEW_PANEL");
		result = (JSONObject)viewPanel.getData("REC_CONTAINER");
		index = (int)targetListRow.get("INDEX");
		
		if (targetListRow != null) {
			
			TagListRow conditionList = (TagListRow) this.param.get("SELECTED_CONDITION");
			
			recordImageCell = (TagListImageCell)conditionList.getCell(0);
			image.setUrl(recordImageCell.getUrl());
			TagListLabelCell nameLabelCell = (TagListLabelCell)conditionList.getCell(1);
			nameLabelCell.getText();
			this.nameValueLabel.setText(nameLabelCell.getText());
			TagListLabelCell contentCountLabelCell = (TagListLabelCell)conditionList.getCell(2);
			this.contentCountValueLabel.setText(contentCountLabelCell.getText() + " 개");
			TagListLabelCell selectedCountLabelCell = (TagListLabelCell)conditionList.getCell(3);
			this.selectedContentCountValueLabel.setText(selectedCountLabelCell.getText() + " 개");
			isAutomationLabelCell = (TagListLabelCell)conditionList.getCell(4);
			
			if (isAutomationLabelCell.getText().equals("자동")) {
				isAutomation.setValue(true);
			} else if (isAutomationLabelCell.getText().equals("수동")) {
				isAutomation.setValue(false);
				searchPanel.setEnabled(true);
			}
			
			searchPanel.clearContentArea();
			searchPanel.setKeyword(this.nameValueLabel.getText());
			
		}
	}
}
