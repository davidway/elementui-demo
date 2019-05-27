
注册接口
```shell
curl -X POST --header 'Content-Type: application/json' --header 'Accept: application/json' -d '{"configDto":{"createUserPrivateKey":"w1ecBrij8PmWy55CW3F7ksgyYZHXkAC3WCaj269KXjI=","createUserPublicKey":"AjSjvrUOvSpMK5ARrebyXqSFsy0\/rDHz7IH2xSDlTmgQ","chainId":"chBiMAet6e3sRmeuws","ledgerId":"111","mchId":"gbHHj3f4gv36p5IQJw","nodeId":"nd8XbJ8hRtUC5GWP7j","host":"http:\/\/134.175.220.183:15910"},
"name":"weiqiang",
"id":"weiqiang"
}' 'http://localhost:8080/new-blockchain/user/addUserHasBaseAccount'

curl -X POST --header 'Content-Type: application/json' --header 'Accept: application/json' -d '{"configDto":{"createUserPrivateKey":"w1ecBrij8PmWy55CW3F7ksgyYZHXkAC3WCaj269KXjI=","createUserPublicKey":"AjSjvrUOvSpMK5ARrebyXqSFsy0\\/rDHz7IH2xSDlTmgQ","chainId":"chBiMAet6e3sRmeuws","ledgerId":"111","mchId":"gbHHj3f4gv36p5IQJw","nodeId":"nd8XbJ8hRtUC5GWP7j","host":"http:\\/\\/134.175.220.183:15910"},"name":"davidway123","id":"davidway123"}' 'http://localhost:8080/new-blockchain/user/addUserHasBaseAccount'
curl -X POST --header 'Content-Type: application/json' --header 'Accept: application/json' -d '{"configDto":{"createUserPrivateKey":"w1ecBrij8PmWy55CW3F7ksgyYZHXkAC3WCaj269KXjI=","createUserPublicKey":"AjSjvrUOvSpMK5ARrebyXqSFsy0\\/rDHz7IH2xSDlTmgQ","chainId":"chBiMAet6e3sRmeuws","ledgerId":"111","mchId":"gbHHj3f4gv36p5IQJw","nodeId":"nd8XbJ8hRtUC5GWP7j","host":"http:\\/\\/134.175.220.183:15910"},"name":"wenzhi","id":"wenzhi"}' 'http://localhost:8080/new-blockchain/user/addUserHasBaseAccount'
curl -X POST --header 'Content-Type: application/json' --header 'Accept: application/json' -d '{"configDto":{"createUserPrivateKey":"w1ecBrij8PmWy55CW3F7ksgyYZHXkAC3WCaj269KXjI=","createUserPublicKey":"AjSjvrUOvSpMK5ARrebyXqSFsy0\\/rDHz7IH2xSDlTmgQ","chainId":"chBiMAet6e3sRmeuws","ledgerId":"111","mchId":"gbHHj3f4gv36p5IQJw","nodeId":"nd8XbJ8hRtUC5GWP7j","host":"http:\\/\\/134.175.220.183:15910"},"name":"shaxin","id":"shaxin"}' 'http://localhost:8080/new-blockchain/user/addUserHasBaseAccount'
```

```json
{"configDto":{"createUserPrivateKey":"w1ecBrij8PmWy55CW3F7ksgyYZHXkAC3WCaj269KXjI=","createUserPublicKey":"AjSjvrUOvSpMK5ARrebyXqSFsy0\/rDHz7IH2xSDlTmgQ","chainId":"chBiMAet6e3sRmeuws","ledgerId":"111","mchId":"gbHHj3f4gv36p5IQJw","nodeId":"nd8XbJ8hRtUC5GWP7j","host":"http:\/\/134.175.220.183:15910"},
"name":"weiqiang",
"id":"weiqiang"
}
```
返回
```json
{
  "data": {
    "name": "davidway123",
    "basePublicKey": "AjWDQprCqYpdICB+XFjTCvtPjTRj7cSqas19YemamUq1",
    "id": "davidway123",
    "baseAccountAddress": "1LVnPKfq1udbx9ZWVWq6nMRP7GU4x1PZcA",
    "basePrivateKey": "olIU4uTpFE18MC3G4WLSSsWacwmhVZen4GnGoDDg3Ik="
  },
  "retmsg": "成功",
  "retcode": 0
}
```

```json
{
  "data": {
    "name": "wenzhi",
    "basePublicKey": "AljX49hJ6Z+ZiGAazv+X0eIPfUVQqGLzfElzKn+xIaym",
    "id": "wenzhi",
    "baseAccountAddress": "17c7P7U6qZn2PZMDLncp7SovxJCDVTzbE4",
    "basePrivateKey": "drOogEmxzX1xrJsHNDiTCCm/kcq/CoZVsc0byi/zinY="
  },
  "retmsg": "成功",
  "retcode": 0
}
```
```json
{
  "data": {
    "name": "shaxin",
    "basePublicKey": "A+VEN8ErratbaBO9qrBmqeSIxGH7lb3L1xcUFZ/WkO9I",
    "id": "shaxin",
    "baseAccountAddress": "12sRxG9Az8URR1pDXz2zJxkwADCPe4bH6M",
    "basePrivateKey": "dywghKpv3lHmFRcXr4Nlikp93ratBUkvd0NuIRBuFYk="
  },
  "retmsg": "成功",
  "retcode": 0
}
```
```json
{
  "data": {
    "name": "weiqiang",
    "basePublicKey": "AibMwza1JZvaN4f2SN+ZBDtoQvcLYuVAj+QjFzPck7sz",
    "id": "weiqiang",
    "baseAccountAddress": "1E8VwbzeUGxbVqLqDzi9FivRZtC2N9VPc9",
    "basePrivateKey": "ebWUGdEy6nInNdk9npRli6eu7b/BK3PMAoGX97ko+So="
  },
  "retmsg": "成功",
  "retcode": 0
}

```
## 查询当前资产
```json
{
  "assetAccount": "1LVnPKfq1udbx9ZWVWq6nMRP7GU4x1PZcA",
  "pageLimit": 10,
  "pageNo": 1,
  "state": [
    0
  ],
  "configDto":{"createUserPrivateKey":"w1ecBrij8PmWy55CW3F7ksgyYZHXkAC3WCaj269KXjI=","createUserPublicKey":"AjSjvrUOvSpMK5ARrebyXqSFsy0\/rDHz7IH2xSDlTmgQ","chainId":"chBiMAet6e3sRmeuws","ledgerId":"111","mchId":"gbHHj3f4gv36p5IQJw","nodeId":"nd8XbJ8hRtUC5GWP7j","host":"http:\/\/134.175.220.183:15910"}
}

```
```json
{
  "data": [
    {
      "amount": 10000000000000,
      "assetAccount": "1LVnPKfq1udbx9ZWVWq6nMRP7GU4x1PZcA",
      "assetId": "26aBCThrbmerwydnbKirwEU9RD63sQzhasdDrSZfg4Udsnc",
      "state": 0,
      "content": {
        "wsy": 201905271112
      },
      "assetType": 1
    },
    {
      "amount": 9999999999990,
      "assetAccount": "1LVnPKfq1udbx9ZWVWq6nMRP7GU4x1PZcA",
      "assetId": "27tS9eSPhp1DfYYTBEv12Ct27VqJ7od4qsaTjDQM4GaAWUX",
      "state": 0,
      "content": {
        "wsy": 201905271112
      },
      "assetType": 1
    }
  ],
  "retmsg": "成功",
  "retcode": 0
}
```

## 发行
```jshelllanguage
curl -X POST --header 'Content-Type: application/json' --header 'Accept: application/json' -d '{
  "configDto":{"createUserPrivateKey":"w1ecBrij8PmWy55CW3F7ksgyYZHXkAC3WCaj269KXjI=","createUserPublicKey":"AjSjvrUOvSpMK5ARrebyXqSFsy0\/rDHz7IH2xSDlTmgQ","chainId":"chBiMAet6e3sRmeuws","ledgerId":"111","mchId":"gbHHj3f4gv36p5IQJw","nodeId":"nd8XbJ8hRtUC5GWP7j","host":"http:\/\/134.175.220.183:15910"},
  "sourceId": "201905271112",
  "content": "{wsy:201905271112}",
  "amount": "10000000000000",
  "createUserAccountAddress": "1LVnPKfq1udbx9ZWVWq6nMRP7GU4x1PZcA",
  "unit": "wsy",
  "userId": "davidway123",
  "userPublicKey": "AjWDQprCqYpdICB+XFjTCvtPjTRj7cSqas19YemamUq1",
  "userPrivateKey": "olIU4uTpFE18MC3G4WLSSsWacwmhVZen4GnGoDDg3Ik="
}' 'http://localhost:8080/new-blockchain/asset/issue'
```
##返回
```json
{
  "data": {
    "assetId": "26aKkzK97HAbAJrT9Mp96MwZrx7tG79mYvXFP7i5jEh3yyS",
    "transHash": "a319989069deb75fc609ad0f555bebf6dc2dd64abc7ae68f3b14dcd1cd9f4219",
    "transactionId": "201905270007036899"
  },
  "retmsg": "成功",
  "retcode": 0
}
```

## 转账
```jshelllanguage
curl -X POST --header 'Content-Type: application/json' --header 'Accept: application/json' -d '{
  "configDto":{"createUserPrivateKey":"w1ecBrij8PmWy55CW3F7ksgyYZHXkAC3WCaj269KXjI=","createUserPublicKey":"AjSjvrUOvSpMK5ARrebyXqSFsy0\/rDHz7IH2xSDlTmgQ","chainId":"chBiMAet6e3sRmeuws","ledgerId":"111","mchId":"gbHHj3f4gv36p5IQJw","nodeId":"nd8XbJ8hRtUC5GWP7j","host":"http:\/\/134.175.220.183:15910"},
  "amount": "1",
  "srcAccount": "1LVnPKfq1udbx9ZWVWq6nMRP7GU4x1PZcA",
  "dstAccount": "17c7P7U6qZn2PZMDLncp7SovxJCDVTzbE4",
  "srcAsset": "26aKkzK97HAbAJrT9Mp96MwZrx7tG79mYvXFP7i5jEh3yyS",
  "feeAccount": "12sRxG9Az8URR1pDXz2zJxkwADCPe4bH6M",
  "feeAmount": "1",
  "userPrivateKey": "olIU4uTpFE18MC3G4WLSSsWacwmhVZen4GnGoDDg3Ik=",
  "dstAccountPublicKey": "AljX49hJ6Z+ZiGAazv+X0eIPfUVQqGLzfElzKn+xIaym",
  "srcAccountPublicKey": "AjWDQprCqYpdICB+XFjTCvtPjTRj7cSqas19YemamUq1",
  "feeAccountPublicKey": "A+VEN8ErratbaBO9qrBmqeSIxGH7lb3L1xcUFZ/WkO9I",
  "feeAccountUid": "shaxin",
  "dstAccountUid": "wenzhi",
  "srcAccountUid": "davidway123"
}' 'http://localhost:8080/new-blockchain/asset/transfer'
```

```json
{
  "configDto":{"createUserPrivateKey":"w1ecBrij8PmWy55CW3F7ksgyYZHXkAC3WCaj269KXjI=","createUserPublicKey":"AjSjvrUOvSpMK5ARrebyXqSFsy0\/rDHz7IH2xSDlTmgQ","chainId":"chBiMAet6e3sRmeuws","ledgerId":"111","mchId":"gbHHj3f4gv36p5IQJw","nodeId":"nd8XbJ8hRtUC5GWP7j","host":"http:\/\/134.175.220.183:15910"},
  "amount": "1",
  "srcAccount": "1LVnPKfq1udbx9ZWVWq6nMRP7GU4x1PZcA",
  "dstAccount": "17c7P7U6qZn2PZMDLncp7SovxJCDVTzbE4",
  "srcAsset": "26aKkzK97HAbAJrT9Mp96MwZrx7tG79mYvXFP7i5jEh3yyS",
  "feeAccount": "12sRxG9Az8URR1pDXz2zJxkwADCPe4bH6M",
  "feeAmount": "1",
  "userPrivateKey": "olIU4uTpFE18MC3G4WLSSsWacwmhVZen4GnGoDDg3Ik=",
  "dstAccountPublicKey": "AljX49hJ6Z+ZiGAazv+X0eIPfUVQqGLzfElzKn+xIaym",
  "srcAccountPublicKey": "AjWDQprCqYpdICB+XFjTCvtPjTRj7cSqas19YemamUq1",
  "feeAccountPublicKey": "A+VEN8ErratbaBO9qrBmqeSIxGH7lb3L1xcUFZ/WkO9I",
  "feeAccountUid": "shaxin",
  "dstAccountUid": "wenzhi",
  "srcAccountUid": "davidway123"
}
```
返回
```json
{
  "data": {
    "dstAssetAmount": "1",
    "transBHeight": 4347,
    "feeAssetId": "27tCLzA26LJLRJJ96yda7zjS43x56RKNfPnguAnga6wb9Lk",
    "srcAssetId": "29CF85362msaW52ADSZj3ScxZPU3XA45wTzVwN32PRRnzMN",
    "transHash": "0bada43fea26f66b95a223f4027b4ffa24b5e9049980bed3973275722c03ab29",
    "srcAmount": "9999999999998",
    "dstAssetId": "26a9ZuGx9tj6LXa7zWhRCYquYiS6fgafPKasryYLknTPJL8",
    "transBTimestamp": 1558927722,
    "feeAssetAmount": "1",
    "transactionId": "201905270007036941"
  },
  "retmsg": "成功",
  "retcode": 0
}
```
## 转账申请
```json
{
  "configDto":{"createUserPrivateKey":"w1ecBrij8PmWy55CW3F7ksgyYZHXkAC3WCaj269KXjI=","createUserPublicKey":"AjSjvrUOvSpMK5ARrebyXqSFsy0\/rDHz7IH2xSDlTmgQ","chainId":"chBiMAet6e3sRmeuws","ledgerId":"111","mchId":"gbHHj3f4gv36p5IQJw","nodeId":"nd8XbJ8hRtUC5GWP7j","host":"http:\/\/134.175.220.183:15910"},
  "amount": "1",
  "srcAccount": "1LVnPKfq1udbx9ZWVWq6nMRP7GU4x1PZcA",
  "dstAccount": "17c7P7U6qZn2PZMDLncp7SovxJCDVTzbE4",
  "srcAsset": "29CF85362msaW52ADSZj3ScxZPU3XA45wTzVwN32PRRnzMN",
  "feeAccount": "12sRxG9Az8URR1pDXz2zJxkwADCPe4bH6M",
  "feeAmount": "1",
  "userPrivateKey": "olIU4uTpFE18MC3G4WLSSsWacwmhVZen4GnGoDDg3Ik=",
  "dstAccountPublicKey": "AljX49hJ6Z+ZiGAazv+X0eIPfUVQqGLzfElzKn+xIaym",
  "srcAccountPublicKey": "AjWDQprCqYpdICB+XFjTCvtPjTRj7cSqas19YemamUq1",
  "feeAccountPublicKey": "A+VEN8ErratbaBO9qrBmqeSIxGH7lb3L1xcUFZ/WkO9I",
  "feeAccountUid": "shaxin",
  "dstAccountUid": "wenzhi",
  "srcAccountUid": "davidway123"
}
```
返回
```json
{
  "data": {
    "configDto": "",
    "signList": "[{\"id\":1,\"sign_str\":\"5f3e01ac1faa1b9bb8db94da46dad084cf2498d3417fd6e01852715bf9dead58\",\"account\":\"1LVnPKfq1udbx9ZWVWq6nMRP7GU4x1PZcA\"}]",
    "userPrivateKey": "olIU4uTpFE18MC3G4WLSSsWacwmhVZen4GnGoDDg3Ik=",
    "srcAssetId": "29CM9nPkcL5kBZ8BwuPq7Akakkyrvu3KkLDvG3roDkrKFvF",
    "dstAssetId": "26aFbcdcjSwG21g9iyXXGGyXk5wv5RZuCBpJBfN7b7suZu1",
    "transactionId": "201905270007037024"
  },
  "retmsg": "成功",
  "retcode": 0
}
```

## 转账仅提交
```json
{
   "configDto":{"createUserPrivateKey":"w1ecBrij8PmWy55CW3F7ksgyYZHXkAC3WCaj269KXjI=","createUserPublicKey":"AjSjvrUOvSpMK5ARrebyXqSFsy0\/rDHz7IH2xSDlTmgQ","chainId":"chBiMAet6e3sRmeuws","ledgerId":"111","mchId":"gbHHj3f4gv36p5IQJw","nodeId":"nd8XbJ8hRtUC5GWP7j","host":"http:\/\/134.175.220.183:15910"},
    "signList": "[{\"id\":1,\"sign_str\":\"5f3e01ac1faa1b9bb8db94da46dad084cf2498d3417fd6e01852715bf9dead58\",\"account\":\"1LVnPKfq1udbx9ZWVWq6nMRP7GU4x1PZcA\"}]",
    "userPrivateKey": "olIU4uTpFE18MC3G4WLSSsWacwmhVZen4GnGoDDg3Ik=",
    "srcAssetId": "29CM9nPkcL5kBZ8BwuPq7Akakkyrvu3KkLDvG3roDkrKFvF",
    "dstAssetId": "26aFbcdcjSwG21g9iyXXGGyXk5wv5RZuCBpJBfN7b7suZu1",
    "transactionId": "201905270007037024"
  }

```

##返回
```json
{
  "data": {
    "dstAssetAmount": "1",
    "transBHeight": 4348,
    "feeAssetId": "27tJNhWgftWW6nQAqSTgBis4FRTtWAJcUG27DrcTQSN7Qud",
    "srcAssetId": "29CM9nPkcL5kBZ8BwuPq7Akakkyrvu3KkLDvG3roDkrKFvF",
    "transHash": "65441b0dd85f0132275042eda13ce6ed8003538b73af35dff90a22da2f28f3c8",
    "srcAmount": "9999999999996",
    "dstAssetId": "26aFbcdcjSwG21g9iyXXGGyXk5wv5RZuCBpJBfN7b7suZu1",
    "transBTimestamp": 1558928125,
    "feeAssetAmount": "1",
    "transactionId": "201905270007037024"
  },
  "retmsg": "成功",
  "retcode": 0
}
```

## 兑付
```json
{
  "configDto":{"createUserPrivateKey":"w1ecBrij8PmWy55CW3F7ksgyYZHXkAC3WCaj269KXjI=","createUserPublicKey":"AjSjvrUOvSpMK5ARrebyXqSFsy0\/rDHz7IH2xSDlTmgQ","chainId":"chBiMAet6e3sRmeuws","ledgerId":"111","mchId":"gbHHj3f4gv36p5IQJw","nodeId":"nd8XbJ8hRtUC5GWP7j","host":"http:\/\/134.175.220.183:15910"},
  "amount": "1",
  "ownerAccount": "1LVnPKfq1udbx9ZWVWq6nMRP7GU4x1PZcA",
  "userPrivateKey": "olIU4uTpFE18MC3G4WLSSsWacwmhVZen4GnGoDDg3Ik=",
  "srcAsset": "29CLacemoqqZdGtxCXMuPsRqKybGXWc4hU768vwx71hi7w4",
  "ownerPublickey": "AjWDQprCqYpdICB+XFjTCvtPjTRj7cSqas19YemamUq1",
  "ownerId": "davidway123"
}
```
##返回
```json
{
  "data": {
    "srcAssetId": "27tQB1aZF5hADshLEJ3VzsSNATKAQ5BXsWMgb9ymcCuHzBC",
    "transHash": "bb6baf888869d1870fd333c5dfa5f38a9220d2639c4a8b32da65e517111157b3",
    "transactionId": "201905270007037318"
  },
  "retmsg": "成功",
  "retcode": 0
}
```

## 兑付申请
```json
{
  "configDto":{"createUserPrivateKey":"w1ecBrij8PmWy55CW3F7ksgyYZHXkAC3WCaj269KXjI=","createUserPublicKey":"AjSjvrUOvSpMK5ARrebyXqSFsy0\/rDHz7IH2xSDlTmgQ","chainId":"chBiMAet6e3sRmeuws","ledgerId":"111","mchId":"gbHHj3f4gv36p5IQJw","nodeId":"nd8XbJ8hRtUC5GWP7j","host":"http:\/\/134.175.220.183:15910"},
  "amount": "1",
  "ownerAccount": "1LVnPKfq1udbx9ZWVWq6nMRP7GU4x1PZcA",
  "userPrivateKey": "olIU4uTpFE18MC3G4WLSSsWacwmhVZen4GnGoDDg3Ik=",
  "srcAsset": "27tQB1aZF5hADshLEJ3VzsSNATKAQ5BXsWMgb9ymcCuHzBC",
  "ownerPublickey": "AjWDQprCqYpdICB+XFjTCvtPjTRj7cSqas19YemamUq1",
  "ownerId": "davidway123"
}
```

##兑付提交
```json
{
    "configDto":{"createUserPrivateKey":"w1ecBrij8PmWy55CW3F7ksgyYZHXkAC3WCaj269KXjI=","createUserPublicKey":"AjSjvrUOvSpMK5ARrebyXqSFsy0\/rDHz7IH2xSDlTmgQ","chainId":"chBiMAet6e3sRmeuws","ledgerId":"111","mchId":"gbHHj3f4gv36p5IQJw","nodeId":"nd8XbJ8hRtUC5GWP7j","host":"http:\/\/134.175.220.183:15910"},
    "signList": "[{\"id\":1,\"sign_str\":\"ad457fe6e817d740a86499b903b6665a92ef0f75407f23fd6f309828e69364eb\",\"account\":\"1LVnPKfq1udbx9ZWVWq6nMRP7GU4x1PZcA\"}]",
    "userPrivateKey": "olIU4uTpFE18MC3G4WLSSsWacwmhVZen4GnGoDDg3Ik=",
    "transactionId": "201905270007037321"
  }
```
##兑付提交后的报文
```json
{
  "data": {
    "srcAssetId": "27tS9eSPhp1DfYYTBEv12Ct27VqJ7od4qsaTjDQM4GaAWUX",
    "transHash": "d8c99324c18949a64477f3844779c1ee16c28e3f866d7dde7f19dbe26a2d7de8",
    "transactionId": "201905270007037321"
  },
  "retmsg": "成功",
  "retcode": 0
}
```