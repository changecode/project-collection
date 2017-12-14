package cn.sunline.spring.cloud.client;

import java.util.Map;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(value="SERVER-01")
@RequestMapping("/")
public interface HelloRemote {

	@RequestMapping(value="/hello/{name}/{age}", method={RequestMethod.GET})
    public Map<String, Object> hello(@PathVariable("name") String name, @PathVariable("age")int age);
}
