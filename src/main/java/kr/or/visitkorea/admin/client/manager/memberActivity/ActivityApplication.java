package kr.or.visitkorea.admin.client.manager.memberActivity;

import java.util.Map;

import kr.or.visitkorea.admin.client.application.ApplicationView;
import kr.or.visitkorea.admin.client.manager.ApplicationBase;
import kr.or.visitkorea.admin.client.manager.memberActivity.dialogs.ActivityCourseDetailDialog;
import kr.or.visitkorea.admin.client.manager.memberActivity.dialogs.CommentDetailDialog;
import kr.or.visitkorea.admin.client.manager.memberActivity.dialogs.CommentListDialog;
import kr.or.visitkorea.admin.client.manager.memberActivity.dialogs.CommentReplyDialog;
import kr.or.visitkorea.admin.client.manager.memberActivity.dialogs.QnaDetailDialog;
import kr.or.visitkorea.admin.client.manager.memberActivity.dialogs.UserImageDetailDialog;
import kr.or.visitkorea.admin.client.manager.memberActivity.dialogs.UserImageExcelDialog;
import kr.or.visitkorea.admin.client.manager.memberActivity.dialogs.ZikimiDetailDialog;
import kr.or.visitkorea.admin.client.widgets.window.MaterialExtentsWindow;

public class ActivityApplication extends ApplicationBase{
	public static final String QNA_DETAIL = "QNA_DETAIL";
	public static final String ZIKIMI_DETAIL = "ZIKIMI_DETAIL";
	public static final String COURSE_DETAIL = "COURSE_DETAIL"; 
	public static final String USER_IMAGE_DETAIL = "USER_IMAGE_DETAIL"; 
	public static final String USER_IMAGE_EXCEL = "USER_IMAGE_EXCEL"; 
	public static final String COMMENT_LIST = "COMMENT_LIST";
	public static final String COMMENT_REPLY = "COMMENT_REPLY";
	public static final String COMMENT_DETAIL = "COMMENT_DETAIL";
	
	private static String REQUEST_TYPE;
	
	MemberActivityMain host;
	
	public ActivityApplication(ApplicationView applicationView) {
		super(applicationView);
	}

	public void setWindow(MaterialExtentsWindow materialExtentsWindow, String divisionName) {
		this.setDivisionName(divisionName);
		this.windowLiveFlag = true;
		this.window = materialExtentsWindow;
		host = new MemberActivityMain(this.window, this);
		this.window.addDialog(QNA_DETAIL, new QnaDetailDialog(this.window, host));
		this.window.addDialog(ZIKIMI_DETAIL, new ZikimiDetailDialog(this.window, host));
		this.window.addDialog(COURSE_DETAIL, new ActivityCourseDetailDialog(this.window, host));
		this.window.addDialog(USER_IMAGE_DETAIL, new UserImageDetailDialog(this.window, host));
		this.window.addDialog(COMMENT_LIST, new CommentListDialog(this.window, host));
		this.window.addDialog(COMMENT_DETAIL, new CommentDetailDialog(this.window, host));
		this.window.addDialog(COMMENT_REPLY, new CommentReplyDialog(this.window, host));
		this.window.addDialog(USER_IMAGE_EXCEL, new UserImageExcelDialog(this.window, host));
		this.window.addCloseHandler(event->{
			window.closeDialog();
			windowLiveFlag = false;
		});
	}
	
	public void start() {
		start(null);
	}
	
	@Override
	public void start(Map<String, Object> params) {
		
		if (params.get("MODE") != null)
			ActivityApplication.REQUEST_TYPE = (String) params.get("MODE");
		
		this.params = params;
		host = new MemberActivityMain(this.window, this);
		this.window.add(host);
		this.window.open(this.window);
	}
	
	public static String getReqType() {
		return ActivityApplication.REQUEST_TYPE;
	}
}