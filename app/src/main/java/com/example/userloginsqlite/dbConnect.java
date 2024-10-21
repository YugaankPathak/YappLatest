package com.example.userloginsqlite;

import android.util.Log;
import androidx.annotation.NonNull;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Map;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public class dbConnect {
    private static final String BASE_URL = "http://192.168.80.6:8000/"; // Replace with actual API URL

    public static Retrofit retrofit;
    public final ApiService apiService;

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
                    Log.d("API",Integer.toString(newUser.getAge()));
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
    public String getUserIdByEmail(String email) {
        if (email == null || email.isEmpty()) {
            Log.e("API Error", "Email is null or empty");
            return "nothing here";
        }
        Log.e("Info",email);
        Call<String> call = apiService.getUserIdByEmail(email);
        try {
            Response<String> response = call.execute();
            if (response.isSuccessful()) {
                String userId = response.body();
                if (userId != null) {
                    return userId; // Return the user ID if not null
                } else {
                    Log.e("API Error", "Response body is null for email: " + email);
                }
            } else {
                Log.e("API Error", "Response code: " + response.code() + ", Message: " + response.message());
            }
        } catch (Exception e) {
            Log.e("API Error", "Exception occurred: " + e.getMessage());
            e.printStackTrace();
        }
        return "nothing"; // Return nothing if any issues occur
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



    // Method to fetch an image for apparel by category
    public void fetchImg(String upperLower, Callback<String> callback) {
        Call<String> call = apiService.fetchImg(upperLower);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    callback.onResponse(call, response);
                } else {
                    Log.e("API", "Failed to fetch image: " + response.code());
                    callback.onFailure(call, new Throwable("Failed to fetch image"));
                }
            }

            @Override
            public void onFailure(@NonNull Call<String> call, Throwable t) {
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
                    apparel.getInfo(apparel);
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
    public void post_combination(String combination, String base64Screenshot) {
        try {
            // Create JSON object to send combination and screenshot
            JSONObject requestBodyJson = new JSONObject();
            requestBodyJson.put("ownership", combination);
            requestBodyJson.put("image", base64Screenshot);

            // Convert JSON object to RequestBody
            RequestBody requestBody = RequestBody.create(
                    MediaType.parse("application/json"),
                    requestBodyJson.toString()
            );

            // Make API call using Retrofit
            Call<Void> call = apiService.post_combination(requestBody);
            call.enqueue(new Callback<Void>() {
                @Override
                public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                    if (response.isSuccessful()) {
                        Log.d("API", "Combination saved successfully!");
                    } else {
                        Log.e("API", "Failed to create combination: " + response.code());
                    }
                }

                @Override
                public void onFailure(@NonNull Call<Void> call, Throwable t) {
                    Log.e("API", "Error occurred: " + t.getMessage());
                }
            });
        } catch (JSONException e) {
            Log.e("API", "JSON error: " + e.getMessage());
        }
    }

    // Fetch the last combinations based on ownership
    public void get_last_combinations(String ownership, final OutfitCallback<List<Map<String, String>>> callback) {
        Call<List<Map<String, String>>> call = apiService.get_last_combinations(ownership);
        call.enqueue(new Callback<List<Map<String, String>>>() {
            @Override
            public void onResponse(Call<List<Map<String, String>>> call, Response<List<Map<String, String>>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body());  // Return the list of combinations
                } else {
                    callback.onFailure(new Exception("Failed to get combinations"));
                }
            }

            @Override
            public void onFailure(Call<List<Map<String, String>>> call, Throwable t) {
                callback.onFailure(t);
            }
        });
    }

    public void get_all_combinations(String ownership, final OutfitCallback<List<Map<String, String>>> callback) {
        try {
            // Create JSON object to send ownership data
            JSONObject requestBodyJson = new JSONObject();
            requestBodyJson.put("ownership", ownership);

            // Convert JSON object to RequestBody
            RequestBody requestBody = RequestBody.create(
                    MediaType.parse("application/json"),
                    requestBodyJson.toString()
                        );

            // Make API call using Retrofit
            Call<List<Map<String, String>>> call = apiService.get_all_combinations(requestBody);
            call.enqueue(new Callback<List<Map<String, String>>>() {
                @Override
                public void onResponse(Call<List<Map<String, String>>> call, Response<List<Map<String, String>>> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        List<Map<String, String>> combinations = response.body();
                        Log.d("RetrofitSuccess", "Fetched combinations successfully: " + combinations.size());
                        callback.onSuccess(combinations);
                    } else {
                        try {
                            // Log error body for better debugging
                            Log.e("RetrofitError", "Error fetching combinations: " + response.errorBody().string());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        callback.onFailure(new Exception("Failed to fetch combinations"));
                    }
                }

                @Override
                public void onFailure(Call<List<Map<String, String>>> call, Throwable t) {
                    Log.e("RetrofitFailure", "Request failed: " + t.getMessage());
                    callback.onFailure(new Exception("Failed to get combinations", t));
                }
            });
        } catch (Exception e) {
            Log.e("RetrofitException", "Error creating request body: " + e.getMessage());
            callback.onFailure(new Exception("Error creating request body", e));
        }
    }
    // Recommend an outfit based on ownership and occasion
    public void recommendOutfit(String ownership, String occasion, OutfitCallback<Map<String, String>> callback) {
        // Create an instance of the request body
        OutfitRequest request = new OutfitRequest(ownership, occasion);

        // Pass the request object to the API method
        Call<Map<String, String>> call = apiService.recommend_outfit(request);

        call.enqueue(new Callback<Map<String, String>>() {
            @Override
            public void onResponse(Call<Map<String, String>> call, Response<Map<String, String>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body());
                } else {
                    callback.onFailure(new Exception("Failed to get outfit recommendations"));
                }
            }

            @Override
            public void onFailure(Call<Map<String, String>> call, Throwable t) {
                callback.onFailure(t);
            }
        });
    }

    // Define a generic callback interface for both methods
    public interface OutfitCallback<T> {
        void onSuccess(T result);
        void onFailure(Throwable t);
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


        @GET("users/password/name/{name}/email/{email}/")
        Call<String> getPasswordByNameAndEmail(@Path("name") String name, @Path("email") String email);

        @GET("users/id/email/{email}/")
        Call<String> getUserIdByEmail(@Path("email") String email);

        @PUT("users/update/")
        Call<Void> updateUser(@Body users user);

        @PUT("users/profile/update/")
        Call<Boolean> updateUserProfile(@Body users user);

        @GET("users/details/email/{email}")
        Call<users> get_user_by_email(@Path("email") String email);

        @GET("apparel/user/{email}/")
        Call<String> fetchImg(@Path("email") String email);

        @POST("apparel/create")
        Call<Void> createApparel(@Body Apparel apparel);

        @GET("/users/gender/email/{email}/")
        Call<String> getGenderByEmail(@Path("email") String email);

        @GET("/apparel/user/{email}/")
        Call<List<Map<String,String>>> getAllApparelsByEmail(@Path("email") String email);

        @GET("/apparel/user/{email}/{upper_lower}/")
        Call<List<Map<String,String>>> getApparelsByTypeAndEmail(@Path("email") String email, @Path("upper_lower") String upper_lower);

        @POST("/combinations/post/")
        Call<Void> post_combination(@Body RequestBody combination);
        @POST("/combinations/last/")
        Call<List<Map<String,String>>> get_last_combinations(@Body String ownership);
        @POST("/combinations/all/")
        Call<List<Map<String,String>>> get_all_combinations(@Body RequestBody ownership );

        @POST("/outfit/recommendation/")
        Call<Map<String, String>> recommend_outfit(@Body OutfitRequest request);

        @GET("/users/details/email/{email}/")
        Call<users> getUserByEmail(@Path("email") String email);


    }

    public users getUserByEmail(String email) {
        Call<users> call = apiService.getUserByEmail(email);

        call.enqueue(new Callback<users>() {
            @Override
            public void onResponse(Call<users> call, Response<users> response) {
                if (response.isSuccessful()) {
                    users user = response.body();
                    if (user != null) {
                        // Handle the success, e.g., display user data

                        Log.d("User Fetch Success", "User Name: " + user.getName());

                        // You can update the UI here
                    } else {
                        Log.d("User Fetch Success", "User not found");
                    }
                } else {
                    Log.e("User Fetch Failure", "Response Error: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<users> call, Throwable t) {
                // Handle failure
                Log.e("User Fetch Failure", "Error: " + t.getMessage());
            }
        });

        return null;
    }

    // Callback interface
    public interface UserCallback
    {
        void onSuccess(users user);
        void onFailure(Throwable t);
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

    public List<Map<String,String>> getAllApparelsByEmail(String email) {
        Call<List<Map<String,String>>> call = apiService.getAllApparelsByEmail(email);
        try {
            Response<List<Map<String,String>>>response = call.execute();
            return response.isSuccessful() ? response.body(): null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
class OutfitRequest {
    String ownership;
    String occasion;

    // Constructor
    public OutfitRequest(String ownership, String occasion) {
        this.ownership = ownership;
        this.occasion = occasion;
    }}