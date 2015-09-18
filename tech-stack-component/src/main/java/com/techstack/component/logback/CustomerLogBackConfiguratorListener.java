package com.techstack.component.logback;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.slf4j.impl.StaticLoggerBinder;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.joran.JoranConfigurator;
import ch.qos.logback.core.joran.spi.JoranException;

public class CustomerLogBackConfiguratorListener implements ServletContextListener{

	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void contextInitialized(ServletContextEvent arg0) {
		LoggerContext loggerContext = (LoggerContext) StaticLoggerBinder
				.getSingleton().getLoggerFactory();
		loggerContext.reset();
		JoranConfigurator configurator = new JoranConfigurator();
		configurator.setContext(loggerContext);
		try {
			configurator.doConfigure(ClassLoader.getSystemResourceAsStream("logback/logback.xml"));
			System.out.println("INFO: use log config success");
		} catch (JoranException e) {
			e.printStackTrace();
		}
	}

}
