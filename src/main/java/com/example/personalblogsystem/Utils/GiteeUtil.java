package com.example.personalblogsystem.Utils;

import lombok.Data;

/**
 * @Version 1.0
 * @Author:ris
 * @Date:2023/1/8
 */
@Data
public class GiteeUtil {

    //用户授权码
    private final String access_token = "99b8890f75c9b2f32deb10db03ac6c87";
    //仓库所属空间地址(企业、组织或个人的地址path)
    private final String owner = "ris-two-thousand-and-twenty";
    //仓库路径(path)
    private final String repo = "personal-blog-map-bed";
    //文件的路径
    private String path;
    //文件内容, 要用 base64 编码
    private String content;
    //提交信息
    private String message = "COMMIT";

    public GiteeUtil(String path,String content) {
        this.path = path;
        this.content = content;
    }
    public GiteeUtil(String path,String content,String message) {
        this.path = path;
        this.content = content;
        this.message = message;
    }

    public String getUploadUrl() {
        String url = "https://gitee.com/api/v5/repos/"+ owner + "/" + repo + "/contents/" + path;
        return url;
    }

}
