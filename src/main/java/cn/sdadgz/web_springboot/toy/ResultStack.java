package cn.sdadgz.web_springboot.toy;

public class ResultStack implements DeepCloneAble {

    private StringBuilder value;

    private boolean isHighLevel = true;

    public ResultStack(Long l) {
        init(l);
    }

    @Override
    public String toString() {
        return value.toString();
    }

    // 最开始得放进去点东西
    public void init(Long l) {
        value = new StringBuilder(l.toString());
    }

    public ResultStack(StringBuilder value, boolean isHighLevel) {
        this.value = value;
        this.isHighLevel = isHighLevel;
    }

    protected ResultStack newRS() {
        ResultStack clone = new ResultStack(value, isHighLevel);
        clone.value = new StringBuilder(clone.value);
        return clone;
    }

    // 在屁股后面加东西
    public void append(ResultStack rs, String sign, boolean level) {
        // 确定括号
        rs.addBrackets(level);
        addBrackets(level);

        // 首位是负号
        if (rs.value.charAt(0) == '-') {
            if (level) {
                // 高运算级时 负号开头需要加括号 因为首位是负号可以省略
                for (int i = 0; i < rs.value.length(); i++) {
                    char c = rs.value.charAt(i);
                    if (c == '*' || c == '/') { // 必须有高优先级的运算符，不然不会出负号
                        rs.value.insert(i, ')');
                        break;
                    }
                }
                rs.value.insert(0, sign + "(");
            } else {
                // 需要逆置为+
                if ("-".equals(sign)) {
                    value.replace(0, 1, "+");
                }
            }
        } else {
            value.append(sign);
        }

        value.append(rs.value);
        isHighLevel = level;
    }

    // 在前面加上东西
    public void insert(String s, boolean level) {
        // 上一次运算是低优先级的运算，这次运算是高优先级的运算，需要加上括号
        addBrackets(level);

        isHighLevel = level;
        value.insert(0, s);
    }

    // 加括号
    private void addBrackets(boolean level) {
        if (!isHighLevel && level) {
            value.insert(0, '(');
            value.append(')');
        }
    }

    // 减法 在前面加东西
    public void sub(Long l, boolean isNormal) {
        // 如果上次还是减法
        if (isNormal) {
            // 正常中间减号
            // 需要套括号
            addBrackets(true);

            // 加餐
            if (value.charAt(0) == '-') {
                // 如果头是减号，逆置
                value.replace(0, 1, "+");
                value.insert(0, l);
            } else {
                value.insert(0, l + "-");
            }
        } else {
            // 最前面加负号，中间是加号
            value.insert(0, value.charAt(0) == '-' ? "-" + l : "-" + l + "+");
        }
        isHighLevel = false;
    }

    // 减法，前面是结果 在后面加东西
    public void sub(ResultStack resultStack, boolean isNormal) {
        if (isNormal) {
            // 后者套括号
            resultStack.addBrackets(true);

            // 中间是减号
            if (resultStack.value.charAt(0) == '-') {
                // 受的头是减号，得逆置
                resultStack.value.replace(0, 1, "+");
            } else {
                // 正常减
                value.append('-');
            }
        } else {
            // 前者套括号
            addBrackets(true);

            // 负号在开头
            if (value.charAt(0) == '-') {
                value.replace(0, 1, "");
            } else {
                value.insert(0, '-');
            }
            value.append(resultStack.value.charAt(0) == '-' ? "" : '+');
        }
        value.append(resultStack);
        isHighLevel = false;
    }

    // 否定内移
    private void minusInsert() {
        // 在内移之前需要先加上负号

    }

    @Override
    public DeepCloneAble deepClone() {
        return newRS();
    }
}
