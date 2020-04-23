	package kr.or.visitkorea.admin.client.manager.otherDepartment.composite.localGov;
	
	import java.util.HashMap;
import java.util.Map;

import com.google.gwt.dom.client.Style.FontWeight;
import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.user.client.ui.Widget;

import gwt.material.design.client.base.AbstractIconButton;
import gwt.material.design.client.constants.Color;
import gwt.material.design.client.constants.IconType;
import gwt.material.design.client.ui.MaterialImage;
import gwt.material.design.client.ui.MaterialLabel;
import gwt.material.design.client.ui.MaterialLink;
import gwt.material.design.client.ui.MaterialPanel;
import kr.or.visitkorea.admin.client.application.Registry;
import kr.or.visitkorea.admin.client.manager.otherDepartment.OtherDepartmentMainApplication;
import kr.or.visitkorea.admin.client.manager.widgets.IDUtil;
import kr.or.visitkorea.admin.client.widgets.window.MaterialExtentsWindow;
	
	public class MetroUIContentPanel extends MaterialPanel {
	
		private MetroUI uiType;
		private boolean menuVisible = false;
		private MaterialLink li_01;
		private AbstractIconButton li_03;
		private AbstractIconButton li_04;
		private MaterialExtentsWindow window;
		private MaterialPanel contentPanel;
		private MaterialImage imagePanel;
		private JSONObject diagObj;
		private JSONArray linkObjList;
		private int dispIndex;
		private MaterialLabel headerLabel;
		private MaterialLabel titleLabel;
		
		public MetroUIContentPanel(MetroUI uiType) {
			super();
			this.uiType = uiType;
			init();
		}
	
		public MetroUIContentPanel(String... initialClass) {
			super(initialClass);
			init();
		}
	
		private void init() {
			
			this.setId(IDUtil.uuid());
	
			this.setBorder("1px solid #aaaaaa");
			
			contentPanel =new MaterialPanel();
			contentPanel.setLayoutPosition(Position.ABSOLUTE);
			contentPanel.setTop(0);
			contentPanel.setRight(0);
			contentPanel.setWidth("100%");
			contentPanel.setHeight("100%");
			contentPanel.setVisible(false);
			this.add(contentPanel);
			
			headerLabel = new MaterialLabel("헤더");
			headerLabel.setLayoutPosition(Position.ABSOLUTE);
			headerLabel.setFontSize("0.7em");
			headerLabel.setTop(10);
			headerLabel.setLeft(10);
			contentPanel.add(headerLabel);
			
			titleLabel = new MaterialLabel("타이틀");
			titleLabel.setLayoutPosition(Position.ABSOLUTE);
			titleLabel.setFontWeight(FontWeight.BOLD);
			titleLabel.setTop(30);
			titleLabel.setLeft(10);
			contentPanel.add(titleLabel);
			
			imagePanel =new MaterialImage();
			imagePanel.setLayoutPosition(Position.ABSOLUTE);
			imagePanel.setTop(0);
			imagePanel.setRight(0);
			imagePanel.setWidth("100%");
			imagePanel.setHeight("100%");
			imagePanel.setBackgroundColor(Color.RED);
			imagePanel.setVisible(false);
			
			imagePanel.addErrorHandler(event->{
				imagePanel.setValue(Registry.getDefaultImagePath());
			});
			this.add(imagePanel);
			
			MaterialPanel menuPanel =new MaterialPanel();
			menuPanel.setLayoutPosition(Position.ABSOLUTE);
			menuPanel.setTop(0);
			menuPanel.setRight(0);
			menuPanel.setWidth("100%");
			menuPanel.setHeight("100%");
			this.add(menuPanel);
			
			
			// menu button
			MaterialLink li_00 = new MaterialLink();
			li_00.setIconType(IconType.MENU);
			li_00.setLayoutPosition(Position.ABSOLUTE);
			li_00.setTop(2);
			li_00.setRight(2);
			li_00.getElement().getFirstChildElement().getStyle().setMarginRight(0, Unit.PX);
			li_00.setBackgroundColor(Color.WHITE);
			menuPanel.add(li_00);
		
			// add button
			li_01 = new MaterialLink();
			li_01.setIconType(IconType.ADD);
			li_01.setLayoutPosition(Position.ABSOLUTE);
			li_01.setTop(2);
			li_01.setLeft(2);
			li_01.setVisible(false);
			li_01.getElement().getFirstChildElement().getStyle().setMarginRight(0, Unit.PX);
			li_01.setBackgroundColor(Color.WHITE);
			li_01.addClickHandler(event->{
			});
			//menuPanel.add(li_01);
	
			// remove button
			li_03 = new MaterialLink();
			li_03.setIconType(IconType.REMOVE);
			li_03.setLayoutPosition(Position.ABSOLUTE);
			li_03.setBottom(3);
			li_03.setLeft(2);
	//		li_03.setBorder("1px solid #bbbbbb");
			li_03.setBackgroundColor(Color.WHITE);
			li_03.setVisible(false);
			li_03.getElement().getFirstChildElement().getStyle().setMarginRight(0, Unit.PX);
			li_03.addClickHandler(event->{
				
			});
			//menuPanel.add(li_03);
	
			// edit button
			li_04 = new MaterialLink();
			li_04.setIconType(IconType.EDIT);
			li_04.setLayoutPosition(Position.ABSOLUTE);
			li_04.setBottom(3);
			li_04.setRight(2);
			li_04.setBackgroundColor(Color.WHITE);
			li_04.setVisible(false);
			li_04.getElement().getFirstChildElement().getStyle().setMarginRight(0, Unit.PX);
			li_04.addClickHandler(event->{
				setpVisible(false);
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("LINK_OBJECT", this.diagObj);
				map.put("TARGET", getPanel());
				window.openDialog(OtherDepartmentMainApplication.LOCAL_GOV_METRO_UI_EDIT, map, 800);
			});
			menuPanel.add(li_04);
			
			li_00.addClickHandler(event->{
				setpVisible(!this.menuVisible);
				setupSelectedCheck();
			});
			
		}
	
		public void registContent() {
			if (this.linkObjList.size() > this.dispIndex) {
				this.linkObjList.set(this.dispIndex, null);
			}
			this.linkObjList.set(this.dispIndex, diagObj);
		}
		
		public void setupContent(JSONObject diagObj) {
			
			this.diagObj = diagObj;
			
			String contentType = diagObj.get("CONTENT_TYPE_NAME").isString().stringValue().toLowerCase();
			
			// in case of text display mode
			if (contentType.equals("text")) {
				
				imagePanel.setVisible(false);
				contentPanel.setVisible(true);

				String header = "";
				String headerColor = "";
				String title = "";

				if (diagObj.get("HEADER") != null) header = diagObj.get("HEADER").isString().toString().replaceAll("\"", "");
				if (diagObj.get("HEADER_COLOR") != null) headerColor = diagObj.get("HEADER_COLOR").isString().toString().replaceAll("\"", "");
				if (diagObj.get("TITLE") != null) title = diagObj.get("TITLE").isString().toString().replaceAll("\"", "");
			
				headerLabel.setText(header);
				headerLabel.getElement().getStyle().setProperty("color", headerColor);
				titleLabel.setText(title);
				
			}else if (contentType.equals("image")) {
			// in case of image display mode
				
				String tempImageId = "";
				if (diagObj.get("IMG_ID") == null || diagObj.get("IMG_ID").isString().stringValue() == "") {
					tempImageId = Registry.getDefaultImageId();
				}else {
					tempImageId = diagObj.get("IMG_ID").isString().toString();
				}
				
				imagePanel.setUrl((String) Registry.get("image.server") + "/img/call?cmd=VIEW&id=" + tempImageId.replaceAll("\"", ""));
				imagePanel.setVisible(true);
				contentPanel.setVisible(false);
				
			}
			
		}
		
/*		
		private void setupContentForDialog(JSONObject diagObj) {
			
			this.diagObj = diagObj;
			
			// in case of text display mode
			if (diagObj.get("contentType").isString().stringValue().toLowerCase().equals("true")) {
				imagePanel.setVisible(false);
				contentPanel.setVisible(true);

				String header = diagObj.get("header").isString().toString().replaceAll("\"", "");
				String headerColor = diagObj.get("headerColor").isString().toString().replaceAll("\"", "");
				String title = diagObj.get("title").isString().toString().replaceAll("\"", "");
			
				headerLabel.setText(header);
				headerLabel.getElement().getStyle().setProperty("color", headerColor);
				titleLabel.setText(title);
				
			}else {
			// in case of image display mode
				
				String tempImageId = diagObj.get("image").isString().toString();
				String ext = diagObj.get("ext").isString().toString();
				String savePath = "";
				String[] imgMainSplitArr = tempImageId.split("-");
				for (String splitArrMember : imgMainSplitArr) {
					savePath += "/" + splitArrMember.substring(0, 2);
				}
				savePath += "/" +tempImageId + ext;
				
				imagePanel.setUrl((String) Registry.get("image.server") + "/img/call?cmd=TEMP_VIEW&name=" + (tempImageId + ext).replaceAll("\"", ""));
				imagePanel.setVisible(true);
				contentPanel.setVisible(false);
			}
			
		}
*/
		private MetroUIContentPanel getPanel() {
			return this;
		}

		private void setupSelectedCheck() {
			MaterialPanel parentPanel = (MaterialPanel)this.getParent();
			for (Widget widget : parentPanel.getChildrenList()) {
				MetroUIContentPanel muicp = (MetroUIContentPanel)widget;
				if (!muicp.getId().equals(this.getId())) {
					muicp.setpVisible(false);
				}
			}
		}
		
		public void setpVisible(boolean visibleFlag) {
			this.menuVisible = visibleFlag;
			li_01.setVisible(this.menuVisible);
			li_03.setVisible(this.menuVisible);
			li_04.setVisible(this.menuVisible);
		}
	
		public void setWindow(MaterialExtentsWindow window) {
			this.window = window;
		}

		public void setList(JSONArray jObjArray) {
			this.linkObjList = jObjArray;
			if ( this.linkObjList.size() > this.dispIndex ) {
				setupContent(this.linkObjList.get(this.dispIndex).isObject());
			}
		}

		public void setDiaplayIndex(int idx) {
			this.dispIndex = idx;
		}
	
		
		
	}
