package kr.or.visitkorea.admin.client.manager.otherDepartment.dialogs;

import java.util.Map;

import com.google.gwt.dom.client.Style.FontWeight;
import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.json.client.JSONObject;

import gwt.material.design.client.constants.Color;
import gwt.material.design.client.ui.MaterialButton;
import gwt.material.design.client.ui.MaterialLabel;
import gwt.material.design.client.ui.MaterialListBox;
import gwt.material.design.client.ui.MaterialTextBox;
import gwt.material.design.jquery.client.api.Functions.Func3;
import kr.or.visitkorea.admin.client.manager.VisitKoreaBusiness;
import kr.or.visitkorea.admin.client.manager.otherDepartment.composite.category.sitesee.CurationItem;
import kr.or.visitkorea.admin.client.manager.widgets.ContentTable;
import kr.or.visitkorea.admin.client.widgets.dialog.DialogContent;
import kr.or.visitkorea.admin.client.widgets.window.MaterialExtentsWindow;

public class CurationHeaderDialog extends DialogContent {

	private ContentTable table;
	private MaterialListBox listBox;
	private MaterialTextBox searchBox;
	private String contentType;
	private MaterialLabel dialogTitle;
	private boolean multipleContents;
	private ContentTable targetTable;
	private String tbl;
	private String cotId;
	private Object tgrPanel;
	private String otdId;
	private MaterialTextBox headerBox;
	private String savePath;
	private String imageUUID;
	private String uploadValue;
	private String extString;
	private MaterialTextBox headerColorBox;
	private MaterialTextBox titleBox;
	private MaterialTextBox linkBox;

	public CurationHeaderDialog(MaterialExtentsWindow window) {
		super(window);
	}

	@Override
	public void init() {
		buildContent();
	}

	@Override
	protected void onLoad() {
		super.onLoad();
	}

	public void buildContent() {
		
		addDefaultButtons();

		// dialog title define
		dialogTitle = new MaterialLabel("큐레이션 컨텐츠 수정");
		dialogTitle.setFontSize("1.4em");
		dialogTitle.setFontWeight(FontWeight.BOLD);
		dialogTitle.setTextColor(Color.BLUE);
		dialogTitle.setPaddingTop(10);
		dialogTitle.setPaddingLeft(30);

		this.add(dialogTitle);

		headerBox = new MaterialTextBox();
		headerBox.getElement().getElementsByTagName("input").getItem(0).getStyle().setProperty("margin", "0");
		headerBox.setLayoutPosition(Position.ABSOLUTE);
		headerBox.setLabel("머릿말");
		headerBox.setLeft(30);
		headerBox.setRight(30);
		headerBox.setTop(60);
		headerBox.setMarginTop(0);
		headerBox.setMarginBottom(0);
		this.add(headerBox);

		headerColorBox = new MaterialTextBox();
		headerColorBox.getElement().getElementsByTagName("input").getItem(0).getStyle().setProperty("margin", "0");
		headerColorBox.setLayoutPosition(Position.ABSOLUTE);
		headerColorBox.setLabel("머릿말 색상");
		headerColorBox.setLeft(30);
		headerColorBox.setRight(30);
		headerColorBox.setTop(130);
		headerColorBox.setMarginTop(0);
		headerColorBox.setMarginBottom(0);
		this.add(headerColorBox);

		titleBox = new MaterialTextBox();
		titleBox.getElement().getElementsByTagName("input").getItem(0).getStyle().setProperty("margin", "0");
		titleBox.setLayoutPosition(Position.ABSOLUTE);
		titleBox.setLabel("제목");
		titleBox.setLeft(30);
		titleBox.setRight(30);
		titleBox.setTop(200);
		titleBox.setMarginTop(0);
		titleBox.setMarginBottom(0);
		this.add(titleBox);

		linkBox = new MaterialTextBox();
		linkBox.getElement().getElementsByTagName("input").getItem(0).getStyle().setProperty("margin", "0");
		linkBox.setLayoutPosition(Position.ABSOLUTE);
		linkBox.setLabel("Link");
		linkBox.setLeft(30);
		linkBox.setRight(30);
		linkBox.setTop(270);
		linkBox.setMarginTop(0);
		linkBox.setMarginBottom(0);
		this.add(linkBox);
		
		
		MaterialButton selectButton = new MaterialButton("머릿말 지정");
		selectButton.setBackgroundColor(Color.RED_LIGHTEN_2);
		selectButton.addClickHandler(event->{
			CurationItem curationItem = (CurationItem) getParameters().get("ITEM");
			curationItem.setHeader(headerBox.getValue());
			curationItem.setHeaderColor(headerColorBox.getValue()); 
			curationItem.setLink(this.linkBox.getValue());
			curationItem.setBoxTitle(titleBox.getValue());
			getMaterialExtentsWindow().closeDialog();
		});
		
		this.addButton(selectButton);

	}

	private String getString(JSONObject recObj, String key, String defaultValue) {
		if (recObj.get(key) == null || recObj.get(key).isString().stringValue().equals("")) return defaultValue;
		else return recObj.get(key).isString().stringValue();
	}

	@Override
	public void setParameters(Map<String, Object> parameters) {
		super.setParameters(parameters);
		
		CurationItem curationItem = (CurationItem) parameters.get("ITEM");

		this.headerBox.setValue(curationItem.getHeader());
		this.headerColorBox.setValue(curationItem.getHeaderColor());
		this.titleBox.setValue(curationItem.getBoxTitle());
		this.linkBox.setValue(curationItem.getLink());
		
	}

	private String getContentType(String object) {
		return this.contentType;
	}

	@Override
	public int getHeight() {
		return 500;
	}

	private void executeBusiness(JSONObject jObj) {
		
		VisitKoreaBusiness.post("call", jObj.toString(), new Func3<Object, String, Object>() {

			@Override
			public void call(Object param1, String param2, Object param3) {}
		});
	}

}
