package com.dl.member.util;

import java.util.ArrayList;
import java.util.List;

public class BonusUtil {
	
	public static List<Double> getBonusRandomData(Double recharegePrice){
		List<Double> list = new ArrayList<Double>(){{add(0.95);add(0.05);add(0.00);add(0.04);add(0.5);add(1.00);}};
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

}
