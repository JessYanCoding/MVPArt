package me.jessyan.mvpart.demo.mvp.ui.adapter;

import android.view.View;

import me.jessyan.art.base.BaseHolder;
import me.jessyan.art.base.DefaultAdapter;

import java.util.List;

import me.jessyan.mvpart.demo.R;
import me.jessyan.mvpart.demo.mvp.model.entity.User;
import me.jessyan.mvpart.demo.mvp.ui.holder.UserItemHolder;

/**
 * Created by jess on 9/4/16 12:57
 * Contact with jess.yan.effort@gmail.com
 */
public class UserAdapter extends DefaultAdapter<User> {
    public UserAdapter(List<User> infos) {
        super(infos);
    }

    @Override
    public BaseHolder<User> getHolder(View v, int viewType) {
        return new UserItemHolder(v);
    }

    @Override
    public int getLayoutId(int viewType) {
        return R.layout.recycle_list;
    }
}
