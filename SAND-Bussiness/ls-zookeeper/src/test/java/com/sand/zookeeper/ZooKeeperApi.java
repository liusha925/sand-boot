/**
 * 软件版权：流沙~~
 * 修改日期   修改人员     修改说明
 * =========  ===========  =====================
 * 2020/1/14    liusha   新增
 * =========  ===========  =====================
 */
package com.sand.zookeeper;

import org.apache.zookeeper.AsyncCallback;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;
import org.junit.Test;

import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * 功能说明：ZooKeeper Api 测试类
 * 开发人员：@author liusha
 * 开发日期：2020/1/14 9:58
 * 功能描述：会话创建，节点创建、删除，数据读取、更新，权限控制等
 */
public class ZooKeeperApi implements Watcher {
  private static Stat stat = new Stat();
  private static ZooKeeper zooKeeper = null;
  private static final String host = "127.0.0.1:2181";
  private static CountDownLatch countDownLatch = new CountDownLatch(1);
  private static final String hosts = "127.0.0.1:2181,127.0.0.1:2182,127.0.0.1:2183";

  /**
   * ZooKeeper CreateMode节点类型说明：
   * 1.PERSISTENT：持久型
   * 2.PERSISTENT_SEQUENTIAL：持久顺序型
   * 3.EPHEMERAL：临时型
   * 4.EPHEMERAL_SEQUENTIAL：临时顺序型
   * <p>
   * 1、2种类型客户端断开后不会消失
   * 3、4种类型客户端断开后超时时间内没有新的连接节点将会消失
   */

  /**
   * ZooKeeper ZooDefs.Ids权限类型说明：
   * OPEN_ACL_UNSAFE：完全开放的ACL，任何连接的客户端都可以操作该属性znode
   * CREATOR_ALL_ACL：只有创建者才有ACL权限
   * READ_ACL_UNSAFE：只能读取ACL
   */

  /**
   * ZooKeeper EventType事件类型说明：
   * NodeCreated：节点创建
   * NodeDataChanged：节点的数据变更
   * NodeChildrenChanged：子节点的数据变更
   * NodeDeleted：子节点删除
   */

  /**
   * ZooKeeper KeeperState状态类型说明：
   * Disconnected：连接失败
   * SyncConnected：连接成功
   * AuthFailed：认证失败
   * Expired：会话过期
   * None：状态切换
   */

  /**
   * 接收事件通知
   *
   * @param event 事件通知
   */
  @Override
  public void process(WatchedEvent event) {
    System.out.println("Receive WatchedEvent：" + event);
    try {
      if (Event.KeeperState.SyncConnected == event.getState()) {
        System.out.println("通知：会话连接成功");
        if (Event.EventType.None == event.getType() && null == event.getPath()) {
          System.out.println("会话状态切换");
          countDownLatch.countDown();
        } else if (event.getType() == Event.EventType.NodeCreated) {
          System.out.println("节点创建通知：" + event.getPath());
          zooKeeper.exists(event.getPath(), true);
        } else if (event.getType() == Event.EventType.NodeDataChanged) {
          System.out.println("节点的数据变更通知：" + new String(zooKeeper.getData(event.getPath(), true, stat)));
          System.out.println("czxid=" + stat.getCzxid() + "，mzxid=" + stat.getMzxid() + "，version=" + stat.getVersion());
          zooKeeper.exists(event.getPath(), true);
        } else if (event.getType() == Event.EventType.NodeChildrenChanged) {
          System.out.println("子节点的数据变更通知：" + zooKeeper.getChildren(event.getPath(), true));
          zooKeeper.exists(event.getPath(), true);
        } else if (event.getType() == Event.EventType.NodeDeleted) {
          System.out.println("节点删除通知：" + event.getPath());
          zooKeeper.exists(event.getPath(), true);
        } else {
          System.out.println("未知事件通知类型：" + event.getType());
          zooKeeper.exists(event.getPath(), true);
        }
      } else if (Event.KeeperState.Disconnected == event.getState()) {
        System.out.println("通知：会话连接失败");
      } else if (Event.KeeperState.AuthFailed == event.getState()) {
        System.out.println("通知：会话认证失败");
      } else if (Event.KeeperState.Expired == event.getState()) {
        System.out.println("通知：会话过期");
      } else {
        System.out.println("未知的通知状态：" + event.getState());
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * 创建会话（最基础的实例）
   *
   * @throws Exception Exception
   */
  @Test
  public void constructor_usage_simple() throws Exception {
    zooKeeper = new ZooKeeper(hosts, 5000, new ZooKeeperApi());
    System.out.println("ZooKeeper.state：" + zooKeeper.getState());
    countDownLatch.await();
    System.out.println("ZooKeeper session会话创建完成。");
  }

  /**
   * 创建会话（可复用sessionId的实例）
   *
   * @throws Exception Exception
   */
  @Test
  public void constructor_usage_SID_PWD() throws Exception {
    zooKeeper = new ZooKeeper(host, 5000, new ZooKeeperApi());
    System.out.println("ZooKeeper.state：" + zooKeeper.getState());
    countDownLatch.await();
    long sessionId = zooKeeper.getSessionId();
    byte[] sessionPasswd = zooKeeper.getSessionPasswd();
    System.out.println(String.format("首次获取sessionId：%s，sessionPasswd：%s", sessionId, sessionPasswd));
    // 使用不正确的sessionId
    zooKeeper = new ZooKeeper(host, 5000, new ZooKeeperApi(), 1L, "123".getBytes());
    System.out.println("ZooKeeper.state err session：" + zooKeeper.getState());
    // 使用正确的sessionId
    zooKeeper = new ZooKeeper(host, 5000, new ZooKeeperApi(), sessionId, sessionPasswd);
    System.out.println("ZooKeeper.state session：" + zooKeeper.getState());
    Thread.sleep(Integer.MAX_VALUE);
  }

  /**
   * 创建节点（同步）
   *
   * @throws Exception Exception
   */
  @Test
  public void create_API_sync() throws Exception {
    String path = "/zk-create-znode-test-";
    zooKeeper = new ZooKeeper(host, 5000, new ZooKeeperApi());
    System.out.println("ZooKeeper.state：" + zooKeeper.getState());
    countDownLatch.await();

    String path1 = zooKeeper.create(path, "".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
    System.out.println("节点创建成功：" + path1);
    String path2 = zooKeeper.create(path, "".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL);
    System.out.println("节点创建成功：" + path2);
  }

  /**
   * 创建节点（异步）
   * 同步接口创建节点时需要考虑接口抛出异常的情况，
   * 异步接口的异常体现在回调函数的ResultCode响应码中，比同步接口更健壮。
   *
   * @throws Exception Exception
   */
  @Test
  public void create_API_async() throws Exception {
    String path = "/zk-create-znode-test-";
    zooKeeper = new ZooKeeper(host, 5000, new ZooKeeperApi());
    System.out.println("ZooKeeper.state：" + zooKeeper.getState());
    countDownLatch.await();

    zooKeeper.create(path, "".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL,
        new CreateCallBack(), "ZooKeeper async create znode.");
    zooKeeper.create(path, "".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL,
        new CreateCallBack(), "ZooKeeper async create znode.");
    zooKeeper.create(path, "".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL,
        new CreateCallBack(), "ZooKeeper async create znode.");
    Thread.sleep(Integer.MAX_VALUE);
  }

  /**
   * 创建节点异步回调
   */
  class CreateCallBack implements AsyncCallback.StringCallback {
    /**
     * @param rc   服务端响应码 0：接口调用成功，-4：客户端与服务端连接已断开，-110：指定节点已存在，-112：会话已过期
     * @param path 调用接口时传入的节点路径（原样输出）
     * @param ctx  调用接口时传入的ctx值（原样输出）
     * @param name 实际在服务端创建的节点名
     */
    @Override
    public void processResult(int rc, String path, Object ctx, String name) {
      System.out.println("创建结果：rc=" + rc + "，path=" + path + "，ctx=" + ctx + "，name=" + name);
      if (rc == 0) {
        System.out.println("节点创建成功：" + name);
      } else if (rc == -4) {
        System.out.println("客户端与服务端连接已断开");
      } else if (rc == -110) {
        System.out.println("指定节点已存在");
      } else if (rc == -112) {
        System.out.println("会话已过期");
      } else {
        System.out.println("服务端响应码未知");
      }
    }
  }

  /**
   * 删除节点（同步）
   * 注：只允许删除叶子节点，不能直接删除根节点
   *
   * @throws Exception Exception
   */
  @Test
  public void delete_API_sync() throws Exception {
    String path = "/zk-delete-znode-test";
    zooKeeper = new ZooKeeper(host, 5000, new ZooKeeperApi());
    System.out.println("ZooKeeper state：" + zooKeeper.getState());
    countDownLatch.await();
    zooKeeper.create(path, "".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
    zooKeeper.delete(path, -1);
    Thread.sleep(Integer.MAX_VALUE);
  }

  /**
   * 删除节点（异步）
   * 注：只允许删除叶子节点，不能直接删除根节点
   *
   * @throws Exception Exception
   */
  @Test
  public void delete_API_async() throws Exception {
    String path = "/zk-delete-znode-test";
    zooKeeper = new ZooKeeper(host, 5000, new ZooKeeperApi());
    System.out.println("ZooKeeper state：" + zooKeeper.getState());
    countDownLatch.await();
    zooKeeper.create(path, "".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
    zooKeeper.delete(path, -1, new DeleteCallBack(), "ZooKeeper async delete znode");
    Thread.sleep(Integer.MAX_VALUE);
  }

  /**
   * 删除节点异步回调
   */
  class DeleteCallBack implements AsyncCallback.VoidCallback {
    /**
     * @param rc   服务端响应码 0：接口调用成功，-4：客户端与服务端连接已断开，-110：指定节点已存在，-112：会话已过期
     * @param path 调用接口时传入的节点路径（原样输出）
     * @param ctx  调用接口时传入的ctx值（原样输出）
     */
    @Override
    public void processResult(int rc, String path, Object ctx) {
      System.out.println("删除结果：rc=" + rc + "，path=" + path + "，ctx=" + ctx);
      if (rc == 0) {
        System.out.println("节点删除成功");
      } else if (rc == -4) {
        System.out.println("客户端与服务端连接已断开");
      } else if (rc == -112) {
        System.out.println("会话已过期");
      } else {
        System.out.println("服务端响应码未知");
      }
    }
  }

  /**
   * 获取子节点（同步）
   *
   * @throws Exception Exception
   */
  @Test
  public void getChildren_API_sync() throws Exception {
    String path = "/zk-getChildren-sync-test";
    zooKeeper = new ZooKeeper(host, 5000, new ZooKeeperApi());
    System.out.println("ZooKeeper state：" + zooKeeper.getState());
    countDownLatch.await();
    zooKeeper.create(path, "".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
    zooKeeper.create(path + "/children1", "".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
    List<String> childrenList = zooKeeper.getChildren(path, true);
    System.out.println("获取子节点：" + childrenList);
    zooKeeper.create(path + "/children2", "".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
    Thread.sleep(Integer.MAX_VALUE);
  }

  /**
   * 获取子节点（异步）
   *
   * @throws Exception Exception
   */
  @Test
  public void getChildren_API_async() throws Exception {
    String path = "/zk-getChildren-async-test";
    zooKeeper = new ZooKeeper(host, 5000, new ZooKeeperApi());
    System.out.println("ZooKeeper state：" + zooKeeper.getState());
    countDownLatch.await();
    zooKeeper.create(path, "".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
    zooKeeper.create(path + "/children1", "".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
    zooKeeper.getChildren(path, true, new ChildrenCallBack(), "异步获取子节点");
    zooKeeper.create(path + "/children2", "".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
    Thread.sleep(Integer.MAX_VALUE);
  }

  /**
   * 获取子节点异步回调
   */
  class ChildrenCallBack implements AsyncCallback.Children2Callback {
    /**
     * @param rc           服务端响应码 0：接口调用成功，-4：客户端与服务端连接已断开，-110：指定节点已存在，-112：会话已过期
     * @param path         调用接口时传入的节点路径（原样输出）
     * @param ctx          调用接口时传入的ctx值（原样输出）
     * @param childrenList 子节点列表
     * @param stat         节点状态，由服务器端响应的新stat替换
     */
    @Override
    public void processResult(int rc, String path, Object ctx, List<String> childrenList, Stat stat) {
      System.out.println("获取结果：rc=" + rc + "，path=" + path + "，ctx=" + ctx + "，childrenList=" + childrenList + "，stat=" + stat);
      if (rc == 0) {
        System.out.println("子节点获取成功：" + childrenList);
      } else if (rc == -4) {
        System.out.println("客户端与服务端连接已断开");
      } else if (rc == -112) {
        System.out.println("会话已过期");
      } else {
        System.out.println("服务端响应码未知");
      }
    }
  }

  /**
   * 获取节点数据（同步）
   * 更新节点数据（同步）
   *
   * @throws Exception Exception
   */
  @Test
  public void getData_API_sync() throws Exception {
    String path = "/zk-getData-sync-test";
    zooKeeper = new ZooKeeper(host, 5000, new ZooKeeperApi());
    System.out.println("ZooKeeper state：" + zooKeeper.getState());
    countDownLatch.await();
    zooKeeper.create(path, "test".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
    System.out.println("节点数据：" + new String(zooKeeper.getData(path, true, stat)));
    System.out.println("czxid=" + stat.getCzxid() + "，mzxid=" + stat.getMzxid() + "，version=" + stat.getVersion());
    zooKeeper.setData(path, "test".getBytes(), -1);
    Thread.sleep(Integer.MAX_VALUE);
  }

  /**
   * 获取节点数据（异步）
   * 更新节点数据（同步）
   *
   * @throws Exception Exception
   */
  @Test
  public void getData_API_async() throws Exception {
    String path = "/zk-getData-async-test";
    zooKeeper = new ZooKeeper(host, 5000, new ZooKeeperApi());
    System.out.println("ZooKeeper state：" + zooKeeper.getState());
    countDownLatch.await();
    zooKeeper.create(path, "test".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
    zooKeeper.getData(path, true, new DataCallBack(), "异步获取节点数据");
    System.out.println("czxid=" + stat.getCzxid() + "，mzxid=" + stat.getMzxid() + "，version=" + stat.getVersion());
    Stat stat1 = zooKeeper.setData(path, "test".getBytes(), -1);
    System.out.println("czxid=" + stat1.getCzxid() + "，mzxid=" + stat1.getMzxid() + "，version=" + stat1.getVersion());
    Stat stat2 = zooKeeper.setData(path, "test123".getBytes(), stat1.getVersion());
    System.out.println("czxid=" + stat2.getCzxid() + "，mzxid=" + stat2.getMzxid() + "，version=" + stat2.getVersion());
    try {
      zooKeeper.setData(path, "test123456".getBytes(), stat1.getVersion());
    } catch (KeeperException e) {
      System.out.println("Error Code：" + e.code() + "，" + e.getMessage());
    }
    Thread.sleep(Integer.MAX_VALUE);
  }

  /**
   * 获取节点数据异步回调
   */
  class DataCallBack implements AsyncCallback.DataCallback {
    /**
     * @param rc   服务端响应码 0：接口调用成功，-4：客户端与服务端连接已断开，-110：指定节点已存在，-112：会话已过期
     * @param path 调用接口时传入的节点路径（原样输出）
     * @param ctx  调用接口时传入的ctx值（原样输出）
     * @param data 节点数据
     * @param stat 节点状态，由服务器端响应的新stat替换
     */
    @Override
    public void processResult(int rc, String path, Object ctx, byte[] data, Stat stat) {
      System.out.println("获取结果：rc=" + rc + "，path=" + path + "，ctx=" + ctx + "，data=" + new String(data) + "，stat=" + stat);
      System.out.println("czxid=" + stat.getCzxid() + "，mzxid=" + stat.getMzxid() + "，version=" + stat.getVersion());
    }
  }

  /**
   * 更新节点数据（异步）
   *
   * @throws Exception Exception
   */
  @Test
  public void setData_API_async() throws Exception {
    String path = "/zk-setData-test";
    zooKeeper = new ZooKeeper(host, 5000, new ZooKeeperApi());
    System.out.println("ZooKeeper state：" + zooKeeper.getState());
    zooKeeper.exists(path, true);
    countDownLatch.await();
    zooKeeper.create(path, "test".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
    zooKeeper.setData(path, "test123456".getBytes(), -1, new StatCallBack(), "异步更新节点数据");
    Thread.sleep(Integer.MAX_VALUE);
  }

  /**
   * 更新节点数据异步回调
   */
  class StatCallBack implements AsyncCallback.StatCallback {
    /**
     * @param rc   服务端响应码 0：接口调用成功，-4：客户端与服务端连接已断开，-110：指定节点已存在，-112：会话已过期
     * @param path 调用接口时传入的节点路径（原样输出）
     * @param ctx  调用接口时传入的ctx值（原样输出）
     * @param stat 节点状态，由服务器端响应的新stat替换
     */
    @Override
    public void processResult(int rc, String path, Object ctx, Stat stat) {
      System.out.println("更新结果：rc=" + rc + "，path=" + path + "，ctx=" + ctx + "，stat=" + stat);
      System.out.println("czxid=" + stat.getCzxid() + "，mzxid=" + stat.getMzxid() + "，version=" + stat.getVersion());
    }
  }

  /**
   * 权限控制
   *
   * @throws Exception Exception
   */
  @Test
  public void auth_control_API() throws Exception {
    String path = "/zk-setData-test";
    zooKeeper = new ZooKeeper(host, 5000, new ZooKeeperApi());
    zooKeeper.addAuthInfo("digest", "zoo:true".getBytes());
    zooKeeper.create(path, "init".getBytes(), ZooDefs.Ids.CREATOR_ALL_ACL, CreateMode.EPHEMERAL);
    // 1）无权限信息访问
//    ZooKeeper zooKeeper1 = new ZooKeeper(host, 5000, new ZooKeeperApi());
//    System.out.println("访问结果：" + new String(zooKeeper1.getData(path, true, stat)));
    // 2）错误权限信息访问
//    ZooKeeper zooKeeper2 = new ZooKeeper(host, 5000, new ZooKeeperApi());
//    zooKeeper2.addAuthInfo("digest", "zoo:false".getBytes());
//    System.out.println("访问结果：" + new String(zooKeeper2.getData(path, true, stat)));
    // 3）正确权限信息访问
    ZooKeeper zooKeeper3 = new ZooKeeper(host, 5000, new ZooKeeperApi());
    zooKeeper3.addAuthInfo("digest", "zoo:true".getBytes());
    System.out.println("访问结果：" + new String(zooKeeper3.getData(path, true, stat)));
    Thread.sleep(Integer.MAX_VALUE);
  }

}
