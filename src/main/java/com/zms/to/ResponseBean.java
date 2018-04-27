package com.zms.to;

import java.io.Serializable;
import java.util.List;

/**
 * @Author:zms
 * @Description:统一封装接口格式
 * @Date:Create On 2018/4/27 15:18
 */
public class ResponseBean implements Serializable {
    private static final long serialVersionUID = 1L;

    public static String SUCCESS = "0";

    public static String FAIL = "1";

    public static String NOLOGIN = "-1";

    /**
     * 状态-1未登录,1失败,0成功
     */
    private String code;

    /**
     * 当状态是1的时候，“操作成功”，若是0的时候，里面是其它的各种错误信息
     */
    private String msg;

    private List<?> data;


    private Integer count;

    public ResponseBean() {
        super();
    }

    public ResponseBean(String code, String msg) {
        super();
        this.code = code;
        this.msg = msg;
    }

    public ResponseBean(String code, String msg, List<?> data) {
        super();
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public ResponseBean(String code, String msg, List<?> data, int count) {
        super();
        this.code = code;
        this.msg = msg;
        this.data = data;
        this.count = count;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public List<?> getData() {
        return data;
    }

    public void setData(List<?> data) {
        this.data = data;
    }




}