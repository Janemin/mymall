package com.mmall.util;

import com.mmall.common.Const;
import org.apache.commons.net.ftp.FTPClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

/**
 * Created by Jane on 2018/1/19.
 */
public class FTPUtil {

    private static Logger logger = LoggerFactory.getLogger(FTPUtil.class);

    private static String ftpIp = PropertiesUtil.getProperty(Const.FTP_SERVER_IP);
    private static String ftpPort = PropertiesUtil.getProperty(Const.FTP_SERVER_PORT);
    private static String ftpUser = PropertiesUtil.getProperty(Const.FTP_SERVER_USERNAME);
    private static String ftpPwd = PropertiesUtil.getProperty(Const.FTP_SERVER_PASSWORD);

    private String ip;
    private String port;
    private String user;
    private String pwd;
    private FTPClient ftpClient;

    public FTPUtil(String ip, String port, String user, String pwd) {
        this.ip = ip;
        this.port = port;
        this.user = user;
        this.pwd = pwd;
    }

    public static boolean uploadFile(List<File> fileList) throws IOException {
        FTPUtil ftpUtil = new FTPUtil(ftpIp, ftpPort, ftpUser,ftpPwd);
        logger.info("开始连接ftp服务器");
        boolean result = ftpUtil.uploadFile("img", fileList);
        logger.info("开始连接ftp服务器,结束上传，上传结果:{}", result);
        return result;
    }

    private boolean uploadFile(String remotePath, List<File> fileList) throws IOException{
        boolean uploaded = true;
        FileInputStream fis = null;
        if(connectServer(this.ip, this.port, this.user, this.pwd)) {
            try {
                ftpClient.changeWorkingDirectory(remotePath);
                ftpClient.setBufferSize(1024);
                ftpClient.setControlEncoding("UTF-8");
                ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
                ftpClient.enterLocalPassiveMode();
                for(File fileItem : fileList) {
                    fis = new FileInputStream(fileItem);
                    ftpClient.storeFile(fileItem.getName(), fis);
                }
            } catch (IOException e) {
                uploaded = false;
                logger.error("上传文件异常", e);
                e.printStackTrace();
            } finally {
                fis.close();
                ftpClient.disconnect();
            }
        }
        return uploaded;
    }

    private boolean connectServer(String ip, String port, String user, String pwd){
        boolean isConnected = false;
        ftpClient = new FTPClient();
        try {
            ftpClient.connect(ip);
            isConnected = ftpClient.login(user, pwd);
        } catch (IOException e) {
            logger.error("连接FTP服务器异常", e);
            e.printStackTrace();
        }
        return isConnected;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public FTPClient getFtpClient() {
        return ftpClient;
    }

    public void setFtpClient(FTPClient ftpClient) {
        this.ftpClient = ftpClient;
    }
}
