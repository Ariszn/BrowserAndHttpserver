package 源代码.服务器;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.Calendar;
import java.util.Date;

public class serverlog {
	File serverlog;
	static FileOutputStream fout;
	public serverlog(String file) throws IOException {
		serverlog=new File(file);
		fout=new FileOutputStream(serverlog,true);
		if(!serverlog.exists()) {
			System.out.println("new");
			serverlog.createNewFile();}
	}
	public void record(InetSocketAddress inetAddress,String method,String uri) throws IOException {
		try {
			 Calendar calendar = Calendar.getInstance();
			 Date time = calendar.getTime();
			 StringBuilder sb=new StringBuilder();
			 /*
			  * 
			  * Date:
			  * IP+name	get/head/put url 
			  * */
			 sb.append(time).append(":\n");
			 sb.append(inetAddress.getAddress().getHostAddress()).append(" ");
			 sb.append("from port:").append(inetAddress.getPort()).append(" ");
			 sb.append(method).append(" ").append(uri).append("\n");
			 fout.write(sb.toString().getBytes());
		 }catch(Exception e) {
			 fout.write(("error:\r\n"+e.getMessage()+"\r\n").getBytes());
		 }
	}
}
