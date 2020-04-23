package kr.or.visitkorea.admin.client.manager.fcm;

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
import kr.or.visitkorea.admin.client.manager.fcm.dialog.SendMessageDialog;
import kr.or.visitkorea.admin.client.manager.fcm.msg.FcmMessage;
import kr.or.visitkorea.admin.client.widgets.window.MaterialExtentsWindow;

public class FcmApplication extends ApplicationBase {
	public static final String SEND_FCM_MESSAGE_DIALOG = "SEND_FCM_MESSAGE_DIALOG";

    private FcmMain main;
    private SendMessageDialog dialog;

    public FcmApplication(ApplicationView applicationView) {
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

        main =  new FcmMain(materialExtentsWindow);
        dialog = new SendMessageDialog(materialExtentsWindow, main);
        window.addDialog(SEND_FCM_MESSAGE_DIALOG, dialog);
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

    public static void fetchFcmHistory(JSONObject paramJson, Functions.Func2<Integer, List<FcmMessage>> callback) {
        VisitKoreaBusiness.post("call", paramJson.toString(), (Object param1, String param2, Object param3) -> {
            JSONObject result = (JSONObject) JSONParser.parseStrict(JSON.stringify((JsObject) param1));
            JSONObject header = (JSONObject) result.get("header");
            String processResult = header.get("process").isString().stringValue();
            
            if (processResult.equals("success")) {
                JSONObject body = result.get("body").isObject();
                JSONArray bodyResult = body.get("result").isArray();
                int totalCount = (int) body.get("resultCnt").isNumber().doubleValue();
                
                if (callback != null) {
                    ArrayList<FcmMessage> list = new ArrayList<>();
                    for (int i = 0, size = bodyResult.size(); i < size; i ++) {
                        JSONObject item = bodyResult.get(i).isObject();
                        list.add(FcmMessage.valueOf(item));
                    }
                    callback.call(totalCount, list);
                }
            } else {
                if (callback != null) {
                    callback.call(0, null);
                }
            }
        });
    }

    public static void sendFcmMessage(FcmMessage message, Functions.Func1<Boolean> callback) {
        JSONObject parameter = new JSONObject();
        parameter.put("cmd", new JSONString("SEND_FCM_MESSAGE"));

        try {
            parameter.put("target", new JSONString(message.getTarget()));
        } catch (NullPointerException e) {
            callback.call(false);
            return;
        }

        try {
            parameter.put("title", new JSONString(message.getTitle()));
        } catch (NullPointerException e) {
            callback.call(false);
            return;
        }

        try {
            parameter.put("message", new JSONString(message.getMessage()));
        } catch (NullPointerException e) {
            callback.call(false);
            return;
        }
        
        try {
            parameter.put("url", new JSONString(message.getUrl()));
        } catch (NullPointerException e) {
            callback.call(false);
            return;
        }

        VisitKoreaBusiness.post("call", parameter.toString(), (param1, param2, param3) -> {
            JSONObject result = (JSONObject) JSONParser.parseStrict(JSON.stringify((JsObject) param1));
            JSONObject header = (JSONObject) result.get("header");
            String processResult = header.get("process").isString().stringValue();
            if (callback != null) {
                callback.call(processResult.equals("success"));
            }
        });
    }
}
