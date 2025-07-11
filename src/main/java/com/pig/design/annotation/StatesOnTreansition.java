package com.pig.design.annotation;

import com.pig.design.enums.ArticleState;
import org.springframework.statemachine.annotation.OnTransition;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@OnTransition
public @interface StatesOnTreansition {
    ArticleState[] source() default {};

    ArticleState[] target() default {};
}
