package allen.pattern.singleton;

/**
 * 以我个人感觉，单例模式似乎无太大作用，其可被静态方法类所替代
 * */
public class Singleton {
	private static Singleton singleton = new Singleton();
	
	private Singleton(){}
	
	public static Singleton getInstance(){
		return singleton;
	}
	
	public void doSomething(){
		System.out.println("done====");
	}
}
