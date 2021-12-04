package com.hxy.springboot.starter.auth;

import com.hxy.springboot.starter.SpringContextHolder;
import com.hxy.springboot.starter.anno.Auth;
import com.hxy.springboot.starter.anno.Security;
import com.hxy.springboot.starter.anno.SecurityController;
import com.hxy.springboot.starter.model.BizException;
import com.hxy.springboot.starter.model.BizHttpCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;


public abstract class AuthInterceptorAdapter extends HandlerInterceptorAdapter implements CommandLineRunner {
    private final Logger logger = LoggerFactory.getLogger(AuthInterceptorAdapter.class);
    private static final Map<Method, String[]> ROUTE_AUTH_MAPPING = new HashMap<>();

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }

        String[] auth = ROUTE_AUTH_MAPPING.get(((HandlerMethod) handler).getMethod());
        if (auth == null) {
            return true;
        }

        String authorion = request.getHeader("Authorion");
        if (auth.length == 0 && checkLogin(authorion)) {
            return true;
        }
        if (auth.length == 0 && checkAuth(authorion)) {
            return true;
        }

        throw new BizException(BizHttpCode.C_401);
    }


    public abstract boolean checkLogin(String authorion);

    public abstract boolean checkAuth(String authorion);

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
                           ModelAndView modelAndView) throws Exception {
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
            throws Exception {
    }

    @Override
    public void run(String... args) throws Exception {
        Map<RequestMappingInfo, HandlerMethod> handlerMethods = SpringContextHolder.getBean(RequestMappingHandlerMapping.class).getHandlerMethods();
        handlerMethods.forEach((k, v) -> {
            Auth auth = v.getMethod().getAnnotation(Auth.class);
            if (auth != null) {
                ROUTE_AUTH_MAPPING.put(v.getMethod(), auth.role());
            } else {
                Security security = v.getMethod().getAnnotation(Security.class);
                SecurityController securityController = v.getBeanType().getAnnotation(SecurityController.class);
                if (security != null || securityController != null) {
                    ROUTE_AUTH_MAPPING.put(v.getMethod(), new String[]{});
                }
            }
        });
    }
}
