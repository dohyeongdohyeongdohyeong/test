package kr.or.visitkorea.admin.server.application.modules.monitoring;

import org.json.JSONObject;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

import kr.or.visitkorea.admin.server.application.Call;
import kr.or.visitkorea.admin.server.application.modules.AbstractModule;
import kr.or.visitkorea.admin.server.application.modules.BusinessMapping;


@BusinessMapping(id="MONITORING_PLAY")
public class MonitoringPlay extends AbstractModule {
		
	public void run(JSONObject resultHeaderObject, JSONObject resultBodyObject) {

		 String username = Call.getProperty("monitor.server.Id");
	        String host = Call.getProperty("monitor.server.ip");
	        int port = 22;
	        String password = Call.getProperty("monitor.server.Pw");
	        
	        System.out.println("==> Connecting to" + host);
	        Session session = null;
	        Channel channel = null;
	     
	        // 2. 세션 객체를 생성한다 (사용자 이름, 접속할 호스트, 포트를 인자로 준다.)
	        try {
	            // 1. JSch 객체를 생성한다.
	            JSch jsch = new JSch();
	            session = jsch.getSession(username, host, port);
	         
	            
	            // 3. 패스워드를 설정한다.
	            session.setPassword(password);
	         
	            // 4. 세션과 관련된 정보를 설정한다.
	            java.util.Properties config = new java.util.Properties();
	            // 4-1. 호스트 정보를 검사하지 않는다.
	            config.put("StrictHostKeyChecking", "no");
	            session.setConfig(config);
	         
	            // 5. 접속한다.
	            session.connect();
	         
	            // 6. sftp 채널을 연다.
	            channel = session.openChannel("exec");
	         
	            
	            // 8. 채널을 SSH용 채널 객체로 캐스팅한다
	            ChannelExec channelExec = (ChannelExec) channel;
	         
	            System.out.println("==> Connected to" + host);
	            
	            channelExec.setCommand("cd systemMonitor/; nohup ./run.sh &");
	            channelExec.connect();
	            
	            System.out.println("==> Connected to" + host);
	      
	        } catch (JSchException e) {
	            e.printStackTrace();
	        } finally {
	            if (channel != null) {
	                channel.disconnect();
	            }
	            if (session != null) {
	                session.disconnect();
	            }
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
