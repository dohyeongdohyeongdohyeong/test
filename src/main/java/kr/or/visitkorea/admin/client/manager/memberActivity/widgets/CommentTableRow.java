package kr.or.visitkorea.admin.client.manager.memberActivity.widgets;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.google.gwt.dom.client.Style.Cursor;
import com.google.gwt.dom.client.Style.Overflow;
import com.google.gwt.dom.client.Style.TextDecoration;
import com.google.gwt.dom.client.Style.TextOverflow;
import com.google.gwt.event.dom.client.ClickHandler;

import gwt.material.design.client.base.MaterialWidget;
import gwt.material.design.client.constants.Color;
import gwt.material.design.client.constants.Display;
import gwt.material.design.client.constants.FlexAlignItems;
import gwt.material.design.client.constants.FlexJustifyContent;
import gwt.material.design.client.constants.FlexWrap;
import gwt.material.design.client.constants.TextAlign;
import gwt.material.design.client.ui.MaterialLabel;
import gwt.material.design.client.ui.MaterialPanel;

public class CommentTableRow extends MaterialPanel {
	private ClickHandler clickEventListener;
	private CommentTableRow parentRow;
	private MaterialPanel childRowContainer;
	private List<MaterialWidget> childRowList = new ArrayList<>();
	private List<MaterialWidget> columnList = new ArrayList<>();
	private HashMap<String, Object> internalMap = new HashMap<String, Object>();
	private MaterialWidget selectedColumn = null;
	private boolean isSelected;
	private List<Integer> columnWidthList = new ArrayList<>();
	private boolean childrenVisible = false;
	
	public CommentTableRow() {
		super();
		this.init();
		this.addRowEventListener();
	}

	public CommentTableRow(int[] link, List<Integer> columnWidthList, Object... args) {
		super();
		this.columnWidthList = columnWidthList;
		this.init();
		this.addRowEventListener();
		this.addColumn(link, columnWidthList, args);
	}

	private void addColumn(int[] link, List<Integer> columnWidthList, Object... args) {
		List<Integer> linkList = new ArrayList<Integer>();
		for (int i : link) {
			linkList.add(i);
		}
		
		for (int i = 0; i < args.length; i++) {
			if (args[i] instanceof MaterialWidget) {
				MaterialWidget widget = (MaterialWidget) args[i];
				widget.setWidth(columnWidthList.get(i) + "%");
				widget.addClickHandler(e -> {
					this.selectedColumn = widget;
				});
				widget.setOverflow(Overflow.HIDDEN);
				widget.getElement().getStyle().setTextOverflow(TextOverflow.ELLIPSIS);
				this.columnList.add(widget);
				
			} else {
				String str = args[i] + "";
				
				MaterialLabel label = addColumnLabel(str);
				label.setWidth(columnWidthList.get(i) + "%");
				label.getElement().getStyle().setProperty("wordBreak", "break-all");
				label.addClickHandler(e -> {
					this.selectedColumn = label;
				});
				
				
				if (linkList.contains(i)) {
					label.getElement().getStyle().setTextDecoration(TextDecoration.UNDERLINE);
				}
				this.columnList.add(label);
			}
		}
		this.render();
	}

	public int getSelectedColumn() {
		int idx = this.columnList.indexOf(this.selectedColumn);
		this.selectedColumn = null;
		return idx;
	}
	
	public CommentTableRow addCommentChildRow(int[] link, Object... args) {
		CommentTableRow row = new CommentTableRow(link, this.columnWidthList, args);
		row.setWidth("100%");
		row.setParentRow(this);
		row.addRowEventListener();
		
		this.childRowList.add(row);
		this.renderChild();
		return row;
	}
	
	public CommentTableRow getParentRow() {
		return parentRow;
	}

	public void setParentRow(CommentTableRow parentRow) {
		this.parentRow = parentRow;
	}

	public void setChildrenVisible(boolean isVisible) {
		this.childrenVisible = isVisible;
		this.childRowContainer.setVisible(this.childrenVisible);
	}
	
	public boolean getChildrenVisible() {
		return this.childrenVisible;
	}
	
	public void clearChildRows() {
		this.childRowList.clear();
		this.childRowContainer.clear();
	}
	
	private void renderChild() {
		this.childRowList.forEach(this.childRowContainer::add);
	}
	
	private void render() {
		this.columnList.forEach(this::add);
	}

	private void init() {
		this.setWidth("101.45%");
		this.setDisplay(Display.FLEX);
		this.setFlexJustifyContent(FlexJustifyContent.START);
		this.setFlexAlignItems(FlexAlignItems.CENTER);
		this.setFlexWrap(FlexWrap.WRAP);
		this.getElement().getStyle().setCursor(Cursor.POINTER);
		this.setBackgroundColor(Color.WHITE);
		
		this.childRowContainer = new MaterialPanel();
		this.childRowContainer.setWidth("100%");
		this.childRowContainer.setVisible(false);
		this.childRowContainer.setFlexOrder(1);
		this.add(this.childRowContainer);
	}

	public void addRowEventListener() {
		this.addClickHandler(e -> {
			if (this.clickEventListener != null)
				this.clickEventListener.onClick(e);
		});
		this.addMouseOverHandler(e -> {
			if (!this.isSelected) {
				this.setBackgroundColor(Color.GREY_LIGHTEN_2);
			} else {
				this.setBackgroundColor(Color.WHITE);
			}
		});
		this.addMouseOutHandler(e -> {
			if (!this.isSelected) {
				this.setBackgroundColor(Color.WHITE); 
			} else {
				this.setBackgroundColor(Color.GREY_LIGHTEN_2);
			}
		});
	}
	
	public boolean isSelected() {
		return isSelected;
	}

	public void setSelected(boolean isSelected) {
		this.isSelected = isSelected;
	}

	public List<MaterialWidget> getColumnList() {
		return columnList;
	}

	public HashMap<String, Object> getInternalMap() {
		return internalMap;
	}

	public MaterialPanel getChildRowContainer() {
		return childRowContainer;
	}

	public void setClickEventListener(ClickHandler handler) {
		this.clickEventListener = handler;
	}
	
	public MaterialWidget getColumnObject(int idx) {
		return this.columnList.get(idx);
	}
	
	private MaterialLabel addColumnLabel(String title) {
		MaterialLabel label = new MaterialLabel();
		label.setText(title);
		label.setFontSize("1.0em");
		label.setPaddingTop(10);
		label.setPaddingBottom(10);
		label.setPaddingLeft(5);
		label.setPaddingRight(5);
		label.setTextAlign(TextAlign.CENTER);
		label.setFlexJustifyContent(FlexJustifyContent.CENTER);
		label.setDisplay(Display.FLEX);
		label.setFlexAlignItems(FlexAlignItems.CENTER);
		return label;
	}
}
