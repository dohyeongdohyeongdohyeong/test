package kr.or.visitkorea.admin.client.manager.contents.database.composite;

import java.util.HashMap;
import java.util.Map;

import com.google.gwt.dom.client.Style.Float;
import com.google.gwt.dom.client.Style.FontWeight;
import com.google.gwt.dom.client.Style.Overflow;
import com.google.gwt.json.client.JSONNumber;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONString;

import gwt.material.design.client.MaterialDesignBase;
import gwt.material.design.client.constants.Color;
import gwt.material.design.client.constants.Display;
import gwt.material.design.client.constants.IconType;
import gwt.material.design.client.constants.InputType;
import gwt.material.design.client.constants.TextAlign;
import gwt.material.design.client.ui.MaterialButton;
import gwt.material.design.client.ui.MaterialColumn;
import gwt.material.design.client.ui.MaterialIcon;
import gwt.material.design.client.ui.MaterialInput;
import gwt.material.design.client.ui.MaterialLabel;
import gwt.material.design.client.ui.MaterialListValueBox;
import gwt.material.design.client.ui.MaterialPanel;
import gwt.material.design.client.ui.MaterialRow;
import gwt.material.design.client.ui.MaterialTextBox;
import gwt.material.design.jquery.client.api.Functions.Func3;
import kr.or.visitkorea.admin.client.application.Registry;
import kr.or.visitkorea.admin.client.manager.VisitKoreaBusiness;
import kr.or.visitkorea.admin.client.manager.contents.recommand.RecommContentBundle;
import kr.or.visitkorea.admin.client.manager.widgets.AbstractContentComposite;
import kr.or.visitkorea.admin.client.manager.widgets.SelectionPanel;
import kr.or.visitkorea.admin.client.manager.widgets.StatusChangeEvent;
import kr.or.visitkorea.admin.client.widgets.window.MaterialExtentsWindow;

abstract public class AbtractContents extends AbstractContentComposite {

	static {
		MaterialDesignBase.injectCss(RecommContentBundle.INSTANCE.contentCss());
	}

	private MaterialExtentsWindow window;

	public AbtractContents(MaterialExtentsWindow materialExtentsWindow) {
		super();
		this.window = materialExtentsWindow;
	}
	
	public MaterialExtentsWindow getWindow() {
		return this.window;
	}

	private MaterialLabel titleLabel;
	private MaterialIcon saveIcon;
	private MaterialIcon addIcon;
	private MaterialIcon editIcon;
	
	@Override
	public void init() {
		this.setTextAlign(TextAlign.CENTER);
		this.setFloat(com.google.gwt.dom.client.Style.Float.LEFT);
		this.setWidth("1083px");
		this.setHeight("100%");
		this.setOverflow(Overflow.HIDDEN);
		
		buildTitle();
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

		addIcon = new MaterialIcon(IconType.ADD);
		addIcon.setPadding(4);
		addIcon.setFloat(Float.RIGHT);
		addIcon.setIconColor(Color.WHITE);
		addIcon.setDisplay(Display.NONE);
		addIcon.setTooltip("팜플렛 영역 추가");
		addIcon.setMargin(0);
		
		saveIcon = new MaterialIcon(IconType.SAVE);
		saveIcon.setPadding(4);
		saveIcon.setFloat(Float.RIGHT);
		saveIcon.setIconColor(Color.WHITE);
		saveIcon.setDisplay(Display.NONE);
		saveIcon.setTooltip("컨텐츠 저장");
		saveIcon.setMargin(0);
		
		editIcon = new MaterialIcon(IconType.EDIT);
		editIcon.setPadding(4);
		editIcon.setFloat(Float.RIGHT);
		editIcon.setIconColor(Color.WHITE);
		editIcon.setDisplay(Display.NONE);
		editIcon.setTooltip("컨텐츠 수정");
		editIcon.setMargin(0);
		

		this.add(editIcon);
		this.add(saveIcon);
		this.add(addIcon);
		this.add(titleLabel);
	}

	public MaterialIcon showAddIconAndGetIcon() {
		addIcon.setDisplay(Display.BLOCK);
		return addIcon;
	}
	
	public MaterialIcon showSaveIconAndGetIcon() {
		saveIcon.setDisplay(Display.BLOCK);
		return saveIcon;
	}
	
	public MaterialIcon showEditIconAndGetIcon() {
		editIcon.setDisplay(Display.BLOCK);
		return editIcon;
	}
	
	
	public void setTitle(String title) {
		this.titleLabel.setText(title);
	}
	
	@Override
	public void buildOutLinkTypeContent() {
		
	}

	@Override
	public void buildDefaultTypeContent() {
		
	}
	
	private String cotid;
	private String otdid;
	
	public String getCotId() {
		return cotid;
	}
	
	public String getOtdId() {
		if(otdid != null) return otdid;
		return "";
	}
	public void setCotId(String cotid) {
		this.cotid = cotid;
	}
	
	public void setOtdId(String otdId) {
		this.otdid = otdId;
	}
	
	
	public abstract void setReadOnly(boolean readFlag);
	
	protected String getBigKey(String bigAreaText) {
		Map<String, String> map = (Map<String, String>) Registry.get("ADDRESS_BIG_CODE");
		for (String key : map.keySet()) {
			if (bigAreaText.equals(map.get(key))) {
				return key;
			}
		}
		return null;
	}
	
	protected SelectionPanel addSelectionPanel(MaterialRow row, String grid, TextAlign align, HashMap<String, Object> valueMap, String tbl, String column) {
		return addSelectionPanel(row, grid, align, valueMap, 5, 5, 8, true, tbl, column);
	}
	
	protected SelectionPanel addSelectionPanel(MaterialRow row, String grid, TextAlign align, 
			HashMap<String, Object> valueMap, int margin, int padding, int radius, boolean isSingleSelection, String tbl, String column) {
		
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
		box.addStatusChangeEvent(new StatusChangeEvent() {
			
			@Override
			public void fire(Object selecteBaseLink) {

				if(tbl != null) {
					
					Integer selectedInt = (Integer)box.getSelectedValue();
					
					JSONObject paramObj = new JSONObject();
					paramObj.put("tbl", new JSONString(tbl));
					paramObj.put("colTitle", new JSONString(column));
					paramObj.put("cotId", new JSONString(getCotId()));
					paramObj.put("value", new JSONNumber(selectedInt));
					
					Func3<Object, String, Object> callback =  new Func3<Object, String, Object>() {
	
						@Override
						public void call(Object param1, String param2, Object param3) {}
						
					};
					
					invokeQuery("UPDATE_SINGLE_ROW", paramObj, callback);
				
				}
				
			}
		});
		
		
		MaterialColumn col1 = new MaterialColumn();
		col1.setTextAlign(align);
		col1.setGrid(grid);
		col1.add(box);
		row.add(col1);
		return box;
	}
	
	protected MaterialTextBox addInputText(MaterialRow row, String string, String grid, String tbl, String column) {
		MaterialTextBox box = new MaterialTextBox();
		box.getElement().getElementsByTagName("input").getItem(0).getStyle().setProperty("margin", "0");
		box.setText(string);
		box.setMarginTop(0);
		box.setMarginBottom(0);
		if(tbl != null) {
			box.addKeyUpHandler(event->{
				if (event.getNativeKeyCode() == 13) {
					
					JSONObject paramObj = new JSONObject();
					paramObj.put("tbl", new JSONString(tbl));
					paramObj.put("value", new JSONString(box.getValue()));
					paramObj.put("colTitle", new JSONString(column));
					paramObj.put("cotId", new JSONString(getCotId()));
					
					Func3<Object, String, Object> callback =  new Func3<Object, String, Object>() {

						@Override
						public void call(Object param1, String param2, Object param3) {}
						
					};
					
					invokeQuery("UPDATE_SINGLE_ROW", paramObj, callback);
					
				}
			});
		
			box.addBlurHandler(event->{
				
				JSONObject paramObj = new JSONObject();
				paramObj.put("tbl", new JSONString(tbl));
				paramObj.put("value", new JSONString(box.getValue()));
				paramObj.put("colTitle", new JSONString(column));
				paramObj.put("cotId", new JSONString(getCotId()));
				
				Func3<Object, String, Object> callback =  new Func3<Object, String, Object>() {
	
					@Override
					public void call(Object param1, String param2, Object param3) {}
					
				};
				
				invokeQuery("UPDATE_SINGLE_ROW", paramObj, callback);
				
			});
		}
		
		MaterialColumn col1 = new MaterialColumn();
		col1.setGrid(grid);
		col1.add(box);
		row.add(col1);
		return box;
	}

	protected MaterialListValueBox<String> addList(MaterialRow row, String[] strings, String grid, String tbl, String column) {
		MaterialListValueBox<String> box = new MaterialListValueBox<String>();
		box.setMarginTop(0);
		box.setMarginBottom(0);
		
		for (String str : strings) {
			box.addItem(str);
		}
	
		MaterialColumn col1 = new MaterialColumn();
		col1.setGrid(grid);
		col1.add(box);
		row.add(col1);
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
	
	protected MaterialButton addButton(MaterialRow row, String defaultValue, TextAlign tAlign, Color bgColor, String grid,int padding) {
		
		MaterialButton button = new MaterialButton(defaultValue);
		button.setTextAlign(tAlign);
		button.setLineHeight(46.25);
		button.setHeight("46.25px");
		button.setBackgroundColor(bgColor);
		button.setBorderRadius("8px");
		MaterialColumn col1 = new MaterialColumn();
		if(padding>= 0)
			col1.setPadding(padding);
		col1.setGrid(grid);
		col1.add(button);
		row.add(col1);
		return button;
	}

	
	protected MaterialIcon addIcon(MaterialRow row, IconType iconType, String grid) {
		MaterialIcon icon = new MaterialIcon(iconType);
		icon.setGrid(grid);
		icon.setFontSize("30px");
		icon.setTextAlign(TextAlign.LEFT);
		icon.setPadding(9);
		
		row.add(icon);
		return icon;
	}
	
	protected MaterialInput addDatePicker(MaterialRow row, TextAlign tAlign, Color bgColor, String grid, String tbl, String column) {
	
		MaterialInput input =  new MaterialInput(InputType.DATE);
		input.getElement().getStyle().setProperty("marginBottom", "0px");
		input.addBlurHandler(event->{
			
			if(tbl != null) {
				
				JSONObject paramObj = new JSONObject();
				paramObj.put("tbl", new JSONString(tbl));
				paramObj.put("value", new JSONString(input.getValue().replaceAll("-", "")));
				paramObj.put("colTitle", new JSONString(column));
				paramObj.put("cotId", new JSONString(getCotId()));
				
				Func3<Object, String, Object> callback =  new Func3<Object, String, Object>() {
	
					@Override
					public void call(Object param1, String param2, Object param3) {}
					
				};
				
				invokeQuery("UPDATE_SINGLE_ROW", paramObj, callback);
			}
			
		});
		
		MaterialColumn col1 = new MaterialColumn();
		col1.setGrid(grid);
		col1.setTextAlign(tAlign);
		col1.add(input);
		row.add(col1);
		return input;
	}

	protected MaterialRow addRow(MaterialPanel panel) {
		MaterialRow row = new MaterialRow();
		panel.add(row);
		return row;
	}

	@Override
	public void loading(boolean loadFlag) {
		super.loading(loadFlag);
	}

	protected void invokeQuery(String cmd, JSONObject parameterJSON, Func3 callback) {
		
		parameterJSON.put("cmd", new JSONString(cmd));

		VisitKoreaBusiness.post("call", parameterJSON.toString(), callback);
	}
	

	protected MaterialTextBox addInputTextNotEvent(MaterialRow row, String placeholder, String grid) {
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
	
	protected SelectionPanel addSelectionPanelNotEvent(MaterialRow row, String grid, TextAlign align, 
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
}
