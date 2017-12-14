package com.bpm.framework.utils.file;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.log4j.Logger;

import com.bpm.framework.console.Application;
import com.bpm.framework.utils.StringUtils;

/**
 * 
 * 文件上传工具类
 * 
 * @author lixx
 * @createDate 2014-05-29 10:06:00
 */
public class RequestUploadUtils {
	private static Logger log = Logger.getLogger(RequestUploadUtils.class);
	private static final String DEFAULT_UPLOAD_CALL_BACK = "upload_call_back";

	public static final String DEFAULT_UPLOAD_DIR = Application.getInstance().getUploadFileDirectory();

	public static final String REPOSITORY_DIR = Application.getInstance().getTempFileDirectory();

	public static void upload(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		upload(request, response, DEFAULT_UPLOAD_DIR, DEFAULT_UPLOAD_CALL_BACK);
	}

	public static void upload(HttpServletRequest request,
			HttpServletResponse response, String uploadDir) throws IOException {
		upload(request, response, uploadDir, DEFAULT_UPLOAD_CALL_BACK);
	}

	public static void upload(HttpServletRequest request,
			HttpServletResponse response, String uploadDir,
			String callbackMethodName) throws IOException {
		try {
			boolean isMultipart = ServletFileUpload.isMultipartContent(request);
			if (!isMultipart) {
				throw new Exception("");
			} else {
				DiskFileItemFactory factory = new DiskFileItemFactory();
				factory
						.setRepository(new File(
								getRepositoryDir(REPOSITORY_DIR)));
				ServletFileUpload upload = new ServletFileUpload(factory);

				List items = upload.parseRequest(request);
				Iterator iter = items.iterator();
				while (iter.hasNext()) {
					FileItem item = (FileItem) iter.next();
					if (item.isFormField()) {
						// processFormField
					} else {
						String fileName = item.getName();
						int i2 = fileName.lastIndexOf("\\");
						if (i2 > -1)
							fileName = fileName.substring(i2 + 1);
						File dirs = new File(uploadDir);
						FileUtils.createDir(uploadDir);

						File uploadedFile = new File(dirs, fileName);
						item.write(uploadedFile);
					}
				}
			}
		} catch (Exception ex) {
			response.getWriter().write(
					"<script type='text/javascript'>parent."
							+ callbackMethodName + "(\"failure\");</script>");
			return;
		}
		response.getWriter().write(
				"<script type='text/javascript'>parent." + callbackMethodName
						+ "(\"success\");</script>");
	}

	@SuppressWarnings("rawtypes")
	public static File uploadFile(HttpServletRequest request, String uploadDir)
			throws Exception {
		File uploadedFile = null;
		try {
			boolean isMultipart = ServletFileUpload.isMultipartContent(request);
			if (!isMultipart) {
				return null;
			} else {
				DiskFileItemFactory factory = new DiskFileItemFactory();
				factory
						.setRepository(new File(
								getRepositoryDir(REPOSITORY_DIR)));
				ServletFileUpload upload = new ServletFileUpload(factory);

				List items = upload.parseRequest(request);
				Iterator iter = items.iterator();

				while (iter.hasNext()) {
					FileItem item = (FileItem) iter.next();
					if (item.isFormField()) {
					} else {
						String fileName = item.getName();
						int i2 = fileName.lastIndexOf("\\");
						if (i2 > -1)
							fileName = fileName.substring(i2 + 1);
						File dirs = new File(uploadDir);
						FileUtils.createDir(uploadDir);
						uploadedFile = new File(dirs, fileName);
						item.write(uploadedFile);
					}
				}
			}
		} catch (IOException ex) {
			throw ex;
		}
		return uploadedFile;
	}

	/***
	 * 上传多个文件
	 * @param items
	 * @param uploadDir
	 * @param fileNameSuffix
	 * @param nameMap 用来存放文件名
	 * @return
	 * @throws IOException
	 */
	public static List<String> uploadFilesRemoveExtension(List<FileItem> items,
			String uploadDir, String fileNameSuffix,Map<String,String> nameMap) throws Exception {
		List<String> strList = new ArrayList<String>();
		Iterator iter = items.iterator();
		while (iter.hasNext()) {
			FileItem item = (FileItem) iter.next();
			if (item.isFormField()) {
				// processFormField
			} else {
				String fileName = item.getName();
				if (!StringUtils.hasLength(fileName)) {
					continue;
				}
				int i2 = fileName.lastIndexOf("\\");
				if (i2 > -1)
					fileName = fileName.substring(i2 + 1);
				File dirs = new File(uploadDir);
				FileUtils.createDir(uploadDir);

				int index = fileName.lastIndexOf(".");
				String trueName = "";
				String fileExtension = "";
				if (index > 0) {
					trueName = fileName.substring(0, index);
					fileExtension = fileName.substring(index);
				} else
					trueName = fileName;
				
				//String newFileName =  trueName + "_" + fileNameSuffix;
				Thread.sleep(1) ;
				String newFileName =  fileNameSuffix + "_" + System.currentTimeMillis();
				File uploadedFile = new File(dirs, newFileName);

				newFileName = newFileName + fileExtension;

				try {
					item.write(uploadedFile);
				} catch (Exception e) {
					log.error("", e);
				}
				strList.add(newFileName);
				nameMap.put(newFileName,trueName);
			}
		}
		return strList;
	}

	private static String getRepositoryDir(String dir) throws IOException {
		FileUtils.createDir(dir);
		return dir;
	}

	public static String makeUpScript(String methodName, String param) {
		StringBuffer sb = new StringBuffer("<script type='text/javascript'>");
		sb.append(methodName);
		sb.append("('");
		sb.append(param);
		sb.append("')");
		sb.append(";</script>");
		return sb.toString();
	}
	
	public static Map<String,Object> upload(HttpServletRequest req,String repositoryDir) {
		Map<String, Object> map = new HashMap<String, Object>();
		boolean isMultipart = ServletFileUpload.isMultipartContent(req);
		if (! isMultipart) {
			return null;
		} else {
			DiskFileItemFactory factory = new DiskFileItemFactory();
			File dir = new File(repositoryDir);
			if( ! dir.exists() ) {
				dir.mkdir();
			}
			factory.setRepository(dir);
			ServletFileUpload upload = new ServletFileUpload(factory);
			List items;
			try {
				items = upload.parseRequest(req);
				Iterator iter = items.iterator();
				while (iter.hasNext()) {
					FileItem item = (FileItem) iter.next();
					if (item.isFormField()) {
						String temp = item.getString();
						// 处理乱码问题
						try {
							map.put(item.getFieldName(), new String(temp
									.getBytes("ISO-8859-1"), "utf-8"));
						} catch (UnsupportedEncodingException e) {
							log.error("", e);
						}
					} else {
						map.put("file", item);
					}
				}
			} catch (FileUploadException e1) {
				log.error("", e1);
			}
		}
		return map;
	}
}
