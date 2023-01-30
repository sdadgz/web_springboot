package cn.sdadgz.web_springboot.toy;

import java.util.*;

public class Toy {

    // 以这个数字表示任意数字
    private long target = 114514;

    // 测试数字，默认值
    private long testNumber = 114514;

    // 返回结果
    private String ans = "";

    // 储存结果集
    private final Map<Long, ResultStack> results = new HashMap<>();

    private final List<String> allResult = new ArrayList<>();

    public Map<Long, ResultStack> getResults() {
        return results;
    }

    public List<String> getAllResult() {
        return allResult;
    }

    public Toy() {
    }

    public Toy(Long target) {
        if (target != null){
            String s = String.valueOf(target);
            if (s.length() > 10) {
                return;
            }
            this.target = target;
        }
    }

    // 计算
    public String start(long testNumber) {
        this.testNumber = testNumber;
        if (run(target)) {
            return ans;
        }
        return "又要去改bug咯" + ans;
    }

    // 第一级计算，避免无法算出结果所以直接翻倍
    private boolean run(long target) {
        // 字符串目标值
        String strTarget = String.valueOf(target);
        // 等于不用算了
        if (target == testNumber) {
            ans = strTarget;
            return true;
        }

        // 俩数拆开相乘会减小定理
//        if (target < testNumber) { // 数太大了
//            long newValue = increase(strTarget);
//            if (newValue > 0) {
//                return run(newValue);
//            } else {
//                // target超范围了，换方法
//                ans = otherWay();
//                return true;
//            }
//        }

        // 正常操作啊
        for (int i = 1; i < strTarget.length(); i++) {
            if (split(new long[]{target}, i)) {
                return true;
            }
        }

        long newValue = increase(strTarget);
        if (newValue > 0) {
            return run(newValue);
        } else {
            // target超范围了，换方法
            ans = otherWay();
            return true;
        }
    }

    // 分割numbers 末尾元素从索引处拆成两个
    private boolean split(long[] numbers, int index) {
        // todo 暴力，等重置
        // 分割
        long number = numbers[numbers.length - 1];
        String strNumber = String.valueOf(number);

        String begin = strNumber.substring(0, index);
        String end = strNumber.substring(index);

        // 开新的
        long[] newNumbers = Arrays.copyOf(numbers, numbers.length + 1);
        newNumbers[newNumbers.length - 2] = Long.parseLong(begin);
        newNumbers[newNumbers.length - 1] = Long.parseLong(end);

        // 栈结合递归
        if (recursion(newNumbers, new Stack<>(), 0, new Stack<>(), true)) {
            return true;
        }

        // 下一次分割
        for (int i = 1; i < end.length(); i++) {
            if (split(newNumbers, i)) {
                return true;
            }
        }

        return false;
    }

    // 递归结果，入栈
    private boolean recursion(long[] numbers,
                              Stack<NumberStack> stack,
                              int index,
                              Stack<ResultStack> stringBuilder,
                              boolean lastLevel) {

        // 不能同时出现同等级的运算符号并列，80%准确定理

        // 没有数可以入栈了，递归出口
        if (index >= numbers.length) {
            // 拦住最后一次没有计算的，长度不是1的
            if (stack.size() > 1) {
                return false;
            }

            // 最后的终点，一切的终点
            ResultStack top = stringBuilder.peek();
            ans = top.toString();
            long res = stack.peek().getValue();
            results.put(res, top);

//            allResult.add(res + "=" + ans);
//            System.out.println(res + "=" + ans);

            return res == testNumber;
//            return false;
        }

        // 入栈
        stack.push(new NumberStack(numbers[index]));

        // 递归，去赋运算符号
        if (operation(numbers, Util.stackDeepClone(stack), index, Util.stackDeepClone(stringBuilder), lastLevel)) {
            return true;
        }

        // 无操作直接入栈，收汁，他是最后一个了，不必要拷贝了吧
        return recursion(numbers, stack, index + 1, stringBuilder, lastLevel);
    }

    // 常规试值，根据栈长度
    private boolean operation(long[] numbers,
                              Stack<NumberStack> stack,
                              int index,
                              Stack<ResultStack> stringBuilder,
                              boolean lastLevel) {

        // 以高低两个优先级遍历所有深度，不包括0
        for (int i = 1; i < stack.size(); i++) {
            if (operation(numbers, Util.stackDeepClone(stack), index, Util.stackDeepClone(stringBuilder), lastLevel, true, i) ||
                    operation(numbers, Util.stackDeepClone(stack), index, Util.stackDeepClone(stringBuilder), lastLevel, false, i)) {
                return true;
            }
        }

        return false;
    }

    // 赋深度为多少的运算符
    private boolean operation(long[] numbers,
                              Stack<NumberStack> stack,
                              int index,
                              Stack<ResultStack> stringBuilder,
                              boolean lastLevel,
                              boolean thisLevel,
                              int deep) {

        // 递归出口，调用入栈
        if (deep < 1) {
            // 收汁，不拷贝
            return recursion(numbers, stack, index + 1, stringBuilder, lastLevel);
        }

        // 最上面保留的结果
        NumberStack m = stack.pop();

        // 如果栈顶不是结果，仍进去
        if (!m.isResult()) {
            stringBuilder.push(new ResultStack(m.getValue()));
        }

        // 取出公共部分
        NumberStack s = stack.pop();
        Stack<NumberStack> clone = Util.stackDeepClone(stack);
        Stack<ResultStack> sbClone = Util.stackDeepClone(stringBuilder);
        ResultStack sbm; // 栈顶抖m
        sbm = sbClone.pop().newRS();

        // 上符号，括号在上符号之后上
        if (thisLevel) {
            // 乘法
            // 结果仍clone里
            clone.push(new NumberStack(s.getValue() * m.getValue(), true));
            // 改变sb
            if (s.isResult()) {
                // 上一个是结果，合并
                assert sbm != null;
                sbClone.peek().append(sbm, "*", true);
            } else {
                // 上一个不是结果，插入到左边
                sbClone.push(sbm);
                sbClone.peek().insert(s.getValue() + "*", true);
            }
            // 递归
            if (operation(numbers, clone, index, sbClone, true, false, deep - 1)) {
                return true;
            }

            // 可以不clone吧 除法 乘法把副本玩了，除法可以玩本体吧
            if (m.getValue() != 0 && s.getValue() % m.getValue() == 0) { // 被除数不能为0 && 除的开
                // clone
                clone = stack;
                sbClone = stringBuilder;
                sbm = sbClone.pop().newRS();
                // 结果仍clone里
                clone.push(new NumberStack(s.getValue() / m.getValue(), true));
                // 改变sb
                if (s.isResult()) {
                    // 上一个是结果，合并
                    assert sbm != null;
                    sbClone.peek().append(sbm, "/", true);
                } else {
                    // 上一个不是结果，插入到左边
                    sbClone.push(sbm);
                    sbClone.peek().insert(s.getValue() + "/", true);
                }
                return operation(numbers, clone, index, sbClone, true, false, deep - 1);
            }

            return false;
        } else {
            // 加
            // 结果仍clone里
            clone.push(new NumberStack(s.getValue() + m.getValue(), true));
            // 改变sb
            if (s.isResult()) {
                // 上一个是结果，合并
                assert sbm != null;
                sbClone.peek().append(sbm, "+", false);
            } else {
                // 上一个不是结果，插入到左边
                sbClone.push(sbm);
                sbClone.peek().insert(s.getValue() + "+", false);
            }
            // 递归
            if (operation(numbers, clone, index, sbClone, false, true, deep - 1)) {
                return true;
            }

            // 减 看除法
            clone = stack;
            sbClone = stringBuilder;
            sbm = sbClone.pop().newRS();
            // 结果仍clone里
            long result = s.getValue() - m.getValue();
            clone.push(new NumberStack(Math.abs(result), true));
            // 改变sb
            if (s.isResult()) {
                // 上一个是结果，合并
                assert sbm != null;
                sbClone.peek().sub(sbm, result >= 0);
            } else {
                // 上一个不是结果，插入到左边
                sbClone.push(sbm);
                sbClone.peek().sub(s.getValue(), result >= 0);
            }
            // 递归
            return operation(numbers, clone, index, sbClone, false, true, deep - 1);
        }
    }

    // 太长了换方法
    private String otherWay() {
        Set<Long> keySet = results.keySet();
        Long[] longs = keySet.toArray(new Long[0]);
        Arrays.sort(longs);

        int i = -Arrays.binarySearch(longs, testNumber); // 必是复数，不然怎么会走到这一步呢
        long target = i > longs.length ? longs[longs.length - 1] : longs[i - 2];

        otherRecursion(longs, target, results.get(target).newRS());
        return ans;
    }

    // 将所选数字增至与 testNumber相同
    private void otherRecursion(Long[] longs, long target, ResultStack resultStack) {
        // 递归出口
        if (target == testNumber) {
            ans = resultStack.toString();
            return;
        }
        // 先试试倍数更高的乘法
        boolean change = false;
        long multiple = testNumber / target;
        if (multiple > 1) {
            int i = Arrays.binarySearch(longs, multiple);
            if (i != -1) {
                if (i < -1) {
                    // 不存在，但是他前面有东西
                    i = -i - 2;
                }
                resultStack.append(results.get(longs[i]), "*", true);
                target *= longs[i];
                change = true;
            }
        }

        // 乘法没用上，还是试试加法吧
        if (!change) {
            long add = testNumber - target;
            int i = Arrays.binarySearch(longs, add);
            if (i != -1) {
                if (i < -1) {
                    i = -i - 2;
                }
                resultStack.append(results.get(longs[i]), "+", false);
                target += longs[i];
            }
        }

        // 递归
        otherRecursion(longs, target, resultStack);
    }

    // 增加长度，-1表失败
    private long increase(String oldValue) {
        // 与其说我来判断，我觉得java比我更会判断，要不然我用异常类干嘛是吧
        try {
            return Long.parseLong((oldValue + target).length() < 10 ? oldValue + target : "-1");
        } catch (NumberFormatException e) {
            return -1;
        }
    }

}
