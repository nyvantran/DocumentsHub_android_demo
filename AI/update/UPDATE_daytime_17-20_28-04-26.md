# Nhật ký cập nhật - DocumentHub (Android)

**Thời gian tạo:** 17:20 Ngày 28-04-2026

## 🛠 Các công việc đã thực hiện

### 1. Nâng cấp giao diện Profile (ProfileFragment)
- **Interactive Tabs**: 
    - Cập nhật `fragment_profile.xml`: Thay thế phần Tabs tĩnh bằng hệ thống Tab tương tác (Overview, Documents, Collections, Liked, Trash) có thanh chỉ báo (indicator).
    - Cập nhật `ProfileFragment.java`: Triển khai logic chuyển đổi Tab linh hoạt, cập nhật màu sắc văn bản và hiển thị indicator tương ứng.
- **Dynamic Content Container**:
    - Thay thế vùng chứa nội dung cũ bằng `FrameLayout` (`tab_content_container`) để nạp động các View (ListView/GridView) tùy theo tab được chọn.

### 2. Triển khai danh sách Tài liệu & Bộ sưu tập
- **Models**: Tạo model `Collection.java` để quản lý thông tin bộ sưu tập.
- **Layout Items**:
    - `item_profile_document.xml`: Thiết kế dạng List item cho tài liệu, bao gồm thumbnail, thông tin lượt xem/thích và các nút chức năng.
    - `item_profile_collection.xml`: Thiết kế dạng Grid item cho bộ sưu tập với thumbnail lớn và nút xóa (ImageButton) được đặt ở góc trên.
- **Adapters**:
    - `ProfileDocumentAdapter.java`: Xử lý hiển thị danh sách tài liệu trong các tab Documents, Liked, Trash.
    - `ProfileCollectionAdapter.java`: Xử lý hiển thị lưới các bộ sưu tập.
- **Optimization**: Triển khai các hàm helper `setListViewHeightBasedOnChildren` và `setGridViewHeightBasedOnChildren` để xử lý vấn đề lồng ListView/GridView vào trong ScrollView, đảm bảo hiển thị đầy đủ và cuộn mượt mà.

### 3. Chức năng Chi tiết Bộ sưu tập
- **Detail View**: Tạo `layout_collection_detail.xml` hiển thị tiêu đề bộ sưu tập và danh sách các tài liệu bên trong theo thiết kế `collection_screen.jpg`.
- **Navigation**: Thiết lập sự kiện click vào item trong tab Collections để chuyển sang màn hình chi tiết. Hỗ trợ nút quay lại danh sách bộ sưu tập ban đầu.

### 4. Chức năng Xóa (Giả lập)
- **UI Enhancement**: Sử dụng `ImageButton` cho các nút xóa trên cả tài liệu và bộ sưu tập, hỗ trợ hiệu ứng phản hồi khi nhấn.
- **Toast Notifications**: Thiết lập sự kiện click cho các nút xóa, hiển thị thông báo `Toast` xác nhận:
    - Tài liệu: `đã xóa tài liệu có id={id}`
    - Bộ sưu tập: `đã xóa collection có id={id}`

### 5. Sửa lỗi & Tối ưu code
- Thay thế `getResources().getColor()` bằng `ContextCompat.getColor()` để đảm bảo tính tương thích.
- Sửa lỗi cú pháp liên quan đến đơn vị kích thước chữ trong code Java (`14sp` sang `14`).
- Chuẩn hóa các import và gán ID cho các thành phần giao diện mới.
