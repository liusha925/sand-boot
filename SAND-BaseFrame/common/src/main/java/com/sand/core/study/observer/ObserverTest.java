package com.sand.core.study.observer;

/**
 * 功能说明：观察模式测试类 <br>
 * 开发人员：@author gy-hsh <br>
 * 开发时间：2021/7/13 10:07 <br>
 * 功能描述：写明作用，调用方式，使用场景，以及特殊情况 <br>
 */
public class ObserverTest {
    public static void main(String[] args) {
        // 创建观察目标
        ArticleObservable articleObservable = new ArticleObservable();

        // 添加观察者
        articleObservable.addObserver(new ReaderObserver("小明"));
        articleObservable.addObserver(new ReaderObserver("小张"));
        articleObservable.addObserver(new ReaderObserver("小爱"));

        // 发表文章
        articleObservable.publish("什么是观察者模式？");
    }
}