package com.sand.mybatisplus;

import org.apache.ibatis.jdbc.SQL;

/**
 * 功能说明：验证SQL构造器 <br>
 * 开发人员：@author gy-hsh <br>
 * 开发时间：2021/8/20 17:20 <br>
 * 功能描述：写明作用，调用方式，使用场景，以及特殊情况 <br>
 */
public class SQLConstructor {
    /**
     * <p>
     * 功能描述：匿名内部类风格
     * </p>
     * 开发人员：@author hsh
     * 开发时间：2021/8/20 17:21
     * 修改记录：新建
     *
     * @return java.lang.String
     */
    private String testAnonymousSql() {
        return new SQL() {{
            SELECT("P.ID, P.USERNAME, P.PASSWORD, P.FULL_NAME");
            SELECT("P.LAST_NAME, P.CREATED_ON, P.UPDATED_ON");
            FROM("PERSON P");
            FROM("ACCOUNT A");
            INNER_JOIN("DEPARTMENT D on D.ID = P.DEPARTMENT_ID");
            INNER_JOIN("COMPANY C on D.COMPANY_ID = C.ID");
            WHERE("P.ID = A.ID");
            WHERE("P.FIRST_NAME like ?");
            OR();
            WHERE("P.LAST_NAME like ?");
            GROUP_BY("P.ID");
            HAVING("P.LAST_NAME like ?");
            OR();
            HAVING("P.FIRST_NAME like ?");
            ORDER_BY("P.ID");
            ORDER_BY("P.FULL_NAME");
        }}.toString();
    }

    /**
     * <p>
     * 功能描述：Builder / Fluent 风格
     * </p>
     * 开发人员：@author gy-hsh
     * 开发时间：2021/8/20 17:23
     * 修改记录：新建
     *
     * @return java.lang.String
     */
    public String testBuilderSql() {
        String sql = new SQL()
                .INSERT_INTO("PERSON")
                .VALUES("ID, FIRST_NAME", "#{id}, #{firstName}")
                .VALUES("LAST_NAME", "#{lastName}")
                .toString();
        return sql;
    }

    /**
     * <p>
     * 功能描述：动态条件（注意参数需要使用 final 修饰，以便匿名内部类对它们进行访问）
     * </p>
     * 开发人员：@author gy-hsh
     * 开发时间：2021/8/20 17:23
     * 修改记录：新建
     *
     * @param id        id
     * @param firstName firstName
     * @param lastName  lastName
     * @return java.lang.String
     */
    public String testDynamicSql(final String id, final String firstName, final String lastName) {
        return new SQL() {{
            SELECT("P.ID, P.USERNAME, P.PASSWORD, P.FIRST_NAME, P.LAST_NAME");
            FROM("PERSON P");
            if (id != null) {
                WHERE("P.ID like #{id}");
            }
            if (firstName != null) {
                WHERE("P.FIRST_NAME like #{firstName}");
            }
            if (lastName != null) {
                WHERE("P.LAST_NAME like #{lastName}");
            }
            ORDER_BY("P.LAST_NAME");
        }}.toString();
    }

}
