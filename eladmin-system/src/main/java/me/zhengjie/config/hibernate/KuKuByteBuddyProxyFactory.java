package me.zhengjie.config.hibernate;

import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.proxy.HibernateProxy;
import org.hibernate.proxy.ProxyConfiguration;
import org.hibernate.proxy.pojo.bytebuddy.ByteBuddyInterceptor;
import org.hibernate.proxy.pojo.bytebuddy.ByteBuddyProxyFactory;
import org.hibernate.proxy.pojo.bytebuddy.ByteBuddyProxyHelper;
import org.hibernate.type.CompositeType;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class KuKuByteBuddyProxyFactory extends ByteBuddyProxyFactory {

    public KuKuByteBuddyProxyFactory(ByteBuddyProxyHelper byteBuddyProxyHelper) {
        super(byteBuddyProxyHelper);
    }

    @Override
    public HibernateProxy getProxy(Object id, SharedSessionContractImplementor session) throws HibernateException {
        try {
            Class<ByteBuddyProxyFactory> clazz = ByteBuddyProxyFactory.class;
            Field entityNameField = clazz.getDeclaredField("entityName");
            entityNameField.setAccessible(true);
            String entityName = (String) entityNameField.get(this);

            Field persistentClassField = clazz.getDeclaredField("persistentClass");
            persistentClassField.setAccessible(true);
            Class persistentClass = (Class) persistentClassField.get(this);

            Field interfacesField = clazz.getDeclaredField("interfaces");
            interfacesField.setAccessible(true);
            Class[]  interfaces = (Class[]) interfacesField.get(this);

            Field getIdentifierMethodField = clazz.getDeclaredField("getIdentifierMethod");
            getIdentifierMethodField.setAccessible(true);
            Method getIdentifierMethod = (Method) getIdentifierMethodField.get(this);

            Field setIdentifierMethodField = clazz.getDeclaredField("setIdentifierMethod");
            setIdentifierMethodField.setAccessible(true);
            Method setIdentifierMethod = (Method) setIdentifierMethodField.get(this);

            Field componentIdTypeField = clazz.getDeclaredField("componentIdType");
            componentIdTypeField.setAccessible(true);
            CompositeType componentIdType = (CompositeType) componentIdTypeField.get(this);

            Field overridesEqualsField = clazz.getDeclaredField("overridesEquals");
            overridesEqualsField.setAccessible(true);
            boolean overridesEquals = (boolean) overridesEqualsField.get(this);

            final ByteBuddyInterceptor interceptor = new KuKuByteBuddyInterceptor(
                    entityName,
                    persistentClass,
                    interfaces,
                    id,
                    getIdentifierMethod,
                    setIdentifierMethod,
                    componentIdType,
                    session,
                    overridesEquals
            );
            Method proxyMethod = clazz.getDeclaredMethod("getHibernateProxy");
            final HibernateProxy instance = (HibernateProxy) proxyMethod.invoke(this);
            final ProxyConfiguration proxyConfiguration = instance.asProxyConfiguration();
            if ( proxyConfiguration == null ) {
                throw new HibernateException( "Produced proxy does not correctly implement ProxyConfiguration" );
            }
            proxyConfiguration.$$_hibernate_set_interceptor( interceptor );
            return instance;
        } catch (Exception e) {
            return null;
        }
    }
}
