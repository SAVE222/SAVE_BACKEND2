package com.save_backend.src.child;

import com.save_backend.src.child.model.GetChildInfoRes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;

@Repository
public class ChildDao {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource){
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public GetChildInfoRes getChildInfoByIdx(int childIdx){
        String getChildInfoByIdxQuery =
                "select childIdx, name, isCertain, gender, age, address, detailAddress from Child where childIdx=?";
        int getChildInfoByIdxParams = childIdx;
        return this.jdbcTemplate.queryForObject(getChildInfoByIdxQuery,
                (rs, rowNum) -> new GetChildInfoRes (
                        rs.getInt("childIdx"),
                        rs.getString("name"),
                        rs.getBoolean("isCertain"),
                        rs.getString("gender"),
                        rs.getString("age"),
                        rs.getString("address"),
                        rs.getString("detailAddress")
                ), getChildInfoByIdxParams
        );
    }
}
