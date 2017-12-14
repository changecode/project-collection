package com.bpm.framework.utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.Adler32;
import java.util.zip.CheckedOutputStream;
import java.util.zip.ZipInputStream;

import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipOutputStream;

import com.bpm.framework.SystemConst;
import com.bpm.framework.exception.FrameworkRuntimeException;

/**
 * 
 * zip 压缩文件
 * 
 * @author andyLee
 * @createDate 2015-11-04 20:20:00
 */
public class ZipUtils implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2129081066150913003L;

	public static final byte[] BUFFER = new byte[32 * 1024];
	public static final String EXT = ".zip";
	
	private ZipUtils() {}
	
	public static File zip(List<File> fileList) {
		return zip(fileList, SystemConst.ENCODING_GBK);
	}

	public static File zip(List<File> fileList, String encoding) {
		Assert.notEmpty(fileList);
		Assert.hasLength(encoding);
		ZipOutputStream zos = null;
		BufferedWriter writer = null;
		try {
			File zipFile = new File(fileList.get(0).getParent(), "process" + System.currentTimeMillis() + EXT);
			FileOutputStream fos = new FileOutputStream(zipFile);
			// 输出校验流,采用Adler32更快
			CheckedOutputStream csum = new CheckedOutputStream(fos, new Adler32());
			// 创建压缩输出流
			zos = new ZipOutputStream(csum);
			// 最大压缩率
			zos.setLevel(9);
			// 编码
			zos.setEncoding(encoding);
			writer = new BufferedWriter(new OutputStreamWriter(zos, encoding));
			// 设置Zip文件注释
			zos.setComment("Simple java Zipping");
			for (File file : fileList) {
				BufferedReader reader = null;
				try {
					// 针对单个文件建立读取流
					reader = new BufferedReader(
							new InputStreamReader(new FileInputStream(file), encoding));
					// ZipEntry ZIP 文件条目
					// putNextEntry 写入新条目，并定位到新条目开始处
					zos.putNextEntry(new ZipEntry(file.getName()));
					String line = null;
					while ((line = reader.readLine()) != null) {
						writer.write(line);
					}
					reader.close();
					writer.flush();
				} finally {
					if (null != reader) {
						reader.close();
						reader = null;
					}
				}
			}
			return zipFile;
		} catch (Exception e) {
			throw new FrameworkRuntimeException("压缩文件异常：", e);
		} finally {
			try {
				if (null != zos) {
					zos.closeEntry();
					zos = null;
				}
				if (null != writer) {
					writer.close();
					writer = null;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	public static List<File> unzip(File zipFile) {
		final List<File> fileList = new ArrayList<File>();
		InputStream is = null;
		ZipInputStream zipis = null;
        try {
            // 先指定压缩档的位置和档名，建立FileInputStream对象
            is = new FileInputStream(zipFile);
            // 将is传入ZipInputStream中
            zipis = new ZipInputStream(is);
            java.util.zip.ZipEntry ze = null;
            byte[] buffer = new byte[512];
            while ((ze = zipis.getNextEntry()) != null) {
            	String path = zipFile.getParent() + "/" + ze.getName();
                File zfile = new File(path);
                if (ze.isDirectory()) {
                    if (!zfile.exists()) {
                        zfile.mkdirs();
                    }
                    zipis.closeEntry();
                } else {
                	OutputStream os = null;
                	try {
	                    os = new FileOutputStream(zfile);
	                    int i = -1;
	                    while ((i = zipis.read(buffer)) != -1) {
	                        os.write(buffer, 0, i);
	                    }
	                    zipis.closeEntry();
	                    os.close();
	                    // add to list
	                    fileList.add(zfile);
                	} finally {
                		if(null != os) {
                			os.close();
                			os = null;
                		}
                	}
                }
            }
            is.close();
            zipis.close();
        } catch (Exception e) {
            throw new FrameworkRuntimeException("解压文件异常：", e);
        } finally {
        	try {
	        	if(null != is) {
					is.close();
	        		is = null;
	        	}
	        	if(null != zipis) {
	        		zipis.close();
	        		zipis = null;
	        	}
        	} catch (IOException e) {
        		e.printStackTrace();
        	}
        }
        return fileList;
	}

	public static void main(String[] args) throws Exception {
//		List<File> fileList = new ArrayList<>();
//		fileList.add(new File("C:/a/a.txt"));
//		fileList.add(new File("C:/a/b.txt"));
//		fileList.add(new File("C:/a/c.txt"));
//		zip(fileList);
		File zipFile = new File("C:/a/process1447677797479.zip");
		List<File> file = unzip(zipFile);
		for(File f : file) {
			System.out.println(f.getPath());
		}
	}
}
