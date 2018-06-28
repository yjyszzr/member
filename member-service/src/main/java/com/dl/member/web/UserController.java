package com.dl.member.web;

import io.swagger.annotations.ApiOperation;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Base64;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import tk.mybatis.mapper.entity.Condition;
import tk.mybatis.mapper.entity.Example.Criteria;

import com.dl.base.result.BaseResult;
import com.dl.base.result.ResultGenerator;
import com.dl.base.util.SessionUtil;
import com.dl.member.configurer.URLConfig;
import com.dl.member.dto.ChannelCustomerBindDTO;
import com.dl.member.dto.ImgShowDTO;
import com.dl.member.dto.UserDTO;
import com.dl.member.dto.UserNoticeDTO;
import com.dl.member.model.DlChannelConsumer;
import com.dl.member.model.DlChannelDistributor;
import com.dl.member.model.User;
import com.dl.member.param.MobileNumberParam;
import com.dl.member.param.QueryUserNoticeParam;
import com.dl.member.param.SetLoginPassParam;
import com.dl.member.param.StrParam;
import com.dl.member.param.UpdateImgParam;
import com.dl.member.param.UpdateUnReadNoticeParam;
import com.dl.member.param.UserHeadInfoParam;
import com.dl.member.param.UserIdParam;
import com.dl.member.param.UserIdRealParam;
import com.dl.member.param.UserLoginPassParam;
import com.dl.member.service.DlChannelConsumerService;
import com.dl.member.service.UserService;
import com.dl.member.util.FileUpload;

/**
 * Created by CodeGenerator on 2018/03/08.
 */
@RestController
@RequestMapping("/user")
public class UserController {
	@Resource
	private UserService userService;

	@Resource
	private DlChannelConsumerService dlChannelConsumerService;

	@Resource
	public URLConfig urlConfig;

	/**
	 * 校验手机号
	 * 
	 * @param mobileNumberParam
	 * @return
	 */
	@ApiOperation(value = "校验手机号", notes = "校验手机号")
	@PostMapping("/validateMobile")
	public BaseResult<String> validateMobile(@RequestBody MobileNumberParam mobileNumberParam) {
		return userService.validateUserMobile(mobileNumberParam.getMobileNumber());
	}

	/**
	 * 修改用户登录密码
	 * 
	 * @param userLoginPassParam
	 * @return
	 */
	@ApiOperation(value = "修改用户登录密码", notes = "修改用户登录密码")
	@PostMapping("/updateLoginPass")
	public BaseResult<String> updateLoginPass(@RequestBody UserLoginPassParam userLoginPassParam) {
		return userService.updateUserLoginPass(userLoginPassParam.getUserLoginPass(), userLoginPassParam.getMobileNumber(), userLoginPassParam.getSmsCode());
	}

	@ApiOperation(value = "设置用户登录密码", notes = "设置用户登录密码")
	@PostMapping("/setLoginPass")
	public BaseResult<String> setLoginPass(@RequestBody SetLoginPassParam param) {
		Integer userId = SessionUtil.getUserId();
		return userService.setUserLoginPass(param, userId);
	}

	/**
	 * 查询用户信息除了登录密码和支付密码
	 * 
	 * @param userLoginPassParam
	 * @return
	 */
	@ApiOperation(value = "查询用户信息除了登录密码和支付密码", notes = "查询用户信息除了登录密码和支付密码")
	@PostMapping("/userInfoExceptPass")
	public BaseResult<UserDTO> queryUserInfo(@RequestBody StrParam strParam) {
		// 测试prod1.0.0 修改
		return userService.queryUserByUserIdExceptPass();
	}

	@ApiOperation(value = "查询用户卡券或消息提示", notes = "查询用户卡券或消息提示")
	@PostMapping("/queryUserNotice")
	public BaseResult<UserNoticeDTO> queryUserNotice(@RequestBody QueryUserNoticeParam param) {
		Integer userId = SessionUtil.getUserId();
		UserNoticeDTO queryUserNotice = userService.queryUserNotice(userId);
		return ResultGenerator.genSuccessResult("success", queryUserNotice);
	}

	@ApiOperation(value = "更新未读消息提示", notes = "更新未读消息提示")
	@PostMapping("/updateUnReadNotice")
	public BaseResult<String> updateUnReadNotice(@RequestBody UpdateUnReadNoticeParam param) {
		Integer userId = SessionUtil.getUserId();
		int rst = userService.updateUnReadNotice(userId, param.getType());
		return ResultGenerator.genSuccessResult("success");
	}

	@ApiOperation(value = "根据用户ID查询", notes = "根据用户ID查询")
	@PostMapping("/queryUserInfo")
	public BaseResult<UserDTO> queryUserInfo(@RequestBody UserIdParam param) {
		UserDTO userDTO = userService.queryUserInfo(param);
		return ResultGenerator.genSuccessResult("succ", userDTO);
	}

	@ApiOperation(value = "根据用户ID查询真实信息", notes = "根据用户ID查询")
	@PostMapping("/queryUserInfoReal")
	public BaseResult<UserDTO> queryUserInfoReal(@RequestBody UserIdRealParam param) {
		UserDTO userDTO = userService.queryUserInfoReal(param);
		return ResultGenerator.genSuccessResult("succ", userDTO);
	}

	@ApiOperation(value = "查询用户信息除了登录密码和支付密码", notes = "查询用户信息除了登录密码和支付密码")
	@PostMapping("/userInfoExceptPassReal")
	public BaseResult<UserDTO> queryUserByUserIdExceptPassReal(@RequestBody UserIdParam param) {
		return userService.queryUserByUserIdExceptPassReal();
	}

	/**
	 * 查询用户是否与店员绑定过
	 * 
	 * @param UserBonusParam
	 * @return
	 */
	@ApiOperation(value = "查询用户是否与店员绑定过", notes = "查询用户是否与店员绑定过")
	@PostMapping("/queryChannelDistributorByUserId")
	public BaseResult<ChannelCustomerBindDTO> queryChannelDistributorByUserId(@RequestBody UserIdParam params) {
		Condition condition = new Condition(DlChannelDistributor.class);
		Criteria criteria = condition.createCriteria();
		criteria.andCondition("user_id =", params.getUserId());
		List<DlChannelConsumer> channelConsumers = dlChannelConsumerService.findByCondition(condition);
		DlChannelConsumer channelConsumer = new DlChannelConsumer();
		ChannelCustomerBindDTO customerBindDTO = new ChannelCustomerBindDTO();
		if (!CollectionUtils.isEmpty(channelConsumers)) {
			channelConsumer = channelConsumers.get(0);
			BeanUtils.copyProperties(channelConsumer, customerBindDTO);
			return ResultGenerator.genSuccessResult("succ", customerBindDTO);
		}

		return ResultGenerator.genFailResult("failure");
	}

	/**
	 * 根据手机号获取token
	 * 
	 * @param mobileNumberParam
	 * @return
	 */
	// @ApiOperation(value = "根据手机号获取token(不提供调用)", notes =
	// "根据手机号获取token(不提供调用)")
	// @PostMapping("/getTokenByMobile")
	// public BaseResult<String> getTokenByMobile(@RequestBody MobileNumberParam
	// mobileNumberParam) {
	// User user = userService.findBy("mobile",
	// mobileNumberParam.getMobileNumber());
	// if(null == user) {
	// return ResultGenerator.genResult(MemberEnums.DBDATA_IS_NULL.getcode(),
	// "没有该用户，无法生成token");
	// }
	//
	// String token = TokenUtil.genToken(user.getUserId(), 1);
	// return ResultGenerator.genSuccessResult("获取token成功", token);
	// }
	@ApiOperation(value = "上传头像", notes = "上传头像")
	@PostMapping("/uploadHeadImg")
	public BaseResult<ImgShowDTO> uploadHeadImg(@RequestBody UpdateImgParam param, BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			return ResultGenerator.genFailResult(bindingResult.getFieldError().getDefaultMessage());
		}
		Integer userId = SessionUtil.getUserId();
		byte[] bytes = Base64.getDecoder().decode(param.getFile());
		for (int i = 0; i < bytes.length; ++i) {
			if (bytes[i] < 0) {// 调整异常数据
				bytes[i] += 256;
			}
		}
		try {
			InputStream input = new ByteArrayInputStream(bytes);
			String base64UserId = Base64.getEncoder().encodeToString(String.valueOf(userId).getBytes());
			String fileName = "headImg/head_" + base64UserId + System.currentTimeMillis() + "." + param.getFileType();
			String filePath = urlConfig.getUploadURL();
			String realName = FileUpload.copyFile(input, filePath, filePath + fileName);
			ImgShowDTO imgShow = new ImgShowDTO();
			imgShow.setImgShowUrl(urlConfig.getImgShowUrl() + realName);
			imgShow.setImgUrl(realName);
			return ResultGenerator.genSuccessResult("更新成功", imgShow);
		} catch (Exception e) {
			e.printStackTrace();
			return ResultGenerator.genFailResult("上传失败！");
		}
	}

	@ApiOperation(value = "修改用户昵称和头像", notes = "修改用户昵称和头像")
	@PostMapping("/updateUserInfo")
	public BaseResult<Integer> updateUserInfo(@RequestBody UserHeadInfoParam userHeadInfoParam) {
		User user = new User();
		user.setHeadImg(userHeadInfoParam.getHeadimg());
		user.setNickname(userHeadInfoParam.getNickname());
		Integer userId = 400056;
		// Integer userId = SessionUtil.getUserId();
		user.setUserId(userId);
		Integer result = userService.updateUserInfo(user);
		return ResultGenerator.genSuccessResult("更新成功", result);
	}
}
