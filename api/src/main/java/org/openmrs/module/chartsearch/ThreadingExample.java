package org.openmrs.module.chartsearch;

public class ThreadingExample {
	public static String var = "STARTED!!!!!!!!!!!!!!!!!!";
	public static void main(String args[]) {
		System.out.println(var);
		ThreadDemo T1 = new ThreadDemo("Thread-1");
		T1.start();
		
		ThreadDemo T2 = new ThreadDemo("Thread-2");
		T2.start();
	}
}

class ThreadDemo extends Thread {
	
	private Thread t;
	
	private String threadName;
	
	ThreadDemo(String name) {
		threadName = name;
		System.out.println("Creating " + threadName);
	}
	
	public void run() {ThreadingExample.var += this.threadName;
		System.out.println("Running " + threadName);
		try {
			for (int i = 4; i > 0; i--) {
				System.out.println("Thread: " + threadName + ", " + i);
				// Let the thread sleep for a while.
				Thread.sleep(50);
			}
		}
		catch (InterruptedException e) {
			System.out.println("Thread " + threadName + " interrupted.");
		}
		System.out.println("Thread " + threadName + " exiting.");
	}
	
	public void start() {ThreadingExample.var += this.threadName;
		System.out.println("Starting " + threadName);
		if (t == null) {
			t = new Thread(this, threadName);
			t.start();
		}
		System.out.println(ThreadingExample.var);
	}
	
}
