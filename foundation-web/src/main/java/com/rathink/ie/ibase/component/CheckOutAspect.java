package com.rathink.ie.ibase.component;

import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

/**
 * Created by Hean on 2015/10/31.
 */
@Aspect
@Component
public class CheckOutAspect {

    @Before("@annotation(com.rathink.ie.ibase.component.CheckOut)")
    public void checkOut2() {
        System.out.println("test annotation");
    }
}
