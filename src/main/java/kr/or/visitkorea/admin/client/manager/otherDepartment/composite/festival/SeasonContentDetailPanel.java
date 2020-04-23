package kr.or.visitkorea.admin.client.manager.otherDepartment.composite.festival;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Float;
import com.google.gwt.dom.client.Style.FontWeight;
import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONString;

import gwt.material.design.client.constants.Color;
import gwt.material.design.client.constants.IconType;
import gwt.material.design.client.constants.TextAlign;
import gwt.material.design.client.ui.MaterialLink;
import gwt.material.design.client.ui.MaterialToast;
import gwt.material.design.jquery.client.api.Functions.Func3;
import gwt.material.design.jscore.client.api.JSON;
import gwt.material.design.jscore.client.api.JsObject;
import kr.or.visitkorea.admin.client.application.Console;
import kr.or.visitkorea.admin.client.application.Registry;
import kr.or.visitkorea.admin.client.manager.VisitKoreaBusiness;
import kr.or.visitkorea.admin.client.manager.otherDepartment.OtherDepartmentMainApplication;
import kr.or.visitkorea.admin.client.manager.otherDepartment.composite.AbtractOtherDepartmentMainContents;
import kr.or.visitkorea.admin.client.manager.otherDepartment.composite.OtherDepartmentMainEditor;
import kr.or.visitkorea.admin.client.manager.otherDepartment.widgets.AreaComponent;
import kr.or.visitkorea.admin.client.manager.otherDepartment.widgets.SearchBodyWidget;
import kr.or.visitkorea.admin.client.manager.otherDepartment.widgets.SearchTagWidget;
import kr.or.visitkorea.admin.client.manager.otherDepartment.widgets.TagListImageCell;
import kr.or.visitkorea.admin.client.manager.otherDepartment.widgets.TagListLabelCell;
import kr.or.visitkorea.admin.client.manager.otherDepartment.widgets.TagListRow;
import kr.or.visitkorea.admin.client.manager.otherDepartment.widgets.ViewPanel;
import kr.or.visitkorea.admin.client.manager.otherDepartment.widgets.VisitKoreaListCell;
import kr.or.visitkorea.admin.client.widgets.window.MaterialExtentsWindow;

public class SeasonContentDetailPanel extends AbtractOtherDepartmentMainContents {

	private String seasonId;
	private String otdId;
	private SearchTagWidget search;
	private SearchBodyWidget searchBody;
	private ArrayList<TagListRow> listRows;
	private Map<String, Object> internalMap;
	private String bgImageName;
	private Map<String, Object> paramBackgroundImageMap;
	private MaterialExtentsWindow materialExtentsWindow;
	private JSONObject containerQueryObject;
	private ViewPanel targetView;
	private String previewUrl;

	public SeasonContentDetailPanel(MaterialExtentsWindow materialExtentsWindow, OtherDepartmentMainEditor otherDepartmentMainEditor, String otdId) {
		super(materialExtentsWindow);
		this.materialExtentsWindow = materialExtentsWindow;
		Registry.put("SeasonContentDetailPanel", this);
		this.otdId = otdId;
		initComp();
	}

	public void initComp() {
		super.init();
		this.setLayoutPosition(Position.RELATIVE);
		this.setHeight("100%");
		this.setTitle("계절별 컨텐츠 설정");
		buildContent();
	}

	private void buildContent() {

		MaterialLink headLink = this.addLink(new MaterialLink());
		headLink.setLayoutPosition(Position.ABSOLUTE);
		headLink.setTooltip("서버 반영");
		headLink.setTop(5);
		headLink.setRight(0);
		headLink.setIconType(IconType.SAVE);
		headLink.setIconColor(Color.WHITE);
		headLink.addClickHandler(event->{
			
			Map<String, Object> paramMap = new HashMap<String, Object>();
			paramMap.put("SAVE_CLICK_HANDLER", new ClickHandler() {
				
				@Override
				public void onClick(ClickEvent ev) {

					JSONArray jarray = new JSONArray();
					
					for (int i=0; i<searchBody.getRows().size(); i++) {
						TagListRow tagListRow = (TagListRow) searchBody.getRows().get(i);
						JSONObject tagListRowJSONObject = tagListRow.getJSONObject();
						
						JSONArray jsonArr =  (JSONArray) tagListRow.get("SELECTED_DETAIL_LIST");
						JSONObject jObject = new JSONObject();
						jObject.put("tagList", tagListRowJSONObject);
						if (!tagListRow.getCell(4).getValue().equals("자동")) {
							jObject.put("tagContentList", jsonArr);
						}
						jarray.set(i, jObject);
					}
					
					JSONObject parameterJSON = new JSONObject();
					parameterJSON.put("cmd", new JSONString("INSERT_FESTIVAL_MAIN_SEASON"));
					parameterJSON.put("SEASON", new JSONString(search.getSeasonTitle()));
					
					if (paramBackgroundImageMap != null && 
							paramBackgroundImageMap.containsKey("BG_IMG_NAME")) {
						
						parameterJSON.put("BG_IMG_NAME", 
								new JSONString(paramBackgroundImageMap.get("BG_IMG_NAME").toString()));
						
					}
					
					if (paramBackgroundImageMap != null && 
							paramBackgroundImageMap.containsKey("BG_SAVE_NAME") && 
							paramBackgroundImageMap.get("BG_SAVE_NAME") != null) {
						
						parameterJSON.put(
								"BG_SAVE_NAME", 
								new JSONString(paramBackgroundImageMap.get("BG_SAVE_NAME").toString())
						);
					}
					
					parameterJSON.put("SEASON_CONTENTS_ALL", jarray);
					
					JSONObject result = (JSONObject)targetView.getData("REC_CONTAINER");
					
					if (result.get("season").isObject().get("IMG_ID") != null) {
						String imgId = result.get("season").isObject().get("IMG_ID").isString().stringValue();
						parameterJSON.put(
								"TMP_IMG_ID", 
								new JSONString(imgId)
						);
					}
					
					if (result.get("season").isObject().get("TMP_IMG_URL") != null) {
						String tmpImgUrl = result.get("season").isObject().get("TMP_IMG_URL").isString().stringValue();
						parameterJSON.put(
								"TMP_IMG_URL", 
								new JSONString(tmpImgUrl)
						);
					}

					
					Console.log("parameterJSON :: " + parameterJSON);
					
					VisitKoreaBusiness.post("call", parameterJSON.toString(), new Func3<Object, String, Object>() {
						
						@Override
						public void call(Object param1, String param2, Object param3) {
							JSONObject resultObj = (JSONObject) JSONParser.parseStrict(JSON.stringify((JsObject)param1));
							JSONObject headerObj = (JSONObject) resultObj.get("header");
							String processResult = headerObj.get("process").isString().toString();
							processResult = processResult.replaceAll("\"", "");
							
							if (processResult.equals("success")) {
								MaterialToast.fireToast("성공적으로 저장되었습니다.", 5000);
							}
						}
					});
				}
			});
			
			getWindow().openDialog(OtherDepartmentMainApplication.SAVE_CONTENTS, paramMap, 600);
			saveContent(event);
		});
		
		search = new SearchTagWidget(this.otdId, getWindow());
		search.setPadding(20);
		
		searchBody = new SearchBodyWidget();
		searchBody.setWindow(getWindow());
		searchBody.setMarginLeft(30);
		searchBody.setWidth("820px");
		searchBody.setHeight("390px");
		
		MaterialLink upLink = new MaterialLink(IconType.KEYBOARD_ARROW_UP);
		MaterialLink dnLink = new MaterialLink(IconType.KEYBOARD_ARROW_DOWN);
		MaterialLink rmLink = new MaterialLink(IconType.REMOVE);
		MaterialLink imgLink = new MaterialLink(IconType.IMAGE);
		
		upLink.setTooltip("위로 이동");
		dnLink.setTooltip("아래로 이동");
		rmLink.setTooltip("선택 삭제");
		imgLink.setTooltip("이미지 설정");
		
		searchBody.addLink(dnLink, Float.RIGHT);
		searchBody.addLink(upLink, Float.RIGHT);
		searchBody.addLink(imgLink, Float.RIGHT);
		searchBody.addLink(rmLink, Float.LEFT);

		search.setBody(searchBody);
		searchBody.setSearch(search);
		
		listRows = new ArrayList<TagListRow>();
		
		searchBody.addRowAll(listRows);
		
		this.add(search);
		this.add(searchBody);
		
		searchBody.setHeaderTitleVisible(true);
		searchBody.setHeaderTitle(new String[] {"태그명", "컨텐츠 보유 수", "노출 수", "노출 설정"});
		searchBody.setHeaderTitleWidths(new String[] {"300px", "200px", "150px", "130px"});
		searchBody.render();
		
		rmLink.addClickHandler(event->{
			int selectedIndex = searchBody.getListBody().getWidgetIndex(searchBody.getSelectedPanel());
			JSONObject recObj = (JSONObject) this.targetView.getData("REC_CONTAINER");
			JSONArray tempArr = recObj.get("seasonTagsArr").isArray();
			JSONArray recArr = new JSONArray();
			
			int idx = 0;
			for (int i=0; i<tempArr.size(); i++) {
				if (i == selectedIndex) {
					continue;
				}
				recArr.set(idx, tempArr.get(i));
				idx++;
			}
			
			recObj.put("seasonTagsArr", recArr);
			searchBody.removeSelectedPanel();
		});
		
		upLink.addClickHandler(event->{
			searchBody.selectedPanelMoveUp();
		});
		
		dnLink.addClickHandler(event->{
			searchBody.selectedPanelMoveDown();
		});
		
		imgLink.addClickHandler(event->{
			
			paramBackgroundImageMap = new HashMap<String, Object>();
			paramBackgroundImageMap.put("CONTENT_DETAIL", this);
			getWindow().openDialog(OtherDepartmentMainApplication.SEASON_IMAGES, paramBackgroundImageMap, 560);
			
		});

	}
	
	private void saveContent(ClickEvent event) {
	}

	private void searchTag(String value) {
	}
	
	public void setSeasonTitle(String seasonTitle) {
		this.search.setSeasonTile(seasonTitle);
		searchBody.removeAll();
	}
	
	public void setViewValue(Map<String, Object>  viewValue) {
		this.internalMap = viewValue;
	}
	
	@Override
	public void loading(boolean loadFlag) {
		super.loading(loadFlag);
	}

	@Override
	public void setReadOnly(boolean readFlag) {}

	public void setAreaComponent(AreaComponent aac) {
	}

	@Override
	public void loadData() {
		
	}
	
	public String getBgImageName() {
		return this.bgImageName;
	}
	
	public void setSeasonId(String seasonId) {
		this.seasonId = seasonId;
	}
	
	public String getSeasonId() {
		return this.seasonId;
	}
	
	public void setVeiw(ViewPanel targetViewPanel) {
		this.targetView = targetViewPanel;
		search.setView(this.targetView);
	}

	public ViewPanel getView() {
		return this.targetView;
	}
	
	public void redrawList() {
		
		JSONObject result = (JSONObject)this.targetView.getData("REC_CONTAINER");
		
		String imgServer = (String) Registry.get("image.server");
		
		Console.log("redrawList.targetView.REC_CONTAINER :: "+result);
		
		this.search.setData("RESULT", result);
		this.search.setData("SEASON_ID", this.getSeasonId());
		this.searchBody.setMapValue("RESULT", result);
		this.searchBody.setMapValue("SEASON_ID", this.getSeasonId());
		this.searchBody.setMapValue("IMAGE_URL", result.get("season").isObject().get("IMG_ID"));
		
		if (result.get("season").isObject().containsKey("TMP_IMG_URL")) {
			this.previewUrl = result.get("season").isObject().get("TMP_IMG_URL").isString().stringValue();
		}else if (result.get("season").isObject().containsKey("IMG_ID")) {
			this.previewUrl = (String) Registry.get("image.server") + "/img/call?cmd=VIEW&id=" + result.get("season").isObject().get("IMG_ID").isString().stringValue();
		}else {
			this.previewUrl = GWT.getHostPageBaseURL() + "images/default-image.png";
		}
		
		JSONArray seasonTagsArr = result.get("seasonTagsArr").isArray();
		
		if (seasonTagsArr != null && seasonTagsArr.size() > 0) {
			
			listRows.clear();
			
			for (int i=0; i<seasonTagsArr.size(); i++) {
				
				JSONArray seasonTagsContentsTargetItem = new JSONArray();
				JSONObject seasonTagsObj = seasonTagsArr.get(i).isObject();
				
				JSONArray seasonTagsContentsObj = seasonTagsObj.get("CHILDREN").isArray();
				if (seasonTagsContentsObj != null && seasonTagsContentsObj.size() > 0) {
					for (int j=0; j<seasonTagsContentsObj.size(); j++) {
						JSONObject seasonTagsContentsObjItem = seasonTagsContentsObj.get(j).isObject();
						if (seasonTagsObj.get("SAT_ID").isString().stringValue() != seasonTagsContentsObj.get(j).isObject().get("SAT_ID").isString().stringValue())
							continue;
						
						JSONObject newRecordObject = new JSONObject();
						newRecordObject.put("TITLE", seasonTagsContentsObjItem.get("TITLE").isString());
						newRecordObject.put("AREA", seasonTagsContentsObjItem.get("AREA"));
						newRecordObject.put("SIGUGUN", seasonTagsContentsObjItem.get("SIGUGUN"));
						newRecordObject.put("ADDRESS", new JSONString(seasonTagsContentsObjItem.get("AREA").isString().stringValue() + " " + seasonTagsContentsObjItem.get("SIGUGUN").isString().stringValue()));
						newRecordObject.put("START_DATE", seasonTagsContentsObjItem.get("START_DATE"));
						newRecordObject.put("END_DATE", seasonTagsContentsObjItem.get("END_DATE"));
						newRecordObject.put("DATES", new JSONString(seasonTagsContentsObjItem.get("START_DATE").isString().stringValue().substring(4) + " ~ " + seasonTagsContentsObjItem.get("END_DATE").isString().stringValue().substring(4)));
						newRecordObject.put("COT_ID", seasonTagsContentsObjItem.get("COT_ID").isString());
						newRecordObject.put("SAT_ID", seasonTagsContentsObjItem.get("SAT_ID").isString());
						
						seasonTagsContentsTargetItem.set(j, newRecordObject);
					}
				}
				
				List<VisitKoreaListCell> cellArray = new ArrayList<VisitKoreaListCell>();

				if (seasonTagsObj.containsKey("TMP_IMG_URL")) {
					String urlValue = seasonTagsObj.get("TMP_IMG_URL").isString().stringValue();
					cellArray.add(new TagListImageCell(urlValue, Float.LEFT, "30px",  "50", 50, FontWeight.BOLDER, false));
				}else if (seasonTagsObj.containsKey("IMG_ID")) {
					String urlValue = imgServer + "/img/call?cmd=VIEW&id=" + seasonTagsObj.get("IMG_ID").isString().stringValue();
					cellArray.add(new TagListImageCell(urlValue, Float.LEFT, "30px",  "50", 50, FontWeight.BOLDER, false));
				}else {
					cellArray.add(new TagListImageCell(GWT.getHostPageBaseURL() + "images/small_log.png", Float.LEFT, "30px",  "50", 50, FontWeight.BOLDER, false));
				}
				
				cellArray.add(new TagListLabelCell(seasonTagsObj.get("TAG_NAME").isString().stringValue(), Float.LEFT, "269px",  "50", 50, FontWeight.BOLDER, true, TextAlign.LEFT));
				cellArray.add(new TagListLabelCell(String.valueOf((int)seasonTagsObj.get("TAGS_CNT").isNumber().doubleValue()), Float.LEFT, "200px",  "50", 50, FontWeight.BOLDER, true, TextAlign.CENTER));
				if ((int) seasonTagsObj.get("IS_AUTO").isNumber().doubleValue() == 0)
					cellArray.add(new TagListLabelCell("10", Float.LEFT, "150px",  "50", 50, FontWeight.BOLDER, true, TextAlign.CENTER));
				else
					cellArray.add(new TagListLabelCell(String.valueOf(seasonTagsContentsObj.size()), Float.LEFT, "150px",  "50", 50, FontWeight.BOLDER, true, TextAlign.CENTER));
				
				cellArray.add(new TagListLabelCell((int)seasonTagsObj.get("IS_AUTO").isNumber().doubleValue() == 0 ? "자동" : "수동", Float.LEFT, "130px",  "50", 50, FontWeight.BOLDER, false, TextAlign.CENTER));
				
				TagListRow tagListRow = new TagListRow(cellArray);
				tagListRow.setTagName(seasonTagsObj.get("TAG_NAME").isString().stringValue());
				tagListRow.setCount((int)seasonTagsObj.get("TAGS_CNT").isNumber().doubleValue());
				tagListRow.setIsAutoCount(Integer.parseInt("1"));
				tagListRow.put("TAG_ID", seasonTagsObj.get("TAG_ID").isString().stringValue());
				tagListRow.setIsAuto((int) seasonTagsObj.get("IS_AUTO").isNumber().doubleValue());
				tagListRow.put("SELECTED_DETAIL_LIST", seasonTagsContentsTargetItem);
				tagListRow.put("SEASON_TAG_OBJ", seasonTagsObj);
				tagListRow.put("INDEX", i);
				tagListRow.put("RESULT", result);
				tagListRow.put("VIEW_PANEL", this.targetView);
				
				tagListRow.addDoubleClickHandler(ev->{
					
					Map<String, Object> selectedConditionMap = new HashMap<String, Object>();
					selectedConditionMap.put("SELECTED_CONDITION", tagListRow);
					selectedConditionMap.put("VIEW_PANEL", getView());
					
					materialExtentsWindow.openDialog(OtherDepartmentMainApplication.SEASON_CONTENT, selectedConditionMap, 1100);
					
				});
				
				listRows.add(tagListRow);
				searchBody.addRowAll(listRows);
			}
			
			
		}
	}

	public void setSeasonImageUrl(String previewUrl) {
		this.previewUrl = previewUrl;
		String imgId = getID(previewUrl);
		JSONObject result = (JSONObject)this.targetView.getData("REC_CONTAINER");
		result.get("season").isObject().put("IMG_ID", new JSONString(imgId));
		result.get("season").isObject().put("TMP_IMG_URL", new JSONString(previewUrl));
		this.targetView.setData("IMAGE_URL", previewUrl);
	}
	
	private String getID(String targetUrl) {
		String fileNameStirng = targetUrl.replaceAll(Registry.get("image.server") + "/img/call?cmd=TEMP_VIEW&name=", "");
		
		return fileNameStirng.substring(fileNameStirng.lastIndexOf("=")+1, fileNameStirng.lastIndexOf("."));
		
	}

	public String getSeasonImageUrl() {
		return this.previewUrl;
	}
}