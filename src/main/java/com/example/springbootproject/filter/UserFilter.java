package com.example.springbootproject.filter;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebFilter(filterName = "UserFilter",urlPatterns = {"/MyInfo/*"})
//urlPatterns会把符合要求的访问跳到执行doFilter
public class UserFilter implements Filter {
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest myRequest=(HttpServletRequest) servletRequest;//强制转换
        HttpSession session = myRequest.getSession();//获取这个请求的session,这个session是在发起请求的时候就创建了
        if(session.getAttribute("email") != null){//如果记录过它的email,即登录成功过
            filterChain.doFilter(servletRequest, servletResponse);//往下一个filter跳,无filter则通过了
        }
        else{//如果没登陆成功,则拦截并跳到login界面
            session.setAttribute("errorMessage","需要登录才可进入个人信息界面!");//添加session参数-errorMessage,让login做出不同的响应(显示提示登录的讯息)
            myRequest.getRequestDispatcher("/Login").forward(myRequest,servletResponse);//跳转至@RequsetMapping("/Login")的地方
        }
//        System.out.println("--doFilter--");
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        Filter.super.init(filterConfig);
//        System.out.println("--initFilter--");
    }

    @Override
    public void destroy() {
        Filter.super.destroy();
//        System.out.println("--destroyFilter--");
    }
}
