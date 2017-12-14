package com.bpm.framework.utils.file;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.Serializable;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.bpm.framework.utils.date.DateUtils;

/***
 * 主要用于解析2007以上版本Excel文件，兼容2007以前的Excel
 * 
 * @author lixx
 * @createDate 2015-10-08 14:39:00
 */
public class Excel2007Utils implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -284207141392887329L;

	private Excel2007Utils() {}
	
	/**
	 * 
	 * 该方法可以根据传入文件，自动尝试两种方式解析，推荐使用
	 * 
	 * @param file
	 * @return
	 * @throws Exception
	 */
	public static Workbook getWorkbook(File file) throws Exception {
		Workbook wb = null;
		try {
			wb = new XSSFWorkbook(new FileInputStream(file));// excel 2007+
		} catch(Exception e) {
			wb = new HSSFWorkbook(new FileInputStream(file));// excel 2003-
		}
		return wb;
	}
	
	/**
	 * 
	 * 该方法存在缺陷，当传入的is为2003-的时候，解析会报Read Error错误
	 * 
	 * @param is
	 * @return
	 * @throws Exception
	 * @see getWorkbook(File file)
	 */
	@Deprecated
	public static Workbook getWorkbook(InputStream is) throws Exception {
		Workbook wb = null;
		try {
			wb = new XSSFWorkbook(is);// excel 2007+
		} catch(Exception e) {
			wb = new HSSFWorkbook(is);// excel 2003-
		}
		return wb;
	}
	
	/**
	 * 
	 * 获取指定sheet
	 * 
	 * @param file
	 * @param sheetIndex 从0开始
	 * @return
	 * @throws Exception
	 */
	public static Sheet getSheet(File file, int sheetIndex) throws Exception {
		Workbook book = getWorkbook(file);
		return book.getSheetAt(sheetIndex);
	}
	
	/**
	 * 
	 * 获取指定sheet
	 * 
	 * @param file
	 * @param sheetIndex 从0开始
	 * @return
	 * @throws Exception
	 */
	public static Sheet getSheet(InputStream is, int sheetIndex) throws Exception {
		Workbook book = getWorkbook(is);
		return book.getSheetAt(sheetIndex);
	}
	
	/**
	 * 读excel表中单元格的内容（文本格式的）
	 * 
	 * @param st
	 *            工作表
	 * @param row
	 *            行（0开始）
	 * @param col
	 *            列（0开始的）
	 * @return
	 */
	public static String getCellValue(Sheet st, int row, int col) {
		String str = "";
		Cell cell = st.getRow(row).getCell(col);
		if(cell != null ){
			switch (cell.getCellType()) {
			case Cell.CELL_TYPE_NUMERIC:
				// 判断当前Cell是否为Date
				if (HSSFDateUtil.isCellDateFormatted(cell)) {
					str = DateUtils.toString(cell.getDateCellValue(), DateUtils.YEAR_MONTH_DAY_HH_MM_SS);
					break;
				}
				// 由于POI会自动将数字文本当成数字读取，此处需要程序处理成文本
				double  doubleVal = cell.getNumericCellValue();   //  用于判断是否要去掉整数后面的0
				long longVal = Math.round(cell.getNumericCellValue());  
			    if(Double.parseDouble(longVal + ".0") == doubleVal) {
			    	str = String.valueOf(longVal);
			    }else{
			    	str = String.valueOf(doubleVal);
			    }
				break;
			case Cell.CELL_TYPE_STRING:
				str = cell.getStringCellValue();
				break;
			case Cell.CELL_TYPE_BOOLEAN:
				str = String.valueOf(cell.getBooleanCellValue());
				break;
			default:
				break;
			}
		}
		return str == null ? "" : str;
	}
	
	/**
	 * 判断当前行是否为空行
	 * @param st
	 * @param totalCol
	 * @param curRow
	 * @return
	 */
	public static Boolean isEmptyRow(Sheet st,int totalCol,int curRow){
		String str = "";
		Boolean result=true;
		for (int i = 0; i < totalCol; i++) {
			Cell cell = st.getRow(curRow).getCell(i);
			switch (cell.getCellType()) {
				case Cell.CELL_TYPE_NUMERIC:
					// 判断当前Cell是否为Date
					if (HSSFDateUtil.isCellDateFormatted(cell)) {
						str = DateUtils.toString(cell.getDateCellValue(), DateUtils.YEAR_MONTH_DAY_HH_MM_SS);
						break;
					}
					// 由于POI会自动将数字文本当成数字读取，此处需要程序处理成文本
					str = String.valueOf((cell.getNumericCellValue()));
					break;
				case Cell.CELL_TYPE_STRING:
					str = cell.getStringCellValue();
					break;
				case Cell.CELL_TYPE_BOOLEAN:
					str = String.valueOf(cell.getBooleanCellValue());
					break;
				default:
					str = cell.getStringCellValue();
					break;
			}
			
			if(!StringUtils.isEmpty(str)){
				result = false;
				break;
			}
		}
		
		return result;
	}
	
	public static void main(String[] args) throws Exception {
		try {
			File file = new File("E:/a.xlsx");
			Workbook book = getWorkbook(file);
			Sheet sheet = book.getSheetAt(0);
	        Row row = sheet.getRow(0);
	        Cell cell = row.getCell(0);
	        System.out.println("cell value = " + cell.getStringCellValue());
	        Object obj = getCellValue(sheet, 0, 0);
	        System.out.println(sheet.getLastRowNum());
	        System.out.println(obj);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
