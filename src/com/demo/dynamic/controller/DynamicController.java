package com.demo.dynamic.controller;

import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import com.demo.dynamic.service.DynamicService;

@RestController
public class DynamicController {

	
	
	@Autowired DynamicService dynamicService;
	
	@RequestMapping(value ="/getUserById/{id}", method = RequestMethod.GET)
	public ResponseEntity<Map<String,Object>> getUserById(HttpServletRequest request,@PathVariable int id) throws Exception {
		Map<String,Object> result=new HashMap<String, Object>(); 
		String res=dynamicService.getUserById(id);
		result.put("success",res==null?false:true);
		result.put("name",res);
		return new ResponseEntity<Map<String, Object>>(result, HttpStatus.OK);
	}
	
	
	@RequestMapping(value ="/getUsers", method = RequestMethod.GET)
	public ResponseEntity<Map<String,Object>> getUsers(HttpServletRequest request) throws Exception {
		Map<String,Object> result=new HashMap<String, Object>(); 
		result.put("success",true);
		result.put("data",dynamicService.getUsers());
		return new ResponseEntity<Map<String, Object>>(result, HttpStatus.OK);
	}
}
