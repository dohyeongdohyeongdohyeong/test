package kr.or.visitkorea.admin.client.manager.otherDepartment.composite;

public interface ContentDetail {

	ContentRow getCustomRow();

	void addRow(ContentRow masterContentRow);

	void setRecIndex(int i);

}
