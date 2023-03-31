package me.zhengjie.config.hibernate;

import org.hibernate.bytecode.internal.bytebuddy.ByteBuddyState;
import org.hibernate.bytecode.internal.bytebuddy.ProxyFactoryFactoryImpl;
import org.hibernate.engine.spi.SessionFactoryImplementor;
import org.hibernate.proxy.ProxyFactory;
import org.hibernate.proxy.pojo.bytebuddy.ByteBuddyProxyHelper;

import java.lang.reflect.Field;

public class KuKuProxyFactoryFactory extends ProxyFactoryFactoryImpl {

    public KuKuProxyFactoryFactory(ByteBuddyState byteBuddyState, ByteBuddyProxyHelper byteBuddyProxyHelper) {
        super(byteBuddyState, byteBuddyProxyHelper);
    }


    @Override
    public ProxyFactory buildProxyFactory(SessionFactoryImplementor sessionFactory) {
        try {
            Class<ProxyFactoryFactoryImpl> clazz = ProxyFactoryFactoryImpl.class;
            Field field = clazz.getDeclaredField("byteBuddyProxyHelper");
            field.setAccessible(true);
            ByteBuddyProxyHelper byteBuddyProxyHelper = (ByteBuddyProxyHelper) field.get(this);
            return new KuKuByteBuddyProxyFactory(byteBuddyProxyHelper);
        } catch (Exception e) {
            return null;
        }
    }
}
