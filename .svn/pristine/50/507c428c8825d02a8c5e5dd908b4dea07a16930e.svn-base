package com.blockchain.springjson;

/*
<value>WriteMapNullValue</value> <!--输出Map中的null值 -->
<value>WriteNullListAsEmpty</value> <!-- 引用为null的列表/数组/集合输出为[] -->
<value>WriteNullStringAsEmpty</value> <!-- 引用为null的String输出为“” -->
<value>WriteNullNumberAsZero</value> <!-- 引用为null的数字类型输出为0 -->
<value>WriteNullBooleanAsFalse</value> <!-- 引用为null的Boolean输出为false -->*/

public enum SerializerFeature {
   WriteNullListAsEmpty,
   WriteNullStringAsEmpty,
   WriteNullNumberAsZero,
   WriteNullBooleanAsFalse;
   
   public final int mask;
   
   SerializerFeature() {
       mask = (1 << ordinal());
   }
}
