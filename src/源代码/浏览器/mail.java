package 源代码.浏览器;
//此类实现发送邮件的功能
import java.awt.AWTEvent;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import java.util.Date;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.sun.mail.util.MailSSLSocketFactory;


public class mail extends JFrame implements ActionListener {
		/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
		//邮箱地址、授权码
		String useraddress = "",password = "";
		//定义组件
		JPanel cp,tp=new JPanel(),bp=new JPanel();
		//名称、地址
		JLabel mailaddress=new JLabel("    mailaddress     "),
					   auc=new JLabel("Authorization code"),
				receivelabel = new JLabel("Recipients"),//收件人
				themelabel =   new JLabel("Subject   "),//主题
				contentlabel = new JLabel("text");//正文
		JTextField emailaddress=new JTextField(20),//地址文本框
				auctext=new JTextField(20),//密码文本框
				receivetext = new JTextField(15),//收件人文本框
				theme = new JTextField(15);//标题文本框
		JEditorPane content = new JEditorPane();
		//确定、取消按钮
		JButton loginbutton=new JButton("login"),Sendbutton=new JButton("send");
	public mail() {
		setLocationRelativeTo(null);//窗口默认位于屏幕中央
		setResizable(false);//不可改变大小
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);//隐藏窗口，不关闭程序
		this.enableEvents(AWTEvent.WINDOW_EVENT_MASK);
		this.setSize(new Dimension(400,200));
		this.setTitle("mail");
		//添加组件

		tp.add(mailaddress);
		tp.add(emailaddress);
		emailaddress.setEnabled(true);
		emailaddress.setFocusable(true);
		
		tp.add(auc);
		tp.add(auctext);
		auctext.setEditable(true);
		
		bp.add(loginbutton);
		loginbutton.setFocusable(false);
		Sendbutton.setFocusable(false);
		
		cp=(JPanel)this.getContentPane();
		cp.setLayout(new BorderLayout());
		tp.setLayout(new FlowLayout());
		bp.setLayout(new FlowLayout());
		
		cp.add(tp,BorderLayout.CENTER);
		cp.add(bp,BorderLayout.SOUTH);
		
		loginbutton.addActionListener(this);
		Sendbutton.addActionListener(this);
	}
	@SuppressWarnings("deprecation")
	public void start()
	{
		this.show();
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if(e.getSource()==loginbutton) {
			useraddress=emailaddress.getText();
			password=auctext.getText();
			if(useraddress.equals("")||password.equals("")) {
				JOptionPane.showMessageDialog(null, "Cantnot be empty!","error",JOptionPane.ERROR_MESSAGE);
				return;
			}
			cp.removeAll();//更新界面
			emailaddress.setVisible(false);
			auctext.setVisible(false);
			mailaddress.setVisible(false);
			auc.setVisible(false);
			//隐藏上述标签
			this.setSize(new Dimension(280,400));//改变尺寸
			//移除输入框
			tp.removeAll();
			tp.add(receivelabel);
			tp.add(receivetext);
			tp.add(themelabel);
			tp.add(theme);
			tp.add(content);
			content.setSize(200, 150);
			cp.add(tp);
			cp.add(Sendbutton,BorderLayout.SOUTH);
		}
		else if(e.getSource()==Sendbutton) {
			SendEmail one=new SendEmail(useraddress,password);
			one.send(receivetext.getText(), theme.getText(), content.getText());
		}
		
	}
}
class SendEmail {
	private String account="",Auc="",server="smtp.qq.com",port="465",protocol="smtp";
	//登陆账号，授权码，服务器地址，端口，协议
	//授权类
	static class MyAuthenricator extends Authenticator {
        String u,p;
        public MyAuthenricator(String u,String p){  
            this.u=u;  
            this.p=p;  
        }
        @Override  
        protected PasswordAuthentication getPasswordAuthentication() {
            return new PasswordAuthentication(u,p);
        }  
    }
	public SendEmail(String account,String Auc){
		this.account=account;this.Auc=Auc;
		
	}
	public void send(String address,String subject,String text) {
		Properties prop = new Properties();
		prop.setProperty("mail.transport.protocol", protocol);//协议
        prop.setProperty("mail.smtp.host", server);//服务器
        prop.setProperty("mail.smtp.port", port);//端口
        prop.setProperty("mail.smtp.auth", "true");//使用smtp身份验证
        MailSSLSocketFactory sf = null;//
        try {
            sf = new MailSSLSocketFactory();
            sf.setTrustAllHosts(true);
        } catch (GeneralSecurityException e1) {
            e1.printStackTrace();
        }
        prop.put("mail.smtp.ssl.enable", "true");
        prop.put("mail.smtp.ssl.socketFactory", sf);
        Session session = Session.getDefaultInstance(prop, new MyAuthenricator(account,Auc));
        session.setDebug(true);
        try
        {
        	MimeMessage message = new MimeMessage(session);
        	// Set From: 头部头字段
        	message.setFrom(new InternetAddress(account,"Ari"));
            // Set To: 头部头字段
            message.addRecipient(Message.RecipientType.TO,
             new InternetAddress(address));
            // Set Subject: 头部头字段
            message.setSubject(subject);
            message.setSentDate(new Date());//时间
            Multipart multpart = new MimeMultipart();//容器类，可以包含多个MimeBodyPart对象
            MimeBodyPart body = new MimeBodyPart();//MimeBodyPart可以包装文本，图片，附件
            body.setContent(text, "text/html; charset=UTF-8");//HTML正文
            multpart.addBodyPart(body);
            message.setContent(multpart);;
            Transport.send(message);
            JOptionPane.showMessageDialog(null, "Have sent the email!","Succeed!",
            		JOptionPane.INFORMATION_MESSAGE);
            
        } catch (javax.mail.MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
