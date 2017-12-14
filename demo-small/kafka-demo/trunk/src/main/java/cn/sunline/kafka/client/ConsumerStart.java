package cn.sunline.kafka.client;

import cn.sunline.kafka.ConfigureAPI.KafkaProperties;
import cn.sunline.kafka.consumer.MyConsumer;

public class ConsumerStart {

	public static void main(String[] args) {
		final MyConsumer con = new MyConsumer(KafkaProperties.TOPIC);
        con.start();
	}
}
