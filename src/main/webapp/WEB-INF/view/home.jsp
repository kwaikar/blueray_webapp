<%@ include file="include.jsp"%>
<html>
<head>
<title>BlueRay : Fine Grain Access Control System</title>
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
			<c:if test="${userName!=null}">
<div align="right">
						<a href="logout">logout</a>&nbsp;
					</div>
					</c:if>
		<div class="panel-body">
			<c:if test="${userName==null}">

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

			<c:if test="${userName!=null}">

				<div class="panel panel-info center"
					style="width: 840px; text-align: center; margin-top: 50px">
					<div class="panel-heading">Execute Program on Apache Spark Cluster</div>
						
					<div class="panel-body">
						<form method="POST" name="fileUploadForm" id="fileUploadForm"
							action="upload" enctype="multipart/form-data">
							<div class="row" style="text-align: left">
								<div class="col-xs-3">Main Class Name</div>
								<div class="col-xs-9">
									<input name="className" name="className" type="text" />
								</div>
							</div>
							<div class="row" style="text-align: left;padding-top:10px">
								<div class="col-xs-3">Jar File:</div>
								<div class="col-xs-9">
								<label class="btn btn-default btn-file">
    <u>Browse Jar</u> <input type="file" name="multipartFile" id="multipartFile"  />
</label>
								
									
								</div>
							</div>

							<div class="row" style="text-align: right;padding-top:10px">
								<div class="col-xs-12" style="text-align: right">
									<input type="submit" value="Run Code on Spark Cluster" class="btn btn-info" />
									
									
								</div>
							</div>
						</form>
						<c:if test="${output!=null}">
							<div class="row" style="text-align: left;padding-top:10px">
							 
								<div class="col-xs-12">${output}</div></div>
						</c:if>
					</div>
				</div>
			</c:if>
		</div>

	</div>
</body>
</html>