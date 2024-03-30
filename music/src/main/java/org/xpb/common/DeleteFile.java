package org.xpb.common;

import cn.hutool.core.util.StrUtil;

import java.io.File;

public class DeleteFile {
    public void deleteFile(String path) {
        if (!StrUtil.isBlank(path)) {
            File file = new File(path);
            if(file.exists())  {
                file.delete();
            }
        }
    }
}