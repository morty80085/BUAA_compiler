package backend;

public class Register {
    private String name;

    // 零寄存器：恒为零，任何写入无效，常用于常数0或比较
    public static Register zero = new Register("zero");

    // 汇编暂存：由汇编器保留用于伪指令展开，程序员不应直接使用
    public static Register at = new Register("at");

    // 返回值寄存器：用于存储函数返回结果
    public static Register v0 = new Register("v0");
    public static Register v1 = new Register("v1");

    // 参数寄存器：用于传递函数的前4个参数
    public static Register a0 = new Register("a0");
    public static Register a1 = new Register("a1");
    public static Register a2 = new Register("a2");
    public static Register a3 = new Register("a3");

    // 临时寄存器：调用者保存，可在函数中自由使用而不保留值
    public static Register t0 = new Register("t0");
    public static Register t1 = new Register("t1");
    public static Register t2 = new Register("t2");
    public static Register t3 = new Register("t3");
    public static Register t4 = new Register("t4");
    public static Register t5 = new Register("t5");
    public static Register t6 = new Register("t6");
    public static Register t7 = new Register("t7");

    // 保存寄存器：被调用者保存，使用时必须保留原值并在返回前恢复
    public static Register s0 = new Register("s0");
    public static Register s1 = new Register("s1");
    public static Register s2 = new Register("s2");
    public static Register s3 = new Register("s3");
    public static Register s4 = new Register("s4");
    public static Register s5 = new Register("s5");
    public static Register s6 = new Register("s6");
    public static Register s7 = new Register("s7");

    // 临时寄存器：额外的临时寄存器，调用者保存
    public static Register t8 = new Register("t8");
    public static Register t9 = new Register("t9");

    // 内核寄存器：保留给操作系统内核和异常处理程序使用
    public static Register k0 = new Register("k0");
    public static Register k1 = new Register("k1");

    // 全局指针：指向静态数据区，用于快速访问全局变量
    public static Register gp = new Register("gp");

    // 栈指针：指向当前栈顶，用于函数调用和局部变量存储
    public static Register sp = new Register("sp");

    // 帧指针：指向当前栈帧底部，用于调试和栈回溯
    public static Register fp = new Register("fp");

    // 返回地址：存储函数调用后的返回地址
    public static Register ra = new Register("ra");

    public Register(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "$" + name;
    }
}
