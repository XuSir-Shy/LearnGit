package my.filter;

import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.Principal;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Locale;
import java.util.Map;

@Component
public class loginFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request1 = (HttpServletRequest)request;
        HttpServletResponse response1 = (HttpServletResponse)response;
        HttpSession session = request1.getSession();
        Object manager2 = session.getAttribute("managerinfo");
        Object customer2 = session.getAttribute("customerInfo");
        String url = request1.getRequestURI();
        if(url.equals("/manager/customerList")||url.equals("/customer/info")){//
            if(manager2==null&&customer2==null){
                //回到登录页面
                request.getRequestDispatcher("/login/loginPage").forward(request,response);
            }
            else {
                filterChain.doFilter(request,response);
            }
        }
        else {
            filterChain.doFilter(request,response);
        }


    }

    @Override
    public void destroy() {

    }
}
