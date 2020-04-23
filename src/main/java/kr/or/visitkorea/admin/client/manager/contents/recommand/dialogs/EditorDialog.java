/*
package kr.or.visitkorea.admin.client.manager.contents.recommand.dialogs;

import java.util.Map;

import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;

import gwt.material.design.addins.client.richeditor.MaterialRichEditor;
import gwt.material.design.client.MaterialDesignBase;
import gwt.material.design.client.constants.Color;
import gwt.material.design.client.ui.MaterialButton;
import gwt.material.design.client.ui.MaterialPanel;
import gwt.material.design.jquery.client.api.JQuery;
import gwt.material.design.jquery.client.api.Functions.Func3;
import kr.or.visitkorea.admin.client.manager.dialogs.DialogsBundle;
import kr.or.visitkorea.admin.client.widgets.dialog.DialogContent;
import kr.or.visitkorea.admin.client.widgets.window.MaterialExtentsWindow;

public class EditorDialog extends DialogContent {
 
	static {
		MaterialDesignBase.injectCss(DialogsBundle.INSTANCE.contentCss());
	}

	private MaterialRichEditor editor;
	private String cotId;

	public EditorDialog(MaterialExtentsWindow window) {
		super(window);
	}

	public void init() {
		addDefaultButtons();

		MaterialButton selectButton = new MaterialButton("저장");
		selectButton.setBackgroundColor(Color.RED_LIGHTEN_2);
		selectButton.addClickHandler(event->{
			MaterialPanel panel = (MaterialPanel)getParameters().get("TARGET");
			SafeHtmlBuilder builder =  new SafeHtmlBuilder();
			builder.appendHtmlConstant(editor.getHTML());
			panel.getElement().setInnerSafeHtml(builder.toSafeHtml());
			
			
			JSONObject paramObj1 = new JSONObject();
			paramObj1.put("tbl", new JSONString("ARTICLE_MASTER"));
			paramObj1.put("value", new JSONString(editor.getHTML()));
			paramObj1.put("colTitle", new JSONString("BIGO"));
			paramObj1.put("cotId", new JSONString(cotId));
			
			Func3<Object, String, Object> callback1 =  new Func3<Object, String, Object>() {
				
				@Override
				public void call(Object param1, String param2, Object param3) {}
				
			};
			
			invokeQuery("UPDATE_SINGLE_ROW", paramObj1, callback1);
			
			getMaterialExtentsWindow().closeDialog();
		});
		this.addButton(selectButton);

		editor = new MaterialRichEditor();
		editor.setHeight("210px");
		editor.setTop(10);
		this.add(editor);		
	}

	@Override
	public void setParameters(Map<String, Object> parameters) {
		super.setParameters(parameters);
		MaterialPanel panel = (MaterialPanel)getParameters().get("TARGET");
		cotId = (String)getParameters().get("COT_ID");
		editor.setHTML(panel.getElement().getInnerHTML());
	}
	

	@Override
	public int getHeight() {
		return 480;
	}

	private void invokeQuery(String cmd, JSONObject parameterJSON, Func3 callback) {
		
		parameterJSON.put("cmd", new JSONString(cmd));

		VK.post("call", parameterJSON.toString(), callback);
	}

}
======= */
package kr.or.visitkorea.admin.client.manager.contents.recommand.dialogs;

import java.util.Map;

import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;

import gwt.material.design.addins.client.richeditor.MaterialRichEditor;
import gwt.material.design.client.MaterialDesignBase;
import gwt.material.design.client.constants.Color;
import gwt.material.design.client.ui.MaterialButton;
import gwt.material.design.client.ui.MaterialPanel;
import gwt.material.design.jquery.client.api.Functions.Func3;
import kr.or.visitkorea.admin.client.manager.VisitKoreaBusiness;
import kr.or.visitkorea.admin.client.manager.dialogs.DialogsBundle;
import kr.or.visitkorea.admin.client.widgets.dialog.DialogContent;
import kr.or.visitkorea.admin.client.widgets.window.MaterialExtentsWindow;

public class EditorDialog extends DialogContent {

	static {
		MaterialDesignBase.injectCss(DialogsBundle.INSTANCE.contentCss());
	}

	private MaterialRichEditor editor;
	private String cotId;

	public EditorDialog(MaterialExtentsWindow window) {
		super(window);
	}

	public void init() {
		addDefaultButtons();

		MaterialButton selectButton = new MaterialButton("저장");
		selectButton.setBackgroundColor(Color.RED_LIGHTEN_2);
		selectButton.addClickHandler(event->{
			MaterialPanel panel = (MaterialPanel)getParameters().get("TARGET");
			SafeHtmlBuilder builder =  new SafeHtmlBuilder();
			builder.appendHtmlConstant(editor.getHTML());
			panel.getElement().setInnerSafeHtml(builder.toSafeHtml());
			
			
			JSONObject paramObj1 = new JSONObject();
			paramObj1.put("tbl", new JSONString("ARTICLE_MASTER"));
			paramObj1.put("value", new JSONString(editor.getHTML()));
			paramObj1.put("colTitle", new JSONString("BIGO"));
			paramObj1.put("cotId", new JSONString(cotId));
			
			Func3<Object, String, Object> callback1 =  new Func3<Object, String, Object>() {
				
				@Override
				public void call(Object param1, String param2, Object param3) {}
				
			};
			
			invokeQuery("UPDATE_SINGLE_ROW", paramObj1, callback1);
			
			getMaterialExtentsWindow().closeDialog();
		});
		this.addButton(selectButton);

		editor = new MaterialRichEditor();
		editor.setHeight("210px");
		editor.setTop(10);
		this.add(editor);		
	}

	@Override
	public void setParameters(Map<String, Object> parameters) {
		super.setParameters(parameters);
		MaterialPanel panel = (MaterialPanel)getParameters().get("TARGET");
		cotId = (String)getParameters().get("COT_ID");
		editor.setHTML(panel.getElement().getInnerHTML());
	}
	

	@Override
	public int getHeight() {
		return 480;
	}

	private void invokeQuery(String cmd, JSONObject parameterJSON, Func3 callback) {
		
		parameterJSON.put("cmd", new JSONString(cmd));

		VisitKoreaBusiness.post("call", parameterJSON.toString(), callback);
	}

}