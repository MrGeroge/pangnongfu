package com.pangnongfu.server.bal.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.*;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URL;

/**
 * Author:zhangyu
 * create on 15/9/28.
 */
public class ImageUtils {

    private final static Logger logger= LoggerFactory.getLogger(ImageUtils.class);

    public final static void scale(int max,String targetUrl,File outputFile) {

        try {
            BufferedImage src = ImageIO.read(new URL(targetUrl));
            int width = src.getWidth(); // 得到源图宽
            int height = src.getHeight(); // 得到源图长

            if(width<height && width>max){
                height=height*max/width;
                width=max;
            }else{
                if(width > height && height>max){
                    width=width*max/height;
                    height=max;
                }else{
                    if(width==height && width>max){
                        width=height=max;
                    }
                }
            }

            Image image = src.getScaledInstance(width, height,
                    Image.SCALE_DEFAULT);
            BufferedImage tag = new BufferedImage(width, height,
                    BufferedImage.TYPE_INT_RGB);
            Graphics g = tag.getGraphics();
            g.drawImage(image, 0, 0, null); // 绘制缩小后的图
            g.dispose();

            ImageIO.write(tag, "JPEG", outputFile);// 输出到文件流
        } catch (IOException e) {
            logger.error("scale image error url:" + targetUrl, e);
        }
    }

    public final static void cut(int max,String targetUrl,File outputFile){
        try {
            BufferedImage src = ImageIO.read(new URL(targetUrl));
            int width = src.getWidth(); // 得到源图宽
            int height = src.getHeight(); // 得到源图长

            if(width<height && width>max){
                height=height*max/width;
                width=max;
            }else{
                if(width > height && height>max){
                    width=width*max/height;
                    height=max;
                }else{
                    if(width==height && width>max){
                        width=height=max;
                    }
                }
            }

            Image scaleImage = src.getScaledInstance(width, height,
                    Image.SCALE_DEFAULT);

            ImageFilter cropFilter = new CropImageFilter(width/2-max/2,height/2-max/2,max,max);
            Image cutImage= Toolkit.getDefaultToolkit().createImage(
                    new FilteredImageSource(scaleImage.getSource(),
                            cropFilter));

            BufferedImage tag = new BufferedImage(max,max,
                    BufferedImage.TYPE_INT_RGB);
            Graphics g = tag.getGraphics();
            g.drawImage(cutImage, 0, 0, null); // 绘制缩小后的图
            g.dispose();

            ImageIO.write(tag, "JPEG", outputFile);// 输出到文件流
        } catch (IOException e) {
            logger.error("scale image error url:" + targetUrl, e);
        }
    }
}
