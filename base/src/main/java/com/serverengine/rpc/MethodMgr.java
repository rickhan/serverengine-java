package com.serverengine.rpc;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.serverengine.log.Log;

/**
 * 反射函数管理
 * 
 * @author rick <rick.han@yahoo.com>
 * 
 */
public class MethodMgr {

	class ObjectMethod {
		public Object inst;
		public Class<?> returnType;
		public Class<?>[] argTypes;
		public Method method;
	}

	/**
	 * 函数列表
	 */
	private HashMap<String, ObjectMethod> allMethods = new HashMap<String, ObjectMethod>();

	/**
	 * 下一个回调id
	 */
	private AtomicLong nextCallbackId = new AtomicLong(0);

	/**
	 * 回调列表
	 */
	private ConcurrentHashMap<Long, CallbackObj> callbackObjs = new ConcurrentHashMap<Long, CallbackObj>();

	/**
	 * 提取出公开的，并且无法返回值的函数
	 * 
	 * @param inst
	 */
	public void extractPublicVoidMethod(Object inst) {
		for (Method m : inst.getClass().getDeclaredMethods()) {
			// 加快访问速度
			m.setAccessible(true);
			Class<?> retType = m.getReturnType();
			if (retType != void.class && retType != Void.class) {
				continue;
			}

			if (allMethods.containsKey(m.getName())) {
				Log.error("发现同名函数，暂时不支持函数重载！methodName=" + m.getName());
				continue;
			}

			ObjectMethod om = new ObjectMethod();
			om.argTypes = m.getParameterTypes();
			om.inst = inst;
			om.returnType = retType;
			om.method = m;
			allMethods.put(m.getName(), om);
		}
	}
	
	/**
	 * 提取出带特定注解的函数
	 * 
	 * @param inst
	 * @param annotation
	 */
	public void extractMethodsWithAnnotation(Object inst, Class<? extends Annotation> annotation)
	{
		for (Method m : inst.getClass().getDeclaredMethods()) {
			// 加快访问速度
			m.setAccessible(true);
			if (m.getAnnotation(annotation) == null)
			{
				continue;
			}
			
			Class<?> retType = m.getReturnType();
			if (retType != void.class && retType != Void.class) {
				Log.error("暂时不支持带返回值的函数！methodName=" + m.getName());
				continue;
			}
			
			if (allMethods.containsKey(m.getName())) {
				Log.error("发现同名函数，暂时不支持函数重载！methodName=" + m.getName());
				continue;
			}
			
			ObjectMethod om = new ObjectMethod();
			om.argTypes = m.getParameterTypes();
			om.inst = inst;
			om.returnType = m.getReturnType();
			om.method = m;
			allMethods.put(m.getName(), om);
		}
	}

	/**
	 * 消息处理
	 * 
	 * @param channel
	 * @param msg
	 * @return
	 */
	public boolean handleMessage(RpcChannel channel, Message msg) {
		Kryo kryo = ThreadSafeKryo.getKryo();
		Input input = new Input(msg.getMessageData());
		String methodName = kryo.readObjectOrNull(input, String.class);

		if (methodName.equals("__rpccallback")) {
			long callbackId = kryo.readObjectOrNull(input, long.class); // 第一段字必须是回调id
			CallbackObj callback = callbackObjs.remove(callbackId);
			if (callback == null) {
				Log.error("函数回调前，已经被删除了！,callbackId= " + callbackId);
				return true;
			}

			Method callbackMethod = null;
			for (Method m : callback.getClass().getDeclaredMethods()) {
				m.setAccessible(true);
				if (m.getName().equals("onCallback")) {
					callbackMethod = m;
					break;
				}
			}

			if (callbackMethod == null) {
				Log.error("没有回调函数！callbackObj=" + callback);
				return true;
			}

			Object[] args = extractArgs(channel, kryo, callbackMethod.getParameterTypes(), input);
			try {
				callbackMethod.invoke(callback, args);
			} catch (Exception e) {
				e.printStackTrace();
				return false;
			}
			return true;
		}

		ObjectMethod m = allMethods.get(methodName);
		if (m == null) {
			return false;
		}

		Object[] args = extractArgs(channel, kryo, m.argTypes, input);
		try {
			m.method.invoke(m.inst, args);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	/**
	 * 处理事件
	 * 
	 * @param methodName
	 * @param args
	 */
	public void handleEvent(String methodName, Object... args) {
		ObjectMethod om = allMethods.get(methodName);
		if (om == null) {
			return;
		}

		try {
			om.method.invoke(om.inst, args);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 添加回调
	 * 
	 * @param obj
	 * @return
	 */
	public long addCallbackObj(CallbackObj obj) {
		long callbackId = nextCallbackId.incrementAndGet();
		obj.setCallbackId(callbackId);
		callbackObjs.put(callbackId, obj);
		return callbackId;
	}
	
	/**
	 * 销毁
	 */
	public void release()
	{
		callbackObjs.clear();
		allMethods.clear();
	}

	/**
	 * 提取函数参数
	 * 
	 * @param channel
	 * @param kryo
	 * @param argTypes
	 * @param input
	 * @return
	 */
	private Object[] extractArgs(RpcChannel channel, Kryo kryo, Class<?>[] argTypes, Input input) {
		Object[] args = null;
		if (argTypes != null && argTypes.length > 0) {
			args = new Object[argTypes.length];
			for (int i = 0; i < argTypes.length; ++i) {
				Class<?> clazz = argTypes[i];
				if (clazz == RpcChannel.class) {
					args[i] = channel;
					continue;
				}

				if (clazz == CallbackObj.class) {
					CallbackObj obj = new CallbackObj();
					obj.setChannel(channel.getSocketChannel());
					obj.setCallbackId(kryo.readObjectOrNull(input, long.class));
					obj.setMethodMgr(this);
					args[i] = obj;
					continue;
				}

				args[i] = kryo.readObjectOrNull(input, clazz);
			}
		}

		return args;
	}
}
