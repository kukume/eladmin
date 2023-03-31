package me.zhengjie.config.hibernate;

import org.hibernate.bytecode.internal.bytebuddy.ByteBuddyState;
import org.hibernate.bytecode.internal.bytebuddy.BytecodeProviderImpl;
import org.hibernate.bytecode.spi.ProxyFactoryFactory;
import org.hibernate.proxy.pojo.bytebuddy.ByteBuddyProxyHelper;

import java.lang.reflect.Field;

public class KuKuBytecodeProvider extends BytecodeProviderImpl {

    @Override
    public ProxyFactoryFactory getProxyFactoryFactory() {
        try {
            Class<BytecodeProviderImpl> clazz = BytecodeProviderImpl.class;
            Field stateField = clazz.getDeclaredField("byteBuddyState");
            stateField.setAccessible(true);
            ByteBuddyState byteBuddyState = (ByteBuddyState) stateField.get(this);
            Field helperField = clazz.getDeclaredField("byteBuddyProxyHelper");
            helperField.setAccessible(true);
            ByteBuddyProxyHelper byteBuddyProxyHelper = (ByteBuddyProxyHelper) helperField.get(this);
            return new KuKuProxyFactoryFactory(byteBuddyState, byteBuddyProxyHelper);
        } catch (Exception e) {
            return null;
        }
    }
}
