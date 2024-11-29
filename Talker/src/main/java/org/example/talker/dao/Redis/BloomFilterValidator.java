package org.example.talker.dao.Redis;

import org.redisson.api.RBloomFilter;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class BloomFilterValidator {

    private final RedissonClient redisson;
    private final RBloomFilter<String> bloomFilter;

    @Autowired
    BloomFilterValidator(RedissonClient redisson) {
        this.redisson = redisson;
        this.bloomFilter = redisson.getBloomFilter("myBloomFilter");

        // 初始化布隆过滤器，设置预期元素数量和误判率
        this.bloomFilter.tryInit(55000000L, 0.03);
    }

    // 添加key到布隆过滤器
    public void addToBloomFilter(String key) {
        bloomFilter.add(key);
    }

    // 使用布隆过滤器验证key是否可能存在
    public boolean mightContain(String key) {
        return bloomFilter.contains(key);
    }

    // 在验证token时使用布隆过滤器
    public boolean validateTokenWithBloomFilter(String key) {
        if (mightContain(key)) {
            // 如果布隆过滤器表明key可能存在，再进行实际的Redis查询
            RBucket<String> bucket = redisson.getBucket(key);
            return bucket.isExists();
        }
        return false; // 布隆过滤器表明key一定不存在
    }
}
