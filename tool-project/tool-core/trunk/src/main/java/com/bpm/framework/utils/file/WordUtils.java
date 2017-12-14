package com.bpm.framework.utils.file;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.poi.poifs.filesystem.DirectoryEntry;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;

/**
 * 将实体对象数据导出为Word文件
 * @author dgj
 * @date 2017年5月27日 下午1:43:05
 */
public class WordUtils implements Serializable {

	private static final long serialVersionUID = 2915647922671689907L;

	private static Logger log = Logger.getLogger(WordUtils.class);

	private final static String WORD_Extension = ".doc";
	
	/**
     * @param list
     * @param request
     * @param f：文件名
     * @param out
     * @throws IOException
     */
    public boolean exportWordFile(List<String> list, HttpServletRequest request, String f, OutputStream out) {  
        List<String> fileNames = new ArrayList<String>();
        File zip = new File(f + ".zip");
        
        if(list == null){
        	return false;
        }
        
        try{
	        for (int k = 0, n = list.size(); k < n; k++) {
				if(list.get(k) == null){
		        	continue;
		        }
				
				POIFSFileSystem fs = new POIFSFileSystem(); 
				DirectoryEntry directory = fs.getRoot();
				
				byte b[] = ("<html>"+ list.get(k).toString() +"</html>").getBytes("gb2312");
				ByteArrayInputStream bais = new ByteArrayInputStream(b); 
				
				directory.createDocument("WordDocument", bais);
	            String file = f + "-" + k + WORD_Extension;
	            fileNames.add(file);
	            FileOutputStream o = null;
	            try {  
	            	o = new FileOutputStream(file);
	                fs.writeFilesystem(o);
	            } catch (Exception ex) {
	                log.error("Exception of word file when export >>>>>", ex);
	                return false;
	            } finally {
	            	bais.close();
	                o.flush();  
	                o.close();  
	            }  
	        }  
	        File srcfile[] = new File[fileNames.size()];  
	        for (int i = 0, n = fileNames.size(); i < n; i++) {  
	            srcfile[i] = new File(fileNames.get(i));  
	        }  
	        this.ZipFiles(srcfile, zip);  
	        FileInputStream inStream = new FileInputStream(zip);  
	        byte[] buf = new byte[4096];  
	        int readLength;  
	        while (((readLength = inStream.read(buf)) != -1)) {  
	            out.write(buf, 0, readLength);  
	        }  
	        inStream.close();
        }catch(Exception e){
        	log.error("Exception of word file when export >>>>>", e);
        	return false;
        }
        
        return true;
    }
    
    /**
     * @param srcfile 文件名数组  
     * @param zipfile 压缩后文件  
     */  
    public void ZipFiles(File[] srcfile, File zipfile) {  
        byte[] buf = new byte[1024];
        try {
            ZipOutputStream out = new ZipOutputStream(new FileOutputStream(zipfile));
            for (int i = 0; i < srcfile.length; i++) {
                FileInputStream in = new FileInputStream(srcfile[i]);
                out.putNextEntry(new ZipEntry(srcfile[i].getName()));
                int len;
                while ((len = in.read(buf)) > 0) {
                    out.write(buf, 0, len);
                }
                out.closeEntry();
                in.close();
            }
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
            log.error("Exception of zip file >>>>>", e);
        }
    }
}
