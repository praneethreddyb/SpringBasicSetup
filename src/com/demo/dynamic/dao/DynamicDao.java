package com.demo.dynamic.dao;

import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Repository;
import com.demo.dao.common.DaoCommon;

@Repository(value="dynamicDao")
public class DynamicDao extends DaoCommon{

	

	public String getUserById(int id) {
		try {
			Map<String, Object> data= jdbcTemplate.queryForMap("SELECT * FROM app_user WHERE pk_id=?",id);
			return data.get("name")+"";
		}catch (Exception e) {
			return null;
		}
		
	}

	public List<Map<String,Object>> getUsers() {
		return jdbcTemplate.queryForList("SELECT * FROM app_user");
	}
	
}
