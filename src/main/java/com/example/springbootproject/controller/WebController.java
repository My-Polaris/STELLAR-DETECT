package com.example.springbootproject.controller;

import com.example.springbootproject.Util.EmailRandomUtil;
import com.example.springbootproject.model.Poster;
import com.example.springbootproject.model.Roler;
import com.example.springbootproject.model.User;
import com.example.springbootproject.service.PosterService;
import com.example.springbootproject.service.RolerService;
import com.example.springbootproject.service.SendEmailService;
import com.example.springbootproject.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
public class WebController {
    @Resource
    private UserService userService;

    @Resource
    private RolerService rolerService;

    @Resource
    private PosterService posterService;

    @Autowired
    private SendEmailService sendEmailService;

    /*主页面初始化*/
    @RequestMapping("/Home")
    public String index(HttpSession session,Model model){
        boolean ifLogin=false;
        if(session.getAttribute("email")!=null){
            ifLogin=true;
        }
        model.addAttribute("ifLogin",ifLogin);
        // Paging
        PageRequest pageRequest= PageRequest.of(0,6);
        Page<Poster> posterPage=posterService.findAllOrderByCreateDateDesc(pageRequest);
        model.addAttribute("postList",posterPage);
        return "Index";
    }

    /*post的显示切换*/
    @RequestMapping("/togglePost")
    @ResponseBody
    public Page<Poster> togglePost(int index,Model model,int pageNum){
        // Paging
        PageRequest pageRequest= PageRequest.of(pageNum-1,6);
        Page<Poster> posterPage;
        if(index==0)    posterPage=posterService.findAllOrderByCreateDateDesc(pageRequest);
        else if(index==1)   posterPage=posterService.findByPostTypeOrderByCreateDateDesc(pageRequest,"公告");
        else if(index==2)   posterPage=posterService.findByPostTypeOrderByCreateDateDesc(pageRequest,"活动");
        else    posterPage=posterService.findByPostTypeOrderByCreateDateDesc(pageRequest,"新闻");
//        for(Poster x:posterPage){
//            System.out.println(x.getPostType() + " " + x.getPostTitle());
//        }
        return posterPage;
    }

    /*跳转至登录页面*/
    @RequestMapping("/Login")
    public String login(Model model,HttpSession session){
        model.addAttribute("myGoal","login");
        if(session.getAttribute("errorMessage") != null){//如果会话中存在errorMessage,则将errorMessage置为提示登录
            model.addAttribute("errorMessage",session.getAttribute("errorMessage").toString());
            session.removeAttribute("errorMessage");//删除errorMessage,以免白给
        }
        return "Login";
    }

    /*跳转至注册页面*/
    @RequestMapping("/SignUp")
    public String signUp(Model model){
        model.addAttribute("myGoal","signUp");
        return "Login";
    }

    /*跳转至个人页面*/
    @RequestMapping("/MyInfo")
    public String myInfo(HttpSession session,Model model){
        String userEmail = session.getAttribute("email").toString();//这里直接获取email是因为Filter已经对没有"email"的session进行过滤了
        Roler roler = new Roler();
        if(rolerService.existsByUserEmail(userEmail)){
            roler = rolerService.findByUserEmail(userEmail);
            model.addAttribute("email",session.getAttribute("email"));
            model.addAttribute("roleName",roler.getRoleName());
            model.addAttribute("roleUid",roler.getRoleUid());
        }else{
            System.out.println("找不到此人!");
            model.addAttribute("email",session.getAttribute("email"));
            model.addAttribute("roleName","暂无角色信息,期待您的加入");
            model.addAttribute("roleUid","暂无角色信息,期待您的加入");
        }
        return "MyInfo";
    }

    /*退出登录*/
    @RequestMapping("/SignOut")
    public String SignOutAction(HttpSession session,Model model,HttpServletResponse response){
        session.invalidate();//清除整个session
        return "redirect:/Home";//重定向到@RequestMapping("/Home")
    }

    /*密码登录验证*/
    @RequestMapping("/LoginInPassword")
    public String loginInPassword(String email, String password, HttpSession session, HttpServletResponse response, Model model){
        System.out.println(email);
        System.out.println(password);
        if(userService.existsByEmail(email)){
            User user = userService.findByEmail(email);
            if(user.getPassword().equals(password)){//密码正确
                //session.setAttribute("userId",user.getId());
                session.setAttribute("email",user.getEmail());
            }else{
                model.addAttribute("errorMessage","密码错误!");
                model.addAttribute("myGoal","login");
                return "Login";
            }
        }else{
            model.addAttribute("errorMessage","该邮箱未注册!");
            model.addAttribute("myGoal","login");
            return "Login";
        }
        return "redirect:/Home";
    }

    /*发送邮箱验证码*/
    @RequestMapping("/login/verCode")
    @ResponseBody
    public String verCode(String toMail,int index,HttpSession session,Model model) {
        if(index==1 && userService.existsByEmail(toMail)){//如果是注册页面的验证码，则需要验证该用户邮箱是否已经存在
            //model.addAttribute("errorMessage","此邮箱已经注册!");
            return "此邮箱已经注册!";
        }
        String verCode = EmailRandomUtil.generateVerCode();//产生随机数
        String content = "尊敬的用户,您好:\n"
                + "\n本次请求的邮件验证码为:\n\n" + verCode + "\n\n本验证码5分钟内有效，请及时输入。（请勿泄露此验证码）\n"
                + "\n如非本人操作，请忽略该邮件。\n(这是一封自动发送的邮件，请不要直接回复）";
        if(sendEmailService.sendEmail(toMail,"星际探测 | 您的邮箱验证码",content)){
            session.setAttribute("verCode",verCode);
            System.out.println("Send email success!");
            return "success";
        }else{
            //model.addAttribute("errorMessage","此邮箱不存在,请填入正确的邮箱!");
            System.out.println("Fail to send email!");
            //这里还缺一个发送失败后前端页面的反馈,估计要用ajax的success与否来对应
            return "邮件发送失败,请填入正确的邮箱!";
        }
    }

    /*验证码登录验证*/
    @RequestMapping("/LoginInEmailCode")
    public String loginInEmailCode(String email2,String verCode1,HttpSession session,Model model){
        if(session.getAttribute("verCode")==null){
            model.addAttribute("errorMessage","还未发送验证码!");
            model.addAttribute("myGoal","login");
            return "Login";
        };
        String verCode2 = session.getAttribute("verCode").toString();
        /*注意这里,角色表的邮箱与用户表的邮箱,有其一即可使用邮箱验证码登录*/
        if(userService.existsByEmail(email2) || rolerService.existsByUserEmail(email2)){
            if(verCode2.equals(verCode1)){//验证码正确
                session.setAttribute("email",email2);
                session.removeAttribute("verCode");//成功登录后删除session中的验证码讯息
            }else{
                model.addAttribute("errorMessage","验证码错误!");
                model.addAttribute("myGoal","login");
                return "Login";
            }
        }else{
            model.addAttribute("errorMessage","该邮箱尚未注册!");
            model.addAttribute("myGoal","login");
            return "Login";
        }
        return "redirect:/Home";
    }

    /*注册验证*/
    @RequestMapping("/SignUpAction")
    public String signUpAction(String email3,String password1,String password2,String verCode2,Model model,HttpSession session){
        if(!userService.existsByEmail(email3)){
            if(session.getAttribute("verCode")==null){
                model.addAttribute("errorMessage","还未发送验证码!");
                model.addAttribute("myGoal","signUp");
                return "Login";
            };
            if(!password1.equals(password2)){//如果两次密码不一样,其实js里已经判断过一次了hh
                model.addAttribute("errorMessage","两次密码不一致!");
                model.addAttribute("myGoal","signUp");
                return "Login";
            }
            if(verCode2.equals(verCode2)){//验证码正确
                User user = new User();
                int id = userService.findAll().size()+1;
                user.setId(id);
                user.setEmail(email3);
                user.setPassword(password1);
                userService.save(user);
                //session.setAttribute("userId",user.getId());//添加对应session
                session.setAttribute("email",user.getEmail());
                session.removeAttribute("verCode");//成功登录后删除session中的验证码讯息
            }else{
                model.addAttribute("errorMessage","验证码错误!");
                model.addAttribute("myGoal","signUp");
                return "Login";
            }
        }else{
            model.addAttribute("errorMessage","该邮箱已经注册!");
            model.addAttribute("myGoal","signUp");
            return "Login";
        }
        return "redirect:/Home";
    }
}
