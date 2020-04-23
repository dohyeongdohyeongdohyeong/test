package kr.or.visitkorea.admin.client.manager.event.composite;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gwt.dom.client.Style.Overflow;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONNumber;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.user.client.ui.Widget;

import gwt.material.design.client.base.MaterialWidget;
import gwt.material.design.client.constants.IconType;
import gwt.material.design.client.ui.MaterialLink;
import gwt.material.design.client.ui.MaterialPanel;
import gwt.material.design.client.ui.MaterialRow;
import gwt.material.design.jscore.client.api.JSON;
import gwt.material.design.jscore.client.api.JsObject;
import kr.or.visitkorea.admin.client.manager.VisitKoreaBusiness;
import kr.or.visitkorea.admin.client.manager.event.EventApplication;
import kr.or.visitkorea.admin.client.manager.event.EventContentsTree;
import kr.or.visitkorea.admin.client.manager.event.EventStatus;
import kr.or.visitkorea.admin.client.manager.event.components.AbstractEventComponent;
import kr.or.visitkorea.admin.client.manager.event.components.EventComponentFactory;
import kr.or.visitkorea.admin.client.manager.event.components.EventComponentText;
import kr.or.visitkorea.admin.client.manager.event.components.EventComponentTreeFactory;
import kr.or.visitkorea.admin.client.manager.event.components.EventComponentType;
import kr.or.visitkorea.admin.client.manager.event.components.IEventComponent;
import kr.or.visitkorea.admin.client.manager.event.dialogs.EventResultDescription;
import kr.or.visitkorea.admin.client.manager.event.model.EventComponent;
import kr.or.visitkorea.admin.client.manager.event.tree.EventTreeItem;
import kr.or.visitkorea.admin.client.manager.event.widgets.FetchCallback;
import kr.or.visitkorea.admin.client.widgets.window.MaterialExtentsWindow;

public class EventContentsResultComponents extends AbstractEventContents {
	private MaterialLink descCommand;
	private EventContentsTree host;
	private MaterialPanel panel;
	private List<AbstractEventComponent> componentList = new ArrayList<>();
	private MaterialExtentsWindow window;
	
	public EventContentsResultComponents(EventContentsTree host, MaterialExtentsWindow window) {
		super(window);
		this.window = window;
		this.host = host;
	}

	@Override
	public void init() {
		super.init();
		this.setTitle("당첨자 발표 컴포넌트");
	}
	
	@Override
	public MaterialWidget render() {
		
		descCommand = new MaterialLink();
		descCommand.setMarginTop(10);
		descCommand.setIconType(IconType.EVENT_NOTE);
		descCommand.setTooltip("본문편집");
		descCommand.setFloat(com.google.gwt.dom.client.Style.Float.RIGHT);
		
		descCommand.addClickHandler(event->{
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("evtId", getEvtId());
			params.put("cotId", getCotId());
			this.window.addDialog(EventApplication.INSERT_EVENT_RESULT_DESCRIPTION, new EventResultDescription(this.window));
			this.window.openDialog(EventApplication.INSERT_EVENT_RESULT_DESCRIPTION, params, 800, 600);
		});
		MaterialRow row = new MaterialRow();
		add(row);
		row.setMarginBottom(0);
		row.add(descCommand);
		panel = new MaterialPanel();
		panel.setPadding(10);
		panel.setHeight("91.5%");
		panel.getElement().getStyle().setOverflowY(Overflow.SCROLL);

		return panel;
	}
	

	public void addComponent(AbstractEventComponent component) {
		component.setWindow(this.getMaterialExtendsWindow());
		component.setEventContentsResultComponents(this);
		this.componentList.add(component);
	}
	
	public void renderComponent() {
		this.panel.clear();
		
		//	왼쪽 트리 구조 초기화
		this.host.getResultTreeItem().getTreeItems().forEach(item -> {
			item.removeFromTree();
		});
		
		//	삭제된 컴포넌트를 제외한 컴포넌트 갯수
		int originSize = (int) this.componentList.stream()
				.filter(o -> !o.getComponentObj().isDelete())
				.count();
					
		//	삭제된 컴포넌트를 제외하고 rendering
		this.componentList.stream()
			.sorted((o1, o2) -> o1.getComponentObj().getCompIdx() > o2.getComponentObj().getCompIdx() ? 1 : -1)
			.filter(item -> !item.getComponentObj().isDelete())
			.forEach(item -> {
				switch (this.eventStatus) {
					case WRITING: {
						item.visibleAllIcons(true);
						item.visibleSaveEditIcon(true, false, true);
						
						if (item.getComponentObj().getCompIdx() == 0)
							item.visibleOrderIcon(false, true);
						else if (item.getComponentObj().getCompIdx() == originSize - 1)
							item.visibleOrderIcon(true, false);
						else
							item.visibleOrderIcon(true, true);
					} break;
					
					case APPROVAL_WAIT: {
						item.visibleAllIcons(false);
					} break;
					
					default: break;
				}
				this.host.getResultTreeItem().add(item.getTreeItem());
				this.panel.add(item);
			});
	}
	
	@Override
	public void loadData(FetchCallback callback) {
		this.componentList.clear();
		JSONObject paramJson = new JSONObject();
		paramJson.put("cmd", new JSONString("GET_EVENT_RESULT_COMPONENT"));
		paramJson.put("evtId", new JSONString(this.getEvtId()));
		paramJson.put("cotId", new JSONString(this.getCotId()));
		VisitKoreaBusiness.post("call", paramJson.toString(), (param1, param2, param3) -> {
			JSONObject resultObj = JSONParser.parseStrict(JSON.stringify((JsObject) param1)).isObject();
			String process = resultObj.get("header").isObject().get("process").isString().stringValue();
			
			if (process.equals("success")) {
				JSONArray bodyArr = resultObj.get("body").isObject().get("result").isArray();
				
				for (int i = 0; i < bodyArr.size(); i++) {
					JSONObject obj = bodyArr.get(i).isObject();
					
					this.setupContentValue(obj);
				}
				this.renderComponent();
			}
		});
	}

	@Override
	public void saveData() {
		JSONObject paramJson = new JSONObject();
		paramJson.put("cmd", new JSONString("SAVE_EVENT_RESULT_COMPONENT"));
		paramJson.put("model", this.buildEventModel());
		VisitKoreaBusiness.post("call", paramJson.toString(), this.saveCallback());
	}

	@Override
	public void setupContentValue(JSONObject obj) {
		EventComponent componentObj = EventComponent.fromJson(obj);
		EventComponentType componentType = EventComponentType.values()[componentObj.getCompType()];
		
		EventTreeItem treeItem = EventComponentTreeFactory.getInstance(componentType);
		IEventComponent component = EventComponentFactory.getInstance(componentType, this.host,window,"result");
		
		AbstractEventComponent aComponent = (AbstractEventComponent) component;
		aComponent.setComponentObj(componentObj);
		aComponent.setTreeItem(treeItem);
		aComponent.setupContents();
		
		treeItem.addClickHandler(e -> {
			this.getComponentList().forEach(item -> {
				this.host.goDetail(this.host.getResultTreeItem().getSlidingValue() * -1);
				item.refresh();
			});
		});
		
		this.addComponent(aComponent);
	}

	@Override
	public JSONObject buildEventModel() {
		JSONObject model = new JSONObject();
		JSONArray components = new JSONArray();
		model.put("COMPONENTS", components);
		
		int index = 0;
		
		for (AbstractEventComponent component : this.componentList) {
			EventComponent componentObj = component.getComponentObj();
			
			JSONObject obj = componentObj.toJson();
			components.set(components.size(), obj);

			if (componentObj.isDelete())
				continue;
			
			obj.put("COMP_IDX", new JSONNumber(index++));
		}
		
		return model;
	}
	
	@Override
	public void statusChangeProcess(EventStatus status) {
		this.eventStatus = status;
	}
	
	public List<AbstractEventComponent> getComponentList() {
		return componentList;
	}

	public void orderUp(int idx) {
		List<Widget> treeList = this.host.getResultTreeItem().getChildrenList();
		Collections.swap(treeList, idx - 1, idx);
		
		AbstractEventComponent currComponent = this.componentList.get(idx);
		AbstractEventComponent prevComponent = this.componentList.get(idx - 1);

		currComponent.getComponentObj().setCompIdx(idx - 1);
		prevComponent.getComponentObj().setCompIdx(idx);
		
		Collections.swap(this.componentList, idx - 1, idx);
		
		this.renderComponent();
	}
	
	public void orderDown(int idx) {
		List<Widget> treeList = this.host.getResultTreeItem().getChildrenList();
		Collections.swap(treeList, idx, idx + 1);
		
		AbstractEventComponent currComponent = this.componentList.get(idx);
		AbstractEventComponent nextComponent = this.componentList.get(idx + 1);

		currComponent.getComponentObj().setCompIdx(idx + 1);
		nextComponent.getComponentObj().setCompIdx(idx);
		
		Collections.swap(this.componentList, idx, idx + 1);
		
		this.renderComponent();
	}
}
