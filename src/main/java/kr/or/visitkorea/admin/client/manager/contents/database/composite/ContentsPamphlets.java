package kr.or.visitkorea.admin.client.manager.contents.database.composite;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.google.gwt.dom.client.Style.Float;
import com.google.gwt.dom.client.Style.FontWeight;
import com.google.gwt.dom.client.Style.Overflow;
import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONNumber;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONString;

import gwt.material.design.client.constants.Color;
import gwt.material.design.client.constants.Display;
import gwt.material.design.client.constants.IconType;
import gwt.material.design.client.constants.TextAlign;
import gwt.material.design.client.ui.MaterialColumn;
import gwt.material.design.client.ui.MaterialIcon;
import gwt.material.design.client.ui.MaterialLabel;
import gwt.material.design.client.ui.MaterialLink;
import gwt.material.design.client.ui.MaterialPanel;
import gwt.material.design.client.ui.MaterialRow;
import gwt.material.design.client.ui.MaterialTextBox;
import gwt.material.design.jscore.client.api.JSON;
import gwt.material.design.jscore.client.api.JsObject;
import kr.or.visitkorea.admin.client.manager.VisitKoreaBusiness;
import kr.or.visitkorea.admin.client.manager.contents.database.widget.PamphletImage;
import kr.or.visitkorea.admin.client.manager.contents.database.widget.PamphletItem;
import kr.or.visitkorea.admin.client.manager.otherDepartment.widgets.SearchBodyWidget;
import kr.or.visitkorea.admin.client.manager.otherDepartment.widgets.TagListLabelCell;
import kr.or.visitkorea.admin.client.manager.otherDepartment.widgets.TagListRow;
import kr.or.visitkorea.admin.client.manager.otherDepartment.widgets.VisitKoreaListCell;
import kr.or.visitkorea.admin.client.manager.widgets.IDUtil;
import kr.or.visitkorea.admin.client.manager.widgets.SelectionPanel;
import kr.or.visitkorea.admin.client.widgets.window.MaterialExtentsWindow;

public class ContentsPamphlets extends AbtractContents {

	private MaterialTextBox title;
	private MaterialTextBox textColor;
	private SelectionPanel viewType;
	private MaterialPanel leftPanel;
	private MaterialPanel rightPanel;
	private MaterialIcon saveIcon;
	private MaterialIcon addIcon;
	private MaterialRow imgRow;
	private SearchBodyWidget searchBody;
	private PamphletItem selectedItem;
	
	public PamphletItem getSelectedItem() {
		return selectedItem;
	}

	public SearchBodyWidget getSearchBody() {
		return searchBody;
	}

	public ContentsPamphlets(MaterialExtentsWindow materialExtentsWindow) {
		super(materialExtentsWindow);
	}

	@Override
	public void init() {
		super.init();
		this.setTitle("팜플렛 관리");
		addIcon = showAddIconAndGetIcon();
		addIcon.addClickHandler(event -> {
			PamphletItem item = new PamphletItem();
			item.setPplId(IDUtil.uuid());
			item.setIdx(this.searchBody.getRows().size());
			addPamphlet(item);
		});
		
		saveIcon = showSaveIconAndGetIcon();
		saveIcon.addClickHandler(event -> {
			JSONArray pamphletArr = new JSONArray();
			this.searchBody.getRows().forEach(row -> {
				PamphletItem item = (PamphletItem) ((TagListRow)row).get("PAMPHLET");
				JSONObject obj = new JSONObject();
				obj.put("pplId", new JSONString(item.getPplId()));
				obj.put("cotId", new JSONString(this.getCotId()));
				obj.put("view", new JSONNumber(item.getViewType()));;
				obj.put("idx", new JSONNumber(item.getIdx()));
				if (item.getTitle() != null)
					obj.put("title", new JSONString(item.getTitle()));
				if (item.getFontColor() != null)
					obj.put("textColor", new JSONString(item.getFontColor()));
				
				JSONArray imgArr = new JSONArray();
				item.getImageList().forEach(image -> {
					if (image.getImgId() != null) {
						JSONObject imgObj = new JSONObject();
						imgObj.put("ppcId", new JSONString(image.getPpcId()));
						imgObj.put("pplId", new JSONString(image.getPplId()));
						imgObj.put("imgId", new JSONString(image.getImgId()));
						imgObj.put("idx", new JSONNumber(image.getIdx()));
						imgObj.put("imgPath", new JSONString(image.getImgPath()));
						if (image.getImgDesc() != null)
							imgObj.put("imgDesc", new JSONString(image.getImgDesc()));
						imgArr.set(imgArr.size(), imgObj);
					}
				});
				obj.put("contents", imgArr);
				pamphletArr.set(pamphletArr.size(), obj);
			});
			
			JSONObject parameterJSON = new JSONObject();
			parameterJSON.put("cmd", new JSONString("INSERT_PAMPHLET"));
			parameterJSON.put("cotId", new JSONString(this.getCotId()));
			parameterJSON.put("pamphletList", pamphletArr);
			VisitKoreaBusiness.post("call", parameterJSON.toString(), (param1, param2, param3) -> {
				JSONObject resultObj = JSONParser.parseStrict(JSON.stringify((JsObject) param1)).isObject();
				String process = resultObj.get("header").isObject().get("process").isString().stringValue();
				
				if (process.equals("success")) {
					getWindow().alert("수정사항이 반영되었습니다.");
				} else {
					
				}
			});
		});
		
		buildContent();
	}

	private void buildContent() {
		MaterialRow row = new MaterialRow();
		row.setMargin(0);
		row.setHeight("94.4%");
		this.add(row);
		
		searchBody = addSearchBody(row, "s6", 10);
		rightPanel = addPanel(row, "s6", 10);

		MaterialLink upLink = new MaterialLink(IconType.KEYBOARD_ARROW_UP);
		MaterialLink dnLink = new MaterialLink(IconType.KEYBOARD_ARROW_DOWN);
		MaterialLink rmLink = new MaterialLink(IconType.REMOVE);
		
		upLink.setTooltip("위로 이동");
		upLink.addClickHandler(event -> {
			TagListRow tagRow = (TagListRow) this.searchBody.getSelectedRow();
			PamphletItem item = (PamphletItem) tagRow.get("PAMPHLET");
			if (item.getIdx() == 0) 
				return;
			TagListRow tagRow2 = (TagListRow) this.searchBody.getRows().get(item.getIdx() - 1);
			PamphletItem item2 = (PamphletItem) tagRow2.get("PAMPHLET");
			
			item.setIdx(item.getIdx() - 1);
			item2.setIdx(item2.getIdx() + 1);
			
			this.searchBody.selectedPanelMoveUp();
		});
		dnLink.setTooltip("아래로 이동");
		dnLink.addClickHandler(event -> {
			TagListRow tagRow = (TagListRow) this.searchBody.getSelectedRow();
			PamphletItem item = (PamphletItem) tagRow.get("PAMPHLET");
			if (item.getIdx() == this.searchBody.getRows().size() - 1) 
				return;
			TagListRow tagRow2 = (TagListRow) this.searchBody.getRows().get(item.getIdx() + 1);
			PamphletItem item2 = (PamphletItem) tagRow2.get("PAMPHLET");
			
			item.setIdx(item.getIdx() + 1);
			item2.setIdx(item2.getIdx() - 1);
			
			this.searchBody.selectedPanelMoveDown();
		});
		rmLink.setTooltip("선택 삭제");
		rmLink.addClickHandler(event -> {
			TagListRow tagRow = (TagListRow) this.searchBody.getSelectedRow();
			PamphletItem item = (PamphletItem) tagRow.get("PAMPHLET");
			
			for (int i = item.getIdx(); i < this.searchBody.getRows().size(); i++) {
				TagListRow tRow = (TagListRow) this.searchBody.getRows().get(i);
				PamphletItem rowItem = (PamphletItem) tRow.get("PAMPHLET");
				rowItem.setIdx(rowItem.getIdx() - 1);
			}
			
			this.searchBody.removeSelectedPanel();
		});

		searchBody.addLink(dnLink, Float.RIGHT);
		searchBody.addLink(upLink, Float.RIGHT);
		searchBody.addLink(rmLink, Float.LEFT);

		buildLeftArea(leftPanel);
		buildRightArea(rightPanel);
	}
	
	private void buildLeftArea(MaterialPanel leftPanel) {
		
	}
	
	private void buildRightArea(MaterialPanel rightPanel) {
		MaterialRow row1 = addRow(rightPanel);
		addLabel(row1, "타이틀", TextAlign.CENTER, Color.GREY_LIGHTEN_3, "s4");
		this.title = addInputTextNotEvent(row1, "제목을 입력해주세요.", "s8");
		this.title.addKeyUpHandler(event -> {
			TagListRow row = (TagListRow) this.searchBody.getSelectedRow();
			MaterialLabel label = (MaterialLabel) row.getCell(0);
			label.setText(this.title.getValue());
			this.selectedItem.setTitle(this.title.getValue());
		});

		MaterialRow row2 = addRow(rightPanel);
		addLabel(row2, "글자색상", TextAlign.CENTER, Color.GREY_LIGHTEN_3, "s4");
		this.textColor = addInputTextNotEvent(row2, "글자의 색상값을 입력해주세요.", "s8");
		this.textColor.setMaxLength(7);
		this.textColor.addKeyUpHandler(event -> {
			this.selectedItem.setFontColor(this.textColor.getValue());
		});
		
		MaterialRow row3 = addRow(rightPanel);
		addLabel(row3, "노출형태", TextAlign.CENTER, Color.GREY_LIGHTEN_3, "s4");
		HashMap<String, Object> viewValueMap = new HashMap<>();
		viewValueMap.put("일반형", 0);
		viewValueMap.put("와이드형", 1);
		this.viewType = addSelectionPanelNotEvent(row3, "s8", TextAlign.LEFT, viewValueMap, 5, 5, 3, true);
		this.viewType.setSelectionOnSingleMode("일반형");
		this.viewType.addStatusChangeEvent(event -> {
			this.selectedItem.setViewType((int) this.viewType.getSelectedValue()); 
		});

		MaterialRow row4 = addRow(rightPanel);
		addLabel(row4, "노출 이미지 첨부", TextAlign.CENTER, Color.GREY_LIGHTEN_3, "s4");
		
		MaterialIcon imgAddIcon = addIcon(row4, IconType.ADD_CIRCLE, "s1");
		imgAddIcon.addClickHandler(event -> {
			PamphletImage image = new PamphletImage(this);
			this.imgRow.add(image);
		});
		
		this.imgRow = addRow(rightPanel);
		this.imgRow.setHeight("233px");
		this.imgRow.setMargin(0);
		this.imgRow.setDisplay(Display.FLEX);
		this.imgRow.getElement().getStyle().setOverflowX(Overflow.SCROLL);
		this.imgRow.getElement().getStyle().setOverflowY(Overflow.AUTO);
	}
	
	private void setupForm(PamphletItem item) {
		this.title.setValue(item.getTitle());
		this.textColor.setValue(item.getFontColor() != null ? item.getFontColor() : "");
		this.viewType.setSelectionOnSingleMode(item.getViewType() == 0 ? "일반형" : "와이드형");
		renderImgRow();
	}

	public void renderImgRow() {
		this.imgRow.clear();
		if (this.selectedItem != null) {
			this.selectedItem.getImageList().forEach(image -> {
				this.imgRow.add(image);
			});
		}
	}
	
	private void addPamphlet(PamphletItem pamphlet) {
		List<VisitKoreaListCell> cellArray = new ArrayList<VisitKoreaListCell>();
		TagListLabelCell cell = new TagListLabelCell(
				pamphlet.getTitle() != null ? pamphlet.getTitle() : "팜플렛 영역", Float.LEFT, "100%", "75", 75, FontWeight.BOLDER, true, TextAlign.CENTER);
		cell.setFontSize("25px");
		cellArray.add(cell);
		
		TagListRow tagListRow = new TagListRow(cellArray);
		tagListRow.setHeight("75px");
		tagListRow.setTagName(IDUtil.uuid());
		tagListRow.put("INDEX", pamphlet.getIdx());
		tagListRow.put("PAMPHLET", pamphlet);
		tagListRow.addClickHandler(event -> {
			this.selectedItem = pamphlet;
			setupForm(this.selectedItem);
		});
		
		this.searchBody.addRowForLastIndex(tagListRow);
	}
	
	public void loadData() {
		this.setupForm(new PamphletItem());
		this.imgRow.clear();
		this.searchBody.removeAll();
		JSONObject parameterJSON = new JSONObject();
		parameterJSON.put("cotId", new JSONString(this.getCotId()));

		invokeQuery("GET_PAMPHELET_WITH_COTID", parameterJSON, (param1, param2, param3) -> {
			JSONObject resultObj = (JSONObject) JSONParser.parseStrict(JSON.stringify((JsObject) param1));
			JSONObject headerObj = (JSONObject) resultObj.get("header");
			String process = headerObj.get("process").isString().stringValue();

			if (process.equals("success")) {
				JSONArray bodyArr = resultObj.get("body").isObject().get("result").isArray();
				
				for (int i = 0; i < bodyArr.size(); i++) {
					JSONObject obj = bodyArr.get(i).isObject();

					PamphletItem pamphlet = new PamphletItem();
					pamphlet.setPplId(obj.containsKey("PPL_ID") ? obj.get("PPL_ID").isString().stringValue() : null);
					pamphlet.setIdx(i);
					pamphlet.setTitle(obj.containsKey("TITLE") ? obj.get("TITLE").isString().stringValue() : null);
					pamphlet.setViewType(obj.containsKey("VIEW") ? (int) obj.get("VIEW").isNumber().doubleValue() : -1);
					pamphlet.setFontColor(obj.containsKey("TEXT_COLOR") ? obj.get("TEXT_COLOR").isString().stringValue() : null);

					List<PamphletImage> contentsList = new ArrayList<>();
					if (obj.containsKey("CONTENTS")) {
						JSONArray contentsArr =  obj.get("CONTENTS").isArray();
						for (int j = 0; j < contentsArr.size(); j++) {
							JSONObject contentsObj = contentsArr.get(j).isObject();
							PamphletImage image = new PamphletImage(this);
							image.setPplId(contentsObj.containsKey("PPL_ID") ? contentsObj.get("PPL_ID").isString().stringValue() : null);
							image.setPpcId(contentsObj.containsKey("PPC_ID") ? contentsObj.get("PPC_ID").isString().stringValue() : null);
							image.setImgId(contentsObj.containsKey("IMG_ID") ? contentsObj.get("IMG_ID").isString().stringValue() : null);
							image.setIdx(contentsObj.containsKey("IDX") ? (int) contentsObj.get("IDX").isNumber().doubleValue() : -1);
							image.setImgPath(contentsObj.containsKey("IMAGE_PATH") ? contentsObj.get("IMAGE_PATH").isString().stringValue() : null);
							image.setImgDesc(contentsObj.containsKey("IMAGE_DESCRIPTION") ? contentsObj.get("IMAGE_DESCRIPTION").isString().stringValue() : null);
							contentsList.add(image);
						}
					}
					pamphlet.setImageList(contentsList);
					addPamphlet(pamphlet);
				}
			}				
		});
	}
	
	private MaterialPanel addPanel(MaterialRow row, String grid, int padding) {
		MaterialPanel panel = new MaterialPanel();
		panel.setHeight("100%");
		
		MaterialColumn col = new MaterialColumn();
		col.setHeight("100%");
		col.setGrid(grid);
		col.setPadding(padding);
		col.add(panel);
		row.add(col);
		return panel;
	}
	
	private SearchBodyWidget addSearchBody(MaterialRow row, String grid, int padding) {
		SearchBodyWidget body = new SearchBodyWidget();
		body.setWindow(getWindow());
		body.setLayoutPosition(Position.RELATIVE);
		body.setWidth("100%");
		body.setHeight("100%");
		body.setHeaderTitleVisible(false);
		
		MaterialColumn col = new MaterialColumn();
		col.setHeight("100%");
		col.setGrid(grid);
		col.setPadding(10);
		col.add(body);
		row.add(col);
		return body;
	}
	
	@Override
	public void setReadOnly(boolean readFlag) {
		
	}
}
