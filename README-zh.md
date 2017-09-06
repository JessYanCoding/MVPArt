# MVPArt
[ ![Bintray](https://img.shields.io/badge/bintray-v2.2.0-brightgreen.svg) ](https://bintray.com/jessyancoding/maven/MVPArt/2.2.0/link)
[ ![Build Status](https://travis-ci.org/JessYanCoding/MVPArt.svg?branch=complete) ](https://travis-ci.org/JessYanCoding/MVPArt)
[ ![API](https://img.shields.io/badge/API-15%2B-blue.svg?style=flat-square) ](https://developer.android.com/about/versions/android-4.0.3.html)
[ ![License](http://img.shields.io/badge/License-Apache%202.0-blue.svg?style=flat-square) ](http://www.apache.org/licenses/LICENSE-2.0)
[ ![QQGroup](https://img.shields.io/badge/QQ群-301733278-ff69b4.svg) ](https://shang.qq.com/wpa/qunwpa?idkey=1a5dc5e9b2e40a780522f46877ba243eeb64405d42398643d544d3eec6624917)

## A New Android MVP Architecture
**此框架旨在解决传统 **MVP** 类和接口太多,并且 **Presenter** 和 **View** 通过接口通信过于繁琐,重用 **Presenter** 代价太大等问题**


## Architectural
<img src="https://github.com/JessYanCoding/MVPArt/raw/master/image/Architecture.png" width="80%" height="80%">

## Introduction
> [**Master** 分支](https://github.com/JessYanCoding/MVPArt/tree/master)
>> **Master** 分支是一个不含网络层的简易框架,主要通过4个 **Demo** 介绍本框架的思想,特性以及使用方法,小巧灵活适合已经有一整套现有框架但又需要重构为 **MVP** 结构的项目,但不管你使用哪个分支都强烈建议你先看看 **Master** 分支的 **Demo**    
  
> [**Complete** 分支](https://github.com/JessYanCoding/MVPArt/tree/complete)
>> **Complete** 分支是一个含有网络层的完整框架,将 **Retrofit** 作为网络层并使用 **Dagger2** 管理所有对象,成熟强大适合新建的项目

## Notice
* 使用之前,请参阅 [传统MVP用在项目中是真的方便还是累赘?](https://gold.xitu.io/post/58b25e588d6d810057ed3659)

* [使用 Template 自动生成 MVP 相关类](https://github.com/JessYanCoding/MVPArmsTemplate)
* 此框架是一个轻量级框架,比较适合中小型项目,大型项目请使用 [MVPArms](https://github.com/JessYanCoding/MVPArms)
* 传统MVP每个页面对应一个 **Presenter** ,而大多数 **Presenter** 只有一两个方法,这样导致存在大量代码寥寥无几的 **Presenter** ,此框架旨在解决复用 **Presenter** 时需要实现过多多余接口方法的问题,鼓励开发者将相近的逻辑写在一个 **Presenter** 中,不断重用 **Presenter** ,减少大量类文件
* 当然很多不同的逻辑都写在一个 **Presenter** 中,虽然可以少写很多类,但是后面的扩展性肯定不好,所以这个粒度需要自己控制,但是对于外包项目简直是福音


## Download
``` gradle
 compile 'me.jessyan:art:2.2.0'  //rxjava2

 compile 'me.jessyan:art:1.4.3' //rxjava1(不再维护)
```

## Donate
![alipay](https://raw.githubusercontent.com/JessYanCoding/MVPArms/master/image/pay_alipay.jpg) ![](https://raw.githubusercontent.com/JessYanCoding/MVPArms/master/image/pay_wxpay.jpg)

## About Me
* **Email**: <jess.yan.effort@gmail.com>  
* **Home**: <http://jessyan.me>
* **掘金**: <https://gold.xitu.io/user/57a9dbd9165abd0061714613>
* **简书**: <http://www.jianshu.com/u/1d0c0bc634db>

## License
```
 Copyright 2017, jessyan

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
```
