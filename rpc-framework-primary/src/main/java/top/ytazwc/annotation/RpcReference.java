package top.ytazwc.annotation;

import java.lang.annotation.*;

/**
 * @author 花木凋零成兰
 * @title RpcReference
 * @date 2024/6/17 10:52
 * @package top.ytazwc.annotation
 * @description RPC引用注解，自动连接服务实现类
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
@Inherited
public @interface RpcReference {

    /**
     * 服务版本，默认值为空字符串
     */
    String version() default "";

    /**
     * 服务组，默认值为空字符串
     */
    String group() default "";

}
