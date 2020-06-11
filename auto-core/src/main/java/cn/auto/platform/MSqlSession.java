package cn.auto.platform;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by chenmeng on 2017/4/19.
 */
public class MSqlSession {
    private static SqlSessionFactoryBuilder builder;
    private static SqlSession sqlSession;
    private static SqlSessionFactory factory;
    private static InputStream input;


    public static void close() {
        if (sqlSession != null) {
            sqlSession.close();
        }
        if (input != null) {
            try {
                input.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static SqlSession getSqlSession() {
        input = MSqlSession.class.getClassLoader().getResourceAsStream("config/mybatis.xml");
        builder = new SqlSessionFactoryBuilder();
        factory = builder.build(input);
        sqlSession = factory.openSession(true);
        return sqlSession;
    }
}
