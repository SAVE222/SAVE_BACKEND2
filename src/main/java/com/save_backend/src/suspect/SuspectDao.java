package com.save_backend.src.suspect;

import com.save_backend.src.suspect.model.GetSuspectRes;
import com.save_backend.src.suspect.model.PatchSuspectReq;
import com.save_backend.src.suspect.model.PatchSuspectRes;
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
    public PatchSuspectRes modifyCertainSuspect(int suspectIdx, PatchSuspectReq patchSuspectReq){
        // 1) 해당 학대의심자의 정보 수정
        String modifyCertainSuspectQuery = "UPDATE suspect\n" +
                "SET suspect_name = ?, suspect_gender = ?, suspect_age = ?,\n" +
                "    suspect_address = ?, suspect_detail_address = ?, relation_with_child = ?, suspect_etc = ?," +
                "edit_date = current_date\n" +
                "WHERE suspect_idx = ?;";
        Object[] modifyCertainSuspectParams = new Object[]{
                patchSuspectReq.getSuspectName(),
                patchSuspectReq.getSuspectGender(),
                patchSuspectReq.getSuspectAge(),
                patchSuspectReq.getSuspectAddress(),
                patchSuspectReq.getSuspectDetailAddress(),
                patchSuspectReq.getRelationWithChild(),
                patchSuspectReq.getSuspectEtc(),
                suspectIdx
        };
        this.jdbcTemplate.update(modifyCertainSuspectQuery, modifyCertainSuspectParams);

        // 2) 수정된 학대의심자 정보 조회
        String getSuspectByIdxQuery = "SELECT\n" +
                "    suspect_name, suspect_gender, suspect_age, suspect_address, suspect_detail_address, relation_with_child, suspect_etc\n" +
                "FROM suspect\n" +
                "WHERE suspect_idx = ?;";
        int getSuspectByIdxParams = suspectIdx;
        return this.jdbcTemplate.queryForObject(getSuspectByIdxQuery,
                (rs, rowNum) -> new PatchSuspectRes(
                        suspectIdx,
                        rs.getString("suspect_name"),
                        rs.getString("suspect_gender"),
                        rs.getString("suspect_age"),
                        rs.getString("suspect_address"),
                        rs.getString("suspect_detail_address"),
                        rs.getString("relation_with_child"),
                        rs.getString("suspect_etc")
                        ),
                getSuspectByIdxParams);
    }

    public int deleteCertainSuspect(int suspectIdx) {
        String deleteCertainSuspectQuery = "UPDATE suspect SET status = 'INACTIVE' WHERE suspect_idx = ?";
        int deleteCertainSuspectParam = suspectIdx;

        return this.jdbcTemplate.update(deleteCertainSuspectQuery,deleteCertainSuspectParam);
    }


    /**
     * Validation
     * 존재 여부 확인 = 인덱스 확인 + status가 ACTIVE인지 확인
     */
    public int checkChild(int childIdx){
        String checkChildQuery = "select exists(select child_idx from child where child_idx = ? and status = 'ACTIVE')";
        int checkChildParams = childIdx;
        return this.jdbcTemplate.queryForObject(checkChildQuery,int.class,checkChildParams);
    }
    public int checkSuspect(int suspectIdx){
        String checkChildQuery = "select exists(select suspect_idx from suspect where suspect_idx = ? and status = 'ACTIVE')";
        int checkChildParams = suspectIdx;
        return this.jdbcTemplate.queryForObject(checkChildQuery,int.class,checkChildParams);
    }
}
