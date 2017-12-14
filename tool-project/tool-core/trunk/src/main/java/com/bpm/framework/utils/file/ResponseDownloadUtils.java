package com.bpm.framework.utils.file;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Collection;

import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.bpm.framework.SystemConst;

/**
 * 
 * 文件下载工具类
 * 
 * @author lixx
 * @createDate 2014-05-29 10:06:00
 */
public class ResponseDownloadUtils {

	protected static Logger log = Logger.getLogger(ResponseDownloadUtils.class);

	public static void download(HttpServletResponse response, String title, String[] columnTitles,
			Collection<?> data) {
		File f = ExcelUtils.simpleCreate(title, columnTitles, data);
		download(response, f);
	}
	
	public static void download(HttpServletResponse response, String path) {
		File f = new File(path);
		download(response, f);
	}
	
	
	public static void download(HttpServletResponse response, File file, boolean isFireFox) {

		BufferedInputStream br = null;
		OutputStream out = null;
		try {
			File f = file;
			if (!f.exists()) {
				response.sendError(404, "File not found!");
				return;
			}
			br = new BufferedInputStream(new FileInputStream(f));
			byte[] buf = new byte[1024];
			int len = 0;

			response.reset();

//			String fileName = new String(f.getName().getBytes(), "ISO8859-1");
			String fileTrueName = URLDecoder.decode(f.getName().replaceAll(" ", ""), SystemConst.ENCODING_UTF8);
			fileTrueName=fileTrueName.replace("+", "%20");
			response.setContentType("applicatoin/octet-stream");// application/x-msdownload
			if(isFireFox){
				response.addHeader("Content-Disposition", "attachment;filename="+ new String(fileTrueName.getBytes(SystemConst.ENCODING_UTF8),"ISO-8859-1"));
			}else{
				response.setHeader("Content-Disposition", "attachment; filename=" + URLEncoder.encode(fileTrueName, SystemConst.ENCODING_UTF8));
			}
			
			out = response.getOutputStream();
			while ((len = br.read(buf)) > 0)
				out.write(buf, 0, len);
			br.close();
			out.close();

		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			try {
				if (br != null)
					br.close();
				if (out != null)
					out.close();
			} catch (IOException e) {
				log.error("", e);
			}

		}
	}
	
	
	public static void download(HttpServletResponse response, File file) {

		BufferedInputStream br = null;
		OutputStream out = null;
		try {
			File f = file;
			if (!f.exists()) {
				response.sendError(404, "File not found!");
				return;
			}
			br = new BufferedInputStream(new FileInputStream(f));
			byte[] buf = new byte[1024];
			int len = 0;

			response.reset();

//			String fileName = new String(f.getName().getBytes(), "ISO8859-1");
			String fileTrueName = URLDecoder.decode(f.getName(), SystemConst.ENCODING_UTF8);
			fileTrueName=fileTrueName.replace("+", "%20");
			response.setContentType("applicatoin/octet-stream");// application/x-msdownload
			response.setHeader("Content-Disposition", "attachment; filename="
					+ URLEncoder.encode(fileTrueName, SystemConst.ENCODING_UTF8));
			out = response.getOutputStream();
			while ((len = br.read(buf)) > 0)
				out.write(buf, 0, len);
			br.close();
			out.close();

		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			try {
				if (br != null)
					br.close();
				if (out != null)
					out.close();
			} catch (IOException e) {
				log.error("", e);
			}

		}
	}
	
	public static void download(HttpServletResponse response, File file,String fileName) {
		BufferedInputStream br = null;
		OutputStream out = null;
		try {
			File f = file;
			if (!f.exists()) {
				response.sendError(404, "File not found!");
				return;
			}
			br = new BufferedInputStream(new FileInputStream(f));
			byte[] buf = new byte[1024];
			int len = 0;

			response.reset();
			
			
//			String fileTrueName = new String(fileName.getBytes(), "ISO8859-1");
			String fileTrueName = URLDecoder.decode(fileName, SystemConst.ENCODING_UTF8);
			fileTrueName=fileTrueName.replace("+", "%20");
			response.setContentType("applicatoin/octet-stream");// application/x-msdownload
			response.setHeader("Content-Disposition", "attachment; filename="
					+ URLEncoder.encode(fileTrueName, SystemConst.ENCODING_UTF8));
			
			out = response.getOutputStream();
			while ((len = br.read(buf)) > 0)
				out.write(buf, 0, len);
			br.close();
			out.close();

		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			try {
				if (br != null)
					br.close();
				if (out != null)
					out.close();
			} catch (IOException e) {
				log.error("", e);
			}
		}
	}
	
	public static void download(HttpServletResponse response, File file,String fileName,boolean isFireFox) {
		BufferedInputStream br = null;
		OutputStream out = null;
		try {
			File f = file;
			if (!f.exists()) {
				response.sendError(404, "File not found!");
				return;
			}
			br = new BufferedInputStream(new FileInputStream(f));
			byte[] buf = new byte[1024];
			int len = 0;

			response.reset();
			
			
//			String fileTrueName = new String(fileName.getBytes(), "ISO8859-1");
			String fileTrueName = URLDecoder.decode(fileName, SystemConst.ENCODING_UTF8);
			fileTrueName=fileTrueName.replace("+", "%20");
			response.setContentType("applicatoin/octet-stream");// application/x-msdownload
			
			if(isFireFox){
				response.addHeader("Content-Disposition", "attachment;filename="+ new String(fileTrueName.getBytes(SystemConst.ENCODING_UTF8),"ISO-8859-1"));
			}else{
				response.setHeader("Content-Disposition", "attachment; filename=" + URLEncoder.encode(fileTrueName, SystemConst.ENCODING_UTF8));
			}
			
			out = response.getOutputStream();
			while ((len = br.read(buf)) > 0)
				out.write(buf, 0, len);
			br.close();
			out.close();

		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			try {
				if (br != null)
					br.close();
				if (out != null)
					out.close();
			} catch (IOException e) {
				log.error("", e);
			}
		}
	}
	
	public static void showImg(HttpServletResponse response, File file) {

		BufferedInputStream br = null;
		OutputStream out = null;
		try {
			File f = file;
			if (!f.exists()) {
				response.sendError(404, "File not found!");
				return;
			}
			br = new BufferedInputStream(new FileInputStream(f));
			byte[] buf = new byte[1024];
			int len = 0;

			response.reset();

//			String fileName = new String(f.getName().getBytes(), "ISO8859-1");
			String fileTrueName = URLDecoder.decode(f.getName(), SystemConst.ENCODING_UTF8);
			fileTrueName=fileTrueName.replace("+", "%20");
			response.setContentType("image/*");
			response.setHeader("Content-Disposition", "attachment; filename="
					+ URLEncoder.encode(fileTrueName, SystemConst.ENCODING_UTF8));

			out = response.getOutputStream();
			while ((len = br.read(buf)) > 0)
				out.write(buf, 0, len);
			br.close();
			out.close();

		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			try {
				if (br != null)
					br.close();
				if (out != null)
					out.close();
			} catch (IOException e) {
				log.error("", e);
			}

		}
	}
	
	
}
