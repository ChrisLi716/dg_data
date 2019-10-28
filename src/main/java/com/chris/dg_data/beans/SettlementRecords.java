package com.chris.dg_data.beans;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

import java.util.Date;

/**
 * t_ms_order_settlement_record
 */
@Data
@ToString
@AllArgsConstructor
public class SettlementRecords {
	private String order_sn;

	private Date order_date;

	private String settlement_type_name;

	private String order_amount;

	private String settlement_discount;

	private String settlementer_name;

	private String settlementer_amount;

	private Date settlement_date;

	private String assignee;

	private String assigneeAddress;
}
