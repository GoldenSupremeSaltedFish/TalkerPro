package org.example.talker.Exception;

import org.mybatis.logging.Logger;
import org.mybatis.logging.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.function.Supplier;
/**
 * 这个类用于处理全局异常，并返回统一的错误响应。
 *
 * @author HumphreyLi
 * @version 1.0
 * @since 2024-11-20
 *
 */
@ControllerAdvice(basePackages = "org.example.talker.controller")
public class ExceptionAdvice {
    private static final Logger logger = LoggerFactory.getLogger(ExceptionAdvice.class);

    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public ResponseEntity<String> handleException(Exception e) {
        // 记录异常日志
        Supplier<String> errorMessageSupplier = () -> "请求处理发生错误：" + e.getMessage();
        logger.error(errorMessageSupplier,e);


        // 返回统一的错误响应
        return new ResponseEntity<>("请求处理发生错误：" + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }


}

