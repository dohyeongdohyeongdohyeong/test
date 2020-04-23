package kr.or.visitkorea.admin.client.manager.contents.database.dialogs;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gwt.dom.client.Style;
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
import gwt.material.design.client.ui.MaterialButton;
import gwt.material.design.client.ui.MaterialIcon;
import gwt.material.design.client.ui.MaterialLabel;
import gwt.material.design.client.ui.MaterialPanel;
import gwt.material.design.client.ui.MaterialRow;
import gwt.material.design.jquery.client.api.Functions.Func3;
import gwt.material.design.jscore.client.api.JSON;
import gwt.material.design.jscore.client.api.JsObject;
import kr.or.visitkorea.admin.client.application.Registry;
import kr.or.visitkorea.admin.client.manager.VisitKoreaBusiness;
import kr.or.visitkorea.admin.client.manager.widgets.ContentTable;
import kr.or.visitkorea.admin.client.manager.widgets.ContentTableRow;
import kr.or.visitkorea.admin.client.manager.widgets.SelectionPanel;
import kr.or.visitkorea.admin.client.manager.widgets.UploadPanel;
import kr.or.visitkorea.admin.client.widgets.dialog.DialogContent;
import kr.or.visitkorea.admin.client.widgets.window.MaterialExtentsWindow;

public class UpdateAccommodationDialog extends DialogContent {

	private ContentTable table;
	private MaterialPanel ImagePanel;
	private List<HashMap<String, String>> ImageList = new ArrayList<HashMap<String,String>>();
	private List<String> DeleteList = new ArrayList<String>();
	private List<String> VisiableList = new ArrayList<String>();
	private List<String> NotVisiableList = new ArrayList<String>();
	private HashMap<String, HashMap<String,String>> ImageMap = new HashMap<String, HashMap<String,String>>();
	public UpdateAccommodationDialog(MaterialExtentsWindow window) {
		super(window);
	}

	@Override
	public void init() {
		buildContent();
		buildImageArea();
	}

	public void buildContent() {
		addDefaultButtons();

		// dialog title define
		MaterialLabel dialogTitle = new MaterialLabel("객실 이미지 및 순서 변경");
		dialogTitle.setFontSize("1.4em");
		dialogTitle.setFontWeight(FontWeight.BOLD);
		dialogTitle.setTextColor(Color.BLUE);
		dialogTitle.setPaddingTop(10);
		dialogTitle.setPaddingLeft(30);

		this.add(dialogTitle);
		
		this.table = new ContentTable(ContentTable.TABLE_SELECT_TYPE.SELECT_SINGLE, Position.ABSOLUTE);
		this.table.setWidth("300px");
		this.table.setLeft(30);
		this.table.setTop(42);
		this.table.setHeight(459);
		this.table.appendTitle("룸명", 300, TextAlign.CENTER);
		
		MaterialIcon icon3 = new MaterialIcon(IconType.DELETE);
		icon3.setVisible(false);
		icon3.setTextAlign(TextAlign.CENTER);
		icon3.addClickHandler(event->{
			String Code = (String)table.getSelectedRows().get(0).get("ROOM_CODE");
			DeleteList.add(Code);
			table.getRowContainer().remove(table.getSelectedRows().get(0));
			ImageList.remove(table.getRowContainer().getWidgetIndex(table.getSelectedRows().get(0)));
		});

		MaterialIcon icon1 = new MaterialIcon(IconType.ARROW_UPWARD);
		icon1.setTextAlign(TextAlign.CENTER);
		icon1.addClickHandler(event->{
			if (table.getSelectedRows().size() > 0) {
				ContentTableRow IndexUpTarget = table.getSelectedRows().get(0);
				int TargetIndex = table.getRowContainer().getWidgetIndex(IndexUpTarget);
				if (0 < TargetIndex) {
					Collections.swap(ImageList, TargetIndex, TargetIndex - 1);
					refresh();
					table.setSelectedIndex(TargetIndex - 1);
				}
			}
		});
		
		MaterialIcon icon2 = new MaterialIcon(IconType.ARROW_DOWNWARD);
		icon2.setTextAlign(TextAlign.CENTER);
		icon2.addClickHandler(event->{
			if (table.getSelectedRows().size() > 0) {
				ContentTableRow IndexUpTarget = table.getSelectedRows().get(0);
			int TargetIndex = table.getRowContainer().getWidgetIndex(IndexUpTarget);
			if (table.getRowsList().size() > TargetIndex) {
				Collections.swap(ImageList, TargetIndex, TargetIndex + 1);
				refresh();
				table.setSelectedIndex(TargetIndex + 1);
			}
		}
		});
		this.table.setTopMenuDisplay(Display.NONE);
		this.table.getButtomMenu().addIcon(icon3, "룸 삭제", com.google.gwt.dom.client.Style.Float.LEFT);
		this.table.getButtomMenu().addIcon(icon1, "위로", com.google.gwt.dom.client.Style.Float.RIGHT);
		this.table.getButtomMenu().addIcon(icon2, "아래로", com.google.gwt.dom.client.Style.Float.RIGHT);
		
		this.add(this.table);
		
//		this.add(buildTable());
		
		MaterialButton selectButton = new MaterialButton("저장");
		selectButton.setBackgroundColor(Color.RED_LIGHTEN_2);
		selectButton.addClickHandler(event -> {

			
			String cotId = (String)this.getParameters().get("cotId");
			
			JSONObject parameterJSON = new JSONObject();
			parameterJSON.put("cmd", new JSONString("UPDATE_ACCOMMODATION"));
			parameterJSON.put("cotId", new JSONString(cotId));
			
			
			JSONArray JsonDeleteimages = new JSONArray();
			for (int i = 0; i < DeleteList.size(); i++) {
				JsonDeleteimages.set(i, new JSONString(DeleteList.get(i)));
			}
			if(DeleteList.size() != 0)
				parameterJSON.put("DeleteImages", JsonDeleteimages);
			
			JSONArray JsonVisiableImages = new JSONArray();
			for (int i = 0; i < VisiableList.size(); i++) {
				JsonVisiableImages.set(i, new JSONString(VisiableList.get(i)));
			}
			
			if(VisiableList.size() != 0)
				parameterJSON.put("VisiableImages", JsonVisiableImages);
			
			JSONArray JsonNotVisiableImages = new JSONArray();
			for (int i = 0; i < NotVisiableList.size(); i++) {
				JsonNotVisiableImages.set(i, new JSONString(NotVisiableList.get(i)));
			}
			
			if(NotVisiableList.size() != 0)
				parameterJSON.put("NotVisiableImages", JsonNotVisiableImages);
			
			JSONArray Jsonimages = new JSONArray();
			for (int i = 0; i < ImageList.size(); i++) {
				JSONObject Images = new JSONObject();
				Images.put("ROOM_CODE", new JSONString(ImageList.get(i).get("ROOM_CODE") != null ? ImageList.get(i).get("ROOM_CODE") :"" ));
				Images.put("ROOM_IMG1", new JSONString(ImageList.get(i).get("ROOM_IMG1") != null ? ImageList.get(i).get("ROOM_IMG1") :"" ));
				Images.put("ROOM_IMG2", new JSONString(ImageList.get(i).get("ROOM_IMG2") != null ? ImageList.get(i).get("ROOM_IMG2") :"" ));
				Images.put("ROOM_IMG3", new JSONString(ImageList.get(i).get("ROOM_IMG3") != null ? ImageList.get(i).get("ROOM_IMG3") :"" ));
				Images.put("ROOM_IMG4", new JSONString(ImageList.get(i).get("ROOM_IMG4") != null ? ImageList.get(i).get("ROOM_IMG4") :"" ));
				Images.put("ROOM_IMG5", new JSONString(ImageList.get(i).get("ROOM_IMG5") != null ? ImageList.get(i).get("ROOM_IMG5") :"" ));
				Images.put("ROOM_TITLE", new JSONString(ImageList.get(i).get("ROOM_TITLE") != null ? ImageList.get(i).get("ROOM_TITLE") :"" ));
				Images.put("ROOM_ORDER", new JSONNumber(i));
				
				Jsonimages.set(i, Images);
			}
			parameterJSON.put("Images", Jsonimages);
			
			

			VisitKoreaBusiness.post("call", parameterJSON.toString(), new Func3<Object, String, Object>() {
			//VK.post("call", parameterJSON.toString(), new Func3<Object, String, Object>() {

				@Override
				public void call(Object param1, String param2, Object param3) {
					
				}
			});
			
			getMaterialExtentsWindow().closeDialog();

		});

		this.addButton(selectButton);

	}

	private void buildImageArea() {

		
		ImagePanel = new MaterialPanel();
		ImagePanel.setLayoutPosition(Position.ABSOLUTE);
		ImagePanel.setTop(70);
		ImagePanel.setLeft(370);
		ImagePanel.setWidth("550px");
		ImagePanel.setHeight("430px");
		ImagePanel.setBackgroundColor(Color.WHITE);
		this.add(ImagePanel);
		
		ImagePanel.setBorder("1px solid #AAAAAA");
		ImagePanel.setOverflow(Overflow.AUTO);

		
		ImagePanel.add(addTitle());
		
	}

	
	
	private MaterialRow imgBuild(String imgId,int Imgindex) {
		
		MaterialRow ImageRow = new MaterialRow();
		if(ImageMap.containsKey(imgId)) {
			
		HashMap<String, String> map = ImageMap.get(imgId);
		
	
		UploadPanel UploadPanel = new UploadPanel(350, 250, (String) Registry.get("image.server") + "/img/call");
		UploadPanel.getBtn().setVisible(false);
		ImageRow.add(UploadPanel);
		ImageRow.setMarginTop(15);
		ImageRow.setMarginBottom(15);
		UploadPanel.setFloat(Style.Float.LEFT);
		UploadPanel.setMarginLeft(15);
		UploadPanel.setImageUrl((String) Registry.get("image.server") + "/img/call?cmd=TEMP_VIEW&name="+map.get("IMAGE_PATH"));
		
		HashMap<String, Object> viewValueMap = new HashMap<String, Object>();
		viewValueMap.put("노출", 1);
		viewValueMap.put("비노출", 0);
		
		SelectionPanel isViewPanel = new SelectionPanel();
		isViewPanel.setValues(viewValueMap);
		isViewPanel.setSelectionOnSingleMode(Integer.parseInt(map.get("IS_VISIABLE")) == 1 ? "노출" : "비노출");
		isViewPanel.addStatusChangeEvent(e -> {
			map.put("IS_VISIABLE", isViewPanel.getSelectedValue()+"");
			if((int)isViewPanel.getSelectedValue() == 1) {
				VisiableList.add(imgId);
				for (int i = 0; i < NotVisiableList.size(); i++) {
					if(NotVisiableList.get(i) == imgId) NotVisiableList.remove(i);
				}
			} else {
				NotVisiableList.add(imgId);
				for (int i = 0; i < VisiableList.size(); i++) {
					if(VisiableList.get(i) == imgId) VisiableList.remove(i);
				}
			}
				
		});
		
		isViewPanel.setPaddingTop(110);
		ImageRow.add(isViewPanel);
		return ImageRow;
		}
		
		return ImageRow;
	}
	
	
	@Override
	public void setParameters(Map<String, Object> parameters) {
		super.setParameters(parameters);
	}

	@Override
	public int getHeight() {
		return 600;
	}

	
	public void LoadData() {
		
		table.clearRows();
		JSONObject parameterJSON = new JSONObject();
		parameterJSON.put("cmd", new JSONString("GET_ACCOMMODATION"));
		parameterJSON.put("cotId", new JSONString((String)this.getParameters().get("cotId")));
		
		VisitKoreaBusiness.post("call", parameterJSON.toString(), new Func3<Object, String, Object>() {

			@Override
			public void call(Object param1, String param2, Object param3) {
				
				JSONObject resultObj = (JSONObject) JSONParser.parseStrict(JSON.stringify((JsObject)param1));
				JSONObject headerObj = (JSONObject) resultObj.get("header");
				String processResult = headerObj.get("process").isString().stringValue();
				if (processResult.equals("success")) {
					
					ImageList.clear();
					JSONObject bodyObj =  resultObj.get("body").isObject();
					JSONObject resultObj1 = bodyObj.get("result").isObject();
					JSONArray infoArray = resultObj1.get("info").isArray();
					JSONArray imageArray = resultObj1.get("Images").isArray();
					
					for (int i = 0; i < imageArray.size(); i++) {
						
						HashMap<String,String> map2 = new HashMap<String,String>();
						JSONObject object = imageArray.get(i).isObject();
						String IMAGE_PATH = object.get("IMAGE_PATH").isString().stringValue();
						String IS_VISIABLE = object.get("IS_VISIABLE").isNumber().doubleValue()+"";
						String IMG_ID = object.get("IMG_ID").isString().stringValue();
						String IMAGE_DESCRIPTION = object.get("IMAGE_DESCRIPTION").isString().stringValue();
						
						map2.put("IMAGE_PATH", IMAGE_PATH.substring(IMAGE_PATH.lastIndexOf("/") + 1));
						map2.put("IS_VISIABLE", IS_VISIABLE);
						map2.put("IMG_ID", IMG_ID);
						map2.put("IMAGE_DESCRIPTION", IMAGE_DESCRIPTION);
						ImageMap.put(IMG_ID,map2);
						
					}
					
					
					for (int i = 0; i < infoArray.size(); i++) {
						
						JSONObject info = infoArray.get(i).isObject();
						String RoomTitle = info.get("ROOM_TITLE").isString().stringValue();
						
						ContentTableRow tableRow = table.addRow(Color.WHITE, RoomTitle);
						
						tableRow.addClickHandler(event->{
							tableClickeventMethod();
						});
						
						ArrayList<String> imglist = new ArrayList<String>();
						HashMap<String, String> map = new HashMap<String, String>();
						map.put("ROOM_TITLE",info.get("ROOM_TITLE").isString().stringValue());
						map.put("ROOM_CODE",info.get("ROOM_CODE").isString().stringValue());
						if(info.containsKey("ROOM_IMG1")) {
							imglist.add(info.get("ROOM_IMG1").isString().stringValue());
							map.put("ROOM_IMG1", info.get("ROOM_IMG1").isString().stringValue());
						} else 
							map.put("ROOM_IMG1", null);
						
						if(info.containsKey("ROOM_IMG2")) {
							imglist.add(info.get("ROOM_IMG2").isString().stringValue());
							map.put("ROOM_IMG2", info.get("ROOM_IMG2").isString().stringValue());
						} else
							map.put("ROOM_IMG2", null);
						
						if(info.containsKey("ROOM_IMG3")) {
							imglist.add(info.get("ROOM_IMG3").isString().stringValue());
							map.put("ROOM_IMG3", info.get("ROOM_IMG3").isString().stringValue());
						} else
							map.put("ROOM_IMG3", null);
						if(info.containsKey("ROOM_IMG4")) {
							imglist.add(info.get("ROOM_IMG4").isString().stringValue());
							map.put("ROOM_IMG4", info.get("ROOM_IMG4").isString().stringValue());
						} else
							map.put("ROOM_IMG4", null);
						if(info.containsKey("ROOM_IMG5")) {
							imglist.add(info.get("ROOM_IMG5").isString().stringValue());
							map.put("ROOM_IMG5", info.get("ROOM_IMG5").isString().stringValue());
						} else
							map.put("ROOM_IMG5", null);
						
						tableRow.put("ImgList",imglist);
						tableRow.put("ROOM_CODE",info.get("ROOM_CODE").isString().stringValue());
						ImageList.add(map);
					}
				}
			}
		});
		
	}
	
	private void tableClickeventMethod() {
		
		ArrayList<String> Imglist = (ArrayList<String>) table.getSelectedRows().get(0).get("ImgList");
		ImagePanel.clear();
		ImagePanel.add(addTitle());
		for (int i = 0; i < Imglist.size(); i++) {
			ImagePanel.add(imgBuild(Imglist.get(i),i));
		}
		
	}
	
	@Override
	protected void onLoad() {
		super.onLoad();
		LoadData();
	}
	
	private MaterialLabel addTitle() {
		MaterialLabel ImageTitle = new MaterialLabel("이미지 목록");
		ImageTitle.setBackgroundColor(Color.GREY_LIGHTEN_1);
		ImageTitle.setFontWeight(FontWeight.BOLD);
		ImageTitle.setHeight("40px");
		ImageTitle.setTextAlign(TextAlign.CENTER);
		ImageTitle.setPaddingTop(9);
		return ImageTitle;
	}
	
	private void refresh() {
		
		table.clearRows();
		
		
		for (int i=0; i<ImageList.size(); i++) {
			
			ContentTableRow tableRow = table.addRow(
					Color.WHITE, ImageList.get(i).get("ROOM_TITLE") );
			
			tableRow.addClickHandler(event->{
				tableClickeventMethod();
			});
			
			ArrayList<String> imglist = new ArrayList<String>();
			if(ImageList.get(i).containsKey("ROOM_IMG1")) 
				imglist.add(ImageList.get(i).get("ROOM_IMG1"));
			if(ImageList.get(i).containsKey("ROOM_IMG2")) 
				imglist.add(ImageList.get(i).get("ROOM_IMG2"));
			if(ImageList.get(i).containsKey("ROOM_IMG3")) 
				imglist.add(ImageList.get(i).get("ROOM_IMG3"));
			if(ImageList.get(i).containsKey("ROOM_IMG4")) 
				imglist.add(ImageList.get(i).get("ROOM_IMG4"));
			if(ImageList.get(i).containsKey("ROOM_IMG5")) 
				imglist.add(ImageList.get(i).get("ROOM_IMG5"));
			tableRow.put("ImgList",imglist);
			tableRow.put("ROOM_CODE",ImageList.get(i).get("ROOM_CODE"));
			
		}
		
	}

}
