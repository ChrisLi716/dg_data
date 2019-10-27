package com.chris.dg_data.beans;

import lombok.Data;
import lombok.ToString;

/**
 * t_ms_order_settlement_record
 */
@Data
@ToString
public class SettlementRecords {
    private String order_sn;
    private String order_date;
    private String settlement_type_name;
    private String order_amount;
    private String settlement_discount;
    private String settlementer_name;
    private String settlementer_amount;
    private String settlement_date;
    private String assignee;
    private String assigneeAddress;
}
