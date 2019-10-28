package com.chris.dg_data.service.impl;

import com.chris.dg_data.beans.SettlementRecords;
import com.chris.dg_data.dao.SettlementerDao;
import com.chris.dg_data.service.SettlementerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SettlementerServiceImpl implements SettlementerService {

	@Autowired
	private SettlementerDao settlementerDao;

	@Override
	public List<String> getAllSettlementer() {
		List<String> settlementers = new ArrayList<>();
		return settlementerDao.getAllSettlementer();
	}

	@Override
	public List<SettlementRecords> getSettlementRecords(String settlementer, String beginDay, String endDay) {
		return settlementerDao.getSettlementRecords(settlementer, beginDay, endDay);
	}
}
