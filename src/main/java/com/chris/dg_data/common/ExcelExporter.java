package com.chris.dg_data.common;

import com.chris.dg_data.beans.SettlementRecords;
import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.xssf.streaming.SXSSFCell;
import org.apache.poi.xssf.streaming.SXSSFRow;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ExcelExporter {

	private static Logger logger = Logger.getLogger(ExcelExporter.class);

	public static void exportExcel(String settlementerDir, String fileName, List<String> colHeader,
		List<SettlementRecords> settlementRecords) {
		logger.info(">>>>>>>>>>>>>>>>>>>>开始进入导出方法>>>>>>>>>>");
		FileOutputStream fos = null;
		SXSSFWorkbook wb = new SXSSFWorkbook(1000);// 保留1000条数据在内存中
		SXSSFSheet sheet = wb.createSheet();
		// 设置报表头样式
		CellStyle headerStyle = ExcelFormatUtil.headSytle(wb);// cell样式
		CellStyle contentStyle = ExcelFormatUtil.contentStyle(wb);// 报表体样式

		// 字段名所在表格的宽度
		List<Integer> initWidths = new ArrayList<>();
		for (int i = 0; i < colHeader.size(); i++) {
			initWidths.add(5000);
		}
		// 设置表头样式
		ExcelFormatUtil.initTitleEX(sheet, headerStyle, colHeader, initWidths);
		if (settlementRecords.size() > 0) {
			logger.info(">>>>>>>>>>>>>>>>>>>>begin to loop the list>>>>>>>>>>");
			for (int i = 0; i < settlementRecords.size(); i++) {
				SettlementRecords sr = settlementRecords.get(i);
				SXSSFRow row = sheet.createRow(i + 1);
				int j = 0;

				SXSSFCell cell = row.createCell(j++);
				cell.setCellValue(sr.getOrder_sn());
				cell.setCellStyle(contentStyle);

				cell = row.createCell(j++);
				cell.setCellValue(sr.getOrder_date());
				cell.setCellStyle(contentStyle);

				cell = row.createCell(j++);
				cell.setCellValue(sr.getSettlement_type_name());
				cell.setCellStyle(contentStyle);

				cell = row.createCell(j++);
				cell.setCellValue(sr.getOrder_amount());
				cell.setCellStyle(contentStyle);

				cell = row.createCell(j++);
				cell.setCellValue(sr.getSettlement_discount());
				cell.setCellStyle(contentStyle);

				cell = row.createCell(j++);
				cell.setCellValue(sr.getSettlementer_name());
				cell.setCellStyle(contentStyle);

				cell = row.createCell(j++);
				cell.setCellValue(sr.getSettlementer_amount());
				cell.setCellStyle(contentStyle);

				cell = row.createCell(j++);
				cell.setCellValue(sr.getSettlement_date());
				cell.setCellStyle(contentStyle);

				cell = row.createCell(j++);
				cell.setCellValue(sr.getAssigneeAddress());
				cell.setCellStyle(contentStyle);

				cell = row.createCell(j);
				cell.setCellValue(sr.getAssignee());
				cell.setCellStyle(contentStyle);
			}
			logger.info(">>>>>>>>>>>>>>>>>>>>end to loop the list>>>>>>>>>>");
		}
		try {
			fos = new FileOutputStream(settlementerDir + File.separator + fileName);
			wb.write(fos);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			try {
				if (fos != null) {
					fos.close();
				}
				wb.close();
			}
			catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
