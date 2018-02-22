package challenge.service;

import challenge.DAO.MessageDao;
import challenge.Model.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
public class MessageService {

    @Autowired
    private MessageDao mssgDao;

    @RequestMapping("/message")
    public List message(@RequestParam(value="search", defaultValue="") String query,
                       Principal principal) {

        String handle = principal.getName();

        List<Message> lst =  mssgDao.fetchMessages(handle,query);

        return lst;

    }
}
