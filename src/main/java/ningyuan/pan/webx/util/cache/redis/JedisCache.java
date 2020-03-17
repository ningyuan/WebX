/**
 * 
 */
package ningyuan.pan.webx.util.cache.redis;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Properties;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ningyuan.pan.webx.util.cache.Cache;
import ningyuan.pan.webx.util.cache.State;
import ningyuan.pan.webx.util.exception.Utils;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.exceptions.JedisException;

/**
 * @author ningyuan
 *
 */
public class JedisCache implements Cache {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(JedisCache.class);
	
	private State state = State.CLOSED;
	
	private JedisPool jedisPool;
	
	private final JedisPoolConfig jedisPoolConfig;
	
	private String host = "127.0.0.1";
	
	private int port = 1234;
	
	private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
	
	private final Lock readLock = lock.readLock();
	
	private final Lock writeLock = lock.writeLock();
	
	public JedisCache(String redisPropFile) {
		jedisPoolConfig = new JedisPoolConfig();
		
		jedisPoolConfig.setMaxTotal(100);
		jedisPoolConfig.setMaxIdle(5);
		jedisPoolConfig.setMaxWaitMillis(100000);
		jedisPoolConfig.setTestOnBorrow(true);
	
		Properties configProp;
		try {
			configProp = new Properties();
        	configProp.load(new InputStreamReader(getClass().getClassLoader().getResourceAsStream(redisPropFile)));
		
        	host = configProp.getProperty("redis.host");
        	port = Integer.parseInt(configProp.getProperty("redis.port"));
        		
		} catch (IOException e) {
			LOGGER.error(Utils.printStackTraceToString(e));
        }	
	}
	
	@Override
	public void open(String... args) {
		writeLock.lock();
		try {
			if(state == State.CLOSED) {
				LOGGER.debug("open()");
				
				// jedis pool must be initiated before the state is changed to OPEN
				jedisPool = new JedisPool(jedisPoolConfig, host, port);
				
				state = State.OPEN;
			}
		}
		finally {
			writeLock.unlock();
		}
	}

	@Override
	public void clear() {
		Jedis jedis = null;
		
		readLock.lock();
		try {
			if(state == State.OPEN) {
				LOGGER.debug("clear()");
				
				jedis = jedisPool.getResource();
				
				jedis.flushDB();
			}
		}
		catch (JedisException e) {
			LOGGER.error(Utils.printStackTraceToString(e));
		}
		finally {
			if(jedis != null) {
				jedis.close();
			}
			
			readLock.unlock();
		}
	}

	@Override
	public String get(String key) {
		Jedis jedis = null;
		
		readLock.lock();
		try {
			if(state == State.OPEN) {
				LOGGER.debug("get("+key+")");
				
				jedis = jedisPool.getResource();
				
				String ret = jedis.get(key);
				
				if(ret == null) {
					return "";
				}
				else {
					return ret;
				}
			}
			else {
				return "";
			}
		}
		catch (JedisException e) {
			LOGGER.error(Utils.printStackTraceToString(e));
			
			return "";
		}
		finally {
			if(jedis != null) {
				jedis.close();
			}
			
			readLock.unlock();
		}
	}
	
	@Override
	public boolean put(String key, String value) {
		Jedis jedis = null;
		
		readLock.lock();
		try {
			if(state == State.OPEN) {
				LOGGER.debug("put("+key+", "+value+")");
				
				jedis = jedisPool.getResource();
				
				String state = jedis.set(key, value);
					
				if(state.equals("1")) {
					return true;
				}
				else {
					return false;
				}
			}
			else {
				return false;
			}
		}
		catch (JedisException e) {
			LOGGER.error(Utils.printStackTraceToString(e));
			
			return false;
		}
		finally {
			if(jedis != null) {
				jedis.close();
			}
			
			readLock.unlock();
		}
	}

	@Override
	public boolean remove(String key) {
		Jedis jedis = null;
		
		readLock.lock();
		try {
			if(state == State.OPEN) {
				LOGGER.debug("remove("+key+")");
				
				jedis = jedisPool.getResource();
				
				Long state = jedis.del(key);
				
				if(state > 1) {
					return true;
				}
				else {
					return false;
				}
			}
			else {
				return false;
			}
		}
		catch (JedisException e) {
			LOGGER.error(Utils.printStackTraceToString(e));
			
			return false;
		}
		finally {
			if(jedis != null) {
				jedis.close();
			}
			
			readLock.unlock();
		}
	}
	
	@Override
	public void close(String... args) {
		writeLock.lock();
		try {
			if(state == State.OPEN) {
				LOGGER.debug("close()");
				
				jedisPool.close();
				
				state = State.CLOSED;
			}
		}
		finally {
			writeLock.unlock();
		}
	}
}
