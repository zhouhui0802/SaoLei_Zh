package com.zh.Ui;

import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.text.DecimalFormat;

import javax.swing.*;

import com.zh.Entity.Mine;
import com.zh.Util.Utils;

public class MyFrame extends JFrame implements ActionListener,Runnable,MouseListener{
	
	private JLabel mainLab;  //主窗口
	
	private JLabel bar;	//图标
	
	public static int mineNum;				// 地雷计数器
	private int startMineNum; //初始地雷数
	
	private JLabel timeLab,mineLab;  //时间和地雷
	private JButton flush,min,close;
	private JButton style1,style2,style3;
	
	private long start;
	
	private int row,col;  //行列数
	private Mine[][] mines;  //地雷按钮数组
	private JPanel jp; //放置地雷的面板
	private int left,up;  //面板在窗体上的位置
	

	public static final int MINE_SIZE=22;
	
	// 存放不同风格的图片
	private ImageIcon[] mainLabs = {new ImageIcon("./img/back.png"),new ImageIcon("./img/style2/back.png"),new ImageIcon("./img/style3/back.png")};
	
	private ImageIcon[] style1s = {new ImageIcon("./img/style1.png"),new ImageIcon("./img/style2/style1.png"),new ImageIcon("./img/style3/style1.png")};
	private ImageIcon[] style2s = {new ImageIcon("./img/style2.png"),new ImageIcon("./img/style2/style2.png"),new ImageIcon("./img/style3/style2.png")};
	private ImageIcon[] style3s = {new ImageIcon("./img/style3.png"),new ImageIcon("./img/style2/style3.png"),new ImageIcon("./img/style3/style3.png")};
	
	private ImageIcon[] btn_backs = {new ImageIcon("./img/but_back.png"),new ImageIcon("./img/style2/but_back.png"),new ImageIcon("./img/style3/but_back.png")};
	private ImageIcon[] btn_opens = {new ImageIcon("./img/btn_open.png"),new ImageIcon("./img/style2/btn_open.png"),new ImageIcon("./img/style3/btn_open.png")};
	private ImageIcon[] btn_flags = {new ImageIcon("./img/hongqi.png"),new ImageIcon("./img/style2/hongqi.png"),new ImageIcon("./img/style3/hongqi.png")};
	
	private int index=0;
	
	private JMenuBar menuBar;
	private JMenu menu;
	private JCheckBoxMenuItem easy,normal,hard;
	
	public MyFrame(int startMineNum,int row,int col,int left,int up)
	{
		mainLab=new JLabel(mainLabs[index]);
		
		style1=new JButton(style1s[index]);
		style2=new JButton(style2s[index]);
		style3=new JButton(style3s[index]);
		
		bar = new JLabel(new ImageIcon("./img/bar.png"));
		flush = new JButton(new ImageIcon("./img/flush.png"));
		min = new JButton(new ImageIcon("./img/min.png"));
		close = new JButton(new ImageIcon("./img/close.png"));
		
		this.startMineNum = startMineNum;		// 初始雷数量
		mineNum = startMineNum;	// 雷计数器
		timeLab = new JLabel("00:00");
		mineLab = new JLabel(mineNum+"");
		
		this.row = row;		// 19
		this.col = col;		// 15
		this.left = left;	// 85
		this.up = up;		// 100
		mines = new Mine[row][col];
		
		//设置菜单栏
		menuBar=getMyMenuBar();
		//设置面板
		jp=getMinePanel();
		
		//初始化各个控件
		init();
		
		//注册个监听器
		addEventHandler();
		
		Utils.setFrameDrag(this);
		Utils.initFrame(this);
		
	}
	
	//注册监听器
	public void addEventHandler()
	{
		//将当前对象作为监听器
		min.addActionListener(this);
		close.addActionListener(this);
		flush.addActionListener(this);
		
		style1.addActionListener(this);
		style2.addActionListener(this);
		style3.addActionListener(this);
		
		easy.addActionListener(this);
		normal.addActionListener(this);
		hard.addActionListener(this);
		
	}
	
	public JPanel getMinePanel()
	{
		JPanel jp=new JPanel();
		
		//设置网络布局管理器
		jp.setLayout(new GridLayout(row,col));
		
		//创建地雷按钮，添加到数组面板中
		for(int i=0;i<row;i++)
		{
			for(int j=0;j<col;j++)
			{
				mines[i][j]=new Mine(i,j);
				mines[i][j].setIcon(btn_backs[index]);
				mines[i][j].addMouseListener(this);
				jp.add(mines[i][j]);
			}
		}
		
		//随机设置地雷
		layMines();
		
		//设置每个按钮周围的地雷数量   统计的是非雷区的数量
		for(int i=0;i<row;i++)
		{
			for(int j=0;j<col;j++)
			{
				if(mines[i][j].getValue()==0)
				{
					mines[i][j].setValue(calMineNum(i,j));
				}
			}
		}
		
		//测试
		for(int j=0;j<row;j++)
		{
			for(int k=0;k<col;k++)
			{
				System.out.print(mines[j][k].getValue()>=0?"  "+mines[j][k].getValue():" "+mines[j][k].getValue());
			}
			System.out.println();
		}
		
		return jp;
	}
	
	//随机放置地雷
	public void layMines(){
		for(int i=0;i<mineNum;i++)
		{
			int ii=(int)(Math.random()*row);
			int jj=(int)(Math.random()*col);
			if(mines[ii][jj].getValue()==-1)
			{
				i--;
				continue;
			}
			mines[ii][jj].setValue(-1);
		}
	}
	
	//计算周围地雷的数量
	public int calMineNum(int i,int j)
	{
		int num=0;
		for(int k=i-1;k<=i+1;k++)
		{
			for(int k2=j-1;k2<=j+1;k2++)
			{
				if(k>=0&&k2>=0&&k<row&&k2<col&&mines[k][k2].getValue()==-1)
				{
					num++;
				}
			}
		}
		return num;
	}
	
	
	
	private JMenuBar getMyMenuBar()
	{
		JMenuBar m=new JMenuBar();
		m.setOpaque(false);
		m.setBorderPainted(false);
		
		menu=new JMenu();
		menu.setIcon(new ImageIcon("./img/bar.png"));
		m.add(menu);
		
		easy=new JCheckBoxMenuItem("初级",new ImageIcon("./img/1.gif"));
		normal=new JCheckBoxMenuItem("中级",new ImageIcon("./img/2.gif"));
		hard=new JCheckBoxMenuItem("高级",new ImageIcon("./img/3.gif"));
		
		menu.add(easy);
		menu.add(normal);
		menu.add(hard);
		
		return m;
	}
	
	public void init()
	{
		//取消窗体边框
		super.setUndecorated(true);
		super.add(mainLab);
		
		//刷新按钮
		flush.setBounds(716, 16, 31, 31);	// 设置大小 位置
		flush.setBorderPainted(false);		// 取消边框
		flush.setContentAreaFilled(false);	// 取消背景填充
		mainLab.add(flush);					// 按钮添加到mainLab中
		
		//最小化按钮
		min.setBounds(766, 16, 31, 31);		// 最小化按钮
		min.setBorderPainted(false);
		min.setContentAreaFilled(false);
		mainLab.add(min);
		
		//关闭按钮
		close.setBounds(816, 16, 31, 31);	// 关闭退出按钮
		close.setBorderPainted(false);
		close.setContentAreaFilled(false);
		mainLab.add(close);
		
		
		//三种不同风格的按钮设置，宁静，清爽，粉嫩
		style1.setBounds(728, 322, 78, 39);
		style1.setBorderPainted(false);
		style1.setContentAreaFilled(false);
		mainLab.add(style1);
		
		style2.setBounds(728, 390, 78, 39);
		style2.setBorderPainted(false);
		style2.setContentAreaFilled(false);
		mainLab.add(style2);
		
		style3.setBounds(728, 458, 78, 39);
		style3.setBorderPainted(false);
		style3.setContentAreaFilled(false);
		mainLab.add(style3);
		
		//扫雷图标
		menuBar.setBounds(20, 2, 110, 61);
		mainLab.add(menuBar);
		
		// 计时器
		timeLab.setFont(new Font("微软雅黑", Font.BOLD, 20));	
		timeLab.setBounds(736, 129, 100, 60);
		mainLab.add(timeLab);
		
		//地雷数量
		mineLab.setFont(new Font("微软雅黑", Font.BOLD, 20));// 地雷数
		mineLab.setBounds(751, 202, 100, 60);
		mainLab.add(mineLab);
		
		//将地雷按钮添加到面板上
		jp.setBounds(left, up, col*MINE_SIZE, row*MINE_SIZE);// 地雷面板
		mainLab.add(jp);
		
		//开启计时器线程
		new Thread(this).start();
		
		//窗体最小化的图标
		super.setIconImage(new ImageIcon("./img/icon.jpg").getImage());
		
		// 窗体大小自适应
		super.pack();
	}
	
	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		Object source=e.getSource();
		if(!(source instanceof Mine))
		{
			return;
		}
		
		Mine m=(Mine)source;
		if(e.getButton()==MouseEvent.BUTTON1)
		{
			//鼠标左击
			if(m.isFlag()||m.isOpen()==true)  //当已经打开一个或者已经右击
			{
				return;
			}
			
			m.setOpen(true);
					
			//点到的是炸弹
			if(m.getValue()==-1)
			{
				m.setIcon(new ImageIcon("./img/bomb.gif"));
				openAll();   //打开全部
				gameover(); //gameover弹窗
				return;
			}else if(m.getValue()==0)  //点击到的是空格
			{
				m.setIcon(btn_opens[index]);
				openAround(m);
			}else{                                //点到的是数字
				m.setIcon(new ImageIcon("./img/"+m.getValue()+".gif"));
			}
		}else if(e.getButton()==MouseEvent.BUTTON2)
		{
			open9(m);
			
		}else if(e.getButton()==MouseEvent.BUTTON3)
		{
			if (m.isOpen()==true) {
				return;
			}
			// 鼠标右键点击
			
			if (!m.isFlag()) {
				m.setIcon(btn_flags[index]);
				mineNum--;
				m.setFlag(true);
			}else {
				m.setIcon(btn_backs[index]);
				mineNum++;
				m.setFlag(false);
			}
		}
		
		win();
	}
	
	public void win()
	{
   	 int open=0;
		 int flagNum=0;
   	 int dileiNum=0;
		for (int i = 0; i < row; i++) {
			for (int j = 0; j < col; j++) {
				if (mines[i][j].isOpen()) {
					open++;
				}
				if(mines[i][j].isFlag()==true)
				{
					flagNum++;
				}
				if(mines[i][j].getValue()==-1)
				{
					dileiNum++;
				}
			}
		}
		
		System.out.println("open="+open);
		System.out.println("dileiNum="+dileiNum);
		System.out.println("flagNum="+flagNum);
		
		if ((open +flagNum)== row*col&&dileiNum==flagNum) {
			// 弹出对话框 ----位置,内容消息,标题
			//System.out.println("调用成功");
			int showConfirmDialog = JOptionPane.showConfirmDialog(this, "WIN, 是否重新开始", "胜利", JOptionPane.YES_NO_OPTION);
			if (showConfirmDialog == 0) {
				flush();
			}else if (showConfirmDialog == 1) {
				System.exit(0);
			}
		}
	}
	
	public void open9(Mine m)
	{
		int x = m.getRow();
		int y = m.getCol();
		int flag = 0;
		int num = 0;
		
		for (int k = x-1; k <= x+1; k++) {
			for (int k2 = y-1; k2 <= y+1; k2++) {
				if (k<row && k2 <col && k>=0 && k2>=0) {
					if (mines[k][k2].isFlag()) {					
						flag++;          //统计右击旗帜数量
						System.out.println("flag="+flag);
					}
					if (mines[k][k2].getValue()==-1) {
						num++;     //统计实际地雷数量
						System.out.println("num="+num);
					}
				}
			}
		}
		
		if (flag==num) {
			for (int k = x-1; k <= x+1; k++) {
				for (int k2 = y-1; k2 <= y+1; k2++) {
					if (k<row && k2 <col && k>=0 && k2>=0) {
						if (mines[k][k2].isFlag() || mines[k][k2].isOpen()) {
							continue;
						}
						m.setOpen(true);
						if (m.getValue()==-1) {
							m.setIcon(new ImageIcon("./img/bomb.gif"));
							openAll();					// 打开全部
							gameover();					// gameover弹窗
							return;
						} else if (m.getValue()==0) {
							m.setIcon(btn_opens[index]);
						} else {
							m.setIcon(new ImageIcon("./img/"+ m.getValue() +".gif"));
						}
					}
				}
			}
		}
		
		
	}
	
	//打开空格的周围
	private void openAround(Mine m)
	{
		int x=m.getRow();
		int y=m.getCol();
		
		if(m.getValue()==-1)
		{
			return;
		}
		
		for(int k=x-1;k<=x+1;k++)
		{
			for(int k2=y-1;k2<=y+1;k2++)
			{
				if(k<row && k2 <col && k>=0 && k2>=0)
				{
					if (mines[k][k2].isFlag() || mines[k][k2].isOpen()) {
						continue;
					}//如果已经被右击且被打开
					
					if (mines[k][k2].getValue()==-1) {
						continue;
					} else if (mines[k][k2].getValue()==0) {
						mines[k][k2].setOpen(true);
						mines[k][k2].setIcon(btn_opens[index]);
						openAround(mines[k][k2]);
					} else {
						mines[k][k2].setOpen(true);
						mines[k][k2].setIcon(new ImageIcon("./img/"+ mines[k][k2].getValue() +".gif"));
					}
					
				}
			}
		}
	}
	
	
	//游戏结束
	public void gameover()
	{
		int showConfirmDialog=JOptionPane.showConfirmDialog(this, "GAMEOVER, 是否重新开始","失败了", JOptionPane.YES_NO_OPTION);
		if(showConfirmDialog==0)
		{
			flush();
		}else if(showConfirmDialog==1)
		{
			System.exit(0);
		}
	}
	
	
	//当点击到地雷之后，打开所有的按钮
	public void openAll()
	{
		for(int i=0;i<row;i++)
		{
			for(int j=0;j<col;j++)
			{
				openIt(mines[i][j]);
			}
		}
	}
	
	//打开当前的按钮
	public void openIt(Mine m)
	{
		m.setOpen(true);
		if(m.getValue()==-1)
		{
			m.setIcon(new ImageIcon("./img/bomb.gif"));
		}else if(m.getValue()==0)
		{
			m.setIcon(btn_opens[index]);
		}else{
			m.setIcon(new ImageIcon("./img/"+m.getValue()+".gif"));
		}
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		//用于统计你花费了多少时间
		start=System.currentTimeMillis();
		
		DecimalFormat df=new DecimalFormat("00");
		
		while(true)
		{
			long x=System.currentTimeMillis()-start;
			int minute=(int)(x/1000/60);
			int second=(int)(x/1000%60);
			timeLab.setText(df.format(minute)+":"+df.format(second));
			mineLab.setText(mineNum+"");
			
			//休眠
			try{
				Thread.sleep(1000);
			}catch(InterruptedException e)
			{
				e.printStackTrace();
			}
			
			// 判断是否胜利
			if(mineNum==0)
			{
				System.out.println("进入判断函数");
				win();
			}
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		Object source=e.getSource();
		
		if(source==min)
		{
			this.setState(JFrame.ICONIFIED);
		}else if(source==close)
		{
			System.exit(0);
		}else if(source==flush)
		{
			flush();
		}else if(source==style1)
		{
			index=0;
			changeStyle();
		}else if(source==style2)
		{
			index=1;
			changeStyle();
		}else if(source==style3)
		{
			index=2;
			changeStyle();
		}else if(source==easy)
		{
			new MyFrame(10,10,10,250,200);
			this.dispose();
		}else if(source==normal)
		{
			new MyFrame(50,15,20,120,165);
			this.dispose();
		}else if(source==hard)
		{
			new MyFrame(90,19,25,85,100);
			this.dispose();
		}
	}
	
	//重新设置界面风格的图片
	public void changeStyle()
	{
		mainLab.setIcon(mainLabs[index]);
		
		style1.setIcon(style1s[index]);
		style2.setIcon(style2s[index]);
		style3.setIcon(style3s[index]);
		
		for (int i = 0; i < row; i++) {
			for (int j = 0; j < col; j++) {
				if (mines[i][j].isFlag()) {		// 旗子
					mines[i][j].setIcon(btn_flags[index]);
				}else {
					if (mines[i][j].isOpen()) {	// 打开
						if (mines[i][j].getValue()==0) {
							mines[i][j].setIcon(btn_opens[index]);
						}
					}else {						// 关闭
						mines[i][j].setIcon(btn_backs[index]);
					}
				}
				jp.add(mines[i][j]);
			}
		}
		
	}
	
	//刷新按钮的作用
	public void flush()
	{
		for(int i=0;i<row;i++)
		{
			for(int j=0;j<col;j++)
			{
				mines[i][j].setIcon(btn_backs[index]);
				mines[i][j].setFlag(false);
				mines[i][j].setOpen(false);
				mines[i][j].setValue(0);
			}
		}
		
		layMines();
		
		//设置每个按钮的周围地雷数量
		for(int i=0;i<row;i++)
		{
			for(int j=0;j<col;j++)
			{
				if(mines[i][j].getValue()==0)
				{
					mines[i][j].setValue(calMineNum(i,j));
				}
			}
		}
		
		start=System.currentTimeMillis();
		mineNum=startMineNum;
		
	}
	
	public static void main(String args[])
	{
		MyFrame mf=new MyFrame(100,19,25,85,100);
	}

}
