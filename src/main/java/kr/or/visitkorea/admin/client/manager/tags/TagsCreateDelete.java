package kr.or.visitkorea.admin.client.manager.tags;

import com.google.gwt.dom.client.Style.FontWeight;
import com.google.gwt.dom.client.Style.Overflow;

import gwt.material.design.client.MaterialDesignBase;
import gwt.material.design.client.constants.Color;
import gwt.material.design.client.constants.IconType;
import gwt.material.design.client.constants.TextAlign;
import gwt.material.design.client.ui.MaterialIcon;
import gwt.material.design.client.ui.MaterialLabel;
import kr.or.visitkorea.admin.client.manager.widgets.AbstractContentPanel;
import kr.or.visitkorea.admin.client.manager.widgets.ContentBottomPager;
import kr.or.visitkorea.admin.client.manager.widgets.ContentTable;
import kr.or.visitkorea.admin.client.widgets.window.MaterialExtentsWindow;

public class TagsCreateDelete extends AbstractContentPanel {

	static {
		MaterialDesignBase.injectCss(TagsContentBundle.INSTANCE.contentCss());
	}

	public TagsCreateDelete(MaterialExtentsWindow meWindow) {
		super(meWindow);
	}

	public void init() {
		
		this.setTextAlign(TextAlign.CENTER);
		this.setFloat(com.google.gwt.dom.client.Style.Float.LEFT);
		this.setHeight("100%");
		this.setOverflow(Overflow.HIDDEN);
		this.setPaddingTop(15);
		this.setPaddingBottom(30);
		this.setPaddingRight(30);
		this.setPaddingLeft(30);
		
		buildTitle();
		
		ContentTable table = new ContentTable(ContentTable.TABLE_SELECT_TYPE.SELECT_SINGLE);
		table.setHeight("440px");

		table.appendTitle("태그ID", 150, TextAlign.CENTER);
		table.appendTitle("태그명", 248, TextAlign.LEFT);
		table.appendTitle("수정일시", 170, TextAlign.CENTER);
		table.appendTitle("등록일시", 170, TextAlign.CENTER);
		
		for (int i=0; i<6; i++) {
			if (i%2 == 0) {
				table.addRow(Color.WHITE, "T0000"+i,"충남","2018-05-07 15:16:02", "2018-05-07 15:16:02");
			}else {
				table.addRow(Color.GREY_LIGHTEN_4, "T0000"+i,"강원","2018-05-07 15:16:02", "2018-05-07 15:16:02");
			}
		}

		ContentBottomPager pager = new ContentBottomPager();
		
		MaterialIcon icon1 = new MaterialIcon(IconType.SEARCH);
		icon1.addClickHandler(event->{
			getMaterialExtentsWindow().openDialog(TagsApplication.SEARCH_TAG_NAME, 720);
		});

		MaterialIcon icon2 = new MaterialIcon(IconType.CREATE);
		icon2.addClickHandler(event->{
			getMaterialExtentsWindow().openDialog(TagsApplication.CREATE_TAG, 720);
		});
		
		MaterialIcon icon3 = new MaterialIcon(IconType.UPDATE);
		icon3.addClickHandler(event->{
			getMaterialExtentsWindow().openDialog(TagsApplication.EDIT_TAG, 720);
		});
		
		MaterialIcon icon4 = new MaterialIcon(IconType.DELETE);
		icon4.addClickHandler(event->{
			getMaterialExtentsWindow().openDialog(TagsApplication.DELETE_TAG, 720);
		});
		
		pager.addIcon(icon1, "검색", com.google.gwt.dom.client.Style.Float.LEFT);
		pager.addIcon(icon2, "생성", com.google.gwt.dom.client.Style.Float.LEFT);
		pager.addIcon(icon3, "선택 수정", com.google.gwt.dom.client.Style.Float.LEFT);
		pager.addIcon(icon4, "선택 삭제", com.google.gwt.dom.client.Style.Float.LEFT);
		
		this.add(table);
		this.add(pager);
		
	}

	private void buildTitle() {
		MaterialLabel titleLabel = new MaterialLabel("- 태그 생성/삭제");
		titleLabel.setTextAlign(TextAlign.LEFT);
		titleLabel.setFontWeight(FontWeight.BOLD);
		titleLabel.setTextColor(Color.BLUE);
		titleLabel.setFontSize("1.7em");
		titleLabel.setMarginBottom(10);
		
		this.add(titleLabel);
	}

}
