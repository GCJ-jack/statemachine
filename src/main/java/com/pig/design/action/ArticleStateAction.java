package com.pig.design.action;

import com.pig.design.annotation.StatesOnTransition;
import com.pig.design.entity.Article;
import com.pig.design.enums.ArticleState;
import com.pig.design.service.ArticleService;
import lombok.RequiredArgsConstructor;
import org.springframework.statemachine.annotation.EventHeaders;
import org.springframework.statemachine.annotation.WithStateMachine;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@WithStateMachine( name = "stateMachine")
@RequiredArgsConstructor
public class ArticleStateAction {

    private final ArticleService articleService;

    @StatesOnTransition(source = ArticleState.TEMP, target = ArticleState.PROCESSING)
    public void apply(@EventHeaders Map<String, Object> headers) {
        Article article = (Article) headers.get("article");
        article.setState(ArticleState.PROCESSING);
        articleService.updateById(article);
    }

    @StatesOnTransition
    public void agree(@EventHeaders Map<String, Object> headers){
        Article article = (Article) headers.get("article");
        article.setState(ArticleState.APPROVED);
        articleService.updateById(article);
    }


    @StatesOnTransition(source = ArticleState.PROCESSING, target = ArticleState.REJECTED)
    public void disagree(@EventHeaders Map<String,Object> headers){
        Article article = (Article) headers.get("article");
        article.setState(ArticleState.REJECTED);
        articleService.updateById(article);
    }

    @StatesOnTransition(source = {ArticleState.TEMP, ArticleState.REJECTED, ArticleState.APPROVED}, target = ArticleState.TEMP)
    public void edit(@EventHeaders Map<String, Object> headers) {
        Article article = (Article) headers.get("article");
        article.setState(ArticleState.TEMP);

        boolean res = articleService.updateById(article);
        if (!res) {
            throw new RuntimeException("保存失败");
        }
    }
}
