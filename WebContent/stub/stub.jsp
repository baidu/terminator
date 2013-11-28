<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<!DOCTYPE html>
<html>
<head>
<jsp:include page="/include/common.jsp" flush="true" />
</head>

<script type="text/html" id="condition_submit_template">
	<tr id="condition{{conditionId}}" name="condition">
		<td>键<input type="text" name="key"/></td>
		<td>值<input type="text" name="value"/></td>
		<td><input type="button" onclick="deleteCondition({{conditionId}})" value="删除"/></td>
	</tr>
</script>

<script type="text/html" id="condition_list_template">
	<thead>
		<tr>
			<th>ID</th>
			<th>匹配条件</th>
			<th>匹配条件关系</th>
			<th>预期返回</th>
			<th>延时</th>
			<th>操作</th>
		</tr>
	</thead>
	{{#stubData}}
	<tr>
		<td>{{id}}</td>
		<td>
			{{#conditions}}
				{{key}} : {{value}}  <br>
			{{/conditions}}
		</td>
		<td>
			{{operator}}
		</td>
		<td>{{response}}</td>
		<td>{{delay}}</td>
		<td><a onclick="deleteStubData({{id}})">删除</a></td>
	</tr>
	{{/stubData}}
</script>

<body>

<jsp:include page="/include/header.jsp" flush="true">
	<jsp:param name="index" value="0" />
</jsp:include>

<div class="main">
<div class="subnav">
<ul>
	<li class="first current1">桩设置</li>
</ul>
</div>

<div style="margin-bottom: 5px">匹配条件说明:</div>
<div style="margin-bottom: 5px; margin-top: 5px" id="message"></div>

<div style="margin-bottom: 5px">匹配条件:</div>

<input id="addCondition" type="button" onclick="addCondition()"
	value="增加匹配条件" style="margin-bottom: 5px; margin-top: 5px" /> <label>请选择匹配条件之间关系：</label>
<input type="radio" name="operator_radio" value="and" checked="checked">AND
<input type="radio" name="operator_radio" value="or">OR <input
	type="radio" name="operator_radio" value="or">NOT <span
	id="conditionPrompt" style="display: none; color: red"></span>
<table id="addConditionTable" class="listtbl1"
	style="margin-bottom: 5px; margin-top: 5px">
</table>
<input type="hidden" name="sequence" value=0 />
<div style="margin-bottom: 5px; margin-top: 5px">预设返回:</div>
<textarea id="response" style="height: 100px; width: 382px"></textarea>

<br>
<input type="button" onclick="submitCondition()" value="提交" class="btn" />

<table id="listConditionTable" class="listtbl1" style="margin-top: 15px"></table>
</div>

<script type="text/javascript">
		$.ajaxSetup({cache:false});
		baseUrl = "<%=request.getContextPath()%>"
		linkId = "<%=request.getParameter("linkId")%>"

		var conditionId = 0;

		$(document).ready(function() {
			$.Mustache.addFromDom('condition_submit_template');
			$.Mustache.addFromDom('condition_list_template');
			
			getExtractorMessage();
			listConditions();
		});
		
		function getExtractorMessage(){
			$.ajax({
				type : "GET",
				url : baseUrl + '/stub/' + linkId + '/extractor',
				contentType : "application/json",
				success : function(data, textStatus, jqXHR) {
					$('#message').html(data.message);
				},
				error: function (XMLHttpRequest, textStatus, errorThrown) {
		            alert(XMLHttpRequest.responseText);
		        }
			});
		}
		
		function listConditions(){
			$.ajax({
				type : "GET",
				url : baseUrl + '/stub/' + linkId,
				contentType : "application/json",
				success : function(data, textStatus, jqXHR) {
					$('#listConditionTable').empty().mustache('condition_list_template', {
						stubData : data
					});
				},
				error: function (XMLHttpRequest, textStatus, errorThrown) {
		            alert(XMLHttpRequest.responseText);
		        }
			});
		}

		function addCondition() {
			$('#addConditionTable').mustache("condition_submit_template", {
				conditionId : conditionId,
			});
		}

		function deleteCondition(conditionId) {
			$("#condition" + conditionId).remove();
		}

		function submitCondition() {
			// define Conditon Object
			function Condition(key,value){
		        this.key = key;
		        this.value = value;
			}
			
			var trConditions = $('tr[name=condition]')
			if (trConditions.length == 0){
				$("#conditionPrompt").html('请填写匹配条件!');
				$("#conditionPrompt").show();
				return false;
			}
			$("#conditionPrompt").hide();
			
			var conditions = [];
			var keys = $('input[name=key]')
			var values = $('input[name=value]')
			var sequences = $('input[name=sequence]')[0].value;
			var response = $('#response').val();
			var operators = "and";
			if($('input[name=operator_radio]')[0].checked == true){
				operators = "and";
			}else if($('input[name=operator_radio]')[1].checked == true){
				operators = "or";
			}else if($('input[name=operator_radio]')[2].checked == true){
				operators = "not";
			}
				
			for (var i = 0; i < keys.length; i++) {
				var c = new Condition(keys[i].value,values[i].value);
				conditions.push(c);
			}
			
			$.ajax({
				type : "POST",
				url : baseUrl + "/stub/" + linkId,
				contentType : "application/json;charset=utf-8",
				data : JSON.stringify({
					"conditions" : conditions,
					"operator" : operators,
					"sequence" : sequences,
					"response" : response,
				}),
				success : function(data, textStatus, jqXHR) {
					conditionId = 0;
					listConditions();
					alert("匹配条件提交成功!");
				},
				error: function (XMLHttpRequest, textStatus, errorThrown) {
		            alert(XMLHttpRequest.responseText);
		        }
			});
		}
		
		function deleteStubData(id){
			var comfirm = confirm('您确定要删除设置的桩数据吗？');
			if (comfirm) {
				$.ajax({
					type : "DELETE",
					url : baseUrl + "/stub/" + linkId + "/" + id,
					contentType : "application/json",
					success : function(data, textStatus, jqXHR) {
						listConditions();
						alert("桩数据已删除！");
					},
					error: function (XMLHttpRequest, textStatus, errorThrown) {
			            alert(XMLHttpRequest.responseText);
			        }

				});
			}
		}
	</script>

</body>
</html>