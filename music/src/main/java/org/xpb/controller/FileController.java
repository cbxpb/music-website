package org.xpb.controller;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.xpb.common.Result;
import org.xpb.domain.URL;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;

@RestController
@RequestMapping("/file")
public class FileController {

    @Value("${server.port}")
    String port;

    @Value("${ip:localhost}")
    String ip;

    //资源的根目录
    private static final String ROOT_PATH = System.getProperty("user.dir") + File.separator + "files";
    //图片的根目录
    private static final String IMG_PATH = ROOT_PATH + File.separator + "img";
    private static final String ADMIN_IMG = IMG_PATH + File.separator + "adminImg";   // img/adminImg      type=1
    private static final String USER_IMG = IMG_PATH + File.separator + "userImg";   // img/userImg      type=2
    private static final String SINGER_IMG = IMG_PATH + File.separator + "singerImg"; // img/singerImg  type=3
    private static final String SONG_IMG = IMG_PATH + File.separator + "songImg"; // img/songImg        type=4
    private static final String SONG_LIST_IMG = IMG_PATH + File.separator + "songListImg"; // img/songListImg        type=5
    //音乐的根目录
    private static final String MUSIC_PATH = ROOT_PATH + File.separator + "music";
    private static final String SONG_PATH = MUSIC_PATH + File.separator + "song";   //music/song        type=6
    private static final String LRC_PATH = MUSIC_PATH + File.separator + "lrc";     //music/lrc         type=7
    private static final String[] PATH = {ADMIN_IMG,USER_IMG,SINGER_IMG,SONG_IMG,SONG_LIST_IMG,SONG_PATH,LRC_PATH};  //所需路径地址数组

    /**
     * 文件上传
     * @param file
     * @param type
     * @return
     * @throws IOException
     */
    @PostMapping("/upload")
    public Result upload(MultipartFile file,Integer type) throws IOException {
        if (file.isEmpty()){
            return Result.error("文件上传失败");
        }
        String originalFilename = file.getOriginalFilename();   //获取文件的原始名称
        String mainName = FileUtil.mainName(originalFilename);  //获取文件的前缀名称
        String extName = FileUtil.extName(originalFilename);    //获取文件的后缀名称
        String newFilename = System.currentTimeMillis() + "_" + mainName + "." + extName;   //给上传的文件一个新名字防止重复
        System.out.println(mainName);
        System.out.println(extName);
        System.out.println(newFilename);
        for(String path : PATH) {                              //没有上级目录就创建
           if(!FileUtil.exist(path)) {
               FileUtil.mkdir(path);
           }
        }
        File saveFile = new File(PATH[type-1] + File.separator + newFilename);  //不同类型的文件存储到不同的目录
        file.transferTo(saveFile);
        URL url = new URL();
        url.setDownUrl("http://" + ip + ":" + port + "/file/download/" + newFilename);
        url.setUploadUrl(PATH[type-1] + File.separator + newFilename);
        url.setFilename(newFilename);
       return Result.success(url);
    }

    @GetMapping("/download/{fileName}")
    public Result dowmload(@PathVariable String fileName, HttpServletResponse response) throws IOException {
        Integer flog = 0;
        String filePath = new String();
        for(String path : PATH) {                              //没有上级目录就创建
            filePath = path + File.separator + fileName;
            if(FileUtil.exist((filePath))) {
                flog = 1;
                break;
            }
        }
        if (flog == 1) {
            byte[] bytes = FileUtil.readBytes(filePath);
            ServletOutputStream outputStream = response.getOutputStream();
            long length = bytes.length;
            response.addHeader("Accept-Ranges", "bytes");
            response.addHeader("Content-Length", length + "");
            response.setContentType("text/plain;charset=utf-8");        //不加这行会导致文本类型文件中中文乱码
            outputStream.write(bytes);
            outputStream.flush();
            outputStream.close();
        }else {
            return Result.error("404","没找到资源");
        }
        return Result.success();
    }

    //删除文件
    @DeleteMapping("/delete")
    public Result delete(@RequestParam String uploadUrl) {
        if(StrUtil.isBlank(uploadUrl))
        {
            return Result.error("没有文件");
        }
        System.out.println(uploadUrl);
        File file = new File(uploadUrl);
        if(file.exists())  {
            file.delete();
        }
        return Result.success("删除了");
    }
}
