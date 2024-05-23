package top.ytazwc.extension;

/**
 * @author 花木凋零成兰
 * @title Holder
 * @date 2024/5/23 15:37
 * @package top.ytazwc.extension
 * @description 多个线程持有
 */
public class Holder<T> {

    /**
     * 值
     * volatile 当一个线程修改了value 其他线程也能看到
     */
    private volatile T value;

    public T get() {
        return value;
    }

    public void set(T value) {
        this.value = value;
    }

}
