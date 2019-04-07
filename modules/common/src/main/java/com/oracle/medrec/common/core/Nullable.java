package com.oracle.medrec.common.core;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;


/**
 * Annotate parameter which can be null. The annotation is handled by {@link MethodParameterValidatorImpl}.
 *
 * @author : xiaojwu.
 *         Copyright (c) 2007, 2017, Oracle and/or its
 *         affiliates. All rights reserved.
 */
@Retention(RUNTIME)
@Target(PARAMETER)
public @interface Nullable {

}
