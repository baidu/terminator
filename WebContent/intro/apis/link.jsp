<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<!DOCTYPE html>
<html>
<head>
<jsp:include page="/include/common.jsp" flush="true" />
</head>

<body>
	<jsp:include page="/include/header.jsp" flush="true">
		<jsp:param name="index" value="2" />
	</jsp:include>

	<div class="main">
		<jsp:include page="/intro/apis/apiMenu.jsp" flush="true">
			<jsp:param name="index" value="1" />
		</jsp:include>

		<div class="us-content">
			<div class="cont">
				<div>
					<img src="<%=request.getContextPath()%>/resources/images/point.png" style="float: left;padding-right: 3px">
					<h4>新增链路配置</h4>
				</div>

				<h5>请求:</h5>
				接口地址:http://ip:port/terminator/link<br> Http方法:POST<br>
				<table>
					<tr>
						<td>参数列表</td>
						<td>描述</td>
					</tr>
					<tr>
						<td>name(必填)</td>
						<td>链路配置的名称</td>
					</tr>
					<tr>
						<td>localPort(必填)</td>
						<td>Terminator监听client接口的端口</td>
					</tr>
					<tr>
						<td>remoteAddress(必填)</td>
						<td>Terminator连接远程服务的IP</td>
					</tr>
					<tr>
						<td>remotePort(必填)</td>
						<td>Terminator连接远程服务的端口</td>
					</tr>
					<tr>
						<td>workMode(必填)</td>
						<td>工作模式</td>
					</tr>
					<tr>
						<td>protocolType(必填)</td>
						<td>协议类型:HTTP,Mcpack+JSON-RPC over HTTP,Nshead+Mcpack over TCP</td>
					</tr>
					<tr>
						<td>storageType(必填)</td>
						<td>存储类型:CACHE,DATABASE</td>
					</tr>
					<tr>
						<td>signClass（可选）</td>
						<td>签名类</td>
					</tr>
					<tr>
						<td>extractClass（可选）</td>
						<td>抽取类</td>
					</tr>
				</table>
				<br>

				<h5>返回:</h5>
				<p>
					状态码:201（CREATED）<br> Http
					Header:location中返回新增链接的资源/link/{linkId}(其中linkId为这个链接配置的唯一标识)<br> 
					Http Body:空
				</p>

				<h5>例子:</h5>
				<p>
					向接口地址发送POST Http请求:{"name":"http_proxy","localPort":8080,"remoteAddress":"61.135.169.125","remotePort":80,"workMode":"RECORD" ,"protocolType":"HTTP","storageType":"CACHE","signClass":"com.baidu.terminator.plugin.signer.DefaultHttpSigner" ,"extractClass":"com.baidu.terminator.plugin.extractor.DefaultHttpExtractor"}<br>
					HTTP响应: header中location=/link/10<br>
					通过访问http://ip:port/terminator/link/10就能够访问到新增的这个链接配置
				</p>
				
				<div>
					<img src="<%=request.getContextPath()%>/resources/images/point.png" style="float: left;padding-right: 3px">
					<h4>查询所有链路配置</h4>
				</div>

				<h5>请求:</h5>
				<p>
					接口地址:http://ip:port/terminator/link<br> Http方法:GET<br>
				</p>

				<h5>返回:</h5>
				<p>
					状态码:200（OK）<br> Http Body:所有链接配置
				</p>

				<h5>例子:</h5>
				<p>
					Http请求:向接口地址发送GET请求<br> 
					HTTP响应:状态码为200，HTTP Body为：
					[{"id":11,"name":"local_proxy","localPort":8082,"remoteAddress":"www.baidu.com","remotePort":80 ,"workMode":"RECORD","protocolType":"HTTP","storageType":"CACHE","signClass":"","status":"Stopped"}]<br>
				</p>
				
				<div>
					<img src="<%=request.getContextPath()%>/resources/images/point.png" style="float: left;padding-right: 3px">
					<h4>查询某个链路配置</h4>
				</div>

				<h5>请求:</h5>
				<p>
					接口地址:http://ip:port/terminator/link/{linkId}<br> Http方法:GET<br>
				</p>

				<h5>返回:</h5>
				<p>
					状态码:200（OK）<br> Http Body:link id为{linkId}的链路配置
				</p>

				<h5>例子:</h5>
				<p>
					Http请求:向接口地址发送GET请求<br> 
					HTTP响应:状态码为200，HTTP Body为：
					{"id":11,"name":"local_proxy","localPort":8082,"remoteAddress":"www.baidu.com","remotePort":80 ,"workMode":"RECORD","protocolType":"HTTP","storageType":"CACHE","signClass":"","status":"Stopped"}<br>
				</p>
				
				<div>
					<img src="<%=request.getContextPath()%>/resources/images/point.png" style="float: left;padding-right: 3px">
					<h4>修改某个链路配置</h4>
				</div>

				<h5>请求:</h5>
				接口地址:http://ip:port/terminator/link/{linkId}<br> Http方法:PUT<br>
				<table>
					<tr>
						<td>参数列表</td>
						<td>描述</td>
					</tr>
					<tr>
						<td>name(必填)</td>
						<td>链路配置的名称</td>
					</tr>
					<tr>
						<td>localPort(必填)</td>
						<td>Terminator监听client接口的端口</td>
					</tr>
					<tr>
						<td>remoteAddress(必填)</td>
						<td>Terminator连接远程服务的IP</td>
					</tr>
					<tr>
						<td>remotePort(必填)</td>
						<td>Terminator连接远程服务的端口</td>
					</tr>
					<tr>
						<td>workMode(必填)</td>
						<td>工作模式</td>
					</tr>
					<tr>
						<td>protocolType(必填)</td>
						<td>协议类型:HTTP,Mcpack+JSON-RPC over HTTP,Nshead+Mcpack over TCP</td>
					</tr>
					<tr>
						<td>storageType(必填)</td>
						<td>存储类型:CACHE,DATABASE</td>
					</tr>
					<tr>
						<td>signClass（可选）</td>
						<td>签名类</td>
					</tr>
					<tr>
						<td>extractClass（可选）</td>
						<td>抽取类</td>
					</tr>
				</table>
				<br>

				<h5>返回:</h5>
				<p>
					状态码:204（NO CONTENT）<br> 
					Http Body:空
				</p>

				<h5>例子:</h5>
				<p>
					向接口地址发送Http PUT请求:{"name":"http_proxy","localPort":8080,"remoteAddress":"61.135.169.125","remotePort":80,"workMode":"RECORD" ,"protocolType":"HTTP","storageType":"CACHE","signClass":"com.baidu.terminator.plugin.signer.DefaultHttpSigner" ,"extractClass":"com.baidu.terminator.plugin.extractor.DefaultHttpExtractor"}<br>
					HTTP响应: 状态码为204，HTTP Body为空
				</p>
				
				<div>
					<img src="<%=request.getContextPath()%>/resources/images/point.png" style="float: left;padding-right: 3px">
					<h4>删除某个链路配置</h4>
				</div>

				<h5>请求:</h5>
				<p>
					接口地址:http://ip:port/terminator/link/{linkId}<br> Http方法:DELETE<br>
				</p>

				<h5>返回:</h5>
				<p>
					状态码:204（No Content）<br> Http Body:空
				</p>

				<h5>例子:</h5>
				<p>
					Http请求:向接口地址发送DELETE请求<br> 
					HTTP响应: 状态码为204，HTTP Body为空<br>
				</p>
			</div>
		</div>
		<!--end us-content-->
		<div class="clearfix"></div>
	</div>
	<!--end main-->

</body>
</html>