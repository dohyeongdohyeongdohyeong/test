package kr.or.visitkorea.admin.client.manager.contents.recommand.dialogs;

import java.util.Map;

import com.google.gwt.dom.client.Style.FontWeight;
import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONString;

import gwt.material.design.client.constants.Color;
import gwt.material.design.client.ui.MaterialButton;
import gwt.material.design.client.ui.MaterialLabel;
import gwt.material.design.jquery.client.api.Functions.Func3;
import gwt.material.design.jscore.client.api.JSON;
import gwt.material.design.jscore.client.api.JsObject;
import kr.or.visitkorea.admin.client.manager.VisitKoreaBusiness;
import kr.or.visitkorea.admin.client.manager.widgets.ContentTableRow;
import kr.or.visitkorea.admin.client.widgets.dialog.DialogContent;
import kr.or.visitkorea.admin.client.widgets.window.MaterialExtentsWindow;

public class ChageStatusDeleteDialog extends DialogContent {

	public ChageStatusDeleteDialog(MaterialExtentsWindow window) {
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
		MaterialLabel dialogTitle = new MaterialLabel("삭제 상태로 전환합니다.");
		dialogTitle.setFontSize("1.4em");
		dialogTitle.setFontWeight(FontWeight.BOLD);
		dialogTitle.setTextColor(Color.RED);
		dialogTitle.setPaddingTop(10);
		dialogTitle.setPaddingLeft(30);

		this.add(dialogTitle);

		// dialog title define
		MaterialLabel mentLabel = new MaterialLabel("삭제상태가 되면 더 이상 컨텐츠가 서비스 되지 않습니다. 삭제 하시겠습니까?");
		mentLabel.setFontSize("1.1em");
		mentLabel.setLayoutPosition(Position.ABSOLUTE);
		mentLabel.setFontWeight(FontWeight.BOLD);
		mentLabel.setTextColor(Color.BLUE_GREY);
		mentLabel.setLeft(100);
		mentLabel.setTop(80);
		this.add(mentLabel);

		MaterialButton selectButton = new MaterialButton("삭제");
		selectButton.setBackgroundColor(Color.RED_LIGHTEN_2);
		selectButton.addClickHandler(event -> {

			
			String cotId = (String) this.getParameters().get("cotId");
			ContentTableRow tableRow = (ContentTableRow) this.getParameters().get("tgrItem");
			
			JSONObject parameterJSON = new JSONObject();
			parameterJSON.put("cmd", new JSONString("UPDATE_FOR_DELETE_STATUS"));
			parameterJSON.put("cotId", new JSONString(cotId.trim()));

			VisitKoreaBusiness.post("call", parameterJSON.toString(), new Func3<Object, String, Object>() {

				@Override
				public void call(Object param1, String param2, Object param3) {

					JSONObject resultObj = (JSONObject) JSONParser.parseStrict(JSON.stringify((JsObject) param1));
					JSONObject headerObj = (JSONObject) resultObj.get("header");
					String processResult = headerObj.get("process").isString().toString();
					processResult = processResult.replaceAll("\"", "");

					if (processResult.equals("success")) {

						MaterialLabel label = (MaterialLabel) tableRow.getChildrenList().get(4);
						label.setText("삭제");
						
					}
					
					getMaterialExtentsWindow().closeDialog();
				}
			});

		});

		this.addButton(selectButton);

	}
	
	@Override
	public void setParameters(Map<String, Object> parameters) {
		super.setParameters(parameters);
	}

	@Override
	public int getHeight() {
		return 250;
	}

}
