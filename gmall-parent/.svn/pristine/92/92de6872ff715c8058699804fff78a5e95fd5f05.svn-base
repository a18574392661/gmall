package com.atguigu.gmall.util;

import java.io.IOException;
import java.net.InetSocketAddress;

import org.csource.common.MyException;
import org.csource.fastdfs.ClientGlobal;
import org.csource.fastdfs.StorageClient;
import org.csource.fastdfs.TrackerClient;
import org.csource.fastdfs.TrackerServer;
import org.springframework.web.multipart.MultipartFile;

public class PmsUploadUtil {


    public static String uploadImage(MultipartFile multipartFile) throws MyException, IOException {

    	//应该写常量 
        String imgUrl =  "http://192.168.72.128";
        // 上传图片到服务器
        // 配置fdfs的全局链接地址
        String tracker = PmsUploadUtil.class.getResource("/tracker.conf").getPath();// 获得配置文件的路径
        try {
            ClientGlobal.init(tracker);
        } catch (Exception e) {
            e.printStackTrace();
        }

        TrackerClient trackerClient = new TrackerClient();

        // 获得一个trackerServer的实例
        InetSocketAddress inetSocketAddress=new InetSocketAddress("192.168.72.128", 22122);
        TrackerServer trackerServer = new TrackerServer(inetSocketAddress);
        try {
        //	trackerServer=trackerClient.getTrackerServer();
        //  trackerServer = trackerClient.getConnection();
          trackerServer.getConnection();
        //	trackerServer.getConnection();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // 通过tracker获得一个Storage链接客户端
        StorageClient storageClient = new StorageClient(trackerServer,null);

        try {

            byte[] bytes = multipartFile.getBytes();// 获得上传的二进制对象

            // 获得文件后缀名
            String originalFilename = multipartFile.getOriginalFilename();// a.jpg
            System.out.println("文件名字"+originalFilename);
            int i = originalFilename.lastIndexOf(".");
            String extName = originalFilename.substring(i+1);

            String[] uploadInfos = storageClient.upload_file(bytes, extName, null);

            for (String uploadInfo : uploadInfos) {
                imgUrl += "/"+uploadInfo;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return imgUrl;
    }
    
    public static void main(String[] args) {
		String b="adsdas/sus/eeee/1111";
		 String res = b.replace("sus", "");
		 System.out.println(res);
		 res = res.replace("eeee", "");
		 System.out.println(res);
	}
}
