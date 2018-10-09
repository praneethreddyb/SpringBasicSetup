package com.demo.util;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.HorizontalAlignment;

public class ExcelGeneration {

	public HSSFWorkbook generateExcel(String sheetName, List<Map<String,Object>> dataList,List<Map<String,Object>> colNames) throws Exception {
		
		HSSFWorkbook workbook = new HSSFWorkbook();
	    HSSFSheet sheet = workbook.createSheet(sheetName);
	    HSSFCellStyle style = workbook.createCellStyle();
	    style.setFillBackgroundColor(HSSFColor.HSSFColorPredefined.SKY_BLUE.getIndex());
	    style.setFillForegroundColor(HSSFColor.HSSFColorPredefined.SKY_BLUE.getIndex());
	  
	    HSSFFont font = workbook.createFont();
	    font.setBold(true);
	    font.setFontName(HSSFFont.FONT_ARIAL);
	    font.setFontHeightInPoints((short) 15);
	    font.setColor(HSSFColor.HSSFColorPredefined.CORNFLOWER_BLUE.getIndex());
	    style.setFont(font);

	    HSSFCellStyle cellStyle = workbook.createCellStyle();
	    cellStyle = workbook.createCellStyle();
	    
	    HSSFFont hSSFFont = workbook.createFont();
	    hSSFFont.setFontName(HSSFFont.FONT_ARIAL);
	    hSSFFont.setFontHeightInPoints((short)10);
	    hSSFFont.setBold(true);
	    hSSFFont.setColor(HSSFColor.HSSFColorPredefined.BLACK.getIndex());
	    cellStyle.setFont(hSSFFont);
	    cellStyle.setWrapText(true);
	    cellStyle.setAlignment(HorizontalAlignment.CENTER); 
	    
	    HSSFCellStyle dataCellStyle = workbook.createCellStyle();
	    dataCellStyle = workbook.createCellStyle();
	    HSSFFont hSSFDataFont = workbook.createFont();
	    hSSFDataFont.setFontName(HSSFFont.FONT_ARIAL);
	    hSSFDataFont.setFontHeightInPoints((short)10);
	    dataCellStyle.setFont(hSSFDataFont);
	    dataCellStyle.setWrapText(true);
	    dataCellStyle.setAlignment(HorizontalAlignment.LEFT); 
	    dataCellStyle.setDataFormat((short) HSSFDataFormat.getNumberOfBuiltinBuiltinFormats());
	    
	    HSSFCellStyle dataCellStyle2 = workbook.createCellStyle();
	    dataCellStyle2 = workbook.createCellStyle();
	    HSSFFont hSSFDataFont2 = workbook.createFont();
	    hSSFDataFont2.setFontName(HSSFFont.FONT_ARIAL);
	    hSSFDataFont2.setFontHeightInPoints((short)10);
	    dataCellStyle2.setFont(hSSFDataFont);
	    dataCellStyle2.setWrapText(true);
	    dataCellStyle2.setAlignment(HorizontalAlignment.RIGHT); 
	    dataCellStyle2.setDataFormat((short) HSSFDataFormat.getNumberOfBuiltinBuiltinFormats());
	    HSSFRow row = sheet.createRow(0);
	    int rowNum = 1;
	    int colNum = 0;
	   for(Map<String,Object> column : colNames) {
		    row.setRowStyle(style);
		    row.setHeightInPoints(15);
	    	HSSFCell headCell =  row.createCell(colNum);
	    	headCell.setCellStyle(cellStyle);
	    	headCell.setCellValue(column.get("label")+"");
	    	sheet.setColumnWidth(colNum, column.get("label").toString().length()*1000);
	    	colNum++;
	   }
    	for(Map<String,Object> inventory : dataList){
    		colNum = 0;
    		row = sheet.createRow(rowNum);
    		for(Map<String,Object> column : colNames) {
    			try{    				
    				HSSFCell cell =  row.createCell(colNum++);
    				cell.setCellStyle(dataCellStyle);
    				String val = getValueOfField(inventory, column);
    				cell.setCellValue(val);
    			}catch (Exception e) {
    				e.printStackTrace();
    			}
    		}
    		rowNum++;
		}
		return workbook;
	}
	
	@SuppressWarnings("unchecked")
	public String getValueOfField(Map<String,Object> widgetData, Map<String,Object> field) throws Exception {
		String value = "";
		String type = field.get("type")+"";
		String fieldName = field.get("name")+"";
		if(widgetData.get(fieldName) instanceof List) {
			String nameOrLabel = type.equals("file") ? "name" : "label";
			for(Map<String,Object> item : ((List<Map<String,Object>>)widgetData.get(fieldName))) value += (value.length() == 0 ? "" : ", ") + item.get(nameOrLabel)+"";
		}else if(widgetData.get(fieldName) instanceof Map) 
			if(type.equals("currency")) 
				value = (AppUtil.isBlank(((Map<String,Object>)widgetData.get(fieldName)).get("code")) ? "" : ((Map<String,Object>)widgetData.get(fieldName)).get("code")) 
						+" "+ (AppUtil.isBlank(((Map<String,Object>)widgetData.get(fieldName)).get("value")) ? "" : getFormattedCurrency(((Map<String,Object>)widgetData.get(fieldName)).get("value")+"", ",", "."));
			else if(type.equals("phone")) 
				value = ((Map<String,Object>)widgetData.get(fieldName)).get("value")+"";
			else
				value = ((Map<String,Object>)widgetData.get(fieldName)).get("label")+"";
		else if(type.equals("date") && !AppUtil.isBlank(widgetData.get(fieldName))) 
			value = new SimpleDateFormat("dd-MMM-yyyy").format(AppUtil.getDateFromUtcString(widgetData.get(fieldName)+""));  
		else if(type.equals("datetime") && !AppUtil.isBlank(widgetData.get(fieldName))) 
			value = new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss").format(AppUtil.getDateFromUtcString(widgetData.get(fieldName)+""));  
		else
			value = widgetData.get(fieldName)+"";
		return AppUtil.isBlank(value) ? "--" : value;
	}

	private String getFormattedCurrency(String value, String seperator, String decimal) {
		if(AppUtil.isBlank(value)) return "--";
		String[] inputFraction = value.split("\\.");
		value = inputFraction[0];
		for(int valLen = value.length(); valLen > 3; valLen -= 3) 
			value = value.substring(0, valLen - 3) + seperator + value.substring(valLen - 3, valLen) 
							+ value.substring(valLen, value.length());
		
		value = value + ((inputFraction.length == 2 ? inputFraction[1] : "").length() > 0 ? (decimal + inputFraction[1]) : "");
//		return String.format("%,.2f", Double.parseDouble(value));
		return value;
	}
}
