package kr.or.visitkorea.admin.client.manager.tags.dialogs;

import com.google.gwt.dom.client.Style.FontWeight;
import com.google.gwt.dom.client.Style.Position;

import gwt.material.design.client.constants.Color;
import gwt.material.design.client.constants.IconType;
import gwt.material.design.client.constants.TextAlign;
import gwt.material.design.client.ui.MaterialButton;
import gwt.material.design.client.ui.MaterialCheckBox;
import gwt.material.design.client.ui.MaterialIcon;
import gwt.material.design.client.ui.MaterialLabel;
import gwt.material.design.client.ui.MaterialTextBox;
import kr.or.visitkorea.admin.client.manager.widgets.ContentMenu;
import kr.or.visitkorea.admin.client.manager.widgets.ContentTable;
import kr.or.visitkorea.admin.client.widgets.dialog.DialogContent;
import kr.or.visitkorea.admin.client.widgets.window.MaterialExtentsWindow;

public class AppendMemberTagDialog extends DialogContent {


	private MaterialCheckBox checkBox;
	private MaterialLabel commentLabel;
	private MaterialTextBox inputBox;
	private MaterialLabel selectTargetTag;
	private ContentTable searchTable;
	private ContentTable table;

	public AppendMemberTagDialog(MaterialExtentsWindow window) {
		super(window);
	}

	@Override
	public void init() {
		
	       addDefaultButtons();
			
	        // dialog title define
			inputBox = new MaterialTextBox();
			inputBox.setLabel("멤버 태그 검색");
			
			inputBox.addKeyUpHandler(event->{
				if (event.getNativeKeyCode() == 13) {
					searchTag();
				}
			}); 
			
			inputBox.setIconType(IconType.SEARCH);
			inputBox.setLayoutPosition(Position.ABSOLUTE);
			inputBox.setRight(60);
			inputBox.setLeft(350);
			inputBox.setTop(30);
			
			this.add(inputBox);

			table = new ContentTable(ContentTable.TABLE_SELECT_TYPE.SELECT_SINGLE);
			table.setHeight(280);
			table.setLayoutPosition(Position.ABSOLUTE);
			table.setLeft(50);
			table.setTop(110);
			table.appendTitle("멤버 태그", 270, TextAlign.LEFT);

			this.add(table);

			// 맴버 태그 pager
			ContentMenu pager = new ContentMenu();
			pager.setLayoutPosition(Position.ABSOLUTE);
			pager.setLeft(50);
			pager.setTop(390);
			pager.setWidth("272px");
		
			MaterialIcon icon1 = new MaterialIcon(IconType.DELETE);
			icon1.setTextAlign(TextAlign.CENTER);
			icon1.addClickHandler(event->{
			});

			pager.addIcon(icon1, "삭제", com.google.gwt.dom.client.Style.Float.LEFT);
		
			this.add(pager);

			
			searchTable = new ContentTable(ContentTable.TABLE_SELECT_TYPE.SELECT_SINGLE);
			searchTable.setHeight(280);
			searchTable.setLayoutPosition(Position.ABSOLUTE);
			searchTable.setLeft(350);
			searchTable.setTop(110);
			searchTable.appendTitle("검색 태그", 330, TextAlign.LEFT);
		
			this.add(searchTable);

			// 맴버 태그 pager
			ContentMenu searchPager = new ContentMenu();
			searchPager.setLayoutPosition(Position.ABSOLUTE);
			searchPager.setLeft(350);
			searchPager.setTop(390);
			searchPager.setWidth("332px");
		
			MaterialIcon icon2 = new MaterialIcon(IconType.ADD);
			icon2.setTextAlign(TextAlign.CENTER);
			icon2.addClickHandler(event->{
			});

			searchPager.addIcon(icon2, "등록", com.google.gwt.dom.client.Style.Float.LEFT);
		
			this.add(searchPager);
			
			selectTargetTag = new MaterialLabel("#바다");
			selectTargetTag.setLayoutPosition(Position.ABSOLUTE);
			selectTargetTag.setLeft(60);
			selectTargetTag.setTop(50);
			selectTargetTag.setFontSize("1.5em");
			selectTargetTag.setFontWeight(FontWeight.BOLD);
			selectTargetTag.setTextColor(Color.BLUE);
			
			this.add(selectTargetTag);	
			
			
			MaterialButton selectButton = new MaterialButton("저장");
			selectButton.setBackgroundColor(Color.RED_LIGHTEN_2);
			selectButton.addClickHandler(event->{
				getMaterialExtentsWindow().closeDialog();
			});
			
			this.addButton(selectButton);

	}

	private void searchTag() {
		
	       searchTable.clearRows();
	       for (int i=0; i<10; i++) {
				if (i%2 == 0) {
					searchTable.addRow(Color.WHITE,  "#바다");
				}else {
					searchTable.addRow(Color.GREY_LIGHTEN_4, "#3월");
				}
	       }
		
	}

	@Override
	protected void onLoad() {
	       super.onLoad();
	       
	       table.clearRows();
	       for (int ii=0; ii<5; ii++) {
				if (ii%2 == 0) {
					table.addRow(Color.WHITE,  "#바다");
				}else {
					table.addRow(Color.GREY_LIGHTEN_4, "#3월");
				}
	       }

   }

	@Override
	public int getHeight() {
		return 500;
	}
	
}
