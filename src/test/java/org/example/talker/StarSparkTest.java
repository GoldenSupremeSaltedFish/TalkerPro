package org.example.talker;

import org.example.talker.util.Impl.StarSparkAuthUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;

/**
 * @author HumphreyLi
 * @version 1.0
 * @brief
 * @details
 * @date 2024/11/26
 * @time 上午2:03
 */
public class StarSparkTest {




    @Test
    public void testGenerateAuthUrl() throws Exception {
        // 调用 generateAuthUrl 方法
        String authUrl = StarSparkAuthUtils.generateAuthUrl();

        // 断言生成的授权 URL 不为空
        Assertions.assertNotNull(authUrl);
        // 打印生成的授权 URL
        System.out.println("Generated Authorization URL: " + authUrl);


    }
}
