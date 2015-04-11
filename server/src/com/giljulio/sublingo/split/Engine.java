package com.giljulio.sublingo.split;

import com.giljulio.sublingo.split.task.Task;

import java.util.concurrent.*;

/**
 * The 'core' class of the server which processes all the logic tasks in one
 * single logic <code>ExecutorService</code>. This service is scheduled which
 * means <code>Event</code>s are also submitted to it.
 * @author Graham Edgecombe
 *
 */
public class Engine implements Runnable {

    /**
     * The technique service, used by <code>ParallelTask</code>s.
     */
    private final BlockingExecutorService taskService = new BlockingExecutorService(Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors()));

	/**
	 * A queue of pending tasks.
	 */
	private final BlockingQueue<Task> tasks = new LinkedBlockingQueue<Task>();

	/**
	 * The work service, generally for file I/O and other blocking operations.
	 */
    private final ExecutorService workService = Executors.newSingleThreadExecutor();
	
	/**
	 * Running flag.
	 */
	private boolean running = false;
	
	/**
	 * Thread instance.
	 */
	private Thread thread;
	
	/**
	 * Submits a new technique which is processed on the logic thread as soon as
	 * possible.
	 * @param task The technique to submit.
	 */
	public void pushTask(Task task) {
		tasks.offer(task);
	}

	/**
	 * Checks if this <code>GameEngine</code> is running.
	 * @return <code>true</code> if so, <code>false</code> if not.
	 */
	public boolean isRunning() {
		return running;
	}
	
	/**
	 * Starts the <code>GameEngine</code>'s thread.
	 */
	public void start() {
		if(running) {
			throw new IllegalStateException("The engine is already running.");
		}
		running = true;
		thread = new Thread(this);
		thread.start();
	}
	
	/**
	 * Stops the <code>GameEngine</code>'s thread.
	 */
	public void stop() {
		if(!running) {
			throw new IllegalStateException("The engine is already stopped.");
		}
		running = false;
		thread.interrupt();
	}
	
	@Override
	public void run() {
		try {
			while(running) {
				try {
					final Task task = tasks.take();
					submitWork(new Runnable() {
						@Override
						public void run() {
							task.execute(Engine.this);
						}
					});
				} catch(InterruptedException e) {
					continue;
				}
			}
		} finally {
			workService.shutdown();
		}
	}


    /**
     * Submits a technique to run in the parallel technique service.
     * @param runnable The runnable.
     */
    public void submitTask(final Runnable runnable) {
        taskService.submit(new Runnable() {
            public void run() {
                try {
                    runnable.run();
                } catch(Throwable t) {
                    System.out.println("ERROR: " + t);
                }
            }
        });
    }

    /**
     * Submits a technique to run in the work service.
     * @param runnable The runnable.
     */
	public void submitWork(final Runnable runnable) {
		workService.submit(new Runnable() {
			public void run() {
				try {
					runnable.run();
				} catch(Throwable t) {
					System.out.println("ERROR: " + t);
				}
			}
		});
	}

    /**
     * Waits for pending parallel tasks.
     * @throws java.util.concurrent.ExecutionException If an error occurred during a technique.
     */
    public void waitForPendingParallelTasks() throws ExecutionException {
        taskService.waitForPendingTasks();
    }


}
