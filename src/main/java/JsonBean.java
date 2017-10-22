import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;


public class JsonBean {
	private int intValue;
	private Integer integerValue;
	private double dblValue;
	private Double doubleValue;
	private String strValue;
	private BigDecimal bigValue;
	private List<String> listValue = new ArrayList<String>();
	
	public BigDecimal getBigValue() {
		return bigValue;
	}
	public void setBigValue(BigDecimal bigValue) {
		this.bigValue = bigValue;
	}
	public int getIntValue() {
		return intValue;
	}
	public void setIntValue(int intValue) {
		this.intValue = intValue;
	}
	public Integer getIntegerValue() {
		return integerValue;
	}
	public void setIntegerValue(Integer integerValue) {
		this.integerValue = integerValue;
	}
	public double getDblValue() {
		return dblValue;
	}
	public void setDblValue(double dblValue) {
		this.dblValue = dblValue;
	}
	public Double getDoubleValue() {
		return doubleValue;
	}
	public void setDoubleValue(Double doubleValue) {
		this.doubleValue = doubleValue;
	}
	public String getStrValue() {
		return strValue;
	}
	public void setStrValue(String strValue) {
		this.strValue = strValue;
	}
	public List<String> getListValue() {
		return listValue;
	}
	public void setListValue(List<String> listValue) {
		this.listValue = listValue;
	}
	
}
