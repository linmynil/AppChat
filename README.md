# Chat Pro App

## Công nghệ: 
Java + Firebase

## Giao diện:
Mình sẽ có khoảng 6 màn hình chính
 - Đăng ký
 - Đăng nhập
 - Danh sách ChatRoom
 - ChatRoom
 - Danh bạ (danh sách user)
 - Trang cá nhân

## Mô tả tính năng:

 ### Đăng ký:
> Đăng ký cần thông tin SĐT, Mật khẩu, Họ Tên, Hình đại diện (Có thể chưa cần liền, sau này có thời gian sẽ bổ sung sau)
 ### Đăng nhập:
> Đăng nhập bằng sđt, mật khẩu
 ### Danh sách phòng chat:
> Hiển thị danh sách các phòng chat có nội dung tin nhắn. Giao diện từng item bao gồm 1 ảnh đại diện (chọn đại 1 ảnh mặc định), Họ tên, tin nhắn cuối cùng, thời gian.
 ### Phòng chat:
> Hiển thị danh sách các tin nhắn có Id của phòng chat đó, người gửi bên phải, người nhận bên trái. (Giao diện từng item gồm tên người gửi, nội dung, thời gian). Danh sách này lấy dữ liệu từ FireBase, khi có tin nhắn mới phải tự động cập nhật và hiển thị lên.
 ### Gửi tin nhắn:
> Khi nhấn nút gửi, thông tin tin nhắn sẽ được gửi lên FireBase
 ### Danh bạ:
> Hiển thị danh sách, thêm, xóa, sửa người dùng đã được thêm vào danh bạ. Khi thêm người dùng phải kiểm tra sẽ sđt đã có trên firebase chưa, nếu chưa thì không được phép thêm.
 ### Trang cá nhân:
> Sửa thông tin họ tên, ảnh đại diện của người dùng, nút đăng xuất.

## Database Model:
- User:
     + id (String)
     + PhoneNumber (String) 
     + fullName (String)
     + Picture (String)
     + phoneBook (ArrayList<String>)
- Message:
     + id (String)
     + content (String)
     + time (String)
     + chatRoomId (String)
     + phoneUser1Id (String)
     + phoneUser2Id (String)
- ChatRoom:
     + Id (String)
     + nameUser2 (String)
     + lastMessageID (String)
     + messageList (ArrayList<String>)
     + userPhoneNumber (ArrayList<String>)
     + lastMessageDate (Date)
