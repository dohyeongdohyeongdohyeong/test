package kr.or.visitkorea.admin.client.manager.event.widgets;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import com.google.api.client.util.DateTime;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Float;
import com.google.gwt.dom.client.Style.FontWeight;
import com.google.gwt.dom.client.Style.Overflow;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.datepicker.client.CalendarUtil;

import gwt.material.design.client.constants.Color;
import gwt.material.design.client.constants.Display;
import gwt.material.design.client.constants.FlexAlignItems;
import gwt.material.design.client.constants.FlexJustifyContent;
import gwt.material.design.client.constants.IconType;
import gwt.material.design.client.constants.TextAlign;
import gwt.material.design.client.ui.MaterialIcon;
import gwt.material.design.client.ui.MaterialLabel;
import gwt.material.design.client.ui.MaterialPanel;
import kr.or.visitkorea.admin.client.application.Console;

public class GantChartTable extends MaterialPanel {
	
	private int selectedYear;
	private int selectedMonth;
	private int totalDate;
	private MaterialLabel currMonthLabel;
	private MaterialPanel topArea;
	private MaterialPanel contentArea;
	private MaterialPanel tableHeader;
	private MaterialPanel rowContainer;
	private static final int NAME_COLUMN_WIDTH = 150;
	private List<HashMap<String, Object>> itemList = new ArrayList<>();
	
	public GantChartTable() {
		this.setWidth("100%");
		this.setHeight("100%");
		initialDate();
		init();
	}

	public GantChartTable(String width, String height) {
		this.setWidth(width);
		this.setHeight(height);
		initialDate();
		init();
	}
	
	public void initialDate() {
		Date date = new Date();
		Date copyOfDate = CalendarUtil.copyDate(date);
		
		CalendarUtil.addMonthsToDate(copyOfDate, 1);
		int daysBetween = CalendarUtil.getDaysBetween(date, copyOfDate);
		
		String currDate = DateTimeFormat.getFormat("yyyy-MM-dd").format(date);
		String[] dateArr = currDate.split("-");
		this.selectedYear = Integer.parseInt(dateArr[0]);
		this.selectedMonth = Integer.parseInt(dateArr[1]);
		this.totalDate = daysBetween;
	}
	
	public void init() {
		topArea = new MaterialPanel();
		topArea.setWidth("100%");
		topArea.setDisplay(Display.FLEX);
		topArea.setPadding(10);
		topArea.setFlexJustifyContent(FlexJustifyContent.CENTER);
		topArea.setFlexAlignItems(FlexAlignItems.CENTER);
		
		MaterialIcon prevIcon = new MaterialIcon(IconType.CHEVRON_LEFT);
		prevIcon.setFontSize("2.4em");
		prevIcon.addClickHandler(event -> {
			if (this.selectedMonth == 1) {
				this.selectedYear--;
				this.selectedMonth = 12;
			} else {
				this.selectedMonth--;
			}
			render();
		});
		
		currMonthLabel = new MaterialLabel();
		currMonthLabel.setText(this.selectedYear + "년 " + this.selectedMonth + "월");
		currMonthLabel.setTextColor(Color.BLUE);
		currMonthLabel.setFontSize("2.2em");
		currMonthLabel.setMarginLeft(15);
		currMonthLabel.setMarginRight(15);
		
		MaterialIcon nextIcon = new MaterialIcon(IconType.CHEVRON_RIGHT);
		nextIcon.setFontSize("2.4em");
		nextIcon.addClickHandler(event -> {
			if (this.selectedMonth == 12) {
				this.selectedYear++;
				this.selectedMonth = 1;
			} else {
				this.selectedMonth++;
			}
			render();
		});
		
		topArea.add(prevIcon);
		topArea.add(currMonthLabel);
		topArea.add(nextIcon);
		
		contentArea = new MaterialPanel();
		contentArea.getElement().getStyle().setOverflowX(Overflow.SCROLL);
		contentArea.getElement().getStyle().setOverflowY(Overflow.SCROLL);
		
		tableHeader = addHeaderRow();
		
		rowContainer = new MaterialPanel();
		rowContainer.setWidth(this.totalDate * 50 + NAME_COLUMN_WIDTH + 100 + "px");
		rowContainer.setWidth("100%");
		
		contentArea.add(tableHeader);
		contentArea.add(rowContainer);
		
		this.add(topArea);
		this.add(contentArea);
	}
	
	public MaterialPanel addHeaderRow() {
		MaterialPanel header = buildRow();

		MaterialLabel nameLabel = addLabel("이벤트명", NAME_COLUMN_WIDTH, 45, Color.BLUE, Color.WHITE);
		header.add(nameLabel);
		for (int i = 1; i <= this.totalDate; i++) {
			MaterialLabel label = addLabel(i + "", 50, 45, Color.BLUE, Color.WHITE);
			header.add(label);
		}
		return header;
	}
	
	public MaterialPanel getRowContainer() {
		return rowContainer;
	}

	public List<Widget> getRows() {
		return this.rowContainer.getChildrenList();
	}

	protected GantChartTableRow buildRow() {
		GantChartTableRow row = new GantChartTableRow();
		row.setTextAlign(TextAlign.LEFT);
		row.setWidth(this.totalDate * 50 + NAME_COLUMN_WIDTH + 100 + "px");
		return row;
	}
	
	public MaterialPanel getContentArea() {
		return contentArea;
	}

	public void render() {
		currMonthLabel.setText(this.selectedYear + "년 " + this.selectedMonth + "월");

		Console.log(this.itemList.toString());
		this.rowContainer.clear();
		this.itemList.forEach(item -> {
			appendRow(item, (Color) item.get("bgColor"), (String) item.get("name"));
		});
	}
	
	public GantChartTableRow addRow(Color bgColor, String name, String startDate, String endDate) {
		int startYear = Integer.parseInt(startDate.split("-")[0]);
		int startMonth = Integer.parseInt(startDate.split("-")[1]);
		int startDay = Integer.parseInt(startDate.split("-")[2]);

		int endYear = Integer.parseInt(endDate.split("-")[0]);
		int endMonth = Integer.parseInt(endDate.split("-")[1]);
		int endDay = Integer.parseInt(endDate.split("-")[2]);
		
		HashMap<String, Object> params = new HashMap<>();
		params.put("name", name);
		params.put("startYear", startYear);
		params.put("startMonth", startMonth);
		params.put("startDay", startDay);
		params.put("endYear", endYear);
		params.put("endMonth", endMonth);
		params.put("endDay", endDay);
		params.put("bgColor", bgColor);
		this.itemList.add(params);
		
		return appendRow(params, bgColor, name);
	}
	
	private GantChartTableRow appendRow(HashMap<String, Object> params, Color bgColor, String name) {
		int startYear = (int) params.get("startYear");
		int startMonth = (int) params.get("startMonth");
		int startDay =  (int) params.get("startDay");
		int endYear =  (int) params.get("endYear");
		int endMonth =  (int) params.get("endMonth");
		int endDay =  (int) params.get("endDay");

		if (this.selectedYear < startYear || this.selectedYear > endYear) {
			return null;
		} else if (startYear == endYear) {
			if (this.selectedMonth < startMonth || this.selectedMonth > endMonth) {
				return null;
			}
		} else if (startYear < endYear) {
			if (this.selectedYear <= startYear && this.selectedMonth < startMonth) {
				return null;
			}
			if (this.selectedYear >= endYear && this.selectedMonth > endMonth) {
				return null;
			}
		}
		
		GantChartTableRow row = buildRow();
		MaterialLabel nameLabel = addLabel(name, NAME_COLUMN_WIDTH, 60, bgColor, Color.BLACK);
		row.add(nameLabel);
		
		for (int i = 1; i <= this.totalDate; i++) {
			MaterialLabel label = addLabel("", 50, 60, bgColor, Color.BLACK);
			label.setPaddingTop(20);
			label.setPaddingBottom(20);

			Date sDate = DateTimeFormat.getFormat("yyyy-MM-dd").parse(startYear + "-" + startMonth + "-" + startDay);
			Date eDate = DateTimeFormat.getFormat("yyyy-MM-dd").parse(endYear + "-" + endMonth + "-" + endDay);
			
			Date currentDate = DateTimeFormat.getFormat("yyyy-MM-dd").parse(this.selectedYear + "-" + this.selectedMonth + "-" + i);
			
			if (currentDate.compareTo(sDate) >= 0 && currentDate.compareTo(eDate) <= 0) {
				MaterialLabel mark = addMark();
				label.add(mark);
			}
			
			row.add(label);
		}
		
		this.rowContainer.add(row);
		return row;
	}
	
	private MaterialLabel addMark() {
		MaterialLabel label = new MaterialLabel();
		label.setWidth("100%");
		label.setHeight("100%");
		label.setBackgroundColor(Color.BLACK);
		return label;		
	}
	
	protected MaterialLabel addLabel(String text, int width, int height, Color bgColor, Color textColor) {
		MaterialLabel label = new MaterialLabel();
		label.setText(text);
		label.setFloat(Float.LEFT);
		label.setWidth(width + "px");
		label.setFontWeight(FontWeight.BOLD);
		label.setDisplay(Display.INLINE_BLOCK);
		label.setHeight(height + "px");
		label.setBorder("1px solid white");
		label.setTextAlign(TextAlign.CENTER);
		label.setBackgroundColor(bgColor);
		label.setTextColor(textColor);
		return label;		
	}
}
