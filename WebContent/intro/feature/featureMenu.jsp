<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%
	String index = request.getParameter("index");
%>

<div class="us-sidebar">
	<dl class="menu">
		<dt>
			<img src="<%=request.getContextPath()%>/resources/images/document.png"/>
			功能规划
		</dt>
		<dd>
			<ul>
				<li class="<%=index.equals("0") ? "current" : ""%>">
					<a href="<%=request.getContextPath()%>/intro/feature/features.jsp">整体功能</a>
				</li>
				<li class="<%=index.equals("1") ? "current" : ""%>">
					<a href="<%=request.getContextPath()%>/intro/feature/version1.0.jsp">1.X路线图</a>
				</li>
			</ul>
		</dd>
	</dl>
</div>
