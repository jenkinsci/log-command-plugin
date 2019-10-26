package org.jenkinsci.plugins.commands;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException ;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;

import org.kohsuke.args4j.Argument;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;
import org.kohsuke.args4j.spi.Setter;

import com.sun.jna.StringArray;

import hudson.Extension;
import hudson.model.AbstractBuild;
import hudson.model.AbstractProject;
import hudson.plugins.im.IMChat;
import hudson.plugins.im.IMException;
import hudson.plugins.im.IMMessage;
import hudson.plugins.im.Sender;
import hudson.plugins.im.bot.AbstractTextSendingCommand;
import hudson.plugins.im.bot.Bot;
import hudson.plugins.im.bot.BotCommand;

@Extension(optional=true)
public class ShowLogIMCommand extends AbstractTextSendingCommand {

	private CmdLineParser parser;
	@Override
	protected String getReply(Bot bot, Sender sender, String[] args) {
		ShowLogCommand sCommand = new ShowLogCommand();
		parser = new CmdLineParser(sCommand);
						
		String[] argsToParser = args.length > 1 ? Arrays.copyOfRange(args, 1, args.length) : new String[0]; 
				
		try {
			parser.parseArgument(argsToParser);
		} catch (CmdLineException e) {			
			return sender.toString() + ": " + e.getMessage() + "\n"
					+ getUsageString();
		}

		try {
			return SLC.showLog(
					sCommand.getJob(), 
					sCommand.getBuildNumber(), 
					sCommand.getMaxLines());
		} catch (WrongBuildNumberException e) {
			return sender.toString() + ": " + e.getMessage() + "\n"
					+ getUsageString();
		}						
	}
	
	private String getUsageString () {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		parser.printSingleLineUsage(baos);
    try{
		  return "Usage: " + getUsageCommandName() + " " + baos.toString("UTF-8");
    } catch(UnsupportedEncodingException e) {
		  return "Usage: " + getUsageCommandName() + " " + "error printing usage line";
    }
	}

	private String getUsageCommandName() {
		Collection<String> cNames = getCommandNames();
		if(cNames.size() == 1)
			return cNames.iterator().next();
		
		StringBuffer uCn = new StringBuffer();
    uCn.append("{");
		
		//Iterator<String> iterator = cNames.iterator();
		for (String name : cNames) {
			uCn.append(name + " | "); 
		}
    String uCnRet = uCn.toString();
		return uCnRet.substring(0, uCnRet.lastIndexOf('|')) + "}";
		
	}
	
	@Override
	public Collection<String> getCommandNames() {
		return Arrays.asList("show-log", "sl");
	}

	
	
	@Override
	public String getHelp() {
		return " <job> [-bNumber (-b) <N>] [-nLines (-n) <N>] - " + Messages.showLogCommand();
	}
}
