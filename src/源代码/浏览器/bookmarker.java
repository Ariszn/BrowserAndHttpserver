package 源代码.浏览器;
//此类实现书签管理器
import java.awt.BorderLayout;
import java.util.HashMap;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.WindowConstants;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;

import com.teamdev.jxbrowser.chromium.Browser;

public class bookmarker {
	private final HashMap<String,String> bkhash = new HashMap<String,String>();
	DefaultMutableTreeNode root;
	JScrollPane scrollPane;
	Browser browse;
	JFrame frame;
	JTree tree;
	JPanel panel;
	public bookmarker(Browser browse) {
		this.browse=browse;
		buildroot();
		buildlistener();
	}
	public void buildroot() {
		frame=new JFrame("书签查看");
		//主窗口设置
		frame.setSize(300,600);
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		root=new DefaultMutableTreeNode("书签");
		//树初始化
		tree=new JTree(root);
		tree.setShowsRootHandles(true);
        tree.setEditable(true);
        tree.updateUI();
        tree.addTreeSelectionListener(new TreeSelectionListener() {
			public void valueChanged(TreeSelectionEvent arg0) {
				open();
			}
        });
        //根节点
		
		//滚动条
		scrollPane=new JScrollPane();
		scrollPane.setViewportView(tree);
		//显示面板
		panel=new JPanel(new BorderLayout());
        panel.add(scrollPane, BorderLayout.CENTER);
        frame.setContentPane(panel);
	}
	public void buildlistener() {
		tree.addTreeSelectionListener(new TreeSelectionListener() {
            @Override
            public void valueChanged(TreeSelectionEvent e) {
                System.out.println("当前被选中的节点: " + e.getPath());
            }
        });
	}
	public static void main(String args[]) {
	}
	public void start() {
		frame.setVisible(true);
	}
	public void addmark(String web,String title) {//添加书签函数
		System.out.print(web+"1"+title);
		if(!bkhash.containsKey(title))//判断是否已经存在(title一个可以对应多个web)
		{
			bkhash.put(title,web);//加入string对
			DefaultMutableTreeNode nd=new DefaultMutableTreeNode(title);
			root.add(nd);
			tree.updateUI();
			JOptionPane.showMessageDialog(null, "添加书签成功！","Tips",JOptionPane.INFORMATION_MESSAGE);
		}
		else {
			JOptionPane.showMessageDialog(null, "书签已存在！","Tips",JOptionPane.ERROR_MESSAGE);
		}
	}
	public void open() {
		String selected=((DefaultMutableTreeNode)tree.getLastSelectedPathComponent()).toString();//返回最后选定的节点
		if(selected.equals("书签"))
			return;
		browse.loadURL(bkhash.get(selected));
	}

}
