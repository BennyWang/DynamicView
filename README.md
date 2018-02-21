DynamicView
===========

一个支持动态解析XML布局的库，通过这个库，可以动态渲染类似下面的布局
```xml
<VBox>
    <TitleView title="Hello world" logo="{logo}" />
    <BodyView />
    <FooterView />
</VBox>
```

## 组件说明


### 布局节点

布局的节点并不能直接使用Android Framework中的View或者其他第三方的控件，必须要重新实现节点对应的控件类。
这么做的原因是考虑到性能问题，复杂的布局解析比较耗时，会影响到APP的用户体验。

### 节点说明

```xml
<VBox>
    <TitleView title="Hello world" logo="{logo}" />
    <BodyView />
    <FooterView />
</VBox>
```
节点的名称是我们需要自定义控件的类名，属性是定义的类的setter，例如针对TitleView我们会有如下定义

```java
@DynamicView
public class TitleView extends RelativeLayout implements ViewType.View {
    private ImageView vLogo;
    private TextView vTitle;

    public TitleView(Context context) {
        super(context);
    }

    public void setTitle(String title) {
        vTitle.setText(title);
    }

    public void setLogo(String logo) {
        Glide.with(getContext()).load(logo).into(vLogo);
    }
}
```
DynamicView注解是为了表示这个类是节点控件，编译期会自动生成对应的帮助类

ViewType有三种
* View - 表示此节点为View，不能包含子节点
* GroupView - 表示此节点为容器，可包含子节点，参照LinearLayout、RelativeLayout等容器控件
* AdapterView - 此节点只能有一个子节点，根据数据来决定生成多少子view，参照ListView、RecyclerView

属性值有两种形态
* 字面值 title="Hello World"，这种属性会直接通过setter设置给控件
* 动态属性 logo="{logo}" logo会绑定到一个Map或者是JSONObject的key为logo的值


### 使用方法

```java
// 创建View，第一个参数是Context，第二个是包含xml的字符串
View convertView = DynamicViewEngine.getInstance().createView(MainActivity.this, layoutXml);
// 绑定动态属性，第一个参数是通过上面方法创建的view，第二个值是数据，Map<String, String> 或者 JSONObject
DynamicViewEngine.getInstance().bindView(convertView, data);

```

## Discussion

### QQ Group: 516157585
