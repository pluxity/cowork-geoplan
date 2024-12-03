<%@ page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
    <!-- Sidebar -->
 	<ul class="sidebar navbar-nav">
 	<c:forEach items="${sessionScope.LOGIN_INFO.leftMenuList}" var="leftMenu">
 		<li class="nav-item dropdown <c:if test="${fn:length(leftMenu.subMenuList) > 0}">dropdown</c:if>" data-menu-url="${leftMenu.menuKey}">
 		<c:choose>
 			<c:when test="${fn:length(leftMenu.subMenuList) > 0}">
 			<a class="nav-link dropdown-toggle" href="#" role="button" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
          		${leftMenu.menuIcon}
          		<span> ${leftMenu.menuNm}</span>
        	</a>
        	<div class="dropdown-menu">
				<c:forEach items="${leftMenu.subMenuList}" var="subMenu">
				<a class="dropdown-item" href="${subMenu.menuUrl}">${subMenu.menuNm}</a>
				</c:forEach>
        	</div>
 			</c:when>
 			<c:otherwise>
 			<a class="nav-link" href="${leftMenu.menuUrl}" target="${leftMenu.linkTarget}">
          		${leftMenu.menuIcon}
          		<span> ${leftMenu.menuNm}</span>
          	</a>
 			</c:otherwise>
 		</c:choose>
 		</li>
 	</c:forEach>
    </ul>


