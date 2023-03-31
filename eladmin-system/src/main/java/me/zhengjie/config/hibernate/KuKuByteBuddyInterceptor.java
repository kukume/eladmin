package me.zhengjie.config.hibernate;

import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.proxy.pojo.bytebuddy.ByteBuddyInterceptor;
import org.hibernate.type.CompositeType;

import java.io.Serializable;
import java.lang.reflect.Method;

public class KuKuByteBuddyInterceptor extends ByteBuddyInterceptor {

    public KuKuByteBuddyInterceptor(String entityName, Class persistentClass, Class[] interfaces, Object id, Method getIdentifierMethod, Method setIdentifierMethod, CompositeType componentIdType, SharedSessionContractImplementor session, boolean overridesEquals) {
        super(entityName, persistentClass, interfaces, id, getIdentifierMethod, setIdentifierMethod, componentIdType, session, overridesEquals);
    }

    @Override
    public Object intercept(Object proxy, Method thisMethod, Object[] args) throws Throwable {
        try {
            return super.intercept(proxy, thisMethod, args);
        } catch (Exception e) {
            return null;
        }
    }
}
