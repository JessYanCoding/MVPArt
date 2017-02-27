# MVPArt
[![Chrome Web Store](https://img.shields.io/chrome-web-store/stars/nimelepbpejjlbmoobocpfnjhihnpked.svg)]()
[![License](http://img.shields.io/badge/License-Apache%202.0-blue.svg?style=flat-square)](http://www.apache.org/licenses/LICENSE-2.0)
[![API](https://img.shields.io/badge/API-15%2B-blue.svg?style=flat-square)](https://developer.android.com/about/versions/android-4.0.3.html)

## A New Android MVP Architecture

qq群:301733278 有什么问题可以直接问我

[传统MVP用在项目中是真的方便还是累赘?](https://gold.xitu.io/post/58b25e588d6d810057ed3659)

## Introduction
* 此框架是一个轻量级框架,比较适合中小型项目,大型项目请使用[MVPArms](https://github.com/JessYanCoding/MVPArms)
* 此框架指在解决传统**MVP**类和接口太多,并且**Presenter**和**View**通过接口通信过于繁琐,重用**Presenter**代价太大等问题
* 传统MVP每个页面对应一个presenter,而大多数presenter只有一两个方法,这样导致存在大量代码寥寥无几的**Presenter**,此框架指在解决复用**Presenter**时需要实现过多多余接口方法的问题,鼓励开发者将相近的逻辑写在一个**Presenter**中,不断重用**Presenter**,减少大量类文件
* 当然很多不同的逻辑都写在一个Presenter中,虽然可以少写很多类,但是后面的扩展性肯定好,所以这个粒度需要自己控制,但是对于外包项目简直是福音
## Architectural
<img src="https://github.com/JessYanCoding/MVPArt/raw/master/image/Architecture.png" width="80%" height="80%">

## TODO
* 后面会开个分支提交比较完整的包含网络层的框架和文档,像[MVPArms](https://github.com/JessYanCoding/MVPArms)一样

## About Me
* **Email**: jess.yan.effort@gmail.com
* **Home**: http://jessyan.me

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
