/**
 * 软件版权：流沙~~
 * 修改日期   修改人员     修改说明
 * =========  ===========  =====================
 * 2020/8/14    liusha   新增
 * =========  ===========  =====================
 */
package com.sand.web.repository;

import com.sand.web.domain.User;

import java.util.List;

/**
 * 功能说明：
 * 开发人员：@author liusha
 * 开发日期：2020/8/14 17:29
 * 功能描述：写明作用，调用方式，使用场景，以及特殊情况
 */
public interface UserRepository {
  /**
   * 新增或者修改用户
   *
   * @param user
   * @return
   */
  User saveOrUpdateUser(User user);

  /**
   * 删除用户
   *
   * @param id
   */
  void deleteUser(Long id);

  /**
   * 根据用户id获取用户
   *
   * @param id
   * @return
   */
  User getUserById(Long id);

  /**
   * 获取所有用户的列表
   *
   * @return
   */
  List<User> listUser();
}
