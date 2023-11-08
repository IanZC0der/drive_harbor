package com.imooc.pan.lock.core;

import com.imooc.pan.lock.core.annotation.Lock;
import lombok.Data;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.reflect.MethodSignature;

import java.lang.reflect.Method;

/**
 * lock context
 * analyze the pointCut
 */
@Data
public class LockContext {

    /**
     * class name of the point cut
     */
    private String className;

    /**
     * method name of the point cut
     */
    private String methodName;

    /**
     * lock annotation on the point cut method
     */
    private Lock annotation;

    /**
     * classType
     */
    private Class classType;

    /**
     * method
     */
    private Method method;

    /**
     * args
     */
    private Object[] args;

    /**
     * parameters types
     */
    private Class[] parameterTypes;

    /**
     * target
     */
    private Object target;

    /**
     * lock context init
     *
     * @param proceedingJoinPoint
     * @return
     */
    public static LockContext init(ProceedingJoinPoint proceedingJoinPoint) {
        LockContext lockContext = new LockContext();
        doInit(lockContext, proceedingJoinPoint);
        return lockContext;
    }

    /**
     * do lock context init
     *
     * @param lockContext
     * @param proceedingJoinPoint
     */
    private static void doInit(LockContext lockContext, ProceedingJoinPoint proceedingJoinPoint) {
        Signature signature = proceedingJoinPoint.getSignature();
        Object[] args = proceedingJoinPoint.getArgs();
        Object target = proceedingJoinPoint.getTarget();
        String methodName = signature.getName();
        Class classType = signature.getDeclaringType();
        String className = signature.getDeclaringTypeName();
        Class[] parameterTypes = ((MethodSignature) signature).getParameterTypes();
        Method method = ((MethodSignature) signature).getMethod();
        Lock annotation = method.getAnnotation(Lock.class);

        lockContext.setArgs(args);
        lockContext.setTarget(target);
        lockContext.setMethodName(methodName);
        lockContext.setClassType(classType);
        lockContext.setClassName(className);
        lockContext.setParameterTypes(parameterTypes);
        lockContext.setMethod(method);
        lockContext.setAnnotation(annotation);
    }

}
