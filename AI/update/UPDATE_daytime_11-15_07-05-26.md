# CẬP NHẬT TIẾN ĐỘ DỰ ÁN DOCUMENTHUB
**Ngày thực hiện:** 07/05/2026
**Thời gian:** 11:15

## 1. Tính năng Xóa tài liệu khỏi bộ sưu tập (Remove Document from Collection)
- **API:** Bổ sung endpoint `DELETE /api/v1/collections/{collection_id}/items/{document_id}` vào `CollectionService`.
- **Repository:** Thêm phương thức `removeItemFromCollection` vào `CollectionRepository`.
- **ViewModel:** Cập nhật `ProfileViewModel` với hàm `removeItemFromCollection`, tự động làm mới danh sách tài liệu trong bộ sưu tập sau khi xóa thành công.
- **UI/UX:** 
    - Triển khai hộp thoại xác nhận (AlertDialog) khi người dùng nhấn vào nút xóa (`btn_delete_doc`) trong chi tiết bộ sưu tập của `ProfileFragment`.
    - Hiển thị thông báo trạng thái (Toast) sau khi thực hiện thao tác xóa.

---
*Ghi chú: Đã hoàn thành toàn bộ luồng xóa bộ sưu tập và xóa tài liệu khỏi bộ sưu tập, đảm bảo tính nhất quán của dữ liệu.*
