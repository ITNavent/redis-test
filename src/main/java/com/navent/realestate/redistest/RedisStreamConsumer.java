package com.navent.realestate.redistest;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.data.redis.connection.stream.MapRecord;
import org.springframework.data.redis.stream.StreamListener;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Profile("consumidor")
public class RedisStreamConsumer implements StreamListener<String, MapRecord<String, String, String>> {

	@Value("${navent.stream.key}")
	private String streamKey;

	@Value("${navent.consumer.group}")
	private String consumerGroup;

	@Override
	public void onMessage(MapRecord<String, String, String> message) {
		log.info("consumo {}", message);
	}
}
