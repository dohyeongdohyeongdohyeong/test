package kr.or.visitkorea.admin.client.manager.contents.course;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.TextResource;

public interface CourseContentBundle extends ClientBundle{

	CourseContentBundle INSTANCE = GWT.create(CourseContentBundle.class);

    @Source("resources/css/content.css")
    TextResource contentCss();

}
