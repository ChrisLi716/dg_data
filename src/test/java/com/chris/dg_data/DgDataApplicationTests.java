package com.chris.dg_data;

import com.chris.dg_data.beans.SettlementRecords;
import com.chris.dg_data.common.CommonUtils;
import com.chris.dg_data.dao.SettlementerDao;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
@WebAppConfiguration
class DgDataApplicationTests {

	@Autowired
	private SettlementerDao settlementerDao;

	@Value("#{'${dg.header}'.split(',')}")
	private List<String> header;

	@Test
	void testAllSettlementer() {
		List<String> allSettlementer = settlementerDao.getAllSettlementer(CommonUtils.getFirstDateOfMonth("2019", "09"),
			CommonUtils.getLastDateOfMonth("2019", "09"));
		System.out.println("all settlementer:" + allSettlementer.size());
		for (String s : allSettlementer) {
			System.out.println(s);
		}
	}

	@Test
	void testSettlementRecords() {

		List<SettlementRecords> settlementRecords =
			settlementerDao.getSettlementRecords("_掌柜哒", "2019-09-01", "2019-09-30");
		for (SettlementRecords r : settlementRecords) {
			System.out.println(r.toString());
		}
		CommonUtils.generateCsvFile("D:\\temp\\9\\_掌柜哒", "1-30.csv", header, settlementRecords);
	}

}
