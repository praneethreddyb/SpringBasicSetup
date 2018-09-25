package com.demo.dynamic.dao;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository(value="dynamicDao")
public class DynamicDao {

	
	@Autowired JdbcTemplate jdbcTemplate;

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
