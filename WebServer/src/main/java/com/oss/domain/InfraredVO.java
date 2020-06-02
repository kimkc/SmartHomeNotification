package com.oss.domain;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class InfraredVO {

	private int ino;
	private Date regdate;
	private String name;
}
