package org.example.talker.config;

import jakarta.annotation.PostConstruct;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import javax.sql.DataSource;

/**
 *   这个类是mybatis的配置类，用于配置mybatis的连接信息，包括连接地址、用户名、密码等。
 *
 * @author HumphreyLi
 * @version 1.0
 * @since 2024-11-20
 *
 */

@Configuration
@MapperScan("org.example.talker.mapper")
public class MyBatisConfig {

//    @Bean public SqlSessionFactory sqlSessionFactory(DataSource dataSource) throws Exception
//    {
//        SqlSessionFactoryBean sessionFactory = new SqlSessionFactoryBean();
//        sessionFactory.setDataSource(dataSource);
//        return sessionFactory.getObject();
//    }

}
