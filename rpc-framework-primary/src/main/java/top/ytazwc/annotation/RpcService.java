package top.ytazwc.annotation;

import java.lang.annotation.*;

/**
 * @author 花木凋零成兰
 * @title RpcService
 * @date 2024/6/17 10:59
 * @package top.ytazwc.annotation
 * @description TODO
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Inherited
public @interface RpcService {

    /**
     * Service version, default value is empty string
     */
    String version() default "";

    /**
     * Service group, default value is empty string
     */
    String group() default "";

}
