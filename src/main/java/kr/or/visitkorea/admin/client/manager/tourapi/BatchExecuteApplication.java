package kr.or.visitkorea.admin.client.manager.tourapi;

import java.util.Map;

import kr.or.visitkorea.admin.client.application.ApplicationView;
import kr.or.visitkorea.admin.client.manager.ApplicationBase;
import kr.or.visitkorea.admin.client.manager.tourapi.composite.BatchExecuteMain;
import kr.or.visitkorea.admin.client.manager.tourapi.dialog.SearchDialog;
import kr.or.visitkorea.admin.client.manager.widgets.ConfirmDialog;
import kr.or.visitkorea.admin.client.widgets.window.MaterialExtentsWindow;

public class BatchExecuteApplication extends ApplicationBase {

    public static final String SEARCH_WITH_CALENDAR_DIALOG = "SEARCH_WITH_CALENDAR_DIALOG2";
    public static final String CONFIRM_EXECUTE_BATCH_DIALOG = "CONFIRM_EXECUTE_BATCH_DIALOG";

    public BatchExecuteApplication(ApplicationView applicationView) {
        super(applicationView);
    }

    @Override
    public void setWindow(MaterialExtentsWindow materialExtentsWindow, String divisionName) {
        this.setDivisionName(divisionName);
        this.windowLiveFlag = true;
        this.window = materialExtentsWindow;
        this.window.addCloseHandler(event -> {
            windowLiveFlag = false;
        });
        this.window.addDialog(SEARCH_WITH_CALENDAR_DIALOG, new SearchDialog(this.window));
        ConfirmDialog confirmDialog = new ConfirmDialog(this.window);
        confirmDialog.setMsg("확인", "배치를 실행하시겠습니까?");
        this.window.addDialog(CONFIRM_EXECUTE_BATCH_DIALOG, confirmDialog);
    }

    @Override
    public void start() {
        start(null);
    }

    @Override
    public void start(Map<String, Object> params) {
        this.params = params;
        this.window.add(new BatchExecuteMain(this.window));
        this.window.open(this.window);
    }
}
