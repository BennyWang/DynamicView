DynamicView
===========

一个支持动态解析XML布局的库，通过这个库，可以动态渲染类似下面的布局
```xml
<RBox sn='000001'>
    <VBox background='#80E0E0E0 20 20 0 0' padding='18 18 18 10' margin='14'>
        <Text text='{title}' fontSize='20' color='black'/>
        <Text text='金额' margin='0 10 0 0'/>
        <RBox>
            <Text name='money' text='{money}' fontSize='28' color='black'/>
            <Text rightOf='@money' alignBaseline='@money' text='元'/>
        </RBox>
        <Grid spanCount='2' dataSource='{items}'>
            <VBox margin='0 10 0 0'><Text text='{name}'/>
                <Text text='{value}' color='black'/>
            </VBox>
        </Grid>
    </VBox>
</RBox>
```

## 组件说明


### 布局节点

布局的节点并不能直接使用Android Framework中的View或者其他第三方的控件，必须要重新实现节点对应的控件类。
这么做的原因是考虑到性能问题，复杂的布局解析比较耗时，会影响到APP的用户体验。

### 节点说明

节点的名称是我们需要自定义控件的类名，属性是定义的类的setter，例如Text会有如下定义

```java
@DynamicView
public class Text extends TextView implements ViewType.View {
    public Text(Context context) {
        super(context);
    }

    public void setText(String text) {
        super.setText(text);
    }

    public void setFontSize(String value) {
        setTextSize(Integer.parseInt(value));
    }

    public void setColor(String value) {
        setTextColor(Color.parseColor(value));
    }

    public void setStyle(String value) {
        switch (value) {
            case "bold":
                setTypeface(getTypeface(), Typeface.BOLD);
                break;
            case "italic":
                setTypeface(getTypeface(), Typeface.ITALIC);
                break;
        }
    }

    public void setAlign(String value) {
        GravityProperty property = GravityProperty.of(getContext(), value);
        setGravity(property.gravity);
    }
}
```
DynamicView注解是为了表示这个类是节点控件，编译期会自动生成对应的帮助类

ViewType有三种
* View - 表示此节点为View，不能包含子节点
* GroupView - 表示此节点为容器，可包含子节点，参照LinearLayout、RelativeLayout等
* AdapterView - 此节点只能有一个子节点，根据数据来决定生成多少子view，参照ListView、RecyclerView等

属性值有两种形态
* 字面值 title="Hello World"，这种属性会直接通过setter设置给控件
* 动态属性 logo="{logo}" logo会绑定到一个Map或者是JSONObject的key为logo的值

### 内置节点

* HBox 横向布局容器，LinearLayout Orientation为Horizontal
* VBox 纵向布局容器，LinearLayout Orientation为Vertical
* RBox 相对布局容器，使用RelativeLayout实现
* Grid Adapter布局容器，使用RecyclerView实现
* Text 文本控件
* Image 图片控件

### 属性说明

通用属性
| 名称 | 格式(N表示数值，S表示字符串) | 说明(除非特别说明，N的单位都是dp) |
| - | - | - |
|name| S |控件标识
|size| match\|wrap\|N [match\|wrap\|N]|设置width和height， 如果只有一个，则width，height相同
|margin| N [N] [N] [N] | 左上右下，一个时全部相同，两个时左右，上下
|padding| N [N] [N] [N] | 同上
|background| N(color) [N] [N] [N] [N] | 第一个为背景颜色，后面四个为圆角半径，左上，右上，右下，左下
|gravity|center\|left\|right\|top\|bottom| 单个或者组合，使用\|分隔
|weight|N|只有放在HBox和VBox中的控件设置才会有效果
|leftOf|@S|S为通过name设置的控件标识, 只有放在RBox中的控件设置才会有效果
|rightOf|@S|同上
|above|@S|同上
|below|@S|同上
|alignLeft|@S|同上
|alignRight|@S|同上
|alignTop|@S|同上
|alignBottom|@S|同上
|alignBaseline|@S|同上
|align|center\|left\|right\|top\|bottom| 单个或者组合，使用\|分隔，<font color=#A52A2A>容器类和Txt具有此属性，控制子元素的对齐方式

Text
| 名称 | 格式(N表示数值，S表示字符串) | 说明 |
| - | - | - |
|text| S | 文本内容
|fontSize| N |字体大小，单位为sp
|color| N(color) | 字体颜色
|style| bold\|italic| 设置粗体或者斜体

Image
| 名称 | 格式(N表示数值，S表示字符串) | 说明 |
| - | - | - |
|src| S | URL或者本地路径

Grid
| 名称 | 格式(N表示数值，S表示字符串) | 说明 |
| - | - | - |
|dataSource| S(JSONArray) | 内容数组
|spanCount| N | 列数量


### Grid节点

```xml
<Grid spanCount='2' dataSource='{items}'>
    <VBox margin='0 10 0 0'><Text text='{name}'/>
        <Text text='{value}' color='black'/>
    </VBox>
</Grid>
```
Grid节点只能有一个子节点，此节点可以理解成子控件的模版，Grid会根据items数组的数量，动态生成对应的子View。items数组中的每个值都是一个JSONObject，子节点中的动态属性绑定到这个JSONObject上


### 使用方法

```java
// 创建View，第一个参数是Context，第二个是包含xml的字符串
View convertView = DynamicViewEngine.getInstance().inflate(context, parent, layoutXml);
// 绑定动态属性，第一个参数是通过上面方法创建的view，第二个值是数据，Map<String, String> 或者 JSONObject
DynamicViewEngine.getInstance().bindView(convertView, data);

```

## Discussion

### QQ Group: 516157585
