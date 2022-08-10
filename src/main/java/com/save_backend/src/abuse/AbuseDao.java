package com.save_backend.src.abuse;

import com.save_backend.src.abuse.model.*;
import com.save_backend.src.suspect.model.PatchSuspectReq;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.Collections;
import java.util.List;

@Repository
public class AbuseDao {

    private JdbcTemplate jdbcTemplate;
    private GetAbuseSuspectRes getAbuseSuspectRes;
    private List<GetAbusePicRes> getAbusePicRes;
    private List<GetAbuseVidRes> getAbuseVidRes;
    private List<GetAbuseRecRes> getAbuseRecRes;

    @Autowired
    public void setDataSource(DataSource dataSource){
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }


    public int insertAbuse(PostAbuseReq postAbuseReq) {
        String insertAbuseQuery = "insert into abuse_situation(abuse_child_idx, abuse_date, abuse_time, abuse_place, abuse_type, detail_description, etc) " +
                "VALUES (?, ?, ? ,?, ?, ?, ?);";
        Object[] insertAbuseParams = new Object[]{
                postAbuseReq.getChildIdx(),
                postAbuseReq.getDate(),
                postAbuseReq.getTime(),
                postAbuseReq.getPlace(),
                postAbuseReq.getType(),
                postAbuseReq.getDetail(),
                postAbuseReq.getEtc()
        };

        this.jdbcTemplate.update(insertAbuseQuery, insertAbuseParams);

        String lastInsertIdQuery = "select last_insert_id()";
        return this.jdbcTemplate.queryForObject(lastInsertIdQuery, int.class);
    }

    public int insertAbuseSuspect(int abuseIdx, int suspectIdx) {
        String insertAbuseSuspectQuery = "insert into situation_suspect_relation(abuse_idx_relation, suspect_idx_relation) " +
                "VALUES (?, ?)";
        Object[] insertAbuseSuspectParams = new Object[]{
                abuseIdx,
                suspectIdx
        };
        this.jdbcTemplate.update(insertAbuseSuspectQuery, insertAbuseSuspectParams);

        String lastInserIdQuery = "select last_insert_id()";
        return this.jdbcTemplate.queryForObject(lastInserIdQuery,int.class);
    }

    public GetAbuseRes getAbuseByIdx(int abuseIdx){
        String getAbuseByIdxQuery = "SELECT abuse_idx, abuse_date, abuse_time, abuse_place, abuse_type, detail_description, etc, create_date\n" +
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
                        rs.getDate("create_date"),
                        getAbusePicRes = this.jdbcTemplate.query(
                                "SELECT pic_path " +
                                        "FROM picture " +
                                        "WHERE pic_abuse_idx = ?;\n",
                                (rk,rownum) -> new GetAbusePicRes(
                                        rk.getString("pic_path")
                                ),rs.getInt("abuse_idx")
                        ),
                        getAbuseVidRes = this.jdbcTemplate.query(
                                "SELECT vid_path, thumbnail_path FROM video " +
                                        "WHERE vid_abuse_idx = ?;\n",
                                (rk,rownum) -> new GetAbuseVidRes(
                                        rk.getString("vid_path"),
                                        rk.getString("thumbnail_path")
                                ),rs.getInt("abuse_idx")
                        ),
                        getAbuseRecRes = this.jdbcTemplate.query(
                                "SELECT rec_path, recording_name FROM recording " +
                                        "WHERE rec_abuse_idx = ?;\n",
                                (rk,rownum) -> new GetAbuseRecRes(
                                        rk.getString("rec_path"),
                                        rk.getString("recording_name")
                                ),rs.getInt("abuse_idx")
                        ),
                        getAbuseSuspectRes = getAbuseSuspect(getAbuseByIdxParams)
                ),getAbuseByIdxParams);
    }

    public GetAbuseSuspectRes getAbuseSuspect(int abuse_idx){
        try{
            return this.jdbcTemplate.queryForObject(
                    "SELECT s.suspect_name, s.suspect_gender, s.suspect_age, s.suspect_address, s.suspect_detail_address, s.relation_with_child " +
                            "FROM suspect as s join situation_suspect_relation as ss on ss.suspect_idx_relation = s.suspect_idx\n" +
                            "        WHERE ss.abuse_idx_relation = ?;\n",
                    (rk,rownum) -> new GetAbuseSuspectRes(
                            rk.getString("suspect_name"),
                            rk.getString("suspect_gender"),
                            rk.getString("suspect_age"),
                            rk.getString("suspect_address"),
                            rk.getString("suspect_detail_address"),
                            rk.getString("relation_with_child")
                    ),abuse_idx);
        }catch(EmptyResultDataAccessException e){
            return null;
        }
    }


    public int modifyAbuse(PatchAbuseReq patchAbuseReq, int abuseIdx){

        String modifyAbuseQuery = "UPDATE abuse_situation " +
                "SET abuse_date = ?, abuse_time = ?, abuse_place = ?, abuse_type = ?, detail_description = ?, etc = ?, edit_date = current_date\n" +
                "WHERE abuse_idx = ?;";

        Object[] modifyAbuseParams = new Object[]{
                patchAbuseReq.getDate(),
                patchAbuseReq.getTime(),
                patchAbuseReq.getPlace(),
                patchAbuseReq.getType(),
                patchAbuseReq.getDetail(),
                patchAbuseReq.getEtc(),
                abuseIdx
        };

        return this.jdbcTemplate.update(modifyAbuseQuery, modifyAbuseParams);
    }

    public int modifyAbuseSuspect(int suspectIdx, int abuseIdx){

        String modifyAbuseSuspectQuery = "UPDATE situation_suspect_relation " +
                "SET suspect_idx_relation = ? WHERE abuse_idx_relation = ?;";

        Object[] modifyAbuseSuspectParams = new Object[]{
                suspectIdx,
                abuseIdx
        };

        return this.jdbcTemplate.update(modifyAbuseSuspectQuery, modifyAbuseSuspectParams);
    }


    public PatchAbuseDelRes deleteAbuse(int abuseIdx) {

        String getAbuseByIdxQuery =
                "SELECT abuse_date, abuse_time, abuse_place, abuse_type, detail_description, etc\n" +
                        "FROM abuse_situation WHERE abuse_idx = ?;";

        // abuse 테이블에서 삭제
        String deleteAbuseQuery = "update abuse_situation set status = 'INACTIVE' where abuse_idx = ?";
        int deleteAbuseParam = abuseIdx;
        this.jdbcTemplate.update(deleteAbuseQuery, deleteAbuseParam);

        // 관련된 picture, video, recording 파일 삭제
        String deletePictureAboutAbuseQuery = "UPDATE picture SET status = 'INACTIVE' WHERE pic_abuse_idx = ?;";
        String deleteVideoAboutAbuseQuery = "UPDATE video SET status = 'INACTIVE' WHERE vid_abuse_idx = ?;";
        String deleteRecordingAboutAbuseQuery = "UPDATE recording SET status = 'INACTIVE' WHERE rec_abuse_idx = ?;";
        this.jdbcTemplate.update(deletePictureAboutAbuseQuery, deleteAbuseParam);
        this.jdbcTemplate.update(deleteVideoAboutAbuseQuery, deleteAbuseParam);
        this.jdbcTemplate.update(deleteRecordingAboutAbuseQuery, deleteAbuseParam);
        
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
