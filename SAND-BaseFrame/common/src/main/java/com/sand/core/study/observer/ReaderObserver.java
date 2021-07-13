package com.sand.core.study.observer;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.Observable;
import java.util.Observer;

/**
 * 功能说明：观察者 <br>
 * 开发人员：@author gy-hsh <br>
 * 开发时间：2021/7/13 9:58 <br>
 * 功能描述：写明作用，调用方式，使用场景，以及特殊情况 <br>
 */
@RequiredArgsConstructor
public class ReaderObserver implements Observer {
    @NonNull
    private String name;

    @Override
    public void update(Observable o, Object arg) {
        updateArticle(arg);
    }

    /**
     * <p>
     * 功能描述：更新文章
     * </p>
     * 开发人员：@author gy-hsh
     * 开发时间：2021/7/13 10:02
     * 修改记录：新建
     *
     * @param arg arg
     */
    private void updateArticle(Object arg) {
        System.out.printf("我是读者：%s，文章已更新：%s\n", this.name, arg);
    }
}