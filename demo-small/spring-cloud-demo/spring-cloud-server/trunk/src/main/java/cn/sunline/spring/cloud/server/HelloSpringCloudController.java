package cn.sunline.spring.cloud.server;

import java.util.HashMap;
import java.util.Map;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloSpringCloudController {

	@RequestMapping("/hello/{name}/{age}")
	public Map<String, Object> hello(@PathVariable("name")String name, @PathVariable("age")int age) {
		Map<String, Object> map = new HashMap<>();
		map.put("name", name);
		map.put("age", age);
		return map;
	}
}
