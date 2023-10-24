package com.imooc.pan.core.utils;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.springframework.aop.support.AopUtils;
import org.springframework.context.expression.AnnotatedElementKey;
import org.springframework.context.expression.CachedExpressionEvaluator;
import org.springframework.context.expression.MethodBasedEvaluationContext;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.core.ParameterNameDiscoverer;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * utils for analyzing SpEl expressions
 */
public class SpElUtil {

    private static final RPanExpressionEvaluator expressionEvaluator = new RPanExpressionEvaluator();

    /**
     *
     * @param spElExpression
     * @param returnType
     * @param className
     * @param methodName
     * @param classType
     * @param method
     * @param args
     * @param parameterTypes
     * @param target
     * @param <T>
     * @return
     */
    public static <T> T getCustomerValue(String spElExpression,
                                         Class<T> returnType,
                                         String className,
                                         String methodName,
                                         Class classType,
                                         Method method,
                                         Object[] args,
                                         Class[] parameterTypes,
                                         Object target) {
        EvaluationContext evaluationContext = expressionEvaluator.createEvaluationContext(className, methodName, classType, method, args, parameterTypes, target);
        AnnotatedElementKey methodKey = new AnnotatedElementKey(method, classType);
        return expressionEvaluator.getValueWithCustomerType(spElExpression, methodKey, evaluationContext, returnType);
    }

    /**
     *
     * @param spElExpression
     * @param className
     * @param methodName
     * @param classType
     * @param method
     * @param args
     * @param parameterTypes
     * @param target
     * @return
     */
    public static Object getValue(String spElExpression,
                                  String className,
                                  String methodName,
                                  Class classType,
                                  Method method,
                                  Object[] args,
                                  Class[] parameterTypes,
                                  Object target) {
        EvaluationContext evaluationContext = expressionEvaluator.createEvaluationContext(className, methodName, classType, method, args, parameterTypes, target);
        AnnotatedElementKey methodKey = new AnnotatedElementKey(method, classType);
        return expressionEvaluator.getValue(spElExpression, methodKey, evaluationContext);
    }

    /**
     *
     * @param spElExpression
     * @param className
     * @param methodName
     * @param classType
     * @param method
     * @param args
     * @param parameterTypes
     * @param target
     * @return
     */
    public static String getStringValue(String spElExpression,
                                        String className,
                                        String methodName,
                                        Class classType,
                                        Method method,
                                        Object[] args,
                                        Class[] parameterTypes,
                                        Object target) {
        EvaluationContext evaluationContext = expressionEvaluator.createEvaluationContext(className, methodName, classType, method, args, parameterTypes, target);
        AnnotatedElementKey methodKey = new AnnotatedElementKey(method, classType);
        return expressionEvaluator.getValueWithStringType(spElExpression, methodKey, evaluationContext);
    }

    /**
     * #root.className
     * #root.methodName
     * #root.classType
     * #root.method
     * #root.args
     * #root.parameterTypes
     * #root.target
     */
    @Data
    @AllArgsConstructor
    private static class RPanExpressionRootObject {

        private String className;

        private String methodName;

        private Class classType;

        private Method method;

        private Object[] args;

        private Class[] parameterTypes;

        private Object target;

    }

    @Data
    private static class RPanExpressionEvaluator extends CachedExpressionEvaluator {
        private final ParameterNameDiscoverer paramNameDiscoverer = new DefaultParameterNameDiscoverer();
        private final Map<ExpressionKey, Expression> conditionCache = new ConcurrentHashMap<>(256);
        private final Map<AnnotatedElementKey, Method> targetMethodCache = new ConcurrentHashMap<>(256);

        /**
         *
         * @param className
         * @param methodName
         * @param classType
         * @param method
         * @param args
         * @param parameterTypes
         * @param target
         * @return
         */
        private EvaluationContext createEvaluationContext(String className,
                                                          String methodName,
                                                          Class classType,
                                                          Method method,
                                                          Object[] args,
                                                          Class[] parameterTypes,
                                                          Object target) {
            Method targetMethod = getTargetMethod(classType, method);
            RPanExpressionRootObject root = new RPanExpressionRootObject(className, methodName, classType, method, args, parameterTypes, target);
            return new MethodBasedEvaluationContext(root, targetMethod, args, this.paramNameDiscoverer);
        }

        /**
         *
         * @param conditionExpression
         * @param elementKey
         * @param evalContext
         * @param clazz
         * @return
         */
        public <T> T getValueWithCustomerType(String conditionExpression, AnnotatedElementKey elementKey, EvaluationContext evalContext, Class<T> clazz) {
            return getExpression(this.conditionCache, elementKey, conditionExpression).getValue(evalContext, clazz);
        }

        /**
         *
         * @param conditionExpression
         * @param elementKey
         * @param evalContext
         * @return
         */
        public Object getValue(String conditionExpression, AnnotatedElementKey elementKey, EvaluationContext evalContext) {
            Expression expression = getExpression(this.conditionCache, elementKey, conditionExpression);
            return expression.getValue(evalContext);
        }

        /**
         *
         * @param conditionExpression
         * @param elementKey
         * @param evalContext
         * @return
         */
        public String getValueWithStringType(String conditionExpression, AnnotatedElementKey elementKey, EvaluationContext evalContext) {
            Object value = getValue(conditionExpression, elementKey, evalContext);
            if (Objects.nonNull(value)) {
                return value.toString();
            }
            return StringUtils.EMPTY;
        }

        /**
         *
         * @param targetClass
         * @param method
         * @return
         */
        private Method getTargetMethod(Class<?> targetClass, Method method) {
            AnnotatedElementKey methodKey = new AnnotatedElementKey(method, targetClass);
            Method targetMethod = this.targetMethodCache.get(methodKey);
            if (targetMethod == null) {
                targetMethod = AopUtils.getMostSpecificMethod(method, targetClass);
                if (targetMethod == null) {
                    targetMethod = method;
                }
                this.targetMethodCache.put(methodKey, targetMethod);
            }
            return targetMethod;
        }
    }

}
