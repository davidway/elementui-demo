package test.blockchain.test;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Arrays;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import test.blockchain.java.AssetInfo;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.blockchain.DTO.BaseParamDTO;

public class testLogger {
	final static Logger logger = LoggerFactory.getLogger("issueLogger");

	@Test
	public void test() {
		AssetTransferToThirdSubmitApplyForm assetTransferSignFormVO = new AssetTransferToThirdSubmitApplyForm();
		AssetInfo assetInfo1 = new AssetInfo();
		assetInfo1.setSrc_amount(BaseParamDTO.user_account_address);
		assetInfo1.setFee_amount(BaseParamDTO.third_user_account_address);
		assetInfo1.setAsset_id("26a8opMYhniqQW5igf8axpksHHCB5XyuVNfvWqJsemWNz6c");
		JSONArray jsonArray = new JSONArray();
		JSONObject finalAssetInfo = new JSONObject();
		JSONObject jsonAssetInfo = (JSONObject) JSON.toJSON(assetInfo1);
		jsonArray.add(jsonAssetInfo);
	
		finalAssetInfo.put("asset_info", jsonArray);
		assetTransferSignFormVO.setAssetInfo(finalAssetInfo);
		System.out.println(assetTransferSignFormVO);
	}

	@Test
	public void test2() {
		int[] salesDepartmentIdList = { 1, 22, 34, 2, 34 };
		int[][] subDepartmentIdList = new int[5][10];
		int[][] groupIdList = new int[5][10];

		for (int i = 0; i < salesDepartmentIdList.length; i++) {
			subDepartmentIdList[i] = getSubdDepartmentIdBySId(salesDepartmentIdList[i]);
			for (int j = 0; j < subDepartmentIdList[i].length; j++) {
				groupIdList[j] = getGroupIdbySubdepartmentId(subDepartmentIdList[j]);
			}
		}
		System.out.println("salesDepartmentId="+Arrays.toString(salesDepartmentIdList));
		System.out.println("subDepartmentId="+Arrays.toString(subDepartmentIdList[0]));
		System.out.println("groupId="+Arrays.toString(groupIdList[0]));
	}

	private int[] getGroupIdbySubdepartmentId(int[] i) {
		int id[] = { 1, 2, 3, 45 };
		return id;
	}

	private int[] getSubdDepartmentIdBySId(int i) {
		int id[] = { 2, 4, 5, 1, 2 };
		return id;
	}
	
	
	@Test
	public void test3(){
		String a ="{\"amount\":999999999999999990,\"asset_type\":\"1\",\"chain_id\":\"chkpmqytr367uwgfju\",\"content\":{\"wsy\":201806251016},\"ledger_id\":\"ldj9xey32i9mq76mvk\",\"mch_id\":\"gb433b4b8de489629\",\"mch_sign\":\"MEQCIEAl8f/ghutLRoI48lCCGQTExPewKeHdJ5WmTninBPH3AiBPmx8duV/fOcvarX+rlhuOrfJ3lfUDBllv1VTpqHD8zg==\",\"node_id\":\"ndns65jwuhdv2heukt\",\"owner_account\":\"1LgAVV3UPvMUyV9PjrCLp5mdyxF9WiTXfY\",\"sign_type\":\"ECDSA\",\"source_id\":\"201806251016\",\"timestamp\":1530004190,\"unit\":\"wsy\",\"version\":\"1.0\"}";
		JSONObject json = JSON.parseObject(a);
		Long amount = json.getLong("amount");
		System.out.println(amount);
	}
	
	@Test
	 public void test4(){
		String a ="27tC7oRrnbajvBb1G7jksAc5bX3FknuQABHj2HCT31E5Bv7,26aAKB9Y3UCxPuDuag16vvnqBP2ZZP9dhWtqjBUa4hfy29y,26aAKK9xo73RhGd8c2YYDPBD6K6fvpwGrNcBBAJbeMFWwyr,26aB9EmzTJr7bLvCfBWTVTa72ypMqvcjKzg8XNyBpJVXLyi,26aBjxZpkXfiEmsEuft8xT2h3PMb5M2SBt3So5Ftyyo3eR5,26aBR7BVm8GVd1Bfug4GS5Qi3RHFHcCvnNUUcrkNnvgGJTq,26aBz3VDdmbv4p1uYDcbxnX96WNsnGoNzbpZFa3nsT588Jb,26aBaDhv2vfmsrYpa2a2cFW5tTHnuD61NDRrWFCYoHVXvYN,26aA865Lgb4VwBKTBtATsLipXBkKZnBkGMJbcPfRBfP8Qid,26aBM6TpJ6kdcME5WUJffth42duvFjpSGEpCM68sfJACf5L,26aCczsZv2zwejRk8UBX33SFDpnY3uBSuNQmn23yiard1kr,26aC3BGzC9kdGBgrPsmwrXD3XBcFCFRZ9Jdrkxn83JsRtda,26aDjHDziJVJ9xiJ44YSgVrawtBDuQsGKRATm5L65e6i4Aq,26aDoftvakxZuckysxBNv45VFw4HNxv8Cxc4xAprKUoUrHM,26aDTBTuMCWzmMATAmHr2jzNfYEvuJBHGSyoHUT7niw71By,26aEmwrppe5ohavunzdE9BWrJSBbyWx6XTpp3S539pAYYqT,26aFQwcg884TRSGb5hWLm1LqTGxjKvwwpvh3vZMk29DKXqe,26aCi1FFM7i7N16jndhvZeRpgVTULBtgPZ7fRYz2KouUmgM,26aD4q6ZwaGFf7LzZqanLWpC8PyW2LS2sgVR4ZMYaaoKCbi,26aFRoMMWmimRtCknMbvbLkBR4SRVdYXcfYf23zJ2XMkZ1e,26aGvQfNbAa6TWFbRzUkwrNi8dEV4oseXBZdAqhudPkkuXu,26aHUagMmKtFTjBv8Ea5hXpDjGN7Ly4329TyXuqzmL5C6Xi,26aJXbKU1vHLme5d4jTmVsGgdG3DXtomtm4bU1oicgLboct,26aMSvrqqN7uNLqkdW6kFxeU3WCKueUfHbBhiCP8U3LLUdr,26aMUsE4e8m1sMs1PrKbsseLQwse1hNzApUYWSEwgevQBWv,26aN91ZnwQn3ztCsSP6pUv9HeA2zW79tajvU4ndoXMHiADw,26aK3Eo2C3XuGVzbUmtsJatTtp3C7fDhZfSzCcCtwFPdUnb,26aHQVHBJp3CqYqywNg3bE3hnsREGx3jDmJnCJH8NgUEMR5,26aJ9avfop5yTPmLt9qptrSd8K3vxksgo4xZZq8iTnWfkoc,26aJY5J2yXbeG8dgnwQXQbDZoiXXUdhaM4g53EayXXdrrGA,26aQpXtxBUTez3mAJo5evkaxeX7UM6nNGf8oBfZHJdtzxx5,26aNAGPE55F7dx2sUMum6pNZMqe1JuC1jroa2d2SUXGPTbM,26aNKT6uyLxdWUyDe8Ms82zKvpZMPbhdCgPggREW3XF19gN,26aPiSo7k3WRdLv1FktqeJBotNFV46CywdY3UW1dVgGriqC,26aQ2Fd1sy1pWVidq6pRaKwKGeqNfH789PAoD2RzyF6dnT1,26aNj8ZX5tJqXgttsqeET3PWP4rAvpjmiJLpL54RRJfju3u,26aQAboBznnQfHBCuqKgi74V3H6y9bFW8cuZSCWYb2vEuya,26aQdsnwQBQsNVJzFJ1M2aohw99WNGUGavQmLwJoUogHd1R,26aPa2Xjv5sk6PPVnwgp19ycexSZywFMaNpHcRZ21VwCrpV,26aPkk8JuzXNjKbNpdwVB4S9F7mT44d3EuGNsVEFgEozBop,27tHyRYKXMs6pYxe7pRvCoWqoKqxnK3ufDAtsAFARgJf3uJ,26aR9phh6K53TfNKhoJMWFFrQbcxWmnsPTumYxFEAFvrisV,26aRDJSCYmaKpXLbFEAv766bzXxAVDytF67cLvu6pAWX2Sw,26aRhcRcu4JUnpnj5xSbs7rPo3yveCJKA2H3Ya9PA2XbEo5,26aRRyTA78kEcibs4XYLtUndzqbcdnwHmb8cuRmNAuC9YwY,26aRyFDd4EZJSHUihhXh2bjVccwZPGsBSkBEH4k1uATKwsn,29CTxp1CRREar1UAfi1w7iR7HBedQvkJ6QNVyUJ2ZgJ2rtF,26aRxKqrMUwTXGNuPhmyF8Gu9p9CovnMuYFzCPE7bg97Urp";
		
		String[] b=  a.split(",");
		StringBuffer string=new StringBuffer("");
		boolean first = true;
		for  (int i=14 ; i<b.length; i++){
			if ( first){
				string.append(b[i]);
				first = false;
			}else{
				string.append(","+b[i]);
			}
		}
		System.out.println(string.toString());
		System.out.println(b.length);
	}
	
	@Test 
	public void testStringSplit(){
		String a="asd,1234";
		String[] b = a.split(",");
		System.out.println(b.length);
	}

}
