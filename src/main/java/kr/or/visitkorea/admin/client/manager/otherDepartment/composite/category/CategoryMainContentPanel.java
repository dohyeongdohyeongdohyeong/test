package kr.or.visitkorea.admin.client.manager.otherDepartment.composite.category;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.user.client.ui.Widget;

import gwt.material.design.client.base.AbstractIconButton;
import gwt.material.design.client.constants.Color;
import gwt.material.design.client.constants.IconType;
import gwt.material.design.client.ui.MaterialImage;
import gwt.material.design.client.ui.MaterialLabel;
import gwt.material.design.client.ui.MaterialLink;
import gwt.material.design.client.ui.MaterialPanel;
import kr.or.visitkorea.admin.client.application.Console;
import kr.or.visitkorea.admin.client.application.Registry;
import kr.or.visitkorea.admin.client.manager.otherDepartment.OtherDepartmentMainApplication;
import kr.or.visitkorea.admin.client.manager.widgets.IDUtil;
import kr.or.visitkorea.admin.client.widgets.window.MaterialExtentsWindow;

public class CategoryMainContentPanel extends ContentLayoutPanel {

	private CategoryMainContentType uiType;
	private boolean menuVisible = false;
	private MaterialLink li_01;
	private AbstractIconButton li_03;
	private AbstractIconButton li_04;
	private MaterialExtentsWindow extWindow;
	private MaterialImage imagePanel;
	private JSONObject diagObj;
	private List<JSONObject> linkObjList = new ArrayList<JSONObject>();
	private int dispIndex;
	private MaterialLabel headerLabel;
	private MaterialLabel titleLabel;
	private MaterialLink li_00;
	private ContentSection contentSection;

	public CategoryMainContentPanel(ContentSection contentSection) {
		super();
		this.contentSection = contentSection;
	}

	protected void init() {

		this.setId(IDUtil.uuid());

		this.setBorder("1px solid #aaaaaa");

		imagePanel = new MaterialImage();
		imagePanel.setLayoutPosition(Position.ABSOLUTE);
		imagePanel.setTop(0);
		imagePanel.setRight(0);
		imagePanel.setWidth("100%");
		imagePanel.setHeight("100%");
		imagePanel.setBackgroundColor(Color.RED);
		imagePanel.setVisible(false);
		this.add(imagePanel);

		MaterialPanel menuPanel = new MaterialPanel();
		menuPanel.setLayoutPosition(Position.ABSOLUTE);
		menuPanel.setTop(0);
		menuPanel.setRight(0);
		menuPanel.setWidth("100%");
		menuPanel.setHeight("100%");
		this.add(menuPanel);

		// MENU button
		li_00 = new MaterialLink();
		li_00.setIconType(IconType.MENU);
		li_00.setLayoutPosition(Position.ABSOLUTE);
		li_00.setTop(2);
		li_00.setRight(2);
		li_00.getElement().getFirstChildElement().getStyle().setMarginRight(0, Unit.PX);
		li_00.setBackgroundColor(Color.WHITE);
		menuPanel.add(li_00);

		// ADD button
		li_01 = new MaterialLink();
		li_01.setIconType(IconType.ADD);
		li_01.setLayoutPosition(Position.ABSOLUTE);
		li_01.setTop(2);
		li_01.setLeft(2);
		li_01.setVisible(false);
		li_01.getElement().getFirstChildElement().getStyle().setMarginRight(0, Unit.PX);
		li_01.setBackgroundColor(Color.WHITE);
		li_01.addClickHandler(event -> {

			Map<String, Object> map = new HashMap<String, Object>();
			map.put("OTD_ID", this.extWindow.getValueMap().get("OTD_ID"));
			map.put("AREA_COMPONENT", this);
			map.put("SECTION", contentSection);
			this.extWindow.openDialog(OtherDepartmentMainApplication.SELECT_CONTENT, map, 640);

		});
		menuPanel.add(li_01);

		// REMOVE button
		li_03 = new MaterialLink();
		li_03.setIconType(IconType.REMOVE);
		li_03.setLayoutPosition(Position.ABSOLUTE);
		li_03.setBottom(3);
		li_03.setLeft(2);
		// li_03.setBorder("1px solid #bbbbbb");
		li_03.setBackgroundColor(Color.WHITE);
		li_03.setVisible(false);
		li_03.getElement().getFirstChildElement().getStyle().setMarginRight(0, Unit.PX);
		li_03.addClickHandler(event -> {

			this.diagObj = null;
			String URL = (String) Registry.get("image.server") + "/img/call?cmd=VIEW&id="
					+ Registry.getDefaultImageId();
			imagePanel.setUrl(URL);
			imagePanel.setVisible(true);

		});
		menuPanel.add(li_03);

		// EDIT button
		li_04 = new MaterialLink();
		li_04.setIconType(IconType.EDIT);
		li_04.setLayoutPosition(Position.ABSOLUTE);
		li_04.setBottom(3);
		li_04.setRight(2);
		li_04.setBackgroundColor(Color.WHITE);
		li_04.setVisible(false);
		li_04.getElement().getFirstChildElement().getStyle().setMarginRight(0, Unit.PX);
		li_04.addClickHandler(event -> {
			setpVisible(false);
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("SECTION_OBJECT", this.diagObj);
			map.put("TARGET", getPanel());
			extWindow.openDialog(OtherDepartmentMainApplication.CATEGORY_MAIN_SELECT_CONTENT_EDIT, map, 800);
		});
		menuPanel.add(li_04);

		li_00.addClickHandler(event -> {
			setpVisible(!this.menuVisible);
			setupSelectedCheck();
		});

	}

	public void registContent() {
		if (this.linkObjList.size() > this.dispIndex) {
			this.linkObjList.remove(this.dispIndex);
		}
		this.linkObjList.add(this.dispIndex, diagObj);
	}

	public void setupContent(JSONObject diagObj) {
		this.diagObj = diagObj;

		Console.log("diagObj :: " + this.diagObj);
		String tempImageId = null;
		
		if (diagObj.containsKey("image")) {
			tempImageId = diagObj.get("image").isString().toString();
			imagePanel.setUrl(Registry.get("image.server") + "/img/call?cmd=VIEW&id=" + tempImageId.replaceAll("\"", ""));
			this.diagObj.put("IMG_ID", new JSONString(tempImageId.replaceAll("\"", "")));
		} else {
			this.diagObj.put("IMG_ID", null);
		}
		if (diagObj.containsKey("title"))
			this.diagObj.put("TITLE", new JSONString(diagObj.get("title").isString().toString().replaceAll("\"", "")));

		if (diagObj.containsKey("url"))
			this.diagObj.put("LINK_URL", new JSONString(diagObj.get("url").isString().toString().replaceAll("\"", "")));

		imagePanel.setVisible(true);
	}

	private CategoryMainContentPanel getPanel() {
		return this;
	}

	private void setupSelectedCheck() {
		MaterialPanel parentPanel = (MaterialPanel) this.getParent();
		for (Widget widget : parentPanel.getChildrenList()) {
			if (widget instanceof CategoryMainContentPanel) {
				CategoryMainContentPanel muicp = (CategoryMainContentPanel) widget;
				if (!muicp.getId().equals(this.getId())) {
					muicp.setpVisible(false);
				}
			}
		}
	}

	public void setpVisible(boolean visibleFlag) {
		this.menuVisible = visibleFlag;
		li_01.setVisible(this.menuVisible);
		li_03.setVisible(this.menuVisible);
		li_04.setVisible(this.menuVisible);
	}

	public void setWindow(MaterialExtentsWindow extWindow) {
		this.extWindow = extWindow;
	}

	public void setList(List<JSONObject> linkObjList) {
		this.linkObjList = linkObjList;
		if (this.linkObjList.size() > this.dispIndex) {
			setupContent(this.linkObjList.get(this.dispIndex));
		}
	}

	public void setDiaplayIndex(int idx) {
		this.dispIndex = idx;
	}

	public void setMenuDisplay(boolean visibleFlag) {
		this.li_00.setVisible(visibleFlag);
	}

	public JSONObject getJSONObject() {
		
		return this.diagObj;

	}

	@Override
	public void setSelected(boolean flag) {

	}

	@Override
	public void loadData() {

	}

	public void setData(JSONObject rsObj) {

		if (rsObj.get("result") == null) {
			this.diagObj = rsObj;
		} else {
			this.diagObj = rsObj.get("result").isObject();
		}

		this.diagObj.put("CTITLE", this.diagObj.get("TITLE"));

		if (this.diagObj.get("IMG_ID") == null) {

			String URL = (String) Registry.get("image.server") + "/img/call?cmd=VIEW&id="
					+ Registry.getDefaultImageId();
			imagePanel.setUrl(URL);
			imagePanel.setVisible(true);

		} else {

			String IMG_ID = this.diagObj.get("IMG_ID").isString().stringValue();
			String URL = (String) Registry.get("image.server") + "/img/call?cmd=VIEW&id=" + IMG_ID;
			imagePanel.setUrl(URL);
			imagePanel.setVisible(true);

		}
	}

}
