package jungle.spaceship.controller;

import jungle.spaceship.entity.Role;
import jungle.spaceship.entity.auth.SessionMember;
import jungle.spaceship.response.ExtendedResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@RequiredArgsConstructor
@Controller
public class IndexController{

    private final HttpSession httpSession;

    @GetMapping("/")
    public String index(Model model) {
//        model.addAttribute("posts", postsService.findAllDesc());

        SessionMember user = (SessionMember)httpSession.getAttribute("user");

        if(user != null) {
            model.addAttribute("userName", user.getName());
        }

        return "hi";
    }

    @GetMapping("/private/login")
    public @ResponseBody ResponseEntity<ExtendedResponse<Role>> loginCallback(
            @RequestParam(name = "loginSuccess") boolean loginSuccess,
            @RequestParam(name = "accessToken", required = false) String accessToken,
            @RequestParam(name = "refreshToken", required = false) String refreshToken,
            HttpServletResponse response) {
        System.out.println("IndexController.tokenTest");

        if (loginSuccess) {
            response.addHeader("Authorization", accessToken);
            response.addHeader("refresh", refreshToken);
            ExtendedResponse<Role> extendedResponse = new ExtendedResponse<>(Role.USER, HttpStatus.ACCEPTED.value(), "로그인 완료");
            return new ResponseEntity<>(extendedResponse, HttpStatus.ACCEPTED);
        } else {
            ExtendedResponse<Role> extendedResponse = new ExtendedResponse<>(Role.GUEST, HttpStatus.CREATED.value(), "회원 가입 완료");
            return new ResponseEntity<>(extendedResponse, HttpStatus.CREATED);
        }
    }
}


