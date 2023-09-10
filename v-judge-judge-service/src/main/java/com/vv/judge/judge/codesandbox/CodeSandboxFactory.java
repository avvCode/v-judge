package com.vv.judge.judge.codesandbox;


import com.vv.judge.judge.codesandbox.impl.RemoteCodeSandbox;

/**
 * 代码沙箱工厂（根据字符串参数创建指定的代码沙箱实例）
 */
public class CodeSandboxFactory {

    /**
     * 创建代码沙箱示例
     *
     * @param type 沙箱类型
     * @return
     */
    public static CodeSandbox newInstance(String type) {
        //外接不同的判题实现可如以下方式。目前支持Java本地
        /**
        switch (type) {
            case "example":
                return new ExampleCodeSandbox();
            case "remote":
                return new RemoteCodeSandbox();
            default:
                return new ExampleCodeSandbox();
        }**/
        return new RemoteCodeSandbox();
    }
}
