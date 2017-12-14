package cn.sunline.kafka.producer;

import java.io.Serializable;
import java.util.Properties;

import cn.sunline.kafka.ConfigureAPI.KafkaProperties;
import kafka.javaapi.producer.Producer;
import kafka.producer.KeyedMessage;
import kafka.producer.ProducerConfig;

public class MyProducer extends Thread implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -428706153353961428L;
	
	private Producer<Integer, String> producer;
	private String topic;
	private Properties props = new Properties();
	private final int SLEEP = 1000 * 3;

	public MyProducer(String topic) {
        props.put("serializer.class", "kafka.serializer.StringEncoder");
        props.put("metadata.broker.list", KafkaProperties.BROKER_LIST);
        producer = new Producer<Integer, String>(new ProducerConfig(props));
        this.topic = topic;
    }

	@Override
	public void run() {
		int offsetNo = 1;
		while (true) {
			String msg = new String("Message_" + offsetNo);
			System.out.println("Send->[" + msg + "]");
			producer.send(new KeyedMessage<Integer, String>(topic, msg));
			offsetNo++;
			try {
				sleep(SLEEP);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
	}
}
