package org.example.dao;

import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;

import javax.sql.DataSource;
import java.util.Map;

@Configurable
public class UserDaoFactory {

    @Bean
    public UserDao awsUserDao(){
        return new UserDao(new AwsConnectionMaker());
    }

    @Bean
    UserDao localUserDao() {
        return new UserDao(localDataSource());
    }
    @Bean
    DataSource awsDataSource() {
        Map<String, String> env=System.getenv();
        SimpleDriverDataSource dataSource=new SimpleDriverDataSource();
        dataSource.setDriverClass(com.mysql.cj.jdbc.Driver.class);
        dataSource.setUrl(env.get("DB_HOST"));
        dataSource.setUsername(env.get("DB_USER"));
        dataSource.setPassword(env.get("DB_PASSWORD"));

        return dataSource;
    }

    @Bean
    DataSource localDataSource() {
        Map<String, String> env=System.getenv();
        SimpleDriverDataSource dataSource=new SimpleDriverDataSource();
        dataSource.setDriverClass(com.mysql.cj.jdbc.Driver.class);
        dataSource.setUrl("localhost");
        dataSource.setUsername(env.get("minji"));
        dataSource.setPassword(env.get("12345678"));

        return dataSource;
    }
}
