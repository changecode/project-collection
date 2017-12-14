package com.bpm.framework.utils.log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.apache.log4j.FileAppender;
import org.apache.log4j.Layout;
import org.apache.log4j.helpers.LogLog;
import org.apache.log4j.spi.LoggingEvent;

/**
 * 切换日志文件时将归档日志压缩存放<br/>
 * 参数<br/>
 * backupDir -- 归档日志存放目录，默认与源文件在同一个目录<br/>
 * deleteAfterZip -- 压缩后是否删除源文件，true删除，默认是true<br/>
 * maxBackupIndex -- 保留的文件个数，默认是0，即全部保留<br/>
 * 
 * @author lixx
 */

public class DailyRollingZipFileAppender extends FileAppender {
	/**
	 * 备份存放目录
	 */
	private String backupDir = null;
	/**
	 * 备份后是否删除源文件
	 */
	private boolean deleteAfterZip = true;
	/**
	 * 最大备份文件数
	 */
	private int maxBackupIndex = 0;
	// The code assumes that the following constants are in a increasing
	// sequence.
	static final int TOP_OF_TROUBLE = -1;
	static final int TOP_OF_MINUTE = 0;
	static final int TOP_OF_HOUR = 1;
	static final int HALF_DAY = 2;
	static final int TOP_OF_DAY = 3;
	static final int TOP_OF_WEEK = 4;
	static final int TOP_OF_MONTH = 5;

	/**
	 * The date pattern. By default, the pattern is set to "'.'yyyy-MM-dd"
	 * meaning daily rollover.
	 */
	private String datePattern = "'.'yyyy-MM-dd";

	/**
	 * The log file will be renamed to the value of the scheduledFilename
	 * variable when the next interval is entered. For example, if the rollover
	 * period is one hour, the log file will be renamed to the value of
	 * "scheduledFilename" at the beginning of the next hour.
	 * 
	 * The precise time when a rollover occurs depends on logging activity.
	 */
	private String scheduledFilename;

	/**
	 * The next time we estimate a rollover should occur.
	 */
	private long nextCheck = System.currentTimeMillis() - 1;

	Date now = new Date();

	SimpleDateFormat sdf;

	RollingCalendar rc = new RollingCalendar();

	// The gmtTimeZone is used only in computeCheckPeriod() method.
	static final TimeZone gmtTimeZone = TimeZone.getTimeZone("GMT");

	/**
	 * The default constructor does nothing.
	 */
	public DailyRollingZipFileAppender() {
	}

	/**
	 * Instantiate a <code>DailyRollingFileAppender</code> and open the file
	 * designated by <code>filename</code>. The opened filename will become the
	 * ouput destination for this appender.
	 */
	public DailyRollingZipFileAppender(Layout layout, String filename,
			String datePattern) throws IOException {
		super(layout, filename, true);
		this.datePattern = datePattern;
		activateOptions();
	}

	/**
	 * The <b>DatePattern</b> takes a string in the same format as expected by
	 * {@link SimpleDateFormat}. This options determines the rollover schedule.
	 */
	public void setDatePattern(String pattern) {
		datePattern = pattern;
	}

	/** Returns the value of the <b>DatePattern</b> option. */
	public String getDatePattern() {
		return datePattern;
	}

	/**
	 * 设置备份目录
	 * 
	 * @param dir
	 */
	public void setBackupDir(String dir) {
		backupDir = dir;
	}

	public String getBackupDir() {
		return backupDir;
	}

	public boolean isDeleteAfterZip() {
		return deleteAfterZip;
	}

	/**
	 * 设置开关：备份后是否删除源文件
	 * 
	 * @param deleteAfterBackup
	 *            true－删除 false－不删除
	 */
	public void setDeleteAfterZip(boolean deleteAfterBackup) {
		this.deleteAfterZip = deleteAfterBackup;
	}

	/**
	 * 设置最大备份文件数
	 * 
	 * @param maxBackupIndex
	 *            设为0则全部保留
	 */
	public void setMaxBackupIndex(int maxBackupIndex) {
		this.maxBackupIndex = maxBackupIndex;
	}

	public void activateOptions() {
		super.activateOptions();
		if (datePattern != null && fileName != null) {
			now.setTime(System.currentTimeMillis());
			sdf = new SimpleDateFormat(datePattern);
			int type = computeCheckPeriod();
			printPeriodicity(type);
			rc.setType(type);
			File file = new File(fileName);
			scheduledFilename = fileName
					+ sdf.format(new Date(file.lastModified()));

		} else {
			LogLog
					.error("Either File or DatePattern options are not set for appender ["
							+ name + "].");
		}
	}

	void printPeriodicity(int type) {
		switch (type) {
		case TOP_OF_MINUTE:
			LogLog.debug("Appender [" + name + "] to be rolled every minute.");
			break;
		case TOP_OF_HOUR:
			LogLog.debug("Appender [" + name
					+ "] to be rolled on top of every hour.");
			break;
		case HALF_DAY:
			LogLog.debug("Appender [" + name
					+ "] to be rolled at midday and midnight.");
			break;
		case TOP_OF_DAY:
			LogLog.debug("Appender [" + name + "] to be rolled at midnight.");
			break;
		case TOP_OF_WEEK:
			LogLog.debug("Appender [" + name
					+ "] to be rolled at start of week.");
			break;
		case TOP_OF_MONTH:
			LogLog.debug("Appender [" + name
					+ "] to be rolled at start of every month.");
			break;
		default:
			LogLog.warn("Unknown periodicity for appender [" + name + "].");
		}
	}

	// This method computes the roll over period by looping over the
	// periods, starting with the shortest, and stopping when the r0 is
	// different from from r1, where r0 is the epoch formatted according
	// the datePattern (supplied by the user) and r1 is the
	// epoch+nextMillis(i) formatted according to datePattern. All date
	// formatting is done in GMT and not local format because the test
	// logic is based on comparisons relative to 1970-01-01 00:00:00
	// GMT (the epoch).

	int computeCheckPeriod() {
		RollingCalendar rollingCalendar = new RollingCalendar(gmtTimeZone,
				Locale.ENGLISH);
		// set sate to 1970-01-01 00:00:00 GMT
		Date epoch = new Date(0);
		if (datePattern != null) {
			for (int i = TOP_OF_MINUTE; i <= TOP_OF_MONTH; i++) {
				SimpleDateFormat simpleDateFormat = new SimpleDateFormat(
						datePattern);
				simpleDateFormat.setTimeZone(gmtTimeZone); // do all date
				// formatting in GMT
				String r0 = simpleDateFormat.format(epoch);
				rollingCalendar.setType(i);
				Date next = new Date(rollingCalendar.getNextCheckMillis(epoch));
				String r1 = simpleDateFormat.format(next);
				// System.out.println("Type = "+i+", r0 = "+r0+", r1 = "+r1);
				if (r0 != null && r1 != null && !r0.equals(r1)) {
					return i;
				}
			}
		}
		return TOP_OF_TROUBLE; // Deliberately head for trouble...
	}

	/**
	 * Rollover the current file to a new file.
	 */
	void rollOver() throws IOException {

		/* Compute filename, but only if datePattern is specified */
		if (datePattern == null) {
			errorHandler.error("Missing DatePattern option in rollOver().");
			return;
		}

		String datedFilename = fileName + sdf.format(now);
		// It is too early to roll over because we are still within the
		// bounds of the current interval. Rollover will occur once the
		// next interval is reached.
		if (scheduledFilename.equals(datedFilename)) {
			return;
		}

		// close current file, and rename it to datedFilename
		this.closeFile();

		File target = new File(scheduledFilename);
		if (target.exists()) {
			target.delete();
		}

		File file = new File(fileName);
		boolean result = file.renameTo(target);
		if (result) {
			LogLog.debug(fileName + " -> " + scheduledFilename);
			// 启动线程压缩文件
			new ZipFileThread(scheduledFilename, backupDir, deleteAfterZip,
					maxBackupIndex).start();
		} else {
			LogLog.error("Failed to rename [" + fileName + "] to ["
					+ scheduledFilename + "].");
		}

		try {
			// This will also close the file. This is OK since multiple
			// close operations are safe.
			this.setFile(fileName, false, this.bufferedIO, this.bufferSize);
		} catch (IOException e) {
			errorHandler.error("setFile(" + fileName + ", false) call failed.");
		}
		scheduledFilename = datedFilename;
	}

	/**
	 * This method differentiates DailyRollingFileAppender from its super class.
	 * 
	 * <p>
	 * Before actually logging, this method will check whether it is time to do
	 * a rollover. If it is, it will schedule the next rollover time and then
	 * rollover.
	 * */
	protected void subAppend(LoggingEvent event) {
		long n = System.currentTimeMillis();
		if (n >= nextCheck) {
			now.setTime(n);
			nextCheck = rc.getNextCheckMillis(now);
			try {
				rollOver();
			} catch (IOException ioe) {
				LogLog.error("rollOver() failed.", ioe);
			}
		}
		super.subAppend(event);
	}
}

/**
 * RollingCalendar is a helper class to DailyRollingFileAppender. Given a
 * periodicity type and the current time, it computes the start of the next
 * interval.
 * */
class RollingCalendar extends GregorianCalendar {
	private static final long serialVersionUID = -3560331770601814177L;

	int type = DailyRollingZipFileAppender.TOP_OF_TROUBLE;

	RollingCalendar() {
		super();
	}

	RollingCalendar(TimeZone tz, Locale locale) {
		super(tz, locale);
	}

	void setType(int type) {
		this.type = type;
	}

	public long getNextCheckMillis(Date now) {
		return getNextCheckDate(now).getTime();
	}

	public Date getNextCheckDate(Date now) {
		this.setTime(now);

		switch (type) {
		case DailyRollingZipFileAppender.TOP_OF_MINUTE:
			this.set(Calendar.SECOND, 0);
			this.set(Calendar.MILLISECOND, 0);
			this.add(Calendar.MINUTE, 1);
			break;
		case DailyRollingZipFileAppender.TOP_OF_HOUR:
			this.set(Calendar.MINUTE, 0);
			this.set(Calendar.SECOND, 0);
			this.set(Calendar.MILLISECOND, 0);
			this.add(Calendar.HOUR_OF_DAY, 1);
			break;
		case DailyRollingZipFileAppender.HALF_DAY:
			this.set(Calendar.MINUTE, 0);
			this.set(Calendar.SECOND, 0);
			this.set(Calendar.MILLISECOND, 0);
			int hour = get(Calendar.HOUR_OF_DAY);
			if (hour < 12) {
				this.set(Calendar.HOUR_OF_DAY, 12);
			} else {
				this.set(Calendar.HOUR_OF_DAY, 0);
				this.add(Calendar.DAY_OF_MONTH, 1);
			}
			break;
		case DailyRollingZipFileAppender.TOP_OF_DAY:
			this.set(Calendar.HOUR_OF_DAY, 0);
			this.set(Calendar.MINUTE, 0);
			this.set(Calendar.SECOND, 0);
			this.set(Calendar.MILLISECOND, 0);
			this.add(Calendar.DATE, 1);
			break;
		case DailyRollingZipFileAppender.TOP_OF_WEEK:
			this.set(Calendar.DAY_OF_WEEK, getFirstDayOfWeek());
			this.set(Calendar.HOUR_OF_DAY, 0);
			this.set(Calendar.MINUTE, 0);
			this.set(Calendar.SECOND, 0);
			this.set(Calendar.MILLISECOND, 0);
			this.add(Calendar.WEEK_OF_YEAR, 1);
			break;
		case DailyRollingZipFileAppender.TOP_OF_MONTH:
			this.set(Calendar.DATE, 1);
			this.set(Calendar.HOUR_OF_DAY, 0);
			this.set(Calendar.MINUTE, 0);
			this.set(Calendar.SECOND, 0);
			this.set(Calendar.MILLISECOND, 0);
			this.add(Calendar.MONTH, 1);
			break;
		default:
			throw new IllegalStateException("Unknown periodicity type.");
		}
		return getTime();
	}
}

/**
 * 压缩文件的线程
 * 
 * @author fanjj
 * 
 */
class ZipFileThread extends Thread {
	private File file = null;
	private String parentDir = null;
	private boolean deleteAfterBackup = true;
	private int maxBackupIndex = 0;

	public ZipFileThread(String file, String parent, boolean deleteAfterBackup,
			int maxBackupIndex) {
		this.file = new File(file);
		parentDir = (parent == null) ? this.file.getParent() : parent;
		this.deleteAfterBackup = deleteAfterBackup;
		this.maxBackupIndex = maxBackupIndex;
	}

	public void run() {
		byte[] buf = new byte[32 * 1024];
		ZipOutputStream zos = null;
		FileInputStream fis = null;

		try {
			fis = new FileInputStream(file);
			zos = new ZipOutputStream(new FileOutputStream(new File(parentDir,
					file.getName() + ".zip")));
			// 设置为最大压缩率
			zos.setLevel(9);
			zos.putNextEntry(new ZipEntry(file.getName()));
			LogLog.debug(file.getName() + " -> " + file.getName() + ".zip");
			int len = -1;
			while ((len = fis.read(buf)) > 0) {
				zos.write(buf, 0, len);
			}
			zos.closeEntry();
			fis.close();
			LogLog.debug(file.getName() + " -> " + file.getName()
					+ ".zip successful!");
			// 判断是否要删除源文件
			if (deleteAfterBackup)
				LogLog.debug("delete " + file.getName() + ".zip "
						+ (file.delete() ? "successful!" : "fail!"));

			// 判断是否要删除超出最大备份数的文件
			if (maxBackupIndex > 0) {
				// 取文件名相同的所有文件列表
				File[] backupFiles = file.getParentFile().listFiles(
						new FilenameFilter() {
							String namePattern = file.getName().substring(0,
									file.getName().lastIndexOf('.'));

							public boolean accept(File dir, String name) {
								return name.length() > namePattern.length()
										&& name
												.substring(
														0,
														file.getName()
																.lastIndexOf(
																		'.'))
												.equals(namePattern);
							}
						});
				if (backupFiles.length > maxBackupIndex) {
					// 需要删除多余文件
					// 不是很有必要，保险起见排个序
					Arrays.sort(backupFiles);
					for (int i = 0; i < backupFiles.length - maxBackupIndex; i++)
						backupFiles[i].delete();
				}
			}
		} catch (IOException e) {
			LogLog.error("add zip file(" + file.getName() + ") failed.");
		} finally {
			try {
				if (zos != null) {
					zos.closeEntry();
					zos.close();
				}
			} catch (Exception e) {
				LogLog.error("error while closing zip file", e);
			}
			try {
				if (fis != null)
					fis.close();
			} catch (Exception e) {
				LogLog.error("error while closing file", e);
			}
		}
	}
}
