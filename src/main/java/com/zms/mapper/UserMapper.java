package com.zms.mapper;

import com.zms.vo.User;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

/**
 * @Author:zms
 * @Description:UserMapper
 * @Date:Create On 2018/4/26 15:22
 */

@Mapper
@Repository("UserMapper")
public interface UserMapper {

    /**
     * @Author:zms
     *
     * @Description:添加用户
     *
     * @Date:2018/4/26 15:38
     *
     */
    @Insert("insert into user(username,password,salt) values(#{username},#{password},#{salt})")
    public int addUser(User user);

    /**
     * @Author:zms
     *
     * @Description:用户登陆
     *
     * @Date:2018/4/26 15:38
     *
     */
    @Select("select username,password from user where username=#{userName} and password=#{password}")
    public User login(@Param("userName") String userName,@Param("password") String password);

    /**
     * @Author:zms
     *
     * @Description:根据用户名查找用户信息
     *
     * @Date:2018/4/27 14:32
     *
     */
    @Select("select * from user where username=#{userName}")
    public User queryUserByName(String userName);
}
