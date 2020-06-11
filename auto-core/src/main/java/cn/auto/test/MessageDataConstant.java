package cn.auto.test;

import java.math.BigDecimal;

public interface MessageDataConstant {

	/**
	 * 积分映射价格
	 */
	enum POINT_MAP_PRICE {
		level_one(null, 1000L, null, new BigDecimal("15")),
		level_two(1000L, 1200L, new BigDecimal("10"), new BigDecimal("20")),
		level_three(1200L, 2000L, new BigDecimal("12"), new BigDecimal("30")),
		level_four(2000L, null, new BigDecimal("20"), null),;

		/** 积分开始（不包含） */
		public  final Long		pointBegin;
		/** 积分结束（包含） */
		public  final Long		pointEnd;
		/** 价格开始（包含） */
		public  final BigDecimal	priceBegin;
		/** 价格结束（包含） */
		public  final BigDecimal	priceEnd;

		POINT_MAP_PRICE(Long pointBegin, Long pointEnd, BigDecimal priceBegin, BigDecimal priceEnd) {
			this.pointBegin = pointBegin;
			this.pointEnd = pointEnd;
			this.priceBegin = priceBegin;
			this.priceEnd = priceEnd;
		}

		/**
		 * 通过积分获取区间
		 * @param point
		 * @return
		 */
		public static POINT_MAP_PRICE getUserByPoint(Long point) {
			for (POINT_MAP_PRICE e : POINT_MAP_PRICE.values()) {

				if (e.pointBegin != null && point <= e.pointBegin) {
					continue;
				}
				if (e.pointEnd != null && point > e.pointEnd) {
					continue;
				}
				return e;
			}
			return null;
		}
	}
}
