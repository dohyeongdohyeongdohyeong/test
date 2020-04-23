package kr.or.visitkorea.admin.client.manager.event.widgets;


import java.util.List;

import com.google.gwt.dom.client.Style.Float;
import com.google.gwt.dom.client.Style.FontWeight;
import com.google.gwt.dom.client.Style.VerticalAlign;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONNumber;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.user.client.ui.Widget;

import gwt.material.design.client.base.MaterialWidget;
import gwt.material.design.client.constants.Color;
import gwt.material.design.client.constants.IconType;
import gwt.material.design.client.constants.TextAlign;
import gwt.material.design.client.ui.MaterialButton;
import gwt.material.design.client.ui.MaterialColumn;
import gwt.material.design.client.ui.MaterialIcon;
import gwt.material.design.jscore.client.api.JSON;
import gwt.material.design.jscore.client.api.JsObject;

import gwt.material.design.client.ui.MaterialLabel;
import gwt.material.design.client.ui.MaterialLink;
import gwt.material.design.client.ui.MaterialPanel;
import gwt.material.design.client.ui.MaterialRow;
import gwt.material.design.client.ui.MaterialTextBox;
import gwt.material.design.client.ui.MaterialToast;
import kr.or.visitkorea.admin.client.application.Console;
import kr.or.visitkorea.admin.client.manager.VisitKoreaBusiness;
import kr.or.visitkorea.admin.client.manager.event.composite.EventContentsResultTemplate;
import kr.or.visitkorea.admin.client.widgets.window.MaterialExtentsWindow;

public class EventAnnouncePagePanel extends MaterialPanel{

	/** 경품 아이디*/
	private String gftId;
	private String evtId;
	private String subEvtId;
	/** 당첨 페이지 아이디*/
	private String eapId; 
	/** 선정방식 */
	private int electType ; 
	
	/** 정렬순서 */
	private int sort;
	
	private MaterialLabel label;
	private MaterialTextBox prizeNm;
	private MaterialTextBox giftNesc;
	private MaterialTextBox programNm;
	private MaterialTextBox searchWord;
	private MaterialPanel searchResultPanel;
	private MaterialPanel winnerPanel;
	private MaterialLink orderUpIcon;
	private MaterialLink orderDownIcon;
	private MaterialButton randomButton;
	private MaterialIcon searchIcon ;
	
	private MaterialTextBox searchWinWord;
	private MaterialIcon searchWinIcon ;
	
	private JSONObject internalGiftObject;
	private MaterialExtentsWindow window ;
	private EventContentsResultTemplate parent;
	
	

	
	public EventAnnouncePagePanel(JSONObject internalGiftObject, MaterialExtentsWindow window, EventContentsResultTemplate parent) {
		super();
		this.internalGiftObject = internalGiftObject;
		this.window = window;
		this.parent = parent;
		init();
	}
	
	public void init() {
		this.setMarginBottom(50);
		
		MaterialPanel panel = new MaterialPanel();
		MaterialRow row = null;
		this.add(panel);

		panel.add(buildHeader());
		
		row = addRow(panel);
		addLabel(row, "프로그램", Color.GREY_LIGHTEN_3, "s2");
		programNm = addInputBox(row, "이벤트명 / 경품명 ", "s10");

		
		row = addRow(panel);
		addLabel(row, "상이름", Color.GREY_LIGHTEN_3, "s2");
		prizeNm = addInputBox(row, "", "s10");


		row = addRow(panel);
		addLabel(row, "경품명/수량", Color.GREY_LIGHTEN_3, "s2");
		giftNesc = addInputBox(row, "", "s10");
		

		
		row = addRow(panel);
		label = addLabel(row, "선정방식 및 수량", TextAlign.LEFT, Color.GREY_LIGHTEN_3, "s12"); 
		
		row = addRow(panel);
		
		randomButton = new MaterialButton("랜덤추첨");
		randomButton.setMargin(5);
		randomButton.setBackgroundColor(Color.RED_LIGHTEN_2);
		randomButton.setFloat(Float.RIGHT);
		randomButton.addClickHandler(event -> {
			getRandomList();
		});
	
		
		//당첨자 목록 판넬
		addLabel(row, "당첨자 목록", TextAlign.LEFT, Color.GREY_LIGHTEN_3, "s12", randomButton); 
		row = addRow(panel);


		searchWinWord = addInputBox(row, "당첨자 핸드폰번호 검색", "s4");
	    
		searchWinIcon = new MaterialIcon(IconType.SEARCH);
		searchWinIcon.setGrid("");
		searchWinIcon.setMarginTop(11);
		searchWinIcon.setTooltip("당첨자 핸드폰 번호로 검색");
		searchWinIcon.addClickHandler(event -> {
			searchResultPanel.clear();
			JSONObject paramJson = new JSONObject();
			paramJson.put("cmd", new JSONString("GET_EVENT_USER_WIN_LIST"));
			paramJson.put("subEvtId", new JSONString(subEvtId));
			paramJson.put("gftId", new JSONString(gftId));
			paramJson.put("eapId", new JSONString(eapId));
			paramJson.put("evtId", new JSONString(evtId));
			paramJson.put("tel", new JSONString(searchWinWord.getText()));
			
			VisitKoreaBusiness.post("call", paramJson.toString(), (param1, param2, param3) -> {
				JSONObject resultObj = JSONParser.parseStrict(JSON.stringify((JsObject) param1)).isObject();
				String process = resultObj.get("header").isObject().get("process").isString().stringValue();
				
				if (process.equals("success")) {
					winnerPanel.clear();
					JSONArray resultArr = resultObj.get("body").isObject().get("result").isArray();
					int usrCnt = resultArr.size();
					if(usrCnt == 0) {
						MaterialToast.fireToast("당첨자 휴대폰 번호를 찾을 수 없습니다.", 1000);
					}else {
						for (int i = 0; i < usrCnt; i++) {
							JSONObject obj = resultArr.get(i).isObject();
							addItemPanel(obj);
						}
					}
				}
			});
		});
		row.add(this.searchWinIcon);
		row = addRow(panel);
		
		winnerPanel = new MaterialPanel();
		row.add(winnerPanel);
		
		MaterialButton randomAddButton = new MaterialButton("랜덤추가");
		randomAddButton.setMargin(5);
		randomAddButton.setBackgroundColor(Color.BLUE);
		randomAddButton.setFloat(Float.RIGHT);
		randomAddButton.addClickHandler(event -> {
			getRandomAddList();
		});
		
		addLabel(row, "미당첨자 검색", TextAlign.LEFT, Color.GREY_LIGHTEN_3, "s12", randomAddButton); 
	
		row = addRow(panel);
		searchWord = addInputBox(row, "핸드폰번호 검색", "s4");
	    
		searchIcon = new MaterialIcon(IconType.SEARCH);
		searchIcon.setGrid("");
		searchIcon.setMarginTop(11);
		searchIcon.setTooltip("미당첨자 핸드폰 번호로 검색");
		searchIcon.addClickHandler(event -> {
			searchResultPanel.clear();
			JSONObject paramJson = new JSONObject();
			paramJson.put("cmd", new JSONString("GET_EVENT_USER_ENTRY_LIST"));
			paramJson.put("subEvtId", new JSONString(subEvtId));
			paramJson.put("tel", new JSONString(searchWord.getText()));

			VisitKoreaBusiness.post("call", paramJson.toString(), (param1, param2, param3) -> {
				JSONObject resultObj = JSONParser.parseStrict(JSON.stringify((JsObject) param1)).isObject();
				String process = resultObj.get("header").isObject().get("process").isString().stringValue();
				
				if (process.equals("success")) {
					JSONArray resultArr = resultObj.get("body").isObject().get("result").isArray();
					int usrCnt = resultArr.size();
					if(usrCnt == 0) {
						MaterialToast.fireToast("당첨 가능한 휴대폰 번호를 찾을 수 없습니다.", 1000);
					}else {
						for (int i = 0; i < usrCnt; i++) {
							JSONObject obj = resultArr.get(i).isObject();
							addSearchItemPanel(obj);
						}
					}
				}
			});
		});
		row.add(this.searchIcon);
		row = addRow(panel);
		
		
		//검색결과 Panel
		searchResultPanel = new MaterialPanel();
		row.add(searchResultPanel);
		
	    
	}
	
	@Override
	public void onLoad() {
		super.onLoad();
		getWinnerList();
		int itemSize =  parent.getEventAnnouncePagePanelSize();
		
		Console.log("itemSize :" + itemSize);
		
		//선정방식이 즉시선정일 경우
		if(electType == 1) {
			this.randomButton.setVisible(false);
		}
		
		if(sort == 0) {
			orderUpIcon.setVisible(false);
		}else if(sort == itemSize-1) {
			orderDownIcon.setVisible(false);
		}

	}
	
	
	public MaterialPanel getWinnerPanel() {
		return winnerPanel;
	}
	
	/**당첨자 목록*/
	public void getWinnerList() {
		
		winnerPanel.clear();
		JSONObject paramJson = new JSONObject();
		paramJson.put("cmd", new JSONString("GET_EVENT_USER_WIN_LIST"));
		paramJson.put("subEvtId", new JSONString(subEvtId));
		paramJson.put("gftId", new JSONString(gftId));
		paramJson.put("eapId", new JSONString(eapId));
		paramJson.put("evtId", new JSONString(evtId));
		
		VisitKoreaBusiness.post("call", paramJson.toString(), (param1, param2, param3) -> {
			JSONObject resultObj = JSONParser.parseStrict(JSON.stringify((JsObject) param1)).isObject();
			String process = resultObj.get("header").isObject().get("process").isString().stringValue();
			
			if (process.equals("success")) {
				JSONArray resultArr = resultObj.get("body").isObject().get("result").isArray();
				int usrCnt = resultArr.size();
				for (int i = 0; i < usrCnt; i++) {
					JSONObject obj = resultArr.get(i).isObject();
					addItemPanel(obj);
				}
			}
		});
	}
	
	/** 랜덤추첨*/
	public void getRandomList() {

		
		window.confirm("기존에 등록된 당첨자가 초기화 됩니다. 계속 진행하시겠습니까?", clickEvent -> {
			MaterialButton btn = (MaterialButton) clickEvent.getSource();
			if (btn.getId().equals("yes")) {

				winnerPanel.clear();
				JSONObject paramJson = new JSONObject();
				paramJson.put("cmd", new JSONString("GET_EVENT_USER_RANDOM_LIST"));
				paramJson.put("subEvtId", new JSONString(subEvtId));
				paramJson.put("gftId", new JSONString(gftId));
				paramJson.put("evtId", new JSONString(evtId));
				paramJson.put("eapId", new JSONString(eapId));
				
				VisitKoreaBusiness.post("call", paramJson.toString(), (param1, param2, param3) -> {
					JSONObject resultObj = JSONParser.parseStrict(JSON.stringify((JsObject) param1)).isObject();
					String process = resultObj.get("header").isObject().get("process").isString().stringValue();
					
					if (process.equals("success")) {
						JSONArray resultArr = resultObj.get("body").isObject().get("result").isArray();
						int usrCnt = resultArr.size();
						for (int i = 0; i < usrCnt; i++) {
							JSONObject obj = resultArr.get(i).isObject();
							addItemPanel(obj);
						}
					}else {
						String ment = resultObj.get("header").isObject().get("ment").isString().stringValue();
						window.alert(ment);
					}
				});
			}

		});
	}
	
	/** 랜덤추첨*/
	public void getRandomAddList() {

		window.confirm("랜덤으로 당첨자가 추가됩니다. 계속 진행하시겠습니까?", clickEvent -> {
			MaterialButton btn = (MaterialButton) clickEvent.getSource();
			if (btn.getId().equals("yes")) {

				JSONObject paramJson = new JSONObject();
				paramJson.put("cmd", new JSONString("GET_EVENT_USER_RANDOM_LIST"));
				paramJson.put("subEvtId", new JSONString(subEvtId));
				paramJson.put("gftId", new JSONString(gftId));
				paramJson.put("evtId", new JSONString(evtId));
				paramJson.put("eapId", new JSONString(eapId));
				paramJson.put("mode", new JSONString("add"));
				
				VisitKoreaBusiness.post("call", paramJson.toString(), (param1, param2, param3) -> {
					JSONObject resultObj = JSONParser.parseStrict(JSON.stringify((JsObject) param1)).isObject();
					String process = resultObj.get("header").isObject().get("process").isString().stringValue();
					
					if (process.equals("success")) {
						JSONArray resultArr = resultObj.get("body").isObject().get("result").isArray();
						int usrCnt = resultArr.size();
						for (int i = 0; i < usrCnt; i++) {
							JSONObject obj = resultArr.get(i).isObject();
							addItemPanel(obj);
						}
						//parent.loadData(null);
					}else {
						String ment = resultObj.get("header").isObject().get("ment").isString().stringValue();
						window.alert(ment);
					}
				});
			}

		});
	}
	
	/** 담청자 목록*/
	public void addItemPanel(JSONObject gObj) {

		EventAnnouncePageSearchRow item = new EventAnnouncePageSearchRow(this, true);
		item.setEeuId(gObj.containsKey("EEU_ID") ? gObj.get("EEU_ID").isString().stringValue() : "");
		item.setName(gObj.containsKey("NAME") ? gObj.get("NAME").isString().stringValue() : "");
		item.setSubEvtId(subEvtId);
		item.setTel(gObj.containsKey("TEL") ? gObj.get("TEL").isString().stringValue() : "");
		item.setEvtId(evtId);
		item.setGftId(gftId);
		item.setEapId(eapId);
		winnerPanel.add(item);
	}
	
	/** 검색 목록*/
	public void addSearchItemPanel(JSONObject gObj) {
		EventAnnouncePageSearchRow item = new EventAnnouncePageSearchRow(this,false);
		item.setEeuId(gObj.containsKey("EEU_ID") ? gObj.get("EEU_ID").isString().stringValue() : "");
		item.setName(gObj.containsKey("NAME") ? gObj.get("NAME").isString().stringValue() : "");
		item.setEvtId(gObj.containsKey("EVT_ID") ? gObj.get("EVT_ID").isString().stringValue() : "");
		item.setSubEvtId(subEvtId);
		item.setTel(gObj.containsKey("TEL") ? gObj.get("TEL").isString().stringValue() : "");
		item.setGftId(gftId);
		item.setEapId(eapId);
		item.setEvtId(evtId);
		searchResultPanel.add(item);
	}
	



	private MaterialLabel buildHeader() {
		MaterialLabel titleLabel = new MaterialLabel();
		titleLabel.setWidth("100%");
		titleLabel.setHeight("26px");
		titleLabel.setBackgroundColor(Color.BLUE);
		titleLabel.setTextAlign(TextAlign.CENTER);
		titleLabel.setTextColor(Color.WHITE);
		
		this.add(titleLabel);

		orderUpIcon = addIcon(titleLabel, IconType.ARROW_UPWARD, Float.LEFT);
		orderUpIcon.addClickHandler(e -> {
			
			int sort = (int) internalGiftObject.get("SORT").isNumber().doubleValue();
			if(sort > 0) {
			
				List<Widget> contentList = parent.getContentArea().getChildrenList();
				EventAnnouncePagePanel currComponent = (EventAnnouncePagePanel)contentList.get(sort);
				EventAnnouncePagePanel prevComponent = (EventAnnouncePagePanel)contentList.get(sort - 1);
				

				JSONObject paramJson = new JSONObject();
				paramJson.put("cmd", new JSONString("SAVE_EVENT_ANNOUNCE_PAGE_SORT"));
				paramJson.put("currEapId", new JSONString(currComponent.getEapId()));
				paramJson.put("prevEapId", new JSONString(prevComponent.getEapId()));
				paramJson.put("currSort", new JSONNumber(sort-1));
				paramJson.put("prevSort", new JSONNumber(sort));
				paramJson.put("mode", new JSONString("up"));

				
				VisitKoreaBusiness.post("call", paramJson.toString(), (param1, param2, param3) -> {
					JSONObject resultObj = JSONParser.parseStrict(JSON.stringify((JsObject) param1)).isObject();
					String process = resultObj.get("header").isObject().get("process").isString().stringValue();
					
					if (process.equals("success")) {
						parent.loadData(null);
					}
				});
			}
			
			
		});
		
		orderDownIcon = addIcon(titleLabel, IconType.ARROW_DOWNWARD, Float.LEFT);
		orderDownIcon.addClickHandler(e -> {
			int sort = (int) internalGiftObject.get("SORT").isNumber().doubleValue();
			
				List<Widget> contentList = parent.getContentArea().getChildrenList();
				
				EventAnnouncePagePanel currComponent = (EventAnnouncePagePanel)contentList.get(sort);
				EventAnnouncePagePanel nextComponent = (EventAnnouncePagePanel)contentList.get(sort + 1);
				

				JSONObject paramJson = new JSONObject();
				paramJson.put("cmd", new JSONString("SAVE_EVENT_ANNOUNCE_PAGE_SORT"));
				paramJson.put("currEapId", new JSONString(currComponent.getEapId()));
				paramJson.put("prevEapId", new JSONString(nextComponent.getEapId()));
				paramJson.put("currSort", new JSONNumber(sort+1));
				paramJson.put("prevSort", new JSONNumber(sort));

				
				VisitKoreaBusiness.post("call", paramJson.toString(), (param1, param2, param3) -> {
					JSONObject resultObj = JSONParser.parseStrict(JSON.stringify((JsObject) param1)).isObject();
					String process = resultObj.get("header").isObject().get("process").isString().stringValue();
					
					if (process.equals("success")) {
						parent.loadData(null);
					}
				});
			
		});
		
		MaterialLink removeIcon = addIcon(titleLabel, IconType.REMOVE, Float.RIGHT);
		removeIcon.addClickHandler(e -> {
			remove();
		});
		
		return titleLabel ;
	}
	
	private void remove() {
		window.confirm("삭제 경고", "해당 컴포넌트를 삭제시겠습니까?", 450, e -> {
			if (e.getSource().toString().contains("yes")) {
				
				JSONObject paramJson = new JSONObject();
				paramJson.put("cmd", new JSONString("DELETE_EVENT_ANNOUNCE_PAGE"));
				paramJson.put("evtId", new JSONString(getEvtId()));
				paramJson.put("eapId", new JSONString(getEapId()));
				paramJson.put("gftId", new JSONString(getGftId()));
				paramJson.put("subEvtId", new JSONString(getSubEvtId()));

				VisitKoreaBusiness.post("call", paramJson.toString(), (param1, param2, param3) -> {
					JSONObject resultObj = JSONParser.parseStrict(JSON.stringify((JsObject) param1)).isObject();
					String process = resultObj.get("header").isObject().get("process").isString().stringValue();
					
					if (process.equals("success")) {
						parent.loadData(null);
					}
				});
				
				
			}
		});
	}
	
	public void setLabel(JSONObject gObj) { 
		
		
	};
	
	
	public String getGftId() {
		return gftId;
	}

	public void setGftId(String gftId) {
		this.gftId = gftId;
	}

	public String getEvtId() {
		return evtId;
	}

	public void setEvtId(String evtId) {
		this.evtId = evtId;
	}

	public String getSubEvtId() {
		return subEvtId;
	}

	public void setSubEvtId(String subEvtId) {
		this.subEvtId = subEvtId;
	}

	public String getLabel() {
		return this.label.getText();
	}

	public void setLabel(String index) {
		 this.label.setText(index);
	}


	public String getPrizeNm() {
		return prizeNm.getText();
	}

	public void setPrizeNm(String prizeNm) {
		this.prizeNm.setText(prizeNm);
	}

	public String getGiftNesc() {
		return giftNesc.getText();
	}

	public void setGiftNesc(String giftNesc) {
		this.giftNesc.setText(giftNesc);
	}
	
	public String getProgramNm() {
		return programNm.getText();
	}

	public void setProgramNm(String programNm) {
		this.programNm.setText(programNm);
	}
	

	public int getSort() {
		return sort;
	}

	public void setSort(int sort) {
		this.sort = sort;
	}

	public void setEapId(String eapId) {
		this.eapId = eapId;
	}
	
	public String getEapId() {
		return eapId;
	}
	
	public int getElectType() {
		return electType;
	}

	public void setElectType(int electType) {
		this.electType = electType;
	}

	
	private MaterialTextBox addInputBox(MaterialRow row, String placeholder, String grid) {
		MaterialTextBox tBox = new MaterialTextBox();
		tBox.setPlaceholder(placeholder);
		tBox.setMargin(0);
		tBox.setHeight("46.25px");

		
		MaterialColumn col = new MaterialColumn();
		col.setGrid(grid);
		col.add(tBox);
		row.add(col);
		return tBox;
	}
	
	private MaterialRow addRow(MaterialWidget widget) {
		MaterialRow row = new MaterialRow();
		widget.add(row);
		return row;
	}


	
	private MaterialLabel addLabel(MaterialRow row, String text, Color color, String grid) {
		MaterialLabel label = new MaterialLabel(text);
		label.setBackgroundColor(color);
		label.setLineHeight(46.25);
		label.setFontWeight(FontWeight.BOLD);

		MaterialColumn col = new MaterialColumn();
		col.setGrid(grid);
		col.add(label);
		row.add(col);
		return label;
	}
	
	
	protected MaterialLabel addLabel(MaterialRow row, String defaultValue, TextAlign tAlign, Color bgColor, String grid) {
		MaterialLabel tmpLabel = new MaterialLabel(defaultValue);
		tmpLabel.setTextAlign(tAlign);
		tmpLabel.setLineHeight(46.25);
		tmpLabel.setHeight("46.25px");
		tmpLabel.setBackgroundColor(bgColor);
		tmpLabel.setFontWeight(FontWeight.BOLD);
		
		MaterialColumn col1 = new MaterialColumn();
		col1.setGrid(grid);
		col1.add(tmpLabel);
		row.add(col1);
		return tmpLabel;
	}
	
	protected MaterialLabel addLabel(MaterialRow row, String defaultValue, TextAlign tAlign, Color bgColor, String grid, MaterialButton btn) {
		MaterialLabel tmpLabel = new MaterialLabel(defaultValue);
		tmpLabel.setTextAlign(tAlign);
		tmpLabel.setLineHeight(46.25);
		tmpLabel.setHeight("46.25px");
		tmpLabel.setBackgroundColor(bgColor);
		tmpLabel.setFontWeight(FontWeight.BOLD);
		tmpLabel.add(btn);
		
		MaterialColumn col1 = new MaterialColumn();
		col1.setGrid(grid);
		col1.add(tmpLabel);
		row.add(col1);
		return tmpLabel;
	}
	
	
	protected MaterialLink addIcon(MaterialWidget parent, IconType iconType, Float floatAlign) {
		MaterialLink icon = new MaterialLink();
		icon.setFloat(floatAlign);
		icon.setIconType(iconType);
		icon.setMargin(0);
		icon.setFontSize("26px");
		icon.setIconColor(Color.WHITE);
		parent.add(icon);
		return icon;
	}
	
	
	public void visibleOrderIcon(boolean upIcon, boolean downIcon) {
		orderUpIcon.setVisible(upIcon);
		orderDownIcon.setVisible(downIcon);
	}

}
