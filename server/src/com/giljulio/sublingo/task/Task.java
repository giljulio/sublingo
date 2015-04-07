package com.giljulio.sublingo.task;

import com.giljulio.sublingo.Engine;

/**
 * A task is a class which carries out a unit of work.
 * @author Graham Edgecombe
 *
 */
public interface Task {
	
	/**
	 * Executes the task. The general contract of the execute method is that it
	 * may take any action whatsoever.
	 * @param context The game engine this task is being executed in.
	 */
	public void execute(Engine context);

}
