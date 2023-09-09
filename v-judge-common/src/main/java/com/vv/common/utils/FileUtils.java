package com.vv.common.utils;

import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileNotFoundException;

/**
 * @author vv
 */
public class FileUtils {
    public static String getResourcePath(String dirName) throws FileNotFoundException {
        File path = new File(ResourceUtils.getURL("classpath:").getPath());
        File file = new File(path.getAbsolutePath() + File.separator + dirName);
        return file.getPath();
    }
}
