package com.sand.core.util;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * 功能说明：集合工具类 <br>
 * 开发人员：gy-hsh <br>
 * 开发时间：2021/6/16 16:22 <br>
 * 功能描述：写明作用，调用方式，使用场景，以及特殊情况 <br>
 */
public class ListUtil {

    /**
     * <p>
     * 功能描述：按比例抽取集合
     * </p>
     * 开发人员：gy-hsh
     * 开发时间：2021/6/16 9:31
     * 修改记录：新建
     *
     * @param list  抽取集合
     * @param scale 占比
     */
    @SuppressWarnings("rawtypes")
    public static List getListByScale(List list, BigDecimal scale) {
        if (null == list) {
            return new ArrayList<>();
        }
        int count = new BigDecimal(list.size()).multiply(scale)
                .setScale(0, BigDecimal.ROUND_UP)
                .intValue();
        return getRandomList(list, count);
    }

    /**
     * <p>
     * 功能描述：按个数抽取集合
     * </p>
     * 开发人员：gy-hsh
     * 开发时间：2021/6/16 9:31
     * 修改记录：新建
     *
     * @param list  抽取集合
     * @param count 抽取元素的个数
     */
    @SuppressWarnings("rawtypes")
    public static List getListByCount(List list, int count) {
        return getRandomList(list, count);
    }

    /**
     * <p>
     * 功能描述：随机抽取集合
     * </p>
     * 开发人员：gy-hsh
     * 开发时间：2021/6/16 9:31
     * 修改记录：新建
     *
     * @param list  抽取集合
     * @param count 抽取元素的个数
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    public static List getRandomList(List list, int count) {
        if (null == list) {
            return new ArrayList<>();
        }
        int size = list.size();
        if (size <= 0) {
            return new ArrayList<>();
        }
        if (count <= 0) {
            return new ArrayList<>();
        }
        if (size <= count) {
            return list;
        }

        Random random = new Random();
        // 临时存放产生的list索引，去除重复的索引
        List<Integer> tempList = new ArrayList<>();
        // 生成新的list集合
        List newList = new ArrayList<>();
        int temp = 0;
        // 如果数据小于1，取一条数据
        if (count <= 1) {
            temp = random.nextInt(size);
            newList.add(list.get(temp));
        } else {
            for (int i = 0; i < Math.ceil(count); i++) {
                // 初始化一个随机数，将产生的随机数作为被抽list的索引
                temp = random.nextInt(size);
                // 判断随机抽取的随机数
                if (!tempList.contains(temp)) {
                    tempList.add(temp);
                    newList.add(list.get(temp));
                } else {
                    i--;
                }
            }
        }
        return newList;
    }
}
