package com.example.userloginsqlite;

import android.util.Log;
import androidx.annotation.NonNull;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public class dbConnect {
    private static final String BASE_URL = "http://192.168.174.97:8000/"; // Replace with your actual API URL

    public static Retrofit retrofit;
    private final ApiService apiService;

    public dbConnect() {
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        apiService = retrofit.create(ApiService.class);
    }

    // Method to create a user (with asynchronous call)
    public void createUser(users newUser) {
        Call<Void> call = apiService.createUser(newUser);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, @NonNull Response<Void> response) {
                if (response.isSuccessful()) {
                    Log.d("API", "User created successfully!");
                } else {
                    Log.e("API", "Failed to create user: " + response.code());
                }
            }

            @Override
            public void onFailure(@NonNull Call<Void> call, Throwable t) {
                Log.e("API", "Error occurred: " + t.getMessage());
            }
        });
    }

    // Method to check if email exists
    public boolean checkEmailExists(String email) {
        Call<Boolean> call = apiService.checkEmailExists(email);
        try {
            Response<Boolean> response = call.execute();
            return response.isSuccessful() && Boolean.TRUE.equals(response.body());
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static Retrofit getClient() {
        return retrofit;
    }

    // Method to check if name exists
    public boolean checkNameExists(String name) {
        Call<Boolean> call = apiService.checkNameExists(name);
        try {
            Response<Boolean> response = call.execute();
            return response.isSuccessful() && Boolean.TRUE.equals(response.body());
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // Method to get password by email
    public String getPasswordByEmail(String email) {
        Call<String> call = apiService.getPasswordByEmail(email);
        try {
            Response<String> response = call.execute();
            return response.isSuccessful() ? response.body() : null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // Method to get password by name and email
    public String getPasswordByNameAndEmail(String name, String email) {
        Call<String> call = apiService.getPasswordByNameAndEmail(name, email);
        try {
            Response<String> response = call.execute();
            return response.isSuccessful() ? response.body() : null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // Method to get user ID by email
    public int getUserIdByEmail(String email) {
        Call<Integer> call = apiService.getUserIdByEmail(email);
        try {
            Response<Integer> response = call.execute();
            return response.isSuccessful() ? response.body() : -1;
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    // Method to update user
    public void updateUser(users user) {
        Call<Void> call = apiService.updateUser(user);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Log.d("API", "User updated successfully!");
                } else {
                    Log.e("API", "Failed to update user: " + response.code());
                }
            }

            @Override
            public void onFailure(@NonNull Call<Void> call, Throwable t) {
                Log.e("API", "Error occurred: " + t.getMessage());
            }
        });
    }

    // Method to create an apparel table
    public void createApparelTable() {
        Call<Void> call = apiService.createApparelTable();
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Log.d("API", "Apparel table created successfully!");
                } else {
                    Log.e("API", "Failed to create apparel table: " + response.code());
                }
            }

            @Override
            public void onFailure(@NonNull Call<Void> call, Throwable t) {
                Log.e("API", "Error occurred: " + t.getMessage());
            }
        });
    }

    // Method to fetch an image for apparel by category
    public void fetchImg(String upperLower, Callback<byte[]> callback) {
        Call<byte[]> call = apiService.fetchImg(upperLower);
        call.enqueue(new Callback<byte[]>() {
            @Override
            public void onResponse(Call<byte[]> call, Response<byte[]> response) {
                if (response.isSuccessful()) {
                    callback.onResponse(call, response);
                } else {
                    Log.e("API", "Failed to fetch image: " + response.code());
                    callback.onFailure(call, new Throwable("Failed to fetch image"));
                }
            }

            @Override
            public void onFailure(@NonNull Call<byte[]> call, Throwable t) {
                Log.e("API", "Error occurred: " + t.getMessage());
                callback.onFailure(call, t);
            }
        });
    }

    // Method to create an apparel entry
    public void createApparel(Apparel apparel) {
        Call<Void> call = apiService.createApparel(apparel);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                if (response.isSuccessful()) {
                    Log.d("API", "Apparel created successfully!");
                } else {
                    Log.e("API", "Failed to create apparel: " + response.code());
                }
            }

            @Override
            public void onFailure(@NonNull Call<Void> call, Throwable t) {
                Log.e("API", "Error occurred: " + t.getMessage());
            }
        });
    }

    public users getUserByEmail(String email) {
        Call<users> call = apiService.getUserByEmail(email);
        try {
            Response<users> response = call.execute();
            if (response.isSuccessful() && response.body() != null) {
                return response.body(); // Return the user object if the request is successful
            } else {
                Log.e("API", "Failed to fetch user: " + response.code());
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean updateUserProfile(users user) {
        Call<Boolean> call = apiService.updateUserProfile(user);
        try {
            Response<Boolean> response = call.execute();
            return response.isSuccessful() && Boolean.TRUE.equals(response.body());
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


    // Retrofit API interface
    public interface ApiService {
        @POST("users/")
        Call<Void> createUser(@Body users user);

        @GET("users/email/{email}/")
        Call<Boolean> checkEmailExists(@Path("email") String email);

        @GET("users/name/{name}/")
        Call<Boolean> checkNameExists(@Path("name") String name);

        @GET("users/password/email/{email}/")
        Call<String> getPasswordByEmail(@Path("email") String email);
        @GET("users/{email}/")
        Call<users> getUserByEmail(@Path("email") String email);

        @GET("users/password/name/{name}/email/{email}/")
        Call<String> getPasswordByNameAndEmail(@Path("name") String name, @Path("email") String email);

        @GET("users/id/{email}/")
        Call<Integer> getUserIdByEmail(@Path("email") String email);

        @PUT("users/update/")
        Call<Void> updateUser(@Body users user);
        @PUT("users/profile/update/")
        Call<Boolean> updateUserProfile(@Body users user);
        @POST("apparel/")
        Call<Void> createApparelTable();

        @GET("apparel/{upperLower}/")
        Call<byte[]> fetchImg(@Path("upperLower") String upperLower);

        @POST("apparel/")
        Call<Void> createApparel(@Body Apparel apparel);

        @GET("/users/gender/{email}/")
        Call<String> getGenderByEmail(@Path("email") String email);

        @GET("/apparel/{email}/")
        Call<List<String>> getAllApparelsByEmail(@Path("email") String email);
        @GET("/apparel/{email}/{category}/")
        Call<List<String>> getApparelsByTypeAndEmail(@Path("email") String email, @Path("category") String category);
    }


    public String getGenderByEmail(String email) {
        Call<String> call = apiService.getGenderByEmail(email);
        try {
            Response<String> response = call.execute();
            return response.isSuccessful() ? response.body() : null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}

