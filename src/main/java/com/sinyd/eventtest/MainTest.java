package com.sinyd.eventtest;

import java.util.concurrent.atomic.AtomicInteger;

import com.sinyd.eventtest.event.MyEvent;
import com.sinyd.eventtest.listen.IMyEventListener;
import com.sinyd.eventtest.task.Task1;

public class MainTest implements IMyEventListener {
	private int threadNum = 5;
	private long start = System.currentTimeMillis();
	private AtomicInteger number = new AtomicInteger(0);
	@Override
	public int doAction(MyEvent event) {
		System.out.println(event.toString());
		if(number.incrementAndGet()==threadNum){
			System.out.println("============\n"+(System.currentTimeMillis()-this.start));
		}
		return 0;
	}

	public void startMain(){
		for(int i=0;i<threadNum;i++){
			Task1 t = new Task1((i+1)*500);
			t.addListener(this);
			new Thread(t).start();
			System.out.println("======end============");
		}
	}
	
	public static void main(String[] ss){
		new MainTest().startMain();
	}
}
