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

import io.lettuce.core.RedisClient;
import io.lettuce.core.RedisURI;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.sync.RedisCommands;

import ningyuan.pan.util.exception.ExceptionUtils;
import ningyuan.pan.webx.util.cache.Cache;
import ningyuan.pan.webx.util.cache.State;


/**
 * @author ningyuan
 *
 */
public class LettuceCache implements Cache {
	private static final Logger LOGGER = LoggerFactory.getLogger(LettuceCache.class);
	
	private State  state = State.CLOSED;
	
	private final RedisURI uri;
	
	private String host = "127.0.0.1";
	
	private int port = 1234;
	
	private RedisClient redisClient;
	
	// lettuce redis connection is designed to be long lived and thread safe
	private StatefulRedisConnection<String, String> connection;
	
	private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
	
	// redis command is atomic both read and write operation could be protected by the read lock
	private final Lock readLock = lock.readLock();
	
	// protect state variable in multi-thread environment
	private final Lock writeLock = lock.writeLock();
	
	public LettuceCache(String redisPropFile) {
		Properties configProp;
		try {
			configProp = new Properties();
        	configProp.load(new InputStreamReader(getClass().getClassLoader().getResourceAsStream(redisPropFile)));
		
        	host = configProp.getProperty("redis.host");
        	port = Integer.parseInt(configProp.getProperty("redis.port"));
        		
		} catch (IOException e) {
			LOGGER.error(ExceptionUtils.printStackTraceToString(e));
        }
		
		uri = RedisURI.builder().withHost(host).withPort(port).build();
	}
	
	@Override
	public void open(String... args) {
		writeLock.lock();
		try {
			if(state == State.CLOSED) {
				redisClient = RedisClient.create(uri);
				connection = redisClient.connect();
				
				state = State.OPEN;
			}
		}
		finally {
			writeLock.unlock();
		}
	}

	@Override
	public void clear() throws Exception{
		readLock.lock();
		try {
			if(state == State.OPEN) {
				RedisCommands<String, String> syncCommands = connection.sync();
				
				syncCommands.flushdb();
			}
		}
		finally {
			readLock.unlock();
		}
	}

	@Override
	public String getText(String key) throws Exception{
		readLock.lock();
		try {
			if(state == State.OPEN) {
				RedisCommands<String, String> syncCommands = connection.sync();
				
				return syncCommands.get(key);
			}
			
			return null;
		}
		finally {
			readLock.unlock();
		}
	}
	
	@Override
	public boolean putText(String key, String value) throws Exception{
		readLock.lock();
		try {
			if(state == State.OPEN) {
				RedisCommands<String, String> syncCommands = connection.sync();
				String ret = syncCommands.set(key, value);
				
				if(ret.equalsIgnoreCase("OK")) {
					return true;
				}
				else {
					return false;
				}
			}
			
			return false;
		}
		finally {
			readLock.unlock();
		}
	}

	@Override
	public boolean remove(String key) throws Exception{
		readLock.lock();
		try {
			if(state == State.OPEN) {
				RedisCommands<String, String> syncCommands = connection.sync();
				long ret = syncCommands.del(key);
				
				if(ret == 1) {
					return true;
				}
				else {
					return false;
				}
			}
			
			return false;
		}
		finally {
			readLock.unlock();
		}
	}	
	
	@Override
	public void close(String... args) {
		writeLock.lock();
		try {
			if(state == State.OPEN) {
				connection.close();
				redisClient.shutdown();
				
				state = State.CLOSED;
			}
		}
		finally {
			writeLock.unlock();
		}
	}

	@Override
	public void setExpire(int seconds) {
		// TODO Auto-generated method stub
		
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
