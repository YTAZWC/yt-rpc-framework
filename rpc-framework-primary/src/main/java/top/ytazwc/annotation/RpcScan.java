package top.ytazwc.annotation;

import org.springframework.context.annotation.Import;
import top.ytazwc.spring.CustomScannerRegistrar;

import java.lang.annotation.*;

/**
 * @author 花木凋零成兰
 * @title RpcScan
 * @date 2024/6/17 10:54
 * @package top.ytazwc.annotation
 * @description 扫描自定义批注
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Import(CustomScannerRegistrar.class)
@Documented
public @interface RpcScan {

    String[] basePackage();

}
