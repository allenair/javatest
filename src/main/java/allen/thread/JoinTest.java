package allen.thread;
/*
 * join()方法的特点
 * 1.当前线程会被挂起，让join 进来的线程执行
 * 2.join进来的线程没有执行完毕之前，会一直阻塞当前线程
 *
 * main方法启动时，就会创建当前java程序主线程
 */
public class JoinTest extends Thread {
	public JoinTest(String name) {
		super(name);
	}

	// 重写run方法
	public void run() {
		// 线程执行代码
		for (int i = 0; i < 5; i++) {
			System.out.println(getName() + i);
		}
	}

	public static void main(String[] args) {
		for (int i = 0; i < 10; i++) {
			if (i == 5) {
				// 实例化JoinTest线程对象
				Thread jt = new JoinTest("半路杀出线程：" + i);

				try {
					jt.start();
					jt.join();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			System.out.println(Thread.currentThread().getName() + i);
		}
	}

}
