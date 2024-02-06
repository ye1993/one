package cn.madcoder.one.framework.web.core.filter;

import cn.madcoder.one.framework.common.util.servlet.ServletUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Request Body 缓存 Filter，实现它的可重复读取
 *
 * @author
 */
public class CacheRequestBodyFilter extends OncePerRequestFilter {

    /**
     * doFilterInternal 方法是 OncePerRequestFilter 类的抽象方法，它在每次请求时执行。在这里，它使用 CacheRequestBodyWrapper 包装了请求，该包装器会缓存请求体内容。
     * @param request
     * @param response
     * @param filterChain
     * @throws IOException
     * @throws ServletException
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws IOException, ServletException {
        // 使用 CacheRequestBodyWrapper 包装请求，以便后续的过滤器或处理器可以多次读取请求体内容
        filterChain.doFilter(new CacheRequestBodyWrapper(request), response);
    }

    /**
     * shouldNotFilter 方法用于判断是否应该执行过滤器。在这里，通过 ServletUtils.isJsonRequest(request) 判断请求是否为 JSON 请求，如果不是，则返回 true，表示不进行过滤。
     * @param request
     * @return
     */
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        // 只处理 json 请求内容，如果不是 json 请求，则不进行处理
        return !ServletUtils.isJsonRequest(request);
    }

}
