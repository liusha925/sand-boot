package com.sand.core.snapshots.search;

import com.sand.core.snapshots.search.handler.SearchHandler;

import java.util.List;

/**
 * 功能说明：全文搜索测试类 <br>
 * 开发人员：@author gy-hsh <br>
 * 开发时间：2021/7/8 9:24 <br>
 * 功能描述：写明作用，调用方式，使用场景，以及特殊情况 <br>
 */
public class SearchTest {
    public static void main(String[] args) {
        // 建立搜索库
        SearchHandler searchHandler = SearchHandler.getSearchBase();
        searchHandler.add("1", "你好！", "你好！");
        searchHandler.add("2", "你好！我是张三。", "你好！我是张三。");
        searchHandler.add("3", "今天的天气挺好的。", "今天的天气挺好的。");
        searchHandler.add("4", "你是谁？", "你是谁？");
        searchHandler.add("5", "高数这门学科很难", "高数确实很难。");
        searchHandler.add("6", "测试", "上面的只是测试");
        // 搜索的内容
        String ids = searchHandler.getIds("你的高数");
        System.out.println(ids);
        List<Object> objs = searchHandler.getObjects(ids);
        if (objs != null) {
            for (Object obj : objs) {
                System.out.println((String) obj);
            }
        }
    }
}