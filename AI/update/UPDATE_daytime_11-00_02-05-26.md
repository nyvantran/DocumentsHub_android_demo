# Nhật ký cập nhật - DocumentHub (Android)

**Thời gian tạo:** 11:00 Ngày 02-05-2026

## 🛠 Các công việc đã thực hiện

### 1. Quản lý Thùng rác (Trash Frame)
- **Giao diện**: Tạo layout `item_trash_document.xml` đồng bộ với giao diện Profile nhưng thay đổi nút chức năng thành nút "Khôi phục" (`ic_history`).
- **Logic**: Sử dụng API `api/v1/users/me/documents?statuses=DELETED` để hiển thị danh sách tài liệu đã xóa của người dùng.

### 2. Chuẩn hóa Profile & Tài liệu cá nhân
- **API Integration**: Cập nhật tab "Documents" sử dụng API thực tế với status `READY`.
- **Adapter Refactoring**: 
    - Cấu trúc lại `ProfileDocumentAdapter` để nhận nhiều Listeners (xóa/khôi phục và click item).
    - Giải quyết vấn đề nút bấm chiếm focus của item trong ListView bằng cách xử lý click trực tiếp trên `convertView`.

### 3. Trải nghiệm người dùng (UX)
- **Xác nhận xóa**: Thêm `AlertDialog` yêu cầu xác nhận trước khi thực hiện hành động xóa tài liệu trong tab Documents.
- **Điều hướng**: Kích hoạt sự kiện nhấn vào item ở tất cả các tab (Documents, Trash, Liked, Collections) để chuyển sang màn hình chi tiết `DocumentActivity` kèm theo `DOCUMENT_ID`.

### 4. Kiến trúc MVVM
- **ProfileViewModel**: Tạo mới để quản lý tập trung các trạng thái dữ liệu của màn hình Profile, hỗ trợ tải dữ liệu thực từ API.
- **Service & Repository**: Cập nhật `DocumentService` và `DocumentRepository` với các phương thức `restoreDocument` (POST) và `getMyDocuments` (GET).

## 📝 Ghi chú
- Các tab Collections và Liked hiện vẫn đang sử dụng dữ liệu mẫu nhưng đã được cấu trúc lại để sẵn sàng kết nối API.
- Đã thêm icon `ic_add.xml` phục vụ cho việc mở rộng tính năng tạo bộ sưu tập sau này.
