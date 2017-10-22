package allen.pattern.singleton;

/**
 * ���Ҹ��˸о�������ģʽ�ƺ���̫�����ã���ɱ���̬�����������
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
