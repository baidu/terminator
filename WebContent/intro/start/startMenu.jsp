<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%
	String index = request.getParameter("index");
%>

<div class="us-sidebar">
	<dl class="menu">
		<dt>
			<img src="<%=request.getContextPath()%>/resources/images/document.png"/>
			使用手冊
		</dt>
		<dd>
			<ul>
				<li class="<%=index.equals("0") ? "current" : ""%>">
					<a href="<%=request.getContextPath()%>/intro/start/start.jsp">整体介绍</a>
				</li>
				<li class="<%=index.equals("1") ? "current" : ""%>">
					<a href="<%=request.getContextPath()%>/intro/start/record.jsp">录制回放模式</a>
				</li>
				<li class="<%=index.equals("2") ? "current" : ""%>">
					<a href="<%=request.getContextPath()%>/intro/start/stub.jsp">通用桩模式</a>
				</li>
				<li class="<%=index.equals("3") ? "current" : ""%>">
					<a href="<%=request.getContextPath()%>/intro/start/qa.jsp">常见问题</a>
				</li>
			</ul>
		</dd>
	</dl>
</div>
