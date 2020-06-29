package 源代码.浏览器;
//此类实现多线程下载工具
import java.awt.AWTEvent;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JTextField;

public class Downloader extends JFrame  implements ActionListener{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	JPanel contentpane,textpanel=new JPanel(),buttonpanel=new JPanel(),progressbarpanel=new JPanel();
	JLabel urllabel=new JLabel("URL:"), pathlabel=new JLabel("保存路径:"),filename = new JLabel("文件名称:");
	JTextField urltext=new JTextField(20),pathtext=new JTextField(20),fileurltext = new JTextField(20);
	JButton OKbutton=new JButton("开始"),pausebutton=new JButton("暂停"),Choosebutton=new JButton("选择路径");
	JFileChooser jfc;
	static JProgressBar jpb=new JProgressBar(0,100);
	static boolean stop = false;
	JLabel label = new JLabel("进度：");
	static JLabel label1 = new JLabel("0%");
	String url;
	String name;
	String path;
	String defaultpath=".\\src";
	public Downloader()
	{
		setLocationRelativeTo(null);
		setResizable(false);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.enableEvents(AWTEvent.WINDOW_EVENT_MASK);
		this.setSize(new Dimension(300,200));
		this.setTitle("下载");
		//设置窗体属性
		contentpane=(JPanel)this.getContentPane();
		contentpane.setLayout(new BorderLayout());
		textpanel.setLayout(new FlowLayout());
		textpanel.add(urllabel);
		textpanel.add(urltext);
		urltext.setEnabled(true);
		urltext.setFocusable(true);
		textpanel.add(pathlabel);
		textpanel.add(pathtext);
		pathtext.setEditable(true);
		textpanel.add(filename);
		textpanel.add(fileurltext);
		JButton a=new JButton("           ");
        a.setContentAreaFilled(false);
        a.setBorderPainted(false);
        a.setEnabled(false);
        textpanel.add(a);
		textpanel.add(label);
		textpanel.add(label1);
		JButton c=new JButton("            ");
	        c.setContentAreaFilled(false);
	  	c.setBorderPainted(false);
		c.setEnabled(false);
		textpanel.add(c);
		textpanel.add(jpb);
		buttonpanel.setLayout(new FlowLayout());
		buttonpanel.add(Choosebutton);
		Choosebutton.setFocusable(false);
		buttonpanel.add(OKbutton);
		OKbutton.setFocusable(false);
		buttonpanel.add(pausebutton);
		pausebutton.setFocusable(false);
		//progressbarpanel.setLayout(new FlowLayout());
		//progressbarpanel.add(jpb);
		contentpane.add(textpanel,BorderLayout.CENTER);
		contentpane.add(buttonpanel,BorderLayout.SOUTH);
		//contentpane.add(progressbarpanel,BorderLayout.SOUTH);
		
		OKbutton.addActionListener(this);
		pausebutton.addActionListener(this);
		Choosebutton.addActionListener(this);
	 
 }
	public void start(String url) {
		this.setVisible(true);
		this.urltext.setText(url);
		this.pathtext.setText(defaultpath);
	}
public void actionPerformed(ActionEvent e) {
	if(e.getSource()==this.OKbutton)
	{
		
		if(urltext.getText().equals("")||pathtext.getText().equals("")||fileurltext.getText().equals("")) {
			JOptionPane.showMessageDialog(null, "不能为空！","Tips",JOptionPane.ERROR_MESSAGE);
		}else {
			
				url = urltext.getText();
				path = pathtext.getText();
				name = fileurltext.getText();
				stop = false;
				pausebutton.setText("暂停");
				new Thread(new download(url,path,name)).start();
			}
		}
		
	else if(e.getSource()==this.pausebutton) {
		if(!stop) {
			stop = true;
			pausebutton.setText("继续");
			System.out.println(stop);
		}else {
			stop = false;
			pausebutton.setText("暂停");
			System.out.println(stop);
		}
	}	
	else if(e.getSource()==this.Choosebutton) {
		jfc=new JFileChooser();
		jfc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		int returnVal=jfc.showSaveDialog(null);
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			path = jfc.getSelectedFile().getAbsolutePath();
		}
		pathtext.setText(path);
	}
}
	public void downloadpage(String url,String title)
	{
		try {
		System.out.println(url);
		File file = new File(defaultpath+title+".html");
		HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
		connection.setRequestMethod("GET");
		connection.setConnectTimeout(3 * 1000);
		if (!file.exists())
			file.createNewFile();
		InputStream input = connection.getInputStream();
		FileOutputStream fout = new FileOutputStream(file, true);
		byte[] buffer = new byte[1024];
		int len;
		while((len=input.read(buffer))!=-1)
			fout.write(buffer,0,len);
		fout.close();
		System.out.println(title);
		JOptionPane.showMessageDialog(null, "已经把文件下载到"+file.getPath(),"Tips",JOptionPane.INFORMATION_MESSAGE);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
class download implements Runnable{

	String url=null,savepath=null,name=null;

	public download(String url,String savepath,String name)
	{
		this.url=url;
		this.savepath=savepath;
		this.name=name;
	}
	@Override
	public void run() {
		// TODO Auto-generated method stub
		try {
			File file = new File(savepath+"\\"+name);
			HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
			connection.setRequestMethod("GET");
			connection.setConnectTimeout(3 * 1000);
			long savedlength = 0;
			if (file.exists()) {
				savedlength = file.length();
				JOptionPane.showMessageDialog(null, "文件已存在，开始断点续传！", "Tips",JOptionPane.WARNING_MESSAGE);
                connection.setRequestProperty("Range", "bytes="+ file.length() + "-");//断点续传
            }
			int Responsecode = connection.getResponseCode();
			 if (Responsecode==200||Responsecode==206)//断点续传
			 {
				 long filelength=connection.getContentLength();
				 System.out.println(filelength);
				 filelength+=savedlength;//总长度
				 InputStream input = connection.getInputStream();
				 FileOutputStream fout = new FileOutputStream(file, true);//断点续传
				 byte[] buffer = new byte[1024];
				 int len;
				 while ((len = input.read(buffer)) != -1) {
	                	if(Downloader.stop) 
	                		continue;//暂停
	                	fout.write(buffer,0,len);
	                	savedlength += len;
	                	float jindu=(savedlength* 0.1f/filelength)*1000;
	                	System.out.println(jindu);
	                	Downloader.label1.setText((int)jindu+"%");//进度
	                	Downloader.label1.updateUI();
	                	Downloader.jpb.setValue((int)jindu);
				 }
				 fout.close();
				 JOptionPane.showMessageDialog(null, "下载完成！", "Tips",JOptionPane.WARNING_MESSAGE);
			 }
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	
}

