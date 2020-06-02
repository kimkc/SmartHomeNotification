package com.oss.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class HourPlaceCountDTO {

	private String hour;
	private String name="";
	private int count=0;
}
