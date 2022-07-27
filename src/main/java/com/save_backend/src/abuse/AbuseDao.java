package com.save_backend.src.abuse;

import com.save_backend.src.abuse.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;

@Repository
public class AbuseDao {

    private JdbcTemplate jdbcTemplate;
    private List<GetAbuseSuspectRes> getAbuseSuspectRes;

    @Autowired
    public void setDataSource(DataSource dataSource){
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }


    public int insertAbuse(String typeStr, PostAbuseReq postAbuseReq) {
        String insertAbuseQuery = "insert into abuse_situation(abuse_type, abuse_child_idx, abuse_date, abuse_time, abuse_place, detail_description, etc) " +
                "VALUES (?, ?, ? ,?, ?, ?, ?);";
        Object[] insertAbuseParams = new Object[]{
                typeStr,
                postAbuseReq.getChildIdx(),
                postAbuseReq.getDate(),
                postAbuseReq.getTime(),
                postAbuseReq.getPlace(),
                postAbuseReq.getDetail(),
                postAbuseReq.getEtc()
        };

        this.jdbcTemplate.update(insertAbuseQuery, insertAbuseParams);

        String lastInsertIdQuery = "select last_insert_id()";
        return this.jdbcTemplate.queryForObject(lastInsertIdQuery, int.class);
    }

    public int insertAbuseSuspect(int abuseIdx, PostAbuseSuspectReq postAbuseSuspectReq) {
        String insertAbuseSuspectQuery = "insert into situation_suspect_relation(abuse_idx_relation, suspect_idx_relation) " +
                "VALUES (?, ?)";
        Object[] insertAbuseSuspectParams = new Object[]{
                abuseIdx,
                postAbuseSuspectReq.getSuspectIdx()
        };
        this.jdbcTemplate.update(insertAbuseSuspectQuery, insertAbuseSuspectParams);

        String lastInserIdQuery = "select last_insert_id()";
        return this.jdbcTemplate.queryForObject(lastInserIdQuery,int.class);
    }

    public GetAbuseRes getAbuseByIdx(int abuseIdx){
        String getAbuseByIdxQuery = "SELECT abuse_idx, abuse_date, abuse_time, abuse_place, abuse_type, detail_description, etc\n" +
                "FROM abuse_situation WHERE abuse_idx = ?;";
        int getAbuseByIdxParams = abuseIdx;
        return this.jdbcTemplate.queryForObject(getAbuseByIdxQuery,
                (rs, rowNum) -> new GetAbuseRes(
                        rs.getInt("abuse_idx"),
                        rs.getString("abuse_date"),
                        rs.getString("abuse_time"),
                        rs.getString("abuse_place"),
                        rs.getString("abuse_type"),
                        rs.getString("detail_description"),
                        rs.getString("etc"),
                        getAbuseSuspectRes = this.jdbcTemplate.query(
                                "SELECT s.suspect_name, s.suspect_gender, s.suspect_age, s.suspect_address " +
                                        "FROM suspect as s join situation_suspect_relation as ss on ss.suspect_idx_relation = s.suspect_idx\n" +
                                        "        WHERE ss.abuse_idx_relation = ?;\n",
                                (rk,rownum) -> new GetAbuseSuspectRes(
                                        rk.getString("suspect_name"),
                                        rk.getString("suspect_gender"),
                                        rk.getString("suspect_age"),
                                        rk.getString("suspect_address")
                                ),rs.getInt("abuse_idx"))),getAbuseByIdxParams);
    }


    public int modifyAbuse(PatchAbuseReq patchAbuseReq, int abuseIdx){
        //학대 유형 list -> string 변환
        List<String> type = patchAbuseReq.getType();
        StringBuilder sb = new StringBuilder();
        for (String s : type) {
            sb.append(s);
            sb.append(",");
        }
        String typeStr = sb.toString();

        String modifyAbuseQuery = "UPDATE abuse_situation " +
                "SET abuse_date = ?, abuse_time = ?, abuse_place = ?, abuse_type = ?, detail_description = ?, etc = ?, edit_date = current_date\n" +
                "WHERE abuse_idx = ?;";

        Object[] modifyAbuseParams = new Object[]{
                patchAbuseReq.getDate(),
                patchAbuseReq.getTime(),
                patchAbuseReq.getPlace(),
                typeStr,
                patchAbuseReq.getDetail(),
                patchAbuseReq.getEtc(),
                abuseIdx
        };

        return this.jdbcTemplate.update(modifyAbuseQuery, modifyAbuseParams);
    }

    public void deleteAbuseSuspect(int abuseIdx) {
        String deleteAbuseSuspectQuery = "delete from situation_suspect_relation where abuse_idx_relation = ?";
        int deleteAbuseParam = abuseIdx;

        this.jdbcTemplate.update(deleteAbuseSuspectQuery, deleteAbuseParam);
    }

    public int modifyAbuseSuspect(int abuseIdx, PatchAbuseSuspectReq patchAbuseSuspectReq) {
        String modifyAbuseSuspectQuery = "insert into situation_suspect_relation(abuse_idx_relation, suspect_idx_relation) " +
                "VALUES (?, ?)";
        Object[] modifyAbuseSuspectParams = new Object[]{
                abuseIdx,
                patchAbuseSuspectReq.getSuspectIdx()
        };
        this.jdbcTemplate.update(modifyAbuseSuspectQuery, modifyAbuseSuspectParams);

        String lastInserIdQuery = "select last_insert_id()";
        return this.jdbcTemplate.queryForObject(lastInserIdQuery,int.class);
    }


    public PatchAbuseDelRes deleteAbuse(int abuseIdx) {

        String getAbuseByIdxQuery =
                "SELECT abuse_date, abuse_time, abuse_place, abuse_type, detail_description, etc\n" +
                        "FROM abuse_situation WHERE abuse_idx = ?;";

        String deleteAbuseQuery = "update abuse_situation set status = 'INACTIVE' where abuse_idx = ?";
        int deleteAbuseParam = abuseIdx;

        this.jdbcTemplate.update(deleteAbuseQuery, deleteAbuseParam);

        return this.jdbcTemplate.queryForObject(getAbuseByIdxQuery,
                (rs, rowNum) -> new PatchAbuseDelRes(
                        rs.getString("abuse_date"),
                        rs.getString("abuse_time"),
                        rs.getString("abuse_place"),
                        rs.getString("abuse_type"),
                        rs.getString("detail_description"),
                        rs.getString("etc"),
                        "학대 정황 삭제가 완료되었습니다."
                ), deleteAbuseParam);
    }


    /**
     * Validation
     */
    public int checkChild(int childIdx){
        String checkChildQuery = "select exists(select child_idx from child where child_idx = ? and status = 'ACTIVE')";
        int checkChildParams = childIdx;
        return this.jdbcTemplate.queryForObject(checkChildQuery,int.class,checkChildParams);
    }
    public int checkSuspect(int suspectIdx){
        String checkSuspectQuery = "select exists(select suspect_idx from suspect where suspect_idx = ? and status = 'ACTIVE')";
        int checkSuspectParams = suspectIdx;
        return this.jdbcTemplate.queryForObject(checkSuspectQuery,int.class,checkSuspectParams);
    }
    public int checkAbuse(int abuseIdx){
        String checkAbuseQuery = "select exists(select abuse_idx from abuse_situation where abuse_idx = ? and status = 'ACTIVE')";
        int checkAbuseParams = abuseIdx;
        return this.jdbcTemplate.queryForObject(checkAbuseQuery,int.class,checkAbuseParams);
    }
}
