package com.dili.trace.util;

import com.dili.trace.service.UserService;
import com.google.zxing.*;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.OutputStream;
import java.util.Base64;
import java.util.Hashtable;
import java.util.Random;

/**
 *
 * @Author guzman.liu
 * @Description
 * 二维码生成工具类
 * @Date 2020/11/23 10:19
 * @return
 */
@Component
public class QRCodeUtil
{
    private static final Logger logger = LoggerFactory.getLogger(QRCodeUtil.class);
    private static final String CHARSET = "utf-8";
    private static final String FORMAT_NAME = "png";
    private static final int DEFAULT_QRCODE_SIZE = 300;
    private static final int DEAUFLT_IMG_WIDTH = 60;
    private static final int DEFAULT_IMG_HEIGHT = 60;
    private static final int QRCODE_SIZE = getSize();

    @Autowired
    private static UserService userService;

    /**
     *
     * @Author guzman.liu
     * @Description
     * 创建二维码图片
     * @Date 2020/11/23 10:31
     * @return
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    private static BufferedImage createImage(String content, String imgPath,
            boolean needCompress,String extText) throws Exception
    {
        logger.info("create image.");
        Hashtable hints = new Hashtable();
        hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
        hints.put(EncodeHintType.CHARACTER_SET, CHARSET);
        hints.put(EncodeHintType.MARGIN, 1);
        BitMatrix bitMatrix = new MultiFormatWriter().encode(content,
                BarcodeFormat.QR_CODE, QRCODE_SIZE, QRCODE_SIZE, hints);
        
       
        int width = bitMatrix.getWidth();
        int height = bitMatrix.getHeight();
        
        //图片高度增加20. 300 * 320.
        BufferedImage image = new BufferedImage(width, height + 20,
                BufferedImage.TYPE_INT_RGB);
        for (int x = 0; x < width; x++)
        {
            for (int y = 0; y < height; y++)
            {
                image.setRGB(x, y, bitMatrix.get(x, y) ? 0xFF000000
                        : 0xFFFFFFFF);
            }
            
            //下面增加部分颜色设置为白色.
            for (int y = 0; y < 20; y++)
            {
                 image.setRGB(x, height + y,  0xFFFFFFFF);
            }
            
        }
        
        
        //获取图像面板
        Graphics2D graph = image.createGraphics();

        Font font=new Font("微软雅黑",Font.BOLD,16);
       
        graph.setFont(font);
        graph.setColor(Color.BLACK);
        
        //写入编号
        graph.drawString(extText, 27, 305);
        
      
        graph.dispose(); 
        
       
        if (imgPath == null || "".equals(imgPath))
        {
            return image;
        }
        insertImage(image, imgPath, needCompress);
        return image;
    }

    /**
     *
     * @Author guzman.liu
     * @Description 
     * @Date 2020/11/23 10:32
     * @Param 插入图片
     */
    private static void insertImage(BufferedImage source, String imgPath,
            boolean needCompress) throws Exception
    {
        File file = new File(imgPath);
        if (!file.exists())
        {
            logger.error(" file name " + imgPath + "does not exist!");
            return;
        }
        Image src = ImageIO.read(new File(imgPath));
        int width = src.getWidth(null);
        int height = src.getHeight(null);
        int imgWidth = getImgWidth();
        int imgHeight = getImgHeight();
        if (needCompress)
        {
            if (width > imgWidth)
            {
                width = imgWidth;
            }
            if (height > imgHeight)
            {
                height = imgHeight;
            }
            Image image = src.getScaledInstance(width, height,
                    Image.SCALE_SMOOTH);
            BufferedImage tag = new BufferedImage(width, height,
                    BufferedImage.TYPE_INT_RGB);
            Graphics g = tag.getGraphics();
            g.drawImage(image, 0, 0, null);
            g.dispose();
            src = image;
        }
        Graphics2D graph = source.createGraphics();
        int x = (QRCODE_SIZE - width) / 2;
        int y = (QRCODE_SIZE - height) / 2;
        graph.drawImage(src, x, y, width, height, null);
        Shape shape = new RoundRectangle2D.Float(x, y, width, width, 6, 6);
        graph.setStroke(new BasicStroke(3f));
        graph.draw(shape);
        graph.dispose();
    }

    /**
     *
     * @Author guzman.liu
     * @Description
     * @Date 2020/11/23 10:32
     * 生成图片的二进制码
     * @return
     */
    public static byte[] encode(String content, String imgPath,
                                boolean needCompress,String extText) throws Exception
    {
        BufferedImage image = createImage(content, imgPath, needCompress,extText);
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        ImageIO.write(image, FORMAT_NAME, os);
        return os.toByteArray();
    }
    /**
     *
     * @Author guzman.liu
     * @Description
     * 产生base64位码
     * @Date 2020/11/23 10:33
     */
    public static String base64Image(byte[] bytes) {
        String base64 = Base64.getEncoder().encodeToString(bytes).trim();
        base64 = "data:image/"+FORMAT_NAME+";base64," + base64;
        return base64;

    }

    /**
     *
     * @Author guzman.liu
     * @Description
     * @Date 2020/11/23 10:32
     * 生成图片的二进制码
     * @return
     */
    public static void encode(String content, String imgPath,
            OutputStream output, boolean needCompress) throws Exception
    {
        BufferedImage image = createImage(content, imgPath, needCompress,"");
        ImageIO.write(image, FORMAT_NAME, output);
    }

    /**
     *
     * @Author guzman.liu
     * @Description
     * @Date 2020/11/23 10:32
     * 生成图片的二进制码
     * @return
     */
    public static void encode(String content, OutputStream output)
            throws Exception
    {
        encode(content, null, output, false);
    }

    /**
     *
     * @Author guzman.liu
     * @Description
     * @Date 2020/11/23 10:32
     * 生成图片的二进制码
     * @return
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public static String decode(File file) throws Exception
    {
        BufferedImage image = ImageIO.read(file);
        if (image == null)
        {
            return null;
        }
        BufferedImageLuminanceSource source = new BufferedImageLuminanceSource(
                image);
        BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));
        Hashtable hints = new Hashtable();
        hints.put(DecodeHintType.CHARACTER_SET, CHARSET);
        Result result = new MultiFormatReader().decode(bitmap, hints);
        return result.getText();
    }

    /**
     *
     * @Author guzman.liu
     * @Description
     * @Date 2020/11/23 10:32
     * 生成图片的二进制码
     * @return
     */
    public static String decode(String path) throws Exception
    {
        return decode(new File(path));
    }

    /**
     *
     * @Author guzman.liu
     * @Description
     * @Date 2020/11/23 10:34
     */
    public static String getDefaultFileName()
    {
        Random random = new Random();
        int num = random.nextInt(99999);
        return num + ".jpg";
    }

    /**
     *
     * @Author guzman.liu
     * @Description
     * 获取大小
     * @Date 2020/11/23 10:34
     * @return
     */
    public static int getSize()
    {
        return  DEFAULT_QRCODE_SIZE;
    }

    /**
     * @Author guzman.liu
     * @Description
     * @Date 2020/11/23 10:34
     * @return
     */
    public static int getImgWidth()
    {
        return  DEAUFLT_IMG_WIDTH;
    }

    /**
     * @Author guzman.liu
     * @Description
     * @Date 2020/11/23 10:34
     * @return
     */
    public static int getImgHeight()
    {
        return DEFAULT_IMG_HEIGHT;
    }

    /**
     * @Author guzman.liu
     * @Description
     * @Date 2020/11/23 10:34
     * @return
     */
    public static String getDestPath(){
        return "";
    }

    /**
     * @Author guzman.liu
     * @Description
     * @Date 2020/11/23 10:34
     * @return
     */
    public static String getImgPath(){
        return "";
    }



}
