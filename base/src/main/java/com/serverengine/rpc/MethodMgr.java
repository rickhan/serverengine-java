package com.serverengine.rpc;

import java.lang.reflect.Method;
import java.util.HashMap;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;

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

	private HashMap<String, ObjectMethod> allMethods = new HashMap<String, ObjectMethod>();

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
		ObjectMethod m = allMethods.get(methodName);
		if (m == null) {
			return false;
		}

		Object[] args = null;
		if (m.argTypes != null && m.argTypes.length > 0) {
			args = new Object[m.argTypes.length];
			for (int i = 0; i < m.argTypes.length; ++i) {
				Class<?> typ = m.argTypes[i];
				if (typ == RpcChannel.class) {
					args[i] = channel;
					continue;
				}

				if (typ == CallbackObj.class) {
					CallbackObj obj = new CallbackObj();
					obj.setChannel(channel.getSocketChannel());
					obj.setCallbackId(kryo.readObjectOrNull(input, long.class));
					args[i] = obj;
					continue;
				}

				args[i] = kryo.readObjectOrNull(input, typ);
			}
		}

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
}
