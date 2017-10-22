package com.sinyd.eventtest.event;

import java.util.EventObject;

public class MyEvent extends EventObject {
	private static final long serialVersionUID = 7107840338354589594L;

	private Object srcObj;
	
	public MyEvent(Object source) {
		super(source);
		this.srcObj = source;
	}
	
	public String toString(){
		return srcObj.toString();
	}

}
