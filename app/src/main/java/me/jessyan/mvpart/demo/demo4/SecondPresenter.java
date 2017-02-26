package me.jessyan.mvpart.demo.demo4;

import me.jessyan.art.mvp.BasePresenter;
import me.jessyan.art.mvp.Message;
import rx.Observable;
import rx.functions.Action1;
import rx.functions.Func1;

/**
 * Created by jess on 26/02/2017 09:42
 * Contact with jess.yan.effort@gmail.com
 */

public class SecondPresenter extends BasePresenter {

    /**
     * 这里为了展示一个页面使用多个presenter时,what冲突的情况
     * 故意使用MainPresenter中使用过的what数字
     * @param message
     */
    public void request3(Message message){
        addSubscrebe(Observable.just(message)
                .filter(new Func1<Message, Boolean>() {
                    @Override
                    public Boolean call(Message message) {
                        return message != null;
                    }
                }).subscribe(new Action1<Message>() {
                    @Override
                    public void call(Message message) {
                        message.what=2;
                        message.HandleMessageToTarget();
                    }
                }));
    }
}
