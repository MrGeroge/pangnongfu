package com.pangnongfu.server.bal.task;

import com.aliyun.oss.OSSClient;
import com.aliyun.oss.model.ObjectMetadata;
import com.aliyun.oss.model.PutObjectResult;
import com.pangnongfu.server.bal.utils.ImageUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.asm.Handle;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

/**
 * Author:zhangyu
 * create on 15/9/29.
 */
public class ImageHandleTask implements Runnable{

    private static final Logger logger= LoggerFactory.getLogger(ImageHandleTask.class);

    private static final String accessKey = "YXQgY83rQThKc7jb";
    private static final String secretKey = "nzSfm46kUVqLnCNPt9L0w8xzWB2eyG";
    private static final String bucketName="wbsn2";
    private static final String endpoint="http://oss-cn-hangzhou.aliyuncs.com";
    private static final String baseDir=System.getProperty("user.dir");

    private String url;
    private String fileKey;
    private int imgMaxPix;
    //处理图片类型
    private String type;

    public ImageHandleTask(String url,String fileKey,int imgMaxPix,String type) {
        this.url= url;
        this.fileKey=fileKey;
        this.imgMaxPix=imgMaxPix;
        this.type=type;
    }

    @Override
    public void run() {

        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
            String tempFileName=sdf.format(new Date())+"_"+String.valueOf(new Random().nextInt(899) + 100)+".jpg";
            File tempFile=new File(baseDir,tempFileName);

            tempFile.createNewFile();

            if(type.equals("scale"))
                ImageUtils.scale(imgMaxPix,url, tempFile);

            if(type.equals("cut"))
                ImageUtils.cut(imgMaxPix,url, tempFile);

            //初始化OSSClient
            OSSClient client = new OSSClient(endpoint, accessKey, secretKey );


            InputStream content = new FileInputStream(tempFile);
            // 创建上传Object的Metadata
            ObjectMetadata meta = new ObjectMetadata();
            // 必须设置ContentLength
            meta.setContentLength(tempFile.length());
            // 上传Object.
            PutObjectResult result = client.putObject(bucketName,fileKey, content, meta);

            //删除本地临时文件
            if(callBack!=null){
                callBack.onSuccess();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public interface HandleCallBack{
        void onSuccess();
    }

    private HandleCallBack callBack;

    public ImageHandleTask setCallBack(HandleCallBack callBack) {
        this.callBack = callBack;
        return this;
    }
}
