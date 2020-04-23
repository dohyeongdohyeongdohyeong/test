package kr.or.visitkorea.admin.client.manager.otherDepartment.composite.festival;

import java.util.Map;

public interface FestivalContentRight {

	public void setLeftPanel(FestivalContentLeftArea leftArea);

	public void clearAll();

	public void setDisableContent(boolean disable);

	public Map<String, Object> getValueMap();

}
