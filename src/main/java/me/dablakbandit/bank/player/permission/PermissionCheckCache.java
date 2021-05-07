package me.dablakbandit.bank.player.permission;

import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.TimeUnit;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

public class PermissionCheckCache<T>{
	
	public PermissionCheckCache(int expireInSeconds){
		init(expireInSeconds);
	}
	
	private LoadingCache<Long, T> cache = null;
	
	private void init(int expireInseconds){
		cache = CacheBuilder.newBuilder().expireAfterWrite(expireInseconds, TimeUnit.SECONDS).build(new CacheLoader<Long, T>(){
			public T load(Long key){
				return getUnchecked(key);
			}
		});
	}
	
	private T getUnchecked(Long key){
		return cache.getUnchecked(key);
	}
	
	public void add(Long key, T o){
		cache.put(key, o);
	}
	
	public void delete(String key){
		cache.invalidate(key);
	}
	
	public long getSize(){
		return cache.size();
	}
	
	public void cleanup(){
		cache.cleanUp();
	}
	
	public void clear(){
		cache.invalidateAll();
	}
	
	public ConcurrentMap<Long, T> getAsMap(){
		return cache.asMap();
	}
	
	public LoadingCache<Long, T> getCache(){
		return cache;
	}
	
}
