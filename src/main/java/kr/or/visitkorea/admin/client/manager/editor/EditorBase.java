package kr.or.visitkorea.admin.client.manager.editor;

import com.google.gwt.dom.client.Style.Display;
import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONString;

import gwt.material.design.addins.client.richeditor.MaterialRichEditor;
import gwt.material.design.client.base.MaterialWidget;
import gwt.material.design.client.constants.Color;
import gwt.material.design.client.constants.IconType;
import gwt.material.design.client.constants.TextAlign;
import gwt.material.design.client.ui.MaterialColumn;
import gwt.material.design.client.ui.MaterialIcon;
import gwt.material.design.client.ui.MaterialLabel;
import gwt.material.design.client.ui.MaterialPanel;
import gwt.material.design.client.ui.MaterialRow;
import gwt.material.design.client.ui.MaterialToast;
import gwt.material.design.jquery.client.api.Functions.Func3;
import gwt.material.design.jscore.client.api.JSON;
import gwt.material.design.jscore.client.api.JsObject;
import kr.or.visitkorea.admin.client.application.Console;
import kr.or.visitkorea.admin.client.application.Registry;
import kr.or.visitkorea.admin.client.manager.VisitKoreaBusiness;
import kr.or.visitkorea.admin.client.manager.widgets.ContentTable;
import kr.or.visitkorea.admin.client.manager.widgets.ContentTableRow;
import kr.or.visitkorea.admin.client.manager.widgets.IDUtil;
import kr.or.visitkorea.admin.client.manager.widgets.UploadPanel;

public class EditorBase extends MaterialRichEditor {

	private String CodeViewHtml;
	private Boolean CodeViewClickChk = true;
	private String CotId,NwsId;
	private int mode;
	private ContentTable imageTable;
	private MaterialPanel ImagePanel;
	private MaterialLabel imageIdLabel;
	private UploadPanel uploadPanel;
	private ContentTableRow selectedRow;
	private MaterialIcon addIcon;
	private MaterialIcon removeIcon;
	
	public void codeView() {
		getEditor().find("div[data-event='codeview']").on("click",(e, param1) -> {
			CodeViewHtml = getValue();
				if(CodeViewHtml.indexOf("<") == -1
						&& CodeViewHtml.indexOf(">") == -1) {
					if(CodeViewClickChk == true) {
						String Value = "<p>" + CodeViewHtml + "</p>";
						setValue(Value);
						CodeViewClickChk = false;
						return true;
					} else {
						CodeViewClickChk = true;
					}
				} else {
					return true;
				}

				return false;
	    });
		
	}
	
	
	@Override
	public void load() {
		super.load();
		Setting();
		codeView();
		getCkMediaOptions();
		picture();
	}
	
	
	public void Setting() {
		getEditor().find("div[class='note-toolbar btn-toolbar']").attr("style", "z-index: 0 !important");
	};
	
	public MaterialPanel CreatePanel(String grid,String Height) {
		MaterialPanel EditorPanel = new MaterialPanel();
		EditorPanel.setHeight(Height);
		EditorPanel.setGrid(grid);
		EditorPanel.add(this);
		EditorPanel.setTextAlign(TextAlign.LEFT);
		return EditorPanel;
	}
	
	public void picture() {
		getEditor().find("div[data-event='showImageDialog']").on("click",(e, param1) -> {
			body().find("div[id='ImageTable']").remove();
			
			if(mode == 1) {
				//getContentImage();
				
			} else if(mode == 2)  {
				
				if(GetNwsId() != null) 
					getNewsImage();
				else {
					MaterialToast.fireToast("먼저 소식 추가를 진행해주세요.");
					return false;
				}
			}
			
				return true;
			
	    });
		
	}
	
	
	protected MaterialLabel addLabel(MaterialRow row, String defaultValue, TextAlign tAlign, Color bgColor, String grid) {
		MaterialLabel tmpLabel = new MaterialLabel(defaultValue);
		tmpLabel.setTextAlign(tAlign);
		tmpLabel.setLineHeight(46.25);
		tmpLabel.setHeight("46.25px");
		tmpLabel.setBackgroundColor(bgColor);
		
		MaterialColumn col1 = new MaterialColumn();
		col1.setGrid(grid);
		col1.add(tmpLabel);
		row.add(col1);
		return tmpLabel;
	}
	

	
	
	protected void getNewsImage() {
		
		
		
		imageTable = new ContentTable(ContentTable.TABLE_SELECT_TYPE.SELECT_SINGLE);
		 
		MaterialWidget DialogWidget = new MaterialWidget(getEditor().find("div[class='modal-content']"));

	//	DialogWidget.getElement().removeChild(DialogWidget.getElement().getChild(2));
		DialogWidget.getElement().getChild(2).getChild(0).getParentElement().getStyle().setDisplay(Display.NONE);
		getEditor().find("div[class='modal-content']").
		find("h4").text("이미지 추가");
		getEditor().find("div[class='modal-content']").
		find("input[class='note-image-url']").
		find("label").text("이미지 주소");
		ImagePanel = new MaterialPanel();
		ImagePanel.setId("ImageTable");
		ImagePanel.setHeight("400px");
		ImagePanel.setWidth("850px");
		ImagePanel.setLayoutPosition(Position.RELATIVE);
		ImagePanel.getElement().getStyle().setProperty("margin", "auto");
		ImagePanel.add(imageTable);
		DialogWidget.add(ImagePanel);
		
		imageTable.setHeight(500);
		imageTable.setLayoutPosition(Position.ABSOLUTE);
		imageTable.setWidth("352px");
		imageTable.setTop(20);
		imageTable.appendTitle("이미지 고유 아이디", 350, TextAlign.CENTER);
		
		
		
		MaterialLabel info = new MaterialLabel("- 컨텐츠 이미지 설정");
		info.setLayoutPosition(Position.ABSOLUTE);
		info.setLeft(0);
		info.setTop(10);
		
		ImagePanel.add(info);
		
		imageIdLabel = new MaterialLabel();
		imageIdLabel.setLayoutPosition(Position.ABSOLUTE);
		imageIdLabel.setRight(30);
		imageIdLabel.setTop(48);
		imageIdLabel.setFontSize("1.2em");
		imageIdLabel.setWidth("400px");
		imageIdLabel.setHeight("40px");
		imageIdLabel.setTextAlign(TextAlign.CENTER);
		imageIdLabel.setLineHeight(40);
		imageIdLabel.setBackgroundColor(Color.GREY_LIGHTEN_2);
		
		uploadPanel = new UploadPanel(400, 300, (String) Registry.get("image.server") + "/img/call");
		ImagePanel.add(uploadPanel);
		ImagePanel.add(imageIdLabel);
		
		uploadPanel.setLayoutPosition(Position.ABSOLUTE);
		uploadPanel.setRight(30);
		uploadPanel.setTop(108);
		uploadPanel.setButtonPostion(false);
		uploadPanel.setImageUrl("");
		uploadPanel.getBtn().getElement().setId("UploadButton");
		uploadPanel.getBtn().setVisible(false);
		
		
		uploadPanel.getUploader().addSuccessHandler(event -> {

			JSONObject resultObj = (JSONObject) JSONParser.parseStrict(event.getResponse().getBody());
			String fullPath = resultObj.get("body").isObject().get("result").isArray().get(0).isObject().get("fullPath").isString().stringValue();
			fullPath = fullPath.replaceAll("/data/images", "");
			String NewImgId = resultObj.get("body").isObject().get("result").isArray().get(0).isObject().get("uuid").isString().stringValue();
			JSONObject parameterJSON = new JSONObject();
			String nsiId = selectedRow.get("nsiId").toString();
			parameterJSON.put("cmd", new JSONString("UPDATE_NEWS_IMAGE"));
			parameterJSON.put("imgId", new JSONString(imageIdLabel.getText()));
			parameterJSON.put("NewimgId", new JSONString(NewImgId));
			parameterJSON.put("imgPath", new JSONString(fullPath));
			parameterJSON.put("nsiId", new JSONString(nsiId));
			
			VisitKoreaBusiness.post("call", parameterJSON.toString(), new Func3<Object, String, Object>() {

				@Override
				public void call(Object param1, String param2, Object param3) {
					String url = (String) Registry.get("image.server") + "/img/call?cmd=VIEW&id=" + NewImgId + "&uuid=" + IDUtil.uuid();
					uploadPanel.setImageUrl(url);
					imageIdLabel.setText(NewImgId);
					ContentTableRow Target = imageTable.getSelectedRows().get(0);
					Target.getChildrenList().get(0).getElement().setInnerText(NewImgId);
					Target.put("IMG_ID", NewImgId);
					selectedRow.put("IMG_ID", NewImgId);
					String imgUrl = (String) Registry.get("image.server") + "/img/call?cmd=VIEW&id="+ NewImgId;
					getEditor().find("input[class='note-image-url']").val(imgUrl);
					Target.addClickHandler( e->{
						uploadPanel.setImageUrl(imgUrl);
						imageIdLabel.setText(NewImgId);
						getEditor().find("input[class='note-image-url']").val(imgUrl);
						getEditor().find("input[class='note-image-url']").next().addClass("active");
						getEditor().find("div[class='modal-footer']").children("button").removeClass("disabled");
						getEditor().find("div[class='modal-footer']").children("button").removeAttr("disabled");
					});
					
				}
			});
			
			
			

		});
		
		
		
		
		addIcon = new MaterialIcon(IconType.ADD);
		addIcon.addClickHandler(event->{
			addImage();
		});
		imageTable.getTopMenu().addIcon(addIcon, "추가", com.google.gwt.dom.client.Style.Float.RIGHT, "1.8em", false);
		
		removeIcon = new MaterialIcon(IconType.DELETE);
		removeIcon.setTextAlign(TextAlign.CENTER);
		removeIcon.addClickHandler(e -> {
			DeleteImage();
		});
		
		imageTable.getButtomMenu().addIcon(removeIcon, "선택 삭제", com.google.gwt.dom.client.Style.Float.LEFT);
		
		JSONObject paramJson = new JSONObject();
		paramJson.put("cmd", new JSONString("SELECT_NEWS_IMAGE"));
		paramJson.put("nwsId", new JSONString(this.GetNwsId()));
		
		Func3<Object, String, Object> callback = new Func3<Object, String, Object>() {
			 
			@Override 
			public void call(Object param1, String param2, Object param3) {
				
			JSONObject resultObj = JSONParser.parseStrict(JSON.stringify((JsObject) param1)).isObject();
			String process = resultObj.get("header").isObject().get("process").isString().stringValue();
			
			if (process.equals("success") && resultObj.get("body").isObject().containsKey("result")) {
				
				JSONArray resultArray = resultObj.get("body").isObject().get("result").isArray();
				
				int usrCnt = resultArray.size();
				
				
				for (int i=0; i<usrCnt; i++) {
					
					JSONObject recObj = resultArray.get(i).isObject();
					String imgId = getString(recObj, "IMG_ID");
					String nsiId = getString(recObj, "NSI_ID");
					
					ContentTableRow tableRow = imageTable.addRow(
							Color.WHITE, imgId);
					
					tableRow.put("IMG_ID", imgId);
					tableRow.put("nsiId", nsiId);
					tableRow.put("IMG_DESC", "");
					tableRow.addClickHandler(event->{
						uploadPanel.getBtn().setVisible(true);
						String imgUrl = (String) Registry.get("image.server") + "/img/call?cmd=VIEW&id="+ imgId;
						uploadPanel.setImageUrl(imgUrl);
						setSelectedRow(tableRow);
						imageIdLabel.setText(imgId);
						getEditor().find("input[class='note-image-url']").val(imgUrl);
						getEditor().find("input[class='note-image-url']").next().addClass("active");
						getEditor().find("div[class='modal-footer']").children("button").removeClass("disabled");
						getEditor().find("div[class='modal-footer']").children("button").removeAttr("disabled");
					});
					
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
		imageTable.setQueryParamter(paramJson);
		imageTable.setCallbackFunction(callback);
		imageTable.getData();
		
		
	}
	
	public void SetCotId(String cotId) {
		this.CotId = cotId;
	}

	public void SetNwsId(String NwsId) {
		this.NwsId = NwsId;
	}
	
	public String GetCotId() {
		return this.CotId;
	}
	
	public String GetNwsId() {
		return this.NwsId;
	}
	
	
	public void setMode(String mode) {
		if(mode.equals("CONTENTS"))
			this.mode = 1;
		else if(mode.equals("NEWS"))
			this.mode = 2;
	}
	
	public void addImage() {
		String uuid = IDUtil.uuid();
		String nsiId = IDUtil.uuid();
		JSONObject parameterJSON = new JSONObject();
		parameterJSON.put("cmd", new JSONString("INSERT_NEWS_IMAGE"));
		parameterJSON.put("nwsId", new JSONString(GetNwsId()));
		parameterJSON.put("nsiId", new JSONString(nsiId));
		parameterJSON.put("imgId", new JSONString(uuid));
		parameterJSON.put("imgPath", new JSONString(""));
		parameterJSON.put("imgDesc", new JSONString(""));
		VisitKoreaBusiness.post("call", parameterJSON.toString(), new Func3<Object, String, Object>() {
			@Override
			public void call(Object param1, String param2, Object param3) {
				JSONObject resultObj = (JSONObject) JSONParser.parseStrict(JSON.stringify((JsObject)param1));
				JSONObject headerObj = (JSONObject) resultObj.get("header");
				String processResult = headerObj.get("process").isString().toString();
				processResult = processResult.replaceAll("\"", "");
				if (processResult.equals("fail")) 
					MaterialToast.fireToast("이미지 추가에 실패하였습니다");
				else {
					ContentTableRow tableRow = imageTable.addRow(Color.WHITE, uuid);
					tableRow.put("IMG_ID", uuid);
					tableRow.put("nsiId", nsiId);
					tableRow.put("IMG_DESC", "");
					tableRow.put("NEW_IMAGE", true);
					tableRow.addClickHandler(event -> {
						uploadPanel.getBtn().setVisible(true);
						String imgId = null;
						String imgName = null;
						StringBuilder sb = new StringBuilder()
									.append(Registry.get("image.server"));
						if (tableRow.get("IMG_NAME") == null) {
							imgId = tableRow.get("IMG_ID").toString();
							sb.append("/img/call?cmd=VIEW&id=")
								.append(imgId);
						} else {
							imgName = tableRow.get("IMG_NAME").toString();
							sb.append("/img/call?cmd=TEMP_VIEW&name=")
								.append(imgName);
						}
						imageIdLabel.setText(imgId);
						uploadPanel.setImageUrl(sb.toString());
						setSelectedRow(tableRow);
						String imgUrl = (String) Registry.get("image.server") + "/img/call?cmd=VIEW&id="+ imgId;
						uploadPanel.setImageUrl("");
						imageIdLabel.setText(imgId);
						getEditor().find("input[class='note-image-url']").val(imgUrl);
						getEditor().find("input[class='note-image-url']").next().addClass("active");
						getEditor().find("div[class='modal-footer']").children("button").removeClass("disabled");
						getEditor().find("div[class='modal-footer']").children("button").removeAttr("disabled");
					});
				}
			}
		});
	}
	
	public void DeleteImage() {
		if(selectedRow == null) {
			MaterialToast.fireToast("삭제할 이미지를 선택해주세요");
			return;
		}
		
		String nsiId = selectedRow.get("nsiId").toString();
		String imgId = selectedRow.get("IMG_ID").toString();
		
		if(getValue().indexOf(imgId) != -1) {
			MaterialToast.fireToast("내용에 첨부되어 있는 이미지는 삭제할 수 없습니다.");
			return;
		}
		JSONObject parameterJSON = new JSONObject();
		parameterJSON.put("cmd", new JSONString("DELETE_NEWS_IMAGE"));
		parameterJSON.put("nsiId", new JSONString(nsiId));
		parameterJSON.put("imgId", new JSONString(imgId));
		VisitKoreaBusiness.post("call", parameterJSON.toString(), new Func3<Object, String, Object>() {
			@Override
			public void call(Object param1, String param2, Object param3) {
				JSONObject resultObj = (JSONObject) JSONParser.parseStrict(JSON.stringify((JsObject)param1));
				JSONObject headerObj = (JSONObject) resultObj.get("header");
				String processResult = headerObj.get("process").isString().toString();
				processResult = processResult.replaceAll("\"", "");
				if (processResult.equals("fail")) 
					MaterialToast.fireToast("이미지삭제에 실패하였습니다");
				else {
					
					imageTable.getSelectedRows().get(0).removeFromParent();
					selectedRow.put("IS_DELETE", true);		
					uploadPanel.setImageUrl("");
					imageIdLabel.setText("");
					uploadPanel.getBtn().setVisible(false);
					selectedRow = null;
					MaterialToast.fireToast("이미지삭제에 성공하였습니다");
				}
				
			}
		});
	}
	
	public void setSelectedRow(ContentTableRow selectedRow) {
		this.selectedRow = selectedRow;
	}
	
}

