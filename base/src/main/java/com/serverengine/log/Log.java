package com.serverengine.log;

import java.util.Properties;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import com.serverengine.base.AppStartupInfo;

public class Log {

	// private static final Logger logger = LoggerFactory.;
	private static final Logger logger = Logger.getLogger("");

	public static void init() {
		Properties props = new Properties();

		props.setProperty("log4j.rootLogger", "ALL,DEBUG,INFO,WARN,ERROR,CONSOLE");
		props.setProperty("log4j.addivity.org.apache", "true");
		initLogProps(props, "CONSOLE");
		initLogProps(props, "DEBUG");
		initLogProps(props, "INFO");
		initLogProps(props, "WARN");
		initLogProps(props, "ERROR");
		PropertyConfigurator.configure(props);
	}

	private static void initLogProps(Properties props, String filter) {
		String key = "log4j.appender." + filter;
		if (filter.equals("CONSOLE")) {
			props.setProperty(key, "org.apache.log4j.ConsoleAppender");
			props.setProperty(key + ".Threshold", filter);
			props.setProperty(key + ".Target", "System.out");
			props.setProperty(key + ".layout", "org.apache.log4j.PatternLayout");
			props.setProperty(key + ".layout.ConversionPattern", "%-5p:%d-%c-%-2r[%t]%x%n%m %n");
		} else {
			props.setProperty(key, "org.apache.log4j.DailyRollingFileAppender");
			props.setProperty(key + ".Threshold", filter);
			props.setProperty(key + ".File",
					"log/" + AppStartupInfo.currentServerName() + "_" + filter.toLowerCase() + ".log");
			props.setProperty(key + ".DatePattern", "'.'yyyyMMdd");
			props.setProperty(key + ".Append", "true");
			props.setProperty(key + ".layout", "org.apache.log4j.PatternLayout");
			props.setProperty(key + ".layout.ConversionPattern", "%-5p:%d-%c-%-2r[%t]%x%n%m %n");
		}
	}

	public static void error(Object args) {
		logger.error(args);
	}

	public static void info(Object args) {
		logger.info(args);
	}

	public static void debug(Object args) {
		logger.debug(args);
	}

	public static void warn(Object args) {
		logger.warn(args);
	}
}
