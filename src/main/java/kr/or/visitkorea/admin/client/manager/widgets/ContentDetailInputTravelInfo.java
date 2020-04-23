package kr.or.visitkorea.admin.client.manager.widgets;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gwt.dom.client.Style;
import com.google.gwt.dom.client.Style.Float;
import com.google.gwt.dom.client.Style.FontWeight;
import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.dom.client.Style.Visibility;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONNumber;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONString;

import gwt.material.design.client.constants.Color;
import gwt.material.design.client.constants.Display;
import gwt.material.design.client.constants.IconType;
import gwt.material.design.client.constants.TextAlign;
import gwt.material.design.client.ui.MaterialIcon;
import gwt.material.design.client.ui.MaterialLabel;
import gwt.material.design.client.ui.MaterialLink;
import gwt.material.design.client.ui.MaterialPanel;
import gwt.material.design.jscore.client.api.JSON;
import gwt.material.design.jscore.client.api.JsObject;
import kr.or.visitkorea.admin.client.manager.VisitKoreaBusiness;
import kr.or.visitkorea.admin.client.manager.contents.recommand.RecommApplication;
import kr.or.visitkorea.admin.client.manager.contents.recommand.RecommContentsTree;
import kr.or.visitkorea.admin.client.manager.otherDepartment.widgets.SearchBodyWidget;
import kr.or.visitkorea.admin.client.manager.otherDepartment.widgets.TagListLabelCell;
import kr.or.visitkorea.admin.client.manager.otherDepartment.widgets.TagListRow;
import kr.or.visitkorea.admin.client.manager.otherDepartment.widgets.VisitKoreaListCell;
import kr.or.visitkorea.admin.client.widgets.window.MaterialExtentsWindow;

public class ContentDetailInputTravelInfo extends ContentDetailItem {
	private boolean isEditMode;
	private MaterialIcon iconDelete;
	private boolean isSave = false;
	private MaterialExtentsWindow window;
	private SearchBodyWidget searchBody;
	
	public ContentDetailInputTravelInfo(ContentTreeItem treeItem, boolean displayLabel, String cotId,
			boolean displayDelete, boolean ordering) {
		super(treeItem, displayLabel, cotId, displayDelete, ordering);
		init();
	}

	public ContentDetailInputTravelInfo(ContentTreeItem treeItem, boolean displayLabel, String cotId,
			boolean displayDelete, boolean ordering, MaterialExtentsWindow window) {
		super(treeItem, displayLabel, cotId, displayDelete, ordering);
		this.window = window;
		init();
	}
	
	@Override
	protected void init() {
		this.isRenderMode = true;
		
		if (isDisplayLabel) {
			this.icon = new MaterialIcon(IconType.ARCHIVE);
			this.icon.setFloat(com.google.gwt.dom.client.Style.Float.LEFT);
		
			this.title = new MaterialLabel(titleName);
			this.title.setTextAlign(TextAlign.CENTER);
			this.title.setFontSize("1.2em");
			this.title.setFontWeight(FontWeight.BOLD);

			this.add(this.icon);
			this.add(this.title);
		}	

		this.content = new MaterialPanel();
		this.content.setStyle("clear:both;");
		this.content.setFloat(com.google.gwt.dom.client.Style.Float.NONE);
		this.content.setPaddingTop(1);

		if (!isDisplayLabel) {
			
			this.contentPreview = new MaterialPanel();
			this.contentPreview.setVisible(true);
			this.contentPreview.setLayoutPosition(Position.RELATIVE);
			this.contentPreview.setHeight("100%");
			
			MaterialLabel previewTitle = new MaterialLabel(titleName);
			previewTitle.setTextAlign(TextAlign.CENTER);
			previewTitle.setFontSize("1.1em");
			previewTitle.setPaddingLeft(10);
			previewTitle.setBackgroundColor(Color.GREY_LIGHTEN_2);
			
			MaterialPanel dispContent = new MaterialPanel();
			dispContent.setWidth("100%");

			MaterialPanel dispContentBox = new MaterialPanel();
			dispContentBox.setLayoutPosition(Position.RELATIVE);
			dispContentBox.setBorder("1px solid #e0e0e0");
			dispContentBox.setHeight("100%");
			dispContentBox.setWidth("810px");
			dispContentBox.setMargin(15);
			
			MaterialPanel menuContent = new MaterialPanel();
			menuContent.setLayoutPosition(Position.ABSOLUTE);
			menuContent.setTop(0);
			menuContent.setRight(0);
			menuContent.setWidth("100%");
			
			searchBody = new SearchBodyWidget();
			searchBody.setWindow(this.window);
			searchBody.setLayoutPosition(Position.RELATIVE);
			searchBody.setWidth("100%");
			searchBody.setHeight("250px");
			searchBody.setHeaderTitleVisible(true);
			searchBody.setHeaderTitle(new String[] {"구분", "컨텐츠", "주소", "전화번호", "홈페이지"});
			searchBody.setHeaderTitleWidths(new String[] {"70px", "190px", "190px", "140px", "216px"});
			searchBody.render();
			
			MaterialLink linkContnetsAdd = new MaterialLink(IconType.ADD);
			linkContnetsAdd.setTooltip("컨텐츠 추가");
			linkContnetsAdd.setTextAlign(TextAlign.CENTER);
			linkContnetsAdd.setVisibility(Visibility.HIDDEN);
			linkContnetsAdd.addClickHandler(event -> {
				HashMap<String, Object> params = new HashMap<>();
				params.put("mode", "CONTENTS");
				params.put("host", this);
				params.put("ACI_ID", this.item.getInternalReferences().get("ACI_ID"));
				this.window.openDialog(RecommApplication.TRAVEL_INFO_CONTENT, params, 700);
			});
			
			MaterialLink linkSelfAdd = new MaterialLink(IconType.CLOUD);
			linkSelfAdd.setTooltip("직접 추가");
			linkSelfAdd.setTextAlign(TextAlign.CENTER);
			linkSelfAdd.setVisibility(Visibility.HIDDEN);
			linkSelfAdd.addClickHandler(event -> {
				HashMap<String, Object> params = new HashMap<>();
				params.put("mode", "SELF");
				params.put("host", this);
				params.put("ACI_ID", this.item.getInternalReferences().get("ACI_ID"));
				this.window.openDialog(RecommApplication.TRAVEL_INFO_CONTENT, params, 700, 550);
			});
			
			MaterialLink linkOrderUp = new MaterialLink(IconType.ARROW_UPWARD);
			linkOrderUp.setTooltip("위로 이동");
			linkOrderUp.setTextAlign(TextAlign.CENTER);
			linkOrderUp.setVisibility(Visibility.HIDDEN);
			linkOrderUp.addClickHandler(event -> {
				if (this.searchBody.getSelectedRow() == null) return;
				TagListRow selectedRow = (TagListRow) this.searchBody.getSelectedRow();
				int index = this.searchBody.getListBody().getWidgetIndex(selectedRow);
				
				if (index == 0)
					return;
				
				Collections.swap(this.searchBody.getRows(), index, index - 1);
				this.searchBody.selectedPanelMoveUp();
			});
			
			MaterialLink linkOrderDown = new MaterialLink(IconType.ARROW_DOWNWARD);
			linkOrderDown.setTooltip("아래로 이동");
			linkOrderDown.setTextAlign(TextAlign.CENTER);
			linkOrderDown.setVisibility(Visibility.HIDDEN);
			linkOrderDown.addClickHandler(event -> {
				if (this.searchBody.getSelectedRow() == null) return;
				TagListRow selectedRow = (TagListRow) this.searchBody.getSelectedRow();
				int index = this.searchBody.getListBody().getWidgetIndex(selectedRow);
				
				if (index == this.searchBody.getRows().size() - 1)
					return;
				
				Collections.swap(this.searchBody.getRows(), index, index + 1);
				this.searchBody.selectedPanelMoveDown();
			});
			
			MaterialLink linkRemove = new MaterialLink(IconType.REMOVE);
			linkRemove.setTooltip("여행정보 삭제");
			linkRemove.setTextAlign(TextAlign.CENTER);
			linkRemove.setVisibility(Visibility.HIDDEN);
			linkRemove.addClickHandler(event -> {
				TagListRow selectedRow = (TagListRow) this.searchBody.getSelectedRow();
				JSONObject obj = (JSONObject) selectedRow.get("TRAVEL_INFO");
				obj.put("STATUS", new JSONNumber(-1));
				
				selectedRow.setDisplay(Display.NONE);
			});
			
			
			searchBody.addLink(linkContnetsAdd, Float.LEFT);
			searchBody.addLink(linkSelfAdd, Float.LEFT);
			searchBody.addLink(linkRemove, Float.RIGHT);
			searchBody.addLink(linkOrderDown, Float.RIGHT);
			searchBody.addLink(linkOrderUp, Float.RIGHT);
			
			dispContentBox.add(searchBody); 
			dispContentBox.add(dispContent);
			contentPreview.add(previewTitle);
			contentPreview.add(dispContentBox);
			contentPreview.add(menuContent);

			MaterialIcon iconEdit = new MaterialIcon(IconType.EDIT);
			MaterialIcon iconSave = new MaterialIcon(IconType.SAVE);

			iconSave.setVisible(false);
			
			iconEdit.setFloat(Style.Float.RIGHT);
			iconEdit.setMarginLeft(0);
			iconEdit.addClickHandler(event -> {
				isEditMode = true;
				iconEdit.setVisible(false);
				iconDelete.setVisible(false);
				iconSave.setVisible(true);
				this.searchBody.getMenuArea().getChildrenList().forEach(item -> {
					if (item instanceof MaterialLink)
						((MaterialLink) item).setVisibility(Visibility.VISIBLE);
				});
			});
			
			iconSave.setFloat(Style.Float.RIGHT);
			iconSave.setMarginLeft(0);
			iconSave.addClickHandler(event -> {
				isEditMode = false;
				iconEdit.setVisible(true);
				iconDelete.setVisible(true);
				iconSave.setVisible(false);
				this.searchBody.getMenuArea().getChildrenList().forEach(item -> {
					if (item instanceof MaterialLink)
						((MaterialLink) item).setVisibility(Visibility.HIDDEN);
				});
			});
			
			if (this.displayDelete) {
				
				iconDelete = new MaterialIcon(IconType.REMOVE);
				iconDelete.setFloat(Style.Float.RIGHT);
				iconDelete.setMarginLeft(0);
				iconDelete.addClickHandler(event->{
					if (this.isRenderMode) {
						
						Object windowObj = this.item.getInternalReferences().get("WINDOW");
						Object diagId = this.item.getInternalReferences().get("DIALOG_ID");
						
						if (windowObj != null && diagId != null) {
							
							MaterialExtentsWindow win = (MaterialExtentsWindow) windowObj;
							String diagIdStr = (String)diagId;
							
							Map<String, Object> paramterMap = new HashMap<String, Object>();
							paramterMap.put("ITEM", this);
							paramterMap.put("CONTENT_TREE", this.item.getInternalReferences().get("WINDOW"));
							
							win.openDialog(diagIdStr, paramterMap, 800);
							
						}

					}
				});
				
				menuContent.add(iconDelete);
			}
			
			addOrdering(menuContent);
			
			menuContent.add(iconSave);
			menuContent.add(iconEdit);
			renderComponent(dispContent);

			loadData();
			this.content.add(this.contentPreview);
		}
		
		this.add(this.content);
	}

	public void loadData() {
		JSONObject parameterJSON = new JSONObject();
		parameterJSON.put("cmd", new JSONString("SELECT_ARTICLE_TRAVEL_INFO"));
		parameterJSON.put("aciId", new JSONString(this.item.getInternalReferences().get("ACI_ID").toString()));
		VisitKoreaBusiness.post("call", parameterJSON.toString(), (param1, param2, param3) -> {
			JSONObject resultObj = JSONParser.parseStrict(JSON.stringify((JsObject) param1)).isObject();
			String result = resultObj.get("header").isObject().get("process").isString().stringValue();
			
			if (result.equals("success")) {
				JSONArray resultArr = resultObj.get("body").isObject().get("result").isArray();
				if (this.item.getInternalReferences().containsKey("TRAVEL_ARRAY")) {
					renderTable((JSONArray) this.item.getInternalReferences().get("TRAVEL_ARRAY"));
				} else {
					renderTable(resultArr);
				}
			}
		});
	}
	
	public SearchBodyWidget getSearchBody() {
		return searchBody;
	}

	public void appendTableRow(JSONObject obj, int index) {
		List<VisitKoreaListCell> listCell = new ArrayList<VisitKoreaListCell>();
		listCell.add(new TagListLabelCell(obj.containsKey("COT_ID") ? "컨텐츠" : "직접"
				, Float.LEFT, "70px", "40", 40, FontWeight.BOLDER, true, TextAlign.CENTER));
		listCell.add(new TagListLabelCell(obj.containsKey("TITLE") ? obj.get("TITLE").isString().stringValue() : null
				, Float.LEFT, "190px", "40", 40, FontWeight.BOLDER, true, TextAlign.CENTER));
		listCell.add(new TagListLabelCell(obj.containsKey("ADDRESS") ? obj.get("ADDRESS").isString().stringValue() : null
				, Float.LEFT, "190px", "40", 40, FontWeight.BOLDER, true, TextAlign.CENTER));
		listCell.add(new TagListLabelCell(obj.containsKey("TEL") ? obj.get("TEL").isString().stringValue() : null
				, Float.LEFT, "140px", "40", 40, FontWeight.BOLDER, true, TextAlign.CENTER));
		listCell.add(new TagListLabelCell(obj.containsKey("HOMEPAGE") ? obj.get("HOMEPAGE").isString().stringValue() : null
				, Float.LEFT, "216px", "40", 40, FontWeight.BOLDER, true, TextAlign.CENTER));
		
		obj.put("IDX", new JSONNumber(index));
		
		TagListRow tagRow = new TagListRow(listCell);
		tagRow.setHeight("40px");
		tagRow.setTagName(IDUtil.uuid());
		tagRow.put("INDEX", index);
		tagRow.put("TRAVEL_INFO", obj);
		tagRow.addDoubleClickHandler(event -> {
			if (!isEditMode)
				return;
			HashMap<String, Object> params = new HashMap<>();
			params.put("host", this);
			params.put("ACI_ID", this.item.getInternalReferences().get("ACI_ID"));
			params.put("OBJ", tagRow.get("TRAVEL_INFO"));
			if (obj.containsKey("COT_ID")) {
				params.put("mode", "CONTENTS_MODIFY");
				this.window.openDialog(RecommApplication.TRAVEL_INFO_CONTENT, params, 700);
			} else {
				params.put("mode", "SELF_MODIFY");
				this.window.openDialog(RecommApplication.TRAVEL_INFO_CONTENT, params, 700, 500);
			}
		});
		
		if (obj.get("STATUS").isNumber().doubleValue() == -1) 
			tagRow.setDisplay(Display.NONE);
		
		if (!this.item.getInternalReferences().containsKey("TRAVEL_ARRAY")) {
			this.item.getInternalReferences().put("TRAVEL_ARRAY", new JSONArray());
		}
		
		JSONArray tempArray = (JSONArray) this.item.getInternalReferences().get("TRAVEL_ARRAY");
		tempArray.set(tempArray.size(), obj);
		this.item.getInternalReferences().put("TRAVEL_ARRAY", tempArray);
		
		searchBody.addRowForLastIndex(tagRow);
	}
	
	public void renderTable(JSONArray array) {
		this.searchBody.removeAll();
		this.item.getInternalReferences().put("TRAVEL_TABLE", this.searchBody);
		this.item.getInternalReferences().remove("TRAVEL_ARRAY");
		int size = array.size();
		for (int i = 0; i < size; i++) {
			JSONObject obj = array.get(i).isObject();
			
			if (obj.containsKey("STATUS"))
				obj.put("STATUS", new JSONNumber(obj.get("STATUS").isNumber().doubleValue()));
			else
				obj.put("STATUS", new JSONNumber(0));
			
			appendTableRow(obj, i);
		}
	}
	
	@Override
	protected void renderComponent(MaterialPanel dispContent) {
	}

	@Override
	protected void createComponent(MaterialPanel tgrPanel) {
	}

	@Override
	public void deleteData() {
		Object contentTree = item.getInternalReferences().get("CONTENT_TREE");
		
		item.removeFromParent();
		getParent().removeFromParent();
		
		if (contentTree != null && contentTree instanceof RecommContentsTree) {
			RecommContentsTree rct = (RecommContentsTree) contentTree;
			rct.reOrderItems();
			rct.renderDetail();
		}
	}
	
}
