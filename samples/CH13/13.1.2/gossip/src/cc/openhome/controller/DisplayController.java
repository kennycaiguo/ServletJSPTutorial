package cc.openhome.controller;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import cc.openhome.model.Message;
import cc.openhome.model.UserService;


@Controller
public class DisplayController {
    private String INDEX_PATH = "/WEB-INF/jsp/index.jsp";
    private String USER_PATH = "/WEB-INF/jsp/user.jsp";
    
    @RequestMapping("/")
    public void index(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        UserService userService = (UserService) request.getServletContext().getAttribute("userService");
        List<Message> newest = userService.newestMessages(10);
        request.setAttribute("newest", newest);
        
        request.getRequestDispatcher(INDEX_PATH)
               .forward(request, response);
    }
    
    @RequestMapping("user/*")
    public void user(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        String username = getUsername(request);
        UserService userService = (UserService) request.getServletContext().getAttribute("userService");
        
        request.setAttribute("username", username);
        if(userService.userExisted(username)) {
            List<Message> messages = userService.messages(username);
            request.setAttribute("messages", messages);
        } else {
            request.setAttribute("errors", Arrays.asList(String.format("%s 還沒有發表訊息", username)));
        }
        
        request.getRequestDispatcher(USER_PATH)
               .forward(request, response);
    }
    
    private String getUsername(HttpServletRequest request) {
        return request.getRequestURI().replace("/gossip/user/", "");
    }
}
