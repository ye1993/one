package cn.madcoder.one.framework.security.core.service;

import cn.hutool.core.collection.CollUtil;
import cn.madcoder.one.framework.common.core.KeyValue;
import cn.madcoder.one.framework.common.util.cache.CacheUtils;
import cn.madcoder.one.framework.security.core.LoginUser;
import cn.madcoder.one.framework.security.core.util.SecurityFrameworkUtils;
import cn.madcoder.one.module.system.api.permission.PermissionApi;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;

import static cn.madcoder.one.framework.security.core.util.SecurityFrameworkUtils.getLoginUserId;


/**
 * 默认的 {@link SecurityFrameworkService} 实现类
 *
 * @author mad
 */
@AllArgsConstructor
public class SecurityFrameworkServiceImpl implements SecurityFrameworkService {


    private final PermissionApi permissionApi;

    /**
     * 针对 {@link #hasAnyPermissions(String...)} 的缓存
     */
    private final LoadingCache<KeyValue<Long, List<String>>, Boolean> hasAnyPermissionsCache = CacheUtils.buildAsyncReloadingCache(
            Duration.ofMinutes(1L), // 过期时间 1 分钟
            new CacheLoader<KeyValue<Long, List<String>>, Boolean>() {

                @Override
                public Boolean load(KeyValue<Long, List<String>> key) {
                    return permissionApi.hasAnyPermissions(key.getKey(), key.getValue().toArray(new String[0])).getCheckedData();
                }

            });


    @Override
    public boolean hasPermission(String permission) {
        return hasAnyPermissions(permission);
    }

    @Override
    @SneakyThrows
    public boolean hasAnyPermissions(String... permissions) {
        return hasAnyPermissionsCache.get(new KeyValue<>(getLoginUserId(), Arrays.asList(permissions)));
    }

    @Override
    public boolean hasScope(String scope) {
        return hasAnyScopes(scope);
    }

    @Override
    public boolean hasAnyScopes(String... scope) {
        LoginUser user = SecurityFrameworkUtils.getLoginUser();
        if (user == null) {
            return false;
        }
        return CollUtil.containsAny(user.getScopes(), Arrays.asList(scope));
    }



}
