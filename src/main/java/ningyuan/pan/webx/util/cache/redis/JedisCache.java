/**
 * 
 */
package ningyuan.pan.webx.util.cache.redis;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ningyuan.pan.webx.util.cache.Cache;
import ningyuan.pan.webx.util.cache.State;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * @author ningyuan
 *
 */
public class JedisCache implements Cache {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(JedisCache.class);
	
	private volatile State state = State.CLOSED;
	
	private JedisPool jedisPool;
	
	public JedisCache() {
		
	}
	
	@Override
	public void open(String... args) {
		synchronized (this) {
			if(state == State.CLOSED) {
				LOGGER.debug("open()");
				
				JedisPoolConfig config = new JedisPoolConfig();
				config.setMaxTotal(100);
				config.setMaxIdle(5);
				config.setMaxWaitMillis(100000);
				config.setTestOnBorrow(true);
				
				jedisPool = new JedisPool(config, args[0], Integer.parseInt(args[1]));
				
				state = State.OPEN;
			}
		}
	}

	@Override
	public void clear() {
		// TODO Auto-generated method stub

	}

	@Override
	public String get(String key) {
		if(state == State.OPEN) {
			LOGGER.debug("get("+key+")");
			
			Jedis jedis = jedisPool.getResource();
			
			String ret = jedis.get(key);
			
			jedis.close();
			
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

	@Override
	public void close(String... args) {
		synchronized (this) {
			if(state == State.OPEN) {
				LOGGER.debug("close()");
				
				jedisPool.close();
				
				state = State.CLOSED;
			}
		}
	}
}
