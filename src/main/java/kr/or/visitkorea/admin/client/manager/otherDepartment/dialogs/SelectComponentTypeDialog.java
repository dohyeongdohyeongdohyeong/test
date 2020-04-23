
package kr.or.visitkorea.admin.client.manager.otherDepartment.dialogs;

import java.util.HashMap;
import java.util.Map;

import com.google.gwt.dom.client.Style.Position;

import gwt.material.design.client.constants.Color;
import gwt.material.design.client.ui.MaterialButton;
import gwt.material.design.client.ui.MaterialLabel;
import gwt.material.design.client.ui.MaterialPanel;
import kr.or.visitkorea.admin.client.manager.otherDepartment.widgets.AreaComponent;
import kr.or.visitkorea.admin.client.manager.otherDepartment.widgets.ServiceCompStack;
import kr.or.visitkorea.admin.client.manager.otherDepartment.widgets.comp.CourseDescription;
import kr.or.visitkorea.admin.client.manager.otherDepartment.widgets.comp.HorizontalTagList;
import kr.or.visitkorea.admin.client.manager.otherDepartment.widgets.comp.PairCircleImage;
import kr.or.visitkorea.admin.client.manager.otherDepartment.widgets.comp.PairSquareImage;
import kr.or.visitkorea.admin.client.manager.otherDepartment.widgets.comp.SingleImageWithDescription;
import kr.or.visitkorea.admin.client.manager.otherDepartment.widgets.comp.SingleLargeImage;
import kr.or.visitkorea.admin.client.manager.otherDepartment.widgets.comp.SingleSmallImage;
import kr.or.visitkorea.admin.client.manager.otherDepartment.widgets.comp.VerticalArticleList;
import kr.or.visitkorea.admin.client.manager.otherDepartment.widgets.comp.SingleImageComponent;
import kr.or.visitkorea.admin.client.manager.otherDepartment.widgets.comp.SliderImageComponent;
import kr.or.visitkorea.admin.client.manager.otherDepartment.widgets.comp.VerticalSmallImageList;
import kr.or.visitkorea.admin.client.widgets.dialog.DialogContent;
import kr.or.visitkorea.admin.client.widgets.window.MaterialExtentsWindow;

public class SelectComponentTypeDialog extends DialogContent {
			
	private AreaComponent selectedComponent;
	private Map<String, String> desc;
	private MaterialLabel mentPanel;
	private Map<String, Object> paramters;
	
	public SelectComponentTypeDialog(MaterialExtentsWindow window) {
		super(window);
	}

	@Override
	public void init() {

		desc = new HashMap<String, String>();
		desc.put("01", ":: 한 개의 큰 이미지를 표시하는 컴포넌트 입니다.");
		desc.put("02", ":: 짝수 개의 사각형 이미지를 제목과 함게 노출하는 컴포넌트 입니다. 주의사항으로 꼭 짝수개의 컨텐츠를 선택해야 합니다.");
		desc.put("03", ":: 이미지와 간략한 설명을 포함하는 컴포넌트 입니다.");
		desc.put("04", ":: 텍스트 제목과 간단한 설명을 포함하는 컴포넌트 입니다.");
		desc.put("05", ":: 이미지와 설명을 포함되어지도록 만들어진 컴포넌트 입니다.");
		desc.put("06", ":: 작은 이미지를 베너형태로 표출 할 수 있도록 구성되어진 컴포넌트입니다..");
		desc.put("07", ":: 원형 테두리를 가지는 이미지를 표현할 수 있는 컴포넌트 입니다. 다만, 꼭 짝수개의 컨텐츠를 선택해야 합니다.");
		desc.put("13", ":: 단일 이미지를 표시하는 컴포넌트입니다.");
		desc.put("14", ":: 슬라이더 이미지를 표시하는 컴포넌트입니다.");
		
/*
		desc.put("08", ":: 한 개의 코스를 간단히 설명하는 컴포넌트 입니다.");
		desc.put("09", ":: 여러개의 태그를 입력할 수 있도록 구성할 수 있는 컴포넌트 입니다.");
*/		
		
		addDefaultButtons();

		MaterialPanel contentPanel = new MaterialPanel();
		contentPanel.setLayoutPosition(Position.RELATIVE);
		contentPanel.setWidth("100%");
		contentPanel.setTop(30);
		contentPanel.setLeft(30);
		this.add(contentPanel);

		contentPanel.add(addEvent(new SingleLargeImage(com.google.gwt.dom.client.Style.Float.LEFT)));
		contentPanel.add(addEvent(new PairSquareImage(com.google.gwt.dom.client.Style.Float.LEFT)));
		contentPanel.add(addEvent(new VerticalSmallImageList(com.google.gwt.dom.client.Style.Float.LEFT)));
		contentPanel.add(addEvent(new VerticalArticleList(com.google.gwt.dom.client.Style.Float.LEFT)));
		contentPanel.add(addEvent(new SingleImageWithDescription(com.google.gwt.dom.client.Style.Float.LEFT)));
		contentPanel.add(addEvent(new SingleSmallImage(com.google.gwt.dom.client.Style.Float.LEFT)));
		contentPanel.add(addEvent(new PairCircleImage(com.google.gwt.dom.client.Style.Float.LEFT)));
		
		contentPanel.add(addEvent(new SingleImageComponent(com.google.gwt.dom.client.Style.Float.LEFT)));
		contentPanel.add(addEvent(new SliderImageComponent(com.google.gwt.dom.client.Style.Float.LEFT)));
		
/*		
		contentPanel.add(addEvent(new CourseDescription(com.google.gwt.dom.client.Style.Float.LEFT)));
		contentPanel.add(addEvent(new HorizontalTagList(com.google.gwt.dom.client.Style.Float.LEFT)));
*/
		mentPanel = new MaterialLabel();
		mentPanel.setLayoutPosition(Position.ABSOLUTE);
		mentPanel.setTop(180);
		mentPanel.setLeft(30);
		this.add(mentPanel);
		
		MaterialButton selectButton = new MaterialButton("선택");
		selectButton.setBackgroundColor(Color.RED_LIGHTEN_2);
		selectButton.addClickHandler(event->{
			
			String titleLabelText = selectedComponent.getTitleLabel().getText();
			
			ServiceCompStack itemPanel = (ServiceCompStack) this.paramters.get("COMP_STACK");
			itemPanel.setHeight((itemPanel.getChildrenList().size() * 104) + "px");

			if (titleLabelText.equals("01")) {
				itemPanel.insertComponent(new SingleLargeImage(com.google.gwt.dom.client.Style.Float.LEFT));
			}else if (titleLabelText.equals("02")) {
				itemPanel.insertComponent(new PairSquareImage(com.google.gwt.dom.client.Style.Float.LEFT));
			}else if (titleLabelText.equals("03")) {
				itemPanel.insertComponent(new VerticalSmallImageList(com.google.gwt.dom.client.Style.Float.LEFT));
			}else if (titleLabelText.equals("04")) {
				itemPanel.insertComponent(new VerticalArticleList(com.google.gwt.dom.client.Style.Float.LEFT));
			}else if (titleLabelText.equals("05")) {
				itemPanel.insertComponent(new SingleImageWithDescription(com.google.gwt.dom.client.Style.Float.LEFT));
			}else if (titleLabelText.equals("06")) {
				itemPanel.insertComponent(new SingleSmallImage(com.google.gwt.dom.client.Style.Float.LEFT));
			}else if (titleLabelText.equals("07")) {
				itemPanel.insertComponent(new PairCircleImage(com.google.gwt.dom.client.Style.Float.LEFT));
			}else if (titleLabelText.equals("08")) {
				itemPanel.insertComponent(new CourseDescription(com.google.gwt.dom.client.Style.Float.LEFT));
			}else if (titleLabelText.equals("09")) {
				itemPanel.insertComponent(new HorizontalTagList(com.google.gwt.dom.client.Style.Float.LEFT));
			}else if (titleLabelText.equals("13")) {
				itemPanel.insertComponent(new SingleImageComponent(com.google.gwt.dom.client.Style.Float.LEFT));
			}else if (titleLabelText.equals("14")) {
				itemPanel.insertComponent(new SliderImageComponent(com.google.gwt.dom.client.Style.Float.LEFT));
			}
			
			getMaterialExtentsWindow().closeDialog();
		});
		
		this.addButton(selectButton);

	}
	
	private AreaComponent addListEvent(AreaComponent aac) {
		
		aac.addMouseOverHandler(event->{
			if (selectedComponent != aac)
			aac.getTitleLabel().setTextColor(Color.BLUE);
		});
		
		aac.addMouseOutHandler(event->{
			if (selectedComponent != aac)
			aac.getTitleLabel().setTextColor(Color.BLACK);
		});
		
		aac.addClickHandler(event->{
			
			if (selectedComponent != null) {
				selectedComponent.getTitleLabel().setTextColor(Color.BLACK);
			}

			selectedComponent = aac;
			aac.getTitleLabel().setTextColor(Color.RED);
			String titleLabelText = aac.getTitleLabel().getText();
			mentPanel.setText(desc.get(titleLabelText));
			
		});

		return aac;

	}
	
	private AreaComponent addEvent(AreaComponent aac) {
		
		aac.addMouseOverHandler(event->{
			if (selectedComponent != aac)
			aac.getTitleLabel().setTextColor(Color.BLUE);
		});
		
		aac.addMouseOutHandler(event->{
			if (selectedComponent != aac)
			aac.getTitleLabel().setTextColor(Color.BLACK);
		});
		
		aac.addClickHandler(event->{
			
			if (selectedComponent != null) {
				selectedComponent.getTitleLabel().setTextColor(Color.BLACK);
			}

			selectedComponent = aac;
			aac.getTitleLabel().setTextColor(Color.RED);
			String titleLabelText = aac.getTitleLabel().getText();
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
		return 280;
	}
	
}
