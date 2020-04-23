package kr.or.visitkorea.admin.client.manager.widgets;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;

import gwt.material.design.client.base.MaterialWidget;
import gwt.material.design.client.constants.CheckBoxType;
import gwt.material.design.client.constants.Color;
import gwt.material.design.client.constants.Display;
import gwt.material.design.client.constants.TextAlign;
import gwt.material.design.client.ui.MaterialCheckBox;
import gwt.material.design.client.ui.MaterialCollection;
import gwt.material.design.client.ui.MaterialCollectionItem;
import gwt.material.design.client.ui.MaterialLabel;
import gwt.material.design.client.ui.MaterialPanel;
import gwt.material.design.client.ui.MaterialTextBox;

public class ContentTableRow extends MaterialPanel {

	private Color setupColor;
	private Object[] values;
	private List<Integer> widthList;
	private List<TextAlign> alignList;
	private Map<String, Object> objectCollection = new HashMap<String, Object>();
	private boolean checked = false;
	private int clickCol = -1;
	private int[] link = null;
	public ContentTableRow() {
		super();
		init();
	}

	public ContentTableRow(String... initialClass) {
		super(initialClass);
		init();
	}
	public ContentTableRow(List<Integer> widthList, List<TextAlign> alignList,Color bgColor, Object ... values) {
		this(widthList, alignList, bgColor, null, values);
	}
	public ContentTableRow(List<Integer> widthList, List<TextAlign> alignList,Color bgColor, int[] link, Object ... values) {
		super();
		this.getElement().getStyle().setCursor(com.google.gwt.dom.client.Style.Cursor.POINTER);
		this.setupColor = bgColor;
		this.setBackgroundColor(bgColor);
		this.values = values;
		this.widthList = widthList;
		this.alignList = alignList;
		this.link = link;
		init();
	}
	public ContentTableRow(List<Integer> widthList, List<TextAlign> alignList,Color bgColor, boolean checked, Object ... values) {
		this(widthList, alignList, bgColor,checked, null, values);
	}
	public ContentTableRow(List<Integer> widthList, List<TextAlign> alignList,Color bgColor, boolean checked, int[] link, Object ... values) {
		super();
		this.getElement().getStyle().setCursor(com.google.gwt.dom.client.Style.Cursor.POINTER);
		this.setupColor = bgColor;
		this.setBackgroundColor(bgColor);
		this.values = values;
		this.widthList = widthList;
		this.alignList = alignList;
		this.checked = checked;
		this.link = link;
		init();
	}
	
    private void init() {
		this.setLayoutPosition(Position.RELATIVE);
		this.setHeight("40px");
		loadData();
	}
    
    public boolean isChecked() {
    	return this.checked;
    }
    
    public void setTotalWidth(int totalWidth) {
    	this.setWidth(totalWidth+"px");
    }

    public void restoreBackgroundColor() {
    	this.setBackgroundColor(this.setupColor);
    	this.setTextColor(Color.BLACK);
    }
    
	@Override
    protected void onLoad() {
        super.onLoad();
    }

	public int getSelectedColumn() {
		return clickCol;
	}
	
	private boolean blink = false;
	private List<MaterialWidget> headerList = new ArrayList<MaterialWidget>();
	
	public void loadData() {
		for (int i=0; i<values.length; i++) {
			final int ii = i;
			ClickHandler handler = new ClickHandler() {
				@Override
				public void onClick(ClickEvent event) {
					clickCol = ii;
				}
			};
			if(link != null) {
				for(int ln:link) {
					if(ln==i) {
						blink = true;
						break;
					} else blink = false;
				}
			}
			
			if(values[i] instanceof Integer) {
				addLabelObject(this, this.alignList.get(i),  ""+values[i], widthList.get(i) + "px", handler);
			} else {
				
				addLabelObject(this, this.alignList.get(i),  ""+values[i], widthList.get(i) + "px", handler);
			}
		}
	}
	private void addLabelObject(MaterialPanel rows, TextAlign align, String title, String width, ClickHandler handler) {
		
		if(title.equals("")) title = "-";
		MaterialLabel header1 = new MaterialLabel(title);
		header1.setDisplay(Display.INLINE_BLOCK);
		if(blink) {
			header1.setStyle("overflow-x: hidden;text-overflow: ellipsis;white-space: nowrap;text-decoration: underline;"); // 
		} else
			header1.setStyle("overflow-x: hidden;text-overflow: ellipsis;white-space: nowrap;"); // 
		
		header1.setPaddingLeft(5);
		header1.setPaddingRight(5);
		header1.setLineHeight(40);
		header1.setTextAlign(align);
		header1.setWidth(width);
		header1.setFloat(com.google.gwt.dom.client.Style.Float.LEFT);
		header1.addClickHandler(handler);
		rows.add(header1);
		
		headerList.add(header1);
		
		if (checked) {
			MaterialCheckBox chk = new MaterialCheckBox();
			chk.setType(CheckBoxType.FILLED);
			MaterialPanel chkPanel = new MaterialPanel();
			chkPanel.setLayoutPosition(Position.ABSOLUTE);
			chkPanel.setTop(10);
			chkPanel.setRight(0);
			chkPanel.add(chk);
			rows.add(chkPanel);
		}
	}
	private MaterialTextBox buildTextInputForm(MaterialCollection collection, String inputTitle) {
		MaterialCollectionItem item = new MaterialCollectionItem();
		MaterialTextBox component = new MaterialTextBox();
		component.setPlaceholder("값을 입력해 주세요.");
		component.setLabel(inputTitle);
		component.setActive(true);
		item.add(component);
		collection.add(item);
		return component;
	}

	private void addCheckedObject(MaterialPanel rows, TextAlign align, String title, String width) {
		
		MaterialCheckBox chk = new MaterialCheckBox();
		chk.setType(CheckBoxType.FILLED);
		
		MaterialPanel chkPanel = new MaterialPanel();
		chkPanel.add(chk);
		
		chkPanel.setStyle("overflow-x: hidden; text-overflow: ellipsis; white-space: nowrap;");
		chkPanel.setDisplay(Display.INLINE_BLOCK);
		chkPanel.setPaddingLeft(5);
		chkPanel.setPaddingRight(5);
		chkPanel.setLineHeight(40);
		chkPanel.setTextAlign(align);
		chkPanel.setWidth(width);
		chkPanel.setFloat(com.google.gwt.dom.client.Style.Float.LEFT);
		rows.add(chkPanel);
	}

	public int size() {
		return objectCollection.size();
	}

	public boolean isEmpty() {
		return objectCollection.isEmpty();
	}

	public Object get(Object key) {
		return objectCollection.get(key);
	}

	public Object put(String key, Object value) {
		return objectCollection.put(key, value);
	}

	public Object remove(Object key) {
		return objectCollection.remove(key);
	}

	public void clear() {
		objectCollection.clear();
	}

	public Set<String> keySet() {
		return objectCollection.keySet();
	}
	
	public MaterialWidget getColumnObject(int index) {
		return headerList.get(index);
	}

	public Collection<Object> values() {
		return objectCollection.values();
	}
//	public void setupColor(Color col) {
//		setupColor = col;
//		this.setBackgroundColor(setupColor);
//	}
}
