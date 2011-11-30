/**
 * 
 */
package org.jhlabs.scany.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Gulendol
 *
 * <p>Created: 2011. 11. 30. 오전 10:12:02</p>
 *
 */
public class Timer {
	
	private static final Logger logger = LoggerFactory.getLogger(Timer.class);
	
	private long startTime = 0L;

	private long endTime = 0L;
	
	private String startMessage;

	private String stopMessage;

	public void start(String message) {
		startMessage = message;
		start();
	}

	public void start() {
		if(startMessage != null) {
			logger.info(startMessage);
		}

		this.startTime = System.currentTimeMillis();
	}
	
	public void stop(String message) {
		stopMessage = message;
		stop();
	}

	public void stop() {
		this.endTime = System.currentTimeMillis();
		
		if(stopMessage != null) {
			logger.info("{} : {} seconds.", stopMessage, getInterval());
		}
	}
	
	public long getStartTime() {
		return this.startTime;
	}

	public long getEndTime() {
		return this.endTime;
	}

	public double getInterval() {
		return (getIntervalL() / 1000.0D);
	}

	public String getStartMessage() {
		return startMessage;
	}

	public void setStartMessage(String startMessage) {
		this.startMessage = startMessage;
	}

	public String getStopMessage() {
		return stopMessage;
	}

	public void setStopMessage(String stopMessage) {
		this.stopMessage = stopMessage;
	}

	public long getIntervalL() {
		if(this.startTime < this.endTime)
			return (this.endTime - this.startTime);
		return 0L;
	}
}