package com.bpm.framework.utils.file;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.apache.log4j.Logger;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import com.bpm.framework.utils.Assert;
import com.bpm.framework.utils.StringUtils;
import com.bpm.framework.utils.socket.NetUtils;

/**
 * 
 * 
 * @author lixx
 * @since 1.0
 */
public class FileUtils {
	
	private static final String FILEPATH = "log4j.xml";

	public static void createDir(String dir) throws IOException {
		File file = new File(dir);
		while (!file.exists()) {
			file.mkdirs();
		}
	}

	/**
	 * 获取系统对应的路径分隔符
	 * 
	 * @return
	 */
	public static String getFileSeparator() {
		return File.separator;
	}

	/**
	 * 获取文件路径方法
	 * 
	 * @param isSeparatorStart
	 *            路径是否以分隔符开始，true表示以分隔符开始
	 * @param path
	 * @return
	 */
	public static String getFilePath(boolean isSeparatorStart, String... path) {
		StringBuffer paths = new StringBuffer();
		if (isSeparatorStart) {
			paths.append(getFileSeparator());
		}
		for (String temp : path) {
			paths.append(temp);
			paths.append(getFileSeparator());
		}
		return paths.toString();
	}

	/**
	 * 获取文件路径方法，该方法获取的路径是不以分隔符开始的
	 * 
	 * @param path
	 * @return
	 */
	public static String getFilePath(String... path) {
		return getFilePath(false, path);
	}

	public static String getFileName(String path) {
		Assert.hasLength(path);
		int index = path.lastIndexOf("/");
		if (index < 0) {
			index = path.lastIndexOf("\\");
		}
		return path.substring(index + 1);
	}

	/**
	 * 从classpath获取文件
	 * 
	 * @param pattern
	 *            目前支持一个*的通配，例如*.xml,app-*,app-*.xml等，多个*的通配不支持
	 * @return
	 */
	public static File[] getFilesFromClasspath(String pattern) {
		return getFilesFromClasspath("/", pattern);
	}

	public static File[] getFilesFromClasspath(String path, String pattern) {
		Resource r = new ClassPathResource(path);
		File file;
		try {
			file = r.getFile();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

		File[] fileArray = new File[0];
		if (file == null) {
			return fileArray;
		}

		File[] files = file.listFiles();
		if (files == null) {
			return fileArray;
		}

		String[] patterns = pattern.split("\\*");
		String prefix = patterns.length < 1 ? "" : patterns[0];
		String suffix = patterns.length < 2 ? "" : patterns[1];

		List<File> tempList = new ArrayList<File>();
		for (int i = 0; i < files.length; i++) {
			String fileName = files[i].getName();
			if (StringUtils.hasLength(fileName)) {
				if (fileName.startsWith(prefix) && fileName.endsWith(suffix)) {
					tempList.add(files[i]);
				}
			}
		}
		return tempList.toArray(fileArray);
	}

	public static JarEntry[] getFilesFromJar(String jarFilePattern,
			String fileNamePattern) {
		try {
			List<JarEntry> flist = new ArrayList<JarEntry>();
			// 为了兼容TongWeb服务器，必须指定FILEPATH，否则找不到WEB-INF目录
			// 必须保证FILEPATH文件在classpath下面
			URL parentUrl=Thread.currentThread().getContextClassLoader().getResource(FILEPATH);
			File file=new File(parentUrl.getFile());
			File libPath=new File(URLDecoder.decode(file.getParentFile().getParent(), NetUtils.ENCODING) + FileUtils.getFileSeparator()+"lib");
			File[] libFiles = libPath.listFiles();
			for (File f : libFiles) {
				URL url=null;
				try {
					url = f.toURI().toURL();
				} catch (Exception e1) {
					throw new RuntimeException(e1);
				}
				String urlstr = url.toString();
				String name = getFileName(URLDecoder.decode(urlstr, NetUtils.ENCODING));
				
				if (!"file".equals(url.getProtocol()) || !name.endsWith(".jar")
						|| !isWellFormat(name, jarFilePattern)) {
					continue;
				}
	
				try {
					URI entryURI = url.toURI();
					Logger.getLogger(FileUtils.class).info("jarFile:" + URLDecoder.decode(entryURI.toString(), NetUtils.ENCODING).substring(5));
					JarFile jarFile = new JarFile(URLDecoder.decode(entryURI.toString(), NetUtils.ENCODING).substring(5));//注意路径在windows和unix上兼容的问题
					Enumeration<JarEntry> jes = jarFile.entries();
					while (jes.hasMoreElements()) {
						JarEntry je = jes.nextElement();
						String fileName = getFileName(je.getName());
						if (!je.isDirectory()
								&& isWellFormat(fileName, fileNamePattern)) {
							flist.add(je);
						}
					}
				} catch (Exception e) {
					throw new RuntimeException(e);
				}
			}
			return flist.toArray(new JarEntry[] {});
		} catch(Exception e) {
			throw new RuntimeException(e);
		}
	}

	public static boolean isWellFormat(String srouce, String pattern) {
		String[] patterns = pattern.split("\\*");
		String prefix = patterns.length < 1 ? "" : patterns[0];
		String suffix = patterns.length < 2 ? "" : patterns[1];
		if (srouce.startsWith(prefix) && srouce.endsWith(suffix)) {
			return true;
		} else {
			return false;
		}
	}

	public static void main(String... args) {

		System.out.println(File.separator);

		File[] files = getFilesFromClasspath("/", "JbpmTemp*.class");
		for (File f : files) {
			System.out.println(f.getName());
		}

		JarEntry[] jfiles = getFilesFromJar("*", "*.xml");
		for (JarEntry f : jfiles) {
			System.out.println(f.getName());
		}
	}

}
