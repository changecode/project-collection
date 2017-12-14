package com.fulihui.sharding.jdbc.test;

import com.fulihui.sharding.jdbc.dal.do_.StudentDO;
import com.fulihui.sharding.jdbc.dal.do_.UserDO;
import com.fulihui.sharding.jdbc.service.StudentService;
import com.fulihui.sharding.jdbc.service.UserService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;

/**
 * 测试
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(
        locations = { "classpath*:config/spring/spring-database.xml",
        "classpath*:config/spring/spring-sharding.xml" })
public class ShardingJdbcMybatisTest {

    @Resource
    public UserService userService;
    
    @Resource
    public StudentService studentService;

    @Test
    public void testUserInsert() {
        UserDO u = new UserDO();
        u.setUserId(9);
        u.setAge(25);
        u.setName("levon");
        Assert.assertEquals(userService.insert(u), true);
    }
    
    @Test
    public void testStudentInsert() {
    	for(int i = 1; i <= 10000; i++) {
	        StudentDO StudentDO = new StudentDO();
	        StudentDO.setStudentId(i);
	        StudentDO.setAge(i+20);
	        StudentDO.setName("hehe");
	        Assert.assertEquals(studentService.insert(StudentDO), true);
    	}
    }

    @Test
    public void testFindAll(){
        List<UserDO> UserDOs = userService.findAll();
        if(null != UserDOs && !UserDOs.isEmpty()){
            for(UserDO u :UserDOs){
                System.out.println(u);
            }
        }
    }
    
    @Test
    public void testSQLIN(){
        List<UserDO> UserDOs = userService.findByUserIds(Arrays.asList(12,14,17));
        if(null != UserDOs && !UserDOs.isEmpty()){
            for(UserDO u :UserDOs){
                System.out.println(u);
            }
        }
    }
    
    @Test
    public void testTransactionTestSucess(){
        userService.transactionTestSucess();
    }
    
    @Test(expected = IllegalAccessException.class)
    public void testTransactionTestFailure() throws IllegalAccessException{
        userService.transactionTestFailure();
    }
    
    
}
