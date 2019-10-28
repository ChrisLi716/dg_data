package com.chris.dg_data.service;

import com.chris.dg_data.beans.SettlementRecords;
import org.springframework.stereotype.Service;

import java.util.List;

public interface SettlementerService {

	public List<String> getAllSettlementer();

	public List<SettlementRecords> getSettlementRecords(String settlementer, String beginDay, String endDay);
}
