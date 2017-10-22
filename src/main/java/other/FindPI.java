package other;


public class FindPI {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		long st = System.currentTimeMillis();
		new FindPI().findPI(200000000);
		System.out.println(System.currentTimeMillis() - st);

	}

	public void findPI(int n) {
		double res = 0.0;
		for (int i = 0; i < n; i++) {
			res += 4 * Math.pow(-1, i) / (2 * i + 1);
		}

		System.out.println(res);
	}

}
