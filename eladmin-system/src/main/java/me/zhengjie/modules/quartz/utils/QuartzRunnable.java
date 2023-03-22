package me.zhengjie.modules.quartz.utils;

import kotlin.text.StringsKt;
import lombok.extern.slf4j.Slf4j;
import me.zhengjie.utils.SpringContextHolder;
import org.springframework.util.ReflectionUtils;
import java.lang.reflect.Method;
import java.util.concurrent.Callable;

@Slf4j
public class QuartzRunnable implements Callable<Object> {

	private final Object target;
	private final Method method;
	private final String params;

	QuartzRunnable(String beanName, String methodName, String params)
			throws NoSuchMethodException, SecurityException {
		this.target = SpringContextHolder.getBean(beanName);
		this.params = params;
		if (!StringsKt.isBlank(params)) {
			this.method = target.getClass().getDeclaredMethod(methodName, String.class);
		} else {
			this.method = target.getClass().getDeclaredMethod(methodName);
		}
	}

	@Override
	@SuppressWarnings("all")
	public Object call() throws Exception {
		ReflectionUtils.makeAccessible(method);
		if (!StringsKt.isBlank(params)) {
			method.invoke(target, params);
		} else {
			method.invoke(target);
		}
		return null;
	}
}
