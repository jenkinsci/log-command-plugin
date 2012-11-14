package org.jenkinsci.plugins.commands;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
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
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			parser.printSingleLineUsage(baos);
			return getUsageCommandName() + " " + baos.toString();
		}

		return SLC.showLog(
				sCommand.getJob(), 
				sCommand.getBuildNumber(), 
				sCommand.getMaxLines());						
	}

	private String getUsageCommandName() {
		Collection<String> cNames = getCommandNames();
		if(cNames.size() == 1)
			return cNames.iterator().next();
		
		String uCn = "{";
		
		Iterator<String> iterator = cNames.iterator();
		for (String name : cNames) {
			uCn += name + " | "; 
		}
		uCn = uCn.substring(0, uCn.lastIndexOf('|')) + "}";
		return uCn;
		
	}
	
	@Override
	public Collection<String> getCommandNames() {
		return Arrays.asList("show-log", "sl");
	}

	
	
	@Override
	public String getHelp() {
		return " - " + Messages.ShowLogCommand();
	}
}
