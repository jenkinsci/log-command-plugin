package org.jenkinsci.plugins.commands;

import org.kohsuke.args4j.Argument;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;

import hudson.Extension;
import hudson.cli.CLICommand;
import hudson.model.AbstractProject;

@Extension
public class ShowLogCommand extends CLICommand {
		
	private final int NOBUILD_ERRORCODE = 1;
	
	private CmdLineParser cmdLineParser;
	
	public ShowLogCommand() {
		this.cmdLineParser = new CmdLineParser(this);
	}
	
	@Argument(required=true, usage="Name of the job to get the log", metaVar="JOB")
	private AbstractProject<?,?> job;
	
	@Option(name="-nLines", aliases={"-n"}, usage="Max. number of log lines to show (default=10)")
	private int maxLines = 10;
	
	@Option(name="-bNumber", aliases={"-b"}, usage="Number of the build to get the log (default=last build)")
	private int buildNumber=-1;
	
	
	@Override
	public String getShortDescription() {
		return Messages.ShowLogCommand(); 
	}

	protected int getBuildNumber() {
		return this.buildNumber;
	}
	
	protected int getMaxLines() {
		return this.maxLines;
	}

	protected AbstractProject<?, ?> getJob() {
		return job;
	}
	
	@Override
	protected int run() throws Exception {
		try{
			stdout.println(SLC.showLog(job, buildNumber, maxLines));
		}
		catch(WrongBuildNumberException e){
			stderr.println(e.getMessage());			
			this.printUsage(stderr, cmdLineParser);
			return NOBUILD_ERRORCODE;
		}
		return 0;
	}	
}
