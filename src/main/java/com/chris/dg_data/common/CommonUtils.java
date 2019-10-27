package com.chris.dg_data.common;

import com.chris.dg_data.beans.SettlementRecords;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.lang3.StringUtils;

import java.io.*;
import java.util.List;

public class CommonUtils {

	public static boolean generateFolder(String path) {
		if (StringUtils.isNotEmpty(path)) {
			File file = new File(path);
			if (!file.exists()) {
				return file.mkdirs();
			}
		}
		return false;
	}

	public static void generateCsvFile(String path, String fileName, List<String> header, List<SettlementRecords> settlementRecords) {
		CSVPrinter csvPrinter = null;
		try {
			FileOutputStream fos = new FileOutputStream(path + fileName);
			OutputStreamWriter osw = new OutputStreamWriter(fos, "GBK");

			CSVFormat csvFormat = CSVFormat.DEFAULT.withHeader((String[])header.toArray());
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

}
