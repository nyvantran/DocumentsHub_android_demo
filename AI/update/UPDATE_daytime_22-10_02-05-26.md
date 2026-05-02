# Nhật ký cập nhật - DocumentHub (Android)

**Thời gian tạo:** 13:30 Ngày 02-05-2026

## 🛠 Các công việc đã thực hiện (Nghiên cứu & Kiểm định)

### 1. Phân tích cấu trúc và Công nghệ
- **Kiến trúc**: Xác nhận dự án tuân thủ mô hình **MVVM** với sự tách biệt rõ ràng giữa Activity/Fragment, ViewModel, Repository và Model.
- **Networking**: Kiểm tra hệ thống Retrofit tích hợp `AuthInterceptor` và `TokenAuthenticator` để xử lý JWT tự động. Base URL được cấu hình tại `ApiClient`.
- **Dữ liệu**: Đã nắm rõ các đối tượng `Document`, `Collection`, `Category` và logic xử lý API tương ứng.

### 2. Kiểm định tiến độ qua Git (Audit)
- Phân tích commit gần nhất (`4478536`):
    - **Tính năng Thùng rác (Trash)**: Đã được triển khai với giao diện `item_trash_document.xml` và logic khôi phục tài liệu qua API.
    - **Refactoring Adapter**: `ProfileDocumentAdapter` đã được chuyển sang cơ chế Listener-based để xử lý sự kiện click item và nút xóa độc lập, giải quyết vấn đề chiếm focus của ListView.
    - **MVVM Integration**: `ProfileViewModel` đã được liên kết với `ProfileFragment` để quản lý dữ liệu thực tế từ API thay cho dữ liệu mẫu.
    - **UX**: Đã có hộp thoại xác nhận khi xóa tài liệu và điều hướng chi tiết tài liệu qua `DOCUMENT_ID`.

### 3. Đánh giá trạng thái hiện tại
- **Màn hình Profile**: Các tab Documents và Trash đã chạy với dữ liệu thực. Tab Liked và Collections đã có khung giao diện và adapter nhưng vẫn đang dùng dữ liệu mẫu.
- **Màn hình Home**: Đã có tính năng tìm kiếm và lọc theo danh mục.
- **Màn hình Chi tiết (DocumentActivity)**: Đã sẵn sàng để hiển thị thông tin chi tiết.

## 📝 Ghi chú cho phiên tiếp theo
- Cần tiếp tục kết nối API cho tab **Liked** và **Collections** trong màn hình Profile.
- Hoàn thiện tính năng **Upload** tài liệu (đã có khung giao diện `UploadFragment`).
- Kiểm tra và tối ưu hóa việc hiển thị Preview tài liệu (phiên bản PDF) trong `DocumentActivity`.
