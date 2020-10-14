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
### 推图请求（不需要登录）
GET
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
                "users": [
                    {
                        "userid": "xxxxxx"
                    }
                ],
                "number": 0
            },
            "createdAt": "2020-10-02xxxxx",
            "updatedAt": "2020-10-02xxxxx"
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
| userid | string | 点赞用户id |
| number | int | 点赞数 |
| createdAt | string | 图片的上传时间 |
| updatedAt | string | 图片信息更新时间 |
### 登录请求
POST
path:/login
```
{
    "result": true,
    "user": {
        "account": "xxxxxxxx",
        "password": "xxxxxxxx",
        "token": "xxxxxxxxx"
    }
}
```
| 参数 | 取值 | 说明 |
| :---- | :----: | :---- |
| result | true/false | 请求是否成功 |
| user | object | 用户账号信息 |
| account | string | 用户账号 |
| password | string | 用户密码 |
| token | string | 用户token |

### 用户关系信息请求（需要登录）
GET
path:/myInfo
```
{
    "result": true,
    "content": {
        "id": 3,
        "follow": {
            "users": [
                {
                    "userid":"xxxxxx"
                }
            ],
            "number": 0
        },
        "fans": {
            "users": [
                {
                    "userid":"xxxxxx"
                }
            ],
            "number": 0
        },
        "likes_images": {
            "images": [
                {
                    "imageid": "xxxxxxxx"
                }
            ],
            "number": 1
        },
        "collections_images": {
            "images": [
                {
                    "imageid":"xxxxxx"
                }
            ],
            "number": 0
        },
        "createdAt": "2020-07-08T10:24:35.000Z",
        "updatedAt": "2020-10-12T09:14:01.000Z",
        "UserId": 3
    }
}
```
| 参数 | 取值 | 说明 |
| :---- | :----: | :---- |
| result | true/false | 请求是否成功 |
| content | object | 请求到的用户关系信息 |
| id | int | 用户id |
| follow | object | 用户关注的人 |
| fans | object | 关注用户的人 |
| likes_imagesid | object | 用户点赞的图片 |
| collections_images | object | 用户收藏的图片 |
| users | array | 对应的用户列表 |
| userid | string | 对应用户id |
| images | array | 对应的图片列表 |
| imageid | string | 对应图片id |
| number | int | 对应用户或者图片列表的总数 |
| createdAt | string | 数据上传时间 |
| updatedAt | string | 数据更新时间 |
| UserId | int | 用户id |
### 点赞请求（需要登录）
POST
path:/images/like
```
{
    "result": true,
    "content": "点赞成功"
}
```
| 参数 | 取值 | 说明 |
| :---- | :----: | :---- |
| result | true/false | 请求是否成功 |
| content | string | 成功提示 |
### 取消点赞请求（需要登录）
POST
path:/images/cancellike
```
{
    "result": true,
    "content": "取消点赞成功"
}
```
| 参数 | 取值 | 说明 |
| :---- | :----: | :---- |
| result | true/false | 请求是否成功 |
| content | string | 成功提示 |
