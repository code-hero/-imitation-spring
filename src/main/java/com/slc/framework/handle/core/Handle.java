package com.slc.framework.handle.core;

import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

public class Handle {

    public boolean doBeforeHandle(Object obj, Method method, Object[] args, MethodProxy proxy) {
        if (HandleFactory.handleConfigs.size() == 0) {
            return true;
        }
        boolean beforeFlag = true;
        HandleConfig temp = HandleFactory.firstHandleConfig.clone();
        while (beforeFlag) {
            beforeFlag = beforeFlag && temp.getHandle().beforeHandle(obj, method, args, proxy);
            temp = temp.getNext();
            if (temp != null) {
                if (temp.getNext() == null) {
                    beforeFlag = beforeFlag && temp.getHandle().beforeHandle(obj, method, args, proxy);
                    break;
                }
            } else {
                break;
            }
        }
        return beforeFlag;
    }

    public Object doAfterHandle(Object obj, Method method, Object[] args, MethodProxy proxy, Object result) {
        if (HandleFactory.handleConfigs.size() == 0) {
            return result;
        }
        HandleConfig temp = HandleFactory.lastHandleConfig.clone();
        while (true) {
            result = temp.getHandle().afterHandle(obj, method, args, proxy, result);
            temp = temp.getBefore();
            if (temp != null) {
                if (temp.getBefore() == null) {
                    result = temp.getHandle().afterHandle(obj, method, args, proxy, result);
                    break;
                }
            } else {
                break;
            }
        }
        return result;
    }

    public boolean beforeHandle(Object obj, Method method, Object[] args, MethodProxy proxy) {
        return true;
    }

    public Object afterHandle(Object obj, Method method, Object[] args, MethodProxy proxy, Object result) {
        return result;
    }
}
