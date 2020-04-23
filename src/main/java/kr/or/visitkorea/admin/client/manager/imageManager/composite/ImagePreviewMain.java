package kr.or.visitkorea.admin.client.manager.imageManager.composite;

import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONString;

import gwt.material.design.client.ui.MaterialImage;
import gwt.material.design.client.ui.MaterialPanel;
import gwt.material.design.client.ui.MaterialRow;
import gwt.material.design.client.ui.MaterialTextArea;
import gwt.material.design.client.ui.MaterialTextArea.ResizeRule;
import gwt.material.design.client.ui.MaterialTextBox;
import gwt.material.design.jquery.client.api.Functions.Func3;
import gwt.material.design.jscore.client.api.JSON;
import gwt.material.design.jscore.client.api.JsObject;
import kr.or.visitkorea.admin.client.application.Registry;
import kr.or.visitkorea.admin.client.manager.VisitKoreaBusiness;
import kr.or.visitkorea.admin.client.manager.imageManager.ImageManagerApplication;
import kr.or.visitkorea.admin.client.manager.imageManager.ImagePreviewApplication;
import kr.or.visitkorea.admin.client.manager.widgets.AbstractContentPanel;
import kr.or.visitkorea.admin.client.widgets.window.MaterialExtentsWindow;


/**
 * @author Admin
 * 이미지 관리 Main
 */
public class ImagePreviewMain extends AbstractContentPanel implements ImageManagerApplication.OnSearchListener {

	private ImagePreviewApplication appview;
	private MaterialImage imgView;
	private MaterialTextBox fileNm, ext, abPath, link, width, height, size;
	private MaterialPanel infoPanel;
	private MaterialRow row;
	private MaterialTextArea descBody;

	public ImagePreviewMain(MaterialExtentsWindow materialExtentsWindow, ImagePreviewApplication pa) {
		super(materialExtentsWindow);
		appview = pa;
	}

	@Override
	public void init() {
		Registry.put("ImagePreviewMain", this);
		setLayoutPosition(Position.RELATIVE);

		buildContent();
	}

	@Override
	protected void onLoad() {
		
		JSONObject parameterJSON = new JSONObject();
		parameterJSON.put("cmd", new JSONString("GET_ABPATH_IMAGE"));
		parameterJSON.put("abPath", new JSONString(appview.getParams().get("abPath").toString()));

		VisitKoreaBusiness.post("call", parameterJSON.toString(), new Func3<Object, String, Object>() {

			@Override
			public void call(Object param1, String param2, Object param3) {

				JSONObject resultObj = (JSONObject) JSONParser.parseStrict(JSON.stringify((JsObject)param1));
				JSONObject headerObj = (JSONObject) resultObj.get("header");
				String processResult = headerObj.get("process").isString().stringValue();
				JSONObject bodyObj = (JSONObject) resultObj.get("body");
				
				if (processResult.equals("success")) {
					String token = bodyObj.get("token").isString().stringValue();
					imgView.setUrl(Registry.get("image.server").toString() + "/img/call?cmd=AB_VIEW&token=" + token);
				} else {
					String error = bodyObj.get("error").isString().stringValue();
					getMaterialExtentsWindow().alert(error, 400);
				}
			}
		});

		fileNm.setText(appview.getParams().get("fileNm").toString());
		ext.setText(appview.getParams().get("ext").toString());
		abPath.setText(appview.getParams().get("abPath").toString());
		link.setText(appview.getParams().get("link").toString());
		width.setText(appview.getParams().get("width").toString());
		height.setText(appview.getParams().get("height").toString());
		size.setText(appview.getParams().get("size").toString());
		descBody.setText(appview.getParams().get("desc").toString());
	}

	public void buildContent() {

		infoPanel = new MaterialPanel();
		infoPanel.setPadding(20);
		infoPanel.setLayoutPosition(Position.RELATIVE);

		imgView = new MaterialImage();
		imgView.setWidth("640px");
		imgView.setHeight("360px");
		imgView.setBorderRadius("ipx");
		imgView.setPaddingBottom(10);
		imgView.setPixelSize(640, 360);

		infoPanel.add(imgView);

		row = new MaterialRow();
		infoPanel.add(row);

		fileNm = new MaterialTextBox();
		fileNm.setLabel("이미지명"); fileNm.setGrid("s8");
		fileNm.setReadOnly(true); 
		row.add(fileNm);
		ext = new MaterialTextBox();
		ext.setLabel("확장자"); ext.setGrid("s4");
		ext.setReadOnly(true);
		row.add(ext);

		abPath = new MaterialTextBox();
		abPath.setLabel("절대 경로"); abPath.setGrid("s12"); abPath.setTop(-30);
		abPath.setReadOnly(true);
		row.add(abPath);

		width = new MaterialTextBox();
		width.setLabel("가로"); width.setGrid("s3"); width.setTop(-60);
		width.setReadOnly(true);
		row.add(width);
		height = new MaterialTextBox();
		height.setLabel("세로"); height.setGrid("s3"); height.setTop(-60);
		height.setReadOnly(true);
		row.add(height);
		size = new MaterialTextBox();
		size.setLabel("크기"); size.setGrid("s3"); size.setTop(-60);
		size.setReadOnly(true);
		row.add(size);
		link = new MaterialTextBox();
		link.setLabel("분류"); link.setGrid("s3"); link.setTop(-60);
		link.setReadOnly(true);
		row.add(link);

		descBody = new MaterialTextArea();
		descBody.setLabel("이미지 설명"); descBody.setGrid("s12"); descBody.setTop(-90);
		descBody.setReadOnly(true);
		descBody.setResizeRule(ResizeRule.AUTO);
		row.add(descBody);

		this.add(infoPanel);
	}

	@Override
	public void onSearch(String startDate, String endDate) {

	}
}