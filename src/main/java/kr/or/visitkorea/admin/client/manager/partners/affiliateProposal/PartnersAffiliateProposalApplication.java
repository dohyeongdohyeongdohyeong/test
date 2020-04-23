package kr.or.visitkorea.admin.client.manager.partners.affiliateProposal;

import java.util.HashMap;
import java.util.Map;

import kr.or.visitkorea.admin.client.application.ApplicationView;
import kr.or.visitkorea.admin.client.manager.ApplicationBase;
import kr.or.visitkorea.admin.client.manager.partners.affiliateProposal.dialogs.SelectContentDetail;
//import kr.or.visitkorea.admin.client.manager.partners.affiliateProposal.dialogs.SelectContentDetail;
import kr.or.visitkorea.admin.client.widgets.window.MaterialExtentsWindow;

/**
 * @author Admin
 * 제휴/제안 신청 Application
 */
public class PartnersAffiliateProposalApplication extends ApplicationBase {
	// 파트너 제휴/제안 신청 어플리케이션 내에서 공용으로 사용해야 할 데이터를 담는 콜렉션
	private static final Map<String, Object> internalMap = new HashMap<String, Object>();
	
	// 리스트에서 호출될 상세 페이지 다이어로그 등록
	public static final String CONTENT_DETAIL = "CONTENT_DETAIL";
	
	PartnersAffiliateProposalMain host;
	
	public PartnersAffiliateProposalApplication(ApplicationView applicationView) {
		super(applicationView);
	}
	
	@Override
	public void setWindow(MaterialExtentsWindow materialExtentsWindow, String divisionName) {
		this.setDivisionName(divisionName);
		this.windowLiveFlag = true;
		this.window = materialExtentsWindow;
		host = new PartnersAffiliateProposalMain(this.window, this);
		// 등록된 상세 페이지 다이어로그 추가
		this.window.addDialog(CONTENT_DETAIL, new SelectContentDetail(this.window, host));
		this.window.addCloseHandler(event -> {
			windowLiveFlag = false;
		});
	}

	// partner 제휴/제안 신청 어플리케이션에 시작을 알리면 lifecycle
	@Override
	public void start() {
		start(null);
	}

	// partner 제휴/제안 신청 어플리케이션에 시작을 알리면 lifecycle, 메소드 오버로딩, 파라미터가 있는 경우
	@Override
	public void start(Map<String, Object> params) {
		this.params = params;
		host = new PartnersAffiliateProposalMain(this.window, this);
		this.window.add(host);
		this.window.open(this.window);
	}
	public static void setValue(String key, Object value) {
		internalMap.put(key, value);
	}
	public static Object getValue(String key) {
		return internalMap.get(key);
	}
}