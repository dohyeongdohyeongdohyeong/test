package kr.or.visitkorea.admin.client.manager.appVersion;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONString;

import gwt.material.design.jquery.client.api.Functions;
import gwt.material.design.jscore.client.api.JSON;
import gwt.material.design.jscore.client.api.JsObject;
import kr.or.visitkorea.admin.client.application.ApplicationView;
import kr.or.visitkorea.admin.client.manager.ApplicationBase;
import kr.or.visitkorea.admin.client.manager.VisitKoreaBusiness;
import kr.or.visitkorea.admin.client.manager.appVersion.dialog.InputDialog;
import kr.or.visitkorea.admin.client.manager.appVersion.table.Version;
import kr.or.visitkorea.admin.client.widgets.window.MaterialExtentsWindow;

public class AppVersionApplication extends ApplicationBase {

    static final String APP_VERSION_NEW_DIALOG = "APP_VERSION_NEW_DIALOG";
    static final String APP_VERSION_EDIT_DIALOG = "APP_VERSION_EDIT_DIALOG";

    private AppVersionMain main;

    private InputDialog newDialog;
    private InputDialog editDialog;

    public AppVersionApplication(ApplicationView applicationView) {
        super(applicationView);
    }

    @Override
    public void setWindow(MaterialExtentsWindow materialExtentsWindow, String divisionName) {
        setDivisionName(divisionName);
        windowLiveFlag = true;
        window = materialExtentsWindow;
        window.addCloseHandler(event-> {
            windowLiveFlag = false;
        });

        main = new AppVersionMain(window, this);

        newDialog = new InputDialog("new", window, main);
        editDialog = new InputDialog("edit", window, main);

        window.addDialog(APP_VERSION_NEW_DIALOG, newDialog);
        window.addDialog(APP_VERSION_EDIT_DIALOG, editDialog);
    }

    void clearNewDialog() {
        newDialog.setData(null);
    }

    void setVersionData(Version version) {
        editDialog.setData(version);
    }

    @Override
    public void start() {
        start(null);
    }

    @Override
    public void start(Map<String, Object> params) {
        this.params = params;
        window.add(main);
        window.open(window);
    }

    static void fetchAllAppVersion(Functions.Func1<List<Version>> callback) {
        fetchAppVersion(null, callback);
    }

    static void fetchAppVersion(String apId, Functions.Func1<List<Version>> callback) {
        JSONObject parameter = new JSONObject();
        parameter.put("cmd", new JSONString("SELECT_APP_VERSION"));
        if (apId != null && !apId.isEmpty()) {
            parameter.put("apId", new JSONString(apId));
        }

        VisitKoreaBusiness.post("call", parameter.toString(), (Object param1, String param2, Object param3) -> {
            JSONObject result = (JSONObject) JSONParser.parseStrict(JSON.stringify((JsObject) param1));
            JSONObject header = (JSONObject) result.get("header");
            String processResult = header.get("process").isString().stringValue();
            if (processResult.equals("success")) {
                JSONObject body = (JSONObject) result.get("body");
                JSONArray bodyResult = (JSONArray) body.get("result");
                if (callback != null) {
                    ArrayList<Version> list = new ArrayList<>();
                    for (int i = 0, size = bodyResult.size(); i < size; i ++) {
                        JSONObject item = bodyResult.get(i).isObject();
                        list.add(Version.valueOf(item));
                    }
                    callback.call(list);
                }
            } else {
                if (callback != null) {
                    callback.call(null);
                }
            }
        });
    }

    static void insertAppVersion(Version version, Functions.Func1<Boolean> callback) {
        JSONObject parameter = new JSONObject();
        parameter.put("cmd", new JSONString("INSERT_APP_VERSION"));

        try {
            parameter.put("apId", new JSONString(version.getApId()));
        } catch (NullPointerException e) {
            callback.call(false);
            return;
        }

        try {
            parameter.put("ver", new JSONString(version.getVersion()));
        } catch (NullPointerException e) {
            callback.call(false);
            return;
        }

        try {
            parameter.put("min_ver", new JSONString(version.getMinVersion()));
        } catch (NullPointerException e) {
            callback.call(false);
            return;
        }

        try {
            parameter.put("url", new JSONString(version.getUrl()));
        } catch (NullPointerException e) {
            callback.call(false);
            return;
        }
        try {
            parameter.put("header_color", new JSONString(version.getHeaderColor()));
        } catch (NullPointerException e) {
            callback.call(false);
            return;
        }
        VisitKoreaBusiness.post("call", parameter.toString(), (Object param1, String param2, Object param3) -> {
            JSONObject result = (JSONObject) JSONParser.parseStrict(JSON.stringify((JsObject) param1));
            JSONObject header = (JSONObject) result.get("header");
            String processResult = header.get("process").isString().stringValue();
            if (callback != null) {
                callback.call(processResult.equals("success"));
            }
        });
    }

    static void updateAppVersion(Version version, Functions.Func1<Boolean> callback) {
        JSONObject parameter = new JSONObject();
        parameter.put("cmd", new JSONString("UPDATE_APP_VERSION"));

        try {
            parameter.put("apId", new JSONString(version.getApId()));
        } catch (NullPointerException e) {
            callback.call(false);
            return;
        }

        try {
            parameter.put("ver", new JSONString(version.getVersion()));
        } catch (NullPointerException e) {
            // ignore
        }

        try {
            parameter.put("min_ver", new JSONString(version.getMinVersion()));
        } catch (NullPointerException e) {
            // ignore
        }

        if (parameter.get("ver") == null && parameter.get("min_ver") == null) {
            callback.call(false);
            return;
        }

        try {
            parameter.put("url", new JSONString(version.getUrl()));
        } catch (NullPointerException e) {
            // ignore
        }
        
        try {
            parameter.put("header_color", new JSONString(version.getHeaderColor()));
        } catch (NullPointerException e) {
        	// ignore
        }
        
/*        
        ApplicationBusiness.post("call", parameter.toString(), (Object param1, String param2, Object param3) -> {
            JSONObject result = (JSONObject) JSONParser.parseStrict(JSON.stringify((JsObject) param1));
            JSONObject header = (JSONObject) result.get("header");
            String processResult = header.get("process").isString().stringValue();
            if (callback != null) {
                callback.call(processResult.equals("success"));
            }
        });
*/        
        VisitKoreaBusiness.post("call", parameter.toString(), (Object param1, String param2, Object param3) -> {
        	
            JSONObject result = (JSONObject) JSONParser.parseStrict(JSON.stringify((JsObject) param1));
            JSONObject header = (JSONObject) result.get("header");
            String processResult = header.get("process").isString().stringValue();
            
            if (callback != null) {
                callback.call(processResult.equals("success"));
            }
            
        });
        
    }

    static void deleteAppVersion(String apId, Functions.Func1<Boolean> callback) {
    	
        JSONObject parameter = new JSONObject();
        parameter.put("cmd", new JSONString("DELETE_APP_VERSION"));

        try {
            parameter.put("apId", new JSONString(apId));
        } catch (NullPointerException e) {
            callback.call(false);
            return;
        }

        VisitKoreaBusiness.post("call", parameter.toString(), (Object param1, String param2, Object param3) -> {
            
        	JSONObject result = (JSONObject) JSONParser.parseStrict(JSON.stringify((JsObject) param1));
            JSONObject header = (JSONObject) result.get("header");
            String processResult = header.get("process").isString().stringValue();
            
            if (callback != null) {
                callback.call(processResult.equals("success"));
            }
            
        });
    }
}
