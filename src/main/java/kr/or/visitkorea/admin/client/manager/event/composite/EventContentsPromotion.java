package kr.or.visitkorea.admin.client.manager.event.composite;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONString;

import gwt.material.design.client.base.MaterialWidget;
import gwt.material.design.client.constants.ButtonType;
import gwt.material.design.client.constants.Color;
import gwt.material.design.client.constants.TextAlign;
import gwt.material.design.client.constants.WavesType;
import gwt.material.design.client.ui.MaterialButton;
import gwt.material.design.client.ui.MaterialColumn;
import gwt.material.design.client.ui.MaterialLabel;
import gwt.material.design.client.ui.MaterialLink;
import gwt.material.design.client.ui.MaterialPanel;
import gwt.material.design.client.ui.MaterialRow;
import gwt.material.design.client.ui.MaterialTab;
import gwt.material.design.client.ui.MaterialTabItem;
import gwt.material.design.jscore.client.api.JSON;
import gwt.material.design.jscore.client.api.JsObject;
import kr.or.visitkorea.admin.client.application.Console;
import kr.or.visitkorea.admin.client.manager.VisitKoreaBusiness;
import kr.or.visitkorea.admin.client.manager.event.EventContentsTree;
import kr.or.visitkorea.admin.client.manager.event.EventStatus;
import kr.or.visitkorea.admin.client.manager.event.widgets.FetchCallback;
import kr.or.visitkorea.admin.client.manager.widgets.IDUtil;
import kr.or.visitkorea.admin.client.manager.widgets.UploadPanelWithNoImage;
import kr.or.visitkorea.admin.client.widgets.window.MaterialExtentsWindow;

public class EventContentsPromotion extends AbstractEventContents {
	private EventContentsTree host;
	private MaterialRow uploadResultRow;
	private MaterialButton uploadBtn;
	private List<HashMap<String, Object>> fileList = new ArrayList<HashMap<String,Object>>();
	
	public EventContentsPromotion(EventContentsTree host, MaterialExtentsWindow window) {
		super(window);
		this.host = host;
	}

	@Override
	public void init() {
		super.init();
		this.setTitle("홍보배너");
	}

	@Override
	public MaterialWidget render() {
		MaterialPanel panel = new MaterialPanel();
		panel.setPaddingTop(15);
		
		MaterialRow row1 = addRow(panel);
		MaterialLabel bannerLabel = addLabel(row1, "배너등록", TextAlign.CENTER, Color.GREY_LIGHTEN_3, "s2");
		bannerLabel.setLineHeight(180);
		bannerLabel.setHeight("180px");
		
		MaterialColumn rightColumn = new MaterialColumn();
		rightColumn.setGrid("s9");
		row1.add(rightColumn);
		
		MaterialRow uploadRow = addRow(rightColumn);
		MaterialLabel fileNameLabel = addLabel(uploadRow, "", TextAlign.LEFT, Color.WHITE, "s9");
		
		uploadResultRow = addRow(rightColumn);
		uploadResultRow.setHeight("113px");
		rightColumn.add(uploadResultRow);
		
		MaterialColumn col10 = new MaterialColumn();
		col10.setGrid("s3");
		uploadRow.add(col10);
		
		UploadPanelWithNoImage bannerFileBtn = new UploadPanelWithNoImage(0, 0, "call?cmd=UNZIP_EVENT_BANNER");
		bannerFileBtn.setWidth("100%");

		uploadBtn = bannerFileBtn.getBtn();
		uploadBtn.setHeight("46.25px");
		uploadBtn.setWidth("200px");
		uploadBtn.setType(ButtonType.FLAT);
		uploadBtn.setTop(-8);
		uploadBtn.setPadding(0);
		uploadBtn.setText("찾아보기");
		uploadBtn.remove(0);
		uploadBtn.setLineHeight(0);
		uploadBtn.setTextColor(Color.WHITE);
		uploadBtn.setRight(-110);
		uploadBtn.setTextAlign(TextAlign.CENTER);
		bannerFileBtn.getUploader().setAcceptedFiles(".zip, .7z, .egg, .alz");
		bannerFileBtn.getUploader().addSuccessHandler(event -> {
			JSONObject resultObj = JSONParser.parseStrict(event.getResponse().getBody()).isObject();
			
			fileNameLabel.setText(event.getTarget().getName().toString());
			
			JSONArray files = resultObj.get("body").isObject().get("files").isArray();
			Console.log(files.toString());	
			
			this.fileList.clear();
			
			for (int i = 0; i < files.size(); i++) {
				JSONObject fileObj = files.get(i).isObject();
				String imgId = fileObj.get("uuid").isString().stringValue();
				String imgPath = fileObj.get("fullPath").isString().stringValue();
				String fileName = fileObj.get("fileName").isString().stringValue();
				String upload = fileObj.get("upload").isString().stringValue();

				HashMap<String, Object> file = new HashMap<String, Object>();
				file.put("banId", IDUtil.uuid());
				file.put("imgId", imgId);
				file.put("fileName", fileName);
				file.put("imgPath", imgPath);
				file.put("upload", upload);
				
				this.addFile(file);
			}
			this.renderFiles();
		});
		col10.add(bannerFileBtn);

		MaterialRow bannerLabelRow = addRow(panel);
		addLabel(bannerLabelRow, "# 배너 이미지 등록 안내", TextAlign.LEFT, Color.GREY_LIGHTEN_5, "s12");
		
		this.buildBannerInformation(panel);
		return panel;
	}

	@Override
	public void loadData(FetchCallback callback) {
		JSONObject paramJson = new JSONObject();
		paramJson.put("cmd", new JSONString("GET_EVENT_PROMOTION"));
		paramJson.put("evtId", new JSONString(this.getEvtId()));
		paramJson.put("cotId", new JSONString(this.getCotId()));
		VisitKoreaBusiness.post("call", paramJson.toString(), (param1, param2, param3) -> {
			JSONObject resultObj = JSONParser.parseStrict(JSON.stringify((JsObject) param1)).isObject();
			String process = resultObj.get("header").isObject().get("process").isString().stringValue();
			
			if (process.equals("success")) {
				JSONArray bodyResultArr = resultObj.get("body").isObject().get("result").isArray();
				
				for (int i = 0; i < bodyResultArr.size(); i++) {
					JSONObject obj = bodyResultArr.get(i).isObject();

					String banId = obj.get("BAN_ID").isString().stringValue();
					String imgId = obj.get("IMG_ID").isString().stringValue();
					String imgPath = obj.get("IMAGE_PATH").isString().stringValue();
					String fileName = imgPath.substring(imgPath.lastIndexOf("/") + 1);
					String upload = "success";
					
					HashMap<String, Object> file = new HashMap<String, Object>();
					file.put("banId", banId);
					file.put("imgId", imgId);
					file.put("fileName", fileName);
					file.put("imgPath", imgPath);
					file.put("upload", upload);
					
					this.addFile(file);
				}
				
				this.renderFiles();
			}
		});
	}

	@Override
	public void saveData() {
		JSONObject paramJson = new JSONObject();
		paramJson.put("cmd", new JSONString("SAVE_EVENT_PROMOTION"));
		paramJson.put("model", this.buildEventModel());
		paramJson.put("evtId", new JSONString(this.getEvtId()));
		VisitKoreaBusiness.post("call", paramJson.toString(), this.saveCallback());
	}

	@Override
	public JSONObject buildEventModel() {
		JSONObject model = new JSONObject();
		JSONArray array = new JSONArray();
		model.put("PROMOTION", array);
		
		this.fileList.forEach(item -> {
			JSONObject obj = new JSONObject();
			obj.put("banId", new JSONString(item.get("banId").toString()));
			obj.put("evtId", new JSONString(this.getEvtId()));
			obj.put("cotId", new JSONString(this.getCotId()));
			obj.put("imgId", new JSONString(item.get("imgId").toString()));
			obj.put("imgPath", new JSONString(item.get("imgPath").toString()));
			array.set(array.size(), obj);
		});
		
		return model;
	}

	@Override
	public void setupContentValue(JSONObject obj) {
		
	}
	
	@Override
	public void statusChangeProcess(EventStatus status) {
		this.eventStatus = status;
		this.uploadBtn.setVisible(this.host.getEditPossSet().contains(this.eventStatus));
	}

	private void buildBannerInformation(MaterialPanel panel) {
		MaterialRow bannerInfoRow = addRow(panel);
		
		MaterialColumn column = new MaterialColumn();
		column.setGrid("s12");
		bannerInfoRow.add(column);
		
		MaterialTab tab = new MaterialTab();
		tab.setIndicatorColor(Color.YELLOW);
		tab.setShadow(1);
		tab.addSelectionHandler(event -> {
			
		});
		column.add(tab);
		
		for (int i = 1; i <= 5; i++) {
			addTabButton(tab, "배너 0" + i, "#tab" + i);
			addTabContent(bannerInfoRow, i + "번 패널", "tab" + i);
		}
	}
	
	private void renderFiles() {
		this.uploadResultRow.clear();
		for (int i = 0; i < this.fileList.size(); i++) {
			HashMap<String, Object> item = this.fileList.get(i);
			String fileName = item.get("fileName").toString();
			String upload = item.get("upload").toString();
			
			MaterialRow fileRow = addFileRow(fileName, upload, i);
			this.uploadResultRow.add(fileRow);
		}
	}
	
	private MaterialRow addFileRow(String fileName, String upload, int index) {
		MaterialRow row = new MaterialRow();
		row.setMargin(0);
		
		MaterialColumn col1 = new MaterialColumn();
		col1.setGrid("s6");
		row.add(col1);
		
		MaterialLabel fileNameLabel = new MaterialLabel();
		fileNameLabel.setTextAlign(TextAlign.LEFT);
		fileNameLabel.setText(" -  " + index + ". " + fileName);
		col1.add(fileNameLabel);
		
		MaterialColumn col2 = new MaterialColumn();
		col2.setGrid("s3");
		row.add(col2);
	
		MaterialColumn col3 = new MaterialColumn();
		col3.setGrid("s3");
		row.add(col3);
		
		MaterialLabel resultLabel = new MaterialLabel();
		resultLabel.setText(upload.equals("success") ? "성공" : "실패");
		col3.add(resultLabel);
		
		return row;
	}
	
	private void addFile(HashMap<String, Object> file) {
		this.fileList.add(file);
	}

	private void addTabButton(MaterialTab tab, String text, String href) {
		MaterialTabItem item = new MaterialTabItem();
		item.setWaves(WavesType.YELLOW);
		tab.add(item);
		
		MaterialLink link = new MaterialLink();
		link.setText(text);
		link.setHref(href);
		link.setTextColor(Color.WHITE);
		item.add(link);
	}

	private void addTabContent(MaterialRow bannerInfoRow, String text, String id) {
		MaterialColumn column = new MaterialColumn();
		column.setGrid("s12");
		column.setId(id);
		bannerInfoRow.add(column);
		
		MaterialLabel label = new MaterialLabel(text);
		column.add(label);
	}
	
}
