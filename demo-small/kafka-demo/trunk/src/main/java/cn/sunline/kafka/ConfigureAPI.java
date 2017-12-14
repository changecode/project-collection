package cn.sunline.kafka;

import java.io.Serializable;

public class ConfigureAPI implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1464623905841087711L;

	public interface KafkaProperties extends Serializable {
        public final static String ZK = "10.22.0.160:2181";
        public final static String GROUP_ID = "test-consumer-group";
        public final static String TOPIC = "test-topic2";
        public final static String BROKER_LIST = "10.22.0.160:9092";
        public final static int BUFFER_SIZE = 16 * 1024;
        public final static int TIMEOUT = 20000;
        public final static int INTERVAL = 10000;
    }
}
