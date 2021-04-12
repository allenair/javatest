import java.math.BigDecimal;
import java.math.RoundingMode;
import java.security.AlgorithmParameters;
import java.security.Security;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.UUID;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.util.XmlUtil;
import cn.hutool.crypto.SecureUtil;

public class Test2021 {
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String[] aa = "D:\\temp\\pic\\一口钟\\0648-一口钟-001.jpg".split("\\\\");
		System.out.println(aa);
		
		System.out.println(UUID.randomUUID().toString().replaceAll("-", ""));
		System.out.println(UUID.randomUUID().toString().replace("-", ""));
		
//		String sessionKey = "tiihtNczf5v6AKRyjwEUhQ==";
//		String encryptedData = "CiyLU1Aw2KjvrjMdj8YKliAjtP4gsMZM" + "QmRzooG2xrDcvSnxIMXFufNstNGTyaGS"
//				+ "9uT5geRa0W4oTOb1WT7fJlAC+oNPdbB+" + "3hVbJSRgv+4lGOETKUQz6OYStslQ142d"
//				+ "NCuabNPGBzlooOmB231qMM85d2/fV6Ch" + "evvXvQP8Hkue1poOFtnEtpyxVLW1zAo6"
//				+ "/1Xx1COxFvrc2d7UL/lmHInNlxuacJXw" + "u0fjpXfz/YqYzBIBzD6WUfTIF9GRHpOn"
//				+ "/Hz7saL8xz+W//FRAUid1OksQaQx4CMs" + "8LOddcQhULW4ucetDf96JcR3g0gfRK4P"
//				+ "C7E/r7Z6xNrXd2UIeorGj5Ef7b1pJAYB" + "6Y5anaHqZ9J6nKEBvB4DnNLIVWSgARns"
//				+ "/8wR2SiRS7MNACwTyrGvt9ts8p12PKFd" + "lqYTopNHR1Vf7XjfhQlVsAJdNiKdYmYV"
//				+ "oKlaRv85IfVunYzO0IKXsyl7JCUjCpoG" + "20f0a04COwfneQAGGwd5oa+T8yO5hzuy" + "Db/XcxxmK01EpqOyuxINew==";
//		String iv = "r7BXXKkLb8qrSNn05n0qiA==";
//		System.out.println(decodeWxMessage(sessionKey, encryptedData, iv));
		
		test0410();
	}
	
	public static void test0410() {
		String content = "晒干或低温干燥。\u003c/p\u003e \\n\u003cp\u003e\u003cstrong\u003e【性状】\u003c/strong\u003e";
		content = content.replace('\u003c', '<');
		content = content.replace('\u003e', '>');
		
		System.out.println(content);
	}
	public static void test0302() {
		double sum = 12.3456783;
		BigDecimal tt = new BigDecimal(sum).setScale(2,RoundingMode.HALF_UP);
		System.out.println(tt);
		System.out.println(LocalDate.now().getMonthValue());
		System.out.println(LocalDate.now().getYear());
		System.out.println(SecureUtil.sha256("1"));
	}

	public static void test03() {
//		OkHttpClient client = new OkHttpClient();
//		String url = "https://api.weixin.qq.com/sns/jscode2session?appid=wx282692b60e64c51b&secret=1387fbd351021a2a09c0ed758196f641&js_code=073vV40w36r5XV2N1z2w3YXtxX1vV40Y&grant_type=authorization_code";
//		Request request = new Request.Builder().url(url).build();
//		try (Response response = client.newCall(request).execute()) {
//			System.out.println(response.body().string());
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
		
		String xmlStr = "<xml>"
				+ "  <appid><![CDATA[wx2421b1c4370ec43b]]></appid>"
				+ "  <attach><![CDATA[支付测试]]></attach>"
				+ "  <bank_type><![CDATA[CFT]]></bank_type>"
				+ "  <fee_type><![CDATA[CNY]]></fee_type>"
				+ "  <is_subscribe><![CDATA[Y]]></is_subscribe>"
				+ "  <mch_id><![CDATA[10000100]]></mch_id>"
				+ "  <nonce_str><![CDATA[5d2b6c2a8db53831f7eda20af46e531c]]></nonce_str>"
				+ "  <openid><![CDATA[oUpF8uMEb4qRXf22hE3X68TekukE]]></openid>"
				+ "  <out_trade_no><![CDATA[1409811653]]></out_trade_no>"
				+ "  <result_code><![CDATA[SUCCESS]]></result_code>"
				+ "  <return_code><![CDATA[SUCCESS]]></return_code>"
				+ "  <sign><![CDATA[B552ED6B279343CB493C5DD0D78AB241]]></sign>"
				+ "  <time_end><![CDATA[20140903131540]]></time_end>"
				+ "  <total_fee>1</total_fee>"
				+ "  <coupon_fee><![CDATA[10]]></coupon_fee>"
				+ "  <coupon_count><![CDATA[1]]></coupon_count>"
				+ "  <coupon_type><![CDATA[CASH]]></coupon_type>"
				+ "  <coupon_id><![CDATA[10000]]></coupon_id>"
				+ "  <trade_type><![CDATA[JSAPI]]></trade_type>"
				+ "  <transaction_id><![CDATA[1004400740201409030005092168]]></transaction_id>"
				+ "</xml>";
		
		
		xmlStr = "<xml><appid><![CDATA[wx2421b1c4370ec43b]]></appid><attach><![CDATA[支付测试]]></attach><bank_type><![CDATA[CFT]]></bank_type><fee_type><![CDATA[CNY]]></fee_type><is_subscribe><![CDATA[Y]]></is_subscribe><mch_id><![CDATA[10000100]]></mch_id><nonce_str><![CDATA[5d2b6c2a8db53831f7eda20af46e531c]]></nonce_str><openid><![CDATA[oUpF8uMEb4qRXf22hE3X68TekukE]]></openid><out_trade_no><![CDATA[1409811653]]></out_trade_no><result_code><![CDATA[SUCCESS]]></result_code><return_code><![CDATA[SUCCESS]]></return_code><sign><![CDATA[B552ED6B279343CB493C5DD0D78AB241]]></sign><time_end><![CDATA[20140903131540]]></time_end><total_fee>1</total_fee><coupon_fee><![CDATA[10]]></coupon_fee><coupon_count><![CDATA[1]]></coupon_count><coupon_type><![CDATA[CASH]]></coupon_type><coupon_id><![CDATA[10000]]></coupon_id><trade_type><![CDATA[JSAPI]]></trade_type><transaction_id><![CDATA[1004400740201409030005092168]]></transaction_id></xml>";
		
		Document docResult = XmlUtil.parseXml(xmlStr);
		
		System.out.println("appid: " + docResult.getElementsByTagName("appid").item(0).getTextContent());
		System.out.println("result_code: " + docResult.getElementsByTagName("result_code").item(0).getTextContent());
		System.out.println("out_trade_no: " + docResult.getElementsByTagName("out_trade_no").item(0).getTextContent());
		System.out.println("time_end: " + docResult.getElementsByTagName("time_end").item(0).getTextContent());
		
//		System.out.println(docResult.getChildNodes().item(0).getChildNodes().getLength());
		
		NodeList nodeList = docResult.getFirstChild().getChildNodes();
		for(int i=0;i<nodeList.getLength();i++) {
			Node node = nodeList.item(i);
			System.out.println(node.getNodeName()+"====="+node.getTextContent());
		}
	}
	
	public static String decodeWxMessage(String sessionKey, String encryptedData, String iv) {
		 // 被加密的数据
       byte[] dataByte = Base64.decode(encryptedData);
       // 加密秘钥
       byte[] keyByte = Base64.decode(sessionKey);
       // 偏移量
       byte[] ivByte = Base64.decode(iv);
       try {
           // 如果密钥不足16位，那么就补足.  这个if 中的内容很重要
           int base = 16;
           if (keyByte.length % base != 0) {
               int groups = keyByte.length / base + (keyByte.length % base != 0 ? 1 : 0);
               byte[] temp = new byte[groups * base];
               Arrays.fill(temp, (byte) 0);
               System.arraycopy(keyByte, 0, temp, 0, keyByte.length);
               keyByte = temp;
           }
           // 初始化
           Security.addProvider(new BouncyCastleProvider());
           Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
           SecretKeySpec spec = new SecretKeySpec(keyByte, "AES");
           AlgorithmParameters parameters = AlgorithmParameters.getInstance("AES");
           parameters.init(new IvParameterSpec(ivByte));
           cipher.init(Cipher.DECRYPT_MODE, spec, parameters);// 初始化
           byte[] resultByte = cipher.doFinal(dataByte);
           if (null != resultByte && resultByte.length > 0) {
               String result = new String(resultByte, "UTF-8");
               return result;
           }
       } catch (Exception e) {
           e.printStackTrace();
       }
       
       return "";
	}
}
