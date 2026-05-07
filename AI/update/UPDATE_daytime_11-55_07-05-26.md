# CẬP NHẬT TIẾN ĐỘ DỰ ÁN DOCUMENTHUB
**Ngày thực hiện:** 07/05/2026
**Thời gian:** 11:55

## 1. Tính năng Hiển thị tài liệu đã thích (Liked Documents)
- **API:** Bổ sung endpoint `GET /api/v1/users/me/liked_documents` vào `UserService`.
- **Repository:** Thêm phương thức `getLikedDocuments` vào `UserRepository`.
- **ViewModel:** 
    - Cập nhật `ProfileViewModel` với hàm `fetchLikedDocuments` để lấy danh sách tài liệu đã thích.
    - Thêm hàm `unlikeDocument` để xử lý việc bỏ thích tài liệu trực tiếp từ danh sách.
- **UI/UX:** 
    - Cập nhật tab "Liked" trong `ProfileFragment` để hiển thị dữ liệu thực tế từ API.
    - Triển khai hộp thoại xác nhận khi người dùng nhấn vào nút xóa (bỏ thích) trên tài liệu.
    - Tự động làm mới danh sách sau khi người dùng thực hiện bỏ thích.

---
*Ghi chú: Toàn bộ các tab trong Profile hiện đã được kết nối với dữ liệu thực tế từ API, đảm bảo trải nghiệm người dùng đồng nhất.*
