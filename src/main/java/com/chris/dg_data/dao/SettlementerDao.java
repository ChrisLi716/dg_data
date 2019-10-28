package com.chris.dg_data.dao;

import com.chris.dg_data.beans.SettlementRecords;
import org.springframework.stereotype.Repository;

import java.util.List;

public interface SettlementerDao {

	public List<String> getAllSettlementer();

	public List<SettlementRecords> getSettlementRecords(String settlementCode, String beginDay, String endDay);
}
