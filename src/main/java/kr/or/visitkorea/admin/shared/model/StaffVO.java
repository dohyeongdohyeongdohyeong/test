package kr.or.visitkorea.admin.shared.model;

import java.sql.Timestamp;
import java.time.LocalDateTime;

import org.apache.ibatis.type.Alias;
import org.json.JSONObject;

@Alias(value="staffVo")
public class StaffVO {
	private String usrId;
	private String stfId;
	private String dispName;
	private String imagePath;
	private int checkUse;
	private String templateId;
	private String createDate;
	private String lastLoginDate;
	private LocalDateTime pwRefreshDate;
	private long loginResult;

	public static StaffVO fromJson(JSONObject obj) {
		StaffVO staff = new StaffVO();
		if (obj.has("USR_ID"))
			staff.setUsrId(obj.getString("USR_ID"));
		if (obj.has("STF_ID"))
			staff.setStfId(obj.getString("STF_ID"));
		if (obj.has("DISP_NAME"))
			staff.setDispName(obj.getString("DISP_NAME"));
		if (obj.has("IMAGE_PATH"))
			staff.setImagePath(obj.getString("IMAGE_PATH"));
		if (obj.has("CHK_USE"))
			staff.setCheckUse(obj.getInt("CHK_USE"));
		if (obj.has("TEMPLATE_ID"))
			staff.setTemplateId(obj.getString("TEMPLATE_ID"));
		if (obj.has("CREATE_DATE"))
			staff.setCreateDate(obj.getString("CREATE_DATE"));
		if (obj.has("LAST_LOGIN_DATETIME"))
			staff.setLastLoginDate(obj.getString("LAST_LOGIN_DATETIME"));
		if (obj.has("PW_REFRESH_DATE"))
			staff.setPwRefreshDate(Timestamp.valueOf(obj.getString("PW_REFRESH_DATE")).toLocalDateTime());
		if (obj.has("LOGIN_RESULT"))
			staff.setLoginResult((long) obj.get("LOGIN_RESULT"));
		return staff;
	}
	
	public String getUsrId() {
		return usrId;
	}

	public void setUsrId(String usrId) {
		this.usrId = usrId;
	}

	public String getStfId() {
		return stfId;
	}

	public void setStfId(String stfId) {
		this.stfId = stfId;
	}

	public String getDispName() {
		return dispName;
	}

	public void setDispName(String dispName) {
		this.dispName = dispName;
	}

	public String getImagePath() {
		return imagePath;
	}

	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
	}

	public int getCheckUse() {
		return checkUse;
	}

	public void setCheckUse(int checkUse) {
		this.checkUse = checkUse;
	}

	public String getTemplateId() {
		return templateId;
	}

	public void setTemplateId(String templateId) {
		this.templateId = templateId;
	}

	public String getCreateDate() {
		return createDate;
	}

	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}

	public String getLastLoginDate() {
		return lastLoginDate;
	}

	public void setLastLoginDate(String lastLoginDate) {
		this.lastLoginDate = lastLoginDate;
	}

	public LocalDateTime getPwRefreshDate() {
		return pwRefreshDate;
	}

	public void setPwRefreshDate(LocalDateTime pwRefreshDate) {
		this.pwRefreshDate = pwRefreshDate;
	}


	public long getLoginResult() {
		return loginResult;
	}

	public void setLoginResult(long loginResult) {
		this.loginResult = loginResult;
	}

	@Override
	public String toString() {
		return "StaffVO [usrId=" + usrId + ", stfId=" + stfId + ", dispName=" + dispName + ", imagePath=" + imagePath
				+ ", checkUse=" + checkUse + ", templateId=" + templateId + ", createDate=" + createDate
				+ ", lastLoginDate=" + lastLoginDate + ", pwRefreshDate=" + pwRefreshDate + ", loginResult="
				+ loginResult + "]";
	}

}
