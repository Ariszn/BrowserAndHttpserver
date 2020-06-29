package 源代码.浏览器;
//此类实现浏览器的日志功能
import java.io.*;
import java.util.*;

public class logforbrowse {
	File log;
	static FileOutputStream fout;
	static OutputStreamWriter out;
	static String filepath=".\\src";//日志文件
	public logforbrowse() {
		try {
		log=new File(filepath);
		fout=new FileOutputStream(log,true);
		out = new OutputStreamWriter(fout,"UTF-8"); 
		if(!log.exists()) log.createNewFile();}
		catch(IOException e){
			e.printStackTrace();
		}
	}
	public void record(String web,String title) {
		StringBuilder sb = new StringBuilder();
		Calendar calendar = Calendar.getInstance();
		Date time = calendar.getTime();
		sb.append(time+" ");
		sb.append(title+" ");
		sb.append(web+" \r\n");
		try {
			out.write(sb.toString());
			out.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
		
}
