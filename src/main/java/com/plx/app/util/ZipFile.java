package com.plx.app.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveInputStream;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.plx.app.constant.CmnConst;

/**
 *  @Class Name : ZipFile
 *  @Project Name :
 *  @since
 *  @Description : 압축 파일 처리
 */
public class ZipFile {

	protected Log logger = LogFactory.getLog(getClass());

	/**
	 * @Method Name : decompress
	 * @since
	 * @description 압축 해제 처리
	 * @param zipFileName
	 * @param directory
	 * @throws Exception
	 */
	public void decompress(File zipFile, String directory) throws Exception {
        FileInputStream fis = null;
        ZipArchiveInputStream zis = null;
        ZipArchiveEntry zipentry = null;

        try {
            //파일 스트림
            fis = new FileInputStream(zipFile);
            //Zip 파일 스트림
            //zis = new ZipArchiveInputStream(fis, "utf-8");
            zis = new ZipArchiveInputStream(fis, "euc-kr");

            File mapUnzipFile = new File(directory);
            if(mapUnzipFile.mkdirs()) {
            	logger.info("디렉토리 생성:" + mapUnzipFile.getName());

            	//entry가 없을때까지 뽑기
                while ((zipentry = zis.getNextZipEntry()) != null) {
                    String filename = zipentry.getName();
                    System.out.println("filename:" + filename);

                    File file = new File(directory, filename);
                    //entiry가 폴더면 폴더 생성
                    if (zipentry.isDirectory()) {
                        if(file.mkdirs()) {
                        	logger.info("디렉토리 생성:" + file.getName());
                        } else {
                        	logger.error("디렉토리 생성 실패");
                        }
                    } else {
                        //파일이면 파일 만들기
                        createFile(file, zis);
                    }
                }
            } else {
            	logger.error("디렉토리 생성 실패");
            }

        } catch (IOException ioe) {
            logger.error("압축해제 파일 IOException");
        } finally {
            if (zis != null) {
            	try {
            		zis.close();
            	} catch(IOException e) {
            		logger.error(e.getMessage());
            	}
            }
            if (fis != null) {
            	try {
            		fis.close();
            	} catch(IOException e) {
            		logger.error(e.getMessage());
            	}
            }

        }
    }


	/**
	 * @Method Name : decompressChk
	 * @since
	 * @description 압축 해제시 허용 확장자 체크
	 * @param zipFileName
	 * @return
	 * @throws Exception
	 */
	public boolean decompressChk(File zipFile) throws Exception {
        FileInputStream fis = null;
        ZipArchiveInputStream zis = null;
        ZipArchiveEntry zipentry = null;
        boolean zipResult = true;

        try {
            //파일 스트림
            fis = new FileInputStream(zipFile);
            //Zip 파일 스트림
            //zis = new ZipArchiveInputStream(fis, "utf-8");
            zis = new ZipArchiveInputStream(fis, "euc-kr");

            // 압축파일 검증
            while ((zipentry = zis.getNextZipEntry()) != null) {
                if(!zipentry.isDirectory()) {
                	String filename = zipentry.getName();
                	String ext = filename.substring(filename.lastIndexOf(".")+1).toLowerCase();
                    if(!CmnConst.EXT_LIST.contains(ext)) {
                    	zipResult = false;
                     	continue;
                    }
                }
            }
        } catch (IOException ioe) {
            logger.error("압축해제 파일 IOException");
        } finally {
            if (zis != null) {
            	try {
            		zis.close();
            	} catch(IOException e) {
            		logger.error(e.getMessage());
            	}
            }
            if (fis != null) {
            	try {
            		fis.close();
            	} catch(IOException e) {
            		logger.error(e.getMessage());
            	}
            }

        }

        return zipResult;
    }


    /**
     * 파일 만들기 메소드
     * @param file 파일
     * @param zis Zip스트림
     */
    private void createFile(File file, ZipArchiveInputStream zis) throws Exception {
        //디렉토리 확인
        File parentDir = new File(file.getParent());
        //디렉토리가 없으면 생성하자
        if (!parentDir.exists()) {
            if(parentDir.mkdirs()) {
            	logger.info("디렉토리 생성:" + parentDir.getName());
            } else {
            	logger.error("디렉토리 생성 실패");
            }
        }

        //파일 스트림 선언
        FileOutputStream fos  = null;
        try {
        	fos = new FileOutputStream(file);
            byte[] buffer = new byte[256];
            int size = 0;
            //Zip스트림으로부터 byte뽑아내기
            while ((size = zis.read(buffer)) > 0) {
                //byte로 파일 만들기
                fos.write(buffer, 0, size);
            }
        } catch (IOException ioe) {
        	logger.error("FileOutputStream IOException");
        } finally {
            if (fos != null) {
            	try {
            		fos.close();
            	} catch(IOException e) {
            		logger.error(e.getMessage());
            	}
            }
        }
    }
}
