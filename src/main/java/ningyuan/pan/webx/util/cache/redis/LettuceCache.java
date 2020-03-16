/**
 * 
 */
package ningyuan.pan.webx.util.cache.redis;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/*import io.lettuce.core.RedisClient;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.sync.RedisCommands;*/
import ningyuan.pan.webx.util.cache.Cache;
import ningyuan.pan.webx.util.cache.State;


/**
 * @author ningyuan
 *
 */
public class LettuceCache implements Cache {
	private static final Logger LOGGER = LoggerFactory.getLogger(LettuceCache.class);
	
	
	private static State  state = State.CLOSED;
	
	//private static RedisClient redisClient;
	
	@Override
	public void open(String... args) {
		/*synchronized(RedisCache.class) {
			if(state == State.CLOSED) {
				
				redisClient = RedisClient.create(args[0]);
				
				state = State.OPEN;
				
				LOGGER.debug("open()");
			}
		}*/
	}

	@Override
	public void clear() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String get(String key) {
		/*if(state == State.OPEN) {
			StatefulRedisConnection<String, String> connection = redisClient.connect();
			RedisCommands<String, String> syncCommands = connection.sync();

			String ret = syncCommands.get(key);
			connection.close();
		
			return ret;
		}
		else {
			return null;
		}*/
		
		return "";
	}

	@Override
	public void close(String... args) {
		/*synchronized(RedisCache.class) {
			if(state == State.OPEN) {
				redisClient.shutdown();
				
				state = State.CLOSED;
				
				LOGGER.debug("close()");
			}
		}*/
	}	
}
