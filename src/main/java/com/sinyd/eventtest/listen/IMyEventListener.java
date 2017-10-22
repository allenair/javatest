package com.sinyd.eventtest.listen;

import java.util.EventListener;

import com.sinyd.eventtest.event.MyEvent;

public interface IMyEventListener extends EventListener {
	public int doAction(MyEvent event);
}
