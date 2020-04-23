
package kr.or.visitkorea.admin.client.manager.otherDepartment.dialogs;

import java.util.HashMap;
import java.util.Map;

import com.google.gwt.dom.client.Style.Cursor;
import com.google.gwt.dom.client.Style.Position;

import gwt.material.design.client.constants.Color;
import gwt.material.design.client.ui.MaterialButton;
import gwt.material.design.client.ui.MaterialLabel;
import gwt.material.design.client.ui.MaterialPanel;
import gwt.material.design.client.ui.MaterialToast;
import kr.or.visitkorea.admin.client.manager.otherDepartment.widgets.AreaComponent;
import kr.or.visitkorea.admin.client.manager.otherDepartment.widgets.ServiceCompStack;
import kr.or.visitkorea.admin.client.manager.otherDepartment.widgets.comp.MainTitle;
import kr.or.visitkorea.admin.client.manager.otherDepartment.widgets.comp.Showcase;
import kr.or.visitkorea.admin.client.manager.otherDepartment.widgets.comp.SubTitle;
import kr.or.visitkorea.admin.client.widgets.dialog.DialogContent;
import kr.or.visitkorea.admin.client.widgets.window.MaterialExtentsWindow;

public class SelectComponentTypeWithShowcaseAndTitleDialog extends DialogContent {
			
	private AreaComponent selectedComponent;
	private Map<String, String> desc;
	private MaterialLabel mentPanel;
	private Map<String, Object> paramters;
	
	public SelectComponentTypeWithShowcaseAndTitleDialog(MaterialExtentsWindow window) {
		super(window); 
	}

	@Override
	public void init() {

		desc = new HashMap<String, String>();
		desc.put("10", ":: 컨텐츠의 Showcase 정보를 추가할 수 있는 컴포넌트 입니다.");
		desc.put("11", ":: 부서 서비스의 메인 타이틀을 설정할 수 있는 컴포넌트 입니다.");
		desc.put("12", ":: 부서 서비스의 서브 타이틀을 설정할 수 있는 컴포넌트 입니다.");
		
		addDefaultButtons();

		MaterialPanel contentPanel = new MaterialPanel();
		contentPanel.setLayoutPosition(Position.RELATIVE);
		contentPanel.setTop(30);
		contentPanel.setLeft(150);
		this.add(contentPanel);

		contentPanel.add(addEvent(new Showcase(com.google.gwt.dom.client.Style.Float.LEFT)));
		contentPanel.add(addEvent(new MainTitle(com.google.gwt.dom.client.Style.Float.LEFT)));
		contentPanel.add(addEvent(new SubTitle(com.google.gwt.dom.client.Style.Float.LEFT)));

		mentPanel = new MaterialLabel();
		mentPanel.setLayoutPosition(Position.ABSOLUTE);
		mentPanel.setTop(140);
		mentPanel.setLeft(30);
		mentPanel.setBorder("1px solid #aaaaaa");
		this.add(mentPanel);
		
		MaterialButton selectButton = new MaterialButton("선택");
		selectButton.setBackgroundColor(Color.RED_LIGHTEN_2);
		selectButton.addClickHandler(event->{
			
			String titleLabelText = selectedComponent.getTitleLabel().getText();
			
			ServiceCompStack itemPanel = (ServiceCompStack) this.paramters.get("COMP_STACK");
			//itemPanel.setHeight((itemPanel.getChildrenList().size() * 104) + "px");

			if (titleLabelText.equals("10")) {
				if (itemPanel.isExist(titleLabelText)) {
					mentPanel.setTextColor(Color.RED);
					mentPanel.setText(" : 해당 컴포넌트는 하나 이상 추가할 수 없습니다.");
				}else {
					itemPanel.insertComponent(new Showcase(com.google.gwt.dom.client.Style.Float.LEFT));
					getMaterialExtentsWindow().closeDialog();
				}
			}else if (titleLabelText.equals("11")) {
				if (itemPanel.isExist(titleLabelText)) {
					mentPanel.setTextColor(Color.RED);
					mentPanel.setText("해당 컴포넌트는 하나 이상 추가할 수 없습니다.");
				}else {
					itemPanel.insertComponent(new MainTitle(com.google.gwt.dom.client.Style.Float.LEFT));
					getMaterialExtentsWindow().closeDialog();
				}
			}else if (titleLabelText.equals("12")) {
				if (itemPanel.isExist(titleLabelText)) {
					mentPanel.setTextColor(Color.RED);
					mentPanel.setText("해당 컴포넌트는 하나 이상 추가할 수 없습니다.");
				}else {
					itemPanel.insertComponent(new SubTitle(com.google.gwt.dom.client.Style.Float.LEFT));
					getMaterialExtentsWindow().closeDialog();
				}
			}

			
		});
		
		this.addButton(selectButton);

	}

	private AreaComponent addEvent(AreaComponent aac) {
		
		aac.addMouseOverHandler(event->{
			if (selectedComponent != aac) aac.getTitleLabel().setTextColor(Color.BLUE);
			aac.getElement().getStyle().setCursor(Cursor.POINTER);
		});
		
		aac.addMouseOutHandler(event->{
			if (selectedComponent != aac) aac.getTitleLabel().setTextColor(Color.BLACK);
			aac.getElement().getStyle().setCursor(Cursor.DEFAULT);

		});
		
		aac.addClickHandler(event->{
			
			if (selectedComponent != null) {
				selectedComponent.getTitleLabel().setTextColor(Color.BLACK);
			}

			selectedComponent = aac;
			aac.getTitleLabel().setTextColor(Color.RED);
			String titleLabelText = aac.getTitleLabel().getText();
			mentPanel.setTextColor(Color.BLACK);
			mentPanel.setText(desc.get(titleLabelText));
			
		});

		return aac;
	}
	
	@Override
	public void setParameters(Map<String, Object> parameters) {
		this.paramters = parameters;
	}

	@Override
	public int getHeight() {
		return 250;
	}
	
}
