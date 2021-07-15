package com.sand.core.snapshots.observer;

import java.util.Observable;

/**
 * 功能说明：观察目标（被观察者/主题） <br>
 * 开发人员：@author gy-hsh <br>
 * 开发时间：2021/7/13 9:53 <br>
 * 功能描述：写明作用，调用方式，使用场景，以及特殊情况 <br>
 */
public class ArticleObservable extends Observable {
    /**
     * <p>
     * 功能描述：发表文章
     * </p>
     * 开发人员：@author gy-hsh
     * 开发时间：2021/7/13 9:57
     * 修改记录：新建
     *
     * @param article article
     */
    public void publish(String article) {
        // 改变状态
        this.setChanged();
        // 通知所有观察者
        this.notifyObservers(article);
    }
}