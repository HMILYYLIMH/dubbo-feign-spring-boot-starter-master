package com.hmily.cloud.dubbo.feign.core.excp;

/**
 * <h1>Dubbo 结果判断异常类。</h1>
 *
 * @author hmilyylimh
 *         ^_^
 * @version 0.0.1
 *         ^_^
 * @date 2020-7-29
 *
 */
public class DubboResultJudgeException extends RuntimeException {

    /** 返回码 */
    private String errorCode;
    /** 信息 */
    private String errorMsg;

    /**
     * 构造函数
     */
    public DubboResultJudgeException() {
        super();
    }

    /**
     * 构造函数
     *
     * @param errorCode
     */
    public DubboResultJudgeException(String errorCode) {
        super(errorCode);
    }

    /**
     * 构造函数
     *
     * @param cause
     */
    public DubboResultJudgeException(Throwable cause) {
        super(cause);
    }

    /**
     * 构造函数
     *
     * @param errorCode
     * @param cause
     */
    public DubboResultJudgeException(String errorCode, Throwable cause) {
        super(errorCode, cause);
        this.errorCode = errorCode;
    }

    /**
     * 构造函数
     *
     * @param errorCode
     * @param errorMsg
     */
    public DubboResultJudgeException(String errorCode, String errorMsg) {
        super(errorCode + ": " + errorMsg);
        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
    }

    /**
     * 构造函数
     *
     * @param errorCode
     * @param errorMsg
     * @param cause
     */
    public DubboResultJudgeException(String errorCode, String errorMsg, Throwable cause) {
        super(errorCode + ": " + errorMsg, cause);
        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public String getErrorMsg() {
        return errorMsg;
    }
}