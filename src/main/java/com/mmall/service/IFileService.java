package com.mmall.service;

import org.springframework.web.multipart.MultipartFile;

/**
 * Created by Jane on 2018/1/19.
 */
public interface IFileService {

    String upload(MultipartFile file, String path);
}
