package cn.sdadgz.web_springboot.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Result {

    private String code;
    private String msg;
    private Object data;

    // 不需要返回值
    public static Result success() {
        return new Result(Constants.CODE_200, "操作成功", null);
    }

    // 需要返回值
    public static Result success(Object data) {
        return new Result(Constants.CODE_200, "", data);
    }


    // 需要返回状态
    public static Result error(String code, String msg) {
        return new Result(code, msg, null);
    }

    // 需要返回状态
    public static Result error(String code, String msg, Object data) {
        return new Result(code, msg, data);
    }

    // 不需要返回状态
    public static Result error() {
        return new Result(Constants.CODE_500, "系统错误", null);
    }
}
