package com.blockchain.exception;


public  class StatusCode {
	/** 成功状态 */
	public static final int  SUCCESS = 0;
	public static final String SUCCESS_MESSAGE="成功";
	/**
	 * 0	访问成功
	100001	未知异常，提示：系统繁忙，请稍后再试
	100002	{参数名称}取值错误或者不可为空
	100003	{参数名称}格式不正确
	100004	{参数名称}长度超过限制
	100005	Token错误
	100006	权限不足
	100007	非法请求
	100008	接口不存在
	100009	请求的HTTP METHOD不支持，请检查是否选择了正确的POST/GET方式
	100010	IP限制不能请求该资源
	100011	IP请求频次超过上限
	100012	用户请求频次超过上限
	100013	用户请求特殊接口{接口名称}频次超过上限
	 */


	/** 系统错误 */
	public static final int SYSTEM_UNKOWN_ERROR = 100001;
	public static final String SYSTEM_UNKOWN_ERROR_MESSAGE = "系统发生未知错误";
	
	/** {参数名称}取值错误或者不可为空 */
	public static final int PARAM_ERROR= 100002;
	public static final String PARAM_ERROR_MESSAGE="前端：参数出错";
	/**{参数名称}格式不正确 */
	public static final int CONFIG_NOT_SET = 100003;	
	public static final String CONFIG_NOT_SET_MESSAGE ="配置文件内容暂未设置完整";
	/** {参数名称}长度超过限制 */
	public static final int PARAM_LENGTH_ERROR = 100004;		
	/** Token错误 */
	public static final int TOKEN_ERROR = 100005;	
	
	/** 非法请求 */
	public static final int  ILLEGAL_REQUEST_ERROR= 100007;	
	/** 接口不存在*/
	public static final int INTERFACE_NOT_EXIST_ERROR = 100008;	
	/** 请求的HTTP METHOD不支持，请检查是否选择了正确的POST/GET方式*/
	public static final int REQUEST_METHOD_ERROR = 100009;	
	/** IP限制不能请求该资源 */
	public static final int IP_AUTHORITY_ERROR = 100010;		
	/** IP请求频次超过上限 */
	public static final int IP_REQUEST_TOO_MANY_TIMES = 100011;		
	/** 用户请求频次超过上限 */
	public static final int USER_REQUEST_TOO_MANY_TIMES = 100012;
	/** 用户请求特殊接口{接口名称}频次超过上限 */
	public static final int USER_REQUEST_SPECIAL_INTERFACE_TOO_MANY_TIMES = 100013;

	/**body的旧密码错误*/
	public static final int OLD_PASSWORD_ERROR=200001;
	/**body的登录帐号或者密码错误*/
	public static final int LOGIN_ERROR=200002;
	/**body的安全密码错误*/
	public static final int SAFEPWD_ERROR=200003;
	/**body的用户没有绑定*/
	public static final int USER_NOT_BIND=200004;


	public static final int SERVICE_EXCEPTION = 201000;
	public static final String SERVICE_ERROR_MESSAGE="后端：业务发生未知错误";
	
	/**提交业务错误*/
	public static final int SUBMIT_ERROR = 201001;
	public static final String SUBMIT_ERROR_MESSAGE="后端：业务执行提交时错误";
	/**申请业务错误*/
	public static final int APPLY_ERROR = 201002;
	public static final String APPLY_ERROR_MESSAGE="后端：业务执行申请时错误";
	
	/**线程溢出并发异常**/
	public static final int THREAD_ERROR = 301000;
	public static final String THREAD_ERROR_MESSAGE="队列得不到执行权限";
	public static final int APPLY_THREAD_ERROR = 301001;
	public static final String APPLY_THREAD_ERROR_MESSAGE="申请得不到执行权限";
	public static final int SUBMIT_THREAD_ERROR = 301002;
	public static final String SUBMIT_THREAD_ERROR_MESSAGE="提交得不到执行权限";
	public static final int PAIR_KEY_ERROR = 100004;
	public static final String PAIR_KEY_ERROR_MESSAGE = "公私钥匹配失败";
	public static final String AUTHORITY_ERROR_MESSAGE = "缺少权限";
	
	public static final int TIME_OUT = 201003;
	public static final String TIME_OUT_MESSAGE="超时错误";
	public static final int URL_NOT_EXISTS = 201004;
	public static final String URL_NOT_EXISTS_MESSAGE="配置文件的URL错误";
	/** 权限不足 */
	public static final int AUTHORITY_ERROR = 201005;	
}
