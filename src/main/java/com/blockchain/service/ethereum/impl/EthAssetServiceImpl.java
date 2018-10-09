package com.blockchain.service.ethereum.impl;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.util.concurrent.CompletableFuture;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.web3j.crypto.CipherException;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.ECKeyPair;
import org.web3j.crypto.RawTransaction;
import org.web3j.crypto.TransactionEncoder;
import org.web3j.crypto.WalletUtils;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.admin.Admin;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.request.Transaction;
import org.web3j.protocol.core.methods.response.EthEstimateGas;
import org.web3j.protocol.core.methods.response.EthGetTransactionCount;
import org.web3j.protocol.core.methods.response.EthSendTransaction;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.ChainId;
import org.web3j.tx.FastRawTransactionManager;
import org.web3j.tx.gas.DefaultGasProvider;
import org.web3j.tx.response.PollingTransactionReceiptProcessor;
import org.web3j.utils.Numeric;

import com.alibaba.fastjson.JSON;
import com.blockchain.exception.ServiceException;
import com.blockchain.exception.StatusCode;
import com.blockchain.service.ethereum.EthAssetService;
import com.blockchain.service.ethereum.dto.EthAssetIssueFormDto;
import com.blockchain.service.ethereum.dto.EthAssetSettleDto;
import com.blockchain.service.ethereum.dto.EthAssetTransferFormDto;
import com.blockchain.service.ethereum.dto.EthereumConfig;
import com.blockchain.service.ethereum.dto.GasInfoDto;
import com.blockchain.service.ethereum.ethjava.TokenERC20;
import com.blockchain.service.ethereum.ethjava.utils.Environment;
import com.blockchain.service.ethereum.util.ChainUtil;
import com.blockchain.service.ethereum.util.EthAssetIssueUtils;
import com.blockchain.service.ethereum.util.EthAssetSettleUtils;
import com.blockchain.service.ethereum.util.EthAssetTransferUtils;
import com.blockchain.service.ethereum.util.SmartContractUtils;
import com.blockchain.service.ethereum.vo.EthAssetIssueVo;
import com.blockchain.service.ethereum.vo.EthAssetSettleVo;
import com.blockchain.service.ethereum.vo.EthAssetTransferVo;
import com.blockchain.service.ethereum.vo.GasInfoVo;
import com.blockchain.service.tencent.dto.AssetTransferDto;
import com.blockchain.service.tencent.trustsql.sdk.exception.TrustSDKException;
import com.blockchain.service.tencent.trustsql.sdk.util.HttpClientUtil;
import com.blockchain.service.tencent.vo.PhpSystemJsonContentVo;

@Service("EthAssetServiceImpl")
public class EthAssetServiceImpl implements EthAssetService {
	public static final Logger issueLogger = LoggerFactory.getLogger("issueLogger");
	public static final Logger transferLogger = LoggerFactory.getLogger("transferLogger");
	public static final Logger settleLogger = LoggerFactory.getLogger("settleLogger");
	public static final int DECIMALS = 18;// 默认token的精度是18，莫改
	private static Admin admin = Admin.build(new HttpService(Environment.getRpcUrl()));

	public static final Integer PRIVATE_KEY_LENGTH = 16;
	CompletableFuture<EthSendTransaction> ethSendTransaction = null;
	private static final Logger logger = LoggerFactory.getLogger(EthAssetServiceImpl.class);
	public static Web3j web3j = Web3j.build(new HttpService(Environment.getRpcUrl()));

	@Override
	public EthAssetIssueVo issueToken(EthAssetIssueFormDto assetIssueFormDto) throws Exception {
		Web3j web3j = Web3j.build(new HttpService(Environment.getRpcUrl()));

		EthereumConfig ethereumConfig = new EthereumConfig();
		String serviceUrl = ethereumConfig.getServiceSystemUrl();
		String issueActionName = ethereumConfig.getIssueActionName();
		String submitUrl = serviceUrl + issueActionName;

		// 校验他是否是离线文件模式或者是私钥模式；
		String privateKey = assetIssueFormDto.getUserPrivateKey();
		String password = assetIssueFormDto.getPassword();

		String keyStore = assetIssueFormDto.getKeyStore();

		// privateKey = getPrivateKey(privateKey, keyStore, password);

		BigInteger gasPrice = new BigInteger(assetIssueFormDto.getGasPrice());
		BigInteger gasLimit = new BigInteger(assetIssueFormDto.getGasLimit());

		String name = assetIssueFormDto.getFullName();
		BigInteger amount = new BigInteger(assetIssueFormDto.getAmount());
		String unit = assetIssueFormDto.getUnit();

		CompletableFuture<TokenERC20> contract = null;
		try {

			Credentials credentials = getCredentials(privateKey, keyStore, password);

			contract = TokenERC20.deploy(web3j, credentials, gasPrice, gasLimit, amount, name, unit).sendAsync();

			contract.thenAccept(transactionReceipt -> {
				EthAssetIssueVo assetIssueVo = new EthAssetIssueVo();

				EthAssetIssueUtils ethAssetIssueUtils = new EthAssetIssueUtils();
				assetIssueVo = ethAssetIssueUtils.generateAssetIssueVo(transactionReceipt, credentials, assetIssueVo, ethereumConfig);
				PhpSystemJsonContentVo phpSystemJsonContent = new PhpSystemJsonContentVo();
				phpSystemJsonContent.setData(assetIssueVo);
				try {
					HttpClientUtil.post(submitUrl, JSON.toJSONString(phpSystemJsonContent));
				} catch (Exception e) {
					logger.error("post时发生错误{}", e);
				}
			}).exceptionally(sendAsyncException -> {

				PhpSystemJsonContentVo phpSystemJsonContent = new PhpSystemJsonContentVo();
				phpSystemJsonContent = phpSystemJsonContent.setUnkownError(sendAsyncException.getMessage());
				logger.error("发行时发生错误异常", sendAsyncException);
				try {
					HttpClientUtil.post(submitUrl, JSON.toJSONString(phpSystemJsonContent));
				} catch (Exception e) {
					logger.error("post时发生错误{}", e);
				}
				return null;
			});
		} catch (ServiceException e) {
			PhpSystemJsonContentVo phpSystemJsonContent = new PhpSystemJsonContentVo();
			phpSystemJsonContent = phpSystemJsonContent.setUnkownError(e.getErrorMessage());
			logger.error("发行时发生错误异常", e);
			try {
				HttpClientUtil.post(submitUrl, JSON.toJSONString(phpSystemJsonContent));
			} catch (Exception ex) {
				logger.error("post时发生错误{}", ex);
			}
			return null;
		}

		// 因为结果给了回调进行，所以return null
		return null;
	}

	private Credentials getCredentials(String privateKey, String keyStore, String password) throws IOException, CipherException, ServiceException {
		if (keyStore != null && StringUtils.isNotBlank(password)) {

			// create a temp file
			File temp = File.createTempFile("tempfile", ".tmp");
			// write it
			BufferedWriter bw = new BufferedWriter(new FileWriter(temp));
			bw.write(keyStore);
			bw.close();
			Credentials c = WalletUtils.loadCredentials(password, temp);

			return c;
		} else if (StringUtils.isNotBlank(privateKey)) {
			return Credentials.create(privateKey);
		} else {
			throw new ServiceException().errorCode(StatusCode.PARAM_ERROR).errorMessage(StatusCode.PARAM_ERROR_MESSAGE);
		}

	}

	@Override
	public EthAssetTransferVo transferToken(EthAssetTransferFormDto assetTransferFormDto) throws TrustSDKException, Exception {

		EthereumConfig ethereumConfig = new EthereumConfig();
		String serviceUrl = ethereumConfig.getServiceSystemUrl();
		String transferActionName = ethereumConfig.getTransferActionName();
		String submitUrl = serviceUrl + transferActionName;

		// 取值
		String keyStore = assetTransferFormDto.getKeyStore();
		String password = assetTransferFormDto.getPassword();
		String privateKey = assetTransferFormDto.getUserPrivateKey();

		String srcAccout = assetTransferFormDto.getSrcAccount();
		String dstAccount = assetTransferFormDto.getDstAccount();

		BigInteger gasPrice = new BigInteger(assetTransferFormDto.getGasPrice());
		BigInteger gasLimit = new BigInteger(assetTransferFormDto.getGasLimit());

		String contractAddress = ethereumConfig.getContractAddress();
		Long amount = Long.valueOf(assetTransferFormDto.getAmount());

		// 校验他是否是离线文件模式或者是私钥模式；
		Credentials credentials = getCredentials(privateKey, keyStore, password);
		EthGetTransactionCount ethGetTransactionCount = null;
		try {
			ethGetTransactionCount = web3j.ethGetTransactionCount(srcAccout, DefaultBlockParameterName.PENDING).send();
		} catch (IOException e) {
			e.printStackTrace();
		}
		if (ethGetTransactionCount == null)
			return null;
		BigInteger nonce = getNonce(assetTransferFormDto.getNonce(), ethGetTransactionCount);
		BigInteger value = BigInteger.ZERO; // value是发送交易的时候要不要带上一些以太币
		try {
			/*** 签名加密 ********/
			String data = SmartContractUtils.genereateSignSmartContractMethodAndParam(dstAccount, amount, "transfer");
			byte chainId = ChainUtil.getChainId();
			String signedData = "";
			signedData = signTransaction(nonce, gasPrice, gasLimit, contractAddress, value, data, chainId, credentials.getEcKeyPair().getPrivateKey().toString(16));

			if (signedData != null) {
				/****** 发送请求 ******/
				CompletableFuture<EthSendTransaction> ethSendTransaction = web3j.ethSendRawTransaction(signedData).sendAsync();

				/************* SendAsync后的回调 **********************/
				ethSendTransaction.thenAccept(transactionReceipt -> {
					EthAssetTransferUtils ethAssetTransferUtils = new EthAssetTransferUtils();
					EthAssetTransferVo assetIssueDTO = ethAssetTransferUtils.genereateTranferParam(nonce, transactionReceipt);

					PhpSystemJsonContentVo phpSystemJsonContent = new PhpSystemJsonContentVo();
					org.web3j.protocol.core.Response.Error error = transactionReceipt.getError();
					if (error != null) {

						phpSystemJsonContent.setRetcode(error.getCode());
						phpSystemJsonContent.setRetmsg(error.getMessage());
					} else {
						phpSystemJsonContent.setData(assetIssueDTO);
					}

					try {
						HttpClientUtil.post(submitUrl, JSON.toJSONString(phpSystemJsonContent));
					} catch (Exception e) {
						logger.error("post时发生错误{}", e);
					}

				}).exceptionally(sendAsyncException -> {
					PhpSystemJsonContentVo phpSystemJsonContent = new PhpSystemJsonContentVo();
					phpSystemJsonContent.setUnkownError(sendAsyncException.getMessage());

					logger.error("发送交易失败", sendAsyncException);
					try {
						HttpClientUtil.post(submitUrl, JSON.toJSONString(phpSystemJsonContent));
					} catch (Exception e) {

						logger.error("post时异常{}", e);
					}
					return null;
				});
			}
		} catch (IOException e) {
			logger.error("post时异常{}", e);
		}

		return null;
	}

	/**
	 * 导出私钥
	 *
	 * @param keystorePath
	 *            账号的keystore路径
	 * @param password
	 *            密码
	 */
	private static String exportPrivateKey(String keystorePath, String password) {
		try {
			Credentials credentials = WalletUtils.loadCredentials(password, keystorePath);
			BigInteger privateKey = credentials.getEcKeyPair().getPrivateKey();
			return privateKey.toString(16);
		} catch (IOException | CipherException e) {
			logger.error("导出私钥异常{}", e);
			e.printStackTrace();
		}
		return null;
	}

	private BigInteger getNonce(String nonceString, EthGetTransactionCount ethGetTransactionCount) {

		BigInteger nonce = null;
		if (StringUtils.isNotBlank(nonceString)) {
			nonce = new BigInteger(nonceString);
		} else {
			synchronized (this) {
				nonce = ethGetTransactionCount.getTransactionCount();
			}
		}
		return nonce;
	}

	@Override
	public EthAssetSettleVo settleToken(EthAssetSettleDto assetSettleFormDto) throws UnsupportedEncodingException, TrustSDKException, Exception {

		// 取值
		String keyStore = assetSettleFormDto.getKeyStore();
		String password = assetSettleFormDto.getPassword();
		String privateKey = assetSettleFormDto.getUserPrivateKey();

		String srcAccout = assetSettleFormDto.getOwnerAccount();

		BigInteger gasPrice = new BigInteger(assetSettleFormDto.getGasPrice());
		BigInteger gasLimit = new BigInteger(assetSettleFormDto.getGasLimit());

		EthereumConfig ethereumConfig = new EthereumConfig();
		String serviceUrl = ethereumConfig.getServiceSystemUrl();
		String settleActionName = ethereumConfig.getSettleActionName();
		String submitUrl = serviceUrl + settleActionName;
		EthereumConfig etherumConfig = new EthereumConfig();
		String contractAddress = etherumConfig.getContractAddress();
		Long amount = Long.valueOf(assetSettleFormDto.getAmount());

		// 校验他是否是离线文件模式或者是私钥模式；
		Credentials credentials = getCredentials(privateKey, keyStore, password);
		EthGetTransactionCount ethGetTransactionCount = null;

		try {
			ethGetTransactionCount = web3j.ethGetTransactionCount(srcAccout, DefaultBlockParameterName.PENDING).send();
		} catch (IOException e) {
			logger.error("post时发生错误{}", e);
		}
		if (ethGetTransactionCount == null)
			return null;
		BigInteger nonce = getNonce(assetSettleFormDto.getNonce(), ethGetTransactionCount);
		BigInteger value = BigInteger.ZERO;

		try {
			/*** 签名加密 ********/
			String data = SmartContractUtils.genereateSignSmartContractMethodAndParam(null, amount, "burn");
			byte chainId = ChainUtil.getChainId();
			String signedData = "";
			signedData = signTransaction(nonce, gasPrice, gasLimit, contractAddress, value, data, chainId, credentials.getEcKeyPair().getPrivateKey().toString(16));

			if (signedData != null) {
				/****** 发送请求 ******/
				CompletableFuture<EthSendTransaction> ethSendTransaction = web3j.ethSendRawTransaction(signedData).sendAsync();

				/************* SendAsync后的回调 **********************/
				ethSendTransaction.thenAccept(transactionReceipt -> {
					EthAssetSettleUtils ethAssetSettleUtils = new EthAssetSettleUtils();
					EthAssetSettleVo assetSettleDto = ethAssetSettleUtils.genereateTranferParam(nonce, transactionReceipt);

					logger.debug(JSON.toJSONString(assetSettleDto));
					PhpSystemJsonContentVo phpSystemJsonContent = new PhpSystemJsonContentVo();
					org.web3j.protocol.core.Response.Error error = transactionReceipt.getError();
					if (error != null) {

						phpSystemJsonContent.setRetcode(error.getCode());
						phpSystemJsonContent.setRetmsg(error.getMessage());
					} else {
						phpSystemJsonContent.setData(assetSettleDto);
					}

					try {
						HttpClientUtil.post(submitUrl, JSON.toJSONString(phpSystemJsonContent));
					} catch (Exception e) {
						logger.error("post时异常{}", e);
					}

				}).exceptionally(sendAsyncException -> {

					PhpSystemJsonContentVo phpSystemJsonContent = new PhpSystemJsonContentVo();
					phpSystemJsonContent.setUnkownError(sendAsyncException.getMessage());
					logger.error("发送交易失败", sendAsyncException);

					try {
						HttpClientUtil.post(submitUrl, JSON.toJSONString(phpSystemJsonContent));
					} catch (Exception e) {
						logger.error("post时异常{}", e);
					}
					return null;
				});
			}
		} catch (IOException e) {
			logger.error("post时异常{}", e);
		}
		return null;
	}

	@Override
	public EthAssetTransferVo transferEth(EthAssetTransferFormDto assetTransferFormDto) throws TrustSDKException, Exception {
		String srcAccout = assetTransferFormDto.getSrcAccount();
		String dstAccount = assetTransferFormDto.getDstAccount();
		String amount = assetTransferFormDto.getAmount();

		// 校验他是否是离线文件模式或者是私钥模式；
		String keyStore = assetTransferFormDto.getKeyStore();
		String password = assetTransferFormDto.getPassword();
		String privateKey = assetTransferFormDto.getUserPrivateKey();

		EthereumConfig ethereumConfig = new EthereumConfig();
		String serviceUrl = ethereumConfig.getServiceSystemUrl();
		String settleActionName = ethereumConfig.getSettleActionName();
		String submitUrl = serviceUrl + settleActionName;

		Credentials credentials = getCredentials(privateKey, keyStore, password);

		byte chainId = ChainUtil.getChainId();
		AssetTransferDto assetTransferDto = new AssetTransferDto();
		Web3j web3j = Web3j.build(new HttpService(Environment.getRpcUrl()));

		BigInteger nonce;
		EthGetTransactionCount ethGetTransactionCount = null;
		try {
			ethGetTransactionCount = web3j.ethGetTransactionCount(srcAccout, DefaultBlockParameterName.PENDING).send();
		} catch (IOException e) {
			e.printStackTrace();
		}
		if (ethGetTransactionCount == null)
			return null;

		nonce = getNonce(assetTransferFormDto.getNonce(), ethGetTransactionCount);

		BigInteger gasPrice = new BigInteger(assetTransferFormDto.getGasPrice());
		BigInteger gasLimit = new BigInteger(assetTransferFormDto.getGasLimit());

		String data = "";
		String signedData;
		try {

			signedData = signTransaction(nonce, gasPrice, gasLimit, dstAccount, new BigInteger(amount), data, chainId, credentials.getEcKeyPair().getPrivateKey().toString(16));

			if (signedData != null) {
				/****** 发送请求 ******/
				CompletableFuture<EthSendTransaction> ethSendTransaction = web3j.ethSendRawTransaction(signedData).sendAsync();

				/************* SendAsync后的回调 **********************/
				ethSendTransaction.thenAcceptAsync(transactionReceipt -> {
					EthAssetSettleUtils ethAssetSettleUtils = new EthAssetSettleUtils();
					EthAssetSettleVo assetSettleDto = ethAssetSettleUtils.genereateTranferParam(nonce, transactionReceipt);

					logger.debug(JSON.toJSONString(assetSettleDto));
					PhpSystemJsonContentVo phpSystemJsonContent = new PhpSystemJsonContentVo();
					org.web3j.protocol.core.Response.Error error = transactionReceipt.getError();
					if (error != null) {

						phpSystemJsonContent.setRetcode(error.getCode());
						phpSystemJsonContent.setRetmsg(error.getMessage());
					} else {
						phpSystemJsonContent.setData(assetSettleDto);
					}

					try {
						HttpClientUtil.post(submitUrl, JSON.toJSONString(phpSystemJsonContent));
					} catch (Exception e) {
						logger.error("post时异常{}", e);
					}

				}).exceptionally(sendAsyncException -> {

					PhpSystemJsonContentVo phpSystemJsonContent = new PhpSystemJsonContentVo();
					phpSystemJsonContent.setUnkownError(sendAsyncException.getMessage());
					logger.error("发送交易失败", sendAsyncException);

					try {
						HttpClientUtil.post(submitUrl, JSON.toJSONString(phpSystemJsonContent));
					} catch (Exception e) {
						logger.error("post时异常{}", e);
					}
					return null;
				});
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		// TODO 增加返回值
		return null;
	}

	/**
	 * 签名交易
	 */
	public static String signTransaction(BigInteger nonce, BigInteger gasPrice, BigInteger gasLimit, String to, BigInteger value, String data, byte chainId, String privateKey) throws IOException {
		byte[] signedMessage;
		RawTransaction rawTransaction = RawTransaction.createTransaction(nonce, gasPrice, gasLimit, to, value, data);

		if (privateKey.startsWith("0x")) {
			privateKey = privateKey.substring(2);
		}
		ECKeyPair ecKeyPair = ECKeyPair.create(new BigInteger(privateKey, 16));
		Credentials credentials = Credentials.create(ecKeyPair);

		if (chainId > ChainId.NONE) {
			signedMessage = TransactionEncoder.signMessage(rawTransaction, chainId, credentials);
		} else {
			signedMessage = TransactionEncoder.signMessage(rawTransaction, credentials);
		}

		String hexValue = Numeric.toHexString(signedMessage);
		return hexValue;
	}

	@Override
	public GasInfoVo getGasInfo(GasInfoDto gasInfoDto) throws IOException {
		GasInfoVo gasInfoVo = new GasInfoVo();
		gasInfoVo.setGasLimit(DefaultGasProvider.GAS_LIMIT);
		BigInteger sum = new BigInteger("0");

		sum = getAllGas(gasInfoDto);

		gasInfoVo.setGasPrice(DefaultGasProvider.GAS_PRICE);
		gasInfoVo.setEthEstimateGas(sum);
		return gasInfoVo;
	}

	private BigInteger getAllGas(GasInfoDto gasInfoDto) throws IOException {
		String fromAddress = gasInfoDto.getSrcAccount();
		String dstAccount = gasInfoDto.getDstAccount();
		Long amount = gasInfoDto.getAmount();
		Integer[] methodType = gasInfoDto.getMethodType();
		BigInteger sum = new BigInteger("0");
		BigInteger estimateGas = new BigInteger("0");

		for (Integer i : methodType) {
			switch (i) {

			case SmartContractUtils.TRANSFER:
				estimateGas = getTransferEstmateGas(fromAddress, dstAccount, amount, "transfer");
				sum = sum.add(estimateGas);
				break;
			case SmartContractUtils.BURN:
				estimateGas = getBurnEstmateGas(fromAddress, dstAccount, amount, "burn");
				sum = sum.add(estimateGas);
				break;
			}
		}
		return sum;
	}

	private BigInteger getBurnEstmateGas(String fromAddress, String dstAccount, Long amount, String methodName) throws IOException {
		String data = SmartContractUtils.genereateSignSmartContractMethodAndParam(null, amount, methodName);
		Transaction t = Transaction.createEthCallTransaction(fromAddress, dstAccount, data);
		EthEstimateGas ethEstimateGas = web3j.ethEstimateGas(t).send();
		BigInteger estimateGas = ethEstimateGas.getAmountUsed();
		return estimateGas;
	}

	private BigInteger getTransferEstmateGas(String fromAddress, String dstAccount, Long amount, String methodName) throws IOException {
		String data = SmartContractUtils.genereateSignSmartContractMethodAndParam(dstAccount, amount, methodName);
		Transaction t = Transaction.createEthCallTransaction(fromAddress, dstAccount, data);
		EthEstimateGas ethEstimateGas = web3j.ethEstimateGas(t).send();
		BigInteger estimateGas = ethEstimateGas.getAmountUsed();
		return estimateGas;
	}

}
