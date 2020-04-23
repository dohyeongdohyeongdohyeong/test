package kr.or.visitkorea.admin.client.manager.memberActivity.widgets;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.dom.client.Style.FontWeight;
import com.google.gwt.dom.client.Style.Overflow;
import com.google.gwt.event.dom.client.ClickHandler;

import gwt.material.design.client.base.MaterialWidget;
import gwt.material.design.client.constants.Color;
import gwt.material.design.client.constants.Display;
import gwt.material.design.client.constants.FlexAlignItems;
import gwt.material.design.client.constants.FlexJustifyContent;
import gwt.material.design.client.constants.TextAlign;
import gwt.material.design.client.ui.MaterialLabel;
import gwt.material.design.client.ui.MaterialPanel;
import kr.or.visitkorea.admin.client.manager.widgets.ContentMenu;

public class CommentTable extends MaterialPanel {
	private ContentMenu topArea;
	private MaterialPanel headerArea;
	private MaterialPanel rowContainer;
	private ContentMenu bottomArea;
	private CommentTableRow selectedRow;
	private int selectedRowIndex = -1;
	private List<CommentTableRow> rowList = new ArrayList<>();
	private List<MaterialWidget> headerColumnList = new ArrayList<>();
	private List<Integer> columnWidthList = new ArrayList<>();

	private final String DEFAULT_WIDTH = "100%";
	private final String TOP_DEFAULT_HEIGHT = "25px";
	private final String HEADER_DEFAULT_HEIGHT = "45px";
	private final String CONTAINER_DEFAULT_HEIGHT = "430px";
	private final String BOTTOM_DEFAULT_HEIGHT = "30px";
	
	public CommentTable() {
		init();
	}
	
	private void init() {
		this.headerArea = new MaterialPanel();
		this.headerArea.setDisplay(Display.FLEX);
		this.headerArea.setFlexJustifyContent(FlexJustifyContent.START);
		this.headerArea.setWidth(DEFAULT_WIDTH);
		this.headerArea.setHeight(HEADER_DEFAULT_HEIGHT);
		this.headerArea.setBackgroundColor(Color.BLUE);
		this.headerArea.setPaddingRight(20);
		this.add(headerArea);

		this.rowContainer = new MaterialPanel();
		this.rowContainer.setWidth(DEFAULT_WIDTH);
		this.rowContainer.setHeight(CONTAINER_DEFAULT_HEIGHT);
		this.rowContainer.getElement().getStyle().setOverflowY(Overflow.SCROLL);
		this.rowContainer.getElement().getStyle().setOverflowX(Overflow.HIDDEN);
		this.rowContainer.setBorderLeft("1px solid gainsboro");
		this.rowContainer.setBorderRight("1px solid gainsboro");
		this.rowContainer.setPaddingRight(22);
		this.add(rowContainer);

		this.bottomArea = new ContentMenu();
		this.bottomArea.setWidth(DEFAULT_WIDTH);
		this.bottomArea.setHeight(BOTTOM_DEFAULT_HEIGHT);
		this.bottomArea.setBackgroundColor(Color.WHITE);
		this.bottomArea.setBorder("1px solid gainsboro");
		this.add(bottomArea);
		
		this.render();
	}

	private void render() {
		this.rowContainer.clear();
		this.rowList.forEach(this.rowContainer::add);
	}
	
	private MaterialLabel addHeaderLabel(String title, int width) {
		MaterialLabel label = new MaterialLabel();
		label.setText(title);
		label.setWidth(width + "%");
		label.setFontWeight(FontWeight.BOLD);
		label.setFontSize("1.2em");
		label.setTextAlign(TextAlign.CENTER);
		label.setBackgroundColor(Color.BLUE);
		label.setFlexJustifyContent(FlexJustifyContent.CENTER);
		label.setDisplay(Display.FLEX);
		label.setFlexAlignItems(FlexAlignItems.CENTER);
		label.setTextColor(Color.WHITE);
		return label;
	}
	
	public CommentTable appendTitle(String title, int percentWidth) {
		this.headerArea.clear();
		
		MaterialLabel label = this.addHeaderLabel(title, percentWidth);
		
		this.columnWidthList.add(percentWidth);
		this.headerColumnList.add(label);
		this.headerColumnList.forEach(this.headerArea::add);
		return this;
	}
	
	public CommentTableRow addCommentRow(int[] link, Object... args) {
		CommentTableRow row = new CommentTableRow(link, columnWidthList, args);
		row.setClickEventListener(this.setRowSelectedEvent());

		this.rowList.add(row);
		this.render();
		return row;
	}
	
	private ClickHandler setRowSelectedEvent() {
		return e -> {
			CommentTableRow row = (CommentTableRow) e.getSource();
			if (this.selectedRow != null) {
				this.selectedRow.setSelected(false);
				this.selectedRow.setBackgroundColor(Color.WHITE);
			}
			this.selectedRow = row;
			this.selectedRow.setSelected(true);
			this.selectedRow.setBackgroundColor(Color.GREY_LIGHTEN_2);
			this.selectedRowIndex = this.rowList.indexOf(row);
		};
	}
	
	public int getSelectedRowIndex() {
		return selectedRowIndex;
	}

	public void clearRows() {
		this.rowList.clear();
		this.render();
	}
	
	public int getCommentCount() {
		return this.rowList.size();
	}
	
	public CommentTableRow getSelectedRow() {
		return selectedRow;
	}

	public void setSelectedRow(CommentTableRow selectedRow) {
		this.selectedRow = selectedRow;
	}

	public List<CommentTableRow> getRowList() {
		return rowList;
	}

	public void setRowList(List<CommentTableRow> rowList) {
		this.rowList = rowList;
	}

	public ContentMenu getTopArea() {
		return topArea;
	}

	public MaterialPanel getHeaderArea() {
		return headerArea;
	}

	public MaterialPanel getRowContainer() {
		return rowContainer;
	}

	public ContentMenu getBottomArea() {
		return bottomArea;
	}

}
