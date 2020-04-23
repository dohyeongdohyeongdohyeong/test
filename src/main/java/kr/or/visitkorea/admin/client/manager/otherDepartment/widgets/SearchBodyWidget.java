package kr.or.visitkorea.admin.client.manager.otherDepartment.widgets;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gwt.dom.client.Style.Cursor;
import com.google.gwt.dom.client.Style.Float;
import com.google.gwt.dom.client.Style.FontWeight;
import com.google.gwt.dom.client.Style.Overflow;
import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.user.client.ui.Widget;

import gwt.material.design.client.constants.Color;
import gwt.material.design.client.constants.Display;
import gwt.material.design.client.constants.TextAlign;
import gwt.material.design.client.ui.MaterialLink;
import gwt.material.design.client.ui.MaterialPanel;
import gwt.material.design.client.ui.MaterialToast;
import kr.or.visitkorea.admin.client.application.Console;
import kr.or.visitkorea.admin.client.manager.otherDepartment.dialogs.SeasonContentDialog;
import kr.or.visitkorea.admin.client.widgets.dialog.DialogContent;
import kr.or.visitkorea.admin.client.widgets.window.MaterialExtentsWindow;

public class SearchBodyWidget  extends MaterialPanel implements VisitKoreaListBody{

	private VisitKoreaSearch search;
	private MaterialPanel menuArea;
	private MaterialPanel listArea;
	private MaterialPanel selectedPanel;
	private MaterialPanel listBody;
	private String[] headerTitles;
	private int totalWidth;
	private String[] widths;
	private boolean headerTitleVisible = false;
	private MaterialPanel headerArea;
	private Map<String, Object> valueMap = new HashMap<String, Object>();
	private DialogContent dialog;
	private MaterialExtentsWindow materialExtentsWindow;
	
	public SearchBodyWidget() {
		super();
		init();
	}
	
	public SearchBodyWidget(DialogContent dialog) {
		super();
		this.dialog = dialog;
		init();
	}


	private void init() {
		
		this.setLayoutPosition(Position.ABSOLUTE);
		this.setPadding(20);
		this.setBorder("1px solid #c8c8c8");
		
		this.menuArea = new MaterialPanel();
		this.menuArea.setHeight("24px");
		this.menuArea.setLayoutPosition(Position.ABSOLUTE);
		this.menuArea.setLeft(0);
		this.menuArea.setRight(0);
		this.menuArea.setBottom(0);
		this.menuArea.setBorderTop("1px solid #c8c8c8");
		
		this.headerArea = new MaterialPanel();
		this.headerArea.setHeight("50px");
		//this.headerArea.getElement().getStyle().setProperty("width", "calc( 100% - 14px )");
		this.headerArea.setWidth("100%");
		this.headerArea.setLayoutPosition(Position.ABSOLUTE);
		this.headerArea.setLeft(0);
		this.headerArea.setTop(0);
		this.headerArea.setVisible(headerTitleVisible);
		this.headerArea.setBorderBottom("1px solid #c8c8c8");
		

		this.listBody = new MaterialPanel();
		this.listBody.getElement().setAttribute("data", "listBody");
		this.listBody.setLayoutPosition(Position.RELATIVE);
		this.listBody.setLeft(0);
		this.listBody.setRight(0);
		this.listBody.setMargin(0);
		this.listBody.setPadding(0);
		
		
		this.listArea = new MaterialPanel();
		this.listArea.getElement().setAttribute("data", "listArea");
		this.listArea.setLayoutPosition(Position.ABSOLUTE);
		this.listArea.setPadding(0);
		this.listArea.setLeft(0);
		this.listArea.setRight(0);
		this.listArea.setTop(50);
		this.listArea.setBottom(24);
		this.listArea.getElement().getStyle().setOverflowX(Overflow.HIDDEN);
		this.listArea.getElement().getStyle().setOverflowY(Overflow.AUTO);
		this.listArea.add(this.listBody);
		
		this.add(headerArea);
		this.add(listArea);
		this.add(menuArea);
		
	}

	public MaterialPanel getListBody(){
		return this.listBody;	
	}
	
	@Override
	public void setSearch(VisitKoreaSearch search) {
		this.search = search;
	}

	
	public void addLink(MaterialLink link, Float floatStyle) {
		
		link.setDisplay(Display.INLINE_BLOCK);
		link.setFloat(floatStyle);
		link.getElement().getFirstChildElement().getStyle().setLineHeight(24, Unit.PX);
		link.getElement().getFirstChildElement().getStyle().setMarginRight(0, Unit.PX);
		
		if (floatStyle.equals(Float.LEFT)) {
			link.setBorderRight("1px solid #c8c8c8");
		}else{
			link.setBorderLeft("1px solid #c8c8c8");
		}
		
		this.menuArea.add(link);
		
	}

	public MaterialPanel getMenuArea() {
		return menuArea;
	}

	public void addRowAll(List<TagListRow> listRows) {
		
		for (TagListRow row : listRows) {
			getListBody().add(appendEvent(row));
		}
		
	}
	
	public MaterialPanel getSelectedPanel() {
		return this.selectedPanel;
	}
	
	public void removeSelectedPanel() {
		
		MaterialPanel tgrPanel = getSelectedPanel();
	
		if (tgrPanel != null){
			tgrPanel.removeFromParent();
			this.selectedPanel = null;
		}
	}

	public void selectedPanelMoveUp() {
		
		MaterialPanel tgrPanel = getSelectedPanel();
		
		if (tgrPanel != null){
			
			int widgetIndex = getListBody().getWidgetIndex(tgrPanel);
			
			if (widgetIndex > 0) {
				
				MaterialPanel prevPanel = (MaterialPanel) getListBody().getWidget(widgetIndex-1);
				prevPanel.removeFromParent();
				
				getListBody().insert(prevPanel, widgetIndex);
			
			}
		}
		
	}

	public void selectedPanelMoveDown() {
		MaterialPanel tgrPanel = getSelectedPanel();
		
		if (tgrPanel != null){
			int widgetIndex = getListBody().getWidgetIndex(tgrPanel);
			if (widgetIndex + 1 < getListBody().getWidgetCount()) {
				
				MaterialPanel nextPanel = (MaterialPanel) getListBody().getWidget(widgetIndex+1);
				nextPanel.removeFromParent();
				
				getListBody().insert(nextPanel, widgetIndex);

			}
		}
		
	}

	public void addRow(SeasonContentDialog targetDialog, TagListRow row) {
		if (row instanceof TagListRow) {
			String tagName = ((TagListRow)row).getUniqueName();
			if (tagName != null && !tagName.equals("") && notExistTagName(tagName)) {
				getListBody().insert(appendEvent((TagListRow)row), 0);
			}else {
				targetDialog.alert(					
						"안내", 600, 250,
						new String[] {
								"자료가 존재 합니다.",
								"이미 선택된 자료 입니다. ", 
								"다른 데이터를 선택하거나 기존 자료를 삭제 후 선택해 주십시오."
						});

			}
		}
		
	}

	@Override
	public void addRow(TagListRow row){
		if (row instanceof TagListRow) {
			String tagName = ((TagListRow)row).getUniqueName();
			if (tagName != null && !tagName.equals("") && notExistTagName(tagName)) {
				getListBody().insert(appendEvent((TagListRow)row), 0);
			}else {
				if (this.materialExtentsWindow == null) {
					MaterialToast.fireToast("자료가 이미 존재 합니다.");
				}else {
					this.materialExtentsWindow.alert("주의~!", "자료가 이미 존재 합니다.", 600);
				}
			}
		}
	}

	@Override
	public void addRow(TagListRow row, Map<String, Object> resultMap) {
		if (row instanceof TagListRow) {
			String tagName = ((TagListRow)row).getUniqueName();
			if (tagName != null && !tagName.equals("") && notExistTagName(tagName)) {
				getListBody().insert(appendEvent((TagListRow)row), 0);
				
				JSONObject resultObj = (JSONObject)resultMap.get("RESULT_OBJECT");
				JSONArray tempArr = (JSONArray)resultMap.get("NEW_OBJECT");
				JSONArray seasonTagsArr = resultObj.get("seasonTagsArr").isArray();
				
				for (int i=0; i<seasonTagsArr.size(); i++) {
					
					tempArr.set(i+1, seasonTagsArr.get(i));
					
				}
				
				resultObj.put("seasonTagsArr", tempArr);

				
			}else {
				if (this.materialExtentsWindow == null) {
					MaterialToast.fireToast("자료가 이미 존재 합니다.");
				}else {
					this.materialExtentsWindow.alert("주의~!", "자료가 이미 존재 합니다.", 600);
				}
			}
		}
	}
	
	public void addRowForLastIndex(TagListRow row){
		if (row instanceof TagListRow) {
			String tagName = ((TagListRow)row).getUniqueName();
			if (tagName != null && !tagName.equals("") && notExistTagName(tagName)) {
				getListBody().add(appendEvent((TagListRow)row));
			}
		}
	}

	public List<Widget> getChildrenList() {
		return getListBody().getChildrenList();
	}
	
	private boolean notExistTagName(String tagName) {
		
		boolean retBoolean = true;
		
		for (Widget widget : getListBody().getChildrenList()) {
			TagListRow tagListRow = (TagListRow)widget;
			if (tagName.equals(tagListRow.getUniqueName())){
				retBoolean = false;
				break;
			}
		}
		
		return retBoolean;
	}

	private TagListRow appendEvent(TagListRow row) {
		
		row.addClickHandler(event->{
			
			if (this.selectedPanel != null) {
				this.selectedPanel.setTextColor(Color.BLACK);
				this.selectedPanel.setBackgroundColor(Color.TRANSPARENT);
			}
			
			this.selectedPanel = row;
			row.getElement().getStyle().setCursor(Cursor.POINTER);
			row.setTextColor(Color.WHITE);
			row.setBackgroundColor(Color.BLUE);
			
		});

		row.addMouseOverHandler(event->{
			
			if (!row.equals(this.selectedPanel)) {
				row.getElement().getStyle().setCursor(Cursor.POINTER);
				row.setTextColor(Color.WHITE);
				row.setBackgroundColor(Color.BLUE_LIGHTEN_3);
			}
			
		});
		
		row.addMouseOutHandler(event->{
			
			if (!row.equals(this.selectedPanel)) {
				row.setTextColor(Color.BLACK);
				row.setBackgroundColor(Color.TRANSPARENT);
			}
			
		});
		
		return row;
	}

	public void setHeaderTitle(String[] headerTitles) {
		this.headerTitles = headerTitles;
	}

	public void setHeaderTitleVisible(boolean visible) {
		this.headerTitleVisible = visible;
		this.headerArea.setVisible(visible);
		if (this.headerArea.isVisible()) {
			this.listArea.setTop(50);
			this.listArea.setMarginTop(0);
			this.headerArea.setHeight("50px");
		}else {
			this.listArea.setTop(0);
			this.listArea.setMarginTop(0);
			this.headerArea.setHeight("0px");
		}
	}
	
	public boolean isHeaderTitleVisible() {
		return this.headerTitleVisible;
	}
	
	public void render() {
		renderTitle();
	}
	
	private void renderTitle() {
		
		if (isHeaderTitleVisible()) {
			totalWidth = 0;
			List<VisitKoreaListCell> rowInfoList = new ArrayList<VisitKoreaListCell>();
			for (int i=0; i<this.headerTitles.length; i++) {
				
				String targetTitle = this.headerTitles[i];
				String targetWidth = this.widths[i];
				totalWidth += Integer.parseInt(targetWidth.replace("px", ""));
				TagListLabelCell cell = new TagListLabelCell(targetTitle, Float.LEFT, targetWidth,  "50", 50, FontWeight.BOLDER, true, TextAlign.CENTER);
				if (i == this.headerTitles.length-1) {
					cell = new TagListLabelCell(targetTitle, Float.LEFT, targetWidth,  "50", 50, FontWeight.BOLDER, false, TextAlign.CENTER);
				}
				cell.setFontSize("1.2em");
				cell.setTextColor(Color.BROWN_LIGHTEN_1);
				rowInfoList.add(cell);
				
			}

			this.headerArea.add(new TagListRow(rowInfoList));
			this.listBody.setWidth(totalWidth+"px");
		}
	}

	public void removeAll() {
		this.listBody.clear();
	}

	public MaterialPanel getSelectedRow() {
		return selectedPanel;
	}

	@Override
	public void setEnabled(boolean enabled) {
		super.setEnabled(enabled);
		
	}

	public void setHeaderTitleWidths(String[] titleWidths) {
		this.widths = titleWidths;
	}

	@Override
	public List<Widget> getRows() {
		return listBody.getChildrenList();
	}
	
	public void setMapValue(String key, Object value) {
		this.valueMap.put(key, value);
	}
	
	public Object getMapValue(String key) {
		return this.valueMap.get(key);
	}
	
	public Map<String, Object> getValueMap(){
		return this.valueMap;
	}

	public void setWindow(MaterialExtentsWindow window) {
		this.materialExtentsWindow = window; 
	}

}
