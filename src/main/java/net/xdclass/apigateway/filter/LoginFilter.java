package net.xdclass.apigateway.filter;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import org.apache.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;

import static org.springframework.cloud.netflix.zuul.filters.support.FilterConstants.PRE_TYPE;

/**
 * 登陆过滤器
 */
@Component
public class LoginFilter extends ZuulFilter {
    /**
     * 设置过滤器类型，前置过滤器
     * @return
     */
    @Override
    public String filterType() {
        return PRE_TYPE;
    }

    /**
     * 设置过滤器执行顺序，越小越先执行
     * @return
     */
    @Override
    public int filterOrder() {
        return 4;
    }

    /**
     * 设置过滤器是否生效，true生效，false不生效
     * @return
     */
    @Override
    public boolean shouldFilter() {
        RequestContext currentContext = RequestContext.getCurrentContext();
        HttpServletRequest request=currentContext.getRequest();
        //System.out.println(request.getRequestURI());
        //System.out.println(request.getRequestURL());
        if("/apigateway/order/api/v1/order/save".equalsIgnoreCase(request.getRequestURI())){
            return true;
        }
        return false;
    }

    /**
     * 业务逻辑
     * @return
     * @throws ZuulException
     */
    @Override
    public Object run() throws ZuulException {
        //JWT
        RequestContext currentContext = RequestContext.getCurrentContext();
        HttpServletRequest request=currentContext.getRequest();

        //token对象
        String token=request.getHeader("token");
        if(StringUtils.isEmpty(token)){
            //尝试从请求参数中拿
            token=request.getParameter("token");
        }


        //登陆校验逻辑，根据公司情况自定义 JWT
        if(StringUtils.isEmpty(token)){
            currentContext.setSendZuulResponse(false);
            currentContext.setResponseStatusCode(HttpStatus.SC_UNAUTHORIZED);
        }

        return null;
    }
}
