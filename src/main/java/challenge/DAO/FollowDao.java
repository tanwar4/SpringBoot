package challenge.DAO;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import challenge.Model.User;
import challenge.Model.UserFollower;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class FollowDao {

    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private final String FETCH_FOLLOWERS_SQL =
               "SELECT p.id AS id,p.handle AS handle "+
               "FROM FOLLOWERS f INNER JOIN PEOPLE p  ON f.FOLLOWER_PERSON_ID = p.ID "+
               "WHERE f.PERSON_ID = (SELECT ID "+
                                    "FROM PEOPLE "+
                                    "WHERE HANDLE = :handle )";


    private  final String FETCH_FOLLOWING_SQL =
            "SELECT f.PERSON_ID AS id,p.handle AS handle "+
                    "FROM FOLLOWERS f INNER JOIN PEOPLE p  ON f.PERSON_ID = p.ID "+
                    "WHERE f.FOLLOWER_PERSON_ID = (SELECT ID "+
                    "FROM PEOPLE "+
                    "WHERE HANDLE = :handle )";

    private final String FOLLOW_USER_SQL =
            "INSERT INTO FOLLOWERS(PERSON_ID,FOLLOWER_PERSON_ID) "+
             "VALUES (( SELECT ID FROM PEOPLE WHERE HANDLE= :fhandle),( SELECT ID FROM PEOPLE WHERE HANDLE= :handle)) ";


    private final String UNFOLLOW_USER_SQL =
            "DELETE FROM FOLLOWERS "+
             "WHERE PERSON_ID =( SELECT ID FROM PEOPLE WHERE HANDLE= :fhandle) " +
                    " AND FOLLOWER_PERSON_ID =( SELECT ID FROM PEOPLE WHERE HANDLE= :handle) ";



   // this will fetch  all the most popular followers if there is a tie between them i.e they have same popularity
    private final String FETCH_POPULARITY_SQL =
            "SELECT inn.fcount ,inn.follower,inn.user  "+
            "FROM " +
                  "(SELECT p.id AS pid,MAX(x.fcount) AS score "+
                  " FROM  PEOPLE p LEFT JOIN FOLLOWERS f  ON f.PERSON_ID=p.ID "+
                                    " INNER JOIN ( SELECT p.id as id, COUNT(p.id) AS fcount "+
                                                    "FROM PEOPLE p  LEFT JOIN FOLLOWERS f ON f.PERSON_ID=p.ID "+
                                                    "GROUP BY p.id ) x ON x.id = f.FOLLOWER_PERSON_ID "+
                  " GROUP BY(p.id) ) out "+

            "INNER JOIN "+

                    "(SELECT  fcount,p.id as id,p.handle as user,x.handle as follower "+
                    " FROM PEOPLE p LEFT JOIN FOLLOWERS f ON f.PERSON_ID=p.ID  INNER JOIN "+
                                     "(SELECT p.id as pid, COUNT(f.FOLLOWER_PERSON_ID) AS fcount ,p.handle "+
                                      "FROM  PEOPLE p LEFT JOIN FOLLOWERS f ON f.PERSON_ID=p.ID  "+
                                                    " GROUP BY p.id) x ON x.pid = f.FOLLOWER_PERSON_ID )  inn  "+


            " ON inn.fcount = out.score AND inn.id = out.pid ";




    public List fetchFollowers(String handle) {
        Map parameters = new HashMap();
        parameters.put("handle", handle);

        List<String> followers = namedParameterJdbcTemplate.query(FETCH_FOLLOWERS_SQL, parameters, new UserMapper());

        return followers;
    }


    public List fetchFollowing(String handle) {
        Map parameters = new HashMap();
        parameters.put("handle", handle);


        List<String> following = namedParameterJdbcTemplate.query(FETCH_FOLLOWING_SQL, parameters, new UserMapper());

        return following;
    }


    public String followUser(String handle,String fuser){
        Map parameters = new HashMap();
        parameters.put("handle", handle);
        parameters.put("fhandle",fuser);

        int status = namedParameterJdbcTemplate.update(FOLLOW_USER_SQL,parameters);

        if(status>0) return "success";
        else return "Unable to follow user";

    }

    public String unfollowUser(String handle,String fuser){
        Map parameters = new HashMap();
        parameters.put("handle", handle);
        parameters.put("fhandle",fuser);

        int status = namedParameterJdbcTemplate.update(UNFOLLOW_USER_SQL,parameters);
        if(status>0) return "success";
        else return "Unable to process request";

    }


    public List populaityList(){
        List<String> popularityList = namedParameterJdbcTemplate.query(FETCH_POPULARITY_SQL,new PopularityMapper());
        return popularityList;
    }
}



class UserMapper implements RowMapper {

    @Override
    public User mapRow(ResultSet rs, int rowNum) throws SQLException {
        User user = new User();
        user.setId(rs.getInt("id"));
        user.setHandle(rs.getString("handle"));
        return user;
    }

}

class PopularityMapper implements RowMapper {

    @Override
    public UserFollower mapRow(ResultSet rs, int rowNum) throws SQLException {
        UserFollower user = new UserFollower();
        user.setHandle(rs.getString("user"));
        user.setPopularFollower(rs.getString("follower"));
        user.setPopularityScore(rs.getInt("fcount"));
        return user;
    }

}