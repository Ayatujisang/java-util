<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN" 
"http://mybatis.org/dtd/mybatis-3-mapper.dtd">

<mapper namespace="${packageName}.${ModelName}Mapper">
	<sql id="Columns">
        <#list fieds as f><#if f_index gt 0 >,</#if>${f.column}</#list>
    </sql>

	<select id="getById" parameterType="int" resultType="${ModelName}">
		select
		<include refid="Columns"/>
		 from ${tableName} where id = ${SPTJ}{id}
	</select>
	
	<insert id="insert" parameterType="${ModelName}" useGeneratedKeys="true" keyProperty="id">
		insert into
		${tableName}(<#list fieds as f><#if f_index gt 0 ><#if f_index gt 1 >,</#if> ${f.column}</#if></#list>)
		values(<#list fieds as f><#if f_index gt 0 ><#if f_index gt 1 >,</#if>${SPTJ}{${f.field}}</#if></#list>)
	</insert>
	
	<update id="updateById" parameterType="${ModelName}">
		update ${tableName} set
		<#list fieds as f><#if f_index gt 0 ><#if f_index gt 1 >,</#if>${f.column}=${SPTJ}{${f.field}}</#if></#list>
		where id=${SPTJ}{id}
	</update>
	
	<delete id="deleteById" parameterType="int">
		delete from ${tableName} where
		id=${SPTJ}{id}
	</delete>
	
</mapper>
