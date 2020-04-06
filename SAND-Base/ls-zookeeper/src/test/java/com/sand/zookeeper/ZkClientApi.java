/**
 * 软件版权：流沙~~
 * 修改日期   修改人员     修改说明
 * =========  ===========  =====================
 * 2020/1/16    liusha   新增
 * =========  ===========  =====================
 */
package com.sand.zookeeper;

import org.I0Itec.zkclient.IZkDataListener;
import org.I0Itec.zkclient.ZkClient;
import org.junit.Test;

import java.util.List;

/**
 * 功能说明：ZkClient Api 测试类
 * 开发人员：@author liusha
 * 开发日期：2020/1/16 9:19
 * 功能描述：会话创建，节点创建、删除，数据读取、更新，权限控制等
 */
public class ZkClientApi {
  private static final String hosts = "127.0.0.1:2181,127.0.0.1:2182,127.0.0.1:2183";

  @Test
  public void zkClient_demo() throws InterruptedException {
    String path = "/zk-zkClient-test";
    ZkClient zkClient = new ZkClient(hosts, 5000);
    System.out.println("不需要异步等待处理，ZkClient session会话创建完成。");

    System.out.println("创建临时节点[" + path + "]");
    zkClient.createEphemeral(path);
    System.out.println("是否存在节点[" + path + "]：" + zkClient.exists(path));
    Thread.sleep(1000);

    System.out.println("删除节点[" + path + "]");
    zkClient.delete(path);
    System.out.println("是否存在节点[" + path + "]：" + zkClient.exists(path));
    Thread.sleep(1000);

    System.out.println("注册监听器ZkChildListener");
    zkClient.subscribeChildChanges(path, (parentPath, childrenList) -> {
      System.out.println("父节点：" + parentPath + "，子节点：" + childrenList);
    });
    Thread.sleep(1000);

    String pathChildren1 = path + "/children1";
    System.out.println("递归创建节点[" + pathChildren1 + "]");
    zkClient.createPersistent(pathChildren1, true);
    System.out.println("是否存在节点[" + pathChildren1 + "]：" + zkClient.exists(pathChildren1));
    Thread.sleep(1000);

    String pathChildren2 = path + "/children2";
    System.out.println("创建子节点[" + pathChildren2 + "]");
    zkClient.createPersistent(pathChildren2, false);
    System.out.println("是否存在节点[" + pathChildren2 + "]：" + zkClient.exists(pathChildren2));
    Thread.sleep(1000);

    List<String> childrenList = zkClient.getChildren(path);
    System.out.println("获取子节点信息：" + childrenList);
    Thread.sleep(1000);

    System.out.println("注册监听器ZkDataListener");
    zkClient.subscribeDataChanges(path, new IZkDataListener() {
      @Override
      public void handleDataChange(String path, Object data) throws Exception {
        System.out.println("节点[" + path + "]内容已经更新，更新后内容：" + data);
      }

      @Override
      public void handleDataDeleted(String path) throws Exception {
        System.out.println("节点[" + path + "]已被删除");
      }
    });
    Thread.sleep(1000);

    System.out.println("写入前获取节点[" + path + "]的数据：" + zkClient.readData(path));
    Thread.sleep(1000);

    System.out.println("向节点[" + path + "]写入数据");
    zkClient.writeData(path, "ZkClientDemo");
    Thread.sleep(1000);

    System.out.println("写入后获取节点[" + path + "]的数据：" + zkClient.readData(path));
    Thread.sleep(1000);

    System.out.println("递归删除节点[" + path + "]");
    // 递归删除从根节点开始
    zkClient.deleteRecursive(path);
    System.out.println("是否存在节点[" + path + "]：" + zkClient.exists(path));
    // 休眠3秒以保证程序结束前ZkDataListener能够监听到节点的变化
    Thread.sleep(3000);
  }
}
