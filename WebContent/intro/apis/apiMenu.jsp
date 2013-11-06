<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>

<%
	String index = request.getParameter("index");
%>

<div class="us-sidebar">
	<dl class="menu">
		<dt>
			<img
				src="<%=request.getContextPath()%>/resources/images/document.png" />
			开放APIs
		</dt>
		<dd>
			<ul>
				<li class="<%=index.equals("0") ? "current" : ""%>"><a
					href="<%=request.getContextPath()%>/intro/apis/apis.jsp">简介</a></li>
				<li class="<%=index.equals("1") ? "current" : ""%>"><a
					href="<%=request.getContextPath()%>/intro/apis/link.jsp">链路接口</a></li>
				<li class="<%=index.equals("2") ? "current" : ""%>"><a
					href="<%=request.getContextPath()%>/intro/apis/workmode.jsp">工作模式控制</a>
				</li>
				<li class="<%=index.equals("3") ? "current" : ""%>"><a
					href="<%=request.getContextPath()%>/intro/apis/stub.jsp">通用桩接口</a>
				</li>
			</ul>
		</dd>
	</dl>
</div>
