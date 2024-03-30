package org.xpb.domain;

import lombok.Data;

/**
 * 文件所需的url
 */
@Data
public class URL {
    String downUrl;     //返回的下载地址或预览地址
    String filename;    //文件保存在本地的名称
    String uploadUrl;   //文件保存在本地的地址
}
