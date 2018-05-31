package com.dl.member.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class DonationPriceDTO {
	
	@ApiModelProperty("赠送的金额")
	private Double donationPrice;

}
