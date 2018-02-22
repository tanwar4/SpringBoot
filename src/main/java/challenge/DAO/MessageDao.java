package challenge.DAO;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import challenge.Model.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;


@Repository
public class MessageDao {

    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private final String FOLLOWS_MSSG_SQL =
            "SELECT  f.PERSON_ID,p.handle,m.content "+
            "FROM  FOLLOWERS f INNER JOIN PEOPLE p  ON f.PERSON_ID = p.ID "+
            "INNER JOIN MESSAGES m ON m.PERSON_ID = f.PERSON_ID  "+
            "WHERE (f.FOLLOWER_PERSON_ID =(SELECT p.ID "+
                                        "FROM PEOPLE p "+
                                        "WHERE p.HANDLE  = :id))  AND m.content LIKE :srch "+


            "UNION "+

            "SELECT  m.PERSON_ID,p.handle,m.content "+
            "FROM MESSAGES m INNER JOIN PEOPLE p ON p.ID = m.PERSON_ID  "+
            "WHERE p.ID = (SELECT p.ID  "+
                           "FROM PEOPLE p "+
                           "WHERE p.HANDLE  = :id)  AND m.content LIKE :srch ";



    public List fetchMessages(String handle,String search) {
        Map parameters = new HashMap();
        parameters.put("id", handle);
        parameters.put("srch","%"+search+"%");
        return namedParameterJdbcTemplate.query(FOLLOWS_MSSG_SQL,parameters, new MessgMapper());
    }

}

class MessgMapper implements RowMapper {

    @Override
    public Message mapRow(ResultSet rs, int rowNum) throws SQLException {
        Message mssg = new Message();
        mssg.setId(rs.getInt("person_id"));
        mssg.setHandle(rs.getString("handle"));
        mssg.setMessage(rs.getString("content"));
        return mssg;
    }

}
