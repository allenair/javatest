package other;

public class WeilingTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		new WeilingTest().findResult();
	}
	
	public void findResult(){
		for(int i=1024; i<2048; i++){
			int tmpScore = 10;
			String tmpStr = Integer.toBinaryString(i).substring(1);
			
			for(int k=0;k<10;k++){
				char c = tmpStr.charAt(k);
				if(c=='0'){
					tmpScore-=(k+1);
				}else{
					tmpScore*=2;
				}
			}
			
			if(tmpScore==100){
				System.out.println(tmpStr);
			}
		}
	}
}
