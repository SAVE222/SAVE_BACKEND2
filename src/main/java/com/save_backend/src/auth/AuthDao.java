package com.save_backend.src.auth;

import com.save_backend.src.auth.model.GetAlarmRes;
import com.save_backend.src.auth.model.PatchAlarmRes;
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

    public GetAlarmRes getAlarm(int userIdx) {
        String getAlarmQuery = "SELECT is_alarm FROM user WHERE user_idx = ?";
        int getAlarmParam = userIdx;

        return this.jdbcTemplate.queryForObject(getAlarmQuery,
                (rs, rowNum) -> new GetAlarmRes(
                        userIdx,
                        rs.getBoolean("is_alarm")
                ), getAlarmParam);
    }
    public PatchAlarmRes changeAlarm(int userIdx) {
        String changeDaoQuery = "UPDATE user\n" +
                "SET user.is_alarm =\n" +
                "    IF((SELECT is_alarm FROM(SELECT u.is_alarm FROM user as u WHERE u.user_idx = ?) tmp)= true, false, true)\n" +
                "WHERE user_idx = ?;";
        Object[] changeDaoParam = new Object[]{userIdx,userIdx};
        this.jdbcTemplate.update(changeDaoQuery, changeDaoParam);

        String getAlarmQuery = "SELECT is_alarm FROM user WHERE user_idx = ?";
        int getAlarmParam = userIdx;

        PatchAlarmRes patchAlarmRes = this.jdbcTemplate.queryForObject(getAlarmQuery,
                (rs, rowNum) -> new PatchAlarmRes(
                        userIdx,
                        rs.getBoolean("is_alarm")
                ), getAlarmParam);
        return patchAlarmRes;
    }

    /**
     * validation
     */
    public int checkUser(int userIdx){
        String checkUserQuery = "select exists(select user_idx from user where user_idx = ? and status = 'ACTIVE')";
        int checkUserParams = userIdx;
        return this.jdbcTemplate.queryForObject(checkUserQuery,int.class,checkUserParams);
    }
}
