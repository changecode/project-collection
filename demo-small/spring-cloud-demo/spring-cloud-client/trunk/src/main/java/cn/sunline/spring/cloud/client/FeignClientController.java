package cn.sunline.spring.cloud.client;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class FeignClientController {

	@Autowired
	private HelloRemote helloRemote;
	
	@RequestMapping("/h/{name}/{age}")
    public Object a(@PathVariable("name")String name, @PathVariable("age") int age) {
		Map<String, Object> result = helloRemote.hello(name, age);
        System.out.println("======================  " + result);
        return result;
    }
}
