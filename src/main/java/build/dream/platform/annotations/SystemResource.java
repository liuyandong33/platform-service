package build.dream.platform.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface SystemResource {
    String name() default "";
    String code() default "";
    String controllerName() default "";
    String actionName() default "";
    String remark() default "";
}
