package com.homebit.androidtest.utils;

/**
 * Created by Prince on 11-10-2017.
 */
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.NetworkResponse;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;


public class APIHandler {


    private static APIHandler mSharedInstance = null;
    private static Context mContext;
    private RequestQueue mRequestQueue;

    private final static String TAG = "API";

    public synchronized static APIHandler getsharedInstance(Context context) {
        mContext = context;

        if (mSharedInstance == null) {
            mSharedInstance = new APIHandler();
        }
        return mSharedInstance;
    }


    /**
     * Get Singleton Request Queue
     *
     * @return Request queue
     */
    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            // getApplicationContext() is key, it keeps you from leaking the
            // Activity or BroadcastReceiver if someone passes one in.
            mRequestQueue = Volley.newRequestQueue(mContext.getApplicationContext());
        }
        return mRequestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req) {
        getRequestQueue().add(req);
    }

    public void executeJsonObject(int requestMethod, String url, JSONObject jsonData, final Response.Listener<JSONObject> onExecuteResponse,
                                  final Response.ErrorListener executeErrorListener, final String token) {

        Log.d(TAG, "URL: " + url);
        Log.d(TAG, "Request: " + jsonData);

        // Form requestURL from BASE_URL and API strings
        String requestURL = url;

        JsonObjectRequest request = new JsonObjectRequest(requestMethod, requestURL, jsonData,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Log.d(TAG, "Response: " + response);
                            onExecuteResponse.onResponse(response);
                        } catch (Exception e) {
                            Log.d(TAG, "System Error: " + e.toString());
                            e.printStackTrace();
                        }
                    }
                },

                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        try {
                            executeErrorListener.onErrorResponse(error);
                            String message = "Server error.";


                            NetworkResponse networkResponse = error.networkResponse;
                            if (networkResponse != null) {

                                Log.e("Submit", "Error. HTTP Status Code:" + networkResponse.statusCode);
                            }

                            if (error instanceof TimeoutError) {
                                Log.e("Volley", "TimeoutError");
                                message = "Server Timeout.";

                            } else if (error instanceof NoConnectionError) {
                                Log.e("Volley", "NoConnectionError");
                                message = "No internet connection.";
                                Toast.makeText(mContext, "" + message, Toast.LENGTH_SHORT).show();
                            } else if (error instanceof AuthFailureError) {
                                Log.e("Volley", "AuthFailureError");
                                message = "Server error.";
                            } else if (error instanceof ServerError) {
                                try {
                                    message = "Server error.";
                                    String res = new String(networkResponse.data,
                                            HttpHeaderParser.parseCharset(networkResponse.headers));
                                    // Now you can use any deserializer to make sense of data
                                    JSONObject obj = new JSONObject(res);
                                } catch (UnsupportedEncodingException e1) {
                                    // Couldn't properly decode data to string
                                    e1.printStackTrace();
                                } catch (JSONException e2) {
                                    // returned data is not JSONObject?
                                    e2.printStackTrace();
                                }
                            } else if (error instanceof NetworkError) {
                                message = "Network error.";
                                Log.e("Volley", "NetworkError");
                            } else if (error instanceof ParseError) {
                                message = "Server error.";
                                Log.e("Volley", "ParseError");
                            }

                            Toast.makeText(mContext, message, Toast.LENGTH_LONG).show();

                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                }
        ) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Content-Type", "application/json");
                if (token != null) {
                    params.put("access_token", token);

                }

                return params;
            }

        };

        request.setRetryPolicy(new DefaultRetryPolicy(10 * 1000, 1, 1.0f));
        getRequestQueue().add(request);
    }

    public void executeJsonArray(int requestMethod, String url, JSONObject jsonData, final Response.Listener<JSONArray> onExecuteResponse,
                                 final Response.ErrorListener executeErrorListener, final String token) {

        Log.d(TAG, "URL: " + url);
        Log.d(TAG, "Request: " + jsonData);

        // Form requestURL from BASE_URL and API strings
        String requestURL = url;

        JsonArrayRequest request = new JsonArrayRequest(requestURL, new Response.Listener<JSONArray>() {

                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            Log.d(TAG, "Response: " + response);
                            onExecuteResponse.onResponse(response);
                        } catch (Exception e) {
                            Log.d(TAG, "System Error: " + e.toString());
                            e.printStackTrace();
                        }
                    }
                },

                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        try {
                            executeErrorListener.onErrorResponse(error);
                            String message = "Server error.";


                            NetworkResponse networkResponse = error.networkResponse;
                            if (networkResponse != null) {

                                Log.e("Submit", "Error. HTTP Status Code:" + networkResponse.statusCode);
                            }

                            if (error instanceof TimeoutError) {
                                Log.e("Volley", "TimeoutError");
                                message = "Server Timeout.";

                            } else if (error instanceof NoConnectionError) {
                                Log.e("Volley", "NoConnectionError");
                                message = "No internet connection.";
                                Toast.makeText(mContext, "" + message, Toast.LENGTH_SHORT).show();
                            } else if (error instanceof AuthFailureError) {
                                Log.e("Volley", "AuthFailureError");
                                message = "Server error.";
                            } else if (error instanceof ServerError) {
                                try {
                                    message = "Server error.";
                                    String res = new String(networkResponse.data,
                                            HttpHeaderParser.parseCharset(networkResponse.headers));
                                    // Now you can use any deserializer to make sense of data
                                    JSONObject obj = new JSONObject(res);
                                } catch (UnsupportedEncodingException e1) {
                                    // Couldn't properly decode data to string
                                    e1.printStackTrace();
                                } catch (JSONException e2) {
                                    // returned data is not JSONObject?
                                    e2.printStackTrace();
                                }
                            } else if (error instanceof NetworkError) {
                                message = "Network error.";
                                Log.e("Volley", "NetworkError");
                            } else if (error instanceof ParseError) {
                                message = "Server ParseError.";
                                Log.e("Volley", "ParseError");
                            }

                            Toast.makeText(mContext, message, Toast.LENGTH_LONG).show();
                            System.out.println("API Calling Exception "+message);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                }
        ) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Content-Type", "application/json");
                if (token != null) {
                    params.put("token", token);

                }

                return params;
            }

        };

        request.setRetryPolicy(new DefaultRetryPolicy(120 * 1000, 1, 1.0f));
        getRequestQueue().add(request);
    }
    public static class restAPI {
        //private static String globalURL = "https://smdn0m0tdf.execute-api.us-west-2.amazonaws.com/prod/";
        //public static String globalURL = "https://smdn0m0tdf.execute-api.us-west-2.amazonaws.com/prod/";

        private static String globalURL = "https://jsonplaceholder.typicode.com/";

        public final static String USER_LIST = globalURL + "users";
        public final static String USER_POST = globalURL + "posts?userId=";


    }


}
