package org.example.starksparkservice.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

/**
 *   这个类是mybatis的配置类，用于配置mybatis的连接信息，包括连接地址、用户名、密码等。
 *
 * @author HumphreyLi
 * @version 1.0
 * @since 2024-11-20
 *
 */

@Configuration
@MapperScan("org.example.starksparkservice.mapper")
public class MyBatisConfig {

//    @Bean public SqlSessionFactory sqlSessionFactory(DataSource dataSource) throws Exception
//    {
//        SqlSessionFactoryBean sessionFactory = new SqlSessionFactoryBean();
//        sessionFactory.setDataSource(dataSource);
//        return sessionFactory.getObject();
//    }

}
