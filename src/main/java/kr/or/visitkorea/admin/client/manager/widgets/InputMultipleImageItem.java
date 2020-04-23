package kr.or.visitkorea.admin.client.manager.widgets;

import com.google.gwt.dom.client.Style.Overflow;
import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.user.client.ui.Widget;

import gwt.material.design.client.base.TransitionConfig;
import gwt.material.design.client.constants.Color;
import gwt.material.design.client.constants.Display;
import gwt.material.design.client.constants.IconType;
import gwt.material.design.client.constants.TextAlign;
import gwt.material.design.client.ui.MaterialLink;
import gwt.material.design.client.ui.MaterialPanel;

public class InputMultipleImageItem extends MaterialPanel {

	private MaterialPanel scrollPanel;
	private MaterialPanel bottomPanel;
	private MaterialPanel dispContent;
	private String[] keys;
	private String uploadUrl;
	private InputAccommodationItemDetail inputAccommodationItemDetail;
	private MaterialPanel scrollBasePanel;

	public InputMultipleImageItem(MaterialPanel dispContent, String[] keys, InputAccommodationItemDetail inputAccommodationItemDetail, String uploadUrl) {
		super();
		this.dispContent = dispContent;
		this.keys = keys;
		this.uploadUrl = uploadUrl;
		this.inputAccommodationItemDetail = inputAccommodationItemDetail;
		init();
	}

	private void init() {
		
		this.setLayoutPosition(Position.RELATIVE);
		this.scrollPanel = buildScrollPanel();
		this.bottomPanel = buildBottomPanel();

		this.add(scrollPanel);
		this.add(bottomPanel);

		this.dispContent.add(this);
	}

	private MaterialPanel buildScrollPanel() {
		
		MaterialPanel scrollPanel = new MaterialPanel();
		scrollPanel.setLayoutPosition(Position.RELATIVE);
		scrollPanel.setOverflow(Overflow.HIDDEN);
		scrollPanel.setHeight("196px");
		scrollPanel.setWidth("100%");

		scrollBasePanel = new MaterialPanel();
		scrollBasePanel.setLayoutPosition(Position.ABSOLUTE);
		scrollBasePanel.setTop(0);
		scrollBasePanel.setLeft(0);
		scrollBasePanel.setWidth((310 * keys.length) + "px");
		scrollBasePanel.setHeight("100%");
		
		scrollPanel.add(scrollBasePanel);
		
		for (int i=0; i<keys.length; i++) {
			
			String imgValue = "";
			if (keys[i] != null && this.inputAccommodationItemDetail.getValue(keys[i]) != null) {
				imgValue = this.uploadUrl + "?cmd=VIEW&id=" + this.inputAccommodationItemDetail.getValue(keys[i]).isString().stringValue();
			}else {
				imgValue = this.uploadUrl + "?cmd=TEMP_VIEW&name=6b23c237-7a8c-4b50-b550-1553e29129c9.png";
				this.inputAccommodationItemDetail.setValue(keys[i], imgValue);
			}
		
			UploadPanel uploadPanel = new UploadPanel(294, 196, this.uploadUrl);
			uploadPanel.setLayoutPosition(Position.RELATIVE);
			uploadPanel.setFloat(com.google.gwt.dom.client.Style.Float.LEFT);
			uploadPanel.setTop(0);
			uploadPanel.setButtonPostion(false);
			uploadPanel.setWidth("294px");
			uploadPanel.setHeight("196px");
			uploadPanel.setImageUrl(imgValue);
			uploadPanel.getUploader().addSuccessHandler(event->{
				
				uploadPanel.setProgress(0);
				
				
			});
			
			scrollBasePanel.add(uploadPanel);
		}
		
		return scrollPanel;
	}

	private MaterialPanel buildBottomPanel() {
		MaterialPanel tgrPanel = new MaterialPanel();
		tgrPanel.setHeight("30px");
		tgrPanel.setTop(5);
		tgrPanel.setLeft(5);
		tgrPanel.setRight(5);
		tgrPanel.setBackgroundColor(Color.WHITE);
		tgrPanel.setTextAlign(TextAlign.CENTER);
		
		for (int i=0; i<keys.length; i++) {
			MaterialLink link = new MaterialLink();
			link.setDisplay(Display.INLINE_BLOCK);
			link.setTextAlign(TextAlign.CENTER);
			link.setWidth("30px");
			link.setLineHeight(30);
			if (i == 0) {
				link.setIconType(IconType.RADIO_BUTTON_CHECKED);
			}else {
				link.setIconType(IconType.RADIO_BUTTON_UNCHECKED);
			}
			tgrPanel.add(link);
			
			link.addClickHandler(event->{
				
				int index = tgrPanel.getChildrenList().indexOf(link);
				int nowPosition = index * -294;
				TransitionConfig cfg = new TransitionConfig();
				cfg.setProperty("left");
				cfg.setDuration(100);
				scrollBasePanel.setTransition(cfg);
				scrollBasePanel.setTransform("translate("+nowPosition+"px,0);");
				scrollBasePanel.setLeft(nowPosition);
				
				for (Widget widget : tgrPanel.getChildrenList()) {
					((MaterialLink)widget).setIconType(IconType.RADIO_BUTTON_UNCHECKED);
				}
				
				link.setIconType(IconType.RADIO_BUTTON_CHECKED);
				
			});
		}
		
		return tgrPanel;
	}
	
}
