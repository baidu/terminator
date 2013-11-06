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
			开发手册
		</dt>
		<dd>
			<ul>
				<li class="<%=index.equals("0") ? "current" : ""%>"><a
					href="<%=request.getContextPath()%>/intro/develop/architecture.jsp">整体架构</a>
				</li>
				<li class="<%=index.equals("1") ? "current" : ""%>"><a
					href="<%=request.getContextPath()%>/intro/develop/protocol.jsp">协议开发</a>
				</li>
				<li class="<%=index.equals("2") ? "current" : ""%>"><a
					href="<%=request.getContextPath()%>/intro/develop/signer.jsp">签名类开发</a>
				</li>
				<li class="<%=index.equals("3") ? "current" : ""%>"><a
					href="<%=request.getContextPath()%>/intro/develop/extractor.jsp">抽取类开发</a>
				</li>
				<li class="<%=index.equals("4") ? "current" : ""%>"><a
					href="<%=request.getContextPath()%>/intro/develop/log.jsp">日志开发</a>
				</li>
			</ul>
		</dd>
	</dl>
</div>
