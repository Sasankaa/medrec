package com.oracle.medrec.common.core;

import javax.interceptor.InterceptorBinding;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Annotate a method or all methods of a class is being cached.
 * It suit the 'get' method which is a time-consuming but updated infrequently,
 * e.g. using webservice to get all records of a patient.
 * First invocation invokes the real method and then push the results into cache.
 * Second time, the client will get the results from cache directly instead.
 *
 * @author : xiaojwu.
 *         Copyright (c) 2007, 2017, Oracle and/or its
 *         affiliates. All rights reserved.
 * @see MethodInvocationCachingInterceptor
 */
@InterceptorBinding
@Target({TYPE, METHOD})
@Retention(RUNTIME)
public @interface MethodInvocationCached {

}
