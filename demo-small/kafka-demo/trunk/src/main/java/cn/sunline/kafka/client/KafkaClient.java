package cn.sunline.kafka.client;

import java.io.Serializable;
import java.util.Properties;

import org.apache.kafka.common.security.JaasUtils;

import cn.sunline.kafka.ConfigureAPI.KafkaProperties;
import cn.sunline.kafka.consumer.MyConsumer;
import cn.sunline.kafka.producer.MyProducer;
import kafka.admin.AdminUtils;
import kafka.utils.ZkUtils;

public class KafkaClient implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5643068870815595572L;
	
	public static final boolean isDelete = true;

	public static void main(String[] args) {
		// 创建topic
		ZkUtils zk = ZkUtils.apply("10.22.0.160:2181", 30000, 30000, JaasUtils.isZkSecurityEnabled());
		if(!AdminUtils.topicExists(zk, KafkaProperties.TOPIC)) {
			AdminUtils.createTopic(zk, KafkaProperties.TOPIC, 1, 1, new Properties());
		}
		zk.close();
		
        final MyProducer pro = new MyProducer(KafkaProperties.TOPIC);
        pro.start();

        final MyConsumer con = new MyConsumer(KafkaProperties.TOPIC);
        con.start();
        
        Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
			@Override
			public void run() {
        		pro.stop();
        		con.stop();
			}
		}));
    }
}
