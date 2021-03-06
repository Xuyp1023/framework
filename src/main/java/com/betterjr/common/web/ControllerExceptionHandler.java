package com.betterjr.common.web;

import java.util.List;

import org.slf4j.Logger;

import com.alibaba.dubbo.rpc.RpcException;
import com.betterjr.common.exception.BytterException;

public class ControllerExceptionHandler {
    public static String exec(ExceptionHandler run, String errorMessage, Logger logger) {
        try {
            return run.handle();
        }
        catch (RpcException btEx) {
            if (BytterException.isCauseBytterException(btEx)) {
                logger.error(btEx.getMessage(), btEx);
                List<String> errorList = ((BytterException) btEx.getCause()).getErrorList();
                if (errorList != null) {
                    logger.error(errorList.toString());
                }
                return AjaxObject.newError(btEx.getCause().getMessage()).toJson();
            }
            logger.error(btEx.getMessage(), btEx);
            return AjaxObject.newError(errorMessage).toJson();
        }
        catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            return AjaxObject.newError(errorMessage).toJson();
        }

    }

    public static interface ExceptionHandler {
        public String handle();
    }
}
