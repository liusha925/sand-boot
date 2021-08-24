package com.sand.core.util.ys7.enums;

/**
 * 功能说明：萤石返回码 <br>
 * 开发人员：@author gy-hsh <br>
 * 开发时间：2021/8/24 9:41 <br>
 * 功能描述：来源https://open.ys7.com/doc/zh/book/index/api-code.html <br>
 */
public enum YS7Status {
    /**
     * 返回码、返回消息、描述
     */
    CODE_200(200, "操作成功", "请求成功"),
    CODE_10001(10001, "参数错误", "参数为空或格式不正确"),
    CODE_10002(10002, "accessToken异常或过期", "重新获取accessToken"),
    CODE_10004(10004, "用户不存在", ""),
    CODE_10005(10005, "appKey异常", "appKey被冻结"),
    CODE_10017(10017, "appKey不存在", "确认appKey是否正确"),
    CODE_10026(10026, "设备数量超出个人版限制，当前设备无法操作，请充值企业版", ""),
    CODE_20002(20002, "设备不存在", ""),
    CODE_20007(20007, "设备不在线", ""),
    CODE_20014(20014, "deviceSerial不合法", ""),
    CODE_20018(20018, "该用户不拥有该设备", "检查设备是否属于当前账户"),
    CODE_20032(20032, "该用户下通道不存在", ""),
    CODE_60020(60020, "不支持该命令", "确认设备是否支持预览操作"),
    CODE_60063(60063, "直播未使用或直播已关闭", ""),
    CODE_49999(49999, "数据异常", "接口调用异常"),
    ;
    private int code;
    private String msg;
    private String description;

    YS7Status(int code, String msg, String description) {
        this.code = code;
        this.msg = msg;
        this.description = description;
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    public String getDescription() {
        return description;
    }

    public static YS7Status getByCode(int code) {
        for (YS7Status item : YS7Status.values()) {
            if (item.code == code) {
                return item;
            }
        }
        return null;
    }

    public static String getMsgByCode(int code) {
        for (YS7Status item : YS7Status.values()) {
            if (item.code == code) {
                return item.msg;
            }
        }
        return null;
    }
}
