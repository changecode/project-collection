package com.bpm.framework.utils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.SocketException;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.log4j.Logger;

import com.bpm.framework.utils.file.FileUtils;


/**
 * FTP操作类
 * 
 * @author lixx
 * 
 */
public class FtpUtils {
	private static Logger log = Logger.getLogger(FtpUtils.class);

	/**
	 * 下载符合fileNameSubStr前缀文件文件.
	 * 
	 * @param client
	 *            FTP客户端
	 * @param fileNameSubStr
	 *            文件的前缀
	 * @param downFilePath
	 *            下载的本地地址
	 * @return
	 * @throws SocketException
	 * @throws IOException
	 */
	public static boolean downAllFile(FTPClient client, String fileNameSubStr,
			String downFilePath) throws SocketException, IOException {
		BufferedOutputStream buffOut = null;
		boolean result = false;
		try {
			String[] str = client.listNames();
			if (null != str) {
				for (String ff : str) {
					System.out.println(ff + "  " + fileNameSubStr + "  "
							+ ff.startsWith(fileNameSubStr));
					if (ff.startsWith(fileNameSubStr)) {
						File localFile = new File(downFilePath
								+ FileUtils.getFileSeparator() + ff);
						if (localFile.exists()) {
							boolean isSuccess = localFile.delete();
							if (!isSuccess) {
								throw new RuntimeException(
										"file or directory isn't successfully deleted");
							}
						}
						buffOut = new BufferedOutputStream(
								new FileOutputStream(localFile));

						result = client.retrieveFile(ff, buffOut);

						buffOut.flush();
						try {
							if (null != buffOut) {
								buffOut.close();
							}
						} catch (IOException e) {
							throw e;
						}
					}
				}
			}
		} catch (SocketException e) {
			throw e;
		} catch (IOException e) {
			throw e;
		} finally {
			try {
				if (null != buffOut) {
					buffOut.close();
				}
			} catch (IOException e) {
				throw e;
			}
		}
		return result;
	}

	/**
	 * 读ftp上指定的某个文件到本地
	 * 
	 * @param client
	 *            FTP客户端
	 * @param downFilePath
	 *            下载的本地地址
	 * @param ftpFileName
	 *            要下载的ftp文件名
	 * @return
	 * @throws SocketException
	 * @throws IOException
	 */
	public static boolean downOneFile(FTPClient client, String downFilePath,
			String ftpFileName) throws SocketException, IOException {
		BufferedOutputStream buffOut = null;
		boolean result = false;
		try {
			File localFile = new File(downFilePath
					+ FileUtils.getFileSeparator() + ftpFileName);
			if (localFile.exists()) {
				boolean isSuccess = localFile.delete();
				if (!isSuccess) {
					throw new RuntimeException(
							"file or directory isn't successfully deleted");
				}
			}
			buffOut = new BufferedOutputStream(new FileOutputStream(localFile));
			result = client.retrieveFile(ftpFileName, buffOut);

			buffOut.flush();
			buffOut.close();
		} catch (IOException e) {
			e.printStackTrace();
			throw e;
		} finally {
			try {
				if (null != buffOut) {
					buffOut.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
				log.error(e.toString());
			}
		}

		return result;
	}

	/**
	 * 上传符合regex正则表达式的文件.
	 * @param client FTP客户端
	 * @param regex 文件名称  支持正则表达式
	 * @param tempFilePath 要上传文件的存放路径
	 */
	public static void upLoadByReg(FTPClient client, String regex,
			String tempFilePath) throws SocketException, IOException {
		BufferedInputStream buffIn = null;
		File tempFile = new File(tempFilePath);
		File[] tempFiles = tempFile.listFiles();
		
		for (File file : tempFiles) {
			if (file.getName().matches(regex)) {
				if (file.getName().endsWith(".rar") || file.getName().endsWith(".zip")){
					client.setFileType(FTPClient.BINARY_FILE_TYPE);
				}
				buffIn = new BufferedInputStream(new FileInputStream(file));
				// 将上传文件存储到指定目录
				if (client.storeFile(file.getName(), buffIn)) {
					log.info("文件:" + tempFilePath + File.separator + file.getName() + " 上传成功!");
				} else {
					log.info("文件:" + tempFilePath + File.separator + file.getName() + " 上传失败!");
				}
				buffIn.close();
			}
		}
		client.disconnect();
	}
	/**
	 * @param client 
	 * @param file 上传的文件
	 * @param upLoadFilePath 上传文件保存目录
	 * @return boolean 是否成功
	 * @throws IOException 
	 */
	public static boolean upLoadFile(FTPClient client,File file,String upLoadFilePath) throws IOException{
		boolean flag=false;
		InputStream ins=null;
		try {
			ins =  new FileInputStream(file);
			if(upLoadFilePath.startsWith("/")){
				upLoadFilePath = upLoadFilePath.substring(1,upLoadFilePath.length());
			}
			//如果保存上传文件的 目录不存在 则创建该目录
			if(!client.changeWorkingDirectory(upLoadFilePath)){
				client.makeDirectory(upLoadFilePath);
				client.changeWorkingDirectory(upLoadFilePath);
			}
			client.setFileType(FTPClient.BINARY_FILE_TYPE);
			flag = client.storeFile(file.getName(),ins);
		} catch (FileNotFoundException e) {
			log.info("未找到上传文件。");
			e.printStackTrace();
			throw e;
		} catch (IOException e) {
			log.info("文件流创建失败。");
			e.printStackTrace();
			throw e;
		}finally{
			try {
				if(ins!=null){
					ins.close();  //关闭流
				}
				if(client.isConnected()){
					client.disconnect(); //断开ftp连接
				}
			} catch (IOException e) {
				e.printStackTrace();
				throw e;
			}
			
		}
		
		return flag;
	}
	/**
	 * 下载符合regex正则表达式的文件
	 * 
	 * @param client
	 *            FTP客户端
	 * @param fileNameSubStr
	 *            文件的前缀
	 * @param downFilePath
	 *            下载的本地地址
	 * @return
	 * @throws SocketException
	 * @throws IOException
	 */
	public static void downLoadByReg(FTPClient client, String regex,
			String downFilePath) throws SocketException, IOException {
		BufferedOutputStream buffOut = null;
		
		//如果下载路径不存在 ，则创建该路径
		File tempFile = new File(downFilePath);
		if(!tempFile.exists()){
			tempFile.mkdirs();
		}
		
		try {
			String[] str = client.listNames();
			if (null != str) {
				for (String ff : str) {
					if (ff.matches(regex)) {
						File localFile = new File(downFilePath + FileUtils.getFileSeparator() + ff);
						buffOut = new BufferedOutputStream(
								new FileOutputStream(localFile));
						if(client.retrieveFile(ff, buffOut)){
							log.info("文件:" + ff + " 下载成功!");
						}else{
							log.info("文件:" + ff + " 下载失败!");
						}
						buffOut.flush();
						try {
							if (null != buffOut) {
								buffOut.close();
							}
						} catch (IOException e) {
							throw e;
						}
					}
				}
			}
		} catch (SocketException e) {
			throw e;
		} catch (IOException e) {
			throw e;
		} finally {
			try {
				if (null != buffOut) {
					buffOut.close();
				}
			} catch (IOException e) {
				throw e;
			}
			client.disconnect();
		}
	}

	/**
	 * 登录FTP (被动模式连接ftp)
	 * 
	 * @param ftpUrl
	 * @param ftpUserName
	 * @param ftpPwd
	 * @return
	 * @throws SocketException
	 * @throws IOException
	 */
	public static FTPClient loginFtp(String ftpUrl, String ftpUserName,
			String ftpPwd) throws SocketException, IOException {
		FTPClient client = new FTPClient();
		// 让FTP支持中文 必须放在connect之前 否则无效
		client.setControlEncoding("UTF-8");
		
		client.connect(ftpUrl);
		boolean login = client.login(ftpUserName, ftpPwd);
		if (!login) {
			return null;
		}
		client.enterLocalPassiveMode(); // 更改获取模式
		return client;
	}
	
	/**
	 * 登录FTP (主动模式连接ftp)
	 * 
	 * @param ftpUrl
	 * @param ftpUserName
	 * @param ftpPwd
	 * @return
	 * @throws SocketException
	 * @throws IOException
	 */
	public static FTPClient loginFtpOnPortMode(String ftpUrl, String ftpUserName,
			String ftpPwd) throws SocketException, IOException {
		FTPClient client = new FTPClient();
		// 让FTP支持中文 必须放在connect之前 否则无效
		client.setControlEncoding("UTF-8");
		
		client.connect(ftpUrl);
		boolean login = client.login(ftpUserName, ftpPwd);
		if (!login) {
			return null;
		}
		//client.enterLocalPassiveMode(); // 更改获取模式
		return client;
	}
}
