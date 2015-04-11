package com.giljulio.sublingo.split.task;

import com.giljulio.sublingo.split.Engine;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * A technique which executes a group of tasks in a guaranteed sequence.
 * @author Graham Edgecombe
 *
 */
public class ConsecutiveTask implements Task {

	/**
	 * The tasks.
	 */
	private Collection<Task> tasks;
	
	/**
	 * Creates the consecutive technique.
	 * @param tasks The child tasks to execute.
	 */
	public ConsecutiveTask(Task... tasks) {
		List<Task> taskList = new ArrayList<Task>();
		for(Task task : tasks) {
			taskList.add(task);
		}
		this.tasks = Collections.unmodifiableCollection(taskList);
	}
	
	@Override
	public void execute(Engine context) {
		for(Task task : tasks) {
			task.execute(context);
		}
	}

}
