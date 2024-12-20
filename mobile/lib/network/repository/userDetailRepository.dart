import 'package:dio/dio.dart';
import '../../models/user.dart';
import '../api/userDetailApi.dart';
import '../dioException.dart';

class UserDetailRepository {
  final UserDetailApi userDetail;
  UserDetailRepository(this.userDetail);
  
  Future<User> getUserRequested() async {
    try {
      final response = await userDetail.userDetailApi();
      User user = User.fromJson(response.data);
      print("user info: ${response.data}");

      return user;
    } on DioException catch (e) {
      final errorMessage = DioExceptions.fromDioError(e).toString();
      throw errorMessage;
    }
  }

  Future<String> updateUserRequested(String firstName, String lastName) async {
    try {
      final response = await userDetail.updateUserDetailApi(firstName, lastName);
      print("user info: ${response}");
      return response.data;
    } on DioException catch (e) {
      final errorMessage = DioExceptions.fromDioError(e).toString();
      throw errorMessage;
    }
  }
}