<%@ page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<sec:authentication var="principal" property="principal" />
	<nav class="navbar navbar-expand navbar-dark bg-dark static-top">
		<button class="btn btn-link btn-sm text-white order-1 order-sm-0" id="sidebarToggle" href="#">
			<i class="fas fa-bars"></i>
		</button>
		<a class="navbar-brand mr-1" href="/adm/main/index.do"><img src="/resources/img/logo_admin_header.gif" style="height:43px;">
			<span>실내공간정보관리시스템</span>
		</a>
	    <!-- Navbar Search -->
	    <form class="d-md-inline-block form-inline ml-auto mr-0 mr-md-3 my-2 my-md-0"></form>
	    <!-- Navbar -->
	    <ul class="navbar-nav ml-auto ml-md-0">
	      <li class="nav-item dropdown no-arrow">
	        <a class="nav-link dropdown-toggle" href="#" id="userDropdown" role="button" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
	          <i class="fas fa-user-circle fa-fw"></i>
	          <span style="color:#fff;"><sec:authorize access="isAuthenticated()">${principal.username}님 로그인중</sec:authorize></span>
	        </a>
	        <div class="dropdown-menu dropdown-menu-right" aria-labelledby="userDropdown">
	          <a class="dropdown-item" href="/adm/main/index.do">개인정보수정</a>
	          <div class="dropdown-divider"></div>
	          <a class="dropdown-item" href="/login/logout.do">로그아웃</a>
	        </div>
	      </li>
		</ul>
	  </nav>