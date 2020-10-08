# GraphicDesign 【图设(图摄)】

## app依赖
```
implementation "androidx.recyclerview:recyclerview:1.1.0"
implementation 'androidx.lifecycle:lifecycle-extensions:2.2.0'
implementation 'androidx.lifecycle:lifecycle-livedata-ktx:2.20'
implementation 'com.google.android.material:material:1.2.1'
implementation 'com.squareup.retrofit2:retrofit:2.9.0'
implementation 'com.squareup.retrofit2:converter-gson:2.6.1'
implementation 'org.jetbrainskotlinx:kotlinx-coroutines-core:1.3.9'
implementation 'org.jetbrainskotlinx:kotlinx-coroutines-android:1.3.9'
implementation 'com.github.bumptech.glide:glide:4.11.0'
annotationProcessor 'com.github.bumptech.glide:compiler:4.110'
implementation 'com.github.chrisbanes.photoview:library:1.2.4'
// 解决jdk版本低的问题
android {
    compileOptions {
        sourceCompatibility JavaVersion.VERSION_1_8
        targetCompatibility JavaVersion.VERSION_1_8
    }
}
```

## 数据分析
### 推图请求
path:/images
```
{
    "result": true,
    "number": 9,
    "content": [
        {
            "id": 21,
            "url": "http://192.168.137.1//图廊/data/images/1601637306572.png",
            "thumbnailUrl": "http://192.168.137.1//图廊/data/thumbnail/1601637306572.png",
            "thumbnailHeight": 800,
            "thumbnailWidth": 600,
            "likes": {
                "users": [],
                "number": 0
            },
            "createdAt": "2020-10-02T11:15:06.000Z",
            "updatedAt": "2020-10-02T11:15:06.000Z"
        },
        ........
    ]
}
```
| 参数 | 取值 | 说明 |
| :---- | :----: | :---- |
| result | true/false | 请求是否成功 |
| number | int | 请求到的图片数a量 |
| content | array | 请求到的图片 |
| id | int | 图片id |
| url | string | 图片的访问地址 |
| thumbnailUrl | string | 缩略图的访问地址 |
| thumbnailHeight | int | 缩略图的高度 |
| thumbnailWidth | int | 缩略图的宽度 |
| likes | object | 图片的点赞信息 |
| users | array | 点赞图片的用户 |
| number | int | 点赞数 |
| createdAt | string | 图片的上传时间 |
| updatedAt | string | 图片信息更新时间 |
