# GraphicDesign 【图设(图摄)】


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
