<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@include file="../includes/header.jsp"%>


	<div class="container theme-showcase" role="main">
  		<div class="page-header">
   			<h1>컴퓨터 접근 횟수</h1>
  		</div>
 		<div class="row">
			<div class=".col-md-8">
    			<table class="table table-condensed">
					<thead>
						<tr>
							<th>#번호</th>
							<th>등록일</th>
							<th>이름</th>
						</tr>
					</thead>
					<tbody>
						<c:forEach items="${infraredList }" var="inf">
							<tr>
								<td><c:out value="${inf.ino }" /></td>
								<td><fmt:formatDate pattern="yyyy-MM-dd HH:mm:ss" value="${ inf.regdate }" /></td>
								<td><c:out value="${inf.name }" /></td> <!-- <a class='move' href='<c:out value="${chores.cno }" />'> 클릭시 삭제 기능? -->
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
							
				<c:forEach var="num" begin="${pageMaker.startPage}" end="${pageMaker.endPage }">
					<li class='paginate_button  ${pageMaker.cri.page == num ? "active":""} '>
						<a href="${num}">${num}</a>
					</li>
				</c:forEach>

				<c:if test="${pageMaker.next}">
					<li class="paginate_button next"><a href="${pageMaker.endPage +1 }">Next</a></li>
				</c:if>

			</ul>
		</div>
	</div>
					
	<form id='actionForm' action="/infraredList" method='get'>
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