/**
 * 软件版权：流沙~~
 * 修改日期   修改人员     修改说明
 * =========  ===========  =====================
 * 2020/4/30    liusha   新增
 * =========  ===========  =====================
 */
package com.sand.redis.util;

import com.sand.common.exception.BusinessException;
import com.sand.common.util.lang3.StringUtil;
import com.sand.redis.manager.RedisLockBean;
import com.sand.redis.manager.RedisLockManager;
import lombok.extern.slf4j.Slf4j;

import java.util.Objects;
import java.util.concurrent.locks.Condition;

/**
 * 功能说明：Redis加锁处理机制
 * 开发人员：@author liusha
 * 开发日期：2020/4/30 11:21
 * 功能描述：Redis加锁处理机制
 */
@Slf4j
public class RedisLockUtil {

  public static void lock(String body, String regex) {
    if (StringUtil.isNotBlank(body)) {
      // 规则需要客户端与服务端约定好
      String[] bodyArray = body.split(regex);
      if (bodyArray.length == 2) {
        String uuid = bodyArray[0];
        String data = bodyArray[1];
        if (RedisLockManager.lockBeanMap.containsKey(uuid)) {
          RedisLockBean redisLockBean = RedisLockManager.lockBeanMap.get(uuid);
          Condition condition = redisLockBean.getCondition();
          if (Objects.nonNull(condition)) {
            redisLockBean.setHasSub(true);
            redisLockBean.setResult(data);
            RedisLockManager.lock.lock();
            condition.signal();
            RedisLockManager.lock.unlock();
          }
        } else {
          log.info("Redis加锁找不到此uuid：{}", uuid);
          throw new BusinessException("Redis加锁找不到此uuid");
        }
      } else {
        log.info("Redis加锁主体格式不符合规定：{}", regex);
        throw new BusinessException("Redis加锁主体格式不符合规定");
      }
    } else {
      log.info("Redis加锁主体为空，无法加锁处理");
      throw new BusinessException("Redis加锁主体为空，无法加锁处理");
    }
  }

}
