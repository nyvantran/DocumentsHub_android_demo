# Nhật ký cập nhật - DocumentHub (Android)

**Thời gian tạo:** 11:26 Ngày 24-04-2026

## 🛠 Các công việc đã thực hiện

### 1. Tái cấu trúc Repository (Refactoring)
- **Tách `CategoryRepository`**: Di chuyển toàn bộ logic liên quan đến danh mục từ `DocumentRepository` sang `CategoryRepository` mới để đảm bảo nguyên tắc Single Responsibility.
- **Dọn dẹp `DocumentRepository`**: Loại bỏ `CategoryService` và các phương thức liên quan đến danh mục, tập trung vào nghiệp vụ tài liệu.

### 2. Nâng cấp Giao diện Trang chủ (Home Screen)
- **Thanh danh mục động (Categories)**:
    - Thay thế `HorizontalScrollView` bằng `RecyclerView` để tối ưu hiệu suất.
    - Tạo `CategoryAdapter` và layout `item_category_tab.xml` để quản lý danh sách danh mục từ API.
    - Hỗ trợ trạng thái được chọn (Selected/Unselected) và tự động lọc tài liệu khi nhấn vào danh mục.
- **Thanh tìm kiếm (Search Bar)**:
    - Thêm ID `et_search` và cấu hình `imeOptions="actionSearch"`, `singleLine="true"` để kích hoạt nút Tìm kiếm trên bàn phím.
    - Xử lý sự kiện tìm kiếm và tự động ẩn bàn phím sau khi thực hiện.
    - Thêm `TextWatcher` để tự động khôi phục dữ liệu "Trending" khi người dùng xóa trắng thanh tìm kiếm.

### 3. Hiển thị Ảnh thu nhỏ (Thumbnail)
- **Tích hợp Thư viện Glide**: Bổ sung Glide vào `libs.versions.toml` và `build.gradle.kts` để xử lý tải ảnh từ URL.
- **Cập nhật `item_document.xml`**: Thay thế khối `CardView` giả lập bằng `ImageView` (`iv_doc_thumbnail`) để hiển thị ảnh thực tế từ API.
- **Cập nhật `DocumentAdapter`**: Thực hiện bind dữ liệu `file_thumbnail_url` vào `ImageView` sử dụng Glide với các chế độ placeholder và error handling.

### 4. Kết nối API & Logic Nghiệp vụ
- **Chuẩn hóa Search API**:
    - Cập nhật `DocumentService` để hỗ trợ đầy đủ tham số: `q`, `page`, `limit`, `sort`.
    - `HomeViewModel`: Thực hiện tìm kiếm mặc định với query trống và sắp xếp theo `-view` (Trending) ngay khi khởi tạo ứng dụng, thay thế hoàn toàn dữ liệu giả (Dummy data).
- **Phân tách dữ liệu**: Tách biệt LiveData cho danh sách danh mục (thanh cuộn ngang) và các section tài liệu (ListView chính).

### 5. Sửa lỗi (Bug Fixes)
- Khắc phục vấn đề nút Search trên bàn phím không hoạt động bằng cách cấu hình đúng `inputType` và `imeOptions`.
- Đảm bảo bàn phím được ẩn đi sau khi người dùng xác nhận tìm kiếm để không che khuất kết quả.

## 📝 Ghi chú
- Dữ liệu hiện tại đã được lấy thực tế từ API backend thông qua các Service tương ứng.
- Ảnh thumbnail tài liệu sẽ tự động hiển thị nếu API trả về URL hợp lệ.
