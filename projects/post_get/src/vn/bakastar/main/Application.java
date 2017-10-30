package vn.bakastar.main;

import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;


public class Application {

	public static void main(String[] args) {

		setLog(args);

		Thread callStoreThread = new Thread(new CallStoreAction());
		callStoreThread.setName("STORE-thread");
		callStoreThread.start();

		Thread postThread = new Thread(new PostAction());
		postThread.setName("POST-thread");
		postThread.start();

		Thread getThread = new Thread(new GetAction());
		getThread.setName("GET-thread");
		getThread.start();
	}

	private static void setLog(String[] args) {

		if (args.length < 1) {
			return;
		}

		boolean isEnableConsole = false;
		Level logLevel = Level.INFO;

		for (int i=0; i<args.length; i++) {
			if ((i + 1) >= args.length) {
				break;
			}

			if (args[i].equals("--console")) {

				isEnableConsole = isEnableConsoleLog(args[i + 1]);
			}
			else if (args[i].equals("--loglevel")) {

				logLevel = getLogLevel(args[i + 1]);
			}
		}

		PatternLayout layout = new PatternLayout("%d{yyyy-MM-dd HH:mm:ss} %-5p [%t][%c{1}:%L] %m%n");

		Logger rootLogger = Logger.getRootLogger();
		rootLogger.setLevel(logLevel);

		if (isEnableConsole) {
			ConsoleAppender console = new ConsoleAppender();

			console.setLayout(layout);
			console.setTarget("System.out");
			console.activateOptions();

			rootLogger.addAppender(console);
		}
	}

	private static boolean isEnableConsoleLog(String argument) {

		return Boolean.valueOf(argument);
	}

	private static Level getLogLevel(String argument) {

		switch (argument.toUpperCase()) {
			case "ALL":
				return Level.ALL;

			case "TRACE":
				return Level.TRACE;

			case "DEBUG":
				return Level.DEBUG;

			case "INFO":
				return Level.INFO;

			case "WARN":
				return Level.WARN;

			case "ERROR":
				return Level.ERROR;

			case "FATAL":
				return Level.FATAL;

			case "OFF":
				return Level.OFF;

			default:
				return Level.INFO;
		}
	}
}
