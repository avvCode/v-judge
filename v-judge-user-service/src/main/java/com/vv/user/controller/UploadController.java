package com.vv.user.controller;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.resource.ResourceUtil;
import com.vv.oj.common.BaseResponse;
import com.vv.oj.common.ErrorCode;
import com.vv.oj.common.ResultUtils;
import com.vv.oj.exception.BusinessException;
import com.vv.oj.utils.FileUtils;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.UUID;

/**
 * @author vv
 */
@RestController
@RequestMapping("/file")
public class UploadController {

    private static final String FILE_DIR_NAME =  "static";
    @PostMapping("/userAvatar/add")
    public BaseResponse<String> addUserAvatar(@RequestPart("file") MultipartFile file){
        try {
            String fileName = UUID.randomUUID() + ".png";
            String resourcePath = FileUtils.getResourcePath(FILE_DIR_NAME);
            file.transferTo(new File(  resourcePath + File.separator + fileName));
            return ResultUtils.success(fileName);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    @PostMapping("/userAvatar/del")
    public BaseResponse<Boolean> delUserAvatar(String oldPath){
        // 设置上传至项目文件夹下的uploadFile文件夹中，没有文件夹则创建
        try {
            String resourcePath = FileUtils.getResourcePath(FILE_DIR_NAME);
            boolean del = FileUtil.del(resourcePath + File.separator + oldPath);
            if(!del){
                throw new BusinessException(ErrorCode.SYSTEM_ERROR,"删除文件失败");
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        return ResultUtils.success(true);
    }
}
