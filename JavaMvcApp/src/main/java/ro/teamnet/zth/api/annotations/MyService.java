package ro.teamnet.zth.api.annotations;

import java.lang.annotation.*;

/**
 * Created by user on 18.07.2016.
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface MyService {
    String name();
}
