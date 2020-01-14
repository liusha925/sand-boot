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
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooKeeper;
import org.junit.Test;

import java.util.concurrent.CountDownLatch;

/**
 * 功能说明：ZooKeeper Api 测试类
 * 开发人员：@author liusha
 * 开发日期：2020/1/14 9:58
 * 功能描述：会话创建，节点创建、删除，数据读取、更新，权限控制等
 */
public class ZooKeeperApi implements Watcher {
  private static CountDownLatch countDownLatch = new CountDownLatch(1);
  private static final String host = "127.0.0.1:2181";
  private static final String hosts = "127.0.0.1:2181,127.0.0.1:2182,127.0.0.1:2183";

  @Override
  public void process(WatchedEvent watchedEvent) {
    System.out.println("Receive WatchedEvent：" + watchedEvent);
    if (Event.KeeperState.SyncConnected == watchedEvent.getState()) {
      countDownLatch.countDown();
    }
  }

  /**
   * 创建会话（最基础的实例）
   *
   * @throws Exception
   */
  @Test
  public void constructor_usage_simple() throws Exception {
    ZooKeeper zooKeeper = new ZooKeeper(hosts, 5000, new ZooKeeperApi());
    System.out.println("ZooKeeper.state：" + zooKeeper.getState());
    countDownLatch.await();
    System.out.println("ZooKeeper session会话创建完成。");
  }

  /**
   * 创建会话（可复用sessionId的实例）
   *
   * @throws Exception
   */
  @Test
  public void constructor_usage_with_SID_PWD() throws Exception {
    ZooKeeper zooKeeper = new ZooKeeper(host, 5000, new ZooKeeperApi());
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
   * @throws Exception
   */
  @Test
  public void create_sync_API() throws Exception {
    ZooKeeper zooKeeper = new ZooKeeper(host, 5000, new ZooKeeperApi());
    System.out.println("ZooKeeper.state：" + zooKeeper.getState());
    countDownLatch.await();

    String path1 = zooKeeper.create("/zk-test-ephemeral-", "".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
    System.out.println("Success create znode：" + path1);
    String path2 = zooKeeper.create("/zk-test-ephemeral-", "".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL);
    System.out.println("Success create znode：" + path2);
  }

  /**
   * 创建节点（异步）
   *
   * @throws Exception
   */
  @Test
  public void create_async_API() throws Exception {
    ZooKeeper zooKeeper = new ZooKeeper(host, 5000, new ZooKeeperApi());
    System.out.println("ZooKeeper.state：" + zooKeeper.getState());
    countDownLatch.await();

    zooKeeper.create("/zk-test-ephemeral-", "".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL,
        new IStringCallBack(), "hello ZooKeeper.");
    zooKeeper.create("/zk-test-ephemeral-", "".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL,
        new IStringCallBack(), "hello ZooKeeper.");
    zooKeeper.create("/zk-test-ephemeral-", "".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL,
        new IStringCallBack(), "hello ZooKeeper.");
    Thread.sleep(Integer.MAX_VALUE);
  }

  class IStringCallBack implements AsyncCallback.StringCallback {
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
        System.out.println("节点创建成功");
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
}
