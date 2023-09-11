package com.vv.user.controller;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.resource.ResourceUtil;
import com.vv.common.common.BaseResponse;
import com.vv.common.common.ErrorCode;
import com.vv.common.common.ResultUtils;
import com.vv.common.exception.BusinessException;
import com.vv.common.utils.FileUtils;
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
            if(!FileUtil.exist(resourcePath)){
                FileUtil.mkdir(resourcePath);
            }
            file.transferTo(new File(  resourcePath + File.separator + fileName));
            return ResultUtils.success(fileName);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    @PostMapping("/userAvatar/del")
    public BaseResponse<Boolean> delUserAvatar(String oldPath){
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
