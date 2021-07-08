package com.sand.core.search.bean;

import lombok.Data;

/**
 * 功能说明：排序 <br>
 * 开发人员：@author gy-hsh <br>
 * 开发时间：2021/7/8 9:25 <br>
 * 功能描述：写明作用，调用方式，使用场景，以及特殊情况 <br>
 */
@Data
public class SortBean {
    /**
     * 搜索唯一标识
     */
    private String id;
    /**
     * 出现的次数
     */
    private int times;
}