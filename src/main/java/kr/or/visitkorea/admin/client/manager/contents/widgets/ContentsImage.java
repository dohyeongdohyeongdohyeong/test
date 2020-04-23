package kr.or.visitkorea.admin.client.manager.contents.widgets;

import com.google.gwt.dom.client.Style.FontWeight;
import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONString;

import gwt.material.design.client.constants.Color;
import gwt.material.design.client.constants.IconType;
import gwt.material.design.client.constants.TextAlign;
import gwt.material.design.client.ui.MaterialIcon;
import gwt.material.design.client.ui.MaterialLabel;
import gwt.material.design.client.ui.MaterialTextBox;
import gwt.material.design.jscore.client.api.JSON;
import gwt.material.design.jscore.client.api.JsObject;
import kr.or.visitkorea.admin.client.application.Console;
import kr.or.visitkorea.admin.client.application.Registry;
import kr.or.visitkorea.admin.client.manager.VisitKoreaBusiness;
import kr.or.visitkorea.admin.client.manager.contents.database.composite.AbtractContents;
import kr.or.visitkorea.admin.client.manager.widgets.ContentTable;
import kr.or.visitkorea.admin.client.manager.widgets.ContentTableRow;
import kr.or.visitkorea.admin.client.manager.widgets.IDUtil;
import kr.or.visitkorea.admin.client.manager.widgets.UploadPanel;
import kr.or.visitkorea.admin.client.widgets.window.MaterialExtentsWindow;

public abstract class ContentsImage extends AbtractContents {
	protected ContentTable imageTable;
	protected UploadPanel uploadPanel;
	protected MaterialLabel imageIdLabel;
	protected MaterialTextBox imageDescLabel;
	protected ContentTableRow selectedRow;
	protected MaterialLabel masterImageLabel;
	protected String masterImageId = null;
	protected MaterialIcon refreshIcon;
	protected MaterialIcon removeIcon;
	protected MaterialIcon masterImageIcon;
	protected MaterialIcon addIcon;
	
	public ContentsImage(MaterialExtentsWindow materialExtentsWindow) {
		super(materialExtentsWindow);
	}

	@Override
	public void init() {
		super.init();
		this.setTitle("연관컨텐츠 :: 컨텐츠에 등록된 이미지");
		buildContent(); 
	}

	private void buildContent() {
		this.setLayoutPosition(Position.RELATIVE);
		
		masterImageLabel = new MaterialLabel();
		masterImageLabel.setLayoutPosition(Position.ABSOLUTE);
		masterImageLabel.setLeft(30);
		masterImageLabel.setTop(60);
		masterImageLabel.setFontWeight(FontWeight.BOLD);
		masterImageLabel.setTextColor(Color.BLUE);
		masterImageLabel.setText("대표이미지 : ");
		this.add(masterImageLabel);
		
		imageTable = new ContentTable(ContentTable.TABLE_SELECT_TYPE.SELECT_SINGLE);
		imageTable.setHeight(435);
		imageTable.setLayoutPosition(Position.ABSOLUTE);
		imageTable.setLeft(30);
		imageTable.setWidth("372px");
		imageTable.setTop(92);
		imageTable.appendTitle("이미지 고유 아이디", 370, TextAlign.CENTER);
		this.add(imageTable);
		
		addIcon = new MaterialIcon(IconType.ADD);
		addIcon.addClickHandler(e -> {
			String uuid = IDUtil.uuid();
			
			ContentTableRow tableRow = imageTable.addRow(Color.WHITE, uuid);
			tableRow.put("IMG_ID", uuid);
			tableRow.put("IMG_DESC", "");
			tableRow.put("NEW_IMAGE", true);
			tableRow.addClickHandler(event -> {
				String imgId = null;
				String imgName = null;
				String imgDesc = tableRow.get("IMG_DESC").toString();

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
				this.imageIdLabel.setText(imgId);
				this.imageDescLabel.setText(imgDesc);
				this.uploadPanel.setImageUrl(sb.toString());
				this.setSelectedRow(tableRow);
			});
		});
		
		refreshIcon = new MaterialIcon(IconType.REFRESH);
		refreshIcon.addClickHandler(e -> {
			this.loadData();
		});
		
		imageTable.getTopMenu().addIcon(refreshIcon, "리로드", com.google.gwt.dom.client.Style.Float.RIGHT, "1.8em", false);
		imageTable.getTopMenu().addIcon(addIcon, "추가", com.google.gwt.dom.client.Style.Float.RIGHT, "1.8em", false);
		
		removeIcon = new MaterialIcon(IconType.DELETE);
		removeIcon.setTextAlign(TextAlign.CENTER);
		removeIcon.addClickHandler(e -> {
			if (imageTable.getSelectedRows().size() == 0)
				return;
			
			this.imageTable.getSelectedRows().get(0).removeFromParent();
			this.selectedRow.put("IS_DELETE", true);
		});
		masterImageIcon = new MaterialIcon(IconType.IMAGE);
		masterImageIcon.setTextAlign(TextAlign.CENTER);
		masterImageIcon.addClickHandler(e -> {
			if (imageTable.getSelectedRows().size() == 0)
				return;
			
			String imgId = this.imageTable.getSelectedRows().get(0).get("IMG_ID").toString();
			this.masterImageId = imgId;
			this.masterImageLabel.setText("대표이미지 : " + this.masterImageId);
		});
		imageTable.getButtomMenu().addIcon(removeIcon, "선택 삭제", com.google.gwt.dom.client.Style.Float.LEFT);
		imageTable.getButtomMenu().addIcon(masterImageIcon, "대표이미지 설정", com.google.gwt.dom.client.Style.Float.LEFT);

		imageIdLabel = new MaterialLabel();
		imageIdLabel.setLayoutPosition(Position.ABSOLUTE);
		imageIdLabel.setRight(30);
		imageIdLabel.setTop(120);
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
		imageDescLabel.setRight(30);
		imageDescLabel.setBottom(40);
		imageDescLabel.setFontSize("1.2em");
		imageDescLabel.setWidth("480px");
		imageDescLabel.setTextAlign(TextAlign.CENTER);
		imageDescLabel.setLabel("이미지 설명");
		imageDescLabel.addValueChangeHandler(e -> {
			this.selectedRow.put("IMG_DESC", e.getValue());
		});
		this.add(imageDescLabel);
		
		uploadPanel = new UploadPanel(480, 300, (String) Registry.get("image.server") + "/img/call");
		uploadPanel.setLayoutPosition(Position.ABSOLUTE);
		uploadPanel.setRight(30);
		uploadPanel.setTop(160);
		uploadPanel.setButtonPostion(false);
		uploadPanel.getUploader().addSuccessHandler(e -> {
			JSONObject resultObj = JSONParser.parseStrict(e.getResponse().getBody()).isObject();
			JSONObject bodyObj = resultObj.get("body").isObject().get("result").isArray().get(0).isObject();

			String saveName = bodyObj.get("saveName").isString().stringValue();
			String NewImgId = bodyObj.get("uuid").isString().stringValue();
			String[] saveNameSplitArr = saveName.split("-");
			StringBuilder sb = new StringBuilder();
			for (String str : saveNameSplitArr) {
				sb.append("/")
				  .append(str.substring(0, 2));
			}
			sb.append("/")
			  .append(saveName);
			
			String imgid = imageIdLabel.getText();
			this.selectedRow.put("IMG_NAME", saveName);
			this.selectedRow.put("IMG_PATH", sb.toString());
			if(selectedRow.get("IMG_UPDATE") == null) {
				
				if(selectedRow.get("NEW_IMAGE") == null) {
						this.selectedRow.put("IMG_UPDATE", imgid);
				} else {
					if((Boolean) selectedRow.get("NEW_IMAGE") == false) {
						this.selectedRow.put("IMG_UPDATE", imgid);
					}
				}
				
			}
			
			this.selectedRow.put("IMG_ID", NewImgId);
			
			if(this.masterImageId == imgid) {
				this.masterImageId = NewImgId;
				this.masterImageLabel.setText("대표이미지 : " + this.masterImageId);
				this.selectedRow.put("MAIN_IMG", true);
			}
			
			imageIdLabel.setText(NewImgId);
			ContentTableRow Target = imageTable.getSelectedRows().get(0);
			Target.getChildrenList().get(0).getElement().setInnerText(NewImgId);
			Target.addClickHandler( event->{
				this.imageIdLabel.setText(NewImgId);
				this.imageDescLabel.setText(selectedRow.get("IMG_DESC").toString());
				this.uploadPanel.setImageUrl(Registry.get("image.server") + "/img/call?cmd=TEMP_VIEW&name=" + saveName);
				this.setSelectedRow(Target);
			});

		});

		this.add(uploadPanel);
		
	}
	
	public void loadData() {
		imageTable.clearRows();
		imageIdLabel.setText("");
		imageDescLabel.setText("");

		JSONObject paramJson = new JSONObject();
		paramJson.put("cmd", new JSONString("SELECT_IMAGE_LIST"));
		paramJson.put("cotId", new JSONString(this.getCotId()));
		paramJson.put("Type", new JSONString("추천"));
		VisitKoreaBusiness.post("call", paramJson.toString(), (param1, param2, param3) -> {
			JSONObject resultObj = JSONParser.parseStrict(JSON.stringify((JsObject) param1)).isObject();
			String process = resultObj.get("header").isObject().get("process").isString().toString();
			process = process.replaceAll("\"", "");

			if (process.equals("success")) {
				JSONArray bodyResultArr = resultObj.get("body").isObject().get("result").isArray();

				int usrCnt = bodyResultArr.size();
				
				for (int i = 0; i < usrCnt; i++) {
					JSONObject obj = bodyResultArr.get(i).isObject();
					String imgId = obj.containsKey("IMG_ID") ? obj.get("IMG_ID").isString().stringValue() : "";
					String imgDesc = obj.containsKey("IMAGE_DESCRIPTION") ? obj.get("IMAGE_DESCRIPTION").isString().stringValue() : "";
					String imgPath = obj.containsKey("IMAGE_PATH") ? obj.get("IMAGE_PATH").isString().stringValue() : "";
					
					ContentTableRow tableRow = imageTable.addRow(Color.WHITE, imgId);
					tableRow.put("IMG_ID", imgId);
					tableRow.put("IMG_DESC", imgDesc);
					tableRow.put("IMG_PATH", imgPath);
					tableRow.addClickHandler(e -> {
						this.imageIdLabel.setText(imgId);
						this.imageDescLabel.setText(imgDesc);
						this.uploadPanel.setImageUrl(Registry.get("image.server") + "/img/call?cmd=VIEW&id=" + imgId);
						this.setSelectedRow(tableRow);
					});
				}
			}
		});
	}

	public ContentTableRow getSelectedRow() {
		return selectedRow;
	}

	public void setSelectedRow(ContentTableRow selectedRow) {
		this.selectedRow = selectedRow;
	}

	@Override
	public void setReadOnly(boolean readFlag) {
		
	}

	public String getMasterImageId() {
		return masterImageId;
	}

	public void setMasterImageId(String masterImageId) {
		this.masterImageId = masterImageId;
		this.masterImageLabel.setText("대표이미지 : " + this.masterImageId);
	}
	
}