package org.jenkinsci.plugins.commands;

import java.io.IOException;

import hudson.model.AbstractBuild;
import hudson.model.AbstractProject;

public class SLC {
	
	public static String showLog(AbstractProject<?, ?> job, int buildNumber, int maxLines) {		
		AbstractBuild<?, ?> build = buildNumber > 0 ? job.getBuildByNumber(buildNumber) : job.getLastBuild();
		
		String toRet = "";
		if(null != build)
		{		
			try{				
				for (String logLine : build.getLog(maxLines + 1)) {
					toRet += logLine + "\n";
				}				
			}
			catch(IOException e){
				e.printStackTrace();
			}
		}
		return toRet;
	}
}
