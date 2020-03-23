package com.zxk175.exception.util;

import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * @author zxk175
 * @since 2020-03-20 16:46
 */
public class RequestUtils {

    public static HttpServletRequest request() {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        if (requestAttributes == null) {
            return null;
        }

        return ((ServletRequestAttributes) requestAttributes).getRequest();
    }
    
}
