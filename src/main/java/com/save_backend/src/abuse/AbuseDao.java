package com.save_backend.src.abuse;

import com.save_backend.src.abuse.model.PostAbuseReq;
import com.save_backend.src.abuse.model.PostAbuseSuspectReq;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;

@Repository
public class AbuseDao {

    private JdbcTemplate jdbcTemplate;

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


    /**
     * Validation
     * 아동 존재(active)하는지 idx 확인
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
