package kr.or.visitkorea.admin.client.manager.contents.components;

import java.util.HashMap;

import com.google.gwt.dom.client.Style.FontWeight;
import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.json.client.JSONNumber;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONString;

import gwt.material.design.client.base.MaterialWidget;
import gwt.material.design.client.constants.Color;
import gwt.material.design.client.constants.Display;
import gwt.material.design.client.constants.FlexAlignItems;
import gwt.material.design.client.constants.FlexJustifyContent;
import gwt.material.design.client.constants.TextAlign;
import gwt.material.design.client.ui.MaterialColumn;
import gwt.material.design.client.ui.MaterialLabel;
import gwt.material.design.client.ui.MaterialRow;
import gwt.material.design.client.ui.MaterialTextBox;
import kr.or.visitkorea.admin.client.application.Registry;
import kr.or.visitkorea.admin.client.manager.widgets.IDUtil;
import kr.or.visitkorea.admin.client.manager.widgets.SelectionPanel;
import kr.or.visitkorea.admin.client.manager.widgets.UploadPanel;

public class ContentComponentImage extends AbstractContentComponent implements ContentComponent {
	private String imgId;
	private String imgName;
	private UploadPanel uploadPanel;
	private SelectionPanel imgSizeType;
	private MaterialTextBox imgLink;
	private MaterialTextBox imgDesc;
	
	public ContentComponentImage(ContentComponentType componentType) {
		super(componentType);
	}

	@Override
	protected void init() {
		MaterialRow contentRow = new MaterialRow();
		contentRow.setDisplay(Display.FLEX);
		contentRow.setFlexAlignItems(FlexAlignItems.CENTER);
		
		MaterialColumn leftCol = new MaterialColumn();
		leftCol.setGrid("s6");
		leftCol.setDisplay(Display.FLEX);
		leftCol.setFlexJustifyContent(FlexJustifyContent.CENTER);
		contentRow.add(leftCol);

		uploadPanel = new UploadPanel(360, 250, (String) Registry.get("image.server") + "/img/call?cmd=TEMP_VIEW&name=55a0ec2e-a5c1-4938-8bfd-4a7e7095bec9.png");
		uploadPanel.setLayoutPosition(Position.RELATIVE);
		uploadPanel.updateImageInformation(true);
		uploadPanel.setLeft(0);
		uploadPanel.setTop(0);
		uploadPanel.setButtonPostion(false);
		uploadPanel.getBtn().setVisible(false);
		uploadPanel.getUploader().addSuccessHandler(e -> {
			JSONObject resultObj = JSONParser.parseStrict(e.getResponse().getBody()).isObject();
			JSONObject bodyObj = resultObj.get("body").isObject().get("result").isArray().get(0).isObject();
			
			this.imgId = bodyObj.get("uuid").isString().stringValue();
			this.imgName = bodyObj.get("saveName").isString().stringValue();
			
			this.getInternalObject().put("IMG_ID", new JSONString(this.imgId));
		});
		
		leftCol.add(uploadPanel);
		
		MaterialColumn rightCol = new MaterialColumn();
		rightCol.setGrid("s6");
		contentRow.add(rightCol);

		MaterialRow row = null;
		
		row = addRow(rightCol);
		addLabel(row, "사이즈", "s4", Color.GREY_LIGHTEN_3);
		
		HashMap<String, Object> sizeTypeMap = new HashMap<>();
		sizeTypeMap.put("일반", 0);
		sizeTypeMap.put("와이드", 1);
		imgSizeType = addSelectionPanel(row, "s7", sizeTypeMap);
		imgSizeType.addStatusChangeEvent(e -> {
			this.getInternalObject().put("SIZE_TYPE", new JSONNumber((int) this.imgSizeType.getSelectedValue()));
		});
		
		row = addRow(rightCol);
		addLabel(row, "이미지 설명", "s4", Color.GREY_LIGHTEN_3);
		imgDesc = addTextBox(row, "s8");
		imgDesc.addValueChangeHandler(e -> {
			this.getInternalObject().put("IMG_DESC", new JSONString(e.getValue()));
		});

		row = addRow(rightCol);
		addLabel(row, "링크 값", "s4", Color.GREY_LIGHTEN_3);
		imgLink = addTextBox(row, "s8");
		imgLink.addValueChangeHandler(e -> {
			this.getInternalObject().put("IMG_LINK", new JSONString(e.getValue()));
		});
		
		this.add(contentRow);
	}

	@Override
	protected void modifyClickAction() {
		if (!this.isEditMode) {
			this.uploadPanel.getBtn().setVisible(true);
			this.imgSizeType.setEnabled(true);
			this.imgDesc.setEnabled(true);
			this.imgLink.setEnabled(true);
			this.isEditMode = true;
		}
	}

	@Override
	protected void saveClickAction() {
		if (this.isEditMode) {
			this.uploadPanel.getBtn().setVisible(false);
			this.imgSizeType.setEnabled(false);
			this.imgDesc.setEnabled(false);
			this.imgLink.setEnabled(false);
			this.isEditMode = false;
		}
	}

	@Override
	public void setupContents() {
		String imgDesc = this.getInternalObject().containsKey("IMG_DESC") ? this.getInternalObject().get("IMG_DESC").isString().stringValue() : "";
		int imgSizeType = this.getInternalObject().containsKey("SIZE_TYPE") ? (int) this.getInternalObject().get("SIZE_TYPE").isNumber().doubleValue() : -1;
		String imgLink = this.getInternalObject().containsKey("IMG_LINK") ? this.getInternalObject().get("IMG_LINK").isString().stringValue() : "";
		String imgId = this.getInternalObject().containsKey("IMG_ID") ? this.getInternalObject().get("IMG_ID").isString().stringValue() : IDUtil.uuid();
		
		this.setImgId(imgId);
		this.imgDesc.setText(imgDesc);
		this.imgSizeType.setSelectionOnSingleMode(imgSizeType == 0 ? "일반" : "와이드");
		this.imgLink.setText(imgLink);
		this.uploadPanel.setImageId(imgId);
	}
	
	
	public String getImgId() {
		return imgId;
	}

	public void setImgId(String imgId) {
		this.imgId = imgId;
	}

	public String getImgName() {
		return imgName;
	}

	public void setImgName(String imgName) {
		this.imgName = imgName;
	}

	private MaterialRow addRow(MaterialWidget parent) {
		MaterialRow row = new MaterialRow();
		parent.add(row);
		return row;
	}
	
	private SelectionPanel addSelectionPanel(MaterialWidget parent, String grid, HashMap<String, Object> valueMap) {
		SelectionPanel panel = new SelectionPanel();
		panel.setValues(valueMap);
		panel.setTextAlign(TextAlign.LEFT);
		panel.setMargin(0);
		panel.setEnabled(false);
		
		MaterialColumn col = new MaterialColumn();
		col.setGrid(grid);
		col.add(panel);
		col.setLineHeight(46.25);	
		parent.add(col);
		return panel;
	}
	
	private MaterialTextBox addTextBox(MaterialWidget parent, String grid) {
		MaterialTextBox tbox = new MaterialTextBox();
		tbox.setMargin(0);
		tbox.getElement().getFirstChildElement().getStyle().setMargin(0, Unit.PX);
		tbox.setEnabled(false);
		
		MaterialColumn col = new MaterialColumn();
		col.setGrid(grid);
		col.add(tbox);
		col.setLineHeight(46.25);	
		parent.add(col);
		return tbox;
	}
	
	private MaterialLabel addLabel(MaterialWidget parent, String text, String grid, Color bgColor) {
		MaterialLabel label = new MaterialLabel();
		label.setText(text);
		label.setBackgroundColor(bgColor);
		label.setLineHeight(46.25);
		label.setHeight("46.25px");
		label.setFontSize("1.2em");
		label.setFontWeight(FontWeight.BOLD);
		
		MaterialColumn col = new MaterialColumn();
		col.setGrid(grid);
		col.add(label);
		col.setLineHeight(46.25);
		parent.add(col);
		return label;
	}

}
