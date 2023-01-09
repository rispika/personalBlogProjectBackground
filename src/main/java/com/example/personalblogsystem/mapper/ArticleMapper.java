package com.example.personalblogsystem.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.personalblogsystem.domain.Article;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ArticleMapper extends BaseMapper<Article> {
}
