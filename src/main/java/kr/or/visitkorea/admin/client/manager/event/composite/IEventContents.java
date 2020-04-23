package kr.or.visitkorea.admin.client.manager.event.composite;

import com.google.gwt.json.client.JSONObject;

import kr.or.visitkorea.admin.client.manager.event.EventStatus;
import kr.or.visitkorea.admin.client.manager.event.widgets.FetchCallback;

public interface IEventContents {
	public abstract void loadData(FetchCallback callback);
	public abstract void saveData();
	
	//	이벤트 상태값 (Status)에 따른 처리
	public abstract void statusChangeProcess(EventStatus status);
	
	//	데이터 값 setup
	public abstract void setupContentValue(JSONObject obj);
	
	//	저장할 데이터 모델 build
	public abstract JSONObject buildEventModel();
}
