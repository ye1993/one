package cn.madcoder.one.framework.common.util.http;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.map.TableMap;
import cn.hutool.core.net.url.UrlBuilder;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.StrUtil;
import org.springframework.util.StringUtils;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import java.net.URI;
import java.nio.charset.Charset;
import java.util.Map;

/**
 * HTTP 工具类
 *
 * @author
 */
public class HttpUtils {


    /**
     * 这个方法的目的是替换URL中的查询参数（Query Parameters）。方法使用了 Hutool 库中的 UrlBuilder 来处理URL的构建和查询参数的修改。
     *
     *
     * @param url 原始的URL字符串。
     * @param key 要替换的查询参数的键。
     * @param value 要替换的查询参数的新值。
     * @return  URL
     */
    @SuppressWarnings("unchecked")
    public static String replaceUrlQuery(String url, String key, String value) {
        //   使用 Hutool 的 UrlBuilder 构建 URL
        UrlBuilder builder = UrlBuilder.of(url, Charset.defaultCharset());
        // 先移除指定键的查询参数
        TableMap<CharSequence, CharSequence> query = (TableMap<CharSequence, CharSequence>)
                ReflectUtil.getFieldValue(builder.getQuery(), "query");
        query.remove(key);
        // 后添加新的查询参数
        builder.addQuery(key, value);
        return builder.build();
    }

    private String append(String base, Map<String, ?> query, boolean fragment) {
        return append(base, query, null, fragment);
    }

    /**
     * 拼接 URL
     * 这个方法的目的是在给定的基础URL (base) 上附加查询参数，生成一个新的URL。该方法使用了 Spring Framework 中的 UriComponentsBuilder 类来构建和处理URL
     *
     * copy from Spring Security OAuth2 的 AuthorizationEndpoint 类的 append 方法
     *
     * @param base 基础 URL
     * @param query 查询参数
     * @param keys query 的 key，对应的原本的 key 的映射。例如说 query 里有个 key 是 xx，实际它的 key 是 extra_xx，则通过 keys 里添加这个映射
     * @param fragment URL 的 fragment，即拼接到 # 中
     * @return 拼接后的 URL
     */
    public static String append(String base, Map<String, ?> query, Map<String, String> keys, boolean fragment) {
        // 创建一个新的 UriComponentsBuilder
        UriComponentsBuilder template = UriComponentsBuilder.newInstance();
        // 从基础URL构建 UriComponentsBuilder
        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(base);
        URI redirectUri;
        try {
            // 假设它是已编码的（如果它是通过网络传输的）
            redirectUri = builder.build(true).toUri();
        } catch (Exception e) {
            // ... 但允许客户端注册包含硬编码的非编码值
            redirectUri = builder.build().toUri();
            builder = UriComponentsBuilder.fromUri(redirectUri);
        }
        // 设置新的 UriComponentsBuilder 的基础信息（scheme、port、host、userInfo、path）
        template.scheme(redirectUri.getScheme()).port(redirectUri.getPort()).host(redirectUri.getHost())
                .userInfo(redirectUri.getUserInfo()).path(redirectUri.getPath());

        if (fragment) {
            // 如果要添加到 URL 的是 fragment
            StringBuilder values = new StringBuilder();
            if (redirectUri.getFragment() != null) {
                String append = redirectUri.getFragment();
                values.append(append);
            }
            for (String key : query.keySet()) {
                if (values.length() > 0) {
                    values.append("&");
                }
                String name = key;
                if (keys != null && keys.containsKey(key)) {
                    name = keys.get(key);
                }
                values.append(name).append("={").append(key).append("}");
            }
            if (values.length() > 0) {
                template.fragment(values.toString());
            }
            UriComponents encoded = template.build().expand(query).encode();
            builder.fragment(encoded.getFragment());
        } else {
            // 如果要添加到 URL 的是 query 参数
            for (String key : query.keySet()) {
                String name = key;
                if (keys != null && keys.containsKey(key)) {
                    name = keys.get(key);
                }
                template.queryParam(name, "{" + key + "}");
            }
            template.fragment(redirectUri.getFragment());
            UriComponents encoded = template.build().expand(query).encode();
            builder.query(encoded.getQuery());
        }
        // 构建最终的 URL 并返回
        return builder.build().toUriString();
    }

    /**
     * 从HttpServletRequest中提取基本身份验证（Basic Authentication）的客户端ID和客户端密钥。
     * 基本身份验证通常在HTTP请求的Authorization头中，格式为"Basic Base64(clientId:clientSecret)"。
     *
     * @param request HttpServletRequest对象，表示当前的HTTP请求。
     * @return 包含客户端ID和客户端密钥的字符串数组，如果提取失败则返回null。
     */
    public static String[] obtainBasicAuthorization(HttpServletRequest request) {
        // 客户端ID
        String clientId;
        // 客户端密钥
        String clientSecret;
        // 先从 Header 中获取 Authorization
        String authorization = request.getHeader("Authorization");
        // 去除"Basic "前缀
        authorization = StrUtil.subAfter(authorization, "Basic ", true);
        if (StringUtils.hasText(authorization)) {
            // 如果 Authorization 存在，解码并提取 clientId 和 clientSecret
            authorization = Base64.decodeStr(authorization);
            // 从解码后的字符串中提取 clientId 和 clientSecret
            clientId = StrUtil.subBefore(authorization, ":", false);
            clientSecret = StrUtil.subAfter(authorization, ":", false);
        // 再从 Param 中获取
        } else {
            // 如果 Authorization 不存在，从参数中获取 client_id 和 client_secret
            clientId = request.getParameter("client_id");
            clientSecret = request.getParameter("client_secret");
        }

        // 如果 clientId 和 clientSecret 非空，则返回
        if (StrUtil.isNotEmpty(clientId) && StrUtil.isNotEmpty(clientSecret)) {
            return new String[]{clientId, clientSecret};
        }
        // 如果提取失败，返回 null
        return null;
    }


}
