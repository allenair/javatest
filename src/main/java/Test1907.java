import java.nio.charset.Charset;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import org.apache.commons.lang3.StringUtils;

import com.google.common.hash.Hashing;

public class Test1907 {

	public static void main(String[] args) {
		Test1907 tt = new Test1907();
		
		tt.test0715();
	}
	public void test0715() {
		System.out.println(patternFind("轿厢地板：干板喷涂", "地板|轿底|轿厢地板"));
		System.out.println(LocalTime.now().format(DateTimeFormatter.ofPattern("hh_mm_ss_n")));
		System.out.println((int)Math.round(1.0*14/5));
		System.out.println(String.format("%1.4f", Double.parseDouble("0.123456")));
	}
	public void test0712() {
		int[] arr = {12,11,13,14,15};
		
		String res = String.join(",", Arrays.stream(arr).mapToObj(n->""+n).collect(Collectors.toList()));
		
//		String res = String.join(",", Stream.of(arr).map(n->""+n).collect(Collectors.toList()));
		
		
		String[] sarr = {"aaa","bbb","ccc"};
		res = "('";
		res = res + String.join("','", sarr);
		res = res + "')";
		System.out.println(res);
	}
	
	private String patternFind(String srcStr, String patternStr) {
		if(StringUtils.isEmpty(srcStr) || StringUtils.isEmpty(patternStr)) {
			return "";
		}
		Pattern pattern = Pattern.compile(patternStr);
		Matcher matcher = pattern.matcher(srcStr);
		if (matcher.find()) {
			return matcher.group();
		} 
		return "";
	}
	
	public void test0709() {
		String src = "07/08/2019,Vehicle loan or lease,Loan,Getting a loan or lease,Credit denial,,,MERCEDES BENZ FINANCIAL SERVICES,MO,640XX,,,Web,07/08/2019,In progress,Yes,N/A,3298806 07/08/2019,Debt collection,Medical debt,Attempts to collect debt not owed,Debt is not yours,,Company has responded to the consumer and the CFPB and chooses not to provide a public response,\"HCFS Health Care Financial Services, Inc.\",OK,748XX,Servicemember,Consent not provided,Web,07/08/2019,Closed with explanation,Yes,N/A,3298636";
		String hashStr = Hashing.sha256().hashString(src, Charset.forName("utf-8")).toString();
		System.out.println(hashStr);
		System.out.println(hashStr.length());
	}
	public void test0704() {
		int[] score = new int[10];
		IntStream.range(1, 10).forEach(x->{
			if(x==5) {
				return;
			}
			System.out.println("===="+x);
		});
		System.out.println();
	}
}
