package com.serverengine.rpc;

import com.esotericsoftware.kryo.Kryo;

public class ThreadSafeKryo {
	private static final ThreadLocal<Kryo> kryos = new ThreadLocal<Kryo>() {
		protected Kryo initialValue() {
			Kryo kryo = new Kryo();
			return kryo;
		}
	};
	
	public static Kryo getKryo()
	{
		return kryos.get();
	}
}
