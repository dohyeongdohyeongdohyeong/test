package kr.or.visitkorea.admin.client.manager.widgets;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Style.FontWeight;
import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.user.client.ui.Widget;

import gwt.material.design.addins.client.fileuploader.MaterialFileUploader;
import gwt.material.design.addins.client.richeditor.MaterialRichEditor;
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
import gwt.material.design.client.ui.MaterialVideo;
import gwt.material.design.jquery.client.api.Functions.Func3;
import gwt.material.design.jscore.client.api.JSON;
import gwt.material.design.jscore.client.api.JsObject;
import kr.or.visitkorea.admin.client.application.Registry;
import kr.or.visitkorea.admin.client.manager.VisitKoreaBusiness;
import kr.or.visitkorea.admin.client.manager.contents.database.DatabaseContentType;

public class ContentDetailCourseItem extends MaterialPanel{

	private MaterialIcon icon;
	
	private MaterialLabel title;
	private MaterialPanel content;
	private String titleName;

	private boolean isDisplayLabel;

	private MaterialPanel contentPreview;

	private MaterialPanel uploaderPanel;
	
	private List<ItemInformation> imageList = new ArrayList<ItemInformation>(); 
	
	private Map<String, ImageInformation> infoMap = new HashMap<String, ImageInformation>();

	private boolean isRenderMode;

	private DatabaseContentType databaseContentType;

	private InputCourseItemDetail inputCourseItemDetail;

	private String key;

	public ContentDetailCourseItem(String title, DatabaseContentType databaseContentType, InputCourseItemDetail inputCourseItemDetail, String key) {
		
		super();
		
		this.titleName = title;
		this.databaseContentType = databaseContentType;
		this.inputCourseItemDetail = inputCourseItemDetail;
		this.key = key;
		this.isDisplayLabel = false;
		
		init();
	}

	private void init() {
		
		this.isRenderMode = true;
		this.setWidth("100%");
		
		if (isDisplayLabel) {
			
			this.icon = new MaterialIcon(IconType.ARCHIVE);
			this.icon.setFloat(com.google.gwt.dom.client.Style.Float.LEFT);
		
			this.title = new MaterialLabel(titleName);
			this.title.setTextAlign(TextAlign.LEFT);
			this.title.setFontSize("1.2em");
			this.title.setFontWeight(FontWeight.BOLD);
			this.title.setFloat(com.google.gwt.dom.client.Style.Float.LEFT);

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
			
			MaterialLabel previewTitle = new MaterialLabel(titleName);
			previewTitle.setTextAlign(TextAlign.LEFT);
			previewTitle.setFontSize("1.1em");
			previewTitle.setPaddingLeft(10);
			previewTitle.setBackgroundColor(Color.GREY_LIGHTEN_2);
	
			MaterialPanel dispContent = new MaterialPanel();
			dispContent.setWidth("100%");
			dispContent.setHeight("40px");
			dispContent.setBorder("1px solid #e0e0e0");
			
			contentPreview.add(previewTitle);
			contentPreview.add(dispContent);
			
			if (this.databaseContentType.equals(DatabaseContentType.INPUT_HTML)) {
				
				MaterialIcon iconEdit = new MaterialIcon(IconType.EDIT);
				iconEdit.setLayoutPosition(Position.ABSOLUTE);
				iconEdit.setTop(0);
				iconEdit.setRight(30);
				iconEdit.addClickHandler(event->{
					if (this.isRenderMode) createComponent(dispContent);
				});
				
				MaterialIcon iconSave = new MaterialIcon(IconType.SAVE);
				iconSave.setLayoutPosition(Position.ABSOLUTE);
				iconSave.setTop(0);
				iconSave.setRight(10);
				iconSave.addClickHandler(event->{
					if (!this.isRenderMode) {
						saveData();
						renderComponent(dispContent);
					}
				});

				contentPreview.add(iconEdit);
				contentPreview.add(iconSave);
				renderComponent(dispContent);
				
			}else if (this.databaseContentType.equals(DatabaseContentType.INPUT_IMAGE)) {
				
				dispContent.setTextAlign(TextAlign.CENTER);
				renderComponent(dispContent);
				
			}else if (this.databaseContentType.equals(DatabaseContentType.INPUT_MOVIE)) {
				
				dispContent.setTextAlign(TextAlign.CENTER);
				
				MaterialIcon iconEdit = new MaterialIcon(IconType.EDIT);
				iconEdit.setLayoutPosition(Position.ABSOLUTE);
				iconEdit.setTop(0);
				iconEdit.setRight(30);
				iconEdit.addClickHandler(event->{
					if (this.isRenderMode) createComponent(dispContent);
				});
				
				MaterialIcon iconSave = new MaterialIcon(IconType.SAVE);
				iconSave.setLayoutPosition(Position.ABSOLUTE);
				iconSave.setTop(0);
				iconSave.setRight(10);
				iconSave.addClickHandler(event->{
					if (!this.isRenderMode) renderComponent(dispContent);
				});

				contentPreview.add(iconEdit);
				contentPreview.add(iconSave);
				renderComponent(dispContent);
				
			}else if (this.databaseContentType.equals(DatabaseContentType.INPUT_TEXT)) {
				
				dispContent.setTextAlign(TextAlign.LEFT);
				
				MaterialIcon iconEdit = new MaterialIcon(IconType.EDIT);
				iconEdit.setLayoutPosition(Position.ABSOLUTE);
				iconEdit.setTop(0);
				iconEdit.setRight(30);
				iconEdit.addClickHandler(event->{
					if (this.isRenderMode) createComponent(dispContent);
				});
				
				MaterialIcon iconSave = new MaterialIcon(IconType.SAVE);
				iconSave.setLayoutPosition(Position.ABSOLUTE);
				iconSave.setTop(0);
				iconSave.setRight(10);
				iconSave.addClickHandler(event->{
					if (!this.isRenderMode) {
						saveData();
						renderComponent(dispContent);
					}
				});

				contentPreview.add(iconEdit);
				contentPreview.add(iconSave);
				renderComponent(dispContent);
					
			}else if (this.databaseContentType.equals(DatabaseContentType.INPUT_COURSE_DETAIL)) {
				
				dispContent.setTextAlign(TextAlign.CENTER);
				
				MaterialIcon iconEdit = new MaterialIcon(IconType.ADD);
				iconEdit.setLayoutPosition(Position.ABSOLUTE);
				iconEdit.setTop(0);
				iconEdit.setRight(30);
				iconEdit.addClickHandler(event->{
					if (this.isRenderMode) createComponent(dispContent);
				});
				
				contentPreview.add(iconEdit);
				renderComponent(dispContent);
					
			}else if (this.databaseContentType.equals(DatabaseContentType.INPUT_ADDITIONAL_INFORMATION)) {
				
				dispContent.setTextAlign(TextAlign.CENTER);
				
				MaterialIcon iconEdit = new MaterialIcon(IconType.EDIT);
				iconEdit.setLayoutPosition(Position.ABSOLUTE);
				iconEdit.setTop(0);
				iconEdit.setRight(30);
				iconEdit.addClickHandler(event->{
					if (this.isRenderMode) createComponent(dispContent);
				});
				
				MaterialIcon iconSave = new MaterialIcon(IconType.SAVE);
				iconSave.setLayoutPosition(Position.ABSOLUTE);
				iconSave.setTop(0);
				iconSave.setRight(10);
				iconSave.addClickHandler(event->{
					if (!this.isRenderMode) renderComponent(dispContent);
				});
	
				contentPreview.add(iconEdit);
				contentPreview.add(iconSave);
				renderComponent(dispContent);
			
			}else {
				
				dispContent.addDoubleClickHandler(event->{
					
					if (this.isRenderMode) createComponent( (MaterialPanel)event.getSource() );
					
				});				
				renderComponent(dispContent);
			}
			
			this.content.add(this.contentPreview);
			
		}
		
		this.add(this.content);
		
	}

	private void saveData() {
		
		String TBL = "COURSE_INFO";
		String COL_TITLE = this.key;
		String COL = this.titleName;
		String COT_ID = this.inputCourseItemDetail.getCotId();
		String itemValue = this.inputCourseItemDetail.getValue(key).isString().stringValue();
		String subContentID = this.inputCourseItemDetail.getValue("SUB_CONTENT_ID").isString().stringValue();
		
		if (itemValue != null) {
			
			JSONObject parameterJSON = new JSONObject();
			parameterJSON.put("cmd", new JSONString("DATABASE_SINGLE_ROW"));
			parameterJSON.put("tbl", new JSONString(TBL));
			parameterJSON.put("col", new JSONString(COL));
			parameterJSON.put("colTitle", new JSONString(COL_TITLE));
			parameterJSON.put("cotId", new JSONString(COT_ID));
			parameterJSON.put("value", new JSONString(itemValue));
			parameterJSON.put("subContentId", new JSONString(subContentID));
			
			VisitKoreaBusiness.post("call", parameterJSON.toString(), new Func3<Object, String, Object>() {
				
				@Override
				public void call(Object param1, String param2, Object param3) {
					
				}
			});
			
		}
		
	}

	private void renderComponent(MaterialPanel dispContent) {
		
		this.isRenderMode = true;
		
		dispContent.clear();
		dispContent.setTextAlign(TextAlign.LEFT);
		dispContent.setHeight("");
		
		
		if (this.databaseContentType.equals(DatabaseContentType.INPUT_HTML)) {
			if (key != null && this.inputCourseItemDetail.getValue(key) != null) {
				String itemValue = this.inputCourseItemDetail.getValue(key).isString().stringValue();
				dispContent.getElement().setInnerHTML(itemValue);
			}else {
				dispContent.getElement().setInnerHTML("");
				this.inputCourseItemDetail.setValue(key, "");
			}
		}else if (this.databaseContentType.equals(DatabaseContentType.INPUT_TEXT)) {
			if (key != null && this.inputCourseItemDetail.getValue(key) != null) {
				String itemValue = this.inputCourseItemDetail.getValue(key).isString().stringValue();
				dispContent.getElement().setInnerText(itemValue);
			}else {
				dispContent.getElement().setInnerText("");
				this.inputCourseItemDetail.setValue(key, "");
			}
		}else if (this.databaseContentType.equals(DatabaseContentType.INPUT_IMAGE)) {
			renderImage(dispContent);
		}
		
	}

	private void renderImage(MaterialPanel dispContent) {
		
		String imgValue = "";
		if (key != null && this.inputCourseItemDetail.getValue(key) != null) {
			imgValue = (String) Registry.get("image.server") + "/img/call?cmd=VIEW&id=" + this.inputCourseItemDetail.getValue(key).isString().stringValue();
		}else {
			imgValue = (String) Registry.get("image.server") + "/img/call?cmd=TEMP_VIEW&name=6b23c237-7a8c-4b50-b550-1553e29129c9.png";
			this.inputCourseItemDetail.setValue(key, imgValue);
		}
		
		UploadPanel uploadPanel = new UploadPanel(295, 196, (String) Registry.get("image.server") + "/img/call");
		uploadPanel.setLayoutPosition(Position.ABSOLUTE);
		uploadPanel.setLeft(0);
		uploadPanel.setTop(0);
		uploadPanel.setButtonPostion(false);
		uploadPanel.setWidth("295px");
		uploadPanel.setHeight("196px");
		uploadPanel.setImageUrl(imgValue);
		uploadPanel.getUploader().addSuccessHandler(event->{
			
			JSONObject resultObj = (JSONObject) JSONParser.parseStrict(event.getResponse().getBody());
			String uploadValue = resultObj.get("body").isObject().get("result").isArray().get(0).isObject().get("saveName").isString().stringValue();
			
			String url = (String) Registry.get("image.server") + "/img/call?cmd=TEMP_VIEW&name=" + uploadValue;
			uploadPanel.setImageUrl(url);

			String uuid = uploadValue.substring(0, 36);
			String ext = uploadValue.substring(37);
			
			String cotId = this.inputCourseItemDetail.getCotId();
			String subContentID = this.inputCourseItemDetail.getValue("SUB_CONTENT_ID").isString().stringValue();
			
			JSONObject parameterJSON = new JSONObject();
			parameterJSON.put("cmd", new JSONString("UPDATE_IMAGE_COURSE_INFO"));
			parameterJSON.put("cotId", new JSONString(cotId));
			parameterJSON.put("subContentId", new JSONString(subContentID));
			parameterJSON.put("uuid", new JSONString(uuid));
			parameterJSON.put("ext", new JSONString(ext));

			VisitKoreaBusiness.post("call", parameterJSON.toString(), new Func3<Object, String, Object>() {

				@Override
				public void call(Object param1, String param2, Object param3) {
					
					JSONObject resultObj = (JSONObject) JSONParser.parseStrict(JSON.stringify((JsObject)param1));
					JSONObject headerObj = (JSONObject) resultObj.get("header");
					String processResult = headerObj.get("process").isString().toString();
					processResult = processResult.replaceAll("\"", "");
					if (processResult.equals("success")) {

						JSONObject bodyObj = resultObj.get("body").isObject();
						JSONArray resltObj = bodyObj.get("result").isArray();
					}
				}
			});	
		});

		dispContent.add(uploadPanel);
	
	}

	private void createComponent(MaterialPanel tgrPanel) {
		
		this.isRenderMode = false;
		
		if (this.databaseContentType.equals(DatabaseContentType.INPUT_TEXT)) {
			
			tgrPanel.clear();
			tgrPanel.getElement().setInnerText("");
			tgrPanel.setTextAlign(TextAlign.LEFT);

			if (this.inputCourseItemDetail.getValue(key) == null) this.inputCourseItemDetail.setValue(key, "");		
			
			MaterialTextBox box = new MaterialTextBox();
			box.setTop(-30);
			box.setPadding(5);
			box.setText(this.inputCourseItemDetail.getValue(key).isString().stringValue());
			box.addValueChangeHandler(boxEvent->{
				SafeHtmlBuilder boxContentBuilder = new SafeHtmlBuilder();
				boxContentBuilder.appendHtmlConstant(box.getText());
				this.inputCourseItemDetail.setValue(key, boxContentBuilder.toString());
			});

			tgrPanel.setHeight("40px");
			tgrPanel.add(box);
			
			box.setFocus(true);
			
		}else if (this.databaseContentType.equals(DatabaseContentType.INPUT_HTML)) {
			
			tgrPanel.clear();
			tgrPanel.getElement().setInnerText("");
			
			if (this.inputCourseItemDetail.getValue(key) == null) this.inputCourseItemDetail.setValue(key, "");		
			
			MaterialRichEditor box = new MaterialRichEditor();
			box.setAirMode(true);
			box.setHeight("100px");
			box.setTop(-30);
			box.setPadding(5);
			box.setHTML(this.inputCourseItemDetail.getValue(key).isString().stringValue());
			box.addValueChangeHandler(boxEvent->{
				SafeHtmlBuilder boxContentBuilder = new SafeHtmlBuilder();
				boxContentBuilder.appendHtmlConstant(box.getText());
				this.inputCourseItemDetail.setValue(key, box.getHTML());
			});
			
			tgrPanel.setHeight("");
			tgrPanel.add(box);
			
			box.setFocus(true);
			
		}else if (this.databaseContentType.equals(DatabaseContentType.INPUT_IMAGE)) {
			
			tgrPanel.clear();
			tgrPanel.getElement().setInnerText("");
			tgrPanel.setHeight("");
		
		}else if (this.databaseContentType.equals(DatabaseContentType.INPUT_MOVIE)) {
			
			tgrPanel.clear();
			tgrPanel.setHeight("");
			tgrPanel.getElement().setInnerText("");
			
			MaterialVideo player = new MaterialVideo();
			
			MaterialTextBox box = new MaterialTextBox();
			box.setText(player.getUrl());
			box.setMarginTop(20);
			box.setLeft(30);
			box.setWidth("830px");
			box.setLabel("URL");
			box.setPlaceholder("동영상의 URL 을 입력해 주세요.");
		
			tgrPanel.add(player);
			tgrPanel.add(box);
			
		}else if (this.databaseContentType.equals(DatabaseContentType.INPUT_ADDITIONAL_INFORMATION)) {
			
			tgrPanel.clear();
			tgrPanel.getElement().setInnerText("");
			
			AdditionalInformationPanel additionalPanel = new AdditionalInformationPanel();
			additionalPanel.setPadding(5);
			
			tgrPanel.setHeight("");
			tgrPanel.add(additionalPanel);
			
			
		}else if (this.databaseContentType.equals(DatabaseContentType.INPUT_COURSE_DETAIL)) {
			
			tgrPanel.clear();
			tgrPanel.getElement().setInnerText("");
			
			MaterialPanel courseDetailPanel = new MaterialPanel();
			courseDetailPanel.setPadding(5);
			
			tgrPanel.setHeight("");
			tgrPanel.add(courseDetailPanel);
			
		}
		
	}

	private MaterialImage getImageBtton(int top, int left, String height, String width) {
		
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

	private MaterialRow getImageUploader(String tgrUrl, String tgrComment, int imageIndex, ImageInformation imageInfo, String tgrCaption) {
		
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
		uploader.setUrl("http://211.254.216.169/img/call");
		uploader.setPreview(false);
		uploader.setMaxFileSize(20);  
		uploader.setShadow(0);
		uploader.setClickable(uniqueId);
		
		MaterialCard card = new MaterialCard();
		MaterialCardImage cardImage = new MaterialCardImage();
		cardImage.setBorder("1px solid #e0e0e0");
		cardImage.setTextAlign(TextAlign.CENTER);
		
		MaterialImage imgPreview = new MaterialImage();
/*
		if (item.getEditorValue() != null && item.getEditorValue().length() > 0) {
			imgPreview.setUrl(item.getEditorValue());
		}
*/		
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

	private MaterialRow getImageUploader(int imageIndex) {
		
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
	
	
}