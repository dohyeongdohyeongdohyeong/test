package kr.or.visitkorea.admin.client.manager.appVersion.table;

import com.google.gwt.json.client.JSONObject;

public class Version {
    private String apId;
    private String version;
    private String minVersion;
    private String url;
    private String date;
    private String headerColor;

    public static Version valueOf(JSONObject item) {
    	
        Version version = new Version();
        version.apId = item.get("AP_ID").isString().stringValue();
        version.version = item.get("VER").isString().stringValue();
        version.minVersion = item.get("VER_MIN").isString().stringValue();
        version.url = item.get("URL").isString().stringValue();
        version.date = item.get("UPDATE_DATE").isString().stringValue();
        version.headerColor = item.get("headerColor").isString().stringValue();
        
        return version;
        
    }

    public static Version valueOf(String apId, String ver, String min, String url) {
        
    	Version version = new Version();
        version.apId = apId;
        version.version = ver;
        version.minVersion = min;
        version.url = url;
        
        return version;
        
    }

    public String getHeaderColor() {
		return headerColor;
	}

	public void setHeaderColor(String headerColor) {
		this.headerColor = headerColor;
	}

	public String getApId() {
        return apId;
    }

    public void setApId(String apId) {
        this.apId = apId;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getMinVersion() {
        return minVersion;
    }

    public void setMinVersion(String version) {
        this.minVersion = version;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "Version (" +
                "apId=" + apId + ", " +
                "version=" + version + ", " +
                "minVersion=" + minVersion + ", " +
                "url=" + url +
                "headerColor=" + headerColor +
                ")";
    }
}
