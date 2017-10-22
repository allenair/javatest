package com.zxtech.gk;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

public class Test {

	private static class TrustAnyTrustManager implements X509TrustManager {

		public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
		}

		public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
		}

		public X509Certificate[] getAcceptedIssuers() {
			return new X509Certificate[] {};
		}
	}

	private static class TrustAnyHostnameVerifier implements HostnameVerifier {
		public boolean verify(String hostname, SSLSession session) {
			return true;
		}
	}

	/**
	 * post方式请求服务器(https协议)
	 * 
	 * @param url
	 *            请求地址
	 * @param content
	 *            参数
	 * @param charset
	 *            编码
	 * @return
	 * @throws NoSuchAlgorithmException
	 * @throws KeyManagementException
	 * @throws IOException
	 */
	public static byte[] post(String url, String content, String charset)
			throws NoSuchAlgorithmException, KeyManagementException, IOException {
		SSLContext sc = SSLContext.getInstance("SSL");
		sc.init(null, new TrustManager[] { new TrustAnyTrustManager() }, new java.security.SecureRandom());

		URL console = new URL(url);
		HttpsURLConnection conn = (HttpsURLConnection) console.openConnection();
		conn.setSSLSocketFactory(sc.getSocketFactory());
		conn.setHostnameVerifier(new TrustAnyHostnameVerifier());
		conn.setDoOutput(true);
		conn.setDoInput(true);
		conn.connect();
		DataOutputStream out = new DataOutputStream(conn.getOutputStream());
		out.write(content.getBytes(charset));
		// 刷新、关闭
		out.flush();
		out.close();
		InputStream is = conn.getInputStream();
		if (is != null) {
			ByteArrayOutputStream outStream = new ByteArrayOutputStream();
			byte[] buffer = new byte[1024];
			int len = 0;
			while ((len = is.read(buffer)) != -1) {
				outStream.write(buffer, 0, len);
			}
			is.close();
			return outStream.toByteArray();
		}
		return null;
	}

	public static void main(String[] args) throws Exception {
		String url = "https://qas.kone.com/HttpAdapter/HttpMessageServlet?interfaceNamespace=http%3A//kone.com/XI/GiantKONE&interface=SO_SI_VBAPI&senderService=GiantKONE&senderParty=&qos=BE&targetSystem=Z03C007&j_username=EDMS_PDM_USR&j_password=Epii2ssc";

		byte[] result = Test.post(url, Test.xmlcontent, "utf-8");
		
		System.out.println(new String(result));
	}
	
//	private static String xmlcontent = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><ns:_-APICON_-VBE_BAPI xmlns:ns=\"urn:sap-com:document:sap:rfc:functions\"><FFEHLERHANDLING/><FVBELN_IN></FVBELN_IN><FRCODE/><FRTEXT/><SHEADER><DOC_TYPE>YFLO</DOC_TYPE><SALES_ORG>298</SALES_ORG><DISTR_CHAN>01</DISTR_CHAN><DIVISION>01</DIVISION><SALES_OFF>GKFJ</SALES_OFF><INCOTERMS1>SIT</INCOTERMS1><INCOTERMS2>XIAMEN</INCOTERMS2><REQ_DATE_H>2018-11-21</REQ_DATE_H><DATE_TYPE>T</DATE_TYPE><PURCH_DATE>2016-11-21</PURCH_DATE><PMNTTRMS/><PURCH_NO_C>GKFJ20161121</PURCH_NO_C><PP_SEARCH>彬依奴（厦门）休闲服饰</PP_SEARCH><CUST_GRP1/></SHEADER><SLOGIC_SWITCH><PRICING>G</PRICING></SLOGIC_SWITCH><TPARTNER><item><PARTN_ROLE>AG</PARTN_ROLE><PARTN_NUMB>12522365</PARTN_NUMB></item><item><PARTN_ROLE>VE</PARTN_ROLE><PARTN_NUMB>28500183</PARTN_NUMB></item><item><PARTN_ROLE>YK</PARTN_ROLE><PARTN_NUMB>12522365</PARTN_NUMB></item></TPARTNER><TKONDITIONEN><item><ITM_NUMBER>5</ITM_NUMBER><COND_TYPE>ZG04</COND_TYPE><COND_VALUE>159363</COND_VALUE><CURRENCY>RMB</CURRENCY></item><item><ITM_NUMBER>10</ITM_NUMBER><COND_TYPE>ZG04</COND_TYPE><COND_VALUE>33258</COND_VALUE><CURRENCY>RMB</CURRENCY></item><item><ITM_NUMBER>15</ITM_NUMBER><COND_TYPE>ZG04</COND_TYPE><COND_VALUE>219200</COND_VALUE><CURRENCY>RMB</CURRENCY></item><item><ITM_NUMBER>20</ITM_NUMBER><COND_TYPE>ZG04</COND_TYPE><COND_VALUE>19868</COND_VALUE><CURRENCY>RMB</CURRENCY></item></TKONDITIONEN><TPOSITIONEN><item><ITM_NUMBER>5</ITM_NUMBER><HG_LV_ITEM/><PO_ITM_NO>5</PO_ITM_NO><MATERIAL>KM_GM23K_FL</MATERIAL><SHORT_TEXT>L1</SHORT_TEXT><PO_METHOD>ZCTC</PO_METHOD><SALES_DIST/><DIVISION/></item><item><ITM_NUMBER>10</ITM_NUMBER><HG_LV_ITEM/><PO_ITM_NO>10</PO_ITM_NO><MATERIAL>GK_INST_ESC</MATERIAL><SHORT_TEXT>L1-INST</SHORT_TEXT><PO_METHOD>ZCTC</PO_METHOD><SALES_DIST/><DIVISION/></item><item><ITM_NUMBER>15</ITM_NUMBER><HG_LV_ITEM/><PO_ITM_NO>15</PO_ITM_NO><MATERIAL>KM_GM23K_FL</MATERIAL><SHORT_TEXT>L2</SHORT_TEXT><PO_METHOD>ZCTC</PO_METHOD><SALES_DIST/><DIVISION/></item><item><ITM_NUMBER>20</ITM_NUMBER><HG_LV_ITEM/><PO_ITM_NO>20</PO_ITM_NO><MATERIAL>GK_INST_ESC</MATERIAL><SHORT_TEXT>L2-INST</SHORT_TEXT><PO_METHOD>ZCTC</PO_METHOD><SALES_DIST/><DIVISION/></item></TPOSITIONEN><TEINTEILUNGEN><item><ITM_NUMBER>5</ITM_NUMBER><REQ_QTY>1</REQ_QTY></item><item><ITM_NUMBER>10</ITM_NUMBER><REQ_QTY>1</REQ_QTY></item><item><ITM_NUMBER>15</ITM_NUMBER><REQ_QTY>1</REQ_QTY></item><item><ITM_NUMBER>20</ITM_NUMBER><REQ_QTY>1</REQ_QTY></item></TEINTEILUNGEN><TMERKMALSWERTE><item><CONFIG_ID>5</CONFIG_ID><INST_ID>5</INST_ID><CHARC>KCO_RUSH_TECH_PLATFORM</CHARC><VALUE>GM3</VALUE></item><item><CONFIG_ID>5</CONFIG_ID><INST_ID>5</INST_ID><CHARC>KCO_RUSH_BUILDING_STATE</CHARC><VALUE>N</VALUE></item><item><CONFIG_ID>5</CONFIG_ID><INST_ID>5</INST_ID><CHARC>KCO_RUSH_BUSINESS_TYPE</CHARC><VALUE>RES</VALUE></item><item><CONFIG_ID>5</CONFIG_ID><INST_ID>5</INST_ID><CHARC>CN_GK_SO_NUMBER</CHARC><VALUE>GK2016-00198</VALUE></item><item><CONFIG_ID>5</CONFIG_ID><INST_ID>5</INST_ID><CHARC>CN_GK_INST_COMPANY</CHARC><VALUE>厦门宏菱电梯有限公司</VALUE></item><item><CONFIG_ID>5</CONFIG_ID><INST_ID>5</INST_ID><CHARC>CN_GK_INST_TEAM</CHARC><VALUE>KHB-21775</VALUE></item><item><CONFIG_ID>5</CONFIG_ID><INST_ID>5</INST_ID><CHARC>CN_GK_REF_SALES_OFFICE</CHARC><VALUE>GKFJ</VALUE></item><item><CONFIG_ID>5</CONFIG_ID><INST_ID>5</INST_ID><CHARC>CN_GK_PRODUCT_CODE</CHARC><VALUE>GM23K</VALUE></item><item><CONFIG_ID>5</CONFIG_ID><INST_ID>5</INST_ID><CHARC>CN_GK_INST_CONTRACT</CHARC><VALUE>A</VALUE></item><item><CONFIG_ID>5</CONFIG_ID><INST_ID>5</INST_ID><CHARC>KCSM_WARRANTY_PERIOD</CHARC><VALUE>12</VALUE></item><item><CONFIG_ID>5</CONFIG_ID><INST_ID>5</INST_ID><CHARC>KCSM_WARRANTY_DURATION</CHARC><VALUE>12</VALUE></item><item><CONFIG_ID>5</CONFIG_ID><INST_ID>5</INST_ID><CHARC>TYP_PROCESS</CHARC><VALUE>A</VALUE></item><item><CONFIG_ID>5</CONFIG_ID><INST_ID>5</INST_ID><CHARC>CN_GK_INST_DOWNLOADING</CHARC><VALUE>0</VALUE></item><item><CONFIG_ID>5</CONFIG_ID><INST_ID>5</INST_ID><CHARC>CN_GK_INST_HOISTING</CHARC><VALUE>2</VALUE></item><item><CONFIG_ID>5</CONFIG_ID><INST_ID>5</INST_ID><CHARC>CN_GK_INST_SCAFOLDING</CHARC><VALUE>0</VALUE></item><item><CONFIG_ID>5</CONFIG_ID><INST_ID>5</INST_ID><CHARC>CN_GK_INST_INSPECTION</CHARC><VALUE>2</VALUE></item><item><CONFIG_ID>5</CONFIG_ID><INST_ID>5</INST_ID><CHARC>CN_GK_INST_ELECTRICITY</CHARC><VALUE>1</VALUE></item><item><CONFIG_ID>5</CONFIG_ID><INST_ID>5</INST_ID><CHARC>CN_GK_INST_OTHER</CHARC><VALUE>1</VALUE></item><item><CONFIG_ID>5</CONFIG_ID><INST_ID>5</INST_ID><CHARC>KCSM_WARRANTY_INCLUDED</CHARC><VALUE>1</VALUE></item><item><CONFIG_ID>5</CONFIG_ID><INST_ID>5</INST_ID><CHARC>TYP_SAPVC_LOCK</CHARC><VALUE>1</VALUE></item><item><CONFIG_ID>5</CONFIG_ID><INST_ID>5</INST_ID><CHARC>TYP_CHECK_CHAR</CHARC><VALUE>1</VALUE></item><item><CONFIG_ID>5</CONFIG_ID><INST_ID>5</INST_ID><CHARC>2100</CHARC><VALUE>96550.30</VALUE></item><item><CONFIG_ID>5</CONFIG_ID><INST_ID>5</INST_ID><CHARC>SLTP</CHARC><VALUE>78677.25</VALUE></item><item><CONFIG_ID>5</CONFIG_ID><INST_ID>5</INST_ID><CHARC>SLEN</CHARC><VALUE>50.50</VALUE></item><item><CONFIG_ID>5</CONFIG_ID><INST_ID>5</INST_ID><CHARC>SLTR</CHARC><VALUE>11414.92</VALUE></item><item><CONFIG_ID>5</CONFIG_ID><INST_ID>5</INST_ID><CHARC>SLGA</CHARC><VALUE>1810.00</VALUE></item><item><CONFIG_ID>5</CONFIG_ID><INST_ID>5</INST_ID><CHARC>2300</CHARC><VALUE>4320.00</VALUE></item><item><CONFIG_ID>5</CONFIG_ID><INST_ID>5</INST_ID><CHARC>3100</CHARC><VALUE>7514.00</VALUE></item><item><CONFIG_ID>5</CONFIG_ID><INST_ID>5</INST_ID><CHARC>3410</CHARC><VALUE>76.00</VALUE></item><item><CONFIG_ID>5</CONFIG_ID><INST_ID>5</INST_ID><CHARC>3105</CHARC><VALUE>3203.88</VALUE></item><item><CONFIG_ID>5</CONFIG_ID><INST_ID>5</INST_ID><CHARC>3300</CHARC><VALUE>5272.79</VALUE></item><item><CONFIG_ID>5</CONFIG_ID><INST_ID>5</INST_ID><CHARC>TYP_ESC_INCLINATION</CHARC><VALUE>1</VALUE></item><item><CONFIG_ID>5</CONFIG_ID><INST_ID>5</INST_ID><CHARC>COL_ESC_ALU_STEP</CHARC><VALUE>1</VALUE></item><item><CONFIG_ID>5</CONFIG_ID><INST_ID>5</INST_ID><CHARC>DIM_ESC_BA_HEIGHT</CHARC><VALUE>900</VALUE></item><item><CONFIG_ID>5</CONFIG_ID><INST_ID>5</INST_ID><CHARC>DIM_ESC_TRUSS_TO_TRUSS</CHARC><VALUE>0</VALUE></item><item><CONFIG_ID>5</CONFIG_ID><INST_ID>5</INST_ID><CHARC>DIM_ESC_STEP_WIDTH</CHARC><VALUE>1000</VALUE></item><item><CONFIG_ID>5</CONFIG_ID><INST_ID>5</INST_ID><CHARC>TYP_LOAD_CONDITION</CHARC><VALUE>1</VALUE></item><item><CONFIG_ID>5</CONFIG_ID><INST_ID>5</INST_ID><CHARC>TYP_ELECTRICAL_SYSTEM_ESC</CHARC><VALUE>1</VALUE></item><item><CONFIG_ID>5</CONFIG_ID><INST_ID>5</INST_ID><CHARC>MAT_ESC_ACCESS_COVER_PLATE</CHARC><VALUE>H</VALUE></item><item><CONFIG_ID>5</CONFIG_ID><INST_ID>5</INST_ID><CHARC>MAT_ESC_HANDRAIL_PROFILE</CHARC><VALUE>4</VALUE></item><item><CONFIG_ID>5</CONFIG_ID><INST_ID>5</INST_ID><CHARC>MAT_ESC_SKIRT</CHARC><VALUE>6</VALUE></item><item><CONFIG_ID>5</CONFIG_ID><INST_ID>5</INST_ID><CHARC>VAL_MAX_CAPACITY</CHARC><VALUE>6000</VALUE></item><item><CONFIG_ID>5</CONFIG_ID><INST_ID>5</INST_ID><CHARC>DIM_ESC_VERTICAL_RISE</CHARC><VALUE>3200</VALUE></item><item><CONFIG_ID>5</CONFIG_ID><INST_ID>5</INST_ID><CHARC>TYP_ESC_ALU_STEP_DEMARCATION</CHARC><VALUE>0</VALUE></item><item><CONFIG_ID>5</CONFIG_ID><INST_ID>5</INST_ID><CHARC>TYP_ESC_ARRANGEMENT</CHARC><VALUE>1</VALUE></item><item><CONFIG_ID>5</CONFIG_ID><INST_ID>5</INST_ID><CHARC>TYP_ESC_AUTO_LUBRICATION</CHARC><VALUE>0</VALUE></item><item><CONFIG_ID>5</CONFIG_ID><INST_ID>5</INST_ID><CHARC>TYP_ESC_AUX_BRAKE</CHARC><VALUE>0</VALUE></item><item><CONFIG_ID>5</CONFIG_ID><INST_ID>5</INST_ID><CHARC>TYP_ESC_BRAKE_LINE_WEAR_MON</CHARC><VALUE>0</VALUE></item><item><CONFIG_ID>5</CONFIG_ID><INST_ID>5</INST_ID><CHARC>TYP_ESC_BUZZER_ALARM</CHARC><VALUE>0</VALUE></item><item><CONFIG_ID>5</CONFIG_ID><INST_ID>5</INST_ID><CHARC>TYP_ESC_CENTER_DECK</CHARC><VALUE>0</VALUE></item><item><CONFIG_ID>5</CONFIG_ID><INST_ID>5</INST_ID><CHARC>TYP_ESC_COMB_HEATING</CHARC><VALUE>0</VALUE></item><item><CONFIG_ID>5</CONFIG_ID><INST_ID>5</INST_ID><CHARC>TYP_ESC_COMB_LIGHT</CHARC><VALUE>0</VALUE></item><item><CONFIG_ID>5</CONFIG_ID><INST_ID>5</INST_ID><CHARC>TYP_ESC_ENVIRONMENT</CHARC><VALUE>1</VALUE></item><item><CONFIG_ID>5</CONFIG_ID><INST_ID>5</INST_ID><CHARC>TYP_ESC_FAULT_DISPLAY</CHARC><VALUE>0</VALUE></item><item><CONFIG_ID>5</CONFIG_ID><INST_ID>5</INST_ID><CHARC>TYP_ESC_GB_VERSION</CHARC><VALUE>2</VALUE></item><item><CONFIG_ID>5</CONFIG_ID><INST_ID>5</INST_ID><CHARC>TYP_ESC_GEAR_BOX</CHARC><VALUE>1</VALUE></item><item><CONFIG_ID>5</CONFIG_ID><INST_ID>5</INST_ID><CHARC>TYP_ESC_HANDRAIL_SUPPLIER</CHARC><VALUE>1</VALUE></item><item><CONFIG_ID>5</CONFIG_ID><INST_ID>5</INST_ID><CHARC>TYP_ESC_HORIZONTAL_PALLET</CHARC><VALUE>6</VALUE></item><item><CONFIG_ID>5</CONFIG_ID><INST_ID>5</INST_ID><CHARC>TYP_ESC_MONITOR_CONTACT</CHARC><VALUE>0</VALUE></item><item><CONFIG_ID>5</CONFIG_ID><INST_ID>5</INST_ID><CHARC>TYP_ESC_OPERATION_MODE</CHARC><VALUE>1</VALUE></item><item><CONFIG_ID>5</CONFIG_ID><INST_ID>5</INST_ID><CHARC>TYP_ESC_SKIRT_DEFLECTOR</CHARC><VALUE>0</VALUE></item><item><CONFIG_ID>5</CONFIG_ID><INST_ID>5</INST_ID><CHARC>TYP_ESC_SKIRT_SWITCH</CHARC><VALUE>0</VALUE></item><item><CONFIG_ID>5</CONFIG_ID><INST_ID>5</INST_ID><CHARC>TYP_ESC_TR_HEATING</CHARC><VALUE>0</VALUE></item><item><CONFIG_ID>5</CONFIG_ID><INST_ID>5</INST_ID><CHARC>TYP_ESC_TRAFFIC_LIGHT</CHARC><VALUE>0</VALUE></item><item><CONFIG_ID>5</CONFIG_ID><INST_ID>5</INST_ID><CHARC>TYP_ESC_UNDERSTEP_LIGHT</CHARC><VALUE>0</VALUE></item><item><CONFIG_ID>5</CONFIG_ID><INST_ID>5</INST_ID><CHARC>TYP_ESC_VOICE_BEFORE_EXIT</CHARC><VALUE>0</VALUE></item><item><CONFIG_ID>5</CONFIG_ID><INST_ID>5</INST_ID><CHARC>TYP_ESC_WRONG_DIRECT_WARNING</CHARC><VALUE>0</VALUE></item><item><CONFIG_ID>5</CONFIG_ID><INST_ID>5</INST_ID><CHARC>VAL_ESC_SPEED</CHARC><VALUE>0.5</VALUE></item><item><CONFIG_ID>5</CONFIG_ID><INST_ID>5</INST_ID><CHARC>TYP_LOGO_COMPANY</CHARC><VALUE>GK</VALUE></item><item><CONFIG_ID>10</CONFIG_ID><INST_ID>10</INST_ID><CHARC>KCO_RUSH_TECH_PLATFORM</CHARC><VALUE>GM3</VALUE></item><item><CONFIG_ID>10</CONFIG_ID><INST_ID>10</INST_ID><CHARC>CN_GK_BUSINESS_PLACE</CHARC><VALUE></VALUE></item><item><CONFIG_ID>10</CONFIG_ID><INST_ID>10</INST_ID><CHARC>KCO_RUSH_BUILDING_STATE</CHARC><VALUE>N</VALUE></item><item><CONFIG_ID>10</CONFIG_ID><INST_ID>10</INST_ID><CHARC>KCO_RUSH_BUSINESS_TYPE</CHARC><VALUE>RES</VALUE></item><item><CONFIG_ID>15</CONFIG_ID><INST_ID>15</INST_ID><CHARC>KCO_RUSH_TECH_PLATFORM</CHARC><VALUE>GM3</VALUE></item><item><CONFIG_ID>15</CONFIG_ID><INST_ID>15</INST_ID><CHARC>KCO_RUSH_BUILDING_STATE</CHARC><VALUE>N</VALUE></item><item><CONFIG_ID>15</CONFIG_ID><INST_ID>15</INST_ID><CHARC>KCO_RUSH_BUSINESS_TYPE</CHARC><VALUE>RES</VALUE></item><item><CONFIG_ID>15</CONFIG_ID><INST_ID>15</INST_ID><CHARC>CN_GK_SO_NUMBER</CHARC><VALUE>GK2016-00198</VALUE></item><item><CONFIG_ID>15</CONFIG_ID><INST_ID>15</INST_ID><CHARC>CN_GK_INST_COMPANY</CHARC><VALUE>厦门宏菱电梯有限公司</VALUE></item><item><CONFIG_ID>15</CONFIG_ID><INST_ID>15</INST_ID><CHARC>CN_GK_INST_TEAM</CHARC><VALUE>KHB-21775</VALUE></item><item><CONFIG_ID>15</CONFIG_ID><INST_ID>15</INST_ID><CHARC>CN_GK_REF_SALES_OFFICE</CHARC><VALUE>GKFJ</VALUE></item><item><CONFIG_ID>15</CONFIG_ID><INST_ID>15</INST_ID><CHARC>CN_GK_PRODUCT_CODE</CHARC><VALUE>GM23K</VALUE></item><item><CONFIG_ID>15</CONFIG_ID><INST_ID>15</INST_ID><CHARC>CN_GK_INST_CONTRACT</CHARC><VALUE>A</VALUE></item><item><CONFIG_ID>15</CONFIG_ID><INST_ID>15</INST_ID><CHARC>KCSM_WARRANTY_PERIOD</CHARC><VALUE>12</VALUE></item><item><CONFIG_ID>15</CONFIG_ID><INST_ID>15</INST_ID><CHARC>KCSM_WARRANTY_DURATION</CHARC><VALUE>12</VALUE></item><item><CONFIG_ID>15</CONFIG_ID><INST_ID>15</INST_ID><CHARC>TYP_PROCESS</CHARC><VALUE>C</VALUE></item><item><CONFIG_ID>15</CONFIG_ID><INST_ID>15</INST_ID><CHARC>CN_GK_INST_DOWNLOADING</CHARC><VALUE>0</VALUE></item><item><CONFIG_ID>15</CONFIG_ID><INST_ID>15</INST_ID><CHARC>CN_GK_INST_HOISTING</CHARC><VALUE>2</VALUE></item><item><CONFIG_ID>15</CONFIG_ID><INST_ID>15</INST_ID><CHARC>CN_GK_INST_SCAFOLDING</CHARC><VALUE>0</VALUE></item><item><CONFIG_ID>15</CONFIG_ID><INST_ID>15</INST_ID><CHARC>CN_GK_INST_INSPECTION</CHARC><VALUE>2</VALUE></item><item><CONFIG_ID>15</CONFIG_ID><INST_ID>15</INST_ID><CHARC>CN_GK_INST_ELECTRICITY</CHARC><VALUE>1</VALUE></item><item><CONFIG_ID>15</CONFIG_ID><INST_ID>15</INST_ID><CHARC>CN_GK_INST_OTHER</CHARC><VALUE>1</VALUE></item><item><CONFIG_ID>15</CONFIG_ID><INST_ID>15</INST_ID><CHARC>KCSM_WARRANTY_INCLUDED</CHARC><VALUE>1</VALUE></item><item><CONFIG_ID>15</CONFIG_ID><INST_ID>15</INST_ID><CHARC>TYP_SAPVC_LOCK</CHARC><VALUE>1</VALUE></item><item><CONFIG_ID>15</CONFIG_ID><INST_ID>15</INST_ID><CHARC>TYP_CHECK_CHAR</CHARC><VALUE>1</VALUE></item><item><CONFIG_ID>15</CONFIG_ID><INST_ID>15</INST_ID><CHARC>2100</CHARC><VALUE>81027.27</VALUE></item><item><CONFIG_ID>15</CONFIG_ID><INST_ID>15</INST_ID><CHARC>SLTP</CHARC><VALUE>74853.83</VALUE></item><item><CONFIG_ID>15</CONFIG_ID><INST_ID>15</INST_ID><CHARC>SLEN</CHARC><VALUE>505.00</VALUE></item><item><CONFIG_ID>15</CONFIG_ID><INST_ID>15</INST_ID><CHARC>SLTR</CHARC><VALUE>0.00</VALUE></item><item><CONFIG_ID>15</CONFIG_ID><INST_ID>15</INST_ID><CHARC>SLGA</CHARC><VALUE>1810.00</VALUE></item><item><CONFIG_ID>15</CONFIG_ID><INST_ID>15</INST_ID><CHARC>2300</CHARC><VALUE>4320.00</VALUE></item><item><CONFIG_ID>15</CONFIG_ID><INST_ID>15</INST_ID><CHARC>3100</CHARC><VALUE>7950.00</VALUE></item><item><CONFIG_ID>15</CONFIG_ID><INST_ID>15</INST_ID><CHARC>3410</CHARC><VALUE>80.00</VALUE></item><item><CONFIG_ID>15</CONFIG_ID><INST_ID>15</INST_ID><CHARC>TYP_ESC_INCLINATION</CHARC><VALUE>3</VALUE></item><item><CONFIG_ID>15</CONFIG_ID><INST_ID>15</INST_ID><CHARC>COL_ESC_ALU_STEP</CHARC><VALUE>1</VALUE></item><item><CONFIG_ID>15</CONFIG_ID><INST_ID>15</INST_ID><CHARC>DIM_ESC_BA_HEIGHT</CHARC><VALUE>900</VALUE></item><item><CONFIG_ID>15</CONFIG_ID><INST_ID>15</INST_ID><CHARC>DIM_ESC_TRUSS_TO_TRUSS</CHARC><VALUE>0</VALUE></item><item><CONFIG_ID>15</CONFIG_ID><INST_ID>15</INST_ID><CHARC>DIM_ESC_STEP_WIDTH</CHARC><VALUE>1000</VALUE></item><item><CONFIG_ID>15</CONFIG_ID><INST_ID>15</INST_ID><CHARC>TYP_LOAD_CONDITION</CHARC><VALUE>1</VALUE></item><item><CONFIG_ID>15</CONFIG_ID><INST_ID>15</INST_ID><CHARC>TYP_ELECTRICAL_SYSTEM_ESC</CHARC><VALUE>1</VALUE></item><item><CONFIG_ID>15</CONFIG_ID><INST_ID>15</INST_ID><CHARC>MAT_ESC_ACCESS_COVER_PLATE</CHARC><VALUE>H</VALUE></item><item><CONFIG_ID>15</CONFIG_ID><INST_ID>15</INST_ID><CHARC>MAT_ESC_HANDRAIL_PROFILE</CHARC><VALUE>4</VALUE></item><item><CONFIG_ID>15</CONFIG_ID><INST_ID>15</INST_ID><CHARC>MAT_ESC_SKIRT</CHARC><VALUE>6</VALUE></item><item><CONFIG_ID>15</CONFIG_ID><INST_ID>15</INST_ID><CHARC>VAL_MAX_CAPACITY</CHARC><VALUE>6000</VALUE></item><item><CONFIG_ID>15</CONFIG_ID><INST_ID>15</INST_ID><CHARC>DIM_ESC_VERTICAL_RISE</CHARC><VALUE>3200</VALUE></item><item><CONFIG_ID>15</CONFIG_ID><INST_ID>15</INST_ID><CHARC>TYP_ESC_ALU_STEP_DEMARCATION</CHARC><VALUE>0</VALUE></item><item><CONFIG_ID>15</CONFIG_ID><INST_ID>15</INST_ID><CHARC>TYP_ESC_ARRANGEMENT</CHARC><VALUE>1</VALUE></item><item><CONFIG_ID>15</CONFIG_ID><INST_ID>15</INST_ID><CHARC>TYP_ESC_AUTO_LUBRICATION</CHARC><VALUE>0</VALUE></item><item><CONFIG_ID>15</CONFIG_ID><INST_ID>15</INST_ID><CHARC>TYP_ESC_AUX_BRAKE</CHARC><VALUE>0</VALUE></item><item><CONFIG_ID>15</CONFIG_ID><INST_ID>15</INST_ID><CHARC>TYP_ESC_BRAKE_LINE_WEAR_MON</CHARC><VALUE>0</VALUE></item><item><CONFIG_ID>15</CONFIG_ID><INST_ID>15</INST_ID><CHARC>TYP_ESC_BUZZER_ALARM</CHARC><VALUE>0</VALUE></item><item><CONFIG_ID>15</CONFIG_ID><INST_ID>15</INST_ID><CHARC>TYP_ESC_CENTER_DECK</CHARC><VALUE>0</VALUE></item><item><CONFIG_ID>15</CONFIG_ID><INST_ID>15</INST_ID><CHARC>TYP_ESC_COMB_HEATING</CHARC><VALUE>0</VALUE></item><item><CONFIG_ID>15</CONFIG_ID><INST_ID>15</INST_ID><CHARC>TYP_ESC_ENVIRONMENT</CHARC><VALUE>1</VALUE></item><item><CONFIG_ID>15</CONFIG_ID><INST_ID>15</INST_ID><CHARC>TYP_ESC_FAULT_DISPLAY</CHARC><VALUE>0</VALUE></item><item><CONFIG_ID>15</CONFIG_ID><INST_ID>15</INST_ID><CHARC>TYP_ESC_GB_VERSION</CHARC><VALUE>2</VALUE></item><item><CONFIG_ID>15</CONFIG_ID><INST_ID>15</INST_ID><CHARC>TYP_ESC_GEAR_BOX</CHARC><VALUE>1</VALUE></item><item><CONFIG_ID>15</CONFIG_ID><INST_ID>15</INST_ID><CHARC>TYP_ESC_HANDRAIL_SUPPLIER</CHARC><VALUE>1</VALUE></item><item><CONFIG_ID>15</CONFIG_ID><INST_ID>15</INST_ID><CHARC>TYP_ESC_HORIZONTAL_PALLET</CHARC><VALUE>6</VALUE></item><item><CONFIG_ID>15</CONFIG_ID><INST_ID>15</INST_ID><CHARC>TYP_ESC_MONITOR_CONTACT</CHARC><VALUE>0</VALUE></item><item><CONFIG_ID>15</CONFIG_ID><INST_ID>15</INST_ID><CHARC>TYP_ESC_OPERATION_MODE</CHARC><VALUE>1</VALUE></item><item><CONFIG_ID>15</CONFIG_ID><INST_ID>15</INST_ID><CHARC>TYP_ESC_SKIRT_DEFLECTOR</CHARC><VALUE>0</VALUE></item><item><CONFIG_ID>15</CONFIG_ID><INST_ID>15</INST_ID><CHARC>TYP_ESC_SKIRT_SWITCH</CHARC><VALUE>0</VALUE></item><item><CONFIG_ID>15</CONFIG_ID><INST_ID>15</INST_ID><CHARC>TYP_ESC_TR_HEATING</CHARC><VALUE>0</VALUE></item><item><CONFIG_ID>15</CONFIG_ID><INST_ID>15</INST_ID><CHARC>TYP_ESC_TRAFFIC_LIGHT</CHARC><VALUE>0</VALUE></item><item><CONFIG_ID>15</CONFIG_ID><INST_ID>15</INST_ID><CHARC>TYP_ESC_UNDERSTEP_LIGHT</CHARC><VALUE>0</VALUE></item><item><CONFIG_ID>15</CONFIG_ID><INST_ID>15</INST_ID><CHARC>TYP_ESC_VOICE_BEFORE_EXIT</CHARC><VALUE>0</VALUE></item><item><CONFIG_ID>15</CONFIG_ID><INST_ID>15</INST_ID><CHARC>TYP_ESC_WRONG_DIRECT_WARNING</CHARC><VALUE>0</VALUE></item><item><CONFIG_ID>15</CONFIG_ID><INST_ID>15</INST_ID><CHARC>VAL_ESC_SPEED</CHARC><VALUE>0.5</VALUE></item><item><CONFIG_ID>15</CONFIG_ID><INST_ID>15</INST_ID><CHARC>TYP_LOGO_COMPANY</CHARC><VALUE>GK</VALUE></item><item><CONFIG_ID>20</CONFIG_ID><INST_ID>20</INST_ID><CHARC>KCO_RUSH_TECH_PLATFORM</CHARC><VALUE>GM3</VALUE></item><item><CONFIG_ID>20</CONFIG_ID><INST_ID>20</INST_ID><CHARC>CN_GK_BUSINESS_PLACE</CHARC><VALUE></VALUE></item><item><CONFIG_ID>20</CONFIG_ID><INST_ID>20</INST_ID><CHARC>KCO_RUSH_BUILDING_STATE</CHARC><VALUE>N</VALUE></item><item><CONFIG_ID>20</CONFIG_ID><INST_ID>20</INST_ID><CHARC>KCO_RUSH_BUSINESS_TYPE</CHARC><VALUE>RES</VALUE></item></TMERKMALSWERTE><TKONFIGURATION><item><POSEX>5</POSEX><CONFIG_ID>5</CONFIG_ID><ROOT_ID>5</ROOT_ID><COMPLETE></COMPLETE><CONSISTENT></CONSISTENT></item><item><POSEX>10</POSEX><CONFIG_ID>10</CONFIG_ID><ROOT_ID>10</ROOT_ID><COMPLETE></COMPLETE><CONSISTENT></CONSISTENT></item><item><POSEX>15</POSEX><CONFIG_ID>15</CONFIG_ID><ROOT_ID>15</ROOT_ID><COMPLETE></COMPLETE><CONSISTENT></CONSISTENT></item><item><POSEX>20</POSEX><CONFIG_ID>20</CONFIG_ID><ROOT_ID>20</ROOT_ID><COMPLETE></COMPLETE><CONSISTENT></CONSISTENT></item></TKONFIGURATION><TKONFIGURATION_REF><item><POSEX>5</POSEX><CONFIG_ID>5</CONFIG_ID><INST_ID>5</INST_ID></item><item><POSEX>10</POSEX><CONFIG_ID>10</CONFIG_ID><INST_ID>10</INST_ID></item><item><POSEX>15</POSEX><CONFIG_ID>15</CONFIG_ID><INST_ID>15</INST_ID></item><item><POSEX>20</POSEX><CONFIG_ID>20</CONFIG_ID><INST_ID>20</INST_ID></item></TKONFIGURATION_REF><TKONFIGURATION_INST><item><CONFIG_ID>5</CONFIG_ID><INST_ID>5</INST_ID><COMPLETE></COMPLETE><CONSISTENT></CONSISTENT></item><item><CONFIG_ID>10</CONFIG_ID><INST_ID>10</INST_ID><COMPLETE></COMPLETE><CONSISTENT></CONSISTENT></item><item><CONFIG_ID>15</CONFIG_ID><INST_ID>15</INST_ID><COMPLETE></COMPLETE><CONSISTENT></CONSISTENT></item><item><CONFIG_ID>20</CONFIG_ID><INST_ID>20</INST_ID><COMPLETE></COMPLETE><CONSISTENT></CONSISTENT></item></TKONFIGURATION_INST><TEXTENSIONIN><item><STRUCTURE>BAPE_VBAK</STRUCTURE><VALUEPART1>000000</VALUEPART1><VALUEPART2>ZZMLST_0</VALUEPART2><VALUEPART3>20161121</VALUEPART3></item><item><STRUCTURE>BAPE_VBAP</STRUCTURE><VALUEPART1>0000000000000010</VALUEPART1><VALUEPART2>ZZ_REF_POSNR</VALUEPART2><VALUEPART3>000005</VALUEPART3></item><item><STRUCTURE>BAPE_VBAP</STRUCTURE><VALUEPART1>0000000000000020</VALUEPART1><VALUEPART2>ZZ_REF_POSNR</VALUEPART2><VALUEPART3>000015</VALUEPART3></item></TEXTENSIONIN></ns:_-APICON_-VBE_BAPI>";
	private static String xmlcontent = "<?xml version=\"1.0\" encoding=\"utf-8\"?><ns:_-APICON_-VBE_BAPI xmlns:ns=\"urn:sap-com:document:sap:rfc:functions\"><SHEADER><DOC_TYPE>YFLO</DOC_TYPE><SALES_ORG>298</SALES_ORG><DISTR_CHAN>01</DISTR_CHAN><DIVISION>01</DIVISION><SALES_OFF>GKFJ</SALES_OFF><INCOTERMS1>SIT</INCOTERMS1><INCOTERMS2>XIAMEN</INCOTERMS2><REQ_DATE_H>2018-11-21</REQ_DATE_H><DATE_TYPE>T</DATE_TYPE><PURCH_DATE>2016-11-21</PURCH_DATE><PURCH_NO_C>GKFJ20161121</PURCH_NO_C><PP_SEARCH>xiamen</PP_SEARCH></SHEADER><SLOGIC_SWITCH><PRICING>G</PRICING></SLOGIC_SWITCH><TPARTNER><item><PARTN_ROLE>AG</PARTN_ROLE><PARTN_NUMB>12522365</PARTN_NUMB></item><item><PARTN_ROLE>VE</PARTN_ROLE><PARTN_NUMB>28500183</PARTN_NUMB></item><item><PARTN_ROLE>YK</PARTN_ROLE><PARTN_NUMB>12522365</PARTN_NUMB></item></TPARTNER><TKONDITIONEN><item><ITM_NUMBER>5</ITM_NUMBER><COND_TYPE>ZG04</COND_TYPE><COND_VALUE>159363</COND_VALUE><CURRENCY>RMB</CURRENCY></item><item><ITM_NUMBER>10</ITM_NUMBER><COND_TYPE>ZG04</COND_TYPE><COND_VALUE>33258</COND_VALUE><CURRENCY>RMB</CURRENCY></item><item><ITM_NUMBER>15</ITM_NUMBER><COND_TYPE>ZG04</COND_TYPE><COND_VALUE>219200</COND_VALUE><CURRENCY>RMB</CURRENCY></item><item><ITM_NUMBER>20</ITM_NUMBER><COND_TYPE>ZG04</COND_TYPE><COND_VALUE>19868</COND_VALUE><CURRENCY>RMB</CURRENCY></item></TKONDITIONEN><TPOSITIONEN><item><ITM_NUMBER>5</ITM_NUMBER><PO_ITM_NO>5</PO_ITM_NO><MATERIAL>KM_GM23K_FL</MATERIAL><SHORT_TEXT>L1</SHORT_TEXT><PO_METHOD>ZCTC</PO_METHOD></item><item><ITM_NUMBER>10</ITM_NUMBER><PO_ITM_NO>10</PO_ITM_NO><MATERIAL>GK_INST_ESC</MATERIAL><SHORT_TEXT>L1-INST</SHORT_TEXT><PO_METHOD>ZCTC</PO_METHOD></item></TPOSITIONEN><TEINTEILUNGEN><item><ITM_NUMBER>5</ITM_NUMBER><REQ_QTY>1</REQ_QTY></item><item><ITM_NUMBER>10</ITM_NUMBER><REQ_QTY>1</REQ_QTY></item><item><ITM_NUMBER>15</ITM_NUMBER><REQ_QTY>1</REQ_QTY></item><item><ITM_NUMBER>20</ITM_NUMBER><REQ_QTY>1</REQ_QTY></item></TEINTEILUNGEN><TMERKMALSWERTE><item><CONFIG_ID>5</CONFIG_ID><INST_ID>5</INST_ID><CHARC>KCO_RUSH_TECH_PLATFORM</CHARC><VALUE>GM3</VALUE></item><item><CONFIG_ID>5</CONFIG_ID><INST_ID>5</INST_ID><CHARC>KCO_RUSH_BUILDING_STATE</CHARC><VALUE>N</VALUE></item></TMERKMALSWERTE><TKONFIGURATION><item><POSEX>5</POSEX><CONFIG_ID>5</CONFIG_ID><ROOT_ID>5</ROOT_ID></item><item><POSEX>10</POSEX><CONFIG_ID>10</CONFIG_ID><ROOT_ID>10</ROOT_ID></item><item><POSEX>15</POSEX><CONFIG_ID>15</CONFIG_ID><ROOT_ID>15</ROOT_ID></item><item><POSEX>20</POSEX><CONFIG_ID>20</CONFIG_ID><ROOT_ID>20</ROOT_ID></item></TKONFIGURATION><TKONFIGURATION_REF><item><POSEX>5</POSEX><CONFIG_ID>5</CONFIG_ID><INST_ID>5</INST_ID></item><item><POSEX>10</POSEX><CONFIG_ID>10</CONFIG_ID><INST_ID>10</INST_ID></item><item><POSEX>15</POSEX><CONFIG_ID>15</CONFIG_ID><INST_ID>15</INST_ID></item><item><POSEX>20</POSEX><CONFIG_ID>20</CONFIG_ID><INST_ID>20</INST_ID></item></TKONFIGURATION_REF><TKONFIGURATION_INST><item><CONFIG_ID>5</CONFIG_ID><INST_ID>5</INST_ID></item><item><CONFIG_ID>10</CONFIG_ID><INST_ID>10</INST_ID></item><item><CONFIG_ID>15</CONFIG_ID><INST_ID>15</INST_ID></item><item><CONFIG_ID>20</CONFIG_ID><INST_ID>20</INST_ID></item></TKONFIGURATION_INST><TEXTENSIONIN><item><STRUCTURE>BAPE_VBAK</STRUCTURE><VALUEPART1>000000</VALUEPART1><VALUEPART2>ZZMLST_0</VALUEPART2><VALUEPART3>20161121</VALUEPART3></item><item><STRUCTURE>BAPE_VBAP</STRUCTURE><VALUEPART1>0000000000000010</VALUEPART1><VALUEPART2>ZZ_REF_POSNR</VALUEPART2><VALUEPART3>000005</VALUEPART3></item></TEXTENSIONIN></ns:_-APICON_-VBE_BAPI>";

}
