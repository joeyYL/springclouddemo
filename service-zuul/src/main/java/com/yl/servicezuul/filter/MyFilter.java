package com.yl.servicezuul.filter;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

@Component
public class MyFilter extends ZuulFilter {

    /* filterType：返回一个字符串代表过滤器的类型，在zuul中定义了四种不同生命周期的过滤器类型，具体如下：
     * pre：路由之前
     * routing：路由之时
     * post： 路由之后
     * error：发送错误调用
     */
    @Override
    public String filterType() {
        return "pre";
    }

    /* filterOrder：过滤的顺序 */
    @Override
    public int filterOrder() {
        return 0;
    }

    /* shouldFilter：这里可以写逻辑判断，根据返回值确定是否要过滤。 true，过滤 */
    @Override
    public boolean shouldFilter() {
        return true;
    }

    /* run：过滤器的具体逻辑。可用很复杂，包括查sql，nosql去判断该请求到底有没有权限访问。 */
    @Override
    public Object run() throws ZuulException {
        RequestContext ctx = RequestContext.getCurrentContext();
        HttpServletRequest request = ctx.getRequest();
        System.out.println(request.getMethod() + " >>>> " + request.getRequestURL().toString());
        Object accessToken = request.getParameter("token");
        if (accessToken == null) {
            System.err.println("token is empty");
            ctx.setSendZuulResponse(false);
            ctx.setResponseStatusCode(401);
            try{
                ctx.getResponse().getWriter().write("token is empty");
            } catch (Exception e){
                return null;
            }
        }
        System.out.println("ok");
        return null;
    }
}
