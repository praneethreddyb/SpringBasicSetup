package com.demo.dynamic.service;

import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import com.demo.dynamic.dao.DynamicDao;

@Repository(value="dynamicService")
public class DynamicService {

	@Autowired DynamicDao dynamicDao;

	@Transactional(readOnly=true)
	public String getUserById(int id) {
		return dynamicDao.getUserById(id);
	}
	
	@Transactional(readOnly=true)
	public List<Map<String, Object>> getUsers() {
		return dynamicDao.getUsers();
	}
}
