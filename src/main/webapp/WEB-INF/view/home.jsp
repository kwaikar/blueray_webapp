<%@ include file="include.jsp"%>
<html>
<head>
<title>KWIC Indexing System</title>
<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<link rel="stylesheet"
	href="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/css/bootstrap.min.css">
<link rel="stylesheet"
	href="https://cdn.datatables.net/1.10.12/css/dataTables.bootstrap.min.css">


<link rel="stylesheet"
	href="//code.jquery.com/ui/1.12.1/themes/base/jquery-ui.css">
<link rel="stylesheet" href="resources/css/style.css">
<script src="http://code.jquery.com/jquery-2.1.4.min.js"></script>
<script src="https://code.jquery.com/ui/1.12.1/jquery-ui.js"></script>

<script
	src="https://cdn.datatables.net/1.10.12/js/jquery.dataTables.min.js"></script>
<script
	src="https://cdn.datatables.net/1.10.12/js/dataTables.bootstrap.min.js"></script>

<script
	src="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/js/bootstrap.min.js"></script>

<script type="text/javascript">
	$(document).ready(function() {
		$('#searchResults').DataTable();

		$("#prefix").autocomplete({
			headers : {
				Accept : 'application/json'
			},
			source : "autocomplete",
			minLength : 1
		});

	});
</script>
<head>
<body>
	<div class="panel panel-info center"
		style="width: 1000px; text-align: center; margin-top: 50px">
		<div class="panel-heading" style="text-align: center">
			<div>
				<h3>BlueRay : Fine Grain Access Control System</h3>
			</div>
			<c:if test="${message!=null}">
				<div class="row" style="color: #ff0000; text-align: left">
					<B>${message}</B>
				</div>
			</c:if>
		</div>
		<div class="panel-body">
			<c:if test="${user==null}">

				<div class="panel panel-info center"
					style="width: 400px; text-align: center; margin-top: 50px">
					<div class="panel-heading">
						<div>
							<h3>Login</h3>
						</div>


					</div>
					<div class="panel-body">
						<form name="loginForm" id="loginForm" action="login" method="post">
							<div class="row" style="padding-top: 10px">
								<div class="col-xs-6">User Id:</div>
								<div class="col-xs-6">
									<input type="text" required class="form-control"
										name="userName" id="userName" value="" />
								</div>
							</div>
							<div class="row" style="padding-top: 10px">
								<div class="col-xs-6">Password</div>
								<div class="col-xs-6">
									<input type="password" class="form-control" required
										id="password" name="password" value="" />
								</div>
							</div>
							<div class="row" style="padding: 10px">
								<div class="col-xs-12" style="text-align: right">
									<input id="loginBtn" name="loginBtn" class="btn btn-info"
										type="submit" value="Login!" />
								</div>
							</div>
						</form>
					</div>
				</div>
			</c:if>

			<c:if test="${user!=null}">
				<div class="panel-heading">Output of Source code</div>
				<div class="panel-body">
	<div style="text-align:left">${output }</div> 
				</div>
			</c:if>
		</div></div>
</body>
</html>