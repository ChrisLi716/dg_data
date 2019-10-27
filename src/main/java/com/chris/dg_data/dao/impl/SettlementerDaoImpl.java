package com.chris.dg_data.dao.impl;

import com.chris.dg_data.beans.SettlementRecords;
import com.chris.dg_data.dao.SettlementerDao;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository public class SettlementerDaoImpl implements SettlementerDao {

	@Override public List<String> getAllSettlementer() {
		return null;
	}

	@Override public List<SettlementRecords> getSettlementRecords(String settlementCode, int beginDay, int endDay) {
		return null;
	}
}
