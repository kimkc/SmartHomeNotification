<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@include file="./includes/header.jsp"%>


	<div class="container theme-showcase" role="main">
  		<div class="page-header">
   			<h1>할 일 목록</h1>
  		</div>
  		<div class="row">
			<div class=".col-md-8">
    			<table class="table table-condensed">
					<thead>
						<tr>
							<th>#번호</th>
							<th>날짜</th>
							<th>할 일</th>
						</tr>
					</thead>
					<tbody>
						<c:forEach items="${todoList }" var="todo">
							<tr>
								<td><c:out value="${todo.dno }" /></td>
								<td><c:out value="${ todo.regdate }" /></td>
								<td><c:out value="${todo.todo }" /></td> 
							</tr>
						</c:forEach>
					</tbody>
				</table>
			</div>
		</div>
			<div class='pull-right'>
				<ul class="pagination">

					<c:if test="${pageMaker.prev}">
						<li class="paginate_button previous"><a
								href="${pageMaker.startPage -1}">Previous</a></li>
					</c:if>
							
					<c:forEach var="num" begin="${pageMaker.startPage}"
								end="${pageMaker.endPage }">
						<li class='paginate_button  ${pageMaker.cri.page == num ? "active":""} '>
							<a href="${num}">${num}</a>
						</li>
					</c:forEach>

					<c:if test="${pageMaker.next}">
						<li class="paginate_button next"><a
								href="${pageMaker.endPage +1 }">Next</a></li>
						</c:if>

				</ul>
			</div>
	</div>
			
			<form id='actionForm' action="/todolists" method='get'>
				<input type='hidden' name='page' value='${pageMaker.cri.page}'>
				<input type='hidden' name='perPageNum' value='${pageMaker.cri.perPageNum}'>
			</form>

  
</body>


<script>
$(document).ready(function() {
	var actionForm = $("#actionForm");
	$(".paginate_button a").on(
		"click",
		function(e) {
			e.preventDefault();
			console.log('click');
			actionForm.find("input[name='page']").val($(this).attr("href"));
			actionForm.submit();
		});
});
</script>
</html>