package kr.or.visitkorea.admin.client.manager.event.widgets;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import com.google.gwt.dom.client.Style.Cursor;
import com.google.gwt.dom.client.Style.FontWeight;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONNumber;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.user.client.ui.Widget;

import gwt.material.design.client.base.MaterialWidget;
import gwt.material.design.client.constants.Color;
import gwt.material.design.client.constants.InputType;
import gwt.material.design.client.constants.TextAlign;
import gwt.material.design.client.ui.MaterialColumn;
import gwt.material.design.client.ui.MaterialDatePicker;
import gwt.material.design.client.ui.MaterialDatePicker.MaterialDatePickerType;
import gwt.material.design.jscore.client.api.JSON;
import gwt.material.design.jscore.client.api.JsObject;
import gwt.material.design.client.ui.MaterialInput;
import gwt.material.design.client.ui.MaterialLabel;
import gwt.material.design.client.ui.MaterialPanel;
import gwt.material.design.client.ui.MaterialRow;
import gwt.material.design.client.ui.MaterialTextBox;
import gwt.material.design.client.ui.MaterialToast;
import kr.or.visitkorea.admin.client.application.Console;
import kr.or.visitkorea.admin.client.manager.VisitKoreaBusiness;
import kr.or.visitkorea.admin.client.manager.widgets.SelectionPanel;

public class EventAnnouncePageSearchRow extends MaterialRow {
	private String eeuId;
	private String evtId;
	private String subEvtId;
	private String gftId; 
	private String eapId; 
	
	private MaterialLabel indexLabel;
	private MaterialTextBox tel;
	private MaterialTextBox name;
	private int order;
	private boolean isWinner;
	private EventAnnouncePagePanel parent;
	

	public EventAnnouncePageSearchRow(EventAnnouncePagePanel parent, boolean isWinner) {
		super();
		this.isWinner = isWinner;
		this.parent = parent;
		init();
	}

	
	public void init() {
		this.setMargin(0);
		
		MaterialColumn col1 = new MaterialColumn();
		col1.setGrid("s1");
		if(isWinner) {
			indexLabel = new MaterialLabel("-");
		}else {
			indexLabel = new MaterialLabel("+");
		}
		indexLabel.setBackgroundColor(Color.BLUE);
		indexLabel.setLineHeight(46.5);
		indexLabel.setFontSize("20px");
		indexLabel.setTextColor(Color.WHITE);
		indexLabel.getElement().getStyle().setCursor( Cursor.POINTER );
		indexLabel.addClickHandler(event -> {
			if("+".equals(this.indexLabel.getText())){
				List<Widget> list = parent.getWinnerPanel().getChildrenList();
				for(Widget widget : list) {
					if(getTel().equals(((EventAnnouncePageSearchRow)widget).getTel())) {
						MaterialToast.fireToast("중복된 휴대폰 번호가 존재합니다", 1000);
						return;
					}
				}
				insert();
			}else{
				delete();
			}
		});
		
		col1.add(indexLabel);
		this.add(col1);
		
		MaterialColumn col2 = new MaterialColumn();
		col2.setGrid("s11");
		col2.setPadding(0);
		this.add(col2);
		
		MaterialRow row = null;
		
		row = addRow(col2);
		addLabel(row, "이름", Color.GREY_LIGHTEN_3, "s2");
		name = addInputBox(row, "이름", "s3");
		addLabel(row, "휴대폰번호", Color.GREY_LIGHTEN_3, "s2");
		tel = addInputBox(row, "휴대폰번호", "s3");

	}
	
	
	/**당첨자 단건 저장*/
	public void insert() {
		JSONObject paramJson = new JSONObject();
		paramJson.put("cmd", new JSONString("SAVE_EVENT_USER_WIN"));
		paramJson.put("eeuId", new JSONString(getEeuId()));
		paramJson.put("evtId", new JSONString(getEvtId()));
		paramJson.put("subEvtId", new JSONString(getSubEvtId()));
		paramJson.put("gftId", new JSONString(getGftId()));
		paramJson.put("eapId", new JSONString(getEapId()));
		paramJson.put("mode", new JSONString("insert"));
		
		VisitKoreaBusiness.post("call", paramJson.toString(), (param1, param2, param3) -> {
			JSONObject resultObj = JSONParser.parseStrict(JSON.stringify((JsObject) param1)).isObject();
			String process = resultObj.get("header").isObject().get("process").isString().stringValue();
			
			if (process.equals("success")) {
				this.indexLabel.setText("-");
				parent.getWinnerPanel().add(this);
			}
		});
	}
	
	/**당첨자 단건 삭제*/
	public void delete() {
		JSONObject paramJson = new JSONObject();
		paramJson.put("cmd", new JSONString("SAVE_EVENT_USER_WIN"));
		paramJson.put("eeuId", new JSONString(getEeuId()));
		paramJson.put("evtId", new JSONString(getEvtId()));
		paramJson.put("subEvtId", new JSONString(getSubEvtId()));
		paramJson.put("gftId", new JSONString(getGftId()));
		paramJson.put("eapId", new JSONString(getEapId()));
		paramJson.put("mode", new JSONString("delete"));
		
		VisitKoreaBusiness.post("call", paramJson.toString(), (param1, param2, param3) -> {
			JSONObject resultObj = JSONParser.parseStrict(JSON.stringify((JsObject) param1)).isObject();
			String process = resultObj.get("header").isObject().get("process").isString().stringValue();
			
			if (process.equals("success")) {
				parent.getWinnerPanel().remove(this);
			}
		});
	}
	

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

	public String getIndex() {
		return this.indexLabel.getText();
	}

	public void setIndex(String index) {
		this.indexLabel.setText(index);
	}

	public String getTitle() {
		return this.name.getValue();
	}

	public void setName(String name) {
		this.name.setValue(name);
	}
	
	public String getName() {
		return this.name.getValue();
	}
	
	public String getTel() {
		return this.tel.getValue();
	}

	public void setTel(String tel) {
		this.tel.setValue(tel);
	}
	
	public void setEapId(String eapId) {
		this.eapId = eapId ;
	}
	
	public String getEapId() {
		return eapId ;
	}
	
	public String getEeuId() {
		return eeuId;
	}

	public void setEeuId(String eeuId) {
		this.eeuId = eeuId;
	}

	public int getOrder() {
		return order;
	}

	public void setOrder(int order) {
		this.order = order;
	}

	
	private MaterialTextBox addInputBox(MaterialRow row, String placeholder, String grid) {
		MaterialTextBox tBox = new MaterialTextBox();
		tBox.setPlaceholder(placeholder);
		tBox.setMargin(0);
		tBox.setHeight("46.25px");
		tBox.setReadOnly(true);
		
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
}
