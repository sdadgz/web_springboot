package cn.sdadgz.web_springboot.toy;

import java.util.Stack;

public class Util {

    public static <T extends DeepCloneAble> Stack<T> stackDeepClone(Stack<T> src) {
        Stack<T> temp = new Stack<>();
        Stack<T> res = new Stack<>();
        while (!src.empty()) {
            temp.push(src.pop());
        }
        while (!temp.empty()) {
            src.push(temp.peek());
            res.push((T) temp.pop().deepClone());
        }
        return res;
    }

}
