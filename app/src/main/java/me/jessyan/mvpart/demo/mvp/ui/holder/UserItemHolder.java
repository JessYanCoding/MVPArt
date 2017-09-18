/**
  * Copyright 2017 JessYan
  *
  * Licensed under the Apache License, Version 2.0 (the "License");
  * you may not use this file except in compliance with the License.
  * You may obtain a copy of the License at
  *
  *      http://www.apache.org/licenses/LICENSE-2.0
  *
  * Unless required by applicable law or agreed to in writing, software
  * distributed under the License is distributed on an "AS IS" BASIS,
  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  * See the License for the specific language governing permissions and
  * limitations under the License.
  */
package me.jessyan.mvpart.demo.mvp.ui.holder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import me.jessyan.art.base.BaseHolder;
import me.jessyan.art.di.component.AppComponent;
import me.jessyan.art.http.imageloader.ImageLoader;
import me.jessyan.art.http.imageloader.glide.ImageConfigImpl;
import me.jessyan.art.utils.ArtUtils;
import me.jessyan.mvpart.demo.R;
import me.jessyan.mvpart.demo.mvp.model.entity.User;

/**
 * ================================================
 * 展示 {@link BaseHolder} 的用法
 * <p>
 * Created by JessYan on 9/4/16 12:56
 * Contact with <mailto:jess.yan.effort@gmail.com>
 * Follow me on <https://github.com/JessYanCoding>
 * ================================================
 */
public class UserItemHolder extends BaseHolder<User> {

    @BindView(R.id.iv_avatar)
    ImageView mAvater;
    @BindView(R.id.tv_name)
    TextView mName;
    private AppComponent mAppComponent;
    private ImageLoader mImageLoader;//用于加载图片的管理类,默认使用glide,使用策略模式,可替换框架

    public UserItemHolder(View itemView) {
        super(itemView);
        //可以在任何可以拿到Application的地方,拿到AppComponent,从而得到用Dagger管理的单例对象
        mAppComponent = ArtUtils.obtainAppComponentFromContext(itemView.getContext());
        mImageLoader = mAppComponent.imageLoader();
    }

    @Override
    public void setData(User data, int position) {

        mName.setText(data.getLogin());

        mImageLoader.loadImage(mAppComponent.appManager().getTopActivity() == null
                        ? mAppComponent.application() : mAppComponent.appManager().getTopActivity(),
                ImageConfigImpl
                        .builder()
                        .url(data.getAvatarUrl())
                        .imageView(mAvater)
                        .build());
    }


    @Override
    protected void onRelease() {
        mImageLoader.clear(mAppComponent.application(), ImageConfigImpl.builder()
                .imageViews(mAvater)
                .build());
    }
}
