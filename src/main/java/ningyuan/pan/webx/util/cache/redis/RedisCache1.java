/**
 * 
 */
package ningyuan.pan.webx.util.cache.redis;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ningyuan.pan.webx.util.cache.Cache;
import ningyuan.pan.webx.util.cache.State;
import redis.clients.jedis.Jedis;

/**
 * @author ningyuan
 *
 */
public class RedisCache1 implements Cache {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(RedisCache1.class);
	
	private static State  state = State.CLOSED;
	
	@Override
	public void open(String... args) {
		LOGGER.debug("open()");
		
		state = State.OPEN;
	}

	@Override
	public void clear() {
		// TODO Auto-generated method stub

	}

	@Override
	public String get(String key) {
		LOGGER.debug("get("+key+")");
		
		Jedis jedis = new Jedis("localhost");
		
		String ret = jedis.get(key);
		
		jedis.close();
		
		if(ret == null) {
			return "";
		}
		else {
			return ret;
		}
	}

	@Override
	public void close(String... args) {
		LOGGER.debug("close()");
		
		state = State.CLOSED;
	}

}
