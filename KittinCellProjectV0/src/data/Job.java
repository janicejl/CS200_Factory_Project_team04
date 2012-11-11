package data;

import java.io.*;

public class Job implements Serializable{

	KitInfo kit;
	Integer amount;
	
	public Job(KitInfo k, Integer i){
		kit = new KitInfo(k);
		amount = new Integer(i);
	}

	public synchronized KitInfo getKit() {
		return kit;
	}

	public synchronized void setKit(KitInfo kit) {
		this.kit = kit;
	}

	public synchronized Integer getAmount() {
		return amount;
	}

	public synchronized void setAmount(Integer amount) {
		this.amount = amount;
	}
}
