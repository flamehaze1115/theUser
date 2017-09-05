package com.example.theuser;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;


public class DataTrans {
	//到节点12345的距离
	private float dis1;
	private float dis2;
	private float dis3;
	private float dis4;
	private float dis5;
	
	private int time;
	
	OutputStreamWriter pw;
	
	public void SetData(float d1,float d2,float d3,float d4,float d5,int t)
	{
		this.dis1 = d1;
		this.dis2 = d2;
		this.dis3 = d3;
		this.dis4 = d4;
		this.dis5 = d5;
		this.time = t;
	}
	
	public void Print()
	{
		System.out.println("以下是第"+time+"次实验");
		System.out.println("Distance5 is:"+dis5);
		System.out.println("Distance4 is:"+dis4);
		System.out.println("Distance3 is:"+dis3);
		System.out.println("Distance2 is:"+dis2);
		System.out.println("Distance1 is:"+dis1);
	}
	
	public void WriteToText()
	{

		try {
//			pw = new OutputStreamWriter(new FileOutputStream("E:/test.txt"),"GBK");
			pw = new OutputStreamWriter(new FileOutputStream("/mnt/sdcard/test.txt",true),"GBK");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		try {
			pw.write("以下是第"+time+"次实验"+"\r\n");
			pw.write("Distance1 is:"+String.valueOf(dis1)+"\r\n");
			pw.write("Distance2 is:"+String.valueOf(dis2)+"\r\n");
			pw.write("Distance3 is:"+String.valueOf(dis3)+"\r\n");
			pw.write("Distance4 is:"+String.valueOf(dis4)+"\r\n");
			pw.write("Distance5 is:"+String.valueOf(dis5)+"\r\n");
			pw.close();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
