package com.automic.global.util;

public class ConstantGlo {

	public static final String YES = "1";
	public static final String NO = "0";

	public static final int plan_status_modify = 1;// 计划任务状态-编辑
	public static final int plan_status_validate = 2;// 计划任务状态-确认

	public static final String INUSER_ADMIN = "1";// 超级管理员-用户
	public static final String INUSER_SYS = "2";// 系统管理员-用户
	public static final String ROLE_ADMIN = "admin";// 超级管理员-角色

	public static final String PERMISSION_TEMPER = "-1";// 临时授权码

	private static final String[][] permTps = new String[][] { { "-1", "测试权限类" }, { "1", "信息查询类" }, { "2", "信息维护类" },
			{ "3", "信息配置类" }, { "4", "功能操作类" } };

	/**
	 * 获取权限类别名称
	 * 
	 * @param permTp
	 * @return
	 */
	public static String getPermTpNm(String permTp) {
		for (int i = 0; i < permTps.length; i++) {
			String[] tp = permTps[i];
			if (tp[0].equals(permTp)) {

				return tp[1];
			}
		}

		return null;
	}

	// 项目类别
	public static String[][] proj_tps = new String[][] { { "1", "集成项目" }, { "2", "营销管理部" }, { "3", "客服中心" },
			{ "4", "PPP项目" }, { "5", "产品销售" }, { "6", "售后" }, { "7", "规划" }};

	/**
	 * 获取项目类别名称
	 * 
	 * @param projTp
	 * @return
	 */
	public static String getProjTpNm(String projTp) {
		if (projTp == null || projTp.equals("")) {

			return null;
		}

		for (int i = 0; i < proj_tps.length; i++) {
			String[] tp = proj_tps[i];
			if (tp[0].equals(projTp)) {

				return tp[1];
			}
		}

		return null;
	}

	//计划任务
	public static String[][] pt_overCase = new String[][] { { "1", "跟踪" }, { "2", "未完成" }, { "3", "完成" }};
	
	/**
	 * 获取计划任务进度名称
	 * 
	 * @param ptOverCase
	 * @return
	 */
	public static String getPtOverCaseNm(String ptOverCase) {
		if (ptOverCase == null || ptOverCase.equals("")) {

			return "新建";
		}

		for (int i = 0; i < pt_overCase.length; i++) {
			String[] tp = pt_overCase[i];
			if (tp[0].equals(ptOverCase)) {

				return tp[1];
			}
		}

		return null;
	}
}
