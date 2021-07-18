package me.dablakbandit.bank.save.loader;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import me.dablakbandit.bank.config.BankPluginConfiguration;
import me.dablakbandit.bank.log.BankLog;

public class LoaderThread implements Runnable{
	
	private final AtomicBoolean		stop	= new AtomicBoolean(false);
	private volatile boolean	log		= false;
	
	public void terminate(){
		stop.set(true);
	}
	
	public void log(){
		log = true;
	}
	
	private final List<Runnable> runners = Collections.synchronizedList(new ArrayList<>());
	
	public boolean finished(){
		return stop.get() && runners.size() == 0;
	}
	
	public void add(Runnable run){
		runners.add(run);
	}
	
	public void run(){
		while(!stop.get()){
			runRunners();
			try{
				Thread.sleep(10);
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		BankLog.info(BankPluginConfiguration.BANK_LOG_PLUGIN_LEVEL, "Loader thread stopped");
	}
	
	public synchronized void runRunners(){
		while(runners.size() != 0){
			Runnable run = null;
			try{
				run = runners.get(0);
				run.run();
			}catch(Exception e){
				e.printStackTrace();
			}finally{
				runners.remove(run);
			}
		}
	}
	
	public boolean getStop(){
		return stop.get();
	}
}
