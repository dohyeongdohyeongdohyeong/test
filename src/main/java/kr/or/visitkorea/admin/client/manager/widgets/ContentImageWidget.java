package kr.or.visitkorea.admin.client.manager.widgets;

import java.util.List;
import java.util.Map;

import com.google.gwt.dom.client.Style.Float;
import com.google.gwt.dom.client.Style.FontWeight;
import com.google.gwt.dom.client.Style.Overflow;
import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.json.client.JSONNumber;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONString;

import gwt.material.design.client.constants.Color;
import gwt.material.design.client.constants.IconType;
import gwt.material.design.client.constants.InputType;
import gwt.material.design.client.constants.TextAlign;
import gwt.material.design.client.ui.MaterialColumn;
import gwt.material.design.client.ui.MaterialInput;
import gwt.material.design.client.ui.MaterialLabel;
import gwt.material.design.client.ui.MaterialLink;
import gwt.material.design.client.ui.MaterialPanel;
import gwt.material.design.client.ui.MaterialRow;
import gwt.material.design.jquery.client.api.Functions.Func3;
import gwt.material.design.jscore.client.api.JSON;
import gwt.material.design.jscore.client.api.JsObject;
import kr.or.visitkorea.admin.client.application.Console;
import kr.or.visitkorea.admin.client.application.Registry;
import kr.or.visitkorea.admin.client.manager.VisitKoreaBusiness;

public class ContentImageWidget extends MaterialPanel {

	private String url;
	
	private String alt;
	
	private String caption;

	private ContentImageListPanel cil;

	private int itemIndex;

	private String imageId;

	private String uploadValue;
	
	private int iscaption;

	private ImageInformation imgInfo;

	private String desc;

	private List<ItemInformation> list;

	private MaterialLink ALTCheckIcon;

	private MaterialLink CAPTIONCheckIcon;

	private Map<String, Map<String, Object>> idMap;
	
	public ContentImageWidget(List<ItemInformation> list, ItemInformation ii, ContentImageListPanel contentImageListPanel, int itemIndex, String desc, Map<String, Map<String, Object>> idMap) {
		super();
		
		this.list = list;
		this.imgInfo = (ImageInformation)ii;
		this.url = this.imgInfo.getUrl();
		this.imageId = this.imgInfo.getImgId();
		this.alt = this.imgInfo.getComment();
		this.cil = contentImageListPanel;
		this.itemIndex = itemIndex;
		this.caption = this.imgInfo.getCaption();
		this.desc = desc;
		this.iscaption = this.imgInfo.getisCaption();
		this.idMap = idMap;
		render();
	}
	
	public ContentImageWidget getPanel() {
		return this;
	}
	
	public void render() {
		
		this.setTextAlign(TextAlign.LEFT);
		this.setOverflow(Overflow.HIDDEN);
		this.setMargin(5);
		this.setHeight("390px");
		this.setWidth("350px");
//		this.setBorder("1px solid #efefef");
//		this.setBackgroundColor(Color.BLUE_GREY_LIGHTEN_3);
		
		//첫줄  -- 이미지 레이블, 삭제 버튼
		MaterialRow row1 = new MaterialRow();
		row1.getElement().getStyle().setMarginBottom(0,Unit.PX);
		this.add(row1);
		
		// 첫줄 이미지 ID -- IMG_ID 
		MaterialLabel imageIdLabel = new MaterialLabel();
		imageIdLabel.setFontSize("1.1em");
		imageIdLabel.setTextAlign(TextAlign.CENTER);
		imageIdLabel.setLineHeight(30);
		imageIdLabel.setHeight("30px");
		imageIdLabel.setBackgroundColor(Color.GREY_LIGHTEN_4);
		imageIdLabel.setText(this.imageId);
		
		MaterialColumn col1 = new MaterialColumn();
		col1.setGrid("s11");
		col1.setPadding(0);
		col1.add(imageIdLabel);
		row1.add(col1);

		// 첫줄 삭제 버튼 -- BTN Delete
		MaterialLink deleteButton = new MaterialLink();
		deleteButton.setTextAlign(TextAlign.RIGHT);
		deleteButton.setLineHeight(45);
		deleteButton.setMarginTop(5);
		deleteButton.setIconType(IconType.DELETE);
		deleteButton.setHeight("45px");
		deleteButton.addClickHandler(event->{
			
			JSONObject parameterJSON = new JSONObject();
			parameterJSON.put("cmd", new JSONString("DELETE_IMAGE_WIDTH_IMGID"));
			parameterJSON.put("imgId", new JSONString(this.imageId));
 
			VisitKoreaBusiness.post("call", parameterJSON.toString(), new Func3<Object, String, Object>() {

				@Override
				public void call(Object param1, String param2, Object param3) {

					JSONObject resultObj = (JSONObject) JSONParser.parseStrict(JSON.stringify((JsObject)param1));
					JSONObject headerObj = (JSONObject) resultObj.get("header");
					String processResult = headerObj.get("process").isString().toString();
					processResult = processResult.replaceAll("\"", "");
					
					if (processResult.equals("success")) {
						
						JSONObject parameterJSON1 = new JSONObject();
						parameterJSON1.put("cmd", new JSONString("DELETE_ARTICLE_CONTENT_WD_IMGID"));
						parameterJSON1.put("imgId", new JSONString(imageId));

						VisitKoreaBusiness.post("call", parameterJSON1.toString(), new Func3<Object, String, Object>() {

							@Override
							public void call(Object param1, String param2, Object param3) {

								JSONObject resultObj = (JSONObject) JSONParser.parseStrict(JSON.stringify((JsObject)param1));
								JSONObject headerObj = (JSONObject) resultObj.get("header");
								String processResult = headerObj.get("process").isString().toString();
								processResult = processResult.replaceAll("\"", "");
								
								if (processResult.equals("success")) {

									list.remove(imgInfo);
									cil.removeItem(getPanel());
								}
							}
						});

					}
				}
			});
			
		});
		
		MaterialColumn col2 = new MaterialColumn();
		col2.setGrid("s1");
		col2.setPadding(0);
		col2.add(deleteButton);
		row1.add(col2);
		
		
		
		//두 번쩨 줄  -- 이미지 패널
		MaterialRow row2 = new MaterialRow();
		row2.getElement().getStyle().setMarginBottom(0,Unit.PX);
		this.add(row2);

		// 둘째 줄 업로드 이미지
		UploadPanel uploadPanel = new UploadPanel(320, 240, (String) Registry.get("image.server") + "/img/call");
		uploadPanel.setLayoutPosition(Position.ABSOLUTE);
		uploadPanel.setButtonPostion(false);
		uploadPanel.setWidth("320px");
		uploadPanel.setHeight("240px");
		uploadPanel.setImageUrl(this.url);
		uploadPanel.getUploader().addSuccessHandler(event->{
			Console.log("업로드 시작");
			this.imageId = this.imgInfo.getImgId();
			JSONObject resultObj = (JSONObject) JSONParser.parseStrict(event.getResponse().getBody());
			uploadValue = resultObj.get("body").isObject().get("result").isArray().get(0).isObject().get("saveName").isString().stringValue();
			uploadValue = uploadValue.substring(0,  uploadValue.lastIndexOf("."));

			String fullpath = resultObj.get("body").isObject().get("result").isArray().get(0).isObject().get("fullPath").isString().stringValue();
			fullpath = fullpath.replaceAll("data/images/", "");
			String NewImgId = resultObj.get("body").isObject().get("result").isArray().get(0).isObject().get("uuid").isString().stringValue();
			
			// update image path
			JSONObject parameterJSON = new JSONObject();
			parameterJSON.put("cmd", new JSONString("IMAGE_PATH_UPDATE_WITH_IMGID"));
			parameterJSON.put("imgId", new JSONString(imageId));
			parameterJSON.put("imgPath", new JSONString(fullpath));
			parameterJSON.put("NewimgId", new JSONString(NewImgId));
			
			VisitKoreaBusiness.post("call", parameterJSON.toString(), new Func3<Object, String, Object>() {
				
				@Override
				public void call(Object param1, String param2, Object param3) {
					
					JSONObject resultObj = (JSONObject) JSONParser.parseStrict(JSON.stringify((JsObject)param1));
					JSONObject headerObj = (JSONObject) resultObj.get("header");
					String processResult = headerObj.get("process").isString().toString();
					processResult = processResult.replaceAll("\"", "");
					
					if (processResult.equals("success")) {
						imgInfo.setImgId(NewImgId);
						String url2 = (String) Registry.get("image.server") + "/img/call?cmd=VIEW&id="+NewImgId + "&chk=" + IDUtil.uuid(16);
						uploadPanel.setImageUrl(url2);
						imgInfo.setUrl(url2);
						imageIdLabel.setText(NewImgId);
						cil.getcontentDetailInputImage().getrecommContentsDetails().getHost().getcontentsRelatedImage().loadData();
						idMap.get(imageId);
						
						idMap.put(NewImgId, idMap.get(imageId));
						
						Console.log("idMap " + NewImgId + " :: " + idMap.get(NewImgId));
						Console.log("idMap " + imageId + " :: " + idMap.get(imageId));
						Console.log("업로드 종료");
						
						imageId = NewImgId;
					}
				}
				
			});

		
		});
		
		MaterialColumn col3 = new MaterialColumn();
		col3.setGrid("s12");
		col3.add(uploadPanel);
		col3.setHeight("240px");
		row2.add(col3);	

		// 세 번쩨 줄  -- EditBox
		MaterialRow row3 = new MaterialRow();
		row3.getElement().getStyle().setMarginBottom(0,Unit.PX);
		row3.setMarginTop(10);
		this.add(row3);
		
		// 세번 째 줄 텍스트 입력
		
		MaterialLabel inputlabel = new MaterialLabel("알트");
		inputlabel.setHeight("46.25px");
		inputlabel.setLineHeight(46.25);
		inputlabel.setWidth("30px");
		inputlabel.setMarginRight(10);
		inputlabel.setFloat(Float.LEFT);
		inputlabel.setTextColor(Color.BLUE);
		inputlabel.setFontWeight(FontWeight.BOLD);
		MaterialInput inputText = new MaterialInput(InputType.TEXT);
		inputText.setTextAlign(TextAlign.LEFT);
		inputText.setLineHeight(46.25);
		inputText.setHeight("46.25px");
		inputText.setText(this.desc);
		inputText.setWidth("260px");		
		inputText.setFloat(Float.LEFT);
		inputText.addKeyUpHandler(event->{
			// update image path
			JSONObject parameterJSON = new JSONObject();
			parameterJSON.put("cmd", new JSONString("UPDATE_IMAGE_DESCRIPTION"));
			parameterJSON.put("imgId", new JSONString(imageId));
			parameterJSON.put("desc", new JSONString(inputText.getText()));
			this.idMap.get(this.imageId).put("IMAGE_DESCRIPTION", inputText.getValue());
			VisitKoreaBusiness.post("call", parameterJSON.toString(), new Func3<Object, String, Object>() {
				
				@Override
				public void call(Object param1, String param2, Object param3) {
					imgInfo.setComment(inputText.getText());
//					valueMap.put("IMAGE_DESCRIPTION", imgInfo.getComment());
					
				}
				
			});
			
		});
		ALTCheckIcon = new MaterialLink(IconType.CHECK_BOX);
		ALTCheckIcon.setFloat(Float.LEFT);
		ALTCheckIcon.setTextAlign(TextAlign.RIGHT);
		ALTCheckIcon.setMarginTop(11);
		ALTCheckIcon.setHeight("45px");
		ALTCheckIcon.setWidth("25px");
		ALTCheckIcon.addClickHandler(e ->{
			this.idMap.get(this.imageId).put("IS_CAPTION", 0);
			imgInfo.setisCaption(0);
			JSONObject parameterJSON = new JSONObject();
			parameterJSON.put("cmd", new JSONString("UPDATE_IMAGE_IS_CAPTION"));
			parameterJSON.put("imgId", new JSONString(imageId));
			parameterJSON.put("iscaption", new JSONNumber(0));

			VisitKoreaBusiness.post("call", parameterJSON.toString(), new Func3<Object, String, Object>() {
				
				@Override
				public void call(Object param1, String param2, Object param3) {
					ALTCheckIcon.setIconType(IconType.CHECK_BOX);
					CAPTIONCheckIcon.setIconType(IconType.CHECK_BOX_OUTLINE_BLANK);
				}
			});
			
		});
		
		
		MaterialColumn col4 = new MaterialColumn();
		col4.setGrid("s12");
		col4.setMargin(0);
		col4.add(inputlabel);
		col4.add(inputText);
		col4.add(ALTCheckIcon);
		row3.add(col4);
		
		// 네 번쩨 줄  -- EditBox
				MaterialRow row4 = new MaterialRow();
				row4.getElement().getStyle().setMarginBottom(0,Unit.PX);
				this.add(row4);
				
				
		MaterialLabel inputlabel2 = new MaterialLabel("캡션");
		inputlabel2.setHeight("49.25px");
		inputlabel2.setLineHeight(49.25);
		inputlabel2.setWidth("30px");
		inputlabel2.setMarginRight(10);
		inputlabel2.setFloat(Float.LEFT);
		inputlabel2.setFontWeight(FontWeight.BOLD);
		inputlabel2.setTextColor(Color.BLUE);
		// 네번 째 줄 텍스트 입력
		MaterialInput inputText2 = new MaterialInput(InputType.TEXT);
		inputText2.setTextAlign(TextAlign.LEFT);
		inputText2.setLineHeight(46.25);
		inputText2.setHeight("46.25px");
		inputText2.setWidth("260px");
		inputText2.setText(this.caption);
		inputText2.setFloat(Float.LEFT);
		inputText2.addKeyUpHandler(event->{
			this.idMap.get(this.imageId).put("IMAGE_CAPTION", inputText2.getValue());
			imgInfo.setCaption(inputText2.getValue());
			
//			// update image path
//			JSONObject parameterJSON = new JSONObject();
//			parameterJSON.put("cmd", new JSONString("UPDATE_IMAGE_CAPTION"));
//			parameterJSON.put("imgId", new JSONString(imageId));
//			parameterJSON.put("caption", new JSONString(inputText2.getText()));
//
//			VisitKoreaBusiness.post("call", parameterJSON.toString(), new Func3<Object, String, Object>() {
//				
//				@Override
//				public void call(Object param1, String param2, Object param3) {
//					imgInfo.setCaption(inputText2.getText());
//				}
//			});
		});
		
		CAPTIONCheckIcon = new MaterialLink(IconType.CHECK_BOX_OUTLINE_BLANK);
		CAPTIONCheckIcon.setFloat(Float.LEFT);
		CAPTIONCheckIcon.setTextAlign(TextAlign.RIGHT);
		CAPTIONCheckIcon.setMarginTop(11);
		CAPTIONCheckIcon.setHeight("45px");
		CAPTIONCheckIcon.setWidth("25px");
		CAPTIONCheckIcon.addClickHandler(e ->{
			this.idMap.get(this.imageId).put("IS_CAPTION", 1);
			imgInfo.setisCaption(1);
			ALTCheckIcon.setIconType(IconType.CHECK_BOX_OUTLINE_BLANK);
			CAPTIONCheckIcon.setIconType(IconType.CHECK_BOX);
			
			
			JSONObject parameterJSON = new JSONObject();
			parameterJSON.put("cmd", new JSONString("UPDATE_IMAGE_IS_CAPTION"));
			parameterJSON.put("imgId", new JSONString(imageId));
			parameterJSON.put("iscaption", new JSONNumber(1));

			VisitKoreaBusiness.post("call", parameterJSON.toString(), new Func3<Object, String, Object>() {
				
				@Override
				public void call(Object param1, String param2, Object param3) {
					ALTCheckIcon.setIconType(IconType.CHECK_BOX_OUTLINE_BLANK);
					CAPTIONCheckIcon.setIconType(IconType.CHECK_BOX);
				}
			});
			
		});
		
		if(iscaption == 0) {
			ALTCheckIcon.setIconType(IconType.CHECK_BOX);
			CAPTIONCheckIcon.setIconType(IconType.CHECK_BOX_OUTLINE_BLANK);
		} else {
			ALTCheckIcon.setIconType(IconType.CHECK_BOX_OUTLINE_BLANK);
			CAPTIONCheckIcon.setIconType(IconType.CHECK_BOX);
		}
		
		MaterialColumn col5 = new MaterialColumn();
		col5.setMargin(0);
		col5.setGrid("s12");
		col5.add(inputlabel2);
		col5.add(inputText2);
		col5.add(CAPTIONCheckIcon);
		row4.add(col5);
				
	}
	
}
