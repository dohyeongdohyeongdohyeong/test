package kr.or.visitkorea.admin.client.manager.otherDepartment.composite.festival;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gwt.dom.client.Style.Float;
import com.google.gwt.dom.client.Style.FontWeight;
import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONNumber;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.user.client.ui.Widget;

import gwt.material.design.client.constants.Color;
import gwt.material.design.client.constants.IconType;
import gwt.material.design.client.constants.TextAlign;
import gwt.material.design.client.ui.MaterialLink;
import gwt.material.design.client.ui.MaterialPanel;
import kr.or.visitkorea.admin.client.application.Console;
import kr.or.visitkorea.admin.client.application.Registry;
import kr.or.visitkorea.admin.client.manager.otherDepartment.OtherDepartmentMainApplication;
import kr.or.visitkorea.admin.client.manager.otherDepartment.dialogs.SeasonContentDialog;
import kr.or.visitkorea.admin.client.manager.otherDepartment.widgets.SearchBodyWidget;
import kr.or.visitkorea.admin.client.manager.otherDepartment.widgets.TagListLabelCell;
import kr.or.visitkorea.admin.client.manager.otherDepartment.widgets.TagListRow;
import kr.or.visitkorea.admin.client.manager.otherDepartment.widgets.ViewPanel;
import kr.or.visitkorea.admin.client.manager.otherDepartment.widgets.VisitKoreaListCell;
import kr.or.visitkorea.admin.client.widgets.dialog.ButtonInfo;
import kr.or.visitkorea.admin.client.widgets.dialog.DialogContent;
import kr.or.visitkorea.admin.client.widgets.window.MaterialExtentsWindow;

public class FestivalDetailSelectContent extends MaterialPanel implements FestivalContentRight {

	private FestivalContentLeftArea leftArea;
	private MaterialExtentsWindow materialWindow;
	private DialogContent dialog;
	private MaterialLink upLink;
	private MaterialLink dnLink;
	private MaterialLink rmLink;
	private MaterialLink rmAllLink;
	private SearchBodyWidget searchBody; 
	private Map<String, Object> valueMap = new HashMap<String, Object>();
	private Map<String, Object> param;
	private TagListRow targetListRow;
	private JSONArray detailArray;
	private JSONArray selectedTagsContentObj;
	private JSONObject result;
	private ViewPanel viewPanel;
	private int index;

	public FestivalDetailSelectContent(MaterialExtentsWindow materialExtentsWindow, SeasonContentDialog seasonContentDialog) {
		this.materialWindow = materialExtentsWindow;
		this.dialog = seasonContentDialog;
		init();
	}

	public void init() {
		this.setLayoutPosition(Position.RELATIVE);
		this.setHeight("100%");
		buildContent();
	}

	private void buildContent() {
		
		NoneSearchWidget search = new NoneSearchWidget();
		
		searchBody = new SearchBodyWidget();
		searchBody.setMarginTop(20);
		searchBody.setWidth("100%");
		searchBody.setHeight("100%");
		
		upLink = new MaterialLink(IconType.KEYBOARD_ARROW_UP);
		dnLink = new MaterialLink(IconType.KEYBOARD_ARROW_DOWN);
		rmLink = new MaterialLink(IconType.REMOVE);
		rmAllLink = new MaterialLink(IconType.ALL_OUT);
		
		upLink.setTooltip("위로 이동");
		dnLink.setTooltip("아래로 이동");
		rmLink.setTooltip("선택 삭제");
		rmAllLink.setTooltip("전체 삭제");
		
		searchBody.addLink(dnLink, com.google.gwt.dom.client.Style.Float.RIGHT);
		searchBody.addLink(upLink, com.google.gwt.dom.client.Style.Float.RIGHT);
		searchBody.addLink(rmLink, com.google.gwt.dom.client.Style.Float.LEFT);
		searchBody.addLink(rmAllLink, com.google.gwt.dom.client.Style.Float.LEFT);
		
		search.setBody(searchBody);
		searchBody.setSearch(search);
		
		List<TagListRow> listRows = new ArrayList<TagListRow>();
		
		searchBody.addRowAll(listRows);
		
		this.add(search);
		this.add(searchBody);
		
		searchBody.setHeaderTitleVisible(false);
		searchBody.setHeaderTitle(new String[] {"태그명","컨텐츠 보유 수","노출 수", "노출 설정"});
		searchBody.render();
		
		rmAllLink.addClickHandler(event->{
				
				List<ButtonInfo> btnList = new ArrayList<ButtonInfo>();
				
				btnList.add(new ButtonInfo(
						"삭제", Color.RED, Float.RIGHT, 
						new ClickHandler() {
							
							@Override
							public void onClick(ClickEvent event) {
								searchBody.removeAll();
								dialog.closeDialog();
							}
							
						}
				));
				
				this.dialog.open(
						"컨텐츠 전체 삭제", 600, 250,
						new String[] {
							"등록된 컨텐츠 전체를 삭제 하시겠습니까?",
							"데이터를 삭제하면 되돌릴 수 없습니다.", 
							"신중하게 선택해 주세요."
						},
				btnList);
		});
		
		rmLink.addClickHandler(event->{
			if (searchBody.getSelectedRow() != null) {
				
				List<ButtonInfo> btnList = new ArrayList<ButtonInfo>();
				btnList.add(new ButtonInfo(
						"삭제", Color.RED, Float.RIGHT, 
						new ClickHandler() {
							
							@Override
							public void onClick(ClickEvent event) {
								searchBody.removeSelectedPanel();
								dialog.closeDialog();
							}
							
						}
				));
				
				this.dialog.open(
						"컨텐츠 삭제", 600, 250,
						new String[] {
							"삭제 하시겠습니까?",
							"데이터를 삭제하면 되돌릴 수 없습니다.", 
							"신중하게 선택해 주세요."
						},
						btnList);
			
			}
			
		});
		
		upLink.addClickHandler(event->{
			searchBody.selectedPanelMoveUp();
		});
		
		dnLink.addClickHandler(event->{
			searchBody.selectedPanelMoveDown();
		});
		
		valueMap.put("LIST", searchBody);
		
	}
	
	public int getRowCount() {
		return searchBody.getRows().size();
	}
	
	public Map<String, Object> getValueMap(){
		return this.valueMap;
	}
	
	private MaterialExtentsWindow getWindow() {
		return this.materialWindow;
	}
	
	@Override
	public void setLeftPanel(FestivalContentLeftArea leftArea) {
		this.leftArea = leftArea;
	}
	
	@Override
	public void clearAll() {
		setDisableContent(true);
	}
	
	public void setDisableContent(boolean disable) {
		
		upLink.setEnabled(!disable);
		dnLink.setEnabled(!disable);
		rmLink.setEnabled(!disable);
		rmAllLink.setEnabled(!disable);
		searchBody.setEnabled(!disable);
	}
	
	public void setParamters(Map<String, Object> parameters) {
		
		this.param = parameters;
		this.searchBody.getListBody().clear();
		
		selectedTagsContentObj = (JSONArray) this.param.get("SEASON_TAG_CONTENT_OBJ");
		targetListRow = (TagListRow) this.param.get("SELECTED_CONDITION");
		detailArray = (JSONArray)targetListRow.get("SELECTED_DETAIL_LIST");
		viewPanel = (ViewPanel)parameters.get("VIEW_PANEL");
		result = (JSONObject)viewPanel.getData("REC_CONTAINER");
		index = (int)targetListRow.get("INDEX");
		
		TagListLabelCell isAutomationLabelCell = (TagListLabelCell)targetListRow.getCell(4);
		
		searchBody.removeAll();

		if (isAutomationLabelCell.getText().equals("자동")) {
			setDisableContent(false);
		} else if (isAutomationLabelCell.getText().equals("수동")) {
			setDisableContent(false);
			displayDetailArray();
		}
		
		
	}
	
	private void displayDetailArray() {
		
		JSONObject recContainer = (JSONObject)viewPanel.getData("REC_CONTAINER");
		JSONObject recObject = recContainer.get("seasonTagsArr").isArray().get(index).isObject();
		JSONArray tagChildren = recObject.get("CHILDREN").isArray();
		
		Console.log("displayDetailArray.tagChildren :: " + tagChildren);
		
		for (int i=tagChildren.size()-1; i>-1; i--) {
			
			JSONObject jObj = tagChildren.get(i).isObject();
			
			String address = jObj.containsKey("ADDRESS") ? jObj.get("ADDRESS").isString().stringValue() : jObj.get("AREA").isString().stringValue() + " " + jObj.get("SIGUGUN").isString().stringValue();
			String dates = jObj.containsKey("DATES") ? jObj.get("DATES").isString().stringValue() : jObj.get("START_DATE").isString().stringValue() + " ~ " + jObj.get("END_DATE").isString().stringValue();
			int status = jObj.containsKey("CONTENT_STATUS") ? (int) jObj.get("CONTENT_STATUS").isNumber().doubleValue() : 0;
			
			List<VisitKoreaListCell> rowInfoList = new ArrayList<VisitKoreaListCell>();
			if (status == 2) {
			rowInfoList.add(new TagListLabelCell(jObj.get("TITLE").isString().stringValue(), Float.LEFT, "268px",  "50", 50, FontWeight.BOLDER, true, TextAlign.LEFT));
			rowInfoList.add(new TagListLabelCell(address, Float.LEFT, "200px",  "50", 50, FontWeight.BOLDER, true, TextAlign.CENTER));
			rowInfoList.add(new TagListLabelCell(dates, Float.LEFT, "100px",  "50", 50, FontWeight.BOLDER, false, TextAlign.CENTER));
			} else {
				rowInfoList.add(new TagListLabelCell(jObj.get("TITLE").isString().stringValue(), Float.LEFT, "268px",  "50", 50, FontWeight.BOLDER, true, TextAlign.LEFT,Color.RED_LIGHTEN_1));
				rowInfoList.add(new TagListLabelCell(address, Float.LEFT, "200px",  "50", 50, FontWeight.BOLDER, true, TextAlign.CENTER,Color.RED_LIGHTEN_1));
				rowInfoList.add(new TagListLabelCell(dates, Float.LEFT, "100px",  "50", 50, FontWeight.BOLDER, false, TextAlign.CENTER,Color.RED_LIGHTEN_1));
			}

			TagListRow tagListRow = new TagListRow(rowInfoList);	
			tagListRow.put("AREA", jObj.get("AREA").isString().stringValue());
			tagListRow.put("SIGUGUN", jObj.get("SIGUGUN").isString().stringValue());
			tagListRow.put("START_DATE", jObj.get("START_DATE").isString().stringValue());
			tagListRow.put("END_DATE", jObj.get("END_DATE").isString().stringValue());
			tagListRow.put("SAT_ID", jObj.get("SAT_ID").isString().stringValue());
			tagListRow.setTagName(jObj.get("COT_ID").isString().stringValue());
			tagListRow.setCount(0);
			
			searchBody.addRow(tagListRow);
			
		}
		
	}

	public void save() {
		
		List<Widget> lists  = searchBody.getRows();
		
		JSONArray newJSONArray = new JSONArray();
		
		for (int i=0; i<lists.size(); i++) {
			
			TagListRow tagListrow  = (TagListRow)lists.get(i);
			
			JSONObject newRecordObject = new JSONObject();
			newRecordObject.put("TITLE", new JSONString(tagListrow.getCell(0).getValue()));
			newRecordObject.put("ADDRESS", new JSONString(tagListrow.getCell(1).getValue()));
			newRecordObject.put("DATES", new JSONString(tagListrow.getCell(2).getValue()));
			newRecordObject.put("COT_ID", new JSONString(tagListrow.getUniqueName()));
			newRecordObject.put("SAT_ID", new JSONString((String) tagListrow.get("SAT_ID")));
			newRecordObject.put("AREA", new JSONString((String) tagListrow.get("AREA")));
			newRecordObject.put("SIGUGUN", new JSONString((String) tagListrow.get("SIGUGUN")));
			newRecordObject.put("START_DATE", new JSONString((String) tagListrow.get("START_DATE")));
			newRecordObject.put("END_DATE", new JSONString((String) tagListrow.get("END_DATE")));
			
			newJSONArray.set(i, newRecordObject);
			
		}

		int rowIndex = (int)targetListRow.get("INDEX");
		
		JSONObject rec_container = (JSONObject)viewPanel.getData("REC_CONTAINER");
		rec_container.get("seasonTagsArr").isArray().get(rowIndex).isObject().put("CHILDREN", newJSONArray);
		viewPanel.setData("REC_CONTAINER", rec_container);
		
		targetListRow.put("SELECTED_DETAIL_LIST", newJSONArray);

		
		
		
	}
	
}
