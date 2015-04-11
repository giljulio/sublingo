package com.giljulio.sublingo.split.task;

import com.giljulio.sublingo.split.Engine;

/**
 * A technique is a class which carries out a unit of work.
 * @author Graham Edgecombe
 *
 */
public interface Task {
	
	/**
	 * Executes the technique. The general contract of the execute method is that it
	 * may take any action whatsoever.
	 * @param context The game engine this technique is being executed in.
	 */
	public void execute(Engine context);

}
