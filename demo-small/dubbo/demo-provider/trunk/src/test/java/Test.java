import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Test {

	public static void main(String[] args) throws Exception {
		final ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(new String[] { "applicationContext.xml" });  
        Runtime.getRuntime().addShutdownHook(new Thread() {
        	@Override
        	public void run() {
        		context.close();
        	}
        });
        context.start();  
        System.out.println("服务已启动...");  
        System.in.read();
	}
}
