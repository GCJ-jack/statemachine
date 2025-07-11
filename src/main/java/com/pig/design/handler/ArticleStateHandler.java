package com.pig.design.handler;

import cn.hutool.extra.spring.SpringUtil;
import com.pig.design.entity.Article;
import com.pig.design.enums.ArticleState;
import com.pig.design.enums.StateEvent;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.persist.StateMachinePersister;

@Slf4j
public class ArticleStateHandler {
    // 发送事件的通用方法
    @SneakyThrows
    public synchronized static void handleArticleState(Article article, StateEvent event) {
        //获取状态机
        StateMachine<ArticleState, StateEvent> stateMachine = SpringUtil.getBean("stateMachine");

        //获取状态机持久化
        StateMachinePersister<ArticleState, StateEvent, Article> persister = SpringUtil.getBean("persister");

        try{
            stateMachine.start();
            persister.restore(stateMachine, article);
            Message<StateEvent> message = MessageBuilder.withPayload(event)
                    .setHeader("article", article)
                    .build();
            boolean res = stateMachine.sendEvent(message);
            persister.persist(stateMachine, article);

            if (!res) {
                log.error("从{}状态, 触发{}事件失败", article.getState().getDesc(), event);
                throw new Exception("状态机状态转换失败");
            }
        } finally {
            stateMachine.stop();
        }
    }
}
