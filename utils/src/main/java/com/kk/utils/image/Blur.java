package com.kk.utils.image;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 图片模糊化
 */
public class Blur {
    private static Log logger = LogFactory.getLog(Blur.class);

    public static void fastblur(int[] pix, int w, int h, int radius) {

        int wm = w - 1;
        int hm = h - 1;
        int wh = w * h;
        int div = radius + radius + 1;

        int r[] = new int[wh];
        int g[] = new int[wh];
        int b[] = new int[wh];
        int rsum, gsum, bsum, x, y, i, p, yp, yi, yw;
        int vmin[] = new int[Math.max(w, h)];

        int divsum = (div + 1) >> 1;
        divsum *= divsum;
        int dv[] = new int[256 * divsum];
        for (i = 0; i < 256 * divsum; i++) {
            dv[i] = (i / divsum);
        }

        yw = yi = 0;

        int[][] stack = new int[div][3];
        int stackpointer;
        int stackstart;
        int[] sir;
        int rbs;
        int r1 = radius + 1;
        int routsum, goutsum, boutsum;
        int rinsum, ginsum, binsum;

        for (y = 0; y < h; y++) {
            rinsum = ginsum = binsum = routsum = goutsum = boutsum = rsum = gsum = bsum = 0;
            for (i = -radius; i <= radius; i++) {
                p = pix[yi + Math.min(wm, Math.max(i, 0))];
                sir = stack[i + radius];
                sir[0] = (p & 0xff0000) >> 16;
                sir[1] = (p & 0x00ff00) >> 8;
                sir[2] = (p & 0x0000ff);
                rbs = r1 - Math.abs(i);
                rsum += sir[0] * rbs;
                gsum += sir[1] * rbs;
                bsum += sir[2] * rbs;
                if (i > 0) {
                    rinsum += sir[0];
                    ginsum += sir[1];
                    binsum += sir[2];
                } else {
                    routsum += sir[0];
                    goutsum += sir[1];
                    boutsum += sir[2];
                }
            }
            stackpointer = radius;

            for (x = 0; x < w; x++) {

                r[yi] = dv[rsum];
                g[yi] = dv[gsum];
                b[yi] = dv[bsum];

                rsum -= routsum;
                gsum -= goutsum;
                bsum -= boutsum;

                stackstart = stackpointer - radius + div;
                sir = stack[stackstart % div];

                routsum -= sir[0];
                goutsum -= sir[1];
                boutsum -= sir[2];

                if (y == 0) {
                    vmin[x] = Math.min(x + radius + 1, wm);
                }
                p = pix[yw + vmin[x]];

                sir[0] = (p & 0xff0000) >> 16;
                sir[1] = (p & 0x00ff00) >> 8;
                sir[2] = (p & 0x0000ff);

                rinsum += sir[0];
                ginsum += sir[1];
                binsum += sir[2];

                rsum += rinsum;
                gsum += ginsum;
                bsum += binsum;

                stackpointer = (stackpointer + 1) % div;
                sir = stack[(stackpointer) % div];

                routsum += sir[0];
                goutsum += sir[1];
                boutsum += sir[2];

                rinsum -= sir[0];
                ginsum -= sir[1];
                binsum -= sir[2];

                yi++;
            }
            yw += w;
        }
        for (x = 0; x < w; x++) {
            rinsum = ginsum = binsum = routsum = goutsum = boutsum = rsum = gsum = bsum = 0;
            yp = -radius * w;
            for (i = -radius; i <= radius; i++) {
                yi = Math.max(0, yp) + x;

                sir = stack[i + radius];

                sir[0] = r[yi];
                sir[1] = g[yi];
                sir[2] = b[yi];

                rbs = r1 - Math.abs(i);

                rsum += r[yi] * rbs;
                gsum += g[yi] * rbs;
                bsum += b[yi] * rbs;

                if (i > 0) {
                    rinsum += sir[0];
                    ginsum += sir[1];
                    binsum += sir[2];
                } else {
                    routsum += sir[0];
                    goutsum += sir[1];
                    boutsum += sir[2];
                }

                if (i < hm) {
                    yp += w;
                }
            }
            yi = x;
            stackpointer = radius;
            for (y = 0; y < h; y++) {
                // Preserve alpha channel: ( 0xff000000 & pix[yi] )
                pix[yi] = (0xff000000 & pix[yi]) | (dv[rsum] << 16)
                        | (dv[gsum] << 8) | dv[bsum];

                rsum -= routsum;
                gsum -= goutsum;
                bsum -= boutsum;

                stackstart = stackpointer - radius + div;
                sir = stack[stackstart % div];

                routsum -= sir[0];
                goutsum -= sir[1];
                boutsum -= sir[2];

                if (x == 0) {
                    vmin[y] = Math.min(y + r1, hm) * w;
                }
                p = x + vmin[y];

                sir[0] = r[p];
                sir[1] = g[p];
                sir[2] = b[p];

                rinsum += sir[0];
                ginsum += sir[1];
                binsum += sir[2];

                rsum += rinsum;
                gsum += ginsum;
                bsum += binsum;

                stackpointer = (stackpointer + 1) % div;
                sir = stack[stackpointer];

                routsum += sir[0];
                goutsum += sir[1];
                boutsum += sir[2];

                rinsum -= sir[0];
                ginsum -= sir[1];
                binsum -= sir[2];

                yi += w;
            }
        }

    }

    /**
     * 得到网络图片字节数组
     *
     * @param photoUrl
     * @return
     */
    public static byte[] getPhotoStream(String photoUrl) {
        HttpClient httpClient = new HttpClient();
        httpClient.getHttpConnectionManager().getParams()
                .setConnectionTimeout(15000);
        GetMethod method = new GetMethod(photoUrl);
        method.setRequestHeader("User-Agent",
                "Mozilla/5.0 (Windows NT 5.1; rv:22.0) Gecko/20100101 Firefox/22.0");
        method.setRequestHeader("Cache-Control", "max-age=0");
        method.setRequestHeader("Accept",
                "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
        // get.setRequestHeader("Accept-Encoding", "gzip, deflate");
        method.setRequestHeader("Accept-Language",
                "zh-cn,zh;q=0.8,en-us;q=0.5,en;q=0.3");
        method.setRequestHeader("Accept-Charset",
                "zh-cn,zh;q=0.8,en-us;q=0.5,en;q=0.3");
        try {
            int statusCode = httpClient.executeMethod(method);
            if (statusCode != HttpStatus.SC_OK) {
                return new byte[]{};
            }
            return method.getResponseBody();
        } catch (HttpException e) {
            logger.error(
                    "Error occurred while get photo stream from photo url", e);
        } catch (IOException e) {
            logger.error(
                    "Error occurred while get photo stream from photo url", e);
        } finally {
            method.releaseConnection();
        }
        return new byte[]{};
    }

    /**
     * 得到网络图片的原名
     *
     * @param photoUrl
     * @return 若不规范则返回""
     */
    public static String getPhotoUrlOriName(String photoUrl) {
        try {
            String[] parts = StringUtils.split(photoUrl, "/");
            return parts[parts.length - 1];
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * 得到网络图片的后缀名
     *
     * @param photoUrl
     * @return 若不规范则返回""
     */
    public static String getPhotoUrlImageType(String photoUrl) {
        try {
            String name = getPhotoUrlOriName(photoUrl);
            String[] parts = StringUtils.split(name, ".");
            return parts[parts.length - 1];
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * 图片模糊化
     *
     * @param data 图片字节数组
     * @param type 图片后缀名
     * @return
     * @throws Exception
     */
    public static byte[] blur(byte[] data, String type) throws Exception {
        int[] result = null;
//        if (!UploadConf.imageTypes.contains(type)) {
//            throw new IllegalArgumentException("type 不合法!");
//        }

        BufferedImage bufImg = ImageIO.read(new ByteArrayInputStream(data));

        int height = bufImg.getHeight();
        int width = bufImg.getWidth();

        BufferedImage bufImg2 = new BufferedImage(width, height,
                BufferedImage.TYPE_INT_RGB);

        bufImg2.createGraphics().drawImage(bufImg, 0, 0, Color.WHITE, null);

        result = new int[width * height];

        result = new int[width * height];
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {

                int rgb = bufImg2.getRGB(i, j) & 0xFFFFFF;

                // bufImg.set

                result[j * width + i] = rgb;

                // System.out.println("r: "+r+"g:"+g);

            }
        }

        Blur.fastblur(result, width, height, 20);

        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {

                bufImg2.setRGB(i, j, result[j * width + i]);

                // System.out.println("r: "+r+"g:"+g);

            }
        }

        // ImageIO.write(bufImg2, "jpg", new File("data/test.png"));

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        boolean flag = ImageIO.write(bufImg2, type, out);
        byte[] b = out.toByteArray();
        return b;
    }

    /**
     * 图片模糊化
     *
     * @param photoUrl
     * @return
     * @throws Exception
     */
    public static byte[] blur(String photoUrl) throws Exception {
        return blur(getPhotoStream(photoUrl), getPhotoUrlImageType(photoUrl));
    }

}
