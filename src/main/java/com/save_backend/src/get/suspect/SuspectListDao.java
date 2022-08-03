package com.save_backend.src.get.suspect;

import com.save_backend.src.get.suspect.model.GetSuspectListRes;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class SuspectListDao {

    private final JdbcTemplate jdbcTemplate;


    public List<GetSuspectListRes> getSuspectList(int childIdx) {
        String getSuspectListQuery =
                "select suspect_idx, suspect_name, suspect_gender, suspect_age, suspect_address, relation_with_child from suspect " +
                "where status = 'ACTIVE' and child_idx = ? ";

        return this.jdbcTemplate.query(getSuspectListQuery,
                (resultSet, rowNum) -> new GetSuspectListRes(
                        resultSet.getInt("suspect_idx"),
                        resultSet.getString("suspect_name"),
                        resultSet.getString("suspect_gender"),
                        resultSet.getString("suspect_age"),
                        resultSet.getString("suspect_address"),
                        resultSet.getString("relation_with_child")
                ), childIdx);
    }

    public boolean isExistChild(int childIdx) {
        String checkExistChild = "select exists(select child_idx from child where status = 'ACTIVE' and child_idx = ?)";

        return this.jdbcTemplate.queryForObject(checkExistChild, boolean.class, childIdx);
    }
}
