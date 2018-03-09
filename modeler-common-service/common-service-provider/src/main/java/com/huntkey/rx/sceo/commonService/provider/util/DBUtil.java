package com.huntkey.rx.sceo.commonService.provider.util;

/***********************************************************************
 * @author chenxj												      
 * 																	  
 * @email: kaleson@163.com											  
 * 																	  
 * @date : 2017年6月27日 下午5:18:43											 
 *																	  															 
 **********************************************************************/
public class DBUtil {
	private static final String dot_split = ".";
	
	/**
	 * 根据edm的名字获取物理表的名称
	 * 
	 * @param edmName: edm类名称
	 * 
	 * @return 对应的物理表名称
	 * 
	 * eg: 1. people --> people
	 * 	   2. people.peop001 ---> people_peop001A
	 * 	   3. people.peop001.peop005.peop018---> people_peop018D;
	 */
	public static String getTableName(String edmName) {
		if(edmName == null || "".equals(edmName)){
			return edmName;
		}
		
		// 去掉前后空格
		edmName = edmName.trim();
		while(edmName.startsWith(" ")){
			edmName = edmName.substring(1);
		}
		
		// 如果参数不包含“.”, 说明传入的就是类名，因为约定，类名就是表名，直接返回；
		if(!edmName.contains(dot_split)){
			return edmName;
		} 
		
		// 有效性校验，1. 不用用'.'打头或者结尾，   2. 不能包含二个或以上连续的'.',
		if(edmName.startsWith(dot_split)  || edmName.endsWith(dot_split) || edmName.contains("..")){
			throw new IllegalArgumentException("参数edm名称'"+ edmName + "'无效!");
		}
		
		String [] subs = edmName.split("\\.");
		int size = subs.length;
		char c = (char) (63 + size);
		
		String tableName = subs[0]+"_" + subs[size-1] + c ;
		
		return tableName;
	}
	
//	public static void main(String[] args) {
//		System.out.println(getTableName("person"));
//		System.out.println(getTableName("person.pers001"));
//		System.out.println(getTableName("person.person001.person021"));
//		System.out.println(getTableName("person.persson001.person021.person992"));
//
////		System.out.println(getTableName("person."));
////		System.out.println(getTableName("person...pers001"));
////		System.out.println(getTableName(" .person.person001.person021"));
////		System.out.println(getTableName("  person.persson001.person021.person992  "));
//	}
}
