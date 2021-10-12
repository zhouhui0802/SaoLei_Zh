package com.zh.Util;

import java.awt.event.*;

import javax.swing.*;

public class Utils {
	
	//��갴��ʱ������
	private static int xx,yy;
	
	public static void initFrame(JFrame frame)
	{
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}
	
	public static void setFrameDrag(final JFrame frame)
	{
		frame.addMouseListener(new MouseAdapter(){
			//��갴���¼�
			@Override
			public void mousePressed(MouseEvent e)
			{
				super.mousePressed(e);
				xx=e.getX();
				yy=e.getY();
			}
		});
		
		frame.addMouseMotionListener(new MouseAdapter(){
			@Override
			public void mouseDragged(MouseEvent e)
			{
				super.mouseDragged(e);
				//��ק�����У�����˲ʱ����
				int x1=e.getX();
				int y1=e.getY();
				
				//��¼���ڵĳ�ʼλ��
				int x2=frame.getX();
				int y2=frame.getY();
				
				//ʵʱ���ô��ڵ�λ������
				frame.setLocation(x2+x1-xx, y2+y1-yy);
			}
		});
	}
}
