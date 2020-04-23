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

public class TagsGroups extends AbstractContentPanel {

	static {
		MaterialDesignBase.injectCss(TagsContentBundle.INSTANCE.contentCss());
	}

	public TagsGroups(MaterialExtentsWindow meWindow) {
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

		table.appendTitle("번호", 100, TextAlign.CENTER);
		table.appendTitle("그룹명", 108, TextAlign.LEFT);
		table.appendTitle("소속 태그", 360, TextAlign.LEFT);
		table.appendTitle("최종 변경 일시", 170, TextAlign.CENTER);
		
		for (int i=0; i<6; i++) {
			if (i%2 == 0) {
				table.addRow(Color.WHITE, ""+i,"여름","#바다 #비키니 #해수욕장 #휴가", "2018-05-07 15:16:02");
			}else {
				table.addRow(Color.GREY_LIGHTEN_4, ""+i,"3월","#벚꽃 #입학", "2018-05-07 15:16:02");
			}
		}

		ContentBottomPager pager = new ContentBottomPager();
		
		MaterialIcon icon1 = new MaterialIcon(IconType.SEARCH);
		icon1.addClickHandler(event->{
			getMaterialExtentsWindow().openDialog(TagsApplication.SEARCH_GROUP_TAG_NAME, 720);
		});

		MaterialIcon icon2 = new MaterialIcon(IconType.CREATE);
		icon2.addClickHandler(event->{
			getMaterialExtentsWindow().openDialog(TagsApplication.CREATE_GROUP_TAG, 720);
		});
		
		MaterialIcon icon3 = new MaterialIcon(IconType.UPDATE);
		icon3.addClickHandler(event->{
			getMaterialExtentsWindow().openDialog(TagsApplication.EDIT_GROUP_TAG, 720);
		});
		
		MaterialIcon icon4 = new MaterialIcon(IconType.CARD_MEMBERSHIP);
		icon4.addClickHandler(event->{
			getMaterialExtentsWindow().openDialog(TagsApplication.APPEND_MEMBER_TAG, 720);
		});
		
		MaterialIcon icon5 = new MaterialIcon(IconType.DELETE);
		icon5.addClickHandler(event->{
			getMaterialExtentsWindow().openDialog(TagsApplication.DELETE_GROUP_TAG, 720);
		});
		
		pager.addIcon(icon1, "검색", com.google.gwt.dom.client.Style.Float.LEFT);
		pager.addIcon(icon2, "그룹 생성", com.google.gwt.dom.client.Style.Float.LEFT);
		pager.addIcon(icon3, "그룹명 수정", com.google.gwt.dom.client.Style.Float.LEFT);
		pager.addIcon(icon4, "소속 태그 선택", com.google.gwt.dom.client.Style.Float.LEFT);
		pager.addIcon(icon5, "선택 삭제", com.google.gwt.dom.client.Style.Float.LEFT);
		
		this.add(table);
		this.add(pager);

	}

	private void buildTitle() {
		MaterialLabel titleLabel = new MaterialLabel("- 태그 그룹");
		titleLabel.setTextAlign(TextAlign.LEFT);
		titleLabel.setFontWeight(FontWeight.BOLD);
		titleLabel.setTextColor(Color.BLUE);
		titleLabel.setFontSize("1.7em");
		titleLabel.setMarginBottom(10);
		this.add(titleLabel);
	}

}
