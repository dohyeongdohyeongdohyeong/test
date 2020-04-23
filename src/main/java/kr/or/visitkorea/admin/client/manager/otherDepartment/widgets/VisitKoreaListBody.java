package kr.or.visitkorea.admin.client.manager.otherDepartment.widgets;

import java.util.List;
import java.util.Map;

import com.google.gwt.user.client.ui.Widget;

public interface VisitKoreaListBody {

	public void setSearch(VisitKoreaSearch search);
	public void addRow(TagListRow row);
	public List<Widget> getRows();
	public void addRow(TagListRow tagListRow, Map<String, Object> resultMap);
	
}
