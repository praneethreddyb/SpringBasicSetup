package com.demo.dao.common;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import com.demo.util.AppUtil;

public class DaoCommon {

	@Autowired
	protected JdbcTemplate jdbcTemplate;
	
	
	protected int getGeneratedKey(final String query,final Object[] values)throws Exception {
	     return getGeneratedKey(jdbcTemplate,query, values);
	}
	
	@SuppressWarnings("deprecation")
	public static int getGeneratedKey(JdbcTemplate jdbcTemplate,final String query,final Object[] values)throws Exception {
	     KeyHolder keyHolder = new GeneratedKeyHolder();
	    	jdbcTemplate.update(new PreparedStatementCreator() {
		    	  public PreparedStatement createPreparedStatement(Connection connection)throws SQLException {
		    	    PreparedStatement ps = (PreparedStatement) connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
		    	    if(values!=null){
		    	    	int i=0;
		    	    	for(Object obj:values)
		    	    		ps.setObject(++i, obj);
		    	    }
		    	    return ps;}}, keyHolder);
	    	if(keyHolder==null || keyHolder.getKey()==null )
	    		return 0;
	    	Long generatedId = new Long(keyHolder.getKey().longValue());
	    	return generatedId.intValue();
	}
	protected void insertIds(int...ids) {
		insertIds("_ids",ids);
	}
	
 	@SuppressWarnings("serial")
 	protected void insertIds(String tableName,int...ids) {
 		String sql="DROP TEMPORARY TABLE IF EXISTS "+tableName+";\n" + 
 				"CREATE TEMPORARY TABLE IF NOT EXISTS "+tableName+" (\n" + 
 				"  `id` int(10) unsigned NOT NULL\n" + 
 				");";
 		jdbcTemplate.execute(sql);
 		sql="INSERT INTO _ids (`id`) VALUES (?);";
 		List<Object[]> params=new ArrayList<Object[]>() {{
 			for(int i:ids) add(new Object[] {i});	
 		}};
 		jdbcTemplate.batchUpdate(sql, params);		
 	}
 	
 	@SuppressWarnings("serial")
 	protected void insertCodes(String tableName, String...codes) {
 		String sql="DROP TEMPORARY TABLE IF EXISTS "+tableName+";\n" + 
 				"CREATE TEMPORARY TABLE IF NOT EXISTS "+tableName+"(\n" + 
 				"  `code` VARCHAR(160) NOT NULL\n" + 
 				");";
 		jdbcTemplate.execute(sql);
 		sql="INSERT INTO "+tableName+" (`code`) VALUES (?);";
 		List<Object[]> params=new ArrayList<Object[]>() {{
 			for(String i:codes) add(new Object[] {AppUtil.truncate(i, 160)});	
 		}};
 		jdbcTemplate.batchUpdate(sql, params);		
 	}
	
 	protected void insertCodes(String...codes) {
 		insertCodes("_codes",codes);
 	} 
	
	protected Object queryForObject(final String query, final String key, final Object...values)throws Exception {
		return queryForObject(query,key,jdbcTemplate,values);
	}
	
	
	protected Object queryForObject(final String query, final Object...values)throws Exception {
		return queryForObject(query,null,values);
	}
	
	
	protected Integer queryForInteger(final String query, final String key, final Object[] values)throws Exception {
		return AppUtil.parseInt(queryForObject(query,key,jdbcTemplate,values));
	}
	
	
	protected Integer queryForInteger(final String query, final Object[] values)throws Exception {
		return queryForInteger(query,null,values);
	}
	
	
	protected Integer queryForInteger(final String query)throws Exception {
		return queryForInteger(query,null,new Object[] {});
	}
		
	
	public static Object queryForObject(final String query, final String key, JdbcTemplate jdbcTemplate, final Object...values)throws Exception {
		List<Map<String,Object>> list=jdbcTemplate.queryForList(query,values);
		if(AppUtil.isBlank(list)) return null;
		if(AppUtil.isBlank(key)) {
			Object val=null;
			for(Map.Entry<String, Object> entry:list.get(0).entrySet()) {
				val=entry.getValue();
				break;
			}
			return val;
		}else	
			return list.get(0).get(key);
	}
}
