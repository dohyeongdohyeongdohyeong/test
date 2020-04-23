package kr.or.visitkorea.admin.client.manager.contents.killercontent.dialogs;

import com.google.gwt.dom.client.Style;
import com.google.gwt.dom.client.Style.Float;
import com.google.gwt.dom.client.Style.FontWeight;
import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONNumber;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONString;

import gwt.material.design.client.base.MaterialWidget;
import gwt.material.design.client.constants.Color;
import gwt.material.design.client.constants.IconType;
import gwt.material.design.client.constants.TextAlign;
import gwt.material.design.client.ui.MaterialButton;
import gwt.material.design.client.ui.MaterialColumn;
import gwt.material.design.client.ui.MaterialIcon;
import gwt.material.design.client.ui.MaterialLabel;
import gwt.material.design.client.ui.MaterialPanel;
import gwt.material.design.client.ui.MaterialRow;
import gwt.material.design.client.ui.MaterialTextBox;
import gwt.material.design.client.ui.MaterialToast;
import gwt.material.design.jquery.client.api.Functions.Func3;
import gwt.material.design.jscore.client.api.JSON;
import gwt.material.design.jscore.client.api.JsObject;
import kr.or.visitkorea.admin.client.application.Registry;
import kr.or.visitkorea.admin.client.manager.VisitKoreaBusiness;
import kr.or.visitkorea.admin.client.manager.contents.killercontent.KillerContentMain;
import kr.or.visitkorea.admin.client.manager.contents.killercontent.model.KillerContentBanner;
import kr.or.visitkorea.admin.client.manager.widgets.ContentTable;
import kr.or.visitkorea.admin.client.manager.widgets.ContentTable.TABLE_SELECT_TYPE;
import kr.or.visitkorea.admin.client.manager.widgets.ContentTableRow;
import kr.or.visitkorea.admin.client.manager.widgets.IDUtil;
import kr.or.visitkorea.admin.client.manager.widgets.UploadPanel;
import kr.or.visitkorea.admin.client.widgets.dialog.DialogContent;
import kr.or.visitkorea.admin.client.widgets.window.MaterialExtentsWindow;

public class BannerSettingDialog extends DialogContent {


	private MaterialLabel Title;
	private ContentTable Table;
	private UploadPanel ImagePanel;
	private MaterialLabel IDLabel;
	private MaterialTextBox DescBox;
	private MaterialTextBox LinkBox;
	private MaterialTextBox NameBox;
	private MaterialIcon SaveIcon;
	private MaterialIcon EditIcon;
	private MaterialIcon addIcon;
	private MaterialIcon DeleteIcon;
	private MaterialLabel leftlabel;
	private MaterialLabel rightlabel;
	
	public BannerSettingDialog(MaterialExtentsWindow window) {
		super(window);
	}

	@Override
	public void init() {
		addDefaultButtons();
		Build();
		DetailBuild();
		LoadData();
	}

	@Override
	protected void onLoad() {
		LoadData();
		
		KillerContentMain host = (KillerContentMain) getParameters().get("host");
		getCloseButton().addClickHandler(event->{
			host.loadData();
		});
		
	}

	@Override
	public int getHeight() {
		// TODO Auto-generated method stub
		return 660;
	}
	
	private void Build() {
		
		Title = new MaterialLabel("킬러컨텐츠 - 배너 설정");
		Title.setFontSize("1.4em");
		Title.setTextColor(Color.BLUE);
		Title.setFontWeight(FontWeight.BOLD);
		Title.setPaddingLeft(15);
		Title.setPaddingTop(15);
		Title.setTextAlign(TextAlign.LEFT);
		this.add(Title);
		
		
		MaterialButton leftBtn = new MaterialButton("좌측 배너 등록");
		leftBtn.setMarginRight(10);
		leftBtn.setFloat(Float.RIGHT);
		leftBtn.addClickHandler(event -> {
			BannerImport(1);
		});
		
		MaterialButton RightBtn = new MaterialButton("우측 배너 등록");
		RightBtn.setMarginRight(10);
		RightBtn.setFloat(Float.RIGHT);
		RightBtn.addClickHandler(event -> {
			BannerImport(2);
		});
		
		
		this.getButtonArea().add(RightBtn);
		this.getButtonArea().add(leftBtn);
		
		Table = new ContentTable(TABLE_SELECT_TYPE.SELECT_SINGLE);
		
		Table.setWidth("300px");
		Table.setHeight(547);
		Table.appendTitle("배너명", 300, TextAlign.CENTER);
		Table.setLayoutPosition(Position.ABSOLUTE);
		Table.setTop(39);
		Table.setLeft(20);
		this.add(Table);
		
		addIcon = new MaterialIcon(IconType.ADD);
		addIcon.setTextAlign(TextAlign.CENTER);
		addIcon.addClickHandler(event->{
			AddBanner();
		});
		
		DeleteIcon = new MaterialIcon(IconType.REMOVE);
		DeleteIcon.setTextAlign(TextAlign.CENTER);
		DeleteIcon.setPaddingRight(3);
		DeleteIcon.addClickHandler(event->{
			
			if(Table.getSelectedRows().size() < 1) {
				return;
			}
			
			KillerContentBanner banner = (KillerContentBanner) Table.getSelectedRows().get(0).get("BANNER");
			if(banner.getStatus() > 0) {
				MaterialToast.fireToast("현재 노출중인 배너는 삭제할 수 없습니다.");
				return;
			}
			
			DeleteBanner();
			
		});
		
		Table.getButtomMenu().addIcon(addIcon, "추가", Style.Float.LEFT, "1.0em", "26px", 24, true );
		Table.getButtomMenu().addIcon(DeleteIcon, "삭제", Style.Float.LEFT, "1.0em", "26px", 24, true );
		
	}
	
	private void DetailBuild() {
		
		MaterialPanel DetailPanel = new MaterialPanel();
		
		DetailPanel.setLayoutPosition(Position.ABSOLUTE);
		DetailPanel.setTop(65);
		DetailPanel.setBorder("1px solid rgb(170, 170, 170)");
		DetailPanel.setRight(20);
		DetailPanel.setWidth("600px");
		DetailPanel.setHeight("520px");
		this.add(DetailPanel);

		MaterialLabel TitleLabel = new MaterialLabel("배너 정보");
		TitleLabel.setWidth("100%");
		TitleLabel.setBackgroundColor(Color.BLUE_DARKEN_1);
		TitleLabel.setHeight("30px");
		TitleLabel.setFontSize("17px");
		TitleLabel.setFontWeight(FontWeight.BOLD);
		TitleLabel.setLineHeight(30);
		TitleLabel.setPaddingLeft(10);
		TitleLabel.setTextColor(Color.WHITE);
		DetailPanel.add(TitleLabel);
		MaterialRow row1 = addRow(DetailPanel);
		ImagePanel = new UploadPanel(550, 215, (String) Registry.get("image.server") + "/img/call");
		ImagePanel.setImageUrl("");
		ImagePanel.setButtonPostion(false);
		ImagePanel.getElement().getStyle().setProperty("margin", "auto");
		row1.add(ImagePanel);
		
		MaterialRow row2 = addRow(DetailPanel);
		
		addLabel(row2, "ID", TextAlign.CENTER, Color.GREY_LIGHTEN_2, "s3");
		
		IDLabel = addLabel(row2, "", TextAlign.CENTER, Color.GREY_LIGHTEN_3, "s9");
		
		MaterialRow row3 = addRow(DetailPanel);
		
		addLabel(row3, "배너명", TextAlign.CENTER, Color.GREY_LIGHTEN_2, "s3");
		
		NameBox = addInputText(row3, "배너를 구분할 배너명을 입력해주세요.",  "s9");
		
		MaterialRow row4 = addRow(DetailPanel);
		
		addLabel(row4, "이미지 링크", TextAlign.CENTER, Color.GREY_LIGHTEN_2, "s3");
		
		LinkBox = addInputText(row4, "링크 URL을 입력해주세요", "s9");
		
		MaterialRow row5 = addRow(DetailPanel);
		
		addLabel(row5, "이미지 설명", TextAlign.CENTER, Color.GREY_LIGHTEN_2, "s3");
		
		DescBox = addInputText(row5, "이미지 설명을 입력해주세요.", "s9");
		
		
		ImagePanel.getBtn().setEnabled(false);
		NameBox.setEnabled(false);
		LinkBox.setEnabled(false);
		DescBox.setEnabled(false);
		
		SaveIcon = new MaterialIcon(IconType.SAVE);
		SaveIcon.setLayoutPosition(Position.ABSOLUTE);
		SaveIcon.setRight(5);
		SaveIcon.setTop(2);
		SaveIcon.setTextColor(Color.WHITE);
		SaveIcon.setVisible(false);
		SaveIcon.addClickHandler(event->{
			onoff(false);
			Save();
		});
		
		EditIcon = new MaterialIcon(IconType.EDIT);
		EditIcon.setLayoutPosition(Position.ABSOLUTE);
		EditIcon.setRight(5);
		EditIcon.setVisible(false);
		EditIcon.setTop(2);
		EditIcon.setTextColor(Color.WHITE);
		EditIcon.addClickHandler(event->{
			onoff(true);
		});
		DetailPanel.add(SaveIcon);
		DetailPanel.add(EditIcon);
	}
	
	private void Save() {
		
		
		KillerContentBanner banner = (KillerContentBanner) Table.getSelectedRows().get(0).get("BANNER");

		if(ImagePanel.getImageId() == null) {
			MaterialToast.fireToast("배너 이미지는 반드시 등록해야 합니다");
			return;
		}
		
		
		if(banner.getImgId() != ImagePanel.getImageId()
			|| banner.getImgdesc() != DescBox.getText()
			|| banner.getLinkurl() != LinkBox.getText()
			|| banner.getTitle() != NameBox.getText()) {
			this.confirm(false,"내용이 변경되었습니다. 실제 데이터에 반영하시겠습니까?", event->{
				if (event.getSource().toString().contains("yes")) {
					
					JSONObject paramObj = new JSONObject();
					paramObj.put("cmd", new JSONString("UPDATE_KILLER_CONTENT_BANNER"));
					if(DescBox.getText() != null & !DescBox.getText().equals(""))
						paramObj.put("desc", new JSONString(DescBox.getText()));
					if(ImagePanel.getImageId() != null)
						paramObj.put("imgId", new JSONString(ImagePanel.getImageId()));
					if(ImagePanel.getImagePath() != null)
						paramObj.put("ImgPath", new JSONString(ImagePanel.getImagePath()));
					if(LinkBox.getText() != null & !LinkBox.getText().equals(""))
						paramObj.put("linkurl", new JSONString(LinkBox.getText()));
					if(NameBox.getText() != null & !NameBox.getText().equals(""))
						paramObj.put("title", new JSONString(NameBox.getText()));
					paramObj.put("status", new JSONNumber(banner.getStatus()));
					paramObj.put("kcbId", new JSONString(banner.getKcbId()));
					
					VisitKoreaBusiness.post("call", paramObj.toString(), new Func3<Object, String, Object>() {
						@Override
						public void call(Object param1, String param2, Object param3) {
							JSONObject resultObj = (JSONObject) JSONParser.parseStrict(JSON.stringify((JsObject)param1));
							JSONObject headerObj = (JSONObject) resultObj.get("header");
							String processResult = headerObj.get("process").isString().stringValue();
							if (processResult.equals("success")) {
								
								banner.setImgdesc(DescBox.getText());
								banner.setLinkurl(LinkBox.getText());
								banner.setTitle(NameBox.getText());
								banner.setImgId(ImagePanel.getImageId());
								banner.setImgPath(ImagePanel.getImagePath());
							}
						}
					});	
				}
			});
		}
	}

	public void LoadData()	{
		
		ImagePanel.setImageUrl("");
		IDLabel.setText("");
		NameBox.setText("");
		LinkBox.setText("");
		DescBox.setText("");
		JSONObject parameterJSON = new JSONObject();
		parameterJSON.put("cmd", new JSONString("SELECT_KILLER_CONTENT_BANNER"));
		parameterJSON.put("status", new JSONNumber(-1));
		
		Table.clearRows();
		VisitKoreaBusiness.post("call", parameterJSON.toString(), new Func3<Object, String, Object>() {
			@Override
			public void call(Object param1, String param2, Object param3) {
				JSONObject resultObj = (JSONObject) JSONParser.parseStrict(JSON.stringify((JsObject)param1));
				JSONObject headerObj = (JSONObject) resultObj.get("header");
				String processResult = headerObj.get("process").isString().stringValue();
				if (processResult.equals("success")) {
					
					JSONObject bodyObj = (JSONObject) resultObj.get("body");
					JSONArray bodyResultObj = (JSONArray) bodyObj.get("result");
					
					int usrCnt = bodyResultObj.size();

					for (int i = 0; i < usrCnt; i++) {
						JSONObject recObj = bodyResultObj.get(i).isObject();
						
						int status = getNumber(recObj, "STATUS").intValue();
						
						
						
						ContentTableRow tableRow = Table.addRow(
								Color.WHITE,
								getString(recObj, "TITLE") );
						
							addBannerLabel(status,tableRow);
						
							tableRow.getChildren().get(0).getElement().addClassName("KillerContentsRow");
							
						KillerContentBanner banner = new KillerContentBanner(
								getString(recObj, "TITLE"), 
								getString(recObj, "KCB_ID"), 
								getString(recObj, "IMG_ID"), 
								getString(recObj, "LINK_URL"), 
								getString(recObj, "IMG_DESC"), 
								null, 
								status);
						tableRow.put("BANNER", banner);
						
						tableRow.addClickHandler(event->{
							onoff(false);
							ImagePanel.setImageId(banner.getImgId());
							ImagePanel.setImageUrl((String) Registry.get("image.server") + "/img/call?cmd=VIEW&id="+banner.getImgId());
							IDLabel.setText(banner.getKcbId());
							NameBox.setText(banner.getTitle());
							LinkBox.setText(banner.getLinkurl());
							DescBox.setText(banner.getImgdesc());
						});
						
					}
				}
			}
		});	
		
	}


	private MaterialRow addRow(MaterialWidget parent) {
		MaterialRow row = new MaterialRow();
		parent.add(row);
		return row;
	}
	
	private MaterialLabel addLabel(MaterialRow row, String defaultValue, TextAlign tAlign, Color bgColor, String grid) {
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
	
	private MaterialTextBox addInputText(MaterialRow row, String placeholder, String grid) {
		MaterialTextBox box = new MaterialTextBox();
		box.getElement().getElementsByTagName("input").getItem(0).getStyle().setProperty("margin", "0");
		box.setPlaceholder(placeholder);
		box.setMarginTop(0);
		box.setMarginBottom(0);
		
		MaterialColumn col1 = new MaterialColumn();
		col1.setGrid(grid);
		col1.add(box);
		row.add(col1);
		return box;
	}
	
	private void AddBanner() {

		String kcbId = IDUtil.uuid();
		JSONObject parameterJSON = new JSONObject();
		parameterJSON.put("cmd", new JSONString("INSERT_KILLER_CONTENT_BANNER"));
		parameterJSON.put("kcbId", new JSONString(kcbId));
		
		VisitKoreaBusiness.post("call", parameterJSON.toString(), new Func3<Object, String, Object>() {
			@Override
			public void call(Object param1, String param2, Object param3) {
				JSONObject resultObj = (JSONObject) JSONParser.parseStrict(JSON.stringify((JsObject)param1));
				JSONObject headerObj = (JSONObject) resultObj.get("header");
				String processResult = headerObj.get("process").isString().stringValue();
				if (processResult.equals("success")) {
					
					ContentTableRow tableRow = Table.addRow(
							Color.WHITE,
							"신규 배너(이름없음)");
					
					tableRow.getChildren().get(0).getElement().addClassName("KillerContentsRow");
					
					KillerContentBanner banner = new KillerContentBanner(
							"신규 배너(이름없음)", 
							kcbId, 
							null, 
							null, 
							null, 
							null, 
							0);
					tableRow.put("BANNER", banner);
					
					tableRow.addClickHandler(event->{
						onoff(false);
						ImagePanel.setImageId(banner.getImgId());
						ImagePanel.setImagePath(banner.getImgPath());
						ImagePanel.setImageUrl((String) Registry.get("image.server") + "/img/call?cmd=VIEW&id="+banner.getImgId());
						IDLabel.setText(banner.getKcbId());
						NameBox.setText(banner.getTitle());
						LinkBox.setText(banner.getLinkurl());
						DescBox.setText(banner.getImgdesc());
					});
					
				}
			}
		});	
	}
	
	private void DeleteBanner() {
		
		this.confirm(false,"해당 배너를 삭제하시겠습니까?", event->{
			if (event.getSource().toString().contains("yes")) {
				
				ContentTableRow removeTarget = Table.getSelectedRows().get(0);
				KillerContentBanner banner = (KillerContentBanner) removeTarget.get("BANNER");
				
				
				JSONObject parameterJSON = new JSONObject();
				parameterJSON.put("cmd", new JSONString("DELETE_KILLER_CONTENT_BANNER"));
				parameterJSON.put("kcbId", new JSONString(banner.getKcbId()));
				
				VisitKoreaBusiness.post("call", parameterJSON.toString(), new Func3<Object, String, Object>() {
					@Override
					public void call(Object param1, String param2, Object param3) {
						JSONObject resultObj = (JSONObject) JSONParser.parseStrict(JSON.stringify((JsObject)param1));
						JSONObject headerObj = (JSONObject) resultObj.get("header");
						String processResult = headerObj.get("process").isString().stringValue();
						if (processResult.equals("success")) {
							Table.getRowContainer().remove(removeTarget);
						}
					}
				});	
				
			}
		});
		
		
	}
	
	private void onoff(boolean check) {
		
		EditIcon.setVisible(!check);
		SaveIcon.setVisible(check);
		NameBox.setEnabled(check);
		LinkBox.setEnabled(check);
		DescBox.setEnabled(check);
		ImagePanel.getBtn().setEnabled(check);
	}
	
	private void BannerImport(int status) {
		
		this.confirm(false,"배너 등록시 바로 구석구석 운영서버에 적용됩니다 그래도 등록하시겠습니까?", event->{
			if (event.getSource().toString().contains("yes")) {
				
				KillerContentBanner banner = (KillerContentBanner) Table.getSelectedRows().get(0).get("BANNER");
				
				banner.setStatus(status);
				
				JSONObject paramObj = new JSONObject();
				paramObj.put("cmd", new JSONString("UPDATE_KILLER_CONTENT_BANNER"));
				paramObj.put("status", new JSONNumber(status));
				paramObj.put("kcbId", new JSONString(banner.getKcbId()));
				
				VisitKoreaBusiness.post("call", paramObj.toString(), new Func3<Object, String, Object>() {
					@Override
					public void call(Object param1, String param2, Object param3) {
						JSONObject resultObj = (JSONObject) JSONParser.parseStrict(JSON.stringify((JsObject)param1));
						JSONObject headerObj = (JSONObject) resultObj.get("header");
						String processResult = headerObj.get("process").isString().stringValue();
						if (processResult.equals("success")) {
								addBannerLabel(status,Table.getSelectedRows().get(0));
						}
					}
				});	
				
			}
		});
	}
	
	public void addBannerLabel(int status, ContentTableRow tableRow) {
			
		
			MaterialLabel label = new MaterialLabel();
			label.setHeight("40px");
			label.setLineHeight(40);
			label.setLayoutPosition(Position.ABSOLUTE);
			label.setTop(0);
			label.setLeft(3);
			label.setTextColor(Color.RED_LIGHTEN_2);
			tableRow.add(label);
			if(status == 1){
				if(leftlabel != null)
					leftlabel.getParent().getElement().removeChild(leftlabel.getParent().getElement().getLastChild());
				label.setText("(좌)");
				leftlabel = label;
			} else if(status == 2){
				if(rightlabel != null)
					rightlabel.getParent().getElement().removeChild(rightlabel.getParent().getElement().getLastChild());
				label.setText("(우)");
				rightlabel = label;
			}
	}
	
	
}
