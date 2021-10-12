package com.zh.Entity;

import javax.swing.*;

public class Mine extends JButton{
	
	public int row,col;  //地雷的位置坐标
	public int value;  //-1为地雷，不是就统计四周的地雷数量
	
	public boolean isOpen=false;  //是否被翻开  左击
	public boolean isFlag=false; //是否右击
	
	public int getRow() {
		return row;
	}
	public void setRow(int row) {
		this.row = row;
	}
	public int getCol() {
		return col;
	}
	public void setCol(int col) {
		this.col = col;
	}
	public int getValue() {
		return value;
	}
	public void setValue(int value) {
		this.value = value;
	}
	public boolean isOpen() {
		return isOpen;
	}
	public void setOpen(boolean isOpen) {
		this.isOpen = isOpen;
	}
	public boolean isFlag() {
		return isFlag;
	}
	public void setFlag(boolean isFlag) {
		this.isFlag = isFlag;
	}
	
	public Mine(int row,int col)
	{
		super();
		this.col=col;
		this.row=row;
	}
}
