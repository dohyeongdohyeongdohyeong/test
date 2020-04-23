package kr.or.visitkorea.admin.client.manager.contents.database;

import java.util.List;

import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Style;
import com.google.gwt.dom.client.Style.FontWeight;
import com.google.gwt.dom.client.Style.Overflow;
import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.dom.client.Style.VerticalAlign;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.user.client.ui.Widget;

import gwt.material.design.addins.client.tree.MaterialTree;
import gwt.material.design.addins.client.tree.MaterialTreeItem;
import gwt.material.design.client.MaterialDesignBase;
import gwt.material.design.client.base.MaterialWidget;
import gwt.material.design.client.base.TransitionConfig;
import gwt.material.design.client.constants.Color;
import gwt.material.design.client.constants.Display;
import gwt.material.design.client.constants.IconType;
import gwt.material.design.client.constants.TextAlign;
import gwt.material.design.client.constants.WavesType;
import gwt.material.design.client.ui.MaterialCheckBox;
import gwt.material.design.client.ui.MaterialColumn;
import gwt.material.design.client.ui.MaterialIcon;
import gwt.material.design.client.ui.MaterialLabel;
import gwt.material.design.client.ui.MaterialPanel;
import gwt.material.design.client.ui.MaterialRow;
import gwt.material.design.jscore.client.api.JSON;
import gwt.material.design.jscore.client.api.JsObject;
import kr.or.visitkorea.admin.client.application.Console;
import kr.or.visitkorea.admin.client.manager.VisitKoreaBusiness;
import kr.or.visitkorea.admin.client.manager.contents.database.composite.ContentBanner;
import kr.or.visitkorea.admin.client.manager.contents.database.composite.ContentsBase;
import kr.or.visitkorea.admin.client.manager.contents.database.composite.ContentsCatchplace;
import kr.or.visitkorea.admin.client.manager.contents.database.composite.ContentsDetails;
import kr.or.visitkorea.admin.client.manager.contents.database.composite.ContentsETC;
import kr.or.visitkorea.admin.client.manager.contents.database.composite.ContentsImages;
import kr.or.visitkorea.admin.client.manager.contents.database.composite.ContentsLinks;
import kr.or.visitkorea.admin.client.manager.contents.database.composite.ContentsPamphlets;
import kr.or.visitkorea.admin.client.manager.contents.database.composite.ContentsTags;
import kr.or.visitkorea.admin.client.manager.contents.database.composite.RelatedCotnents;
import kr.or.visitkorea.admin.client.manager.widgets.AbstractContentPanel;
import kr.or.visitkorea.admin.client.manager.widgets.ContentDetailItem;
import kr.or.visitkorea.admin.client.manager.widgets.ContentTableRow;
import kr.or.visitkorea.admin.client.manager.widgets.ContentTree;
import kr.or.visitkorea.admin.client.manager.widgets.ContentTreeItem;
import kr.or.visitkorea.admin.client.widgets.window.MaterialExtentsWindow;

public class DatabaseContentsTree extends AbstractContentPanel {

	static {
		MaterialDesignBase.injectCss(DatabaseContentBundle.INSTANCE.contentCss());
	}

	private MaterialPanel panelTop;
	private MaterialPanel panelContentTop;
	private MaterialPanel topSlider;
	private MaterialPanel fixedTop;
	private int nowPosition;
	private MaterialWidget detailSliderPanel;
	private MaterialTree tree;
	private ContentsDetails editorPreview;
	private ContentTree contentTree;
	private ContentTableRow row;
	private String cotId;
	private String otdId;
	private MaterialLabel titleLabel;
	private JSONObject RETOBJ;
	private RelatedCotnents relatedCotnents;
	private ContentsETC contentsETC;
	private ContentsLinks contentsLinks;
	private ContentsTags contentsTags;
	private ContentsBase contentsBase;
	private ContentsCatchplace contentsCatchplace;
	private ContentsPamphlets contentsPamphlets;
	private ContentBanner contentBanner;
	private ContentTreeItem tItem109;
	private ContentsImages contentsImages;
	
	public DatabaseContentsTree(MaterialExtentsWindow meWindow) {
		super(meWindow);
	}

	public void init() {
		
		editorPreview = new ContentsDetails(getMaterialExtentsWindow());

		initLayout();

		buildTitle();
		buildLayout();
		buildMainTree();
		buildContentTree();

		add(createAddIcon());
	}

	private void initLayout() {
		setLayoutPosition(Position.RELATIVE);
		setTextAlign(TextAlign.CENTER);
		setFloat(Style.Float.LEFT);
		setHeight("100%");
	}
	
	private MaterialIcon createAddIcon() {
		MaterialIcon addIcon = new MaterialIcon(IconType.KEYBOARD_ARROW_LEFT);
		addIcon.setLayoutPosition(Position.ABSOLUTE);
		addIcon.setTooltip("목록으로");
		addIcon.setRight(30);
		addIcon.setTop(25);
		addIcon.setWidth("24");
		addIcon.setBorder("1px solid #e0e0e0");
		addIcon.addClickHandler(event->{
			getMaterialExtentsWindow().goContentSlider(0);;
		});
		return addIcon;
	}

	public void setTitle(String title) {
		super.setTitle(title);
		titleLabel.setText("- " + title);
	}

	private void buildTitle() {
		titleLabel = new MaterialLabel("- 경기북부 어린이 박물관");
		titleLabel.setMarginTop(15);
		titleLabel.setTextAlign(TextAlign.LEFT);
		titleLabel.setPaddingLeft(30);
		titleLabel.setFontWeight(FontWeight.BOLD);
		titleLabel.setTextColor(Color.BLUE);
		titleLabel.setFontSize("1.7em");
		add(titleLabel);
	}

	private void buildContentTree() {

		contentTree = new ContentTree();

		panelContentTop = new MaterialPanel();
		panelContentTop.setStyle("overflow-x: hidden; overflow-y: auto;"); 
		panelContentTop.setFloat(com.google.gwt.dom.client.Style.Float.LEFT);
		panelContentTop.setWidth("334px");
		panelContentTop.setHeight("100%");
		panelContentTop.add(contentTree);
		
		topSlider.add(panelContentTop);
	}

	private void buildContentsTree(String typeString) {
		contentTree.clear();
		new DatabaseTreeBuilder(contentTree, typeString).build();
		contentTree.onLoad();
	}
	
	private void buildMainTree() {
		
		tree = new ContentTree();

		ContentTreeItem tItem101 = createContentTreeItem(0, "기본 정보", IconType.INFO);
		ContentTreeItem tItem102 = createContentTreeItem(1, "기타", IconType.LOCAL_POST_OFFICE);
		ContentTreeItem tItem103 = createContentTreeItem(2, "태그", IconType.CODE);
		ContentTreeItem tItem104 = createContentTreeItem(3, "링크(연결) 정보", IconType.LINK);
		ContentTreeItem tItem105 = createContentTreeItem(4, "상세 정보", IconType.COLLECTIONS);
		ContentTreeItem tItem106 = createContentTreeItem(5, "캐치프레이즈", IconType.SETTINGS);
		ContentTreeItem tItem107 = createContentTreeItem(6, "팜플렛", IconType.DASHBOARD);
		ContentTreeItem tItem108 = createContentTreeItem(7, "컨텐츠에 등록된 이미지", IconType.IMAGE);
		
		tItem109 = createContentTreeItem(8, "홍보 배너", IconType.PANORAMA);

		tree.add(tItem101);
		tree.add(tItem102);
		tree.add(tItem103);
		tree.add(tItem104);
		tree.add(tItem105);
		tree.add(tItem106);
		tree.add(tItem107);
		tree.add(tItem108);
		tree.add(tItem109);
		
		tree.addSelectionHandler(event -> {
			
			ContentTreeItem treeItem = (ContentTreeItem)event.getSelectedItem();
			int slidingValue = treeItem.getSlidingValue();

			if (treeItem.getIcon().getIconType().equals(IconType.COLLECTIONS)) {
				
				int contentType = getContentType();
				String cmd = null;
				if (contentType == 12) {
					
					buildContentsTree(DatabaseTreeBuilder.관광지);
					cmd = "GET_TOURIST_SPOT";
					
				} else if (contentType == 14) {
					
					buildContentsTree(DatabaseTreeBuilder.문화시설);
					cmd = "GET_CULTURAL_FACILITIES";
					
				} else if (contentType == 15) {
					
					buildContentsTree(DatabaseTreeBuilder.축제공연행사);
					cmd = "GET_FESTIVAL";
					
				} else if (contentType == 25) {
					
					String cat2 = RETOBJ.get("CAT2").isString().stringValue();
					if (cat2.equals("C0201")) {
						buildContentsTree(DatabaseTreeBuilder.시티투어);
						cmd = "GET_CITY_TOUR";
					} else {
						buildContentsTree(DatabaseTreeBuilder.여행코스);
						cmd = "GET_COURSE";
					}
					
				} else if (contentType == 28) {
					
					buildContentsTree(DatabaseTreeBuilder.레포츠);
					cmd = "GET_LEPORTS";
					
				} else if (contentType == 32) {
					
					buildContentsTree(DatabaseTreeBuilder.숙박);
					cmd = "GET_ACCOMMODATION";
					
				} else if (contentType == 38) {
					
					buildContentsTree(DatabaseTreeBuilder.쇼핑);
					cmd = "GET_SHOPPING";
					
				} else if (contentType == 39) {
					
					buildContentsTree(DatabaseTreeBuilder.음식점);
					cmd = "GET_EATERY";
				} else if (contentType == 2000) {
					
					buildContentsTree(DatabaseTreeBuilder.생태관광);
					cmd = "GET_GREEN";
					
				} else if (contentType == 25000) {
					
					buildContentsTree(DatabaseTreeBuilder.시티투어);
					cmd = "GET_CITY_TOUR";
				}

				editorPreview.loading(true);
				
				JSONObject parameterJSON = new JSONObject();
				parameterJSON.put("cmd", new JSONString(cmd));
				parameterJSON.put("cotId", new JSONString(cotId));

				ValueMapper mapper = ValueTreeMapper.newInstance(cmd, getMaterialExtentsWindow());
				
				//VK.post("call", parameterJSON.toString(), (Object param1, String param2, Object param3) -> {
				VisitKoreaBusiness.post("call", parameterJSON.toString(), (Object param1, String param2, Object param3) -> {
					JSONObject resultObj = (JSONObject) JSONParser.parseStrict(JSON.stringify((JsObject)param1));
					JSONObject headerObj = (JSONObject) resultObj.get("header");
					String processResult = headerObj.get("process").isString().toString();
					processResult = processResult.replaceAll("\"", "");

					if (processResult.equals("success")) {
						JSONObject bodyObj =  resultObj.get("body").isObject();
						JSONObject resultObject = bodyObj.get("result").isObject();

						contentTree.setupResult(resultObject);
						contentTree.setupValueMapper(mapper);
						editorPreview.goTop(0);
						editorPreview.loading(false);
						buildPreviewContents();
					}
				});
				
			}

			go(slidingValue);
		});
		
		panelTop.add(tree);
	}

	private int getContentType() {
		return (int) RETOBJ.get("CONTENT_TYPE").isNumber().doubleValue();
	}

	private ContentTreeItem createContentTreeItem(int i, String text, IconType icon) {
		
		final int CONTENT_WIDTH = 1083;
		
		ContentTreeItem item = new ContentTreeItem(CONTENT_WIDTH * i);
		item.setTextAlign(TextAlign.LEFT);
		item.setFontSize("1.0em");
		item.setTextColor(Color.BLUE);
		item.setText(text);
		item.setIconType(icon);
		
		return item;
	}

	private void buildLayout() {
		
		MaterialRow row = new MaterialRow();
		row.setTop(50);
		row.setHeight("605px");
		
		MaterialColumn col1 = new MaterialColumn();
		col1.setPaddingLeft(30);
		col1.setPaddingBottom(30);
		col1.setPaddingTop(22);
		col1.setGrid("s3");
		col1.setHeight("100%");
		
		MaterialColumn col2 = new MaterialColumn();
		col2.setPaddingRight(30);
		col2.setPaddingBottom(30);
		col2.setPaddingTop(22);
		col2.setGrid("s9");
		col2.setHeight("100%");
		
		row.add(col1);
		row.add(col2);

		fixedTop = new MaterialPanel();
		fixedTop.setWidth("334px");
		fixedTop.setHeight("100%");
		fixedTop.setOverflow(Overflow.HIDDEN);
		fixedTop.setBorder("1px solid #e0e0e0");
		fixedTop.setLayoutPosition(Position.RELATIVE);
		
		topSlider = new MaterialPanel();
		topSlider.setLayoutPosition(Position.ABSOLUTE);
		topSlider.setTop(0);
		topSlider.setLeft(0);
		topSlider.setOverflow(Overflow.HIDDEN);
		topSlider.setWidth(((260*3)) + "px");
		topSlider.setHeight("100%");
		
		panelTop = new MaterialPanel();
		panelTop.setStyle("overflow-x: hidden; overflow-y: auto;"); 
		panelTop.setLayoutPosition(Position.RELATIVE);
		panelTop.setFloat(com.google.gwt.dom.client.Style.Float.LEFT);
		panelTop.setWidth("334px");
		panelTop.setHeight("100%");

		topSlider.add(panelTop);
		
		fixedTop.add(topSlider);
		
		MaterialPanel panelBottom = new MaterialPanel();
		panelBottom.setBorder("1px solid #e0e0e0");
		panelBottom.setHeight("26px");
		panelBottom.setPadding(0);
		panelBottom.setTop(-2);
		panelBottom.setLayoutPosition(Position.RELATIVE);
		
		MaterialIcon icon5 = new MaterialIcon(IconType.KEYBOARD_ARROW_RIGHT);
		icon5.setWaves(WavesType.DEFAULT);
		icon5.setLineHeight(16);
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
		icon6.setWaves(WavesType.DEFAULT);
		icon6.setLineHeight(16);
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
		
		panelBottom.add(icon5);
		panelBottom.add(icon6);
		
		col1.add(fixedTop);

		MaterialPanel detailPanel = new MaterialPanel();
		detailPanel.setLayoutPosition(Position.RELATIVE);
		detailPanel.setOverflow(Overflow.HIDDEN);
		detailPanel.setPadding(10);
		detailPanel.setBorder("1px solid #e0e0e0");
		detailPanel.setHeight("100%");
		
		detailSliderPanel = new MaterialPanel();
		detailSliderPanel.setLayoutPosition(Position.ABSOLUTE);
		detailSliderPanel.setTop(0);
		detailSliderPanel.setLeft(0);
		detailSliderPanel.setWidth(((1083 * 9) + 36 )  + "px");
		detailSliderPanel.setHeight("100%");
		
		contentsBase = new ContentsBase(getMaterialExtentsWindow());
		contentsTags = new ContentsTags(getMaterialExtentsWindow());
		contentsLinks = new ContentsLinks(getMaterialExtentsWindow());
		contentsETC = new ContentsETC(getMaterialExtentsWindow());
		contentsCatchplace = new ContentsCatchplace(getMaterialExtentsWindow());
		contentsPamphlets = new ContentsPamphlets(getMaterialExtentsWindow());
		contentsImages = new ContentsImages(getMaterialExtentsWindow());
		contentBanner = new ContentBanner(getMaterialExtentsWindow());
		
		
		detailSliderPanel.add(contentsBase);
		detailSliderPanel.add(contentsETC);
		detailSliderPanel.add(contentsTags);
		detailSliderPanel.add(contentsLinks);
		detailSliderPanel.add(editorPreview);
		detailSliderPanel.add(contentsCatchplace);
		detailSliderPanel.add(contentsPamphlets);
		detailSliderPanel.add(contentsImages);
		detailSliderPanel.add(contentBanner);

		detailPanel.add(detailSliderPanel);

		col2.add(detailPanel);
		
		add(row);
	}

	public void go(int position) {
		nowPosition = -position;
		TransitionConfig cfg = new TransitionConfig();
		cfg.setProperty("left");
		cfg.setDuration(100);
		detailSliderPanel.setTransition(cfg);
		detailSliderPanel.setTransform("translate(" + nowPosition + "px,0);");
		detailSliderPanel.setLeft(nowPosition);
	}

	public void goTree(int position) {
		int newTreePosition = (position-1) * -334;
		TransitionConfig cfg = new TransitionConfig();
		cfg.setProperty("left");
		cfg.setDuration(100);
		topSlider.setTransition(cfg);
		topSlider.setTransform("translate("+newTreePosition+"px,0);");
		topSlider.setLeft(newTreePosition);
	}
	

	@Override
	protected void onLoad() {
	   super.onLoad();
	   buildPreviewContents();
	}

	private void buildPreviewContents() {
		editorPreview.clear();
		List<Widget> children = contentTree.getChildrenList();
		for (Widget widget : children) {
			ContentTreeItem treeItem = (ContentTreeItem)widget;
			editorPreview.addContentPanel(buildContent(treeItem));
		}
	}

	private MaterialPanel buildContent(ContentTreeItem treeItem) {

		List<MaterialTreeItem> children = treeItem.getTreeItems();

		String uniqueId = Document.get().createUniqueId();
		treeItem.setId(uniqueId);
		treeItem.setUniqueId(uniqueId);
		
		MaterialPanel mPanel = new MaterialPanel();
		mPanel.setStyle("clear:both");
		mPanel.setScrollspy(uniqueId);
		mPanel.setPaddingLeft(30);
		mPanel.setMarginBottom(10);

		if (children.isEmpty()) {
			
			if (treeItem.getContentType().equals(DatabaseContentType.INPUT_SELECT)) {
				
				MaterialLabel label = new MaterialLabel(treeItem.getText());
				label.setTextAlign(TextAlign.LEFT);
				label.setFontSize("1.2em");
				label.setWidth("150px");
				label.setFontWeight(FontWeight.BOLD);
				label.setMarginBottom(10);
				
				mPanel.add(label);
				
				MaterialPanel checkContentPanel = new MaterialPanel();
				checkContentPanel.setDisplay(Display.INLINE_BLOCK);
				checkContentPanel.setPaddingLeft(30);
				checkContentPanel.setTextAlign(TextAlign.LEFT);
				
				String[] checks = treeItem.getParamters();
				for (String check : checks) {
					MaterialPanel checkPanel = new MaterialPanel();
					MaterialCheckBox checkBox = new MaterialCheckBox();
					checkBox.setText(check);
					checkPanel.add(checkBox);
					checkPanel.setFloat(com.google.gwt.dom.client.Style.Float.LEFT);
					checkPanel.setMarginRight(30);
					checkContentPanel.add(checkPanel);
				}
				mPanel.add(checkContentPanel);
				
			} else {
				
				ContentDetailItem cdi = ContentDetailItem.getInstance(treeItem, false, cotId, false);
				Console.log( treeItem.getTitle() + " :: " + treeItem.getText());
				Console.log("treeItem.getContentType() :: " + treeItem.getContentType());
				
				if (cdi != null) {
					mPanel.add(cdi);
					mPanel.setTextAlign(TextAlign.LEFT);
				}
				
			}
			
		} else {
			
			ContentDetailItem cdi = ContentDetailItem.getInstance(treeItem, true, cotId, false);
			Console.log( treeItem.getTitle() + " :: " + treeItem.getText());
			Console.log("treeItem.getContentType() :: " + treeItem.getContentType());
			
			if (cdi != null) {
				mPanel.add(cdi);
				mPanel.setTextAlign(TextAlign.LEFT);
			}
			
		}

		for (Widget widget : children) {
			if (widget instanceof ContentTreeItem) {
				ContentTreeItem child = (ContentTreeItem) widget;
				mPanel.add(buildContent(child));
			}
		}
		
		return mPanel;
	}

	public void loading() {
		contentsBase.loadData();
		contentsTags.loadData(row);
		contentsLinks.loadData();
		contentsETC.loadData();
		contentsCatchplace.loadData();
		contentsPamphlets.loadData();
		contentsImages.loadData();
	}

	public void setRow(ContentTableRow tableRow) {
		row = tableRow;
		RETOBJ = (JSONObject) row.get("RETOBJ");

		if (getContentType() == 15) {
			tItem109.setVisibility(Style.Visibility.VISIBLE);
		} else {
			tItem109.setVisibility(Style.Visibility.HIDDEN);
		}
	}

	public void setCotId(String cotId) {
		this.cotId = cotId;
		contentsBase.setCotId(cotId);
		contentsTags.setCotId(cotId);
		contentsLinks.setCotId(cotId);
		contentsETC.setCotId(cotId);
		contentsPamphlets.setCotId(cotId);
		contentsCatchplace.setCotId(cotId);
		contentBanner.setCotId(cotId);
		contentsImages.setCotId(cotId);
	}
	
	public void setOtdId(String otdId) {
		this.otdId = otdId;
		Console.log(otdId);
		contentsBase.setOtdId(otdId);
		contentsTags.setOtdId(otdId);
		contentsLinks.setOtdId(otdId);
		contentsETC.setOtdId(otdId);
		contentsPamphlets.setOtdId(otdId);
		contentsCatchplace.setOtdId(otdId);
		contentBanner.setOtdId(otdId);
		contentsImages.setOtdId(otdId);
	}
	
}
