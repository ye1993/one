package cn.madcoder.one.framework.security.core.context;

import com.alibaba.ttl.TransmittableThreadLocal;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolderStrategy;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.util.Assert;


/**
 * 基于 TransmittableThreadLocal 实现的 Security Context 持有者策略
 * 目的是，避免 @Async 等异步执行时，原生 ThreadLocal 的丢失问题
 *
 *试过使用InheritableThreadLocal，这个线程变量的特点是基于每次在开启新线程时，
 * 会把主线程里的InheritableThreadLocal对象复制到子线程中。我们在调用SecurityContextHolder.getContext()获取对象的时候，
 * 如果父线程已经登录，有这个对象，我开启多线程任务，第一次创建线程，是会从父线程拿到登录对象放到子线程。但是由于我用的线程池，
 * 其他地方可能已经创建过这个线程，我只是从线程池复用这个线程做多线程任务，那么我子线程调用SecurityContextHolder.getContext()拿到的对象要么为空，
 * 要么就是这个线程之前登录过的用户信息，而不是现在父线程登录用户信息。
 * 用阿里提供的TransmittableThreadLocal对象，这个对象的特点就是每次调用多线程任务的时候会把父线程的TransmittableThreadLocal对象复制到子线程，所以对runnable方法包了一层，
 * 所以注意需要使用TtlRunnable对原来runnable套一层
 * @author mad
 */
public class TransmittableThreadLocalSecurityContextHolderStrategy implements SecurityContextHolderStrategy {

    /**
     * 使用 TransmittableThreadLocal 作为上下文
     */
    private static final ThreadLocal<SecurityContext> CONTEXT_HOLDER = new TransmittableThreadLocal<>();




    public void clearContext() {
        CONTEXT_HOLDER.remove();
    }

    public SecurityContext getContext() {
        SecurityContext ctx = CONTEXT_HOLDER.get();
        if (ctx == null) {
            ctx = createEmptyContext();
            CONTEXT_HOLDER.set(ctx);
        }
        return ctx;
    }

    public void setContext(SecurityContext context) {
        Assert.notNull(context, "Only non-null SecurityContext instances are permitted");
        CONTEXT_HOLDER.set(context);
    }

    public SecurityContext createEmptyContext() {
        return new SecurityContextImpl();
    }
}
