package kr.or.visitkorea.admin.client.manager.memberActivity.widgets;

import java.util.HashMap;

import com.google.gwt.core.client.GWT;
import com.google.gwt.json.client.JSONObject;

import gwt.material.design.client.constants.Display;
import gwt.material.design.client.constants.FlexAlignSelf;
import gwt.material.design.client.constants.FlexDirection;
import gwt.material.design.client.constants.FlexJustifyContent;
import gwt.material.design.client.constants.IconType;
import gwt.material.design.client.constants.TextAlign;
import gwt.material.design.client.ui.MaterialColumn;
import gwt.material.design.client.ui.MaterialIcon;
import gwt.material.design.client.ui.MaterialImage;
import gwt.material.design.client.ui.MaterialLabel;
import gwt.material.design.client.ui.MaterialPanel;
import gwt.material.design.client.ui.MaterialRow;
import kr.or.visitkorea.admin.client.application.Registry;
import kr.or.visitkorea.admin.client.manager.memberActivity.dialogs.UserImageDetailDialog;
import kr.or.visitkorea.admin.client.manager.widgets.SelectionPanel;

public class UserImageDetailItem extends MaterialRow {
	private MaterialImage image;
	private MaterialLabel user;
	private SelectionPanel isView;
	private MaterialPanel orderPanel;
	private MaterialIcon upBtn;
	private MaterialIcon downBtn;
	
	private UserImageDetailDialog host;
	private String umgId;
	private String imgId;
	private String imgPath;
	private int idx;
	private JSONObject obj;
	private int visiable;
	
	public UserImageDetailItem(JSONObject obj, UserImageDetailDialog host) {
		this.setHeight("190px");
		this.setBorder("1px solid grey");
		this.obj = obj;
		this.host = host;
		if (obj.containsKey("UMG_ID"))
			this.umgId = obj.get("UMG_ID").isString().stringValue();
		if (obj.containsKey("IDX"))
			this.idx = (int) obj.get("IDX").isNumber().doubleValue();
		if (obj.containsKey("IMG_ID"))
			this.imgId = obj.get("IMG_ID").isString().stringValue();
		if (obj.containsKey("IMAGE_PATH"))
			this.imgPath = obj.get("IMAGE_PATH").isString().stringValue();
		if (obj.containsKey("IS_VISIABLE"))
			this.visiable = (int) obj.get("IS_VISIABLE").isNumber().doubleValue();
		init();
		setValue();
	}

	public void setValue() {
		if (this.imgPath != null) {
			this.image.setUrl(Registry.get("image.server") + "/img/call?cmd=TEMP_VIEW&name="+ imgPath.substring(imgPath.lastIndexOf("/") + 1));
		} else {
			this.image.setUrl(GWT.getHostPageBaseURL() + "images/notfound.png");
		}
		if (obj.containsKey("SNS_IDENTIFY"))
			this.user.setText("사용자 : " + obj.get("SNS_IDENTIFY").isString().stringValue());
		this.isView.setSelectionOnSingleMode(this.visiable == 1 ? "노출" : "비노출");
	}
	
	public void init() {
		MaterialColumn col1 = addColumn("s5");
		MaterialColumn col2 = addColumn("s4");
		col2.setPaddingTop(80);
		MaterialColumn col3 = addColumn("s3");
		this.add(col1);
		this.add(col2);
		this.add(col3);

		image = new MaterialImage();
		image.setHeight("90%");
		
		user = new MaterialLabel();
		user.setText("사용자 : ");
		
		col1.setFlexDirection(FlexDirection.COLUMN);
		col1.add(image);
		col1.add(user);
		
		HashMap<String, Object> isViewValue = new HashMap<>();
		isViewValue.put("노출", 1);
		isViewValue.put("비노출", 0);
		
		isView = new SelectionPanel();
		isView.setValues(isViewValue);
		isView.setFlexAlignSelf(FlexAlignSelf.CENTER);
		isView.setFlexJustifyContent(FlexJustifyContent.START);
		isView.setWidth("100%");
		isView.addStatusChangeEvent(event -> {
			this.setVisiable((int) isView.getSelectedValue());
		});
		col2.add(isView);
		
		orderPanel = addOrderBtn();
		orderPanel.setFlexAlignSelf(FlexAlignSelf.END);
		isView.setFlexJustifyContent(FlexJustifyContent.END);
		
		col3.add(orderPanel);
	}
	
	private MaterialPanel addOrderBtn() {
		MaterialPanel panel = new MaterialPanel();
		panel.setWidth("100%");
		panel.setTextAlign(TextAlign.RIGHT);
		
		upBtn = new MaterialIcon(IconType.ARROW_UPWARD);
		upBtn.setFontSize("30px");
		upBtn.setDisplay(this.idx == 0 ? Display.NONE : Display.INLINE_BLOCK);
		upBtn.addClickHandler(event -> {
			host.orderUp(this.idx);
		});
		
		downBtn = new MaterialIcon(IconType.ARROW_DOWNWARD);
		downBtn.setFontSize("30px");
		downBtn.setDisplay(this.idx == (this.host.getItemSize() - 1) ? Display.NONE : Display.INLINE_BLOCK);
		downBtn.addClickHandler(event -> {
			host.orderDown(this.idx);
		});
		
		panel.add(upBtn);
		panel.add(downBtn);
		
		return panel;
	}
	
	private MaterialColumn addColumn(String grid) {
		MaterialColumn col1 = new MaterialColumn();
		col1.setPadding(4);
		col1.setGrid(grid);
		col1.setHeight("100%");
		col1.setDisplay(Display.FLEX);
		return col1;
	}

	public String getUmgId() {
		return umgId;
	}
	public void setUmgId(String umgId) {
		this.umgId = umgId;
	}
	public String getImgId() {
		return imgId;
	}
	public void setImgId(String imgId) {
		this.imgId = imgId;
	}
	public int getIdx() {
		return idx;
	}
	public int getVisiable() {
		return visiable;
	}
	public void setVisiable(int visiable) {
		this.visiable = visiable;
	}
	public void setIdx(int idx) {
		this.idx = idx;
		upBtn.setDisplay(this.idx == 0 ? Display.NONE : Display.INLINE_BLOCK);
		downBtn.setDisplay(this.idx == (this.host.getItemSize() - 1) ? Display.NONE : Display.INLINE_BLOCK);
	}
}
