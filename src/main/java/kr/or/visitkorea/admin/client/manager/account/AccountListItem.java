package kr.or.visitkorea.admin.client.manager.account;

import com.google.gwt.dom.client.Style.Overflow;

import gwt.material.design.client.MaterialDesignBase;
import gwt.material.design.client.constants.Color;
import gwt.material.design.client.constants.IconType;
import gwt.material.design.client.constants.TextAlign;
import gwt.material.design.client.ui.MaterialCollectionItem;
import gwt.material.design.client.ui.MaterialColumn;
import gwt.material.design.client.ui.MaterialIcon;
import gwt.material.design.client.ui.MaterialLabel;
import gwt.material.design.client.ui.MaterialRow;
import kr.or.visitkorea.admin.client.widgets.window.MaterialExtentsWindow;

public class AccountListItem extends MaterialCollectionItem {

	static {
		MaterialDesignBase.injectCss(AccountContentBundle.INSTANCE.accountContentCss());
	}

	private MaterialRow row;
	private MaterialColumn col1;
	private MaterialColumn col2;
	private String accountName;
	private String accountId;
	private String accountConnTime;
	private IconType accountIconType;
	private MaterialExtentsWindow window;

	public AccountListItem(AccountListCollection colletion) {
		super();
		
		this.setPadding(0);
		this.setPaddingTop(14);

		this.addMouseUpHandler(event->{
			AccountListItem item = (AccountListItem)event.getSource();
			item.setBackgroundColor(Color.GREY_LIGHTEN_1);
			colletion.setSelectWidget(item);
			item.setTextColor(Color.BLACK);
		});
		
		this.addMouseOverHandler(event->{
			AccountListItem item = (AccountListItem)event.getSource();
			if(item != colletion.getSelectWidget()) {
				item.setBackgroundColor(Color.GREY_LIGHTEN_2);
				item.setTextColor(Color.BLUE);
			}
		});
		
		this.addMouseOutHandler(event->{
			AccountListItem item = (AccountListItem)event.getSource();
			AccountListItem sItem = colletion.getSelectWidget();
			if (!item.equals(sItem)) {
				item.setBackgroundColor(Color.WHITE);
				item.setTextColor(Color.BLACK);
			}
		});
		
		
		row = new MaterialRow();
		row.setHeight("100%");
		row.setOverflow(Overflow.HIDDEN);
		
		col1 = new MaterialColumn();
		col1.setGrid("s2");
		col1.setHeight("100%");
		col1.setTextAlign(TextAlign.LEFT);
		
		col2 = new MaterialColumn();
		col2.setGrid("s10");
		col2.setHeight("100%");
		col2.setTextAlign(TextAlign.LEFT);
		col2.setPaddingLeft(33);
	
		row.add(col1);
		row.add(col2);
		
		this.add(row);

	}

	public String getAccountId() {
		return accountId;
	}

	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}

	@Override
	protected void onLoad() {
        super.onLoad();
    }

	public String getName() {
		return this.accountName;
	}

	public void setAccountIconType(IconType iconType) {
		// ICON
		this.accountIconType = iconType;
		MaterialIcon leftIcon = new MaterialIcon();
		leftIcon.setIconType(iconType);
		leftIcon.setFontSize("3.0em");
		leftIcon.setWidth("100%");
		leftIcon.setPaddingTop(0);
		col1.add(leftIcon);
	}

	public void setAccountName(String name) {
		// LABEL
		this.accountName = name;
		MaterialLabel label = new MaterialLabel(name);
		label.setFontSize("0.9em");
		col2.add(label);
	}

	public void setAccountLastConnectionTime(String time) {
		// LABEL
		this.accountConnTime = time;
		MaterialLabel connTime = new MaterialLabel(time);
		connTime.setFontSize("0.6em");
		col2.add(connTime);
	}

	public IconType getIconType() {
		return this.accountIconType;
	}

	public void setMaterialExtentsWindow(MaterialExtentsWindow materialExtentsWindow) {
		this.window = materialExtentsWindow;
	}

	public MaterialExtentsWindow getMaterialExtentsWindow() {
		return this.window;
	}

	
}
