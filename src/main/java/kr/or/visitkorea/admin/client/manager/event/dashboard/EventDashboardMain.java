package kr.or.visitkorea.admin.client.manager.event.dashboard;


import kr.or.visitkorea.admin.client.manager.event.dashboard.model.EventMasterModel;
import kr.or.visitkorea.admin.client.manager.event.dashboard.panel.EventJoinDetail;
import kr.or.visitkorea.admin.client.manager.event.dashboard.panel.EventJoinList;
import kr.or.visitkorea.admin.client.manager.widgets.AbstractContentPanel;
import kr.or.visitkorea.admin.client.widgets.window.MaterialExtentsWindow;

public class EventDashboardMain extends AbstractContentPanel {
	private EventJoinList list;
	private EventJoinDetail detail;
	private MaterialExtentsWindow meWindow ;
	private EventMasterModel model ;
	
	public EventDashboardMain() {
		super();
	}

	public EventDashboardMain(MaterialExtentsWindow meWindow) {
		super(meWindow);
		this.meWindow = meWindow;
	}

	@Override
	public void init() {
		list = new EventJoinList(this);
		switchToListPanel();
	}
	
	

	private EventJoinDetail getDetailPanel() {
		if (detail == null) {
			detail = new EventJoinDetail(this);
		}
		return detail;
	}

	
	public void switchToListPanel() {
		if (detail != null) {
			remove(detail);
			detail = null;
		}
		add(list);
	}
	
	public void switchToDetailPanel(EventMasterModel model) {
		remove(list);
		setModel(model);
		add(getDetailPanel());
	}

	public EventMasterModel getModel() {
		return model;
	}

	public void setModel(EventMasterModel model) {
		this.model = model;
	}
	
	
	
	
	
}
