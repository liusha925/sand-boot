/**
 * 软件版权：流沙~~
 * 修改日期   修改人员     修改说明
 * =========  ===========  =====================
 * 2020/8/14    liusha   新增
 * =========  ===========  =====================
 */
package com.sand.web.repository.impl;

import com.sand.web.domain.User;
import com.sand.web.repository.UserRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 功能说明：
 * 开发人员：@author liusha
 * 开发日期：2020/8/14 17:31
 * 功能描述：写明作用，调用方式，使用场景，以及特殊情况
 */
@Repository
public class UserRepositoryImpl implements UserRepository {
  private static AtomicLong counter = new AtomicLong();
  private final ConcurrentMap<Long, User> userMap = new ConcurrentHashMap<>();

  public UserRepositoryImpl() {
    User user = new User();
    user.setAge(26);
    user.setName("流沙");
    this.saveOrUpdateUser(user);
  }

  @Override
  public User saveOrUpdateUser(User user) {
    Long id = user.getId();
    if (id <= 0) {
      id = counter.incrementAndGet();
      user.setId(id);
    }
    this.userMap.put(id, user);
    return user;
  }

  @Override
  public void deleteUser(Long id) {
    this.userMap.remove(id);
  }

  @Override
  public User getUserById(Long id) {
    return this.userMap.get(id);
  }

  @Override
  public List<User> listUser() {
    return new ArrayList<>(this.userMap.values());
  }
}
