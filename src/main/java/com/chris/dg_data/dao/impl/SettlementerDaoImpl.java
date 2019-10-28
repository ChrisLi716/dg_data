package com.chris.dg_data.dao.impl;

import com.chris.dg_data.beans.SettlementRecords;
import com.chris.dg_data.dao.SettlementerDao;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class SettlementerDaoImpl implements SettlementerDao {

	private static Logger logger = Logger.getLogger(SettlementerDaoImpl.class);

	private JdbcOperations jdbc;

	@Autowired
	public SettlementerDaoImpl(JdbcOperations jdbc) {
		this.jdbc = jdbc;
	}

	@Override
	public List<String> getAllSettlementer() {
		return jdbc.queryForList(
			"select DISTINCT t.settlementer_name from t_ms_order_settlement_record t where t.settlementer_code not in ('A02379', 'A02376')",
			String.class);
	}

	@Override
	public List<SettlementRecords> getSettlementRecords(String settlementerName, String beginDay, String endDay) {
		return jdbc.query(
			"SELECT order_sn,order_date,settlement_type_name,order_amount,settlement_discount,settlementer_name,settlementer_amount,settlement_date ,a.full_address,a.consignee FROM t_ms_order_settlement_record t , t_ws_member_address a,t_ms_order o WHERE t.settlementer_name = ? AND t.settlement_date >= DATE_FORMAT( ?, '%Y-%m-%d' ) AND t.settlement_date <= DATE_FORMAT( ?, '%Y-%m-%d' ) AND a.id = o.express_address_id AND o.id = t.order_id ORDER BY t.settlement_date",
			new SettlementRecordsRowMapper(),
			settlementerName,
			beginDay,
			endDay);
	}

	private static class SettlementRecordsRowMapper implements RowMapper<SettlementRecords> {
		public SettlementRecords mapRow(ResultSet rs, int rowNum)
			throws SQLException {
			return new SettlementRecords(rs.getString("order_sn"),
				rs.getDate("order_date"),
				rs.getString("settlement_type_name"),
				rs.getString("order_amount"),
				rs.getString("settlement_discount"),
				rs.getString("settlementer_name"),
				rs.getString("settlementer_amount"),
				rs.getDate("settlement_date"),
				rs.getString("consignee"),
				rs.getString("full_address"));
		}
	}
}
