package com.save_backend.src.get.child;

import com.save_backend.src.get.child.model.GetChildListRes;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class ChildListDao {

    private final JdbcTemplate jdbcTemplate;


    public List<GetChildListRes> getChildList(int userIdx) {
        String getChildListQuery =
                "select child_idx, child_name, child_gender, child_age, child_address from child " +
                "where status = 'ACTIVE' and user_idx = ?";

        return this.jdbcTemplate.query(getChildListQuery,
                (resultSet, rowNum) -> new GetChildListRes(
                        resultSet.getInt("child_idx"),
                        resultSet.getString("child_name"),
                        resultSet.getString("child_gender"),
                        resultSet.getString("child_age"),
                        resultSet.getString("child_address")
                ), userIdx);
    }

    public boolean isUserExist(int userIdx){
        String checkUserExistQuery = "select exists(select user_idx from user where status = 'ACTIVE' and user_idx = ?)";

        return this.jdbcTemplate.queryForObject(checkUserExistQuery, boolean.class, userIdx);
    }
}
