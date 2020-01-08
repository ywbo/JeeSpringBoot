package com.springboot.jeedemo.controller;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.Arrays;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import ch.qos.logback.classic.Logger;

/**
 * 分片上传
 */
@Controller
@RequestMapping("upload")
public class WebuploadController {

	private static Logger logger = (Logger) LoggerFactory.getLogger(WebuploadController.class);

	/**
	 * 上传路径
	 */
	private static String uploadPath = "D:/webupload";
	
	/**
	 * 跳转到首页
	 * @Title: toUpload
	 * @Description: TODO(这里用一句话描述这个方法的作用)
	 * @param @return 参数
	 * @return String 返回类型
	 * @throws
	 */
	@GetMapping("index")
	public String toUpload() {
		return "/upload";
	}
	
	/**
	 * 查看当前分片区是否上传
	 * @Title: checkMD5
	 * @Description: TODO(这里用一句话描述这个方法的作用)
	 * @param @param request
	 * @param @param response 参数
	 * @return void 返回类型
	 * @throws
	 */
	@PostMapping("checkblock")
	@ResponseBody
	public void checkMD5(HttpServletRequest request, HttpServletResponse response) {
		//当前分片
		String chunk = request.getParameter("chunk");
		//分片大小
		String chunkSize = request.getParameter("chunkSize");
		//当前文件的MD5值
		String guid = request.getParameter("guid");
		//分片上传路径
		String tempPath = uploadPath + File.separator + "temp";
		File checkFile = new File(tempPath + File.separator +guid + File.separator + chunk);
		response.setContentType("text/html;charset=utf-8");
		try {
			if (checkFile.exists() && checkFile.length() == Integer.parseInt(chunkSize)) {
				response.getWriter().write("{\"ifExist\":1}");
			}else {
				response.getWriter().write("{\"ifExist\":0}");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	/**
	 * 上传分片
	 * @Title: upload
	 * @Description: TODO(这里用一句话描述这个方法的作用)
	 * @param @param file
	 * @param @param chunk
	 * @param @param guid
	 * @param @throws IOException 参数
	 * @return void 返回类型
	 * @throws
	 */
	@PostMapping("save")
	@ResponseBody
	public void upload(@RequestParam MultipartFile file, Integer chunk, String guid) throws IOException{
		String filePath = uploadPath + File.separator + "temp" + File.separator + guid;
		File tempFile = new File(filePath);
		if (!tempFile.exists()) {
			tempFile.mkdirs();
		}
		RandomAccessFile raFile = null;
		BufferedInputStream inputStream = null;
		if (chunk == null) {
			chunk = 0;
		}
		try {
            File dirFile = new File(filePath, String.valueOf(chunk));
            //以读写的方式打开目标文件
            raFile = new RandomAccessFile(dirFile, "rw");
            raFile.seek(raFile.length());
            inputStream = new BufferedInputStream(file.getInputStream());
            byte[] buf = new byte[1024];
            int length = 0;
            while ((length = inputStream.read(buf)) != -1) {
                raFile.write(buf, 0, length);
            }
        } catch (Exception e) {
            throw new IOException(e.getMessage());
        } finally {
            if (inputStream != null) {
                inputStream.close();
            }
            if (raFile != null) {
                raFile.close();
            }
        }
	}
	
	/**
	 * 合并文件
	 * @Title: combineBlock
	 * @Description: TODO(这里用一句话描述这个方法的作用)
	 * @param @param guid
	 * @param @param fileName 参数
	 * @return void 返回类型
	 * @throws
	 */
	@PostMapping("combine")
    @ResponseBody
    public void combineBlock(String guid, String fileName) {
        //分片文件临时目录
        File tempPath = new File(uploadPath + File.separator + "temp" + File.separator + guid);
        //真实上传路径
        File realPath = new File(uploadPath + File.separator + "real");
        if (!realPath.exists()) {
            realPath.mkdirs();
        }
        File realFile = new File(uploadPath + File.separator + "real" + File.separator + fileName);
        FileOutputStream os = null;// 文件追加写入
        FileChannel fcin = null;
        FileChannel fcout = null;
        try {
            logger.info("合并文件——开始 [ 文件名称：" + fileName + " ，MD5值：" + guid + " ]");
            os = new FileOutputStream(realFile, true);
            fcout = os.getChannel();
            if (tempPath.exists()) {
                //获取临时目录下的所有文件
                File[] tempFiles = tempPath.listFiles();
                //按名称排序
                Arrays.sort(tempFiles, (o1, o2) -> {
                    if (Integer.parseInt(o1.getName()) < Integer.parseInt(o2.getName())) {
                        return -1;
                    }
                    if (Integer.parseInt(o1.getName()) == Integer.parseInt(o2.getName())) {
                        return 0;
                    }
                    return 1;
                });
                //每次读取10MB大小，字节读取
                //byte[] byt = new byte[10 * 1024 * 1024];
                //int len;
                //设置缓冲区为10MB
                ByteBuffer buffer = ByteBuffer.allocate(10 * 1024 * 1024);
                for (int i = 0; i < tempFiles.length; i++) {
                    FileInputStream fis = new FileInputStream(tempFiles[i]);
                    /*while ((len = fis.read(byt)) != -1) {
                        os.write(byt, 0, len);
                    }*/
                    fcin = fis.getChannel();
                    if (fcin.read(buffer) != -1) {
                        buffer.flip();
                        while (buffer.hasRemaining()) {
                            fcout.write(buffer);
                        }
                    }
                    buffer.clear();
                    fis.close();
                    //删除分片
                    tempFiles[i].delete();
                }
                os.close();
                //删除临时目录
                if (tempPath.isDirectory() && tempPath.exists()) {
                    System.gc(); // 回收资源
                    tempPath.delete();
                }
                logger.info("文件合并——结束 [ 文件名称：" + fileName + " ，MD5值：" + guid + " ]");
            }
        } catch (Exception e) {
            logger.error("文件合并——失败 " + e.getMessage());
        }
    }
	
}
