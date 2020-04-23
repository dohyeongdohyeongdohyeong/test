package kr.or.visitkorea.admin.server.application.modules.files;

import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

import kr.or.visitkorea.admin.server.application.Call;
import kr.or.visitkorea.admin.server.application.modules.AbstractModule;
import kr.or.visitkorea.admin.server.application.modules.BusinessMapping;

/**
 * kr.or.visitkorea.admin.server.application.module.Login
 * @author jungwoochoi
 *
 */
@BusinessMapping(id="VIEW")
public class View extends AbstractModule{

	@Override
	public void run(JSONObject resultHeaderObject, JSONObject resultBodyObject) {
		
		HttpServletRequest request = this.getRequest();
		HttpServletResponse response = this.getResponse();
		String nameString = this.parameterObject.getString("name");

    	try {
    		
			String[] uuids = nameString.split("-");
			String directory0 = uuids[0].substring(0, 2);
			String directory1 = uuids[1].substring(0, 2);
			String directory2 = uuids[2].substring(0, 2);
			String directory3 = uuids[3].substring(0, 2);
			String directory4 = uuids[4].substring(0, 2);
    		
	    	String fileUploadPath = Call.getProperty("image.upload.path");

	    	String targetDirectory = String.format(
					"%s/%s/%s/%s/%s/%s/%s", 
					fileUploadPath, 
					directory0, directory1, directory2, directory3, directory4,
					nameString
					);

	    	System.out.println("targetDirectory : " + targetDirectory);

			ServletContext cntx = request.getServletContext();
			String mime = cntx.getMimeType(targetDirectory);
			if (mime == null) {
				response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
				return;
			}
			
			response.setContentType(mime);
			
			File file = new File(targetDirectory);
			response.setContentLength((int) file.length());
	
			FileInputStream in = new FileInputStream(file);
			OutputStream out = response.getOutputStream();
	
			byte[] buf = new byte[1024];
			int count = 0;
			while ((count = in.read(buf)) >= 0) {
				out.write(buf, 0, count);
			}
	
			out.close();
			in.close();
			
			resultObject.put("return.type", "response-end");


        } catch(Exception caught) {
        	
			resultHeaderObject.put("process", "fail");
			resultHeaderObject.put("ment", caught.getMessage());
		}

	}

	@Override
	protected void beforeRun(JSONObject resultHeaderObject, JSONObject resultBodyObject) {
			//resultBodyObject.put("beforeRun", this.getClass().getName());
	}

	@Override
	protected void afterRun(JSONObject resultHeaderObject, JSONObject resultBodyObject) {
		//resultBodyObject.put("afterRun", this.getClass().getName());
	}

}
