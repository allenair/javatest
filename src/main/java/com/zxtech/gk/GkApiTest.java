package com.zxtech.gk;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.io.Writer;
import java.security.GeneralSecurityException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLException;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContextBuilder;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.conn.ssl.X509HostnameVerifier;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.protocol.HTTP;
import org.apache.http.ssl.SSLContexts;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.CompactWriter;
import com.thoughtworks.xstream.io.xml.DomDriver;
import com.thoughtworks.xstream.io.xml.XmlFriendlyNameCoder;
import com.zxtech.gk.bean.RootBean;
import com.zxtech.gk.bean.SheaderBean;
import com.zxtech.gk.bean.SlogicSwitch;
import com.zxtech.gk.bean.TeinteilungenItem;
import com.zxtech.gk.bean.TextensioninItem;
import com.zxtech.gk.bean.TkonditionenItem;
import com.zxtech.gk.bean.TkonfigurationItem;
import com.zxtech.gk.bean.TkonfigurationinstItem;
import com.zxtech.gk.bean.TkonfigurationrefItem;
import com.zxtech.gk.bean.TmerkmalswerteItem;
import com.zxtech.gk.bean.TpartnerItem;
import com.zxtech.gk.bean.TpositionenItem;

public class GkApiTest {

	private static PoolingHttpClientConnectionManager connMgr;
	private static RequestConfig requestConfig;
	private static final int MAX_TIMEOUT = 10000;

	static {
		// 设置连接池
		connMgr = new PoolingHttpClientConnectionManager();
		// 设置连接池大小
		connMgr.setMaxTotal(100);
		connMgr.setDefaultMaxPerRoute(connMgr.getMaxTotal());

		RequestConfig.Builder configBuilder = RequestConfig.custom();
		// 设置连接超时
		configBuilder.setConnectTimeout(MAX_TIMEOUT);
		// 设置读取超时
		configBuilder.setSocketTimeout(MAX_TIMEOUT);
		// 设置从连接池获取连接实例的超时
		configBuilder.setConnectionRequestTimeout(MAX_TIMEOUT);
		// 在提交请求之前 测试连接是否可用
		configBuilder.setStaleConnectionCheckEnabled(true);
		requestConfig = configBuilder.build();
	}

	public static void main(String[] args) throws Exception {
		GkApiTest test = new GkApiTest();
//		test.postTest();
		
		System.out.println(test.getXmlStr());
	}

	private void postTest() throws Exception {
		String url = "https://qas.kone.com/HttpAdapter/HttpMessageServlet?interfaceNamespace=http%3A//kone.com/XI/GiantKONE&interface=SO_SI_VBAPI&senderService=GiantKONE&senderParty=&qos=BE&targetSystem=Z03C007&j_username=EDMS_PDM_USR&j_password=Epii2ssc";
		
		// HttpClient client = HttpClients.createDefault();
		// CloseableHttpClient client =
		// HttpClients.custom().setSSLSocketFactory(createSSLConnSocketFactory()).setConnectionManager(connMgr).setDefaultRequestConfig(requestConfig).build();

		// HttpClient client =new SSLClient();

		SSLContext sslContext = SSLContexts.custom().loadTrustMaterial(null, new TrustStrategy() {
			public boolean isTrusted(X509Certificate[] chain, String authType) throws CertificateException {
				return true;
			}
		}).build();

		// SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(
		// sslContext,
		// SSLConnectionSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);

		SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslContext, new String[] { "TLSv1" }, null,
				SSLConnectionSocketFactory.getDefaultHostnameVerifier());

		CloseableHttpClient client = HttpClients.custom().setSSLSocketFactory(sslsf).build();

		// HttpClientBuilder builder = HttpClientBuilder.create();
		// builder.disableContentCompression();
		// builder.setSSLSocketFactory(sslsf);
		// SocketConfig config =
		// SocketConfig.custom().setSoKeepAlive(true).setSoTimeout(300000).build();
		// builder.setDefaultSocketConfig(config);
		//// CloseableHttpClient httpclient = builder.build();
		//
		// ConnectionConfig connectionConfig = ConnectionConfig.custom()
		// .setBufferSize(4128)
		// .build();
		//
		// CloseableHttpClient client = HttpClients.custom()
		// .setDefaultConnectionConfig(connectionConfig)
		// .build();

		HttpPost post = new HttpPost(url);
		// post.setProtocolVersion(HttpVersion.HTTP_1_0);
		post.setHeader("Content-Type", "text/xml;charset=utf-8");
		// post.setHeader("Connection", "close");
		post.setHeader(HTTP.CONN_DIRECTIVE, HTTP.CONN_CLOSE);

		// String data = this.getXmlStr();
		String data = this.xmlcontent;
		post.setEntity(new StringEntity(data, "utf-8"));
		CloseableHttpResponse resp = client.execute(post);
		InputStream input = resp.getEntity().getContent();
		System.out.println(resp.getEntity().getContentLength());
		BufferedReader read = new BufferedReader(new InputStreamReader(input, "utf8"));

		System.out.println(read.readLine());
	}

	

	private String getXmlStr() {
		RootBean root = new RootBean();

		SheaderBean sheader = new SheaderBean();
		sheader.setDOC_TYPE("YFLO");
		sheader.setSALES_OFF("GKFJ");
		sheader.setSALES_ORG("298");
		sheader.setDISTR_CHAN("01");
		sheader.setDIVISION("01");
		sheader.setINCOTERMS1("SIT");
		sheader.setINCOTERMS2("XIAMEN");
		sheader.setREQ_DATE_H("2018-11-21");
		sheader.setDATE_TYPE("T");
		sheader.setPURCH_DATE("2016-11-21");
		sheader.setPURCH_NO_C("GKFJ20161121");
		sheader.setPP_SEARCH("彬依奴（厦门）休闲服饰");
		root.setSHEADER(sheader);

		SlogicSwitch slogic = new SlogicSwitch();
		slogic.setPRICING("G");
		root.setSLOGIC_SWITCH(slogic);

		List<TpartnerItem> itermList = new ArrayList<>();
		root.setTPARTNER(itermList);
		;

		TpartnerItem tpItem = new TpartnerItem();
		tpItem.setPARTN_ROLE("AG");
		tpItem.setPARTN_NUMB("12522365");
		itermList.add(tpItem);

		tpItem = new TpartnerItem();
		tpItem.setPARTN_ROLE("VE");
		tpItem.setPARTN_NUMB("28500183");
		itermList.add(tpItem);

		tpItem = new TpartnerItem();
		tpItem.setPARTN_ROLE("YK");
		tpItem.setPARTN_NUMB("12522365");
		itermList.add(tpItem);

		List<TkonditionenItem> tkList = new ArrayList<>();
		root.setTKONDITIONEN(tkList);

		TkonditionenItem tkItem = new TkonditionenItem();
		tkItem.setITM_NUMBER("5");
		tkItem.setCOND_TYPE("ZG04");
		tkItem.setCOND_VALUE("159363");
		tkItem.setCURRENCY("RMB");
		tkList.add(tkItem);

		tkItem = new TkonditionenItem();
		tkItem.setITM_NUMBER("10");
		tkItem.setCOND_TYPE("ZG04");
		tkItem.setCOND_VALUE("33258");
		tkItem.setCURRENCY("RMB");
		tkList.add(tkItem);

		tkItem = new TkonditionenItem();
		tkItem.setITM_NUMBER("15");
		tkItem.setCOND_TYPE("ZG04");
		tkItem.setCOND_VALUE("219200");
		tkItem.setCURRENCY("RMB");
		tkList.add(tkItem);

		tkItem = new TkonditionenItem();
		tkItem.setITM_NUMBER("20");
		tkItem.setCOND_TYPE("ZG04");
		tkItem.setCOND_VALUE("19868");
		tkItem.setCURRENCY("RMB");
		tkList.add(tkItem);

		List<TpositionenItem> tpoList = new ArrayList<>();
		root.setTPOSITIONEN(tpoList);

		TpositionenItem tpoItem = new TpositionenItem();
		tpoItem.setITM_NUMBER("5");
		tpoItem.setPO_ITM_NO("5");
		tpoItem.setMATERIAL("KM_GM23K_FL");
		tpoItem.setSHORT_TEXT("L1");
		tpoItem.setPO_METHOD("ZCTC");
		tpoList.add(tpoItem);

		tpoItem = new TpositionenItem();
		tpoItem.setITM_NUMBER("10");
		tpoItem.setPO_ITM_NO("10");
		tpoItem.setMATERIAL("GK_INST_ESC");
		tpoItem.setSHORT_TEXT("L1-INST");
		tpoItem.setPO_METHOD("ZCTC");
		tpoList.add(tpoItem);

//		tpoItem = new TpositionenItem();
//		tpoItem.setITM_NUMBER("15");
//		tpoItem.setPO_ITM_NO("15");
//		tpoItem.setMATERIAL("KM_GM23K_FL");
//		tpoItem.setSHORT_TEXT("L2");
//		tpoItem.setPO_METHOD("ZCTC");
//		tpoList.add(tpoItem);
//
//		tpoItem = new TpositionenItem();
//		tpoItem.setITM_NUMBER("20");
//		tpoItem.setPO_ITM_NO("20");
//		tpoItem.setMATERIAL("GK_INST_ESC");
//		tpoItem.setSHORT_TEXT("L2-INST");
//		tpoItem.setPO_METHOD("ZCTC");
//		tpoList.add(tpoItem);

		List<TeinteilungenItem> telist = new ArrayList<>();
		root.setTEINTEILUNGEN(telist);

		TeinteilungenItem teItem = new TeinteilungenItem();
		teItem.setITM_NUMBER("5");
		teItem.setREQ_QTY("1");
		telist.add(teItem);

		teItem = new TeinteilungenItem();
		teItem.setITM_NUMBER("10");
		teItem.setREQ_QTY("1");
		telist.add(teItem);

		teItem = new TeinteilungenItem();
		teItem.setITM_NUMBER("15");
		teItem.setREQ_QTY("1");
		telist.add(teItem);

		teItem = new TeinteilungenItem();
		teItem.setITM_NUMBER("20");
		teItem.setREQ_QTY("1");
		telist.add(teItem);

		List<TmerkmalswerteItem> tmlist = new ArrayList<>();
		root.setTMERKMALSWERTE(tmlist);
		;

		TmerkmalswerteItem tmItem = new TmerkmalswerteItem();
		tmItem.setCONFIG_ID("5");
		tmItem.setINST_ID("5");
		tmItem.setCHARC("KCO_RUSH_TECH_PLATFORM");
		tmItem.setVALUE("GM3");
		tmlist.add(tmItem);

		tmItem = new TmerkmalswerteItem();
		tmItem.setCONFIG_ID("5");
		tmItem.setINST_ID("5");
		tmItem.setCHARC("KCO_RUSH_BUILDING_STATE");
		tmItem.setVALUE("N");
		tmlist.add(tmItem);

//		tmItem = new TmerkmalswerteItem();
//		tmItem.setCONFIG_ID("5");
//		tmItem.setINST_ID("5");
//		tmItem.setCHARC("KCO_RUSH_BUSINESS_TYPE");
//		tmItem.setVALUE("RES");
//		tmlist.add(tmItem);
//
//		tmItem = new TmerkmalswerteItem();
//		tmItem.setCONFIG_ID("5");
//		tmItem.setINST_ID("5");
//		tmItem.setCHARC("CN_GK_SO_NUMBER");
//		tmItem.setVALUE("GK2016-00198");
//		tmlist.add(tmItem);

		List<TkonfigurationItem> tkflist = new ArrayList<>();
		root.setTKONFIGURATION(tkflist);

		TkonfigurationItem tkfItem = new TkonfigurationItem();
		tkfItem.setPOSEX("5");
		tkfItem.setCONFIG_ID("5");
		tkfItem.setROOT_ID("5");
		tkflist.add(tkfItem);

		tkfItem = new TkonfigurationItem();
		tkfItem.setPOSEX("10");
		tkfItem.setCONFIG_ID("10");
		tkfItem.setROOT_ID("10");
		tkflist.add(tkfItem);

		tkfItem = new TkonfigurationItem();
		tkfItem.setPOSEX("15");
		tkfItem.setCONFIG_ID("15");
		tkfItem.setROOT_ID("15");
		tkflist.add(tkfItem);

		tkfItem = new TkonfigurationItem();
		tkfItem.setPOSEX("20");
		tkfItem.setCONFIG_ID("20");
		tkfItem.setROOT_ID("20");
		tkflist.add(tkfItem);

		List<TkonfigurationrefItem> tkfrList = new ArrayList<>();
		root.setTKONFIGURATION_REF(tkfrList);

		TkonfigurationrefItem tkfrItem = new TkonfigurationrefItem();
		tkfrItem.setPOSEX("5");
		tkfrItem.setCONFIG_ID("5");
		tkfrItem.setINST_ID("5");
		tkfrList.add(tkfrItem);

		tkfrItem = new TkonfigurationrefItem();
		tkfrItem.setPOSEX("10");
		tkfrItem.setCONFIG_ID("10");
		tkfrItem.setINST_ID("10");
		tkfrList.add(tkfrItem);

		tkfrItem = new TkonfigurationrefItem();
		tkfrItem.setPOSEX("15");
		tkfrItem.setCONFIG_ID("15");
		tkfrItem.setINST_ID("15");
		tkfrList.add(tkfrItem);

		tkfrItem = new TkonfigurationrefItem();
		tkfrItem.setPOSEX("20");
		tkfrItem.setCONFIG_ID("20");
		tkfrItem.setINST_ID("20");
		tkfrList.add(tkfrItem);

		List<TkonfigurationinstItem> tkfiList = new ArrayList<>();
		root.setTKONFIGURATION_INST(tkfiList);

		TkonfigurationinstItem tkfiItem = new TkonfigurationinstItem();
		tkfiItem.setCONFIG_ID("5");
		tkfiItem.setINST_ID("5");
		tkfiList.add(tkfiItem);

		tkfiItem = new TkonfigurationinstItem();
		tkfiItem.setCONFIG_ID("10");
		tkfiItem.setINST_ID("10");
		tkfiList.add(tkfiItem);

		tkfiItem = new TkonfigurationinstItem();
		tkfiItem.setCONFIG_ID("15");
		tkfiItem.setINST_ID("15");
		tkfiList.add(tkfiItem);

		tkfiItem = new TkonfigurationinstItem();
		tkfiItem.setCONFIG_ID("20");
		tkfiItem.setINST_ID("20");
		tkfiList.add(tkfiItem);

		List<TextensioninItem> textList = new ArrayList<>();
		root.setTEXTENSIONIN(textList);

		TextensioninItem textItem = new TextensioninItem();
		textItem.setSTRUCTURE("BAPE_VBAK");
		textItem.setVALUEPART1("000000");
		textItem.setVALUEPART2("ZZMLST_0");
		textItem.setVALUEPART3("20161121");
		textList.add(textItem);

		textItem = new TextensioninItem();
		textItem.setSTRUCTURE("BAPE_VBAP");
		textItem.setVALUEPART1("0000000000000010");
		textItem.setVALUEPART2("ZZ_REF_POSNR");
		textItem.setVALUEPART3("000005");
		textList.add(textItem);

//		textItem = new TextensioninItem();
//		textItem.setSTRUCTURE("BAPE_VBAP");
//		textItem.setVALUEPART1("0000000000000020");
//		textItem.setVALUEPART2("ZZ_REF_POSNR");
//		textItem.setVALUEPART3("000015");
//		textList.add(textItem);

		// XStream xstream = new XStream(new DomDriver());
		XStream xstream = new XStream(new DomDriver("UTF-8", new XmlFriendlyNameCoder("-_", "_")));

		xstream.alias("ns:_-APICON_-VBE_BAPI", RootBean.class);
		xstream.alias("item", TpartnerItem.class);
		xstream.alias("item", TkonditionenItem.class);
		xstream.alias("item", TpositionenItem.class);
		xstream.alias("item", TeinteilungenItem.class);
		xstream.alias("item", TmerkmalswerteItem.class);
		xstream.alias("item", TkonfigurationItem.class);
		xstream.alias("item", TkonfigurationrefItem.class);
		xstream.alias("item", TkonfigurationinstItem.class);
		xstream.alias("item", TextensioninItem.class);
		xstream.useAttributeFor(RootBean.class, "xmlnsns");

		// String xml = xstream.toXML(root);
		// System.out.println(xml);
		//
		// System.out.println("====================================================");
		//
		// xml = xml.replaceAll("\r","").replaceAll("\n","");
		// System.out.println(xml);
		//
		// System.out.println("====================================================");

		Writer writer = new StringWriter();
		xstream.marshal(root, new CompactWriter(writer, new XmlFriendlyNameCoder("-_", "_")));

		String xmlStr = writer.toString();
		xmlStr = xmlStr.replace("xmlnsns=", "xmlns:ns=");
		xmlStr = "<?xml version=\"1.0\" encoding=\"utf-8\"?>" + xmlStr;

		System.out.println(xmlStr);

		return xmlStr;
	}

	private String xmlcontent = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><ns:_-APICON_-VBE_BAPI xmlns:ns=\"urn:sap-com:document:sap:rfc:functions\"><FFEHLERHANDLING/><FVBELN_IN></FVBELN_IN><FRCODE/><FRTEXT/><SHEADER><DOC_TYPE>YFLO</DOC_TYPE><SALES_ORG>298</SALES_ORG><DISTR_CHAN>01</DISTR_CHAN><DIVISION>01</DIVISION><SALES_OFF>GKFJ</SALES_OFF><INCOTERMS1>SIT</INCOTERMS1><INCOTERMS2>XIAMEN</INCOTERMS2><REQ_DATE_H>2018-11-21</REQ_DATE_H><DATE_TYPE>T</DATE_TYPE><PURCH_DATE>2016-11-21</PURCH_DATE><PMNTTRMS/><PURCH_NO_C>GKFJ20161121</PURCH_NO_C><PP_SEARCH>彬依奴（厦门）休闲服饰</PP_SEARCH><CUST_GRP1/></SHEADER><SLOGIC_SWITCH><PRICING>G</PRICING></SLOGIC_SWITCH><TPARTNER><item><PARTN_ROLE>AG</PARTN_ROLE><PARTN_NUMB>12522365</PARTN_NUMB></item><item><PARTN_ROLE>VE</PARTN_ROLE><PARTN_NUMB>28500183</PARTN_NUMB></item><item><PARTN_ROLE>YK</PARTN_ROLE><PARTN_NUMB>12522365</PARTN_NUMB></item></TPARTNER><TKONDITIONEN><item><ITM_NUMBER>5</ITM_NUMBER><COND_TYPE>ZG04</COND_TYPE><COND_VALUE>159363</COND_VALUE><CURRENCY>RMB</CURRENCY></item><item><ITM_NUMBER>10</ITM_NUMBER><COND_TYPE>ZG04</COND_TYPE><COND_VALUE>33258</COND_VALUE><CURRENCY>RMB</CURRENCY></item><item><ITM_NUMBER>15</ITM_NUMBER><COND_TYPE>ZG04</COND_TYPE><COND_VALUE>219200</COND_VALUE><CURRENCY>RMB</CURRENCY></item><item><ITM_NUMBER>20</ITM_NUMBER><COND_TYPE>ZG04</COND_TYPE><COND_VALUE>19868</COND_VALUE><CURRENCY>RMB</CURRENCY></item></TKONDITIONEN><TPOSITIONEN><item><ITM_NUMBER>5</ITM_NUMBER><HG_LV_ITEM/><PO_ITM_NO>5</PO_ITM_NO><MATERIAL>KM_GM23K_FL</MATERIAL><SHORT_TEXT>L1</SHORT_TEXT><PO_METHOD>ZCTC</PO_METHOD><SALES_DIST/><DIVISION/></item><item><ITM_NUMBER>10</ITM_NUMBER><HG_LV_ITEM/><PO_ITM_NO>10</PO_ITM_NO><MATERIAL>GK_INST_ESC</MATERIAL><SHORT_TEXT>L1-INST</SHORT_TEXT><PO_METHOD>ZCTC</PO_METHOD><SALES_DIST/><DIVISION/></item><item><ITM_NUMBER>15</ITM_NUMBER><HG_LV_ITEM/><PO_ITM_NO>15</PO_ITM_NO><MATERIAL>KM_GM23K_FL</MATERIAL><SHORT_TEXT>L2</SHORT_TEXT><PO_METHOD>ZCTC</PO_METHOD><SALES_DIST/><DIVISION/></item><item><ITM_NUMBER>20</ITM_NUMBER><HG_LV_ITEM/><PO_ITM_NO>20</PO_ITM_NO><MATERIAL>GK_INST_ESC</MATERIAL><SHORT_TEXT>L2-INST</SHORT_TEXT><PO_METHOD>ZCTC</PO_METHOD><SALES_DIST/><DIVISION/></item></TPOSITIONEN><TEINTEILUNGEN><item><ITM_NUMBER>5</ITM_NUMBER><REQ_QTY>1</REQ_QTY></item><item><ITM_NUMBER>10</ITM_NUMBER><REQ_QTY>1</REQ_QTY></item><item><ITM_NUMBER>15</ITM_NUMBER><REQ_QTY>1</REQ_QTY></item><item><ITM_NUMBER>20</ITM_NUMBER><REQ_QTY>1</REQ_QTY></item></TEINTEILUNGEN><TMERKMALSWERTE><item><CONFIG_ID>5</CONFIG_ID><INST_ID>5</INST_ID><CHARC>KCO_RUSH_TECH_PLATFORM</CHARC><VALUE>GM3</VALUE></item><item><CONFIG_ID>5</CONFIG_ID><INST_ID>5</INST_ID><CHARC>KCO_RUSH_BUILDING_STATE</CHARC><VALUE>N</VALUE></item><item><CONFIG_ID>5</CONFIG_ID><INST_ID>5</INST_ID><CHARC>KCO_RUSH_BUSINESS_TYPE</CHARC><VALUE>RES</VALUE></item><item><CONFIG_ID>5</CONFIG_ID><INST_ID>5</INST_ID><CHARC>CN_GK_SO_NUMBER</CHARC><VALUE>GK2016-00198</VALUE></item><item><CONFIG_ID>5</CONFIG_ID><INST_ID>5</INST_ID><CHARC>CN_GK_INST_COMPANY</CHARC><VALUE>厦门宏菱电梯有限公司</VALUE></item><item><CONFIG_ID>5</CONFIG_ID><INST_ID>5</INST_ID><CHARC>CN_GK_INST_TEAM</CHARC><VALUE>KHB-21775</VALUE></item><item><CONFIG_ID>5</CONFIG_ID><INST_ID>5</INST_ID><CHARC>CN_GK_REF_SALES_OFFICE</CHARC><VALUE>GKFJ</VALUE></item><item><CONFIG_ID>5</CONFIG_ID><INST_ID>5</INST_ID><CHARC>CN_GK_PRODUCT_CODE</CHARC><VALUE>GM23K</VALUE></item><item><CONFIG_ID>5</CONFIG_ID><INST_ID>5</INST_ID><CHARC>CN_GK_INST_CONTRACT</CHARC><VALUE>A</VALUE></item><item><CONFIG_ID>5</CONFIG_ID><INST_ID>5</INST_ID><CHARC>KCSM_WARRANTY_PERIOD</CHARC><VALUE>12</VALUE></item><item><CONFIG_ID>5</CONFIG_ID><INST_ID>5</INST_ID><CHARC>KCSM_WARRANTY_DURATION</CHARC><VALUE>12</VALUE></item><item><CONFIG_ID>5</CONFIG_ID><INST_ID>5</INST_ID><CHARC>TYP_PROCESS</CHARC><VALUE>A</VALUE></item><item><CONFIG_ID>5</CONFIG_ID><INST_ID>5</INST_ID><CHARC>CN_GK_INST_DOWNLOADING</CHARC><VALUE>0</VALUE></item><item><CONFIG_ID>5</CONFIG_ID><INST_ID>5</INST_ID><CHARC>CN_GK_INST_HOISTING</CHARC><VALUE>2</VALUE></item><item><CONFIG_ID>5</CONFIG_ID><INST_ID>5</INST_ID><CHARC>CN_GK_INST_SCAFOLDING</CHARC><VALUE>0</VALUE></item><item><CONFIG_ID>5</CONFIG_ID><INST_ID>5</INST_ID><CHARC>CN_GK_INST_INSPECTION</CHARC><VALUE>2</VALUE></item><item><CONFIG_ID>5</CONFIG_ID><INST_ID>5</INST_ID><CHARC>CN_GK_INST_ELECTRICITY</CHARC><VALUE>1</VALUE></item><item><CONFIG_ID>5</CONFIG_ID><INST_ID>5</INST_ID><CHARC>CN_GK_INST_OTHER</CHARC><VALUE>1</VALUE></item><item><CONFIG_ID>5</CONFIG_ID><INST_ID>5</INST_ID><CHARC>KCSM_WARRANTY_INCLUDED</CHARC><VALUE>1</VALUE></item><item><CONFIG_ID>5</CONFIG_ID><INST_ID>5</INST_ID><CHARC>TYP_SAPVC_LOCK</CHARC><VALUE>1</VALUE></item><item><CONFIG_ID>5</CONFIG_ID><INST_ID>5</INST_ID><CHARC>TYP_CHECK_CHAR</CHARC><VALUE>1</VALUE></item><item><CONFIG_ID>5</CONFIG_ID><INST_ID>5</INST_ID><CHARC>2100</CHARC><VALUE>96550.30</VALUE></item><item><CONFIG_ID>5</CONFIG_ID><INST_ID>5</INST_ID><CHARC>SLTP</CHARC><VALUE>78677.25</VALUE></item><item><CONFIG_ID>5</CONFIG_ID><INST_ID>5</INST_ID><CHARC>SLEN</CHARC><VALUE>50.50</VALUE></item><item><CONFIG_ID>5</CONFIG_ID><INST_ID>5</INST_ID><CHARC>SLTR</CHARC><VALUE>11414.92</VALUE></item><item><CONFIG_ID>5</CONFIG_ID><INST_ID>5</INST_ID><CHARC>SLGA</CHARC><VALUE>1810.00</VALUE></item><item><CONFIG_ID>5</CONFIG_ID><INST_ID>5</INST_ID><CHARC>2300</CHARC><VALUE>4320.00</VALUE></item><item><CONFIG_ID>5</CONFIG_ID><INST_ID>5</INST_ID><CHARC>3100</CHARC><VALUE>7514.00</VALUE></item><item><CONFIG_ID>5</CONFIG_ID><INST_ID>5</INST_ID><CHARC>3410</CHARC><VALUE>76.00</VALUE></item><item><CONFIG_ID>5</CONFIG_ID><INST_ID>5</INST_ID><CHARC>3105</CHARC><VALUE>3203.88</VALUE></item><item><CONFIG_ID>5</CONFIG_ID><INST_ID>5</INST_ID><CHARC>3300</CHARC><VALUE>5272.79</VALUE></item><item><CONFIG_ID>5</CONFIG_ID><INST_ID>5</INST_ID><CHARC>TYP_ESC_INCLINATION</CHARC><VALUE>1</VALUE></item><item><CONFIG_ID>5</CONFIG_ID><INST_ID>5</INST_ID><CHARC>COL_ESC_ALU_STEP</CHARC><VALUE>1</VALUE></item><item><CONFIG_ID>5</CONFIG_ID><INST_ID>5</INST_ID><CHARC>DIM_ESC_BA_HEIGHT</CHARC><VALUE>900</VALUE></item><item><CONFIG_ID>5</CONFIG_ID><INST_ID>5</INST_ID><CHARC>DIM_ESC_TRUSS_TO_TRUSS</CHARC><VALUE>0</VALUE></item><item><CONFIG_ID>5</CONFIG_ID><INST_ID>5</INST_ID><CHARC>DIM_ESC_STEP_WIDTH</CHARC><VALUE>1000</VALUE></item><item><CONFIG_ID>5</CONFIG_ID><INST_ID>5</INST_ID><CHARC>TYP_LOAD_CONDITION</CHARC><VALUE>1</VALUE></item><item><CONFIG_ID>5</CONFIG_ID><INST_ID>5</INST_ID><CHARC>TYP_ELECTRICAL_SYSTEM_ESC</CHARC><VALUE>1</VALUE></item><item><CONFIG_ID>5</CONFIG_ID><INST_ID>5</INST_ID><CHARC>MAT_ESC_ACCESS_COVER_PLATE</CHARC><VALUE>H</VALUE></item><item><CONFIG_ID>5</CONFIG_ID><INST_ID>5</INST_ID><CHARC>MAT_ESC_HANDRAIL_PROFILE</CHARC><VALUE>4</VALUE></item><item><CONFIG_ID>5</CONFIG_ID><INST_ID>5</INST_ID><CHARC>MAT_ESC_SKIRT</CHARC><VALUE>6</VALUE></item><item><CONFIG_ID>5</CONFIG_ID><INST_ID>5</INST_ID><CHARC>VAL_MAX_CAPACITY</CHARC><VALUE>6000</VALUE></item><item><CONFIG_ID>5</CONFIG_ID><INST_ID>5</INST_ID><CHARC>DIM_ESC_VERTICAL_RISE</CHARC><VALUE>3200</VALUE></item><item><CONFIG_ID>5</CONFIG_ID><INST_ID>5</INST_ID><CHARC>TYP_ESC_ALU_STEP_DEMARCATION</CHARC><VALUE>0</VALUE></item><item><CONFIG_ID>5</CONFIG_ID><INST_ID>5</INST_ID><CHARC>TYP_ESC_ARRANGEMENT</CHARC><VALUE>1</VALUE></item><item><CONFIG_ID>5</CONFIG_ID><INST_ID>5</INST_ID><CHARC>TYP_ESC_AUTO_LUBRICATION</CHARC><VALUE>0</VALUE></item><item><CONFIG_ID>5</CONFIG_ID><INST_ID>5</INST_ID><CHARC>TYP_ESC_AUX_BRAKE</CHARC><VALUE>0</VALUE></item><item><CONFIG_ID>5</CONFIG_ID><INST_ID>5</INST_ID><CHARC>TYP_ESC_BRAKE_LINE_WEAR_MON</CHARC><VALUE>0</VALUE></item><item><CONFIG_ID>5</CONFIG_ID><INST_ID>5</INST_ID><CHARC>TYP_ESC_BUZZER_ALARM</CHARC><VALUE>0</VALUE></item><item><CONFIG_ID>5</CONFIG_ID><INST_ID>5</INST_ID><CHARC>TYP_ESC_CENTER_DECK</CHARC><VALUE>0</VALUE></item><item><CONFIG_ID>5</CONFIG_ID><INST_ID>5</INST_ID><CHARC>TYP_ESC_COMB_HEATING</CHARC><VALUE>0</VALUE></item><item><CONFIG_ID>5</CONFIG_ID><INST_ID>5</INST_ID><CHARC>TYP_ESC_COMB_LIGHT</CHARC><VALUE>0</VALUE></item><item><CONFIG_ID>5</CONFIG_ID><INST_ID>5</INST_ID><CHARC>TYP_ESC_ENVIRONMENT</CHARC><VALUE>1</VALUE></item><item><CONFIG_ID>5</CONFIG_ID><INST_ID>5</INST_ID><CHARC>TYP_ESC_FAULT_DISPLAY</CHARC><VALUE>0</VALUE></item><item><CONFIG_ID>5</CONFIG_ID><INST_ID>5</INST_ID><CHARC>TYP_ESC_GB_VERSION</CHARC><VALUE>2</VALUE></item><item><CONFIG_ID>5</CONFIG_ID><INST_ID>5</INST_ID><CHARC>TYP_ESC_GEAR_BOX</CHARC><VALUE>1</VALUE></item><item><CONFIG_ID>5</CONFIG_ID><INST_ID>5</INST_ID><CHARC>TYP_ESC_HANDRAIL_SUPPLIER</CHARC><VALUE>1</VALUE></item><item><CONFIG_ID>5</CONFIG_ID><INST_ID>5</INST_ID><CHARC>TYP_ESC_HORIZONTAL_PALLET</CHARC><VALUE>6</VALUE></item><item><CONFIG_ID>5</CONFIG_ID><INST_ID>5</INST_ID><CHARC>TYP_ESC_MONITOR_CONTACT</CHARC><VALUE>0</VALUE></item><item><CONFIG_ID>5</CONFIG_ID><INST_ID>5</INST_ID><CHARC>TYP_ESC_OPERATION_MODE</CHARC><VALUE>1</VALUE></item><item><CONFIG_ID>5</CONFIG_ID><INST_ID>5</INST_ID><CHARC>TYP_ESC_SKIRT_DEFLECTOR</CHARC><VALUE>0</VALUE></item><item><CONFIG_ID>5</CONFIG_ID><INST_ID>5</INST_ID><CHARC>TYP_ESC_SKIRT_SWITCH</CHARC><VALUE>0</VALUE></item><item><CONFIG_ID>5</CONFIG_ID><INST_ID>5</INST_ID><CHARC>TYP_ESC_TR_HEATING</CHARC><VALUE>0</VALUE></item><item><CONFIG_ID>5</CONFIG_ID><INST_ID>5</INST_ID><CHARC>TYP_ESC_TRAFFIC_LIGHT</CHARC><VALUE>0</VALUE></item><item><CONFIG_ID>5</CONFIG_ID><INST_ID>5</INST_ID><CHARC>TYP_ESC_UNDERSTEP_LIGHT</CHARC><VALUE>0</VALUE></item><item><CONFIG_ID>5</CONFIG_ID><INST_ID>5</INST_ID><CHARC>TYP_ESC_VOICE_BEFORE_EXIT</CHARC><VALUE>0</VALUE></item><item><CONFIG_ID>5</CONFIG_ID><INST_ID>5</INST_ID><CHARC>TYP_ESC_WRONG_DIRECT_WARNING</CHARC><VALUE>0</VALUE></item><item><CONFIG_ID>5</CONFIG_ID><INST_ID>5</INST_ID><CHARC>VAL_ESC_SPEED</CHARC><VALUE>0.5</VALUE></item><item><CONFIG_ID>5</CONFIG_ID><INST_ID>5</INST_ID><CHARC>TYP_LOGO_COMPANY</CHARC><VALUE>GK</VALUE></item><item><CONFIG_ID>10</CONFIG_ID><INST_ID>10</INST_ID><CHARC>KCO_RUSH_TECH_PLATFORM</CHARC><VALUE>GM3</VALUE></item><item><CONFIG_ID>10</CONFIG_ID><INST_ID>10</INST_ID><CHARC>CN_GK_BUSINESS_PLACE</CHARC><VALUE></VALUE></item><item><CONFIG_ID>10</CONFIG_ID><INST_ID>10</INST_ID><CHARC>KCO_RUSH_BUILDING_STATE</CHARC><VALUE>N</VALUE></item><item><CONFIG_ID>10</CONFIG_ID><INST_ID>10</INST_ID><CHARC>KCO_RUSH_BUSINESS_TYPE</CHARC><VALUE>RES</VALUE></item><item><CONFIG_ID>15</CONFIG_ID><INST_ID>15</INST_ID><CHARC>KCO_RUSH_TECH_PLATFORM</CHARC><VALUE>GM3</VALUE></item><item><CONFIG_ID>15</CONFIG_ID><INST_ID>15</INST_ID><CHARC>KCO_RUSH_BUILDING_STATE</CHARC><VALUE>N</VALUE></item><item><CONFIG_ID>15</CONFIG_ID><INST_ID>15</INST_ID><CHARC>KCO_RUSH_BUSINESS_TYPE</CHARC><VALUE>RES</VALUE></item><item><CONFIG_ID>15</CONFIG_ID><INST_ID>15</INST_ID><CHARC>CN_GK_SO_NUMBER</CHARC><VALUE>GK2016-00198</VALUE></item><item><CONFIG_ID>15</CONFIG_ID><INST_ID>15</INST_ID><CHARC>CN_GK_INST_COMPANY</CHARC><VALUE>厦门宏菱电梯有限公司</VALUE></item><item><CONFIG_ID>15</CONFIG_ID><INST_ID>15</INST_ID><CHARC>CN_GK_INST_TEAM</CHARC><VALUE>KHB-21775</VALUE></item><item><CONFIG_ID>15</CONFIG_ID><INST_ID>15</INST_ID><CHARC>CN_GK_REF_SALES_OFFICE</CHARC><VALUE>GKFJ</VALUE></item><item><CONFIG_ID>15</CONFIG_ID><INST_ID>15</INST_ID><CHARC>CN_GK_PRODUCT_CODE</CHARC><VALUE>GM23K</VALUE></item><item><CONFIG_ID>15</CONFIG_ID><INST_ID>15</INST_ID><CHARC>CN_GK_INST_CONTRACT</CHARC><VALUE>A</VALUE></item><item><CONFIG_ID>15</CONFIG_ID><INST_ID>15</INST_ID><CHARC>KCSM_WARRANTY_PERIOD</CHARC><VALUE>12</VALUE></item><item><CONFIG_ID>15</CONFIG_ID><INST_ID>15</INST_ID><CHARC>KCSM_WARRANTY_DURATION</CHARC><VALUE>12</VALUE></item><item><CONFIG_ID>15</CONFIG_ID><INST_ID>15</INST_ID><CHARC>TYP_PROCESS</CHARC><VALUE>C</VALUE></item><item><CONFIG_ID>15</CONFIG_ID><INST_ID>15</INST_ID><CHARC>CN_GK_INST_DOWNLOADING</CHARC><VALUE>0</VALUE></item><item><CONFIG_ID>15</CONFIG_ID><INST_ID>15</INST_ID><CHARC>CN_GK_INST_HOISTING</CHARC><VALUE>2</VALUE></item><item><CONFIG_ID>15</CONFIG_ID><INST_ID>15</INST_ID><CHARC>CN_GK_INST_SCAFOLDING</CHARC><VALUE>0</VALUE></item><item><CONFIG_ID>15</CONFIG_ID><INST_ID>15</INST_ID><CHARC>CN_GK_INST_INSPECTION</CHARC><VALUE>2</VALUE></item><item><CONFIG_ID>15</CONFIG_ID><INST_ID>15</INST_ID><CHARC>CN_GK_INST_ELECTRICITY</CHARC><VALUE>1</VALUE></item><item><CONFIG_ID>15</CONFIG_ID><INST_ID>15</INST_ID><CHARC>CN_GK_INST_OTHER</CHARC><VALUE>1</VALUE></item><item><CONFIG_ID>15</CONFIG_ID><INST_ID>15</INST_ID><CHARC>KCSM_WARRANTY_INCLUDED</CHARC><VALUE>1</VALUE></item><item><CONFIG_ID>15</CONFIG_ID><INST_ID>15</INST_ID><CHARC>TYP_SAPVC_LOCK</CHARC><VALUE>1</VALUE></item><item><CONFIG_ID>15</CONFIG_ID><INST_ID>15</INST_ID><CHARC>TYP_CHECK_CHAR</CHARC><VALUE>1</VALUE></item><item><CONFIG_ID>15</CONFIG_ID><INST_ID>15</INST_ID><CHARC>2100</CHARC><VALUE>81027.27</VALUE></item><item><CONFIG_ID>15</CONFIG_ID><INST_ID>15</INST_ID><CHARC>SLTP</CHARC><VALUE>74853.83</VALUE></item><item><CONFIG_ID>15</CONFIG_ID><INST_ID>15</INST_ID><CHARC>SLEN</CHARC><VALUE>505.00</VALUE></item><item><CONFIG_ID>15</CONFIG_ID><INST_ID>15</INST_ID><CHARC>SLTR</CHARC><VALUE>0.00</VALUE></item><item><CONFIG_ID>15</CONFIG_ID><INST_ID>15</INST_ID><CHARC>SLGA</CHARC><VALUE>1810.00</VALUE></item><item><CONFIG_ID>15</CONFIG_ID><INST_ID>15</INST_ID><CHARC>2300</CHARC><VALUE>4320.00</VALUE></item><item><CONFIG_ID>15</CONFIG_ID><INST_ID>15</INST_ID><CHARC>3100</CHARC><VALUE>7950.00</VALUE></item><item><CONFIG_ID>15</CONFIG_ID><INST_ID>15</INST_ID><CHARC>3410</CHARC><VALUE>80.00</VALUE></item><item><CONFIG_ID>15</CONFIG_ID><INST_ID>15</INST_ID><CHARC>TYP_ESC_INCLINATION</CHARC><VALUE>3</VALUE></item><item><CONFIG_ID>15</CONFIG_ID><INST_ID>15</INST_ID><CHARC>COL_ESC_ALU_STEP</CHARC><VALUE>1</VALUE></item><item><CONFIG_ID>15</CONFIG_ID><INST_ID>15</INST_ID><CHARC>DIM_ESC_BA_HEIGHT</CHARC><VALUE>900</VALUE></item><item><CONFIG_ID>15</CONFIG_ID><INST_ID>15</INST_ID><CHARC>DIM_ESC_TRUSS_TO_TRUSS</CHARC><VALUE>0</VALUE></item><item><CONFIG_ID>15</CONFIG_ID><INST_ID>15</INST_ID><CHARC>DIM_ESC_STEP_WIDTH</CHARC><VALUE>1000</VALUE></item><item><CONFIG_ID>15</CONFIG_ID><INST_ID>15</INST_ID><CHARC>TYP_LOAD_CONDITION</CHARC><VALUE>1</VALUE></item><item><CONFIG_ID>15</CONFIG_ID><INST_ID>15</INST_ID><CHARC>TYP_ELECTRICAL_SYSTEM_ESC</CHARC><VALUE>1</VALUE></item><item><CONFIG_ID>15</CONFIG_ID><INST_ID>15</INST_ID><CHARC>MAT_ESC_ACCESS_COVER_PLATE</CHARC><VALUE>H</VALUE></item><item><CONFIG_ID>15</CONFIG_ID><INST_ID>15</INST_ID><CHARC>MAT_ESC_HANDRAIL_PROFILE</CHARC><VALUE>4</VALUE></item><item><CONFIG_ID>15</CONFIG_ID><INST_ID>15</INST_ID><CHARC>MAT_ESC_SKIRT</CHARC><VALUE>6</VALUE></item><item><CONFIG_ID>15</CONFIG_ID><INST_ID>15</INST_ID><CHARC>VAL_MAX_CAPACITY</CHARC><VALUE>6000</VALUE></item><item><CONFIG_ID>15</CONFIG_ID><INST_ID>15</INST_ID><CHARC>DIM_ESC_VERTICAL_RISE</CHARC><VALUE>3200</VALUE></item><item><CONFIG_ID>15</CONFIG_ID><INST_ID>15</INST_ID><CHARC>TYP_ESC_ALU_STEP_DEMARCATION</CHARC><VALUE>0</VALUE></item><item><CONFIG_ID>15</CONFIG_ID><INST_ID>15</INST_ID><CHARC>TYP_ESC_ARRANGEMENT</CHARC><VALUE>1</VALUE></item><item><CONFIG_ID>15</CONFIG_ID><INST_ID>15</INST_ID><CHARC>TYP_ESC_AUTO_LUBRICATION</CHARC><VALUE>0</VALUE></item><item><CONFIG_ID>15</CONFIG_ID><INST_ID>15</INST_ID><CHARC>TYP_ESC_AUX_BRAKE</CHARC><VALUE>0</VALUE></item><item><CONFIG_ID>15</CONFIG_ID><INST_ID>15</INST_ID><CHARC>TYP_ESC_BRAKE_LINE_WEAR_MON</CHARC><VALUE>0</VALUE></item><item><CONFIG_ID>15</CONFIG_ID><INST_ID>15</INST_ID><CHARC>TYP_ESC_BUZZER_ALARM</CHARC><VALUE>0</VALUE></item><item><CONFIG_ID>15</CONFIG_ID><INST_ID>15</INST_ID><CHARC>TYP_ESC_CENTER_DECK</CHARC><VALUE>0</VALUE></item><item><CONFIG_ID>15</CONFIG_ID><INST_ID>15</INST_ID><CHARC>TYP_ESC_COMB_HEATING</CHARC><VALUE>0</VALUE></item><item><CONFIG_ID>15</CONFIG_ID><INST_ID>15</INST_ID><CHARC>TYP_ESC_ENVIRONMENT</CHARC><VALUE>1</VALUE></item><item><CONFIG_ID>15</CONFIG_ID><INST_ID>15</INST_ID><CHARC>TYP_ESC_FAULT_DISPLAY</CHARC><VALUE>0</VALUE></item><item><CONFIG_ID>15</CONFIG_ID><INST_ID>15</INST_ID><CHARC>TYP_ESC_GB_VERSION</CHARC><VALUE>2</VALUE></item><item><CONFIG_ID>15</CONFIG_ID><INST_ID>15</INST_ID><CHARC>TYP_ESC_GEAR_BOX</CHARC><VALUE>1</VALUE></item><item><CONFIG_ID>15</CONFIG_ID><INST_ID>15</INST_ID><CHARC>TYP_ESC_HANDRAIL_SUPPLIER</CHARC><VALUE>1</VALUE></item><item><CONFIG_ID>15</CONFIG_ID><INST_ID>15</INST_ID><CHARC>TYP_ESC_HORIZONTAL_PALLET</CHARC><VALUE>6</VALUE></item><item><CONFIG_ID>15</CONFIG_ID><INST_ID>15</INST_ID><CHARC>TYP_ESC_MONITOR_CONTACT</CHARC><VALUE>0</VALUE></item><item><CONFIG_ID>15</CONFIG_ID><INST_ID>15</INST_ID><CHARC>TYP_ESC_OPERATION_MODE</CHARC><VALUE>1</VALUE></item><item><CONFIG_ID>15</CONFIG_ID><INST_ID>15</INST_ID><CHARC>TYP_ESC_SKIRT_DEFLECTOR</CHARC><VALUE>0</VALUE></item><item><CONFIG_ID>15</CONFIG_ID><INST_ID>15</INST_ID><CHARC>TYP_ESC_SKIRT_SWITCH</CHARC><VALUE>0</VALUE></item><item><CONFIG_ID>15</CONFIG_ID><INST_ID>15</INST_ID><CHARC>TYP_ESC_TR_HEATING</CHARC><VALUE>0</VALUE></item><item><CONFIG_ID>15</CONFIG_ID><INST_ID>15</INST_ID><CHARC>TYP_ESC_TRAFFIC_LIGHT</CHARC><VALUE>0</VALUE></item><item><CONFIG_ID>15</CONFIG_ID><INST_ID>15</INST_ID><CHARC>TYP_ESC_UNDERSTEP_LIGHT</CHARC><VALUE>0</VALUE></item><item><CONFIG_ID>15</CONFIG_ID><INST_ID>15</INST_ID><CHARC>TYP_ESC_VOICE_BEFORE_EXIT</CHARC><VALUE>0</VALUE></item><item><CONFIG_ID>15</CONFIG_ID><INST_ID>15</INST_ID><CHARC>TYP_ESC_WRONG_DIRECT_WARNING</CHARC><VALUE>0</VALUE></item><item><CONFIG_ID>15</CONFIG_ID><INST_ID>15</INST_ID><CHARC>VAL_ESC_SPEED</CHARC><VALUE>0.5</VALUE></item><item><CONFIG_ID>15</CONFIG_ID><INST_ID>15</INST_ID><CHARC>TYP_LOGO_COMPANY</CHARC><VALUE>GK</VALUE></item><item><CONFIG_ID>20</CONFIG_ID><INST_ID>20</INST_ID><CHARC>KCO_RUSH_TECH_PLATFORM</CHARC><VALUE>GM3</VALUE></item><item><CONFIG_ID>20</CONFIG_ID><INST_ID>20</INST_ID><CHARC>CN_GK_BUSINESS_PLACE</CHARC><VALUE></VALUE></item><item><CONFIG_ID>20</CONFIG_ID><INST_ID>20</INST_ID><CHARC>KCO_RUSH_BUILDING_STATE</CHARC><VALUE>N</VALUE></item><item><CONFIG_ID>20</CONFIG_ID><INST_ID>20</INST_ID><CHARC>KCO_RUSH_BUSINESS_TYPE</CHARC><VALUE>RES</VALUE></item></TMERKMALSWERTE><TKONFIGURATION><item><POSEX>5</POSEX><CONFIG_ID>5</CONFIG_ID><ROOT_ID>5</ROOT_ID><COMPLETE></COMPLETE><CONSISTENT></CONSISTENT></item><item><POSEX>10</POSEX><CONFIG_ID>10</CONFIG_ID><ROOT_ID>10</ROOT_ID><COMPLETE></COMPLETE><CONSISTENT></CONSISTENT></item><item><POSEX>15</POSEX><CONFIG_ID>15</CONFIG_ID><ROOT_ID>15</ROOT_ID><COMPLETE></COMPLETE><CONSISTENT></CONSISTENT></item><item><POSEX>20</POSEX><CONFIG_ID>20</CONFIG_ID><ROOT_ID>20</ROOT_ID><COMPLETE></COMPLETE><CONSISTENT></CONSISTENT></item></TKONFIGURATION><TKONFIGURATION_REF><item><POSEX>5</POSEX><CONFIG_ID>5</CONFIG_ID><INST_ID>5</INST_ID></item><item><POSEX>10</POSEX><CONFIG_ID>10</CONFIG_ID><INST_ID>10</INST_ID></item><item><POSEX>15</POSEX><CONFIG_ID>15</CONFIG_ID><INST_ID>15</INST_ID></item><item><POSEX>20</POSEX><CONFIG_ID>20</CONFIG_ID><INST_ID>20</INST_ID></item></TKONFIGURATION_REF><TKONFIGURATION_INST><item><CONFIG_ID>5</CONFIG_ID><INST_ID>5</INST_ID><COMPLETE></COMPLETE><CONSISTENT></CONSISTENT></item><item><CONFIG_ID>10</CONFIG_ID><INST_ID>10</INST_ID><COMPLETE></COMPLETE><CONSISTENT></CONSISTENT></item><item><CONFIG_ID>15</CONFIG_ID><INST_ID>15</INST_ID><COMPLETE></COMPLETE><CONSISTENT></CONSISTENT></item><item><CONFIG_ID>20</CONFIG_ID><INST_ID>20</INST_ID><COMPLETE></COMPLETE><CONSISTENT></CONSISTENT></item></TKONFIGURATION_INST><TEXTENSIONIN><item><STRUCTURE>BAPE_VBAK</STRUCTURE><VALUEPART1>000000</VALUEPART1><VALUEPART2>ZZMLST_0</VALUEPART2><VALUEPART3>20161121</VALUEPART3></item><item><STRUCTURE>BAPE_VBAP</STRUCTURE><VALUEPART1>0000000000000010</VALUEPART1><VALUEPART2>ZZ_REF_POSNR</VALUEPART2><VALUEPART3>000005</VALUEPART3></item><item><STRUCTURE>BAPE_VBAP</STRUCTURE><VALUEPART1>0000000000000020</VALUEPART1><VALUEPART2>ZZ_REF_POSNR</VALUEPART2><VALUEPART3>000015</VALUEPART3></item></TEXTENSIONIN></ns:_-APICON_-VBE_BAPI>";

}

