package com.save_backend.src.child;

import com.save_backend.src.child.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;

@Repository
public class ChildDao {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource){
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }


    public int insertChild(PostChildReq postChildReq) {
        String insertChildQuery = "insert into child(user_idx, child_name, is_certain, child_gender, child_age, child_address, child_detail_address) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?);";
        Object[] insertChildParams = new Object[]{
                postChildReq.getUserIdx(), postChildReq.getName(), postChildReq.isCertain(), postChildReq.getGender(), postChildReq.getAge(), postChildReq.getAddress(), postChildReq.getDetailAddress()
        };

        this.jdbcTemplate.update(insertChildQuery, insertChildParams);

        String lastInsertIdQuery = "select last_insert_id()";
        return this.jdbcTemplate.queryForObject(lastInsertIdQuery, int.class);
    }


    public GetChildInfoRes getChildInfoByIdx(int childIdx){
        String getChildInfoByIdxQuery =
                "select child_name, is_certain, child_gender, child_age, child_address, child_detail_address " +
                        "from child where child_idx=?;";
        int getChildInfoByIdxParams = childIdx;
        return this.jdbcTemplate.queryForObject(getChildInfoByIdxQuery,
                (rs, rowNum) -> new GetChildInfoRes (
                        rs.getString("child_name"),
                        rs.getString("child_gender"),
                        rs.getString("child_age"),
                        rs.getString("child_address"),
                        rs.getString("child_detail_address")
                ), getChildInfoByIdxParams
        );
    }


    public PatchChildEditRes modifyChild(int childIdx, PatchChildEditReq patchChildEditReq){
        String modifyChildQuery = "UPDATE child\n" +
                "SET child_name = ?, is_certain = ?, child_gender = ?,\n" +
                "    child_age = ?, child_address = ?, child_detail_address = ?," +
                "edit_date = current_date\n" +
                "WHERE child_idx = ?;";
        Object[] modifyChildParams = new Object[]{
                patchChildEditReq.getName(),
                patchChildEditReq.isCertain(),
                patchChildEditReq.getGender(),
                patchChildEditReq.getAge(),
                patchChildEditReq.getAddress(),
                patchChildEditReq.getDetailAddress(),
                childIdx
        };

        this.jdbcTemplate.update(modifyChildQuery, modifyChildParams);

        String modifyChildResQuery = "SELECT\n" +
                "    child_name, is_certain, child_gender, child_age, child_address, child_detail_address\n" +
                "FROM child WHERE child_idx = ?;";
        int modifyChildResParams = childIdx;
        return this.jdbcTemplate.queryForObject(modifyChildResQuery,
                (rs, rowNum) -> new PatchChildEditRes(
                        childIdx,
                        rs.getString("child_name"),
                        rs.getBoolean("is_certain"),
                        rs.getString("child_gender"),
                        rs.getString("child_age"),
                        rs.getString("child_address"),
                        rs.getString("child_detail_address"),
                        "아동 수정이 완료되었습니다."
                ),
                modifyChildResParams);
    }


    public PatchChildDelRes deleteChild(int childIdx) {
        String getChildInfoByIdxQuery =
                "select child_name, is_certain, child_gender, child_age, child_address, child_detail_address from child where child_idx=?;";

        String deleteChlidQuery = "update child set status = 'INACTIVE' where child_idx = ?";
        int deleteChildParam = childIdx;

        this.jdbcTemplate.update(deleteChlidQuery, deleteChildParam);

        return this.jdbcTemplate.queryForObject(getChildInfoByIdxQuery,
                (rs, rowNum) -> new PatchChildDelRes(
                        rs.getString("child_name"),
                        rs.getBoolean("is_certain"),
                        rs.getString("child_gender"),
                        rs.getString("child_age"),
                        rs.getString("child_address"),
                        rs.getString("child_detail_address"),
                        "아동 삭제가 완료되었습니다."
                ), deleteChildParam);
    }

    /**
     * Validation
     * 아동, 유저 존재(active)하는지 idx 확인
     */
    public int checkUser(int userIdx){
        String checkUserQuery = "select exists(select user_idx from user where user_idx = ? and status = 'ACTIVE')";
        int checkUserParams = userIdx;
        return this.jdbcTemplate.queryForObject(checkUserQuery,int.class,checkUserParams);
    }


    public int checkChild(int childIdx){
        String checkChildQuery = "select exists(select child_idx from child where child_idx = ? and status = 'ACTIVE')";
        int checkChildParams = childIdx;
        return this.jdbcTemplate.queryForObject(checkChildQuery,int.class,checkChildParams);
    }
}
