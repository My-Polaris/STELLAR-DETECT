// 加载动画函数,传参修改加载进度
var loading = function (a) {
    $(".loading .pic b").html(a + "%");
}
$(function () {
    // 鼠标经过导航栏按钮时的特效
    $("#menu li").hover(function () {
        $(this).stop().animate({
            left: -15
        }, 250);
    }, function () {
        $(this).stop().animate({
            left: 0
        }, 250);
    });
    //轮播图js控制
    var index = 0;
    function slideShow() {
        index=(index+1)%4;
        $(".img-container").stop().animate({
            left:-624*index
        },1000);
        $(".swiper-index .index-item").eq(index).addClass("active").siblings().removeClass("active");
    }
    var swiperInterval;
    //轮播图点击滑动
    $(".swiper-index .index-item").click(function(){
        // 一旦你有查看某页轮播图的想法,它就不再自动轮播了
        clearInterval(swiperInterval);
        index=$(".swiper-index .index-item").index(this);
        $(".img-container").stop().animate({
            left:-624*index
        },1000);
        $(".swiper-index .index-item").eq(index).addClass("active").siblings().removeClass("active");
    });
    // fullPage基本参数配置
    $('#dowebok').fullpage({
        verticalCentered: false,
        scrollOverflow: true,
        resize: true,
        scrollingSpeed: 500,
        anchors: ['page1', 'page2', 'page3', 'page4'],
        menu: "#menu",
        // 加载完页面时的回调函数
        afterRender: function () {

        },
        // 跳转至某个页面时的回调函数
        afterLoad: function (anchorLink, index) {
            if (index == 1) {
                $(".scrollTips").fadeIn();
            }
            if (index == 2) {
                swiperInterval = setInterval(slideShow,2500);
            }
        },
        //离开某个页面时的回调函数
        onLeave: function (index, direction) {
            if (index == 1) {
                $(".scrollTips").hide();
            }
            if (index == 2) {
                clearInterval(swiperInterval);
            }
        }
    });
    // ajax换页或换类型时,接受返回的data后的处理函数
    function dealPage(data,pageIndex) {
        // console.log(data);
        var pageData={
            pageCount : parseInt(data.totalElements+5)/6,
        }
        $(".articleList").html("");
        $.each(data.content, function (index, poster) {
            let acticleItem =
                $('<a class="articleItem" th:each="poster : ${postList}"\
               href="https://ak.hypergryph.com/news/2021120792.html">\
                <div class="article-content">\
                    <span class="noticeDate" th:utext="${poster.createDate}">'+ poster.createDate +'</span>\
                    <span class="noticeType" th:utext="${poster.postType}">'+ poster.postType +'</span>\
                    <span class="noticeContent"\
                          th:utext="${poster.postTitle}">'+ poster.postTitle +'</span>\
                </div>\
            </a>');
            // console.log(acticleItem);
            $(".articleList").append(acticleItem);
            // $(".article-content").eq(index).find(".noticeDate").html(poster.createDate);
            // $(".article-content").eq(index).find(".noticeType").html(poster.postType);
            // $(".article-content").eq(index).find(".noticeContent").html(poster.postTitle);
        })
        refreshPageInfo(pageData,pageIndex);
    }
    // 第二屏的公告栏切换样式
    var pageType=0;//当前页面的类型,默认是0,即最新
    $(".articleCategoryItem").click(function () {
        $(this).addClass("active").siblings("li").removeClass("active");
        pageType = $(".articleCategoryItem").index(this);//index对应的是相应类型
        // console.log(index);
        $.ajax({
            type: 'POST',
            url: '/togglePost',
            data: {
                'index':pageType,
                'pageNum':1
            },
            success:function (data) {
                dealPage(data,1);
            }
        })
    });
    //分页选项切换
    var pagingInfo = $("#pageInfo")

    var data={
        pageCount : parseInt(parseInt($("#postListInfo").html())/6)+1,
    }

    //表示前后都有省略号时中间页面刷新基准，为了防止点击中间时页面频繁刷新页面导致的视觉错位
    var tmpPageIndex = 0;

    //添加事件
    $(document).on("click", "#pageInfo li a",function () {
        let page = $(this).attr("page")
        if(page){
            page =parseInt(page)
            // console.log(page);
            //这里要注意,pageType表示了当前页面是什么类型,page表示当前是第几页
            $.ajax({
                type: 'POST',
                url: '/togglePost',
                data: {
                    'index':pageType,
                    'pageNum':page
                },
                success:function (data) {
                    dealPage(data,page);
                }
            })
        }
    })

    // 刷新|生成分页信息|自定义属性page表示当前页面索引
    function refreshPageInfo(data, pageIndex) {
        var pageSize = data.pageCount
        pagingInfo.html('')
        var li = $('<li><a page="1">&laquo;</a></li>')
        pagingInfo.append(li)

        // 总页数小于等于6页，全部显示
        if (pageSize <= 6) {
            for (var i = 1; i <= pageSize; i++) {
                var li = $('<li><a page="' + i + '">' + i + '</a></li>')
                addActive(li, i, pageIndex)
                pagingInfo.append(li)
            }
            // 当前页是前5页,前5页+...+最后一页
        } else if (pageIndex < 5) {
            for (var i = 1; i <= 5; i++) {
                var li = $('<li><a page="' + i + '">' + i + '</a></li>')
                addActive(li, i, pageIndex)
                pagingInfo.append(li)
            }
            pagingInfo.append('<li><a>......</a></li>')
            pagingInfo.append('<li><a page=' + pageSize + '>' + pageSize + '</a></li>')
            // 当前页面是最后5页,第一页+...+后5页
        } else if (pageSize - pageIndex < 5) {
            if (pageSize - 5 > 1) {
                pagingInfo.append('<li><a page="1">1</a></li>')
                pagingInfo.append('<li><a>......</a></li>')
            }
            for (var i = pageSize - 5; i <= pageSize; i++) {
                var li = $('<li><a page="' + i + '">' + i + '</a></li>')
                addActive(li, i, pageIndex)
                pagingInfo.append(li)
                if (i == 1) {
                    pagingInfo.append('<li><a>......</a></li>')
                }
            }
        } else {
            // 当前页面基于所有页面中间位置
            // 初始化页面基准坐标
            if (tmpPageIndex == 0) {
                tmpPageIndex = pageIndex
            }
            //第一页+...+4项+...+最后一页
            // 当页面索引达到最前或最后时，需要重新设置页面基准坐标
            if (tmpPageIndex <= pageIndex - 2 || tmpPageIndex >= pageIndex + 2) {
                tmpPageIndex = pageIndex
            }
            pagingInfo.append('<li><a page="1">1</a></li>')
            pagingInfo.append('<li><a>......</a></li>')
            for (var i = tmpPageIndex - 2; i <= tmpPageIndex + 2; i++) {
                var li = $('<li><a page="' + i + '">' + i + '</a></li>')
                addActive(li, i, pageIndex)
                pagingInfo.append(li)
            }
            pagingInfo.append('<li><a>......</a></li>')
            pagingInfo.append('<li><a page=' + pageSize + '>' + pageSize + '</a></li>')
        }

        var li = $('<li><a page="' + data.pageCount + '">&raquo;</a></li>')
        pagingInfo.append(li)
    }

    // 添加分页按钮焦点
    function addActive(li, i, pageIndex) {
        if (i == pageIndex) {
            li.addClass('active')
        }
    }

    refreshPageInfo(data,1);
});
