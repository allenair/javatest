package com.zxtech;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class RedisUtil {
	private static JedisPool jedisPool;
	static {
		JedisPoolConfig config = new JedisPoolConfig();
		// 设置最大连接数
		config.setMaxTotal(50);
		// 设置最大空闲数
		config.setMaxIdle(10);
		// 设置超时时间
		config.setMaxWaitMillis(3000);

		// 初始化连接池
		jedisPool = new JedisPool(config, "127.0.0.1", 6379);
	}

	public static boolean set(String key, String value){
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			jedis.set(key, value);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		} finally {
			jedis.close();
		}
	}

	public static boolean set(String key, String value, int expireSecond){
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			jedis.setex(key, expireSecond, value);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		} finally {
			jedis.close();
		}
	}

	public static boolean del(String key) {
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			jedis.del(key);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		} finally {
			jedis.close();
		}
	}

	public static String get(String key) {
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			String value = jedis.get(key);
			return value;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			jedis.close();
		}
	}
}
