<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@include file="../includes/header.jsp"%>

	<div class="container theme-showcase" role="main">
		<label style="font-size:20px"> <c:out value="${basetime }" /> 시 기준 일기예보</label>
		<div>
			<label style="padding-left: 10px;padding-right:10px;">강수 형태:</label>
			<label style="padding-left: 10px;padding-right:10px;">하늘 상태:</label>
			<label style="padding-left: 10px;padding-right:10px;">온/습도:</label><span></span>
		</div>
		
		<c:choose>
    		<c:when test="${P eq 1}">
        		<img src="/resources/image/umbrella.png" alt="비" width="75px" height="75px" style="margin-right:20px;">
    		</c:when>
    		<c:when test="${P eq 2}">
        		<img src="/resources/image/drops.png" alt="비와 눈 " width="40px" height="40px">+<img src="/resources/image/snow.png" alt="비와 눈 " width="40px" height="40px">
    		</c:when>
    		<c:when test="${P eq 3}">
       			<img src="/resources/image/snow.png" width="75px" height="75x" style="margin-right:20px;">
    		</c:when>
    		<c:when test="${P eq 4}">
        		<img src="/resources/image/umbrella.png" alt="소나기" title="소나기" width="75px" height="75px" style="margin-right:20px;">
    		</c:when>
		</c:choose>


		<c:choose>
    		<c:when test="${S eq 1}">
        		<img src="/resources/image/sun.png" width="75px" height="75px" style="margin-right:20px;">
    		</c:when>
    		<c:when test="${S eq 3}">
        		<img src="/resources/image/cloud.png" alt="구름이 많음 " title="구름이 많음" width="75px" height="75px" style="margin-right:20px;">
    		</c:when>
   			<c:when test="${S eq 5}">
        		<img src="/resources/image/cloudy.png" alt="흐림 " title="흐림" width="75px" height="75px" style="margin-right:20px;">
    		</c:when>
		</c:choose>

		<c:if test="${W eq 4}" >
			<label>바람:</label>
			<img src="/resources/image/wind.png" alt="강한 바람 " title="강한 바람 " width="75px" height="75px">
		</c:if>

		<span style="color:red;font-size:25px"><c:out value="${T }"/></span><span style="font-size:15px">°C/  </span>
		<span style="font-size:25px"><c:out value="${R }"/></span><span style="font-size:15px">%  </span>
		<br>

		<div style="float:right; display:top">
			<label style="font-size:20px;">현재 카메라 보안 모드 상태: </label>
			<button type="button" id="on" class="btn btn-danger btn-lg" data-toggle="modal">
				on  
			</button>
			<button type="button" id="off" class="btn btn-lg" data-toggle="modal" >
				off  
			</button>
		</div>

		<div style="margin:30px">
			<label style="font-size:20px;"> 가정에 전하고 싶은 말을 전달하세요: </label>
			<button type="button" id="sendTTS" class="btn btn-success btn-lg" data-toggle="modal">
				Text To Speech  
			</button>
		</div>
  
		<!-- infrared Sensor db 시작 -->
		<div style="height:300px;width:100%;margin:50px;">
			<iframe src="https://thingspeak.com/channels/867019/charts/2?bgcolor=%23ffffff&color=%23d62020&days=1&dynamic=true&results=150&timescale=10&title=%EC%A7%91%EC%95%88+%EC%98%A8%EB%8F%84&type=line&yaxismax=40&yaxismin=15" style="height:300px;width:100%;"></iframe>
		</div>

		<div style="height:300px;width:100%; margin:50px;">
			<iframe src="https://thingspeak.com/channels/867019/charts/3?bgcolor=%23ffffff&color=%23d62020&days=1&dynamic=true&results=150&timescale=10&title=%EC%A7%91%EC%95%88+%EC%8A%B5%EB%8F%84&type=line&yaxismax=100&yaxismin=10" style="height:300px;width:100%;"></iframe>
		</div>

		<div style="height:300px;width:100%;margin:50px;">
			<iframe src="https://thingspeak.com/channels/867019/charts/1?bgcolor=%23ffffff&color=%23d62020&days=1&dynamic=true&results=30&title=%EC%9C%84%ED%97%98%EA%B8%B0%EA%B8%B0+%EC%A0%91%EA%B7%BC+%EB%B9%88%EB%8F%84&type=line" style="height:300px;width:100%;"></iframe>
		</div>

		<div class="page-header">
  			<h1>오늘 컴퓨터 총 접근 횟수</h1> <!-- 오늘대신 날짜로 변경 가능 -->
		</div>
		<div class="row">
			<div class="col-md-12">
    			<table class="table table-condensed">
					<thead>
						<tr>
							<th>이름</th>
							<th>횟수</th>
						</tr>
					</thead>
					<tbody>
						<c:forEach items="${TodayPlaceCount }" var="todayPlace">
							<tr>
								<td><c:out value="${todayPlace.name }" /> </td>
								<td><c:out value="${todayPlace.count }" /> </td>			
							</tr>
						</c:forEach>
					</tbody>
				</table>
			</div>
		</div>

		<div class="page-header">
   			<h1>현 시간 컴퓨터 접근 횟수</h1> <!-- 현재시간 대신 시간으로 변경 가능 -->
		</div>
		<div class="row">
			<div class="col-md-12">
    			<table class="table table-condensed">
					<thead>
						<tr>
							<th>이름</th>
							<th>횟수</th>
						</tr>
					</thead>
					<tbody>
						<c:forEach items="${HourPlaceCount }" var="hourPlace">
							<tr>
								<td><c:out value="${hourPlace.name }" /> </td>
								<td><c:out value="${hourPlace.count }" /> </td>			
							</tr>
						</c:forEach>
					</tbody>
				</table>
			</div>
		</div>
		<br>
		<br>

		<div class="page-header">
   				<h1>보안 카메라</h1>
		</div>
		<div class="row" style="height:1100px;width:100%;">
  			<iframe src="http://183.100.114.246:5123" style="height:1100px;width:100%;"></iframe>
		</div>
	
		<div class="modal fade" id="ttsModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
        	<div class="modal-dialog">
          		<div class="modal-content">
            		
            		<div class="modal-header">
              			<button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
              			<h4 class="modal-title" id="myModalLabel">TTS</h4>
            		</div>
            		
            		<div class="modal-body">
              			
              			<form id="TTLForm" action="/TTS" method="post">
              				<div class="form-group">
   								<label>Text</label> 
   								<input id="textInput" class="form-control" name='text' value='음성으로 변환할 텍스트를 입력하세요.' style="font-style:italic">
							</div>
          					<div class="form-group">
        						<label>성별</label> 
                				<select  class="form-control" name='style'>
                					<option value="WOMAN_READ_CALM">차분한 여성 낭독체</option>
                					<option value="MAN_READ_CALM">차분한 남성 낭독체</option>
                					<option value="WOMAN_DIALOG_BRIGHT" >밝은 여성 대화체</option>
                					<option value="MAN_DIALOG_BRIGHT">밝은 남성 대화체</option>
                				</select>
      						</div>
      						<div class="form-group">
        						<label>속도</label> 
                				<select  class="form-control" name='speed'>
                					<option value="SS_READ_SPEECH">기본 속도</option>
                					<option value="SS_ALT_FAST_1">빠르게</option>
                					<option value="SS_ALT_SLOW_1">느리게</option>
                				</select>
      						</div>
      					</form>
      				</div>
	  				
	  				<div class="modal-footer">
        				<button id='modalTTL' type="button" class="btn btn-warning">변환하기</button>
        				<button id='modalCloseBtn' type="button" class="btn btn-default">닫기</button>
      				</div>          
      			</div>
          	<!-- /.modal-content -->
      		</div>
      	<!-- /.modal --> 
   		</div>

		<div class="page-header">
  			<h1>할 일 등록</h1>
		</div>
		<div class="row">
   			
   			<div class="modal-header">
            	<h4 class="modal-title" id="myModalLabel">To do List</h4>
        	</div>
            
            <div class="modal-body">
              	<form id="todoForm" action="/todolist" method="post">
              		<div class="form-group">
   						<label>날짜</label> 
   						<input id="regInput" class="form-control" name='regdate' value='2019-10-02 17:00:00 형식으로 입력해주세요.'>
					</div>
          			<div class="form-group">
        				<label>할 일</label> 
                		<input id="todoInput" class="form-control" name='todo' value="예)김광채 설거지하기 ">
      				</div>
      			</form>
      		</div>
	  		
	  		<div class="modal-footer">
        		<button id='regTodoList' type="button" class="btn btn-warning">등록</button>
      		</div>          
        </div>
         
         
      	<c:if test="${not empty isSpeech}" >
      		<p id="success" style="visibility:hidden;"><c:out value="${isSpeech }"/></p>
      	</c:if>
      	<c:if test="${not empty isTodoSuccess}" >
      		<p id="successTodo" style="visibility:hidden;"><c:out value="${isTodoSuccess }"/></p>
      	</c:if>
      
	</div>

              
<script>
$(document).ready(function() {
	//pswtr
	var camState = '<c:out value="${camState.state }"/>'
	
	if (camState == '1'){
		$("#off").hide();	
	}else{
		$("#on").hide();
	}
	
	var modal = $("#ttsModal");
	var modalTTL = $("#modalTTL");
	var TTLForm = $("#TTLForm");
	
	var todoForm = $("#todoForm");
	var regTodoList = $("#regTodoList");
	
    var success = $("#success");
    var successTodo = $("#successTodo");
    
    var regInput = $("#regInput");
    var todoInput = $("#todoInput");
    var textInput = $("#textInput");
    
    if( success.text() == "성공"){
    	alert("전송을 완료하였습니다.");
    }
    
    if( successTodo.text() == "성공"){
    	alert("할 일 등록 성공!");
    }
    
    regInput.on("click",function(e){
    	regInput.val("");
    });
    
    todoInput.on("click",function(e){
    	todoInput.val("");
    });
    
    textInput.on("click",function(e){
    	textInput.val("");
    });
    
    $("#sendTTS").on("click", function(e){
        modal.modal("show");
    });
   
    
  	modalTTL.on("click",function(e){
    	e.preventDefault();

		console.log('TTL click');

		TTLForm.submit();
	          
      });
  	
	$("#modalCloseBtn").on("click", function(e){
    	
    	modal.modal('hide');
    });
	
	$("#close").on("click", function(e){
    	
    	modal.modal('hide');
    });
	
	$("#regTodoList").on("click",function(e){
		e.preventDefault();
		console.log('todoList click');

		todoForm.submit();
	});
	
	$("#off").on("click",function(e){
		e.preventDefault();
		console.log("on")
		$("#off").hide();
		$("#on").show();
		$.ajax({      
        	type:"GET",  
       		url:"http://106.10.35.183:8080/onNoff?state=1",           
        	success:function(args){   
            	alert("on 요청되었습니다.");      
        	},    
        	error:function(e){  
            	alert("on 요청되었습니다.");  
        	}  
    	});
	})
	
	$("#on").on("click",function(e){
		e.preventDefault();
		console.log("off")
		$("#on").hide();
		$("#off").show();
		$.ajax({      
        	type:"GET",  
       		url:"http://106.10.35.183:8080/onNoff?state=0",      
        	success:function(args){   
            	alert("off 요청되었습니다.");      
        	},    
        	error:function(e){  
            	alert("off 요청되었습니다.");  
        	}  
    	});
	})
	
});


</script>

</body>
</html>
