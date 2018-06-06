package com.dl.member.util;

import java.util.ArrayList;
import java.util.List;

import com.dl.member.dto.RechargeBonusLimitDTO;

public class BonusUtil {
	
	
	public static List<Double> getBonusRandomData(Double recharegePrice){
		List<Double> list = new ArrayList<Double>();
		if(recharegePrice >= 10 && recharegePrice <= 20) {
			return new ArrayList<Double>(){{add(0.95);add(0.05);add(0.00);add(0.04);add(0.5);add(1.00);}};
		}else if(recharegePrice > 20 && recharegePrice <= 40) {
			return new ArrayList<Double>(){{add(0.9);add(0.09);add(0.1);add(0.04);add(0.5);add(1.00);}};
		}else if(recharegePrice > 40 && recharegePrice <= 60) {
			return new ArrayList<Double>(){{add(0.85);add(0.13);add(0.02);add(0.04);add(0.5);add(1.00);}};
		}else if(recharegePrice > 60 && recharegePrice <= 80) {
			return new ArrayList<Double>(){{add(0.8);add(0.15);add(0.05);add(0.04);add(0.5);add(1.00);}};
		}else if(recharegePrice > 80 && recharegePrice <= 100) {
			return new ArrayList<Double>(){{add(0.77);add(0.15);add(0.05);add(0.04);add(0.5);add(1.00);}};			
		}else if(recharegePrice > 100 && recharegePrice <= 200) {
			return new ArrayList<Double>(){{add(0.95);add(0.05);add(0.00);add(0.42);add(5.0);add(10.00);}};
		}else if(recharegePrice > 200 && recharegePrice <= 400) {
			return new ArrayList<Double>(){{add(0.9);add(0.09);add(0.01);add(0.42);add(5.0);add(10.0);}};
		}else if(recharegePrice > 400 && recharegePrice <= 600) {
			return new ArrayList<Double>(){{add(0.85);add(0.13);add(0.02);add(0.42);add(5.0);add(10.0);}};
		}else if(recharegePrice > 600 && recharegePrice <= 800) {
			return new ArrayList<Double>(){{add(0.8);add(0.15);add(0.5);add(0.42);add(5.0);add(10.0);}};
		}else if(recharegePrice > 800 && recharegePrice <= 1000) {
			return new ArrayList<Double>(){{add(0.77);add(0.15);add(0.08);add(0.42);add(5.0);add(10.0);}};
		}else if(recharegePrice > 1000 && recharegePrice <= 2000) {
			return new ArrayList<Double>(){{add(0.95);add(0.05);add(0.00);add(4.8);add(50.0);add(100.0);}};
		}else if(recharegePrice > 2000 && recharegePrice <= 4000) {
			return new ArrayList<Double>(){{add(0.9);add(0.09);add(0.01);add(4.8);add(50.0);add(100.0);}};
		}else if(recharegePrice > 4000 && recharegePrice <= 6000) {
			return new ArrayList<Double>(){{add(0.85);add(0.13);add(0.02);add(4.8);add(50.0);add(100.0);}};
		}else if(recharegePrice > 6000) {
			return new ArrayList<Double>(){{add(0.8);add(0.15);add(0.05);add(50.0);add(100.0);add(200.0);}};
		}
		
		return list;
	}
	
	
	public static List<RechargeBonusLimitDTO> getRandomBonusList(Double randomBonusPrice){
		List<RechargeBonusLimitDTO> list = new ArrayList<RechargeBonusLimitDTO>();
		RechargeBonusLimitDTO rechargeBonusLimitDTO = new RechargeBonusLimitDTO();
		if(randomBonusPrice == 0.04) {
			rechargeBonusLimitDTO.setBonusPrice(0.04);
			return new ArrayList<RechargeBonusLimitDTO>(){{add(rechargeBonusLimitDTO);}};
		}else if(randomBonusPrice == 0.5) {
			rechargeBonusLimitDTO.setBonusPrice(0.5);
			return new ArrayList<RechargeBonusLimitDTO>(){{add(rechargeBonusLimitDTO);}};
		}else if(randomBonusPrice == 1) {
			rechargeBonusLimitDTO.setBonusPrice(1.0);
			return new ArrayList<RechargeBonusLimitDTO>(){{add(rechargeBonusLimitDTO);}};
		}else if(randomBonusPrice == 0.42) {
			rechargeBonusLimitDTO.setBonusPrice(0.42);
			return new ArrayList<RechargeBonusLimitDTO>(){{add(rechargeBonusLimitDTO);}};
		}else if(randomBonusPrice == 4.8) {
			rechargeBonusLimitDTO.setBonusPrice(4.8);
			return new ArrayList<RechargeBonusLimitDTO>(){{add(rechargeBonusLimitDTO);}};			
		}else if(randomBonusPrice == 5.0) {
			RechargeBonusLimitDTO rechargeBonusLimitDTO1 = new RechargeBonusLimitDTO();
			rechargeBonusLimitDTO1.setBonusPrice(1.0);
			RechargeBonusLimitDTO rechargeBonusLimitDTO2 = new RechargeBonusLimitDTO();
			rechargeBonusLimitDTO2.setBonusPrice(2.0);
			return new ArrayList<RechargeBonusLimitDTO>(){{add(rechargeBonusLimitDTO1);add(rechargeBonusLimitDTO2);add(rechargeBonusLimitDTO2);}};
		}else if(randomBonusPrice == 10.0) {
			RechargeBonusLimitDTO rechargeBonusLimitDTO1 = new RechargeBonusLimitDTO();
			rechargeBonusLimitDTO1.setBonusPrice(1.0);
			RechargeBonusLimitDTO rechargeBonusLimitDTO2 = new RechargeBonusLimitDTO();
			rechargeBonusLimitDTO2.setBonusPrice(2.0);
			
			return new ArrayList<RechargeBonusLimitDTO>(){{add(rechargeBonusLimitDTO1);add(rechargeBonusLimitDTO2);add(rechargeBonusLimitDTO2);add(rechargeBonusLimitDTO2);add(rechargeBonusLimitDTO2);}};
		}else if(randomBonusPrice == 50.0) {
			for(int i = 0;i < 5;i++) {
				RechargeBonusLimitDTO rechargeBonusLimitDTO1 = new RechargeBonusLimitDTO();
				rechargeBonusLimitDTO1.setBonusPrice(2.0);
				RechargeBonusLimitDTO rechargeBonusLimitDTO2 = new RechargeBonusLimitDTO();
				rechargeBonusLimitDTO2.setBonusPrice(3.0);
				RechargeBonusLimitDTO rechargeBonusLimitDTO3 = new RechargeBonusLimitDTO();
				rechargeBonusLimitDTO3.setBonusPrice(5.0);
				list.add(rechargeBonusLimitDTO1);
				list.add(rechargeBonusLimitDTO2);
				list.add(rechargeBonusLimitDTO3);
			}
			return list;
		}else if(randomBonusPrice == 100.0) {
			for(int i = 0;i < 10;i++) {
				RechargeBonusLimitDTO rechargeBonusLimitDTO1 = new RechargeBonusLimitDTO();
				rechargeBonusLimitDTO1.setBonusPrice(3.0);
				list.add(rechargeBonusLimitDTO1);
			}
			
			for(int i = 0;i < 6;i++) {
				RechargeBonusLimitDTO rechargeBonusLimitDTO2 = new RechargeBonusLimitDTO();
				rechargeBonusLimitDTO2.setBonusPrice(5.0);
				list.add(rechargeBonusLimitDTO2);
			}
			
			for(int i = 0;i < 4;i++) {
				RechargeBonusLimitDTO rechargeBonusLimitDTO3 = new RechargeBonusLimitDTO();
				rechargeBonusLimitDTO3.setBonusPrice(10.0);
				list.add(rechargeBonusLimitDTO3);
			}
			return list;
		}else if(randomBonusPrice == 200.0) {
			for(int i = 0;i < 5;i++) {
				RechargeBonusLimitDTO rechargeBonusLimitDTO1 = new RechargeBonusLimitDTO();
				rechargeBonusLimitDTO1.setBonusPrice(10.0);
				list.add(rechargeBonusLimitDTO1);
			}
			
			for(int i = 0;i < 3;i++) {
				RechargeBonusLimitDTO rechargeBonusLimitDTO2 = new RechargeBonusLimitDTO();
				rechargeBonusLimitDTO2.setBonusPrice(20.0);
				list.add(rechargeBonusLimitDTO2);
			}
			
			for(int i = 0;i < 3;i++) {
				RechargeBonusLimitDTO rechargeBonusLimitDTO3 = new RechargeBonusLimitDTO();
				rechargeBonusLimitDTO3.setBonusPrice(30.0);
				list.add(rechargeBonusLimitDTO3);
			}
			return list;
		}
		return list;
	}

}
