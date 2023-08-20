package com.vv.sandbox.utils;

import cn.hutool.core.util.StrUtil;
import com.vv.sandbox.model.ExecuteMessage;
import org.springframework.util.StopWatch;

import java.io.*;

/**
 * @author vv
 */
public class ProcessUtils {
    public static ExecuteMessage runProcessAndGetMessage(Process compilerProcess, String op){
        ExecuteMessage executeMessage = new ExecuteMessage();
        try {
            StopWatch stopWatch = new StopWatch();
            stopWatch.start();
            int exitValue = compilerProcess.waitFor();
            //等待程序执行
            executeMessage.setExitValue(exitValue);
            //正常退出
            if(exitValue == 0){
                System.out.println(op + "成功");
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(compilerProcess.getInputStream()));
                String runOutputLine;
                StringBuilder runOutputStringBuilder = new StringBuilder();
                while ((runOutputLine = bufferedReader.readLine()) != null){
                    runOutputStringBuilder.append(runOutputLine);
                }
                executeMessage.setMessage(runOutputStringBuilder.toString());
            }else {
                System.out.println(op + "失败，错误码：" + exitValue);
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(compilerProcess.getInputStream()));
                String runOutputLine;
                StringBuilder runOutputStringBuilder = new StringBuilder();
                while ((runOutputLine = bufferedReader.readLine()) != null){
                    runOutputStringBuilder.append(runOutputLine);
                }
               executeMessage.setMessage(runOutputStringBuilder.toString());
                //分批获取进程的错误输出
                BufferedReader errorBufferReader = new BufferedReader(new InputStreamReader(compilerProcess.getErrorStream()));
                StringBuilder errorCompileOutputStringBuilder = new StringBuilder();
                String errorCompileOutputLine;
                while ((errorCompileOutputLine = errorBufferReader.readLine()) != null){
                    errorCompileOutputStringBuilder.append(errorCompileOutputLine);
                }
                executeMessage.setErrorMessage(errorCompileOutputStringBuilder.toString());
            }
            stopWatch.stop();
            executeMessage.setTime(stopWatch.getLastTaskTimeMillis());
        }catch (Exception e){
            e.printStackTrace();
        }
        return executeMessage;
    }
    public static ExecuteMessage runInteractProcessAndGetMessage(Process runProcess, String op,String args){
        ExecuteMessage executeMessage = new ExecuteMessage();
        try {
            OutputStream outputStream = runProcess.getOutputStream();
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(outputStream);
            String[] s = args.split(" ");
            String join = StrUtil.join("\n",s)  + "\n";
            outputStreamWriter.write(join);
            outputStreamWriter.flush();
            InputStream inputStream = runProcess.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder complieOutputStringBuilder = new StringBuilder();
            //逐行读取
            String compileOutputLine;
            while ((compileOutputLine = bufferedReader.readLine()) != null){
                complieOutputStringBuilder.append(compileOutputLine);
            }
            executeMessage.setMessage(complieOutputStringBuilder.toString());
            outputStreamWriter.close();
            outputStream.close();
            inputStream.close();
            runProcess.destroy();
        }catch (Exception e){
            e.printStackTrace();
        }
        return executeMessage;
    }
}
