package kr.or.visitkorea.admin.client.manager.contents.database.widget;

import java.util.List;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;

import gwt.material.design.client.constants.Display;
import gwt.material.design.client.constants.FlexWrap;
import gwt.material.design.client.constants.IconType;
import gwt.material.design.client.ui.MaterialIcon;
import gwt.material.design.client.ui.MaterialPanel;
import gwt.material.design.client.ui.MaterialTextBox;
import kr.or.visitkorea.admin.client.application.Registry;
import kr.or.visitkorea.admin.client.manager.contents.database.composite.ContentsPamphlets;
import kr.or.visitkorea.admin.client.manager.widgets.IDUtil;
import kr.or.visitkorea.admin.client.manager.widgets.UploadPanel;

public class PamphletImage extends MaterialPanel {

	private String pplId = null;
	private String ppcId = null;
	private String imgId = null;
	private String imgPath = null;
	private String imgDesc = null;
	private int idx = -1;
	private UploadPanel uploadPanel;
	private ContentsPamphlets host;
	private MaterialTextBox altValue;

	public PamphletImage() {
		this.setGrid("s5");
		init();
	}
	
	public PamphletImage(ContentsPamphlets host) {
		this.setGrid("s5");
		this.host = host;
		init();
	}

	public void init() {
		this.setDisplay(Display.FLEX);
		this.setFlexWrap(FlexWrap.WRAP);
		this.setMargin(0);

		MaterialIcon closeIcon = new MaterialIcon(IconType.CLOSE);
		closeIcon.setMarginLeft(165);
		closeIcon.addClickHandler(event -> {
			if (this.imgId == null) {
				this.removeFromParent();
				return;
			}
			host.getWindow().confirm("선택한 이미지를 삭제하시겠습니까?", 400, new ClickHandler() {
				@Override
				public void onClick(ClickEvent event) {
					if (event.getSource().toString().contains("yes")) {
						List<PamphletImage> list = host.getSelectedItem().getImageList();
						list.remove(getIdx());
						if (list.size() != 0) {
							for (int i = 0; i < list.size(); i++) {
								list.get(i).setIdx(i);
							}
						}
						setImgDesc(null);
						setImgId(null);
						setImgPath(null);
						setIdx(-1);
						setPplId(null);
						setPpcId(null);
						host.renderImgRow();
						host.getWindow().alert("이미지가 삭제되었습니다.");
					}
				}
			});
			
		});
		this.add(closeIcon);
		
		uploadPanel = new UploadPanel(180, 130, "- 배경 이미지");
		uploadPanel.setButtonPostion(false);
		uploadPanel.getUploader().setUrl(Registry.get("image.server") + "/img/call");
		uploadPanel.getUploader().addSuccessHandler(event -> {
			JSONObject resultObj = JSONParser.parseStrict(event.getResponse().getBody()).isObject();
			String uploadValue = resultObj.get("body").isObject().get("result").isArray().get(0).isObject().get("saveName").isString().stringValue();
			String tempImageId = uploadValue.substring(0, uploadValue.lastIndexOf("."));
			
			this.imgPath = "";
			String[] imgMainSplitArr = tempImageId.split("-");
			for (String splitArrMember : imgMainSplitArr) {
				this.imgPath += "/" + splitArrMember.substring(0, 2);
			}
			this.imgPath += "/" +uploadValue;
			this.pplId = host.getSelectedItem().getPplId();
			this.imgId = IDUtil.uuid();
			this.ppcId = IDUtil.uuid();
			this.idx = host.getSelectedItem().getImageList().size();
			this.imgDesc = altValue.getValue();
			host.getSelectedItem().getImageList().add(this);
		});
		
		altValue = new MaterialTextBox();
		altValue.setMargin(0);
		altValue.setWidth("85%");
		altValue.setValue(this.imgDesc);
		altValue.addKeyUpHandler(event -> {
			this.imgDesc = altValue.getValue();
		});
		
		MaterialIcon removeIcon = new MaterialIcon(IconType.DELETE);
		removeIcon.setWidth("15%");
		removeIcon.setFontSize("30px");
		removeIcon.setMarginTop(10);
		removeIcon.addClickHandler(event -> {
			List<PamphletImage> list = host.getSelectedItem().getImageList();
			list.remove(this.getIdx());
			if (list.size() != 0) {
				for (int i = 0; i < list.size(); i++) {
					list.get(i).setIdx(0);
				}
			}
			this.setImgDesc(null);
			this.setImgId(null);
			this.setImgPath(null);
			this.setIdx(-1);
			this.setPplId(null);
			this.setPpcId(null);
		});

		this.add(uploadPanel);
		this.add(altValue);
		this.add(removeIcon);
	}
	
	public String getPplId() {
		return pplId;
	}

	public void setPplId(String pplId) {
		this.pplId = pplId;
	}

	public String getImgId() {
		return imgId;
	}

	public void setImgId(String imgId) {
		this.imgId = imgId;
	}

	public String getPpcId() {
		return ppcId;
	}

	public void setPpcId(String ppcId) {
		this.ppcId = ppcId;
	}
	
	public int getIdx() {
		return idx;
	}

	public void setIdx(int idx) {
		this.idx = idx;
	}

	public UploadPanel getUploadPanel() {
		return uploadPanel;
	}

	public void setUploadPanel(UploadPanel uploadPanel) {
		this.uploadPanel = uploadPanel;
	}

	public String getImgDesc() {
		return imgDesc;
	}

	public void setImgDesc(String imgDesc) {
		this.imgDesc = imgDesc;
		this.altValue.setValue(imgDesc);
	}

	public String getImgPath() {
		return imgPath;
	}

	public void setImgPath(String imgPath) {
		if (imgPath == null) {
			this.imgPath = null;
			this.uploadPanel.setImageUrl("");
		} else {
			this.imgPath = imgPath.substring(imgPath.lastIndexOf("/") + 1);
			this.uploadPanel.setImageUrl(Registry.get("image.server") + "/img/call?cmd=TEMP_VIEW&name=" + this.imgPath);
		}
	}
	
}
