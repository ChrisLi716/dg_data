package com.chris.dg_data.common;

import com.chris.dg_data.beans.SettlementRecords;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import java.io.*;
import java.text.SimpleDateFormat;
import java.time.Month;
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

	public static void generateCsvFile(String path, String fileName, List<String> header,
		List<SettlementRecords> settlementRecords) {
		CSVPrinter csvPrinter = null;
		try {
			FileOutputStream fos = new FileOutputStream(path + File.separator + fileName);
			logger.info("generateCsvFile, writing csv file : " + path + File.separator + fileName);

			OutputStreamWriter osw = new OutputStreamWriter(fos, "GBK");

			CSVFormat csvFormat = CSVFormat.DEFAULT.withHeader(header.toArray(new String[0]));
			csvPrinter = new CSVPrinter(osw, csvFormat);

			for (SettlementRecords sr : settlementRecords) {
				csvPrinter.printRecord(sr.getOrder_sn(),
					sr.getOrder_date(),
					sr.getSettlement_type_name(),
					sr.getOrder_amount(),
					sr.getSettlement_discount(),
					sr.getSettlementer_name(),
					sr.getSettlementer_amount(),
					sr.getSettlement_date(),
					sr.getAssignee(),
					sr.getAssigneeAddress());
			}

		}
		catch (IOException e) {
			e.printStackTrace();
		}
		finally {
			if (null != csvPrinter) {
				try {
					csvPrinter.flush();
					csvPrinter.close();
				}
				catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
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
