# HƯỚNG DẪN CHẠY CHƯƠNG TRÌNH

## Yêu cầu:
Để chạy được chương trình, máy tính phải đảm bảo các yêu cầu sau:

- Cài đặt Java (phiên bản tối thiểu là 16.0.2).
- Cài đặt Apache Maven (phiên bản tối thiểu là 3.8.3).
- Cài đặt MySQL (phiên bản tối thiểu là 8.0).

## Các bước thực hiện:
### Bước 1: Tạo cơ sở dữ liệu (CSDL) MySQL trong máy

- Tạo CSDL MySQL mới trong máy.
- Đặt tên cho CSDL vừa tạo là __club_management__.

### Bước 2: Thiết lập môi trường trong IntelliJ IDEA

- Mở dự án trong IntelliJ IDEA, chọn _Edit Configuration_.
- Ở tab _Configuration_, ta khai báo các biến môi trường vào khung _Environment variables_. Danh sách các biến môi trường bao gồm:

  |  Tên biến môi trường | Ý nghĩa  | Bắt buộc khai báo  |
  |---|---|---|
  | DB_ADDRESS  | Địa chỉ của CSDL  | Không  |
  | DB_PORT  | Cổng kết nối CSDL  | Không  |
  | DB_DATABASE  | Tên CSDL  | Không  |
  | DB_USERNAME  | Tên đăng nhập vào CSDL  | Có  |
  | DB_PASSWORD  | Mật khẩu đăng nhập vào CSDL  | Có  |
  | CLIENT_TIME_ZONE  | Múi giờ của người dùng  | Không  |
  | EMAIL_USERNAME  | Địa chỉ email dùng để gửi email tự động cho người dùng  | Có  |
  | EMAIL_PASSWORD  | Mật khẩu email dùng để gửi email tự động cho người dùng  | Có  |
  
- Đối với các biến môi trường không bắt buộc khai báo, nếu người dùng không khai báo thì hệ thống sẽ sử dụng giá trị mặc định được gán sẵn trong file __application.yaml__ ở thư mục _./src/main/resources_.

### Bước 3: Chạy chương trình
Nhấn tổ hợp phím Shift + F10 trong IntelliJ IDEA để khởi chạy chương trình.