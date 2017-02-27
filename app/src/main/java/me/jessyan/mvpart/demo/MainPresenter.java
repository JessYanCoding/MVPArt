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

    /**
     * 如果需要依赖一些外部的数据,可以在调用时通过将数据封装到Message中,也可以直接通过方法参数传入
     * 通过将数据封装到Message中的方式好处是,即使需要的数据类型发生改变,也不用更改方法,所以也不会影响到上层调用
     * @param message
     */
    public void request(Message message) {
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
                        //如果需要对view操作多次(把一个复杂的操作细分成多个,便于维护)
                        //那前几次都调用HandleMessageToTargetUnrecycle()
                        //只有最后一次,才调用HandleMessageToTarget()
                        //因为HandleMessageToTarget()处理完操作后,会把message中的所有字段置为空,包括view的引用
                        //并且不管你要对view操作几次,必须在最后调用HandleMessageToTarget(),否则可能发生内存泄漏
                        //message.getTarget().showMessage();使用message.getTarget()可以拿到BaserView的引用从而调用一些默认的方法
                        //调用默认方法后不需要调用HandleMessageToTarget(),但是如果后面对view没有其他操作了请调用message.recycle()回收消息
                        message.what = 0;
                        message.arg1 = R.color.colorPrimaryDark;
                        message.HandleMessageToTargetUnrecycle();
                        message.what = 1;
                        message.str = "hello " + message.obj;
                        message.HandleMessageToTarget();
                    }
                }));
    }


    /**
     * 整个presenter中每个方法使用到的what字段不能重复,如上面的方法what字段使用了0和1
     * 那后面的方法就必须使用其他数字
     * @param message
     */
    public void request2(Message message) {
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
