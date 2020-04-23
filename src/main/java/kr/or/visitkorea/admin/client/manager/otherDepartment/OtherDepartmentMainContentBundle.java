package kr.or.visitkorea.admin.client.manager.otherDepartment;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.TextResource;

public interface OtherDepartmentMainContentBundle extends ClientBundle{

	OtherDepartmentMainContentBundle INSTANCE = GWT.create(OtherDepartmentMainContentBundle.class);

    @Source("resources/css/otherDepartment.css")
    TextResource otherDepartmentCss();

}
