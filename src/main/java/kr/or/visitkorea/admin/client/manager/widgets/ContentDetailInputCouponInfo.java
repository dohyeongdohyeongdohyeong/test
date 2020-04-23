package kr.or.visitkorea.admin.client.manager.widgets;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import com.google.gwt.dom.client.Style;
import com.google.gwt.dom.client.Style.FontWeight;
import com.google.gwt.dom.client.Style.Overflow;
import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.dom.client.Style.TextOverflow;
import com.google.gwt.dom.client.Style.VerticalAlign;
import com.google.gwt.dom.client.Style.WhiteSpace;
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
import gwt.material.design.client.ui.MaterialPanel;
import gwt.material.design.client.ui.MaterialRow;
import gwt.material.design.client.ui.MaterialTextBox;
import gwt.material.design.client.ui.MaterialToast;
import gwt.material.design.jscore.client.api.JSON;
import gwt.material.design.jscore.client.api.JsObject;
import kr.or.visitkorea.admin.client.application.Console;
import kr.or.visitkorea.admin.client.manager.VisitKoreaBusiness;
import kr.or.visitkorea.admin.client.manager.contents.recommand.RecommApplication;
import kr.or.visitkorea.admin.client.manager.contents.recommand.RecommContentsTree;
import kr.or.visitkorea.admin.client.widgets.window.MaterialExtentsWindow;

public class ContentDetailInputCouponInfo extends ContentDetailItem {
	private boolean isEditMode;
	private MaterialIcon iconDelete;
	private boolean isSave = false;
	private MaterialExtentsWindow window;
	private ContentTable CouponTable;
	private MaterialPanel CouponDetailPanel;
	private MaterialLabel TitleLabel;
	private MaterialLabel RSVDateLabel;
	private MaterialIcon Addicon;
	private MaterialIcon RemoveIcon;
	private MaterialIcon IndexUpIcon;
	private MaterialIcon IndexDownIcon;
	private MaterialLabel DescLabel;
	private MaterialLabel AreaLabel;
	private MaterialLabel DCDateLabel;
	private MaterialTextBox Title;
	private boolean chk = true;
	
	public ContentDetailInputCouponInfo(ContentTreeItem treeItem, boolean displayLabel, String cotId,
			boolean displayDelete, boolean ordering) {
		super(treeItem, displayLabel, cotId, displayDelete, ordering);
		init();
	}

	public ContentDetailInputCouponInfo(ContentTreeItem treeItem, boolean displayLabel, String cotId,
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
			
			MaterialPanel menuContent = new MaterialPanel();
			menuContent.setLayoutPosition(Position.ABSOLUTE);
			menuContent.setTop(0);
			menuContent.setRight(0);
			menuContent.setWidth("100%");

			
			
			
			
		
			
			
			CouponTable = new ContentTable(ContentTable.TABLE_SELECT_TYPE.SELECT_SINGLE);
			CouponTable.setHeight(400);
			CouponTable.setLayoutPosition(Position.RELATIVE);
			CouponTable.setWidth("362px");
			CouponTable.setMargin(10);
			CouponTable.appendTitle("상태", 70, TextAlign.CENTER);
			CouponTable.appendTitle("구분", 70, TextAlign.CENTER);
			CouponTable.appendTitle("카테고리", 70, TextAlign.CENTER);
			CouponTable.appendTitle("제목", 150, TextAlign.CENTER);
			CouponTable.setDisplay(Display.INLINE_BLOCK);
			CouponTable.setMarginTop(-20);
			CouponTable.getHeader().setBackgroundColor(Color.GREY_LIGHTEN_2);
			CouponTable.setTableBorder("1px solid #c3c3c3");
			
			Addicon = new MaterialIcon(IconType.ADD);
			Addicon.setTooltip("추가");
			Addicon.setLineHeight(26);
			Addicon.setVerticalAlign(VerticalAlign.MIDDLE);
			Addicon.setFontSize("1.0em");
			Addicon.setBorderRight("1px solid #e0e0e0");
			Addicon.setHeight("26px");
			Addicon.setMargin(0);
			Addicon.setWidth("26px");
			Addicon.setFloat(com.google.gwt.dom.client.Style.Float.LEFT);
			Addicon.setTextAlign(TextAlign.CENTER);
			Addicon.addClickHandler(e->{
				if (!isEditMode)
					return;
				HashMap<String, Object> params = new HashMap<>();
				params.put("mode", "CONTENTS");
				params.put("host", this);
				params.put("ACI_ID", this.item.getInternalReferences().get("ACI_ID"));
				this.window.openDialog(RecommApplication.COUPON_INFO_CONTENT, params, 950);
				
			});
			Addicon.setEnabled(false);
			
			RemoveIcon = new MaterialIcon(IconType.REMOVE);
			RemoveIcon.setTooltip("삭제");
			RemoveIcon.setLineHeight(26);
			RemoveIcon.setVerticalAlign(VerticalAlign.MIDDLE);
			RemoveIcon.setFontSize("1.0em");
			RemoveIcon.setBorderRight("1px solid #e0e0e0");
			RemoveIcon.setHeight("26px");
			RemoveIcon.setMargin(0);
			RemoveIcon.setWidth("26px");
			RemoveIcon.setTextAlign(TextAlign.CENTER);
			RemoveIcon.setEnabled(false);
			
			RemoveIcon.setFloat(com.google.gwt.dom.client.Style.Float.LEFT);
			RemoveIcon.addClickHandler(e->{
				if (!isEditMode)
					return;

					ContentTableRow IndexUpBefore = CouponTable.getSelectedRows().get(0);
					int TargetIndex = CouponTable.getRowContainer().getWidgetIndex(IndexUpBefore);
					JSONObject obj = (JSONObject) IndexUpBefore.get("COUPON_INFO");
					String Delete = obj.get("CP_DEL_FL").isString().stringValue();
					
					
					JSONObject parameterJSON = new JSONObject();
					parameterJSON.put("cmd", new JSONString("DELETE_ARTICLE_COUPON_INFO"));
					parameterJSON.put("CP_ID", new JSONString(obj.get("CP_ID").isString().stringValue()));
					VisitKoreaBusiness.post("call", parameterJSON.toString(), (param1, param2, param3) -> {
						JSONObject resultObj = JSONParser.parseStrict(JSON.stringify((JsObject) param1)).isObject();
						int result = (int) resultObj.get("body").isObject().get("result").isNumber().doubleValue();
						
						if(result > 0) {
							if(Delete == "Y") {
								window.confirm("경고", "해당 쿠폰의 상태를 정상으로 변경하시겠습니까?", 450, event -> {
									obj.put("CP_DEL_FL", new JSONString("N"));
									IndexUpBefore.getChildren().get(0).getElement().setInnerText("정상");
								});
								
							} else {
								
								window.confirm("삭제 경고", "해당쿠폰은 현재 "+result+"명의 사용자가 담은 쿠폰입니다. 쿠폰을 삭제 하시겠습니까?", 450, event -> {
									if (e.getSource().toString().contains("yes")) {
										ContentTableRow IndexUpBefore2 = CouponTable.getSelectedRows().get(0);
										JSONObject obj2 = (JSONObject) IndexUpBefore2.get("COUPON_INFO");
										
										obj2.put("CP_DEL_FL", new JSONString("Y"));
										IndexUpBefore2.getChildren().get(0).getElement().setInnerText("삭제");
									} 
								});
							}
						} else {
							if(Delete == "Y") {
								obj.put("CP_DEL_FL", new JSONString("N"));
								IndexUpBefore.getChildren().get(0).getElement().setInnerText("정상");
							} else {
								window.confirm("삭제 경고", "해당쿠폰은 사용자가 담지 않은 쿠폰입니다. 쿠폰을 완전히 삭제하시겠습니까?", 450, event -> {
									if (e.getSource().toString().contains("yes")) {
										ContentTableRow IndexUpBefore2 = CouponTable.getSelectedRows().get(0);
										JSONObject obj2 = (JSONObject) IndexUpBefore2.get("COUPON_INFO");
										
										obj2.put("CP_DEL_FL", new JSONString("Y"));
										obj2.put("STATUS", new JSONNumber(-1));
										CouponTable.getRowContainer().remove(CouponTable.getSelectedRows().get(0));
										item.setEditorTable((Integer.parseInt(item.getEditorValue())-1)+"");
									} 
								});
							}
						}
					});
					
			});
			IndexUpIcon = new MaterialIcon(IconType.ARROW_UPWARD);
			IndexUpIcon.setTooltip("위로");
			IndexUpIcon.setLineHeight(26);
			IndexUpIcon.setEnabled(false);
			
			IndexUpIcon.setVerticalAlign(VerticalAlign.MIDDLE);
			IndexUpIcon.setFontSize("1.0em");
			IndexUpIcon.setBorderLeft("1px solid #e0e0e0");
			IndexUpIcon.setHeight("26px");
			IndexUpIcon.setMargin(0);
			IndexUpIcon.setTextAlign(TextAlign.CENTER);
			IndexUpIcon.setWidth("26px");
			IndexUpIcon.setFloat(com.google.gwt.dom.client.Style.Float.RIGHT);
			IndexUpIcon.addClickHandler(e->{
				if (!isEditMode)
					return;
				IndexUpdown(true);
			});
			IndexDownIcon = new MaterialIcon(IconType.ARROW_DOWNWARD);
			IndexDownIcon.setTooltip("아래로");
			IndexDownIcon.setLineHeight(26);
			IndexDownIcon.setVerticalAlign(VerticalAlign.MIDDLE);
			IndexDownIcon.setFontSize("1.0em");
			IndexDownIcon.setBorderLeft("1px solid #e0e0e0");
			IndexDownIcon.setHeight("26px");
			IndexDownIcon.setMargin(0);
			IndexDownIcon.setTextAlign(TextAlign.CENTER);
			IndexDownIcon.setWidth("26px");
			IndexDownIcon.setEnabled(false);
			IndexDownIcon.setFloat(com.google.gwt.dom.client.Style.Float.RIGHT);
			IndexDownIcon.addClickHandler(e->{
				if (!isEditMode)
					return;
				IndexUpdown(false);
			});
			CouponTable.getButtomMenu().addIcon(Addicon);
			CouponTable.getButtomMenu().addIcon(RemoveIcon);
			CouponTable.getButtomMenu().addIcon(IndexUpIcon);
			CouponTable.getButtomMenu().addIcon(IndexDownIcon);
			
			CreateCouponPanel();

			contentPreview.add(previewTitle);
			contentPreview.add(menuContent);

			MaterialIcon iconEdit = new MaterialIcon(IconType.EDIT);
			MaterialIcon iconSave = new MaterialIcon(IconType.SAVE);

			iconSave.setVisible(false);
			
			iconEdit.setFloat(Style.Float.RIGHT);
			iconEdit.setMarginLeft(0);
			iconEdit.addClickHandler(event -> {
				Title.setEnabled(true);
				isEditMode = true;
				iconEdit.setVisible(false);
				iconDelete.setVisible(false);
				iconSave.setVisible(true);
				Addicon.setEnabled(true);
				RemoveIcon.setEnabled(true);
				IndexUpIcon.setEnabled(true);
				IndexDownIcon.setEnabled(true);
			});
			
			iconSave.setFloat(Style.Float.RIGHT);
			iconSave.setMarginLeft(0);
			iconSave.addClickHandler(event -> {
				isEditMode = false;
				Title.setEnabled(false);
				iconEdit.setVisible(true);
				iconDelete.setVisible(true);
				iconSave.setVisible(false);
				this.item.getInternalReferences().put("ARTICLE_TITLE", this.Title.getText());
				Addicon.setEnabled(false);
				RemoveIcon.setEnabled(false);
				IndexUpIcon.setEnabled(false);
				IndexDownIcon.setEnabled(false);
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

			loadData();
			this.content.add(this.contentPreview);
			MaterialRow TitleRow = addRow(this.content);
			addLabel(TitleRow, "s3", "컴포넌트 명", Color.GREY_LIGHTEN_2);
			Title = addInputBox(TitleRow, "s8", "컴포넌트 명을 입력해주세요");
			Title.setEnabled(false);
			this.content.add(CouponTable);
			this.content.add(CouponDetailPanel);
		}
		
		this.add(this.content);
	}

	public void loadData() {
		
		JSONObject parameterJSON = new JSONObject();
		parameterJSON.put("cmd", new JSONString("SELECT_ARTICLE_COUPON_INFO"));
		parameterJSON.put("aciId", new JSONString(this.item.getInternalReferences().get("ACI_ID").toString()));
		VisitKoreaBusiness.post("call", parameterJSON.toString(), (param1, param2, param3) -> {
			JSONObject resultObj = JSONParser.parseStrict(JSON.stringify((JsObject) param1)).isObject();
			String result = resultObj.get("header").isObject().get("process").isString().stringValue();
			
			if (result.equals("success")) {
				JSONArray resultArr = resultObj.get("body").isObject().get("result").isArray();
					renderTable(resultArr);
					chk = true;
			}
		});
	}
	

	public void appendTableRow(JSONObject obj, int index, ContentTable addTable ) {
	
		
		
		if(!obj.containsKey("STATUS")) {
			obj.put("STATUS", new JSONNumber(0));
		}
		obj.put("CP_INDEX", new JSONNumber(index));
		
		if(!obj.containsKey("CP_IMG_PATH")) {
			obj.put("CP_IMG_PATH", obj.get("IMAGE_PATH"));
		}
		obj.put("ACI_ID", new JSONString(this.item.getInternalReferences().get("ACI_ID").toString()));
		String Delete = obj.get("CP_DEL_FL").isString().stringValue();
		if(Delete == "Y") Delete = "삭제";
		else Delete = "정상";
		
		String type = obj.get("CP_GB").isString().stringValue();
	
		String Category = "";
		
		if(obj.containsKey("CP_COT_CATEGORY_NAME")) {
			Category = obj.get("CP_COT_CATEGORY_NAME").isString().stringValue();
		}
		
		
		ContentTableRow tableRow = addTable.addRow(
				Color.WHITE, Delete,
				type == "CP" ? "쿠폰정보" : "여행정보",
				Category,
				obj.get("CP_TITLE").isString().stringValue());
		
		if(Delete == "Y") tableRow.setTextColor(Color.RED_LIGHTEN_1);
		tableRow.put("CP_INDEX", index);
		tableRow.put("COUPON_INFO", obj);
		
		tableRow.addDoubleClickHandler(event -> {
			if (!isEditMode)
				return;
			ContentTableRow IndexUpBefore = CouponTable.getSelectedRows().get(0);
			int TargetIndex = CouponTable.getRowContainer().getWidgetIndex(IndexUpBefore);
			
			HashMap<String, Object> params = new HashMap<>();
			params.put("host", this);
			params.put("ACI_ID", this.item.getInternalReferences().get("ACI_ID"));
			params.put("OBJ", CouponTable.getRowsList().get(TargetIndex).get("COUPON_INFO"));
				
			params.put("mode", "MODIFY");
			this.window.openDialog(RecommApplication.COUPON_INFO_CONTENT, params, 950);
		});
		
		tableRow.addClickHandler(event->{
			
			ContentTableRow IndexUpBefore = CouponTable.getSelectedRows().get(0);
			int TargetIndex = CouponTable.getRowContainer().getWidgetIndex(IndexUpBefore);
			
			RSVDateLabel.setText("");
			TitleLabel.setText("");
			DescLabel.setText("");
			AreaLabel.setText("");
			DCDateLabel.setText("");
			
			JSONObject obj2 = null;
				 obj2 = (JSONObject) CouponTable.getRowsList().get(TargetIndex).get("COUPON_INFO");
			TitleLabel.setText(obj2.get("CP_TITLE").isString().stringValue());
			DescLabel.setText(obj2.get("CP_DESC").isString().stringValue());
			String Area = obj2.get("AREA_NAME").isString().stringValue() + " " + obj2.get("SIGUGUN_NAME").isString().stringValue();
			AreaLabel.setText(Area);
			String Date = "";
			if(obj2.containsKey("CP_START_DATE")) {
				Date += obj2.get("CP_START_DATE").isString().stringValue() + " ~ ";
				DCDateLabel.setText(Date);
			}
			if(obj2.containsKey("CP_END_DATE")) {
				Date += obj2.get("CP_END_DATE").isString().stringValue();
				if(Date == "") DCDateLabel.setText(" ~ " + Date);
				else DCDateLabel.setText(Date);
			}
			Date = "";
			if(obj2.containsKey("CP_RSV_START_DATE")) {
				Date += obj2.get("CP_RSV_START_DATE").isString().stringValue() + " ~ ";
				RSVDateLabel.setText(Date);
			}
			if(obj2.containsKey("CP_RSV_END_DATE")) {
				Date += obj2.get("CP_RSV_END_DATE").isString().stringValue();
				if(Date == "") RSVDateLabel.setText(" ~ " + Date);
				else RSVDateLabel.setText(Date);
			}
			
			
		});
		
		
		item.setEditorTable(CouponTable.getRowsList().size()+"");
	}
	
	public void renderTable(JSONArray array) {
			this.item.getInternalReferences().put("COUPON_TABLE", this.CouponTable);
		int size = array.size();
		for (int i = 0; i < size; i++) {
			JSONObject obj = array.get(i).isObject();
			
			if (obj.containsKey("STATUS"))
				obj.put("STATUS", new JSONNumber(obj.get("STATUS").isNumber().doubleValue()));
			else
				obj.put("STATUS", new JSONNumber(0));
			
				appendTableRow(obj, i,this.CouponTable);
		}
		Title.setText(this.item.getInternalReferences().get("ARTICLE_TITLE") != null ? this.item.getInternalReferences().get("ARTICLE_TITLE")+"" : "");
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

	@Override
	protected void renderComponent(MaterialPanel dispContent) {
		// TODO Auto-generated method stub
		
	}
	
	public MaterialLabel addLabel(MaterialRow row, String grid, String text, Color color) {
		MaterialLabel lbl = new MaterialLabel(text);
		lbl.setTextAlign(TextAlign.CENTER);
		lbl.setBackgroundColor(color);
		lbl.setLineHeight(40);
		lbl.setHeight("40px");
		
		MaterialColumn col = addColumn(grid);
		col.add(lbl);
		row.add(col);
		return lbl;
	}
	
	public MaterialRow addRow(MaterialPanel contentPanel) {
		MaterialRow row = new MaterialRow();
		row.setWidth("100%");
		row.setMarginTop(20);
		row.setMarginBottom(0);
		
		contentPanel.add(row);
		return row;
	}
	
	public MaterialColumn addColumn(String grid) {
		MaterialColumn col = new MaterialColumn();
		col.setGrid(grid);
		
		return col;
	}
	
	public MaterialTextBox addInputBox(MaterialRow row, String grid, String placeholder) {
		MaterialTextBox tBox = new MaterialTextBox(placeholder);
		tBox.setMargin(0);
		tBox.setPadding(0);
		
		MaterialColumn col = addColumn(grid);
		col.add(tBox);
		row.add(col);
		return tBox;
	}
	
	public void CreateCouponPanel() {
		
		CouponDetailPanel = new MaterialPanel();
		CouponDetailPanel.setWidth("450px");
		CouponDetailPanel.setHeight("300px");
		CouponDetailPanel.setMarginLeft(10);
		CouponDetailPanel.setDisplay(Display.INLINE_BLOCK);
		CouponDetailPanel.setMarginBottom(50);
		CouponDetailPanel.setBackgroundColor(Color.GREY_LIGHTEN_4);
		
		MaterialRow row1 = addRow(CouponDetailPanel);
		row1.setMarginTop(10);
		row1.setMarginBottom(20);
		
		addLabel(row1, "s3", "제목", Color.GREY_LIGHTEN_2);
		TitleLabel = addLabel(row1, "s9", "", Color.GREY_LIGHTEN_4);
		TitleLabel.setOverflow(Overflow.HIDDEN);
		TitleLabel.getElement().getStyle().setTextOverflow(TextOverflow.ELLIPSIS);
		TitleLabel.getElement().getStyle().setWhiteSpace(WhiteSpace.NOWRAP);
		
		MaterialRow row2 = addRow(CouponDetailPanel);
		
		addLabel(row2, "s3", "소개", Color.GREY_LIGHTEN_2);
		DescLabel = addLabel(row2, "s9", "", Color.GREY_LIGHTEN_4);
		DescLabel.setOverflow(Overflow.HIDDEN);
		DescLabel.getElement().getStyle().setTextOverflow(TextOverflow.ELLIPSIS);
		DescLabel.getElement().getStyle().setWhiteSpace(WhiteSpace.NOWRAP);
		
		
		MaterialRow row3 = addRow(CouponDetailPanel);
		addLabel(row3, "s3", "지역", Color.GREY_LIGHTEN_2);
		AreaLabel = addLabel(row3, "s9", "", Color.GREY_LIGHTEN_4);
		
		
		MaterialRow row4 = addRow(CouponDetailPanel);
		addLabel(row4, "s3", "할인기간", Color.GREY_LIGHTEN_2);
		DCDateLabel = addLabel(row4, "s9", "", Color.GREY_LIGHTEN_4);
		
		MaterialRow row5 = addRow(CouponDetailPanel);
		
		addLabel(row5, "s3", "예약기간", Color.GREY_LIGHTEN_2);
		
		RSVDateLabel = addLabel(row5, "s9", "", Color.GREY_LIGHTEN_4);
		
		
		
	
		
		
		
		
		
	}
	
	public ContentTable getCouponTable() {
		return CouponTable;
	}
	
	
	public void IndexUpdown(boolean order) {
		if(chk == false)
			return;
			chk = false;
		ContentTableRow IndexUpBefore = CouponTable.getSelectedRows().get(0);
		int TargetIndex = CouponTable.getRowContainer().getWidgetIndex(IndexUpBefore);
		int afterTargetIndex = TargetIndex;
		if(order) {
			if(TargetIndex != 0) {
				afterTargetIndex = TargetIndex-1;
				Collections.swap(CouponTable.getRowsList(),TargetIndex,TargetIndex-1);
			} else {
				MaterialToast.fireToast("더이상 올릴 수 없습니다.");
				chk = true;
				return;
			}
		} else {
			
			if(CouponTable.getRowsList().size() != TargetIndex+1) {
				Collections.swap(CouponTable.getRowsList(),TargetIndex,TargetIndex+1);
				afterTargetIndex = TargetIndex+1;
			} else {
				MaterialToast.fireToast("더이상 내릴 수 없습니다.");
				chk = true;
				return;
			}
		}
		
		CouponTable.getRowContainer().clear();
		for (int i = 0; i < CouponTable.getRowsList().size(); i++) {
			CouponTable.getRowContainer().add(CouponTable.getRowsList().get(i));
		}
		
		
		if(order)
			CouponTable.setSelectedIndex(TargetIndex - 1);
		else
			CouponTable.setSelectedIndex(TargetIndex + 1);
		chk = true;
		
	}
	
	public MaterialLabel getTitleLabel(){
		return TitleLabel;
	}
	
	public MaterialLabel getAreaLabel(){
		return AreaLabel;
	}
	
	public MaterialLabel getDescLabel(){
		return DescLabel;
	}
	
	public MaterialLabel getDCDateLabel(){
		return DCDateLabel;
	}
	
	public MaterialLabel getRSVDateLabel(){
		return RSVDateLabel;
	}
	
	
}
