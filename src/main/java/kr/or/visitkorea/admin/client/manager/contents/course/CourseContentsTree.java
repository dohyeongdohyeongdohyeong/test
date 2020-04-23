package kr.or.visitkorea.admin.client.manager.contents.course;

import com.google.gwt.dom.client.Style.FontWeight;
import com.google.gwt.dom.client.Style.Overflow;
import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.dom.client.Style.VerticalAlign;

import gwt.material.design.addins.client.tree.MaterialTree;
import gwt.material.design.client.MaterialDesignBase;
import gwt.material.design.client.base.MaterialWidget;
import gwt.material.design.client.base.TransitionConfig;
import gwt.material.design.client.constants.Color;
import gwt.material.design.client.constants.IconType;
import gwt.material.design.client.constants.TextAlign;
import gwt.material.design.client.constants.WavesType;
import gwt.material.design.client.ui.MaterialColumn;
import gwt.material.design.client.ui.MaterialIcon;
import gwt.material.design.client.ui.MaterialLabel;
import gwt.material.design.client.ui.MaterialPanel;
import gwt.material.design.client.ui.MaterialRow;
import kr.or.visitkorea.admin.client.manager.contents.course.composite.ContentsBase;
import kr.or.visitkorea.admin.client.manager.contents.course.composite.ContentsCourseDetail;
import kr.or.visitkorea.admin.client.manager.contents.course.composite.ContentsInfomation;
import kr.or.visitkorea.admin.client.manager.contents.course.composite.ContentsMasterImage;
import kr.or.visitkorea.admin.client.manager.contents.course.composite.ContentsPartners;
import kr.or.visitkorea.admin.client.manager.contents.course.composite.ContentsTags;
import kr.or.visitkorea.admin.client.manager.widgets.AbstractContentPanel;
import kr.or.visitkorea.admin.client.manager.widgets.ContentTreeItem;
import kr.or.visitkorea.admin.client.widgets.window.MaterialExtentsWindow;

public class CourseContentsTree extends AbstractContentPanel {

	static {
		MaterialDesignBase.injectCss(CourseContentBundle.INSTANCE.contentCss());
	}

	private MaterialPanel panelTop;
	private MaterialPanel panelContentTop;
	private MaterialPanel topSlider;
	private MaterialPanel fixedTop;
	private int nowPosition;
	private MaterialWidget detailSliderPanel;
	private ContentTreeItem contentItem; 
	private ContentTreeItem selectedItem; 
	private ContentTreeItem selectedLastItem;
	private boolean actionOK = false;
	private MaterialTree tree;
	private MaterialTree contentTree;

	public CourseContentsTree(MaterialExtentsWindow meWindow) {
		super(meWindow);
	}

	public void init() {
		
		this.setTextAlign(TextAlign.CENTER);
		this.setFloat(com.google.gwt.dom.client.Style.Float.LEFT);
		this.setHeight("100%");
		
		buildTitle();
	}

	private void buildTitle() {
		
		MaterialLabel titleLabel = new MaterialLabel("- 분야별 상세 정보");
		titleLabel.setMarginTop(15);
		titleLabel.setTextAlign(TextAlign.LEFT);
		titleLabel.setPaddingLeft(30);
		titleLabel.setFontWeight(FontWeight.BOLD);
		titleLabel.setTextColor(Color.BLUE);
		titleLabel.setFontSize("1.7em");
		this.add(titleLabel);
		
		MaterialLabel backLabel = new MaterialLabel("목록으로");
		backLabel.setLayoutPosition(Position.RELATIVE);
		backLabel.setTop(-30);
		backLabel.setLeft(780);
		backLabel.setTextAlign(TextAlign.LEFT);
		backLabel.setPaddingLeft(30);
		backLabel.setFontWeight(FontWeight.BOLD);
		backLabel.setTextColor(Color.BLUE);
		backLabel.setFontSize("1.2em");
		this.add(backLabel);
		
		buildLayout();
		buildMainTree();
		
	}
		
	private void buildMainTree() {
		
		tree = new MaterialTree();
		
		ContentTreeItem tItem101 = new ContentTreeItem(0);
		tItem101.setTextAlign(TextAlign.LEFT);
		tItem101.setFontSize("1.0em");
		tItem101.setTextColor(Color.BLUE);
		tItem101.setText("기본 정보");
		tItem101.setIconType(IconType.INFO);
		
		ContentTreeItem tItem102 = new ContentTreeItem(560 * 1);
		tItem102.setTextAlign(TextAlign.LEFT);
		tItem102.setFontSize("1.0em");
		tItem102.setTextColor(Color.BLUE);
		tItem102.setText("태그");
		tItem102.setIconType(IconType.CODE);
		
		ContentTreeItem tItem103 = new ContentTreeItem(560 * 2);
		tItem103.setTextAlign(TextAlign.LEFT);
		tItem103.setFontSize("1.0em");
		tItem103.setTextColor(Color.BLUE);
		tItem103.setText("개요");
		tItem103.setIconType(IconType.INFO_OUTLINE);

		ContentTreeItem tItem105 = new ContentTreeItem(560 * 3);
		tItem105.setTextAlign(TextAlign.LEFT);
		tItem105.setFontSize("1.0em");
		tItem105.setTextColor(Color.BLUE);
		tItem105.setText("담당부서 및 문의처");
		tItem105.setIconType(IconType.QUESTION_ANSWER);
		
		ContentTreeItem tItem106 = new ContentTreeItem(560 * 4);
		tItem106.setTextAlign(TextAlign.LEFT);
		tItem106.setFontSize("1.0em");
		tItem106.setTextColor(Color.BLUE);
		tItem106.setText("코스 상세");
		tItem106.setIconType(IconType.DETAILS);
		
		ContentTreeItem tItem107 = new ContentTreeItem(560 * 5);
		tItem107.setTextAlign(TextAlign.LEFT);
		tItem107.setFontSize("1.0em");
		tItem107.setTextColor(Color.BLUE);
		tItem107.setText("대표 이미지 설정");
		tItem107.setIconType(IconType.IMAGE);
		
		tree.add(tItem101);
		tree.add(tItem102);
		tree.add(tItem103);
		tree.add(tItem105);
		tree.add(tItem106);
		tree.add(tItem107);
		
		tree.addSelectionHandler(event->{
			
			ContentTreeItem treeItem = (ContentTreeItem)event.getSelectedItem();
			int slidingValue = treeItem.getSlidingValue();
			if (slidingValue > -1) {
				int newSlidingValue = slidingValue * -1;
				go(newSlidingValue);
			}
			
		});
		
		this.panelTop.add(tree);
	}

	private void buildLayout() {
		
		MaterialRow row = new MaterialRow();
		row.setLayoutPosition(Position.RELATIVE);
		row.setTop(-40);
		row.setHeight("625px");
		
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
		fixedTop.setWidth("260px");
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
		panelTop.setWidth("260px");
		panelTop.setHeight("100%");

		topSlider.add(panelTop);
		
		fixedTop.add(topSlider);
		
		MaterialPanel panelBottom = new MaterialPanel();
		panelBottom.setBorder("1px solid #e0e0e0");
		panelBottom.setHeight("26px");
		panelBottom.setPadding(0);
		panelBottom.setTop(-2);
		panelBottom.setLayoutPosition(Position.RELATIVE);
		
		MaterialIcon icon1 = new MaterialIcon(IconType.SUBJECT);
		icon1.setWaves(WavesType.DEFAULT);
		icon1.setLineHeight(16);
		icon1.setFontSize("1.0em");
		icon1.setBorderRight("1px solid #e0e0e0");
		icon1.setHeight("26px");
		icon1.setMargin(0);
		icon1.setWidth("26px");
		icon1.setFloat(com.google.gwt.dom.client.Style.Float.LEFT);
		icon1.addClickHandler(event->{

			if (contentTree.getSelectedItem().getIcon().getIconType().equals(IconType.COLLECTIONS)) {

				ContentTreeItem tItem = new ContentTreeItem(560 * 3);
				tItem.setTextAlign(TextAlign.LEFT);
				tItem.setFontSize("1.0em");
				tItem.setTextColor(Color.BLUE);
				tItem.setText("문단");
				tItem.setIconType(IconType.SUBJECT);
				
				ContentTreeItem titleItem = new ContentTreeItem(560 * 8);
				titleItem.setTextAlign(TextAlign.LEFT);
				titleItem.setFontSize("1.0em");
				titleItem.setTextColor(Color.BLUE);
				titleItem.setText("제목");
				titleItem.setIconType(IconType.TITLE);

				tItem.add(titleItem);
				contentTree.getSelectedItem().add(tItem);
				
			}
		});
		
		MaterialIcon icon2 = new MaterialIcon(IconType.VIEW_HEADLINE); 
		icon2.setWaves(WavesType.DEFAULT);
		icon2.setLineHeight(16);
		icon2.setVerticalAlign(VerticalAlign.MIDDLE);
		icon2.setFontSize("1.0em");
		icon2.setBorderRight("1px solid #e0e0e0");
		icon2.setHeight("26px");
		icon2.setMargin(0);
		icon2.setWidth("26px");
		icon2.setFloat(com.google.gwt.dom.client.Style.Float.LEFT);
		icon2.addClickHandler(event->{
			
			if (contentTree.getSelectedItem().getIcon().getIconType().equals(IconType.SUBJECT)) {
				
				ContentTreeItem tItem = new ContentTreeItem(560 * 8);
				tItem.setTextAlign(TextAlign.LEFT);
				tItem.setFontSize("1.0em");
				tItem.setTextColor(Color.BLUE);
				tItem.setText("문장");
				tItem.setIconType(IconType.VIEW_HEADLINE);
				
				contentTree.getSelectedItem().add(tItem);
				
			}
		});
		
		MaterialIcon icon3 = new MaterialIcon(IconType.IMAGE);
		icon3.setWaves(WavesType.DEFAULT);
		icon3.setLineHeight(16);
		icon3.setVerticalAlign(VerticalAlign.MIDDLE);
		icon3.setFontSize("1.0em");
		icon3.setBorderRight("1px solid #e0e0e0");
		icon3.setHeight("26px");
		icon3.setMargin(0);
		icon3.setWidth("26px");
		icon3.setFloat(com.google.gwt.dom.client.Style.Float.LEFT);
		icon3.addClickHandler(event->{
			
			if (contentTree.getSelectedItem().getIcon().getIconType().equals(IconType.SUBJECT)) {

				ContentTreeItem titleItem = new ContentTreeItem(560 * 3);
				titleItem.setTextAlign(TextAlign.LEFT);
				titleItem.setFontSize("1.0em");
				titleItem.setTextColor(Color.BLUE);
				titleItem.setText("이미지");
				titleItem.setIconType(IconType.IMAGE);

				contentTree.getSelectedItem().add(titleItem);
				
			}
		});
		
		MaterialIcon icon4 = new MaterialIcon(IconType.REMOVE);
		icon4.setWaves(WavesType.DEFAULT);
		icon4.setLineHeight(16);
		icon4.setVerticalAlign(VerticalAlign.MIDDLE);
		icon4.setFontSize("1.0em");
		icon4.setBorderRight("1px solid #e0e0e0");
		icon4.setHeight("26px");
		icon4.setMargin(0);
		icon4.setWidth("26px");
		icon4.setFloat(com.google.gwt.dom.client.Style.Float.LEFT);
		icon4.addClickHandler(event->{
			
			IconType sIconType = contentTree.getSelectedItem().getIcon().getIconType();
			
			if ( sIconType.equals(IconType.IMAGE) || sIconType.equals(IconType.VIEW_HEADLINE) || sIconType.equals(IconType.SUBJECT) ) {
				contentTree.getSelectedItem().removeFromParent();
			}
			
		});
		
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

		});
/*		
		panelBottom.add(icon1);
		panelBottom.add(icon2);
		panelBottom.add(icon3);
		panelBottom.add(icon4);
		panelBottom.add(icon5);
		panelBottom.add(icon6);
*/		
		col1.add(fixedTop);
		col1.add(panelBottom);
		
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
		detailSliderPanel.setWidth(((559 * 9) + 36 )  + "px");
		detailSliderPanel.setHeight("100%");
		
		detailSliderPanel.add(new ContentsBase(this.getMaterialExtentsWindow()));
		detailSliderPanel.add(new ContentsTags(this.getMaterialExtentsWindow()));
		detailSliderPanel.add(new ContentsInfomation(this.getMaterialExtentsWindow()));
		detailSliderPanel.add(new ContentsPartners(this.getMaterialExtentsWindow()));
		detailSliderPanel.add(new ContentsCourseDetail(this.getMaterialExtentsWindow()));
		detailSliderPanel.add(new ContentsMasterImage(this.getMaterialExtentsWindow()));

		detailPanel.add(detailSliderPanel);

		col2.add(detailPanel);
		
		this.add(row);;
		
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
	
}
