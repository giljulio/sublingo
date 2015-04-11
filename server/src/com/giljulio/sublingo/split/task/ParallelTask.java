package com.giljulio.sublingo.split.task;

import com.giljulio.sublingo.split.Engine;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * A technique which can execute multiple child tasks simultaneously.
 * @author Graham Edgecombe
 *
 */
public class ParallelTask implements Task {
	
	/**
	 * The child tasks.
	 */
	private Collection<Task> tasks;
	
	/**
	 * Creates the parallel technique.
	 * @param tasks The child tasks.
	 */
	public ParallelTask(Task... tasks) {
		List<Task> taskList = new ArrayList<Task>();
		for(Task task : tasks) {
			taskList.add(task);
		}
		this.tasks = Collections.unmodifiableCollection(taskList);
	}
	
	@Override
	public void execute(final Engine context) {
		for(final Task task : tasks) {
			context.submitWork(new Runnable() {
				@Override
				public void run() {
					task.execute(context);
				}
			});
		}
		try {
			context.waitForPendingParallelTasks();
		} catch(ExecutionException e) {
			throw new RuntimeException(e);
		}
	}

}
