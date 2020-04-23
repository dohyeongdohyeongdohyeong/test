package kr.or.visitkorea.admin.server.application;

import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.Enumeration;
import java.util.Properties;
import java.util.UUID;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItemIterator;
import org.apache.commons.fileupload.FileItemStream;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.filter.Filters;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.XMLOutputter;
import org.jdom2.xpath.XPathExpression;
import org.jdom2.xpath.XPathFactory;
import org.json.JSONArray;
import org.json.JSONObject;

import kr.or.visitkorea.admin.client.application.Registry;
import kr.or.visitkorea.admin.server.application.modules.AbstractModule;
import kr.or.visitkorea.admin.server.application.modules.Module;

/**
 * Servlet implementation class Call
 */
public class Call extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static Document bizDocument = null;
	private static Document permissionDoc = null;
	private static Document appDocument = null;
	private static Document editorDocument = null;
	private static Document queryMappingDocument = null;
	private static Document fileRepositoryDoc = null;
	private static Properties sysProps = null;
	private static SqlSessionFactory sqlSessionFactory;
	
	static {
		
		ClassLoader classLoader = Call.class.getClassLoader();
	    SAXBuilder builder = new SAXBuilder();
	    
	    try {
	    	
	    	bizDocument = builder.build(new File(classLoader.getResource("kr/or/visitkorea/admin/server/application/BusinessMapping.xml").getFile()));
	    	appDocument = builder.build(new File(classLoader.getResource("kr/or/visitkorea/admin/server/application/applicationMapping.xml").getFile()));
	    	editorDocument = builder.build(new File(classLoader.getResource("kr/or/visitkorea/admin/server/application/contentEditor.xml").getFile()));
	    	InputStream queryMappingConfigXmlInputStream = classLoader.getResource("kr/or/visitkorea/admin/server/application/QueryMappingConfig.xml").openStream();
	    	
			String defaultSystemProperties = "kr/or/visitkorea/admin/server/application/system.properties";

	    	InputStream inputStream = null;
	    	sysProps = new Properties();
	    	String systemProp = null;
	    	Object systemPropObject = System.getenv(Registry.getPropertyConstant());
	    	
	    	if (systemPropObject != null) {
	    		systemProp = ((String)systemPropObject);
	    	}
	    	
	    	if (systemProp == null || !new File(systemProp).exists()) {
	    		
	    		inputStream = classLoader.getResource(defaultSystemProperties).openStream();
	    		
			} else {
				
				inputStream = new FileInputStream(systemProp);
				queryMappingDocument = builder.build(new File(classLoader.getResource("kr/or/visitkorea/admin/server/application/QueryMappingConfig.xml").getFile()));
				queryMappingDocument.getRootElement().getChild("properties").removeAttribute("resource");
				queryMappingDocument.getRootElement().getChild("properties").setAttribute("url", "file:////"+systemProp);
				
				File tempFile = File.createTempFile(UUID.randomUUID().toString(), ".xml");
				FileOutputStream fos = new FileOutputStream(tempFile);
				queryMappingConfigXmlInputStream = new FileInputStream(tempFile);

				XMLOutputter out = new XMLOutputter();
				out.output(queryMappingDocument, fos);
			}
	    	
	    	// end of load properties
	    	sysProps.load(inputStream);

	    	String permissionDocPath = Call.getProperty("menu.xml.path");
	    	
	    	if (permissionDocPath == null) {
		    	permissionDoc = builder.build(new File(classLoader.getResource("kr/or/visitkorea/admin/server/application/defaultMenu.xml").getFile()));
	    	} else {
		    	File file = new File(permissionDocPath);
				if (file.exists()) {
			    	permissionDoc = builder.build(file);
				} else {
			    	permissionDoc = builder.build(new File(classLoader.getResource("kr/or/visitkorea/admin/server/application/defaultMenu.xml").getFile()));
				}
	    	}
	    	
	    	// start of sqlsession object
	    	sqlSessionFactory = new SqlSessionFactoryBuilder().build(queryMappingConfigXmlInputStream);
    	
		} catch (JDOMException | IOException e) {
			e.printStackTrace();
		}
	}

    /**
     * @see HttpServlet#HttpServlet()
     */
    public Call() {
        super();
    }
    
    public static SqlSessionFactory getSqlSessionFactory() {
    	return sqlSessionFactory;
    }

    public static Document getBusinessDocument() {
    	return bizDocument;
    }

    public static Document getPermissionDocument() {
    	return permissionDoc;
    }

    public static Document getEditorDocument() {
    	return editorDocument;
    }
    
    public static Document getApplicationListDocument() {
    	return appDocument;
    }
    
    public static String getProperty(String key) {
    	return sysProps.getProperty(key);
    }
    
	public static void setPermissionDoc(Document permissionDoc) {
		Call.permissionDoc = permissionDoc;
	}
    
	public static Document getFileRepositoryDoc() {
		return fileRepositoryDoc;
	}

	public static void setFileRepositoryDoc(Document fileRepositoryDoc) {
		Call.fileRepositoryDoc = fileRepositoryDoc;
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doExecute(request, response);
	}
	
	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doExecute(request, response);
	}
	
	private void doExecute(HttpServletRequest request, HttpServletResponse response) throws IOException {
		
		try {
			
			request.setCharacterEncoding("UTF-8");
			response.setCharacterEncoding("UTF-8");
			response.setHeader("Cache-Control", "no-cache");
			response.setHeader("Pragma", "no-cache");
			response.setHeader("Expires", "-1");
			
			JSONObject resultObj = invokeModule(requestBody(request), request, response);
			if (resultObj == null) {
				response.getWriter().append("실행 모듈이 정의되어 있지 않습니다. ");
			} else if (resultObj.getString("return.type").equals("application/json")){
				
				response.setCharacterEncoding("UTF-8");
				response.setContentType("application/json");
				response.getWriter().append(resultObj.toString());
				response.getWriter().flush();

			}else if (resultObj.getString("return.type").equals("text/plain")) {
				
				
				response.setCharacterEncoding("UTF-8");
				response.setContentType("text/plain");
				String resultString = resultObj.toString();
				response.getWriter().append(resultString);
				response.getWriter().flush();

			}else if (resultObj.getString("return.type").equals("response-end")) {
				
			}
			
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		
	}
	
	private JSONObject invokeModule(JSONObject obj, HttpServletRequest request, HttpServletResponse response) throws ClassNotFoundException, InstantiationException, IllegalAccessException {
		
		System.out.println("command :: " + obj.toString());
		
		if (obj.has("cmd")) {
			 
			String commandId = obj.getString("cmd");
			AbstractModule tgrModule = (AbstractModule) getClass(commandId).newInstance();
			
			if (tgrModule == null) return null;
			
			tgrModule.setParameter(obj);
			tgrModule.setRequest(request);
			tgrModule.setResponse(response);
			
			return tgrModule.invoke();
			
		}else { 
			
			System.out.println("invoke target module is not define~!");
			
			return null;
		}
		
	}

	private JSONObject requestBody(HttpServletRequest request) throws IOException {
		
		if (ServletFileUpload.isMultipartContent(request)) {
			
			return processUpload(request);
		
		}else {
			
			if (request.getContentType() == null) {
				
				JSONObject paramJSON = new JSONObject();
	
				Enumeration<String> tgrEnum = request.getParameterNames();
				
				while(tgrEnum.hasMoreElements()) {
					String rKey = tgrEnum.nextElement();
					String rValue = request.getParameter(rKey);
					paramJSON.put(rKey, rValue);
				}
				
				return paramJSON;
				
			}else if (request.getContentType().toLowerCase().contains("application/x-www-form-urlencoded")) {
				
				BufferedReader input = new BufferedReader(new InputStreamReader(request.getInputStream()));
				StringBuilder builder = new StringBuilder();
				String buffer;
				
				while ((buffer = input.readLine()) != null) {
					
					if (builder.length() > 0) {
						builder.append("\n");
					}
					
					builder.append(buffer);
					
				}
				
				String bodyString = builder.toString();

				return new JSONObject(bodyString);

			}else{
				
				return null;
				
			}
		}
	}

	private JSONObject processUpload(HttpServletRequest request) {
		
		ServletFileUpload upload = new ServletFileUpload();
		
		try {
			
			FileItemIterator iter = upload.getItemIterator(request);
			
			if (iter.hasNext()) {
				
				FileItemStream fileItemStream = iter.next();
				
				String fileName = fileItemStream.getName().toLowerCase();
				
				if (fileName.endsWith(".xls") || fileName.endsWith(".xlsx")) {
					return processUploadXls(fileItemStream);
				} else if (fileName.endsWith(".zip")) {
					return processUploadZip(fileItemStream);
				} else {
					return processUploadImage(fileItemStream);
				}
			}
			
		} catch (Exception e) {
			
			e.printStackTrace();
		
		}
		
		return null;
		
	}
	
	private JSONObject processUploadZip(FileItemStream stream) {
		JSONObject retObj = new JSONObject();
		JSONArray retArray = new JSONArray();
		retObj.put("cmd", "UNZIP_EVENT_BANNER");
		retObj.put("files", retArray);
		
		String directory = Call.getProperty("temp.image.upload.path");
		
		try (ZipInputStream zis = new ZipInputStream(new BufferedInputStream(stream.openStream()), Charset.forName("euc-kr"))) {
			ZipEntry entry = null;
			
			while ((entry = zis.getNextEntry()) != null) {
				File file = new File(directory, entry.getName());

				if (entry.isDirectory()) {
					file.mkdirs();
				} else {
	                int size;
	                byte[] b = new byte[2048];
	                
					try (
						FileOutputStream fos = new FileOutputStream(file);
						BufferedOutputStream bos = new BufferedOutputStream(fos, b.length)
					) {
	                    while ((size = zis.read(b, 0, b.length)) != -1) {
	                        bos.write(b, 0, size);
	                    }
	                    bos.flush();
					} catch (Exception e) {
						e.printStackTrace();
					}
					retArray.put(processUploadImage(file));
				}
			}
		} catch (Throwable e) {
			e.printStackTrace();
		}
		return retObj;
	}
	
	private JSONObject processUploadImage(File originFile) {
		JSONObject retObj = new JSONObject();
		retObj.put("cmd", "FILE_UPLOAD");

		try {
			
			String fileUploadPath = Call.getProperty("temp.image.upload.path");

			String uuid = UUID.randomUUID().toString();
			String[] uuids = uuid.split("-");
			String directory0 = uuids[0].substring(0, 2);
			String directory1 = uuids[1].substring(0, 2);
			String directory2 = uuids[2].substring(0, 2);
			String directory3 = uuids[3].substring(0, 2);
			String directory4 = uuids[4].substring(0, 2);

			InputStream in = new FileInputStream(originFile);

			String targetDirectory = String.format(
					"%s/%s/%s/%s/%s/%s/",
					fileUploadPath,
					directory0, directory1, directory2, directory3, directory4
			);

			File tgrDir = new File(targetDirectory);
			tgrDir.mkdirs();
			String fileExt = originFile.getName().substring(originFile.getName().length() - 3);
			fileUploadPath = targetDirectory + uuid + "." + fileExt;

			File file = new File(fileUploadPath);
			OutputStream outputStream = new FileOutputStream(file);
			int length = 0;
			byte[] bytes = new byte[1024];

			while ((length = in.read(bytes)) != -1) {
				outputStream.write(bytes, 0, length);
			}

			BufferedImage bi = ImageIO.read(file);
			retObj.put("fileName", originFile.getName());
			retObj.put("uuid", uuid);
			retObj.put("saveName", uuid + "." + fileExt);
			retObj.put("contentType", Files.probeContentType(file.toPath()));
			retObj.put("fullPath", fileUploadPath);
			retObj.put("upload", "success");
			retObj.put("message", "none");
			retObj.put("width", bi.getWidth());
			retObj.put("height", bi.getHeight());

			outputStream.close();
			in.close();

		} catch (Exception caught) {
			caught.printStackTrace();
			retObj.put("fileName", "none");
			retObj.put("uuid", "none");
			retObj.put("saveName", "none");
			retObj.put("contentType", "none");
			retObj.put("fullPath", "none");
			retObj.put("upload", "fail");
			retObj.put("message", caught.getMessage());
			retObj.put("width", "none");
			retObj.put("height", "none");
		}

		return retObj;
	}
	
	private JSONObject processUploadImage(FileItemStream stream) {
		
		JSONObject retObj = new JSONObject();
		JSONArray retArray = new JSONArray();
		retObj.put("cmd", "FILE_UPLOAD");
		retObj.put("files", retArray);

		try {
			
			String fileUploadPath = Call.getProperty("temp.image.upload.path");
			
			if (stream != null) {

				String uuid = UUID.randomUUID().toString();
				String[] uuids = uuid.split("-");
				String directory0 = uuids[0].substring(0, 2);
				String directory1 = uuids[1].substring(0, 2);
				String directory2 = uuids[2].substring(0, 2);
				String directory3 = uuids[3].substring(0, 2);
				String directory4 = uuids[4].substring(0, 2);

				FileItemStream fileItem = stream;

				InputStream in = fileItem.openStream();

				String targetDirectory = String.format(
						"%s/%s/%s/%s/%s/%s/",
						fileUploadPath,
						directory0, directory1, directory2, directory3, directory4
				);

				File tgrDir = new File(targetDirectory);
				tgrDir.mkdirs();
				String fileExt = fileItem.getName().substring(fileItem.getName().length() - 3);
				fileUploadPath = targetDirectory + uuid + "." + fileExt;

				JSONObject fileObj = new JSONObject();
				fileObj.put("fileName", fileItem.getName());
				fileObj.put("uuid", uuid);
				fileObj.put("saveName", uuid + "." + fileExt);
				fileObj.put("contentType", fileItem.getContentType());
				fileObj.put("fullPath", fileUploadPath);
				retArray.put(fileObj);

				File file = new File(fileUploadPath);
				OutputStream outputStream = new FileOutputStream(file);

				int length = 0;
				byte[] bytes = new byte[1024];

				while ((length = in.read(bytes)) != -1) {
					outputStream.write(bytes, 0, length);
				}

				fileObj.put("upload", "success");
				fileObj.put("message", "none");

				outputStream.close();

				in.close();
				
				
			}

		} catch (Exception caught) {
			caught.printStackTrace();
			JSONObject fileObj = new JSONObject();
			fileObj.put("fileName", "none");
			fileObj.put("uuid", "none");
			fileObj.put("saveName", "none");
			fileObj.put("contentType", "none");
			fileObj.put("fullPath", "none");
			fileObj.put("upload", "fail");
			fileObj.put("message", caught.getMessage());
			retArray.put(fileObj);

		}

		return retObj;
	}

	private JSONObject processUploadXls(FileItemStream stream) {
		
		JSONObject retObj = new JSONObject();
		JSONArray retArray = new JSONArray();
		retObj.put("cmd", "FILE_UPLOAD_XLS");
		retObj.put("files", retArray);

		try {
			String fileUploadPath = Call.getProperty("temp.xls.upload.path");
			if (stream != null) {
				String uuid = UUID.randomUUID().toString();
				String[] uuids = uuid.split("-");
				String directory0 = uuids[0].substring(0, 2);
				String directory1 = uuids[1].substring(0, 2);
				String directory2 = uuids[2].substring(0, 2);
				String directory3 = uuids[3].substring(0, 2);
				String directory4 = uuids[4].substring(0, 2);

				FileItemStream fileItem = stream;

				InputStream in = fileItem.openStream();

				String targetDirectory = String.format(
						"%s/%s/%s/%s/%s/%s/",
						fileUploadPath,
						directory0, directory1, directory2, directory3, directory4
				);

				File tgrDir = new File(targetDirectory);
				tgrDir.mkdirs();
				
				String fileExt = fileItem.getName().substring(fileItem.getName().length() - 3);
				
				if(fileExt.equals("lsx")) {
					fileExt = fileItem.getName().substring(fileItem.getName().length() - 4);
				}
				
				fileUploadPath = targetDirectory + uuid + "." + fileExt;

				JSONObject fileObj = new JSONObject();
				fileObj.put("fileName", fileItem.getName());
				fileObj.put("saveName", uuid + "." + fileExt);
				fileObj.put("contentType", fileItem.getContentType());
				fileObj.put("fullPath", fileUploadPath);
				retArray.put(fileObj);

				File file = new File(fileUploadPath);
				OutputStream outputStream = new FileOutputStream(file);

				int length = 0;
				byte[] bytes = new byte[1024];

				while ((length = in.read(bytes)) != -1) {
					outputStream.write(bytes, 0, length);
				}

				outputStream.close();

				in.close();
			}

		} catch (Exception caught) {
			caught.printStackTrace();
		}

		return retObj;
	}

	@SuppressWarnings("unchecked")
	private Class<Module> getClass(String commandId) throws ClassNotFoundException {
		XPathFactory xpfac = XPathFactory.instance();
		String xPathBusiness = String.format("//business[@id='%s']", commandId);
		XPathExpression<Element> xp = xpfac.compile(xPathBusiness, Filters.element());
		Element el = (Element) xp.evaluateFirst(bizDocument);
		String targetClassValue = el.getValue();
		if (targetClassValue != null) {
			Class<?> targetClass = Class.forName(targetClassValue);
			Class<Module> moduleClass = (Class<Module>) Class.forName(targetClassValue);
			return moduleClass;
		}else {
			return null;
		}
	}
	
}