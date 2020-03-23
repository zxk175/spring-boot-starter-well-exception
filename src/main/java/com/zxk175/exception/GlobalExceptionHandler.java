package com.zxk175.exception;

import cn.hutool.core.util.StrUtil;
import com.zxk175.exception.bean.ErrorDto;
import com.zxk175.exception.common.CodeMsg;
import com.zxk175.exception.common.Response;
import com.zxk175.exception.util.RequestUtils;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.validator.internal.engine.path.NodeImpl;
import org.hibernate.validator.internal.engine.path.PathImpl;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.*;

/**
 * @author zxk175
 * @since 2020-03-20 16:46
 */
@Slf4j
@Order(-1000)
@ControllerAdvice
public class GlobalExceptionHandler {

    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public String handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException ex) {
        buildExceptionInfo(ex, "请求方式处理不支持");
        HttpServletRequest request = RequestUtils.request();
        return String.format("Cannot %s %s", request.getMethod(), request.getRequestURI());
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public Object handleHttpMediaTypeNotSupportedException(HttpMediaTypeNotSupportedException ex) {
        return buildExceptionInfo(ex, "媒体类型不支持");
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler(NoHandlerFoundException.class)
    public Object handleNoHandlerFoundException(NoHandlerFoundException ex) {
        buildExceptionInfo(ex, "请求地址不存在");
        HttpServletRequest request = RequestUtils.request();
        String msg = "请求地址不存在：" + request.getRequestURI();
        return Response.fail(msg);
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Object handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        BindingResult bindingResult = ex.getBindingResult();
        List<FieldError> fieldErrors = bindingResult.getFieldErrors();

        ErrorDto errorDto;
        List<ErrorDto> errorDtoList = new ArrayList<>(fieldErrors.size());
        for (FieldError fieldError : fieldErrors) {
            errorDto = new ErrorDto()
                    .setField(fieldError.getField())
                    .setMessage(fieldError.getDefaultMessage())
                    .setRejectedValue(fieldError.getRejectedValue());

            errorDtoList.add(errorDto);
        }

        Map<Object, Object> data = new HashMap<>(8);
        data.put("errors", errorDtoList);

        buildExceptionInfo(ex, "bean参数校验异常");
        return Response.fail(CodeMsg.BIND_ERROR, data);
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler(ConstraintViolationException.class)
    public Object handleConstraintViolationException(ConstraintViolationException ex) {
        Set<ConstraintViolation<?>> violations = ex.getConstraintViolations();
        Iterator<ConstraintViolation<?>> it = violations.iterator();

        ErrorDto errorDto;
        List<ErrorDto> errorDtoList = new LinkedList<>();
        while (it.hasNext()) {
            ConstraintViolation<?> violation = it.next();
            PathImpl propertyPath = (PathImpl) violation.getPropertyPath();
            NodeImpl leafNode = propertyPath.getLeafNode();
            int parameterIndex = leafNode.getParameterIndex();

            errorDto = new ErrorDto()
                    .setField(leafNode.getName())
                    .setMessage(violation.getMessage())
                    .setRejectedValue(violation.getInvalidValue())
                    .setIndex(parameterIndex);

            errorDtoList.add(errorDto);
        }

        // 排序从小到大
        errorDtoList.sort(Comparator.comparingInt(ErrorDto::getIndex));

        Map<Object, Object> data = new HashMap<>(8);
        data.put("errors", errorDtoList);

        buildExceptionInfo(ex, "单个参数校验异常");
        return Response.fail(CodeMsg.BIND_ERROR, data);
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler(Exception.class)
    public Object handleException(Exception ex) {
        log.error("抱歉，出错了", ex);

        String message = null;
        if (ex.getClass().isAssignableFrom(RuntimeException.class)) {
            message = ex.getMessage();
        }

        return buildExceptionInfo(ex, StrUtil.isBlank(message) ? "抱歉，出错了" : message);
    }

    private Object buildExceptionInfo(Exception ex, String title) {
        return Response.fail(title);
    }

}


