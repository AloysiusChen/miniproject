package vttp.batch5.ssf.miniproject.repositories;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public class FinnhubRepository {

    @Autowired
    @Qualifier("redis-0")
    private RedisTemplate<String, String> redisTemplate;

    public void addEtfSymbolsToList(String key, List<String> symbols) {
        redisTemplate.delete(key);
        redisTemplate.opsForList().rightPushAll(key, symbols);
    }

    public void addEtfSymbolsToMap(String key, Map<String, String> symbols) {
        redisTemplate.opsForHash().putAll(key, symbols);
    }

    public List<String> getEtfSymbolsListFromRedis(String key) {
        return redisTemplate.opsForList().range(key, 0, -1);
    }

    public Map<Object, Object> getEtfMapFromRedis(String key) {
        return redisTemplate.opsForHash().entries(key);
    }

    public Map<Object, Object> getQuoteFromRedis(String key) {
        return redisTemplate.opsForHash().entries(key);
    }

    public void saveQuoteToRedis(String key, Map<String, String> quoteData) {
        redisTemplate.opsForHash().putAll(key, quoteData);
    }
}
