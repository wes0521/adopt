<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<!--Start sidebar-wrapper-->
<div id="sidebar-wrapper" data-simplebar="" data-simplebar-auto-hide="true">
    <div class="brand-logo">
        <a href="${pageContext.request.contextPath}/shopping/html/index.jsp">
            <img src="${pageContext.request.contextPath}/admin/assets/images/logo-icon.png" class="logo-icon" alt="logo icon">
            <h5 class="logo-text">安提尼服装商城</h5>
        </a>
    </div>
    <ul class="sidebar-menu do-nicescrol">
        <li class="sidebar-header">目录</li>
        <li>
            <a href="#" class="waves-effect">
                <i class="zmdi zmdi-layers"></i>
                <span>会员</span> <i class="fa fa-angle-left pull-right"></i>
            </a>
            <ul class="sidebar-submenu">
                <li><a href="${pageContext.request.contextPath}/admin/member/AdminFindUserList"><i class="zmdi zmdi-star-outline"></i>会员列表</a></li>
                <li><a href="${pageContext.request.contextPath}/admin/member/findConsumeList"><i class="zmdi zmdi-star-outline"></i> 会员购买商品列表</a></li>
            </ul>
        </li>
        <li>
            <a href="#" class="waves-effect">
                <i class="fa fa-cart-plus text-white"></i></i> <span>服装</span>
                <i class="fa fa-angle-left float-right"></i>
            </a>
            <ul class="sidebar-submenu">
                <li><a href="${pageContext.request.contextPath}/admin/clothes/AdminFindClothList"><i class="zmdi zmdi-star-outline"></i>服装列表</a></li>
                <li><a href="${pageContext.request.contextPath}/admin/clothes-add.jsp"><i class="zmdi zmdi-star-outline"></i>添加商品</a></li>
            </ul>
        </li>

        <li>
            <a href="${pageContext.request.contextPath}/admin/login/adminLoginOut" class="waves-effect">
                <i class="zmdi zmdi-chart-donut text-success"></i>
                <span>
                        <font style="vertical-align: inherit;">
                            <font style="vertical-align: inherit;">退出系统</font>
                        </font>
                    </span>
            </a>
        </li>
    </ul>

</div>
<!--End sidebar-wrapper-->
</html>

