package kr.or.visitkorea.admin.client.manager.addMenu.panel;

import gwt.material.design.client.constants.IconType;
import gwt.material.design.client.constants.TextAlign;
import gwt.material.design.client.ui.MaterialIcon;
import gwt.material.design.client.ui.MaterialRow;
import kr.or.visitkorea.admin.client.application.Registry;
import kr.or.visitkorea.admin.client.manager.addMenu.AddMenuApplication;
import kr.or.visitkorea.admin.client.manager.addMenu.AddMenuMain;

public class ButtonPanel extends MaterialRow {
	private AddMenuMain host;
	private MaterialIcon applyBtn;
	
	public ButtonPanel(AddMenuMain host) {
		this.host = host;
		this.setWidth("100%");
		this.setTextAlign(TextAlign.RIGHT);
		this.setMarginTop(15);
		this.setMarginBottom(0);
		this.setPaddingRight(20);

		applyBtn = new MaterialIcon(IconType.SAVE);
		applyBtn.setMarginRight(5);
		applyBtn.setTooltip("적용");
		applyBtn.setEnabled(Registry.getPermission("775b14e8-8736-11e9-a830-080027924aa7"));
		applyBtn.addClickHandler(e -> {
			if (AddMenuApplication.valueValidation(this.host.getTitleValue().getValue()) 
					|| AddMenuApplication.valueValidation(this.host.getBoundsX().getValue()) 
					|| AddMenuApplication.valueValidation(this.host.getBoundsY().getValue())) {
				
				host.getMaterialExtentsWindow().alert("< 또는 > 문자는 포함될 수 없습니다.");
				
			} else {
				this.host.getMaterialExtentsWindow().confirm("변경된 내용을 적용하시겠습니까?", event -> {
					if (event.getSource().toString().contains("예")) {
						AddMenuApplication.submitXml();
						
					} else {
					}
				});
			}
		});
		
		this.add(applyBtn);
	}

	public MaterialIcon getApplyBtn() {
		return applyBtn;
	}

	public void setApplyBtn(MaterialIcon applyBtn) {
		this.applyBtn = applyBtn;
	}

}
