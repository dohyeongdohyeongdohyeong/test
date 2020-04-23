package kr.or.visitkorea.admin.client.manager.event.components;

import java.util.HashMap;

import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;

import gwt.material.design.client.constants.Color;
import gwt.material.design.client.constants.Display;
import gwt.material.design.client.constants.FlexAlignItems;
import gwt.material.design.client.constants.FlexJustifyContent;
import gwt.material.design.client.ui.MaterialColumn;
import gwt.material.design.client.ui.MaterialLabel;
import gwt.material.design.client.ui.MaterialRow;
import gwt.material.design.client.ui.MaterialTextBox;
import kr.or.visitkorea.admin.client.application.Registry;
import kr.or.visitkorea.admin.client.manager.widgets.AbstractContentPanel;
import kr.or.visitkorea.admin.client.manager.widgets.SelectionPanel;
import kr.or.visitkorea.admin.client.manager.widgets.UploadPanel;

public class EventComponentImage extends AbstractEventComponent {
	private String imgId;
	private String imgName;
	private UploadPanel uploadPanel;
	private SelectionPanel imgSizeType;
	private MaterialTextBox imgLink;
	private MaterialTextBox imgDesc;
	private String mode;
	private MaterialLabel Sizelabel;
	
	public EventComponentImage(EventComponentType componentType, AbstractContentPanel host, String mode) {
		super(componentType, host);
		this.mode = mode;
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
			
			this.getComponentObj().setImgId(imgId);
		});
		
		leftCol.add(uploadPanel);
		
		MaterialColumn rightCol = new MaterialColumn();
		rightCol.setGrid("s6");
		contentRow.add(rightCol);

		MaterialRow row = null;
		
			
		row = addRow(rightCol);
		Sizelabel = addLabel(row, "사이즈", "s4", Color.GREY_LIGHTEN_3);
		
		
		HashMap<String, Object> sizeTypeMap = new HashMap<>();
		sizeTypeMap.put("일반", 0);
		sizeTypeMap.put("와이드", 1);
		imgSizeType = addSelectionPanel(row, "s7", sizeTypeMap);
		imgSizeType.addStatusChangeEvent(e -> {
			this.getComponentObj().setSizeType((int) this.imgSizeType.getSelectedValue());
		});
		
		
		
		row = addRow(rightCol);
		addLabel(row, "이미지 설명", "s4", Color.GREY_LIGHTEN_3);
		imgDesc = addTextBox(row, "s8");
		imgDesc.addValueChangeHandler(e -> {
			this.getComponentObj().setImgDesc(e.getValue());
		});

		row = addRow(rightCol);
		addLabel(row, "링크 값", "s4", Color.GREY_LIGHTEN_3);
		imgLink = addTextBox(row, "s8");
		imgLink.addValueChangeHandler(e -> {
			this.getComponentObj().setImgLink(e.getValue());
		});
		
		this.add(contentRow);
	}

	@Override
	protected void modifyClickAction() {
		this.uploadPanel.getBtn().setVisible(true);
		this.imgSizeType.setEnabled(true);
		this.imgDesc.setEnabled(true);
		this.imgLink.setEnabled(true);
	}

	@Override
	protected void saveClickAction() {
		this.uploadPanel.getBtn().setVisible(false);
		this.imgSizeType.setEnabled(false);
		this.imgDesc.setEnabled(false);
		this.imgLink.setEnabled(false);
	}

	@Override
	public void setupContents() {
		this.setImgId(this.getComponentObj().getImgDesc());
		this.imgDesc.setText(this.getComponentObj().getImgDesc());
		this.imgSizeType.setSelectionOnSingleMode(this.getComponentObj().getSizeType() == 0 ? "일반" : "와이드");
		this.imgLink.setText(this.getComponentObj().getImgLink());
		this.uploadPanel.setImageId(this.getComponentObj().getImgId());
		if(mode.equals("result")) {
			imgSizeType.setVisible(false);
			Sizelabel.setVisible(false);
		}
		
	}

	@Override
	public void refresh() {
		
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

}