package kr.or.visitkorea.admin.client.manager.otherDepartment.composite.festival;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gwt.dom.client.Style.Float;
import com.google.gwt.dom.client.Style.Overflow;
import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.json.client.JSONArray;
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
import kr.or.visitkorea.admin.client.manager.otherDepartment.composite.category.ContentLayoutPanel;
import kr.or.visitkorea.admin.client.manager.otherDepartment.composite.category.ContentSection;
import kr.or.visitkorea.admin.client.manager.otherDepartment.composite.category.festival.FestivalContentLayoutA;
import kr.or.visitkorea.admin.client.manager.otherDepartment.composite.category.festival.FestivalContentLayoutB;
import kr.or.visitkorea.admin.client.manager.otherDepartment.composite.category.festival.FestivalContentLayoutC;
import kr.or.visitkorea.admin.client.manager.otherDepartment.widgets.AreaComponent;
import kr.or.visitkorea.admin.client.manager.otherDepartment.widgets.ViewPanel;
import kr.or.visitkorea.admin.client.widgets.window.MaterialExtentsWindow;

public class MonthlyContentDetailPanel extends AbtractOtherDepartmentMainContents {
	private static final String MONTHLY_DATA = "MONTHLY_DATA";
	private ViewPanel contentPanel;
	private ViewPanel targetViewPanel;

	public MonthlyContentDetailPanel(MaterialExtentsWindow materialExtentsWindow, OtherDepartmentMainEditor otherDepartmentMainEditor) {
		super(materialExtentsWindow);
		Registry.put("MonthlyContentDetailPanel", this);
		buildContent();
	}

	@Override
	public void init() {
		super.init();
		this.setLayoutPosition(Position.RELATIVE);
		this.setHeight("100%");
		this.setTitle("월별 컨텐츠 설정");
	}

	private void buildContent() {
		MaterialPanel bottomPanel = new MaterialPanel();
		bottomPanel.setBorderTop("1px solid #181818");
		bottomPanel.setBackgroundColor(Color.BLUE);
		bottomPanel.setLayoutPosition(Position.ABSOLUTE);
		bottomPanel.setHeight("24px");
		bottomPanel.setWidth("99%");
		bottomPanel.setBottom(0);
		this.add(bottomPanel);
		
		MaterialLink uplink = new MaterialLink();
		uplink.setFloat(Float.RIGHT);
		uplink.setTooltip("위로");
		uplink.setLineHeight(24);
		uplink.setIconType(IconType.KEYBOARD_ARROW_UP);
		uplink.setIconColor(Color.WHITE);
		uplink.addClickHandler(event->{
			selectedPanelMoveUp();
		});
		bottomPanel.add(uplink);
		uplink.getElement().getFirstChildElement().getStyle().setMarginRight(3, Unit.PX);
		
		MaterialLink dnlink = new MaterialLink();
		dnlink.setFloat(Float.RIGHT);
		dnlink.setTooltip("아래로");
		dnlink.setLineHeight(24);
		dnlink.setIconType(IconType.KEYBOARD_ARROW_DOWN);
		dnlink.setIconColor(Color.WHITE);
		dnlink.addClickHandler(event->{
			selectedPanelMoveDown();
		});
		bottomPanel.add(dnlink);
		dnlink.getElement().getFirstChildElement().getStyle().setMarginRight(3, Unit.PX);
		
		MaterialLink removelink = new MaterialLink();
		removelink.setFloat(Float.LEFT);
		removelink.setTooltip("삭제");
		removelink.setLineHeight(24);
		removelink.setIconType(IconType.REMOVE);
		removelink.setIconColor(Color.WHITE);
		removelink.addClickHandler(event->{
			removeSelectedPanel();
		});
		bottomPanel.add(removelink);
		removelink.getElement().getFirstChildElement().getStyle().setMarginRight(3, Unit.PX);

		MaterialLink appendLink = this.addLink(new MaterialLink());
		appendLink.setLayoutPosition(Position.ABSOLUTE);
		appendLink.setTooltip("추가");
		appendLink.setTop(5);
		appendLink.setRight(0);
		appendLink.setIconType(IconType.ADD);
		appendLink.setIconColor(Color.WHITE);
		appendLink.addClickHandler(event->{
			
			Map<String, Object> paramMap = new HashMap<String, Object>();
			paramMap.put("TARGET", this);
			paramMap.put("DISP_TYPE", 1);
			getWindow().openDialog(OtherDepartmentMainApplication.CATEGORY_MAIN_SELECT_CONTENT_LAYOUT, paramMap, 800);
			saveContent(event);
		});

		MaterialLink headLink = this.addLink(new MaterialLink());
		headLink.setLayoutPosition(Position.ABSOLUTE);
		headLink.setTooltip("서버 반영");
		headLink.setTop(5);
		headLink.setRight(30);
		headLink.setIconType(IconType.SAVE);
		headLink.setIconColor(Color.WHITE);
		headLink.addClickHandler(event->{
			Map<String, Object> paramMap = new HashMap<String, Object>();
			paramMap.put("SAVE_CLICK_HANDLER", new ClickHandler() {
				
				@Override
				public void onClick(ClickEvent ev) {
					List<Widget> children = contentPanel.getChildrenList();
					JSONArray jarray = new JSONArray();
					int i=0;
					for (Widget widget : children) {
						
						ContentLayoutPanel contentLayoutPanel = (ContentLayoutPanel)widget;
						JSONObject targetObject = contentLayoutPanel.getJSONObject();
						targetObject.put("COMP_ORDER", new JSONNumber(i));
						jarray.set(i, targetObject);
						i++;
					}
					
					targetViewPanel.setData(MONTHLY_DATA, jarray);
					int mainType = Integer.parseInt(titleLabel.getValue().replaceAll("[^0-9]", ""));
					
					JSONObject parameterJSON = new JSONObject();
					parameterJSON.put("cmd", new JSONString("INSERT_FESTIVAL_MAIN_MONTHLY"));
					parameterJSON.put("CONTENTS", jarray);
					parameterJSON.put("MAIN_TYPE", new JSONNumber(mainType+2));
					
					VisitKoreaBusiness.post("call", parameterJSON.toString(), new Func3<Object, String, Object>() {
						@Override
						public void call(Object param1, String param2, Object param3) {
							JSONObject resultObj = (JSONObject) JSONParser.parseStrict(JSON.stringify((JsObject)param1));
							JSONObject headerObj = (JSONObject) resultObj.get("header");
							String processResult = headerObj.get("process").isString().toString();
							processResult = processResult.replaceAll("\"", "");
							
							if (processResult.equals("success")) {
								MaterialToast.fireToast("성공적으로 저장되었습니다.", 5000);
							}
						}
					});
				}
			});
			
			getWindow().openDialog(OtherDepartmentMainApplication.SAVE_CONTENTS, paramMap, 600);

			saveContent(event);
		});
		
		contentPanel = new ViewPanel();
		contentPanel.setLayoutPosition(Position.ABSOLUTE);
		contentPanel.setPaddingLeft(180);
		contentPanel.setTop(32);
		contentPanel.setWidth("99%");
		contentPanel.setBottom(24);
		contentPanel.getElement().getStyle().setOverflowX(Overflow.HIDDEN);
		contentPanel.getElement().getStyle().setOverflowY(Overflow.SCROLL);
		this.add(contentPanel);
		
	}
	
	public void removeSelectedPanel() {
		contentPanel.getSelectedItem();
		MaterialPanel tgrPanel = contentPanel.getSelectedItem();
		if (tgrPanel != null){
			tgrPanel.removeFromParent();
		}
	}

	public void selectedPanelMoveUp() {
		MaterialPanel tgrPanel = contentPanel.getSelectedItem();
		if (tgrPanel != null){
			int widgetIndex = this.contentPanel.getWidgetIndex(tgrPanel);
			if (widgetIndex > 0) {
				
				MaterialPanel prevPanel = (MaterialPanel) this.contentPanel.getWidget(widgetIndex-1);
				prevPanel.removeFromParent();
				
				this.contentPanel.insert(prevPanel, widgetIndex);
			}
		}
		
	}

	public void selectedPanelMoveDown() {
		MaterialPanel tgrPanel = contentPanel.getSelectedItem();
		
		if (tgrPanel != null){
			int widgetIndex = this.contentPanel.getWidgetIndex(tgrPanel);
			if (widgetIndex + 1 < this.contentPanel.getWidgetCount()) {
				
				MaterialPanel nextPanel = (MaterialPanel) this.contentPanel.getWidget(widgetIndex+1);
				nextPanel.removeFromParent();
				
				this.contentPanel.insert(nextPanel, widgetIndex);
			}
		}
		
	}
	
	public ContentLayoutPanel buildContentTemplate(String contentType, JSONObject value) {
		
		ContentLayoutPanel retPanel = null;
		
		if (contentType.equals("3")) {
			FestivalContentLayoutA layoutA = new FestivalContentLayoutA(ContentSection.FESTIVAL);
			layoutA.setWindow(this.getWindow());
			layoutA.setMainType(Integer.parseInt(this.titleLabel.getValue().replaceAll("[^0-9]", "")) + 2);
			retPanel = layoutA;
		}else if (contentType.equals("4")) {
			FestivalContentLayoutB layoutB = new FestivalContentLayoutB(ContentSection.FESTIVAL);			
			layoutB.setWindow(this.getWindow());
			layoutB.setMainType(Integer.parseInt(this.titleLabel.getValue().replaceAll("[^0-9]", "")) + 2);
			retPanel = layoutB;
		}else if (contentType.equals("5")) {
			FestivalContentLayoutC layoutC = new FestivalContentLayoutC(ContentSection.FESTIVAL);
			layoutC.setWindow(this.getWindow());
			layoutC.setMainType(Integer.parseInt(this.titleLabel.getValue().replaceAll("[^0-9]", "")) + 2);
			retPanel = layoutC;
		}
		
		retPanel.setData(value);
		retPanel.loadData();
		contentPanel.add(retPanel);
		this.targetViewPanel.setComponentCount(contentPanel.getChildrenList().size());
		
		return retPanel;
	}

	private void saveContent(ClickEvent event) {
		
	}

	@Override
	public void loading(boolean loadFlag) {
		super.loading(loadFlag);
	}

	@Override
	public void setReadOnly(boolean readFlag) {}

	public void setAreaComponent(AreaComponent aac) {
	}

	@Override
	public void loadData() {
	}

	public void setViewPanel(ViewPanel targetViewPanel) {
		this.targetViewPanel = targetViewPanel;
		contentPanel.clear();
		setTitle(this.targetViewPanel.getAreaNameLabel().getText() + " 컨텐츠 설정" );
		loadMonthlyData();
	}

	private void loadMonthlyData() {
		
		JSONObject parameterJSON = new JSONObject();
		parameterJSON.put("cmd", new JSONString("GET_FESTIVAL_MAIN"));
		
		VisitKoreaBusiness.post("call", parameterJSON.toString(), new Func3<Object, String, Object>() {
			@Override
			public void call(Object param1, String param2, Object param3) {
				JSONObject resultObj = (JSONObject) JSONParser.parseStrict(JSON.stringify((JsObject)param1));
				JSONObject headerObj = (JSONObject) resultObj.get("header");
				String processResult = headerObj.get("process").isString().toString();
				processResult = processResult.replaceAll("\"", "");
				
				if (processResult.equals("success")) {
					JSONObject bodyObj =  resultObj.get("body").isObject();
					JSONObject monthlyObj = bodyObj.get("result").isObject();
					
					if (monthlyObj.get("COMPONENT") != null) {
						JSONArray monthlyCompArray = monthlyObj.get("COMPONENT").isArray();
						
						if (monthlyCompArray != null) {
							int activatedComp = Integer.parseInt(titleLabel.getValue().replaceAll("[^0-9]", "")) + 2;

							for (int i=0; i<monthlyCompArray.size(); i++) {
								
								if (activatedComp != (int) monthlyCompArray.get(i).isObject().get("MAIN_TYPE").isNumber().doubleValue())
									continue;
								if (monthlyCompArray.get(i).isObject() != null) {
									JSONObject jObj = monthlyCompArray.get(i).isObject();
									String compType = jObj.get("COMP_TYPE").isString().stringValue();
									buildContentTemplate(compType.replaceAll("\"",  ""), jObj);
								}
							}
							
						}
					}
				}
			}
		});
	}
}