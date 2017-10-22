package com.zxtech;

import java.io.IOException;
import java.net.URI;
import java.nio.charset.Charset;
import java.util.Base64;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.Credentials;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.AuthCache;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.ClientContext;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.BasicAuthCache;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;

import microsoft.exchange.webservices.data.core.ExchangeService;
import microsoft.exchange.webservices.data.core.enumeration.misc.ExchangeVersion;
import microsoft.exchange.webservices.data.core.service.item.EmailMessage;
import microsoft.exchange.webservices.data.credential.ExchangeCredentials;
import microsoft.exchange.webservices.data.credential.WebCredentials;
import microsoft.exchange.webservices.data.property.complex.MessageBody;

public class KoneHttpMail {

	private String server_url="https://euwebmail.kone.com/ews/exchange.asmx";
	private HttpHost host = new HttpHost("euwebmail.kone.com/ews/exchange.asmx", 443, "https");
	private String user_name="KONENET\\FMB_KONE100";
	private String mail_name="KONE100@KONE.com";
	private String pass_word="Kone-100";
	
	public static void main(String[] args)throws Exception {
//		new KoneHttpMail().sendExchangeMail("jason@kone.com", "Test Mail from ZXtech", "This is a test Mail.\nHow are you.\n\nAllen(Zxtech)");
//		new KoneHttpMail().sss2();
		new KoneHttpMail().ewsTest();
	}
	
	public void ewsTest()throws Exception {
		ExchangeService service = new ExchangeService(ExchangeVersion.Exchange2010_SP2);
		ExchangeCredentials credentials = new WebCredentials("FMB_KONE100", this.pass_word);
		service.setCredentials(credentials);
		service.setUrl(new URI(this.server_url));
		
		EmailMessage msg= new EmailMessage(service);
		msg.setSubject("Test-from-zxtech-0");
		msg.setBody(MessageBody.getMessageBodyFromText(" This is a test Mail.\n How are you.\n\n Allen(Zxtech),(中新) "));
		msg.getToRecipients().add("Jasonhongtao.ma@kone.com");
		msg.send();
		System.out.println("=====FINISHED=====");
	}
	
	public void sss2() {
		try {
			CredentialsProvider provider = new BasicCredentialsProvider();
			UsernamePasswordCredentials credentials = new UsernamePasswordCredentials(this.user_name, this.pass_word);
//			provider.setCredentials(AuthScope.ANY, credentials);
			provider.setCredentials(new AuthScope("euwebmail.kone.com", host.getPort()), credentials);
			HttpClient client = HttpClientBuilder.create().setDefaultCredentialsProvider(provider).build();

			HttpPost post = new HttpPost(this.server_url);

//			post.setHeader("Content-type", "text/xml; charset=utf-8");
//			post.setHeader("User-Agent", "ExchangeServicesClient/0.0.0.0");
//			post.setHeader("Accept", "text/xml");
//			post.setHeader("Keep-Alive", "300");
//			post.setHeader("Connection", "Keep-Alive");
//			post.setHeader("Accept-Encoding", "gzip,deflate");
//			post.setEntity(new StringEntity(getTemplateValue("jason@kone.com", "test", "test"), ContentType.create(ContentType.TEXT_XML.getMimeType(), "UTF-8")));
			
//			HttpResponse response = client.execute(post); 
			HttpResponse response = client.execute(new HttpGet(this.server_url)); 
			int status = response.getStatusLine().getStatusCode();
	        
	        System.out.println(">>>>>status is<<<<<<<\n"+status);
			HttpEntity entity = response.getEntity();
			EntityUtils.consume(entity);
			for (Header head : response.getAllHeaders()) {
				System.out.println(head.getName()+" ==== "+head.getValue());
			}
		} catch (Exception e) {
			e.printStackTrace();
		} 
	}
	
	
	public String sendExchangeMail(String targetAddress, String subject, String contentBody){
		String res="OK";
		CloseableHttpResponse response=null;
		try {
			String realBody = getTemplateValue(targetAddress, subject, contentBody);
			
			CloseableHttpClient httpclient = HttpClients.createDefault();
			
			HttpPost post = new HttpPost(this.server_url);
			post.setHeader("Content-type", "text/xml; charset=utf-8");
			post.setHeader("User-Agent", "ExchangeServicesClient/0.0.0.0");
			post.setHeader("Accept", "text/xml");
			post.setHeader("Keep-Alive", "300");
			post.setHeader("Connection", "Keep-Alive");
			post.setHeader("Accept-Encoding", "gzip,deflate");
			StringEntity entity = new StringEntity(realBody, ContentType.create(ContentType.TEXT_XML.getMimeType(), "UTF-8"));
			entity.setChunked(true);
			post.setEntity(entity);
			
			HttpGet get = new HttpGet(this.server_url);
			get.setHeader("Content-type", "text/xml; charset=utf-8");
			get.setHeader("User-Agent", "ExchangeServicesClient/0.0.0.0");
			get.setHeader("Accept", "text/xml");
			get.setHeader("Keep-Alive", "300");
			get.setHeader("Connection", "Keep-Alive");
			get.setHeader("Accept-Encoding", "gzip,deflate");
			get.setHeader(HttpHeaders.AUTHORIZATION, getBase());
			
//			post.setHeader(HttpHeaders.AUTHORIZATION, getBase());
			
			
			HttpContext context = createBasicAuthContext(this.user_name, this.pass_word);
//		    response = httpclient.execute(get, context);
		    response = httpclient.execute(get);
	    
	        int status = response.getStatusLine().getStatusCode();
	        
	        System.out.println(">>>>>status is<<<<<<<\n"+status);
	        System.out.println(">>>>>header start<<<<<<\n");
	        for (Header head : response.getAllHeaders()) {
				System.out.println(head.getName()+" ==== "+head.getValue());
			}
//	        System.out.println(">>>>>response<<<<<<<<\n"+EntityUtils.toString(response.getEntity(), "UTF-8"));
	    } catch(Exception e){
	    	e.printStackTrace();
	    }finally {
	        try {
				response.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
	        res="ERROR";
	    }
		
		
		return res;
	}

	private String getBase(){
		String auth = this.user_name + ":" + this.pass_word;
		byte[] encodedAuth = org.apache.commons.codec.binary.Base64.encodeBase64(auth.getBytes(Charset.forName("US-ASCII")));
		return "Basic " + new String(encodedAuth);
	}
	
	private HttpClientContext createBasicAuthContext(String username, String password) {
	    CredentialsProvider credsProvider = new BasicCredentialsProvider();
	    credsProvider.setCredentials(
	    		AuthScope.ANY,
//	            new AuthScope(host.getHostName(), host.getPort()),
	            new UsernamePasswordCredentials(username, password));

	    AuthCache authCache = new BasicAuthCache();
	    BasicScheme basicAuth = new BasicScheme();
	    authCache.put(host, basicAuth);
	 
	    HttpClientContext context = HttpClientContext.create();
	    context.setCredentialsProvider(credsProvider);
	    context.setAuthCache(authCache);
	    return context;
	}
	
	private String getTemplateValue(String targetAddress, String subject, String contentBody){
		StringBuilder bodyTemplate = new StringBuilder();
		bodyTemplate.append("<?xml version=\"1.0\" encoding=\"utf-8\"?>");
		bodyTemplate.append("<soap:Envelope xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:xsi=\"http://www.w3.bodyTemplate.append(org/2001/\"XMLSchema-instance\" xmlns:m=\"http://schemas.microsoft.com/exchange/services/2006/messages\" bodyTemplate.append(xmlns:t=\"http://\"schemas.microsoft.com/exchange/services/2006/types\">");
		bodyTemplate.append("<soap:Header>");
		bodyTemplate.append("    <t:RequestServerVersion Version=\"Exchange2010_SP2\">");
		bodyTemplate.append("    </t:RequestServerVersion>");
		bodyTemplate.append("</soap:Header>");
		bodyTemplate.append("<soap:Body>");
		bodyTemplate.append("    <m:CreateItem MessageDisposition=\"SendOnly\">");
		bodyTemplate.append("        <m:Items>");
		bodyTemplate.append("            <t:Message>");
		bodyTemplate.append("                <t:Subject>%s</t:Subject>");
		bodyTemplate.append("                <t:Body BodyType=\"Text\">%s</t:Body>");
		bodyTemplate.append("                <t:ToRecipients>");
		bodyTemplate.append("                    <t:Mailbox><t:EmailAddress>%s</t:EmailAddress></t:Mailbox>" );
		bodyTemplate.append("                </t:ToRecipients>");
		bodyTemplate.append("            </t:Message>");
		bodyTemplate.append("        </m:Items>");
		bodyTemplate.append("    </m:CreateItem>");
		bodyTemplate.append("</soap:Body>");
		bodyTemplate.append("</soap:Envelope>");
		
		return String.format(bodyTemplate.toString(), subject, contentBody, targetAddress);
	}
}
