package com.chris.dg_data.common;

import com.chris.dg_data.beans.SettlementRecords;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.List;

public class CsvExporter {

	private static Logger logger = Logger.getLogger(CsvExporter.class);

	public static void generateCsvFile(String path, String fileName, List<String> header, List<SettlementRecords> settlementRecords) {
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
					sr.getAssigneeAddress(),
					sr.getAssignee());
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
