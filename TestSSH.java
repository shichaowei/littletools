package com.tairanchina.FengDai.test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import ch.ethz.ssh2.Connection;
import ch.ethz.ssh2.Session;
 

public class TestSSH {
    
    public static void main(String[] args)
    {
    	/*String checktomcat="ps -ef | grep tomcat| grep -v grep |grep -v cmsdeploy | grep -v killpro \n";
    	String starttomcat="/root/script/shell/cmsdeploy.sh 2017/09/02 23:59:35 \n";
    	remoteRunCmd(starttomcat);
    	while(remoteRunCmd(checktomcat).equals("")){
    		remoteRunCmd(starttomcat);
    	}*/
//    	String ipaddress[]={"172.30.251.181","172.30.249.243","172.30.250.25","172.30.251.190","172.30.249.242"};
//    	for(int i=0;i<ipaddress.length;i++){    		
//    		remoteRunCmd(ipaddress[i], "root", "Taihe123", "date -s '2016/8/15 20:31:50'");
//    	}

    	String time="2016/09/04 05:29:40";
    	String endtime="2016/09/19 05:29:40";
    	改变时间重启tomcat(time, endtime);
    	
//    	String ipaddress[]={"172.30.249.177"};
//    	for(int i=0;i<ipaddress.length;i++){    		
//    		remoteRunCmd(ipaddress[i], "root", "Taihe123", "date -s '2016/8/15 20:31:50'");
//    	}
    	
//    	String dubboaddress[]={"172.30.249.243",};
//    	for(int i=0;i<dubboaddress.length;i++){
//    		try {
//				remoteRunCmd(dubboaddress[i], "root", "Taihe123", "/root/script/restartdubbo.sh");
//				Thread.sleep(30000);
//			} catch (Exception e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//    		
//    	}
  	
    	/*remoteRunCmd("172.30.250.25", "root", "Taihe123", "date -s '2016/12/06 5:29:00'");
    	remoteRunCmd("172.30.251.190", "root", "Taihe123", "date -s '2016/12/06 5:29:00'");
    	remoteRunCmd("172.30.249.242", "root", "Taihe123", "date -s '2016/12/06 5:29:00'");*/
    	
    }
    
    
    public static void 改变时间重启tomcat(String time,String endtime){
    	try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
			Date date = sdf.parse(time);
			long unixTimestamp = date.getTime();
			while(unixTimestamp <= sdf.parse(endtime).getTime()){
				String dateStr = sdf.format(new Date(unixTimestamp));
				System.out.println(dateStr);
				System.out.println("/usr/local/tomcat/bin/jstartviatime.sh "+dateStr);
				remoteRunCmd("172.30.249.177", "root", "Taihe123", "/usr/local/tomcat/bin/jstartviatime.sh "+dateStr);
				Thread.sleep(25000);
				unixTimestamp+=24*3600*1000;
			}
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    public static String remoteRunCmd(String hostname,String username,String password,String  cmd)
    {
            System.out.println(hostname);
            
            Connection conn = new Connection(hostname);
            Session sess = null;
            
            try
            {
               
                conn.connect();
               
                boolean isAuthenticated = conn.authenticateWithPassword(username, password);
                if (isAuthenticated == false)
                        throw new IOException("Authentication failed.");
               
                sess = conn.openSession();
                //sess.execCommand("uname -a && date && uptime && who");
               // sess.execCommand("deleteuser -c 2 -P 123456 -i 10 ");
                // sess.execCommand("listuser -c 9189 -A admin -P 123456  ");
                sess.execCommand( cmd);
                InputStream stdout =    sess.getStdout() ;
                BufferedReader br = new BufferedReader(new InputStreamReader(stdout));
                StringBuilder sb = new StringBuilder();
                while (true)
                {
                    String line = br.readLine();
                    if (line == null)
                        break;
                    sb.append(line);
                    sb.append('\n');
                }
               
                //  System.out.println("ExitCode: " + sess.getExitStatus());
               return sb.toString();
            }catch (Exception e){
                return "";
            }finally{
                
                sess.close();
               
                conn.close();
            }
        }
    
}