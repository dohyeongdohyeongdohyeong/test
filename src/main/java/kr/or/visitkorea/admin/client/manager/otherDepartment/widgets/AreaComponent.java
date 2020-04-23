package kr.or.visitkorea.admin.client.manager.otherDepartment.widgets;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.FontWeight;
import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.json.client.JSONObject;

import gwt.material.design.client.constants.Color;
import gwt.material.design.client.constants.TextAlign;
import gwt.material.design.client.ui.MaterialImage;
import gwt.material.design.client.ui.MaterialLabel;
import gwt.material.design.client.ui.MaterialPanel;

public abstract class AreaComponent extends MaterialPanel implements MainAreaComponent {

	private MaterialLabel titleLabel;
	private MaterialImage compIcon;
	private String compType;
	private List<JSONObject> inputObjArr;
	
	private String COMP_ID;
	private int COMP_ORDER;
	private String TEMPLATE_ID;
	private String MAIN_AREA;
	private String OTD_ID;
	private String ODM_ID;
	private String TITLE;
	private int VIEW_TITLE;
	private int COMP_DETAIL_PANEL_INDEX;
	
	public AreaComponent() {
		super();
		COMP_DETAIL_PANEL_INDEX = 2;
		inputObjArr = new ArrayList<JSONObject>();
	}
	
	public List<JSONObject> getInputObjArr() {
		return inputObjArr;
	}

	public void setInputObjArr(List<JSONObject> inputObjArr) {
		this.inputObjArr = inputObjArr;
	}

	public String getCOMP_ID() {
		return COMP_ID;
	}

	public void setCOMP_ID(String cOMP_ID) {
		COMP_ID = cOMP_ID;
	}

	public int getCOMP_ORDER() {
		return COMP_ORDER;
	}

	public void setCOMP_ORDER(int cOMP_ORDER) {
		COMP_ORDER = cOMP_ORDER;
	}

	public String getTEMPLATE_ID() {
		return TEMPLATE_ID;
	}

	public void setTEMPLATE_ID(String tEMPLATE_ID) {
		TEMPLATE_ID = tEMPLATE_ID;
	}

	public String getMAIN_AREA() {
		return MAIN_AREA;
	}

	public void setMAIN_AREA(String mAIN_AREA) {
		MAIN_AREA = mAIN_AREA;
	}

	public String getOTD_ID() { 
		return OTD_ID;
	}

	public void setOTD_ID(String oTD_ID) {
		OTD_ID = oTD_ID;
	}

	public String getODM_ID() {
		return ODM_ID;
	}

	public void setODM_ID(String oDM_ID) {
		ODM_ID = oDM_ID;
	}

	public String getTITLE() {
		return TITLE;
	}

	public void setTITLE(String tITLE) {
		TITLE = tITLE;
	}

	public int getVIEW_TITLE() {
		return VIEW_TITLE;
	}

	public void setVIEW_TITLE(int vIEW_TITLE) {
		VIEW_TITLE = vIEW_TITLE;
	}

	public void setTitleLabel(MaterialLabel titleLabel) {
		this.titleLabel = titleLabel;
	}

	public void appendInfo(JSONObject jObj) {
		inputObjArr.add(jObj);
	}
	
	public List<JSONObject> getInfo(){
		return this.inputObjArr;
	}

	public String getCompType() {
		return compType;
	}

	public void setCompType(String compType) {
		this.compType = compType;
	}

	public void buildUi() {
		
		this.setBorder("1px solid #aaaaaa");
		this.setLayoutPosition(Position.RELATIVE);
		this.setBackgroundColor(Color.WHITE);
		this.setMargin(6);
		this.setWidth("100px");
		this.setHeight("100px");
		
		titleLabel = new MaterialLabel();
		titleLabel.setLayoutPosition(Position.ABSOLUTE);
		titleLabel.setTop(5);
		titleLabel.setLeft(5);
		titleLabel.setPaddingLeft(5);
		titleLabel.setTextAlign(TextAlign.LEFT);
		titleLabel.setHeight("30px");
		titleLabel.setWidth("50px");
		titleLabel.setText(this.compType.substring(0, this.compType.lastIndexOf(".")));
		titleLabel.setFontWeight(FontWeight.BOLDER);
		titleLabel.setFontSize("1.6em");
		
		this.add(titleLabel);
		
		compIcon = new MaterialImage();
		compIcon.setLayoutPosition(Position.ABSOLUTE);
		compIcon.setUrl(GWT.getHostPageBaseURL() + "images/otherDeptIcon/" + this.compType );
		compIcon.setBottom(5);
		compIcon.setRight(5);
		compIcon.setHeight("56px");
		compIcon.setWidth("56px");
		this.add(compIcon);

	}


	public int getCompDetailPanelIndex() {
		return COMP_DETAIL_PANEL_INDEX;
	}

	public void setCompDetailPanelIndex(int panelIndex) {
		COMP_DETAIL_PANEL_INDEX = panelIndex;
	}

	public MaterialLabel getTitleLabel() {
		return titleLabel;
	}

	@Override
	public String toString() {
		return "AreaComponent [titleLabel=" + titleLabel + ", compIcon=" + compIcon + ", compType=" + compType
				+ ", inputObjArr=" + inputObjArr + ", COMP_ID=" + COMP_ID + ", COMP_ORDER=" + COMP_ORDER
				+ ", TEMPLATE_ID=" + TEMPLATE_ID + ", MAIN_AREA=" + MAIN_AREA + ", OTD_ID=" + OTD_ID + ", ODM_ID="
				+ ODM_ID + ", TITLE=" + TITLE + ", VIEW_TITLE=" + VIEW_TITLE + "]";
	}
	
}
