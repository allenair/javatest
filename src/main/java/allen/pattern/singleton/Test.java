package allen.pattern.singleton;

public class Test {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Singleton single = Singleton.getInstance();
		single.doSomething();
	}

}
