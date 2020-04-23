package kr.or.visitkorea.admin.client.manager.contents.recommand;

import java.util.HashMap;
import java.util.Map;

import kr.or.visitkorea.admin.client.application.ApplicationView;
import kr.or.visitkorea.admin.client.manager.ApplicationBase;
import kr.or.visitkorea.admin.client.manager.contents.recommand.dialogs.AuthorSelectDialog;
import kr.or.visitkorea.admin.client.manager.contents.recommand.dialogs.ChageStatusDeleteDialog;
import kr.or.visitkorea.admin.client.manager.contents.recommand.dialogs.CouponInfoContentsDialog;
import kr.or.visitkorea.admin.client.manager.contents.recommand.dialogs.EditorDialog;
import kr.or.visitkorea.admin.client.manager.contents.recommand.dialogs.ExcelDownloadDialog;
import kr.or.visitkorea.admin.client.manager.contents.recommand.dialogs.HistoryManagementDialog;
import kr.or.visitkorea.admin.client.manager.contents.recommand.dialogs.HistorySearchDialog;
import kr.or.visitkorea.admin.client.manager.contents.recommand.dialogs.MoreResultNotFoundDialog;
import kr.or.visitkorea.admin.client.manager.contents.recommand.dialogs.RemoveArticleContentDialog;
import kr.or.visitkorea.admin.client.manager.contents.recommand.dialogs.RemoveContentNodeDialog;
import kr.or.visitkorea.admin.client.manager.contents.recommand.dialogs.SelectContentsDialog;
import kr.or.visitkorea.admin.client.manager.contents.recommand.dialogs.TravelInfoContentsDialog;
import kr.or.visitkorea.admin.client.manager.dialogs.CreateUrlLinkDialog;
import kr.or.visitkorea.admin.client.manager.dialogs.SelectContentWithImageDialog;
import kr.or.visitkorea.admin.client.widgets.window.MaterialExtentsWindow;

public class RecommApplication extends ApplicationBase{
	
	public RecommApplication(ApplicationView applicationView) {
		super(applicationView);
	}
	
	private static final Map<String, Object> internalMap = new HashMap<String, Object>();

	public static final String RICH_EDITOR = "RICH_EDITOR";
	public static final String SEARCH_CONTENT = "SEARCH_CONTENT";
	public static final String SELECT_CONTENT = "SELECT_CONTENT";
	public static final String CREATE_URL_LINK = "CREATE_URL_LINK";
	public static final String SELECT_DURATION_GOV = "SELECT_DURATION_GOV";
	public static final String REMOVE_CONTENT_NODE = "REMOVE_CONTENT_NODE";
	public static final String REMOVE_ARTICLE_CONTENT = "REMOVE_ARTICLE_CONTENT";
	public static final String MORE_RESULT_IS_NOT_FOUNT = "MORE_RESULT_IS_NOT_FOUNT";
	public static final String HTML_EDITOR = "HTML_EDITOR";
	public static final String STATUS_DELETE = "STATUS_DELETE";
	public static final String SELECT_RELATED_CONTENT = "SELECT_RELATED_CONTENT";
	public static final String TRAVEL_INFO_CONTENT = "TRAVEL_INFO_CONTENT";
	public static final String COUPON_INFO_CONTENT = "COUPON_INFO_CONTENT";
	public static final String AUTHOR_SELECT = "AUTHOR_SELECT";
	public static final String EXCEL_DOWNLOAD = "EXCEL_DOWNLOAD";
	public static final String HISTORY_MANAGEMENT = "HISTORY_MANAGEMENT";
	public static final String HISTORY_SEARCH = "HISTORY_SEARCH";

	public static void setValue(String key, Object value) {
		internalMap.put(key, value);
	}

	public static Object getValue(String key) {
		return internalMap.get(key);
	}
	
	public void setWindow(MaterialExtentsWindow materialExtentsWindow, String divisionName) {
		
		this.windowLiveFlag = true;
		this.setDivisionName(divisionName);
		this.window = materialExtentsWindow;
		this.window.actionTarget(divisionName);
		this.window.setTitle("추천 컨텐츠 :: 목록");
		this.window.addDialog(CREATE_URL_LINK, new CreateUrlLinkDialog(this.window));
		this.window.addDialog(SELECT_CONTENT, new SelectContentWithImageDialog(this.window));
		this.window.addDialog(REMOVE_CONTENT_NODE, new RemoveContentNodeDialog(this.window));
		this.window.addDialog(REMOVE_ARTICLE_CONTENT, new RemoveArticleContentDialog(this.window));
		this.window.addDialog(MORE_RESULT_IS_NOT_FOUNT, new MoreResultNotFoundDialog(this.window));
		this.window.addDialog(HTML_EDITOR, new EditorDialog(this.window));
		this.window.addDialog(STATUS_DELETE, new ChageStatusDeleteDialog(this.window)); 
		this.window.addDialog(SELECT_RELATED_CONTENT, new SelectContentsDialog(this.window)); 
		this.window.addDialog(TRAVEL_INFO_CONTENT, new TravelInfoContentsDialog(this.window));
		this.window.addDialog(COUPON_INFO_CONTENT, new CouponInfoContentsDialog(this.window));
		this.window.addDialog(AUTHOR_SELECT, new AuthorSelectDialog(this.window));
		this.window.addDialog(EXCEL_DOWNLOAD, new ExcelDownloadDialog(this.window));
		this.window.addDialog(HISTORY_MANAGEMENT, new HistoryManagementDialog(this.window));
		this.window.addDialog(HISTORY_SEARCH, new HistorySearchDialog(this.window));
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
		String user = (String) this.params.get("MODE");
		String otdId = (String) this.params.get("OTD_ID");
		setValue("MODE", user);
		setValue("OTD_ID", otdId);
		
		String modeString = (String)RecommApplication.getValue("MODE");
/*		
		MaterialIcon iconSelectHeader1 = new MaterialIcon(IconType.COLLECTIONS);
		iconSelectHeader1.setCircle(true);
		iconSelectHeader1.setTooltip("추천 컨텐츠 :: 목록");
		iconSelectHeader1.setWaves(WavesType.DEFAULT);
		iconSelectHeader1.addClickHandler(event->{
			window.setTitle("추천 컨텐츠 :: 목록");
			window.goContentSlider(0);
		});
		
		MaterialIcon iconSelectHeader2 = new MaterialIcon(IconType.CODE);
		iconSelectHeader2.setCircle(true);
		iconSelectHeader2.setWaves(WavesType.DEFAULT);
		iconSelectHeader2.addClickHandler(event->{
			window.setTitle("추천 컨텐츠 :: 분야별 상세 정보");
			window.goContentSlider(window.getWidth() * 1 * -1);
		});
		
		this.window.appendTitleWidget(iconSelectHeader2);
		this.window.appendTitleWidget(iconSelectHeader1);
*/
		this.window.add(new RecommContentsList(this.window));
		this.window.add(new RecommContentsTree(this.window));
		this.window.open(this.window);
	}
	
}
