package kr.or.visitkorea.admin.client.manager.contents.database.dialogs;

import java.util.Map;

import com.google.gwt.dom.client.Style.FontWeight;
import com.google.gwt.dom.client.Style.Position;

import gwt.material.design.client.constants.Color;
import gwt.material.design.client.ui.MaterialButton;
import gwt.material.design.client.ui.MaterialLabel;
import kr.or.visitkorea.admin.client.manager.widgets.InputCourseItem;
import kr.or.visitkorea.admin.client.manager.widgets.InputCourseItemDetail;
import kr.or.visitkorea.admin.client.widgets.dialog.DialogContent;
import kr.or.visitkorea.admin.client.widgets.window.MaterialExtentsWindow;

public class CourseItemDeleteDialog extends DialogContent {

	public CourseItemDeleteDialog(MaterialExtentsWindow window) {
		super(window);
	}

	@Override
	public void init() {
		buildContent();
	}

	@Override
	protected void onLoad() {
		super.onLoad();
	}

	public void buildContent() {
		addDefaultButtons();

		// dialog title define
		MaterialLabel dialogTitle = new MaterialLabel("코스 아이템을 삭제 합니다.");
		dialogTitle.setFontSize("1.4em");
		dialogTitle.setFontWeight(FontWeight.BOLD);
		dialogTitle.setTextColor(Color.RED);
		dialogTitle.setPaddingTop(10);
		dialogTitle.setPaddingLeft(30);

		this.add(dialogTitle);

		// dialog title define
		MaterialLabel mentLabel = new MaterialLabel("삭제된 아이템은 복구가 불가능합니다.  삭제 하시겠습니까?");
		mentLabel.setFontSize("1.1em");
		mentLabel.setLayoutPosition(Position.ABSOLUTE);
		mentLabel.setFontWeight(FontWeight.BOLD);
		mentLabel.setTextColor(Color.BLUE_GREY);
		mentLabel.setLeft(100);
		mentLabel.setTop(80);
		this.add(mentLabel);

		MaterialButton selectButton = new MaterialButton("삭제");
		selectButton.setBackgroundColor(Color.RED_LIGHTEN_2);
		selectButton.addClickHandler(event -> {
			InputCourseItem courseItem = (InputCourseItem)getParameters().get("COURSE_ITEM");
			InputCourseItemDetail courseDetailItem = (InputCourseItemDetail)getParameters().get("DETAIL_ITEM");
			if (courseItem != null && courseDetailItem != null) {
				courseItem.remove(courseDetailItem);
			}
			getMaterialExtentsWindow().closeDialog();
		});

		this.addButton(selectButton);

	}
	
	@Override
	public void setParameters(Map<String, Object> parameters) {
		super.setParameters(parameters);
	}

	@Override
	public int getHeight() {
		return 250;
	}

}
