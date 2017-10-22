import java.sql.Date;
import java.sql.Time;

public class TestBean {
	private String firstStr;
	private Date myDate;
	private Time myTime;
	public String getFirstStr() {
		return firstStr;
	}
	public void setFirstStr(String firstStr) {
		this.firstStr = firstStr;
	}
	public Date getMyDate() {
		return myDate;
	}
	public void setMyDate(Date myDate) {
		this.myDate = myDate;
	}
	public Time getMyTime() {
		return myTime;
	}
	public void setMyTime(Time myTime) {
		this.myTime = myTime;
	}
	
	public String toString(){
		return firstStr+"  "+myDate.toString()+"   "+myTime.toString();
	}
	
	
}
