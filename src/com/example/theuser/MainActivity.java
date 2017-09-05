package com.example.theuser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.media.SoundPool;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.Menu;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MainActivity extends Activity {
	
	int  x,y;
	LinearLayout line1,line2,line3,line4,line5,line6;
	EditText et1,et2,et3,et4;
	TextView text1;
	BorderTextView[] tv1 = new BorderTextView[6];
	BorderTextView[] tv2 = new BorderTextView[7];
	BorderTextView[] tv3 = new BorderTextView[6];
	BorderTextView[] tv4 = new BorderTextView[4];
	BorderTextView btv1;
	Button b_connect,b_record,b_import;
	
	Thread thread1,thread2,thread3,thread4,thread5,thread6;
	PrintWriter pw1,pw2,pw3,pw4,pw5,pw6;
	BufferedReader br1,br2,br3,br4,br5,br6;
	ServerSocket server1,server2,server3,server4,server5,server6;
	Socket socket1,socket2,socket3,socket4,socket5,socket6;
	Timer timer = new Timer();
	
	int FeedbackStart = 0;//开始录音的反馈标志位
	
	private boolean IsRecord = false;//记录录音状态的标志位
	private static int FolderNum = 1;//用于存放录音文件的与 文件夹名称的相关标志位
	private static int FileNum = 0;//录音文件文件名称的相关标志位
	private static String audioName = null;//裸音文件名
	private static String newAudioName = null;//保存的可播放的音乐文件名
	
	//设置音频来源为麦克风
	private static int audioSource = MediaRecorder.AudioSource.MIC;
	//设置采样频率
	private static int sampleRateInHz = 44100;
	//设置音频的录制声道:CHANNEL_IN_STEREO为双声道，CHANNEL_CONFIGURATION_MONO为单声道
	private static int channelConfig = AudioFormat.CHANNEL_IN_STEREO;
	//音频数据格式：PCM编码的样本位数，或者8位，或者16位。要保证设备支持
	private static int audioFormat = AudioFormat.ENCODING_PCM_16BIT;
	//设置表征缓存大小的变量
	private int bufferSizeInBytes = 0;
	
	//录音实现类AudioRecord
	private AudioRecord audioRecord;
	//用SoundPool播放声音
	private SoundPool sp;
	private HashMap<Integer,Integer> spMap;
	
    //估计的真实位置
    static int realpl11,realpl12;
    static int realpl21,realpl22;
    static int realpl31,realpl32;
    static int realpl41,realpl42;
    static int realpl51,realpl52;
    static int realpl61,realpl62;
    //计算相关的变量
    static float dis1,dis2,dis3,dis4,dis5;
    static int num1,num2,num3,num4,num5,num6;
    static int num56,num15,num25,num35,num45;
    
    static boolean com1 = false;
    static boolean com2 = false;
    static boolean com3 = false;
    static boolean com4 = false;
    static boolean com5 = false;
    static boolean com6 = false;
    
    static int time = 0;

    static float fl1,fl2,fl3,fl4;
    
    static int ipAddress;
    static String strip;
	private static String ip; //服务端ip     
	private static int BROADCAST_PORT=9898;  
	private static String BROADCAST_IP="224.0.0.1";     
	InetAddress inetAddress=null;     
	 /*发送广播端的socket*/    
	MulticastSocket multicastSocket=null;  
	static  boolean isRuning= true; 
	
    static boolean connect1 = false;
    static boolean connect2 = false;
    static boolean connect3 = false;
    static boolean connect4 = false;
    static boolean connect5 = false;
    
    double[] resultLoc;//定位的结果
    double[][] resultloop ;//存循环几次的结果
    private static int loopnum = 1;
    int loopflag = 0;
    
    long time1,time2;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		x=getWindowManager().getDefaultDisplay().getWidth();//获取屏幕宽度
		y=getWindowManager().getDefaultDisplay().getHeight();//获取屏幕高度
		
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);//防止休眠
		
		line1 = (LinearLayout)findViewById(R.id.line1);
		line2 = (LinearLayout)findViewById(R.id.line2);
		line3 = (LinearLayout)findViewById(R.id.line3);
		line4 = (LinearLayout)findViewById(R.id.line4);
		line5 = (LinearLayout)findViewById(R.id.line5);
		line6 = (LinearLayout)findViewById(R.id.line6);
		
		text1 = new TextView(this);
		text1.setWidth(x);
		text1.setHeight(y/40);
		text1.setText("");
		text1.setGravity(Gravity.CENTER);
		text1.setVisibility(View.INVISIBLE);
		line6.addView(text1);
		
//		et1 = new EditText(this);
//		et2 = new EditText(this);
//		et3 = new EditText(this);
//		et4 = new EditText(this);
//		
//		et1.setWidth(x/4);
//		et1.setHeight(y/18);
//		et1.setText("");
//		et1.setTextSize(15);
//		et1.setGravity(Gravity.CENTER);
//		et1.setVisibility(View.INVISIBLE);
//		
//		et2.setWidth(x/4);
//		et2.setHeight(y/18);
//		et2.setText("");
//		et2.setTextSize(15);
//		et2.setGravity(Gravity.CENTER);
//		et2.setVisibility(View.INVISIBLE);
//		
//		et3.setWidth(x/4);
//		et3.setHeight(y/18);
//		et3.setText("");
//		et3.setTextSize(15);
//		et3.setGravity(Gravity.CENTER);
//		et3.setVisibility(View.INVISIBLE);
//		
//		et4.setWidth(x/4);
//		et4.setHeight(y/18);
//		et4.setText("");
//		et4.setTextSize(15);
//		et4.setGravity(Gravity.CENTER);
//		et4.setVisibility(View.INVISIBLE);
//		
//		line6.addView(et1);
//		line6.addView(et2);
//		line6.addView(et3);
//		line6.addView(et4);
		
		tv1[0] = new BorderTextView(this);
		tv1[0].setText("Beacon:");
		tv1[0].setWidth(x/6);
		tv1[0].setHeight(y/8);
		tv1[0].setTextSize(16);
		tv1[0].setGravity(Gravity.CENTER);
		
		for(int i=1;i<6;i++)
		{
			tv1[i] = new BorderTextView(this);
			tv1[i].setText(Integer.toString(i));
			tv1[i].setWidth(x/6);
			tv1[i].setHeight(y/8);
			tv1[i].setTextSize(16);
			tv1[i].setGravity(Gravity.CENTER);
		}
		
		for(int i=0;i<6;i++)
		{
			line1.addView(tv1[i]);
		}
		
		tv2[0] = new BorderTextView(this);
		tv2[0].setText("State:");
		tv2[0].setWidth(x/6);
		tv2[0].setHeight(y/8);
		tv2[0].setTextSize(16);
		tv2[0].setGravity(Gravity.CENTER);
		
		for(int i=1;i<7;i++)
		{
			tv2[i] = new BorderTextView(this);
			tv2[i].setText(Integer.toString(i));
			tv2[i].setWidth(5*x/36);
			tv2[i].setHeight(y/8);
			tv2[i].setTextSize(16);
			tv2[i].setGravity(Gravity.CENTER);
			tv2[i].setBackgroundColor(Color.LTGRAY);
		}
		
		for(int i=0;i<7;i++)
		{
			line2.addView(tv2[i]);
		}
		
		tv3[0] = new BorderTextView(this);
		tv3[0].setText("Dis.:");
		tv3[0].setWidth(x/6);
		tv3[0].setHeight(y/8);
		tv3[0].setTextSize(16);
		tv3[0].setGravity(Gravity.CENTER);
		
		for(int i=1;i<6;i++)
		{
			tv3[i] = new BorderTextView(this);
//			tv3[i].setText(Integer.toString(i));
			tv3[i].setWidth(x/6);
			tv3[i].setHeight(y/8);
			tv3[i].setTextSize(16);
			tv3[i].setGravity(Gravity.CENTER);
		}
		
		for(int i=0;i<6;i++)
		{
			line3.addView(tv3[i]);
		}
		
		tv4[0] = new BorderTextView(this);
		tv4[0].setText("Coor.:");
		tv4[0].setWidth(x/6);
		tv4[0].setHeight(y/8);
		tv4[0].setTextSize(16);
		tv4[0].setGravity(Gravity.CENTER);
		
		for(int i=1;i<4;i++)
		{
			tv4[i] = new BorderTextView(this);
//			tv4[i].setText(Integer.toString(i));
			tv4[i].setWidth(5*x/18);
			tv4[i].setHeight(y/8);
			tv4[i].setTextSize(16);
			tv4[i].setGravity(Gravity.CENTER);
		}
		
		for(int i=0;i<4;i++)
		{
			line4.addView(tv4[i]);
		}
		
		btv1 = new BorderTextView(this);
		btv1.setWidth(x);
		btv1.setHeight(y/12);
		btv1.setText("WORKING");
		btv1.setTextSize(18);
		btv1.setGravity(Gravity.CENTER);
		btv1.setBackgroundColor(Color.LTGRAY);
		
		line5.addView(btv1);
		
		resultLoc = new double[3];
		resultloop = new double[loopnum][3];
		
		while(new File("mnt/sdcard/Data"+String.valueOf(FolderNum)).exists()==true){
			FolderNum = FolderNum+1;
		}//建立根文件夹用于存放录音文件
		
		//获得缓冲区大小
		bufferSizeInBytes = AudioRecord.getMinBufferSize(sampleRateInHz,channelConfig,audioFormat);
		System.out.println(bufferSizeInBytes);
		//实例化AudioRecord
		audioRecord = new AudioRecord(audioSource,sampleRateInHz,channelConfig,audioFormat,bufferSizeInBytes);
		//防止文件夹重复
		
	    sp = new SoundPool(7,AudioManager.STREAM_MUSIC,0);//同时播放的最大音频数为7
	    spMap = new HashMap<Integer,Integer>();
	    spMap.put(1, sp.load(this, R.raw.data_refer, 1));
	    
		b_import = (Button)findViewById(R.id.button0);
		b_connect = (Button)findViewById(R.id.button00);
		b_record = (Button)findViewById(R.id.button1);
	    
	    b_import.setEnabled(false);
	    b_record.setEnabled(false);

		b_import.setOnClickListener(new OnClickListener(){
			public void onClick(View v)
			{
//				fl1 = Float.parseFloat(et1.getText().toString());
//				fl2 = Float.parseFloat(et2.getText().toString());
//				fl3 = Float.parseFloat(et3.getText().toString());
//				fl4 = Float.parseFloat(et4.getText().toString());
				//四个距离值
				fl1 = (float)9.48;
				fl2 = (float)9.90;
				fl3 = (float)10.41;
				fl4 = (float)8.21;
				
				Thread waitcon = new Thread(new Runnable(){
					public void run()
					{
						while(true)
						{
//			                if((connect1==true)&&(connect2==true)&&(connect3==true)&&(connect4==true)&&(connect5==true))
			                if(connect5==true)
			                {
				           		 b_record.post(new Runnable(){
				           			 public void run()
				           			 {
				           				 b_record.setEnabled(true);
				           			 }
				           		 });
				           		 b_import.post(new Runnable(){
				           			 public void run()
				           			 {
				           				 b_import.setEnabled(false);
				           			 }
				           		 });
				           		 break;
			                }
						}
					}
				});
				waitcon.start();
				
//				strip = getIpAddress();
//				try   
//		        {  
//		            inetAddress=InetAddress.getByName(BROADCAST_IP);  //用于广播的地址
//		            multicastSocket=new MulticastSocket(BROADCAST_PORT);  //创建一个multicastsocket对象
//		            multicastSocket.setTimeToLive(1);  
//		            multicastSocket.joinGroup(inetAddress);  
//		              
//		        }catch(Exception e)  
//		        {  
//		            e.printStackTrace();  
//		              
//		        }  
//				//发送ip地址广播
//				Thread sendip = new Thread(new Runnable(){
//					public void run(){
//		       //发送的数据包，局网内的所有地址都可以收到该数据包    
//		                 DatagramPacket dataPacket = null;            
//		                 //将本机的IP（这里可以写动态获取的IP）地址放到数据包里，其实server端接收到数据包后也能获取到发包方的IP的    
//		                 byte[] data =strip.getBytes();     
//		                 dataPacket = new DatagramPacket(data, data.length, inetAddress,BROADCAST_PORT);    
//		                 while(true)    
//		                 {  
//		                     if(isRuning)   
//		                     {  
//		                         try    
//		                         {    
//		                            multicastSocket.send(dataPacket);   
//		                            Thread.sleep(3000);    
//		                            System.out.println("再次发送ip地址广播:.....");    
//		                         } catch (Exception e)                
//		                         {      
//		                             e.printStackTrace();       
//		                         }   
//		                     } 
//		                     if((connect1==true)&&(connect2==true)&&(connect3==true)&&(connect4==true)&&(connect5==true))
////		                   	 if((connect5==true))
//		                     {
//		                    	 try {
//		                    		 b_record.post(new Runnable(){
//		                    			 public void run()
//		                    			 {
//		                    				 b_record.setEnabled(true);
//		                    			 }
//		                    		 });
//		                    		 b_import.post(new Runnable(){
//		                    			 public void run()
//		                    			 {
//		                    				 b_import.setEnabled(false);
//		                    			 }
//		                    		 });
//		                    		
//									multicastSocket.leaveGroup(inetAddress);
//									isRuning = false;
//									break;
//								} catch (IOException e) {
//									// TODO Auto-generated catch block
//									e.printStackTrace();
//								}
//		                     }
//		                 }   
//					}
//				});
//				sendip.start();
				
			}
		});
		
		b_connect.setOnClickListener(new OnClickListener(){
			public void onClick(View v)
			{
				b_import.setEnabled(true);
				b_connect.setEnabled(false);
				thread1 = new Thread(new base1());
				thread2 = new Thread(new base2());
				thread3 = new Thread(new base3());
				thread4 = new Thread(new base4());
				thread5 = new Thread(new base5());
//				thread1.start();
//				thread2.start();
//				thread3.start();
//				thread4.start();
				thread5.start();
			}
		});
		
		b_record.setOnClickListener(new OnClickListener(){
			public void onClick(View v){
				if(String.valueOf(b_record.getText()).equals("Start"))
				{
					b_record.setEnabled(false);
					b_record.setText("Stop");
					//状态位置为灰色
					for(int i=1;i<7;i++)
					{
						tv2[i].setBackgroundColor(Color.LTGRAY);
					}
					for(int i=1;i<6;i++)
					{
						tv3[i].setText("");
					}
					for(int i=1;i<4;i++)
					{
						tv4[i].setText("");
					}
					
					timer.schedule(new RecordStart(),0);//开始录音
					long time1 = System.currentTimeMillis();
					while(true)
					{
//						if(FeedbackStart==5)
						if(FeedbackStart==1)
						{
							timer.schedule(new Clock(), 400);//开始时钟同步
							FeedbackStart=0;
							break;
						}
						long time2 = System.currentTimeMillis();
						if((time2-time1)>(long)3)
						{
							timer.schedule(new Clock(), 400);//开始时钟同步
							FeedbackStart=0;
							break;
						}
					}
				}
//				else{
//					b_record.setEnabled(false);
//					b_record.setText("Start");
//					timer.schedule(new RecordEnd(), 0);//结束录音
//				}
			}
		});
		
	}
	
	class base1 implements Runnable{
		public void run(){
			try{
				server1 = new ServerSocket(2001);
				socket1 = server1.accept();
				connect1 = true;
				
				tv1[1].post(new Runnable(){
					public void run()
					{
						tv1[1].setBackgroundColor(Color.GREEN);
					}
				});
				
				pw1 = new PrintWriter(socket1.getOutputStream(),true);
				while(true)
				{
					br1 = new BufferedReader(new InputStreamReader(socket1.getInputStream()));
					String line1 = br1.readLine();
					while(line1!=null)
					{
						if(line1.equals("520"))
						{
							FeedbackStart = FeedbackStart +1;
						}
						if(line1.equals("521"))
						{					
							String linet1 = br1.readLine();
							realpl11 = Integer.parseInt(linet1);
							String linet2 = br1.readLine();
							realpl12 = Integer.parseInt(linet2);
							
							com1 = true;
							
							tv2[1].post(new Runnable(){
								public void run(){
									tv2[1].setBackgroundColor(Color.GREEN);
								}
							});	

						}
						line1 = br1.readLine();
					}
				}
			}catch(IOException e){
				e.printStackTrace();
			}
		}
	}
	
	class base2 implements Runnable{
		public void run(){
			try{
				server2 = new ServerSocket(2002);
				socket2 = server2.accept();
				connect2 = true;
				
				tv1[2].post(new Runnable(){
					public void run()
					{
						tv1[2].setBackgroundColor(Color.GREEN);
					}
				});
				
				pw2 = new PrintWriter(socket2.getOutputStream(),true);
				while(true)
				{
					br2 = new BufferedReader(new InputStreamReader(socket2.getInputStream()));
					String line2 = br2.readLine();
					while(line2!=null)
					{
						if(line2.equals("520"))
						{
							FeedbackStart = FeedbackStart +1;
						}
						if(line2.equals("521"))
						{					
							String linet1 = br2.readLine();
							realpl21 = Integer.parseInt(linet1);
							String linet2 = br2.readLine();
							realpl22 = Integer.parseInt(linet2);
							
							com2 = true;
							
							tv2[2].post(new Runnable(){
								public void run(){
									tv2[2].setBackgroundColor(Color.GREEN);
								}
							});	

						}
						line2 = br2.readLine();
					}
				}
			}catch(IOException e){
				e.printStackTrace();
			}
		}
	}
	
	class base3 implements Runnable{
		public void run(){
			try{
				server3 = new ServerSocket(2003);
				socket3 = server3.accept();
				connect3 = true;
				
				tv1[3].post(new Runnable(){
					public void run()
					{
						tv1[3].setBackgroundColor(Color.GREEN);
					}
				});
				
				pw3 = new PrintWriter(socket3.getOutputStream(),true);
				while(true)
				{
					br3 = new BufferedReader(new InputStreamReader(socket3.getInputStream()));
					String line3 = br3.readLine();
					while(line3!=null)
					{
						if(line3.equals("520"))
						{
							FeedbackStart = FeedbackStart +1;
						}
						if(line3.equals("521"))
						{					
							String linet1 = br3.readLine();
							realpl31 = Integer.parseInt(linet1);
							String linet2 = br3.readLine();
							realpl32 = Integer.parseInt(linet2);
							
							com3 = true;
							
							tv2[3].post(new Runnable(){
								public void run(){
									tv2[3].setBackgroundColor(Color.GREEN);
								}
							});	

						}
						line3 = br3.readLine();
					}
				}
			}catch(IOException e){
				e.printStackTrace();
			}
		}
	}
	
	class base4 implements Runnable{
		public void run(){
			try{
				server4 = new ServerSocket(2004);
				socket4 = server4.accept();
				connect4 = true;
				
				tv1[4].post(new Runnable(){
					public void run()
					{
						tv1[4].setBackgroundColor(Color.GREEN);
					}
				});
				
				pw4 = new PrintWriter(socket4.getOutputStream(),true);
				while(true)
				{
					br4 = new BufferedReader(new InputStreamReader(socket4.getInputStream()));
					String line4 = br4.readLine();
					while(line4!=null)
					{
						if(line4.equals("520"))
						{
							FeedbackStart = FeedbackStart +1;
						}
						if(line4.equals("521"))
						{					
							String linet1 = br4.readLine();
							realpl41 = Integer.parseInt(linet1);
							String linet2 = br4.readLine();
							realpl42 = Integer.parseInt(linet2);
							
							com4 = true;
							
							tv2[4].post(new Runnable(){
								public void run(){
									tv2[4].setBackgroundColor(Color.GREEN);
								}
							});	

						}
						line4 = br4.readLine();
					}
				}
			}catch(IOException e){
				e.printStackTrace();
			}
		}
	}
	
	class base5 implements Runnable{
		public void run(){
			try{
				server5 = new ServerSocket(2005);
				socket5 = server5.accept();
				connect5 = true;
				
				tv1[5].post(new Runnable(){
					public void run()
					{
						tv1[5].setBackgroundColor(Color.GREEN);
					}
				});
				
				pw5 = new PrintWriter(socket5.getOutputStream(),true);
				while(true)
				{
					br5 = new BufferedReader(new InputStreamReader(socket5.getInputStream()));
					String line5 = br5.readLine();
					while(line5!=null){
						if(line5.equals("000"))
						{
							timer.schedule(new TargetGo(), 400);
						}
						if(line5.equals("520"))
						{
							FeedbackStart = FeedbackStart +1;
						}
						if(line5.equals("521"))
						{					
							String linet1 = br5.readLine();
							realpl51 = Integer.parseInt(linet1);
							String linet2 = br5.readLine();
							realpl52 = Integer.parseInt(linet2);
							
							com5 = true;
							
							tv2[5].post(new Runnable(){
								public void run(){
									tv2[5].setBackgroundColor(Color.GREEN);
								}
							});	

						}
						line5 = br5.readLine();
					}			
				}
			}catch(IOException e){
				e.printStackTrace();
			}
		}
	}
	
	class RecordStart extends TimerTask{
		public void run(){
//			pw1.println("00");//00作为开始录音的标志位
//			pw2.println("00");
//			pw3.println("00");
//			pw4.println("00");
			pw5.println("00");
			
			//用户端开始录音
			new File("mnt/sdcard/Data"+String.valueOf(FolderNum)).mkdirs();
			IsRecord = true;
			startRecord();
			btv1.post(new Runnable(){
				public void run(){
					btv1.setBackgroundColor(Color.GREEN);
				}
			});
		}
	}
	
	class RecordEnd extends TimerTask{
		public void run(){
//			pw1.println("01");//01作为结束录音的标志位
//			pw2.println("01");
//			pw3.println("01");
//			pw4.println("01");
			pw5.println("01");
			
			//用户端结束录音
			IsRecord = false;
			stop();
			btv1.post(new Runnable(){
				public void run(){
					btv1.setBackgroundColor(Color.LTGRAY);
				}
			});	
			timer.purge();
		}
	}
	
	class Clock extends TimerTask{
		public void run(){
			pw5.println("02");//02作为5号节点发声的标志位，用于时钟同步
		}
	}
	
	class TargetGo extends TimerTask{
		public void run(){
			playSounds(1,1);
			timer.schedule(new RecordEnd(), 300);//结束录音
		}
	}
	
	class compute implements Runnable{
		public void run(){
			while(true)
			{
				if(new File("/mnt/sdcard/Data"+FolderNum+"/data_"+FileNum+".wav").exists()==true)
				{
					String path1 = "/mnt/sdcard/Data"+FolderNum+"/data_"+FileNum+".wav";
					String path2 = "/mnt/sdcard/data_refer.wav";

					WaveFileReader wr1 = new WaveFileReader(path1);
					WaveFileReader wr2 = new WaveFileReader(path2);
					float[] DataSig = wr1.getData()[0];
					float[] DataRefer = wr2.getData()[0];
					int lensig = wr1.getDataLen();
					int lenrefer = wr2.getDataLen();
					
					FFTprepare fpp = new FFTprepare(DataSig);
					float[] FFTsig = fpp.getsig();
					
					FFTcc fcc = new FFTcc(FFTsig,DataRefer);
					fcc.FindFccMax();
					float[] rcc = fcc.getrcc();
					float max = fcc.getmaxvalue();
					int maxplace = fcc.getmaxplace();
					
					int lenrcc = rcc.length;
					float[] rccn;
					
					if(lensig<2400)
					{
						lensig = 4096;
					}
					
					if(lensig<=lenrcc)
					{
						rccn = new float[lensig];
						for(int i=lensig-1; i>=0; i--)
						{
							rccn[i] = rcc[lenrcc-1];
							lenrcc = lenrcc-1;
						}
					}
					else{
						rccn = rcc; 
					}
					
//					FindAllPeaks fap = new FindAllPeaks(rcc);
//					Float[] peaks = fap.getpeaks();
//					Integer[] peakkeys = fap.getpeakkeys();
//					
//					FindPeaks fp = new FindPeaks(peaks,peakkeys,max,maxplace);
//					float peak1 = fp.getpeak1();
//					float peak2 = fp.getpeak2();
//					int peakkey1 = fp.getpeakkey1();
//					int peakkey2 = fp.getpeakkey2();
//					
//					DistanceEstimate de1 = new DistanceEstimate(rcc,peak1,peakkey1);
//					DistanceEstimate de2 = new DistanceEstimate(rcc,peak2,peakkey2);
					
//					realpl61 = de1.getrealplace();
//					realpl62 = de2.getrealplace();
					
					FindPeaksNew fpn = new FindPeaksNew(rccn);
					int[] realnew = new int[2];
					realnew = fpn.getresult();
					
					realpl61 = realnew[0];
					realpl62 = realnew[1];
					
					com6 = true;

					tv2[6].post(new Runnable(){
						public void run(){
							tv2[6].setBackgroundColor(Color.GREEN);
						}
					});	
					break;
					
				}
			}
		}
	}
	
	class DistanceCompute implements Runnable{
		public void run(){
			while(true)
			{
//				time2 = System.currentTimeMillis();
//				if(time2-time1>10000)
//				{
//					com1 = true;
//					com2 = true;
//					com3 = true;
//					com4 = true;
//					com5 = true;
//					com6 = true;
//					tv2[1].post(new Runnable(){
//						public void run(){
//							tv2[1].setBackgroundColor(Color.GREEN);
//						}
//					});	
//					tv2[2].post(new Runnable(){
//						public void run(){
//							tv2[2].setBackgroundColor(Color.GREEN);
//						}
//					});	
//					tv2[3].post(new Runnable(){
//						public void run(){
//							tv2[3].setBackgroundColor(Color.GREEN);
//						}
//					});	
//					tv2[4].post(new Runnable(){
//						public void run(){
//							tv2[4].setBackgroundColor(Color.GREEN);
//						}
//					});	
//					tv2[5].post(new Runnable(){
//						public void run(){
//							tv2[5].setBackgroundColor(Color.GREEN);
//						}
//					});	
//					tv2[6].post(new Runnable(){
//						public void run(){
//							tv2[6].setBackgroundColor(Color.GREEN);
//						}
//					});	
//				}
//				if((com1==true)&&(com2==true)&&(com3==true)&&(com4==true)&&(com5==true)&&(com6==true))
				if((com5==true)&&(com6==true))
				{
					time = time+1;
					num1 = jueduizhi(realpl11,realpl12);
					num2 = jueduizhi(realpl21,realpl22);
					num3 = jueduizhi(realpl31,realpl32);
					num4 = jueduizhi(realpl41,realpl42);
					num5 = jueduizhi(realpl52,realpl51);
					num6 = jueduizhi(realpl62,realpl61);
					
					num56 = num5 - num6;
					dis5 = (num56/(float)2/44100)*340;//计算目标节点和5号节点的距离
					
					num15 = num1 - num5;
					dis1 = dis5 + (num15/(float)44100+(fl1/340))*340;
					
					num25 = num2 - num5;
					dis2 = dis5 + (num25/(float)44100+(fl2/340))*340;
					
					num35 = num3 - num5;
					dis3 = dis5 + (num35/(float)44100+(fl3/340))*340;
					
					num45 = num4 - num5;
					dis4 = dis5 + (num45/(float)44100+(fl4/340))*340;
					
					double disd1 = Double.parseDouble(String.valueOf(dis1));
					double disd2 = Double.parseDouble(String.valueOf(dis2));
					double disd3 = Double.parseDouble(String.valueOf(dis3));
					double disd4 = Double.parseDouble(String.valueOf(dis4));
					double disd5 = Double.parseDouble(String.valueOf(dis5));
					
					List<double[]> xnode = new ArrayList<double[]>();
					double[] d = {8.615, 0.079, 4.110};
					xnode.add(d);
					d = new double[]{22.888, 0, 6.167};
					xnode.add(d);
					d = new double[]{22.888, 10.787, 6.007};
					xnode.add(d);
					d = new double[]{8.65, 6.245, 4.087};
					xnode.add(d);
					d = new double[]{14.895, 4.968, 9.265};
					xnode.add(d);
					double[] area = {0.0, 40.0, 0.0, 15.0, 0.0, 10.0};
					double[] realDist = {disd1,disd2,disd3,disd4,disd5};
					Dist3DMLGrid util =  Dist3DMLGrid.getInstance();
					double[] resulttemp = util.compute(xnode, realDist, area);
					
					for(int i=0;i<3;i++)
					{
						resultloop[loopflag][i] = resulttemp[i+1];
					}
					loopflag = loopflag+1;
					
					com1 = false;
					com2 = false;
					com3 = false;
					com4 = false;
					com5 = false;		
					com6 = false;
					
					tv3[1].post(new Runnable(){
						public void run(){
//							tv3[1].setText(String.valueOf(dis1));
							tv3[1].setText(String.format("%.3f", dis1));
						}
					});	
					tv3[2].post(new Runnable(){
						public void run(){
//							tv3[2].setText(String.valueOf(dis2));
							tv3[2].setText(String.format("%.3f", dis2));
						}
					});	
					tv3[3].post(new Runnable(){
						public void run(){
//							tv3[3].setText(String.valueOf(dis3));
							tv3[3].setText(String.format("%.3f", dis3));
						}
					});	
					tv3[4].post(new Runnable(){
						public void run(){
//							tv3[4].setText(String.valueOf(dis4));
							tv3[4].setText(String.format("%.3f", dis4));
						}
					});	
					tv3[5].post(new Runnable(){
						public void run(){
//							tv3[5].setText(String.valueOf(dis5));
							tv3[5].setText(String.format("%.3f", dis5));
						}
					});	
					
					if(loopflag==loopnum)
					{
						loopflag = 0;
						double[] coorx = new double[loopnum];
						double[] coory = new double[loopnum];
						double[] coorz = new double[loopnum];
	
						for(int j=0;j<loopnum;j++)
						{
							coorx[j] = resultloop[j][0];
							coory[j] = resultloop[j][1];
							coorz[j] = resultloop[j][2];
						}
						GenerateNumber gnx = new GenerateNumber(coorx);
						GenerateNumber gny = new GenerateNumber(coory);
						GenerateNumber gnz = new GenerateNumber(coorz);
						resultLoc[0] = gnx.GetResult();
						resultLoc[1] = gny.GetResult();
						resultLoc[2] = gnz.GetResult();
						
						tv4[1].post(new Runnable(){
							public void run(){
								tv4[1].setText("X:"+String.format("%.3f", resultLoc[0]));
							}
						});	
						tv4[2].post(new Runnable(){
							public void run(){
								tv4[2].setText("Y:"+String.format("%.3f", resultLoc[1]));
							}
						});	
						tv4[3].post(new Runnable(){
							public void run(){
								tv4[3].setText("Z:"+String.format("%.3f", resultLoc[2]));
							}
						});	
						
						b_record.post(new Runnable(){
							public void run(){
								b_record.setText("Start");
							}
						});
						b_record.post(new Runnable(){
							public void run()
							{
								b_record.setEnabled(true);
							}
						});
					}
					else
					{				
						tv2[1].post(new Runnable(){
							public void run()
							{
								tv2[1].setBackgroundColor(Color.LTGRAY);
							}
						});
						tv2[2].post(new Runnable(){
							public void run()
							{
								tv2[2].setBackgroundColor(Color.LTGRAY);
							}
						});
						tv2[3].post(new Runnable(){
							public void run()
							{
								tv2[3].setBackgroundColor(Color.LTGRAY);
							}
						});
						tv2[4].post(new Runnable(){
							public void run()
							{
								tv2[4].setBackgroundColor(Color.LTGRAY);
							}
						});
						tv2[5].post(new Runnable(){
							public void run()
							{
								tv2[5].setBackgroundColor(Color.LTGRAY);
							}
						});
						tv2[6].post(new Runnable(){
							public void run()
							{
								tv2[6].setBackgroundColor(Color.LTGRAY);
							}
						});

						timer.schedule(new RecordStart(),0);//开始录音
						long time1 = System.currentTimeMillis();
						while(true)
						{
							if(FeedbackStart==1)
							{
								timer.schedule(new Clock(), 400);//开始时钟同步
								FeedbackStart=0;
								break;
							}
							long time2 = System.currentTimeMillis();
							if((time2-time1)>(long)3)
							{
								timer.schedule(new Clock(), 400);//开始时钟同步
								FeedbackStart=0;
								break;
							}
						}
					}
					
	
					//将实验数据封装成DataTrans类
					DataTrans dt = new DataTrans();
					dt.SetData(dis1, dis2, dis3, dis4, dis5,time);
					dt.Print();
					dt.WriteToText();
					
					break;
				}
			}
		}
	}
	
	public String getIpAddress()
	{
        //获取wifi服务  
        WifiManager wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);  
        //判断wifi是否开启  
        if (!wifiManager.isWifiEnabled()) {  
        	wifiManager.setWifiEnabled(true);    
        }  
         
        while(true)
        {   
        	WifiInfo wifiInfo = wifiManager.getConnectionInfo();
	        ipAddress = wifiInfo.getIpAddress();   
	        if(ipAddress!=0)
	        {
	        	break;
	        }
        }
        String ip = intToIp(ipAddress);   
        return ip;
	}
	
    private String intToIp(int i) {       
        
        return (i & 0xFF ) + "." +       
      ((i >> 8 ) & 0xFF) + "." +       
      ((i >> 16 ) & 0xFF) + "." +       
      ( i >> 24 & 0xFF) ;  
   } 
	
	public synchronized static int jueduizhi(int a,int b)
	{
		int temp;
		if(a<b)
		{
			temp = a;
			a = b;
			b = temp;
		}
		return (a-b);
	}
	
	public class BorderTextView extends TextView{  
		  
	    public BorderTextView(Context context) {  
	        super(context);  
	    }  
	    public BorderTextView(Context context, AttributeSet attrs) {  
	        super(context, attrs);  
	    }  
	    private int sroke_width = 1;  
	    @Override  
	    protected void onDraw(Canvas canvas) {  
	        Paint paint = new Paint();  
	        //  将边框设为黑色  
	        paint.setColor(android.graphics.Color.BLACK);  
	        //  画TextView的4个边  
	        canvas.drawLine(0, 0, this.getWidth() - sroke_width, 0, paint);  
	        canvas.drawLine(0, 0, 0, this.getHeight() - sroke_width, paint);  
	        canvas.drawLine(this.getWidth() - sroke_width, 0, this.getWidth() - sroke_width, this.getHeight() - sroke_width, paint);  
	        canvas.drawLine(0, this.getHeight() - sroke_width, this.getWidth() - sroke_width, this.getHeight() - sroke_width, paint);  
	        super.onDraw(canvas);  
	    }  
	} 
	
	private void startRecord(){	
    	audioRecord.startRecording();
    	IsRecord = true;
    	new Thread(new AudioRecordThread()).start();//启动另外一个线程，从而与主线程并发处理
    }
    
	private void stop(){
    	if(audioRecord != null){
    		System.out.println("stop Recording");
    		IsRecord = false;
    		audioRecord.stop();
    	}
    }

	public void playSounds (int sound, int number){
	
	//AudioManger对象通过getSystemService(Service.AUDIO_SERVICE)获取
	AudioManager am = (AudioManager)this.getSystemService(this.AUDIO_SERVICE);  
	//获得手机播放最大音乐音量
	float audioMaxVolumn = am.getStreamMaxVolume(AudioManager.STREAM_MUSIC);  
	//float audioCurrentVolumn = am.getStreamVolume(AudioManager.STREAM_MUSIC);  
	float volumnRatio = audioMaxVolumn;  
    sp.play(spMap.get(sound), volumnRatio, volumnRatio, 1, number, 1);
	}
    
	class AudioRecordThread implements Runnable{
		
		@Override
		public void run(){
			FileNum++;
			audioName = "/mnt/sdcard/Data"+FolderNum+"/data_"+FileNum+".raw";
			newAudioName = "/mnt/sdcard/Data"+FolderNum+"/data_"+FileNum+".wav";			
			writeDateTOFile();	//往文件中写入裸数据			
			copyWaveFile(audioName,newAudioName);//得到可以播放的wav文件
			new File("/mnt/sdcard/Data"+FolderNum+"/data_"+FileNum+".raw").delete();
			new Thread(new compute()).start();
			new Thread(new DistanceCompute()).start();
//			time1 = System.currentTimeMillis();
		}	
	}	
	
	private void writeDateTOFile(){
		
		//new一个byte数组用来存一些字节数据，大小为缓冲区的大小
		byte[] audiodata = new byte[bufferSizeInBytes];		
		FileOutputStream fos = null;		
		int readsize = 0;	
		try{
		File file = new File(audioName);			
			if(file.exists()){				
				file.delete();				
			}			
			fos = new FileOutputStream(file); //建立一个可存取字节的文件			
		}catch(Exception e){
			e.printStackTrace();
		}		
		while(IsRecord == true){			
			//从声卡硬件读取数据，用来填充缓冲数组，并返回数组填充数据的大小
			readsize = audioRecord.read(audiodata,0, bufferSizeInBytes);				
			if(AudioRecord.ERROR_INVALID_OPERATION != readsize){
				try{
					System.out.println("writeDateTOFile..."+readsize);					
					//将audiodata中的数据写进输出文件流fos
					//fos.write(audiodata);
					fos.write(audiodata,0,readsize);					
				}catch(IOException e){
					e.printStackTrace();
				}
			}		
		}
		try{
			fos.close();
		}catch(IOException e){
			e.printStackTrace();
		}
	}
	
	
	//下面的函数实现的功能是将裸音文件转换成可以播放的音频文件
	private void copyWaveFile(String inFilename,String outFilename){
		
		//定义两个如下类型的变量来接收该函数的两个参数
		FileInputStream in = null;
		FileOutputStream out = null;
		
		//以下五个变量是要作为参数传递给自定义的函数WriteWaveFileHeader
		long totalAudioLen = 0;
		long totalDataLen = totalAudioLen+36;
		long longSampleRate = sampleRateInHz;
		int channels = 2;
		long byteRate = 16*sampleRateInHz*channels/8;
		
		//new一个byte数组用来存一些字节数据，大小为缓冲区的大小。上一次定义的那个，只能被那个函数私用
		byte[] data = new byte[bufferSizeInBytes];
		
		try{
			in = new FileInputStream(inFilename);//建立一个输入文件流
			out = new FileOutputStream(outFilename);//建立一个输出文件流
			
			totalAudioLen = in.getChannel().size();//由输入文件流的文件通道得到此通道文件的大小
			totalDataLen = totalAudioLen+36;
			
			//插入文件头
			WriteWaveFileHeader(out,totalAudioLen,totalDataLen,longSampleRate,channels,byteRate);
			
			//将in内的数据写进data,并将data中的数据写进out
			int size = 0;
			while((size = in.read(data)) != -1){
				System.out.println("copyWaveFile..."+size);
				out.write(data,0,size);
			}
		}catch(FileNotFoundException e){
			e.printStackTrace();
		}catch(IOException e){
			e.printStackTrace();
		}		
	}
	
	
	/*
	 * 下面提供一个头文件信息。插入这些信息就可以得到可以播放的文件。这些信息是WAV音频文件所必须的，
	 * 而且所有该格式的文件都是一样的，当然对于格式的文件也有相应的文件头
	 */
	private void WriteWaveFileHeader(FileOutputStream out,long totalAudioLen,long totalDataLen,long longSampleRate,int channels,long byteRate)
	throws IOException{
		
		byte[] header = new byte[44];
		header[0]='R';	//RIFF/WAVE header
		header[1]='I';
		header[2]='F';
		header[3]='F';
		header[4]=(byte)(totalDataLen&0xff);
		header[5]=(byte)((totalDataLen>>8)&0xff);
		header[6]=(byte)((totalDataLen>>16)&0xff);
		header[7]=(byte)((totalDataLen>>24)&0xff);
		header[8]='W';
		header[9]='A';
		header[10]='V';
		header[11]='E';
		header[12]='f';	//'fmt ' chunk
		header[13]='m';
		header[14]='t';
		header[15]=' ';
		header[16]=16;	// 4 bytes: size of 'fmt ' chunk
		header[17]=0;
		header[18]=0;
		header[19]=0;
		header[20]=1;//format=1
		header[21]=0;
		header[22]=(byte)channels;
		header[23]=0;
		header[24]=(byte)(longSampleRate&0xff);
		header[25]=(byte)((longSampleRate>>8)&0xff);
		header[26]=(byte)((longSampleRate>>16)&0xff);
		header[27]=(byte)((longSampleRate>>24)&0xff);
		header[28]=(byte)(byteRate&0xff);
		header[29]=(byte)((byteRate>>8)&0xff);
		header[30]=(byte)((byteRate>>16)&0xff);
		header[31]=(byte)((byteRate>>24)&0xff);
		header[32]=(byte)(2*16/8);//block align
		header[33]=0;
		header[34]=16;//bits per sample
		header[35]=0;
		header[36]='d';
		header[37]='a';
		header[38]='t';
		header[39]='a';
		header[40]=(byte)(totalAudioLen&0xff);
		header[41]=(byte)((totalAudioLen>>8)&0xff);
		header[42]=(byte)((totalAudioLen>>16)&0xff);
		header[43]=(byte)((totalAudioLen>>24)&0xff);
		out.write(header,0,44);		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
