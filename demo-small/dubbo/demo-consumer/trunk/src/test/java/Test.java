import java.util.Date;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import cn.sunline.server.DemoService;

public class Test {

	public static void main(String[] args) {
		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(new String[] { "applicationContext.xml" });  
	    //context.start();  
	    DemoService demoServer = (DemoService) context.getBean("demoService");  
	    System.out.println("dubbo服务调用成功:"+demoServer.hello("Morning"+"1:"+new Date())+"3:"+new Date());
	    context.close();
	}
}
