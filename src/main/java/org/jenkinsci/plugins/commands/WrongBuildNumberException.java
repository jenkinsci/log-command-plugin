package org.jenkinsci.plugins.commands;

public class WrongBuildNumberException extends Exception {

	private String ERROR_MESSAGE = "No such build number for the selected job."; 
	
	@Override
	public String getMessage() {
		return ERROR_MESSAGE;
	}
}
