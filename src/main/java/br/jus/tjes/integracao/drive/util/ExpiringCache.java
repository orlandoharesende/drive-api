package br.jus.tjes.integracao.drive.util;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class ExpiringCache<K, V> {
	private final Map<K, V> cache = new ConcurrentHashMap<>();
	private final Map<K, Long> expirationTimes = new ConcurrentHashMap<>();
	private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
	private int expirationTimeInSeconds;
	private static int FREQUENCIA_REFRESH_IN_SECONDS = 60;

	public ExpiringCache(int expirationTimeInSeconds) {
		this.expirationTimeInSeconds = expirationTimeInSeconds;
		scheduler.scheduleAtFixedRate(this::cleanupExpiredValues, FREQUENCIA_REFRESH_IN_SECONDS,
				FREQUENCIA_REFRESH_IN_SECONDS, TimeUnit.SECONDS);
	}

	public void put(K key, V value) {
		cache.put(key, value);
		long expirationTime = System.currentTimeMillis() + (expirationTimeInSeconds * 1000);
		expirationTimes.put(key, expirationTime);
	}

	public V get(K key) {
		if (isExpired(key)) {
			remove(key);
			return null;
		}
		return cache.get(key);
	}

	public void remove(K key) {
		cache.remove(key);
		expirationTimes.remove(key);
	}

	private boolean isExpired(K key) {
		Long expirationTime = expirationTimes.get(key);
		return expirationTime != null && expirationTime < System.currentTimeMillis();
	}

	private void cleanupExpiredValues() {
		long currentTime = System.currentTimeMillis();
		for (K key : expirationTimes.keySet()) {
			if (expirationTimes.get(key) < currentTime) {
				cache.remove(key);
				expirationTimes.remove(key);
			}
		}
	}

	public void shutdown() {
		scheduler.shutdown();
	}
}
