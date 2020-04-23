package kr.or.visitkorea.admin.client.manager.contents.database.composite;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.event.dom.client.ClickEvent;
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
import gwt.material.design.client.ui.MaterialTextBox;
import gwt.material.design.jquery.client.api.Functions.Func3;
import gwt.material.design.jscore.client.api.JSON;
import gwt.material.design.jscore.client.api.JsObject;
import kr.or.visitkorea.admin.client.application.Console;
import kr.or.visitkorea.admin.client.application.Registry;
import kr.or.visitkorea.admin.client.manager.VisitKoreaBusiness;
import kr.or.visitkorea.admin.client.manager.memberActivity.widgets.CommentTableRow;
import kr.or.visitkorea.admin.client.manager.widgets.ContentTable;
import kr.or.visitkorea.admin.client.manager.widgets.ContentTableRow;
import kr.or.visitkorea.admin.client.manager.widgets.SelectionPanel;
import kr.or.visitkorea.admin.client.manager.widgets.UploadPanel;
import kr.or.visitkorea.admin.client.widgets.window.MaterialExtentsWindow;

public class ContentsImages extends AbtractContents {

	private ContentTable imageTable;
	private UploadPanel uploadPanel;
	private MaterialLabel imageIdLabel;
	private MaterialTextBox imageDescLabel;
	private MaterialIcon saveIcon;
	private List<HashMap<String, String>> images = new ArrayList<HashMap<String,String>>();
	private List<String> Deleteimages = new ArrayList<String>();
	
	public ContentsImages(MaterialExtentsWindow materialExtentsWindow) {
		super(materialExtentsWindow);
	}

	@Override
	public void setReadOnly(boolean readFlag) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void init() {
		super.init();
		this.setTitle("연관컨텐츠 :: 컨텐츠에 등록된 이미지");
		saveIcon = this.showSaveIconAndGetIcon();
		buildContent(); 
	}
	
	private void buildContent() {
		
		this.setLayoutPosition(Position.RELATIVE);
		
		imageTable = new ContentTable(ContentTable.TABLE_SELECT_TYPE.SELECT_SINGLE);
		imageTable.setHeight(515);
		imageTable.setLayoutPosition(Position.ABSOLUTE);
		imageTable.setLeft(30);
		imageTable.setWidth("482px");
		imageTable.setTop(20);
		imageTable.appendTitle("이미지 고유 아이디", 310, TextAlign.CENTER);
		imageTable.appendTitle("노출 관리", 170, TextAlign.CENTER);
		this.add(imageTable);
		
		//MaterialIcon icon, String tooltip, com.google.gwt.dom.client.Style.Float styleFloat, String fontSize, boolean isBorde

		MaterialIcon icon3 = new MaterialIcon(IconType.DELETE);
		icon3.setTextAlign(TextAlign.CENTER);
		icon3.setVisible(false);
		icon3.addClickHandler(event->{
			if (imageTable.getSelectedRows().size() > 0) {
				ContentTableRow removeTarget = imageTable.getSelectedRows().get(0);
				int removeTargetIndex = imageTable.getRowContainer().getWidgetIndex(removeTarget);
				images.remove(removeTargetIndex);
				Deleteimages.add(imageIdLabel.getText());
				imageTable.getRowContainer().remove(imageTable.getSelectedRows().get(0));
				imageTable.setSelectedIndex(removeTargetIndex);
				tableClickeventMethod(event);
			}
		});
		
		MaterialIcon icon1 = new MaterialIcon(IconType.ARROW_UPWARD);
		icon1.setTextAlign(TextAlign.CENTER);
		icon1.setFontSize("24px");
		icon1.setMargin(0);
		icon1.addClickHandler(event->{
			if (imageTable.getSelectedRows().size() > 0) {
				ContentTableRow IndexUpTarget = imageTable.getSelectedRows().get(0);
				int TargetIndex = imageTable.getRowContainer().getWidgetIndex(IndexUpTarget);
				if (0 < TargetIndex) {
					Collections.swap(images, TargetIndex, TargetIndex - 1);
					refresh();
					imageTable.setSelectedIndex(TargetIndex - 1);
				}
			}
		});
		
		MaterialIcon icon2 = new MaterialIcon(IconType.ARROW_DOWNWARD);
		icon2.setTextAlign(TextAlign.CENTER);
		icon2.setFontSize("24px");
		icon2.setMargin(0);
		icon2.addClickHandler(event->{
			if (imageTable.getSelectedRows().size() > 0) {
					ContentTableRow IndexUpTarget = imageTable.getSelectedRows().get(0);
				int TargetIndex = imageTable.getRowContainer().getWidgetIndex(IndexUpTarget);
				if (imageTable.getRowsList().size() > TargetIndex) {
					Collections.swap(images, TargetIndex, TargetIndex + 1);
					refresh();
					imageTable.setSelectedIndex(TargetIndex + 1);
				}
			}
		});
		
		imageTable.getButtomMenu().addIcon(icon1, "위로", com.google.gwt.dom.client.Style.Float.RIGHT);
		imageTable.getButtomMenu().addIcon(icon2, "아래로", com.google.gwt.dom.client.Style.Float.RIGHT);
		imageTable.getButtomMenu().addIcon(icon3, "삭제", com.google.gwt.dom.client.Style.Float.LEFT);

		imageIdLabel = new MaterialLabel();
		imageIdLabel.setLayoutPosition(Position.ABSOLUTE);
		imageIdLabel.setRight(30);
		imageIdLabel.setTop(48);
		imageIdLabel.setFontSize("1.2em");
		imageIdLabel.setWidth("500px");
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
		imageDescLabel.setRight(40);
		imageDescLabel.setBottom(20);
		imageDescLabel.setFontSize("2.0em");
		imageDescLabel.setWidth("485px");
		imageDescLabel.setTextAlign(TextAlign.CENTER);
		imageDescLabel.setLabel("이미지 설명");
		imageDescLabel.addValueChangeHandler(event->{
			
			if (imageTable.getSelectedRows().size() > 0) {
				ContentTableRow removeTarget = imageTable.getSelectedRows().get(0);
				int removeTargetIndex = imageTable.getRowContainer().getWidgetIndex(removeTarget);
				images.get(removeTargetIndex).put("IMG_DESCRIPTION", imageDescLabel.getText());
			} 
			
		});
		
		this.add(imageDescLabel);
		
		uploadPanel = new UploadPanel(500, 370, (String) Registry.get("image.server") + "/img/call");
		uploadPanel.setLayoutPosition(Position.ABSOLUTE);
		uploadPanel.setRight(30);
		uploadPanel.setTop(88);
		uploadPanel.setButtonPostion(false);
		uploadPanel.setImageUrl("");
		uploadPanel.setEnabled(false);
		uploadPanel.getBtn().setVisible(false);
		uploadPanel.getBtn().setEnabled(false);
		this.add(uploadPanel);
		
		
		saveIcon.addClickHandler(event->{
			
			JSONObject parameterJSON = new JSONObject();
			parameterJSON.put("cmd", new JSONString("UPDATE_CONTENT_IMAGES"));
			parameterJSON.put("cotId", new JSONString(this.getCotId()));
			JSONArray JsonDeleteimages = new JSONArray();
			for (int i = 0; i < Deleteimages.size(); i++) {
				JsonDeleteimages.set(i, new JSONString(Deleteimages.get(i)));
			}
			if(Deleteimages.size() != 0)
			parameterJSON.put("DeleteImages", JsonDeleteimages);
			
			JSONArray Jsonimages = new JSONArray();
			for (int i = 0; i < images.size(); i++) {
				JSONObject Images = new JSONObject();
				Images.put("IMG_ID", new JSONString(images.get(i).get("IMG_ID")));
				Images.put("IMG_DESCRIPTION", new JSONString(images.get(i).get("IMG_DESCRIPTION")));
				Images.put("IMG_ORDER", new JSONNumber(i));
				Images.put("IS_VISIABLE", new JSONNumber(Integer.parseInt(images.get(i).get("IMG_IS_VISIABLE"))));
				
				Jsonimages.set(i, Images);
			}
			parameterJSON.put("Images", Jsonimages);
			
			
			VisitKoreaBusiness.post("call", parameterJSON.toString(), (param1, param2, param3) -> {

			});
			
		});
	}


	public void loadData() {
		
		imageTable.loading(true);
		imageTable.clearRows();
		images.clear();
		Deleteimages.clear();
		uploadPanel.setImageUrl("");
		imageIdLabel.setText("");
		imageDescLabel.setText("");
		
		JSONObject parameterJSON = new JSONObject();
		parameterJSON.put("cmd", new JSONString("SELECT_IMAGE_LIST"));
		parameterJSON.put("cotId", new JSONString(this.getCotId()));
		parameterJSON.put("Type", new JSONString("DB"));
		
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
						
						HashMap<String,String> map = new HashMap<String, String>();
						JSONObject recObj = resultArray.get(i).isObject();
						
						String imgId = getString(recObj, "IMG_ID");
						String imgDesc = getString(recObj, "IMAGE_DESCRIPTION");
						String imgPath = getString(recObj, "IMAGE_PATH");
						int imgVisible = (int)recObj.get("IS_VISIABLE").isNumber().doubleValue();
						String imgOrder = "";
						if (recObj.get("IMG_ORDER") == null)
						 imgOrder = recObj.get("IMG_ORDER").isNumber().toString();
					    
						HashMap<String, Object> viewValueMap = new HashMap<String, Object>();
						viewValueMap.put("노출", 1);
						viewValueMap.put("비노출", 0);
						
						
						
						
						
						ContentTableRow tableRow = imageTable.addRow(
								Color.WHITE, imgId , "");
						
						tableRow.addClickHandler(event->{
							tableClickeventMethod(event);
						});
						
						tableRow.put("IMG_ID", imgId);
						tableRow.put("IMG_DESCRIPTION", imgDesc);
						tableRow.put("IMG_ORDER", imgOrder);
						tableRow.put("IMG_IS_VISIABLE", imgVisible);
						tableRow.put("IMG_PATH", imgPath);
						
						
						
						
						map.put("IMG_ID", imgId);
						map.put("IMG_DESCRIPTION", imgDesc);
						map.put("IMG_ORDER", imgOrder);
						map.put("IMG_IS_VISIABLE", imgVisible+"");
						map.put("IMG_PATH", imgPath);
						images.add(map);
						
						SelectionPanel isViewPanel = new SelectionPanel();
						isViewPanel.setValues(viewValueMap);
						isViewPanel.setSelectionOnSingleMode(imgVisible == 1 ? "노출" : "비노출");
						isViewPanel.addStatusChangeEvent(e -> {
							map.put("IMG_IS_VISIABLE", isViewPanel.getSelectedValue()+"");
						});
						
						MaterialLabel column = (MaterialLabel) tableRow.getColumnObject(1);
						column.setHeight("100%");
						column.setText("");
						column.getElement().getStyle().clearOverflowX();
						column.add(isViewPanel);
						
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
	
	private void refresh() {
		
		imageTable.clearRows();
		
		for (int i=0; i<images.size(); i++) {
			
			ContentTableRow tableRow = imageTable.addRow(
					Color.WHITE, images.get(i).get("IMG_ID"),"");
			
			tableRow.addClickHandler(event->{
				tableClickeventMethod(event);
			});
			
			tableRow.put("IMG_ID", images.get(i).get("IMG_ID"));
			tableRow.put("IMG_DESCRIPTION", images.get(i).get("IMG_DESCRIPTION"));
			tableRow.put("IMG_ORDER", images.get(i).get("IMG_ORDER"));
			tableRow.put("IMG_IS_VISIABLE", images.get(i).get("IMG_IS_VISIABLE"));
			tableRow.put("IMG_PATH", images.get(i).get("IMG_PATH"));
			
			HashMap<String, Object> viewValueMap = new HashMap<String, Object>();
			viewValueMap.put("노출", 1);
			viewValueMap.put("비노출", 0);
			
			
			HashMap<String,String> map = new HashMap<String, String>();
			
			map.put("IMG_ID", images.get(i).get("IMG_ID"));
			map.put("IMG_DESCRIPTION", images.get(i).get("IMG_DESCRIPTION"));
			map.put("IMG_ORDER", images.get(i).get("IMG_ORDER"));
			map.put("IMG_IS_VISIABLE", images.get(i).get("IMG_IS_VISIABLE")+"");
			map.put("IMG_PATH", images.get(i).get("IMG_PATH"));
			images.set(i,map);
			
			SelectionPanel isViewPanel = new SelectionPanel();
			isViewPanel.setValues(viewValueMap);
			isViewPanel.setSelectionOnSingleMode(Integer.parseInt(images.get(i).get("IMG_IS_VISIABLE")) == 1 ? "노출" : "비노출");
			isViewPanel.addStatusChangeEvent(e -> {
				map.put("IMG_IS_VISIABLE", isViewPanel.getSelectedValue()+"");
			});
			
			MaterialLabel column = (MaterialLabel) tableRow.getColumnObject(1);
			column.setHeight("100%");
			column.setText("");
			column.getElement().getStyle().clearOverflowX();
			column.add(isViewPanel);
			
		}
		
	}

	private void tableClickeventMethod(ClickEvent e) {
		
		ContentTableRow tableRow = (ContentTableRow) e.getSource();

		int columnIndex = tableRow.getSelectedColumn();
		
		if (columnIndex == 0) {
			
		imageDescLabel.setEnabled(true);
		uploadPanel.setEnabled(true);
		ContentTableRow cRow = imageTable.getSelectedRows().get(0);
		String IMG_ID = (String) imageTable.getSelectedRows().get(0).get("IMG_ID") != null 
				? (String) imageTable.getSelectedRows().get(0).get("IMG_ID") : "";
		String IMG_DESCRIPTION = (String) imageTable.getSelectedRows().get(0).get("IMG_DESCRIPTION") != null
				? (String) imageTable.getSelectedRows().get(0).get("IMG_DESCRIPTION") : "";
		String IMG_PATH = (String) imageTable.getSelectedRows().get(0).get("IMG_PATH") != null
				? (String) imageTable.getSelectedRows().get(0).get("IMG_PATH") : "";
						
		imageIdLabel.setText(IMG_ID);
		imageDescLabel.setText(IMG_DESCRIPTION);
		uploadPanel.setImageUrl((String) Registry.get("image.server") + "/img/call?cmd=TEMP_VIEW&name="+ IMG_PATH.substring(IMG_PATH.lastIndexOf("/") + 1));
		}
		
	}

}
