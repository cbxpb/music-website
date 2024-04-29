package org.xpb.common;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import java.io.File;

/**
 * 删除本地文件的公共类
 */
public class DeleteFile {
    private static final String ROOT_PATH = System.getProperty("user.dir") + File.separator + "files";
    //图片的根目录
    private static final String IMG_PATH = ROOT_PATH + File.separator + "img";
    private static final String ADMIN_IMG = IMG_PATH + File.separator + "adminAvatar";      // img/adminAvatar      type=1
    private static final String USER_IMG = IMG_PATH + File.separator + "userAvatar";        // img/userAvatar       type=2
    private static final String SINGER_IMG = IMG_PATH + File.separator + "singerImg";       // img/singerImg        type=3
    private static final String SONG_IMG = IMG_PATH + File.separator + "songImg";           // img/songImg          type=4
    private static final String SONG_LIST_IMG = IMG_PATH + File.separator + "songListImg";  // img/songListImg      type=5
    //音乐的根目录
    private static final String MUSIC_PATH = ROOT_PATH + File.separator + "music";
    private static final String SONG_PATH = MUSIC_PATH + File.separator + "song";           //music/song            type=6
    private static final String LRC_PATH = MUSIC_PATH + File.separator + "lrc";             //music/lrc             type=7
    private static final String[] PATH = {ADMIN_IMG,USER_IMG,SINGER_IMG,SONG_IMG,SONG_LIST_IMG,SONG_PATH,LRC_PATH};  //所需路径地址数组
    public void deleteFile(String fileName) {
        if(!StrUtil.isBlank(fileName)) {
            String filePath = new String();
            for(String path : PATH) {
                filePath = path + File.separator + fileName;    //拼接成绝对地址
                if(FileUtil.exist((filePath))) {
                    FileUtil.del(filePath);
                    break;
                }
            }
        }
    }
}
