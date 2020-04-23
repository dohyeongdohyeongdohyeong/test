package kr.or.visitkorea.admin.client.manager.contents.recommand;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gwt.aria.client.ImgRole;
import com.google.gwt.dom.client.Style.Float;
import com.google.gwt.dom.client.Style.FontWeight;
import com.google.gwt.dom.client.Style.Overflow;
import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.dom.client.Style.VerticalAlign;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONNumber;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.json.client.JSONValue;
import com.google.gwt.user.client.ui.Widget;

import gwt.material.design.addins.client.tree.MaterialTree;
import gwt.material.design.addins.client.tree.MaterialTreeItem;
import gwt.material.design.client.MaterialDesignBase;
import gwt.material.design.client.base.MaterialWidget;
import gwt.material.design.client.base.TransitionConfig;
import gwt.material.design.client.constants.ButtonType;
import gwt.material.design.client.constants.Color;
import gwt.material.design.client.constants.IconType;
import gwt.material.design.client.constants.TextAlign;
import gwt.material.design.client.ui.MaterialButton;
import gwt.material.design.client.ui.MaterialColumn;
import gwt.material.design.client.ui.MaterialIcon;
import gwt.material.design.client.ui.MaterialLabel;
import gwt.material.design.client.ui.MaterialLink;
import gwt.material.design.client.ui.MaterialLoader;
import gwt.material.design.client.ui.MaterialPanel;
import gwt.material.design.client.ui.MaterialRow;
import gwt.material.design.client.ui.MaterialToast;
import gwt.material.design.jquery.client.api.Functions.Func3;
import gwt.material.design.jscore.client.api.JSON;
import gwt.material.design.jscore.client.api.JsObject;
import kr.or.visitkorea.admin.client.application.Console;
import kr.or.visitkorea.admin.client.application.Registry;
import kr.or.visitkorea.admin.client.manager.VisitKoreaBusiness;
import kr.or.visitkorea.admin.client.manager.contents.database.DatabaseContentType;
import kr.or.visitkorea.admin.client.manager.contents.recommand.composite.RecommContentsBase;
import kr.or.visitkorea.admin.client.manager.contents.recommand.composite.RecommContentsDetails;
import kr.or.visitkorea.admin.client.manager.contents.recommand.composite.RecommContentsETC;
import kr.or.visitkorea.admin.client.manager.contents.recommand.composite.RecommContentsEditor;
import kr.or.visitkorea.admin.client.manager.contents.recommand.composite.RecommContentsPartners;
import kr.or.visitkorea.admin.client.manager.contents.recommand.composite.RecommContentsRelatedCourseAndFestival;
import kr.or.visitkorea.admin.client.manager.contents.recommand.composite.RecommContentsRelatedImage;
import kr.or.visitkorea.admin.client.manager.contents.recommand.composite.RecommContentsRelatedRecommandAndSights;
import kr.or.visitkorea.admin.client.manager.contents.recommand.composite.RecommContentsTags;
import kr.or.visitkorea.admin.client.manager.widgets.AbstractContentPanel;
import kr.or.visitkorea.admin.client.manager.widgets.ContentDetailItem;
import kr.or.visitkorea.admin.client.manager.widgets.ContentPanel;
import kr.or.visitkorea.admin.client.manager.widgets.ContentTableRow;
import kr.or.visitkorea.admin.client.manager.widgets.ContentTree;
import kr.or.visitkorea.admin.client.manager.widgets.ContentTreeItem;
import kr.or.visitkorea.admin.client.manager.widgets.IDUtil;
import kr.or.visitkorea.admin.client.manager.widgets.ImageInformation;
import kr.or.visitkorea.admin.client.manager.widgets.ItemInformation;
import kr.or.visitkorea.admin.client.widgets.window.MaterialExtentsWindow;

public class RecommContentsTree extends AbstractContentPanel {

	static {
		MaterialDesignBase.injectCss(RecommContentBundle.INSTANCE.contentCss());
	}

	private MaterialPanel panelTop;
	private AbstractContentPanel panelContentTop;
	private MaterialPanel topSlider;
	private MaterialPanel fixedTop;
	private int nowPosition;
	private MaterialWidget detailSliderPanel;
	private ContentTreeItem contentItem; 
	private ContentTreeItem selectedItem; 
	private ContentTreeItem selectedLastItem;
	private boolean actionOK = false;
	private MaterialTree tree;
	private RecommContentsEditor editorComposite;
	private RecommContentsDetails editorPreview;
	private ContentTree contentTree;
	private MaterialLabel titleLabel;
	private MaterialWidget icon1;
	private MaterialWidget icon2;
	private MaterialIcon icon3;
	private MaterialIcon icon4;
	private MaterialIcon icon31;
	private MaterialIcon icon32;
	private MaterialIcon icon33;
	
	private ContentTreeItem mainContentTreeItem;
	private String cotid;
	private RecommContentsBase contentBase;
	private RecommContentsTags contentTag;
	private RecommContentsETC contentETC;
	private RecommContentsRelatedRecommandAndSights contentRelatedSights;
	private RecommContentsRelatedCourseAndFestival contentRelatedFestival;
	private ContentTableRow tableRow;
	private RecommContentsRelatedImage contentsRelatedImage;
	private String atmid;
	private MaterialIcon iconTotalSave;
	private MaterialLink previewLink;
	private MaterialIcon icon34;

	public RecommContentsTree(MaterialExtentsWindow meWindow) {
		super(meWindow);
	}

	public void init() {
		
		this.setLayoutPosition(Position.RELATIVE);
		this.setTextAlign(TextAlign.CENTER);
		this.setFloat(com.google.gwt.dom.client.Style.Float.LEFT);
		this.setHeight("100%");
		this.setOverflow(Overflow.HIDDEN);
		
		buildTitle();
		buildLayout();
		buildMainTree();
		buildContentTree();

		MaterialIcon addIcon = new MaterialIcon(IconType.KEYBOARD_ARROW_LEFT);
		addIcon.setLayoutPosition(Position.ABSOLUTE);
		addIcon.setTooltip("목록으로");
		addIcon.setRight(30);
		addIcon.setTop(25);
		addIcon.setWidth("24");
		addIcon.setBorder("1px solid #e0e0e0");
		addIcon.addClickHandler(event->{
			getMaterialExtentsWindow().goContentSlider(0);
		});
		
		this.add(addIcon);
		
		
		previewLink = new MaterialLink(IconType.PAGEVIEW);
		previewLink.setLayoutPosition(Position.ABSOLUTE);
		previewLink.setTooltip("미리보기");
		previewLink.setRight(70);
		previewLink.setTop(22);
		previewLink.addClickHandler(event->{
			Registry.openPreview(this.previewLink, Registry.get("service.server") + "/detail/rem_detail.html?cotid=" + this.cotid);
		});

		this.add(previewLink);
		
		previewLink.getElement().getFirstChildElement().getStyle().setMargin(0, Unit.PX);
		previewLink.getElement().getFirstChildElement().getStyle().setFontSize(2.3, Unit.EM);
		
	}

	public void saveDB() {
		processArticleContent();
	}

	private JSONArray buildSaveModel() {
		
		JSONArray multipleRowArray = new JSONArray();
		
		ContentTreeItem rootItem = contentTree.getItem("컨텐츠");
		
		List<MaterialTreeItem> firstChildItems = rootItem.getTreeItems();
		
		for (MaterialTreeItem firstChildren : firstChildItems) {
			
			List<MaterialTreeItem> secondChildItems = firstChildren.getTreeItems();
			
			for (MaterialTreeItem secondChild : secondChildItems) {
				ContentTreeItem targetItem = (ContentTreeItem)secondChild;
				ContentDetailItem targetDetailItem = (ContentDetailItem)targetItem.getInternalReferences().get("DETAIL_ITEM");

				JSONValue retJSONValue = targetDetailItem.saveArticleContentData();
				if (retJSONValue != null) {
					multipleRowArray.set(multipleRowArray.size(), retJSONValue);
				}
			}
		}
		
		return multipleRowArray;
		
	}

	private void processArticleContent() {
		JSONObject parameterJSON = new JSONObject();
		parameterJSON.put("cmd", new JSONString("INSERT_ARTICLE_CONTENT_MULTI_ROW"));
		parameterJSON.put("atmId", new JSONString(atmid));
		parameterJSON.put("ROWS", buildSaveModel());
		VisitKoreaBusiness.post("call", parameterJSON.toString(), (o1, o2, o3) -> {
			MaterialToast.fireToast("저장되었습니다. ", 3000);
			MaterialLoader.loading(false);
		});
	}
	
//	private void processArticleContent() {
//
//		JSONObject parameterJSON = new JSONObject();
//		parameterJSON.put("cmd", new JSONString("DELETE_ARTICLE_MULTI_ROW"));
//		parameterJSON.put("atmId", new JSONString(atmid));
//		parameterJSON.put("chk", new JSONString(IDUtil.uuid()));
//
//		VisitKoreaBusiness.post("call", parameterJSON.toString(), new Func3<Object, String, Object>() {
//			
//			@Override
//			public void call(Object param1, String param2, Object param3) {
//
//				JSONObject parameterJSON = new JSONObject();
//				parameterJSON.put("cmd", new JSONString("INSERT_ARTICLE_CONTENT_MULTI_ROW"));
//				parameterJSON.put("ROWS", buildSaveModel());
//
//				VisitKoreaBusiness.post("call", parameterJSON.toString(), new Func3<Object, String, Object>() {
//					@Override
//					public void call(Object param1, String param2, Object param3) {
//						
//						MaterialToast.fireToast("저장되었습니다. ", 3000);
//					}
//					
//				});
//				
//			}
//			
//		});
//		
//	}
	
//	private void processArticleContent() {
//		JSONObject paramJson = new JSONObject();
//		paramJson.put("cmd", new JSONString("INSERT_ARTICLE_CONTENT_MULTI_ROW"));
//		paramJson.put("ROWS", buildSaveModel());
//		VisitKoreaBusiness.post("call", paramJson.toString(), (a1, a2, a3) -> {
//			JSONObject paramJson2 = new JSONObject();
//			paramJson2.put("cmd", new JSONString("DELETE_ARTICLE_MULTI_ROW"));
//			paramJson2.put("atmId", new JSONString(atmid));
//			paramJson2.put("chk", new JSONString(IDUtil.uuid()));
//			VisitKoreaBusiness.post("call", paramJson2.toString(), (b1, b2, b3) -> {
//				JSONObject paramJson3 = new JSONObject();
//				paramJson3.put("cmd", new JSONString("INSERT_ARTICLE_CONTENT_MULTI_ROW"));
//				paramJson3.put("ROWS", buildSaveModel());
//				VisitKoreaBusiness.post("call", paramJson3.toString(), (c1, c2, c3) -> {
//					MaterialToast.fireToast("저장되었습니다. ", 3000);
//				});
//			});
//		});
//	}
	
	private void buildTitle() {
		
		titleLabel = new MaterialLabel("");
		titleLabel.setMarginTop(15);
		titleLabel.setTextAlign(TextAlign.LEFT);
		titleLabel.setPaddingLeft(30);
		titleLabel.setFontWeight(FontWeight.BOLD);
		titleLabel.setTextColor(Color.BLUE_DARKEN_2);
		titleLabel.setFontSize("1.4em");
		this.add(titleLabel);
		
	}

	public void setTitle(String title) {
		super.setTitle(title);
		titleLabel.setText(title);
	}
	
	private void buildContentTree() {

		contentTree = new ContentTree();
		
		mainContentTreeItem = new ContentTreeItem(959 * 3);
		mainContentTreeItem.setTextAlign(TextAlign.LEFT);
		mainContentTreeItem.setFontSize("1.0em");
		mainContentTreeItem.setTextColor(Color.BLUE);
		mainContentTreeItem.setText("컨텐츠");
		mainContentTreeItem.setIconType(IconType.COLLECTIONS);
		mainContentTreeItem.setContentType(DatabaseContentType.NONE);
		mainContentTreeItem.setEditorValue("");
		
		contentTree.add(mainContentTreeItem);
		 
		panelContentTop = new ContentPanel();
		panelContentTop.setStyle("overflow-x: hidden; overflow-y: auto;"); 
		panelContentTop.setLayoutPosition(Position.RELATIVE);
		panelContentTop.setFloat(com.google.gwt.dom.client.Style.Float.LEFT);
		panelContentTop.setWidth("458px");
		panelContentTop.setHeight("100%");
		panelContentTop.add(contentTree);
		
		topSlider.add(panelContentTop);
		editorPreview.setTarget(mainContentTreeItem);		
		
		contentTree.addSelectionHandler(event->{
			
			ContentTreeItem treeItem = (ContentTreeItem)event.getSelectedItem();
			
			if (treeItem.getIcon().getIconType().equals(IconType.COLLECTIONS)){
				visibleBottomButton(true, false, false, false, false, false, false,false);
			}else if (treeItem.getIcon().getIconType().equals(IconType.SUBJECT)){
				visibleBottomButton(false, true, true, true, true, true,true,true);
			}else if (treeItem.getIcon().getIconType().equals(IconType.TITLE)){
				visibleBottomButton(false, false, false, false, false, false, false,false);
			}else if (treeItem.getIcon().getIconType().equals(IconType.VIEW_HEADLINE)){
				visibleBottomButton(false, false, false, false, false, false, false,false);
			}else if (treeItem.getIcon().getIconType().equals(IconType.IMAGE)){
				visibleBottomButton(false, false, false, false, false, false, false,false);
			}else if (treeItem.getIcon().getIconType().equals(IconType.MOVIE)){
				visibleBottomButton(false, false, false, false, false, false, false,false);
			}else if (treeItem.getIcon().getIconType().equals(IconType.INFO_OUTLINE)){
				visibleBottomButton(false, false, false, false, false, false, false,false);
			}

		});

	}
	
	public void renderDetail() {
		editorPreview.render();
	}

	private void buildMainTree() {
		
		tree = new MaterialTree();
		
		ContentTreeItem tItem101 = new ContentTreeItem(0);
		tItem101.setTextAlign(TextAlign.LEFT);
		tItem101.setFontSize("1.0em");
		tItem101.setTextColor(Color.BLUE);
		tItem101.setText("기본 정보");
		tItem101.setIconType(IconType.INFO);
		
		ContentTreeItem tItem102 = new ContentTreeItem(959 * 2);
		tItem102.setTextAlign(TextAlign.LEFT);
		tItem102.setFontSize("1.0em");
		tItem102.setTextColor(Color.BLUE);
		tItem102.setText("태그");
		tItem102.setIconType(IconType.CODE);
		
		ContentTreeItem tItem103 = new ContentTreeItem(959 * 1);
		tItem103.setTextAlign(TextAlign.LEFT);
		tItem103.setFontSize("1.0em");
		tItem103.setTextColor(Color.BLUE);
		tItem103.setText("기타 정보");
		tItem103.setIconType(IconType.FACE);

		ContentTreeItem tItem104 = new ContentTreeItem(959);
		tItem104.setTextAlign(TextAlign.LEFT);
		tItem104.setFontSize("1.0em");
		tItem104.setTextColor(Color.BLUE);
		tItem104.setText("컨텐츠");
		tItem104.setIconType(IconType.COLLECTIONS);
	
		ContentTreeItem tItem106 = new ContentTreeItem(-1);
		tItem106.setTextAlign(TextAlign.LEFT);
		tItem106.setFontSize("1.0em");
		tItem106.setTextColor(Color.BLUE);
		tItem106.setText("연관 컨텐츠");
		tItem106.setIconType(IconType.FOLDER_OPEN);
	
		ContentTreeItem tItem10601 = new ContentTreeItem(959 * 6);
		tItem10601.setTextAlign(TextAlign.LEFT);
		tItem10601.setFontSize("1.0em");
		tItem10601.setTextColor(Color.BLUE);
		tItem10601.setText("추천 과 명소");
		tItem10601.setIconType(IconType.GOLF_COURSE);
		
		ContentTreeItem tItem10602 = new ContentTreeItem(959 * 7);
		tItem10602.setTextAlign(TextAlign.LEFT);
		tItem10602.setFontSize("1.0em");
		tItem10602.setTextColor(Color.BLUE);
		tItem10602.setText("코스 와 축제");
		tItem10602.setIconType(IconType.CLASS);
		
		ContentTreeItem tItem10603 = new ContentTreeItem(959 * 5);
		tItem10603.setTextAlign(TextAlign.LEFT);
		tItem10603.setFontSize("1.0em");
		tItem10603.setTextColor(Color.BLUE);
		tItem10603.setText("컨텐츠에 등록된 이미지");
		tItem10603.setIconType(IconType.IMAGE);
		
		tItem106.add(tItem10601);
		tItem106.add(tItem10602);
		tItem106.add(tItem10603);
		
		tree.add(tItem101);
		tree.add(tItem103);
		tree.add(tItem102);
		tree.add(tItem106);
		tree.add(tItem104);
		
		tree.addSelectionHandler(event->{
			
			ContentTreeItem treeItem = (ContentTreeItem)event.getSelectedItem();
			int slidingValue = treeItem.getSlidingValue();
			
			if (treeItem.getIcon().getIconType().equals(IconType.COLLECTIONS)) {
				goTree(2);
				go(959 * -3);
			}else {
				if (slidingValue > -1) {
					int newSlidingValue = slidingValue * -1;
					go(newSlidingValue);
				}
			}
		});
		
		this.panelTop.add(tree);
	}

	private void buildLayout() {
		
		MaterialRow row = new MaterialRow();
		row.setLayoutPosition(Position.RELATIVE);
		row.setTop(-25);
		row.setHeight("620px");
		
		MaterialColumn col1 = new MaterialColumn();
		col1.setPaddingLeft(30);
		col1.setPaddingBottom(54);
		col1.setPaddingTop(22);
		col1.setGrid("s4");
		col1.setHeight("100%");
		
		MaterialColumn col2 = new MaterialColumn();
		col2.setPaddingRight(30);
		col2.setPaddingBottom(30);
		col2.setPaddingTop(22);
		col2.setGrid("s8");
		col2.setHeight("100%");
		
		row.add(col1);
		row.add(col2);

		fixedTop = new MaterialPanel();
		fixedTop.setTop(20);
		fixedTop.setWidth("458px");
		fixedTop.setHeight("100%");
		fixedTop.setOverflow(Overflow.HIDDEN);
		fixedTop.setBorder("1px solid #e0e0e0");
		fixedTop.setLayoutPosition(Position.RELATIVE);
		
		topSlider = new MaterialPanel();
		topSlider.setLayoutPosition(Position.ABSOLUTE);
		topSlider.setTop(0);
		topSlider.setLeft(0);
		topSlider.setOverflow(Overflow.HIDDEN);
		topSlider.setWidth(((458*3)) + "px");
		topSlider.setHeight("100%");
		
		panelTop = new MaterialPanel();
		panelTop.setStyle("overflow-x: hidden; overflow-y: auto;"); 
		panelTop.setLayoutPosition(Position.RELATIVE);
		panelTop.setFloat(com.google.gwt.dom.client.Style.Float.LEFT);
		panelTop.setWidth("458px");
		panelTop.setHeight("100%");
		
		
		//2020-03-27 dohyeong 추가.
		MaterialButton treeTitle = new MaterialButton("컨텐츠 편집 리스트");
		treeTitle.setWidth("50%");
		treeTitle.setHeight("32px");
		treeTitle.setType(ButtonType.OUTLINED);
		
		
		MaterialButton historyManagementButton = new MaterialButton("히스토리 관리");
		historyManagementButton.setWidth("50%");
		historyManagementButton.setHeight("32px");
		historyManagementButton.addClickHandler(handler -> {
			super.getWindowParameters().put("COT_ID", this.cotid);
			super.getMaterialExtentsWindow().openDialog(RecommApplication.HISTORY_MANAGEMENT, super.getWindowParameters(), 1010, 660);
		});
		
		

		// 품질인증 부서만 사용 가능하게..
//		if(super.getMaterialExtentsWindow().getValueMap().get("OTD_ID").equals("456a84d1-84c4-11e8-8165-020027310001")) {
		this.fixedTop.add(treeTitle);
		this.fixedTop.add(historyManagementButton);
		this.topSlider.setMarginTop(32);
		this.topSlider.setPaddingBottom(32);
//		}
		

		topSlider.add(panelTop);
		
		fixedTop.add(topSlider);
		
		MaterialPanel panelBottom = new MaterialPanel();
		panelBottom.setBorder("1px solid #e0e0e0");
		panelBottom.setHeight("26px");
		panelBottom.setPadding(0);
		panelBottom.setTop(20);
		panelBottom.setLayoutPosition(Position.RELATIVE);

		icon1 = new MaterialIcon(IconType.SUBJECT);
		icon1.setTooltip("문단 생성");
		icon1.setLineHeight(26);
		icon1.setFontSize("1.0em");
		icon1.setBorderRight("1px solid #e0e0e0");
		icon1.setHeight("26px");
		icon1.setMargin(0);
		icon1.setWidth("26px");
		icon1.setFloat(com.google.gwt.dom.client.Style.Float.LEFT);
		icon1.addClickHandler(event->{

			if (contentTree.getSelectedItem().getIcon().getIconType().equals(IconType.COLLECTIONS)) {
				
				if (atmid == null) atmid = IDUtil.uuid();

				ContentTreeItem[] tItem = createSectionOne(contentTree.getSelectedItem(), "");
				ContentTreeItem rootTreeItem = (ContentTreeItem) tItem[0].getTree().getChildren().get(0);
				double recContentOrder = rootTreeItem.getWidgetIndex(tItem[0]);
				
				tItem[1].getInternalReferences().put("IS_TITLE", true);
				tItem[1].getInternalReferences().put("TBL", "ARTICLE_CONTENT");
				tItem[1].getInternalReferences().put("COL_TITLE", "ARTICLE_BODY");
				tItem[1].getInternalReferences().put("ATM_ID", contentTree.getAtmId());
				tItem[1].getInternalReferences().put("ACI_ID", IDUtil.uuid().toString());
				tItem[1].getInternalReferences().put("ARTICLE_TITLE", "");
				tItem[1].getInternalReferences().put("CONTENT_ORDER", recContentOrder);
				tItem[1].getInternalReferences().put("ARTICLE_ORDER", 0d);
				tItem[1].getInternalReferences().put("ARTICLE_SUB_ORDER", 0d);
				tItem[1].getInternalReferences().put("ARTICLE_TYPE", 0d);
				tItem[1].getInternalReferences().put("CONTENT_TREE", this);
				tItem[1].getInternalReferences().put("IS_BOX", (double) 0);
				tItem[1].getInternalReferences().put("IS_VERTICAL", (double) 0);
				
				tItem[0].getInternalReferences().put("CONTENT_TREE", this);
				
				reOrderItems();
				editorPreview.render();
			}
			
		});
		
		icon2 = new MaterialIcon(IconType.VIEW_HEADLINE); 
		icon2.setTooltip("문장 생성");
		icon2.setLineHeight(26);
		icon2.setVerticalAlign(VerticalAlign.MIDDLE);
		icon2.setFontSize("1.0em");
		icon2.setBorderRight("1px solid #e0e0e0");
		icon2.setHeight("26px");
		icon2.setMargin(0);
		icon2.setWidth("26px");
		icon2.setFloat(com.google.gwt.dom.client.Style.Float.LEFT);
		icon2.addClickHandler(event->{
			
			if (contentTree.getSelectedItem().getIcon().getIconType().equals(IconType.SUBJECT)) {

				ContentTreeItem pitem = (ContentTreeItem) contentTree.getSelectedItem();
				ContentTreeItem item = createImageItem(contentTree.getSelectedItem(), IconType.VIEW_HEADLINE, "문장", 560 * 8, DatabaseContentType.INPUT_HTML, "", "");
				
				double contentOrder = ((ContentTreeItem)contentTree.getSelectedItem().getParent()).getWidgetIndex(pitem);
				double articleOrder =  contentTree.getSelectedItem().getWidgetIndex(item);
			
				item.getInternalReferences().put("IS_TITLE", false);
				item.getInternalReferences().put("TBL", "ARTICLE_CONTENT");
				item.getInternalReferences().put("COL_TITLE", "ARTICLE_BODY");
				item.getInternalReferences().put("ATM_ID", contentTree.getAtmId());
				item.getInternalReferences().put("ACI_ID", IDUtil.uuid().toString());
				item.getInternalReferences().put("ARTICLE_TITLE", "");
				item.getInternalReferences().put("ARTICLE_BODY", "");
				item.getInternalReferences().put("CONTENT_ORDER", contentOrder);
				item.getInternalReferences().put("ARTICLE_ORDER", articleOrder);
				item.getInternalReferences().put("ARTICLE_SUB_ORDER", 0d);
				item.getInternalReferences().put("ARTICLE_TYPE", 1d);
				item.getInternalReferences().put("CONTENT_TREE", this);
				item.getInternalReferences().put("IS_BOX", (double) 0);
				item.getInternalReferences().put("IS_VERTICAL", (double) 0);

				reOrderItems();
				editorPreview.render();

			}
		});
		
		icon3 = new MaterialIcon(IconType.IMAGE);
		icon3.setTooltip("이미지 생성");
		icon3.setLineHeight(26);
		icon3.setVerticalAlign(VerticalAlign.MIDDLE);
		icon3.setFontSize("1.0em");
		icon3.setBorderRight("1px solid #e0e0e0");
		icon3.setHeight("26px");
		icon3.setMargin(0);
		icon3.setWidth("26px");
		icon3.setFloat(com.google.gwt.dom.client.Style.Float.LEFT);
		icon3.addClickHandler(event->{
			
			if (contentTree.getSelectedItem().getIcon().getIconType().equals(IconType.SUBJECT)) {


				ContentTreeItem pitem = (ContentTreeItem) contentTree.getSelectedItem();
				ContentTreeItem item = createImageItem(pitem, IconType.IMAGE, "이미지", 560 * 3, DatabaseContentType.INPUT_IMAGE, "", IDUtil.uuid());
				
				double contentOrder = ((ContentTreeItem)contentTree.getSelectedItem().getParent()).getWidgetIndex(pitem);
				double articleOrder =  contentTree.getSelectedItem().getWidgetIndex(item);

				item.getInternalReferences().put("ID_MAP", new HashMap<String, Map<String, Object>>());
				item.getInternalReferences().put("IS_TITLE", false);
				item.getInternalReferences().put("TBL", "ARTICLE_CONTENT");
				item.getInternalReferences().put("ATM_ID", contentTree.getAtmId());
				item.getInternalReferences().put("ACI_ID", IDUtil.uuid().toString());
				item.getInternalReferences().put("CONTENT_ORDER", contentOrder);
				item.getInternalReferences().put("ARTICLE_ORDER", articleOrder);
				item.getInternalReferences().put("ARTICLE_TYPE", 2d);
				item.getInternalReferences().put("CONTENT_TREE", this);
				
				reOrderItems();
				editorPreview.render();
				
			}
		});
		
		icon31 = new MaterialIcon(IconType.MOVIE);
		icon31.setTooltip("동영상 생성");
		icon31.setLineHeight(26);
		icon31.setVerticalAlign(VerticalAlign.MIDDLE);
		icon31.setFontSize("1.0em");
		icon31.setBorderRight("1px solid #e0e0e0");
		icon31.setHeight("26px");
		icon31.setMargin(0);
		icon31.setWidth("26px");
		icon31.setFloat(com.google.gwt.dom.client.Style.Float.LEFT);
		icon31.addClickHandler(event->{
			if (contentTree.getSelectedItem().getIcon().getIconType().equals(IconType.SUBJECT)) {

				ContentTreeItem pitem = (ContentTreeItem) contentTree.getSelectedItem();
				ContentTreeItem item = new ContentTreeItem(560 * 3);
				
				if (item != null && pitem != null) pitem.add(item);

				item.setTextAlign(TextAlign.LEFT);
				item.setFontSize("1.0em");
				item.setTextColor(Color.BLUE);
				item.setText("동영상");
				item.setIconType(IconType.MOVIE);
				item.setContentType(DatabaseContentType.INPUT_MOVIE);
				item.setEditorValue("");

				contentTree.getSelectedItem().add(item);

				double contentOrder = ((ContentTreeItem)contentTree.getSelectedItem().getParent()).getWidgetIndex(pitem);
				double articleOrder =  contentTree.getSelectedItem().getWidgetIndex(item);
				
				item.getInternalReferences().put("IS_TITLE", false);
				item.getInternalReferences().put("TBL", "ARTICLE_CONTENT");
				item.getInternalReferences().put("COL_TITLE", "ARTICLE_BODY");
				item.getInternalReferences().put("ATM_ID", contentTree.getAtmId());
				item.getInternalReferences().put("ACI_ID", IDUtil.uuid().toString());
				item.getInternalReferences().put("ARTICLE_TITLE", "");
				item.getInternalReferences().put("ARTICLE_BODY", "");
				item.getInternalReferences().put("CONTENT_ORDER", contentOrder);
				item.getInternalReferences().put("ARTICLE_ORDER", articleOrder);
				item.getInternalReferences().put("ARTICLE_SUB_ORDER", 0d);
				item.getInternalReferences().put("ARTICLE_TYPE", 3d);
				item.getInternalReferences().put("CONTENT_TREE", this);
				
				reOrderItems();
				editorPreview.render();

			}
		});

		icon32 = new MaterialIcon(IconType.REMOVE);
		icon32.setTooltip("끝맺음 처리");
		icon32.setLineHeight(26);
		icon32.setVerticalAlign(VerticalAlign.MIDDLE);
		icon32.setFontSize("1.0em");
		icon32.setBorderRight("1px solid #e0e0e0");
		icon32.setHeight("26px");
		icon32.setMargin(0);
		icon32.setWidth("26px");
		icon32.setFloat(com.google.gwt.dom.client.Style.Float.LEFT);
		icon32.addClickHandler(event->{
			if (contentTree.getSelectedItem().getIcon().getIconType().equals(IconType.SUBJECT)) {

				ContentTreeItem pitem = (ContentTreeItem) contentTree.getSelectedItem();
				ContentTreeItem item = createImageItem(pitem, IconType.REMOVE, "끝맺음", 560 * 3, DatabaseContentType.INPUT_CLOSING_LINE, "", IDUtil.uuid());

				double contentOrder = ((ContentTreeItem)contentTree.getSelectedItem().getParent()).getWidgetIndex(pitem);
				double articleOrder =  contentTree.getSelectedItem().getWidgetIndex(item);
				
				item.getInternalReferences().put("IS_TITLE", false);
				item.getInternalReferences().put("TBL", "ARTICLE_CONTENT");
				item.getInternalReferences().put("COL_TITLE", "ARTICLE_BODY");
				item.getInternalReferences().put("ATM_ID", contentTree.getAtmId());
				item.getInternalReferences().put("ACI_ID", IDUtil.uuid().toString());
				item.getInternalReferences().put("ARTICLE_TITLE", "-");
				item.getInternalReferences().put("ARTICLE_BODY", "-");
				item.getInternalReferences().put("CONTENT_ORDER", contentOrder);
				item.getInternalReferences().put("ARTICLE_ORDER", articleOrder);
				item.getInternalReferences().put("ARTICLE_SUB_ORDER", 0d);
				item.getInternalReferences().put("ARTICLE_TYPE", 5d);
				item.getInternalReferences().put("CONTENT_TREE", this);
				
				reOrderItems();
				editorPreview.render();
			}
		});

		icon33 = new MaterialIcon(IconType.INFO);
		icon33.setTooltip("여행정보 생성");
		icon33.setLineHeight(26);
		icon33.setVerticalAlign(VerticalAlign.MIDDLE);
		icon33.setFontSize("1.0em");
		icon33.setBorderRight("1px solid #e0e0e0");
		icon33.setHeight("26px");
		icon33.setMargin(0);
		icon33.setWidth("26px");
		icon33.setFloat(com.google.gwt.dom.client.Style.Float.LEFT);
		icon33.addClickHandler(event->{
			if (contentTree.getSelectedItem().getIcon().getIconType().equals(IconType.SUBJECT)) {

				ContentTreeItem pitem = (ContentTreeItem) contentTree.getSelectedItem();
				ContentTreeItem item = createImageItem(pitem, IconType.INFO, "여행정보", 560 * 3, DatabaseContentType.INPUT_TRAVEL_INFO, "", "0");

				double contentOrder = ((ContentTreeItem)contentTree.getSelectedItem().getParent()).getWidgetIndex(pitem);
				double articleOrder =  contentTree.getSelectedItem().getWidgetIndex(item);

				item.getInternalReferences().put("IS_TITLE", false);
				item.getInternalReferences().put("TBL", "ARTICLE_CONTENT");
				item.getInternalReferences().put("COL_TITLE", "ARTICLE_BODY");
				item.getInternalReferences().put("ATM_ID", contentTree.getAtmId());
				item.getInternalReferences().put("ACI_ID", IDUtil.uuid().toString());
				item.getInternalReferences().put("ARTICLE_TITLE", "");
				item.getInternalReferences().put("ARTICLE_BODY", "");
				item.getInternalReferences().put("CONTENT_ORDER", contentOrder);
				item.getInternalReferences().put("ARTICLE_ORDER", articleOrder);
				item.getInternalReferences().put("ARTICLE_SUB_ORDER", 0d);
				item.getInternalReferences().put("ARTICLE_TYPE", 6d);
				item.getInternalReferences().put("CONTENT_TREE", this);
				item.getInternalReferences().put("IS_BOX", (double) 0);
				item.getInternalReferences().put("IS_VERTICAL", (double) 0);
				
				reOrderItems();
				editorPreview.render();
			}
		});
		
		icon34 = new MaterialIcon(IconType.LABEL);
		icon34.setTooltip("쿠폰정보 생성");
		icon34.setLineHeight(26);
		icon34.setVerticalAlign(VerticalAlign.MIDDLE);
		icon34.setFontSize("1.0em");
		icon34.setBorderRight("1px solid #e0e0e0");
		icon34.setHeight("26px");
		icon34.setMargin(0);
		icon34.setWidth("26px");
		icon34.setFloat(com.google.gwt.dom.client.Style.Float.LEFT);
		icon34.addClickHandler(event->{
			if (contentTree.getSelectedItem().getIcon().getIconType().equals(IconType.SUBJECT)) {

				ContentTreeItem pitem = (ContentTreeItem) contentTree.getSelectedItem();
				ContentTreeItem item = createImageItem(pitem, IconType.LABEL, "쿠폰정보", 560 * 3, DatabaseContentType.INPUT_COUPON_INFO, "", "0");

				double contentOrder = ((ContentTreeItem)contentTree.getSelectedItem().getParent()).getWidgetIndex(pitem);
				double articleOrder =  contentTree.getSelectedItem().getWidgetIndex(item);

				item.getInternalReferences().put("IS_TITLE", false);
				item.getInternalReferences().put("TBL", "ARTICLE_CONTENT");
				item.getInternalReferences().put("COL_TITLE", "ARTICLE_BODY");
				item.getInternalReferences().put("ATM_ID", contentTree.getAtmId());
				item.getInternalReferences().put("ACI_ID", IDUtil.uuid().toString());
				item.getInternalReferences().put("ARTICLE_TITLE", "");
				item.getInternalReferences().put("ARTICLE_BODY", "");
				item.getInternalReferences().put("CONTENT_ORDER", contentOrder);
				item.getInternalReferences().put("ARTICLE_ORDER", articleOrder);
				item.getInternalReferences().put("ARTICLE_SUB_ORDER", 0d);
				item.getInternalReferences().put("ARTICLE_TYPE", 7d);
				item.getInternalReferences().put("CONTENT_TREE", this);
				item.getInternalReferences().put("IS_BOX", (double) 0);
				item.getInternalReferences().put("IS_VERTICAL", (double) 0);
				
				reOrderItems();
				editorPreview.render();
			}
		});
		
		
		icon4 = new MaterialIcon(IconType.REMOVE);
		icon4.setTooltip("선택 삭제");
		icon4.setLineHeight(26);
		icon4.setVerticalAlign(VerticalAlign.MIDDLE);
		icon4.setFontSize("1.0em");
		icon4.setBorderRight("1px solid #e0e0e0");
		icon4.setHeight("26px");
		icon4.setMargin(0);
		icon4.setWidth("26px");
		icon4.setFloat(com.google.gwt.dom.client.Style.Float.LEFT);
		icon4.addClickHandler(event->{
			
			IconType sIconType = contentTree.getSelectedItem().getIcon().getIconType();
			
			if (sIconType.equals(IconType.SUBJECT)) {
				if (contentTree.getSelectedItem().getChildrenList().size() > 1) {
					Map<String, Object> paramters = new HashMap<String, Object>();
					paramters.put("CONTENT_NODE", contentTree.getSelectedItem());
					paramters.put("REMOVE_TARGET", editorPreview);
					getMaterialExtentsWindow().openDialog(RecommApplication.REMOVE_CONTENT_NODE, paramters, 800);
				}
			}else  if (sIconType.equals(IconType.INFO_OUTLINE) || sIconType.equals(IconType.MOVIE) || sIconType.equals(IconType.IMAGE) || sIconType.equals(IconType.VIEW_HEADLINE)) {
				Map<String, Object> paramters = new HashMap<String, Object>();
				paramters.put("CONTENT_NODE", contentTree.getSelectedItem());
				paramters.put("REMOVE_TARGET", editorPreview);
				getMaterialExtentsWindow().openDialog(RecommApplication.REMOVE_CONTENT_NODE, paramters, 800);
			}
			
			reOrderItems();
			editorPreview.render();

			
		});
		
		MaterialIcon icon5 = new MaterialIcon(IconType.KEYBOARD_ARROW_RIGHT);
		icon5.setLineHeight(26);
		icon5.setVerticalAlign(VerticalAlign.MIDDLE);
		icon5.setFontSize("1.0em");
		icon5.setBorderLeft("1px solid #e0e0e0");
		icon5.setHeight("26px");
		icon5.setMargin(0);
		icon5.setWidth("26px");
		icon5.setFloat(com.google.gwt.dom.client.Style.Float.RIGHT);
		icon5.addClickHandler(event->{
			goTree(2);
		});
		
		MaterialIcon icon6 = new MaterialIcon(IconType.KEYBOARD_ARROW_LEFT);
		icon6.setLineHeight(26);
		icon6.setVerticalAlign(VerticalAlign.MIDDLE);
		icon6.setFontSize("1.0em");
		icon6.setBorderLeft("1px solid #e0e0e0");
		icon6.setHeight("26px");
		icon6.setMargin(0);
		icon6.setWidth("26px");
		icon6.setFloat(com.google.gwt.dom.client.Style.Float.RIGHT);
		icon6.addClickHandler(event->{
			goTree(1);
		});
		
		panelBottom.add(icon1);
		panelBottom.add(icon2);
		panelBottom.add(icon3);
		panelBottom.add(icon31);
		panelBottom.add(icon32);
		panelBottom.add(icon33);
		
		panelBottom.add(icon34);
/*		
		panelBottom.add(icon32);
		panelBottom.add(icon4);
 */
		panelBottom.add(icon5);
		panelBottom.add(icon6);
		
		col1.add(fixedTop);
		col1.add(panelBottom);
		
		MaterialPanel detailPanel = new MaterialPanel();
		detailPanel.setLayoutPosition(Position.RELATIVE);
		detailPanel.setTop(20);
		detailPanel.setOverflow(Overflow.HIDDEN);
		detailPanel.setPadding(10);
		detailPanel.setBorder("1px solid #e0e0e0");
		detailPanel.setHeight("100%");
		
		detailSliderPanel = new MaterialPanel();
		detailSliderPanel.setLayoutPosition(Position.ABSOLUTE);
		detailSliderPanel.setTop(0);
		detailSliderPanel.setLeft(0);
		detailSliderPanel.setWidth(((959 * 9) + 36 )  + "px");
		detailSliderPanel.setHeight("100%");
		
		this.contentBase = new RecommContentsBase(this.getMaterialExtentsWindow());
		this.contentTag = new RecommContentsTags(this.getMaterialExtentsWindow());
		this.contentETC = new RecommContentsETC(this.getMaterialExtentsWindow());
		this.editorPreview = new RecommContentsDetails(this.getMaterialExtentsWindow(), this);
		this.contentRelatedSights = new RecommContentsRelatedRecommandAndSights(this.getMaterialExtentsWindow(), this);
		this.contentRelatedFestival = new RecommContentsRelatedCourseAndFestival(this.getMaterialExtentsWindow(), this);
		this.editorComposite = new RecommContentsEditor(this.getMaterialExtentsWindow());
		this.contentsRelatedImage = new RecommContentsRelatedImage(this.getMaterialExtentsWindow(),editorPreview);
		
		detailSliderPanel.add(this.contentBase);
		detailSliderPanel.add(this.contentETC);
		detailSliderPanel.add(this.contentTag);
		detailSliderPanel.add(this.editorPreview);
		detailSliderPanel.add(new RecommContentsPartners(this.getMaterialExtentsWindow()));
		detailSliderPanel.add(this.contentsRelatedImage);
		detailSliderPanel.add(this.contentRelatedSights);
		detailSliderPanel.add(this.contentRelatedFestival);
		detailSliderPanel.add(this.editorComposite);

		detailPanel.add(detailSliderPanel);

		col2.add(detailPanel);
		
		this.add(row);
		
	}

	private void doLoadRelatedArticleView() {
		JSONObject parameterJSON = new JSONObject();
		parameterJSON.put("cmd", new JSONString("SELECT_ARTICLE_VIEW"));
		parameterJSON.put("ATM_ID", new JSONString(this.atmid));
		VisitKoreaBusiness.post("call", parameterJSON.toString(), (param1, param2, param3) -> {
			JSONObject resultObj = JSONParser.parseStrict(JSON.stringify((JsObject) param1)).isObject();
			String process = resultObj.get("header").isObject().get("process").isString().stringValue();
			
			if (process.equals("success")) {
				JSONObject bodyResultObj = resultObj.get("body").isObject().get("result").isObject();
				if (!bodyResultObj.containsKey("RECOMM_VIEW"))
					bodyResultObj.put("RECOMM_VIEW", new JSONNumber(1));
				if (!bodyResultObj.containsKey("SIGHT_VIEW"))
					bodyResultObj.put("SIGHT_VIEW", new JSONNumber(1));
				if (!bodyResultObj.containsKey("COURSE_VIEW"))
					bodyResultObj.put("COURSE_VIEW", new JSONNumber(1));
				if (!bodyResultObj.containsKey("FESTIVAL_VIEW"))
					bodyResultObj.put("FESTIVAL_VIEW", new JSONNumber(1));
				
				this.contentRelatedFestival.setViewPanelData(bodyResultObj);
				this.contentRelatedSights.setViewPanelData(bodyResultObj);
			}
		});
	}
	
	private ContentTreeItem[] createSectionOne(MaterialTreeItem parentMaterialTreeItem, String editorValue) {
		
		ContentTreeItem[] itemArray = new ContentTreeItem[2];
		
		ContentTreeItem tItem = new ContentTreeItem(560 * 3);
		tItem.setTextAlign(TextAlign.LEFT);
		tItem.setFontSize("1.0em");
		tItem.setTextColor(Color.BLUE);
		tItem.setText("문단");
		tItem.setIconType(IconType.SUBJECT);
		tItem.setContentType(DatabaseContentType.NONE);

		ContentTreeItem titleItem = new ContentTreeItem(560 * 8);
		titleItem.setTextAlign(TextAlign.LEFT);
		titleItem.setFontSize("1.0em");
		titleItem.setTextColor(Color.BLUE);
		titleItem.setText("문단 제목");
		titleItem.setIconType(IconType.TITLE);
		titleItem.setContentType(DatabaseContentType.INPUT_TEXT);
		titleItem.setEditorValue(editorValue);
		
		
		
		tItem.add(titleItem);
		parentMaterialTreeItem.add(tItem);
		
		itemArray[0] = tItem;
		itemArray[1] = titleItem;
		
		return itemArray;
	}

	private ContentTreeItem createImageItem(MaterialTreeItem parentMaterialTreeItem, IconType iconType, String title, int slidingValue, DatabaseContentType inputHtml, String imageDescription, String contentValue) {

		ContentTreeItem tItem = new ContentTreeItem(slidingValue);
		tItem.setTextAlign(TextAlign.LEFT);
		tItem.setFontSize("1.0em");
		tItem.setTextColor(Color.BLUE);
		tItem.setText(title);
		tItem.setIconType(iconType);
		tItem.setContentType(inputHtml);
		
		if (iconType.equals(IconType.IMAGE) && contentValue.length() > 0) {
			tItem.setEditorObject(new ArrayList<ItemInformation>());
		} else if((iconType.equals(IconType.INFO) || iconType.equals(IconType.LABEL) ) ){
			
			if( contentValue != null)
				tItem.setEditorTable(contentValue);
			else
				tItem.setEditorTable("0");
		} else {
			tItem.setEditorValue(contentValue);
		}
		
		if (tItem != null && parentMaterialTreeItem != null) parentMaterialTreeItem.add(tItem);
		
		return tItem;
	}

	private void appendImage(ContentTreeItem tItem, String imageId, String imageDescription,String imageCaption ,int iscaption) {

		ImageInformation imageInformation = new ImageInformation((String) Registry.get("image.server") + "/img/call?cmd=VIEW&id=" + imageId + "&chk=" + IDUtil.uuid(16), imageDescription, imageId,imageCaption,iscaption);
		imageInformation.setImgId(imageId);
		List<ItemInformation> itemList = tItem.getEditorObject();
		itemList.add(imageInformation);
		tItem.updateCount();
	}

	public void go(int position) {
		this.nowPosition = position;
		TransitionConfig cfg = new TransitionConfig();
		cfg.setProperty("left");
		cfg.setDuration(100);
		detailSliderPanel.setTransition(cfg);
		detailSliderPanel.setTransform("translate("+nowPosition+"px,0);");
		detailSliderPanel.setLeft(this.nowPosition);
	}

	public void goTree(int position) {
		int newTreePosition = (position-1) * -458;
		TransitionConfig cfg = new TransitionConfig();
		cfg.setProperty("left");
		cfg.setDuration(100);
		topSlider.setTransition(cfg);
		topSlider.setTransform("translate("+newTreePosition+"px,0);");
		topSlider.setLeft(newTreePosition);
		
		visibleBottomButton(false, false, false, false, false, false, false,false);

	}
	
	private void visibleBottomButton(boolean ... visibility) {
		
		icon1.setVisible(visibility[0]);
		icon2.setVisible(visibility[1]);
		icon3.setVisible(visibility[2]);
		
		icon31.setVisible(visibility[3]);
		icon32.setVisible(visibility[4]);
		icon33.setVisible(visibility[5]);
		icon34.setVisible(visibility[6]);
		icon4.setVisible(visibility[7]);
		
	}

	public void setCotId(String id) {
		this.cotid=id;
		this.contentBase.setCotId(id);
		this.contentTag.setCotId(id);
		this.contentETC.setCotId(id);
		this.editorPreview.setCotId(id);
		this.contentRelatedSights.setCotId(id);
		this.contentRelatedFestival.setCotId(id);
		this.contentsRelatedImage.setCotId(id);
	}

	public void loading() {
		
		this.contentsRelatedImage.loadData();

		this.contentBase.loading(true);
		this.contentBase.setTitleArea(this.titleLabel);
		this.contentBase.setTableRow(this.tableRow);
		this.contentBase.loadData();

		this.contentTag.loading(true);
		this.contentTag.loadData(this.tableRow);

		this.contentETC.loading(true);
		this.contentETC.loadData();

		this.editorPreview.loading(true);
		this.editorPreview.loadData();

		this.contentRelatedSights.loading(true);
		this.contentRelatedSights.loadData();

		this.contentRelatedFestival.loading(true);
		this.contentRelatedFestival.loadData();
		
		loadContentTree();
	}

	private void loadContentTree() {
		
		JSONObject parameterJSON = new JSONObject();
		parameterJSON.put("cmd", new JSONString("GET_ATM_ID"));
		parameterJSON.put("cotId", new JSONString(this.cotid));
		parameterJSON.put("chk", new JSONString(IDUtil.uuid()));

		VisitKoreaBusiness.post("call", parameterJSON.toString(), new Func3<Object, String, Object>() {
			@Override
			public void call(Object param1, String param2, Object param3) {
				
				reset(contentTree);
				
				JSONObject resultObj = (JSONObject) JSONParser.parseStrict(JSON.stringify((JsObject)param1));
				JSONObject headerObj = (JSONObject) resultObj.get("header");
				String processResult = headerObj.get("process").isString().toString();
				processResult = processResult.replaceAll("\"", "");
				
				if (processResult.equals("success")) {
					
					JSONValue ATM_ID = resultObj.get("body").isObject().get("result").isObject().get("ATM_ID");
					if (ATM_ID != null) {
						
						atmid = ATM_ID.isString().stringValue();
						contentTree.setAtmId(atmid);
						
						JSONObject parameterJSON = new JSONObject();
						parameterJSON.put("cmd", new JSONString("SELECT_ARTICLE_CONTENT_DETAIL"));
						parameterJSON.put("cotId", new JSONString(cotid));
						parameterJSON.put("chk", new JSONString(IDUtil.uuid()));

						VisitKoreaBusiness.post("call", parameterJSON.toString(), new Func3<Object, String, Object>() {
							@Override
							public void call(Object param1, String param2, Object param3) {
								
								reset(contentTree);
								
								JSONObject resultObj = (JSONObject) JSONParser.parseStrict(JSON.stringify((JsObject)param1));
								JSONObject headerObj = (JSONObject) resultObj.get("header");
								String processResult = headerObj.get("process").isString().toString();
								processResult = processResult.replaceAll("\"", "");
								
								if (processResult.equals("success")) {
									buildArticleResult(resultObj);
									editorPreview.reLoad();
									doLoadRelatedArticleView();
								}
							}
							
						});
					
					}
				}
			}
			
		});
		
	}

	protected void reset(MaterialTree contentTree2) {
		ContentTreeItem parentTreeItem = (ContentTreeItem)contentTree.getChildren().get(0);
		for (Widget widget : parentTreeItem.getChildrenList()) {
			if (widget instanceof ContentTreeItem) {
				ContentTreeItem cti = (ContentTreeItem)widget;
				cti.removeFromParent();
			}
		}
	}

	private void buildArticleResult(JSONObject resultObj) {
		
		JSONObject bodyObj =  resultObj.get("body").isObject();
		JSONArray resultArray = bodyObj.get("result").isArray();
		int resultSize = resultArray.size();

		double contentOrder = -1;
		int articleSubOrder = 0;
		
		ContentTreeItem tgrItem = null;
		ContentTreeItem imgTgrItem = null;
		
		double articleType = 0;
		double articleOrder = 0;
		
		for (int i=0; i<resultSize; i++) {
			
			JSONObject recObj =  resultArray.get(i).isObject();
			Console.log("recObj"+i+" :: " + recObj);
			double recContentOrder = recObj.get("CONTENT_ORDER").isNumber().doubleValue();
			double recArticleOrder = recObj.get("ARTICLE_ORDER").isNumber().doubleValue();
			double recArticleSubOrder = recObj.get("ARTICLE_SUB_ORDER").isNumber().doubleValue();
			double recArticleType = recObj.get("ARTICLE_TYPE").isNumber().doubleValue();
			double recArticleIsBox = recObj.get("IS_BOX").isNumber().doubleValue();
			double recArticleIsVertical = recObj.get("IS_VERTICAL").isNumber().doubleValue();
			double recArticleIsCaption = recObj.get("IS_CAPTION").isNumber().doubleValue();
			
			String imageDescription = "";
			String recAciId = null;
			String imageCaption = "";
			String recAtmId = recObj.get("ATM_ID").isString().stringValue();
			
			if (recObj.get("ACI_ID") != null) recAciId = recObj.get("ACI_ID").isString().stringValue();
			if (recObj.get("IMAGE_DESCRIPTION") != null) imageDescription = recObj.get("IMAGE_DESCRIPTION").isString().stringValue();
			if (recObj.get("IMG_CAPTION") != null) imageCaption = recObj.get("IMG_CAPTION").isString().stringValue();
			
			if (this.atmid == null) this.atmid = recAtmId;
			
			String recArticleBody = null;
			String recArticleTitle = null;
			String recImgId = null;

			if (recArticleType == 1 || recArticleType == 3 || recArticleType == 7) {
				
				if (recObj.get("ARTICLE_BODY") == null) 
					recArticleBody = "";
				else 
					recArticleBody = recObj.get("ARTICLE_BODY").isString().stringValue();
				
				if (recObj.get("ARTICLE_TITLE") != null) 
					recArticleTitle = recObj.get("ARTICLE_TITLE").isString().stringValue();

			}else if (recArticleType == 2) {
			
				recImgId = recObj.get("IMG_ID").isString().stringValue();
			
			}

			if ( (contentOrder != recContentOrder) || recArticleType == 0 ) {
				
				recArticleTitle = recObj.get("ARTICLE_TITLE") == null ? "" : recObj.get("ARTICLE_TITLE").isString().stringValue();
				
				ContentTreeItem[] items = createSectionOne((MaterialTreeItem)contentTree.getChildrenList().get(0), recArticleTitle);
				tgrItem = items[0];

				items[0].getInternalReferences().put("CONTENT_TREE", this);

				items[1].getInternalReferences().put("IS_TITLE", true);
				items[1].getInternalReferences().put("ACI_ID", recAciId);
				items[1].getInternalReferences().put("TBL", "ARTICLE_CONTENT");
				items[1].getInternalReferences().put("COL_TITLE", "ARTICLE_BODY");
				items[1].getInternalReferences().put("ATM_ID", recAtmId);
				items[1].getInternalReferences().put("ARTICLE_TITLE", recArticleTitle);
				items[1].getInternalReferences().put("CONTENT_ORDER", recContentOrder);
				items[1].getInternalReferences().put("ARTICLE_ORDER", recArticleOrder);
				items[1].getInternalReferences().put("ARTICLE_SUB_ORDER", recArticleSubOrder);
				items[1].getInternalReferences().put("ARTICLE_TYPE", 0);
				items[1].getInternalReferences().put("CONTENT_TREE", this);
				items[1].getInternalReferences().put("IS_BOX", recArticleIsBox);
				items[1].getInternalReferences().put("IS_VERTICAL", recArticleIsVertical);

				contentOrder = recContentOrder;
				
			}
					
			if (recArticleType == 1) {

				ContentTreeItem item = createImageItem(tgrItem, IconType.VIEW_HEADLINE, "문장", 560 * 8, DatabaseContentType.INPUT_HTML, "", recArticleBody);

				item.getInternalReferences().put("IS_TITLE", false);
				item.getInternalReferences().put("ACI_ID", recAciId);
				item.getInternalReferences().put("TBL", "ARTICLE_CONTENT");
				item.getInternalReferences().put("COL_TITLE", "ARTICLE_BODY");
				item.getInternalReferences().put("ATM_ID", recAtmId);
				item.getInternalReferences().put("ARTICLE_TITLE", recArticleTitle);
				item.getInternalReferences().put("ARTICLE_BODY", recArticleBody);
				item.getInternalReferences().put("CONTENT_ORDER", recContentOrder);
				item.getInternalReferences().put("ARTICLE_ORDER", recArticleOrder);
				item.getInternalReferences().put("ARTICLE_SUB_ORDER", recArticleSubOrder);
				item.getInternalReferences().put("ARTICLE_TYPE", recArticleType);
				item.getInternalReferences().put("CONTENT_TREE", this);
				item.getInternalReferences().put("IS_BOX", recArticleIsBox);
				item.getInternalReferences().put("IS_VERTICAL", recArticleIsVertical);

			}else if (recArticleType == 2) {

				if (articleType == recArticleType && articleOrder == recArticleOrder ) {
					
					appendImage(imgTgrItem, recImgId, imageDescription,imageCaption,(int)recArticleIsCaption);


					Map<String, Map<String, Object>> idMap = (Map<String, Map<String, Object>>)imgTgrItem.getInternalReferences().get("ID_MAP");
					
					HashMap<String, Object> idValueMap = new HashMap<String, Object>();
					idValueMap.put("ARTICLE_SUB_ORDER", recArticleSubOrder);
					idValueMap.put("IMAGE_DESCRIPTION", imageDescription);
					idValueMap.put("IMAGE_CAPTION", imageCaption);
					idValueMap.put("IS_CAPTION", recArticleIsCaption);
					idMap.put(recImgId, idValueMap);
					
				}else{
					
					imgTgrItem = createImageItem(tgrItem, IconType.IMAGE, "이미지", 560 * 3, DatabaseContentType.INPUT_IMAGE, imageDescription, recImgId);

					appendImage(imgTgrItem, recImgId, imageDescription,imageCaption,(int)recArticleIsCaption);
					
					Map<String, Map<String, Object>> idMap = new HashMap<String, Map<String, Object>>();
					imgTgrItem.getInternalReferences().put("ID_MAP", idMap);
					
					HashMap<String, Object> idValueMap = new HashMap<String, Object>();
					idValueMap.put("ARTICLE_SUB_ORDER", recArticleSubOrder);
					idValueMap.put("IMAGE_DESCRIPTION", imageDescription);
					idValueMap.put("IMAGE_CAPTION", imageCaption);
					idValueMap.put("IS_CAPTION", recArticleIsCaption);
					idMap.put(recImgId, idValueMap);
					
					imgTgrItem.getInternalReferences().put("IS_TITLE", false);
					imgTgrItem.getInternalReferences().put("ACI_ID", recAciId);
					imgTgrItem.getInternalReferences().put("TBL", "ARTICLE_CONTENT");
					imgTgrItem.getInternalReferences().put("ATM_ID", recAtmId);
					imgTgrItem.getInternalReferences().put("CONTENT_ORDER", recContentOrder);
					imgTgrItem.getInternalReferences().put("ARTICLE_ORDER", recArticleOrder);
					imgTgrItem.getInternalReferences().put("ARTICLE_TYPE", 2);
					imgTgrItem.getInternalReferences().put("CONTENT_TREE", this);
					
				}
				
			}else if (recArticleType == 3) {

				if (recObj.get("ARTICLE_TITLE") != null) recArticleTitle = recObj.get("ARTICLE_TITLE").isString().stringValue();
				
				ContentTreeItem item = createImageItem(tgrItem, IconType.MOVIE, "동영상", 560 * 8, DatabaseContentType.INPUT_MOVIE, "", recArticleBody);

				item.getInternalReferences().put("IS_TITLE", false);
				item.getInternalReferences().put("ACI_ID", recAciId);
				item.getInternalReferences().put("TBL", "ARTICLE_CONTENT");
				item.getInternalReferences().put("COL_TITLE", "ARTICLE_BODY");
				item.getInternalReferences().put("ATM_ID", recAtmId);
				item.getInternalReferences().put("ARTICLE_TITLE", recArticleTitle);
				item.getInternalReferences().put("ARTICLE_BODY", recArticleBody);
				item.getInternalReferences().put("CONTENT_ORDER", recContentOrder);
				item.getInternalReferences().put("ARTICLE_ORDER", recArticleOrder);
				item.getInternalReferences().put("ARTICLE_SUB_ORDER", recArticleSubOrder);
				item.getInternalReferences().put("ARTICLE_TYPE", recArticleType);
				item.getInternalReferences().put("CONTENT_TREE", this);

			}else if (recArticleType == 5) {
	
				ContentTreeItem item = createImageItem(tgrItem, IconType.REMOVE, "끝맺음", 560 * 8, DatabaseContentType.INPUT_CLOSING_LINE, "", recArticleBody);
	
				item.getInternalReferences().put("IS_TITLE", false);
				item.getInternalReferences().put("ACI_ID", recAciId);
				item.getInternalReferences().put("TBL", "ARTICLE_CONTENT");
				item.getInternalReferences().put("COL_TITLE", "ARTICLE_BODY");
				item.getInternalReferences().put("ATM_ID", recAtmId);
				item.getInternalReferences().put("CONTENT_ORDER", recContentOrder);
				item.getInternalReferences().put("ARTICLE_ORDER", recArticleOrder);
				item.getInternalReferences().put("ARTICLE_SUB_ORDER", recArticleSubOrder);
				item.getInternalReferences().put("ARTICLE_TYPE", recArticleType);
				item.getInternalReferences().put("CONTENT_TREE", this);
				
			}else if (recArticleType == 6) {
	
				ContentTreeItem item = createImageItem(tgrItem, IconType.INFO, "여행정보", 560 * 8, DatabaseContentType.INPUT_TRAVEL_INFO, "", recArticleBody);
	
				item.getInternalReferences().put("IS_TITLE", false);
				item.getInternalReferences().put("ACI_ID", recAciId);
				item.getInternalReferences().put("TBL", "ARTICLE_CONTENT");
				item.getInternalReferences().put("COL_TITLE", "ARTICLE_BODY");
				item.getInternalReferences().put("ATM_ID", recAtmId);
				item.getInternalReferences().put("CONTENT_ORDER", recContentOrder);
				item.getInternalReferences().put("ARTICLE_ORDER", recArticleOrder);
				item.getInternalReferences().put("ARTICLE_SUB_ORDER", recArticleSubOrder);
				item.getInternalReferences().put("ARTICLE_TYPE", recArticleType);
				item.getInternalReferences().put("CONTENT_TREE", this);
				item.getInternalReferences().put("ARTICLE_BODY", recArticleBody);
			} else if (recArticleType == 7) {
	
				ContentTreeItem item = createImageItem(tgrItem, IconType.LABEL, "쿠폰정보", 560 * 8, DatabaseContentType.INPUT_COUPON_INFO, "", recArticleBody);
	
				item.getInternalReferences().put("ARTICLE_TITLE", recArticleTitle);
				item.getInternalReferences().put("IS_TITLE", false);
				item.getInternalReferences().put("ACI_ID", recAciId);
				item.getInternalReferences().put("TBL", "ARTICLE_CONTENT");
				item.getInternalReferences().put("COL_TITLE", "ARTICLE_BODY");
				item.getInternalReferences().put("ATM_ID", recAtmId);
				item.getInternalReferences().put("CONTENT_ORDER", recContentOrder);
				item.getInternalReferences().put("ARTICLE_ORDER", recArticleOrder);
				item.getInternalReferences().put("ARTICLE_SUB_ORDER", recArticleSubOrder);
				item.getInternalReferences().put("ARTICLE_TYPE", recArticleType);
				item.getInternalReferences().put("CONTENT_TREE", this);
				item.getInternalReferences().put("ARTICLE_BODY", recArticleBody);
			}
			
			articleType = recArticleType;
			articleOrder = recArticleOrder;
		}
	}

	public void setRow(ContentTableRow tableRow) {
		this.tableRow = tableRow;
	}
	
	public void reOrderItems() {
		List<MaterialTreeItem> contentItems = mainContentTreeItem.getTreeItems();
		
		double contentItemOrder = 0;
		
		for (MaterialTreeItem item : contentItems) {
			ContentTreeItem cItem = (ContentTreeItem)item;
			cItem.getInternalReferences().put("CONTENT_ORDER", contentItemOrder);

			double articleItemOrder = 0;
			
			List<MaterialTreeItem> ccItems = cItem.getTreeItems();
			for (MaterialTreeItem ccItem : ccItems) {
				ContentTreeItem cccItem = (ContentTreeItem)ccItem;
				cccItem.getInternalReferences().put("CONTENT_ORDER", contentItemOrder);
				cccItem.getInternalReferences().put("ARTICLE_ORDER", articleItemOrder);
				articleItemOrder++;
			}
			contentItemOrder++;
		}

	}

	public String getAtmid() {
		return atmid;
	}
	
	public RecommContentsRelatedImage getcontentsRelatedImage() {
		return contentsRelatedImage;
	}
}
