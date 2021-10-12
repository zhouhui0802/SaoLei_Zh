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
	
	private JLabel mainLab;  //������
	
	private JLabel bar;	//ͼ��
	
	public static int mineNum;				// ���׼�����
	private int startMineNum; //��ʼ������
	
	private JLabel timeLab,mineLab;  //ʱ��͵���
	private JButton flush,min,close;
	private JButton style1,style2,style3;
	
	private long start;
	
	private int row,col;  //������
	private Mine[][] mines;  //���װ�ť����
	private JPanel jp; //���õ��׵����
	private int left,up;  //����ڴ����ϵ�λ��
	

	public static final int MINE_SIZE=22;
	
	// ��Ų�ͬ����ͼƬ
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
		
		this.startMineNum = startMineNum;		// ��ʼ������
		mineNum = startMineNum;	// �׼�����
		timeLab = new JLabel("00:00");
		mineLab = new JLabel(mineNum+"");
		
		this.row = row;		// 19
		this.col = col;		// 15
		this.left = left;	// 85
		this.up = up;		// 100
		mines = new Mine[row][col];
		
		//���ò˵���
		menuBar=getMyMenuBar();
		//�������
		jp=getMinePanel();
		
		//��ʼ�������ؼ�
		init();
		
		//ע���������
		addEventHandler();
		
		Utils.setFrameDrag(this);
		Utils.initFrame(this);
		
	}
	
	//ע�������
	public void addEventHandler()
	{
		//����ǰ������Ϊ������
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
		
		//�������粼�ֹ�����
		jp.setLayout(new GridLayout(row,col));
		
		//�������װ�ť����ӵ����������
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
		
		//������õ���
		layMines();
		
		//����ÿ����ť��Χ�ĵ�������   ͳ�Ƶ��Ƿ�����������
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
		
		//����
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
	
	//������õ���
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
	
	//������Χ���׵�����
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
		
		easy=new JCheckBoxMenuItem("����",new ImageIcon("./img/1.gif"));
		normal=new JCheckBoxMenuItem("�м�",new ImageIcon("./img/2.gif"));
		hard=new JCheckBoxMenuItem("�߼�",new ImageIcon("./img/3.gif"));
		
		menu.add(easy);
		menu.add(normal);
		menu.add(hard);
		
		return m;
	}
	
	public void init()
	{
		//ȡ������߿�
		super.setUndecorated(true);
		super.add(mainLab);
		
		//ˢ�°�ť
		flush.setBounds(716, 16, 31, 31);	// ���ô�С λ��
		flush.setBorderPainted(false);		// ȡ���߿�
		flush.setContentAreaFilled(false);	// ȡ���������
		mainLab.add(flush);					// ��ť��ӵ�mainLab��
		
		//��С����ť
		min.setBounds(766, 16, 31, 31);		// ��С����ť
		min.setBorderPainted(false);
		min.setContentAreaFilled(false);
		mainLab.add(min);
		
		//�رհ�ť
		close.setBounds(816, 16, 31, 31);	// �ر��˳���ť
		close.setBorderPainted(false);
		close.setContentAreaFilled(false);
		mainLab.add(close);
		
		
		//���ֲ�ͬ���İ�ť���ã���������ˬ������
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
		
		//ɨ��ͼ��
		menuBar.setBounds(20, 2, 110, 61);
		mainLab.add(menuBar);
		
		// ��ʱ��
		timeLab.setFont(new Font("΢���ź�", Font.BOLD, 20));	
		timeLab.setBounds(736, 129, 100, 60);
		mainLab.add(timeLab);
		
		//��������
		mineLab.setFont(new Font("΢���ź�", Font.BOLD, 20));// ������
		mineLab.setBounds(751, 202, 100, 60);
		mainLab.add(mineLab);
		
		//�����װ�ť��ӵ������
		jp.setBounds(left, up, col*MINE_SIZE, row*MINE_SIZE);// �������
		mainLab.add(jp);
		
		//������ʱ���߳�
		new Thread(this).start();
		
		//������С����ͼ��
		super.setIconImage(new ImageIcon("./img/icon.jpg").getImage());
		
		// �����С����Ӧ
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
			//������
			if(m.isFlag()||m.isOpen()==true)  //���Ѿ���һ�������Ѿ��һ�
			{
				return;
			}
			
			m.setOpen(true);
					
			//�㵽����ը��
			if(m.getValue()==-1)
			{
				m.setIcon(new ImageIcon("./img/bomb.gif"));
				openAll();   //��ȫ��
				gameover(); //gameover����
				return;
			}else if(m.getValue()==0)  //��������ǿո�
			{
				m.setIcon(btn_opens[index]);
				openAround(m);
			}else{                                //�㵽��������
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
			// ����Ҽ����
			
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
			// �����Ի��� ----λ��,������Ϣ,����
			//System.out.println("���óɹ�");
			int showConfirmDialog = JOptionPane.showConfirmDialog(this, "WIN, �Ƿ����¿�ʼ", "ʤ��", JOptionPane.YES_NO_OPTION);
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
						flag++;          //ͳ���һ���������
						System.out.println("flag="+flag);
					}
					if (mines[k][k2].getValue()==-1) {
						num++;     //ͳ��ʵ�ʵ�������
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
							openAll();					// ��ȫ��
							gameover();					// gameover����
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
	
	//�򿪿ո����Χ
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
					}//����Ѿ����һ��ұ���
					
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
	
	
	//��Ϸ����
	public void gameover()
	{
		int showConfirmDialog=JOptionPane.showConfirmDialog(this, "GAMEOVER, �Ƿ����¿�ʼ","ʧ����", JOptionPane.YES_NO_OPTION);
		if(showConfirmDialog==0)
		{
			flush();
		}else if(showConfirmDialog==1)
		{
			System.exit(0);
		}
	}
	
	
	//�����������֮�󣬴����еİ�ť
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
	
	//�򿪵�ǰ�İ�ť
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
		//����ͳ���㻨���˶���ʱ��
		start=System.currentTimeMillis();
		
		DecimalFormat df=new DecimalFormat("00");
		
		while(true)
		{
			long x=System.currentTimeMillis()-start;
			int minute=(int)(x/1000/60);
			int second=(int)(x/1000%60);
			timeLab.setText(df.format(minute)+":"+df.format(second));
			mineLab.setText(mineNum+"");
			
			//����
			try{
				Thread.sleep(1000);
			}catch(InterruptedException e)
			{
				e.printStackTrace();
			}
			
			// �ж��Ƿ�ʤ��
			if(mineNum==0)
			{
				System.out.println("�����жϺ���");
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
	
	//�������ý������ͼƬ
	public void changeStyle()
	{
		mainLab.setIcon(mainLabs[index]);
		
		style1.setIcon(style1s[index]);
		style2.setIcon(style2s[index]);
		style3.setIcon(style3s[index]);
		
		for (int i = 0; i < row; i++) {
			for (int j = 0; j < col; j++) {
				if (mines[i][j].isFlag()) {		// ����
					mines[i][j].setIcon(btn_flags[index]);
				}else {
					if (mines[i][j].isOpen()) {	// ��
						if (mines[i][j].getValue()==0) {
							mines[i][j].setIcon(btn_opens[index]);
						}
					}else {						// �ر�
						mines[i][j].setIcon(btn_backs[index]);
					}
				}
				jp.add(mines[i][j]);
			}
		}
		
	}
	
	//ˢ�°�ť������
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
		
		//����ÿ����ť����Χ��������
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
