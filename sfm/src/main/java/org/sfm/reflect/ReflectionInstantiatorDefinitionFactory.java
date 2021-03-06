package org.sfm.reflect;

import java.lang.reflect.*;
import java.util.ArrayList;
import java.util.List;

public class ReflectionInstantiatorDefinitionFactory {

    interface ParameterBuilder {
        Parameter[] getParameters(Method m, Type target);
        Parameter[] getParameters(Constructor<?> c, Type target);
    }

    public static List<InstantiatorDefinition> extractDefinitions(Type target) {
        if (areParameterNamePresent(target)) {
            return extractDefinitionsWithParamNames(target);
        } else {
            return extractDefinitionsWithoutParamNames(target);
        }
    }

    @SuppressWarnings("unchecked")
    public static List<InstantiatorDefinition> extractDefinitionsWithoutParamNames(Type target) {
        return extractDefinitions(target, new ParameterBuilder() {

            @Override
            public Parameter[] getParameters(Method m, Type target) {
                return ReflectionInstantiatorDefinitionFactory.getParameters(m, target);
            }

            @Override
            public Parameter[] getParameters(Constructor<?> c, Type target) {
                return ReflectionInstantiatorDefinitionFactory.getParameters(c, target);
            }
        });
    }
    @SuppressWarnings("unchecked")
    public static List<InstantiatorDefinition> extractDefinitionsWithParamNames(Type target) {
        return extractDefinitions(target, new ParameterBuilder() {
            @Override
            public Parameter[] getParameters(Method m, Type target) {
                //IFJAVA8_START
                if (true) {
                    return getParametersWithName(m, target);
                }
                //IFJAVA8_END
                throw new IllegalStateException("Only supported on java8");
            }

            @Override
            public Parameter[] getParameters(Constructor<?> c, Type target) {
                //IFJAVA8_START
                if (true) {
                    return getParametersWithName(c, target);
                }
                //IFJAVA8_END
                throw new IllegalStateException("Only supported on java8");
            }
        });
    }

    @SuppressWarnings("unchecked")
    public static List<InstantiatorDefinition> extractDefinitions(Type target, ParameterBuilder parameterBuilder) {
        Class<?> clazz = TypeHelper.toClass(target);
        List<InstantiatorDefinition> instantiatorDefinitions = new ArrayList<InstantiatorDefinition>();

        for(Constructor<?> constructor : clazz.getDeclaredConstructors()) {
            if (Modifier.isPublic(constructor.getModifiers())) {
                InstantiatorDefinition definition = new InstantiatorDefinition(constructor, parameterBuilder.getParameters(constructor, target));
                instantiatorDefinitions.add(definition);
            }
        }

        for(Method m : clazz.getDeclaredMethods()) {
            if (Modifier.isPublic(m.getModifiers())
                    && Modifier.isStatic(m.getModifiers())
                    && clazz.isAssignableFrom(m.getReturnType())) {
                InstantiatorDefinition definition = new InstantiatorDefinition(m, parameterBuilder.getParameters(m, target));
                instantiatorDefinitions.add(definition);
            }
        }

        return instantiatorDefinitions;
    }


    //IFJAVA8_START
    private static Parameter[] getParametersWithName(Executable m, Type target) {

        final java.lang.reflect.Parameter[] ps = m.getParameters();
        Parameter[] parameters = new Parameter[ps.length];

        for(int i = 0; i < parameters.length; i++) {
            parameters[i] = new Parameter(i, ps[i].getName(), ps[i].getType(), ps[i].getParameterizedType());
        }

        return  parameters;
    }
    //IFJAVA8_END

    private static Parameter[] getParameters(Constructor<?> constructor, Type target) {
        return buildParameters(target, constructor.getParameterTypes(), constructor.getGenericParameterTypes(), TypeHelper.toClass(target).getTypeParameters());
    }


    private static Parameter[] getParameters(Method method, Type target) {
        return buildParameters(target, method.getParameterTypes(), method.getGenericParameterTypes(), TypeHelper.toClass(target).getTypeParameters());
    }

    private static Parameter[] buildParameters(Type target, Class<?>[] parameterTypes, Type[] parameterGenericTypes, TypeVariable<Class<Object>>[] targetClassTypeParameters) {
        Parameter[] parameters = new Parameter[parameterTypes.length];

        for(int i = 0; i < parameters.length; i++) {
            Type paramType = parameterGenericTypes[i];
            Type resolvedParamType = null;
            if (paramType instanceof TypeVariable) {
                TypeVariable<?> tv = (TypeVariable<?>) paramType;
                paramType = parameterTypes[i];
                if (target instanceof ParameterizedType) {
                    ParameterizedType pt = (ParameterizedType) target;
                    for (TypeVariable<Class<Object>> typeParameter : targetClassTypeParameters) {
                        if (typeParameter.getName().equals(tv.getName())) {
                            resolvedParamType = pt.getActualTypeArguments()[i];
                            break;
                        }
                    }
                }
            }
            if (resolvedParamType == null) {
                resolvedParamType = paramType;
            }
            parameters[i] = new Parameter(i, null, TypeHelper.toClass(paramType), resolvedParamType);
        }

        return parameters;
    }

    public static boolean areParameterNamePresent(Type target) {
        //IFJAVA8_START
        if (_areParameterNamePresent(target)) {
            return true;
        }
        //IFJAVA8_END
        return false;
    }

    //IFJAVA8_START
    // assume parameter name are either present or not for the type
    private static boolean _areParameterNamePresent(Type target) {
        Class<?> targetClass = TypeHelper.toClass(target);

        for(Method m : targetClass.getDeclaredMethods()) {
            final java.lang.reflect.Parameter[] parameters = m.getParameters();
            if (parameters.length > 0) {
                return parameters[0].isNamePresent();
            }
        }

        for(Constructor<?> c : targetClass.getDeclaredConstructors()) {
            final java.lang.reflect.Parameter[] parameters = c.getParameters();
            if (parameters.length > 0) {
                return parameters[0].isNamePresent();
            }
        }

        return false;
    }
    //IFJAVA8_END
}
