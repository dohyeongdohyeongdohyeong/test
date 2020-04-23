package kr.or.visitkorea.admin.client.manager.contents.recommand.composite;

import java.util.HashMap;
import java.util.Map;

import com.google.gwt.dom.client.Style.Overflow;
import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONNumber;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONString;

import gwt.material.design.addins.client.fileuploader.base.UploadFile;
import gwt.material.design.addins.client.fileuploader.events.SuccessEvent;
import gwt.material.design.client.MaterialDesignBase;
import gwt.material.design.client.constants.Color;
import gwt.material.design.client.constants.IconType;
import gwt.material.design.client.constants.TextAlign;
import gwt.material.design.client.ui.MaterialColumn;
import gwt.material.design.client.ui.MaterialIcon;
import gwt.material.design.client.ui.MaterialLabel;
import gwt.material.design.client.ui.MaterialPanel;
import gwt.material.design.client.ui.MaterialRow;
import gwt.material.design.client.ui.MaterialTextArea;
import gwt.material.design.client.ui.MaterialTextBox;
import gwt.material.design.jquery.client.api.Functions.Func3;
import gwt.material.design.jscore.client.api.JSON;
import gwt.material.design.jscore.client.api.JsObject;
import kr.or.visitkorea.admin.client.application.Registry;
import kr.or.visitkorea.admin.client.manager.VisitKoreaBusiness;
import kr.or.visitkorea.admin.client.manager.contents.recommand.RecommApplication;
import kr.or.visitkorea.admin.client.manager.contents.recommand.RecommContentBundle;
import kr.or.visitkorea.admin.client.manager.otherDepartment.composite.UploadSuccessEventFunc;
import kr.or.visitkorea.admin.client.manager.widgets.SelectionPanel;
import kr.or.visitkorea.admin.client.manager.widgets.UploadPanel;
import kr.or.visitkorea.admin.client.widgets.window.MaterialExtentsWindow;

public class RecommContentsETC extends AbtractRecommContents {

	private MaterialTextBox authors;
	private MaterialTextArea noticeComment;
	private SelectionPanel selectOne;
	private MaterialPanel noticeButton;
	private MaterialPanel panel;
	private UploadPanel uploadPanel;
	private SelectionPanel relatedView;
	private SelectionPanel AuthorView;
	private MaterialTextBox etcAuthor;
	private JSONArray relatedViewSelectedArray;
	
	static {
		MaterialDesignBase.injectCss(RecommContentBundle.INSTANCE.contentCss());
	}

	public RecommContentsETC(MaterialExtentsWindow materialExtentsWindow) {
		super(materialExtentsWindow);
	}

	@Override
	public void init() {
		super.init();
		this.setTitle("기타 정보");
		buildContent();
		setupEvent();
	}

	private void buildContent() {
		
		panel =  new MaterialPanel();
		panel.setPadding(20);
		panel.setHeight("535px");
		panel.setOverflow(Overflow.AUTO);
		this.add(panel);
		
		//첫줄
		MaterialRow row1 = addRow(panel);
		addLabel(row1, "작가검색", TextAlign.CENTER, Color.GREY_LIGHTEN_3,"s2");
		this.authors = addInputText(row1, "", "s5");
		this.authors.setEnabled(false);
		
		MaterialIcon searchIcon = new MaterialIcon(IconType.SEARCH);
		searchIcon.setGrid("s1");
		searchIcon.setFontSize("2.4em");
		searchIcon.setPadding(0);
		searchIcon.setPaddingTop(8);
		searchIcon.setTextAlign(TextAlign.LEFT);
		searchIcon.addClickHandler(event -> {
			HashMap<String, Object> params = new HashMap<>();
			params.put("cotId", this.getCotId());
			params.put("authorTextBox", this.authors);
			params.put("relatedViewSelection", this.relatedView);
			params.put("relatedViewSelectedArray", this.relatedViewSelectedArray);
			this.getWindow().openDialog(RecommApplication.AUTHOR_SELECT, params, 800);
		});
		row1.add(searchIcon);
		
		addLabel(row1, "관련 글", TextAlign.CENTER, Color.GREY_LIGHTEN_3, "s2");
		HashMap<String, Object> relatedViewMap = new HashMap<>();
		relatedViewMap.put("OFF", 0);
		relatedViewMap.put("ON", 1);
		this.relatedView = addSelectionPanel(row1, "s2", TextAlign.LEFT, relatedViewMap);
		this.relatedView.addStatusChangeEvent(event -> {
			relatedViewUpdate();
		});
		MaterialRow row2 = addRow(panel);
		addLabel(row2, "기타 입력", TextAlign.CENTER, Color.GREY_LIGHTEN_3, "s2");
		
		etcAuthor = addInputText(row2, "", "s5");
		
		MaterialIcon SaveIcon = new MaterialIcon(IconType.SAVE);
		SaveIcon.setGrid("s1");
		SaveIcon.setFontSize("2.0em");
		SaveIcon.setPadding(0);
		SaveIcon.setPaddingTop(8);
		SaveIcon.setTextAlign(TextAlign.LEFT);
		SaveIcon.addClickHandler(event -> {
			AuthorUpdate();	
		});
		row2.add(SaveIcon);
		
		addLabel(row2, "노출 여부", TextAlign.CENTER, Color.GREY_LIGHTEN_3, "s2");
		HashMap<String, Object> etcViewMap = new HashMap<>();
		etcViewMap.put("OFF", 0);
		etcViewMap.put("ON", 1);
		this.AuthorView = addSelectionPanel(row2, "s2", TextAlign.LEFT, etcViewMap);
		this.AuthorView.addStatusChangeEvent(event -> {
			etcViewUpdate();
		});
		
		//두번째
		MaterialRow row3 = addRow(panel);
		addLabel(row3, "비고", TextAlign.CENTER, Color.GREY_LIGHTEN_3, "s2");
		this.noticeButton = addButtonPanel(row3, "s10", TextAlign.LEFT);
		
		//세번째
		MaterialRow row4 = addRow(panel);
		addLabel(row4, "공공누리", TextAlign.CENTER, Color.GREY_LIGHTEN_3, "s2");
		HashMap<String, Object> valueMap = new HashMap<String, Object>();
		valueMap.put("사용함", 1);
		valueMap.put("사용하지 않음", 0);
		this.selectOne = addSelectionPanel(row4, "s10", TextAlign.LEFT, valueMap);
		
		//네번째
		MaterialRow row5 = addRow(panel);
		addLabel(row5, "대표 이미지", TextAlign.CENTER, Color.GREY_LIGHTEN_3, "s2");
	
		uploadPanel = new UploadPanel(360, 250, (String) Registry.get("image.server") + "/img/call?cmd=TEMP_VIEW&name=55a0ec2e-a5c1-4938-8bfd-4a7e7095bec9.png");
		uploadPanel.setLayoutPosition(Position.RELATIVE);
		uploadPanel.updateImageInformation(true);
		uploadPanel.setLeft(0);
		uploadPanel.setTop(0);
		uploadPanel.setButtonPostion(false);
		MaterialColumn col1 = new MaterialColumn();
		col1.setTextAlign(TextAlign.LEFT);
		col1.setGrid("s10");
		col1.add(uploadPanel);
		row5.add(col1);

	}
	
	private void relatedViewUpdate() {
		JSONObject parameterJSON = new JSONObject();
		parameterJSON.put("cmd", new JSONString("UPDATE_ARTICLE_RELATED_VIEW"));
		parameterJSON.put("cotId", new JSONString(this.getCotId()));
		parameterJSON.put("relatedView", new JSONNumber((int) this.relatedView.getSelectedValue()));
		VisitKoreaBusiness.post("call", parameterJSON.toString(), (param1, param2, param3) -> {});
	}
	
	private void etcViewUpdate() {
		JSONObject parameterJSON = new JSONObject();
		parameterJSON.put("cmd", new JSONString("UPDATE_ARTICLE_AUTHOR_VIEW"));
		parameterJSON.put("cotId", new JSONString(this.getCotId()));
		parameterJSON.put("authorView", new JSONNumber((int) this.AuthorView.getSelectedValue()));
		VisitKoreaBusiness.post("call", parameterJSON.toString(), (param1, param2, param3) -> {});
	}
	
	private void AuthorUpdate() {
		JSONObject parameterJSON = new JSONObject();
		parameterJSON.put("cmd", new JSONString("UPDATE_ARTICLE_AUTHOR_VIEW"));
		parameterJSON.put("cotId", new JSONString(this.getCotId()));
		parameterJSON.put("Author", new JSONString(etcAuthor.getText()));
		VisitKoreaBusiness.post("call", parameterJSON.toString(), (param1, param2, param3) -> {});
	}
	
	
	@Override
	public void setCotId(String cotid) {
		super.setCotId(cotid);
		uploadPanel.setCotId(getCotId());
	}

	private void setupEvent() {
		this.selectOne.addStatusChangeEvent(event->{
			
			
			Integer sOneInteger = (Integer)this.selectOne.getSelectedValue();
			double sOneDouble = sOneInteger.doubleValue();
			
			JSONObject paramObj1 = new JSONObject();
			paramObj1.put("tbl", new JSONString("ARTICLE_MASTER"));
			paramObj1.put("value", new JSONNumber(sOneDouble));
			paramObj1.put("colTitle", new JSONString("IS_KOGL"));
			paramObj1.put("cotId", new JSONString(getCotId()));
			
			Func3<Object, String, Object> callback1 =  new Func3<Object, String, Object>() {
				
				@Override
				public void call(Object param1, String param2, Object param3) {}
				
			};
			
			invokeQuery("UPDATE_SINGLE_ROW", paramObj1, callback1);
			
		});
		
		this.uploadPanel.setUploadSuccessEvent(new UploadSuccessEventFunc() {
			
			@Override
			public void invoke(SuccessEvent<UploadFile> event) {
			
				/**
				 * update image column
				 */
				JSONObject paramObj2 = new JSONObject();
				paramObj2.put("tbl", new JSONString("ARTICLE_MASTER"));
				paramObj2.put("value", new JSONString(uploadPanel.getImageId()));
				paramObj2.put("colTitle", new JSONString("IMG_ID"));
				paramObj2.put("cotId", new JSONString(getCotId()));
				
				Func3<Object, String, Object> callback2 =  new Func3<Object, String, Object>() {
					
					@Override
					public void call(Object param1, String param2, Object param3) {}
					
				};
				
				invokeQuery("UPDATE_SINGLE_ROW", paramObj2, callback2);

			}
		});
		
	}

	private MaterialPanel addButtonPanel(MaterialRow row, String grid, TextAlign align) {
		
		MaterialPanel box = new MaterialPanel();
		box.setTextAlign(align);
		box.setPadding(5);
		box.setMinHeight("46.25px");
		box.getElement().setInnerText("글을 입력하려면 선택하세요.");
		box.setBackgroundColor(Color.WHITE);
		box.addClickHandler(event->{
			Map<String, Object> paramters = new HashMap<String, Object>();
			paramters.put("TARGET", box);
			paramters.put("COT_ID", getCotId());
			getWindow().openDialog(RecommApplication.HTML_EDITOR, paramters, 1000);
		});
		
		MaterialColumn col1 = new MaterialColumn();
		col1.setTextAlign(align);
		col1.setGrid(grid);
		col1.add(box);
		row.add(col1);
		return box;
	}

	@Override
	public void setReadOnly(boolean readFlag) {
		
	}

	public void loadData() {
		
		JSONObject parameterJSON = new JSONObject();
		parameterJSON.put("cmd", new JSONString("ARTICLE_ETC_SELECT_WITH_COTID"));
		parameterJSON.put("cotId", new JSONString(this.getCotId()));

		VisitKoreaBusiness.post("call", parameterJSON.toString(), new Func3<Object, String, Object>() {
	
			@Override
			public void call(Object param1, String param2, Object param3) {
	
				JSONObject resultObj = (JSONObject) JSONParser.parseStrict(JSON.stringify((JsObject)param1));
				JSONObject headerObj = (JSONObject) resultObj.get("header");
				String processResult = headerObj.get("process").isString().toString();
				processResult = processResult.replaceAll("\"", "");
				
				if (processResult.equals("success")) {
					
					JSONObject bodyObj =  resultObj.get("body").isObject();
					JSONObject returnResultObj = bodyObj.get("result").isObject().get("selectCotId").isObject();
					JSONArray returnRelatedResultArr = bodyObj.get("resultRelatedView").isArray();

					if (returnResultObj.get("AUTHOR") == null) {
						etcAuthor.setText("");
					}else {
						etcAuthor.setText(returnResultObj.get("AUTHOR").isString().stringValue());
					}
					
					if (returnResultObj.get("BIGO") == null) {
						noticeButton.getElement().setInnerHTML("");
					}else {
						noticeButton.getElement().setInnerHTML(returnResultObj.get("BIGO").isString().stringValue());
					}
					
					if (returnResultObj.get("IS_KOGL") == null) {
						selectOne.setSelectionOnSingleMode("사용하지 않음");
					}else {
						if (returnResultObj.get("IS_KOGL").isNumber().doubleValue() == 0) {
							selectOne.setSelectionOnSingleMode("사용하지 않음");
						}else {
							selectOne.setSelectionOnSingleMode("사용함");
						}
					}
					
					if (returnResultObj.get("IMG_ID") != null) {
						uploadPanel.setCotId(getCotId());
						uploadPanel.setImageId(returnResultObj.get("IMG_ID").isString().stringValue());
					} else {
						uploadPanel.setImageUrl("");;
					}

					if (returnResultObj.get("IS_RELATED") != null) {
						int isRelated = (int) returnResultObj.get("IS_RELATED").isNumber().doubleValue();
						relatedView.setSelectionOnSingleMode(isRelated == 1 ? "ON" : "OFF");
					}
					
					if (returnResultObj.get("AUTHOR_VIEW") != null) {
						int authorview = (int) returnResultObj.get("AUTHOR_VIEW").isNumber().doubleValue();
						AuthorView.setSelectionOnSingleMode(authorview == 1 ? "ON" : "OFF");
					}
					
					String inputRelatedValue = "";
					for (int i = 0; i < returnRelatedResultArr.size(); i++) {
						JSONObject obj = returnRelatedResultArr.get(i).isObject();
						String name = obj.get("NAME").isString().stringValue();
						String keyword = obj.get("KEYWORD").isString().stringValue();
						inputRelatedValue += (name + "(" + keyword + "), ");
					}
					authors.setValue(inputRelatedValue.substring(0, inputRelatedValue.lastIndexOf(",")));
					
					relatedViewSelectedArray = returnRelatedResultArr;
					loading(false);
				}
			}
		});
		
		
	}


}
