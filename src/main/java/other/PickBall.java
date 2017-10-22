package other;


public class PickBall {
	private int[] ruleArray = { 8, 7, 3, 1 };

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		new PickBall().test(5-1);
	}

	public void test(int num) {
		if('A'==checkResult(num,'A')){
			System.out.println(1);
		}else{
			System.out.println(0);
		}
	}

	private char checkResult(int num, char person) {
		char res;
		for (int ruleNum : ruleArray) {
			if (num == ruleNum) {
				return person;
			}
			if(num<ruleNum){
				continue;
			}
			res = checkResult(num - ruleNum, person=='A'?'B':'A');
			if(res=='B'){
				return 'B';
			}
		}

		return person;
	}
}
