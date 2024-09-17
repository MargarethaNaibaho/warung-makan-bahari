package com.enigmacamp.springbootwmbreview.service;

import com.enigmacamp.springbootwmbreview.entity.MenuImage;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public interface MenuImageService {
    MenuImage createFile(MultipartFile multipartFile);
    Resource findByPath(String path);
    void deleteFile(String id);
}
