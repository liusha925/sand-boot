/**
 * 软件版权：流沙~~
 * 修改日期   修改人员     修改说明
 * =========  ===========  =====================
 * 2020/3/30    liusha   新增
 * =========  ===========  =====================
 */
package com.sand.redis.manager.repository;

import com.alibaba.fastjson.JSONObject;
import com.sand.core.util.convert.SandCharset;
import org.springframework.data.redis.connection.RedisServerCommands;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * 功能说明：redis资源库
 * 开发人员：@author liusha
 * 开发日期：2020/3/30 17:32
 * 功能描述：redis的CURD
 */
public class RedisRepository {
  private RedisTemplate<String, String> redisTemplate;

  public RedisRepository(RedisTemplate<String, String> redisTemplate) {
    this.redisTemplate = redisTemplate;
  }

  /**
   * 获取过期时间
   *
   * @param key redis主键
   * @return
   */
  public long getExpire(String key) {
    return this.redisTemplate.getExpire(key);
  }

  /**
   * 根据过期时间进行排序
   *
   * @param keys redis主键集合
   * @return
   */
  public List<String> sort(Set<String> keys) {
    Map<String, Long> map = keys.stream()
        .collect(Collectors.toMap(e -> e, e -> getExpire(e)));
    List<String> keyArr = map.entrySet().stream()
        .sorted((a, b) -> (int) (a.getValue() - b.getValue()))
        .map(e -> e.getKey())
        .collect(Collectors.toList());
    return keyArr;
  }

  /**
   * 添加到带有 过期时间的  缓存
   *
   * @param key   redis主键
   * @param value 值
   * @param time  过期时间
   */
  public void setExpire(final byte[] key, final byte[] value, final long time) {
    redisTemplate.execute((RedisCallback<Long>) connection -> {
      connection.set(key, value);
      connection.expire(key, time);
      return 1L;
    });
  }

  /**
   * 添加到带有 过期时间的  缓存
   *
   * @param key   redis主键
   * @param value 值
   * @param time  过期时间
   */
  public void setExpire(final String key, final String value, final long time) {
    redisTemplate.execute((RedisCallback<Long>) connection -> {
      RedisSerializer<String> serializer = getRedisSerializer();
      byte[] keys = serializer.serialize(key);
      byte[] values = serializer.serialize(value);
      connection.set(keys, values);
      connection.expire(keys, time);
      return 1L;
    });
  }

  /**
   * 一次性添加数组到   过期时间的  缓存，不用多次连接，节省开销
   *
   * @param keys   redis主键数组
   * @param values 值数组
   * @param time   过期时间
   */
  public void setExpire(final String[] keys, final String[] values, final long time) {
    redisTemplate.execute((RedisCallback<Long>) connection -> {
      RedisSerializer<String> serializer = getRedisSerializer();
      for (int i = 0; i < keys.length; i++) {
        byte[] bKeys = serializer.serialize(keys[i]);
        byte[] bValues = serializer.serialize(values[i]);
        connection.set(bKeys, bValues);
        connection.expire(bKeys, time);
      }
      return 1L;
    });
  }

  /**
   * 修改缓存过期时间
   *
   * @param key  redis主键
   * @param time 过期时间
   */
  public void setExpire(final String key, final long time) {
    redisTemplate.expire(key, time, TimeUnit.SECONDS);
  }


  /**
   * 一次性添加数组到   过期时间的  缓存，不用多次连接，节省开销
   *
   * @param keys   redis主键集合
   * @param values the values
   */
  public void set(final String[] keys, final String[] values) {
    redisTemplate.execute((RedisCallback<Long>) connection -> {
      RedisSerializer<String> serializer = getRedisSerializer();
      for (int i = 0; i < keys.length; i++) {
        byte[] bKeys = serializer.serialize(keys[i]);
        byte[] bValues = serializer.serialize(values[i]);
        connection.set(bKeys, bValues);
      }
      return 1L;
    });
  }


  /**
   * 添加到缓存
   *
   * @param key   redis主键
   * @param value the value
   */
  public void set(final String key, final String value) {
    redisTemplate.execute((RedisCallback<Long>) connection -> {
      RedisSerializer<String> serializer = getRedisSerializer();
      byte[] keys = serializer.serialize(key);
      byte[] values = serializer.serialize(value);
      connection.set(keys, values);
      return 1L;
    });
  }

  /**
   * 添加到缓存
   *
   * @param key   redis主键
   * @param value the value
   */
  public void set(final String key, final Object value) {
    String jsonValue = JSONObject.toJSONString(value);
    set(key, jsonValue);
  }

  /**
   * 查询在这个时间段内即将过期的key
   *
   * @param key  the key
   * @param time the time
   * @return the list
   */
  public List<String> willExpire(final String key, final long time) {
    final List<String> keysList = new ArrayList<String>();
    redisTemplate.execute((RedisCallback<List<String>>) connection -> {
      Set<String> keys = redisTemplate.keys(key + "*");
      for (String key1 : keys) {
        Long ttl = connection.ttl(key1.getBytes(SandCharset.CHARSET_UTF_8));
        if (0 <= ttl && ttl <= 2 * time) {
          keysList.add(key1);
        }
      }
      return keysList;
    });
    return keysList;
  }


  /**
   * 查询在以keyPatten的所有  key
   *
   * @param keyPatten the key patten
   * @return the set
   */
  public Set<String> keys(final String keyPatten) {
    return redisTemplate.execute((RedisCallback<Set<String>>) connection -> redisTemplate.keys(keyPatten));
  }

  /**
   * 根据key获取对象
   *
   * @param key the key
   * @return the byte [ ]
   */
  public byte[] get(final byte[] key) {
    byte[] result = redisTemplate.execute((RedisCallback<byte[]>) connection -> connection.get(key));
    return result;
  }

  /**
   * 根据key获取对象
   *
   * @param key the key
   * @return the string
   */
  public String get(final String key) {
    String resultStr = redisTemplate.execute((RedisCallback<String>) connection -> {
      RedisSerializer<String> serializer = getRedisSerializer();
      byte[] keys = serializer.serialize(key);
      byte[] values = connection.get(keys);
      return serializer.deserialize(values);
    });
    return resultStr;
  }


  /**
   * 根据key获取对象
   *
   * @param keyPatten the key patten
   * @return the keys values
   */
  public Map<String, String> getKeysValues(final String keyPatten) {
    return redisTemplate.execute((RedisCallback<Map<String, String>>) connection -> {
      RedisSerializer<String> serializer = getRedisSerializer();
      Map<String, String> maps = new HashMap<String, String>();
      Set<String> keys = redisTemplate.keys(keyPatten + "*");
      for (String key : keys) {
        byte[] bKeys = serializer.serialize(key);
        byte[] bValues = connection.get(bKeys);
        String value = serializer.deserialize(bValues);
        maps.put(key, value);
      }
      return maps;
    });
  }

  /**
   * Ops for hash hash operations.
   *
   * @return the hash operations
   */
  public HashOperations<String, String, String> opsForHash() {
    return redisTemplate.opsForHash();
  }

  /**
   * 对HashMap操作
   *
   * @param key       the key
   * @param hashKey   the hash key
   * @param hashValue the hash value
   */
  public void putHashValue(String key, String hashKey, String hashValue) {
    opsForHash().put(key, hashKey, hashValue);
  }

  /**
   * 设置hashMap
   *
   * @param key
   * @param hashKey
   * @param hashValue
   * @param time
   */
  public void expireHashValue(String key, String hashKey, String hashValue, final long time) {
    redisTemplate.opsForHash().put(key, hashKey, hashValue);
    redisTemplate.expire(key, time, TimeUnit.SECONDS);
  }

  /**
   * 获取单个field对应的值
   *
   * @param key     the key
   * @param hashKey the hash key
   * @return the hash values
   */
  public Object getHashValues(String key, String hashKey) {
    return opsForHash().get(key, hashKey);
  }


  /**
   * 获取hash的大小
   *
   * @param key
   * @return
   */
  public long getHashSize(String key) {
    return opsForHash().size(key);
  }

  /**
   * 根据key值删除
   *
   * @param key      the key
   * @param hashKeys the hash keys
   */
  public void delHashValues(String key, Object... hashKeys) {
    opsForHash().delete(key, hashKeys);
  }

  /**
   * key只匹配map
   *
   * @param key the key
   * @return the hash value
   */
  public Map<String, String> getHashValue(String key) {
    return opsForHash().entries(key);
  }

  /**
   * 获取keys
   *
   * @param key
   * @return
   */
  public Set<String> getHashKeys(String key) {
    return opsForHash().keys(key);
  }

  /**
   * 批量添加
   *
   * @param key the key
   * @param map the map
   */
  public void putHashValues(String key, Map<String, String> map) {
    opsForHash().putAll(key, map);
  }

  /**
   * 集合数量
   *
   * @return the long
   */
  public long dbSize() {
    return redisTemplate.execute(RedisServerCommands::dbSize);
  }

  /**
   * 清空redis存储的数据
   *
   * @return the string
   */
  public String flushDB() {
    return redisTemplate.execute((RedisCallback<String>) connection -> {
      connection.flushDb();
      return "ok";
    });
  }

  /**
   * 判断某个主键是否存在
   *
   * @param key the key
   * @return the boolean
   */
  public boolean exists(final String key) {
    return redisTemplate.execute((RedisCallback<Boolean>) connection ->
        connection.exists(key.getBytes(SandCharset.CHARSET_UTF_8)));
  }


  /**
   * 删除key
   *
   * @param keys the keys
   * @return the long
   */
  public long del(final String... keys) {
    return redisTemplate.execute((RedisCallback<Long>) connection -> {
      long result = 0;
      for (String key : keys) {
        result = connection.del(key.getBytes(SandCharset.CHARSET_UTF_8));
      }
      return result;
    });
  }

  /**
   * 获取 RedisSerializer
   *
   * @return the redis serializer
   */
  protected RedisSerializer<String> getRedisSerializer() {
    return redisTemplate.getStringSerializer();
  }

  /**
   * 对某个主键对应的值加一,value值必须是全数字的字符串
   *
   * @param key the key
   * @return the long
   */
  public long incr(final String key) {
    return redisTemplate.execute((RedisCallback<Long>) connection -> {
      RedisSerializer<String> redisSerializer = getRedisSerializer();
      return connection.incr(redisSerializer.serialize(key));
    });
  }

  /**
   * redis List 引擎
   *
   * @return the list operations
   */
  public ListOperations<String, String> opsForList() {
    return redisTemplate.opsForList();
  }

  /**
   * redis List数据结构 : 将一个或多个值 value 插入到列表 key 的表头
   *
   * @param key   redis主键
   * @param value the value
   * @return the long
   */
  public Long leftPush(String key, String value) {
    return opsForList().leftPush(key, value);
  }

  /**
   * redis List数据结构 : 移除并返回列表 key 的头元素
   *
   * @param key the key
   * @return the string
   */
  public String leftPop(String key) {
    return opsForList().leftPop(key);
  }

  /**
   * redis List数据结构 :将一个或多个值 value 插入到列表 key 的表尾(最右边)。
   *
   * @param key   redis主键
   * @param value the value
   * @return the long
   */
  public Long rightPush(String key, String value) {
    return opsForList().rightPush(key, value);
  }

  /**
   * redis List数据结构 : 移除并返回列表 key 的末尾元素
   *
   * @param key the key
   * @return the string
   */
  public String rightPop(String key) {
    return opsForList().rightPop(key);
  }


  /**
   * redis List数据结构 : 返回列表 key 的长度 ; 如果 key 不存在，则 key 被解释为一个空列表，返回 0 ; 如果 key 不是列表类型，返回一个错误。
   *
   * @param key the key
   * @return the long
   */
  public Long length(String key) {
    return opsForList().size(key);
  }


  /**
   * redis List数据结构 : 根据参数 i 的值，移除列表中与参数 value 相等的元素
   *
   * @param key   redis主键
   * @param i     the
   * @param value the value
   */
  public void remove(String key, long i, String value) {
    opsForList().remove(key, i, value);
  }

  /**
   * redis List数据结构 : 将列表 key 下标为 index 的元素的值设置为 value
   *
   * @param key   redis主键
   * @param index the index
   * @param value the value
   */
  public void set(String key, long index, String value) {
    opsForList().set(key, index, value);
  }

  /**
   * redis List数据结构 : 返回列表 key 中指定区间内的元素，区间以偏移量 start 和 end 指定。
   *
   * @param key   redis主键
   * @param start the start
   * @param end   the end
   * @return the list
   */
  public List<String> range(String key, int start, int end) {
    return opsForList().range(key, start, end);
  }

  /**
   * redis List数据结构 : 批量存储
   *
   * @param key  the key
   * @param list the list
   * @return the long
   */
  public Long leftPushAll(String key, List<String> list) {
    return opsForList().leftPushAll(key, list);
  }

  /**
   * redis List数据结构 : 将值 value 插入到列表 key 当中，位于值 index 之前或之后,默认之后。
   *
   * @param key   redis主键
   * @param index the index
   * @param value the value
   */
  public void insert(String key, long index, String value) {
    opsForList().set(key, index, value);
  }

  /**
   * 修改key值名称
   *
   * @param oldKey 老的redis主键
   * @param newKey 新的redis主键
   */
  public void renameKey(String oldKey, String newKey) {
    redisTemplate.rename(oldKey, newKey);
  }
}
