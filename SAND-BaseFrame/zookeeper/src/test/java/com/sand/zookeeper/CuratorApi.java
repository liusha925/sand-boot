/**
 * 软件版权：流沙~~
 * 修改日期   修改人员     修改说明
 * =========  ===========  =====================
 * 2020/1/16    liusha   新增
 * =========  ===========  =====================
 */
package com.sand.zookeeper;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.api.BackgroundCallback;
import org.apache.curator.framework.api.CuratorEvent;
import org.apache.curator.framework.recipes.cache.NodeCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.framework.recipes.leader.LeaderSelector;
import org.apache.curator.framework.recipes.leader.LeaderSelectorListenerAdapter;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.data.Stat;
import org.junit.Test;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 功能说明：Curator Api 测试类
 * 开发人员：@author liusha
 * 开发日期：2020/1/16 14:12
 * 功能描述：会话创建，节点创建、删除，数据读取、更新，Master选举、分布式锁等
 */
public class CuratorApi {
  private static final String hosts = "127.0.0.1:2181,127.0.0.1:2182,127.0.0.1:2183";
  private static CuratorFramework client = CuratorFrameworkFactory.builder()
      .connectString(hosts)
      .sessionTimeoutMs(15000)
      // 使用默认的重试策略
      .retryPolicy(new ExponentialBackoffRetry(1000, 3))
      .build();
  private static final CountDownLatch countDownLatch = new CountDownLatch(2);
  private ExecutorService pool = Executors.newFixedThreadPool(3);

  /**
   * 使用同步接口
   *
   * @throws Exception Exception
   */
  @Test
  public void curator_usage_sync() throws Exception {
    client.start();
    System.out.println("Curator session会话创建完成");
    String path = "/zk-curator-test";

    String pathChildren1 = path + "/children1";
    System.out.println("递归创建前父节点[" + path + "]Stat信息：" + client.checkExists().forPath(path));
    System.out.println("递归创建临时节点[" + pathChildren1 + "]");
    client.create().creatingParentsIfNeeded().withMode(CreateMode.EPHEMERAL).forPath(pathChildren1, "init".getBytes());
    System.out.println("递归创建后父节点[" + path + "]Stat.version：" + client.checkExists().forPath(path).getVersion());
    System.out.println("递归创建后子节点[" + pathChildren1 + "]Stat.version：" + client.checkExists().forPath(pathChildren1).getVersion());
    Thread.sleep(1000);

    Stat stat = new Stat();
    System.out.println("从节点[" + pathChildren1 + "]中获取数据：" + new String(client.getData().storingStatIn(stat).forPath(pathChildren1)));
    System.out.println("getData Stat.version：" + stat.getVersion());
    Thread.sleep(1000);

    int version1 = client.setData().withVersion(stat.getVersion()).forPath(pathChildren1, "setData-test".getBytes()).getVersion();
    System.out.println("setData后的 Stat.version：" + version1);
    System.out.println("节点[" + pathChildren1 + "]更新后数据为：" + new String(client.getData().forPath(pathChildren1)));
    Thread.sleep(1000);

    try {
      // 使用正确的版本version1
      int version2 = client.setData().withVersion(version1).forPath(pathChildren1, "setData-test-001".getBytes()).getVersion();
      // 使用错误的版本stat.getVersion()
//      int version2 = client.setData().withVersion(stat.getVersion()).forPath(pathChildren1, "setData-test-001".getBytes()).getVersion();
      System.out.println("二次setData后的 Stat.version：" + version2);
      System.out.println("节点[" + pathChildren1 + "]更新后数据为：" + new String(client.getData().forPath(pathChildren1)));
    } catch (Exception e) {
      System.out.println("二次更新节点数据异常：" + e.getMessage());
    }
    Thread.sleep(1000);

    System.out.println("递归删除节点[" + path + "]");
    client.delete().deletingChildrenIfNeeded().forPath(path);
    System.out.println("节点删除后父节点[" + path + "]Stat信息：" + client.checkExists().forPath(path));
    System.out.println("节点删除后子节点[" + pathChildren1 + "]Stat信息：" + client.checkExists().forPath(pathChildren1));
    // 休眠5秒以保证程序结束前有足够的时间删除节点
    Thread.sleep(5000);
  }

  /**
   * 使用异步接口
   * 异步处理默认交给EventThread类
   *
   * @throws Exception Exception
   */
  @Test
  public void curator_usage_async() throws Exception {
    String path = "/zk-curator-async-test";
    client.start();
//    System.out.println("由默认线程EventThread处理");
//    client.create().creatingParentsIfNeeded()
//        .withMode(CreateMode.EPHEMERAL)
//        .inBackground(new CuratorBackCall())
//        .forPath(path, "init".getBytes());

    System.out.println("由自定义线程池处理");
    client.create().creatingParentsIfNeeded()
        .withMode(CreateMode.EPHEMERAL)
        .inBackground(new CuratorBackCall(), pool)
        .forPath(path, "init".getBytes());
    Thread.sleep(1000);

    String setDataBeforeStr = new String(client.getData().forPath(path));
    System.out.println("写入前获取节点[" + path + "]的数据：" + setDataBeforeStr);
    Thread.sleep(1000);

    client.setData()
        .inBackground(new CuratorBackCall())
        .forPath(path, "test".getBytes());
    String setDataAfterStr = new String(client.getData().forPath(path));
    System.out.println("写入后获取节点[" + path + "]的数据：" + setDataAfterStr);
    Thread.sleep(1000);

    // 关闭线程池
    pool.shutdown();
  }

  /**
   * 异步回调
   */
  class CuratorBackCall implements BackgroundCallback {

    /**
     * @param client 会话实例
     * @param event  ResultCode说明：0 节点创建成功，-4 客户端与服务端连接已断开，-110 指定节点已存在，-112 会话已过期
     * @throws Exception
     */
    @Override
    public void processResult(CuratorFramework client, CuratorEvent event) throws Exception {
      System.out.println("服务响应码：" + event.getResultCode() + "，事件类型：" + event.getType());
      System.out.println("执行线程：" + Thread.currentThread().getName());
      // 释放所有等待的线程
      countDownLatch.countDown();
    }
  }

  /**
   * 节点变化Listener
   *
   * @throws Exception
   */
  @Test
  public void node_cache_api() throws Exception {
    String path = "/zk-nodeCache-test";
    client.start();
    client.create().creatingParentsIfNeeded().withMode(CreateMode.EPHEMERAL).forPath(path, "init".getBytes());

    final NodeCache cache = new NodeCache(client, path, false);
    cache.start(true);
    cache.getListenable()
        .addListener(() ->
            System.out.println("节点[" + cache.getCurrentData().getPath() + "]变化，新数据：" + new String(cache.getCurrentData().getData())));

    System.out.println("节点[" + path + "]修改前数据：" + new String(client.getData().forPath(path)));
    client.setData().forPath(path, "nodeCache-test".getBytes());
    System.out.println("节点[" + path + "]修改后数据：" + new String(client.getData().forPath(path)));
    Thread.sleep(1000);

    client.delete().deletingChildrenIfNeeded().forPath(path);
    Thread.sleep(Integer.MAX_VALUE);
  }

  /**
   * 子节点变化Listener
   *
   * @throws Exception
   */
  @Test
  public void children_node_cache_api() throws Exception {
    String path = "/zk-child-test";
    client.start();
    final PathChildrenCache cache = new PathChildrenCache(client, path, false);
    // 异步初始化
    cache.start(PathChildrenCache.StartMode.POST_INITIALIZED_EVENT);
    cache.getListenable().addListener((client, event) -> {
      switch (event.getType()) {
        case CHILD_ADDED:
          System.out.println("子节点[" + event.getData().getPath() + "]" + "已添加");
          break;
        case CHILD_UPDATED:
          System.out.println("子节点[" + event.getData().getPath() + "]" + "数据已更改，更改后的数据：" + event);
          break;
        case CHILD_REMOVED:
          System.out.println("子节点[" + event.getData().getPath() + "]" + "已被删除");
          break;
        default:
          System.out.println("其它子节点事件：" + event.getType());
          break;
      }
    });
    System.out.println("开始创建父节点[" + path + "]");
    client.create().withMode(CreateMode.PERSISTENT).forPath(path);
    Thread.sleep(1000);

    String pathChildren1 = path + "/children1";
    System.out.println("开始创建子节点[" + pathChildren1 + "]");
    client.create().withMode(CreateMode.PERSISTENT).forPath(pathChildren1, "children1_init".getBytes());
    Thread.sleep(1000);

    System.out.println("更新子节点[" + pathChildren1 + "]数据：" + new String(client.getData().forPath(pathChildren1)));
    client.setData().forPath(pathChildren1, "children1".getBytes());
    Thread.sleep(1000);

    System.out.println("递归删除节点[" + path + "]");
    client.delete().deletingChildrenIfNeeded().forPath(path);
    Thread.sleep(1000);

  }

  /**
   * Master选举
   * 选定根节点，多台机器向该节点创建子节点，创建成功的即为Master
   *
   * @throws Exception
   */
  @Test
  public void master_select_api() throws Exception {
    String path = "/zk-master-select-test";
    client.start();
    LeaderSelector selector = new LeaderSelector(client, path, new LeaderSelectorListenerAdapter() {
      @Override
      public void takeLeadership(CuratorFramework client) throws Exception {
        System.out.println("【" + client.getZookeeperClient().getZooKeeper() + "】成为Master角色");
        Thread.sleep(3000);
        System.out.println("完成Master操作，释放Master权利");
      }
    });
    selector.autoRequeue();
    selector.start();
    Thread.sleep(Integer.MAX_VALUE);
  }

  /**
   * 生成订单号
   * 未使用锁
   */
  @Test
  public void recipes_unlock_api() {
    final CountDownLatch unLockLatch = new CountDownLatch(1);
    System.out.println("未使用锁的情况下生成订单号");
    for (int i = 0; i < 10; i++) {
      new Thread(() -> {
        try {
          unLockLatch.await();
        } catch (Exception e) {
          System.out.println("异常：" + e.getMessage());
        }
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss|SSS");
        String orderNo = sdf.format(new Date());
        System.out.println("订单号：" + orderNo);
      }).start();
    }
    unLockLatch.countDown();
  }

  /**
   * 生成订单号
   * 使用分布式锁
   */
  @Test
  public void recipes_lock_api() throws Exception {
    String path = "/zk-recipes-lock-test";
    client.start();
    System.out.println("使用锁的情况下生成订单号");
    final InterProcessMutex lock = new InterProcessMutex(client, path);
    final CountDownLatch lockLatch = new CountDownLatch(1);
    for (int i = 0; i < 10; i++) {
      new Thread(() -> {
        try {
          lockLatch.await();
          lock.acquire();
        } catch (Exception e) {
          System.out.println("加锁异常：" + e.getMessage());
          e.printStackTrace();
        }
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss|SSS");
        String orderNo = sdf.format(new Date());
        System.out.println("加锁订单号：" + orderNo);
        try {
          lock.release();
        } catch (Exception e) {
          System.out.println("释放锁异常：" + e.getMessage());
          e.printStackTrace();
        }
      }).start();
    }
    lockLatch.countDown();
  }
}
