package 源代码.浏览器;
/*
 * 大作业浏览器v2.0版本
 * copyright Ari
 * 采用jxbrowser包chrome内核实现
 * ps:jxbrowser简直不要太方便
 * pss:jxbrowser是破解版
 * 
 */

import com.teamdev.jxbrowser.chromium.*;
import com.teamdev.jxbrowser.chromium.events.*;
import com.teamdev.jxbrowser.chromium.swing.BrowserView;
import javax.swing.*;
import java.awt.*;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.math.BigInteger;
public class windows  {
	static String homepage="https://www.baidu.com";
	static Browser browser;
	static BrowserView browserview;
	static JFrame frame;
	static ToolAndMenu tam;
	static bookmarker bmk;
	static logforbrowse lfb;
	// jxbrowser包破解
    static {
        try {
            Class claz = null;
            //6.21版本破解 
            claz =  Class.forName("com.teamdev.jxbrowser.chromium.ba");
            Field e = claz.getDeclaredField("e");
            Field f = claz.getDeclaredField("f");
            e.setAccessible(true);
            f.setAccessible(true);
            Field modifersField = Field.class.getDeclaredField("modifiers");
            modifersField.setAccessible(true);
            modifersField.setInt(e, e.getModifiers() & ~Modifier.FINAL);
            modifersField.setInt(f, f.getModifiers() & ~Modifier.FINAL);
            e.set(null, new BigInteger("1"));
            f.set(null, new BigInteger("1"));
            modifersField.setAccessible(false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public windows() {
    	JPopupMenu.setDefaultLightWeightPopupEnabled(false);
    	browser=new Browser();
    	browserview = new BrowserView(browser);
    	frame = new JFrame("Jme");
    	//退出关闭
    	frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    	//jxbrowser组件添加
    	frame.add(browserview, BorderLayout.CENTER);
    	//构建bmk
    	bmk=new bookmarker(browser);
    	//添加工具栏菜单栏
    	tam=new ToolAndMenu(browser,frame);
    	//构建日志模块
    	lfb=new logforbrowse();
    	//设置尺寸
    	frame.setSize(700,600);
    	//设置居中
    	frame.setLocationRelativeTo(null);
    	//设置默认全屏显示
    	frame.setExtendedState(Frame.MAXIMIZED_BOTH);
        //显示窗口
        frame.setVisible(true);
        this.buildwindows();
}
    //这个方法进行一些类的重写和监听函数
    public void buildwindows() {
    	
    	//在当前窗口打开url
    	browser.setPopupHandler(new PopupHandler() {
            @Override
            public PopupContainer handlePopup(PopupParams popupParams) {
                browser.loadURL(popupParams.getURL());
                return null;
            }
        });
    	 browser.addLoadListener(new LoadAdapter() {
             @Override
             public void onStartLoadingFrame(StartLoadingEvent event) {
                 super.onStartLoadingFrame(event);
             }
             @Override
             public void onProvisionalLoadingFrame(ProvisionalLoadingEvent event) {
                 super.onProvisionalLoadingFrame(event);
             }
             @Override
             public void onFinishLoadingFrame(FinishLoadingEvent event) {
                 super.onFinishLoadingFrame(event);
                //地址栏显示当前url
                 tam.tl.jtf.setText(browser.getURL());
                 //设置前进后退按钮的规则
                 if (browser.canGoBack())
                	 tam.tl.back.setEnabled(true);
                 else
                	 tam.tl.back.setEnabled(false);
                 if (browser.canGoForward())
                	 tam.tl.forward.setEnabled(true);
                 else
                	 tam.tl.forward.setEnabled(false); 
                 frame.setTitle(browser.getTitle());
                 lfb.record(browser.getURL(),browser.getTitle());
             }
     });
                 

 }

    public static void main(String args[]) {
		windows one=new windows();
        browser.loadURL(homepage);
        tam.tl.back.setEnabled(false);
        tam.tl.forward.setEnabled(false);
    }
	
 
}
    	 
