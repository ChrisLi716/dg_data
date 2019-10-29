package com.chris.dg_data.controller;

import com.chris.dg_data.beans.Conditions;
import com.chris.dg_data.beans.SettlementRecords;
import com.chris.dg_data.common.CommonUtils;
import com.chris.dg_data.service.SettlementerService;
import com.chris.dg_data.threadpool.GlobalThreadPool;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.util.*;

@RestController
@RequestMapping("/dg")
public class DgProducer {

	private static Logger logger = Logger.getLogger(CommonUtils.class);

	@Value("${basepath}")
	private String basePath;

	@Value("${default.thread.number}")
	private String defalut_thread_number;

	@Value("#{'${dg.header}'.split(',')}")
	private final List<String> header = null;

	@Autowired
	private SettlementerService settlementerService;

	@RequestMapping(value = "/generateDgData", method = RequestMethod.GET)
	public String generateDgData(@RequestParam(value = "year", required = false) String year,
		@RequestParam(value = "month") String month, @RequestParam(value = "threads") String threads) {

		List<String> allSettlementer = new ArrayList<>();

		String firstDateOfMonth = CommonUtils.getFirstDateOfMonth(year, month);
		String lastDateOfMonth = CommonUtils.getLastDateOfMonth(year, month);

		if (StringUtils.isNotEmpty(month) && StringUtils.isNotEmpty(basePath)) {

			if (StringUtils.isEmpty(year)) {
				year = String.valueOf(CommonUtils.getCurrentYear());
			}
			if (StringUtils.isEmpty(threads)) {
				threads = defalut_thread_number;
			}

			allSettlementer = settlementerService.getAllSettlementer(firstDateOfMonth, lastDateOfMonth);

			if (!allSettlementer.isEmpty()) {
				generateDataFileInBatchs(year, month, allSettlementer, Integer.parseInt(threads));
			}
		}

		String logInfo =
			"Amount of settlementer :" + allSettlementer.size() + ",[" + firstDateOfMonth + " To " + lastDateOfMonth
				+ "]" + ", " + allSettlementer.toString();
		logger.info(logInfo);

		return logInfo;
	}

	private void generateDataFileInBatchs(String year, String month, List<String> allSettlementer, int threads) {

		if (allSettlementer.size() >= threads) {
			int block = allSettlementer.size() / threads;

			int i = 0;
			for (; i < threads - 1; i++) {
				int begin = block * i;
				int end = block * (i + 1);
				List<String> one_block = allSettlementer.subList(begin, end);
				processEachBatch(year, month, one_block);
			}

			// the last batch
			List<String> last_block = allSettlementer.subList(block * i, allSettlementer.size());
			processEachBatch(year, month, last_block);
		}
		else {
			processEachBatch(year, month, allSettlementer);
		}

	}

	private void processEachBatch(String year, String month, List<String> one_block) {
		GlobalThreadPool.execute(() -> {
			String threadName = Thread.currentThread().getName();
			for (String settlementer : one_block) {
				String settlementerDir = basePath + File.separator + month + File.separator + settlementer.trim();

				if (CommonUtils.generateFolder(settlementerDir)) {
					List<Conditions> conditionsList = generateInterval(Integer.parseInt(month));
					for (Conditions conditions : conditionsList) {
						String beginDateStr = year + "-" + month + "-" + conditions.getBeginDay();
						String endDateStr = year + "-" + month + "-" + conditions.getEndDay();
						logger.info(
							threadName + ", settlementer:" + settlementer + "[" + beginDateStr + " To " + endDateStr
								+ "]");

						List<SettlementRecords> records =
							settlementerService.getSettlementRecords(settlementer, beginDateStr, endDateStr);

						String csvFileName = conditions.getBeginDay() + "-" + conditions.getEndDay() + ".csv";
						CommonUtils.generateCsvFile(settlementerDir, csvFileName, header, records);
					}
				}
			}
		});
	}

	/**
	 * calculate the begin and end of the interval
	 * 1-3|4-6...
	 *
	 * @param month month number 1-12
	 * @return Conditions object with begin and end
	 */
	private List<Conditions> generateInterval(int month) {
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.MONTH, month - 1);
		int days = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);

		List<Conditions> conditionList = new ArrayList<>();
		int tmp = 1;
		int number = days / 3;
		for (int i = 0; i < number - 1; i++) {
			int begin = tmp;

			//1-3|4-6...
			tmp += 2;

			conditionList.add(new Conditions(begin, tmp));
			tmp++;
		}

		//set the interval to last day of the month
		conditionList.add(new Conditions(tmp, days));

		return conditionList;
	}

}
