package kr.or.visitkorea.admin.client.manager.main.course;

import java.util.HashMap;
import java.util.Map;

import com.google.gwt.dom.client.Style.FontWeight;
import com.google.gwt.dom.client.Style.Overflow;
import com.google.gwt.dom.client.Style.Position;

import gwt.material.design.client.MaterialDesignBase;
import gwt.material.design.client.constants.Color;
import gwt.material.design.client.constants.IconType;
import gwt.material.design.client.constants.TextAlign;
import gwt.material.design.client.ui.MaterialIcon;
import gwt.material.design.client.ui.MaterialLabel;
import kr.or.visitkorea.admin.client.manager.main.recommand.RecommMainApplication;
import kr.or.visitkorea.admin.client.manager.widgets.AbstractContentPanel;
import kr.or.visitkorea.admin.client.manager.widgets.ContentMenu;
import kr.or.visitkorea.admin.client.manager.widgets.ContentTable;
import kr.or.visitkorea.admin.client.widgets.window.MaterialExtentsWindow;

public class CourseMainContents extends AbstractContentPanel {

	static {
		MaterialDesignBase.injectCss(CourseMainContentBundle.INSTANCE.contentCss());
	}

	private ContentTable table;
	private ContentMenu pager;
	private MaterialIcon icon1;

	public CourseMainContents(MaterialExtentsWindow meWindow) {
		super(meWindow);
	}

	public void init() {
		this.setStyleName("mobileManageDetailContent");
		this.setLayoutPosition(Position.RELATIVE);
		this.setTextAlign(TextAlign.CENTER);
		this.setFloat(com.google.gwt.dom.client.Style.Float.LEFT);
		this.setHeight("100%");
		this.setOverflow(Overflow.HIDDEN);
		
		buildTitle();
		buildLayout();
	}

	private void buildLayout() {
		/**
		 * 노출순서
CID
지역
소지역
코스수
콘텎츠 명
대표태그
등록일자
		 */
		
		table = new ContentTable(ContentTable.TABLE_SELECT_TYPE.SELECT_SINGLE);
		table.setHeight(450);
		table.setLayoutPosition(Position.ABSOLUTE);
		table.setLeft(30);
		table.setTop(60);
		table.appendTitle("순서", 50, TextAlign.CENTER);
		table.appendTitle("CID", 120, TextAlign.CENTER);
		table.appendTitle("지역", 50, TextAlign.CENTER);
		table.appendTitle("소지역", 50, TextAlign.CENTER);
		table.appendTitle("코스수", 50, TextAlign.CENTER);
		table.appendTitle("컨텐츠 명", 200, TextAlign.LEFT);
		table.appendTitle("대표태그", 100, TextAlign.CENTER);
		table.appendTitle("등록일자", 120, TextAlign.CENTER);

/*		
		for (int i=0; i<13; i++) {
			if (i%2 == 0) {
				table.addRow(Color.WHITE, ""+i, "122445","충남","논산시","3","불국사","#불국사", "2018-05-30");
			}else {
				table.addRow(Color.GREY_LIGHTEN_4, ""+i, "24567","강원","속초시","3","불국사","#불국사", "2018-05-30");
			}
		}
*/		
		this.add(table);
/*
		// 맴버 태그 pager
		pager = new ContentBottomMenu();
		pager.setLayoutPosition(Position.ABSOLUTE);
		pager.setLeft(30);
		pager.setTop(510);
		pager.setWidth("742px");
*/
		
		MaterialIcon icon1 = new MaterialIcon(IconType.SEARCH);
		icon1.setTextAlign(TextAlign.CENTER);
		icon1.addClickHandler(event->{
			getMaterialExtentsWindow().openDialog(CourseMainApplication.SELECT_CONTENT, 720);
		});
		
		MaterialIcon icon2 = new MaterialIcon(IconType.CLOUD_QUEUE);
		icon2.setTextAlign(TextAlign.CENTER);
		icon2.addClickHandler(event->{
			getMaterialExtentsWindow().openDialog(CourseMainApplication.CREATE_URL_LINK, 720);
		});
		
		MaterialIcon icon3 = new MaterialIcon(IconType.VIEW_STREAM);
		icon3.setTextAlign(TextAlign.CENTER);
		icon3.addClickHandler(event->{
		});
		
		MaterialIcon icon4 = new MaterialIcon(IconType.DELETE);
		icon4.setTextAlign(TextAlign.CENTER);
		icon4.addClickHandler(event->{
		});

		table.getButtomMenu().addIcon(icon1, "컨텐츠 선택", com.google.gwt.dom.client.Style.Float.LEFT);
		table.getButtomMenu().addIcon(icon2, "링크 URL 선택", com.google.gwt.dom.client.Style.Float.LEFT);
		table.getButtomMenu().addIcon(icon3, "미리 보기", com.google.gwt.dom.client.Style.Float.LEFT);
		table.getButtomMenu().addIcon(icon4, "삭제", com.google.gwt.dom.client.Style.Float.LEFT);
	
//		this.add(pager);
		
	}

	private void buildTitle() {
		MaterialLabel titleLabel = new MaterialLabel("- 2018년 5월");
		titleLabel.setMarginTop(15);
		titleLabel.setTextAlign(TextAlign.LEFT);
		titleLabel.setPaddingLeft(30);
		titleLabel.setFontWeight(FontWeight.BOLD);
		titleLabel.setTextColor(Color.BLUE);
		titleLabel.setFontSize("1.7em");
		this.add(titleLabel);
		
		MaterialIcon addIcon = new MaterialIcon(IconType.ADD);
		addIcon.setLayoutPosition(Position.ABSOLUTE);
		addIcon.setRight(30);
		addIcon.setTop(30);
		addIcon.setWidth("24");
		addIcon.setBorder("1px solid #e0e0e0");
		addIcon.addClickHandler(event->{
			Map<String, Object> paramters = new HashMap<String, Object>();
			paramters.put("CONTENT_TITLE_LABEL", titleLabel);
			getMaterialExtentsWindow().openDialog(RecommMainApplication.SELECT_YEAR_MONTH, paramters, 720);
		});
		
		this.add(addIcon);

		
	}

}
