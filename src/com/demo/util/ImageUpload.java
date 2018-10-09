package com.demo.util;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;

public class ImageUpload {

	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/uploadImage", method = RequestMethod.POST, headers=("content-type=multipart/*"))
    public ResponseEntity uploadImage(@ModelAttribute("imgFile") MultipartFile imgFile,HttpSession httpSession,HttpServletRequest request) throws Exception {
		Map<String,Object> result=new HashMap<String, Object>(); 
		boolean success=false; 
		try{
			if(imgFile.isEmpty()) {
				return new ResponseEntity<Map<String, Object>>(result, HttpStatus.OK);
			}
    		int size=20;
    		String allowedTypes="jpg,png";
    		if(AppUtil.isBlank(allowedTypes)) allowedTypes="*";
    		List<String> successUploads=new ArrayList<String>();
    		Map<String,Object> obj=new HashMap<String, Object>();
			String uuid= UUID.randomUUID().toString();
			/*//Instantiating tika facade class 
		    Tika tika = new Tika();        		      
		    //detecting the file type using detect method
		    String filetype = tika.detect(imgFile.getInputStream());
		    System.out.println(filetype);
			int count = commonService.validateFileByAllowedTypesWithConetentType(allowedTypes,filetype);*/
		    int count =1;
			if(!allowedTypes.equals("*") && count==0){
				//obj.put("imgFile", LangMessage.get(MessageKey.Validation.FILE_TYPE_NOT_SUPPORTED, lang));
				obj.put("imgFile", "Not supported");
			}else if(size>-1 && imgFile.getSize()>size){
				//obj.put("imgFile", LangMessage.get(MessageKey.Validation.SIZE_IS_GRATER_THAN, lang)+" "+ZcUtil.getFileSizeInReadable(size, 0));
				obj.put("imgFile", "Size is greater than allowed size");
				obj.put("size", AppUtil.getFileSizeInReadable(imgFile.getSize(), 0));  
			}else{ 
				//String originalFilelPath=AppSetting.APP_FILES_AND_RESOURCES_LOCATION+AppSetting.APP_FILES_AND_RESOURCES_TEMP_LOCATION+File.separator+uuid;
				String originalFilelPath="D://"+File.separator+uuid;
				AppUtil.saveFileIfNotExist(originalFilelPath);
				imgFile.transferTo(new File(originalFilelPath));	
				successUploads.add(originalFilelPath);	
				obj.put("attachUUID",uuid );
    			obj.put("attachName", imgFile.getOriginalFilename());
				success=true;
			}
    		result.put("success", success);
    		return new ResponseEntity<Map<String, Object>>(result, HttpStatus.OK);		
    	}catch(Exception ex){
    		ex.printStackTrace();
    		result.put("success", success);
    		return new ResponseEntity<Map<String, Object>>(result, HttpStatus.CONFLICT);
    	}
	}
}
