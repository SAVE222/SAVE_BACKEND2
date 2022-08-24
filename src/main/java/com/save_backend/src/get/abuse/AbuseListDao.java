package com.save_backend.src.get.abuse;

import com.save_backend.src.get.abuse.model.GetAbuseListRes;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
@RequiredArgsConstructor
public class AbuseListDao {
    private final JdbcTemplate jdbcTemplate;


    public List<GetAbuseListRes> getAbuseList(int childIdx) {
        String getAbuseListQuery =
                "select abuse_idx, abuse_child_idx, abuse_date, abuse_time, abuse_place, create_date from abuse_situation " +
                        "where status = 'ACTIVE' and abuse_child_idx = ?";

        return this.jdbcTemplate.query(getAbuseListQuery,
                (resultSet, rowNum) -> new GetAbuseListRes(
                        resultSet.getInt("abuse_idx"),
                        resultSet.getInt("abuse_child_idx"),
                        resultSet.getString("abuse_date"),
                        resultSet.getString("abuse_time"),
                        resultSet.getString("abuse_place"),
                        resultSet.getDate("create_date")
                ), childIdx);
    }

    public boolean isChildExist(int childIdx){
        String checkAbuseExistQuery = "select exists(select child_idx from child where status = 'ACTIVE' and child_idx = ?)";

        return this.jdbcTemplate.queryForObject(checkAbuseExistQuery, boolean.class, childIdx);
    }
}
