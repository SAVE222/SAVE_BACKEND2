package com.save_backend.src.suspect;

import com.save_backend.src.suspect.model.GetSuspectRes;
import com.save_backend.src.suspect.model.PostSuspectReq;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;

@Repository
public class SuspectDao {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource){
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public int createSuspect(PostSuspectReq postSuspectReq) {
        String createSuspectQuery = "INSERT INTO suspect " +
                "(child_idx, suspect_name, suspect_gender, suspect_age, suspect_address, suspect_detail_address, relation_with_child, suspect_etc)" +
                " VALUES (?,?,?,?,?,?,?,?);";
        Object[] createSuspectParams = new Object[]{
                postSuspectReq.getChildIdx(),
                postSuspectReq.getSuspectName(),
                postSuspectReq.getSuspectGender(),
                postSuspectReq.getSuspectAge(),
                postSuspectReq.getSuspectAddress(),
                postSuspectReq.getSuspectDetailAddress(),
                postSuspectReq.getRelationWithChild(),
                postSuspectReq.getSuspectEtc()
        };

        this.jdbcTemplate.update(createSuspectQuery, createSuspectParams);
        String lastInsertQuery = "select last_insert_id()";
        return this.jdbcTemplate.queryForObject(lastInsertQuery,int.class);
    }

    public GetSuspectRes getSuspectByIdx(int suspectIdx){
        String getSuspectByIdxQuery = "SELECT\n" +
                "    suspect_name, suspect_gender, suspect_age, suspect_address, suspect_detail_address, suspect_etc\n" +
                "FROM suspect\n" +
                "WHERE suspect_idx = ?;";
        int getSuspectByIdxParams = suspectIdx;
        String x;
        return this.jdbcTemplate.queryForObject(getSuspectByIdxQuery,
                (rs, rowNum) -> new GetSuspectRes(
                        rs.getString("suspect_name"),
                        rs.getString("suspect_gender"),
                        rs.getString("suspect_age"),
                        rs.getString("suspect_address"),
                        rs.getString("suspect_detail_address"),
                        rs.getString("suspect_etc")),
                getSuspectByIdxParams);
    }


    /**
     * Validation
     */
    public int checkChild(int childIdx){
        String checkChildQuery = "select exists(select child_idx from child where child_idx = ?)";
        int checkChildParams = childIdx;
        return this.jdbcTemplate.queryForObject(checkChildQuery,int.class,checkChildParams);
    }
}
