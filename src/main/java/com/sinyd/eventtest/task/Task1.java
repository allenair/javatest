package com.sinyd.eventtest.task;

import java.util.Vector;

import com.sinyd.eventtest.event.MyEvent;
import com.sinyd.eventtest.listen.IMyEventListener;

public class Task1 implements Runnable {
	private int timeLong;
	private Vector<IMyEventListener> listenList = new Vector<IMyEventListener>();
	
	public void addListener(IMyEventListener listen){
		listenList.add(listen);
	}
	public void removeListener(IMyEventListener listen){
		listenList.remove(listen);
	}
	private void fireEvent(MyEvent event){
		for (IMyEventListener listener : listenList) {
			listener.doAction(event);
		}
	}
	
	public Task1(int timeLong){
		this.timeLong = timeLong;
	}
	@Override
	public void run() {
		try {
			Thread.sleep(this.timeLong);
			fireEvent(new MyEvent(this));
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public String toString(){
		return ""+timeLong;
	}
}
