package com.dl.member.model;

import java.sql.Date;

import lombok.Data;

@Data
public class PromotionIncome {
	private Date time;

	private Integer registerNum;

	private Double lotteryAmount;

	private Double income;

	private String ids;
}
