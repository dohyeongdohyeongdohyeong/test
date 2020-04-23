package kr.or.visitkorea.admin.client.manager.contents.recommand.composite;

import com.google.gwt.dom.client.Style;
import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONNumber;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONString;

import gwt.material.design.client.constants.Color;
import gwt.material.design.client.constants.IconType;
import gwt.material.design.client.constants.TextAlign;
import gwt.material.design.client.ui.MaterialIcon;
import gwt.material.design.client.ui.MaterialLabel;
import gwt.material.design.client.ui.MaterialLink;
import gwt.material.design.client.ui.MaterialTextBox;
import gwt.material.design.jquery.client.api.Functions.Func3;
import gwt.material.design.jscore.client.api.JSON;
import gwt.material.design.jscore.client.api.JsObject;
import kr.or.visitkorea.admin.client.application.Console;
import kr.or.visitkorea.admin.client.application.Registry;
import kr.or.visitkorea.admin.client.manager.VisitKoreaBusiness;
import kr.or.visitkorea.admin.client.manager.contents.recommand.RecommContentsTree;
import kr.or.visitkorea.admin.client.manager.widgets.ContentTable;
import kr.or.visitkorea.admin.client.manager.widgets.ContentTableRow;
import kr.or.visitkorea.admin.client.manager.widgets.IDUtil;
import kr.or.visitkorea.admin.client.manager.widgets.UploadPanel;
import kr.or.visitkorea.admin.client.widgets.window.MaterialExtentsWindow;

public class RecommContentsRelatedImage extends AbtractRecommContents {

	private ContentTable imageTable;
	private UploadPanel uploadPanel;
	private MaterialLabel imageIdLabel;
	private MaterialTextBox imageDescLabel;
	private MaterialTextBox imageCaptionLabel;
	private MaterialLink ALTCheckIcon;
	private MaterialLink CaptionCheckIcon;
	private RecommContentsDetails recommContentsDetails;
	private boolean iscaption = false;
	
	public RecommContentsRelatedImage(MaterialExtentsWindow materialExtentsWindow,RecommContentsDetails recommContentsDetails) {
		super(materialExtentsWindow);
		this.recommContentsDetails = recommContentsDetails;
	}

	@Override
	public void init() {
		super.init();
		this.setTitle("연관컨텐츠 :: 컨텐츠에 등록된 이미지 (사용자 사진 제외)");
		buildContent(); 
	}

	private void buildContent() {
		
		this.setLayoutPosition(Position.RELATIVE);
		
		MaterialIcon icon1 = new MaterialIcon(IconType.ADD);
		icon1.addClickHandler(event->{

			String reOrderString = "";
			for (int i = 0; i <imageTable.getRowsList().size(); i++) {
				String ImageId = imageTable.getRowsList().get(i).get("IMG_ID").toString();
				if (i == 0) {
					reOrderString += ( i+1+ "_" + ImageId); 
				}else {
					reOrderString += ( "," + i+1 + "_" + ImageId); 
				}
			}
			
			JSONObject jObj = new JSONObject();
			jObj.put("cmd", new JSONString("UPDATE_ARTICLE_IMAGE_LIST"));
			jObj.put("ORDER", new JSONString(reOrderString));
			jObj.put("COT_ID", new JSONString(getCotId()));
			
			VisitKoreaBusiness.post("call", jObj.toString(), new Func3<Object, String, Object>() {

				@Override
				public void call(Object param1, String param2, Object param3) {

					
					ImageInsert();
					
				}

				
			});
			
		});
		
		MaterialIcon iconRefresh = new MaterialIcon(IconType.REFRESH);
		iconRefresh.addClickHandler(event->{
			loadData();
		});
		this.add(iconRefresh);
		
		imageTable = new ContentTable(ContentTable.TABLE_SELECT_TYPE.SELECT_SINGLE);
		imageTable.setHeight(500);
		imageTable.setLayoutPosition(Position.ABSOLUTE);
		imageTable.setLeft(30);
		imageTable.setWidth("372px");
		imageTable.setTop(45);
		imageTable.appendTitle("이미지 고유 아이디", 370, TextAlign.CENTER);
		this.add(imageTable);
		
		//MaterialIcon icon, String tooltip, com.google.gwt.dom.client.Style.Float styleFloat, String fontSize, boolean isBorde

		imageTable.getTopMenu().addIcon(iconRefresh, "리로드", Style.Float.RIGHT, "1.8em", false );
		imageTable.getTopMenu().addIcon(icon1, "추가", Style.Float.RIGHT, "1.8em", false );
		
		MaterialIcon icon3 = new MaterialIcon(IconType.DELETE);
		icon3.setTextAlign(TextAlign.CENTER);
		icon3.addClickHandler(event->{
			if (imageTable.getSelectedRows().size() > 0) {
				
				int nowIndex = imageTable.getRowContainer().getWidgetIndex(imageTable.getSelectedRows().get(0));
				
				
				JSONObject parameterJSON = new JSONObject();
				if(iscaption) parameterJSON.put("cmd", new JSONString("DELETE_ARTICLE_CONTENT_WD_IMGID"));
				else parameterJSON.put("cmd", new JSONString("DELETE_IMAGE_WIDTH_IMGID"));
				parameterJSON.put("imgId", new JSONString(imageIdLabel.getText()));
					
	
				VisitKoreaBusiness.post("call", parameterJSON.toString(), new Func3<Object, String, Object>() {
	
					@Override
					public void call(Object param1, String param2, Object param3) {
						ContentTableRow removeTarget = imageTable.getSelectedRows().get(0);
						int removeTargetIndex = imageTable.getRowContainer().getWidgetIndex(removeTarget);
						imageTable.getRowContainer().remove(imageTable.getSelectedRows().get(0));
						imageTable.getRowsList().remove(removeTargetIndex);
						imageTable.setSelectedIndex(removeTargetIndex);
						tableClickeventMethod();
						
					}
				});
	
				
			}
		});
		imageTable.getButtomMenu().addIcon(icon3, "선택 삭제", com.google.gwt.dom.client.Style.Float.LEFT);

		imageIdLabel = new MaterialLabel();
		imageIdLabel.setLayoutPosition(Position.ABSOLUTE);
		imageIdLabel.setRight(30);
		imageIdLabel.setTop(73);
		imageIdLabel.setFontSize("1.2em");
		imageIdLabel.setWidth("480px");
		imageIdLabel.setHeight("40px");
		imageIdLabel.setTextAlign(TextAlign.CENTER);
		imageIdLabel.setLineHeight(40);
		imageIdLabel.setBackgroundColor(Color.GREY_LIGHTEN_2);
		this.add(imageIdLabel);

		imageDescLabel = new MaterialTextBox();
		imageDescLabel.getElement().getElementsByTagName("input").getItem(0).getStyle().setProperty("margin", "0");
		imageDescLabel.setLayoutPosition(Position.ABSOLUTE);
		imageDescLabel.setMarginTop(0);
		imageDescLabel.setMarginBottom(0);
		imageDescLabel.setRight(70);
		imageDescLabel.setBottom(90);
		imageDescLabel.setFontSize("1.2em");
		imageDescLabel.setWidth("440px");
		imageDescLabel.setTextAlign(TextAlign.CENTER);
		imageDescLabel.setLabel("이미지 설명");
		imageDescLabel.setEnabled(false);
		imageDescLabel.addValueChangeHandler(event->{
			
			if (imageIdLabel.getText() != null && !imageIdLabel.getText().equals("")) {
				JSONObject parameterJSON = new JSONObject();
				parameterJSON.put("cmd", new JSONString("UPDATE_IMAGE_DESCRIPTION"));
				parameterJSON.put("imgId", new JSONString(imageIdLabel.getText()));
				parameterJSON.put("desc", new JSONString(imageDescLabel.getText()));
	
				VisitKoreaBusiness.post("call", parameterJSON.toString(), new Func3<Object, String, Object>() {
	
					@Override
					public void call(Object param1, String param2, Object param3) {

						ContentTableRow cRow = imageTable.getSelectedRows().get(0);
						cRow.put("IMG_DESCRIPTION", imageDescLabel.getText() );
					}
				});
				
			}
			
		});
		
		this.add(imageDescLabel);
		ALTCheckIcon = new MaterialLink(IconType.CHECK_BOX);
		ALTCheckIcon.setLayoutPosition(Position.ABSOLUTE);
		ALTCheckIcon.setRight(10);
		ALTCheckIcon.setBottom(94);
		ALTCheckIcon.setEnabled(false);
		ALTCheckIcon.getElement().getFirstChildElement().getStyle().setFontSize(2.3, Unit.EM);
		ALTCheckIcon.addClickHandler(e ->{
			if(iscaption) {
				JSONObject parameterJSON = new JSONObject();
				parameterJSON.put("cmd", new JSONString("UPDATE_IMAGE_IS_CAPTION"));
				parameterJSON.put("imgId", new JSONString(imageIdLabel.getText()));
				parameterJSON.put("iscaption", new JSONNumber(0));
	
				VisitKoreaBusiness.post("call", parameterJSON.toString(), new Func3<Object, String, Object>() {
					
					@Override
					public void call(Object param1, String param2, Object param3) {
						ALTCheckIcon.setIconType(IconType.CHECK_BOX);
						CaptionCheckIcon.setIconType(IconType.CHECK_BOX_OUTLINE_BLANK);
						
						ContentTableRow cRow = imageTable.getSelectedRows().get(0);
						cRow.put("IS_CAPTION", 0);
								
					}
				});
			}
		});
		this.add(ALTCheckIcon);
		
		imageCaptionLabel = new MaterialTextBox();
		imageCaptionLabel.getElement().getElementsByTagName("input").getItem(0).getStyle().setProperty("margin", "0");
		imageCaptionLabel.setLayoutPosition(Position.ABSOLUTE);
		imageCaptionLabel.setMarginTop(0);
		imageCaptionLabel.setMarginBottom(0);
		imageCaptionLabel.setRight(70);
		imageCaptionLabel.setBottom(22);
		imageCaptionLabel.setFontSize("1.2em");
		imageCaptionLabel.setWidth("440px");
		imageCaptionLabel.setTextAlign(TextAlign.CENTER);
		imageCaptionLabel.setLabel("이미지 캡션");
		imageCaptionLabel.setEnabled(false);
		imageCaptionLabel.addValueChangeHandler(event->{
			
			if (imageIdLabel.getText() != null && !imageIdLabel.getText().equals("")) {
				JSONObject parameterJSON = new JSONObject();
				parameterJSON.put("cmd", new JSONString("UPDATE_IMAGE_CAPTION"));
				parameterJSON.put("imgId", new JSONString(imageIdLabel.getText()));
				parameterJSON.put("caption", new JSONString(imageCaptionLabel.getText()));
	
				VisitKoreaBusiness.post("call", parameterJSON.toString(), new Func3<Object, String, Object>() {
	
					@Override
					public void call(Object param1, String param2, Object param3) {

						ContentTableRow cRow = imageTable.getSelectedRows().get(0);
						cRow.put("IMG_CAPTION", imageCaptionLabel.getText() );
						
					}
				});
				
			}
			
		});
		this.add(imageCaptionLabel);
		
		CaptionCheckIcon = new MaterialLink(IconType.CHECK_BOX_OUTLINE_BLANK);
		CaptionCheckIcon.setLayoutPosition(Position.ABSOLUTE);
		CaptionCheckIcon.setRight(10);
		CaptionCheckIcon.setBottom(27);
		CaptionCheckIcon.setEnabled(false);
		CaptionCheckIcon.getElement().getFirstChildElement().getStyle().setFontSize(2.3, Unit.EM);
		CaptionCheckIcon.addClickHandler(e ->{
			if(iscaption) {
				JSONObject parameterJSON = new JSONObject();
				parameterJSON.put("cmd", new JSONString("UPDATE_IMAGE_IS_CAPTION"));
				parameterJSON.put("imgId", new JSONString(imageIdLabel.getText()));
				parameterJSON.put("iscaption", new JSONNumber(1));
	
				VisitKoreaBusiness.post("call", parameterJSON.toString(), new Func3<Object, String, Object>() {
					
					@Override
					public void call(Object param1, String param2, Object param3) {
						ALTCheckIcon.setIconType(IconType.CHECK_BOX_OUTLINE_BLANK);
						CaptionCheckIcon.setIconType(IconType.CHECK_BOX);
						
						ContentTableRow cRow = imageTable.getSelectedRows().get(0);
						cRow.put("IS_CAPTION", 1);
						
					}
				});
			}
		});
		this.add(CaptionCheckIcon);
		
		
		uploadPanel = new UploadPanel(480, 300, (String) Registry.get("image.server") + "/img/call");
		uploadPanel.setLayoutPosition(Position.ABSOLUTE);
		uploadPanel.setRight(30);
		uploadPanel.setTop(115);
		uploadPanel.setButtonPostion(false);
		uploadPanel.setImageUrl("");
		uploadPanel.setEnabled(false);
		uploadPanel.getUploader().addSuccessHandler(event->{

			JSONObject resultObj = (JSONObject) JSONParser.parseStrict(event.getResponse().getBody());
			String fullPath = resultObj.get("body").isObject().get("result").isArray().get(0).isObject().get("fullPath").isString().stringValue();
			fullPath = fullPath.replaceAll("/data/images", "");
			String NewImgId = resultObj.get("body").isObject().get("result").isArray().get(0).isObject().get("uuid").isString().stringValue();
			JSONObject parameterJSON = new JSONObject();
			parameterJSON.put("cmd", new JSONString("IMAGE_PATH_UPDATE_WITH_IMGID"));
			parameterJSON.put("imgId", new JSONString(imageIdLabel.getText()));
			parameterJSON.put("NewimgId", new JSONString(NewImgId));
			parameterJSON.put("imgPath", new JSONString(fullPath));

			// image for "image not found"
			// 054bb9a7-772f-4ca7-a20d-921d40de7602.jpg
			
			VisitKoreaBusiness.post("call", parameterJSON.toString(), new Func3<Object, String, Object>() {

				@Override
				public void call(Object param1, String param2, Object param3) {
					String url = (String) Registry.get("image.server") + "/img/call?cmd=VIEW&id=" + NewImgId + "&uuid=" + IDUtil.uuid();
					uploadPanel.setImageUrl(url);
					imageIdLabel.setText(NewImgId);
					ContentTableRow Target = imageTable.getSelectedRows().get(0);
					Target.getChildrenList().get(0).getElement().setInnerText(NewImgId);
					Target.put("IMG_ID", NewImgId);
					Target.addClickHandler( e->{
						tableClickeventMethod();
					});
					
				}
			});
			
		});

		this.add(uploadPanel);
		
	}

	private void ImageInsert() {
		
		String uuid = IDUtil.uuid();
		
		JSONObject parameterJSON = new JSONObject();
		parameterJSON.put("cmd", new JSONString("INSERT_IMAGE"));
		
		parameterJSON.put("cotId", new JSONString(getCotId()));
		parameterJSON.put("imgId", new JSONString(uuid));
		parameterJSON.put("imgPath", new JSONString(""));
		parameterJSON.put("imgDesc", new JSONString(""));
		parameterJSON.put("order", new JSONNumber(0));

		VisitKoreaBusiness.post("call", parameterJSON.toString(), new Func3<Object, String, Object>() {

			@Override
			public void call(Object param1, String param2, Object param3) {
				JSONObject resultObj = (JSONObject) JSONParser.parseStrict(JSON.stringify((JsObject)param1));
				ContentTableRow tableRow = imageTable.addRow( Color.WHITE,  uuid);
				
				tableRow.put("IMG_ID", uuid);
				tableRow.put("IMG_DESCRIPTION", "");

				tableRow.addClickHandler(event->{
					ContentTableRow cRow = imageTable.getSelectedRows().get(0);
					String IMG_ID = (String) imageTable.getSelectedRows().get(0).get("IMG_ID");
					String IMG_DESCRIPTION = (String) imageTable.getSelectedRows().get(0).get("IMG_DESCRIPTION");
					imageIdLabel.setText(IMG_ID);
					imageDescLabel.setText(IMG_DESCRIPTION);
					uploadPanel.setImageUrl((String) Registry.get("image.server") + "/img/call?cmd=VIEW&id="+IMG_ID);
				});
				
				loadData();

			}
		});
	}
	
	@Override
	public void setReadOnly(boolean readFlag) {
		
	}

	public void loadData() {
		
		imageTable.loading(true);

		uploadPanel.setImageUrl("");
		uploadPanel.getBtn().setVisible(false);
		imageIdLabel.setText("");
		imageCaptionLabel.setEnabled(false);
		iscaption = false;
		CaptionCheckIcon.setEnabled(false);
		ALTCheckIcon.setEnabled(false);
		imageDescLabel.setEnabled(false);
		imageDescLabel.setText("");
		
		JSONObject parameterJSON = new JSONObject();
		parameterJSON.put("cmd", new JSONString("SELECT_IMAGE_LIST"));
		parameterJSON.put("Type", new JSONString("추천"));
		parameterJSON.put("cotId", new JSONString(this.getCotId()));
		
		Func3<Object, String, Object> callback = new Func3<Object, String, Object>() {
			 
			@Override 
			public void call(Object param1, String param2, Object param3) {
	
				imageTable.loading(false);

				JSONObject resultObj = (JSONObject) JSONParser.parseStrict(JSON.stringify((JsObject)param1));
				JSONObject headerObj = (JSONObject) resultObj.get("header");
				String processResult = headerObj.get("process").isString().toString();
				processResult = processResult.replaceAll("\"", "");
				
				
				if (processResult.equals("success")) {
					
					JSONObject bodyObj =  resultObj.get("body").isObject();
					JSONArray resultArray = bodyObj.get("result").isArray();

					int usrCnt = resultArray.size();
					
					for (int i=0; i<usrCnt; i++) {
						
						JSONObject recObj = resultArray.get(i).isObject();
						
						String imgId = getString(recObj, "IMG_ID");
						String imgDesc = getString(recObj, "IMAGE_DESCRIPTION");
						int isCaption = -1;
						if (recObj.get("IS_CAPTION") != null)
						isCaption = (int) recObj.get("IS_CAPTION").isNumber().doubleValue();
						
						String imgCaption = getString(recObj, "IMG_CAPTION");
					    
						ContentTableRow tableRow = imageTable.addRow(
								Color.WHITE, imgId );
						
						tableRow.addClickHandler(event->{
							tableClickeventMethod();
						});
						
						tableRow.put("IMG_ID", imgId);
						tableRow.put("IMG_DESCRIPTION", imgDesc);
						tableRow.put("IS_CAPTION", isCaption);
						tableRow.put("IMG_CAPTION", imgCaption);
						
					}
					if(imageTable.getRowsList().size()>0) {
						imageTable.setSelectedIndex(0);
						tableClickeventMethod();
					}
					
				}

			}

			private String getString(JSONObject recObj, String key) {
				return getString(recObj, key, "·");
			}
			
			private String getString(JSONObject recObj, String key, String nullvalue) {
				if (recObj.get(key) == null || recObj.get(key).isString().stringValue().equals("")) return nullvalue;
				else return recObj.get(key).isString().stringValue();
			}


		};
		
		imageTable.clearRows();
		imageTable.setQueryParamter(parameterJSON);
		imageTable.setCallbackFunction(callback);
		imageTable.getData();

	}

	private void tableClickeventMethod() {
		imageDescLabel.setEnabled(true);
		uploadPanel.setEnabled(true);
		imageCaptionLabel.setEnabled(true);
		ContentTableRow cRow = imageTable.getSelectedRows().get(0);
		String IMG_ID = (String) imageTable.getSelectedRows().get(0).get("IMG_ID");
		String IMG_DESCRIPTION = (String) imageTable.getSelectedRows().get(0).get("IMG_DESCRIPTION");
		int IS_CAPTION = (int) imageTable.getSelectedRows().get(0).get("IS_CAPTION");
		String IMG_CAPTION = (String) imageTable.getSelectedRows().get(0).get("IMG_CAPTION");
		Console.log("IS_CAPTION :: " + IS_CAPTION);
		
		if(IS_CAPTION == 0) {
			ALTCheckIcon.setIconType(IconType.CHECK_BOX);
			CaptionCheckIcon.setIconType(IconType.CHECK_BOX_OUTLINE_BLANK);
		} else if( IS_CAPTION == 1){
			ALTCheckIcon.setIconType(IconType.CHECK_BOX_OUTLINE_BLANK);
			CaptionCheckIcon.setIconType(IconType.CHECK_BOX);
		} else {
			ALTCheckIcon.setIconType(IconType.CHECK_BOX_OUTLINE_BLANK);
			CaptionCheckIcon.setIconType(IconType.CHECK_BOX_OUTLINE_BLANK);
		}
		
		if(IS_CAPTION == -1) {
			imageCaptionLabel.setEnabled(false);
			iscaption = false;
			CaptionCheckIcon.setEnabled(false);
			ALTCheckIcon.setEnabled(false);
			uploadPanel.getBtn().setVisible(true);
			
		} else{
			imageCaptionLabel.setEnabled(true);
			iscaption = true;
			CaptionCheckIcon.setEnabled(true);
			ALTCheckIcon.setEnabled(true);
			uploadPanel.getBtn().setVisible(false);
		}
		
		if(IMG_CAPTION == "·") {
			imageCaptionLabel.setText("");
		}else {
			imageCaptionLabel.setText(IMG_CAPTION);
		}
		
		imageIdLabel.setText(IMG_ID);
		imageDescLabel.setText(IMG_DESCRIPTION);
		uploadPanel.setImageUrl((String) Registry.get("image.server") + "/img/call?cmd=VIEW&id="+IMG_ID);
	}

}