<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%
	String index = request.getParameter("index");
%>

<div id="header">

	<div class="logoContainer" id="logo">
		<div class="logoImg" id="logoImg"></div>
	</div>

	<div class="terminator" id="terminator">
		<p style="text-align: left; line-height: 28px;">
			<span
				style="font-family: 微软雅黑; font-size: 33px; font-weight: bold; font-style: normal; text-decoration: none; color: #2383D0;">Terminator</span>
		</p>
	</div>

	<div class="slogan" id="slogan">
		<p style="text-align: left; line-height: 28px;">
			<span
				style="font-family: 楷体; font-size: 16px; font-weight: normal; font-style: normal; text-decoration: none; color: #6B6B6B;">服务虚拟化</span><span
				style="font-family: 楷体; font-size: 16px; font-weight: normal; font-style: normal; text-decoration: none; color: #6B6B6B;">
				畅通无忧行</span>
		</p>
	</div>

	<div class="version" id="version">
		<p style="text-align: left; line-height: 28px;">
			<span
				style="font-family: Arial; font-size: 11px; font-weight: normal; font-style: normal; text-decoration: none; color: #949494;">V1.1 beta</span>
		</p>
	</div>

	<div class="bannerContainer" id="banner">
		<div class="bannerImage" id="bannerImage"></div>
	</div>

	<div class="selectEffect<%=index%>" id="selectEffect">
		<div class="selectEffectImage" id="selectEffectImage"></div>
	</div>

	<div class="index" id="index">
		<p style="text-align: left; line-height: 28px;">
			<a href="<%=request.getContextPath()%>/index.jsp" style="text-decoration:none;">
			<span
				style="font-family: 微软雅黑; font-size: 18px; font-weight: normal; font-style: normal; text-decoration: none; color: #FFFFFF;">首页</span>
			</a>
		</p>
	</div>

	<div class="start" id="start">
		<p style="text-align: left; line-height: 28px;">
			<a href="<%=request.getContextPath()%>/intro/start/start.jsp" style="text-decoration:none;">
			<span
				style="font-family: 微软雅黑; font-size: 18px; font-weight: normal; font-style: normal; text-decoration: none; color: #FFFFFF;">使用手册</span>
			</a>
		</p>
	</div>

	<div class="apis" id="apis">
		<p style="text-align: left; line-height: 28px;">
			<a href="<%=request.getContextPath()%>/intro/apis/apis.jsp" style="text-decoration:none;">
			<span
				style="font-family: 微软雅黑; font-size: 18px; font-weight: normal; font-style: normal; text-decoration: none; color: #FFFFFF;">APIs</span>
			</a>
		</p>
	</div>

	<div class="development" id="development">
		<p style="text-align: left; line-height: 28px;">
			<a href="<%=request.getContextPath()%>/intro/develop/architecture.jsp" style="text-decoration:none;">
			<span
				style="font-family: 微软雅黑; font-size: 18px; font-weight: normal; font-style: normal; text-decoration: none; color: #FFFFFF;">开发指南</span>
			</a>
		</p>
	</div>

	<div class="feature" id="feature">
		<p style="text-align: left; line-height: 28px;">
			<a href="<%=request.getContextPath()%>/intro/feature/features.jsp" style="text-decoration:none;">
			<span
				style="font-family: 微软雅黑; font-size: 18px; font-weight: normal; font-style: normal; text-decoration: none; color: #FFFFFF;">版本规划</span>
			</a>
		</p>
	</div>

</div>