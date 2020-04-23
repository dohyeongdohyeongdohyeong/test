package kr.or.visitkorea.admin.client.manager.otherDepartment.widgets;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gwt.dom.client.Style.Cursor;
import com.google.gwt.dom.client.Style.Float;
import com.google.gwt.dom.client.Style.Overflow;
import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.json.client.JSONNumber;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.user.client.ui.Widget;

import gwt.material.design.client.constants.Color;
import gwt.material.design.client.constants.IconType;
import gwt.material.design.client.ui.MaterialLink;
import gwt.material.design.client.ui.MaterialPanel;
import gwt.material.design.client.ui.MaterialToast;
import gwt.material.design.jquery.client.api.Functions.Func3;
import gwt.material.design.jscore.client.api.JSON;
import gwt.material.design.jscore.client.api.JsObject;
import kr.or.visitkorea.admin.client.application.Console;
import kr.or.visitkorea.admin.client.application.Registry;
import kr.or.visitkorea.admin.client.manager.VisitKoreaBusiness;
import kr.or.visitkorea.admin.client.manager.otherDepartment.OtherDepartmentMainApplication;
import kr.or.visitkorea.admin.client.manager.otherDepartment.composite.AbtractOtherDepartmentMainContents;
import kr.or.visitkorea.admin.client.manager.otherDepartment.composite.OtherDepartmentMainEditor;
import kr.or.visitkorea.admin.client.manager.otherDepartment.composite.detail.OtherDepartmentMainContentDetail;
import kr.or.visitkorea.admin.client.manager.otherDepartment.composite.detail.OtherDepartmentMainContentDetailForShowcase;
import kr.or.visitkorea.admin.client.manager.otherDepartment.composite.title.OtherDepartmentTitleContentDetail;
import kr.or.visitkorea.admin.client.manager.otherDepartment.widgets.comp.SingleLargeImage;
import kr.or.visitkorea.admin.client.manager.widgets.IDUtil;

/**
 * @author yaken
 *
 */
public class ServiceCompStack extends MaterialPanel {

	private MaterialPanel topMenuPanel;
	private MaterialPanel bottomMenuPanel;
	private ServiceMain serviceMain;
	private MaterialPanel itemDisplayPanel;
	private MaterialPanel itemPanel;
	private MaterialLink addLink;
	private MaterialLink remLink;
	private List<MainAreaComponent> areaComponents;
	private ViewPanel viewPanel;
	private MaterialLink upLink;
	private MaterialLink downLink;
	private boolean only03;
	private AreaComponent selectedComponent;
	private OtherDepartmentMainEditor masterPanel;
	private String otdId;
	private String areaValue;
	private String manId;

	public ServiceCompStack() {
		super();
		this.setLayoutPosition(Position.ABSOLUTE);
		this.setHeight("570px");
		buildLayout();
		setupMenu();
		setupMenuEvent();
	}

	private void setupMenuEvent() {
		
		addLink.addClickHandler(event->{
			
			Console.log("this.areaComponents :: " + this.areaComponents);
			Console.log("this.viewPanel :: " + this.viewPanel);
			Console.log("this.only03 :: " + this.only03);
			
			if (this.areaComponents != null && this.viewPanel != null) {
				
				if (this.only03) {
					insertComponent(new SingleLargeImage(com.google.gwt.dom.client.Style.Float.LEFT));
					
				}else {
					
					String areaAttr = this.viewPanel.getElement().getAttribute("area");
					Console.log("areaAttr :: " + areaAttr);
					
					if (areaAttr.equals("A") || areaAttr.equals("B") || areaAttr.equals("C")) {
						Map<String, Object> diagParam = new HashMap<String, Object>();
						diagParam.put("COMP_STACK", this);
						diagParam.put("SHOWCASE", this.only03);
						
						OtherDepartmentMainEditor acp = (OtherDepartmentMainEditor)this.getParent();
						acp.getMaterialExtentsWindow().openDialog(OtherDepartmentMainApplication.SELECT_UI_COMPONENT, diagParam, 1080);
						
					}else if (areaAttr.equals("SHOWCASE_WITH_TITLE")) {
						Map<String, Object> diagParam = new HashMap<String, Object>();
						diagParam.put("COMP_STACK", this);
						diagParam.put("SHOWCASE", this.only03);
						
						OtherDepartmentMainEditor acp = (OtherDepartmentMainEditor)this.getParent();
						acp.getMaterialExtentsWindow().openDialog(OtherDepartmentMainApplication.SELECT_UI_COMPONENT_2, diagParam, 620);
						
					}

				}
			}
		});
		
		remLink.addClickHandler(event->{
			if (selectedComponent != null && itemPanel.getChildrenList().size() > 0) {
				deleteItem();
			}
		});
		
		upLink.addClickHandler(event->{
			if (selectedComponent != null && areaComponents.size() > 0) {
				up();
			}
		});
		
		downLink.addClickHandler(event->{
			if (selectedComponent != null && areaComponents.size() > 0) {
				down();
			}
		});
		
		
		
	}

	private void up() {
		
		
		int widgetIndex = areaComponents.indexOf((MainAreaComponent)selectedComponent);
		if (widgetIndex > 0) {
			Collections.swap(areaComponents, widgetIndex, widgetIndex-1);
			clearRow();
			renderComponent();
			reOrder();
		}

	}

	private void down() {
		
		
		int widgetIndex = areaComponents.indexOf((MainAreaComponent)selectedComponent);
		if (widgetIndex < areaComponents.size()-1) {
			Collections.swap(areaComponents, widgetIndex, widgetIndex+1);
			clearRow();
			renderComponent();
			reOrder();
		}
		
	}

	private void reOrder() {
		
		String reOrderString = "";
		
		int size = areaComponents.size();
		for (int i=0; i<size; i++) {
			
			String compId = ((AreaComponent)areaComponents.get(i)).getInfo().get(0).get("COMP_ID").isString().stringValue();
			if (i == 0) {
				reOrderString += ( i+ "_" + compId); 
			}else {
				reOrderString += ( "," + i + "_" + compId); 
			}
		}
		
		JSONObject jObj = new JSONObject();
		jObj.put("cmd", new JSONString("REORDER_DEPT_AREA"));
		jObj.put("ORDER", new JSONString(reOrderString));
		jObj.put("TYPE", new JSONString("COMP_ORDER"));

		executeBusiness(jObj);
		
		
	}

	private void deleteItem() {
		
		OtherDepartmentMainEditor acp = (OtherDepartmentMainEditor)this.getParent();
		acp.getMaterialExtentsWindow().confirm("정말 삭제 하시겠습니까?",event->{
			if (event.getSource().toString().contains("yes")) {
				List<JSONObject> infoList = selectedComponent.getInfo();
				for (JSONObject infoObj : infoList) {
					infoObj.put("cmd", new JSONString("DELETE_DEPT_AREA"));
					executeBusiness(infoObj);
				}
				
				if(areaComponents.size() > 0) {
					JSONObject infoObj = new JSONObject();
					infoObj.put("cmd", new JSONString("UPDATE_DEPT_ORDER"));
					infoObj.put("OTD_ID", new JSONString(selectedComponent.getOTD_ID()));
					infoObj.put("ORDER", new JSONNumber(selectedComponent.getCOMP_ORDER()));
					infoObj.put("MAIN_AREA", new JSONString(selectedComponent.getMAIN_AREA()));
					executeBusiness(infoObj);
					
					for (int i = selectedComponent.getCOMP_ORDER(); i < areaComponents.size(); i++) {
						AreaComponent comp = (AreaComponent) areaComponents.get(i);
						comp.setCOMP_ORDER(comp.getCOMP_ORDER()-1);
					}
					
				}
				
				int widgetIndex = itemPanel.getWidgetIndex(selectedComponent);
				itemPanel.remove(widgetIndex);
				areaComponents.remove(widgetIndex);
				this.viewPanel.setComponentCount(areaComponents.size());
				
				if (itemPanel.getChildrenList().size() > widgetIndex) {
					Widget tgrWidget = itemPanel.getWidget(widgetIndex);
					
					if (selectedComponent != null) {
						selectedComponent.getTitleLabel().setTextColor(Color.BLACK);
					}

					selectedComponent = (AreaComponent) tgrWidget;
					clickComponent(selectedComponent);
					selectedComponent.getTitleLabel().setTextColor(Color.RED);

				} else if(itemPanel.getChildrenList().size() == 0) {
					selectedComponent = null;
					masterPanel.onoff(false);
				} else{
					
					int newWidgetIndex = widgetIndex - 1;
					
					if (newWidgetIndex > -1) {
					
						Widget tgrWidget = itemPanel.getWidget(newWidgetIndex);
						
						if (selectedComponent != null) {
							selectedComponent.getTitleLabel().setTextColor(Color.BLACK);
						}

						selectedComponent = (AreaComponent) tgrWidget;
						clickComponent(selectedComponent);
						selectedComponent.getTitleLabel().setTextColor(Color.RED);
						
					}
				} 
				
				MaterialToast.fireToast("해당 컴포넌트가 삭제 되었습니다.");
			}
		});

		
		
	}

	private void setupMenu() {
		
		addLink = appendLinkIcon(this.topMenuPanel, IconType.ADD, com.google.gwt.dom.client.Style.Float.RIGHT, "");
		remLink = appendLinkIcon(this.bottomMenuPanel, IconType.REMOVE, com.google.gwt.dom.client.Style.Float.LEFT, "1px solid #aaaaaa");
		downLink = appendLinkIcon(this.bottomMenuPanel, IconType.KEYBOARD_ARROW_DOWN, com.google.gwt.dom.client.Style.Float.RIGHT, "1px solid #aaaaaa");
		upLink = appendLinkIcon(this.bottomMenuPanel, IconType.KEYBOARD_ARROW_UP, com.google.gwt.dom.client.Style.Float.RIGHT, "1px solid #aaaaaa");
	}

	private MaterialLink appendLinkIcon(MaterialPanel tgrPanel, IconType iconType, Float floatStyle, String rightBorderStyle) {
		MaterialLink tmpLink = new MaterialLink();
		tmpLink.setIconType(iconType);
		tmpLink.setMargin(0);
		tmpLink.setFloat(floatStyle);
		if (floatStyle.equals(Float.LEFT)) {
			tmpLink.setBorderRight(rightBorderStyle);
		}else{
			tmpLink.setBorderLeft(rightBorderStyle);
		}
		tgrPanel.add(tmpLink);
		tmpLink.getIcon().setMargin(0);
		return tmpLink;
	}

	private void buildLayout() {
		
		topMenuPanel = new MaterialPanel();
		topMenuPanel.setWidth("100%");
		topMenuPanel.setHeight("24px");
		topMenuPanel.setLayoutPosition(Position.ABSOLUTE);
		topMenuPanel.setTop(-30);
		topMenuPanel.setLeft(0);
		this.add(topMenuPanel);
		
		bottomMenuPanel = new MaterialPanel();
		bottomMenuPanel.setWidth("100%");
		bottomMenuPanel.setHeight("24px");
		bottomMenuPanel.setLayoutPosition(Position.ABSOLUTE);
		bottomMenuPanel.setBottom(0);
		bottomMenuPanel.setBorderTop("1px solid #aaaaaa");
		bottomMenuPanel.setLeft(0);
		this.add(bottomMenuPanel);
		
		itemDisplayPanel = new MaterialPanel();
		itemDisplayPanel.getElement().getStyle().setOverflowX(Overflow.HIDDEN);
		itemDisplayPanel.getElement().getStyle().setOverflowY(Overflow.SCROLL);
		itemDisplayPanel.setWidth("100%");
		itemDisplayPanel.setHeight("544.01px");
		itemDisplayPanel.setLayoutPosition(Position.ABSOLUTE);
		itemDisplayPanel.setTop(0);
		itemDisplayPanel.setBottom(24);
		itemDisplayPanel.setLeft(0);
		this.add(itemDisplayPanel);
		
		itemPanel = new MaterialPanel();
		itemPanel.setWidth("100%");
		itemPanel.setHeight("0px");
		itemPanel.setLayoutPosition(Position.ABSOLUTE);
		itemPanel.setTop(0);
		itemPanel.setBottom(0);
		itemPanel.setLeft(0);
		itemDisplayPanel.add(itemPanel);

	}

	public void setServiceMain(ServiceMain serviceMain) {
		this.serviceMain = serviceMain;
	}

	public void clearRow() {
		this.itemPanel.clear();
	}

	public void appendComponentItems(List<MainAreaComponent> areaComponents) {
		this.areaComponents = areaComponents;
		if (areaComponents != null) renderComponent();
	}

	private void renderComponent() {
		
		for (MainAreaComponent compo : areaComponents) {
			addEvent((AreaComponent)compo);
			this.itemPanel.add((Widget) compo);
		}
		
		if (selectedComponent != null && areaComponents.size() > 0) clickComponent(selectedComponent);
		
	}

	public void setTargetArea(ViewPanel tgrPanel) {
		this.viewPanel = tgrPanel;
		if (tgrPanel != null) {
			String areaAttr = this.viewPanel.getElement().getAttribute("area");
			if (areaAttr != null && areaAttr.equals("S")) {
				this.only03 = true;
			}else {
				this.only03 = false;
			}
		}
	}

	public void insertComponent(AreaComponent areaComponent) {
		
		this.setHeight("570px");
		AreaComponent tgrCompo = addEvent(areaComponent);

		String componentID = Integer.parseInt(tgrCompo.getTitleLabel().getText()) + "";
		
		String COMP_ID = IDUtil.uuid();
		String TITLE_REC_ODM_ID = IDUtil.uuid();
		
		JSONObject jObj = new JSONObject();
		jObj.put("cmd", new JSONString("INSERT_DEPT_AREA_ROW"));
		jObj.put("COMP_ID", new JSONString(COMP_ID));
		jObj.put("COMP_ORDER", new JSONNumber(areaComponents.size()));
		jObj.put("VIEW_TITLE", new JSONNumber(1));
		jObj.put("COT_ORDER", new JSONNumber(0));
		jObj.put("TEMPLATE_ID", new JSONString(componentID));
		jObj.put("MAIN_AREA", new JSONString(this.areaValue));
		jObj.put("ODM_ID", new JSONString(TITLE_REC_ODM_ID));
		jObj.put("OTD_ID", new JSONString(this.otdId));
		jObj.put("TITLE", new JSONString(""));
		
		executeBusiness(jObj);

		// setup default component
		tgrCompo.setCOMP_ID(COMP_ID);
		tgrCompo.setCOMP_ORDER(areaComponents.size());
		tgrCompo.setTEMPLATE_ID(componentID);
		tgrCompo.setMAIN_AREA(this.areaValue);
		tgrCompo.setOTD_ID(this.otdId);
		tgrCompo.setTitle("");
		tgrCompo.setODM_ID(TITLE_REC_ODM_ID);
		tgrCompo.setVIEW_TITLE(1);
		
		tgrCompo.appendInfo(jObj);
		this.itemPanel.add(tgrCompo);
		this.areaComponents.add(tgrCompo);
		this.viewPanel.setComponentCount(areaComponents.size());
		
	}
	
	private void executeBusiness(JSONObject parameterJSON) {
		
		VisitKoreaBusiness.post("call", parameterJSON.toString(), new Func3<Object, String, Object>() {

			@Override
			public void call(Object param1, String param2, Object param3) {

				JSONObject resultObj = (JSONObject) JSONParser.parseStrict(JSON.stringify((JsObject)param1));
				JSONObject headerObj = (JSONObject) resultObj.get("header");
				String processResult = headerObj.get("process").isString().toString();
				processResult = processResult.replaceAll("\"", "");
				
				if (processResult.equals("success")) {
					
				}
			}
		});

	}

	private AreaComponent addEvent(AreaComponent aac) {
		
		aac.addMouseOverHandler(event->{
			if (selectedComponent != aac)
			aac.getTitleLabel().setTextColor(Color.BLUE);
			aac.getElement().getStyle().setCursor(Cursor.POINTER);
		});
		
		aac.addMouseOutHandler(event->{
			if (selectedComponent != aac)
			aac.getTitleLabel().setTextColor(Color.BLACK);
			aac.getElement().getStyle().setCursor(Cursor.DEFAULT);
		});
		
		aac.addClickHandler(event->{
			
			clickComponent(aac);
			
		});

		return aac;

	}

	private void clickComponent(AreaComponent aac) {
		masterPanel.onoff(true);
		if (selectedComponent != null) {
			selectedComponent.getTitleLabel().setTextColor(Color.BLACK);
		} 

		selectedComponent = aac;
		aac.getTitleLabel().setTextColor(Color.RED);
		int areaComponentIndex = aac.getCompDetailPanelIndex();

		AbtractOtherDepartmentMainContents goTargetPanel = (AbtractOtherDepartmentMainContents)this.masterPanel.go(aac.getCompDetailPanelIndex());
		
		if (areaComponentIndex == 2) {
			OtherDepartmentMainContentDetail  detail = (OtherDepartmentMainContentDetail) goTargetPanel;
			detail.setAreaComponent(aac);
		}else if (areaComponentIndex == 9) {
			OtherDepartmentMainContentDetailForShowcase  detail = (OtherDepartmentMainContentDetailForShowcase) goTargetPanel;
			detail.setAreaComponent(aac);
		}else if (areaComponentIndex == 10) {
			OtherDepartmentTitleContentDetail  detail = (OtherDepartmentTitleContentDetail) goTargetPanel;
			detail.setAreaComponent(aac);
		} else if (areaComponentIndex == 14) {
			OtherDepartmentTitleContentDetail  detail = (OtherDepartmentTitleContentDetail) goTargetPanel;
			detail.setAreaComponent(aac);
		}
		
		
	}
	
	private void setPermissionRole() {
		
		addLink.setEnabled(true);
		remLink.setEnabled(true);
		upLink.setEnabled(true);
		downLink.setEnabled(true);
		
		if (manId != null && manId.equals("05427f59-a30a-11e8-8165-020027310001")) { // PC 버전
			addLink.setEnabled(Registry.getPermission("20933535-b3e2-449a-a630-918ebf344815"));
			remLink.setEnabled(Registry.getPermission("fc0669de-f0a0-4489-b02a-c550f1f55ad6"));
			upLink.setEnabled(Registry.getPermission("0735270f-2859-40b6-8f07-698e29a315dd"));
			downLink.setEnabled(Registry.getPermission("0735270f-2859-40b6-8f07-698e29a315dd"));
		} else if (manId != null && manId.equals("9ab9e7af-ba3a-472b-a8bb-3203aa21d0a2")) { // 모바일 버전
			addLink.setEnabled(Registry.getPermission("f8600a2e-6baf-4ce5-8802-8d22e0e9b7c4"));
			remLink.setEnabled(Registry.getPermission("77d63808-8f87-47de-9a0a-92739a53767d"));
			upLink.setEnabled(Registry.getPermission("239b7579-7ba0-484f-8fa1-4ee5899e176a"));
			downLink.setEnabled(Registry.getPermission("239b7579-7ba0-484f-8fa1-4ee5899e176a"));
		}
		
	}

	public void setMasterPanel(OtherDepartmentMainEditor otherDepartmentMainEditor) {
		this.masterPanel = otherDepartmentMainEditor;
	}

	public void setOtdId(String oTD_ID) {
		this.otdId = oTD_ID;
	}
	
	public void setManId(String MAN_ID) {
		this.manId = MAN_ID;
		this.setPermissionRole();
	}

	public void setAreaValue(String areaValue) {
		this.areaValue = areaValue;
	}

	public void setVisibleTopMenu(boolean visibleFlag) {
		this.topMenuPanel.setVisible(visibleFlag);
	}

	public boolean isExist(String compId) {
		
		for (MainAreaComponent mac : areaComponents) {
			if (mac.getTitleLabel().getText().equals(compId)) {
				return true;
			}
		}
		
		return false;
	}

}
