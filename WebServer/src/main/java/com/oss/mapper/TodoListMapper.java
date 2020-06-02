package com.oss.mapper;

import java.util.List;

import com.oss.domain.CamDTO;
import com.oss.domain.Criteria;
import com.oss.domain.TodoListVO;

public interface TodoListMapper {

	public boolean insertTodo(TodoListVO todoList);
	
	public List<TodoListVO> getThisTime();
	
	public List<TodoListVO> listPage(Criteria cri);
	
	public int countPaging(Criteria cri);
	
	
	//camera...
	public void insertOnNOff(String state);
	
	public CamDTO getState(); 
}
