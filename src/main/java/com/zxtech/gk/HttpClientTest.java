package com.zxtech.gk;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;

public class HttpClientTest {
	private static String saltStr = "ZXTECH<->GK#SAP@2017";
	
	public static void main(String[] args) throws Exception {
		HttpClientTest test = new HttpClientTest();
		
		test.postTest();
	}

	private void getTest() throws Exception {
		HttpClient client = HttpClients.createDefault();

		HttpGet get = new HttpGet("http://114.215.173.129:8080/gksap/deal.do?aaa=111");

		HttpResponse resp = client.execute(get);
		InputStream input = resp.getEntity().getContent();
		BufferedReader read = new BufferedReader(new InputStreamReader(input, "utf8"));

		System.out.println(read.readLine());
	}
	
	private void postTest() throws Exception {
		HttpClient client = HttpClients.createDefault();
//		HttpPost post = new HttpPost("http://114.215.173.129:8080/gksap/deal.do");
		HttpPost post = new HttpPost("http://localhost:8080/gksap/deal.do");
		post.setHeader("Content-Type","application/x-www-form-urlencoded;charset=utf8");
		
		String crc="";
		String data="<ns1:CreateCustomer xmlns:ns1=\"http://mes.com/CustomerService\"><ns1:dcCustomers><ns2:Customers xmlns:ns2=\"http://schemas.datacontract.org/2004/07/MES.MI.Service.DataContracts\"><ns2:DcCustomer><ns2:BankAccount>42050117000800000595</ns2:BankAccount><ns2:City>武汉</ns2:City><ns2:Contact>熊炯</ns2:Contact><ns2:ContactDep>销售</ns2:ContactDep><ns2:Country>中国</ns2:Country><ns2:CustomerAddress>武汉武汉市汉阳区四新南路199号综合办公楼1楼</ns2:CustomerAddress><ns2:CustomerCode>          </ns2:CustomerCode><ns2:CustomerName>武汉保和优诚置业有限公司</ns2:CustomerName><ns2:CustomerTel>027-84824488</ns2:CustomerTel><ns2:InvoiceType>Z1</ns2:InvoiceType><ns2:NetCode>GKHU</ns2:NetCode><ns2:OpenBank>中国建设银行股份有限公司湖北省分行营业部</ns2:OpenBank><ns2:Postcode>430000</ns2:Postcode><ns2:SapCode>13012176  </ns2:SapCode><ns2:Street>武汉市汉阳区四新南路199号综合办公楼1楼</ns2:Street><ns2:TaxNumber>91420105MA4KLHUY25</ns2:TaxNumber></ns2:DcCustomer><ns2:DcCustomer><ns2:BankAccount>19-516201040018955</ns2:BankAccount><ns2:City>绍兴</ns2:City><ns2:Country>中国</ns2:Country><ns2:CustomerAddress>绍兴绍兴市上虞区曹娥街道人民西路1821号7-E幢</ns2:CustomerAddress><ns2:CustomerCode>          </ns2:CustomerCode><ns2:CustomerName>绍兴市上虞科达塑业有限公司</ns2:CustomerName><ns2:CustomerTel>0575-82538719</ns2:CustomerTel><ns2:InvoiceType>Z1</ns2:InvoiceType><ns2:NetCode>GKSG</ns2:NetCode><ns2:OpenBank>中国农业银行股份有限公司绍兴东关支行</ns2:OpenBank><ns2:Postcode>312300</ns2:Postcode><ns2:SapCode>13012172  </ns2:SapCode><ns2:Street>绍兴市上虞区曹娥街道人民西路1821号7-E幢</ns2:Street><ns2:TaxNumber>913306046651737746</ns2:TaxNumber></ns2:DcCustomer><ns2:DcCustomer><ns2:BankAccount>010101201010127775</ns2:BankAccount><ns2:City>长春</ns2:City><ns2:Contact>王经理</ns2:Contact><ns2:ContactTelephone>0431-89397633</ns2:ContactTelephone><ns2:Country>中国</ns2:Country><ns2:CustomerAddress>长春长春市绿园区泰来街1688号</ns2:CustomerAddress><ns2:CustomerCode>          </ns2:CustomerCode><ns2:CustomerFax>0431-89397633</ns2:CustomerFax><ns2:CustomerName>长春市轻铁物业管理有限公司</ns2:CustomerName><ns2:CustomerTel>0431-86194884</ns2:CustomerTel><ns2:InvoiceType>Z1</ns2:InvoiceType><ns2:NetCode>GKJL</ns2:NetCode><ns2:OpenBank>吉林银行长春泰莱街支行</ns2:OpenBank><ns2:Postcode>130000</ns2:Postcode><ns2:SapCode>12545651  </ns2:SapCode><ns2:Street>长春市绿园区泰来街1688号</ns2:Street><ns2:TaxNumber>912201067153724221</ns2:TaxNumber></ns2:DcCustomer></ns2:Customers></ns1:dcCustomers></ns1:CreateCustomer>";
		crc = get32BitMd5EncString(data.trim()+saltStr);
		
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();  
        nvps.add(new BasicNameValuePair("function", "customer"));  
        nvps.add(new BasicNameValuePair("data", data));  
        post.setEntity(new UrlEncodedFormEntity(nvps,"UTF-8"));  
		
        HttpResponse resp = client.execute(post);
		InputStream input = resp.getEntity().getContent();
		BufferedReader read = new BufferedReader(new InputStreamReader(input, "utf8"));

		System.out.println(read.readLine());
	}

	private static String get32BitMd5EncString(String plainText) {
		String result = null;
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(plainText.getBytes());
			byte b[] = md.digest();
			int i;
			StringBuffer buf = new StringBuffer("");
			for (int offset = 0; offset < b.length; offset++) {
				i = b[offset];
				if (i < 0)
					i += 256;
				if (i < 16)
					buf.append("0");
				buf.append(Integer.toHexString(i));
			}
			result = buf.toString(); // md5 32bit
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return result;
	}
}
