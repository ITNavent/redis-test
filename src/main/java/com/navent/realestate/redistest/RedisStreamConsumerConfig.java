package com.navent.realestate.redistest;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.time.Duration;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.stream.Consumer;
import org.springframework.data.redis.connection.stream.MapRecord;
import org.springframework.data.redis.connection.stream.ReadOffset;
import org.springframework.data.redis.connection.stream.StreamOffset;
import org.springframework.data.redis.stream.StreamListener;
import org.springframework.data.redis.stream.StreamMessageListenerContainer;
import org.springframework.data.redis.stream.Subscription;

@Profile("consumidor")
@Configuration
public class RedisStreamConsumerConfig {

	@Value("${navent.stream.key}")
	private String streamKey;

	@Value("${navent.consumer.group}")
	private String consumerGroup;
	
	@Autowired
	private StreamListener<String, MapRecord<String, String, String>> redisStreamConsumer;

	@Bean
    public Subscription subscription(RedisConnectionFactory redisConnectionFactory) throws UnknownHostException {
        var options = StreamMessageListenerContainer
                            .StreamMessageListenerContainerOptions
                            .builder()
                            .pollTimeout(Duration.ofSeconds(1))
                            .build();

        var listenerContainer = StreamMessageListenerContainer.create(redisConnectionFactory, options);

        var subscription = listenerContainer.receiveAutoAck(
                Consumer.from(consumerGroup, "consumer-id-" + UUID.randomUUID().toString()),
                StreamOffset.create(streamKey, ReadOffset.lastConsumed()),
                redisStreamConsumer);
//        var subscription = listenerContainer.receive(StreamOffset.fromStart(streamKey), redisStreamConsumer);
        listenerContainer.start();
        return subscription;
    }
}
