package kr.or.visitkorea.admin.client.manager.event.composite;

import java.util.HashMap;
import java.util.Map;

import com.google.gwt.dom.client.Style.Overflow;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONString;

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
import kr.or.visitkorea.admin.client.manager.event.dialogs.EventResultDescription;
import kr.or.visitkorea.admin.client.manager.event.dialogs.EventResultTemplate;
import kr.or.visitkorea.admin.client.manager.event.widgets.EventAnnouncePagePanel;
import kr.or.visitkorea.admin.client.manager.event.widgets.FetchCallback;
import kr.or.visitkorea.admin.client.widgets.window.MaterialExtentsWindow;

public class EventContentsResultTemplate extends AbstractEventContents {
	private MaterialExtentsWindow window;
	private MaterialPanel contentArea;
	private MaterialLink appendCommand;
	int eventAnnouncePagePanelSize;
	
	public EventContentsResultTemplate(EventContentsTree host, MaterialExtentsWindow window) {
		super(window);
		this.window = window;
	}

	@Override
	public void init() {
		super.init();
		this.setTitle("당첨자 발표 템플릿");
	}
	
	@Override
	public MaterialWidget render() {
		
		appendCommand = new MaterialLink();
		appendCommand.setMarginTop(10);
		appendCommand.setIconType(IconType.ADD);
		appendCommand.setTooltip("추가");
		appendCommand.setFloat(com.google.gwt.dom.client.Style.Float.RIGHT);
		//appendCommand.setDisplay(Display.INLINE_BLOCK);
		appendCommand.addClickHandler(event->{
			
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("evtId",this.getEvtId());
			this.window.addDialog(EventApplication.INSERT_EVENT_RESULT_TEMPLATE, new EventResultTemplate(this.window, this));
			window.openDialog(EventApplication.INSERT_EVENT_RESULT_TEMPLATE, params, 800);

		});
		this.add(appendCommand); 
		
		MaterialLink descCommand = new MaterialLink();
		descCommand.setMarginTop(10);
		descCommand.setIconType(IconType.EVENT_NOTE);
		descCommand.setTooltip("본문편집");
		descCommand.setFloat(com.google.gwt.dom.client.Style.Float.RIGHT);
		//descCommand.setDisplay(Display.INLINE_BLOCK);
		descCommand.addClickHandler(event->{
			
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("evtId",this.getEvtId());
			params.put("cotId",this.getCotId());
			this.window.addDialog(EventApplication.INSERT_EVENT_RESULT_DESCRIPTION, new EventResultDescription(this.window));
			this.window.openDialog(EventApplication.INSERT_EVENT_RESULT_DESCRIPTION, params, 800, 600);



		});
		this.add(descCommand);
		
		MaterialPanel panel = new MaterialPanel();
		panel.setHeight("534px");
		panel.getElement().getStyle().setOverflowY(Overflow.SCROLL);

		MaterialRow row = new MaterialRow();
		add(row);

		contentArea = new MaterialPanel();
		contentArea.setWidth("100%");
		panel.add(contentArea);
		
		return panel;
	}
	
	@Override
	public void loadData(FetchCallback callback) {
		
		this.contentArea.clear();

		
		JSONObject paramJson = new JSONObject();
		paramJson.put("cmd", new JSONString("GET_EVENT_ANNOUNCE_PAGE"));
		paramJson.put("evtId", new JSONString(this.getEvtId()));

		VisitKoreaBusiness.post("call", paramJson.toString(), (param1, param2, param3) -> {
			JSONObject resultObj = JSONParser.parseStrict(JSON.stringify((JsObject) param1)).isObject();
			String process = resultObj.get("header").isObject().get("process").isString().stringValue();
			
			if (process.equals("success")) {
				
				JSONObject objMaster = (JSONObject)resultObj.get("body").isObject().get("resultMaster");
				int announceType =  objMaster.containsKey("ANNOUNCE_TYPE") ? (int) objMaster.get("ANNOUNCE_TYPE").isNumber().doubleValue() : -1 ;
				if(announceType != 1) {
					//appendCommand.setVisible(false);
				}
				JSONArray resultArr = resultObj.get("body").isObject().get("result").isArray();
				int usrCnt = resultArr.size();
				eventAnnouncePagePanelSize = usrCnt;
				for (int i = 0; i < usrCnt; i++) {
					JSONObject obj = resultArr.get(i).isObject();
					addContentPanel(obj);
				}
			}
		});
	}

	@Override
	public void saveData() {
		/*
		JSONObject paramJson = new JSONObject();
		paramJson.put("cmd", new JSONString("SAVE_EVENT_USER_WIN_ALL"));
		paramJson.put("evtId", new JSONString(this.getEvtId()));
		
		JSONArray array = new JSONArray();
		List<Widget> contentList = contentArea.getChildrenList();
		int index = 0;
		for(Widget widget : contentList) {
			MaterialPanel winnerPanel = ((EventAnnouncePagePanel) widget).getWinnerPanel() ; 
			List<Widget> list = winnerPanel.getChildrenList();

			for(int i=0; i < list.size(); i++) {
				EventAnnouncePageSearchRow item = (EventAnnouncePageSearchRow)list.get(i) ;
				JSONObject obj = new JSONObject();
				obj.put("eeuId", new JSONString(item.getEeuId()));
				obj.put("evtId", new JSONString(this.getEvtId()));
				obj.put("subEvtId", new JSONString(item.getSubEvtId()));
				obj.put("gftId", new JSONString(item.getGftId()));
				obj.put("eapId", new JSONString(item.getEapId()));
				array.set(index++, obj);
			}
		}
		paramJson.put("model", array);
		
		VisitKoreaBusiness.post("call", paramJson.toString(), this.saveCallback());
		*/
	}

	@Override
	public void statusChangeProcess(EventStatus status) {
		
	}

	@Override
	public JSONObject buildEventModel() {
		JSONObject obj = new JSONObject();
		obj.put("evtId", new JSONString(this.getEvtId()));
		return obj;
	}


	@Override
	public void setupContentValue(JSONObject obj) {
		
	}
	
	
	public void addContentPanel(JSONObject gObj) {
		EventAnnouncePagePanel content = new EventAnnouncePagePanel(gObj, window, this);
		int electType = gObj.containsKey("ELECT_TYPE") ? (int) gObj.get("ELECT_TYPE").isNumber().doubleValue() : -1 ;
		int giftCnt = 0;
		int winCnt = 0;
		int notWinCnt = 0;
		StringBuffer sb = new StringBuffer();
		sb.append(electType == 0 ? "선정방식 : 추후선정" : "선정방식 : 즉시선정");
		if(gObj.containsKey("GIFT_CNT")) {
			giftCnt = (int) gObj.get("GIFT_CNT").isNumber().doubleValue() ;
			sb.append(" , 경품수량:").append(giftCnt).append("개");
		}
		if(gObj.containsKey("WIN_CNT")) {
			winCnt = (int) gObj.get("WIN_CNT").isNumber().doubleValue() ;
			sb.append(" , 추첨수량:").append(winCnt).append("개");
		}
		if(gObj.containsKey("WIN_CNT") && gObj.containsKey("GIFT_CNT")) {
			notWinCnt = giftCnt - winCnt;
			sb.append(" , 미추첨수량:").append(notWinCnt).append("개");
		}
		
		content.setLabel(sb.toString() );
		content.setElectType(electType);
		content.setEapId(gObj.containsKey("EAP_ID") ? gObj.get("EAP_ID").isString().stringValue() : "");
		content.setGftId(gObj.containsKey("GFT_ID") ? gObj.get("GFT_ID").isString().stringValue() : "");
		content.setEvtId(gObj.containsKey("EVT_ID") ? gObj.get("EVT_ID").isString().stringValue() : "");
		content.setSubEvtId(gObj.containsKey("SUB_EVT_ID") ? gObj.get("SUB_EVT_ID").isString().stringValue() : "");
		content.setPrizeNm(gObj.containsKey("PRIZE_NM") ? gObj.get("PRIZE_NM").isString().stringValue() : "");
		content.setGiftNesc(gObj.containsKey("GIFT_DESC") ? gObj.get("GIFT_DESC").isString().stringValue() : ""); 
		content.setSort(gObj.containsKey("SORT") ? (int) gObj.get("SORT").isNumber().doubleValue() : -1);
		String programNm = (gObj.containsKey("SUB_EVT_TITLE") ? gObj.get("SUB_EVT_TITLE").isString().stringValue():"" ) + " / " + (gObj.containsKey("GFT_TITLE") ?  gObj.get("GFT_TITLE").isString().stringValue() : "");
		content.setProgramNm(programNm);
		contentArea.add(content);
	}
	
	
	public MaterialPanel getContentArea() {
		return contentArea;
	}
	
	public int getEventAnnouncePagePanelSize() {
		return eventAnnouncePagePanelSize;
	}
	

}
