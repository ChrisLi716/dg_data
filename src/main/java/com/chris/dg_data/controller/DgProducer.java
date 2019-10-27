package com.chris.dg_data.controller;

import com.chris.dg_data.beans.Conditions;
import com.chris.dg_data.common.CommonUtils;
import com.chris.dg_data.service.SettlementerService;
import org.apache.commons.lang3.StringUtils;
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

	@Value("${basepath}")
	private String basePath;

	@Value("#{'${header}'.split(',')}")
	private final List<String> header = null;

	@Autowired
	private SettlementerService settlementerService;

	@RequestMapping(value = "/generateDgData", method = RequestMethod.GET)
	public String generateDgData(@RequestParam(value = "month") String month) {
		if (StringUtils.isNotEmpty(month) && StringUtils.isNotEmpty(basePath)) {

			return "true";
			/*List<String> allSettlementer = settlementerService.getAllSettlementer();
			List<Conditions> conditionsList = generateDataFolders(basePath, allSettlementer, Integer.valueOf(month));*/
		}
		return "false";
	}

	/**
	 * generate the data directories
	 *
	 * @param basePath the parent dir path
	 * @param month    month number 1-12
	 */
	private List<Conditions> generateDataFolders(String basePath, List<String> allSettlementer, int month) {
		String monthDir = basePath + File.separator + month;
		List<Conditions> conditionList = generateInterval(month);

		for (String settlementer : allSettlementer) {
			String settlementerDir = monthDir + File.separator + settlementer;
			for (Conditions conditions : conditionList) {
				int begin = conditions.getBeginDay();
				int end = conditions.getEndDay();
				String intervalPath = settlementerDir + File.separator + begin + "-" + end;
				CommonUtils.generateFolder(intervalPath);
				conditions.setFilePath(intervalPath);
			}
		}
		return conditionList;
	}

	/**
	 * calcualte the begin and end of the interval
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

	public static void main(String[] args) {
		DgProducer dgProducer = new DgProducer();
		List<String> allSettlementer = new ArrayList<>();
		allSettlementer.add("chris");
		allSettlementer.add("john");
		allSettlementer.add("周佳");
		allSettlementer.add("吴德明");
		dgProducer.generateDataFolders("E:/tmp", allSettlementer, 8);
	}

}
