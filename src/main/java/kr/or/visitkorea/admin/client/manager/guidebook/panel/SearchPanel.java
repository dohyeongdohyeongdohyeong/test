package kr.or.visitkorea.admin.client.manager.guidebook.panel;

import java.util.List;

import com.google.gwt.dom.client.Style;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

import gwt.material.design.addins.client.combobox.MaterialComboBox;
import gwt.material.design.addins.client.inputmask.MaterialInputMask;
import gwt.material.design.client.constants.ButtonSize;
import gwt.material.design.client.constants.ButtonType;
import gwt.material.design.client.constants.Color;
import gwt.material.design.client.constants.IconSize;
import gwt.material.design.client.constants.IconType;
import gwt.material.design.client.constants.TextAlign;
import gwt.material.design.client.ui.MaterialButton;
import gwt.material.design.client.ui.MaterialColumn;
import gwt.material.design.client.ui.MaterialLabel;
import gwt.material.design.client.ui.MaterialRow;
import gwt.material.design.client.ui.MaterialToast;
import kr.or.visitkorea.admin.client.manager.guidebook.GuideBookApplication;
import kr.or.visitkorea.admin.client.manager.guidebook.GuideBookMain;
import kr.or.visitkorea.admin.client.manager.guidebook.model.GuideBook;

public class SearchPanel extends BasePanel {
	private final static int MAX_ROWS_PER_PAGE = 8;

	private MaterialComboBox<String> themeComboBox;
	private MaterialComboBox<String> areaComboBox;
	private MaterialComboBox<String> sigunguComboBox;
	private MaterialInputMask<?> searchInput;
	private MaterialButton moreButton;
	private MaterialButton searchButton;
	private VerticalPanel resultContainer;

	private int currentPage = 1;
	
	public SearchPanel(GuideBookMain host) {
		super(host);

		this.add(this.createSearchForm());
		this.add(this.createResultContainer());

		GuideBookApplication.fetchThemeCodeValues(themeComboBox, result -> {
			if (result) {
				themeComboBox.setSelectedIndex(0);
				searchButton.setEnabled(false);
				refresh();
			}
		});
		
		GuideBookApplication.fetchAreaCodeValues(areaComboBox, (result) -> {
			if (result) {
				areaComboBox.setSelectedIndex(0);
				searchButton.setEnabled(false);
				refresh();
			}
		});
	}

	private Widget createSearchForm() {
		themeComboBox = new MaterialComboBox<>();
		themeComboBox.setMargin(0);
		themeComboBox.setHideSearch(true);
		themeComboBox.setLabel("테마선택");
		themeComboBox.addItem("전체", "");
		themeComboBox.setVisible(false);
		
		areaComboBox = new MaterialComboBox<>();
		areaComboBox.setMargin(0);
		areaComboBox.setHideSearch(true);
		areaComboBox.setLabel("지역선택");
		areaComboBox.addItem("전체", "");
		areaComboBox.addSelectionHandler((event) -> {
			String selectedAreaCode = areaComboBox.getSingleValue();
			if (selectedAreaCode.equals("") || selectedAreaCode.equals("0")) {
				sigunguComboBox.clear();
				sigunguComboBox.addItem("전체", "");
				sigunguComboBox.setSelectedIndex(0);
				sigunguComboBox.setEnabled(false);
				return;
			}
			GuideBookApplication.fetchSigunguCodeValues(sigunguComboBox, selectedAreaCode, (result) -> {
				if (result) {
					sigunguComboBox.setEnabled(true);
					sigunguComboBox.setSelectedIndex(0);
				}
			});
		});

		sigunguComboBox = new MaterialComboBox<>();
		sigunguComboBox.setMargin(0);
		sigunguComboBox.setHideSearch(true);
		sigunguComboBox.setEnabled(false);
		sigunguComboBox.setLabel("시군구선택");
		sigunguComboBox.addItem("전체", "");

		searchInput = new MaterialInputMask<Object>();
		searchInput.setMargin(0);
		searchInput.getElement().getFirstChildElement().getStyle().setMargin(0, Unit.PX);
		searchInput.setLabel("제목검색");
		searchInput.addKeyUpHandler(event -> {
			int keyCode = event.getNativeKeyCode();
			if (keyCode == KeyCodes.KEY_ENTER) {
				event.preventDefault();
				refresh();
			}
		});

		searchButton = new MaterialButton("검색", IconType.SEARCH);
		searchButton.setFontSize(1.1, Style.Unit.EM);
		searchButton.setHeight("40px");
		searchButton.setMarginRight(10);
		searchButton.addClickHandler((event) -> {
			event.preventDefault();
			refresh();
		});

		MaterialButton addButton = new MaterialButton("등록", IconType.ADD);
		addButton.setFontSize(1.1, Style.Unit.EM);
		addButton.setHeight("40px");
		addButton.setMarginRight(10);
		addButton.addClickHandler((event) -> {
			event.preventDefault();
			host.switchToAddPanel();
		});

		MaterialRow row = new MaterialRow();
		row.setMarginBottom(10);
		row.add(wrapWithColumn(themeComboBox, "s2"));
		row.add(wrapWithColumn(areaComboBox, "s2"));
		row.add(wrapWithColumn(sigunguComboBox, "s2"));
		row.add(wrapWithColumn(searchInput, "s3"));

		MaterialColumn searchCol = wrapWithColumn(searchButton, "s2");
		searchCol.setTextAlign(TextAlign.LEFT);
		row.add(searchButton);

		MaterialColumn addCol = wrapWithColumn(addButton, "s2");
		addCol.setTextAlign(TextAlign.RIGHT);
		row.add(addButton);
		return row;
	}

	private Widget createMoreButton() {
		moreButton = new MaterialButton();
		moreButton.setEnabled(true);
		moreButton.setIconType(IconType.EXPAND_MORE);
		moreButton.setIconSize(IconSize.LARGE);
		moreButton.setBackgroundColor(Color.AMBER);
		moreButton.setSize(ButtonSize.LARGE);
		moreButton.setType(ButtonType.FLOATING);
		moreButton.addClickHandler(event -> searchMore());

		MaterialRow row = new MaterialRow();
		MaterialColumn col = new MaterialColumn();
		col.setOffset("s11");
		col.setGrid("s1");
		col.add(moreButton);
		row.add(col);
		return row;
	}

	public void refresh() {
		currentPage = 1;
		doSearch(currentPage);
	}

	public void searchMore() {
		currentPage ++;
		doSearch(currentPage);
	}

	@SuppressWarnings("unchecked")
	private void doSearch(final int page) {
		final String themeCode = themeComboBox.getSingleValue();
		final String areaCode = areaComboBox.getSingleValue();
		final String sigunguCode = sigunguComboBox.getSingleValue();
		final String keyword = searchInput.getText();
		GuideBookApplication.fetchGuideBookList(page, themeCode, areaCode, sigunguCode, keyword, (success, result) -> {
			if (success) {
				updateResultContainer(page, (List<GuideBook>) result);
			} else {
				clearResultContainer();
			}
			searchButton.setEnabled(true);
		});
	}

	private void clearResultContainer() {
		Widget parent = resultContainer.getParent();
		resultContainer.removeFromParent();
		resultContainer = new VerticalPanel();
		((VerticalPanel) parent).insert(resultContainer, 0);
	}

	private Widget createResultContainer() {
		VerticalPanel wrap = new VerticalPanel();
		resultContainer = new VerticalPanel();
		wrap.add(resultContainer);
		wrap.add(createMoreButton());
		return wrapScrollPanel(wrap, "560px");
	}

	private void updateResultContainer(int page, List<GuideBook> list) {
		moreButton.setEnabled(true);

		if (page == 1) {
			clearResultContainer();
		}

		if (list == null || list.size() < MAX_ROWS_PER_PAGE) {
			// moreButton.setEnabled(false);
		}

		if (list != null && !list.isEmpty()) {
			addResult(list);
		} else {
			if (page == 1) {
				setEmptyResult();
			}
		}
	}

	private void setEmptyResult() {
		MaterialLabel label = new MaterialLabel("등록된 가이드북이 없습니다.");
		label.setLineHeight(480);
		label.setTextAlign(TextAlign.CENTER);
		label.setFontSize(2.0, Style.Unit.EM);
		resultContainer.add(label);
	}

	private void addResult(List<GuideBook> list) {

		MaterialRow row1 = null;
		MaterialRow row2 = null;

		int i = 0;
		for (GuideBook item : list) {
			GuideBookItemWidget card = new GuideBookItemWidget(item);
			card.setActionEditListener(event -> {
				host.setSelectedGuideBook(item);
				host.switchToEditPanel();
			});
			card.setActionDeleteListener(event -> {
				host.getMaterialExtentsWindow().confirm("정말 삭제하시겠습니까?", e -> {
					MaterialButton source = (MaterialButton) e.getSource();
					if (source.getText().equals("예")) {
						GuideBookApplication.deleteGuideBook(item.gbId, (success, result) -> {
							if (success) {
								MaterialToast.fireToast("삭제되었습니다.", 3000);
								refresh();
							}
						});
					}
				});
			});
			MaterialColumn col = new MaterialColumn();
			col.setGrid("s3");
			col.add(card);

			if (i < (MAX_ROWS_PER_PAGE / 2)) {
				if (row1 == null) {
					row1 = new MaterialRow();
					row1.setTextAlign(TextAlign.CENTER);
				}
				row1.add(col);
			} else {
				if (row2 == null) {
					row2 = new MaterialRow();
					row2.setTextAlign(TextAlign.CENTER);
				}
				row2.add(col);
			}

			i ++;
		}

		if (row1 != null) {
			resultContainer.add(row1);
		}

		if (row2 != null) {
			resultContainer.add(row2);
		}
	}
}