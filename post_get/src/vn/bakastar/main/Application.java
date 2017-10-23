package vn.bakastar.main;

import vn.bakastar.util.Logger;

public class Application {

	public static void main(String[] args) {

		if (args.length > 1 && args[0].equals("--console") 
				&& Boolean.parseBoolean(args[1])) {
			Logger.enable(true);
		}

		Thread postThread = new Thread(new PostAction());
		postThread.setName("[POST]");
		postThread.start();

		Thread getThread = new Thread(new GetAction());
		getThread.setName("[GET]");
		getThread.start();
	}
}
