package 源代码.浏览器;

import java.awt.BorderLayout;
import java.awt.Image;
import java.awt.Panel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JTextField;
import com.teamdev.jxbrowser.chromium.Browser;


//此类实现菜单栏与工具栏

public class ToolAndMenu {
	public Browser browser;
	public JFrame frame;
	public tool tl;
	public menu mu;
	private static final ImageIcon iconforward=new ImageIcon(".\\src\\ui\\icons\\forward.png");
	private static final ImageIcon iconback=new ImageIcon(".\\src\\ui\\icons\\back.png");
	private static final ImageIcon iconflash=new ImageIcon(".\\src\\ui\\icons\\flash.png");
	private static final ImageIcon iconhome=new ImageIcon(".\\src\\ui\\icons\\home.png");
	private static final ImageIcon iconmark=new ImageIcon(".\\src\\ui\\icons\\tag.png");
	public ToolAndMenu(Browser browser,JFrame frame) {
		iconforward.setImage(iconforward.getImage().getScaledInstance(20, 20,Image.SCALE_DEFAULT));
		iconback.setImage(iconback.getImage().getScaledInstance(20, 20,Image.SCALE_DEFAULT));
		iconhome.setImage(iconhome.getImage().getScaledInstance(20, 20,Image.SCALE_DEFAULT));
		iconflash.setImage(iconflash.getImage().getScaledInstance(20, 20,Image.SCALE_DEFAULT));
		iconmark.setImage(iconmark.getImage().getScaledInstance(20, 20,Image.SCALE_DEFAULT));
		this.browser=browser;
		this.frame=frame;
		tl=new tool();
		tl.buildtool();
		mu=new menu();
		mu.buildmenu();
	}
	class menu{//菜单类 里面有： 书签 下载 邮件 历史记录
		public JMenu bookmark,download,mail;
		public JMenuBar jmb;
		public JMenuItem downloader,save,email,openmark;
		public menu() {
			bookmark=new JMenu("书签");
			download=new JMenu("下载");
			mail=new JMenu("邮件");
			downloader=new JMenuItem("打开下载");
			save=new JMenuItem("保存当前网页");
			email=new JMenuItem("发送邮件");
			openmark=new JMenuItem("打开书签管理器");
	        jmb=new JMenuBar();
		}
		public void buildmenu() {
			download.add(downloader);
			download.add(save);
			mail.add(email);
			bookmark.add(openmark);
			jmb.add(bookmark);
			jmb.add(download);
			jmb.add(mail);
			frame.setJMenuBar(jmb);
			buildlinster();
		}
		public void buildlinster() {
			openmark.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					windows.bmk.start();
				}
			});
			downloader.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					Downloader dl=new Downloader();
					dl.start(browser.getURL());
				}
			});
			save.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					Downloader dl=new Downloader();
					dl.downloadpage(browser.getURL(), browser.getTitle());
				}
			});
			email.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					mail ml=new mail();
					ml.start();
				}
			});
		}
	}
	class tool{//工具栏类 里面有：五个按钮+地址栏
		public JButton back,forward,flash,home,go,hoshi;
		public JTextField jtf;
		public JLabel jlb;
		public Panel panel;
		public tool(){
			//构建五个按钮
			back=new JButton(iconback);
			forward=new JButton(iconforward);
			flash=new JButton(iconflash);
			home=new JButton(iconhome);
			go=new JButton(iconforward);
			hoshi=new JButton(iconmark);
			back.setContentAreaFilled(false);
			forward.setContentAreaFilled(false);
			flash.setContentAreaFilled(false);
			home.setContentAreaFilled(false);
			go.setContentAreaFilled(false);
			hoshi.setContentAreaFilled(false);
			jtf=new JTextField(60);
			jlb=new JLabel("地址:");
			panel=new Panel();
			
		}
		public void buildtool() {
			panel.add(back);
			panel.add(forward);
			panel.add(flash);
			panel.add(home);
			panel.add(jlb);
			panel.add(jtf);
			panel.add(go);
			panel.add(hoshi);
			frame.add(panel,BorderLayout.NORTH);
			//默认状态不可用
			back.setEnabled(false);
			forward.setEnabled(false);
			buildlinster();
		}
		public void buildlinster()
		{	//按回车直接搜索
			jtf.addKeyListener(new KeyAdapter() {
				public void keyTyped(KeyEvent e) {
					if(e.getKeyChar()==KeyEvent.VK_ENTER) {
						go.doClick();
					}
				}
			});
			hoshi.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					windows.bmk.addmark(browser.getURL(), browser.getTitle());
				}
			});
			back.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					browser.goBack();
				}
			});
			forward.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					browser.goForward();
				}
			});
			flash.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					browser.reload();
				}
			});
			home.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					browser.loadURL(windows.homepage);
				}
			});
			go.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					String web=jtf.getText();
					if(CheckURL.isURL(web))
						browser.loadURL(web);
					else
					{
						web="http://www.baidu.com/s?wd="+web;
						browser.loadURL(web);
					}
					
				}
			});
			
		}
	}
}
