package kr.or.visitkorea.admin.client.manager.event.composite;

import java.util.Date;
import java.util.HashMap;

import com.google.gwt.dom.client.Style.Float;
import com.google.gwt.dom.client.Style.FontWeight;
import com.google.gwt.dom.client.Style.Overflow;
import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;

import gwt.material.design.addins.client.combobox.MaterialComboBox;
import gwt.material.design.client.base.MaterialWidget;
import gwt.material.design.client.constants.Color;
import gwt.material.design.client.constants.Display;
import gwt.material.design.client.constants.InputType;
import gwt.material.design.client.constants.TextAlign;
import gwt.material.design.client.ui.MaterialColumn;
import gwt.material.design.client.ui.MaterialDatePicker;
import gwt.material.design.client.ui.MaterialDatePicker.MaterialDatePickerType;
import gwt.material.design.client.ui.MaterialInput;
import gwt.material.design.client.ui.MaterialLabel;
import gwt.material.design.client.ui.MaterialLink;
import gwt.material.design.client.ui.MaterialListValueBox;
import gwt.material.design.client.ui.MaterialRow;
import gwt.material.design.client.ui.MaterialTextBox;
import gwt.material.design.jquery.client.api.Functions.Func3;
import gwt.material.design.jscore.client.api.JSON;
import gwt.material.design.jscore.client.api.JsObject;
import kr.or.visitkorea.admin.client.manager.event.EventStatus;
import kr.or.visitkorea.admin.client.manager.widgets.AbstractContentComposite;
import kr.or.visitkorea.admin.client.manager.widgets.SelectionPanel;
import kr.or.visitkorea.admin.client.widgets.window.MaterialExtentsWindow;

public abstract class AbstractEventContents extends AbstractContentComposite implements IEventContents {
	private String evtId;
	private String cotId;
	private MaterialExtentsWindow materialExtendsWindow;
	private MaterialLabel titleLabel;
	private JSONObject contentsObject;
	protected EventStatus eventStatus;
	private int Saveresult;
	public abstract MaterialWidget render();
	
	//	Constructor
	public AbstractEventContents(MaterialExtentsWindow materialExtendsWindow) {
		super();
		this.materialExtendsWindow = materialExtendsWindow;
	}

	public MaterialExtentsWindow getMaterialExtendsWindow() {
		return materialExtendsWindow;
	}

	public JSONObject getContentsObject() {
		return contentsObject;
	}

	public void setContentsObject(JSONObject contentsObject) {
		this.contentsObject = contentsObject;
	}
	
	@Override
	public void init() {
		this.setWidth("1082px");
		this.setHeight("666px");
		this.setLayoutPosition(Position.RELATIVE);
		this.setTextAlign(TextAlign.CENTER);
		this.setFloat(Float.LEFT);
		this.setOverflow(Overflow.HIDDEN);
		
		buildTitle();
		
		this.add(render());
	}

	public void buildTitle() {
		titleLabel = new MaterialLabel("");
		titleLabel.setBackgroundColor(Color.BLUE);
		titleLabel.setPaddingLeft(10);
		titleLabel.setPaddingTop(3);
		titleLabel.setTextAlign(TextAlign.LEFT);
		titleLabel.setFontWeight(FontWeight.BOLD);
		titleLabel.setTextColor(Color.WHITE);
		titleLabel.setFontSize("1.3em");

		this.add(titleLabel);
	}

	public void setTitle(String title) {
		this.titleLabel.setText(title);
	}
	
	protected String convertDateFormat(String strDate) {
		if (strDate.equals("") || strDate == null) 
			return "";
		
		Date date = DateTimeFormat.getFormat("yyyy-MM-dd HH:mm:ss.s").parse(strDate);
		return convertDateToString(date);
	}
	
	protected String convertDateToString(Date date) {
		return DateTimeFormat.getFormat("yyyy-MM-dd").format(date);
	}

	protected Date convertStringToDate(String date) {
		try {
			return DateTimeFormat.getFormat("yyyy-MM-dd").parse(date);
		} catch (Exception e) {

			return DateTimeFormat.getFormat("yyyy-MM-dd HH:mm:ss.s").parse(date);
		}
	}
	
	protected Func3<Object, String, Object> saveCallback() {
		return (param1, param2, param3) -> {
			JSONObject resultObj = JSONParser.parseStrict(JSON.stringify((JsObject) param1)).isObject();
			String process = resultObj.get("header").isObject().get("process").isString().stringValue();
			
			if (process.equals("success")) {
				this.getMaterialExtendsWindow().alert("성공적으로 저장되었습니다.");
				Saveresult = 1;
			} else {
				Saveresult = 0;
			}
			
		};
	}
	
	
	
	protected MaterialDatePicker addDatePicker(MaterialRow row, String grid) {
		MaterialDatePicker picker = new MaterialDatePicker();
		picker.setSelectionType(MaterialDatePickerType.YEAR_MONTH_DAY);
		picker.setAutoClose(false);  
		picker.setPlaceholder("날짜를 선턱하세요.");
		picker.setFormat("yyyy-MM-dd");
		picker.setMargin(0);
		picker.addClickHandler(e -> {
			picker.open();
    	});
		picker.addValueChangeHandler(e -> {
			picker.close();
    	});
		picker.getElement().getFirstChildElement().getStyle().setMargin(0, Unit.PX);
		
		MaterialColumn col = new MaterialColumn();
		col.setGrid(grid);
		col.add(picker);
		row.add(col);
		return picker;
	}
	
	protected MaterialInput addInputDate(MaterialRow row, String grid) {
		MaterialInput input = new MaterialInput(InputType.DATE);
		input.setMargin(0);
		
		MaterialColumn col = new MaterialColumn();
		col.setGrid(grid);
		col.add(input);
		row.add(col);
		return input;
	}
	
	protected MaterialComboBox<Object> addCombobox(MaterialRow row, String grid) {
		MaterialComboBox<Object> combo = new MaterialComboBox<Object>();
		combo.setMargin(0);
		combo.setHeight("46.25px");
		
		MaterialColumn col = new MaterialColumn();
		col.setGrid(grid);
		col.add(combo);
		row.add(col);
		return combo;
	}
	
	protected SelectionPanel addSelectionViewPanel() {
		HashMap<String, Object> valueMap = new HashMap<>();
		valueMap.put("자동", 1);
		valueMap.put("수동", 0);
		
		SelectionPanel panel = new SelectionPanel();
		panel.setFloat(Float.RIGHT);
		panel.setMarginRight(10);
		panel.setMarginTop(4);
		panel.setDisplay(Display.INLINE_BLOCK);
		panel.setFontSize("0.8em");
		panel.setValues(valueMap);
		return panel;
	}
	
	protected SelectionPanel addSelectionPanel(MaterialRow row, String grid, TextAlign align, HashMap<String, Object> valueMap) {
		return addSelectionPanel(row, grid, align, valueMap, 5, 5, 8, true);
	}
	
	protected SelectionPanel addSelectionPanel(MaterialRow row, String grid, TextAlign align, 
			HashMap<String, Object> valueMap, int margin, int padding, int radius, boolean isSingleSelection) {
		SelectionPanel box = new SelectionPanel();
		box.setElementMargin(margin);
		box.setElementPadding(padding);
		box.setElementRadius(radius);
		box.setTextAlign(align);
		box.setSingleSelection(isSingleSelection);
		box.setValues(valueMap);
		box.setMarginTop(0);
		box.setMarginBottom(0);
		box.setLineHeight(46.25);
		box.setHeight("46.25px");
		
		MaterialColumn col1 = new MaterialColumn();
		col1.setTextAlign(align);
		col1.setGrid(grid);
		col1.add(box);
		row.add(col1);
		return box;
	}
	
	protected SelectionPanel addSelectionPanel(MaterialRow row, String grid, TextAlign align, 
			HashMap<String, Object> valueMap, int margin, int padding, int radius, String fontSize, boolean isSingleSelection) {
		SelectionPanel box = new SelectionPanel();
		box.setElementMargin(margin);
		box.setElementPadding(padding);
		box.setElementRadius(radius);
		box.setTextAlign(align);
		box.setSingleSelection(isSingleSelection);
		box.setValues(valueMap);
		box.setFontSize(fontSize);
		box.setMarginTop(0);
		box.setMarginBottom(0);
		box.setLineHeight(46.25);
		box.setHeight("46.25px");
		
		MaterialColumn col1 = new MaterialColumn();
		col1.setTextAlign(align);
		col1.setGrid(grid);
		col1.add(box);
		row.add(col1);
		return box;
	}
	
	protected MaterialTextBox addInputText(MaterialRow row, String placeholder, String grid) {
		MaterialTextBox box = new MaterialTextBox();
		box.getElement().getElementsByTagName("input").getItem(0).getStyle().setProperty("margin", "0");
		box.setPlaceholder(placeholder);
		box.setMarginTop(0);
		box.setMarginBottom(0);
		
		MaterialColumn col1 = new MaterialColumn();
		col1.setGrid(grid);
		col1.add(box);
		row.add(col1);
		return box;
	}

	protected MaterialListValueBox<String> addList(MaterialRow row, String[] strings, String grid) {
		MaterialListValueBox<String> box = new MaterialListValueBox<String>();
		box.setMarginTop(0);
		box.setMarginBottom(0);
		
		MaterialColumn col1 = new MaterialColumn();
		col1.setGrid(grid);
		col1.add(box);
		row.add(col1);
		for (String str : strings) {
			box.addItem(str);
		}
		return box;
	}

	protected MaterialLabel addLabel(MaterialRow row, String defaultValue, TextAlign tAlign, Color bgColor, String grid) {
		MaterialLabel tmpLabel = new MaterialLabel(defaultValue);
		tmpLabel.setTextAlign(tAlign);
		tmpLabel.setLineHeight(46.25);
		tmpLabel.setHeight("46.25px");
		tmpLabel.setBackgroundColor(bgColor);
		
		MaterialColumn col1 = new MaterialColumn();
		col1.setGrid(grid);
		col1.add(tmpLabel);
		row.add(col1);
		return tmpLabel;
	}

	protected MaterialLink addLink(MaterialRow row, String defaultValue, TextAlign tAlign, Color bgColor, String grid) {
		MaterialLink tmpLabel = new MaterialLink(defaultValue);
		tmpLabel.setTextAlign(tAlign);
		tmpLabel.setLineHeight(46.25);
		tmpLabel.setHeight("46.25px");
		tmpLabel.setBackgroundColor(bgColor);
		
		MaterialColumn col1 = new MaterialColumn();
		col1.setGrid(grid);
		col1.add(tmpLabel);
		row.add(col1);
		return tmpLabel;
	}

	protected MaterialRow addRow(MaterialWidget parent) {
		MaterialRow row = new MaterialRow();
		parent.add(row);
		return row;
	}

	public String getEvtId() {
		return evtId;
	}

	public void setEvtId(String evtId) {
		this.evtId = evtId;
	}

	public String getCotId() {
		return cotId;
	}

	public void setCotId(String cotId) {
		this.cotId = cotId;
	}

	@Override
	public void buildOutLinkTypeContent() {
		
	}

	@Override
	public void buildDefaultTypeContent() {
		
	}

	public EventStatus getEventStatus() {
		return eventStatus;
	}
	

	public void setEventStatus(EventStatus eventStatus) {
		this.eventStatus = eventStatus;
	}

	public int getSaveresult() {
		return Saveresult;
	}
}
