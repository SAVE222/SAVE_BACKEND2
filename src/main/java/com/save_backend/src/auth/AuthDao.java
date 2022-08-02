package com.save_backend.src.auth;

import com.save_backend.src.auth.model.PatchAuthReq;
import com.save_backend.src.auth.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;

@Repository
public class AuthDao {
    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource){
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }


    public User getUserByEmail(String email) {
        String getUserQuery = "SELECT user_idx, user_name, user_phone_number, email, password FROM user WHERE email = ? and status = 'ACTIVE'";
        String getUserParam = email;

        return this.jdbcTemplate.queryForObject(getUserQuery,
                (rs, rowNum) -> new User(
                        rs.getInt("user_idx"),
                        rs.getString("user_name"),
                        rs.getString("user_phone_number"),
                        rs.getString("email"),
                        rs.getString("password")
                ), getUserParam);
    }

    public int modifyPassword(int userIdx, String encryptedNewPassword) {
        String modifyPasswordQuery = "UPDATE user SET password = ? WHERE user_idx = ? AND status = 'ACTIVE';";
        Object[] modifyPasswordParam = new Object[]{encryptedNewPassword, userIdx};

        return this.jdbcTemplate.update(modifyPasswordQuery, modifyPasswordParam);
    }

    public int recreatePassword(String email, String encryptedTempPassword) {
        String modifyPasswordQuery = "UPDATE user SET password = ? WHERE email = ? AND status = 'ACTIVE';";
        Object[] modifyPasswordParam = new Object[]{encryptedTempPassword, email};

        return this.jdbcTemplate.update(modifyPasswordQuery, modifyPasswordParam);
    }

    /**
     * validation
     */
    public int checkUser(int userIdx){
        String checkUserQuery = "select exists(select user_idx from user where user_idx = ? and status = 'ACTIVE')";
        int checkUserParams = userIdx;
        return this.jdbcTemplate.queryForObject(checkUserQuery,int.class,checkUserParams);
    }

    public int checkEmail(String email){
        String checkEmailQuery = "select exists(select email from user where email = ? and status = 'ACTIVE')";
        String checkEmailParams = email;
        return this.jdbcTemplate.queryForObject(checkEmailQuery,int.class,checkEmailParams);
    }

    public String getPassword(int userIdx){
        String getPasswordQuery = "select password from user where user_idx = ? and status = 'ACTIVE'";
        int getPasswordParams = userIdx;

        String passWord =  this.jdbcTemplate.queryForObject(getPasswordQuery,String.class, getPasswordParams);
        return passWord;
    }
}
