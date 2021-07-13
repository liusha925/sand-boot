package com.sand.core.study.search.base;

import com.sand.core.study.search.bean.SortBean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

/**
 * 功能说明：全文搜索工具类 <br>
 * 开发人员：@author hsh <br>
 * 开发时间：2021/7/8 9:26 <br>
 * 功能描述：写明作用，调用方式，使用场景，以及特殊情况 <br>
 */
public class SearchBase {
    /**
     * details 存储搜素对象的详细信息，其中key作为区分Object的唯一标识
     */
    private HashMap<String, Object> details = new HashMap<>();
    /**
     * 对于参与搜索的关键词，这里采用的稀疏数组存储，也可以采用HashMap来存储，定义格式如下
     * private static HashMap<Integer, HashSet<String>> keySearch = new HashMap<Integer, HashSet<String>>();
     * HashMap中额key值相当于稀疏数组中的下标，value相当于稀疏数组在该位置的值
     */
    @SuppressWarnings("unchecked")
    private HashSet<String>[] keySearch = new HashSet[Character.MAX_VALUE];

    /**
     * 这里把构造方法设置成私有为的是单例模式
     */
    private SearchBase() {

    }

    /**
     * <p>
     * 功能描述：实现单例模式，采用Initialization on Demand Holder加载
     * </p>
     * 开发人员：@author hsh
     * 开发时间：2021/7/8 9:29
     * 修改记录：新建
     */
    private static class LazyLoadSearchBase {
        private static final SearchBase searchBase = new SearchBase();
    }

    /**
     * <p>
     * 功能描述：获取单例
     * </p>
     * 开发人员：@author hsh
     * 开发时间：2021/7/8 9:30
     * 修改记录：新建
     *
     * @return com.sand.core.search.base.SearchBase
     */
    public static SearchBase getSearchBase() {
        return LazyLoadSearchBase.searchBase;
    }

    /**
     * <p>
     * 功能描述：根据id获取详细
     * </p>
     * 开发人员：@author hsh
     * 开发时间：2021/7/8 9:30
     * 修改记录：新建
     *
     * @param id id
     * @return java.lang.Object
     */
    public Object getObject(String id) {
        return details.get(id);
    }

    /**
     * <p>
     * 功能描述：根据ids获取详细，id之间用","隔开
     * </p>
     * 开发人员：@author hsh
     * 开发时间：2021/7/8 9:31
     * 修改记录：新建
     *
     * @param ids ids
     * @return java.util.List<java.lang.Object>
     */
    public List<Object> getObjects(String ids) {
        if (ids == null || "".equals(ids)) {
            return null;
        }
        List<Object> objs = new ArrayList<>();
        String[] idArray = ids.split(",");
        for (String id : idArray) {
            objs.add(getObject(id));
        }
        return objs;
    }

    /**
     * <p>
     * 功能描述：根据搜索词查找对应的id，id之间用","分割
     * </p>
     * 开发人员：@author hsh
     * 开发时间：2021/7/8 9:31
     * 修改记录：新建
     *
     * @param key key
     * @return java.lang.String
     */
    public String getIds(String key) {
        if (key == null || "".equals(key)) {
            return null;
        }
        // 查找idTimes存储搜索词每个字符在id中是否出现
        HashMap<String, Integer> idTimes = new HashMap<>(key.length());
        // ids存储出现搜索词中的字符的id
        HashSet<String> ids = new HashSet<>();
        // 从搜索库中去查找
        for (int i = 0; i < key.length(); i++) {
            int at = key.charAt(i);
            // 搜索词库中没有对应的字符，则进行下一个字符的匹配
            if (keySearch[at] == null) {
                continue;
            }
            for (Object obj : keySearch[at].toArray()) {
                String id = (String) obj;
                int times = 1;
                if (ids.contains(id)) {
                    times += idTimes.get(id);
                    idTimes.put(id, times);
                } else {
                    ids.add(id);
                    idTimes.put(id, times);
                }
            }
        }

        // 使用数组排序
        List<SortBean> sortBeans = new ArrayList<>();
        for (String id : ids) {
            SortBean sortBean = new SortBean();
            sortBeans.add(sortBean);
            sortBean.setId(id);
            sortBean.setTimes(idTimes.get(id));
        }
        sortBeans.sort((o1, o2) -> o2.getTimes() - o1.getTimes());

        // 构建返回字符串
        StringBuffer sb = new StringBuffer();
        for (SortBean sortBean : sortBeans) {
            sb.append(sortBean.getId());
            sb.append(",");
        }

        // 释放资源
        idTimes.clear();
        idTimes = null;
        ids.clear();
        ids = null;
        sortBeans.clear();
        sortBeans = null;

        // 返回
        return sb.toString();
    }

    /**
     * <p>
     * 功能描述：添加搜索记录
     * </p>
     * 开发人员：@author hsh
     * 开发时间：2021/7/8 9:32
     * 修改记录：新建
     *
     * @param id        id
     * @param searchKey searchKey
     * @param obj       obj
     */
    public void add(String id, String searchKey, Object obj) {
        // 参数有部分为空，不加载
        if (id == null || searchKey == null || obj == null) {
            return;
        }
        // 保存对象
        details.put(id, obj);
        // 保存搜索词
        addSearchKey(id, searchKey);
    }

    /**
     * <p>
     * 功能描述：将搜索词加入到搜索域中
     * </p>
     * 开发人员：@author hsh
     * 开发时间：2021/7/8 9:33
     * 修改记录：新建
     *
     * @param id        id
     * @param searchKey searchKey
     */
    private void addSearchKey(String id, String searchKey) {
        // 参数有部分为空，不加载，这里是私有方法，可以不做如下判断，但为了设计规范，还是加上
        if (id == null || searchKey == null) {
            return;
        }
        // 下面采用的是字符分词，这里也可以使用现在成熟的其他分词器
        for (int i = 0; i < searchKey.length(); i++) {
            // at值相当于是数组的下标，id组成的HashSet相当于数组的值
            int at = searchKey.charAt(i);
            if (keySearch[at] == null) {
                HashSet<String> value = new HashSet<>();
                keySearch[at] = value;
            }
            keySearch[at].add(id);
        }
    }
}