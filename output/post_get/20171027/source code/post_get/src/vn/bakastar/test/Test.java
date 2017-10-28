package vn.bakastar.test;

import java.text.ParseException;
import java.util.Date;

import vn.bakastar.db.DAOUtil;

public class Test {

	public static void main(String[] args) throws ParseException {
		System.out.println("Date: " + DAOUtil.convertTimezone(new Date()));
	}
}
