package kr.or.visitkorea.admin.client.manager.fcm.msg;

import com.google.gwt.json.client.JSONObject;

public class FcmMessage {
    private int pmId;
    private String target;
    private String title;
    private String message;
    private String url;
    private String sendDate;

    public static FcmMessage valueOf(JSONObject item) {
    	int pmId = (int) item.get("PM_ID").isNumber().doubleValue();
        String target = item.get("TARGET").isString().stringValue();
        String title = item.containsKey("TITLE") ? item.get("TITLE").isString().stringValue() : "";
        String message = item.containsKey("MESSAGE") ? item.get("MESSAGE").isString().stringValue() : "";
        String url = item.containsKey("URL") ? item.get("URL").isString().stringValue() : "";
        String sendDate = item.get("SEND_DATE").isString().stringValue();
        return new FcmMessage(pmId, target, title, message, url, sendDate);
    }

    public int getPmId() {
		return pmId;
	}

	public void setPmId(int pmId) {
		this.pmId = pmId;
	}

	public String getTarget() {
		return target;
	}

	public void setTarget(String target) {
		this.target = target;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getSendDate() {
		return sendDate;
	}

	public void setSendDate(String sendDate) {
		this.sendDate = sendDate;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public FcmMessage (int pmId, String target, String title, String message, String url) {
        this.pmId = pmId;
        this.target = target;
        this.title = title;
        this.message = message;
        this.url = url;
    }

    public FcmMessage (int pmId, String target, String title, String message, String url, String sendDate) {
        this.pmId = pmId;
        this.target = target;
        this.title = title;
        this.message = message;
        this.url = url;
        this.sendDate = sendDate;
    }
}
