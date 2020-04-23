package kr.or.visitkorea.admin.client.manager.account;

import gwt.material.design.client.constants.Color;
import gwt.material.design.client.ui.MaterialCollection;
import gwt.material.design.client.ui.MaterialPanel;
import kr.or.visitkorea.admin.client.application.Registry;
import kr.or.visitkorea.admin.client.manager.account.composite.AccountPermissionComposite;

public class AccountListCollection extends MaterialCollection {

	private AccountListItem selectedItem;
	private MaterialPanel detailPanel;
	private AccountPermissionComposite permComposite;
	
	public AccountListCollection() {
		super();
		Registry.put("AccountListCollection", this);
		permComposite = new AccountPermissionComposite();
	}

	@Override
	protected void onLoad() {
        super.onLoad();
    }

	public void setSelectWidget(AccountListItem item) {
		if (this.selectedItem == null) {
			this.selectedItem = item;
			permComposite.setAccountListItem(item);
		}else if (this.selectedItem == item) {
			
			/*
			this.selectedItem.setBackgroundColor(Color.WHITE);
			this.selectedItem = null;
			this.detailPanel.clear();
			this.detailPanel.add(new AccountIntroComposite());
			 */			
		}else{
			this.selectedItem.setBackgroundColor(Color.WHITE);
			this.selectedItem = item;
			permComposite.setAccountListItem(item);
		}
	}

	public AccountListItem getSelectWidget() {
		return this.selectedItem;
	}

	public void addDetailPanel(MaterialPanel contentDetailPanel) {
		this.detailPanel = contentDetailPanel;
		this.detailPanel.clear();
		this.detailPanel.add(permComposite);
	}

}
