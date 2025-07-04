package org.example.talker.pool;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.concurrent.Executor;

@Configuration
public class ThreadPoolConfig {

    @Bean
    public Executor taskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(2);
        executor.setMaxPoolSize(2);
        executor.setQueueCapacity(500);
        executor.setThreadNamePrefix("TalkerThread-");
        executor.initialize();

        // 包装为支持上下文传递的线程池
        return new RequestContextExecutor(executor);
    }

    // 自定义Executor，用于传递RequestContext
    static class RequestContextExecutor implements Executor {
        private final Executor delegate;

        public RequestContextExecutor(Executor delegate) {
            this.delegate = delegate;
        }

        @Override
        public void execute(Runnable task) {
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            delegate.execute(() -> {
                try {
                    // 传递主线程的RequestContext到异步线程
                    RequestContextHolder.setRequestAttributes(attributes);
                    task.run();
                } finally {
                    // 清理线程的RequestContext
                    RequestContextHolder.resetRequestAttributes();
                }
            });
        }
    }
}
