package com.save_backend.src.user;

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

    public int isExistPhone(String phone) {
        String checkPhoneQuery = "select exists (select user_phone_number from user where user_phone_number = ? and status = 'ACTIVE')";
        String checkEmailParam = phone;

        return this.jdbcTemplate.queryForObject(checkPhoneQuery,
                int.class,
                checkEmailParam);
    }

    public int isExistEmail(String email) {
        String checkEmailQuery = "select exists (select email from user where email = ? and status = 'ACTIVE')";
        String checkEmailParam = email;

        return this.jdbcTemplate.queryForObject(checkEmailQuery,
                int.class,
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

    public int isActiveUser(int userIdx) {
        String checkUserQuery = "select exists (select status from user where user_idx = ? and status = 'ACTIVE')";
        int checkUserParam = userIdx;

        return this.jdbcTemplate.queryForObject(checkUserQuery,
                int.class,
                checkUserParam);
    }

    public PutUserInfoRes modifyUserInfo(PutUserInfoReq putUserInfoReq) {
        String modifyUserInfoQuery = "update user set user_name = ?, user_phone_number = ?, email = ? where user_idx = ?";
        Object[] modifyUserInfoParams = new Object[]{putUserInfoReq.getName(), putUserInfoReq.getPhone(), putUserInfoReq.getEmail(), putUserInfoReq.getUserIdx()};

        this.jdbcTemplate.update(modifyUserInfoQuery, modifyUserInfoParams);

        return new PutUserInfoRes("수정이 완료되었습니다.");
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
}
