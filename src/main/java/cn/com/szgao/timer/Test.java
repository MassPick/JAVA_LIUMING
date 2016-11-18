package cn.com.szgao.timer;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class Test {
	 static int count = 0;
	    
	    public static void showTimer() {
	        TimerTask task = new TimerTask() {
	            @Override
	            public void run() {
	                ++count;
	                System.out.println("时间=" + new Date() + " 执行了" + count + "次"); // 1次
	            }
	        };

	        //设置执行时间
	        Calendar calendar = Calendar.getInstance();
	        int year = calendar.get(Calendar.YEAR);
	        int month = calendar.get(Calendar.MONTH);
	        int day = calendar.get(Calendar.DAY_OF_MONTH);//每天
	        //定制每天的21:09:00执行，
	        calendar.set(year, month, day, 9, 9, 00);
	        Date date = calendar.getTime();
	        Timer timer = new Timer();
	        System.out.println(date);
	        
	        int period = 2 * 1000;
	        //每天的date时刻执行task，每隔2秒重复执行
	        timer.schedule(task, date, period);
	        //每天的date时刻执行task, 仅执行一次
	        //timer.schedule(task, date);
	    }

	    public static void main(String[] args) {
	    	
	    	System.out.println( new java.text.SimpleDateFormat("yyyyMMdd").format(new java.util.Date()));
//	    	System.out.println("启动定时器");
//	        showTimer();
//	    	timer1();
//	    	timer2();
//	    	timer3();
//	    	timer4();
//	    	timer5();
//	    	timer6();
//	    	timer7();
//	        System.out.println("关闭定时器");
	        
	    }
	    
	    
	 // 第一种方法：设定指定任务task在指定时间time执行 schedule(TimerTask task, Date time)
	    public static void timer1() {
	        Timer timer = new Timer();
	        timer.schedule(new TimerTask() {
	            public void run() {
	                System.out.println("-------设定要指定任务--------");
	            }
	        }, 2000);// 设定指定的时间time,此处为2000毫秒
	    }

	    // 第二种方法：设定指定任务task在指定延迟delay后进行固定延迟peroid的执行
	    // schedule(TimerTask task, long delay, long period)
	    public static void timer2() {
	        Timer timer = new Timer();
	        timer.schedule(new TimerTask() {
	            public void run() {
	                System.out.println("-------设定要指定任务--------");
	            }
	        }, 1000, 5000);
	    }

	    // 第三种方法：设定指定任务task在指定延迟delay后进行固定频率peroid的执行。
	    // scheduleAtFixedRate(TimerTask task, long delay, long period)
	    public static void timer3() {
	        Timer timer = new Timer();
	        timer.scheduleAtFixedRate(new TimerTask() {
	            public void run() {
	                System.out.println("-------设定要指定任务--------");
	            }
	        }, 1000, 2000);
	    }

	    // 第四种方法：安排指定的任务task在指定的时间firstTime开始进行重复的固定速率period执行．
	    // Timer.scheduleAtFixedRate(TimerTask task,Date firstTime,long period)
	    public static void timer4() {
	        Calendar calendar = Calendar.getInstance();
	        calendar.set(Calendar.HOUR_OF_DAY, 12); // 控制时
	        calendar.set(Calendar.MINUTE, 0);       // 控制分
	        calendar.set(Calendar.SECOND, 0);       // 控制秒

	        Date time = calendar.getTime();         // 得出执行任务的时间,此处为今天的12：00：00

	        Timer timer = new Timer();
	        timer.scheduleAtFixedRate(new TimerTask() {
	            public void run() {
	                System.out.println("-------设定要指定任务--------");
	            }
	        }, time, 1000 * 60 * 60 * 24);// 这里设定将延时每天固定执行
	        
	    }
	    
	    static int sum=0;
	    static int sumA=0;
	    
	    public static void timer5() {
	    	sumA++;
	    	System.out.println("-----   "+sumA     +"   "+sum   );  
	    	
	        Calendar calendar = Calendar.getInstance();
	        calendar.set(Calendar.HOUR_OF_DAY, 12); // 控制时
	        calendar.set(Calendar.MINUTE, 0);       // 控制分
	        calendar.set(Calendar.SECOND, 0);       // 控制秒

	        Date time = calendar.getTime();         // 得出执行任务的时间,此处为今天的12：00：00

	        Timer timer = new Timer();
	        timer.scheduleAtFixedRate(new TimerTask() {
	        	  int sum5=0;
	        	 
	            public void run() {
//	            	sum++;
//	            	sum5++;
	                System.out.println("-------设定要指定任务--------  "+sum  +"   sum5: "+sum5);
	            }
	        }, time, 1000 * 5);// 这里设定将延时每天固定执行
	        
	    }
	    
	    
	    public static void  timer6(){
	    	SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");  
	        Date startDate = null;
			try {
				startDate = dateFormatter.parse("2016/05/30 01:06:00");
			} catch (ParseException e1) {
				e1.printStackTrace();
			}  
	        Timer timer = new Timer();  
	        timer.schedule(new TimerTask(){  
	           public void run() {  
	               try {  
	            	   
//	                   Thread.sleep(6000);  
	               } catch ( Exception e) {  
	                   e.printStackTrace();  
	               }  
	               System.out.println("execute task!"+ new Timestamp(  this.scheduledExecutionTime() ) );  
	           }  
	        },startDate, 5 * 1000);  
	    }  
	    
	    
	    
	    
	    
	    
	    public static void  timer7(){
	    	SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");  
	        Date startDate = null;
			try {
				startDate = dateFormatter.parse("2016/05/30 01:06:00");
			} catch (ParseException e1) {
				e1.printStackTrace();
			}  
	        Timer timer = new Timer();  
	        timer.scheduleAtFixedRate(new TimerTask(){  
	           public void run() {  
	               try {  
	            	   
	                   Thread.sleep(6000);  
	               } catch ( Exception e) {  
	                   e.printStackTrace();  
	               }  
	               System.out.println("execute task!"+ new Timestamp(  this.scheduledExecutionTime() ) );  
	           }  
	        },startDate, 5 * 1000);  
	    }  
}
