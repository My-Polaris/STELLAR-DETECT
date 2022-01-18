$(function(){
    //alert框渐出
    function alertOut() {
        $(".alert").fadeOut();
        $(".alert").html("");
    }
    if($(".alert").css('display') != "none")    setTimeout(alertOut,2500);
    // 登录/注册界面切换
    $('.selection span').click(function(){
        $(this).addClass("active").siblings("span").removeClass("active");
        var index = $(".selection span").index(this)
        $(".form-info").eq(index).show(0);
        $(".form-info").eq(index).siblings(".form-info").hide(0);
    })
    // 密码/验证码界面切换
    $('.form-special').click(function() {
        $("#login-info .form-content").toggle();
    })
    //对邮箱的验证
    function emailTest(elem){
        var reg = /^([a-zA-Z0-9]+[_|\_|\.]?)*[a-zA-Z0-9]+@([a-zA-Z0-9]+[_|\_|\.]?)*[a-zA-Z0-9]+\.[a-zA-Z]{2,3}$/;
        // console.log(elem.val());
        if(!reg.test(elem.val()))
        {
            elem.next().show();
            return false;//返回false表示测试不通过
        } else {
            elem.next().hide();
            return true;//返回true表示测试通过
        }
    }
    $(".email").blur(function(){
        emailTest($(this));
    })
    // 对密码的验证
    function pwdTest(elem){
        var reg =/^(?:(?=.*[A-Z])(?=.*[a-z])(?=.*[0-9])|(?=.*[A-Z])(?=.*[a-z])(?=.*[^A-Za-z0-9])|(?=.*[A-Z])(?=.*[0-9])(?=.*[^A-Za-z0-9])|(?=.*[a-z])(?=.*[0-9])(?=.*[^A-Za-z0-9])).{8,16}|(?:(?=.*[A-Z])(?=.*[a-z])|(?=.*[A-Z])(?=.*[0-9])|(?=.*[A-Z])(?=.*[^A-Za-z0-9])|(?=.*[a-z])(?=.*[0-9])|(?=.*[a-z])(?=.*[^A-Za-z0-9])|(?=.*[0-9])(?=.*[^A-Za-z0-9])|).{8,16}$/;
        // console.log(elem.val());
        if(!reg.test(elem.val())){
            elem.next().show();
            return false;//返回false表示测试不通过
        }
        else{
            elem.next().hide();
            return true;//返回true表示测试通过
        }
    }
    $(".password").blur(function() {
        pwdTest($(this));
    })
    // 对确认密码的验证
    function pwd2Test(elem){
        // console.log(elem.val());
        // console.log(elem.siblings(".password").val());
        if(elem.val() != elem.siblings(".password").val()){
            elem.next().show();
            return false;//返回false表示测试不通过
        } else {
            elem.next().hide();
            return true;//返回true表示测试通过
        }
    }
    $(".password2").blur(function() {
        pwd2Test($(this));
    });
    // 密码登录初步验证
    $("#password-login-form").submit(function(e){
        if(emailTest($("#password-login-form .email")) && pwdTest($("#password-login-form .password")))    return;
        else    e.preventDefault();
    });
    // 验证码登录初步验证
    $("#code-login-form").submit(function(e){
        if(emailTest($("#code-login-form .email"))) return ;
        else    e.preventDefault();
    });
    // 注册初步验证
    $("#sign-up-form").submit(function(e){
        if(emailTest($("#sign-up-form .email")) && pwdTest($("#sign-up-form .password")) && pwd2Test($("#sign-up-form .password2")))    return;
        else    e.preventDefault();
    });
    // 获取邮箱验证码按钮的监听事件
    var timeInterval=[];
    var time=[];
    $(".form-info .get-num button").click(function () {
        if(emailTest($(this).parent().siblings("input"))){
            $(this).css("background-color","gray");
            $(this).attr('disabled',"true");//禁用按钮
            let index = $(".form-info .get-num button").index(this);
            time[index]=10;
            timeAction($(this),index);
            timeInterval[index] = window.setInterval(_timeAction($(this),index),1000);
            const email = $(this).parent().siblings("input").val();
            // console.log(email);
            //ajax回调函数是保留this关键字的,注意this指针指向
            $.ajax({
                type: 'POST',
                url: 'login/verCode',
                data: {
                    'toMail': email,
                    'index':index
                },
                success:function (msg) {
                    if(msg=="success"){

                    }
                    else{
                        $(".alert").show();
                        $(".alert").html(msg);
                        setTimeout(alertOut,2500);
                    }
                }
            })
        };
    });
    function _timeAction(ele,index) {
        return function() {
            timeAction(ele,index);
        }
    }
    function timeAction(ele,index) {
        if(time[index]<=0){
            clearInterval(timeInterval[index]);
            ele.css("background-color","#158fc5");
            ele.html("获取验证码");
            ele.removeAttr("disabled");
        }
        else    ele.html(time[index]);
        time[index]--;
    }
})