package com.blockchain.service.ethereum.impl;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.security.InvalidAlgorithmParameterException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.FunctionReturnDecoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.crypto.Bip39Wallet;
import org.web3j.crypto.CipherException;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.WalletUtils;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.admin.Admin;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.request.Transaction;
import org.web3j.protocol.core.methods.response.EthCall;
import org.web3j.protocol.core.methods.response.EthEstimateGas;
import org.web3j.protocol.core.methods.response.EthGetBalance;
import org.web3j.protocol.core.methods.response.EthGetTransactionCount;
import org.web3j.protocol.core.methods.response.Log;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.ClientTransactionManager;
import org.web3j.tx.Contract;

import com.blockchain.exception.ServiceException;
import com.blockchain.exception.StatusCode;
import com.blockchain.service.ethereum.EthUserService;
import com.blockchain.service.ethereum.dto.EthAccountQueryFormDto;
import com.blockchain.service.ethereum.dto.EthAndTokenAssetDto;
import com.blockchain.service.ethereum.dto.EthTransInfoDto;
import com.blockchain.service.ethereum.dto.EthUserFormDto;
import com.blockchain.service.ethereum.dto.EthereumConfig;
import com.blockchain.service.ethereum.ethjava.TokenERC20;
import com.blockchain.service.ethereum.ethjava.utils.Environment;
import com.blockchain.service.ethereum.vo.EthereumWalletInfo;
import com.blockchain.service.tencent.dto.AssetTransQueryFormDto;
import com.blockchain.service.tencent.trustsql.sdk.TrustSDK;
import com.blockchain.service.tencent.trustsql.sdk.exception.TrustSDKException;

@Service("EthUserServiceImpl")
public class EthUserServiceImpl implements EthUserService {
	
	private static Admin 	admin = Admin.build(new HttpService(Environment.getRpcUrl()));

	private static Logger logger = Logger.getLogger(EthUserServiceImpl.class);
	public static Web3j web3j = Web3j.build(new HttpService(Environment.getRpcUrl()));

	@Override
	public BigInteger accountTokenQuery(EthAccountQueryFormDto assetFormVO) throws TrustSDKException, Exception {
		String accoutAddress =assetFormVO.getAssetAccount();
		//0xa944fee3cd9a479c94310c00dff27b16048f15f3
		EthereumConfig etherumConfig = new EthereumConfig();
		String contractAddress = etherumConfig.getContractAddress();
		
		getTokenBalance(accoutAddress);
		web3j = Web3j.build(new HttpService(Environment.getRpcUrl()));
	
		BigInteger tokenBalance = getTokenBalance(web3j, accoutAddress, contractAddress);
		return tokenBalance;
	}

	@Override
	public List<EthTransInfoDto> transQuery(AssetTransQueryFormDto assetForm) throws ServiceException, TrustSDKException, Exception {
		EthereumConfig etherumConfig = new EthereumConfig();
		String contractAddress = etherumConfig.getContractAddress();
		Web3j web3j = Web3j.build(new HttpService(Environment.getRpcUrl()));
		ClientTransactionManager transactionManager = new ClientTransactionManager(web3j, contractAddress);
		TokenERC20 token = TokenERC20.load(contractAddress, web3j, transactionManager, Contract.GAS_PRICE, Contract.GAS_LIMIT);
		token.transferEventObservable(DefaultBlockParameterName.EARLIEST, DefaultBlockParameterName.LATEST).limit(10).subscribe(tx -> {
			String toAddress = tx.to;
			String fromAddress = tx.from;
			BigInteger value = tx.value;
			BigDecimal decimal = new BigDecimal(value).divide(BigDecimal.TEN.pow(18));
			Log transLog = tx.log;
			String transHash = transLog.getTransactionHash();
			logger.debug("转账开始方"+fromAddress+"，转账给"+toAddress+",转账金额："+decimal+"transHash="+transHash);
		});
		return null;
	}

	@Override
	public EthereumWalletInfo addUserHasBaseAccount(EthUserFormDto userFormDto) {

		EthereumWalletInfo userInfoVo = new EthereumWalletInfo();
		String password = userFormDto.getPassword();

		try {
			userInfoVo = createAccount(password, WalletUtils.getTestnetKeyDirectory());
			
		} catch (InvalidAlgorithmParameterException | NoSuchAlgorithmException | NoSuchProviderException | CipherException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return userInfoVo;
	}

	/**
	 * 查询代币余额
	 */
	public static BigInteger getTokenBalance(Web3j web3j, String fromAddress, String contractAddress) {

		String methodName = "balanceOf";
		List<Type> inputParameters = new ArrayList<>();
		List<TypeReference<?>> outputParameters = new ArrayList<>();
		Address address = new Address(fromAddress);
		inputParameters.add(address);

		TypeReference<Uint256> typeReference = new TypeReference<Uint256>() {
		};
		outputParameters.add(typeReference);
		Function function = new Function(methodName, inputParameters, outputParameters);
		String data = FunctionEncoder.encode(function);
		Transaction transaction = Transaction.createEthCallTransaction(fromAddress, contractAddress, data);

		EthCall ethCall;
		BigInteger balanceValue = BigInteger.ZERO;
		try {
			ethCall = web3j.ethCall(transaction, DefaultBlockParameterName.LATEST).send();
			List<Type> results = FunctionReturnDecoder.decode(ethCall.getValue(), function.getOutputParameters());
			balanceValue = (BigInteger) results.get(0).getValue();
			balanceValue = balanceValue.divide(new BigInteger("10").pow(18));
			logger.debug(address+",余额为"+balanceValue);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return balanceValue;
	}

	/**
	 * 导入私钥
	 *
	 * @param privateKey
	 *            私钥
	 * @param password
	 *            密码
	 * @param directory
	 *            存储路径 默认测试网络WalletUtils.getTestnetKeyDirectory() 默认主网络
	 *            WalletUtils.getMainnetKeyDirectory()
	 * @return
	 * @throws NoSuchProviderException
	 * @throws NoSuchAlgorithmException
	 * @throws InvalidAlgorithmParameterException
	 * @throws CipherException
	 */
	private static EthereumWalletInfo createAccount(String password, String directory) throws InvalidAlgorithmParameterException, NoSuchAlgorithmException, NoSuchProviderException, CipherException {

		Bip39Wallet bip39Wallet = null;
		EthereumWalletInfo wallet = new EthereumWalletInfo();
		String address = "";
		try {

			bip39Wallet = WalletUtils.generateBip39Wallet("", new File(directory));
			String assistWord = bip39Wallet.getMnemonic();
			String keyStoreName = bip39Wallet.getFilename();
			Credentials credentials = WalletUtils.loadCredentials("", directory+"/"+keyStoreName);
			wallet.setAddress(credentials.getAddress());
			wallet.setAssitWords(assistWord);
			wallet.setKeystore(keyStoreName);
			wallet.setPassword(password);
			wallet.setBasePrivateKey(credentials.getEcKeyPair().getPrivateKey().toString(16));
		} catch (CipherException | IOException e) {
			e.printStackTrace();
		}

		return wallet;
	}

	/**
	 * 导出私钥
	 * 
	 * @param wallet
	 *
	 * @param keystorePath
	 *            账号的keystore路径
	 * @param password
	 *            密码
	 * @return
	 */
	private static EthereumWalletInfo exportPrivateKeyAndAddress(EthereumWalletInfo wallet, String keystorePath, String password) {

		try {
			Credentials credentials = WalletUtils.loadCredentials(password, keystorePath);
			BigInteger privateKey = credentials.getEcKeyPair().getPrivateKey();
			String address = credentials.getAddress();
			wallet.setAddress(address);
			wallet.setBasePrivateKey(privateKey.toString(16));
		} catch (IOException | CipherException e) {
			e.printStackTrace();
		}
		return wallet;
	}


	/**
	 * 获取余额
	 *
	 * @param address
	 *            钱包地址
	 * @return 余额
	 */
	private static BigInteger getTokenBalance(String address) {
		BigInteger balance = null;
		try {
			EthGetBalance ethGetBalance = web3j.ethGetBalance(address, DefaultBlockParameterName.LATEST).send();
			balance = ethGetBalance.getBalance();
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("address " + address + " balance " + balance + "wei");
		return balance;
	}

	/**
	 * 生成一个普通交易对象
	 *
	 * @param fromAddress
	 *            放款方
	 * @param toAddress
	 *            收款方
	 * @param nonce
	 *            交易序号
	 * @param gasPrice
	 *            gas 价格
	 * @param gasLimit
	 *            gas 数量
	 * @param value
	 *            金额
	 * @return 交易对象
	 */
	private static Transaction makeTransaction(String fromAddress, String toAddress, BigInteger nonce, BigInteger gasPrice, BigInteger gasLimit, BigInteger value) {
		Transaction transaction;
		transaction = Transaction.createEtherTransaction(fromAddress, nonce, gasPrice, gasLimit, toAddress, value);
		return transaction;
	}

	/**
	 * 获取普通交易的gas上限
	 *
	 * @param transaction
	 *            交易对象
	 * @return gas 上限
	 */
	private static BigInteger getTransactionGasLimit(Transaction transaction) {
		BigInteger gasLimit = BigInteger.ZERO;
		try {
			EthEstimateGas ethEstimateGas = web3j.ethEstimateGas(transaction).send();
			gasLimit = ethEstimateGas.getAmountUsed();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return gasLimit;
	}

	/**
	 * 获取账号交易次数 nonce
	 *
	 * @param address
	 *            钱包地址
	 * @return nonce
	 */
	private static BigInteger getTransactionNonce(String address) {
		BigInteger nonce = BigInteger.ZERO;
		try {
			EthGetTransactionCount ethGetTransactionCount = web3j.ethGetTransactionCount(address, DefaultBlockParameterName.PENDING).send();
			nonce = ethGetTransactionCount.getTransactionCount();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return nonce;
	}

	@Override
	public BigDecimal accountEthQuery(EthAccountQueryFormDto assetForm) throws TrustSDKException, Exception {
		String account =assetForm.getAssetAccount();
		BigDecimal rest = getEthBalance(account);
		return rest;
	}
	private static BigDecimal getEthBalance(String address) {
		BigInteger balance = null;
		BigDecimal eth = null;
		try {
			EthGetBalance ethGetBalance = web3j.ethGetBalance(address, DefaultBlockParameterName.LATEST).send();
			balance = ethGetBalance.getBalance();
			 eth = new BigDecimal(balance);
			 eth = eth.divide(BigDecimal.TEN.pow(18));
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("address " + address + " balance " + eth + "Eeh");
		return eth;
	}



	@Override
	public EthAndTokenAssetDto ethereumAccountQuery(EthAccountQueryFormDto accountQueryFormDto) throws TrustSDKException, Exception {
		BigDecimal ethBalance = accountEthQuery(accountQueryFormDto);
		BigInteger tokenBalance = accountTokenQuery(accountQueryFormDto);
		
		EthAndTokenAssetDto ethAndTokenAssetDto = new EthAndTokenAssetDto();
		ethAndTokenAssetDto.setEthereumBalance(ethBalance.toString());
		ethAndTokenAssetDto.setTokenBalance(tokenBalance.toString());
		return ethAndTokenAssetDto;
	}

	
	

}