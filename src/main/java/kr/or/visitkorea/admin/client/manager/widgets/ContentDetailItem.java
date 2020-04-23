package kr.or.visitkorea.admin.client.manager.widgets;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Style;
import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONNumber;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.json.client.JSONValue;
import com.google.gwt.user.client.ui.Widget;

import gwt.material.design.addins.client.fileuploader.MaterialFileUploader;
import gwt.material.design.client.base.MaterialWidget;
import gwt.material.design.client.constants.ButtonSize;
import gwt.material.design.client.constants.ButtonType;
import gwt.material.design.client.constants.Color;
import gwt.material.design.client.constants.Display;
import gwt.material.design.client.constants.IconType;
import gwt.material.design.client.constants.ProgressType;
import gwt.material.design.client.constants.TextAlign;
import gwt.material.design.client.ui.MaterialButton;
import gwt.material.design.client.ui.MaterialCard;
import gwt.material.design.client.ui.MaterialCardContent;
import gwt.material.design.client.ui.MaterialCardImage;
import gwt.material.design.client.ui.MaterialIcon;
import gwt.material.design.client.ui.MaterialImage;
import gwt.material.design.client.ui.MaterialLabel;
import gwt.material.design.client.ui.MaterialPanel;
import gwt.material.design.client.ui.MaterialProgress;
import gwt.material.design.client.ui.MaterialRow;
import gwt.material.design.client.ui.MaterialTextBox;
import gwt.material.design.jquery.client.api.Functions.Func3;
import kr.or.visitkorea.admin.client.application.Console;
import kr.or.visitkorea.admin.client.manager.VisitKoreaBusiness;
import kr.or.visitkorea.admin.client.manager.contents.database.DatabaseContentType;
import kr.or.visitkorea.admin.client.manager.contents.recommand.RecommContentsTree;
import kr.or.visitkorea.admin.client.manager.otherDepartment.widgets.SearchBodyWidget;
import kr.or.visitkorea.admin.client.manager.otherDepartment.widgets.TagListRow;

public abstract class ContentDetailItem extends MaterialPanel{

	protected MaterialIcon icon;
	
	protected MaterialLabel title;
	protected MaterialPanel content;
	protected String titleName;

	protected boolean isDisplayLabel;
	protected MaterialPanel contentPreview;
	protected ContentTreeItem item;
	protected MaterialPanel uploaderPanel;
	protected List<ItemInformation> imageList = new ArrayList<ItemInformation>(); 
	protected Map<String, ImageInformation> infoMap = new HashMap<String, ImageInformation>();
	protected boolean isRenderMode;
	protected String cotId;
	protected List<JSONObject> itemList = new ArrayList<JSONObject>();
	protected boolean displayDelete;
	protected boolean ordering;

	private MaterialIcon iconUp;

	private MaterialIcon iconDown;


	public ContentDetailItem(ContentTreeItem treeItem, boolean displayLabel, String cotId, boolean displayDelete, boolean ordering) {
		
		super();
		
		this.item = treeItem;
		this.titleName = treeItem.getText();
		this.isDisplayLabel = displayLabel;
		this.cotId = cotId;
		this.displayDelete = displayDelete;
		this.ordering = ordering;
		this.setMargin(20);
		this.setLayoutPosition(Position.RELATIVE);
		
		iconUp = new MaterialIcon(IconType.ARROW_UPWARD);
		iconUp.setFloat(Style.Float.LEFT);
		iconUp.setMarginRight(0);
		iconUp.setTextColor(Color.BLUE);
		iconUp.addMouseOverHandler(event->{
			iconUp.setTextColor(Color.RED);
		});
		iconUp.addMouseOutHandler(event->{
			iconUp.setTextColor(Color.BLUE);
		});
		iconUp.addClickHandler(event->{
			
			ContentTreeItem parentItem = (ContentTreeItem) item.getParent();
			if (parentItem != null) {
				
				int itemIndex = parentItem.getWidgetIndex(item);
				if (itemIndex > 1) {
					parentItem.insertItem(item, itemIndex-1);
				}
				
				
				reorder();
				
			}

		});

		iconDown = new MaterialIcon(IconType.ARROW_DOWNWARD);
		iconDown.setFloat(Style.Float.LEFT);
		iconDown.setMarginRight(0);
		iconDown.setTextColor(Color.BLUE);
		iconDown.addMouseOverHandler(event->{
			iconDown.setTextColor(Color.RED);
		});
		iconDown.addMouseOutHandler(event->{
			iconDown.setTextColor(Color.BLUE);
		});
		iconDown.addClickHandler(event->{
			
			ContentTreeItem parentItem = (ContentTreeItem) item.getParent();
			
			if (parentItem != null) {
				
				int itemIndex = parentItem.getWidgetIndex(item);

				parentItem.insertItem( item , itemIndex + 2 );					
				
				reorder();
			}

		});
		

		
	}

	private void reorder() {
		
		Object contentObjectTree = item.getInternalReferences().get("CONTENT_TREE");
		
		if (contentObjectTree != null ) { 
			//&& contentObjectTree instanceof RecommContentsTree) {
			
			RecommContentsTree rct = (RecommContentsTree) contentObjectTree;
			rct.reOrderItems();
			rct.renderDetail();
		}
		
	}

	protected ContentDetailItem getPanel() {
		return this;
	}
	
	
	abstract protected void init();
	abstract protected void renderComponent(MaterialPanel dispContent);
	abstract protected void createComponent(MaterialPanel tgrPanel);
	abstract public void deleteData();

	protected void addOrdering(MaterialPanel menuContent) {
		
		ContentTreeItem parentItem = (ContentTreeItem) this.item.getParent();
		
		if (parentItem != null && this.ordering) {
			
			int totalCount = parentItem.getWidgetCount();
			int itemIndex  = parentItem.getWidgetIndex(item);
			
			Console.log("item index :: " + itemIndex + " :: " + item.getText());

			if (itemIndex <= 2 && itemIndex == (totalCount -1)) {
				
				
			}else if (itemIndex == 2 && itemIndex < (totalCount -1)) {
				
				menuContent.add(iconDown);
				
			}else if (itemIndex > 2 && itemIndex < (totalCount -1)) {
				
				menuContent.add(iconUp);
				menuContent.add(iconDown);
				
			}else if (itemIndex == (totalCount -1)) {
				
				menuContent.add(iconUp);
				
				Console.log("");
				
			}
		}
	}

	protected void addOrdering2(MaterialPanel menuContent) {
		if (this.item.getParent() instanceof ContentTreeItem ) {
			
			ContentTreeItem parentItem = (ContentTreeItem) this.item.getParent();
			
			if (parentItem != null && this.ordering) {
				
				int totalCount = parentItem.getWidgetCount();
				int itemIndex  = parentItem.getWidgetIndex(item);
				
				if (itemIndex == 1 && totalCount > 2) {
					
					menuContent.add(iconDown);
					
				}else if (itemIndex > 1 && itemIndex < (totalCount -1)) {
					
					menuContent.add(iconUp);
					menuContent.add(iconDown);
					
				}else if (itemIndex == (totalCount -1) && totalCount > 2) {
					
					menuContent.add(iconUp);
					
				}
			}
		}
	}
	
	protected void saveData() {

		if (this.item.getInternalReferences().get("TBL") != null && this.item.getInternalReferences().get("TBL").equals("ARTICLE_CONTENT")) {
			
			saveArticleContentData();
			
		}else {
			
			String TBL = (String) this.item.getInternalReferences().get("TBL");
			String COL = (String) this.item.getInternalReferences().get("COL");
			String COL_TITLE = (String) this.item.getInternalReferences().get("COL_TITLE");
			String COT_ID = (String) this.item.getInternalReferences().get("COT_ID");
			String itemValue = null;
			
			Console.log("===============> Start saveData() Information DATA");
			Console.log("TBL :: " + TBL);
			Console.log("COL :: " + COL);
			Console.log("COL_TITLE :: " + COL_TITLE);
			Console.log("COT_ID :: " + COT_ID);
			Console.log("===============> end Information DATA");
			
			if (this.item.getContentType().equals(DatabaseContentType.INPUT_HTML)) {
				if(item.getEditorSafeValue() != null)
					itemValue = item.getEditorSafeValue().asString();
				else
					itemValue = "";
			}else if (this.item.getContentType().equals(DatabaseContentType.INPUT_TEXT)) {
				itemValue = item.getEditorValue();
			}
			
			if (itemValue == null) itemValue = "";
	
			if (itemValue != null) {
				
				JSONObject parameterJSON = new JSONObject();
				parameterJSON.put("cmd", new JSONString("DATABASE_SINGLE_ROW"));
				parameterJSON.put("tbl", new JSONString(TBL));
				parameterJSON.put("col", new JSONString(COL));
				parameterJSON.put("colTitle", new JSONString(COL_TITLE));
				parameterJSON.put("cotId", new JSONString(COT_ID));
				parameterJSON.put("value", new JSONString(itemValue));
				
				if (this.item.getInternalReferences().get("ROOM_CODE") != null) {
					String roomCode = (String) this.item.getInternalReferences().get("ROOM_CODE");
					parameterJSON.put("roomCode", new JSONString(roomCode));
				}
				
				if (this.item.getInternalReferences().get("SUB_CONTENT_ID") != null) {
					String subContentId = (String) this.item.getInternalReferences().get("SUB_CONTENT_ID");
					parameterJSON.put("subContentId", new JSONString(subContentId));
				}
				
				VisitKoreaBusiness.post("call", parameterJSON.toString(), new Func3<Object, String, Object>() {
					
					@Override
					public void call(Object param1, String param2, Object param3) {
						
					}
				});
				
			}
		}	
	}
	
	protected String getVideoId() {
		
		String editorValue = item.getEditorSafeValue().asString();
		
		 
		if (editorValue.startsWith("https://www.youtube.com/embed/")) {
		
			String editorTargetValue = item.getEditorValue().replaceAll("https://www.youtube.com/embed/", "");
			//MaterialToast.fireToast("editorTargetValue:: " + editorTargetValue);
			
			return editorTargetValue;
			
		}else if (editorValue.startsWith("<div")) {
			
			int startIndex = editorValue.indexOf("VideoId: '");
			int endIndex = editorValue.lastIndexOf("'");

			//MaterialToast.fireToast("editorValue:: " + editorValue.substring(startIndex + 10, endIndex));
			
			return editorValue.substring(startIndex + 10, endIndex);
		
		}
		
		return null;
	}

	public JSONValue saveArticleContentData() {
		
		Map<String, Object> inputMap = new HashMap<String, Object>();

		Object isTitleObject = this.item.getInternalReferences().get("IS_TITLE");
		
		boolean isTitle = isTitleObject == null ? false : (Boolean)isTitleObject;
		
		if (this.item.getInternalReferences().get("ACI_ID") == null) {
			this.item.getInternalReferences().put("ACI_ID", IDUtil.uuid());
		}
		
		inputMap.put("ACI_ID", this.item.getInternalReferences().get("ACI_ID"));
		inputMap.put("ATM_ID", this.item.getInternalReferences().get("ATM_ID"));
		inputMap.put("CONTENT_ORDER", this.item.getInternalReferences().get("CONTENT_ORDER"));
		inputMap.put("ARTICLE_ORDER", this.item.getInternalReferences().get("ARTICLE_ORDER"));
		inputMap.put("ARTICLE_SUB_ORDER", this.item.getInternalReferences().get("ARTICLE_SUB_ORDER"));
		inputMap.put("ARTICLE_TYPE", this.item.getInternalReferences().get("ARTICLE_TYPE"));

		if (this.item.getInternalReferences().get("IS_BOX") != null)		
			inputMap.put("IS_BOX", this.item.getInternalReferences().get("IS_BOX"));
		
		if (this.item.getInternalReferences().get("IS_VERTICAL") != null)		
			inputMap.put("IS_VERTICAL", this.item.getInternalReferences().get("IS_VERTICAL"));
		
		if (this.item.getInternalReferences().get("ARTICLE_TITLE") != null)		
			inputMap.put("ARTICLE_TITLE", this.item.getInternalReferences().get("ARTICLE_TITLE"));
		
		if (this.item.getInternalReferences().get("ARTICLE_BODY") != null)		
			inputMap.put("ARTICLE_BODY", this.item.getInternalReferences().get("ARTICLE_BODY"));

		if (this.item.getInternalReferences().get("IMG_ID") != null)		
			inputMap.put("IMG_ID", this.item.getInternalReferences().get("IMG_ID"));
		
		if (this.item.getInternalReferences().get("ID_MAP") != null)		{
			inputMap.put("ID_MAP", this.item.getInternalReferences().get("ID_MAP"));
		}
		
Console.log("item ::" + item);
		
		if (this.item.getContentType().equals(DatabaseContentType.INPUT_HTML)) {
			
			if (item.getEditorSafeValue() != null && item.getEditorSafeValue().asString().length() > 0) {
				if (isTitle) {
					inputMap.put("ARTICLE_TITLE", item.getEditorSafeValue().asString());
					inputMap.put("ARTICLE_BODY", "");
				}else {
					inputMap.put("ARTICLE_TITLE", "");
					inputMap.put("ARTICLE_BODY", item.getEditorSafeValue().asString());
				}
//				contentRowProce(inputMap);
				return getContentRowProce(inputMap);
			}
			
		}else if (this.item.getContentType().equals(DatabaseContentType.INPUT_TEXT)) {
			
			if (item.getEditorValue() != null && item.getEditorValue().length() > 0) {
				if (isTitle) {
					inputMap.put("ARTICLE_TITLE", item.getEditorValue());
					inputMap.put("ARTICLE_BODY", "");
				}else {
					inputMap.put("ARTICLE_TITLE", "");
					inputMap.put("ARTICLE_BODY", item.getEditorValue());
				}
//				contentRowProce(inputMap);
				return getContentRowProce(inputMap);
			} else {
				inputMap.put("ARTICLE_TITLE", "");
				inputMap.put("ARTICLE_BODY", item.getEditorValue());
				return getContentRowProce(inputMap);
			}
			
		}else if (this.item.getContentType().equals(DatabaseContentType.INPUT_IMAGE)) {
			List<ItemInformation> informations = item.getEditorObject();
			double subOrderIndex = 0;
			
			JSONArray jArray = new JSONArray();
			
			for (ItemInformation ii : informations){
				String url = ((ImageInformation)ii).getImgId();
				
				
				inputMap.put("VALUE", url);
				inputMap.put("IMG_ID", url);
				inputMap.put("ARTICLE_SUB_ORDER", subOrderIndex);
				
				jArray.set(jArray.size(), getContentRowProce(inputMap));
				subOrderIndex++;
			}
			
			return jArray;
			
		}else if (this.item.getContentType().equals(DatabaseContentType.INPUT_MOVIE)) {
			
			if (item.getEditorValue() != null && item.getEditorValue().length() > 0) {
				String videoFormat = null;
				if (item.getInternalReferences().get("VIDEO_FORMAT") != null) {
					videoFormat = item.getInternalReferences().get("VIDEO_FORMAT").toString();
				} else {
					videoFormat = item.getInternalReferences().get("ARTICLE_BODY").toString();
				}
				inputMap.put("ARTICLE_TITLE", videoFormat);
				inputMap.put("ARTICLE_BODY", videoFormat);
				return getContentRowProce(inputMap);
			}

		}else if (this.item.getContentType().equals(DatabaseContentType.INPUT_CLOSING_LINE)) {
			
			return getContentRowProce(inputMap);
			
		}else if (this.item.getContentType().equals(DatabaseContentType.INPUT_TRAVEL_INFO)) {
			SearchBodyWidget travelTable = (SearchBodyWidget) this.item.getInternalReferences().get("TRAVEL_TABLE");
			List<Widget> rowList = travelTable.getRows();

			JSONArray travelArr = new JSONArray();
			for (int i = 0; i < rowList.size(); i++) {
				TagListRow row = (TagListRow) rowList.get(i);
				JSONObject obj = (JSONObject) row.get("TRAVEL_INFO");
				travelArr.set(travelArr.size(), obj);
			}
			inputMap.put("ARTICLE_BODY", rowList.size()+"");
			inputMap.put("TRAVEL_INFO", travelArr);
			return getContentRowProce(inputMap);
		} else if (this.item.getContentType().equals(DatabaseContentType.INPUT_COUPON_INFO)) {
			ContentTable CouponTable = (ContentTable) this.item.getInternalReferences().get("COUPON_TABLE");
			String CouponTitle = (String)this.item.getInternalReferences().get("ARTICLE_TITLE");
			List<ContentTableRow> rowList = CouponTable.getRowsList();
			JSONArray CouponArr = new JSONArray();
			for (int i = 0; i < rowList.size(); i++) {
				ContentTableRow row = (ContentTableRow) rowList.get(i);
				JSONObject obj = (JSONObject) row.get("COUPON_INFO");
				if(obj.containsKey("INSERT_CHECK") && obj.get("STATUS").isNumber().doubleValue() == 1) {
					obj.put("STATUS",new JSONNumber(0));
				}
				CouponArr.set(CouponArr.size(), obj);
				
				obj.put("INSERT_CHECK",new JSONString("Y"));
			}
			
			inputMap.put("COUPON_INFO", CouponArr);
			inputMap.put("ARTICLE_BODY", rowList.size()+"");
			inputMap.put("ARTICLE_TITLE", CouponTitle);
			return getContentRowProce(inputMap);
		}
		return null;
	}

	protected JSONObject getContentRowProce(Map<String, Object> inputMap) {

		JSONObject parameterJSON = new JSONObject();
		parameterJSON.put("cmd", new JSONString("ARTICLE_CONTENT_ROW"));
		
		String articletype = inputMap.get("ARTICLE_TYPE")+"";
		
		parameterJSON.put("ACI_ID", new JSONString((String) inputMap.get("ACI_ID")));
		parameterJSON.put("ATM_ID", new JSONString((String)this.item.getInternalReferences().get("ATM_ID")));
		parameterJSON.put("CONTENT_ORDER", new JSONNumber((Double)inputMap.get("CONTENT_ORDER")));
		parameterJSON.put("ARTICLE_ORDER", new JSONNumber((Double)inputMap.get("ARTICLE_ORDER")));
		parameterJSON.put("ARTICLE_SUB_ORDER", new JSONNumber((Double)inputMap.get("ARTICLE_SUB_ORDER")));
		parameterJSON.put("ARTICLE_TYPE", new JSONNumber(Double.parseDouble(articletype)));

		if (inputMap.get("IS_BOX") != null)		
			parameterJSON.put("IS_BOX", new JSONNumber((Double)inputMap.get("IS_BOX")));
		
		if (inputMap.get("IS_VERTICAL") != null)		
			parameterJSON.put("IS_VERTICAL", new JSONNumber((Double)inputMap.get("IS_VERTICAL")));

		if (inputMap.get("ARTICLE_TITLE") != null)
			parameterJSON.put("ARTICLE_TITLE", new JSONString((String) inputMap.get("ARTICLE_TITLE")));
		
		if (inputMap.get("ARTICLE_BODY") != null)
			parameterJSON.put("ARTICLE_BODY", new JSONString((String) inputMap.get("ARTICLE_BODY")));
		
		if (inputMap.get("IMG_ID") != null && !inputMap.get("IMG_ID").equals("undefined"))
			parameterJSON.put("IMG_ID", new JSONString((String) inputMap.get("IMG_ID")));
		
		if (inputMap.get("TRAVEL_INFO") != null) {
			if (inputMap.get("TRAVEL_INFO") instanceof JSONObject) {
				parameterJSON.put("TRAVEL_INFO", (JSONObject) inputMap.get("TRAVEL_INFO"));
			} else if (inputMap.get("TRAVEL_INFO") instanceof JSONArray) {
				parameterJSON.put("TRAVEL_INFO", (JSONArray) inputMap.get("TRAVEL_INFO"));
			}
		}
		if (inputMap.get("COUPON_INFO") != null) {
			if (inputMap.get("COUPON_INFO") instanceof JSONObject) {
				parameterJSON.put("COUPON_INFO", (JSONObject) inputMap.get("COUPON_INFO"));
			} else if (inputMap.get("COUPON_INFO") instanceof JSONArray) {
				parameterJSON.put("COUPON_INFO", (JSONArray) inputMap.get("COUPON_INFO"));
			}
		}
		
		
		
		if (inputMap.get("ID_MAP") != null) {
			Map<String, Object> idMap = (Map<String, Object>) inputMap.get("ID_MAP");
			Map<String, Object> imageMap = (Map<String, Object>) idMap.get(inputMap.get("IMG_ID"));
			if(imageMap.get("IMAGE_CAPTION") != null) 
				parameterJSON.put("IMAGE_CAPTION", new JSONString(imageMap.get("IMAGE_CAPTION").toString()));
			if(imageMap.get("IS_CAPTION") != null) 
				parameterJSON.put("IS_CAPTION", new JSONNumber(Integer.parseInt(String.valueOf(imageMap.get("IS_CAPTION")))));
			if(imageMap.get("IMAGE_DESCRIPTION") != null) 
				parameterJSON.put("IMAGE_DESCRIPTION", new JSONString(imageMap.get("IMAGE_DESCRIPTION").toString()));
		}
		
		Console.log("parameterJSON :: " +parameterJSON.toString());
		return parameterJSON;
		
	}

	protected void contentRowProce(Map<String, Object> inputMap) {

		JSONObject parameterJSON = new JSONObject();
		parameterJSON.put("cmd", new JSONString("ARTICLE_CONTENT_ROW"));
		
		
		String articletype = inputMap.get("ARTICLE_TYPE")+"";
		
		parameterJSON.put("ACI_ID", new JSONString((String) inputMap.get("ACI_ID")));
		parameterJSON.put("ATM_ID", new JSONString((String)this.item.getInternalReferences().get("ATM_ID")));
		parameterJSON.put("CONTENT_ORDER", new JSONNumber((Double)inputMap.get("CONTENT_ORDER")));
		parameterJSON.put("ARTICLE_ORDER", new JSONNumber((Double)inputMap.get("ARTICLE_ORDER")));
		parameterJSON.put("ARTICLE_SUB_ORDER", new JSONNumber((Double)inputMap.get("ARTICLE_SUB_ORDER")));
		parameterJSON.put("ARTICLE_TYPE", new JSONNumber(Double.parseDouble(articletype)));
		parameterJSON.put("IS_BOX", new JSONNumber((Double)inputMap.get("IS_BOX")));
		parameterJSON.put("IS_VERTICAL", new JSONNumber((Double)inputMap.get("IS_VERTICAL")));

		if (inputMap.get("ARTICLE_TITLE") != null)
			parameterJSON.put("ARTICLE_TITLE", new JSONString((String) inputMap.get("ARTICLE_TITLE")));
		
		if (inputMap.get("ARTICLE_BODY") != null)
			parameterJSON.put("ARTICLE_BODY", new JSONString((String) inputMap.get("ARTICLE_BODY")));
		
		if (inputMap.get("IMG_ID") != null && !inputMap.get("IMG_ID").equals("undefined")) {
			parameterJSON.put("IMG_ID", new JSONString((String) inputMap.get("IMG_ID")));
		}
		if (inputMap.get("TRAVEL_INFO") != null) {
			if (inputMap.get("TRAVEL_INFO") instanceof JSONObject) {
				parameterJSON.put("TRAVEL_INFO", (JSONObject) inputMap.get("TRAVEL_INFO"));
			} else if (inputMap.get("TRAVEL_INFO") instanceof JSONArray) {
				parameterJSON.put("TRAVEL_INFO", (JSONArray) inputMap.get("TRAVEL_INFO"));
			}
		}
		
		if (inputMap.get("COUPON_INFO") != null) {
			if (inputMap.get("COUPON_INFO") instanceof JSONObject) {
				parameterJSON.put("COUPON_INFO", (JSONObject) inputMap.get("COUPON_INFO"));
			} else if (inputMap.get("COUPON_INFO") instanceof JSONArray) {
				parameterJSON.put("COUPON_INFO", (JSONArray) inputMap.get("COUPON_INFO"));
			}
		}
		
		VisitKoreaBusiness.post("call", parameterJSON.toString(), new Func3<Object, String, Object>() {
			@Override
			public void call(Object param1, String param2, Object param3) {}
		});
		
	}

	
	public MaterialImage getImageBtton(int top, int left, String height, String width) {
		
		MaterialImage image1 = new MaterialImage();
		image1.setLayoutPosition(Position.ABSOLUTE);
		image1.setTop(top);
		image1.setLeft(left);
		image1.setUrl(GWT.getHostPageBaseURL() + "images/notfound.png");
		image1.setLineHeight(430);
		image1.setWidth("auto");
		image1.setHeight("auto");
		image1.setMaxHeight(height);
		image1.setMaxWidth(width);
		image1.setFloat(com.google.gwt.dom.client.Style.Float.LEFT);
		image1.addClickHandler(event->{
			MaterialPanel panel = (MaterialPanel)image1.getParent();
			for (Widget widget : panel.getChildrenList()) {
				if (widget instanceof MaterialImage) {
					((MaterialImage)widget).setBorder("3px solid #ffffff");
				}
			}
			image1.setBorder("3px solid #ff0000");
		});
		return image1;
	}

	protected MaterialRow getImageUploader(String tgrUrl, String tgrComment, int imageIndex, ImageInformation imageInfo, String tgrCaption) {
		
		MaterialRow row = new MaterialRow();
		row.setMarginTop(30);
		row.setLayoutPosition(Position.RELATIVE);
		row.setWidth("275px");
		row.setHeight("300px");
		
		String uniqueId = Document.get().createUniqueId();
		
		MaterialFileUploader uploader = new MaterialFileUploader();
		String uploaderId = Document.get().createUniqueId();

		uploader.setHeight("");
		uploader.setId(uploaderId);
		uploader.setAcceptedFiles("image/*"); 
		uploader.setGrid("14");
		uploader.setUrl(GWT.getHostPageBaseURL() + "call");
		uploader.setPreview(false);
		uploader.setMaxFileSize(20);  
		uploader.setShadow(0);
		uploader.setClickable(uniqueId);
		
		MaterialCard card = new MaterialCard();
		MaterialCardImage cardImage = new MaterialCardImage();
		cardImage.setBorder("1px solid #e0e0e0");
		cardImage.setTextAlign(TextAlign.CENTER);
		
		MaterialImage imgPreview = new MaterialImage();

		if (item.getEditorValue() != null && item.getEditorValue().length() > 0) {
			imgPreview.setUrl(item.getEditorValue());
		}
		
		imgPreview.setDisplay(Display.INLINE_BLOCK);
		imgPreview.setWidth("auto");
		imgPreview.setHeight("auto");
		imgPreview.setMaxHeight("190px");
		imgPreview.setMaxWidth("255px");
		cardImage.add(imgPreview);

		MaterialCardContent content = new MaterialCardContent();
		content.setLayoutPosition(Position.RELATIVE);
		
		MaterialLabel lblName = new MaterialLabel("");
		lblName.setFontSize("1.3em");
		
		MaterialLabel lblSize = new MaterialLabel("");
		lblSize.setFontSize("0.8em");
		
		MaterialProgress progress = new MaterialProgress();
		progress.setLayoutPosition(Position.ABSOLUTE);
		progress.setBottom(0);
		progress.setLeft(0);
		progress.setType(ProgressType.DETERMINATE);
		progress.setPercent(0);
		
		MaterialButton btn = new MaterialButton();
		btn.setId(uniqueId);
		btn.setLayoutPosition(Position.ABSOLUTE);
		btn.setTop(-25);
		btn.setRight(25);
		btn.setType(ButtonType.FLOATING);
		btn.setBackgroundColor(Color.PINK);
		btn.setSize(ButtonSize.LARGE);
		btn.setIconType(IconType.CLOUD_UPLOAD);
		btn.setIconColor(Color.WHITE);
		
		MaterialTextBox comment = new MaterialTextBox();
		comment.setLabel("이미지 설명");
		comment.setValue(tgrComment);
		
		MaterialTextBox caption = new MaterialTextBox();
		caption.setLabel("이미지 캡션");
		caption.setValue(tgrCaption);
		
		content.add(lblName);
		content.add(lblSize);
		content.add(comment);
		content.add(progress);
		content.add(btn);
		content.add(caption);
		
		card.add(cardImage);
		card.add(content);
		uploader.add(card);
		row.add(uploader);
		
		uploader.addTotalUploadProgressHandler(event->{
			progress.setPercent(event.getProgress());
		});

		uploader.addSuccessHandler(event->{
			
			JSONObject resultObj = (JSONObject) JSONParser.parseStrict(event.getResponse().getBody());
			String uploadValue = resultObj.get("body").isObject().get("result").isArray().get(0).isObject().get("saveName").isString().stringValue();
			
			String url = GWT.getHostPageBaseURL() + "call?cmd=TEMP_VIEW&name=" + uploadValue;
			
			lblName.setText(event.getTarget().getName());
			lblSize.setText(event.getTarget().getType());
			imgPreview.setUrl(url);
			
			if (imageInfo != null) {
				imageInfo.setUrl(url);
				imageInfo.setComment(comment.getValue());
			}else if (infoMap.get(uniqueId) == null){
				infoMap.put(uniqueId, new ImageInformation(url, comment.getValue(), uniqueId,caption.getValue(),-1));
				imageList.add(infoMap.get(uniqueId));
			}else {
				ImageInformation info = infoMap.get(uniqueId);
				info.setUrl(url);
				info.setComment(comment.getValue());
			}
			
		});
		
		imgPreview.setUrl(tgrUrl);
		
		return row;
	}

	public MaterialRow getImageUploader(int imageIndex) {
		return getImageUploader(GWT.getHostPageBaseURL() + "images/notfound.png", "", imageIndex, null,"");
	}
	
	public void addContent(MaterialWidget child) {
    	child.setVisible(false);
    	this.content.add(child);
    }
	
    public MaterialIcon getIcon() {
		return this.icon;
	}
	
    public void setIconType(IconType iconType) {
		this.icon.setIconType(iconType);
	}

    public void setCotentMarginTop(double i) {
		this.content.setMarginTop(i);
	}

	public static ContentDetailItem getInstance(ContentTreeItem treeItem, boolean b, String cotId, boolean c) {
		
		ContentDetailItem retItem = null;
		
		if (treeItem.getContentType().equals(DatabaseContentType.INPUT_HTML)) {
			retItem = new ContentDetailInputHtml(treeItem, b, cotId, c, false, true);
		}else if (treeItem.getContentType().equals(DatabaseContentType.INPUT_IMAGE)) {
			retItem = new ContentDetailInputImage(treeItem, b, cotId, c, false,null);
		}else if (treeItem.getContentType().equals(DatabaseContentType.INPUT_MOVIE)) {
			retItem = new ContentDetailInputMovie(treeItem, b, cotId, c, false);
		}else if (treeItem.getContentType().equals(DatabaseContentType.INPUT_TEXT)) {
			retItem = new ContentDetailInputText(treeItem, b, cotId, c, false, true);
		}else if (treeItem.getContentType().equals(DatabaseContentType.INPUT_CITY_TOUR_DETAIL)) {
			retItem = new ContentDetailInputCityTourDetail(treeItem, b, cotId, c);
		}else if (treeItem.getContentType().equals(DatabaseContentType.INPUT_COURSE_DETAIL)) {
			retItem = new ContentDetailInputCourseDetail(treeItem, b, cotId, c);
		}else if (treeItem.getContentType().equals(DatabaseContentType.INPUT_ACCOMMODATION_DETAIL)) {
			retItem = new ContentDetailInputAccommodationDetail(treeItem, b, cotId, c);
		}else if (treeItem.getContentType().equals(DatabaseContentType.NONE)) {
			retItem = new ContentDetailInputNone(treeItem, b, cotId, c, false);
		}else if (treeItem.getContentType().equals(DatabaseContentType.INPUT_CLOSING_LINE)) {
			retItem = new ContentDetailInputClosingLine(treeItem, b, cotId, c, false);
		}else if (treeItem.getContentType().equals(DatabaseContentType.INPUT_TRAVEL_INFO)) {
			retItem = new ContentDetailInputTravelInfo(treeItem, b, cotId, c, false);
		}else if (treeItem.getContentType().equals(DatabaseContentType.INPUT_COUPON_INFO)) {
			retItem = new ContentDetailInputCouponInfo(treeItem, b, cotId, c, false);
		}
		
		return retItem;
	}
	
}