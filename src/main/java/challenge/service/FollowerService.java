package challenge.service;

import challenge.DAO.FollowDao;
import challenge.Model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;

@RestController
public class FollowerService {

    @Autowired
    private FollowDao followDao;

    @RequestMapping("/followers")
    public List followers(Principal principal){

        String user = principal.getName();
        List<User> lst = followDao.fetchFollowers(user);
        return lst;
    }

    @RequestMapping("/following")

    public List following(Principal principal){
        String user = principal.getName();
        List<User> lst = followDao.fetchFollowing(user);
        return lst;

    }

    @RequestMapping("/follow/{uname}")

    public String follow(@PathVariable(value = "uname") final String uname,Principal principal){
        String handle = principal.getName();
        return followDao.followUser(handle,uname);
    }

    @RequestMapping("/unfollow/{uname}")

    public String unfollow(@PathVariable(value = "uname") final String uname, Principal principal){
        String handle = principal.getName();
        return followDao.unfollowUser(handle,uname);
    }

     @RequestMapping("/popularity")
    public List mostPopularfollowers(){
        return followDao.populaityList();
    }

}
