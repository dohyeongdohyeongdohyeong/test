package kr.or.visitkorea.admin.client.manager.otherDepartment.composite;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.google.gwt.dom.client.Style.FontWeight;
import com.google.gwt.dom.client.Style.Overflow;
import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.dom.client.Style.VerticalAlign;
import com.google.gwt.dom.client.Style.Visibility;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.user.client.ui.Widget;

import gwt.material.design.client.constants.Color;
import gwt.material.design.client.constants.TextAlign;
import gwt.material.design.client.ui.MaterialIcon;
import gwt.material.design.client.ui.MaterialLabel;
import gwt.material.design.client.ui.MaterialPanel;
import kr.or.visitkorea.admin.client.application.Console;

public class CustomRowTable extends MaterialPanel{

	private MaterialPanel topMenu;
	private MaterialPanel bodyArea;
	private MaterialPanel botoomMenu;
	private MaterialPanel titleArea;
	private MaterialPanel contentArea;
	private MasterContentRowCheckBox selectedPanel;
	private List<Widget> wList = new ArrayList<Widget>();

	private String borderSpec = "1px solid #aaaaaa";
	
	public CustomRowTable() {
		super();
		init();
	}

	public CustomRowTable(String... initialClass) {
		super(initialClass);
		init();
	}

	private void init() {
		
		this.setLayoutPosition(Position.ABSOLUTE);
		
		this.topMenu = new MaterialPanel();
		this.topMenu.setLayoutPosition(Position.RELATIVE);
		this.topMenu.setWidth("100%");
		this.topMenu.setHeight("40px");
		this.topMenu.setLeft(0);
		
		this.bodyArea = new MaterialPanel();
		this.bodyArea.setLayoutPosition(Position.RELATIVE);
		this.bodyArea.setWidth("100%");
		this.bodyArea.setHeight("414px");
		this.bodyArea.setLeft(0);
		
		this.botoomMenu = new MaterialPanel();
		this.botoomMenu.setLayoutPosition(Position.RELATIVE);
		this.botoomMenu.setWidth("100%");
		this.botoomMenu.setHeight("27px");
		this.botoomMenu.setLeft(0);
		this.botoomMenu.setBorder(borderSpec);
		
		this.add(this.topMenu);
		this.add(this.bodyArea);
		this.add(this.botoomMenu);

		
		this.titleArea = new MaterialPanel();
		this.titleArea.setLayoutPosition(Position.RELATIVE);
		this.titleArea.setBackgroundColor(Color.GREY_LIGHTEN_1);
		this.titleArea.setWidth("100%");
		this.titleArea.setHeight("40px");
		this.titleArea.setLeft(0);
		this.bodyArea.add(this.titleArea);

		this.contentArea = new MaterialPanel();
		this.contentArea.getElement().getStyle().setOverflowX(Overflow.HIDDEN);
		this.contentArea.getElement().getStyle().setOverflowY(Overflow.VISIBLE);
		this.contentArea.setLayoutPosition(Position.RELATIVE);
		this.contentArea.setWidth("100%");
		this.contentArea.setHeight("374px");
		this.contentArea.setBorderLeft(borderSpec);
		this.contentArea.setBorderRight(borderSpec);
		this.contentArea.setLeft(0);
		this.bodyArea.add(this.contentArea);

	}

	@Override
	protected void onLoad() {
		super.onLoad();
		
	}
	
	public void appendTitle(String title, int width, TextAlign textAlign) {
		
		MaterialLabel __title = new MaterialLabel(title);
		__title.setTextAlign(textAlign);
		__title.setWidth(width + "px");
		__title.setFontWeight(FontWeight.BOLD);
		__title.setLineHeight(40);
		__title.setFloat(com.google.gwt.dom.client.Style.Float.LEFT);
		__title.setTextColor(Color.WHITE);
		this.titleArea.add(__title);
	}

	public void addTopMenu(MaterialIcon icon, String tooltip, com.google.gwt.dom.client.Style.Float styleFloat) {
		
		icon.setTooltip(tooltip);
		icon.setVerticalAlign(VerticalAlign.MIDDLE);
		icon.setFontSize("1.5em");
		icon.setHeight("26px");
		icon.setMargin(0);
		icon.setWidth("26px");
		icon.setFloat(styleFloat);
		icon.setLineHeight(26);

		topMenu.add(icon);
		
	}

	public void addBottomMenu(MaterialIcon icon, String tooltip, com.google.gwt.dom.client.Style.Float styleFloat) {
		
		icon.setTooltip(tooltip);
		icon.setVerticalAlign(VerticalAlign.MIDDLE);
		icon.setFontSize("1.0em");
		icon.setHeight("26px");
		icon.setMargin(0);
		icon.setWidth("26px");
		icon.setFloat(styleFloat);
		icon.setLineHeight(24);

		if (styleFloat.equals(com.google.gwt.dom.client.Style.Float.LEFT)) {
			icon.setBorderRight("1px solid #aaaaaa");
		} else {
			icon.setBorderLeft("1px solid #aaaaaa");
		}
		
		botoomMenu.add(icon);
		
	}

	public void addCheckBoxRow(ContentRow contentRow) {
		
		MasterContentRowCheckBox chkboxRow = new MasterContentRowCheckBox(this);
		chkboxRow.addPanel(contentRow);
		wList.add(chkboxRow);		
		this.contentArea.add(chkboxRow);
	}

	public void addCheckBoxRowRED(ContentRow contentRow) {
		
		((MaterialPanel)contentRow).setBackgroundColor(Color.WHITE);
		
		MasterContentRowCheckBox chkboxRow = new MasterContentRowCheckBox(this);
		chkboxRow.setBackgroundColor(Color.RED_LIGHTEN_4);
		chkboxRow.addPanel(contentRow);
		wList.add(chkboxRow);		
		this.contentArea.add(chkboxRow);
		
	}

	public void addNormalboxRow(ContentRow child) {
		
		MasterContentRowNormalBox boxRow = new MasterContentRowNormalBox(this);
		boxRow.addPanel(child);
		wList.add(boxRow);		
		this.contentArea.add(boxRow);
	}

	public void contentClear() {
		this.contentArea.clear();
	}
	
	public void redraw() {

		int titleIndex = 0;
		for (Widget row : wList) {
			ContentRowAdapter rowPanel = (ContentRowAdapter)row;
			ContentRow crow = rowPanel.getContentRow();
			crow.setRowTitle(titleIndex+"");
			this.contentArea.add(row);
			titleIndex++;
		}
	}
	
	public void reOrder(int source, int target) {

		Collections.swap(wList, source, target);
		redraw();
	}
	
	public MaterialPanel getSelectedPanel() {
		 return this.selectedPanel;
	}

	public void setSelectedPanel(MaterialPanel retPanel) {
		
		if (this.selectedPanel != null) {
			this.selectedPanel.setCheckBoxValue(false);
		}
		
		this.selectedPanel = (MasterContentRowCheckBox) retPanel;
	}

	public MaterialPanel getContentPanel() {
		return this.contentArea;
	}

	public void reOrder(MaterialPanel targetPanel, int target) {
		
		int srcIndex = wList.indexOf(targetPanel);
		int totalIndex = wList.size() -1;
		
		if (target > 0 && srcIndex == totalIndex) {
			return;
		}
		
		if (target < 0 && srcIndex == 0) {
			return;
		}
		
		reOrder(srcIndex, (srcIndex + (target)));
		
	}

	public int deleteRow() {
		// TODO: 확인이 필요한 부분
//		int maxindex = wList.size()-1;
//		int beforeIndex = wList.indexOf(selectedPanel);
//		int newSelectedIndex = 0;
//		
//		if (maxindex == beforeIndex) {
//			newSelectedIndex = maxindex -1;
//		}else {
//			newSelectedIndex = maxindex;
//		}
		
		if (this.selectedPanel != null) {
			wList.remove(wList.indexOf(selectedPanel));
		}
		
//		this.selectedPanel = (MasterContentRowCheckBox) wList.get(newSelectedIndex);
		
		this.contentArea.clear();
		redraw();
		return wList.size();
	}

	public JSONArray getData() {
		
		JSONArray retArr = new JSONArray();
		
		double i=0;
		for (Widget widget : wList) {
			MasterContentRowCheckBox ccbr = (MasterContentRowCheckBox)widget;
			ContentRow contentRow = ccbr.getContentRow();
			JSONObject contentRowJSON = contentRow.getJSONObject();
			contentRowJSON.put("CGP_ID", new JSONString(i+""));
			retArr.set(retArr.size(), contentRowJSON);
			i++;
		}
		
		return retArr;
	}

	public void setHeaderVisible(boolean b) {
		if (b) {
			this.titleArea.setVisibility(Visibility.VISIBLE);
			this.contentArea.setHeight("374px");
			this.contentArea.setBorderTop("");
		}else {
			this.titleArea.setVisibility(Visibility.HIDDEN);
			this.contentArea.setBorderTop(borderSpec);
			this.contentArea.setHeight("414px");
		}
		this.titleArea.setVisible(b);
	}

	public boolean validateAll() {
		boolean chkValidate = true;
		
		double i=0;
		for (Widget widget : wList) {
			MasterContentRowCheckBox ccbr = (MasterContentRowCheckBox)widget;
			ContentRow contentRow = ccbr.getContentRow();
			if (contentRow.isValidate() == false) {
				chkValidate = false;
				break;
			}
		}
		
		return chkValidate;
	}
	
	public List<Widget> getwList(){
		return wList;
	}

}
