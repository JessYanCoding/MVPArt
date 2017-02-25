package me.jessyan.mvpart.demo;

import me.jessyan.art.mvp.BasePresenter;
import me.jessyan.art.mvp.Message;
import rx.Observable;
import rx.functions.Action1;
import rx.functions.Func1;

/**
 * Created by jess on 24/02/2017 17:55
 * Contact with jess.yan.effort@gmail.com
 */

public class MainPresenter extends BasePresenter {


    public void compute(Message message) {
        addSubscrebe(Observable.just(message)
                .filter(new Func1<Message, Boolean>() {
                    @Override
                    public Boolean call(Message message) {
                        return message != null;
                    }
                })
                .subscribe(new Action1<Message>() {
                    @Override
                    public void call(Message message) {
                        //如果需要对view操作多次,那前几次都调用HandleMessageToTargetUnrecycle（）
                        //只有最后一次,才调用HandleMessageToTarget()
                        //因为HandleMessageToTarget()处理完操作后,会把message中的所有字段置为空,包括view的引用
                        //并且不管你要对view操作几次,必须在最后调用HandleMessageToTarget(),否则要内存泄漏
                        message.what = 0;
                        message.arg1 = R.color.colorPrimaryDark;
                        message.HandleMessageToTargetUnrecycle();
                        message.what = 1;
                        message.str = "hello jess";
                        message.HandleMessageToTarget();
                    }
                }));
    }
}
