package com.blockchain.springjson;

import org.springframework.beans.factory.FactoryBean;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

final public class CustomObjectMapperFactoryBean implements FactoryBean<ObjectMapper> {
	SerializerFeature[] features;

	public CustomObjectMapperFactoryBean(SerializerFeature[] features) {
		this.features = features;
	}

	@Override
	public ObjectMapper getObject() throws Exception {
		ObjectMapper objectMapper = new ObjectMapper();
//		忽略未知属性
		objectMapper.setSerializerFactory(objectMapper.getSerializerFactory().withSerializerModifier(new FastJsonSerializerFeatureCompatibleForJackson(features))).configure(
				DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		return objectMapper;
	}

	@Override
	public Class<?> getObjectType() {
		return ObjectMapper.class;
	}

	@Override
	public boolean isSingleton() {
		return false;
	}
}
