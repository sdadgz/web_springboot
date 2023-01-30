package cn.sdadgz.web_springboot.toy;

public class NumberStack implements DeepCloneAble {

    private final Long value;

    private boolean isResult = false;

    // 默认构造
    public NumberStack(Long value) {
        this.value = value;
    }

    // 带是否是结果的构造
    public NumberStack(Long value, boolean isResult) {
        this.value = value;
        this.isResult = isResult;
    }

    // 获取值
    public Long getValue() {
        return value;
    }

    // 是否是结果
    public boolean isResult() {
        return isResult;
    }

    @Override
    public DeepCloneAble deepClone() {
        return new NumberStack(value, isResult);
    }
}
