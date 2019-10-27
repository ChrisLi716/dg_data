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
	public String generateDgData(@RequestParam("month") String month) {
		if (StringUtils.isNotEmpty(month) && StringUtils.isNotEmpty(basePath)) {
			List<Conditions> conditionsList = generateDataFolders(basePath, Integer.valueOf(month));
			List<String> allSettlementer = settlementerService.getAllSettlementer();
		}
		return "";
	}

	/**
	 * generate the data directories
	 *
	 * @param basePath the parent dir path
	 * @param month    month number 1-12
	 */
	private List<Conditions> generateDataFolders(String basePath, int month) {
		String parentPath = basePath + File.separator + month;
		CommonUtils.generateFolder(parentPath);
		List<Conditions> conditionList = generateInterval(month);
		for (Conditions conditions : conditionList) {
			int begin = conditions.getBeginDay();
			int end = conditions.getEndDay();
			String subPath = parentPath + File.separator + begin + "-" + end;
			CommonUtils.generateFolder(subPath);
			conditions.setFilePath(subPath);
		}
		return conditionList;
	}

	private List<Conditions> generateInterval(int month) {
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.MONTH, month - 1);
		int days = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);

		List<Conditions> conditionList = new ArrayList<>();
		int tmp = 1;
		int number = days / 3;
		for (int i = 0; i < number - 1; i++) {
			int begin = tmp;
			int end = tmp + 2;
			conditionList.add(new Conditions(begin, end));
			tmp++;
		}

		//set the interval to last day of the month
		conditionList.add(new Conditions(tmp, days));

		return conditionList;
	}

}
