## ViewPagerY
> 自定义ViewPager,支持展示图片,展示布局
##### 依赖
```groovy
implementation 'com.yey.viewpagery:library_vpy:0.0.4'
```
#### XML
```xml
<com.yey.viewpagery.ViewPagerY
    android:id="@+id/vpy"
    android:layout_width="match_parent"
    android:layout_height="match_parent" />
```
#### 代码
```java
mVpy = (ViewPagerY) findViewById(R.id.vpy);
resTypes = new ArrayList<>();
resTypes.add(new ResType<String>("https://wx1.sinaimg.cn/mw690/006LHkGply1gaftm9dxcpj30tk0tzwv6.jpg", ResType.Type.URL));
resTypes.add(new ResType<Integer>(R.mipmap.image1, ResType.Type.IAMG));
resTypes.add(new ResType<String>("https://wx1.sinaimg.cn/mw690/006LHkGply1gaftm45nvhj334022te83.jpg", ResType.Type.URL));
resTypes.add(new ResType<Integer>(R.mipmap.image2, ResType.Type.IAMG));
resTypes.add(new ResType<String>("https://wx3.sinaimg.cn/mw690/006LHkGply1gebsmxnod3j30v80uxh18.jpg", ResType.Type.URL));
resTypes.add(new ResType<Integer>(R.layout.layout_test_vpy, ResType.Type.LAYOUT));
// 设置资源集合
mVpy.setRes(resTypes);
// 设置界面切换时间
mVpy.setScrollDuration(200);
```
#### 效果

![效果图](img/自定义ViewPager.gif)

