package allen.jdk8;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.stream.Stream;

public class TestTwo {

	public static void main(String[] args)  throws Exception {
		// TODO Auto-generated method stub
		test0227();
		test0228();
	}

	public static void test0227() {
		new Thread(()->System.out.println("12")).run();
		System.out.println(Stream.of(1,2,3,4).reduce((x,y)->x+y));
	}
	
	public static void test0228() throws Exception {
		URL site = new URL("http://vertx.io/");
		
		try(BufferedReader in = new BufferedReader(new InputStreamReader(site.openStream()))){
			in.lines().forEach(System.out::println);
		}
	}
}
