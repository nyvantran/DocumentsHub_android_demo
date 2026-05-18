# CẬP NHẬT TIẾN ĐỘ DỰ ÁN DOCUMENTHUB
**Ngày thực hiện:** 07/05/2026
**Thời gian:** 11:30

## 1. Tính năng Thêm bộ sưu tập mới (Create New Collection)
- **Model:** Tạo `CollectionRequest` để đóng gói dữ liệu yêu cầu (tên bộ sưu tập).
- **API:** Bổ sung endpoint `POST /api/v1/collections` vào `CollectionService`.
- **Repository:** Thêm phương thức `createCollection` vào `CollectionRepository`.
- **ViewModel:** Cập nhật `ProfileViewModel` với hàm `createCollection`, tự động làm mới danh sách bộ sưu tập sau khi tạo thành công.
- **UI/UX:** 
    - Cập nhật `showAddCollectionDialog` trong `ProfileFragment` để gọi API tạo bộ sưu tập thực tế thay vì hiển thị thông báo giả.
    - Đóng hộp thoại sau khi nhấn nút "Thêm".
    - Hiển thị thông báo trạng thái sau khi thực hiện thao tác.

---
*Ghi chú: Toàn bộ quy trình quản lý bộ sưu tập (Thêm, Xóa, Xem chi tiết, Xóa tài liệu khỏi bộ sưu tập) hiện đã được tích hợp đầy đủ với API server.*
