package com.bpm.framework.utils.file;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.net.URLEncoder;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.log4j.Logger;

import com.bpm.framework.console.Application;
import com.bpm.framework.utils.StringUtils;
import com.bpm.framework.utils.bean.ConvertUtils;
import com.bpm.framework.utils.date.DateUtils;

import jxl.Cell;
import jxl.CellType;
import jxl.DateCell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.format.Alignment;
import jxl.format.Border;
import jxl.format.BorderLineStyle;
import jxl.format.Colour;
import jxl.read.biff.BiffException;
import jxl.write.DateFormat;
import jxl.write.DateTime;
import jxl.write.Label;
import jxl.write.NumberFormat;
import jxl.write.WritableCell;
import jxl.write.WritableCellFormat;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;

/**
 * 
 * 如果需要解析office2007+则使用Excel7Utils.java
 * 
 * @see Excel2007Utils.java
 * @author andyLee
 */
@SuppressWarnings({"rawtypes", "unchecked"})
public class ExcelUtils implements Serializable {

	private static final long serialVersionUID = 2915647922671689907L;

	private static Logger log = Logger.getLogger(ExcelUtils.class);

	private final static String FILE_PATH = Application.getInstance().getTempFileDirectory();

	private final static String EXCEL_Extension = ".xls";

	private final static String RSPAN = "#rspan";

	private final static String CSPAN = "#cspan";
	
	private final static String SUB_DIR = "/data/temp";

	public static WritableCellFormat FORMAT_NUM = new WritableCellFormat(
			new NumberFormat("###,##0.00"));

	public static WritableCellFormat FORMAT_DATE = new WritableCellFormat(
			new DateFormat("yyyy-MM-dd hh:mm:ss"));

	public static File simpleCreate(String title, String[] columnTitles,
			Collection data) {
		File file = null;
		WritableWorkbook book = null;
		try {
			file = new File(ExcelUtils.getExcelName(title));
			book = Workbook.createWorkbook(file);

			// 生成名为"第一页"的工作表，参数0表示这是第一页
			WritableSheet sheet = book.createSheet(title, 0);

			// 在Label对象的构造子中指名单元格位置是第一列第一行(0,0)
			int rowIndex = 0;

			WritableCellFormat titleFormat = getDefaultTitleFormat();

			// 如果表头不为空，则写入表头
			if (columnTitles != null) {
				for (int i = 0; i < columnTitles.length; i++) {
					WritableCell label = createCell(i, rowIndex,
							columnTitles[i], titleFormat);
					sheet.addCell(label);
				}
				rowIndex++;
			}
			// 写入数据
			Iterator it = data.iterator();
			while (it.hasNext()) {
				Object[] row = (Object[]) it.next();
				int colIndex = 0;
				for (Object cell : row) {
					setColumnView(sheet, colIndex, rowIndex, cell);
					WritableCell label = createCell(colIndex, rowIndex, cell);
					// 将定义好的单元格添加到工作表中
					sheet.addCell(label);
					colIndex++;
				}
				rowIndex++;
			}
			return file;
		} catch (Exception e) {
			log.error("", e);
		} finally {
			try {
				if (book != null) {
					book.write();
					book.close();
				}
			} catch (IOException e) {
				log.error("", e);
			} catch (Exception e) {
				log.error("", e);
			}
		}

		return file;
	}

	public static File mulitHeaderCreate(String title,
			List<String[]> columnTitles, Collection data) {
		return ExcelUtils.mulitHeaderCreate(title, columnTitles, data, null);
	}

	public static File mulitHeaderCreate(String title,
			List<String[]> columnTitles, Collection data,
			List<WritableCellFormat> dataFormats) {
		File file = null;
		WritableWorkbook book = null;
		try {
			file = new File(ExcelUtils.getExcelName(title));

			book = Workbook.createWorkbook(file);

			// 生成名为"第一页"的工作表，参数0表示这是第一页
			WritableSheet sheet = book.createSheet(title, 0);

			// 在Label对象的构造子中指名单元格位置是第一列第一行(0,0)
			int rowIndex = 0;

			WritableCellFormat titleFormat = getDefaultTitleFormat();

			// 如果表头不为空，则写入表头
			if (columnTitles != null) {
				// 当标题行只有一行时，不需要解析，直接顺序显示即可
				if (columnTitles.size() == 1) {
					String[] tempHeads = columnTitles.get(0);
					for (int i = 0; i < tempHeads.length; i++) {
						WritableCell label = createCell(i, rowIndex,
								tempHeads[i], titleFormat);
						sheet.addCell(label);
					}
					rowIndex++;
				} else {// 如果是多表头的时候，就需要解析合并表头
					for (int i = 0; i < columnTitles.size(); i++) {
						String[] tempHeads = columnTitles.get(i);
						for (int j = 0; j < tempHeads.length; j++) {
							if (CSPAN.equals(tempHeads[j])) {
								int preColIndex = j - 1;
								preColIndex = preColIndex < 0 ? 0 : preColIndex;
								if (!CSPAN.equals(tempHeads[preColIndex])) {
									int nextColIndex = j;
									while (nextColIndex < tempHeads.length
											&& CSPAN.equals(tempHeads[nextColIndex])) {
										nextColIndex++;
									}
									sheet.mergeCells(preColIndex, i,
											nextColIndex - 1, i);
								}
							} else if (RSPAN.equals(tempHeads[j])) {
								int preRowIndex = i - 1;
								preRowIndex = preRowIndex < 0 ? 0 : preRowIndex;
								if (!RSPAN
										.equals(columnTitles.get(preRowIndex)[j])) {
									int nextRowIndex = i;
									while (nextRowIndex < columnTitles.size()
											&& RSPAN.equals(columnTitles
													.get(nextRowIndex)[j])) {
										nextRowIndex++;
									}
									sheet.mergeCells(j, preRowIndex, j,
											nextRowIndex - 1);
								}
							} else {
								WritableCell label = createCell(j, i,
										tempHeads[j], titleFormat);
								sheet.addCell(label);
							}
						}
						rowIndex++;
					}
				}
			}
			// 写入数据
			WritableCellFormat format = null;
			WritableCellFormat otherFormat = new WritableCellFormat();
			WritableCellFormat numFormat = new WritableCellFormat(
					new NumberFormat("###,##0.00"));
			WritableCellFormat dateFormat = new WritableCellFormat(
					new DateFormat("yyyy-MM-dd hh:mm:ss"));
			try {
				dateFormat.setAlignment(Alignment.CENTRE);
			} catch (WriteException e) {
				log.error(ExceptionUtils.getStackTrace(e));
			}
			Iterator it = data.iterator();
			while (it.hasNext()) {
				Object[] row = (Object[]) it.next();
				int colIndex = 0;
				for (Object cell : row) {
					setColumnView(sheet, colIndex, rowIndex, cell);
					WritableCell label;
					if (dataFormats != null && dataFormats.size() > colIndex
							&& dataFormats.get(colIndex) != null) {
						label = createCell(colIndex, rowIndex, cell,
								dataFormats.get(colIndex));
					} else {
						// label = createCell(colIndex, rowIndex, cell);
						// if (cell instanceof Number) {//对所有数字类型做格式化
						if (cell instanceof Double || cell instanceof Float) {// 只对小数类型做格式化
							format = numFormat;
						} else if (cell instanceof Date
								|| cell instanceof Timestamp
								|| cell instanceof java.sql.Date) {
							format = dateFormat;
						} else {
							format = otherFormat;
						}
						label = createCell(colIndex, rowIndex, cell, format);
					}
					// 将定义好的单元格添加到工作表中
					sheet.addCell(label);
					colIndex++;
				}
				rowIndex++;
			}
			return file;
		} catch (Exception e) {
			log.error("", e);
		} finally {
			try {
				if (book != null) {
					book.write();
					book.close();
				}
			} catch (IOException e) {
				log.error("", e);
			} catch (Exception e) {
				log.error("", e);
			}
		}

		return file;
	}

	/**
	 * 创建Excel单元格，根据数据的不同类型，创建不同类型的单元格
	 * 
	 * @param colIndex
	 * @param rowIndex
	 * @param value
	 * @return
	 */
	public static WritableCell createCell(int colIndex, int rowIndex,
			Object value) {
		WritableCell cell = null;
		WritableCellFormat format = new WritableCellFormat();

		if (value instanceof Number) {
			value = value == null ? 0 : value;
			format = new WritableCellFormat(new NumberFormat("###,##0.00"));
			cell = new jxl.write.Number(colIndex, rowIndex,
					Double.parseDouble(value.toString()), format);
			return cell;
		}
		if (value instanceof Date || cell instanceof Timestamp
				|| cell instanceof java.sql.Date) {
			format = new WritableCellFormat(new DateFormat(
					"yyyy-MM-dd hh:mm:ss"));
			try {
				format.setAlignment(Alignment.CENTRE);
			} catch (WriteException e) {
				log.error(ExceptionUtils.getStackTrace(e));
			}
			cell = new DateTime(colIndex, rowIndex, (Date) value, format);
			return cell;
		}

		cell = new Label(colIndex, rowIndex, value == null ? ""
				: value.toString(), format);

		return cell;
	}

	/**
	 * 创建Excel单元格，根据数据的不同类型，创建不同类型的单元格，还可以通过WritableCellFormat进行格式化
	 * 
	 * @param colIndex
	 * @param rowIndex
	 * @param value
	 * @param format
	 * @return
	 */
	public static WritableCell createCell(int colIndex, int rowIndex,
			Object value, WritableCellFormat format) {
		WritableCell cell = null;
		if (value instanceof Number) {
			value = value == null ? 0 : value;
			cell = new jxl.write.Number(colIndex, rowIndex,
					Double.parseDouble(value.toString()), format);

			return cell;
		}
		if (value instanceof Date || cell instanceof Timestamp
				|| cell instanceof java.sql.Date) {
			cell = new DateTime(colIndex, rowIndex, (Date) value, format);
			return cell;
		}
		cell = new Label(colIndex, rowIndex, value == null ? ""
				: value.toString(), format);
		return cell;
	}

	/**
	 * 根据数据的长度设置列的宽度
	 * 
	 * @param sheet
	 * @param colIndex
	 * @param rowIndex
	 * @param value
	 */
	@SuppressWarnings("deprecation")
	public static void setColumnView(WritableSheet sheet, int colIndex,
			int rowIndex, Object value) {
		if (value == null) {
			return;
		}
		int width = sheet.getColumnView(colIndex).getDimension();
		width = width == 8 ? 6 : width; // jxl如果不设置列宽，默认为8，这里相当于修改成默认6
		if (value instanceof Number) {
			if (value.toString().length() <= 6 && width <= 6) {
				sheet.setColumnView(colIndex, 6);
			} else {
				sheet.setColumnView(colIndex, 15);
			}
		} else if (value instanceof Date) {
			sheet.setColumnView(colIndex, 20);
		} else {
			if (value.toString().length() <= 6 && width <= 10) {
				sheet.setColumnView(colIndex, 10);
			} else {
				sheet.setColumnView(colIndex, 20);
			}
		}
	}

	public static String getExcelName(String title) {
		File file = null;
		String tempPath = FILE_PATH;
		if(StringUtils.isNullOrBlank(FILE_PATH)) {
			tempPath = "/data/temp";
			file = new File(tempPath);
		} else {
			file = new File(FILE_PATH);
		}
		if (!file.exists()) {
			boolean isSuccess = file.mkdirs();
			if (!isSuccess) {
				throw new RuntimeException(file.getAbsolutePath() + " the directory was created failed");
			}
		}
		return tempPath + "/" + title + "-" + System.currentTimeMillis()
				+ EXCEL_Extension;
	}

	/**
	 * 读excel文件公用方法
	 * 
	 * @param beginRow
	 *            指定从某行开始读 （第一行行号为：0）
	 * @param beginCol
	 *            指定读开始列 (第一列列号为：0)
	 * @param endCol
	 *            指定读结束列
	 * @param pointCol
	 *            指定某列出现指定结束标示的时候结束读文件
	 * @param str
	 *            指定读结束标示
	 * @return 返回一个Object[]数据List
	 * @throws Exception
	 * @author liziguo
	 */
	@SuppressWarnings("unused")
	public static List<Object[]> readExcel(int beginRow, int beginCol,
			int endCol, int pointCol, String str) throws Exception {
		File file = null;
//		RequestUploadUtils.getRequestFileOnserverTemp(ActionUtils
//				.getRequest());

		if (file == null || !file.getName().endsWith(".xls")) {
			return null;
		}
		InputStream is = new FileInputStream(file);

		jxl.Workbook wb = Workbook.getWorkbook(is); // 得到工作薄
		jxl.Sheet st = wb.getSheet(0);// 得到工作薄中的第一个工作表
		List<Object[]> list = new ArrayList<Object[]>();
		boolean flag = true;
		int i = beginRow;
		while (flag) {
			if (str.equals(st.getCell(pointCol, i).getContents().trim())) {
				flag = false;
				break;
			}
			Object[] obj = new Object[endCol - beginCol + 1];
			int j = 0;
			for (int col = beginCol; col <= endCol; col++) {
				Cell cell1 = st.getCell(col, i);// 得到工作表的第n个单元格.
				obj[j++] = cell1.getContents().trim();// getContents()
				// 将Cell中的字符转为字符串
			}

			list.add(obj);
			i++;
		}
		wb.close();// 关闭工作薄
		is.close();// 关闭输入流

		return list;
	}

	public static WritableCellFormat getDefaultTitleFormat() {
		WritableCellFormat titleFormat = new WritableCellFormat();
		try {
			titleFormat.setBackground(Colour.GRAY_25);
			titleFormat.setBorder(Border.ALL, BorderLineStyle.THIN);
			titleFormat.setAlignment(Alignment.CENTRE);
		} catch (WriteException e) {
			log.error(ExceptionUtils.getStackTrace(e));
		}
		return titleFormat;
	}

	/**
	 * 读excel表中单元格的内容（文本格式的）
	 * 
	 * @see Excel2007Utils.java
	 * @param st
	 *            工作表
	 * @param row
	 *            行（1开始）
	 * @param col
	 *            列（0开始的）
	 * @return
	 */
	@Deprecated
	public static String getCellValue(Sheet st, int row, int col) {
		String value = st.getCell(col, row).getContents();
		return value == null ? "" : value;
	}

	/**
	 * 读excel表中单元格的内容（需要特殊处理的单元格）
	 * 
	 * @see Excel2007Utils.java
	 * @param <T>
	 * @param st
	 *            工作表
	 * @param row
	 *            行（1开始）
	 * @param col
	 *            列（0开始的）
	 * @param clazz
	 *            返回的类型
	 * @return
	 */
	@Deprecated
	public static <T> T getCellValue(Sheet st, int row, int col, Class<T> clazz) {
		if (Date.class.getName().equals(clazz.getName())) { // 由于日期格式比较特殊所以需要特殊处理
			CellType ct = st.getCell(col, row).getType();
			if (ct == CellType.DATE) {
				//jxl从excel获取时间类型的单元格（DateCell），是以GMT为标准，而不是系统当前时区（例如中国是GMT+8），所以需要通过时差（offset）修成成当前时区的时间
				Date date =  ((DateCell) st.getCell(col, row)).getDate();
				TimeZone tz = TimeZone.getDefault();
				long offset = tz.getOffset(date.getTime());
				date.setTime(date.getTime() - offset);
				return (T)date;
			} else {
				return (T) DateUtils.toDate(st.getCell(col, row).getContents()
						.trim());// 这里对内容中空格进行了处理，否则包含多余空格时就会转型失败
			}
		}
		return ConvertUtils
				.convertGt(st.getCell(col, row).getContents(), clazz);

	}
	
	/**
	 * 读excel表中单元格的内容（文本格式的）
	 * 
	 * @see Excel2007Utils.java
	 * @param st 工作表
	 * @param row 行（从1开始）
	 * @param col 列（从0开始）
	 * @return
	 * @throws Exception 
	 */
	@Deprecated
	public static String getCellValue(HttpServletRequest request, int sheetNum, int row, int col) 
								throws Exception {
		Sheet sheet = getSheet(request, sheetNum);
		String value = sheet.getCell(col, row).getContents();
		return (value == null ? "" : value);
	}
	
	/**
	 * 
	 * 得到一个Sheet(不上传)
	 * 
	 * @see Excel2007Utils.java
	 * @param request
	 * @param sheetNum 第几个Sheet(从0开始)
	 * @param fileName Excel文件名(不加上路径)
	 * @return
	 * @throws IOException 
	 * @throws BiffException 
	 * @throws FileUploadException 
	 */
	@Deprecated
	public static Sheet getSheet(String fileName, int sheetNum) 
								throws BiffException, IOException, FileUploadException {
		Workbook book = Workbook.getWorkbook(new FileInputStream(new File(fileName)));
		return book.getSheet(sheetNum);
	}
	
	/**
	 * 
	 * 得到所有Sheet(不上传)
	 * 
	 * @see Excel2007Utils.java
	 * @param request
	 * @param sheetNum 第几个Sheet(从0开始)
	 * @param fileName Excel文件名(不加上路径)
	 * @return
	 * @throws IOException 
	 * @throws BiffException 
	 * @throws FileUploadException 
	 */
	@Deprecated
	public static Sheet[] getSheet(String fileName) 
								throws BiffException, IOException, FileUploadException {
		Workbook book = Workbook.getWorkbook(new FileInputStream(new File(fileName)));
		return book.getSheets();
	}
	
	/**
	 * 
	 * 上传Excel并且得到一个Sheet
	 * 
	 * @see Excel2007Utils.java
	 * @param request
	 * @param sheetNum 第几个Sheet(从0开始)
	 * @return
	 * @throws IOException 
	 * @throws BiffException 
	 * @throws Exception 
	 * @throws FileUploadException 
	 */
	@Deprecated
	public static Sheet getSheet(HttpServletRequest request, int sheetNum) 
									throws Exception {
		List<FileItem> itemList = uploadFile(request);
		Workbook book = Workbook.getWorkbook(getInputStream(itemList));
		return book.getSheet(sheetNum);
	}
	
	/**
	 * 
	 * 上传Excel并且得到一个Sheet（和SpringMVC一起使用）
	 * 
	 * @see Excel2007Utils.java
	 * @param sheetNum 第几个Sheet(从0开始)
	 * @param is  输入流
	 * @return
	 * @throws IOException 
	 * @throws BiffException 
	 * @throws Exception 
	 * @throws FileUploadException 
	 */
	@Deprecated
	public static Sheet getSheet(int sheetNum, InputStream is) 
									throws Exception {
		Workbook book = Workbook.getWorkbook(is);
		return book.getSheet(sheetNum);
	}
	
	/**
	 * 
	 * 上传Excel文件并得到所有Sheet
	 * 
	 * @see Excel2007Utils.java
	 * @param request
	 * @param sheetNum 第几个Sheet(从0开始)
	 * @return
	 * @throws IOException 
	 * @throws BiffException 
	 * @throws Exception 
	 * @throws FileUploadException 
	 */
	@Deprecated
	public static Sheet[] getSheet(HttpServletRequest request) 
									throws Exception {
		List<FileItem> itemList = uploadFile(request);
		Workbook book = Workbook.getWorkbook(getInputStream(itemList));
		return book.getSheets();
	}

	/**
	 * 
	 * 得到文件项目
	 * 
	 * @param request
	 * @return List<FileItem>
	 * @throws Exception 
	 */
	public static List<FileItem> uploadFile(HttpServletRequest request) 
						throws Exception {
		boolean isMultipart = ServletFileUpload.isMultipartContent(request);
		if (!isMultipart) return null;
		FileItemFactory uploadFactory = new DiskFileItemFactory();
//		uploadFactory.setRepository(new File(getRepositoryDir(REPOSITORY_DIR)));
		ServletFileUpload fileUpload = new ServletFileUpload(uploadFactory);
		List<FileItem> items = fileUpload.parseRequest(request);
		if(items == null || items.isEmpty())return null;
		for(FileItem i : items) {
			// 如果不是表单域,则写入文件
			if(!i.isFormField()) {
				String fileName = i.getName();
				fileName = fileName.substring(fileName.lastIndexOf("\\") + 1);
				File dirs = new File(request.getSession().getServletContext().getRealPath("") + SUB_DIR);
				getRepositoryDir(request.getSession().getServletContext().getRealPath("") + SUB_DIR);
				File uploadFile = new File(dirs, fileName);
				i.write(uploadFile);
			}
		}
		return items;
	}

	/**
	 * 
	 * 得到文件的输入流
	 * 
	 * @param items
	 * @return
	 * @throws IOException
	 */
	public static InputStream getInputStream(List<FileItem> items)
			throws IOException {
		if (items == null) return null;
		InputStream is = null;
		for (FileItem fileItem : items) {
			if (!fileItem.isFormField()) {
				is = fileItem.getInputStream();
			}
		}
		return is;
	}

	/**
	 * 
	 * 如果指定路径不存在，则创建
	 * 
	 * @param dir
	 * @return
	 * @throws IOException 
	 * @throws IOException
	 */
	public static String getRepositoryDir(String dir) throws IOException {
		File file = new File(dir);
		while (!file.exists()) {
			file.mkdirs();
		}
		return dir;
	}
	
	/**
	 * 
	 * 下载文件
	 * 
	 * @param response
	 * @param file
	 */
	public static void download(HttpServletResponse response, File file) {
		ResponseDownloadUtils.download(response, file);
	}
	
	/**
	 * 
	 * 下载文件
	 * 
	 * @param response
	 * @param file
	 */
	public static void download(HttpServletResponse response, File file, boolean isFirefox) {
		ResponseDownloadUtils.download(response, file, isFirefox);
	}

    /**
     * excel导出
     * 
     * @param title
     *            标题
     * @param column
     *            列宽度（如果为null，默认高度为15）
     * @param header
     *            头部
     * @param content
     *            内容
     * @param response
     *            响应
     * @return 是否成功
     */
    public boolean downloadExcel(String title, Integer[] column,
            String[] header, List<Map<String, String>> content,
            HttpServletResponse response) {
        try {
        	String name = java.net.URLEncoder.encode(title, "UTF8");
            String filename = name
                    + new SimpleDateFormat("yyyyMMddhhmmss").format(new Date());
            filename = new String(filename.getBytes("UTF-8"), "ISO-8859-1");
            response.reset();
            response.setCharacterEncoding("utf-8");
            response.setContentType("application/msexcel");// 设置为下载application/x-download
            response.setHeader("Content-Disposition", "inline;filename=\""
                    + filename + ".xls\"");
            OutputStream os = response.getOutputStream();// 取得输出流
            // 提示下载
            WritableWorkbook wwb = Workbook.createWorkbook(os);
            // 创建excel工作表，指定名字和位置
            WritableSheet sheet = wwb.createSheet(title, 0);

            // 添加标题（行宽）
            for (int i = 0; i < header.length; i++) {
                sheet.addCell(new Label(i, 0, header[i]));
                // 设置excel列宽
                if (column != null) {
                    sheet.setColumnView(i, column[i]);
                } else {// 如果没有设置默认为宽度为50
                    sheet.setColumnView(i, 15);
                }
            }

            // 添加内容
            for (int i = 0; i < content.size(); i++) {
                for (int j = 0; j < content.get(i).size(); j++) {
                    sheet.addCell(new Label(j, i + 1, content.get(i).get(
                            header[j])
                            + ""));
                }
            }

            // 写入工作表
            wwb.write();
            wwb.close();
            os.close();
        } catch (IOException | WriteException e) {
            // TODO: handle exception
        }
        return true;
    }

    /**
     * excel导出
     * 
     * @param title
     *            标题
     * @param column
     *            列宽度（如果为null，默认高度为15）
     * @param header
     *            头部
     * @param content
     *            内容
     * @param response
     *            响应
     * @return 是否成功
     */
    public boolean downloadExcel(String title, Integer[] column,
            String[] header, List<Map<String, String>> content,
            HttpServletResponse response,HttpServletRequest request) {
        try {
            String filename = title
                    + new SimpleDateFormat("yyyyMMddhhmmss").format(new Date());
            
            if (request.getHeader("User-Agent").toUpperCase().indexOf("MSIE") > 0) {  
                filename = URLEncoder.encode(filename, "UTF-8");  
            } else {  
                filename = new String(filename.getBytes("UTF-8"), "ISO8859-1");  
            }  
            response.reset();
            response.setCharacterEncoding("utf-8");
            response.setContentType("application/msexcel");// 设置为下载application/x-download
            response.setHeader("Content-Disposition", "inline;filename=\""
                    + filename + ".xls\"");
            OutputStream os = response.getOutputStream();// 取得输出流
            // 提示下载
            WritableWorkbook wwb = Workbook.createWorkbook(os);
            // 创建excel工作表，指定名字和位置
            WritableSheet sheet = wwb.createSheet(title, 0);

            // 添加标题（行宽）
            for (int i = 0; i < header.length; i++) {
                sheet.addCell(new Label(i, 0, header[i]));
                // 设置excel列宽
                if (column != null) {
                    sheet.setColumnView(i, column[i]);
                } else {// 如果没有设置默认为宽度为50
                    sheet.setColumnView(i, 15);
                }
            }

            // 添加内容
            for (int i = 0; i < content.size(); i++) {
                for (int j = 0; j < content.get(i).size(); j++) {
                    sheet.addCell(new Label(j, i + 1, content.get(i).get(
                            header[j])
                            + ""));
                }
            }

            // 写入工作表
            wwb.write();
            wwb.close();
            os.close();
        } catch (IOException | WriteException e) {
            // TODO: handle exception
        }
        return true;
    }
}
