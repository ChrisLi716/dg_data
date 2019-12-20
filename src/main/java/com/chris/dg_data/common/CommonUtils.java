package com.chris.dg_data.common;

import com.chris.dg_data.beans.SettlementRecords;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

public class CommonUtils {

	private static Logger logger = Logger.getLogger(CommonUtils.class);

	public static boolean generateFolder(String path) {
		boolean success = false;
		if (StringUtils.isNotEmpty(path)) {
			File file = new File(path);
			if (!file.exists()) {
				success = file.mkdirs();
			}
		}
		if (!success) {
			logger.error("mkdir failed, " + path);
		}
		return success;
	}

	public static int getCurrentYear() {
		Calendar calendar = Calendar.getInstance();
		return calendar.get(Calendar.YEAR);
	}

	public static String getFirstDateOfMonth(String year, String month) {
		SimpleDateFormat sdf = new SimpleDateFormat(DateFormatter.YYYY_MM_DD);
		Calendar c = Calendar.getInstance();
		c.set(Integer.parseInt(year), Integer.parseInt(month) - 1, 1);
		return sdf.format(c.getTime());
	}

	public static String getLastDateOfMonth(String year, String month) {
		SimpleDateFormat sdf = new SimpleDateFormat(DateFormatter.YYYY_MM_DD);
		Calendar c = Calendar.getInstance();
		c.set(Integer.parseInt(year), Integer.parseInt(month), 0);
		return sdf.format(c.getTime());
	}

	public static void main(String[] args) {
		System.out.println(1 / 30);
	}
}
