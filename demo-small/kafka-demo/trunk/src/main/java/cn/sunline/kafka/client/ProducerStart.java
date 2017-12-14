package cn.sunline.kafka.client;

import cn.sunline.kafka.ConfigureAPI.KafkaProperties;
import cn.sunline.kafka.producer.MyProducer;

public class ProducerStart {

	public static void main(String[] args) {
		final MyProducer pro = new MyProducer(KafkaProperties.TOPIC);
        pro.start();
	}
}
