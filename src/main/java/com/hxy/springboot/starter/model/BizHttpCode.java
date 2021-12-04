package com.hxy.springboot.starter.model;

public class BizHttpCode {
    public int code;
    public String msg;
    public int status;

    public BizHttpCode(int code, String msg, int status) {
        this.code = code;
        this.msg = msg;
        this.status = status;
    }

    public BizHttpCode(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public static BizHttpCode C_0 = new BizHttpCode(0, "未知错误");
    public static BizHttpCode C_401 = new BizHttpCode(401, "未认证", 401);
    public static BizHttpCode C_403 = new BizHttpCode(403, "拒绝访问", 403);
    public static BizHttpCode C_404 = new BizHttpCode(404, "未找到资源", 404);
    public static BizHttpCode C_409 = new BizHttpCode(409, "资源冲突", 409);
    public static BizHttpCode C_500 = new BizHttpCode(500, "服务器内部错误", 500);
    public static BizHttpCode C_996 = new BizHttpCode(996, "不是可选的枚举值");
    public static BizHttpCode C_997 = new BizHttpCode(997, "获取第三方数据失败");
    public static BizHttpCode C_998 = new BizHttpCode(998, "HTTP Request 参数类型错误");
    public static BizHttpCode C_999 = new BizHttpCode(999, "HTTP Request 参数错误");
}
