# Table of Content
- Các quy ước chung
- User API
    + auth/
    + documents/
    + collections/
    + categories/
    + users/
    + reports/
# Các quy ước chung

## API response 

### API response cho trường hợp response success:
- Các API thường, không yêu cầu phân trang(pagination) dữ liệu:
```json
{   
    success: boolean = true
    data: any
    message: string | none 
}
```

- Các API có phân trang dữ liệu(thường dành cho các api get data có kèm query param `page` và `limit`   


```json
{
    success: boolean = true,
    data: list[any]
    message: string | None

    meta:{
        # page hiện tại (tương ứng query param 'page')
        current_page: int   

        # max items per page (tương ứng query param 'limit')
        per_page: int 
        
        # tổng số items có trong hệ thống
        total_items: int

        # tổng số page
        total_pages: int

        # có thể query trang sau không (go to page+1)
        has_next: boolean

        # có thể query trang trước đó không (go to page-1)
        has_prev: boolean
    }
}

```


### API response cho trường hợp response error:

```json
{   
    success: boolean = true
    error_code: string
    message: string | none 
}
```

- error_code là một chuỗi string thể hiện chi tiết lỗi(http status response chỉ là gom nhóm lỗi trong phạm vi nào, error_code sẽ cho biết cụ thể lỗi là gì)
- Danh sách các error_code(và http status của nó) ở backend/app/core/error_code.py
- Trường hợp lười catch lỗi quá thì display message trả về cho client :)

## API request

## Giá trị mặc định của các request
- Các tham số request có giá trị mặc định trong mô tả ở api là optional, có thể truyền hoặc không. Ví dụ:
  + Các api update, các thuộc tính nào có default=None có nghĩa nếu cần update thì truyền, không cần update thì bỏ qua
  + Các api get data: các query param có thuộc tính mặc định là None/[] là cần filter/query theo điều kiện này thì dùng, không thì bỏ qua

### Cách đính kèm JWT token
Xem chi tiết ở api /auth bên dưới

### Query param
Các api nhận query param là một list cần truyền theo kiểu multi value  
Ví dụ:
- api `/search` nhận query params `tags` là một `list[string]`

- Cần tìm các document có tags [book, java, oop]

=> gọi api với query param theo cú pháp:
get /`search?tags=book&tags=java&tag=oop`

# API user

## /auth/

### [GET] /auth/whoami/
Check nhanh bản thân đăng nhập chưa/thông tin các nhân cơ bản

### Luồng đăng ký tài khoảng:

- Step1: [POST] `/auth/register/request`   
  Nhập email, gởi OTP đến email
- Step2: [POST] `/auth/register/verify`  
  Verify OTP nhận được từ email, nếu OTP và email khớp trả về registration_code
- Step3: [POST] `/auth/register/complete`  
  Nhập đủ thông tin đăng ký tài khoảng kèm registration_code, nếu tạo tài khoảng thành công sẽ tự trả JWT access/refresh token để đăng nhập ngay.

### Cách tuyền JWT token kèm request:
- Access Token  
  + Web client: sau khi đăng nhập, server ghi vào cookie, client ko cần quản lý
  + Mobile client: sau khi đăng nhập, nhận access token từ response, tự lưu trữ, tự đính kèm theo từng request header:  
    'Bearer {access token}'
- Refresh Token  
  + Web client: sau khi đăng nhập, server ghi vào cookie, client ko cần quản lý
  + Mobile client: sau khi đăng nhập, nhận refresh token từ response, tự lưu trữ. Dùng refresh token để lấy refresh access token. Gọi đến api /auth/refresh và đính kèm vào body
```
{
    refresh_token: {refresh token}}
}
```


### [POST] /auth/login
Login, truyền `identity` là username hoặc email đều được

### [POST] /auth/logout
Logout, nếu các JWT token còn hiệu lực thì sẽ tự động bị server thêm vào jwt blacklist
- Với mobile client: có thể truyền kèm access token trong header và refresh token trong body để server thêm các token và jwt blacklist sau khi logout. Nếu lười thì chỉ cần xóa các token đang lưu ở app, khỏi gọi api logout cũng được :)

### [POST] /auth/refresh
Refresh access token
- Web client: tự động truyền refresh token qua cookies
- Mobile client: truyền refresh token trong body

### [POST] /auth/forgot_password
Quên mật khẩu, nhập username/email để nhận otp

### [POST] /auth/reset_password
Nhập username/email + otp + new password để reset pasword

## /search/
### [GET] /search?{query params...}

Các query params:

- `q`: string  
  Từ khóa tìm kiếm (ví dụ: title, nội dung, ...)

- `page`: int  
  Trang hiện tại (mặc định: 1)

- `limit`: int  
  Số lượng item mỗi trang

- `tags`: string[]  
  Lọc theo danh sách tag (có thể truyền nhiều lần)

- `sort`: string  
  Điều kiện sắp xếp, cú pháp:

  [+/-]<field1>,[+/-]<field2>,...

  Quy ước:
  - Độ ưu tiên từ trái sang phải
  - `+` : tăng dần (ascending)
  - `-` : giảm dần (descending)

  Danh sách các fields: 
```python
# file: /backend/app/service/document_service.py
SORT_FIELD_MAP = {
    "title": Document.title,
    "view": Document.view_count,
    "like": Document.like_count,
    "download": Document.download_count,
    "created_at": Document.created_at,
}
```

- Còn nhiều cái nữa, tự coi trong localhost:8000/docs


Ví dụ:
GET /search?q=book&page=1&limit=10&sort=-view,-like&tags=oop&tags=java

=> tìm theo từ khóa "book", lọc theo tags=[oop, java], sắp xếp giảm dần view, giảm dần like

## /documents/

### [GET] /documents/supported_types
Danh sách các loại tài liệu hỗ trợ upload   
- Mặc định luôn trả về ['.pdf', '.doc', '.docx', '.ppt', '.pptx']

### [GET] /documents/max_size
Kích thước tối đa cho phép của tài liệu upload(đơn vị: byte)

### [POST] /documents
Upload tài liệu. Xem api [GET] `/categories` để lấy danh sách các category.

### [GET] /documents/{document_id}
Lấy thông tin chi tiết tài liệu kèm link file preview(là tài liệu nhưng convert sang version pdf để dễ hiển thị)
- field `liked` trong `response.data` là boolean thể hiện user hiện tại có like document này chưa. Hỗ trợ render nút like cho UI.
- field `available_format` trong `response.data` là `list[str]`  danh sách các format của tài liệu hỗ trợ tải về. Nếu tài liệu gốc là PDF thì luôn là [.pdf], nếu tài liệu gốc không phải pdf thì là [{original format}, .pdf]. Hỗ trợ để chọn download format trong api [GET] documents/{document_id}/download

### [PATCH] /documents/{document_id}
Update document
- field `tags`: list[str] trong body nếu được cung cấp sẽ update kiểu override, không phải append

### [PUT] /documents/{document_id}/tags
Thêm tags

### [DELETE] /documents/{document_id}/tags
Xóa tag khỏi document

### [DELETE] /documents/{document_id}
Soft delete. 
- Tự hard delete sau 30 ngày. 
- Có thể lấy danh sách các document đang bị soft delete của bản thân bằng [GET] /user/me/document?tatus=DELETED
- Có thể restore bằng post /documents/{document_id}/restore

### [POST] /documents/{document_id}/restore
Restore soft deleted document

### [PUT] /documents/{document_id}/like
Like document

### [DELETE] /documents/{document_id}/like
Remove like document


### [GET] /documents/{document_id}/download
Lấy link để download. Cho phép chọn format(original/pdf)

### [PUT] /documents/{document_id}/collections
Dùng cho chức năng thêm document vào collection
- Phù hợp UI checkbox
- Danh sách collection_id sẽ cập nhật kiểu override. Các collection nào không nằm trong collection_ids thì sẽ tự tách document ra khỏi colelctions đó

### 

## /categories/
### [GET] /categories

## /collections/

### [POST] /collections
Create new

### [PATCH] /collections/{collection_id}
Update

### [DELETE] /collections/{collection_id}
Delete

### [GET] /collections/{collection_id}/items
Lấy danh sách các document trong collection

### [PUT] /collections/{collection_id}/items/{document_id}
Thêm document vào collection

### [DELETE] /collections/{collection_id}/items/{document_id}
Xóa document khỏi collections


## /reports/
### [GET] /reports/available_reasons
Lấy danh sách report_reason
- Khi user report document, hệ thống có sẵn danh sách các `report reason` là các lý do khái quát, user chọn một trong các lý do đó và gởi kèm mô tả chi tiết


### [POST] /reports/documents/{document_id}
Tạo report (báp cáo tài liệu vi phạm)


## /users/
### [GET] /users/me/profile
Lấy profile cá nhân

### [PATCH] /users/me/profile
Update profile cá nhân
- Avatar là dạng ảnh, cần gởi qua form nên có api riêng để update

### [PUT] /users/me/avatar
Update avatar cá nhân


### [GET] /users/me/documents
Lấy danh sách các document của bản thân

### [GET] /users/me/liked_documents
Lấy danh sách các document mà bản thân đã like

### [GET] /users/me/collections
Lấy danh sách các collection của bản thân

### [GET] /users/{username}/profile
Lấy profile của người khác


### [GET] /users/{username}/documents
Lấy danh sách các document(PUBLIC, READY) của người khác

