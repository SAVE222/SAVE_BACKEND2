package com.save_backend.src.user;

import com.save_backend.src.user.model.GetAlarmRes;
import com.save_backend.src.user.model.PatchAlarmRes;
import com.save_backend.src.user.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class UserDao {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public UserDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public int generalSignUp(PostUserReq postUserReq) {
        String signUpQuery = "insert into user (is_sns_auth, user_name, user_phone_number, email, password) VALUES (?,?,?,?,?)";
        Object[] signUpUserParams = new Object[]{postUserReq.getIsSnsAuth(), postUserReq.getName(), postUserReq.getPhone(), postUserReq.getEmail(), postUserReq.getPassword()};

        this.jdbcTemplate.update(signUpQuery, signUpUserParams);

        String lastInsertIdQuery = "select last_insert_id()";
        return this.jdbcTemplate.queryForObject(lastInsertIdQuery,int.class);
    }

    //회원가입 시 이용
    public boolean isExistPhone(String phone) {
        String checkPhoneQuery = "select exists (select user_phone_number from user where user_phone_number = ? and status = 'ACTIVE')";
        String checkPhoneParam = phone;

        return this.jdbcTemplate.queryForObject(checkPhoneQuery,
                boolean.class,
                checkPhoneParam);
    }

    //회원가입 시 이용
    public boolean isExistEmail(String email) {
        String checkEmailQuery = "select exists (select email from user where email = ? and status = 'ACTIVE')";
        String checkEmailParam = email;

        return this.jdbcTemplate.queryForObject(checkEmailQuery,
                boolean.class,
                checkEmailParam);
    }

    public GetUserInfoRes getUserInfo(int userIdx) {
        String getUserInfoQuery = "select user_name, user_phone_number, email from user where user_idx = ?";
        int getUserInfoParam = userIdx;

        return this.jdbcTemplate.queryForObject(getUserInfoQuery,
                (resultSet, rowNum) -> new GetUserInfoRes(
                        resultSet.getString("user_name"),
                        resultSet.getString("user_phone_number"),
                        resultSet.getString("email")
                ), getUserInfoParam);
    }

    public boolean isActiveUser(int userIdx) {
        String checkUserQuery = "select exists (select status from user where user_idx = ? and status = 'ACTIVE')";
        int checkUserParam = userIdx;

        return this.jdbcTemplate.queryForObject(checkUserQuery,
                boolean.class,
                checkUserParam);
    }

    public PutUserInfoRes modifyUserInfo(int userIdx, PutUserInfoReq putUserInfoReq) {
        String modifyUserInfoQuery = "update user set user_name = ?, user_phone_number = ?, email = ? where user_idx = ?";
        Object[] modifyUserInfoParams = new Object[]{putUserInfoReq.getName(), putUserInfoReq.getPhone(), putUserInfoReq.getEmail(), userIdx};

        this.jdbcTemplate.update(modifyUserInfoQuery, modifyUserInfoParams);

        return new PutUserInfoRes("수정이 완료되었습니다.");
    }

    //회원정보 수정 시 이용
    public boolean isExistPhone(int userIdx, String phone) {
        String checkPhoneQuery = "select exists (select user_phone_number from user where user_phone_number = ? and status = 'ACTIVE' and not user_idx = ?)";
        Object[] checkPhoneParams = new Object[]{phone, userIdx};

        return this.jdbcTemplate.queryForObject(checkPhoneQuery,
                boolean.class,
                checkPhoneParams);
    }

    //회원정보 수정 시 이용
    public boolean isExistEmail(int userIdx, String email) {
        String checkEmailQuery = "select exists (select email from user where email = ? and status = 'ACTIVE' and not user_idx = ?)";
        Object[] checkEmailParams = new Object[]{email, userIdx};

        return this.jdbcTemplate.queryForObject(checkEmailQuery,
                boolean.class,
                checkEmailParams);
    }

    public PatchUserRes deleteUser(int userIdx) {
        String getUserInfoQuery = "select user_name, user_phone_number, email from user where user_idx = ?";

        String deleteUserQuery = "update user set status = 'INACTIVE' where user_idx = ?";
        int deleteUserParam = userIdx;

        this.jdbcTemplate.update(deleteUserQuery, deleteUserParam);

        return this.jdbcTemplate.queryForObject(getUserInfoQuery,
                (resultSet, rowNum) -> new PatchUserRes(
                        resultSet.getString("user_name"),
                        resultSet.getString("user_phone_number"),
                        resultSet.getString("email"),
                        "탈퇴가 완료되었습니다."
                ), deleteUserParam);
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
}
