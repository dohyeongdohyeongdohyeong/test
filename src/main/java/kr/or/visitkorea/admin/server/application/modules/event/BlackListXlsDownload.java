package kr.or.visitkorea.admin.server.application.modules.event;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.json.JSONArray;
import org.json.JSONObject;

import com.google.gwt.dev.ModuleTabPanel.Session;

import kr.or.visitkorea.admin.server.application.Call;
import kr.or.visitkorea.admin.server.application.modules.AbstractModule;
import kr.or.visitkorea.admin.server.application.modules.BusinessMapping;

@BusinessMapping(id="BLACK_LIST_XLS_DOWNLOAD")
public class BlackListXlsDownload extends AbstractModule {

	@Override
	public void run(JSONObject resultHeaderObject, JSONObject resultBodyObject) {
		
		HttpServletResponse response = this.getResponse();
		HttpServletRequest request = this.getRequest();
		String strClient = request.getHeader("User-Agent");
		String SaveName = null;
		String FileName = null;
		String nameString = this.parameterObject.getString("select_type");
		
		if(nameString.equals("BlackList_Base")) {
			
			HashMap<String, Object> result = 
					sqlSession.selectOne("kr.or.visitkorea.system.EventBlacklistMapper.GetBlacklistBaseFile");
			
			FileName = result.get("FILE_NM").toString();
			SaveName = result.get("FILE_SAVE_NM").toString();
		} else {
			SaveName = this.parameterObject.getString("saveName");
			FileName = this.parameterObject.getString("fileName");
		}
		
		String[] uuids = SaveName.split("-");
		String directory0 = uuids[0].substring(0, 2);
		String directory1 = uuids[1].substring(0, 2);
		String directory2 = uuids[2].substring(0, 2);
		String directory3 = uuids[3].substring(0, 2);
		String directory4 = uuids[4].substring(0, 2);
		OutputStream outputstream = null;
        FileInputStream inputstream = null;
        
		String fileUploadPath = Call.getProperty("temp.xls.upload.path");
		
		String targetDirectory = String.format(
				"%s/%s/%s/%s/%s/%s/",
				fileUploadPath,
				directory0, directory1, directory2, directory3, directory4
		);
		
		fileUploadPath = targetDirectory + SaveName;
		
		System.out.println(fileUploadPath);

		File file = new File(fileUploadPath);
		XSSFWorkbook objWorkBook;
		try {
			objWorkBook = new XSSFWorkbook(file);
			
			FileName = new String ( FileName.getBytes("KSC5601"), "8859_1");
			if (strClient.indexOf("MSIE 5.5") > -1) {
			    response.setHeader("Content-Disposition", "filename=" + FileName + ";");
			} else {
			    response.setContentType("application/vnd.ms-excel");
			    response.setHeader("Content-Disposition", "attachment; filename=" + FileName + ";");
			}
			objWorkBook.write(response.getOutputStream());
		    objWorkBook.close();
		    response.getOutputStream().close();
	    
		} catch (Exception e) {
			e.printStackTrace();
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			return;
		}
	    
	}

	@Override
	protected void beforeRun(JSONObject resultHeaderObject, JSONObject resultBodyObject) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void afterRun(JSONObject resultHeaderObject, JSONObject resultBodyObject) {
		// TODO Auto-generated method stub
		
	}

}
