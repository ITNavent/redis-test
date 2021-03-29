package com.navent.realestate.redistest;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.data.redis.connection.stream.StreamRecords;
import org.springframework.data.redis.connection.stream.StringRecord;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Profile("productor")
public class RedisStreamProducer {

	@Value("${navent.stream.key}")
	private String streamKey;
	
	@Autowired
	private RedisTemplate<String, Object> redisTemplate;

	private AtomicInteger atomicInteger = new AtomicInteger(0);

	@Scheduled(fixedRate = 3000)
	public void produce() {
//		var payload = new Message();
//		payload.setId(atomicInteger.incrementAndGet());
//		payload.setMsg(UUID.randomUUID().toString());
//		var record = StreamRecords.objectBacked(payload).withStreamKey(streamKey);

		Map<String, String> fields = new HashMap<>();
        fields.put("id", String.valueOf(atomicInteger.incrementAndGet()));
        StringRecord record = StreamRecords.string(fields).withStreamKey(streamKey);

		log.info("produzco {}", record);
		redisTemplate.opsForStream().add(record);
	}

	
}
