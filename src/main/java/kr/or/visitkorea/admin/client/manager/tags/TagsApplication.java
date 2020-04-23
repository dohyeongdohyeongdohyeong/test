package kr.or.visitkorea.admin.client.manager.tags; 

import java.util.Map;

import gwt.material.design.client.constants.IconType;
import gwt.material.design.client.constants.WavesType;
import gwt.material.design.client.ui.MaterialIcon;
import kr.or.visitkorea.admin.client.application.ApplicationView;
import kr.or.visitkorea.admin.client.manager.ApplicationBase;
import kr.or.visitkorea.admin.client.manager.tags.dialogs.AppendMemberTagDialog;
import kr.or.visitkorea.admin.client.manager.tags.dialogs.CreateGroupTagDialog;
import kr.or.visitkorea.admin.client.manager.tags.dialogs.CreateTagDialog;
import kr.or.visitkorea.admin.client.manager.tags.dialogs.DeleteGroupTagDialog;
import kr.or.visitkorea.admin.client.manager.tags.dialogs.DeleteTagDialog;
import kr.or.visitkorea.admin.client.manager.tags.dialogs.EditGroupTagDialog;
import kr.or.visitkorea.admin.client.manager.tags.dialogs.EditTagDialog;
import kr.or.visitkorea.admin.client.manager.tags.dialogs.SearchGroupTagDialog;
import kr.or.visitkorea.admin.client.manager.tags.dialogs.SearchTagDialog;
import kr.or.visitkorea.admin.client.widgets.window.MaterialExtentsWindow;

public class TagsApplication extends ApplicationBase{
	
	public TagsApplication(ApplicationView applicationView) {
		super(applicationView);
	}

	public static final String SEARCH_GROUP_TAG_NAME = "SEARCH_GROUP_TAG_NAME";
	public static final String CREATE_GROUP_TAG = "CREATE_GROUP_TAG";
	public static final String EDIT_GROUP_TAG = "EDIT_GROUP_TAG";
	public static final String DELETE_GROUP_TAG = "DELETE_GROUP_TAG";
	public static final String APPEND_MEMBER_TAG = "APPEND_MEMBER_TAG";

	public static final String SEARCH_TAG_NAME = "SEARCH_TAG_NAME";
	public static final String CREATE_TAG = "CREATE_TAG";
	public static final String EDIT_TAG = "EDIT_TAG";
	public static final String DELETE_TAG = "DELETE_TAG";
	
	public void setWindow(MaterialExtentsWindow materialExtentsWindow, String divisionName) {
		this.windowLiveFlag = true;
		this.setDivisionName(divisionName);
		this.window = materialExtentsWindow;
		this.window.actionTarget(divisionName);
		this.window.setTitle("태그 :: 태그 생성/삭제");
		
		// append group related dialog 
		this.window.addDialog(SEARCH_GROUP_TAG_NAME, new SearchGroupTagDialog(this.window));
		this.window.addDialog(CREATE_GROUP_TAG, new CreateGroupTagDialog(this.window));
		this.window.addDialog(EDIT_GROUP_TAG, new EditGroupTagDialog(this.window));
		this.window.addDialog(DELETE_GROUP_TAG, new DeleteGroupTagDialog(this.window));
		this.window.addDialog(APPEND_MEMBER_TAG, new AppendMemberTagDialog(this.window));
		
		// append group related dialog 
		this.window.addDialog(SEARCH_TAG_NAME, new SearchTagDialog(this.window));
		this.window.addDialog(CREATE_TAG, new CreateTagDialog(this.window));
		this.window.addDialog(EDIT_TAG, new EditTagDialog(this.window));
		this.window.addDialog(DELETE_TAG, new DeleteTagDialog(this.window));

		this.window.addCloseHandler(event->{
			windowLiveFlag = false;
		});
	}

	public void start() {
		start(null);
		
	}
	@Override
	public void start(Map<String, Object> params) {
		this.params = params;
		MaterialIcon iconSelectHeader1 = new MaterialIcon(IconType.COLLECTIONS);
		iconSelectHeader1.setCircle(true);
		iconSelectHeader1.setWaves(WavesType.DEFAULT);
		iconSelectHeader1.setTooltip("태그 생성/삭제");
		iconSelectHeader1.addClickHandler(event->{
			window.setTitle("태그 :: 태그 생성/삭제");
			window.goContentSlider(0);
		});
		
		MaterialIcon iconSelectHeader2 = new MaterialIcon(IconType.CODE);
		iconSelectHeader2.setCircle(true);
		iconSelectHeader2.setTooltip("태그 그룹");
		iconSelectHeader2.setWaves(WavesType.DEFAULT);
		iconSelectHeader2.addClickHandler(event->{
			window.setTitle("태그 :: 태그 그룹");
			window.goContentSlider(window.getWidth() * 1 * -1);
		});
		
		this.window.appendTitleWidget(iconSelectHeader2);
		this.window.appendTitleWidget(iconSelectHeader1);

		this.window.add(new TagsCreateDelete(this.window));
		this.window.add(new TagsGroups(this.window));
		this.window.open(this.window);
	}
}
