package com.oss.domain;

import java.sql.Date;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class TodayPlaceCountDTO {

	private Date today;
	private String name;
	private int count;
}
