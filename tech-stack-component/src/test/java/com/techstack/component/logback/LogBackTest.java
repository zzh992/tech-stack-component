package com.techstack.component.logback;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.impl.StaticLoggerBinder;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.joran.JoranConfigurator;
import ch.qos.logback.core.joran.spi.JoranException;

public class LogBackTest {

	private static Logger log = LoggerFactory.getLogger(LogBackTest.class);

	static {
		//Thread.currentThread().getContextClassLoader().getResource()
		//InputStream input = ClassLoader.getSystemResourceAsStream("logback/logback.xml");
		//File logFile = new File(input);  
		LoggerContext loggerContext = (LoggerContext) StaticLoggerBinder
				.getSingleton().getLoggerFactory();
		loggerContext.reset();
		JoranConfigurator configurator = new JoranConfigurator();
		configurator.setContext(loggerContext);
		try {
			configurator.doConfigure(ClassLoader.getSystemResourceAsStream("logback/logback.xml"));
		} catch (JoranException e) {
			e.printStackTrace();
		}
		//System.out.println("INFO: use log config " + logFile.getAbsolutePath());
	}

	public static void main(String[] args) {
		log.trace("======trace");
		log.debug("======debug");
		log.info("======info");
		log.warn("======warn");
		log.error("======error");
		Long id = null;
		String name = null;
		Integer isLeaf = new Integer(2);
		Integer temp = new Integer(2);
		System.out.println("11"+ id + name);
		if(isLeaf != temp){
			System.out.println("11"+ isLeaf);
		}
	}

}
