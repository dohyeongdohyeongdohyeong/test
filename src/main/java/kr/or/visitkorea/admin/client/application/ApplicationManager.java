package kr.or.visitkorea.admin.client.application;

import kr.or.visitkorea.admin.client.manager.ApplicationBase;
import kr.or.visitkorea.admin.client.manager.account.AccountApplication;
import kr.or.visitkorea.admin.client.manager.addMenu.AddMenuApplication;
import kr.or.visitkorea.admin.client.manager.analysis.area.AreaAnalysisApplication;
import kr.or.visitkorea.admin.client.manager.analysis.banner.BannerAnalysisApplication;
import kr.or.visitkorea.admin.client.manager.analysis.connect.ConnectAnalysisApplication;
import kr.or.visitkorea.admin.client.manager.analysis.contents.ContentsAnalysisApplication;
import kr.or.visitkorea.admin.client.manager.analysis.department.OtherDepAnalysisApplication;
import kr.or.visitkorea.admin.client.manager.analysis.main.MainAnalysisApplication;
import kr.or.visitkorea.admin.client.manager.analysis.tag.TagAnalysisApplication;
import kr.or.visitkorea.admin.client.manager.appVersion.AppVersionApplication;
import kr.or.visitkorea.admin.client.manager.contents.author.AuthorApplication;
import kr.or.visitkorea.admin.client.manager.contents.banner.AdbannerApplication;
import kr.or.visitkorea.admin.client.manager.contents.codecategory.CodeCategoryApplication;
import kr.or.visitkorea.admin.client.manager.contents.course.CourseApplication;
import kr.or.visitkorea.admin.client.manager.contents.database.DatabaseApplication;
import kr.or.visitkorea.admin.client.manager.contents.department.DepartmentApplication;
import kr.or.visitkorea.admin.client.manager.contents.foodApi.FoodApiManagementApplication;
import kr.or.visitkorea.admin.client.manager.contents.killercontent.KillerContentApplication;
import kr.or.visitkorea.admin.client.manager.contents.recommand.RecommApplication;
import kr.or.visitkorea.admin.client.manager.event.EventApplication;
import kr.or.visitkorea.admin.client.manager.event.blacklist.BlacklistApplication;
import kr.or.visitkorea.admin.client.manager.event.dashboard.EventDashboardAccessApplication;
import kr.or.visitkorea.admin.client.manager.event.dashboard.EventDashboardJoinApplication;
import kr.or.visitkorea.admin.client.manager.event.dashboard.EventDashboardUserApplication;
import kr.or.visitkorea.admin.client.manager.fcm.FcmApplication;
import kr.or.visitkorea.admin.client.manager.guidebook.GuideBookApplication;
import kr.or.visitkorea.admin.client.manager.imageManager.ImageManagerApplication;
import kr.or.visitkorea.admin.client.manager.imageManager.ImagePreviewApplication;
import kr.or.visitkorea.admin.client.manager.login.LoginApplication;
import kr.or.visitkorea.admin.client.manager.main.MainManagerApplication;
import kr.or.visitkorea.admin.client.manager.main.calendar.CalendarMainApplication;
import kr.or.visitkorea.admin.client.manager.main.curation.CurationMainApplication;
import kr.or.visitkorea.admin.client.manager.main.department.DepartmentMainApplication;
import kr.or.visitkorea.admin.client.manager.main.localGovernment.LocalGovernmentMainApplication;
import kr.or.visitkorea.admin.client.manager.main.marketing.MarketingMainApplication;
import kr.or.visitkorea.admin.client.manager.main.showcase.ShowcaseMainApplication;
import kr.or.visitkorea.admin.client.manager.member.MemberApplication;
import kr.or.visitkorea.admin.client.manager.memberActivity.ActivityApplication;
import kr.or.visitkorea.admin.client.manager.monitoring.MonitoringApplication;
import kr.or.visitkorea.admin.client.manager.news.NewsApplication;
import kr.or.visitkorea.admin.client.manager.otherDepartment.OtherDepartmentMainApplication;
import kr.or.visitkorea.admin.client.manager.otherDepartment.OtherDepartmentServiceManagementApplication;
import kr.or.visitkorea.admin.client.manager.partners.affiliateProposal.PartnersAffiliateProposalApplication;
import kr.or.visitkorea.admin.client.manager.partners.channel.PartnersChannelApplication;
import kr.or.visitkorea.admin.client.manager.partners.content.PartnersContentApplication;
import kr.or.visitkorea.admin.client.manager.preview.PreviewApplication;
import kr.or.visitkorea.admin.client.manager.repair.RepairApplication;
import kr.or.visitkorea.admin.client.manager.stamp.StampApplication;
import kr.or.visitkorea.admin.client.manager.tags.TagsApplication;
import kr.or.visitkorea.admin.client.manager.upload.excel.ExcelImageUploadApplication;

public class ApplicationManager {
	
	public static ApplicationBase getApplicationBase(String appKey, ApplicationView applicationView) {
		ApplicationBase retBase = null;
		
		if (appKey.equals(ApplicationView.WINDOW_KEY_MOBILE_MAIN)) {
			retBase = new MainManagerApplication(applicationView);
		}else if (appKey.equals(ApplicationView.WINDOW_KEY_PC_MAIN)) {
			retBase = new MainManagerApplication(applicationView);
		}else if (appKey.equals(ApplicationView.WINDOW_KEY_ACCOUNT)) {
			retBase = new AccountApplication(applicationView);
		}else if (appKey.equals(ApplicationView.WINDOW_KEY_TAGS)) {
			retBase = new TagsApplication(applicationView);
		}else if (appKey.equals(ApplicationView.WINDOW_KEY_RECOMM_MAIN)) {
			retBase = new OtherDepartmentMainApplication(applicationView);
		}else if (appKey.equals(ApplicationView.WINDOW_KEY_COURSE_MAIN)) {
			retBase = new OtherDepartmentMainApplication(applicationView);
		}else if (appKey.equals(ApplicationView.WINDOW_KEY_SIGHTS_MAIN)) {
			retBase = new OtherDepartmentMainApplication(applicationView);
		}else if (appKey.equals(ApplicationView.WINDOW_KEY_FESTIVAL_MAIN)) {
			retBase = new OtherDepartmentMainApplication(applicationView);
		}else if (appKey.equals(ApplicationView.WINDOW_KEY_EVENT_MAIN)) {
			retBase = new OtherDepartmentMainApplication(applicationView);
		}else if (appKey.equals(ApplicationView.WINDOW_KEY_RECOMM_CONTENT)){
			retBase = new RecommApplication(applicationView);
		}else if (appKey.equals(ApplicationView.WINDOW_KEY_COURSE_CONTENT)){
			retBase = new CourseApplication(applicationView);
		}else if (appKey.equals(ApplicationView.WINDOW_KEY_DATABASE_CONTENT)){
			retBase = new DatabaseApplication(applicationView);
		}else if (appKey.equals(ApplicationView.WINDOW_KILLER_CONTENT_MANAGER)){
			retBase = new KillerContentApplication(applicationView);
		}else if (appKey.equals(ApplicationView.WINDOW_KEY_FOOD_API_MANAGER)){
			retBase = new FoodApiManagementApplication(applicationView);
		}else if (appKey.equals(ApplicationView.WINDOW_KEY_LOGIN)){
			retBase = new LoginApplication(applicationView);
		}else if (appKey.equals(ApplicationView.WINDOW_KEY_OTHER_DEPARTMENT_MAIN_CONTENT)){
			retBase = new OtherDepartmentMainApplication(applicationView);
		}else if (appKey.equals(ApplicationView.WINDOW_KEY_MAIN_CONTENT)){
			retBase = new OtherDepartmentMainApplication(applicationView);
		}else if (appKey.equals(ApplicationView.WINDOW_KEY_MAIN_CONTENT_MOBILE)){
			retBase = new OtherDepartmentMainApplication(applicationView);
		}else if (appKey.equals(ApplicationView.WINDOW_KEY_MAIN_CONTENT_RECOMM)){
			retBase = new OtherDepartmentMainApplication(applicationView);
		}else if (appKey.equals(ApplicationView.WINDOW_KEY_CONTENT_PREVIEW)){
			retBase = new PreviewApplication(applicationView);
		}else if (appKey.equals(ApplicationView.WINDOW_MAIN_SHOWCASE)){
			retBase = new ShowcaseMainApplication(applicationView);
		}else if (appKey.equals(ApplicationView.WINDOW_MAIN_CURATION)){
			retBase = new CurationMainApplication(applicationView);
		}else if (appKey.equals(ApplicationView.WINDOW_MAIN_MARKETING)){
			retBase = new MarketingMainApplication(applicationView);
		}else if (appKey.equals(ApplicationView.WINDOW_MAIN_CALENDAR)){
			retBase = new CalendarMainApplication(applicationView);
		}else if (appKey.equals(ApplicationView.WINDOW_MAIN_DEPARTMENT)){
			retBase = new DepartmentMainApplication(applicationView);
		}else if (appKey.equals(ApplicationView.WINDOW_MAIN_LOCAL_GOVERNMENT)){
			retBase = new LocalGovernmentMainApplication(applicationView);
		}else if (appKey.equals(ApplicationView.WINDOW_KEY_NEWS)){
			retBase = new NewsApplication(applicationView);
		}else if (appKey.equals(ApplicationView.WINDOW_MEMBER)){
			retBase = new MemberApplication(applicationView);
		}else if (appKey.equals(ApplicationView.WINDOW_MEMBER_ACTIVITY)){
			retBase = new ActivityApplication(applicationView);
		} else if(appKey.equals(ApplicationView.WINDOW_ADBANNER_MANAGER)) {
			retBase = new AdbannerApplication(applicationView);
		} else if(appKey.equals(ApplicationView.WINDOW_KEY_TOUR_GUIDE_BOOK)) {
			retBase = new GuideBookApplication(applicationView);
		} else if(appKey.equals(ApplicationView.WINDOW_KEY_IMAGE_UPLOAD)) {
			retBase = new ExcelImageUploadApplication(applicationView);
		} else if(appKey.equals(ApplicationView.WINDOW_KEY_IMAGE_MANAGER)) {
			retBase = new ImageManagerApplication(applicationView);
		} else if(appKey.equals(ApplicationView.WINDOW_KEY_IMAGE_PREVIEW)) {
			retBase = new ImagePreviewApplication(applicationView);
		} else if(appKey.equals(ApplicationView.WINDOW_KEY_APP_VERSION)) {
			retBase = new AppVersionApplication(applicationView);
		} else if(appKey.equals(ApplicationView.WINDOW_DEPARTMENT_MANAGER)) {
			retBase = new DepartmentApplication(applicationView);
		} else if(appKey.equals(ApplicationView.WINDOW_ANALYSIS_MAIN)) {
			retBase = new MainAnalysisApplication(applicationView);
		} else if(appKey.equals(ApplicationView.WINDOW_ANALYSIS_TAG)) {
			retBase = new TagAnalysisApplication(applicationView);
		} else if(appKey.equals(ApplicationView.WINDOW_ANALYSIS_AREA)) {
			retBase = new AreaAnalysisApplication(applicationView);
		} else if(appKey.equals(ApplicationView.WINDOW_ANALYSIS_CONTENTS)) {
			retBase = new ContentsAnalysisApplication(applicationView);
		} else if(appKey.equals(ApplicationView.WINDOW_ANALYSIS_BANNER)) {
			retBase = new BannerAnalysisApplication(applicationView);
		} else if(appKey.equals(ApplicationView.WINDOW_ANALYSIS_CONNECT)) {
			retBase = new ConnectAnalysisApplication(applicationView);
		} else if(appKey.equals(ApplicationView.WINDOW_ANALYSIS_OTHERDEP)) {
			retBase = new OtherDepAnalysisApplication(applicationView);
		} else if(appKey.equals(ApplicationView.WINDOW_PARTNERS_CONTENT)) {
			retBase = new PartnersContentApplication(applicationView);
		} else if(appKey.equals(ApplicationView.WINDOW_PARTNERS_CHANNEL)) {
			retBase = new PartnersChannelApplication(applicationView);
		} else if(appKey.equals(ApplicationView.WINDOW_PARTNERS_ACTIVITY)) {
			retBase = new ActivityApplication(applicationView);
		} else if(appKey.equals(ApplicationView.WINDOW_PARTNERS_AFFILIATE_PROPOSAL)) {
			retBase = new PartnersAffiliateProposalApplication(applicationView);
		} else if(appKey.equals(ApplicationView.WINDOW_KEY_FCM_PUSH)) {
			retBase = new FcmApplication(applicationView);
		} else if(appKey.equals(ApplicationView.WINDOW_REPAIR)) {
			retBase = new RepairApplication(applicationView);
		} else if(appKey.equals(ApplicationView.WINDOW_KEY_SERVICE_MANAGEMENT)) {
			retBase = new OtherDepartmentServiceManagementApplication(applicationView);
		} else if(appKey.equals(ApplicationView.WINDOW_KEY_EVENT_MANAGER)) {
			retBase = new EventApplication(applicationView);
		} else if(appKey.equals(ApplicationView.WINDOW_KEY_EVENT_BLACKLIST)) {
			retBase = new BlacklistApplication(applicationView);
		} else if(appKey.equals(ApplicationView.WINDOW_KEY_EVENT_DASHBOARD)) {
			retBase = new EventDashboardJoinApplication(applicationView);
		} else if(appKey.equals(ApplicationView.WINDOW_KEY_AUTHOR_MANAGER)) {
			retBase = new AuthorApplication(applicationView);
		} else if(appKey.equals(ApplicationView.WINDOW_CODE_MANAGER)) {
			retBase = new CodeCategoryApplication(applicationView);
		} else if(appKey.equals(ApplicationView.WINDOW_ADD_MENU)) {
			retBase = new AddMenuApplication(applicationView);
		} else if(appKey.equals(ApplicationView.WINDOW_SERVER_MONITORING)) {
			retBase = new MonitoringApplication(applicationView);
		} else if(appKey.equals(ApplicationView.WINDOW_STAMP_EVENT_STATS)) {
			retBase = new StampApplication(applicationView);
		} else if(appKey.equals(ApplicationView.WINDOW_KEY_EVENT_DASHBOARD_USER)) {
			retBase = new EventDashboardUserApplication(applicationView);
		} else if(appKey.equals(ApplicationView.WINDOW_KEY_EVENT_DASHBOARD_ACCESS)) {
			retBase = new EventDashboardAccessApplication(applicationView);
		}
		
		
		return retBase;
	}

}
