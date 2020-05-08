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

import ningyuan.pan.util.exception.ExceptionUtils;
import ningyuan.pan.webx.util.cache.Cache;
import ningyuan.pan.webx.util.cache.State;
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
	
	// jedis pool is thread safe
	private JedisPool jedisPool;
	
	private final JedisPoolConfig jedisPoolConfig;
	
	private String host = "127.0.0.1";
	
	private int port = 1234;
	
	private int expireTimeInSec = 60;
	
	private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
	
	// redis command is atomic both read and write operation could be protected by the read lock
	private final Lock readLock = lock.readLock();
	
	// protect state variable in multi-thread environment
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
			LOGGER.error(ExceptionUtils.printStackTraceToString(e));
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
	public void clear() throws Exception{
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
			LOGGER.error(ExceptionUtils.printStackTraceToString(e));
		}
		finally {
			if(jedis != null) {
				jedis.close();
			}
			
			readLock.unlock();
		}
	}

	@Override
	public String getText(String key) throws Exception{
		Jedis jedis = null;
		
		readLock.lock();
		try {
			if(state == State.OPEN) {
				LOGGER.debug("get("+key+")");
				
				jedis = jedisPool.getResource();
				
				return jedis.get(key);
				
			}
			
			return null;
		}
		catch (JedisException e) {
			LOGGER.error(ExceptionUtils.printStackTraceToString(e));
			return null;
		}
		finally {
			if(jedis != null) {
				jedis.close();
			}
			
			readLock.unlock();
		}
	}
	
	@Override
	public boolean putText(String key, String value) throws Exception{
		Jedis jedis = null;
		
		readLock.lock();
		try {
			if(state == State.OPEN) {
				LOGGER.debug("put("+key+", "+value+")");
				
				jedis = jedisPool.getResource();
				
				String state = jedis.set(key, value);
				jedis.expire(key, expireTimeInSec);
				
				if(state.equalsIgnoreCase("OK")) {
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
			LOGGER.error(ExceptionUtils.printStackTraceToString(e));
			
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
	public boolean remove(String key) throws Exception{
		Jedis jedis = null;
		
		readLock.lock();
		try {
			if(state == State.OPEN) {
				LOGGER.debug("remove("+key+")");
				
				jedis = jedisPool.getResource();
				
				long state = jedis.del(key);
				
				if(state == 1) {
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
			LOGGER.error(ExceptionUtils.printStackTraceToString(e));
			
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

	@Override
	public void setExpire(int seconds) {
		readLock.lock();
		try {
			expireTimeInSec = seconds;
		}
		finally {
			readLock.unlock();
		}
	}

	@Override
	public byte[] getBinary(String key) throws Exception{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean putBinary(String key, byte[] value) throws Exception{
		// TODO Auto-generated method stub
		return false;
	}
}
