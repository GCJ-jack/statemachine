package com.pig.design.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.pig.design.entity.Article;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ArticleMapper extends BaseMapper<Article> {
}
