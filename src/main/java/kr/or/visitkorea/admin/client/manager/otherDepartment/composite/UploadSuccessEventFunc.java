package kr.or.visitkorea.admin.client.manager.otherDepartment.composite;

import gwt.material.design.addins.client.fileuploader.base.UploadFile;
import gwt.material.design.addins.client.fileuploader.events.SuccessEvent;

public interface UploadSuccessEventFunc {

	void invoke(SuccessEvent<UploadFile> event);

}
