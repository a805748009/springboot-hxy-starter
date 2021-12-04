package com.hxy.springboot.starter.anno;

import org.springframework.core.annotation.AliasFor;
import org.springframework.stereotype.Component;

import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.ANNOTATION_TYPE, ElementType.TYPE})
@Component
@Controller
public @interface SecurityController {

    @AliasFor(
            annotation = Controller.class
    )
    String value() default "";

}
