package 源代码.服务器;


/*
 * copyright Ari

 * */
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.InetSocketAddress;
import java.net.URI;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set; 
import java.util.concurrent.Executors;

import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLServerSocketFactory;

import com.sun.net.httpserver.Headers; 
import com.sun.net.httpserver.HttpExchange; 
import com.sun.net.httpserver.HttpHandler; 
import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.spi.HttpServerProvider;
import com.sun.net.ssl.KeyManagerFactory;
import com.sun.net.ssl.SSLContext; 

public class httpsserver {   
	private static String src=".\\src\\server";
	private static serverlog log;
	public static final int PORT = 8888;
	private static String logpath=".\\src\\server\\log.txt";
	private static Cache cache;
	private static Map<String , byte[]> dm = new HashMap<>();
    	public static void main(String[] args) throws IOException {
    	log=new serverlog(logpath);
        HttpServerProvider provider = HttpServerProvider.provider();  
        HttpServer httpserver =provider.createHttpServer(new InetSocketAddress(PORT), 100);//
        httpserver.createContext("/", new MyResponseHandler());   
        httpserver.setExecutor(Executors.newCachedThreadPool());  
        httpserver.start();  
        System.out.println("server started");  
    }
   /* public void buildssl() throws Exception {
    	SSLContext ct  = SSLContext.getInstance("SSL");
        KeyManagerFactory keyma = KeyManagerFactory.getInstance("SunX509");
        KeyStore keystore = KeyStore.getInstance("JKS");
        char[] password = "123456".toCharArray();
        keystore.load(new FileInputStream("test.keystore"), password);
        keyma.init(keystore, password);
        ct.init(keyma.getKeyManagers(), null, null);
        Arrays.fill(password, '0');
        SSLServerSocketFactory ssfa = ct.getServerSocketFactory();
        SSLServerSocket server = (SSLServerSocket)ssfa.createServerSocket(PORT);
        server.setNeedClientAuth(false);
        String[] support = server.getSupportedCipherSuites();
        String[] anon = new String[support.length];
        int num = 0;
        for(int i = 0; i < support.length; i++) {
            if (support[i].indexOf("_anon_") > 0) {
                anon[num++] = support[i];
            }
        }
        String[] o = server.getEnabledCipherSuites();
        String[] n = new String[o.length + num];
        System.arraycopy(o, 0, o, 0, n.length);
        System.arraycopy(anon, 0, n, o.length, num); 
        server.setEnabledCipherSuites(n);
    }*/
    public static class MyResponseHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange httpExchange) throws IOException {
        	cache=new Cache(httpExchange);
        	cache.putToMap();
            dm = cache.getData();
        	System.out.println("receive");
            String requestMethod = httpExchange.getRequestMethod(); 
            log.record(httpExchange.getRemoteAddress(),requestMethod,httpExchange.getRequestURI().toString());
            System.out.println("收到新请求:"+requestMethod); 
            if(requestMethod.equalsIgnoreCase("GET")){
                Headers responseHeaders = httpExchange.getResponseHeaders();
                responseHeaders.set("Content-Type", "text/html;charset=utf-8");
                URI path=httpExchange.getRequestURI();
                String pathe=".\\src\\server\\test1.html";
				if(!path.toString().equals("/"))
				{
						String tmp=path.toString().replace("/", "\\");
						pathe=src+tmp;
						System.out.println(pathe);
				}
				File file=new File(pathe);
				if(file.exists()) {
					String type=file.getName();
					type=type.substring(type.lastIndexOf(".")+1,type.length());
		        	String MIME=null;
		        	System.out.println("请求的类型:"+type);
		        	if (type.equals("html")||type.equals("htm"))
		        		MIME="text/html";
		        	else if(type.equals("jpg"))
		        		MIME="image/jpeg";
		        	else if(type.equals("css"))
		        		MIME="application/xml";
		        	else if(type.equals("png"))
		        		MIME="image/png";
		        	else if(type.equals("js"))
		        		MIME="application/javascript";
		        	else if(type.equals("txt"))
		        		MIME="text/html";
		        	else
		        		MIME="unknowntype";
		        	System.out.println("MIME:"+MIME);
		        	responseHeaders.set("Content-Type", MIME); 
		        	String cookie = httpExchange.getRequestHeaders().getFirst("Cookie");
		        	Date now=new Date();
		        	if(cookie == null)
		        		responseHeaders.add("Set-Cookie", "ssid=testtestcookie;lasttime="+now);
		        	FileInputStream fis=new FileInputStream(file); 
		        	httpExchange.sendResponseHeaders(200,file.length()); 
		        	DataOutputStream responseBody = new DataOutputStream(httpExchange.getResponseBody());
		        	byte[] bytes=new byte[fis.available()];
		    		fis.read(bytes);
		    		responseBody.write(bytes);
		    		fis.close();
		    		responseBody.close();
		    		
									}
				else 
        		{
					httpExchange.sendResponseHeaders(404, 0);
        		}
				
                
            }
            else if(requestMethod.equalsIgnoreCase("HEAD")){
          
            	Headers responseHeaders = httpExchange.getResponseHeaders();
            	
                responseHeaders.set("Content-Type", "text/html;charset=utf-8");
                URI path=httpExchange.getRequestURI();
                String pathe=".\\src\\server\\test.html";
				if(!path.toString().equals("/"))
				{
						String tmp=path.toString().replace("/", "\\");
						pathe=src+tmp;
				}
				
				File file=new File(pathe);
				if(file.exists()) {
					String type=file.getName();
					type=type.substring(type.lastIndexOf(".")+1,type.length());
		        	String MIME=null;
		        	if (type.equals("html"))
		        		MIME="text/html";
		        	else if(type.equals("jpg"))
		        		MIME="image/jpeg";
		        	responseHeaders.set("Content-Type", MIME); 
		        	responseHeaders.set("Content-Length",String.valueOf(file.length())); 
		        	String cookie = httpExchange.getRequestHeaders().getFirst("Cookie");
		        	Date now=new Date();
		        	if(cookie == null)
		        		responseHeaders.add("Set-Cookie", "ssid=testtestcookie;lasttime="+now);
		        	java.util.Map<String, List<String>> header=httpExchange.getRequestHeaders();
		        	Set<String> keys2 = header.keySet();
		    		for( String key : keys2 ){
		                String val = httpExchange.getRequestHeaders().getFirst(key);
		                	if(key!=null)
		                		System.out.println(key+":"+val);
		                	else
		                		System.out.println(val);
		            }
		        	httpExchange.sendResponseHeaders(200,-1); 
		        	
				}
				else {
					
					httpExchange.sendResponseHeaders(404, 0);
				}
				}
            
				else if (requestMethod.equalsIgnoreCase("POST")) 
		        {
		        	Headers responseHeaders = httpExchange.getResponseHeaders();
		            responseHeaders.set("Content-Type", "text/plain");
		            BufferedReader in=new BufferedReader(new InputStreamReader(httpExchange.getRequestBody()));
		     
		            String cookie = httpExchange.getRequestHeaders().getFirst("Cookie");
		            String response=null;
		            Date now=new Date();
		        	if(cookie == null)
		        	{	
		        	
		            String body=in.readLine();
		            if(!body.contains("&")&&!body.contains("=")) {
		            	 response="input Error!";
		            }
		            else {
		            String uname=body.substring(body.indexOf("=")+1,body.indexOf("&"));
		            String pwd=body.substring(body.lastIndexOf("=")+1,body.length());
		            response="Error!";
		        	if(uname.equals("Admin")&&pwd.equals("123456")) 
		        	{	response="Corrected info!";
		        		responseHeaders.add("Set-Cookie", "ssid=testtestcookie;lasttime="+now);
		        		}
		        	}
		        	in.close();}
		        	else {
		        		response="Welcome back!";
		        	}
		        	httpExchange.sendResponseHeaders(200,response.getBytes().length);
		        	OutputStream responseBody = httpExchange.getResponseBody();
		        	OutputStreamWriter writer = new OutputStreamWriter(responseBody, "UTF-8");
		        	writer.write(response);
		        	writer.close();
		        }

        }
    }
}
