package com.pig.design.config;

import com.pig.design.entity.Article;
import com.pig.design.enums.ArticleState;
import com.pig.design.enums.StateEvent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.statemachine.StateMachineContext;
import org.springframework.statemachine.StateMachinePersist;
import org.springframework.statemachine.config.EnableStateMachine;
import org.springframework.statemachine.config.StateMachineConfigurerAdapter;
import org.springframework.statemachine.config.builders.StateMachineConfigurer;
import org.springframework.statemachine.config.builders.StateMachineStateConfigurer;
import org.springframework.statemachine.config.builders.StateMachineTransitionConfigurer;
import org.springframework.statemachine.persist.DefaultStateMachinePersister;
import org.springframework.statemachine.persist.StateMachinePersister;
import org.springframework.statemachine.support.DefaultStateMachineContext;

import java.util.EnumSet;

@Configuration
@EnableStateMachine(name = "stateMachine")
public class StateMachineConfig extends StateMachineConfigurerAdapter<ArticleState, StateEvent> {
    /**
     * 配置状态
     */

    @Override
    public void configure(StateMachineStateConfigurer<ArticleState, StateEvent> states) throws Exception {
        states.withStates()
                .initial(ArticleState.TEMP)
                .states(EnumSet.allOf(ArticleState.class));
    }

    /**
     * 配置状态转换与事件的关系
     */

    @Override
    public void configure(StateMachineTransitionConfigurer<ArticleState, StateEvent> transitions) throws Exception{
        transitions
                //草稿到审批
                .withExternal()
                .source(ArticleState.TEMP).target(ArticleState.PROCESSING)
                .event(StateEvent.APPLY)
                .and()

                // 审批中到已通过
                .withExternal()
                .source(ArticleState.PROCESSING).target(ArticleState.APPROVED)
                .event(StateEvent.AGREE)
                .and()

                // 从审批中变成已打回
                .withExternal()
                .source(ArticleState.PROCESSING).target(ArticleState.REJECTED)
                .event(StateEvent.DISAGREE)
                .and()

                // 从草稿变为草稿
                .withExternal()
                .source(ArticleState.TEMP).target(ArticleState.TEMP)
                .event(StateEvent.EDIT)
                .and()

                // 从已打回变为草稿
                .withExternal()
                .source(ArticleState.REJECTED).target(ArticleState.TEMP)
                .event(StateEvent.EDIT)
                .and()

                // 从已通过变为草稿
                .withExternal()
                .source(ArticleState.APPROVED).target(ArticleState.TEMP)
                .event(StateEvent.EDIT);
    }

    @Bean
    public StateMachinePersister<ArticleState, StateEvent, Article> persister() {
        return new DefaultStateMachinePersister<>(new StateMachinePersist<ArticleState, StateEvent, Article>() {
            @Override
            public void write(StateMachineContext<ArticleState, StateEvent> context, Article article) throws Exception {
                // 此处并没有进行持久化操作
                // 这里持久化一般是将StateMachineContext状态上下文保存到内存或者redis中, 下次读取在用, 但是实际上状态一般随着实体类保存在数据库表中, 并且状态可能会因为其他原因被手动修改, 所以实际开发中一般不进行持久化操作. 都是实时获取状态
            }

            @Override
            public StateMachineContext<ArticleState, StateEvent> read(Article article) throws Exception {
                // 此处直接获取状态，其实并没有进行持久化读取操作
                return new DefaultStateMachineContext<>(article.getState(), null, null, null);
            }
        });
    }
}
